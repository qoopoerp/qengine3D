package net.qoopo.engine.core.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.engine.Engine;
import net.qoopo.engine.core.engine.EngineTime;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.ligth.QLigth;
import net.qoopo.engine.core.entity.component.mesh.primitive.Poly;
import net.qoopo.engine.core.entity.component.mesh.primitive.Primitive;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.entity.component.transform.QVertexBuffer;
import net.qoopo.engine.core.input.QDefaultListener;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QVector2;
import net.qoopo.engine.core.renderer.buffer.QFrameBuffer;
import net.qoopo.engine.core.renderer.post.flujos.QRenderEfectos;
import net.qoopo.engine.core.renderer.superficie.Superficie;
import net.qoopo.engine.core.scene.Camera;
import net.qoopo.engine.core.scene.QClipPane;
import net.qoopo.engine.core.scene.QOrigen;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.QTextura;
import net.qoopo.engine.core.util.image.ImgReader;

@Getter
@Setter
public abstract class RenderEngine extends Engine {

    protected static BufferedImage imageSplash;
    // --------------------------------------------------------------------------------------------------------------------------------
    // CONSTANTES
    // --------------------------------------------------------------------------------------------------------------------------------
    public static final int RENDER_INTERNO = 1;
    public static final int RENDER_JAVA3D = 2;
    public static final int RENDER_OPENGL = 3;

    private static final Color COLOR_FONDO_ESTADISTICAS = new Color(0, 0, 0.6f, 0.4f);
    protected static int lightOnScreenSize = 20;

    static {
        try {
            // imageSplash = ImgReader.leerImagen(new File("res/imagenes/loading.png"));
            // imageSplash = ImgReader.leerImagen(new File("res/imagenes/wolf/wolf_6.png"));
            // imageSplash = ImgReader.leerImagen(new File("res/imagenes/wolf/wolf_4.png"));
            // imageSplash = ImgReader.leerImagen(new File("res/imagenes/wolf/wolf_5.png"));
            // imageSplash = ImgReader.leerImagen(new File("res/imagenes/wolf/wolf_8.png"));
            // imageSplash = ImgReader.leerImagen(new File("res/imagenes/wolf/wolf_9.png"));
            // BufferedImage bi = ImgReader.leerImagen(new
            // File("res/imagenes/wolf/wolf_2.png"));
            // int maxancho = 400;
            // imageSplash = new BufferedImage(maxancho, (maxancho * bi.getHeight()) /
            // bi.getWidth(), bi.getType());
            // imageSplash.getGraphics().drawImage(bi, imageSplash.getWidth(),
            // imageSplash.getHeight(), null);
            imageSplash = ImgReader.read(RenderEngine.class.getResourceAsStream("/imagenes/wolf/wolf_9.png"));
        } catch (Exception ex) {
            ex.printStackTrace();
            imageSplash = null;
        }
    }

    protected String nombre = "Default";
    /**
     * Indica al renderer que se esta cargando, para lo cual muestra una imagen
     * en cada render en lugar de procesar el escena
     */
    private boolean cargando = false;
    protected QColor colorFondo = QColor.BLACK;

    /**
     * Esta variable indica si se puede interactuar con el renderer Hay casos
     * como los que permiten la visa previa de los materiales y a las entidades
     * que no se debe interactuar con el mouse
     */
    protected boolean interactuar = true;

    // --------------------------------------------------------------------------------------------------------------------------------
    // VARIABLES
    // --------------------------------------------------------------------------------------------------------------------------------
    /**
     * La escena que se está renderizando
     */
    protected Scene scene;
    /**
     * La cámara actual para la toma de la escena
     */
    protected Camera camara;
    /**
     * Las opciones con las que se renderiza
     */
    public QOpcionesRenderer opciones = new QOpcionesRenderer();
    /**
     * Contiene los vértices y polígonos resultado de la transformación
     */

    /**
     * Plano que permite hacer culling
     */
    protected QClipPane panelClip = null;

