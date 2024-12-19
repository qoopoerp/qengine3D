package net.qoopo.engine3d.editor;

import static net.qoopo.engine.core.renderer.RenderEngine.GIZMO_ESCALA;
import static net.qoopo.engine.core.renderer.RenderEngine.GIZMO_NINGUNO;
import static net.qoopo.engine.core.renderer.RenderEngine.GIZMO_ROTACION;
import static net.qoopo.engine.core.renderer.RenderEngine.GIZMO_TRASLACION;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.assets.model.ModelLoader;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.entity.component.camera.QCamaraControl;
import net.qoopo.engine.core.entity.component.camera.QCamaraOrbitar;
import net.qoopo.engine.core.entity.component.gui.QMouseReceptor;
import net.qoopo.engine.core.entity.component.gui.QTecladoReceptor;
import net.qoopo.engine.core.entity.component.ligth.QDirectionalLigth;
import net.qoopo.engine.core.entity.component.ligth.QPointLigth;
import net.qoopo.engine.core.entity.component.ligth.QSpotLigth;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.IcoSphere;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QCaja;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QCilindro;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QCilindroX;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QCilindroZ;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QCono;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QCuboEsfera;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QEsfera;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QGeoesfera;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.PlanarMesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QPlano;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QPrisma;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QSuzane;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QTeapot;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QToro;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QTriangulo;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.wire.QEspiral;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.mallas.QColisionMallaConvexa;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.QColisionCaja;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.QColisionCilindro;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.QColisionCilindroX;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.QColisionCono;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.QColisionEsfera;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.QColisionTriangulo;
import net.qoopo.engine.core.entity.component.terrain.HeightMapTerrain;
import net.qoopo.engine.core.input.QInputManager;
import net.qoopo.engine.core.lwjgl.renderer.OpenGlRenderer;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QVector2;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.renderer.QOpcionesRenderer;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.renderer.post.flujos.antialiasing.QAntialiasing;
import net.qoopo.engine.core.renderer.post.flujos.basicos.QEfectoBlur;
import net.qoopo.engine.core.renderer.post.flujos.basicos.QEfectoCel;
import net.qoopo.engine.core.renderer.post.flujos.basicos.QEfectoContraste;
import net.qoopo.engine.core.renderer.post.flujos.basicos.QEfectoDepthOfField;
import net.qoopo.engine.core.renderer.post.flujos.complejos.QEfectoBloom;
import net.qoopo.engine.core.renderer.post.procesos.blur.QProcesadorDepthOfField;
import net.qoopo.engine.core.renderer.superficie.QJPanel;
import net.qoopo.engine.core.renderer.superficie.Superficie;
import net.qoopo.engine.core.scene.Camera;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.util.QMaterialUtil;
import net.qoopo.engine.core.util.QGlobal;
import net.qoopo.engine.core.util.QUtilComponentes;
import net.qoopo.engine.core.util.mesh.QUtilNormales;
import net.qoopo.engine.java3d.renderer.QRenderJava3D;
import net.qoopo.engine.renderer.SoftwareRenderer;
import net.qoopo.engine3d.QEngine3D;
import net.qoopo.engine3d.core.asset.model.DefaultModelLoader;
import net.qoopo.engine3d.core.input.control.gizmo.QGizmo;
import net.qoopo.engine3d.core.util.Accion;
import net.qoopo.engine3d.core.util.QDefinirCentro;
import net.qoopo.engine3d.core.util.SerializarUtil;
import net.qoopo.engine3d.editor.assets.PnlGestorRecursos;
import net.qoopo.engine3d.editor.entity.EditorEntidad;
import net.qoopo.engine3d.editor.util.ImagePreviewPanel;
import net.qoopo.engine3d.editor.util.QArbolWrapper;
import net.qoopo.engine3d.editor.util.Util;
import net.qoopo.engine3d.test.generaEjemplos.InitialScene;

public class Principal extends javax.swing.JFrame {

    public static Principal instancia;
    // el motor que va a renderizar en el modo de dise;o
    private QEngine3D motor;

    private List<RenderEngine> listaRenderer = new ArrayList<>();
    private RenderEngine renderer; // renderer seleccionado
    boolean objectLock = true;
    boolean objectListLock = false;
    private LinkedList<Entity> clipboard = new LinkedList<>();
    private JFileChooser chooser = new JFileChooser();
    private ImagePreviewPanel preview = new ImagePreviewPanel();
    private EditorEntidad pnlEditorEntidad = new EditorEntidad();
    private PnlGestorRecursos pnlGestorRecursos = new PnlGestorRecursos();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
    private Accion accionSeleccionar;
    private Accion accionActualizarLineaTiempo;
    private boolean cambiandoLineaTiempo = false;
    protected static final DecimalFormat df = new DecimalFormat("0.00");

    private Scene escena;

