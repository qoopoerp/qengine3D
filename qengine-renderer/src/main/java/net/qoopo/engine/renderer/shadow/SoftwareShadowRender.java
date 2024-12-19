/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.renderer.shadow;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.Collections;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.ligth.QDirectionalLigth;
import net.qoopo.engine.core.entity.component.ligth.QLigth;
import net.qoopo.engine.core.entity.component.ligth.QPointLigth;
import net.qoopo.engine.core.entity.component.ligth.QSpotLigth;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.QPoligono;
import net.qoopo.engine.core.entity.component.mesh.primitive.QPrimitiva;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QMath;
import net.qoopo.engine.core.math.QMatriz4;
import net.qoopo.engine.core.math.QVector2;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.math.QVector4;
import net.qoopo.engine.core.renderer.QOpcionesRenderer;
import net.qoopo.engine.core.scene.Camera;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.util.QGlobal;
import net.qoopo.engine.core.util.TempVars;
import net.qoopo.engine.renderer.SoftwareRenderer;
import net.qoopo.engine.renderer.raster.QRaster2;
import net.qoopo.engine.renderer.shader.pixelshader.basico.parciales.QSimpleShaderBAS;
import net.qoopo.engine.renderer.transformacion.QTransformar;

/**
 * EL renderizador de sombras es una implementacion de un mapa de sombras
 *
 * @author alberto
 */
public class SoftwareShadowRender extends SoftwareRenderer {

    // estos tipos los uso para saber que posicion usar para el calculo de la camara
    // si es direcccional uso la posicion de la camara del render
    public static final int DIRECIONALES = 1;
    // si es omnidireccional uso la posicion de la luz, pues es para luces puntuales
    public static final int NO_DIRECCIONALES = 2;
    private JFrame ventana;// para mostrar el mapa de sombras
    private JPanel panelDibujo;
    protected int tipo = DIRECIONALES;
    protected QLigth luz;
    protected Camera camaraRender;
    protected QVector3 direccion;
    protected QVector3 posicion;
    protected QVector3 normalDireccion = QVector3.zero.clone();
    protected QVector3 centro;
    protected QVector3 vArriba = QVector3.unitario_y.clone();
    protected boolean cascada = false;
    protected int cascada_tamanio = 1;
    protected int cascada_indice = 1;

    /**
     * Representa la distancia del centro de la seccion del frustrum cuando es
     * en tipo cascada
     */
    private float distanciaCascada = -1;

    // este factor se calcula en cada renderizado dependiendo del rango de
    // profundidades
    private float factorAcne = 0.0f;

    public SoftwareShadowRender(int tipo, Scene escena, QLigth luz, int ancho, int alto) {
        super(escena, "Sombra " + luz.entity.getName(), null, ancho, alto);
        this.tipo = tipo;
        this.luz = luz;
        this.opciones.setForzarResolucion(true);
    }

    public SoftwareShadowRender(int tipo, Scene escena, QDirectionalLigth luz, Camera camaraRender, int ancho, int alto) {
        this(tipo, escena, luz, ancho, alto);
        camara = new Camera("RenderSombraDireccional");
        camara.frustrumLejos = camaraRender.frustrumLejos;
        camara.setOrtogonal(true);
        camara.setEscalaOrtogonal(100);
        camara.setRadioAspecto(ancho, alto);
        camara.setToRender(false);
        this.camaraRender = camaraRender;
        setDireccion(luz.getDirection());
        raster = new QRaster2(this);
        shader = new QSimpleShaderBAS(this);
    }

    public SoftwareShadowRender(int tipo, Scene escena, QSpotLigth luz, Camera camaraRender, int ancho, int alto) {
        this(tipo, escena, luz, ancho, alto);
        camara = new Camera("RenderSombraQLuzSpot");
        camara.frustrumLejos = Math.min(luz.radio, camaraRender.frustrumLejos);
        camara.setOrtogonal(false);
        camara.setFOV(luz.getAnguloExterno());
        camara.setRadioAspecto(ancho, alto);
        camara.setToRender(false);
        this.camaraRender = camaraRender;
        setDireccion(luz.getDirection());
        raster = new QRaster2(this);
        shader = new QSimpleShaderBAS(this);
    }