    /**
     * Superficie donde se va a dibujar
     */
    protected Superficie superficie;// superficie donde se va a dibujar al finalizar el proceso de renderizado
    /**
     * Buffer donde se dibuja, antes de efectos
     */
    protected QFrameBuffer frameBuffer;
    /**
     * Buffer resultado de los efecto post procesamiento
     */
    // protected QFrameBuffer frameBufferFinal;
    /**
     * La textura donde se va a renderizar la salida del frameBuffer
     */
    protected QTextura textura;

    /**
     * Esta variable indica si el render es real. En caso de no serlo se salta
     * unas fases no necesarias para los virtuales
     */
    public boolean renderReal = true;

    /**
     * Bandera que indica si se muestran las estadísticas de renderizado
     */
    protected boolean showStats = false;

    /**
     * Estadísticas. Los polígonos dibujados
     */
    public int poligonosDibujados = 0;
    public int poligonosDibujadosTemp = 0;

    protected List<QLigth> litgths = new ArrayList<>();

    protected ArrayList<Poly> listaCarasTransparente = new ArrayList<>();
    protected boolean tomar;

    protected QRenderEfectos efectosPostProceso;

    protected boolean forzarActualizacionMapaSombras = false;

    // protected Accion accionSeleccionar = null;//la accion que debe ejecutar
    // cuando selecciona un objeto

    protected QOrigen entidadOrigen;

    public RenderEngine(Scene escena, Superficie superficie, int ancho, int alto) {
        this(escena, "Nuevo Renderer", superficie, ancho, alto);
    }