    public Principal() {
        // configura las acciones para interactuar desde el renderar hacia afuera
        accionSeleccionar = new Accion() {
            @Override
            public void ejecutar(Object... parametros) {
                try {
                    seleccionarEntidad((Entity) parametros[0]);
                } catch (Exception e) {

                }
            }
        };
        accionActualizarLineaTiempo = new Accion() {
            @Override
            public void ejecutar(Object... parametros) {
                try {
                    if (motor.getAnimationEngine() != null && motor.getAnimationEngine().isEjecutando()) {
                        cambiandoLineaTiempo = true;
                        sldLineaTiempo.setValue((int) (motor.getAnimationEngine().getTiempo() * 10));
                        cambiandoLineaTiempo = false;
                    }
                } catch (Exception e) {

                }
            }
        };

        instancia = this;
        initComponents();
        chooser.setCurrentDirectory(new File("assets/"));
        motor = new QEngine3D();
        this.escena = motor.getEscena();
        motor.getAccionesEjecucion().add(accionActualizarLineaTiempo);
        motor.getEscena().setAmbientColor(new QColor(50.0f / 255.0f, 50.0f / 255.0f, 50.0f / 255.0f));
        pnlColorFondo.setBackground(motor.getEscena().getAmbientColor().getColor());

        motor.setIniciarAudio(false);
        motor.setIniciarDiaNoche(false);
        motor.setIniciarFisica(false);
        motor.setIniciarInteligencia(false);
        motor.setIniciarAnimaciones(false);
        agregarRenderer("Main", QVector3.of(0, 10, 10), QVector3.of(0, 0, 0), RenderEngine.RENDER_INTERNO);
        renderer.opciones.setDibujarLuces(true);
        motor.setRenderEngine(renderer);
        // renderer.setPanelClip(new QClipPane(QVector3.unitario_y.clone(), 0));//la
        // normal es hacia arriba
        // renderer.setPanelClip(new QClipPane(QVector3.of(0, -1, 0), 0));//la normal es
        // hacia abajo
        // renderer.setPanelClip(new QClipPane(QVector3.unitario_y.clone(), 2f));//la
        // normal es hacia arriba
        // renderer.setPanelClip(new QClipPane(QVector3.of(0, -1, 0), 2));//la normal es
        // hacia abajo
        motor.start();
        scrollOpciones.getVerticalScrollBar().setUnitIncrement(20);
        this.setExtendedState(MAXIMIZED_BOTH);
        actualizarArbolEscena();
        chooser.setAccessory(preview);
        chooser.addPropertyChangeListener(preview);
        panelHerramientas.addTab("Entidad", pnlEditorEntidad);
        panelHerramientas.addTab("Recursos", pnlGestorRecursos);
        spnNeblinaDensidad.setModel(new SpinnerNumberModel(0.015f, 0, 1, .05));
        treeEntidades.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeEntidades.getLastSelectedPathComponent();
                if (node == null) {
                    return;
                }

                QArbolWrapper nodo = (QArbolWrapper) node.getUserObject();
                if (!objectListLock) {

                    if (!QInputManager.isShitf()) {
                        renderer.entidadesSeleccionadas.clear();
                    }
                    if (nodo.getObjeto() instanceof Entity) {
                        for (RenderEngine rend : listaRenderer) {
                            rend.entidadActiva = (Entity) nodo.getObjeto();
                            renderer.entidadesSeleccionadas.add(rend.entidadActiva);
                        }
                    }

                    pnlEditorEntidad.liberar();
                    populateControls();
                    jPanel2.repaint();
                }
            }
        });

        treeEntidades.setDragEnabled(true);
        treeEntidades.setDropMode(DropMode.ON);
        treeEntidades.setCellRenderer(new ArbolEntidadRenderer());
        loadInitialScene();
    }

    public void loadInitialScene() {
        InitialScene initialScene = new InitialScene();
        initialScene.make(motor.getEscena());
        actualizarArbolEscena();
    }

    public void agregarRenderer(String nombre, int tipoRenderer) {
        agregarRenderer(nombre, new Camera(nombre), tipoRenderer);
    }

    public void agregarRenderer(String nombre, QVector3 posicionCam, QVector3 posicionObjetivo, int tipoRenderer) {
        Camera nuevaCamara = new Camera("Cam. " + nombre);
        nuevaCamara.lookAtTarget(posicionCam, posicionObjetivo, QVector3.unitario_y.clone());
        agregarRenderer(nombre, nuevaCamara, tipoRenderer);
    }

    /**
     * Agrega un renderizador a la ventana
     *
     * @param nombre
     * @param camara
     * @param tipoRenderer
     */
    public void agregarRenderer(String nombre, Camera camara, int tipoRenderer) {
        // agregamos un nuevo panel para el renderer principal
        motor.setModificando(true);

        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }

        QJPanel panelDibujo = new QJPanel();

        int tam = listaRenderer.size();
        switch (tam) {
            case 0:
                panelRenderes.setLayout(new GridLayout(1, 1));
                break;
            case 1:
                panelRenderes.setLayout(new GridLayout(1, 2, 2, 2));
                break;
            default:
                panelRenderes.setLayout(new GridLayout(2, 2, 2, 2));
                break;
        }

        for (RenderEngine render : listaRenderer) {
            panelRenderes.add(render.getSuperficie().getComponente());
        }

        panelRenderes.add(panelDibujo);

        RenderEngine nuevoRenderer;
        switch (tipoRenderer) {
            case RenderEngine.RENDER_JAVA3D:
                nuevoRenderer = new QRenderJava3D(motor.getEscena(), nombre, new Superficie(panelDibujo), 800, 600);
                break;
            case RenderEngine.RENDER_OPENGL:
                nuevoRenderer = new OpenGlRenderer(motor.getEscena(), nombre, new Superficie(panelDibujo), 800, 600);
                break;
            case RenderEngine.RENDER_INTERNO:
            default:
                nuevoRenderer = new SoftwareRenderer(motor.getEscena(), nombre, new Superficie(panelDibujo), 800, 600);
                break;
        }
        nuevoRenderer.opciones.setRenderArtefactos(true);
        panelDibujo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                setRenderer(nuevoRenderer);
                actualizarBordeSeleccionado();
                // nuevoRenderer.getSuperficie().getComponente().setBorder(new
                // LineBorder(Color.RED, 10));
            }
            // public void mouseReleased(java.awt.event.MouseEvent evt) {
            //// rendererMouseReleased(evt);
            // }
        });

        motor.getEscena().addEntity(camara);
        nuevoRenderer.setCamara(camara);// setea la camara inicial creada
        // nuevoRenderer.setAccionSeleccionar(accionSeleccionar);

        for (RenderEngine render : listaRenderer) {
            render.resize();
        }

        listaRenderer.add(nuevoRenderer);
        motor.setRendererList(listaRenderer);
        setRenderer(nuevoRenderer);
        camara.addComponent(new QCamaraControl(camara));
        // camara.agregarComponente(new QCamaraOrbitar(camara));
        // camara.agregarComponente(new QCamaraPrimeraPersona(camara));
        prepararInputListenerRenderer(nuevoRenderer);
        motor.setModificando(false);
        this.pack();
    }

    public void actualizarBordeSeleccionado() {
        // for (QMotorRender render : listaRenderer) {
        // if (render.equals(renderer)) {
        // render.getSuperficie().getComponente().setBorder(new LineBorder(Color.RED,
        // 5));
        // } else {
        // render.getSuperficie().getComponente().setBorder(null);
        // }
        // }
    }

    public RenderEngine getRenderer() {
        return renderer;
    }

    public void setRenderer(RenderEngine renderer) {
        this.renderer = renderer;
    }

    /**
     * Se utiliza para seleccionar un objeto recien agregado a la escena
     *
     * @param entidad
     */
    private void seleccionarEntidad(Entity entidad) {
        for (RenderEngine rend : listaRenderer) {
            if (!QInputManager.isShitf()) {
                rend.entidadesSeleccionadas.clear();
            }
            rend.entidadActiva = entidad;
            rend.entidadesSeleccionadas.add(entidad);
        }
        pnlEditorEntidad.liberar();
        populateControls();
        jPanel2.repaint();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        groupOptVista = new javax.swing.ButtonGroup();
        groupTipoSuperficie = new javax.swing.ButtonGroup();
        barraProgreso = new javax.swing.JProgressBar();
        lblEstad = new javax.swing.JLabel();
        splitPanel = new javax.swing.JSplitPane();
        splitIzquierda = new javax.swing.JSplitPane();
        pnlEscenario1 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        treeEntidades = new javax.swing.JTree();
        panelHerramientas = new javax.swing.JTabbedPane();
        scrollOpciones = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        cbxShowLight = new javax.swing.JCheckBox();
        cbxNormalMapping = new javax.swing.JCheckBox();
        cbxShowBackFaces = new javax.swing.JCheckBox();
        cbxForceRes = new javax.swing.JCheckBox();
        spnWidth = new javax.swing.JSpinner();
        spnHeight = new javax.swing.JSpinner();
        jLabel7 = new javax.swing.JLabel();
        cbxForceSmooth = new javax.swing.JCheckBox();
        cbxZSort = new javax.swing.JCheckBox();
        cbxInterpolar = new javax.swing.JCheckBox();
        chkVerGrid = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        btnRaster1 = new javax.swing.JButton();
        btnRaster2 = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        pnlColorFondo = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        chkNeblina = new javax.swing.JCheckBox();
        jLabel9 = new javax.swing.JLabel();
        spnNeblinaDensidad = new javax.swing.JSpinner();
        jLabel10 = new javax.swing.JLabel();
        pnlNeblinaColor = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        btnFullShader = new javax.swing.JButton();
        btnShadowShader = new javax.swing.JButton();
        btnIlumShader = new javax.swing.JButton();
        btnTexturaShader = new javax.swing.JButton();
        btnPhongShader = new javax.swing.JButton();
        btnFlatShader = new javax.swing.JButton();
        btnSimpleShader = new javax.swing.JButton();
        btnPBRShader = new javax.swing.JButton();
        scrollHeramientas = new javax.swing.JScrollPane();
        pnlHerramientas = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btnInvertirNormales = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btnCentroGeometria = new javax.swing.JButton();
        btnActualizarReflejos = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        btnActualizarSombras = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        btnGuadarScreenShot = new javax.swing.JButton();
        btnSuavizar = new javax.swing.JButton();
        btnNoSuavizar = new javax.swing.JButton();
        btnTipoSolido = new javax.swing.JButton();
        btnTipoAlambre = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        btnDividir = new javax.swing.JButton();
        btnCalcularNormales = new javax.swing.JButton();
        btnDividirCatmull = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtInflarRadio = new javax.swing.JTextField();
        btnEliminarVerticesDuplicados = new javax.swing.JButton();
        pnlProcesadores = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JSeparator();
        jButton18 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        pnlMotores = new javax.swing.JPanel();
        btnStartPhysics = new javax.swing.JButton();
        btnStopPhysics = new javax.swing.JButton();
        btnAccion1 = new javax.swing.JButton();
        btnAccion2 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel21 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        spliDerecha = new javax.swing.JSplitPane();
        pnlLineaTiempo = new javax.swing.JPanel();
        sldLineaTiempo = new javax.swing.JSlider();
        btnAnimIniciar = new javax.swing.JButton();
        btnAnimDetener = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        txtAnimTiempoInicio = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtAnimTiempoFin = new javax.swing.JTextField();
        txtAnimTiempo = new javax.swing.JLabel();
        lblVelocidad = new javax.swing.JLabel();
        btnAnimVelocidad1X = new javax.swing.JButton();
        btnAnimVelocidad15X = new javax.swing.JButton();
        btnAnimVelocidad2X = new javax.swing.JButton();
        btnAnimVelocidad4X = new javax.swing.JButton();
        btnAnimVelocidad025X = new javax.swing.JButton();
        btnANimVelocidad05X = new javax.swing.JButton();
        btnAnimVelocidad075X = new javax.swing.JButton();
        btnAnimInvertir = new javax.swing.JButton();
        panelRenderes = new javax.swing.JPanel();
        barraMenu = new javax.swing.JMenuBar();
        mnuFile = new javax.swing.JMenu();
        jMenuItem19 = new javax.swing.JMenuItem();
        itmMenuOpen = new javax.swing.JMenuItem();
        itmMenuSave = new javax.swing.JMenuItem();
        itmMenuExportentity = new javax.swing.JMenuItem();
        itmMenuImport = new javax.swing.JMenuItem();
        jMenuItem24 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenu9 = new javax.swing.JMenu();
        itmAgregarVista = new javax.swing.JMenuItem();
        itmAgregarRender = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        itmAddCamara = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jSeparator8 = new javax.swing.JPopupMenu.Separator();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        mnuItemGeosfera = new javax.swing.JMenuItem();
        itmCrearCaja = new javax.swing.JMenuItem();
        itmCrearNcoesfera = new javax.swing.JMenuItem();
        itmCrearcuboesfera = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem26 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        mnuEspiral = new javax.swing.JMenuItem();
        mnuItemPrisma = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        mnuItemTetera = new javax.swing.JMenuItem();
        mnuItemSusane = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        jMenu6 = new javax.swing.JMenu();
        mnuLuzDireccional = new javax.swing.JMenuItem();
        mnuLuzPuntual = new javax.swing.JMenuItem();
        mnuLuzConica = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        itmMapaAltura = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        itmSeleccionarTodo = new javax.swing.JMenuItem();
        itmEliminar = new javax.swing.JMenuItem();
        itmMenuEliminarRecursivo = new javax.swing.JMenuItem();
        itmCopiar = new javax.swing.JMenuItem();
        itmPegar = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblEstad.setText("0 vertices - 0 polígonos");

        splitIzquierda.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        pnlEscenario1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Escenario",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new java.awt.Font("Dialog", 0, 9))); // NOI18N

        treeEntidades.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jScrollPane4.setViewportView(treeEntidades);

        javax.swing.GroupLayout pnlEscenario1Layout = new javax.swing.GroupLayout(pnlEscenario1);
        pnlEscenario1.setLayout(pnlEscenario1Layout);
        pnlEscenario1Layout.setHorizontalGroup(
                pnlEscenario1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE));
        pnlEscenario1Layout.setVerticalGroup(
                pnlEscenario1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE));

        splitIzquierda.setLeftComponent(pnlEscenario1);

        panelHerramientas.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N

        scrollOpciones.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        cbxShowLight.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        cbxShowLight.setSelected(true);
        cbxShowLight.setText("Ver luces");
        cbxShowLight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxShowLightActionPerformed(evt);
            }
        });

        cbxNormalMapping.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        cbxNormalMapping.setSelected(true);
        cbxNormalMapping.setText("Normal map");
        cbxNormalMapping.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxNormalMappingActionPerformed(evt);
            }
        });

        cbxShowBackFaces.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        cbxShowBackFaces.setText("Ver caras traseras");
        cbxShowBackFaces.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxShowBackFacesActionPerformed(evt);
            }
        });

        cbxForceRes.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        cbxForceRes.setText("Forzar Resolución");
        cbxForceRes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxForceResActionPerformed(evt);
            }
        });

        spnWidth.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        spnWidth.setModel(new javax.swing.SpinnerNumberModel(1366, 100, 3840, 1));
        spnWidth.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spnWidthStateChanged(evt);
            }
        });

        spnHeight.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        spnHeight.setModel(new javax.swing.SpinnerNumberModel(768, 100, 2160, 1));
        spnHeight.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spnHeightStateChanged(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jLabel7.setText("x");

        cbxForceSmooth.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        cbxForceSmooth.setText("Forzar Suavizado");
        cbxForceSmooth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxForceSmoothActionPerformed(evt);
            }
        });

        cbxZSort.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        cbxZSort.setSelected(true);
        cbxZSort.setText("Z Sort");
        cbxZSort.setToolTipText("Ordena las caras transparentes para renderización correcta");
        cbxZSort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxZSortActionPerformed(evt);
            }
        });

        cbxInterpolar.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        cbxInterpolar.setSelected(true);
        cbxInterpolar.setText("Interpolar Animacion");
        cbxInterpolar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxInterpolarActionPerformed(evt);
            }
        });

        chkVerGrid.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        chkVerGrid.setSelected(true);
        chkVerGrid.setText("Ver Grid");
        chkVerGrid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkVerGridActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                .addGroup(jPanel5Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(cbxForceSmooth)
                                                        .addComponent(cbxForceRes))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(spnWidth, javax.swing.GroupLayout.PREFERRED_SIZE, 64,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(4, 4, 4)
                                                .addComponent(jLabel7)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(spnHeight, javax.swing.GroupLayout.PREFERRED_SIZE, 64,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                .addGroup(jPanel5Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                                .addGroup(jPanel5Layout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(cbxShowLight)
                                                                        .addComponent(chkVerGrid))
                                                                .addGap(59, 59, 59)
                                                                .addGroup(jPanel5Layout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(cbxNormalMapping)
                                                                        .addComponent(cbxZSort)))
                                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                                .addComponent(cbxInterpolar)
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(cbxShowBackFaces)))
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap()));
        jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(cbxShowLight)
                                        .addComponent(cbxNormalMapping))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(chkVerGrid)
                                        .addComponent(cbxZSort))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(cbxInterpolar)
                                        .addComponent(cbxShowBackFaces))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbxForceSmooth)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(cbxForceRes)
                                        .addComponent(spnHeight, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 28,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(spnWidth, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(15, Short.MAX_VALUE)));

        jLabel8.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Opciones Render");
        jLabel8.setOpaque(true);

        btnRaster1.setText("Raster 1");
        btnRaster1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRaster1ActionPerformed(evt);
            }
        });

        btnRaster2.setText("Raster 2");
        btnRaster2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRaster2ActionPerformed(evt);
            }
        });

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Luz Ambiente",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new java.awt.Font("Dialog", 0, 9))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jLabel1.setText("Luz Ambiente");

        pnlColorFondo.setBackground(new java.awt.Color(0, 0, 0));
        pnlColorFondo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                pnlColorFondoMousePressed(evt);
            }

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlColorFondoMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnlColorFondoLayout = new javax.swing.GroupLayout(pnlColorFondo);
        pnlColorFondo.setLayout(pnlColorFondoLayout);
        pnlColorFondoLayout.setHorizontalGroup(
                pnlColorFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 24, Short.MAX_VALUE));
        pnlColorFondoLayout.setVerticalGroup(
                pnlColorFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
                jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(pnlColorFondo, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(223, Short.MAX_VALUE)));
        jPanel11Layout.setVerticalGroup(
                jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(pnlColorFondo, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                jPanel11Layout.createSequentialGroup()
                                                        .addComponent(jLabel1)
                                                        .addGap(4, 4, 4)))
                                .addGap(0, 0, Short.MAX_VALUE)));

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Neblina",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new java.awt.Font("Dialog", 0, 9))); // NOI18N

        chkNeblina.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        chkNeblina.setText("Activar");
        chkNeblina.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkNeblinaActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jLabel9.setText("Densidad:");

        spnNeblinaDensidad.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        spnNeblinaDensidad.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spnNeblinaDensidadStateChanged(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jLabel10.setText("Color:");

        pnlNeblinaColor.setBackground(new java.awt.Color(255, 255, 255));
        pnlNeblinaColor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                pnlNeblinaColorMousePressed(evt);
            }
        });

        javax.swing.GroupLayout pnlNeblinaColorLayout = new javax.swing.GroupLayout(pnlNeblinaColor);
        pnlNeblinaColor.setLayout(pnlNeblinaColorLayout);
        pnlNeblinaColorLayout.setHorizontalGroup(
                pnlNeblinaColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 32, Short.MAX_VALUE));
        pnlNeblinaColorLayout.setVerticalGroup(
                pnlNeblinaColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE));

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
                jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel13Layout.createSequentialGroup()
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel9)
                                        .addComponent(chkNeblina))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(spnNeblinaDensidad, javax.swing.GroupLayout.PREFERRED_SIZE, 113,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel13Layout.createSequentialGroup()
                                                .addComponent(jLabel10)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(pnlNeblinaColor, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)));
        jPanel13Layout.setVerticalGroup(
                jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel13Layout.createSequentialGroup()
                                .addGroup(jPanel13Layout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(pnlNeblinaColor, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(chkNeblina, javax.swing.GroupLayout.PREFERRED_SIZE, 0,
                                                Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(spnNeblinaDensidad, javax.swing.GroupLayout.Alignment.TRAILING,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel9))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        jLabel17.setText("Rasterizador");

        jLabel20.setText("Shader");

        btnFullShader.setText("Estandar");
        btnFullShader.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFullShaderActionPerformed(evt);
            }
        });

        btnShadowShader.setText("Sombras");
        btnShadowShader.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShadowShaderActionPerformed(evt);
            }
        });

        btnIlumShader.setText("Iluminación");
        btnIlumShader.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIlumShaderActionPerformed(evt);
            }
        });

        btnTexturaShader.setText("Textura");
        btnTexturaShader.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTexturaShaderActionPerformed(evt);
            }
        });

        btnPhongShader.setText("Phong");
        btnPhongShader.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPhongShaderActionPerformed(evt);
            }
        });

        btnFlatShader.setText("Flat");
        btnFlatShader.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFlatShaderActionPerformed(evt);
            }
        });

        btnSimpleShader.setText("Simple");
        btnSimpleShader.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpleShaderActionPerformed(evt);
            }
        });

        btnPBRShader.setText("PBR");
        btnPBRShader.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPBRShaderActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 89, Short.MAX_VALUE))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(jPanel2Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                                .addGroup(jPanel2Layout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(jLabel17)
                                                                        .addComponent(jLabel20)
                                                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                                                .addComponent(btnRaster1)
                                                                                .addPreferredGap(
                                                                                        javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                .addComponent(btnRaster2))
                                                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                                                .addGroup(jPanel2Layout
                                                                                        .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                false)
                                                                                        .addComponent(btnFlatShader,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                        .addGroup(
                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                jPanel2Layout
                                                                                                        .createSequentialGroup()
                                                                                                        .addGap(1, 1, 1)
                                                                                                        .addComponent(
                                                                                                                btnShadowShader,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                Short.MAX_VALUE))
                                                                                        .addComponent(btnFullShader,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                98, Short.MAX_VALUE)
                                                                                        .addComponent(btnTexturaShader,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE))
                                                                                .addPreferredGap(
                                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addGroup(jPanel2Layout
                                                                                        .createParallelGroup(
                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                false)
                                                                                        .addComponent(btnPhongShader,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                        .addComponent(btnIlumShader)
                                                                                        .addComponent(btnPBRShader,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                98,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                        .addComponent(btnSimpleShader,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                102, Short.MAX_VALUE))))
                                                                .addGap(0, 0, Short.MAX_VALUE))))
                                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap()));
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel17)
                                .addGap(5, 5, 5)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnRaster1)
                                        .addComponent(btnRaster2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel20)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnFullShader)
                                        .addComponent(btnPBRShader))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnIlumShader)
                                        .addComponent(btnShadowShader))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnPhongShader)
                                        .addComponent(btnTexturaShader))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnSimpleShader)
                                        .addComponent(btnFlatShader))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        scrollOpciones.setViewportView(jPanel2);

        panelHerramientas.addTab("Opciones", scrollOpciones);

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Entidad");
        jLabel2.setFocusable(false);
        jLabel2.setOpaque(true);

        jLabel18.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jLabel18.setText("Sombreado:");

        jLabel6.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jLabel6.setText("Normales:");

        btnInvertirNormales.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        btnInvertirNormales.setText("Invertir");
        btnInvertirNormales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInvertirNormalesActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jLabel11.setText("Tipo:");

        jLabel5.setText("Definir Centro:");

        btnCentroGeometria.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        btnCentroGeometria.setText("Centro de Geometría");
        btnCentroGeometria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCentroGeometriaActionPerformed(evt);
            }
        });

        btnActualizarReflejos.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        btnActualizarReflejos.setText("Actualizar reflejos");
        btnActualizarReflejos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarReflejosActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Mapas");
        jLabel12.setFocusable(false);
        jLabel12.setOpaque(true);

        btnActualizarSombras.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        btnActualizarSombras.setText("Actualizar sombras");
        btnActualizarSombras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarSombrasActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("General");
        jLabel13.setFocusable(false);
        jLabel13.setOpaque(true);

        btnGuadarScreenShot.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        btnGuadarScreenShot.setText("Guardar");
        btnGuadarScreenShot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuadarScreenShotActionPerformed(evt);
            }
        });

        btnSuavizar.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        btnSuavizar.setText("Suave");
        btnSuavizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuavizarActionPerformed(evt);
            }
        });

        btnNoSuavizar.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        btnNoSuavizar.setText("Plano");
        btnNoSuavizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNoSuavizarActionPerformed(evt);
            }
        });

        btnTipoSolido.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        btnTipoSolido.setText("Sólido");
        btnTipoSolido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTipoSolidoActionPerformed(evt);
            }
        });

        btnTipoAlambre.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        btnTipoAlambre.setText("Alambre");
        btnTipoAlambre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTipoAlambreActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jLabel14.setText("Geometría:");

        btnDividir.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        btnDividir.setText("Dividir");
        btnDividir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDividirActionPerformed(evt);
            }
        });

        btnCalcularNormales.setText("Calcular");
        btnCalcularNormales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcularNormalesActionPerformed(evt);
            }
        });

        btnDividirCatmull.setText("Dividir Catmull-Clark");
        btnDividirCatmull.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDividirCatmullActionPerformed(evt);
            }
        });

        jButton9.setText("Inflar");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jLabel4.setText("Inflar:");

        txtInflarRadio.setText("1");

        btnEliminarVerticesDuplicados.setText("Eliminar Duplicados");
        btnEliminarVerticesDuplicados.setToolTipText("Elimina los vertices duplicados");
        btnEliminarVerticesDuplicados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarVerticesDuplicadosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlHerramientasLayout = new javax.swing.GroupLayout(pnlHerramientas);
        pnlHerramientas.setLayout(pnlHerramientasLayout);
        pnlHerramientasLayout.setHorizontalGroup(
                pnlHerramientasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE)
                        .addComponent(btnCentroGeometria, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnActualizarReflejos, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnActualizarSombras, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnGuadarScreenShot, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pnlHerramientasLayout.createSequentialGroup()
                                .addGroup(pnlHerramientasLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pnlHerramientasLayout.createSequentialGroup()
                                                .addGroup(pnlHerramientasLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel18)
                                                        .addComponent(jLabel6)
                                                        .addComponent(jLabel11))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(pnlHerramientasLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                false)
                                                        .addComponent(btnTipoSolido,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(btnCalcularNormales,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(btnSuavizar, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(pnlHerramientasLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(btnTipoAlambre,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(btnInvertirNormales,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(btnNoSuavizar,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 261,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(pnlHerramientasLayout.createSequentialGroup()
                                                .addGroup(pnlHerramientasLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel14)
                                                        .addComponent(jLabel4))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(pnlHerramientasLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                false)
                                                        .addGroup(pnlHerramientasLayout.createSequentialGroup()
                                                                .addGroup(pnlHerramientasLayout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING,
                                                                        false)
                                                                        .addComponent(txtInflarRadio)
                                                                        .addComponent(btnDividir,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE))
                                                                .addGap(18, 18, 18)
                                                                .addGroup(pnlHerramientasLayout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(jButton9,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                        .addComponent(btnEliminarVerticesDuplicados,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)))
                                                        .addComponent(btnDividirCatmull,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                Short.MAX_VALUE))))
                                .addGap(67, 67, 67)));
        pnlHerramientasLayout.setVerticalGroup(
                pnlHerramientasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlHerramientasLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(pnlHerramientasLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel18)
                                        .addGroup(pnlHerramientasLayout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(btnSuavizar)
                                                .addComponent(btnNoSuavizar)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlHerramientasLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel6)
                                        .addComponent(btnInvertirNormales)
                                        .addComponent(btnCalcularNormales))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlHerramientasLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel11)
                                        .addGroup(pnlHerramientasLayout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(btnTipoSolido)
                                                .addComponent(btnTipoAlambre)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCentroGeometria)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(pnlHerramientasLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel14)
                                        .addComponent(btnDividir)
                                        .addComponent(btnEliminarVerticesDuplicados))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDividirCatmull)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlHerramientasLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pnlHerramientasLayout.createSequentialGroup()
                                                .addGroup(pnlHerramientasLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(pnlHerramientasLayout.createSequentialGroup()
                                                                .addGap(3, 3, 3)
                                                                .addComponent(jLabel4))
                                                        .addComponent(txtInflarRadio,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel12))
                                        .addComponent(jButton9))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnActualizarReflejos)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnActualizarSombras)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnGuadarScreenShot)
                                .addGap(0, 91, Short.MAX_VALUE)));

        scrollHeramientas.setViewportView(pnlHerramientas);

        panelHerramientas.addTab("Herram.", scrollHeramientas);

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        jLabel3.setText("PROCESADORES POST RENDERIZADO");

        jButton11.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jButton11.setText("Quitar");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jButton12.setText("Bloom");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton13.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jButton13.setText("Contraste");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton14.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jButton14.setText("Blur");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jButton15.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jButton15.setText("DOF 1");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jButton16.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jButton16.setText("DOF 2");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jButton17.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jButton17.setText("DOF 3");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jButton18.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jButton18.setText("Cel");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        jButton19.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jButton19.setText("MSAA");
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlProcesadoresLayout = new javax.swing.GroupLayout(pnlProcesadores);
        pnlProcesadores.setLayout(pnlProcesadoresLayout);
        pnlProcesadoresLayout.setHorizontalGroup(
                pnlProcesadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlProcesadoresLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(pnlProcesadoresLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jSeparator5)
                                        .addGroup(pnlProcesadoresLayout.createSequentialGroup()
                                                .addGroup(pnlProcesadoresLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                false)
                                                        .addComponent(jButton12, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jButton15, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jButton11, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jButton19, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(pnlProcesadoresLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                false)
                                                        .addComponent(jButton18, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jButton16,
                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jButton13, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(pnlProcesadoresLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                false)
                                                        .addComponent(jButton14, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jButton17, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap()));
        pnlProcesadoresLayout.setVerticalGroup(
                pnlProcesadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlProcesadoresLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(pnlProcesadoresLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton11)
                                        .addComponent(jButton18))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlProcesadoresLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pnlProcesadoresLayout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jButton13)
                                                .addComponent(jButton14))
                                        .addComponent(jButton12))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlProcesadoresLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton15)
                                        .addComponent(jButton16)
                                        .addComponent(jButton17))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        panelHerramientas.addTab("Procesadores", pnlProcesadores);

        btnStartPhysics.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        btnStartPhysics.setText("Iniciar");
        btnStartPhysics.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartPhysicsAP(evt);
            }
        });

        btnStopPhysics.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        btnStopPhysics.setText("Detener");
        btnStopPhysics.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStopPhysicsAP(evt);
            }
        });

        btnAccion1.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        btnAccion1.setText("Accion1");
        btnAccion1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccion1AP(evt);
            }
        });

        btnAccion2.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        btnAccion2.setText("Accion 2");
        btnAccion2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccion2AP(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jButton5.setText("Mover adelante");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jButton6.setText("Mover Atras");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jButton7.setText("Mover Derecha");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jButton8.setText("Mover Izquierda");

        jLabel19.setText("Física:");

        jLabel21.setText("Otros");

        javax.swing.GroupLayout pnlMotoresLayout = new javax.swing.GroupLayout(pnlMotores);
        pnlMotores.setLayout(pnlMotoresLayout);
        pnlMotoresLayout.setHorizontalGroup(
                pnlMotoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlMotoresLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(pnlMotoresLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(pnlMotoresLayout.createSequentialGroup()
                                                .addGroup(pnlMotoresLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                false)
                                                        .addComponent(btnAccion1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                81, Short.MAX_VALUE)
                                                        .addComponent(btnStartPhysics,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(pnlMotoresLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                false)
                                                        .addComponent(btnAccion2, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                88, Short.MAX_VALUE)
                                                        .addComponent(btnStopPhysics,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGap(0, 0, Short.MAX_VALUE))))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlMotoresLayout.createSequentialGroup()
                                .addGroup(pnlMotoresLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pnlMotoresLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(pnlMotoresLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                pnlMotoresLayout.createSequentialGroup()
                                                                        .addGroup(pnlMotoresLayout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                false)
                                                                                .addGroup(pnlMotoresLayout
                                                                                        .createSequentialGroup()
                                                                                        .addComponent(jButton5)
                                                                                        .addPreferredGap(
                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                        .addComponent(jButton6,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE))
                                                                                .addGroup(pnlMotoresLayout
                                                                                        .createSequentialGroup()
                                                                                        .addComponent(jButton7)
                                                                                        .addPreferredGap(
                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                        .addComponent(jButton8)))
                                                                        .addGap(0, 121, Short.MAX_VALUE)))))
                                .addContainerGap()));
        pnlMotoresLayout.setVerticalGroup(
                pnlMotoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlMotoresLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel19)
                                .addGap(2, 2, 2)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 4,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlMotoresLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(btnStartPhysics, javax.swing.GroupLayout.PREFERRED_SIZE, 18,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnStopPhysics, javax.swing.GroupLayout.PREFERRED_SIZE, 0,
                                                Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlMotoresLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(btnAccion1, javax.swing.GroupLayout.PREFERRED_SIZE, 15,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnAccion2, javax.swing.GroupLayout.PREFERRED_SIZE, 0,
                                                Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 6,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlMotoresLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 16,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 0,
                                                Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(
                                        pnlMotoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 14,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 14,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(198, Short.MAX_VALUE)));

        panelHerramientas.addTab("Motores", pnlMotores);

        splitIzquierda.setRightComponent(panelHerramientas);

        splitPanel.setLeftComponent(splitIzquierda);

        spliDerecha.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        sldLineaTiempo.setValue(0);
        sldLineaTiempo.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sldLineaTiempoStateChanged(evt);
            }
        });

        btnAnimIniciar.setText("Iniciar");
        btnAnimIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnimIniciarActionPerformed(evt);
            }
        });

        btnAnimDetener.setText("Detener");
        btnAnimDetener.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnimDetenerActionPerformed(evt);
            }
        });

        jLabel15.setText("Inicio:");

        txtAnimTiempoInicio.setText("0");
        txtAnimTiempoInicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAnimTiempoInicioActionPerformed(evt);
            }
        });
        txtAnimTiempoInicio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAnimTiempoInicioKeyReleased(evt);
            }
        });

        jLabel16.setText("Fin:");

        txtAnimTiempoFin.setText("10");
        txtAnimTiempoFin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAnimTiempoFinActionPerformed(evt);
            }
        });
        txtAnimTiempoFin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAnimTiempoFinKeyReleased(evt);
            }
        });

        txtAnimTiempo.setText("Tiempo:0");

        lblVelocidad.setText("Velocidad:");

        btnAnimVelocidad1X.setFont(new java.awt.Font("Dialog", 0, 8)); // NOI18N
        btnAnimVelocidad1X.setText("1.0 X");
        btnAnimVelocidad1X.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnimVelocidad1XActionPerformed(evt);
            }
        });

        btnAnimVelocidad15X.setFont(new java.awt.Font("Dialog", 0, 8)); // NOI18N
        btnAnimVelocidad15X.setText("1.5 X");
        btnAnimVelocidad15X.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnimVelocidad15XActionPerformed(evt);
            }
        });

        btnAnimVelocidad2X.setFont(new java.awt.Font("Dialog", 0, 8)); // NOI18N
        btnAnimVelocidad2X.setText("2.0 X");
        btnAnimVelocidad2X.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnimVelocidad2XActionPerformed(evt);
            }
        });

        btnAnimVelocidad4X.setFont(new java.awt.Font("Dialog", 0, 8)); // NOI18N
        btnAnimVelocidad4X.setText("4.0 X");
        btnAnimVelocidad4X.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnimVelocidad4XActionPerformed(evt);
            }
        });

        btnAnimVelocidad025X.setFont(new java.awt.Font("Dialog", 0, 8)); // NOI18N
        btnAnimVelocidad025X.setText("0.25 X");
        btnAnimVelocidad025X.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnimVelocidad025XActionPerformed(evt);
            }
        });

        btnANimVelocidad05X.setFont(new java.awt.Font("Dialog", 0, 8)); // NOI18N
        btnANimVelocidad05X.setText("0.5 X");
        btnANimVelocidad05X.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnANimVelocidad05XActionPerformed(evt);
            }
        });

        btnAnimVelocidad075X.setFont(new java.awt.Font("Dialog", 0, 8)); // NOI18N
        btnAnimVelocidad075X.setText("0.75 X");
        btnAnimVelocidad075X.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnimVelocidad075XActionPerformed(evt);
            }
        });

        btnAnimInvertir.setText("Inv.");
        btnAnimInvertir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnimInvertirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlLineaTiempoLayout = new javax.swing.GroupLayout(pnlLineaTiempo);
        pnlLineaTiempo.setLayout(pnlLineaTiempoLayout);
        pnlLineaTiempoLayout.setHorizontalGroup(
                pnlLineaTiempoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlLineaTiempoLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(pnlLineaTiempoLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(sldLineaTiempo, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(pnlLineaTiempoLayout.createSequentialGroup()
                                                .addComponent(btnAnimIniciar)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnAnimDetener, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnAnimInvertir)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel15)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtAnimTiempoInicio,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 67,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel16)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtAnimTiempoFin, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(txtAnimTiempo)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(lblVelocidad)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btnAnimVelocidad025X)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnANimVelocidad05X)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnAnimVelocidad075X)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnAnimVelocidad1X)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnAnimVelocidad15X)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnAnimVelocidad2X)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnAnimVelocidad4X)
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap()));
        pnlLineaTiempoLayout.setVerticalGroup(
                pnlLineaTiempoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlLineaTiempoLayout
                                .createSequentialGroup()
                                .addGroup(pnlLineaTiempoLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnAnimIniciar)
                                        .addComponent(btnAnimDetener)
                                        .addComponent(jLabel15)
                                        .addComponent(txtAnimTiempoInicio, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel16)
                                        .addComponent(txtAnimTiempoFin, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtAnimTiempo)
                                        .addComponent(lblVelocidad)
                                        .addComponent(btnAnimVelocidad1X)
                                        .addComponent(btnAnimVelocidad15X)
                                        .addComponent(btnAnimVelocidad2X)
                                        .addComponent(btnAnimVelocidad4X)
                                        .addComponent(btnAnimVelocidad025X)
                                        .addComponent(btnANimVelocidad05X)
                                        .addComponent(btnAnimVelocidad075X)
                                        .addComponent(btnAnimInvertir))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(sldLineaTiempo, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap()));

        spliDerecha.setLeftComponent(pnlLineaTiempo);

        javax.swing.GroupLayout panelRenderesLayout = new javax.swing.GroupLayout(panelRenderes);
        panelRenderes.setLayout(panelRenderesLayout);
        panelRenderesLayout.setHorizontalGroup(
                panelRenderesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 1498, Short.MAX_VALUE));
        panelRenderesLayout.setVerticalGroup(
                panelRenderesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 631, Short.MAX_VALUE));

        spliDerecha.setRightComponent(panelRenderes);

        splitPanel.setRightComponent(spliDerecha);

        mnuFile.setText("Archivo");

        jMenuItem19.setText("Nuevo");
        mnuFile.add(jMenuItem19);

        itmMenuOpen.setText("Abrir");
        itmMenuOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openFileAP(evt);
            }
        });
        mnuFile.add(itmMenuOpen);

        itmMenuSave.setText("Guardar");
        itmMenuSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveFileAP(evt);
            }
        });
        mnuFile.add(itmMenuSave);

        itmMenuExportentity.setText("Exportar entidad");
        itmMenuExportentity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportEntityAP(evt);
            }
        });
        mnuFile.add(itmMenuExportentity);

        itmMenuImport.setText("Importar objeto");
        itmMenuImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importEntityAP(evt);
            }
        });
        mnuFile.add(itmMenuImport);

        jMenuItem24.setText("Exportar objeto");
        jMenuItem24.setEnabled(false);
        mnuFile.add(jMenuItem24);

        barraMenu.add(mnuFile);

        jMenu3.setText("Renderizadores");

        jMenu5.setText("Tipo Vista");

        jMenuItem13.setText("Alambre");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem13);

        jMenuItem14.setText("Sólido Plano");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem14);

        jMenuItem15.setText("Solido Suave");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem15);

        jMenuItem16.setText("Material");
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem16ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem16);

        jMenuItem17.setText("Sombras");
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem17ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem17);

        jMenu3.add(jMenu5);

        jMenu9.setText("Agregar");

        itmAgregarVista.setText("Interno");
        itmAgregarVista.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itmAgregarVistaActionPerformed(evt);
            }
        });
        jMenu9.add(itmAgregarVista);

        itmAgregarRender.setText("Java3D");
        itmAgregarRender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itmAgregarRenderActionPerformed(evt);
            }
        });
        jMenu9.add(itmAgregarRender);

        jMenu3.add(jMenu9);

        barraMenu.add(jMenu3);

        jMenu4.setText("Crear");

        jMenuItem3.setText("Entidad vacía");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem3);

        itmAddCamara.setText("Cámara");
        itmAddCamara.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itmAddCamaraActionPerformed(evt);
            }
        });
        jMenu4.add(itmAddCamara);

        jMenu1.setText("Primitivas");

        jMenuItem2.setText("Malla");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);
        jMenu1.add(jSeparator8);

        jMenuItem5.setText("Triángulo");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);

        jMenuItem18.setText("Plano");
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem18);

        jMenuItem8.setText("Esfera");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem8);

        mnuItemGeosfera.setText("Geoesfera");
        mnuItemGeosfera.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemGeosferaActionPerformed(evt);
            }
        });
        jMenu1.add(mnuItemGeosfera);

        itmCrearCaja.setText("Cubo");
        itmCrearCaja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itmCrearCajaActionPerformed(evt);
            }
        });
        jMenu1.add(itmCrearCaja);

        itmCrearNcoesfera.setText("Nicoesfera");
        itmCrearNcoesfera.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itmCrearNcoesferaActionPerformed(evt);
            }
        });
        jMenu1.add(itmCrearNcoesfera);

        itmCrearcuboesfera.setText("Cuboesfera");
        itmCrearcuboesfera.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itmCrearcuboesferaActionPerformed(evt);
            }
        });
        jMenu1.add(itmCrearcuboesfera);
        jMenu1.add(jSeparator4);

        jMenuItem10.setText("Toro");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem10);

        jMenuItem11.setText("Cilindro");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem11);

        jMenuItem26.setText("Cilindro X");
        jMenuItem26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem26ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem26);

        jMenuItem1.setText("Cilindro Z");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem12.setText("Cono");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem12);

        mnuEspiral.setText("Espiral");
        mnuEspiral.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuEspiralActionPerformed(evt);
            }
        });
        jMenu1.add(mnuEspiral);

        mnuItemPrisma.setText("Prisma");
        mnuItemPrisma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemPrismaActionPerformed(evt);
            }
        });
        jMenu1.add(mnuItemPrisma);
        jMenu1.add(jSeparator7);

        mnuItemTetera.setText("Tetera");
        mnuItemTetera.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemTeteraActionPerformed(evt);
            }
        });
        jMenu1.add(mnuItemTetera);

        mnuItemSusane.setText("Mona/Susane");
        mnuItemSusane.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemSusaneActionPerformed(evt);
            }
        });
        jMenu1.add(mnuItemSusane);
        jMenu1.add(jSeparator6);

        jMenu4.add(jMenu1);

        jMenu6.setText("Luces");

        mnuLuzDireccional.setText("Direccional");
        mnuLuzDireccional.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuLuzDireccionalActionPerformed(evt);
            }
        });
        jMenu6.add(mnuLuzDireccional);

        mnuLuzPuntual.setText("Puntual");
        mnuLuzPuntual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuLuzPuntualActionPerformed(evt);
            }
        });
        jMenu6.add(mnuLuzPuntual);

        mnuLuzConica.setText("Cónica");
        mnuLuzConica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuLuzConicaActionPerformed(evt);
            }
        });
        jMenu6.add(mnuLuzConica);

        jMenu4.add(jMenu6);

        jMenu8.setText("Terreno");

        itmMapaAltura.setText("Mapa de Altura");
        itmMapaAltura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itmMapaAlturaActionPerformed(evt);
            }
        });
        jMenu8.add(itmMapaAltura);

        jMenu4.add(jMenu8);

        barraMenu.add(jMenu4);

        jMenu2.setText("Edicion");

        itmSeleccionarTodo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A,
                java.awt.event.InputEvent.CTRL_DOWN_MASK));
        itmSeleccionarTodo.setText("Seleccionar Todo");
        itmSeleccionarTodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itmSeleccionarTodoActionPerformed(evt);
            }
        });
        jMenu2.add(itmSeleccionarTodo);

        itmEliminar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0));
        itmEliminar.setText("Eliminar");
        itmEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itmEliminarActionPerformed(evt);
            }
        });
        jMenu2.add(itmEliminar);

        itmMenuEliminarRecursivo.setText("Eliminar Recursivo");
        itmMenuEliminarRecursivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itmMenuEliminarRecursivoActionPerformed(evt);
            }
        });
        jMenu2.add(itmMenuEliminarRecursivo);

        itmCopiar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C,
                java.awt.event.InputEvent.CTRL_DOWN_MASK));
        itmCopiar.setText("Copiar");
        itmCopiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itmCopiarActionPerformed(evt);
            }
        });
        jMenu2.add(itmCopiar);

        itmPegar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V,
                java.awt.event.InputEvent.CTRL_DOWN_MASK));
        itmPegar.setText("Pegar");
        itmPegar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itmPegarActionPerformed(evt);
            }
        });
        jMenu2.add(itmPegar);

        barraMenu.add(jMenu2);

        setJMenuBar(barraMenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(lblEstad))
                                        .addComponent(barraProgreso, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(splitPanel))
                                .addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblEstad)
                                .addGap(4, 4, 4)
                                .addComponent(splitPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(barraProgreso, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap()));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rendererMouseDragged(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_rendererMouseDragged

    }// GEN-LAST:event_rendererMouseDragged

    private void rendererMouseReleased(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_rendererMouseReleased

    }// GEN-LAST:event_rendererMouseReleased

    private void rendererMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {// GEN-FIRST:event_rendererMouseWheelMoved

    }// GEN-LAST:event_rendererMouseWheelMoved

    private void itmEliminarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_itmEliminarActionPerformed
        LinkedList<Entity> toRemove = new LinkedList<>();
        for (Entity object : renderer.entidadesSeleccionadas) {
            toRemove.add(object);
        }
        for (Entity object : toRemove) {
            // renderer.eliminarObjeto(object);
            motor.getEscena().removeEntity(object);
        }

        actualizarArbolEscena();
    }// GEN-LAST:event_itmEliminarActionPerformed

    private void rendererMouseEntered(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_rendererMouseEntered

    }// GEN-LAST:event_rendererMouseEntered

    private void rendererKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_rendererKeyPressed

    }// GEN-LAST:event_rendererKeyPressed

    private void rendererKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_rendererKeyReleased

    }// GEN-LAST:event_rendererKeyReleased

    private void rendererFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_rendererFocusLost

    }// GEN-LAST:event_rendererFocusLost

    private void rendererMousePressed(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_rendererMousePressed

    }// GEN-LAST:event_rendererMousePressed

    private void itmCopiarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_itmCopiarActionPerformed
        clipboard.clear();
        for (Entity object : renderer.entidadesSeleccionadas) {
            clipboard.add(object);
        }
    }// GEN-LAST:event_itmCopiarActionPerformed

    private void itmPegarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_itmPegarActionPerformed
        if (clipboard.size() > 0) {
            renderer.entidadesSeleccionadas.clear();
            for (Entity object : clipboard) {
                Entity newObject = object.clone();
                newObject.setName(object.getName() + " Copy");
                motor.getEscena().addEntity(newObject);
                renderer.entidadesSeleccionadas.add(newObject);
                renderer.entidadActiva = newObject;
            }

            for (Entity object : renderer.entidadesSeleccionadas) {
                System.out.println(object.getName());
            }
            objectListLock = true;
            // lstObjects.clearSelection();
            actualizarArbolEscena();
            populateControls();
            objectListLock = false;
        }
    }// GEN-LAST:event_itmPegarActionPerformed

    private void itmSeleccionarTodoActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_itmSeleccionarTodoActionPerformed
        // lstObjects.setSelectionInterval(0, lstObjects.getModel().getSize());
    }// GEN-LAST:event_itmSeleccionarTodoActionPerformed

    private void itmAgregarVistaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_itmAgregarVistaActionPerformed
        String nombre = JOptionPane.showInputDialog("Nombre del renderizador");
        agregarRenderer(nombre, RenderEngine.RENDER_INTERNO);
    }// GEN-LAST:event_itmAgregarVistaActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem8ActionPerformed
        Entity esfera = new Entity("Esfera");
        esfera.addComponent(new QEsfera(1.0f));
        esfera.addComponent(new QColisionEsfera(1.0f));

        motor.getEscena().addEntity(esfera);
        actualizarArbolEscena();
        seleccionarEntidad(esfera);
    }// GEN-LAST:event_jMenuItem8ActionPerformed

    private void itmCrearCajaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_itmCrearCajaActionPerformed
        Entity cubo = new Entity("Cubo");
        cubo.addComponent(new QCaja(1));
        cubo.addComponent(new QColisionCaja(1, 1, 1));
        motor.getEscena().addEntity(cubo);
        actualizarArbolEscena();
        seleccionarEntidad(cubo);
        // actualizarFigurasRenderers();
    }// GEN-LAST:event_itmCrearCajaActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem10ActionPerformed
        Entity objeto = new Entity("Toro");
        QToro toro = new QToro(3, 1, 30, 20);
        objeto.addComponent(toro);
        // objeto.agregarComponente(new QColisionCapsula(1, 2));
        // objeto.agregarComponente(new QColisionCaja(2, 1, 2));
        objeto.addComponent(new QColisionMallaConvexa(toro));
        motor.getEscena().addEntity(objeto);
        actualizarArbolEscena();
        seleccionarEntidad(objeto);
    }// GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem11ActionPerformed
        Entity objeto = new Entity("Cilindro");
        objeto.addComponent(new QCilindro(1, 1.0f));
        objeto.addComponent(new QColisionCilindro(1, 1.0f));
        motor.getEscena().addEntity(objeto);
        actualizarArbolEscena();
        seleccionarEntidad(objeto);
    }// GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem12ActionPerformed
        Entity objeto = new Entity("Cono");
        objeto.addComponent(new QCono(1, 1.0f));
        objeto.addComponent(new QColisionCono(1, 1.0f));
        motor.getEscena().addEntity(objeto);
        actualizarArbolEscena();
        seleccionarEntidad(objeto);
    }// GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem13ActionPerformed
        renderer.opciones.setTipoVista(QOpcionesRenderer.VISTA_WIRE);
    }// GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem14ActionPerformed
        renderer.opciones.setTipoVista(QOpcionesRenderer.VISTA_FLAT);
    }// GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem15ActionPerformed
        renderer.opciones.setTipoVista(QOpcionesRenderer.VISTA_PHONG);
    }// GEN-LAST:event_jMenuItem15ActionPerformed

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem16ActionPerformed
        renderer.opciones.setMaterial(!renderer.opciones.isMaterial());
    }// GEN-LAST:event_jMenuItem16ActionPerformed

    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem17ActionPerformed
        renderer.opciones.setSombras(!renderer.opciones.isSombras());
    }// GEN-LAST:event_jMenuItem17ActionPerformed

    private void mnuLuzPuntualActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuLuzPuntualActionPerformed
        Entity nuevaLuz = new Entity("Luz Puntual");
        nuevaLuz.addComponent(
                new QPointLigth(0.75f, new QColor(Color.white), Float.POSITIVE_INFINITY, false, false));
        motor.getEscena().addEntity(nuevaLuz);
        actualizarArbolEscena();
        seleccionarEntidad(nuevaLuz);
    }// GEN-LAST:event_mnuLuzPuntualActionPerformed

    private void mnuLuzDireccionalActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuLuzDireccionalActionPerformed
        Entity nuevaLuz = new Entity("Luz Direccional");
        nuevaLuz.addComponent(
                new QDirectionalLigth(1f, new QColor(Color.white), Float.POSITIVE_INFINITY, false, false));
        motor.getEscena().addEntity(nuevaLuz);
        actualizarArbolEscena();
        seleccionarEntidad(nuevaLuz);
    }// GEN-LAST:event_mnuLuzDireccionalActionPerformed

    private void mnuLuzConicaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuLuzConicaActionPerformed
        Entity nuevaLuz = new Entity("Luz Cónica");
        nuevaLuz.addComponent(new QSpotLigth(0.75f, new QColor(Color.white), Float.POSITIVE_INFINITY,
                QVector3.of(0, -1, 0), (float) Math.toRadians(45), (float) Math.toRadians(40), false, false));
        motor.getEscena().addEntity(nuevaLuz);
        actualizarArbolEscena();
        seleccionarEntidad(nuevaLuz);
    }// GEN-LAST:event_mnuLuzConicaActionPerformed

    private void cbxZSortActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cbxZSortActionPerformed
        renderer.opciones.setzSort(cbxZSort.isSelected());
    }// GEN-LAST:event_cbxZSortActionPerformed

    private void cbxForceSmoothActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cbxForceSmoothActionPerformed
        renderer.opciones.setForzarSuavizado(cbxForceSmooth.isSelected());
    }// GEN-LAST:event_cbxForceSmoothActionPerformed

    private void spnHeightStateChanged(javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_spnHeightStateChanged
        applyResolution();
    }// GEN-LAST:event_spnHeightStateChanged

    private void spnWidthStateChanged(javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_spnWidthStateChanged
        applyResolution();
    }// GEN-LAST:event_spnWidthStateChanged

    private void cbxForceResActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cbxForceResActionPerformed
        applyResolution();
    }// GEN-LAST:event_cbxForceResActionPerformed

    private void cbxShowBackFacesActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cbxShowBackFacesActionPerformed
        renderer.opciones.setDibujarCarasTraseras(cbxShowBackFaces.isSelected());
    }// GEN-LAST:event_cbxShowBackFacesActionPerformed

    private void cbxNormalMappingActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cbxNormalMappingActionPerformed
        renderer.opciones.setNormalMapping(cbxNormalMapping.isSelected());
    }// GEN-LAST:event_cbxNormalMappingActionPerformed

    private void cbxShowLightActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cbxShowLightActionPerformed
        renderer.opciones.setDibujarLuces(cbxShowLight.isSelected());
    }// GEN-LAST:event_cbxShowLightActionPerformed

    private void importarObjeto() {

        // chooser.setCurrentDirectory(new File("assets/"));
        chooser.setFileFilter(new FileNameExtensionFilter("Archivos Motor3D", "qengine"));
        chooser.setFileFilter(new FileNameExtensionFilter("Wavefront OBJ", "obj"));
        chooser.setFileFilter(new FileNameExtensionFilter("Archivos ASCII", "txt"));
        chooser.setFileFilter(new FileNameExtensionFilter("Archivos 3DS", "3ds"));
        chooser.setFileFilter(new FileNameExtensionFilter("Archivos Blender", "blend"));
        // chooser.setFileFilter(new FileNameExtensionFilter("Archivos MD5", "md5mesh",
        // "md5anim"));
        chooser.setFileFilter(new FileNameExtensionFilter("Archivos MD5", "md5mesh"));
        chooser.setFileFilter(new FileNameExtensionFilter("Archivos MD2", "md2"));
        chooser.setFileFilter(new FileNameExtensionFilter("Archivos FBX", "fbx"));
        chooser.setFileFilter(new FileNameExtensionFilter("Archivos Collada", "dae"));
        chooser.setFileFilter(new FileNameExtensionFilter("Archivos soportados", "txt", "obj", "3ds", "md2", "md5mesh",
                "qengine", "dae", "blend", "fbx"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                ModelLoader loader = new DefaultModelLoader();
                Entity entity = loader.loadModel(chooser.getSelectedFile());
                motor.getEscena().addEntity(entity);
                seleccionarEntidad(entity);
                actualizarArbolEscena();
            } catch (Exception e) {

            }
        }
    }

    private void btnGuadarScreenShotActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnGuadarScreenShotActionPerformed
        try {
            ImageIO.write(renderer.getFrameBuffer().getRendered(), "png",
                    new File("assets/capturas/captura_" + sdf.format(new Date()) + ".png"));
        } catch (IOException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }

    }// GEN-LAST:event_btnGuadarScreenShotActionPerformed

    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem18ActionPerformed
        Entity entidad = new Entity("Plano");
        Mesh plano = new QPlano(2, 2);
        entidad.addComponent(plano);
        entidad.addComponent(new QColisionMallaConvexa(plano));
        motor.getEscena().addEntity(entidad);
        actualizarArbolEscena();
        seleccionarEntidad(entidad);
    }// GEN-LAST:event_jMenuItem18ActionPerformed

    private void importEntityAP(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem23ActionPerformed
        importarObjeto();
    }// GEN-LAST:event_jMenuItem23ActionPerformed

    private void exportEntityAP(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem22ActionPerformed
        chooser.setFileFilter(new FileNameExtensionFilter("Archivo Entidad Motor3D", "qengine"));
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            SerializarUtil.agregarObjeto(chooser.getSelectedFile().getAbsolutePath(), renderer.entidadActiva, false,
                    true);
        }
    }// GEN-LAST:event_jMenuItem22ActionPerformed

    private void saveFileAP(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem25ActionPerformed
        // chooser.setCurrentDirectory(new File("assets/"));
        chooser.setFileFilter(new FileNameExtensionFilter("Archivo Escenario Motor3D", "qengineuni"));
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            if (chooser.getSelectedFile().exists()) {
                chooser.getSelectedFile().delete();
            }
            barraProgreso.setValue(0);
            String archivo = chooser.getSelectedFile().getAbsolutePath();
            if (!archivo.toLowerCase().endsWith(".qengineuni")) {
                archivo = archivo + ".qengineuni";
            }
            int i = 0;
            int tam = motor.getEscena().getEntities().size();
            for (Entity entidad : motor.getEscena().getEntities()) {
                SerializarUtil.agregarObjeto(archivo, entidad, true, true);
                i++;
                barraProgreso.setValue((int) (100 * (float) i / (float) tam));
            }
            barraProgreso.setValue(100);
        }

    }// GEN-LAST:event_jMenuItem25ActionPerformed

    private void openFileAP(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem20ActionPerformed
        try {
            // chooser.setCurrentDirectory(new File("assets/"));
            chooser.setFileFilter(new FileNameExtensionFilter("Archivo Escenario Motor3D", "qengineuni"));
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                Entity entity = AssetManager.get().loadModel(chooser.getSelectedFile());
                motor.getEscena().addEntity(entity);
                if (entity instanceof Camera) {
                    renderer.setCamara((Camera) entity);
                    actualizarArbolEscena();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al abrir archivo " + e.getMessage());
            e.printStackTrace();
        }

        actualizarArbolEscena();
    }// GEN-LAST:event_jMenuItem20ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton11ActionPerformed
        renderer.setEfectosPostProceso(null);
    }// GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton12ActionPerformed
        renderer.setEfectosPostProceso(new QEfectoBloom());
    }// GEN-LAST:event_jButton12ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton13ActionPerformed
        renderer.setEfectosPostProceso(new QEfectoContraste());
    }// GEN-LAST:event_jButton13ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton14ActionPerformed
        renderer.setEfectosPostProceso(
                new QEfectoBlur(renderer.getFrameBuffer().getAncho(), renderer.getFrameBuffer().getAlto()));
    }// GEN-LAST:event_jButton14ActionPerformed

    private void btnActualizarReflejosActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnActualizarReflejosActionPerformed
        // motor.setForzarActualizacionMapaReflejos(true);
    }// GEN-LAST:event_btnActualizarReflejosActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton15ActionPerformed
        renderer.setEfectosPostProceso(new QEfectoDepthOfField(QProcesadorDepthOfField.DESENFOQUE_CERCA, 0.5f));
    }// GEN-LAST:event_jButton15ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton16ActionPerformed
        renderer.setEfectosPostProceso(new QEfectoDepthOfField(QProcesadorDepthOfField.DESENFOQUE_LEJOS, 0.5f));
    }// GEN-LAST:event_jButton16ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton17ActionPerformed
        renderer.setEfectosPostProceso(new QEfectoDepthOfField(QProcesadorDepthOfField.DESENFOQUE_EXCLUYENTE, 0.5f));
    }// GEN-LAST:event_jButton17ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton18ActionPerformed
        renderer.setEfectosPostProceso(new QEfectoCel());
    }// GEN-LAST:event_jButton18ActionPerformed

    private void pnlColorFondoMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_pnlColorFondoMouseClicked

    }// GEN-LAST:event_pnlColorFondoMouseClicked

    private void pnlColorFondoMousePressed(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_pnlColorFondoMousePressed
        JColorChooser colorChooser = new JColorChooser();
        Color newColor = colorChooser.showDialog(this, "Seleccione un color", pnlColorFondo.getBackground());
        if (newColor != null) {
            pnlColorFondo.setBackground(newColor);
            motor.getEscena().setAmbientColor(new QColor(newColor));
            // renderer.setColorFondo(new QColor(newColor));
        }
    }// GEN-LAST:event_pnlColorFondoMousePressed

    private void btnActualizarSombrasActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnActualizarSombrasActionPerformed
        renderer.setForzarActualizacionMapaSombras(true);
    }// GEN-LAST:event_btnActualizarSombrasActionPerformed

    private void pnlNeblinaColorMousePressed(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_pnlNeblinaColorMousePressed
        JColorChooser colorChooser = new JColorChooser();
        Color newColor = colorChooser.showDialog(this, "Seleccione un color", pnlColorFondo.getBackground());
        if (newColor != null) {
            pnlColorFondo.setBackground(newColor);
            renderer.getEscena().fog.setColour(new QColor(newColor));
        }
    }// GEN-LAST:event_pnlNeblinaColorMousePressed

    private void chkNeblinaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_chkNeblinaActionPerformed
        renderer.getEscena().fog.setActive(chkNeblina.isSelected());
    }// GEN-LAST:event_chkNeblinaActionPerformed

    private void spnNeblinaDensidadStateChanged(javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_spnNeblinaDensidadStateChanged
        renderer.getEscena().fog.setDensity(((Double) spnNeblinaDensidad.getValue()).floatValue());
    }// GEN-LAST:event_spnNeblinaDensidadStateChanged

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem2ActionPerformed
        Entity entidad = new Entity("Malla");
        PlanarMesh malla = new PlanarMesh(true, 20, 20, 20, 20);
        entidad.addComponent(malla);
        entidad.addComponent(new QColisionMallaConvexa(malla));
        motor.getEscena().addEntity(entidad);
        actualizarArbolEscena();
        seleccionarEntidad(entidad);
    }// GEN-LAST:event_jMenuItem2ActionPerformed

    private void itmMapaAlturaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_itmMapaAlturaActionPerformed
        chooser.setFileFilter(new FileNameExtensionFilter("Archivos soportados", "png", "jpg", "jpeg", "bmp", "gif"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            Entity entidad = new Entity("terreno");
            HeightMapTerrain terreno = new HeightMapTerrain(chooser.getSelectedFile(), 1, 0f, 50f, 5, null, true);
            entidad.addComponent(terreno);
            terreno.build();
            motor.getEscena().addEntity(entidad);
            actualizarArbolEscena();
            seleccionarEntidad(entidad);
        }
    }// GEN-LAST:event_itmMapaAlturaActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem3ActionPerformed
        Entity entidad = new Entity("Entidad");
        motor.getEscena().addEntity(entidad);
        actualizarArbolEscena();
        seleccionarEntidad(entidad);
    }// GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem26ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem26ActionPerformed
        Entity objeto = new Entity("Cilindro");
        objeto.addComponent(new QCilindroX(1, 1.0f));
        objeto.addComponent(new QColisionCilindroX(1, 1.0f));
        motor.getEscena().addEntity(objeto);
        actualizarArbolEscena();
        seleccionarEntidad(objeto);
    }// GEN-LAST:event_jMenuItem26ActionPerformed

    private void btnInvertirNormalesActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnInvertirNormalesActionPerformed
        for (Entity seleccionado : renderer.entidadesSeleccionadas) {
            for (EntityComponent compo : seleccionado.getComponents()) {
                if (compo instanceof Mesh) {
                    QUtilNormales.invertirNormales((Mesh) compo);
                }
            }
        }
    }// GEN-LAST:event_btnInvertirNormalesActionPerformed

    private void btnCentroGeometriaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnCentroGeometriaActionPerformed
        for (Entity seleccionado : renderer.entidadesSeleccionadas) {
            QDefinirCentro.definirCentroOrigenAGeometria(seleccionado);
            seleccionarEntidad(seleccionado);
        }
    }// GEN-LAST:event_btnCentroGeometriaActionPerformed

    private void btnSuavizarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnSuavizarActionPerformed
        for (Entity seleccionado : renderer.entidadesSeleccionadas) {
            for (EntityComponent compo : seleccionado.getComponents()) {
                if (compo instanceof Mesh) {
                    QMaterialUtil.suavizar((Mesh) compo, true);
                }
            }
        }
    }// GEN-LAST:event_btnSuavizarActionPerformed

    private void btnNoSuavizarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnNoSuavizarActionPerformed
        for (Entity seleccionado : renderer.entidadesSeleccionadas) {
            for (EntityComponent compo : seleccionado.getComponents()) {
                if (compo instanceof Mesh) {
                    QMaterialUtil.suavizar((Mesh) compo, false);
                }
            }
        }
    }// GEN-LAST:event_btnNoSuavizarActionPerformed

    private void btnTipoSolidoActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnTipoSolidoActionPerformed
        for (Entity seleccionado : renderer.entidadesSeleccionadas) {
            for (EntityComponent compo : seleccionado.getComponents()) {
                if (compo instanceof Mesh) {
                    ((Mesh) compo).tipo = Mesh.GEOMETRY_TYPE_MESH;
                }
            }
        }
    }// GEN-LAST:event_btnTipoSolidoActionPerformed

    private void btnTipoAlambreActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnTipoAlambreActionPerformed
        for (Entity seleccionado : renderer.entidadesSeleccionadas) {
            for (EntityComponent compo : seleccionado.getComponents()) {
                if (compo instanceof Mesh) {
                    ((Mesh) compo).tipo = Mesh.GEOMETRY_TYPE_WIRE;
                }
            }
        }
    }// GEN-LAST:event_btnTipoAlambreActionPerformed

    private void itmAgregarRenderActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_itmAgregarRenderActionPerformed
        String nombre = JOptionPane.showInputDialog("Nombre del renderizador");
        agregarRenderer(nombre, RenderEngine.RENDER_JAVA3D);
    }// GEN-LAST:event_itmAgregarRenderActionPerformed

    private void itmAddCamaraActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_itmAddCamaraActionPerformed
        Camera entidad = new Camera();
        motor.getEscena().addEntity(entidad);
        actualizarArbolEscena();
        seleccionarEntidad(entidad);
    }// GEN-LAST:event_itmAddCamaraActionPerformed

    private void btnDividirActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnDividirActionPerformed
        for (Entity seleccionado : renderer.entidadesSeleccionadas) {
            for (EntityComponent compo : seleccionado.getComponents()) {
                if (compo instanceof Mesh) {
                    // QMallaUtil.subdividir((QGeometria) compo, 1);
                    ((Mesh) compo).dividir();
                }
            }
        }
    }// GEN-LAST:event_btnDividirActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem5ActionPerformed
        Entity entidad = new Entity("Triángulo");
        QTriangulo triangulo = new QTriangulo(1);
        entidad.addComponent(triangulo);
        entidad.addComponent(new QColisionTriangulo(triangulo));
        motor.getEscena().addEntity(entidad);
        actualizarArbolEscena();
        seleccionarEntidad(entidad);
    }// GEN-LAST:event_jMenuItem5ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton19ActionPerformed
        renderer.setEfectosPostProceso(new QAntialiasing());
    }// GEN-LAST:event_jButton19ActionPerformed

    private void mnuEspiralActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuEspiralActionPerformed
        Entity objeto = new Entity("Espiral");
        objeto.addComponent(new QEspiral(1, 10, 20));
        // objeto.agregarComponente(new QColisionCilindro(1, 1.0f));
        motor.getEscena().addEntity(objeto);
        actualizarArbolEscena();
        seleccionarEntidad(objeto);
    }// GEN-LAST:event_mnuEspiralActionPerformed

    private void btnRaster1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnRaster1ActionPerformed
        renderer.cambiarRaster(1);
    }// GEN-LAST:event_btnRaster1ActionPerformed

    private void btnRaster2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnRaster2ActionPerformed
        renderer.cambiarRaster(2);
    }// GEN-LAST:event_btnRaster2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem1ActionPerformed
        Entity objeto = new Entity("Cilindro");
        objeto.addComponent(new QCilindroZ(1, 1.0f));
        objeto.addComponent(new QColisionCilindroX(1, 1.0f));
        motor.getEscena().addEntity(objeto);
        actualizarArbolEscena();
        seleccionarEntidad(objeto);
    }// GEN-LAST:event_jMenuItem1ActionPerformed

    private void mnuItemPrismaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuItemPrismaActionPerformed
        Entity objeto = new Entity("Prisma");
        // objeto.agregarComponente(new QPrisma(3, 1.0f, 1.0f, 20, 3));
        objeto.addComponent(new QPrisma(3, 1.0f, 1.0f, 5, 3));
        // objeto.agregarComponente(new QColisionCilindro(1, 1.0f));
        motor.getEscena().addEntity(objeto);
        actualizarArbolEscena();
        seleccionarEntidad(objeto);
    }// GEN-LAST:event_mnuItemPrismaActionPerformed

    private void mnuItemGeosferaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuItemGeosferaActionPerformed
        Entity esfera = new Entity("Geoesfera");
        esfera.addComponent(new QGeoesfera(1.0f, 3));
        esfera.addComponent(new QColisionEsfera(1.0f));
        motor.getEscena().addEntity(esfera);
        actualizarArbolEscena();
        seleccionarEntidad(esfera);
    }// GEN-LAST:event_mnuItemGeosferaActionPerformed

    private void cbxInterpolarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cbxInterpolarActionPerformed
        QGlobal.ANIMACION_INTERPOLAR = cbxInterpolar.isSelected();
    }// GEN-LAST:event_cbxInterpolarActionPerformed

    private void btnAnimIniciarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAnimIniciarActionPerformed
        motor.startAnimation();
    }// GEN-LAST:event_btnAnimIniciarActionPerformed

    private void btnAnimDetenerActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAnimDetenerActionPerformed
        motor.stopAnimation();
    }// GEN-LAST:event_btnAnimDetenerActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton7ActionPerformed
        if (renderer.entidadActiva != null) {
            renderer.entidadActiva.moveLeft(0.51f);
        }
    }// GEN-LAST:event_jButton7ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton6ActionPerformed
        if (renderer.entidadActiva != null) {
            renderer.entidadActiva.moveForward(-0.2f);
        }
    }// GEN-LAST:event_jButton6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton5ActionPerformed
        if (renderer.entidadActiva != null) {
            renderer.entidadActiva.moveForward(0.2f);
        }
    }// GEN-LAST:event_jButton5ActionPerformed

    private void btnAccion2AP(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton4ActionPerformed
        // if (ejemplo != null && !ejemplo.isEmpty()) {

        // for (GeneraEjemplo ejem : ejemplo) {
        // ejem.accion(2, renderer);
        // }
        // }
    }// GEN-LAST:event_jButton4ActionPerformed

    private void btnAccion1AP(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton3ActionPerformed
        // if (ejemplo != null && !ejemplo.isEmpty()) {
        // for (GeneraEjemplo ejem : ejemplo) {
        // ejem.accion(1, renderer);
        // }
        // }
    }// GEN-LAST:event_jButton3ActionPerformed

    private void btnStopPhysicsAP(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton2ActionPerformed
        motor.detenerFisica();
    }// GEN-LAST:event_jButton2ActionPerformed

    private void btnStartPhysicsAP(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed
        motor.iniciarFisica();
        // motor.iniciarFisica(2);
    }// GEN-LAST:event_jButton1ActionPerformed

    private void txtAnimTiempoInicioActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtAnimTiempoInicioActionPerformed
        motor.getAnimationEngine().setTiempoInicio(Float.valueOf(txtAnimTiempoInicio.getText()));
        sldLineaTiempo.setMinimum(Integer.parseInt(txtAnimTiempoInicio.getText()) * 10);
    }// GEN-LAST:event_txtAnimTiempoInicioActionPerformed

    private void txtAnimTiempoInicioKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtAnimTiempoInicioKeyReleased
        motor.getAnimationEngine().setTiempoInicio(Float.valueOf(txtAnimTiempoInicio.getText()));
        sldLineaTiempo.setMinimum(Integer.parseInt(txtAnimTiempoInicio.getText()) * 10);
    }// GEN-LAST:event_txtAnimTiempoInicioKeyReleased

    private void txtAnimTiempoFinActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtAnimTiempoFinActionPerformed
        motor.getAnimationEngine().setTiempoFin(Float.valueOf(txtAnimTiempoFin.getText()));
        sldLineaTiempo.setMaximum(Integer.parseInt(txtAnimTiempoFin.getText()) * 10);
    }// GEN-LAST:event_txtAnimTiempoFinActionPerformed

    private void txtAnimTiempoFinKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtAnimTiempoFinKeyReleased
        motor.getAnimationEngine().setTiempoFin(Float.valueOf(txtAnimTiempoFin.getText()));
        sldLineaTiempo.setMaximum(Integer.parseInt(txtAnimTiempoFin.getText()) * 10);
    }// GEN-LAST:event_txtAnimTiempoFinKeyReleased

    private void sldLineaTiempoStateChanged(javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_sldLineaTiempoStateChanged

        if (!cambiandoLineaTiempo) {
            motor.getAnimationEngine()
                    .setTiempo(((float) sldLineaTiempo.getValue() / (float) sldLineaTiempo.getMaximum())
                            * ((float) sldLineaTiempo.getMaximum() / 10.f));
            motor.getAnimationEngine().actualizarPoses(motor.getAnimationEngine().getTiempo());
        }
        txtAnimTiempo.setText("Tiempo:" + df.format(motor.getAnimationEngine().getTiempo()));
    }// GEN-LAST:event_sldLineaTiempoStateChanged

    private void btnAnimVelocidad1XActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAnimVelocidad1XActionPerformed
        motor.getAnimationEngine().setVelocidad(1.0f);
    }// GEN-LAST:event_btnAnimVelocidad1XActionPerformed

    private void btnAnimVelocidad075XActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAnimVelocidad075XActionPerformed
        motor.getAnimationEngine().setVelocidad(0.75f);
    }// GEN-LAST:event_btnAnimVelocidad075XActionPerformed

    private void btnANimVelocidad05XActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnANimVelocidad05XActionPerformed
        motor.getAnimationEngine().setVelocidad(0.5f);
    }// GEN-LAST:event_btnANimVelocidad05XActionPerformed

    private void btnAnimVelocidad025XActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAnimVelocidad025XActionPerformed
        motor.getAnimationEngine().setVelocidad(0.25f);
    }// GEN-LAST:event_btnAnimVelocidad025XActionPerformed

    private void btnAnimVelocidad15XActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAnimVelocidad15XActionPerformed
        motor.getAnimationEngine().setVelocidad(1.5f);
    }// GEN-LAST:event_btnAnimVelocidad15XActionPerformed

    private void btnAnimVelocidad2XActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAnimVelocidad2XActionPerformed
        motor.getAnimationEngine().setVelocidad(2.0f);
    }// GEN-LAST:event_btnAnimVelocidad2XActionPerformed

    private void btnAnimVelocidad4XActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAnimVelocidad4XActionPerformed
        motor.getAnimationEngine().setVelocidad(4.0f);
    }// GEN-LAST:event_btnAnimVelocidad4XActionPerformed

    private void btnAnimInvertirActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAnimInvertirActionPerformed
        motor.getAnimationEngine().invertir();
    }// GEN-LAST:event_btnAnimInvertirActionPerformed

    private void btnFullShaderActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnFullShaderActionPerformed
        renderer.cambiarShader(6);
    }// GEN-LAST:event_btnFullShaderActionPerformed

    private void btnShadowShaderActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnShadowShaderActionPerformed
        renderer.cambiarShader(5);
    }// GEN-LAST:event_btnShadowShaderActionPerformed

    private void btnIlumShaderActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnIlumShaderActionPerformed
        renderer.cambiarShader(4);
    }// GEN-LAST:event_btnIlumShaderActionPerformed

    private void btnTexturaShaderActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnTexturaShaderActionPerformed
        renderer.cambiarShader(3);
    }// GEN-LAST:event_btnTexturaShaderActionPerformed

    private void btnPhongShaderActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnPhongShaderActionPerformed
        renderer.cambiarShader(2);
    }// GEN-LAST:event_btnPhongShaderActionPerformed

    private void btnFlatShaderActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnFlatShaderActionPerformed
        renderer.cambiarShader(1);
    }// GEN-LAST:event_btnFlatShaderActionPerformed

    private void btnSimpleShaderActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnSimpleShaderActionPerformed
        renderer.cambiarShader(0);
    }// GEN-LAST:event_btnSimpleShaderActionPerformed

    private void btnPBRShaderActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnPBRShaderActionPerformed
        renderer.cambiarShader(7);
    }// GEN-LAST:event_btnPBRShaderActionPerformed

    private void chkVerGridActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_chkVerGridActionPerformed
        renderer.opciones.setDibujarGrid(chkVerGrid.isSelected());
    }// GEN-LAST:event_chkVerGridActionPerformed

    private void mnuItemTeteraActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuItemTeteraActionPerformed
        Entity item = new Entity("Teapot");
        Mesh malla = new QTeapot();
        item.addComponent(malla);
        item.addComponent(new QColisionMallaConvexa(malla));
        motor.getEscena().addEntity(item);
        actualizarArbolEscena();
        seleccionarEntidad(item);
    }// GEN-LAST:event_mnuItemTeteraActionPerformed

    private void mnuItemSusaneActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuItemSusaneActionPerformed
        Entity item = new Entity("Teapot");
        Mesh malla = new QSuzane();
        item.addComponent(malla);
        item.addComponent(new QColisionMallaConvexa(malla));
        motor.getEscena().addEntity(item);
        actualizarArbolEscena();
        seleccionarEntidad(item);
    }// GEN-LAST:event_mnuItemSusaneActionPerformed

    private void btnCalcularNormalesActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnCalcularNormalesActionPerformed
        for (Entity seleccionado : renderer.entidadesSeleccionadas) {
            for (EntityComponent compo : seleccionado.getComponents()) {
                if (compo instanceof Mesh) {
                    QUtilNormales.calcularNormales((Mesh) compo);
                }
            }
        }
    }// GEN-LAST:event_btnCalcularNormalesActionPerformed

    private void itmMenuEliminarRecursivoActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_itmMenuEliminarRecursivoActionPerformed
        // TODO add your handling code here:
        LinkedList<Entity> toRemove = new LinkedList<>();
        for (Entity object : renderer.entidadesSeleccionadas) {
            toRemove.add(object);
        }
        for (Entity object : toRemove) {
            // renderer.eliminarObjeto(object);
            motor.getEscena().removeEntityAndChilds(object);
        }

        actualizarArbolEscena();
    }// GEN-LAST:event_itmMenuEliminarRecursivoActionPerformed

    private void itmCrearNcoesferaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_itmCrearNcoesferaActionPerformed
        Entity esfera = new Entity("Nicoesfera");
        esfera.addComponent(new IcoSphere(1.0f, 3));
        esfera.addComponent(new QColisionEsfera(1.0f));
        motor.getEscena().addEntity(esfera);
        actualizarArbolEscena();
        seleccionarEntidad(esfera);
    }// GEN-LAST:event_itmCrearNcoesferaActionPerformed

    private void itmCrearcuboesferaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_itmCrearcuboesferaActionPerformed
        Entity esfera = new Entity("Cuboesfera");
        esfera.addComponent(new QCuboEsfera(1.0f, 3));
        esfera.addComponent(new QColisionEsfera(1.0f));
        motor.getEscena().addEntity(esfera);
        actualizarArbolEscena();
        seleccionarEntidad(esfera);
    }// GEN-LAST:event_itmCrearcuboesferaActionPerformed

    private void btnDividirCatmullActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnDividirCatmullActionPerformed
        for (Entity seleccionado : renderer.entidadesSeleccionadas) {
            for (EntityComponent compo : seleccionado.getComponents()) {
                if (compo instanceof Mesh) {
                    // QMallaUtil.subdividir((QGeometria) compo, 1);
                    ((Mesh) compo).dividirCatmullClark();
                }
            }
        }
    }// GEN-LAST:event_btnDividirCatmullActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton9ActionPerformed

        float radio = Float.parseFloat(txtInflarRadio.getText());
        for (Entity seleccionado : renderer.entidadesSeleccionadas) {
            for (EntityComponent compo : seleccionado.getComponents()) {
                if (compo instanceof Mesh) {
                    // QMallaUtil.subdividir((QGeometria) compo, 1);
                    ((Mesh) compo).inflar(radio);
                }
            }
        }
    }// GEN-LAST:event_jButton9ActionPerformed

    private void btnEliminarVerticesDuplicadosActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnEliminarVerticesDuplicadosActionPerformed
        for (Entity seleccionado : renderer.entidadesSeleccionadas) {
            for (EntityComponent compo : seleccionado.getComponents()) {
                if (compo instanceof Mesh) {
                    // QMallaUtil.subdividir((QGeometria) compo, 1);
                    ((Mesh) compo).eliminarVerticesDuplicados();
                }
            }
        } // TODO add your handling code here:
    }// GEN-LAST:event_btnEliminarVerticesDuplicadosActionPerformed

    void applyResolution() {
        renderer.opciones.setForzarResolucion(cbxForceRes.isSelected());
        renderer.opciones.setAncho((Integer) spnWidth.getValue());
        renderer.opciones.setAlto((Integer) spnHeight.getValue());
        renderer.resize();
    }

    private void refreshStats() {
        int vertexCount = 0;
        int faceCount = 0;
        for (Entity objeto : motor.getEscena().getEntities()) {
            if (objeto != null && objeto.isToRender()) {
                for (EntityComponent componente : objeto.getComponents()) {
                    if (componente instanceof Mesh) {
                        vertexCount += ((Mesh) componente).vertices.length;
                        faceCount += ((Mesh) componente).primitivas.length;
                    }
                }

            }
        }
        lblEstad.setText(vertexCount + " vértices; " + faceCount + " polígonos; "
                + motor.getEscena().getEntities().size() + " objetos");
    }

    public void actualizarArbolEscena() {
        // actualizo el arbol
        DefaultMutableTreeNode raiz = new DefaultMutableTreeNode(new QArbolWrapper("Escena", null));
        for (Entity entidad : motor.getEscena().getEntities()) {
            // solo agrego los que no tienen un padre
            if (entidad.getParent() == null) {
                raiz.add(generarArbolEntidad(entidad));
            }
        }
        treeEntidades.setModel(new DefaultTreeModel(raiz));
        refreshStats();
        if (PnlGestorRecursos.instancia != null) {
            PnlGestorRecursos.instancia.actualizarArbol();
        }
    }

    private DefaultMutableTreeNode generarArbolEntidad(Entity entidad) {
        DefaultMutableTreeNode nodo = new DefaultMutableTreeNode(new QArbolWrapper(entidad.getName(), entidad));
        // if (renderer.entidadesSeleccionadas.contains(entidad)) {
        // nodo.setSelected(true)
        // treeEntidades.addSelectionInterval(model.getSize() - 1, model.getSize() - 1);
        // }
        if (entidad.getComponents() != null) {
            for (EntityComponent compo : entidad.getComponents()) {
                // if (comp instanceof QEsqueleto) {
                // QEsqueleto esqueleto = (QEsqueleto) comp;
                // for (QHueso hueso : esqueleto.getHuesos()) {
                // nodo.add(generarArbolEntidad(hueso));
                // }
                // }
                // nodo.add(generarArbolEntidad(hijo));
            }
        }

        if (entidad.getChilds() != null) {
            for (Entity hijo : entidad.getChilds()) {
                nodo.add(generarArbolEntidad(hijo));
            }
        }

        // ahora agrego los componentes
        // for (QComponente comp : entidad.componentes) {
        //
        // }
        return nodo;
    }

    /**
     * Ejecutado al seleciconar una entidad
     */
    private void populateControls() {
        pnlEditorEntidad.editarEntidad(renderer.entidadActiva);
    }

    public class ArbolEntidadRenderer extends JLabel implements TreeCellRenderer {

        private DefaultTreeCellRenderer defaultRenderer = new DefaultTreeCellRenderer();

        private Color backgroundSelectionColor;

        private Color backgroundNonSelectionColor;

        public ArbolEntidadRenderer() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);

            backgroundSelectionColor = defaultRenderer.getBackgroundSelectionColor();
            backgroundNonSelectionColor = defaultRenderer.getBackgroundNonSelectionColor();
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
                boolean leaf, int row, boolean hasFocus) {
            try {
                // int selectedIndex = ((Integer) value).intValue();
                if (selected) {
                    setBackground(backgroundSelectionColor);
                    // setBackground(tree.getSelectionBackground());
                    // setForeground(tree.getSelectionForeground());
                } else {
                    setBackground(backgroundNonSelectionColor);
                    // setBackground(tree.getBackground());
                    // setForeground(tree.getForeground());
                }

                // setBackground(tree.getBackground());
                // setForeground(tree.getForeground());
                ImageIcon icon = Util.cargarIcono16("/cube_16.png");
                String texto = "";
                Object valor = ((DefaultMutableTreeNode) value).getUserObject();
                if (valor instanceof QArbolWrapper) {

                    QArbolWrapper wraper = (QArbolWrapper) valor;
                    // Set the icon and text. If icon was null, say so.

                    if (wraper.getObjeto() == null) {
                        icon = Util.cargarIcono16("/cube.png");
                    } else if (wraper.getObjeto() instanceof Camera) {
                        icon = Util.cargarIcono16("/camera.png");
                    } else if (wraper.getObjeto() instanceof Entity) {
                        icon = Util.cargarIcono16("/cube_16.png");
                    } else {
                        icon = Util.cargarIcono16("/teapot_16.png");
                    }
                    texto = wraper.getNombre();
                } else if (valor instanceof String) {
                    texto = (String) valor;
                } else {
                    texto = "N/A";
                }

                setIcon(icon);

                setText(texto);
                // setFont(list.getFont());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }
    }

    private void prepararInputListenerRenderer(RenderEngine renderer) {
        // creo los receptores para agregar al inputManager
        QInputManager.addMouseListener(new QMouseReceptor() {

            private Entity selectedObject;

            @Override
            public void mouseEntered(MouseEvent evt) {

                try {
                    renderer.getSuperficie().getComponente().requestFocus();
                } catch (Exception e) {
                }
            }

            @Override
            public void mousePressed(MouseEvent evt) {

                if (SwingUtilities.isLeftMouseButton(evt)) {
                    selectedObject = renderer.selectEntity(new QVector2(evt.getX(), evt.getY()));
                    if (selectedObject instanceof QGizmo // || selectedObject instanceof QGizmoParte
                    ) {
                        return;
                    }
                    renderer.entidadActiva = selectedObject;
                    if (renderer.entidadActiva == null) {
                        renderer.entidadesSeleccionadas.clear();
                        return;
                    }
                    if (!QInputManager.isShitf()) {
                        renderer.entidadesSeleccionadas.clear();
                    }
                    renderer.entidadesSeleccionadas.add(renderer.entidadActiva);
                    if (accionSeleccionar != null) {
                        accionSeleccionar.ejecutar(renderer.entidadActiva);
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent evt) {
                selectedObject = null;
            }

            @Override
            public void mouseDragged(MouseEvent evt) {

                if (SwingUtilities.isLeftMouseButton(evt)) {
                    // activo los Gizmos
                    if (selectedObject != null) {
                        if (selectedObject instanceof QGizmo) {
                            ((QGizmo) selectedObject).mouseMove(QInputManager.getDeltaX(), -QInputManager.getDeltaY());
                            // } else if (selectedObject instanceof QGizmoParte) {
                            // ((QGizmoParte) selectedObject).mouseMove(deltaX, -deltaY);
                        }
                    }
                }

                // if (SwingUtilities.isMiddleMouseButton(evt)) {
                //
                // if (QInputManager.isShitf() && QInputManager.isCtrl() &&
                // !QInputManager.isAlt()) {
                // //rota camara en su propio eje
                // renderer.getCamara().aumentarRotY((float)
                // Math.toRadians(-QInputManager.getDeltaX() / 2));
                // renderer.getCamara().aumentarRotX((float)
                // Math.toRadians(-QInputManager.getDeltaY() / 2));
                // } else if (QInputManager.isShitf() && !QInputManager.isCtrl() &&
                // !QInputManager.isAlt()) {
                // //mueve la camara
                // renderer.getCamara().moverDerechaIzquierda(-QInputManager.getDeltaX() /
                // 100.0f);
                // renderer.getCamara().moverArribaAbajo(QInputManager.getDeltaY() / 100.0f);
                // }
                // }
                QInputManager.warpMouse(evt.getXOnScreen(), evt.getYOnScreen());
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent evt) {

                // if (evt.getWheelRotation() < 0) {
                // if (!QInputManager.isShitf()) {
                // renderer.getCamara().moverAdelanteAtras(0.2f);
                // } else {
                // renderer.getCamara().moverAdelanteAtras(1f);
                // }
                // } else {
                // if (!QInputManager.isShitf()) {
                // renderer.getCamara().moverAdelanteAtras(-0.2f);
                // } else {
                // renderer.getCamara().moverAdelanteAtras(-1f);
                // }
                // }
            }

            @Override
            public void mouseMoved(MouseEvent evt) {

            }

            @Override
            public void destruir() {

            }
        });

        QInputManager.addKeyboardListener(new QTecladoReceptor() {
            @Override
            public void keyPressed(KeyEvent evt) {

                switch (evt.getKeyCode()) {

                    case KeyEvent.VK_DECIMAL: {
                        try {
                            QCamaraControl control = QUtilComponentes.getCamaraControl(renderer.getCamara());
                            if (control != null) {
                                control.getTarget().set(renderer.entidadActiva.getTransformacion().getTraslacion());
                                control.updateCamera();
                            } else {
                                QCamaraOrbitar control2 = QUtilComponentes.getCamaraOrbitar(renderer.getCamara());
                                if (control2 != null) {
                                    control2.getTarget()
                                            .set(renderer.entidadActiva.getTransformacion().getTraslacion());
                                    control2.updateCamera();
                                }
                            }

                        } catch (Exception e) {

                        }
                        break;
                    }
                    case KeyEvent.VK_Y:
                        renderer.opciones.setTipoVista(QOpcionesRenderer.VISTA_WIRE);
                        break;
                    case KeyEvent.VK_U:
                        renderer.opciones.setTipoVista(QOpcionesRenderer.VISTA_FLAT);
                        break;
                    case KeyEvent.VK_I:
                        renderer.opciones.setTipoVista(QOpcionesRenderer.VISTA_PHONG);
                        break;
                    case KeyEvent.VK_T:
                        renderer.setShowStats(!renderer.isShowStats());
                        break;
                    case KeyEvent.VK_O:
                        renderer.opciones.setMaterial(!renderer.opciones.isMaterial());
                        break;
                    // case KeyEvent.VK_M:
                    // opciones.setShowNormal(!opciones.isShowNormal());
                    // break;
                    case KeyEvent.VK_B:
                        renderer.opciones.setDibujarCarasTraseras(!renderer.opciones.isDibujarCarasTraseras());
                        break;
                    case KeyEvent.VK_N:
                        renderer.opciones.setNormalMapping(!renderer.opciones.isNormalMapping());
                        break;
                    case KeyEvent.VK_L:
                        renderer.opciones.setDibujarLuces(!renderer.opciones.isDibujarLuces());
                        break;
                    case KeyEvent.VK_P:
                        renderer.opciones.setSombras(!renderer.opciones.isSombras());
                        break;
                    case KeyEvent.VK_1:
                        renderer.setTipoGizmoActual(GIZMO_NINGUNO);
                        break;
                    case KeyEvent.VK_2:
                        renderer.setTipoGizmoActual(GIZMO_TRASLACION);
                        break;
                    case KeyEvent.VK_3:
                        renderer.setTipoGizmoActual(GIZMO_ROTACION);
                        break;
                    case KeyEvent.VK_4:
                        renderer.setTipoGizmoActual(GIZMO_ESCALA);
                        break;

                }
            }

            @Override
            public void keyReleased(KeyEvent evt) {

            }
        });
    }

    public Scene getEscena() {
        return escena;
    }

    public void setEscena(Scene escena) {
        this.escena = escena;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar barraMenu;
    private javax.swing.JProgressBar barraProgreso;
    private javax.swing.JButton btnANimVelocidad05X;
    private javax.swing.JButton btnActualizarReflejos;
    private javax.swing.JButton btnActualizarSombras;
    private javax.swing.JButton btnAnimDetener;
    private javax.swing.JButton btnAnimIniciar;
    private javax.swing.JButton btnAnimInvertir;
    private javax.swing.JButton btnAnimVelocidad025X;
    private javax.swing.JButton btnAnimVelocidad075X;
    private javax.swing.JButton btnAnimVelocidad15X;
    private javax.swing.JButton btnAnimVelocidad1X;
    private javax.swing.JButton btnAnimVelocidad2X;
    private javax.swing.JButton btnAnimVelocidad4X;
    private javax.swing.JButton btnCalcularNormales;
    private javax.swing.JButton btnCentroGeometria;
    private javax.swing.JButton btnDividir;
    private javax.swing.JButton btnDividirCatmull;
    private javax.swing.JButton btnEliminarVerticesDuplicados;
    private javax.swing.JButton btnFlatShader;
    private javax.swing.JButton btnFullShader;
    private javax.swing.JButton btnGuadarScreenShot;
    private javax.swing.JButton btnIlumShader;
    private javax.swing.JButton btnInvertirNormales;
    private javax.swing.JButton btnNoSuavizar;
    private javax.swing.JButton btnPBRShader;
    private javax.swing.JButton btnPhongShader;
    private javax.swing.JButton btnRaster1;
    private javax.swing.JButton btnRaster2;
    private javax.swing.JButton btnShadowShader;
    private javax.swing.JButton btnSimpleShader;
    private javax.swing.JButton btnSuavizar;
    private javax.swing.JButton btnTexturaShader;
    private javax.swing.JButton btnTipoAlambre;
    private javax.swing.JButton btnTipoSolido;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.JCheckBox cbxForceRes;
    private javax.swing.JCheckBox cbxForceSmooth;
    private javax.swing.JCheckBox cbxInterpolar;
    private javax.swing.JCheckBox cbxNormalMapping;
    private javax.swing.JCheckBox cbxShowBackFaces;
    private javax.swing.JCheckBox cbxShowLight;
    private javax.swing.JCheckBox cbxZSort;
    private javax.swing.JCheckBox chkNeblina;
    private javax.swing.JCheckBox chkVerGrid;
    private javax.swing.ButtonGroup groupOptVista;
    private javax.swing.ButtonGroup groupTipoSuperficie;
    private javax.swing.JMenuItem itmAddCamara;
    private javax.swing.JMenuItem itmAgregarRender;
    private javax.swing.JMenuItem itmAgregarVista;
    private javax.swing.JMenuItem itmCopiar;
    private javax.swing.JMenuItem itmCrearCaja;
    private javax.swing.JMenuItem itmCrearNcoesfera;
    private javax.swing.JMenuItem itmCrearcuboesfera;
    private javax.swing.JMenuItem itmEliminar;
    private javax.swing.JMenuItem itmMapaAltura;
    private javax.swing.JMenuItem itmMenuEliminarRecursivo;
    private javax.swing.JMenuItem itmPegar;
    private javax.swing.JMenuItem itmSeleccionarTodo;
    private javax.swing.JButton btnStartPhysics;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton btnStopPhysics;
    private javax.swing.JButton btnAccion1;
    private javax.swing.JButton btnAccion2;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu mnuFile;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem itmMenuOpen;
    private javax.swing.JMenuItem itmMenuExportentity;
    private javax.swing.JMenuItem itmMenuImport;
    private javax.swing.JMenuItem jMenuItem24;
    private javax.swing.JMenuItem itmMenuSave;
    private javax.swing.JMenuItem jMenuItem26;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JPopupMenu.Separator jSeparator8;
    private javax.swing.JLabel lblEstad;
    private javax.swing.JLabel lblVelocidad;
    private javax.swing.JMenuItem mnuEspiral;
    private javax.swing.JMenuItem mnuItemGeosfera;
    private javax.swing.JMenuItem mnuItemPrisma;
    private javax.swing.JMenuItem mnuItemSusane;
    private javax.swing.JMenuItem mnuItemTetera;
    private javax.swing.JMenuItem mnuLuzConica;
    private javax.swing.JMenuItem mnuLuzDireccional;
    private javax.swing.JMenuItem mnuLuzPuntual;
    private javax.swing.JTabbedPane panelHerramientas;
    private javax.swing.JPanel panelRenderes;
    private javax.swing.JPanel pnlColorFondo;
    private javax.swing.JPanel pnlEscenario1;
    private javax.swing.JPanel pnlHerramientas;
    private javax.swing.JPanel pnlLineaTiempo;
    private javax.swing.JPanel pnlMotores;
    private javax.swing.JPanel pnlNeblinaColor;
    private javax.swing.JPanel pnlProcesadores;
    private javax.swing.JScrollPane scrollHeramientas;
    private javax.swing.JScrollPane scrollOpciones;
    private javax.swing.JSlider sldLineaTiempo;
    private javax.swing.JSplitPane spliDerecha;
    private javax.swing.JSplitPane splitIzquierda;
    private javax.swing.JSplitPane splitPanel;
    private javax.swing.JSpinner spnHeight;
    private javax.swing.JSpinner spnNeblinaDensidad;
    private javax.swing.JSpinner spnWidth;
    private javax.swing.JTree treeEntidades;
    private javax.swing.JLabel txtAnimTiempo;
    private javax.swing.JTextField txtAnimTiempoFin;
    private javax.swing.JTextField txtAnimTiempoInicio;
    private javax.swing.JTextField txtInflarRadio;
    // End of variables declaration//GEN-END:variables
}