    public SoftwareShadowRender(int tipo, Scene escena, QPointLigth luz, Camera camaraRender, int ancho, int alto,
            QVector3 direccion, QVector3 arriba) {
        this(tipo, escena, luz, ancho, alto);
        camara = new Camera("RenderSombraQLuzPuntual");
        camara.frustrumLejos = Math.min(luz.radio, camaraRender.frustrumLejos);
        camara.setOrtogonal(false);
        // como es un mapeo cubico el angulo sera de 90 grados, 360grados de vision para
        // 4
        camara.setFOV((float) Math.toRadians(90));
        camara.setRadioAspecto(ancho, alto);
        camara.setToRender(false);
        this.camaraRender = camaraRender;
        setDireccion(direccion);
        vArriba = arriba;
        raster = new QRaster2(this);
        shader = new QSimpleShaderBAS(this);
    }

    @Override
    public void clean() {
        try {
            frameBuffer.limpiarZBuffer();
            // frameBuffer.llenarColor(colorFondo);
        } catch (Exception e) {
        }
    }

    public QVector3 getDireccion() {
        return direccion;
    }

    public void setDireccion(QVector3 direccion) {
        this.direccion = direccion;
    }

    public QLigth getLuz() {
        return luz;
    }

    public void setLuz(QLigth luz) {
        this.luz = luz;
    }

    /**
     * Actualiza el campo de vision de la camara del render de sombras
     */
    protected void actualizarCampoVision() {
        // voy a trabajar con una esfera donde posicionare la camara, esta en el lugar
        // contrario a la direccion de la luz
        normalDireccion.set(direccion);
        // invierte la direcion para ir contrario a la luz
        normalDireccion.multiply(-1.0f);
        normalDireccion.normalize();
        float radio = 0.0f;

        switch (tipo) {
            case SoftwareShadowRender.DIRECIONALES:
                // la camara se puede mover al ser adjunta a una entity
                centro = camaraRender.getMatrizTransformacion(QGlobal.tiempo).toTranslationVector();
                if (!cascada) {
                    camara.setEscalaOrtogonal(Math.abs(camaraRender.frustrumLejos - camaraRender.frustrumCerca) / 2);
                    radio = Math.abs(camaraRender.frustrumLejos - camaraRender.frustrumCerca) / 2;
                    // modifico el centro para que sea el centro de frustrum de visión de la caḿara
                    // por lo tanto aumento un vector de dimensión igual a la distancia del
                    // frustrum/2 (centro)
                    // con dirección de la vista de la ćamara, es negativo (-d) porque la direccion
                    // apunta hacia atras de la camara (la camara apunta haca -z en lugar de +z)
                    centro.add(camaraRender.getDirection().clone().normalize()
                            .multiply(-Math.abs(camaraRender.frustrumLejos - camaraRender.frustrumCerca) / 2));
                } else {
                    // direccional cascada
                    // if (distanciaCascada == -1) {
                    // float uniforme = Math.abs(camaraRender.frustrumLejos -
                    // camaraRender.frustrumCerca) / cascada_tamanio;
                    // float logaritmica = (float) Math.log((float) cascada_tamanio / (float)
                    // cascada_indice);
                    // //esta distancia es la que agregaré a la posición de la cámara
                    //// distanciaCascada = cascada_indice * uniforme + (1 - logaritmica) *
                    // cascada_indice;
                    // distanciaCascada = cascada_indice * uniforme + (logaritmica) *
                    // cascada_indice;
                    // radio = (camaraRender.frustrumLejos - camaraRender.frustrumCerca) /
                    // cascada_tamanio;
                    // -------------------------------------
                    // //metodo jmonkey//
                    float IDM = cascada_indice / (float) cascada_tamanio;
                    float log = camaraRender.frustrumCerca
                            * QMath.pow((camaraRender.frustrumLejos / camaraRender.frustrumCerca), IDM);
                    float uniform = camaraRender.frustrumCerca
                            + (camaraRender.frustrumLejos - camaraRender.frustrumCerca) * IDM;
                    distanciaCascada = log * QGlobal.lambda + uniform * (1.0f - QGlobal.lambda);
                    distanciaCascada = distanciaCascada / 2;
                    camara.setEscalaOrtogonal(Math.abs(distanciaCascada) / 2);
                    radio = distanciaCascada;
                    // }
                    // modifico el centro para que sea el centro de la sección del frustrum de
                    // visión de la caḿara
                    centro.add(camaraRender.getDirection().clone().normalize().multiply(-distanciaCascada));
                }
                // la posicion de la luz
                posicion = centro.clone().add(normalDireccion.multiply(radio));
                camara.lookAtTarget(posicion, centro, vArriba);
                break;
            case SoftwareShadowRender.NO_DIRECCIONALES:
                // LUCES NO DIRECCIONALES
                camara.lookAt(luz.entity.getMatrizTransformacion(QGlobal.tiempo).toTranslationVector(),
                        normalDireccion, vArriba);
                break;
        }

    }