    public RenderEngine(Scene escena, String nombre, Superficie superficie, int ancho, int alto) {
        System.out.println("instanciando nuevo rendered -> " + nombre);

        this.scene = escena;
        this.nombre = nombre;
        this.superficie = superficie;
        this.opciones.setAncho(ancho);
        this.opciones.setAlto(alto);

        if (superficie != null) {
            this.superficie.getComponente().addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    resize();
                }
            });
        }
        prepararInputListener();

        entidadOrigen = new QOrigen();
    }

    /**
     * Si la ventana cambia, actualiza el framebuffer (en caso de un usar
     * resolucion forzada)
     */
    public void resize() {
        int ancho = 0;
        int alto = 0;

        if (opciones.isForzarResolucion()) {
            ancho = opciones.getAncho();
            alto = opciones.getAlto();
        } else {
            if (this.getSuperficie() != null && this.getSuperficie().getComponente() != null) {
                ancho = this.getSuperficie().getComponente().getWidth();
                alto = this.getSuperficie().getComponente().getHeight();
            } else {
                System.out.println("Render:" + nombre + " no hay superficie. Seteando resolucion default 800x600");
                ancho = 800;
                alto = 600;
            }
        }

        // Resolucion default
        if (ancho <= 0) {
            ancho = 800;
        }
        if (alto <= 0) {
            alto = 600;
        }

        if (camara != null) {
            camara.setRadioAspecto(ancho, alto);
        }

        frameBuffer = new QFrameBuffer(ancho, alto, textura);
    }

    /**
     * Permite la selección de un objeto con el mouse
     *
     * @param mouseLocation
     * @return
     */
    public abstract Entity selectEntity(QVector2 mouseLocation);

    public Superficie getSuperficie() {
        return superficie;
    }

    public void setSuperficie(Superficie superficie) {
        this.superficie = superficie;
    }

    public float getCameraRotationZ() {
        return camara.getTransformacion().getRotacion().getAngulos().getAnguloZ();
    }

    public void setCameraRotationZ(float rotateZ) {
        this.camara.getTransformacion().getRotacion().rotarZ(rotateZ);
    }

    public Camera getCamara() {
        return camara;
    }

    public void setCamara(Camera camara) {
        this.camara = camara;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene escena) {
        this.scene = escena;
    }

    protected void prepararInputListener() {
        if (superficie != null && superficie.getComponente() != null) {
            agregarListeners(superficie.getComponente());
            // } else {
            // System.out.println("[x] No se agrego el listener porque no hay una superficie
            // -> " + nombre);
            // Utils.printTrace();
        }
    }

    protected void agregarListeners(Component componente) {
        System.out.println("[+] Agregando listener a " + nombre);
        componente.addMouseMotionListener(QDefaultListener.INSTANCIA);
        componente.addMouseWheelListener(QDefaultListener.INSTANCIA);
        componente.addMouseListener(QDefaultListener.INSTANCIA);
        componente.addKeyListener(QDefaultListener.INSTANCIA);
        componente.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                rendererFocusLost(evt);
            }
        });
    }

    protected void rendererFocusLost(java.awt.event.FocusEvent evt) {

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isShowStats() {
        return showStats;
    }

    public void setShowStats(boolean renderEstadisticas) {
        this.showStats = renderEstadisticas;
    }

    public QFrameBuffer getFrameBuffer() {
        return frameBuffer;
    }

    public void setFrameBuffer(QFrameBuffer frameBuffer) {
        this.frameBuffer = frameBuffer;
    }

    /**
     * Dibuja las estadísticas
     *
     * @param g
     */
    protected void showStats(Graphics g) {
        if (showStats && renderReal) {
            int ancho = 0;
            int alto = 0;

            if (opciones.isForzarResolucion()) {
                ancho = opciones.getAncho();
                alto = opciones.getAlto();
            } else {
                ancho = this.getSuperficie().getComponente().getWidth();
                alto = this.getSuperficie().getComponente().getHeight();
            }
            g.setColor(COLOR_FONDO_ESTADISTICAS);
            g.fillRect(0, 0, 170, 130);
            g.setColor(Color.orange);
            g.setFont(new Font("Arial", Font.BOLD, 10));
            g.drawString(nombre != null ? nombre : "", 10, 10);
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.PLAIN, 10));
            g.drawString(ancho + "x" + alto, 10, 20);
            g.drawString("FPS_______________: " + EngineTime.FPS, 10, 30);
            g.drawString("Delta (ms)________: " + EngineTime.deltaNano / 1000000, 10, 40);
            g.drawString("FPS Raster________: " + DF.format(getFPS()), 10, 50);
            g.drawString("Delta Raster (ms)_: " + DF.format(getDelta()), 10, 60);
            g.drawString("Pol_______________: " + poligonosDibujados, 10, 70);
            g.drawString("T. Vista     : " + (opciones.getTipoVista() == QOpcionesRenderer.VISTA_FLAT ? "FLAT"
                    : (opciones.getTipoVista() == QOpcionesRenderer.VISTA_PHONG ? "PHONG"
                            : (opciones.getTipoVista() == QOpcionesRenderer.VISTA_WIRE ? "WIRE" : "N/A"))),
                    10, 80);
            g.drawString("Material     : " + (opciones.isMaterial() ? "ON" : "OFF"), 10, 90);
            g.drawString("Sombras      : " + (opciones.isSombras() ? "ON" : "OFF"), 10, 100);
            // g.drawString("Hora del día :" + QEngine3D.INSTANCIA.getHoraDelDia(), 10,
            // 110);
            g.drawString("Cam (X;Y;Z)  : (" + DF.format(camara.getTransformacion().getTraslacion().x) + ";"
                    + DF.format(camara.getTransformacion().getTraslacion().y) + ";"
                    + DF.format(camara.getTransformacion().getTraslacion().z) + ")", 10, 120);
            g.drawString("Ang (X;Y;Z)  : ("
                    + DF.format(Math.toDegrees(camara.getTransformacion().getRotacion().getAngulos().getAnguloX()))
                    + ";"
                    + DF.format(Math.toDegrees(camara.getTransformacion().getRotacion().getAngulos().getAnguloY()))
                    + ";"
                    + DF.format(Math.toDegrees(camara.getTransformacion().getRotacion().getAngulos().getAnguloZ()))
                    + ")", 10, 130);
        }
        // gf.drawImage(g., 0, 0, this.getSuperficie().getComponente().getWidth(),
        // this.getSuperficie().getComponente().getHeight(),
        // this.getSuperficie().getComponente());

    }

    /**
     * Muestra una imagen splash
     */
    protected void mostrarSplash() {
        if (renderReal && imageSplash != null) // dibujamos una imagen de carga
        {
            if (this.getSuperficie() != null
                    && this.getSuperficie().getComponente() != null
                    && this.getSuperficie().getComponente().getGraphics() != null) {
                this.getSuperficie().getComponente().getGraphics().setColor(Color.BLACK);
                this.getSuperficie().getComponente().getGraphics().fillRect(0, 0,
                        this.getSuperficie().getComponente().getWidth(),
                        this.getSuperficie().getComponente().getHeight());
                this.getSuperficie().getComponente().getGraphics().drawImage(imageSplash,
                        (this.getSuperficie().getComponente().getWidth() - imageSplash.getWidth()) / 2,
                        (this.getSuperficie().getComponente().getHeight() - imageSplash.getHeight()) / 2,
                        imageSplash.getWidth(),
                        imageSplash.getHeight(),
                        this.getSuperficie().getComponente());

            }
        }
    }

    public QTextura getTextura() {
        return textura;
    }

    public void setTextura(QTextura textura) {
        this.textura = textura;
        if (frameBuffer != null) {
            frameBuffer.setTextura(textura);
        }
    }

    public QClipPane getPanelClip() {
        return panelClip;
    }

    public void setPanelClip(QClipPane panelClip) {
        this.panelClip = panelClip;
    }

    public List<QLigth> getLitgths() {
        return litgths;
    }

    public void setLitgths(List<QLigth> luces) {
        this.litgths = luces;
    }

    // public Map<String, QProcesadorSombra> getProcesadorSombras() {
    // return procesadorSombras;
    // }
    public boolean isCargando() {
        return cargando;
    }

    public void setCargando(boolean cargando) {
        this.cargando = cargando;
    }

    public QRenderEfectos getEfectosPostProceso() {
        return efectosPostProceso;
    }

    public void setEfectosPostProceso(QRenderEfectos efectosPostProceso) {
        this.efectosPostProceso = efectosPostProceso;
    }

    public boolean isForzarActualizacionMapaSombras() {
        return forzarActualizacionMapaSombras;
    }

    public void setForzarActualizacionMapaSombras(boolean forzarActualizacionMapaSombras) {
        this.forzarActualizacionMapaSombras = forzarActualizacionMapaSombras;
    }

    public QColor getColorFondo() {
        return colorFondo;
    }

    public void setColorFondo(QColor colorFondo) {
        this.colorFondo = colorFondo;
    }

    public boolean isInteractuar() {
        return interactuar;
    }

    public void setInteractuar(boolean interactuar) {
        this.interactuar = interactuar;
    }

    public boolean isRenderReal() {
        return renderReal;
    }

    public void setRenderReal(boolean renderReal) {
        this.renderReal = renderReal;
    }

    /**
     * Cambia el raster
     *
     * @param opcion
     */
    public void cambiarRaster(int opcion) {

    }

    /**
     * Cambia el shader
     *
     * @param opcion
     */
    public void cambiarShader(int opcion) {

    }

    // public abstract AbstractRaster(QMotorRender render);
    /**
     *
     * @param primitiva
     * @param p1
     * @param p2
     */
    public abstract void renderLine(Primitive primitiva, Vertex... vertex);

    /**
     * Realiza la rasterización de un polígono
     *
     * @param bufferVertices
     * @param primitiva
     * @param wire
     */
    public abstract void render(QVertexBuffer bufferVertices, Primitive primitiva, boolean wire);

    public abstract void render() throws Exception;

    public abstract void shadeFragments();

    public abstract void postRender();

    public void endUpdate() {

    }

}
