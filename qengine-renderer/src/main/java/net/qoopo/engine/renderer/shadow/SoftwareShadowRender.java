/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.renderer.shadow;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.ligth.QDirectionalLigth;
import net.qoopo.engine.core.entity.component.ligth.QLigth;
import net.qoopo.engine.core.entity.component.ligth.QPointLigth;
import net.qoopo.engine.core.entity.component.ligth.QSpotLigth;
import net.qoopo.engine.core.math.QMath;
import net.qoopo.engine.core.math.Matrix4;
import net.qoopo.engine.core.math.Vector2;
import net.qoopo.engine.core.math.Vector3;
import net.qoopo.engine.core.math.Vector4;
import net.qoopo.engine.core.scene.Camera;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.util.QGlobal;
import net.qoopo.engine.renderer.SoftwareRenderer;
import net.qoopo.engine.renderer.raster.ParallelRaster;
import net.qoopo.engine.renderer.shader.fragment.basico.parciales.OnlyColorFragmentShader;
import net.qoopo.engine.renderer.util.TransformationVectorUtil;

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
    protected Vector3 direccion;
    protected Vector3 posicion;
    protected Vector3 normalDireccion = Vector3.zero.clone();
    protected Vector3 centro;
    protected Vector3 vArriba = Vector3.unitario_y.clone();
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
        super(escena, "Sombra " + luz.getEntity().getName(), null, ancho, alto);
        this.tipo = tipo;
        this.luz = luz;
        this.opciones.setForzarResolucion(true);
    }

    public SoftwareShadowRender(int tipo, Scene escena, QDirectionalLigth luz, Camera camaraRender, int ancho,
            int alto) {
        this(tipo, escena, luz, ancho, alto);
        camera = new Camera("RenderSombraDireccional");
        camera.frustrumLejos = camaraRender.frustrumLejos;
        camera.setOrtogonal(true);
        camera.setEscalaOrtogonal(100);
        camera.setRadioAspecto(ancho, alto);
        camera.setToRender(false);
        this.camaraRender = camaraRender;
        setDireccion(luz.getDirection());
        raster = new ParallelRaster(this);
        shader = new OnlyColorFragmentShader(this);
    }

    public SoftwareShadowRender(int tipo, Scene escena, QSpotLigth luz, Camera camaraRender, int ancho, int alto) {
        this(tipo, escena, luz, ancho, alto);
        camera = new Camera("RenderSombraQLuzSpot");
        camera.frustrumLejos = Math.min(luz.radio, camaraRender.frustrumLejos);
        camera.setOrtogonal(false);
        camera.setFOV(luz.getAnguloExterno());
        camera.setRadioAspecto(ancho, alto);
        camera.setToRender(false);
        this.camaraRender = camaraRender;
        setDireccion(luz.getDirection());
        raster = new ParallelRaster(this);
        shader = new OnlyColorFragmentShader(this);
    }

    public SoftwareShadowRender(int tipo, Scene escena, QPointLigth luz, Camera camaraRender, int ancho, int alto,
            Vector3 direccion, Vector3 arriba) {
        this(tipo, escena, luz, ancho, alto);
        camera = new Camera("RenderSombraQLuzPuntual");
        camera.frustrumLejos = Math.min(luz.radio, camaraRender.frustrumLejos);
        camera.setOrtogonal(false);
        // como es un mapeo cubico el angulo sera de 90 grados, 360grados de vision para
        // 4
        camera.setFOV((float) Math.toRadians(90));
        camera.setRadioAspecto(ancho, alto);
        camera.setToRender(false);
        this.camaraRender = camaraRender;
        setDireccion(direccion);
        vArriba = arriba;
        raster = new ParallelRaster(this);
        shader = new OnlyColorFragmentShader(this);
    }

    @Override
    public void clean() {
        try {
            frameBuffer.cleanZBuffer();
            // frameBuffer.llenarColor(colorFondo);
        } catch (Exception e) {
        }
    }

    public Vector3 getDireccion() {
        return direccion;
    }

    public void setDireccion(Vector3 direccion) {
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
                centro = camaraRender.getMatrizTransformacion(QGlobal.time).toTranslationVector();
                if (!cascada) {
                    camera.setEscalaOrtogonal(Math.abs(camaraRender.frustrumLejos - camaraRender.frustrumCerca) / 2);
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
                    camera.setEscalaOrtogonal(Math.abs(distanciaCascada) / 2);
                    radio = distanciaCascada;
                    // }
                    // modifico el centro para que sea el centro de la sección del frustrum de
                    // visión de la caḿara
                    centro.add(camaraRender.getDirection().clone().normalize().multiply(-distanciaCascada));
                }
                // la posicion de la luz
                posicion = centro.clone().add(normalDireccion.multiply(radio));
                camera.lookAtTarget(posicion, centro, vArriba);
                break;
            case SoftwareShadowRender.NO_DIRECCIONALES:
                // LUCES NO DIRECCIONALES
                camera.lookAt(luz.getEntity().getMatrizTransformacion(QGlobal.time).toTranslationVector(),
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
                ventana = new JFrame(this.name);
                ventana.setSize((int) (frameBuffer.getWidth() * factorVentana),
                        (int) (frameBuffer.getHeight() * factorVentana));
                ventana.setPreferredSize(new Dimension((int) (frameBuffer.getWidth() * factorVentana),
                        (int) (frameBuffer.getHeight() * factorVentana)));
                ventana.setLayout(new BorderLayout());
                ventana.setResizable(false);
                panelDibujo = new JPanel();
                ventana.add(panelDibujo, BorderLayout.CENTER);
                ventana.setVisible(true);
                ventana.pack();
            }
            if (frameBuffer != null) {
                frameBuffer.paintZBuffer(camera.frustrumLejos);
                BufferedImage bi = frameBuffer.getRendered();
                panelDibujo.getGraphics().drawImage(bi, 0, 0, (int) (frameBuffer.getWidth() * factorVentana),
                        (int) (frameBuffer.getHeight() * factorVentana), null);
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
    // @Override
    private void render() {    
        poligonosDibujadosTemp = 0;
        try {
            // La Matriz de vista es la inversa de la matriz de la camara.
            // Esto es porque la camara siempre estara en el centro y movemos el mundo
            // en direccion contraria a la camara.
            Matrix4 matrizVista = camera.getMatrizTransformacion(QGlobal.time).invert();
            Matrix4 matrizVistaInvertidaBillboard = camera.getMatrizTransformacion(QGlobal.time);
            // caras solidas
            scene.getEntities().stream()
                    .filter(entity -> entity.isToRender())
                    .parallel()
                    .forEach(entity -> renderEntity(entity, matrizVista, matrizVistaInvertidaBillboard, false));
        } catch (Exception e) {
            System.out.println("Error render:" + name);
            e.printStackTrace();
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
            frameBuffer.computeMaxMinZbuffer();
            factorAcne = (frameBuffer.getMaximo() - frameBuffer.getMinimo()) * 0.1f;
            if (QGlobal.SOMBRAS_DEBUG_PINTAR) {
                pintarMapa();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        beforeTime = System.currentTimeMillis();
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
    public float factorSombra(Vector3 vector, Entity entity) {
        float factor = 0.0f;
        Vector2 punto = new Vector2();
        try {
            if (frameBuffer != null) {
                vector = TransformationVectorUtil.transformarVector(vector, entity, camera);
                camera.getCoordenadasPantalla(punto, new Vector4(vector, 1), getFrameBuffer().getWidth(),
                        getFrameBuffer().getHeight());
                // si el punto no esta en mi campo de vision
                if ((punto.x < 0)
                        || (punto.y < 0)
                        || (punto.x > getFrameBuffer().getWidth())
                        || (punto.y > getFrameBuffer().getHeight())) {
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