    public boolean isCascada() {
        return cascada;
    }

    public void setCascada(boolean cascada) {
        this.cascada = cascada;
    }

    public int getCascada_tamanio() {
        return cascada_tamanio;
    }

    public void setCascada_tamanio(int cascada_tamanio) {
        this.cascada_tamanio = cascada_tamanio;
    }

    public int getCascada_indice() {
        return cascada_indice;
    }

    public void setCascada_indice(int cascada_indice) {
        this.cascada_indice = cascada_indice;
    }

    public float getDistanciaCascada() {
        return distanciaCascada;
    }

    public void setDistanciaCascada(float distanciaCascada) {
        this.distanciaCascada = distanciaCascada;
    }

    protected void pintarMapa() {
        try {
            float factorVentana = 0.5f;
            if (ventana == null) {
                ventana = new JFrame(this.nombre);
                ventana.setSize((int) (frameBuffer.getAncho() * factorVentana),
                        (int) (frameBuffer.getAlto() * factorVentana));
                ventana.setPreferredSize(new Dimension((int) (frameBuffer.getAncho() * factorVentana),
                        (int) (frameBuffer.getAlto() * factorVentana)));
                ventana.setLayout(new BorderLayout());
                ventana.setResizable(false);
                panelDibujo = new JPanel();
                ventana.add(panelDibujo, BorderLayout.CENTER);
                ventana.setVisible(true);
                ventana.pack();
            }
            if (frameBuffer != null) {
                frameBuffer.pintarMapaProfundidad(camara.frustrumLejos);
                BufferedImage bi = frameBuffer.getRendered();
                panelDibujo.getGraphics().drawImage(bi, 0, 0, (int) (frameBuffer.getAncho() * factorVentana),
                        (int) (frameBuffer.getAlto() * factorVentana), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Realiza el proceso de renderizado
     *
     *
     */
    @Override
    public void render() {
        listaCarasTransparente.clear();
        poligonosDibujadosTemp = 0;
        TempVars t = TempVars.get();
        try {
            // La Matriz de vista es la inversa de la matriz de la camara.
            // Esto es porque la camara siempre estara en el centro y movemos el mundo
            // en direccion contraria a la camara.
            QMatriz4 matrizVista = camara.getMatrizTransformacion(QGlobal.tiempo).invert();
            for (Entity entity : escena.getEntities()) {
                QTransformar.transformar(entity, matrizVista, t.bufferVertices1);

                // --------------------------------------------------------------------------------------
                // CARAS SOLIDAS NO TRANSPARENTES
                // --------------------------------------------------------------------------------------
                for (QPrimitiva poligono : t.bufferVertices1.getPoligonosTransformados()) {
                    // salta las camaras
                    if (poligono.geometria.entity instanceof Camera) {
                        continue;
                    }

                    if (poligono.material == null
                            // q no tengra transparencia cuando tiene el tipo de material basico
                            || (poligono.material instanceof QMaterialBas
                                    && !((QMaterialBas) poligono.material).isTransparencia())) {
                        tomar = false;
                        // comprueba que todos los vertices estan en el campo de vision
                        for (int i : poligono.listaVertices) {
                            if (camara.isVisible(t.bufferVertices1.getVertice(i))) {
                                tomar = true;
                                break;
                            }
                        }
                        if (!tomar) {
                            continue;
                        }
                        poligonosDibujadosTemp++;
                        raster.raster(t.bufferVertices1, poligono,
                                opciones.getTipoVista() == QOpcionesRenderer.VISTA_WIRE
                                        || poligono.geometria.tipo == Mesh.GEOMETRY_TYPE_WIRE);
                    } else {
                        if (poligono instanceof QPoligono) {
                            listaCarasTransparente.add((QPoligono) poligono);
                        }
                    }
                }

                // --------------------------------------------------------------------------------------
                // TRANSPARENTES
                // --------------------------------------------------------------------------------------
                // ordeno las caras transparentes
                if (!listaCarasTransparente.isEmpty()) {
                    if (opciones.iszSort()) {
                        Collections.sort(listaCarasTransparente);
                    }
                    for (QPoligono poligono : listaCarasTransparente) {
                        poligonosDibujadosTemp++;
                        raster.raster(t.bufferVertices1, poligono,
                                opciones.getTipoVista() == QOpcionesRenderer.VISTA_WIRE
                                        || poligono.geometria.tipo == Mesh.GEOMETRY_TYPE_WIRE);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            t.release();
        }

    }

    @Override
    public long update() {
        long tInicio = System.nanoTime();
        try {
            if (frameBuffer != null && direccion != null) {
                clean();
                actualizarCampoVision();
                render();
            }
            poligonosDibujados = poligonosDibujadosTemp;
            // como no tengo normalizadas las coordenadas z necesito estos valores para que
            // el factor acne seal el 10% de esta diferencia
            frameBuffer.calcularMaximosMinimosZBuffer();
            factorAcne = (frameBuffer.getMaximo() - frameBuffer.getMinimo()) * 0.1f;
            if (QGlobal.SOMBRAS_DEBUG_PINTAR) {
                pintarMapa();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        tiempoPrevio = System.currentTimeMillis();
        return System.nanoTime() - tInicio;
    }

    /**
     * Calcula cuanta sombra recibe un punto. 1. Nada de sombra, 0 Totalmente en
     * sombra.
     *
     * @param vector
     * @param entity
     * @return
     */
    public float factorSombra(QVector3 vector, Entity entity) {
        float factor = 0.0f;
        QVector2 punto = new QVector2();
        try {
            if (frameBuffer != null) {
                vector = QTransformar.transformarVector(vector, entity, camara);
                camara.getCoordenadasPantalla(punto, new QVector4(vector, 1), getFrameBuffer().getAncho(),
                        getFrameBuffer().getAlto());
                // si el punto no esta en mi campo de vision
                if ((punto.x < 0)
                        || (punto.y < 0)
                        || (punto.x > getFrameBuffer().getAncho())
                        || (punto.y > getFrameBuffer().getAlto())) {
                    factor = 0;
                } else {
                    // metodo donde sale la sombra pixelada
                    if (!QGlobal.SOMBRAS_SUAVES) {
                        factor = (-vector.z - factorAcne) > frameBuffer.getZBuffer((int) punto.x, (int) punto.y) ? 1
                                : 0;
                    } else {
                        // metodo con la sombra suave
                        for (int row = -1; row <= 1; ++row) {
                            for (int col = -1; col <= 1; ++col) {
                                try {
                                    factor += (-vector.z - factorAcne) > frameBuffer.getZBuffer((int) punto.x + col,
                                            (int) punto.y + row) ? 1 : 0;
                                } catch (Exception e) {
                                }
                            }
                        }
                        factor /= 9.0f;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1.0f - factor;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
