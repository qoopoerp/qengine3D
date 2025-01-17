package net.qoopo.engine3d.editor;

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
import javax.swing.JMenu;
import javax.swing.JMenuItem;
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

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.assets.model.ModelLoader;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.entity.component.camera.CameraController;
import net.qoopo.engine.core.entity.component.camera.CameraOrbiter;
import net.qoopo.engine.core.entity.component.gui.QMouseReceptor;
import net.qoopo.engine.core.entity.component.gui.QTecladoReceptor;
import net.qoopo.engine.core.entity.component.ligth.QDirectionalLigth;
import net.qoopo.engine.core.entity.component.ligth.QPointLigth;
import net.qoopo.engine.core.entity.component.ligth.QSpotLigth;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Box;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Cone;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Cylinder;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.CylinderX;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.CylinderZ;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.GeoSphere;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.IcoSphere;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.PlanarMesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Plane;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Prism;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Sphere;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.SphereBox;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Suzane;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Teapot;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Torus;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.Triangle;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.wire.QEspiral;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.mallas.QColisionMallaConvexa;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.QColisionCaja;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.QColisionCilindro;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.QColisionCilindroX;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.QColisionCono;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.QColisionEsfera;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.QColisionTriangulo;
import net.qoopo.engine.core.input.QInputManager;
import net.qoopo.engine.core.lwjgl.renderer.OpenGlRenderer;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QVector2;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.renderer.RenderOptions;
import net.qoopo.engine.core.renderer.post.FilterTexture;
import net.qoopo.engine.core.renderer.post.filters.antialiasing.MsaaFilter;
import net.qoopo.engine.core.renderer.post.filters.blur.BlurFilter;
import net.qoopo.engine.core.renderer.post.filters.blur.DepthOfFieldFilter;
import net.qoopo.engine.core.renderer.post.filters.color.BloomFilter;
import net.qoopo.engine.core.renderer.post.filters.color.CelShadeFilter;
import net.qoopo.engine.core.renderer.post.filters.color.GammaFixFilter;
import net.qoopo.engine.core.renderer.post.filters.color.HightContrastFilter;
import net.qoopo.engine.core.renderer.superficie.QJPanel;
import net.qoopo.engine.core.renderer.superficie.Superficie;
import net.qoopo.engine.core.scene.Camera;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.util.ComponentUtil;
import net.qoopo.engine.core.util.QGlobal;
import net.qoopo.engine.java3d.renderer.Java3DRenderer;
import net.qoopo.engine.renderer.SoftwareRenderer;
import net.qoopo.engine.renderer.shader.fragment.basico.StandardFragmentShader;
import net.qoopo.engine.renderer.shader.fragment.basico.parciales.FlatFragmentShader;
import net.qoopo.engine.renderer.shader.fragment.basico.parciales.OnlyColorFragmentShader;
import net.qoopo.engine.renderer.shader.fragment.basico.parciales.PhongFramentShader;
import net.qoopo.engine.renderer.shader.fragment.pbr.BRDFFragmentShader;
import net.qoopo.engine.terrain.HeightMapTerrain;
import net.qoopo.engine3d.QEngine3D;
import net.qoopo.engine3d.core.asset.model.DefaultModelLoader;
import net.qoopo.engine3d.core.input.control.gizmo.Gizmo;
import net.qoopo.engine3d.core.util.Accion;
import net.qoopo.engine3d.core.util.QDefinirCentro;
import net.qoopo.engine3d.core.util.SerializarUtil;
import net.qoopo.engine3d.editor.assets.PnlGestorRecursos;
import net.qoopo.engine3d.editor.entity.EditorEntidad;
import net.qoopo.engine3d.editor.util.EditorRenderer;
import net.qoopo.engine3d.editor.util.ImagePreviewPanel;
import net.qoopo.engine3d.editor.util.QArbolWrapper;
import net.qoopo.engine3d.editor.util.Util;
import net.qoopo.engine3d.test.InitialScene;

@Getter
@Setter
public class Principal extends javax.swing.JFrame {

    public static Principal instancia;
    // el motor que va a renderizar en el modo de dise;o
    private QEngine3D engine;

    private List<EditorRenderer> listaEditorRenderer = new ArrayList<>();
    private RenderEngine renderer; // renderer seleccionado
    private EditorRenderer editorRenderer;

    boolean objectLock = true;
    boolean objectListLock = false;
    private LinkedList<Entity> clipboard = new LinkedList<>();
    private JFileChooser chooser = new JFileChooser();
    private ImagePreviewPanel preview = new ImagePreviewPanel();
    private EditorEntidad pnlEditorEntidad = new EditorEntidad();
    private PnlGestorRecursos pnlGestorRecursos = new PnlGestorRecursos();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
    private Accion accionSeleccionar;
    private Accion actionUpdateEditor;
    private boolean cambiandoLineaTiempo = false;
    protected static final DecimalFormat df = new DecimalFormat("0.00");

    private Scene escena;

    private FilterTexture gammaFixFilter = new GammaFixFilter();

    public Principal() {
        // configura las acciones para interactuar desde el renderar hacia afuera
        accionSeleccionar = new Accion() {
            @Override
            public void run(Object... parametros) {
                try {
                    seleccionarEntidad((Entity) parametros[0]);
                } catch (Exception e) {

                }
            }
        };
        actionUpdateEditor = new Accion() {
            @Override
            public void run(Object... parametros) {
                try {
                    if (engine.getAnimationEngine() != null && engine.getAnimationEngine().isEjecutando()) {
                        cambiandoLineaTiempo = true;
                        sldLineaTiempo.setValue((int) (engine.getAnimationEngine().getTiempo() * 10));
                        cambiandoLineaTiempo = false;
                    }
                    if (editorRenderer != null)
                        editorRenderer.render();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        instancia = this;
        initComponents();
        chooser.setCurrentDirectory(new File("assets/"));
        engine = new QEngine3D();
        this.escena = engine.getScene();
        engine.getCustomActions().add(actionUpdateEditor);
        engine.getScene().setAmbientColor(new QColor(50.0f / 255.0f, 50.0f / 255.0f, 50.0f / 255.0f));
        pnlColorFondo.setBackground(engine.getScene().getAmbientColor().getColor());

        engine.setIniciarAudio(false);
        engine.setIniciarDiaNoche(false);
        engine.setIniciarFisica(false);
        engine.setIniciarInteligencia(false);
        engine.setIniciarAnimaciones(false);
        agregarRenderer("Main", QVector3.of(0, 5, 5), QVector3.of(0, 0, 0), RenderEngine.RENDER_INTERNO);
        renderer.opciones.setDibujarLuces(true);
        // motor.setRenderEngine(renderer);
        engine.start();
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
                        editorRenderer.entidadesSeleccionadas.clear();
                    }
                    if (nodo.getObjeto() instanceof Entity) {
                        for (EditorRenderer _editorRenderer : listaEditorRenderer) {
                            _editorRenderer.entidadActiva = (Entity) nodo.getObjeto();
                            editorRenderer.entidadesSeleccionadas.add(_editorRenderer.entidadActiva);
                        }
                    }

                    pnlEditorEntidad.liberar();
                    populateControls();
                    pnlOpciones.repaint();
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
        initialScene.make(engine.getScene());
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
        engine.setModificando(true);

        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }

        QJPanel panelDibujo = new QJPanel();

        int tam = engine.getRendererList().size();
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

        for (RenderEngine render : engine.getRendererList()) {
            panelRenderes.add(render.getSuperficie().getComponente());
        }

        panelRenderes.add(panelDibujo);

        RenderEngine nuevoRenderer;
        switch (tipoRenderer) {
            case RenderEngine.RENDER_JAVA3D:
                nuevoRenderer = new Java3DRenderer(engine.getScene(), nombre, new Superficie(panelDibujo), 800, 600);
                break;
            case RenderEngine.RENDER_OPENGL:
                nuevoRenderer = new OpenGlRenderer(engine.getScene(), nombre, new Superficie(panelDibujo), 800, 600);
                break;
            case RenderEngine.RENDER_INTERNO:
            default:
                nuevoRenderer = new SoftwareRenderer(engine.getScene(), nombre, new Superficie(panelDibujo), 800, 600);
                break;
        }
        // nuevoRenderer.opciones.setRenderArtefactos(true);
        EditorRenderer nuevoEditorRenderer = new EditorRenderer(nuevoRenderer);
        panelDibujo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                setRenderer(nuevoRenderer);
                setEditorRenderer(nuevoEditorRenderer);
                actualizarBordeSeleccionado();
                // nuevoRenderer.getSuperficie().getComponente().setBorder(new
                // LineBorder(Color.RED, 10));
            }
            // public void mouseReleased(java.awt.event.MouseEvent evt) {
            //// rendererMouseReleased(evt);
            // }
        });

        engine.getScene().addEntity(camara);
        nuevoRenderer.setCamera(camara);// setea la camara inicial creada
        // nuevoRenderer.setAccionSeleccionar(accionSeleccionar);

        // if (QGlobal.ENABLE_GAMMA_FIX)
        //     nuevoRenderer.addFilter(gammaFixFilter);

        for (RenderEngine render : engine.getRendererList()) {
            render.resize();
        }

        engine.getRendererList().add(nuevoRenderer);
        listaEditorRenderer.add(nuevoEditorRenderer);

        setRenderer(nuevoRenderer);
        this.editorRenderer = nuevoEditorRenderer;

        camara.addComponent(new CameraController(camara));
        // camara.agregarComponente(new QCamaraOrbitar(camara));
        // camara.agregarComponente(new QCamaraPrimeraPersona(camara));
        prepararInputListenerRenderer(nuevoRenderer);

        // configura el shader como pbr
        // nuevoRenderer.setShader(new PBRFragmentShader(renderer));
        engine.setModificando(false);
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
        for (EditorRenderer _editorRenderer : listaEditorRenderer) {
            if (!QInputManager.isShitf()) {
                _editorRenderer.entidadesSeleccionadas.clear();
            }
            _editorRenderer.entidadActiva = entidad;
            _editorRenderer.entidadesSeleccionadas.add(entidad);
        }
        pnlEditorEntidad.liberar();
        populateControls();
        pnlOpciones.repaint();
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
        pnlOpciones = new javax.swing.JPanel();
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
        lblOpcionesRender = new javax.swing.JLabel();
        pnlLuzAmbiente = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        pnlColorFondo = new javax.swing.JPanel();
        pnlNeblina = new javax.swing.JPanel();
        chkNeblina = new javax.swing.JCheckBox();
        jLabel9 = new javax.swing.JLabel();
        spnNeblinaDensidad = new javax.swing.JSpinner();
        jLabel10 = new javax.swing.JLabel();
        pnlNeblinaColor = new javax.swing.JPanel();
        scrollHeramientas = new javax.swing.JScrollPane();
        pnlHerramientas = new javax.swing.JPanel();
        lblHerramientasEntidad = new javax.swing.JLabel();
        lblSombreado = new javax.swing.JLabel();
        lblToolEntidadNormales = new javax.swing.JLabel();
        btnInvertirNormales = new javax.swing.JButton();
        lblToolEntidadTipo = new javax.swing.JLabel();
        lblToolDefinirCentro = new javax.swing.JLabel();
        btnCentroGeometria = new javax.swing.JButton();
        btnActualizarReflejos = new javax.swing.JButton();
        lblToolsMapas = new javax.swing.JLabel();
        btnActualizarSombras = new javax.swing.JButton();
        lblToolsGeneral = new javax.swing.JLabel();
        btnGuadarScreenShot = new javax.swing.JButton();
        btnSuavizar = new javax.swing.JButton();
        btnNoSuavizar = new javax.swing.JButton();
        btnTipoSolido = new javax.swing.JButton();
        btnTipoAlambre = new javax.swing.JButton();
        lblToolsGeometria = new javax.swing.JLabel();
        btnDividir = new javax.swing.JButton();
        btnCalcularNormales = new javax.swing.JButton();
        btnDividirCatmull = new javax.swing.JButton();
        btnInflar = new javax.swing.JButton();
        lblToolInflar = new javax.swing.JLabel();
        txtInflarRadio = new javax.swing.JTextField();
        btnEliminarVerticesDuplicados = new javax.swing.JButton();
        pnlProcesadores = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        btnClearFilters = new javax.swing.JButton();
        btnBlooomFilter = new javax.swing.JButton();
        btnHightContrastFilter = new javax.swing.JButton();
        btnBlurFilter = new javax.swing.JButton();
        btnDOFfilter1 = new javax.swing.JButton();
        btnDOFfilter2 = new javax.swing.JButton();
        btnDOFfilter3 = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JSeparator();
        btnCelFilter = new javax.swing.JButton();
        btnMsaaFilter = new javax.swing.JButton();
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
        mnuRenderizadores = new javax.swing.JMenu();
        mnutipoVista = new javax.swing.JMenu();
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
        itmToro = new javax.swing.JMenuItem();
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

        lblOpcionesRender.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        lblOpcionesRender.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblOpcionesRender.setText("Opciones Render");
        lblOpcionesRender.setOpaque(true);

        pnlLuzAmbiente.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Luz Ambiente",
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

        javax.swing.GroupLayout pnlLuzAmbienteLayout = new javax.swing.GroupLayout(pnlLuzAmbiente);
        pnlLuzAmbiente.setLayout(pnlLuzAmbienteLayout);
        pnlLuzAmbienteLayout.setHorizontalGroup(
                pnlLuzAmbienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlLuzAmbienteLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(pnlColorFondo, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(223, Short.MAX_VALUE)));
        pnlLuzAmbienteLayout.setVerticalGroup(
                pnlLuzAmbienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlLuzAmbienteLayout.createSequentialGroup()
                                .addGroup(pnlLuzAmbienteLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(pnlColorFondo, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                pnlLuzAmbienteLayout.createSequentialGroup()
                                                        .addComponent(jLabel1)
                                                        .addGap(4, 4, 4)))
                                .addGap(0, 0, Short.MAX_VALUE)));

        pnlNeblina.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Neblina",
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

        javax.swing.GroupLayout pnlNeblinaLayout = new javax.swing.GroupLayout(pnlNeblina);
        pnlNeblina.setLayout(pnlNeblinaLayout);
        pnlNeblinaLayout.setHorizontalGroup(
                pnlNeblinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlNeblinaLayout.createSequentialGroup()
                                .addGroup(
                                        pnlNeblinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel9)
                                                .addComponent(chkNeblina))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlNeblinaLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(spnNeblinaDensidad, javax.swing.GroupLayout.PREFERRED_SIZE, 113,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(pnlNeblinaLayout.createSequentialGroup()
                                                .addComponent(jLabel10)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(pnlNeblinaColor, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)));
        pnlNeblinaLayout.setVerticalGroup(
                pnlNeblinaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlNeblinaLayout.createSequentialGroup()
                                .addGroup(pnlNeblinaLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(pnlNeblinaColor, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(chkNeblina, javax.swing.GroupLayout.PREFERRED_SIZE, 0,
                                                Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlNeblinaLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(spnNeblinaDensidad, javax.swing.GroupLayout.Alignment.TRAILING,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel9))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        javax.swing.GroupLayout pnlOpcionesLayout = new javax.swing.GroupLayout(pnlOpciones);
        pnlOpciones.setLayout(pnlOpcionesLayout);
        pnlOpcionesLayout.setHorizontalGroup(
                pnlOpcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlOpcionesLayout.createSequentialGroup()
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 89, Short.MAX_VALUE))
                        .addGroup(pnlOpcionesLayout.createSequentialGroup()
                                .addGroup(pnlOpcionesLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pnlOpcionesLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(pnlOpcionesLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(pnlLuzAmbiente,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(pnlNeblina, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                // .addGroup(jPanel2Layout.createSequentialGroup()
                                                // .addGroup(
                                                // jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                // .addComponent(lblRasterizador)
                                                // .addComponent(lblShader)
                                                // .addGroup(jPanel2Layout.createSequentialGroup()
                                                // .addComponent(btnRaster1)
                                                // .addPreferredGap(
                                                // javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                // .addComponent(btnRaster2)
                                                // .addPreferredGap(
                                                // javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                // .addComponent(btnRaster3))
                                                // .addGroup(jPanel2Layout.createSequentialGroup()
                                                // .addGroup(jPanel2Layout
                                                // .createParallelGroup(
                                                // javax.swing.GroupLayout.Alignment.TRAILING,
                                                // false)
                                                // .addComponent(btnFlatShader,
                                                // javax.swing.GroupLayout.DEFAULT_SIZE,
                                                // javax.swing.GroupLayout.DEFAULT_SIZE,
                                                // Short.MAX_VALUE)
                                                // .addGroup(
                                                // javax.swing.GroupLayout.Alignment.LEADING,
                                                // jPanel2Layout
                                                // .createSequentialGroup()
                                                // .addGap(1, 1, 1)
                                                // .addComponent(
                                                // btnShadowShader,
                                                // javax.swing.GroupLayout.DEFAULT_SIZE,
                                                // javax.swing.GroupLayout.DEFAULT_SIZE,
                                                // Short.MAX_VALUE)
                                                // )
                                                // .addComponent(btnFullShader,
                                                // javax.swing.GroupLayout.DEFAULT_SIZE,
                                                // 98, Short.MAX_VALUE)
                                                // .addComponent(btnTexturaShader,
                                                // javax.swing.GroupLayout.DEFAULT_SIZE,
                                                // javax.swing.GroupLayout.DEFAULT_SIZE,
                                                // Short.MAX_VALUE)
                                                // )
                                                // .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                // .addGroup(jPanel2Layout
                                                // .createParallelGroup(
                                                // javax.swing.GroupLayout.Alignment.LEADING,
                                                // false)
                                                // .addComponent(btnPhongShader,
                                                // javax.swing.GroupLayout.DEFAULT_SIZE,
                                                // javax.swing.GroupLayout.DEFAULT_SIZE,
                                                // Short.MAX_VALUE)
                                                // .addComponent(btnIlumShader)
                                                // .addComponent(btnPBRShader,
                                                // javax.swing.GroupLayout.PREFERRED_SIZE,
                                                // 98,
                                                // javax.swing.GroupLayout.PREFERRED_SIZE)
                                                // .addComponent(btnSimpleShader,
                                                // javax.swing.GroupLayout.DEFAULT_SIZE,
                                                // 102, Short.MAX_VALUE)
                                                // )
                                                // )
                                                // )
                                                // .addGap(0, 0, Short.MAX_VALUE)

                                                // )
                                                ))
                                        .addComponent(lblOpcionesRender, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap()));
        pnlOpcionesLayout.setVerticalGroup(
                pnlOpcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlOpcionesLayout.createSequentialGroup()
                                .addComponent(lblOpcionesRender)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pnlLuzAmbiente, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pnlNeblina, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                // .addComponent(lblRasterizador)
                                // .addGap(5, 5, 5)
                                // .addGroup(pnlOpcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                // .addComponent(btnRaster1)
                                // .addComponent(btnRaster2)
                                // .addComponent(btnRaster3))
                                // .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                // .addComponent(lblShader)
                                // .addGap(18, 18, 18)
                                // .addGroup(pnlOpcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                // .addComponent(btnFullShader)
                                // .addComponent(btnPBRShader))
                                // .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                // .addGroup(pnlOpcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                // .addComponent(btnIlumShader)
                                // .addComponent(btnShadowShader))
                                // .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                // .addGroup(pnlOpcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                // .addComponent(btnPhongShader)
                                // .addComponent(btnTexturaShader))
                                // .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                // .addGroup(pnlOpcionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                // .addComponent(btnSimpleShader)
                                // .addComponent(btnFlatShader))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        scrollOpciones.setViewportView(pnlOpciones);

        panelHerramientas.addTab("Opciones", scrollOpciones);

        lblHerramientasEntidad.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        lblHerramientasEntidad.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHerramientasEntidad.setText("Entidad");
        lblHerramientasEntidad.setFocusable(false);
        lblHerramientasEntidad.setOpaque(true);

        lblSombreado.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        lblSombreado.setText("Sombreado:");

        lblToolEntidadNormales.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        lblToolEntidadNormales.setText("Normales:");

        btnInvertirNormales.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        btnInvertirNormales.setText("Invertir");
        btnInvertirNormales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInvertirNormalesActionPerformed(evt);
            }
        });

        lblToolEntidadTipo.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        lblToolEntidadTipo.setText("Tipo:");

        lblToolDefinirCentro.setText("Definir Centro:");

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

        lblToolsMapas.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        lblToolsMapas.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblToolsMapas.setText("Mapas");
        lblToolsMapas.setFocusable(false);
        lblToolsMapas.setOpaque(true);

        btnActualizarSombras.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        btnActualizarSombras.setText("Actualizar sombras");
        btnActualizarSombras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarSombrasActionPerformed(evt);
            }
        });

        lblToolsGeneral.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        lblToolsGeneral.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblToolsGeneral.setText("General");
        lblToolsGeneral.setFocusable(false);
        lblToolsGeneral.setOpaque(true);

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

        lblToolsGeometria.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        lblToolsGeometria.setText("Geometría:");

        btnDividir.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        btnDividir.setText("Dividir");
        btnDividir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDividirActionPerformed(evt);
            }
        });

        btnCalcularNormales.setText("Calcular");
        btnCalcularNormales.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        btnCalcularNormales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcularNormalesActionPerformed(evt);
            }
        });

        btnDividirCatmull.setText("Dividir Catmull-Clark");
        btnDividirCatmull.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        btnDividirCatmull.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDividirCatmullActionPerformed(evt);
            }
        });

        btnInflar.setText("Inflar");
        btnInflar.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        btnInflar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                // jButton9ActionPerformed(evt);
            }
        });

        lblToolInflar.setText("Inflar:");

        txtInflarRadio.setText("1");

        btnEliminarVerticesDuplicados.setText("Eliminar Duplicados");
        btnEliminarVerticesDuplicados.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
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
                        .addComponent(lblHerramientasEntidad, javax.swing.GroupLayout.Alignment.TRAILING,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE)
                        .addComponent(btnCentroGeometria, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblToolsMapas, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnActualizarReflejos, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnActualizarSombras, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblToolsGeneral, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnGuadarScreenShot, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pnlHerramientasLayout.createSequentialGroup()
                                .addGroup(pnlHerramientasLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pnlHerramientasLayout.createSequentialGroup()
                                                .addGroup(pnlHerramientasLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(lblSombreado)
                                                        .addComponent(lblToolEntidadNormales)
                                                        .addComponent(lblToolEntidadTipo))
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
                                        .addComponent(lblToolDefinirCentro, javax.swing.GroupLayout.PREFERRED_SIZE, 261,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(pnlHerramientasLayout.createSequentialGroup()
                                                .addGroup(pnlHerramientasLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(lblToolsGeometria)
                                                        .addComponent(lblToolInflar))
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
                                                                        .addComponent(btnInflar,
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
                                .addComponent(lblHerramientasEntidad)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(pnlHerramientasLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblSombreado)
                                        .addGroup(pnlHerramientasLayout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(btnSuavizar)
                                                .addComponent(btnNoSuavizar)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlHerramientasLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblToolEntidadNormales)
                                        .addComponent(btnInvertirNormales)
                                        .addComponent(btnCalcularNormales))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlHerramientasLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblToolEntidadTipo)
                                        .addGroup(pnlHerramientasLayout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(btnTipoSolido)
                                                .addComponent(btnTipoAlambre)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblToolDefinirCentro)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCentroGeometria)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(pnlHerramientasLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblToolsGeometria)
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
                                                                .addComponent(lblToolInflar))
                                                        .addComponent(txtInflarRadio,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(lblToolsMapas))
                                        .addComponent(btnInflar))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnActualizarReflejos)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnActualizarSombras)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblToolsGeneral)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnGuadarScreenShot)
                                .addGap(0, 91, Short.MAX_VALUE)));

        scrollHeramientas.setViewportView(pnlHerramientas);

        panelHerramientas.addTab("Herram.", scrollHeramientas);

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N
        jLabel3.setText("PROCESADORES POST RENDERIZADO");

        btnClearFilters.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        btnClearFilters.setText("Quitar");
        btnClearFilters.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearFiltersAP(evt);
            }
        });

        btnBlooomFilter.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        btnBlooomFilter.setText("Bloom");
        btnBlooomFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBlooomFilterAP(evt);
            }
        });

        btnHightContrastFilter.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        btnHightContrastFilter.setText("Contraste");
        btnHightContrastFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHightContrastFilterAP(evt);
            }
        });

        btnBlurFilter.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        btnBlurFilter.setText("Blur");
        btnBlurFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBlurFilterAP(evt);
            }
        });

        btnDOFfilter1.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        btnDOFfilter1.setText("DOF 1");
        btnDOFfilter1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDOFfilter1AP(evt);
            }
        });

        btnDOFfilter2.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        btnDOFfilter2.setText("DOF 2");
        btnDOFfilter2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDOFfilter2AP(evt);
            }
        });

        btnDOFfilter3.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        btnDOFfilter3.setText("DOF 3");
        btnDOFfilter3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDOFfilter3AP(evt);
            }
        });

        btnCelFilter.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        btnCelFilter.setText("Cel");
        btnCelFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCelFilterAP(evt);
            }
        });

        btnMsaaFilter.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        btnMsaaFilter.setText("MSAA");
        btnMsaaFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMsaaFilterAP(evt);
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
                                                        .addComponent(btnBlooomFilter,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(btnDOFfilter1,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(btnClearFilters,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(btnMsaaFilter,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(pnlProcesadoresLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                false)
                                                        .addComponent(btnCelFilter,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(btnDOFfilter2,
                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(btnHightContrastFilter,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(pnlProcesadoresLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                false)
                                                        .addComponent(btnBlurFilter,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(btnDOFfilter3,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
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
                                        .addComponent(btnClearFilters)
                                        .addComponent(btnCelFilter))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlProcesadoresLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pnlProcesadoresLayout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(btnHightContrastFilter)
                                                .addComponent(btnBlurFilter))
                                        .addComponent(btnBlooomFilter))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlProcesadoresLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnDOFfilter1)
                                        .addComponent(btnDOFfilter2)
                                        .addComponent(btnDOFfilter3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnMsaaFilter)
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

        mnuRenderizadores.setText("Render");

        // shader
        JMenu mnuShader = new JMenu("Shader");

        JMenuItem mnuShaderStandar = new JMenuItem("Estándard");
        mnuShaderStandar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                renderer.setShader(new StandardFragmentShader(renderer));
            }
        });
        mnuShader.add(mnuShaderStandar);

        JMenuItem mnuShaderPBR = new JMenuItem("PBR");
        mnuShaderPBR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                renderer.setShader(new BRDFFragmentShader(renderer));
            }
        });
        mnuShader.add(mnuShaderPBR);

        JMenuItem mnuFlatShader = new JMenuItem("Solid (Flat)");
        mnuFlatShader.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                renderer.setShader(new FlatFragmentShader(renderer));
            }
        });
        mnuShader.add(mnuFlatShader);

        JMenuItem mnoPhongShader = new JMenuItem("Solid (Smooth)");
        mnoPhongShader.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                renderer.setShader(new PhongFramentShader(renderer));
            }
        });
        mnuShader.add(mnoPhongShader);

        JMenuItem mnuColor = new JMenuItem("Color");
        mnuColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                renderer.setShader(new OnlyColorFragmentShader(renderer));
            }
        });
        mnuShader.add(mnuColor);

        mnuRenderizadores.add(mnuShader);

        mnutipoVista.setText("Tipo Vista");

        jMenuItem13.setText("Alambre");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        mnutipoVista.add(jMenuItem13);

        jMenuItem14.setText("Sólido Plano");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        mnutipoVista.add(jMenuItem14);

        jMenuItem15.setText("Solido Suave");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        mnutipoVista.add(jMenuItem15);

        jMenuItem16.setText("Material");
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem16ActionPerformed(evt);
            }
        });
        mnutipoVista.add(jMenuItem16);

        jMenuItem17.setText("Sombras");
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem17ActionPerformed(evt);
            }
        });
        mnutipoVista.add(jMenuItem17);

        mnuRenderizadores.add(mnutipoVista);

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

        mnuRenderizadores.add(jMenu9);

        barraMenu.add(mnuRenderizadores);

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

        itmCrearCaja.setText("Cubo");
        itmCrearCaja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itmCrearCajaActionPerformed(evt);
            }
        });
        jMenu1.add(itmCrearCaja);

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

        itmCrearNcoesfera.setText("Icoesfera");
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

        itmToro.setText("Toro");
        itmToro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itmToroActionPerformed(evt);
            }
        });
        jMenu1.add(itmToro);

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
        for (Entity object : editorRenderer.entidadesSeleccionadas) {
            toRemove.add(object);
        }
        for (Entity object : toRemove) {
            // renderer.eliminarObjeto(object);
            engine.getScene().removeEntity(object);
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
        for (Entity object : editorRenderer.entidadesSeleccionadas) {
            clipboard.add(object);
        }
    }// GEN-LAST:event_itmCopiarActionPerformed

    private void itmPegarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_itmPegarActionPerformed
        if (clipboard.size() > 0) {
            editorRenderer.entidadesSeleccionadas.clear();
            for (Entity object : clipboard) {
                Entity newObject = object.clone();
                newObject.setName(object.getName() + " Copy");
                engine.getScene().addEntity(newObject);
                editorRenderer.entidadesSeleccionadas.add(newObject);
                editorRenderer.entidadActiva = newObject;
            }

            for (Entity object : editorRenderer.entidadesSeleccionadas) {
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
        esfera.addComponent(new Sphere(1.0f));
        esfera.addComponent(new QColisionEsfera(1.0f));

        engine.getScene().addEntity(esfera);
        actualizarArbolEscena();
        seleccionarEntidad(esfera);
    }// GEN-LAST:event_jMenuItem8ActionPerformed

    private void itmCrearCajaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_itmCrearCajaActionPerformed
        Entity cubo = new Entity("Cubo");
        cubo.addComponent(new Box(1));
        cubo.addComponent(new QColisionCaja(1, 1, 1));
        engine.getScene().addEntity(cubo);
        actualizarArbolEscena();
        seleccionarEntidad(cubo);
        // actualizarFigurasRenderers();
    }// GEN-LAST:event_itmCrearCajaActionPerformed

    private void itmToroActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_itmToroActionPerformed
        Entity objeto = new Entity("Toro");
        Torus toro = new Torus(2, 1, 30, 20);
        objeto.addComponent(toro);
        // objeto.agregarComponente(new QColisionCapsula(1, 2));
        // objeto.agregarComponente(new QColisionCaja(2, 1, 2));
        objeto.addComponent(new QColisionMallaConvexa(toro));
        engine.getScene().addEntity(objeto);
        actualizarArbolEscena();
        seleccionarEntidad(objeto);
    }// GEN-LAST:event_itmToroActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem11ActionPerformed
        Entity objeto = new Entity("Cilindro");
        objeto.addComponent(new Cylinder(1, 1.0f));
        objeto.addComponent(new QColisionCilindro(1, 1.0f));
        engine.getScene().addEntity(objeto);
        actualizarArbolEscena();
        seleccionarEntidad(objeto);
    }// GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem12ActionPerformed
        Entity objeto = new Entity("Cono");
        objeto.addComponent(new Cone(1, 1.0f));
        objeto.addComponent(new QColisionCono(1, 1.0f));
        engine.getScene().addEntity(objeto);
        actualizarArbolEscena();
        seleccionarEntidad(objeto);
    }// GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem13ActionPerformed
        renderer.opciones.setTipoVista(RenderOptions.VISTA_WIRE);
    }// GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem14ActionPerformed
        renderer.opciones.setTipoVista(RenderOptions.VISTA_FLAT);
    }// GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem15ActionPerformed
        renderer.opciones.setTipoVista(RenderOptions.VISTA_PHONG);
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
        engine.getScene().addEntity(nuevaLuz);
        actualizarArbolEscena();
        seleccionarEntidad(nuevaLuz);
    }// GEN-LAST:event_mnuLuzPuntualActionPerformed

    private void mnuLuzDireccionalActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuLuzDireccionalActionPerformed
        Entity nuevaLuz = new Entity("Luz Direccional");
        nuevaLuz.addComponent(
                new QDirectionalLigth(1f, new QColor(Color.white), Float.POSITIVE_INFINITY, false, false));
        engine.getScene().addEntity(nuevaLuz);
        actualizarArbolEscena();
        seleccionarEntidad(nuevaLuz);
    }// GEN-LAST:event_mnuLuzDireccionalActionPerformed

    private void mnuLuzConicaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuLuzConicaActionPerformed
        Entity nuevaLuz = new Entity("Luz Cónica");
        nuevaLuz.addComponent(new QSpotLigth(0.75f, new QColor(Color.white), Float.POSITIVE_INFINITY,
                QVector3.of(0, -1, 0), (float) Math.toRadians(45), (float) Math.toRadians(40), false, false));
        engine.getScene().addEntity(nuevaLuz);
        actualizarArbolEscena();
        seleccionarEntidad(nuevaLuz);
    }// GEN-LAST:event_mnuLuzConicaActionPerformed

    private void cbxZSortActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cbxZSortActionPerformed
        renderer.opciones.setZSort(cbxZSort.isSelected());
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
                engine.getScene().addEntity(entity);
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
        Mesh plano = new Plane(2, 2);
        entidad.addComponent(plano);
        entidad.addComponent(new QColisionMallaConvexa(plano));
        engine.getScene().addEntity(entidad);
        actualizarArbolEscena();
        seleccionarEntidad(entidad);
    }// GEN-LAST:event_jMenuItem18ActionPerformed

    private void importEntityAP(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem23ActionPerformed
        importarObjeto();
    }// GEN-LAST:event_jMenuItem23ActionPerformed

    private void exportEntityAP(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem22ActionPerformed
        chooser.setFileFilter(new FileNameExtensionFilter("Archivo Entidad Motor3D", "qengine"));
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            SerializarUtil.agregarObjeto(chooser.getSelectedFile().getAbsolutePath(), editorRenderer.entidadActiva,
                    false,
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
            int tam = engine.getScene().getEntities().size();
            for (Entity entidad : engine.getScene().getEntities()) {
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
                engine.getScene().addEntity(entity);
                if (entity instanceof Camera) {
                    renderer.setCamera((Camera) entity);
                    actualizarArbolEscena();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al abrir archivo " + e.getMessage());
            e.printStackTrace();
        }

        actualizarArbolEscena();
    }// GEN-LAST:event_jMenuItem20ActionPerformed

    private void btnClearFiltersAP(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton11ActionPerformed
        renderer.clearFilters();
    }// GEN-LAST:event_jButton11ActionPerformed

    private void btnBlooomFilterAP(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton12ActionPerformed
        renderer.addFilter(new BloomFilter());
    }// GEN-LAST:event_jButton12ActionPerformed

    private void btnHightContrastFilterAP(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton13ActionPerformed
        renderer.addFilter(new HightContrastFilter());
    }// GEN-LAST:event_jButton13ActionPerformed

    private void btnBlurFilterAP(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton14ActionPerformed
        renderer.addFilter(new BlurFilter());
    }// GEN-LAST:event_jButton14ActionPerformed

    private void btnActualizarReflejosActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnActualizarReflejosActionPerformed
        // motor.setForzarActualizacionMapaReflejos(true);
    }// GEN-LAST:event_btnActualizarReflejosActionPerformed

    private void btnDOFfilter1AP(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton15ActionPerformed
        renderer.addFilter(new DepthOfFieldFilter(DepthOfFieldFilter.DESENFOQUE_CERCA, 0.5f));
    }// GEN-LAST:event_jButton15ActionPerformed

    private void btnDOFfilter2AP(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton16ActionPerformed
        renderer.addFilter(new DepthOfFieldFilter(DepthOfFieldFilter.DESENFOQUE_LEJOS, 0.5f));
    }// GEN-LAST:event_jButton16ActionPerformed

    private void btnDOFfilter3AP(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton17ActionPerformed
        renderer.addFilter(new DepthOfFieldFilter(DepthOfFieldFilter.DESENFOQUE_EXCLUYENTE, 0.5f));
    }// GEN-LAST:event_jButton17ActionPerformed

    private void btnCelFilterAP(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton18ActionPerformed
        renderer.addFilter(new CelShadeFilter());
    }// GEN-LAST:event_jButton18ActionPerformed

    private void pnlColorFondoMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_pnlColorFondoMouseClicked

    }// GEN-LAST:event_pnlColorFondoMouseClicked

    private void pnlColorFondoMousePressed(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_pnlColorFondoMousePressed
        JColorChooser colorChooser = new JColorChooser();
        Color newColor = colorChooser.showDialog(this, "Seleccione un color", pnlColorFondo.getBackground());
        if (newColor != null) {
            pnlColorFondo.setBackground(newColor);
            engine.getScene().setAmbientColor(new QColor(newColor));
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
            renderer.getScene().fog.setColour(new QColor(newColor));
        }
    }// GEN-LAST:event_pnlNeblinaColorMousePressed

    private void chkNeblinaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_chkNeblinaActionPerformed
        renderer.getScene().fog.setActive(chkNeblina.isSelected());
    }// GEN-LAST:event_chkNeblinaActionPerformed

    private void spnNeblinaDensidadStateChanged(javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_spnNeblinaDensidadStateChanged
        renderer.getScene().fog.setDensity(((Double) spnNeblinaDensidad.getValue()).floatValue());
    }// GEN-LAST:event_spnNeblinaDensidadStateChanged

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem2ActionPerformed
        Entity entidad = new Entity("Malla");
        PlanarMesh malla = new PlanarMesh(true, 20, 20, 20, 20);
        entidad.addComponent(malla);
        entidad.addComponent(new QColisionMallaConvexa(malla));
        engine.getScene().addEntity(entidad);
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
            engine.getScene().addEntity(entidad);
            actualizarArbolEscena();
            seleccionarEntidad(entidad);
        }
    }// GEN-LAST:event_itmMapaAlturaActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem3ActionPerformed
        Entity entidad = new Entity("Entidad");
        engine.getScene().addEntity(entidad);
        actualizarArbolEscena();
        seleccionarEntidad(entidad);
    }// GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem26ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem26ActionPerformed
        Entity objeto = new Entity("Cilindro");
        objeto.addComponent(new CylinderX(1, 1.0f));
        objeto.addComponent(new QColisionCilindroX(1, 1.0f));
        engine.getScene().addEntity(objeto);
        actualizarArbolEscena();
        seleccionarEntidad(objeto);
    }// GEN-LAST:event_jMenuItem26ActionPerformed

    private void btnInvertirNormalesActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnInvertirNormalesActionPerformed
        for (Entity seleccionado : editorRenderer.entidadesSeleccionadas) {
            for (EntityComponent compo : seleccionado.getComponents()) {
                if (compo instanceof Mesh) {
                    ((Mesh) compo).invertNormals();
                }
            }
        }
    }// GEN-LAST:event_btnInvertirNormalesActionPerformed

    private void btnCentroGeometriaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnCentroGeometriaActionPerformed
        for (Entity seleccionado : editorRenderer.entidadesSeleccionadas) {
            QDefinirCentro.definirCentroOrigenAGeometria(seleccionado);
            seleccionarEntidad(seleccionado);
        }
    }// GEN-LAST:event_btnCentroGeometriaActionPerformed

    private void btnSuavizarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnSuavizarActionPerformed
        for (Entity seleccionado : editorRenderer.entidadesSeleccionadas) {
            for (EntityComponent compo : seleccionado.getComponents()) {
                if (compo instanceof Mesh) {
                    ((Mesh) compo).smooth();
                }
            }
        }
    }// GEN-LAST:event_btnSuavizarActionPerformed

    private void btnNoSuavizarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnNoSuavizarActionPerformed
        for (Entity seleccionado : editorRenderer.entidadesSeleccionadas) {
            for (EntityComponent compo : seleccionado.getComponents()) {
                if (compo instanceof Mesh) {
                    ((Mesh) compo).unSmooth();
                }
            }
        }
    }// GEN-LAST:event_btnNoSuavizarActionPerformed

    private void btnTipoSolidoActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnTipoSolidoActionPerformed
        for (Entity seleccionado : editorRenderer.entidadesSeleccionadas) {
            for (EntityComponent compo : seleccionado.getComponents()) {
                if (compo instanceof Mesh) {
                    ((Mesh) compo).type = Mesh.GEOMETRY_TYPE_MESH;
                }
            }
        }
    }// GEN-LAST:event_btnTipoSolidoActionPerformed

    private void btnTipoAlambreActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnTipoAlambreActionPerformed
        for (Entity seleccionado : editorRenderer.entidadesSeleccionadas) {
            for (EntityComponent compo : seleccionado.getComponents()) {
                if (compo instanceof Mesh) {
                    ((Mesh) compo).type = Mesh.GEOMETRY_TYPE_WIRE;
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
        engine.getScene().addEntity(entidad);
        actualizarArbolEscena();
        seleccionarEntidad(entidad);
    }// GEN-LAST:event_itmAddCamaraActionPerformed

    private void btnDividirActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnDividirActionPerformed
        for (Entity seleccionado : editorRenderer.entidadesSeleccionadas) {
            for (EntityComponent compo : seleccionado.getComponents()) {
                if (compo instanceof Mesh) {
                    // QMallaUtil.subdividir((QGeometria) compo, 1);
                    // ((Mesh) compo).subdivisionSurfaceSimple();
                }
            }
        }
    }// GEN-LAST:event_btnDividirActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem5ActionPerformed
        Entity entidad = new Entity("Triángulo");
        Triangle triangulo = new Triangle(1);
        entidad.addComponent(triangulo);
        entidad.addComponent(new QColisionTriangulo(triangulo));
        engine.getScene().addEntity(entidad);
        actualizarArbolEscena();
        seleccionarEntidad(entidad);
    }// GEN-LAST:event_jMenuItem5ActionPerformed

    private void btnMsaaFilterAP(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton19ActionPerformed
        renderer.addFilter(new MsaaFilter());
    }// GEN-LAST:event_jButton19ActionPerformed

    private void mnuEspiralActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuEspiralActionPerformed
        Entity objeto = new Entity("Espiral");
        objeto.addComponent(new QEspiral(1, 10, 20));
        // objeto.agregarComponente(new QColisionCilindro(1, 1.0f));
        engine.getScene().addEntity(objeto);
        actualizarArbolEscena();
        seleccionarEntidad(objeto);
    }// GEN-LAST:event_mnuEspiralActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem1ActionPerformed
        Entity objeto = new Entity("Cilindro");
        objeto.addComponent(new CylinderZ(1, 1.0f));
        objeto.addComponent(new QColisionCilindroX(1, 1.0f));
        engine.getScene().addEntity(objeto);
        actualizarArbolEscena();
        seleccionarEntidad(objeto);
    }// GEN-LAST:event_jMenuItem1ActionPerformed

    private void mnuItemPrismaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuItemPrismaActionPerformed
        Entity objeto = new Entity("Prisma");
        // objeto.agregarComponente(new QPrisma(3, 1.0f, 1.0f, 20, 3));
        objeto.addComponent(new Prism(3, 1.0f, 1.0f, 5, 3));
        // objeto.agregarComponente(new QColisionCilindro(1, 1.0f));
        engine.getScene().addEntity(objeto);
        actualizarArbolEscena();
        seleccionarEntidad(objeto);
    }// GEN-LAST:event_mnuItemPrismaActionPerformed

    private void mnuItemGeosferaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuItemGeosferaActionPerformed
        Entity esfera = new Entity("Geoesfera");
        esfera.addComponent(new GeoSphere(1.0f, 3));
        esfera.addComponent(new QColisionEsfera(1.0f));
        engine.getScene().addEntity(esfera);
        actualizarArbolEscena();
        seleccionarEntidad(esfera);
    }// GEN-LAST:event_mnuItemGeosferaActionPerformed

    private void cbxInterpolarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cbxInterpolarActionPerformed
        QGlobal.ANIMACION_INTERPOLAR = cbxInterpolar.isSelected();
    }// GEN-LAST:event_cbxInterpolarActionPerformed

    private void btnAnimIniciarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAnimIniciarActionPerformed
        engine.startAnimation();
    }// GEN-LAST:event_btnAnimIniciarActionPerformed

    private void btnAnimDetenerActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAnimDetenerActionPerformed
        engine.stopAnimation();
    }// GEN-LAST:event_btnAnimDetenerActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton7ActionPerformed
        if (editorRenderer.entidadActiva != null) {
            editorRenderer.entidadActiva.moveLeft(0.51f);
        }
    }// GEN-LAST:event_jButton7ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton6ActionPerformed
        if (editorRenderer.entidadActiva != null) {
            editorRenderer.entidadActiva.moveForward(-0.2f);
        }
    }// GEN-LAST:event_jButton6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton5ActionPerformed
        if (editorRenderer.entidadActiva != null) {
            editorRenderer.entidadActiva.moveForward(0.2f);
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
        engine.detenerFisica();
    }// GEN-LAST:event_jButton2ActionPerformed

    private void btnStartPhysicsAP(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed
        engine.iniciarFisica();
        // motor.iniciarFisica(2);
    }// GEN-LAST:event_jButton1ActionPerformed

    private void txtAnimTiempoInicioActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtAnimTiempoInicioActionPerformed
        engine.getAnimationEngine().setTiempoInicio(Float.valueOf(txtAnimTiempoInicio.getText()));
        sldLineaTiempo.setMinimum(Integer.parseInt(txtAnimTiempoInicio.getText()) * 10);
    }// GEN-LAST:event_txtAnimTiempoInicioActionPerformed

    private void txtAnimTiempoInicioKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtAnimTiempoInicioKeyReleased
        engine.getAnimationEngine().setTiempoInicio(Float.valueOf(txtAnimTiempoInicio.getText()));
        sldLineaTiempo.setMinimum(Integer.parseInt(txtAnimTiempoInicio.getText()) * 10);
    }// GEN-LAST:event_txtAnimTiempoInicioKeyReleased

    private void txtAnimTiempoFinActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtAnimTiempoFinActionPerformed
        engine.getAnimationEngine().setTiempoFin(Float.valueOf(txtAnimTiempoFin.getText()));
        sldLineaTiempo.setMaximum(Integer.parseInt(txtAnimTiempoFin.getText()) * 10);
    }// GEN-LAST:event_txtAnimTiempoFinActionPerformed

    private void txtAnimTiempoFinKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtAnimTiempoFinKeyReleased
        engine.getAnimationEngine().setTiempoFin(Float.valueOf(txtAnimTiempoFin.getText()));
        sldLineaTiempo.setMaximum(Integer.parseInt(txtAnimTiempoFin.getText()) * 10);
    }// GEN-LAST:event_txtAnimTiempoFinKeyReleased

    private void sldLineaTiempoStateChanged(javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_sldLineaTiempoStateChanged

        if (!cambiandoLineaTiempo) {
            engine.getAnimationEngine()
                    .setTiempo(((float) sldLineaTiempo.getValue() / (float) sldLineaTiempo.getMaximum())
                            * ((float) sldLineaTiempo.getMaximum() / 10.f));
            engine.getAnimationEngine().actualizarPoses(engine.getAnimationEngine().getTiempo());
        }
        txtAnimTiempo.setText("Tiempo:" + df.format(engine.getAnimationEngine().getTiempo()));
    }// GEN-LAST:event_sldLineaTiempoStateChanged

    private void btnAnimVelocidad1XActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAnimVelocidad1XActionPerformed
        engine.getAnimationEngine().setVelocidad(1.0f);
    }// GEN-LAST:event_btnAnimVelocidad1XActionPerformed

    private void btnAnimVelocidad075XActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAnimVelocidad075XActionPerformed
        engine.getAnimationEngine().setVelocidad(0.75f);
    }// GEN-LAST:event_btnAnimVelocidad075XActionPerformed

    private void btnANimVelocidad05XActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnANimVelocidad05XActionPerformed
        engine.getAnimationEngine().setVelocidad(0.5f);
    }// GEN-LAST:event_btnANimVelocidad05XActionPerformed

    private void btnAnimVelocidad025XActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAnimVelocidad025XActionPerformed
        engine.getAnimationEngine().setVelocidad(0.25f);
    }// GEN-LAST:event_btnAnimVelocidad025XActionPerformed

    private void btnAnimVelocidad15XActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAnimVelocidad15XActionPerformed
        engine.getAnimationEngine().setVelocidad(1.5f);
    }// GEN-LAST:event_btnAnimVelocidad15XActionPerformed

    private void btnAnimVelocidad2XActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAnimVelocidad2XActionPerformed
        engine.getAnimationEngine().setVelocidad(2.0f);
    }// GEN-LAST:event_btnAnimVelocidad2XActionPerformed

    private void btnAnimVelocidad4XActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAnimVelocidad4XActionPerformed
        engine.getAnimationEngine().setVelocidad(4.0f);
    }// GEN-LAST:event_btnAnimVelocidad4XActionPerformed

    private void btnAnimInvertirActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAnimInvertirActionPerformed
        engine.getAnimationEngine().invertir();
    }// GEN-LAST:event_btnAnimInvertirActionPerformed

    private void chkVerGridActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_chkVerGridActionPerformed
        renderer.opciones.setDibujarGrid(chkVerGrid.isSelected());
    }// GEN-LAST:event_chkVerGridActionPerformed

    private void mnuItemTeteraActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuItemTeteraActionPerformed
        Entity item = new Entity("Teapot");
        Mesh malla = new Teapot();
        item.addComponent(malla);
        item.addComponent(new QColisionMallaConvexa(malla));
        engine.getScene().addEntity(item);
        actualizarArbolEscena();
        seleccionarEntidad(item);
    }// GEN-LAST:event_mnuItemTeteraActionPerformed

    private void mnuItemSusaneActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuItemSusaneActionPerformed
        Entity item = new Entity("Teapot");
        Mesh malla = new Suzane();
        item.addComponent(malla);
        item.addComponent(new QColisionMallaConvexa(malla));
        engine.getScene().addEntity(item);
        actualizarArbolEscena();
        seleccionarEntidad(item);
    }

    private void btnCalcularNormalesActionPerformed(java.awt.event.ActionEvent evt) {
        for (Entity seleccionado : editorRenderer.entidadesSeleccionadas) {
            for (EntityComponent compo : seleccionado.getComponents()) {
                if (compo instanceof Mesh) {
                    ((Mesh) compo).computeNormals();
                    ((Mesh) compo).updateTimeMark();
                }
            }
        }
    }

    private void itmMenuEliminarRecursivoActionPerformed(java.awt.event.ActionEvent evt) {
        LinkedList<Entity> toRemove = new LinkedList<>();
        for (Entity object : editorRenderer.entidadesSeleccionadas) {
            toRemove.add(object);
        }
        for (Entity object : toRemove) {
            engine.getScene().removeEntityAndChilds(object);
        }
        actualizarArbolEscena();
    }

    private void itmCrearNcoesferaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_itmCrearNcoesferaActionPerformed
        Entity esfera = new Entity("Nicoesfera");
        esfera.addComponent(new IcoSphere(1.0f, 3));
        esfera.addComponent(new QColisionEsfera(1.0f));
        engine.getScene().addEntity(esfera);
        actualizarArbolEscena();
        seleccionarEntidad(esfera);
    }// GEN-LAST:event_itmCrearNcoesferaActionPerformed

    private void itmCrearcuboesferaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_itmCrearcuboesferaActionPerformed
        Entity esfera = new Entity("Cuboesfera");
        esfera.addComponent(new SphereBox(1.0f, 3));
        esfera.addComponent(new QColisionEsfera(1.0f));
        engine.getScene().addEntity(esfera);
        actualizarArbolEscena();
        seleccionarEntidad(esfera);
    }

    private void btnDividirCatmullActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnDividirCatmullActionPerformed
        for (Entity seleccionado : editorRenderer.entidadesSeleccionadas) {
            for (EntityComponent compo : seleccionado.getComponents()) {
                if (compo instanceof Mesh) {
                    // QMallaUtil.subdividir((QGeometria) compo, 1);
                    // ((Mesh) compo).subdivisionSurfaceCatmullClark();
                }
            }
        }
    }

    private void btnEliminarVerticesDuplicadosActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnEliminarVerticesDuplicadosActionPerformed
        for (Entity seleccionado : editorRenderer.entidadesSeleccionadas) {
            for (EntityComponent compo : seleccionado.getComponents()) {
                if (compo instanceof Mesh) {
                    // QMallaUtil.subdividir((QGeometria) compo, 1);
                    // ((Mesh) compo).cleanDuplicateVertex();
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
        for (Entity objeto : engine.getScene().getEntities()) {
            if (objeto != null && objeto.isToRender()) {
                for (EntityComponent componente : objeto.getComponents()) {
                    if (componente instanceof Mesh) {
                        vertexCount += ((Mesh) componente).vertexList.length;
                        faceCount += ((Mesh) componente).primitiveList.length;
                    }
                }

            }
        }
        lblEstad.setText(vertexCount + " vértices; " + faceCount + " polígonos; "
                + engine.getScene().getEntities().size() + " objetos");
    }

    public void actualizarArbolEscena() {
        // actualizo el arbol
        DefaultMutableTreeNode raiz = new DefaultMutableTreeNode(new QArbolWrapper("Escena", null));
        for (Entity entidad : engine.getScene().getEntities()) {
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
        // if (editorRenderer.entidadesSeleccionadas.contains(entidad)) {
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
        pnlEditorEntidad.editarEntidad(editorRenderer.entidadActiva);
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
                    if (selectedObject instanceof Gizmo // || selectedObject instanceof QGizmoParte
                    ) {
                        return;
                    }
                    editorRenderer.entidadActiva = selectedObject;
                    if (editorRenderer.entidadActiva == null) {
                        editorRenderer.entidadesSeleccionadas.clear();
                        return;
                    }
                    if (!QInputManager.isShitf()) {
                        editorRenderer.entidadesSeleccionadas.clear();
                    }
                    editorRenderer.entidadesSeleccionadas.add(editorRenderer.entidadActiva);
                    if (accionSeleccionar != null) {
                        accionSeleccionar.run(editorRenderer.entidadActiva);
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
                        if (selectedObject instanceof Gizmo) {
                            ((Gizmo) selectedObject).mouseMove(QInputManager.getDeltaX(), -QInputManager.getDeltaY());
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
            public void destroy() {

            }
        });

        QInputManager.addKeyboardListener(new QTecladoReceptor() {
            @Override
            public void keyPressed(KeyEvent evt) {

                switch (evt.getKeyCode()) {

                    case KeyEvent.VK_DECIMAL: {
                        try {
                            CameraController control = (CameraController) ComponentUtil
                                    .getComponent(renderer.getCamera(), CameraController.class);
                            if (control != null) {
                                control.getTarget()
                                        .set(editorRenderer.entidadActiva.getTransform().getLocation());
                                control.updateCamera();
                            } else {
                                CameraOrbiter control2 = (CameraOrbiter) ComponentUtil
                                        .getComponent(renderer.getCamera(), CameraOrbiter.class);
                                if (control2 != null) {
                                    control2.getTarget()
                                            .set(editorRenderer.entidadActiva.getTransform().getLocation());
                                    control2.updateCamera();
                                }
                            }
                        } catch (Exception e) {

                        }
                        break;
                    }
                    case KeyEvent.VK_Y:
                        renderer.opciones.setTipoVista(RenderOptions.VISTA_WIRE);
                        break;
                    case KeyEvent.VK_U:
                        renderer.opciones.setTipoVista(RenderOptions.VISTA_FLAT);
                        break;
                    case KeyEvent.VK_I:
                        renderer.opciones.setTipoVista(RenderOptions.VISTA_PHONG);
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
                        editorRenderer.setTipoGizmoActual(EditorRenderer.GIZMO_NINGUNO);
                        break;
                    case KeyEvent.VK_2:
                        editorRenderer.setTipoGizmoActual(EditorRenderer.GIZMO_TRASLACION);
                        break;
                    case KeyEvent.VK_3:
                        editorRenderer.setTipoGizmoActual(EditorRenderer.GIZMO_ROTACION);
                        break;
                    case KeyEvent.VK_4:
                        editorRenderer.setTipoGizmoActual(EditorRenderer.GIZMO_ESCALA);
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
    private javax.swing.JButton btnGuadarScreenShot;
    private javax.swing.JButton btnInvertirNormales;
    private javax.swing.JButton btnNoSuavizar;
    private javax.swing.JButton btnSuavizar;
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
    private javax.swing.JButton btnClearFilters;
    private javax.swing.JButton btnBlooomFilter;
    private javax.swing.JButton btnHightContrastFilter;
    private javax.swing.JButton btnBlurFilter;
    private javax.swing.JButton btnDOFfilter1;
    private javax.swing.JButton btnDOFfilter2;
    private javax.swing.JButton btnDOFfilter3;
    private javax.swing.JButton btnCelFilter;
    private javax.swing.JButton btnMsaaFilter;
    private javax.swing.JButton btnStopPhysics;
    private javax.swing.JButton btnAccion1;
    private javax.swing.JButton btnAccion2;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton btnInflar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel lblToolEntidadTipo;
    private javax.swing.JLabel lblToolsMapas;
    private javax.swing.JLabel lblToolsGeneral;
    private javax.swing.JLabel lblToolsGeometria;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel lblSombreado;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel lblHerramientasEntidad;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel lblToolInflar;
    private javax.swing.JLabel lblToolDefinirCentro;
    private javax.swing.JLabel lblToolEntidadNormales;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel lblOpcionesRender;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu mnuRenderizadores;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu mnutipoVista;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu mnuFile;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem itmToro;
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
    private javax.swing.JPanel pnlLuzAmbiente;
    private javax.swing.JPanel pnlNeblina;
    private javax.swing.JPanel pnlOpciones;
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
