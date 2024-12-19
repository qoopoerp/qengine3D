/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.editor.entity;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.entity.component.animation.AnimationComponent;
import net.qoopo.engine.core.entity.component.animation.QCompAlmacenAnimaciones;
import net.qoopo.engine.core.entity.component.animation.Skeleton;
import net.qoopo.engine.core.entity.component.cubemap.QCubeMap;
import net.qoopo.engine.core.entity.component.ligth.QDirectionalLigth;
import net.qoopo.engine.core.entity.component.ligth.QLigth;
import net.qoopo.engine.core.entity.component.ligth.QPointLigth;
import net.qoopo.engine.core.entity.component.ligth.QSpotLigth;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.QPoligono;
import net.qoopo.engine.core.entity.component.mesh.primitive.QPrimitiva;
import net.qoopo.engine.core.entity.component.mesh.primitive.QVertex;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.IcoSphere;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.PlanarMesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QCaja;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QCilindro;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QCilindroX;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QCono;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QCuboEsfera;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QEsfera;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QGeoesfera;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QPlano;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QPrisma;
import net.qoopo.engine.core.entity.component.mesh.primitive.shape.QToro;
import net.qoopo.engine.core.entity.component.particles.ParticleEmissor;
import net.qoopo.engine.core.entity.component.physics.collision.QComponenteColision;
import net.qoopo.engine.core.entity.component.physics.collision.detector.CollisionShape;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.compuesta.QColisionCompuesta;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.mallas.QColisionMallaConvexa;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.mallas.QColisionMallaIndexada;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.mallas.QColisionTerreno;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.QColisionCaja;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.QColisionCapsula;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.QColisionCilindro;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.QColisionCilindroX;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.QColisionCono;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.QColisionEsfera;
import net.qoopo.engine.core.entity.component.physics.dinamica.QObjetoDinamico;
import net.qoopo.engine.core.entity.component.physics.dinamica.QObjetoRigido;
import net.qoopo.engine.core.entity.component.physics.dinamica.QObjetoSuave;
import net.qoopo.engine.core.entity.component.physics.restricciones.QRestriccionBisagra;
import net.qoopo.engine.core.entity.component.physics.restricciones.QRestriccionFija;
import net.qoopo.engine.core.entity.component.physics.restricciones.QRestriccionGenerica6DOF;
import net.qoopo.engine.core.entity.component.physics.restricciones.QRestriccionPunto2Punto;
import net.qoopo.engine.core.entity.component.physics.vehiculo.QVehiculo;
import net.qoopo.engine.core.entity.component.physics.vehiculo.QVehiculoControl;
import net.qoopo.engine.core.entity.component.reflections.PlanarReflection;
import net.qoopo.engine.core.entity.component.terrain.HeightMapTerrain;
import net.qoopo.engine.core.entity.component.transform.QTransformacion;
import net.qoopo.engine.core.entity.component.water.Water;
import net.qoopo.engine.core.entity.component.water.WaterDuDv;
import net.qoopo.engine.core.lwjgl.audio.component.SoundEmissorAL;
import net.qoopo.engine.core.lwjgl.audio.component.SoundListenerAL;
import net.qoopo.engine.core.material.AbstractMaterial;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.material.node.MaterialNode;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.renderer.superficie.QJPanel;
import net.qoopo.engine.core.renderer.superficie.Superficie;
import net.qoopo.engine.core.scene.Camera;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.texture.QTextura;
import net.qoopo.engine.core.texture.util.QMaterialUtil;
import net.qoopo.engine.core.util.QGlobal;
import net.qoopo.engine.core.util.QUtilComponentes;
import net.qoopo.engine.core.util.mesh.QUtilNormales;
import net.qoopo.engine3d.editor.Principal;
import net.qoopo.engine3d.editor.entity.componentes.animacion.PnlAlamacenAnimacion;
import net.qoopo.engine3d.editor.entity.componentes.animacion.PnlEsqueleto;
import net.qoopo.engine3d.editor.entity.componentes.audio.PnlAudioListener;
import net.qoopo.engine3d.editor.entity.componentes.audio.PnlAudioSource;
import net.qoopo.engine3d.editor.entity.componentes.basicos.PnlCamara;
import net.qoopo.engine3d.editor.entity.componentes.basicos.PnlTransformacion;
import net.qoopo.engine3d.editor.entity.componentes.efectos.PnlEmisorParticulas;
import net.qoopo.engine3d.editor.entity.componentes.entorno.PnlMapaCubo;
import net.qoopo.engine3d.editor.entity.componentes.fisica.colision.PnlColision;
import net.qoopo.engine3d.editor.entity.componentes.fisica.colision.PnlColisionCaja;
import net.qoopo.engine3d.editor.entity.componentes.fisica.colision.PnlColisionCapsula;
import net.qoopo.engine3d.editor.entity.componentes.fisica.colision.PnlColisionCilindro;
import net.qoopo.engine3d.editor.entity.componentes.fisica.colision.PnlColisionCilindroX;
import net.qoopo.engine3d.editor.entity.componentes.fisica.colision.PnlColisionCompuesta;
import net.qoopo.engine3d.editor.entity.componentes.fisica.colision.PnlColisionCono;
import net.qoopo.engine3d.editor.entity.componentes.fisica.colision.PnlColisionEsfera;
import net.qoopo.engine3d.editor.entity.componentes.fisica.colision.PnlColisionMallaConvexa;
import net.qoopo.engine3d.editor.entity.componentes.fisica.colision.PnlColisionMallaIndexada;
import net.qoopo.engine3d.editor.entity.componentes.fisica.colision.PnlColisionTerreno;
import net.qoopo.engine3d.editor.entity.componentes.fisica.dinamico.PnlFisicoRigido;
import net.qoopo.engine3d.editor.entity.componentes.fisica.dinamico.PnlFisicoSuave;
import net.qoopo.engine3d.editor.entity.componentes.fisica.restricciones.PnlFisRestriccion6DOF;
import net.qoopo.engine3d.editor.entity.componentes.fisica.restricciones.PnlFisRestriccionBisagra;
import net.qoopo.engine3d.editor.entity.componentes.fisica.restricciones.PnlFisRestriccionFija;
import net.qoopo.engine3d.editor.entity.componentes.fisica.restricciones.PnlFisRestriccionPunto2Punto;
import net.qoopo.engine3d.editor.entity.componentes.fisica.vehiculo.PnlControlVehiculo;
import net.qoopo.engine3d.editor.entity.componentes.fisica.vehiculo.PnlFisVehiculo;
import net.qoopo.engine3d.editor.entity.componentes.geometria.PnlCaja;
import net.qoopo.engine3d.editor.entity.componentes.geometria.PnlCilindro;
import net.qoopo.engine3d.editor.entity.componentes.geometria.PnlCilindroX;
import net.qoopo.engine3d.editor.entity.componentes.geometria.PnlCono;
import net.qoopo.engine3d.editor.entity.componentes.geometria.PnlCuboEsfera;
import net.qoopo.engine3d.editor.entity.componentes.geometria.PnlEsfera;
import net.qoopo.engine3d.editor.entity.componentes.geometria.PnlGeoEsfera;
import net.qoopo.engine3d.editor.entity.componentes.geometria.PnlMalla;
import net.qoopo.engine3d.editor.entity.componentes.geometria.PnlNicoEsfera;
import net.qoopo.engine3d.editor.entity.componentes.geometria.PnlPlano;
import net.qoopo.engine3d.editor.entity.componentes.geometria.PnlPrisma;
import net.qoopo.engine3d.editor.entity.componentes.geometria.PnlToro;
import net.qoopo.engine3d.editor.entity.componentes.luces.PnlLuz;
import net.qoopo.engine3d.editor.entity.componentes.procesadores.agua.PnlAguaSimple;
import net.qoopo.engine3d.editor.entity.componentes.procesadores.espejo.PnlEspejo;
import net.qoopo.engine3d.editor.entity.componentes.terreno.PnlTerreno;
import net.qoopo.engine3d.editor.util.Util;
import net.qoopo.engine3d.engines.render.GuiUTIL;

/**
 *
 * @author alberto
 */
public class EditorEntidad extends javax.swing.JPanel {

    private Entity entity;
    private RenderEngine renderVistaPrevia;
    private Entity fondoVistaPrevia = null;
    private final EditorMaterial pnlEditorMaterial = new EditorMaterial();
    private boolean objectLock = false;
    private AbstractMaterial activeMaterial = null;
    private final ArrayList<AbstractMaterial> editingMaterial = new ArrayList<>();
    private final ArrayList<EntityComponent> editingComponentes = new ArrayList<>();
    private final ArrayList<Mesh> listaGeometrias = new ArrayList<>();

    private int Xtemp = -1;

    // ---------------- EDICION GEOMETRIA
    private Mesh geomActual;
    private QVertex verticeActual;
    private QPoligono poligonoActual;

    private List<Entity> listaPadres = new ArrayList<>();

    public EditorEntidad() {
        initComponents();

        lstGeometrias.setModel(new DefaultListModel<>());
        lstVertices.setModel(new DefaultListModel<>());
        lstCaras.setModel(new DefaultListModel<>());
        cboMaterial.setModel(new DefaultComboBoxModel<>());

        iniciarVistaPrevia();

        pnlMaterialEdit.setVisible(false);
        pnlInspector.setVisible(false);

        txtEntidadNombre.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // actualizarListaObjetos();
                applyObjectControl();
            }
        });

        // lblSpecExp.addMouseMotionListener(spinnerMouseMotion);
        // lblNormalAmount.addMouseMotionListener(spinnerMouseMotion);
        // lblSpecExp.addMouseListener(spinnerMouseInput);
        // lblNormalAmount.addMouseListener(spinnerMouseInput);
        // agrega los panels externos
        this.pnlContMaterial.setLayout(new BorderLayout());
        pnlContMaterial.add(pnlEditorMaterial, BorderLayout.CENTER);
        // pnlEditorMaterial

        // menu de componentes
        crearMenuComponentes();
        DefaultComboBoxModel modeloCombo = new DefaultComboBoxModel();
        cboEntidades.setModel(modeloCombo);
    }

    private void crearMenuComponentes() {
        /*
         * private JMenu mnuGeometria;
         * private JMenu mnuFisica;
         * private JMenu mnuEfectos;
         * private JMenu mnuSonido;
         * private JMenu mnuAnimacion;
         * private JMenu mnuComportamiento;
         * private JMenu mnuAgua;
         * private JMenu mnuEntorno;
         * private JMenu mnuIluminacion;
         * 
         */
        mnuGeometria = new JMenu("Geometría");
        mnuIluminacion = new JMenu("Iluminación");
        mnuFisica = new JMenu("Física");
        mnuEfectos = new JMenu("Efectos");
        mnuSonido = new JMenu("Audio");
        mnuAnimacion = new JMenu("Animación");
        mnuComportamiento = new JMenu("Comportamiento");
        mnuTerreno = new JMenu("Terreno");
        mnuEntorno = new JMenu("Entorno");

        // ---------------------------------- GEOMETRIA
        // -----------------------------------------
        mnuGeometria.setIcon(Util.cargarIcono16("/cube_16.png"));
        JMenuItem itmCaja = GuiUTIL.crearMenuItem("Caja", "", Util.cargarIcono16("/cube_16.png"), false);
        itmCaja.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                entity.addComponent(new QCaja(1));
                editarEntidad(entity);
            }
        });
        mnuGeometria.add(itmCaja);
        JMenuItem itmEsfera = GuiUTIL.crearMenuItem("Esfera", "", Util.cargarIcono16("/sphere_16.png"), false);
        itmEsfera.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                entity.addComponent(new QEsfera(0.5f));
                editarEntidad(entity);
            }
        });
        mnuGeometria.add(itmEsfera);
        JMenuItem itmPlano = GuiUTIL.crearMenuItem("Plano", "", Util.cargarIcono16("/quad_16.png"), false);
        itmPlano.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                entity.addComponent(new QPlano(1, 1));
                editarEntidad(entity);
            }
        });
        mnuGeometria.add(itmPlano);

        // ---------------------------------- LUCES
        // -----------------------------------------
        mnuIluminacion.setIcon(Util.cargarIcono16("/luz.png"));
        JMenuItem itmLuzDireccional = GuiUTIL.crearMenuItem("Direccional", "", Util.cargarIcono16("/sol_16.png"),
                false);
        itmLuzDireccional.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                entity.addComponent(
                        new QDirectionalLigth(0.75f, new QColor(Color.white), Float.POSITIVE_INFINITY, false, false));
                editarEntidad(entity);
            }
        });
        mnuIluminacion.add(itmLuzDireccional);
        JMenuItem itmLuzPuntual = GuiUTIL.crearMenuItem("Puntual", "", Util.cargarIcono16("/luz.png"), false);
        itmLuzPuntual.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                entity.addComponent(
                        new QPointLigth(1.0f, new QColor(Color.white), Float.POSITIVE_INFINITY, false, false));
                editarEntidad(entity);
            }
        });
        mnuIluminacion.add(itmLuzPuntual);
        JMenuItem itmLuzConica = GuiUTIL.crearMenuItem("Cónica", "", Util.cargarIcono16("/spot_luz.png"), false);
        itmLuzConica.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                entity.addComponent(new QSpotLigth(1.5f, new QColor(Color.white), Float.POSITIVE_INFINITY,
                        QVector3.of(0, -1, 0), (float) Math.toRadians(45), (float) Math.toRadians(40), false, false));
                editarEntidad(entity);
            }
        });
        mnuIluminacion.add(itmLuzConica);

        // ---------------------------------- ENTORNO
        // -----------------------------------------
        mnuEntorno.setIcon(Util.cargarIcono16("/cube.png"));
        JMenuItem itmMapaCubo = GuiUTIL.crearMenuItem("Mapa cúbico", "", Util.cargarIcono16("/cube.png"), false);
        itmMapaCubo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                entity.addComponent(new QCubeMap(QGlobal.MAPA_CUPO_RESOLUCION));
                editarEntidad(entity);
            }
        });
        mnuEntorno.add(itmMapaCubo);
        // ---------------------------------- COMPORTAMIENTO
        // -----------------------------------------
        mnuComportamiento.setIcon(Util.cargarIcono16("/cube.png"));
        JMenuItem itmControladorVehiculo = GuiUTIL.crearMenuItem("Controlador vehículo", "",
                Util.cargarIcono16("/cube.png"), false);
        itmControladorVehiculo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                QVehiculo vehi = QUtilComponentes.getVehiculo(entity);
                if (vehi != null) {
                    entity.addComponent(new QVehiculoControl(vehi));
                    editarEntidad(entity);
                } else {
                    JOptionPane.showMessageDialog(EditorEntidad.this,
                            "No hay componentes vehículos. Es necesario un vehículo para poder agregar un controlador.");
                }

                editarEntidad(entity);
            }
        });
        mnuComportamiento.add(itmControladorVehiculo);

        // ---------------------------------- TERRENO
        // -----------------------------------------
        mnuTerreno.setIcon(Util.cargarIcono16("/cube.png"));
        JMenuItem itmTerreno = GuiUTIL.crearMenuItem("Terreno", "", Util.cargarIcono16("/cube.png"), false);
        itmTerreno.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                entity.addComponent(new HeightMapTerrain());
                editarEntidad(entity);
            }
        });
        mnuTerreno.add(itmTerreno);

        // ---------------------------------- FISICA
        // -----------------------------------------
        mnuFisica.setIcon(Util.cargarIcono16("/cube.png"));

        JMenu mnuColision = GuiUTIL.crearMenu("Colisiones", "Formas de colisión", Util.cargarIcono16("/cube.png"),
                false);
        mnuFisica.add(mnuColision);
        JMenuItem itmColisionCaja = GuiUTIL.crearMenuItem("Colisión Caja", "Forma rápida de colisión.",
                Util.cargarIcono16("/cube.png"), false);
        itmColisionCaja.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                entity.addComponent(new QColisionCaja(1, 1, 1));
                editarEntidad(entity);
            }
        });
        mnuColision.add(itmColisionCaja);
        JMenuItem itmColisionEsfera = GuiUTIL.crearMenuItem("Colisión Esfera", "Forma rápida de colisión.",
                Util.cargarIcono16("/sphere_16.png"), false);
        itmColisionEsfera.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                entity.addComponent(new QColisionEsfera(1));
                editarEntidad(entity);
            }
        });
        mnuColision.add(itmColisionEsfera);
        JMenuItem itmColisionCapsula = GuiUTIL.crearMenuItem("Colisión Cápsula", "Forma rápida de colisión.",
                Util.cargarIcono16("/cube.png"), false);
        itmColisionCapsula.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                entity.addComponent(new QColisionCapsula(1, 0.5f));
                editarEntidad(entity);
            }
        });
        mnuColision.add(itmColisionCapsula);
        JMenuItem itmColisioncilindro = GuiUTIL.crearMenuItem("Colisión Cilindro", "Forma rápida de colisión.",
                Util.cargarIcono16("/cube.png"), false);
        itmColisioncilindro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                entity.addComponent(new QColisionCilindro(1, 0.5f));
                editarEntidad(entity);
            }
        });
        mnuColision.add(itmColisioncilindro);
        JMenuItem itmColisionCono = GuiUTIL.crearMenuItem("Colisión Cono", "Forma rápida de colisión.",
                Util.cargarIcono16("/cube.png"), false);
        itmColisionCono.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                entity.addComponent(new QColisionCono(1, 0.5f));
                editarEntidad(entity);
            }
        });
        mnuColision.add(itmColisionCono);
        JMenuItem itmColisionTerreno = GuiUTIL.crearMenuItem("Colisión Terreno",
                "Crea una forma de colisión ideal para Terrenos de altura", Util.cargarIcono16("/teapot_16.png"),
                false);
        itmColisionTerreno.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HeightMapTerrain terr = QUtilComponentes.getTerreno(entity);
                if (terr != null) {
                    entity.addComponent(new QColisionTerreno(terr));
                    editarEntidad(entity);
                } else {
                    JOptionPane.showMessageDialog(EditorEntidad.this,
                            "La entidad no es de tipo Terreno. Sólo se puede agregar esta forma de colisisón a los Terrenos.");
                }

            }
        });
        mnuColision.add(itmColisionTerreno);
        JMenuItem itmMallaColision = GuiUTIL.crearMenuItem("Colisión Malla Convexa",
                "Es el tipo más rapido de forma arbitraria.", Util.cargarIcono16("/teapot_16.png"), false);
        itmMallaColision.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Mesh gem = QUtilComponentes.getMesh(entity);
                if (gem != null) {
                    entity.addComponent(new QColisionMallaConvexa(gem));
                    editarEntidad(entity);
                } else {
                    JOptionPane.showMessageDialog(EditorEntidad.this,
                            "No existe un componente de geometría. Es necesario para poder agregar un componente de colisión de malla convexa.");
                }

            }
        });
        mnuColision.add(itmMallaColision);
        JMenuItem itmMallaColisionIndexada = GuiUTIL.crearMenuItem("Colisión Malla Indexada",
                "Malla de colisión indexada, permite adaptarse a formas complejas como terrenos importados.",
                Util.cargarIcono16("/teapot_16.png"), false);
        itmMallaColisionIndexada.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Mesh gem = QUtilComponentes.getMesh(entity);
                if (gem != null) {
                    entity.addComponent(new QColisionMallaIndexada(gem));
                    editarEntidad(entity);
                } else {
                    JOptionPane.showMessageDialog(EditorEntidad.this,
                            "No existe un componente de geometría. Es necesario para poder agregar un componente de colisión de malla indexada.");
                }

            }
        });
        mnuColision.add(itmMallaColisionIndexada);

        JMenuItem itmColisionCompuesta = GuiUTIL.crearMenuItem("Colisión Compuesta",
                "Permite armar una forma compuesta d otras formas de colisión.", Util.cargarIcono16("/teapot_16.png"),
                false);
        itmColisionCompuesta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                entity.addComponent(new QColisionCompuesta());
                editarEntidad(entity);
            }
        });
        mnuColision.add(itmColisionCompuesta);

        JMenu mnuDinamica = GuiUTIL.crearMenu("Dinámica", "Permite la dinámica", Util.cargarIcono16("/cube_16.png"),
                false);
        mnuFisica.add(mnuDinamica);
        JMenuItem itmRigido = GuiUTIL.crearMenuItem("Objeto Rígido", "", Util.cargarIcono16("/cube_16.png"), false);
        itmRigido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // valida si existe un componente de colisión
                CollisionShape col = QUtilComponentes.getColision(entity);
                if (col != null) {
                    QObjetoRigido comp = QUtilComponentes.getFisicoRigido(entity);
                    if (comp == null) {
                        entity.addComponent(new QObjetoRigido(QObjetoDinamico.DINAMICO, 1.0f));
                        editarEntidad(entity);
                    } else {
                        JOptionPane.showMessageDialog(EditorEntidad.this,
                                "Ya existe un componente rígido. Solo se puede crear un componente rígido.");
                    }
                } else {
                    JOptionPane.showMessageDialog(EditorEntidad.this,
                            "No existe un componente de colisión. Es necesario para poder agregar un componente de física.");
                }
            }
        });
        mnuDinamica.add(itmRigido);
        JMenuItem itmVehiculo = GuiUTIL.crearMenuItem("Vehículo", "", Util.cargarIcono16("/cube.png"), false);
        itmVehiculo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                QObjetoRigido rigido = QUtilComponentes.getFisicoRigido(entity);
                if (rigido != null) {
                    entity.addComponent(new QVehiculo(rigido));
                    editarEntidad(entity);
                } else {
                    JOptionPane.showMessageDialog(EditorEntidad.this,
                            "No existe un componente rígido. Es necesario para armar el chasis del vehículo .");
                }
            }
        });
        mnuDinamica.add(itmVehiculo);

        JMenu mnuRestricciones = GuiUTIL.crearMenu("Restricciones",
                "Permite agregar restricciones para los movimientos de los rígios", Util.cargarIcono16("/cube.png"),
                false);
        mnuFisica.add(mnuRestricciones);
        JMenuItem itmUnionFija = GuiUTIL.crearMenuItem("Unión Fija", "", Util.cargarIcono16("/cube.png"), false);
        itmUnionFija.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // valida si existe un componente rigido
                QObjetoRigido comp = QUtilComponentes.getFisicoRigido(entity);
                if (comp != null) {
                    entity.addComponent(new QRestriccionFija(comp));
                    editarEntidad(entity);
                } else {
                    JOptionPane.showMessageDialog(EditorEntidad.this,
                            "No existe un componente físico. Es necesario para poder agregar un componente de restricción.");
                }
            }
        });
        mnuRestricciones.add(itmUnionFija);
        JMenuItem itmUnionPuntoAPunto = GuiUTIL.crearMenuItem("Unión Punto a Punto", "",
                Util.cargarIcono16("/cube.png"), false);
        itmUnionPuntoAPunto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // valida si existe un componente rigido
                QObjetoRigido comp = QUtilComponentes.getFisicoRigido(entity);
                if (comp != null) {
                    entity.addComponent(new QRestriccionPunto2Punto(comp));
                    editarEntidad(entity);
                } else {
                    JOptionPane.showMessageDialog(EditorEntidad.this,
                            "No existe un componente físico. Es necesario para poder agregar un componente de restricción.");
                }
            }
        });
        mnuRestricciones.add(itmUnionPuntoAPunto);
        JMenuItem itmBisagra = GuiUTIL.crearMenuItem("Bisagra", "", Util.cargarIcono16("/cube.png"), false);
        itmBisagra.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // valida si existe un componente rigido
                QObjetoRigido comp = QUtilComponentes.getFisicoRigido(entity);
                if (comp != null) {
                    entity.addComponent(new QRestriccionBisagra(comp));
                    editarEntidad(entity);
                } else {
                    JOptionPane.showMessageDialog(EditorEntidad.this,
                            "No existe un componente físico. Es necesario para poder agregar un componente de restricción.");
                }
            }
        });
        mnuRestricciones.add(itmBisagra);
        JMenuItem itmAmortiguador = GuiUTIL.crearMenuItem("Amortiguador", "", Util.cargarIcono16("/cube.png"), false);
        itmAmortiguador.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // valida si existe un componente rigido
                QObjetoRigido comp = QUtilComponentes.getFisicoRigido(entity);
                if (comp != null) {
                    entity.addComponent(new QRestriccionBisagra(comp));
                    editarEntidad(entity);
                } else {
                    JOptionPane.showMessageDialog(EditorEntidad.this,
                            "No existe un componente físico. Es necesario para poder agregar un componente de restricción.");
                }
            }
        });
        mnuRestricciones.add(itmAmortiguador);
        JMenuItem itmGeneric6DOF = GuiUTIL.crearMenuItem("Genérica", "", Util.cargarIcono16("/cube.png"), false);
        itmGeneric6DOF.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // valida si existe un componente rigido
                QObjetoRigido comp = QUtilComponentes.getFisicoRigido(entity);
                if (comp != null) {
                    entity.addComponent(new QRestriccionGenerica6DOF(comp));
                    editarEntidad(entity);
                } else {
                    JOptionPane.showMessageDialog(EditorEntidad.this,
                            "No existe un componente físico. Es necesario para poder agregar un componente de restricción.");
                }
            }
        });
        mnuRestricciones.add(itmGeneric6DOF);

        // --------agrega al popup menu
        mnuComponentes.add(mnuGeometria);
        mnuComponentes.add(mnuIluminacion);
        mnuComponentes.add(mnuFisica);
        mnuComponentes.add(mnuEfectos);
        mnuComponentes.add(mnuSonido);
        mnuComponentes.add(mnuAnimacion);
        mnuComponentes.add(mnuComportamiento);
        mnuComponentes.add(mnuTerreno);
        mnuComponentes.add(mnuEntorno);

    }

    public EditorEntidad(Entity entidad) {
        this();
        // renderVistaPrevia.getUniverso().addEntity(entidad
        editarEntidad(entidad);
    }

    private void iniciarVistaPrevia() {
        try {
            Scene escena = new Scene();
            pnlVistaPrevia.setLayout(new BorderLayout());
            QJPanel pnl = new QJPanel();
            pnlVistaPrevia.add(pnl, BorderLayout.CENTER);
            // renderVistaPrevia = new QRender(escena, "Vista Previa", new Superficie(pnl), 0, 0);
            renderVistaPrevia = AssetManager.get().getRendererFactory().createRenderEngine(escena, "Vista Previa",new Superficie(pnl), 0, 0);
            Camera camara = new Camera("Material");
            camara.lookAtTarget(QVector3.of(10, 10, 10), QVector3.of(0, 0, 0), QVector3.of(0, 1, 0));
            camara.frustrumLejos = 20.0f;
            renderVistaPrevia.setCamara(camara);
            Entity luz = new Entity("luz");
            luz.addComponent(new QDirectionalLigth(QVector3.of(-1, -1, -1)));

            fondoVistaPrevia = crearFondoCuadros();
            // entidadVistaPrevia = crearTestCubo(new QMaterial(QColor.LIGHT_GRAY,
            // QColor.WHITE, 50));
            // entidadVistaPrevia = crearTestEsfera(new QMaterial(QColor.LIGHT_GRAY,
            // QColor.WHITE, 50));
            renderVistaPrevia.getEscena().addEntity(fondoVistaPrevia);
            // renderVistaPrevia.getUniverso().addEntity(entidadVistaPrevia);
            renderVistaPrevia.getEscena().addEntity(luz);
            renderVistaPrevia.setShowStats(false);
            renderVistaPrevia.opciones.setDibujarLuces(false);
            renderVistaPrevia.setInteractuar(false);
            renderVistaPrevia.start();
            renderVistaPrevia.resize();
            renderVistaPrevia.update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Entity crearFondoCuadros() {
        try {
            Entity entFondo = new Entity("Fondo");
            QTextura text = new QTextura(ImageIO.read(Principal.class.getResourceAsStream("/fondo.jpg")));
            AbstractMaterial matFondo = new QMaterialBas(text, 50);
            entFondo.addComponent(
                    QUtilNormales.invertirNormales(QMaterialUtil.aplicarMaterial(new QCaja(10f), matFondo)));
            entFondo.move(3, 3, 3);
            return entFondo;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void liberar() {
        // renderVistaPrevia.detener();
    }

    public void editarEntidad(Entity objeto) {
        objectLock = true;
        if (this.entity != null) {
            renderVistaPrevia.getEscena().eliminarEntidadSindestruir(entity);
        }
        this.pnlInspector.setVisible(true);
        this.renderVistaPrevia.start();
        this.entity = objeto;
        // this.renderVistaPrevia.getEscena().addEntity(entidad);
        txtEntidadNombre.setText(objeto.getName());

        // limpio la lista de materiales
        DefaultListModel model = (DefaultListModel) lstMaterials.getModel();
        editingMaterial.clear();
        model.clear();

        DefaultListModel modelGeometrias = (DefaultListModel) lstGeometrias.getModel();
        listaGeometrias.clear();
        editingComponentes.clear();
        modelGeometrias.clear();
        pnlMaterialEdit.setVisible(false);
        // elimino los paneles que existan
        pnlListaComponentes.removeAll();
        // pnlListaComponentes.setLayout(new GridLayout(entidad.componentes.size(), 1));
        pnlListaComponentes.setLayout(new BoxLayout(pnlListaComponentes, BoxLayout.Y_AXIS));

        // primer componente es la transformación
        pnlListaComponentes.add(new PnlTransformacion((QTransformacion) entity.getTransformacion()));

        if (entity instanceof Camera) {
            pnlListaComponentes.add(new PnlCamara((Camera) entity));
        }

        Dimension dimensionBotonCerrar = new Dimension(40, 20);
        Dimension dimensionLabel = new Dimension(300, 15);
        for (EntityComponent componente : entity.getComponents()) {
            ActionListener eliminarComp = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    entity.getComponents().remove(componente);
                    editarEntidad(entity);
                }
            };
            JButton btnCerrar = new JButton("x");
            btnCerrar.setPreferredSize(dimensionBotonCerrar);
            btnCerrar.setMaximumSize(dimensionBotonCerrar);
            btnCerrar.setBorderPainted(false);
            btnCerrar.setBorder(null);
            btnCerrar.addActionListener(eliminarComp);
            JPanel pnBar = new JPanel();
            pnBar.setLayout(new GridLayout(1, 1));
            pnBar.setPreferredSize(dimensionBotonCerrar);
            pnBar.setMaximumSize(dimensionBotonCerrar);
            pnBar.add(btnCerrar);

            pnlListaComponentes.add(pnBar);
            if (componente instanceof QLigth) {
                pnlListaComponentes.add(new PnlLuz((QLigth) componente));
            } else if (componente instanceof Mesh) {
                pnlMaterialEdit.setVisible(true);
                desplegarListaMateriales((Mesh) componente);
                modelGeometrias.addElement(((Mesh) componente).nombre);
                listaGeometrias.add((Mesh) componente);
                if (componente instanceof QCuboEsfera) {
                    pnlListaComponentes.add(new PnlCuboEsfera((QCuboEsfera) componente));
                } else if (componente instanceof QCaja) {
                    pnlListaComponentes.add(new PnlCaja((QCaja) componente));
                } else if (componente instanceof QEsfera) {
                    pnlListaComponentes.add(new PnlEsfera((QEsfera) componente));
                } else if (componente instanceof QPlano) {
                    pnlListaComponentes.add(new PnlPlano((QPlano) componente));
                } else if (componente instanceof QToro) {
                    pnlListaComponentes.add(new PnlToro((QToro) componente));
                } else if (componente instanceof PlanarMesh) {
                    pnlListaComponentes.add(new PnlMalla((PlanarMesh) componente));
                } else if (componente instanceof QCilindro) {
                    pnlListaComponentes.add(new PnlCilindro((QCilindro) componente));
                } else if (componente instanceof QCilindroX) {
                    pnlListaComponentes.add(new PnlCilindroX((QCilindroX) componente));
                } else if (componente instanceof QPrisma) {
                    pnlListaComponentes.add(new PnlPrisma((QPrisma) componente));
                } else if (componente instanceof QCono) {
                    pnlListaComponentes.add(new PnlCono((QCono) componente));
                } else if (componente instanceof QGeoesfera) {
                    pnlListaComponentes.add(new PnlGeoEsfera((QGeoesfera) componente));
                } else if (componente instanceof IcoSphere) {
                    pnlListaComponentes.add(new PnlNicoEsfera((IcoSphere) componente));
                } else {
                    JPanel pnBar2 = new JPanel();
                    pnBar2.setLayout(new GridLayout(1, 1));
                    pnBar2.add(GuiUTIL.crearJLabel("Geometría:" + ((Mesh) componente).nombre,
                            Util.cargarIcono16("/cube_16.png")));
                    pnBar2.setPreferredSize(dimensionLabel);
                    pnBar2.setMaximumSize(dimensionLabel);
                    pnlListaComponentes.add(pnBar2);
                }

                // totales de la geometria
                JPanel pnTotales = new JPanel();
                pnTotales.setLayout(new GridLayout(1, 1));
                pnTotales.add(GuiUTIL.crearJLabel("Vert:" + ((Mesh) componente).vertices.length,
                        Util.cargarIcono16("/cube_16.png")));
                pnTotales.add(GuiUTIL.crearJLabel("Pol:" + ((Mesh) componente).primitivas.length,
                        Util.cargarIcono16("/cube_16.png")));
                pnTotales.setPreferredSize(dimensionLabel);
                pnTotales.setMaximumSize(dimensionLabel);
                pnlListaComponentes.add(pnTotales);

            } else if (componente instanceof QCompAlmacenAnimaciones) {
                pnlListaComponentes.add(new PnlAlamacenAnimacion((QCompAlmacenAnimaciones) componente));
            } else if (componente instanceof AnimationComponent) {
                JPanel pnBar2 = new JPanel();
                pnBar2.setLayout(new GridLayout(1, 1));
                pnBar2.add(GuiUTIL.crearJLabel("Animación:" + ((AnimationComponent) componente).getNombre(),
                        Util.cargarIcono16("/animacion.png")));
                pnBar2.setPreferredSize(dimensionLabel);
                pnBar2.setMaximumSize(dimensionLabel);
                pnlListaComponentes.add(pnBar2);
            } else if (componente instanceof SoundListenerAL) {
                pnlListaComponentes.add(new PnlAudioListener());
            } else if (componente instanceof SoundEmissorAL) {
                pnlListaComponentes.add(new PnlAudioSource((SoundEmissorAL) componente));
            } else if (componente instanceof ParticleEmissor) {
                pnlListaComponentes.add(new PnlEmisorParticulas());
            } else if (componente instanceof WaterDuDv) {
                pnlListaComponentes.add(new PnlAguaSimple((WaterDuDv) componente));
            } else if (componente instanceof PlanarReflection) {
                pnlListaComponentes.add(new PnlEspejo());
            } else if (componente instanceof QCubeMap) {
                pnlListaComponentes.add(new PnlMapaCubo((QCubeMap) componente));
            } else if (componente instanceof QObjetoRigido) {
                pnlListaComponentes.add(new PnlFisicoRigido((QObjetoRigido) componente));
            } else if (componente instanceof QVehiculo) {
                pnlListaComponentes.add(new PnlFisVehiculo((QVehiculo) componente, Principal.instancia.getEscena()));
            } else if (componente instanceof QVehiculoControl) {
                pnlListaComponentes.add(new PnlControlVehiculo((QVehiculoControl) componente));
            } else if (componente instanceof QObjetoSuave) {
                pnlListaComponentes.add(new PnlFisicoSuave());
            } else if (componente instanceof QComponenteColision) {
                pnlListaComponentes.add(new PnlColision());
            } else if (componente instanceof QColisionCaja) {
                pnlListaComponentes.add(new PnlColisionCaja((QColisionCaja) componente));
            } else if (componente instanceof QColisionEsfera) {
                pnlListaComponentes.add(new PnlColisionEsfera((QColisionEsfera) componente));
            } else if (componente instanceof QColisionCapsula) {
                pnlListaComponentes.add(new PnlColisionCapsula((QColisionCapsula) componente));
            } else if (componente instanceof QColisionCilindro) {
                pnlListaComponentes.add(new PnlColisionCilindro((QColisionCilindro) componente));
            } else if (componente instanceof QColisionCilindroX) {
                pnlListaComponentes.add(new PnlColisionCilindroX((QColisionCilindroX) componente));
            } else if (componente instanceof QColisionCono) {
                pnlListaComponentes.add(new PnlColisionCono((QColisionCono) componente));
            } else if (componente instanceof QColisionMallaConvexa) {
                pnlListaComponentes.add(new PnlColisionMallaConvexa((QColisionMallaConvexa) componente));
            } else if (componente instanceof QColisionMallaIndexada) {
                pnlListaComponentes.add(new PnlColisionMallaIndexada((QColisionMallaIndexada) componente));
            } else if (componente instanceof QColisionTerreno) {
                pnlListaComponentes.add(new PnlColisionTerreno((QColisionTerreno) componente));
            } else if (componente instanceof QColisionCompuesta) {
                pnlListaComponentes.add(new PnlColisionCompuesta((QColisionCompuesta) componente));
            } else if (componente instanceof Skeleton) {
                pnlListaComponentes.add(new PnlEsqueleto((Skeleton) componente));
            } else if (componente instanceof HeightMapTerrain) {
                pnlListaComponentes.add(new PnlTerreno((HeightMapTerrain) componente));
            } else if (componente instanceof QRestriccionFija) {
                pnlListaComponentes.add(new PnlFisRestriccionFija((QRestriccionFija) componente));
            } else if (componente instanceof QRestriccionPunto2Punto) {
                pnlListaComponentes.add(new PnlFisRestriccionPunto2Punto((QRestriccionPunto2Punto) componente));
            } else if (componente instanceof QRestriccionBisagra) {
                pnlListaComponentes.add(new PnlFisRestriccionBisagra((QRestriccionBisagra) componente));
            } else if (componente instanceof QRestriccionGenerica6DOF) {
                pnlListaComponentes.add(new PnlFisRestriccion6DOF((QRestriccionGenerica6DOF) componente));
            } else {
                JPanel pnBar2 = new JPanel();
                pnBar2.setLayout(new GridLayout(1, 1));
                pnBar2.add(GuiUTIL.crearJLabel("Desconocido", Util.cargarIcono16("/cube.png")));
                pnBar2.setPreferredSize(dimensionLabel);
                pnBar2.setMaximumSize(dimensionLabel);
                pnlListaComponentes.add(pnBar2);
            }
            // agrego a la lista de componentes
            editingComponentes.add(componente);
        }
        renderVistaPrevia.update();
        actualizarCboMaterial();
        actualizaListaEntidades();
        objectLock = false;
    }

    private void actualizaListaEntidades() {
        try {
            listaPadres.clear();
            DefaultComboBoxModel modeloCombo = new DefaultComboBoxModel();
            modeloCombo.addElement("Ninguno");
            listaPadres.add(null);
            for (Entity ent : Principal.instancia.getEscena().getEntities()) {
                modeloCombo.addElement(ent.getName());
                listaPadres.add(ent);
            }
            cboEntidades.setModel(modeloCombo);
            if (entity.getParent() != null) {
                cboEntidades.setSelectedIndex(listaPadres.indexOf(entity.getParent().getName()));
            } else {
                cboEntidades.setSelectedIndex(0);
            }
        } catch (Exception e) {
        }

    }

    // private static void imprimirArbolEntidad(Entity entidad, String espacios,
    // StringBuilder sb) {
    //// System.out.println(espacios + entidad.nombre);
    // sb.append(espacios).append(entidad.nombre).append("\n");
    // espacios += " ";
    // if (entidad.getHijos() != null) {
    // for (Entity hijo : entidad.getHijos()) {
    // imprimirArbolEntidad(hijo, espacios, sb);
    // }
    // }
    // }
    private void applyObjectControl() {
        if (!objectLock) {
            entity.setName(txtEntidadNombre.getText());
            renderVistaPrevia.update();
            Principal.instancia.actualizarArbolEscena();
            // if (entidad instanceof QGeometria) {
            // for (QPoligono face : ((QGeometria) entidad).primitivas) {
            // face.smooth = btnObjectoSuavizado.isSelected();
            // }
            // }
        }
    }

    //
    private void desplegarListaMateriales(Mesh object) {
        DefaultListModel model = (DefaultListModel) lstMaterials.getModel();
        for (QPrimitiva face : object.primitivas) {
            if (!editingMaterial.contains(face.material)) {
                editingMaterial.add(face.material);
                if (face.material instanceof QMaterialBas) {
                    model.addElement("(BAS) " + face.material.getNombre());
                } else if (face.material instanceof MaterialNode) {
                    model.addElement("(Nodo) " + face.material.getNombre());
                }
                lstMaterials.setSelectedIndex(0);
                // activeMaterial = face.material;
                // populateMaterialControl(face.material);
            }
        }
    }

    private void actualizarCboMaterial() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (AbstractMaterial mat : editingMaterial) {
            model.addElement(mat.getNombre());
        }
        cboMaterial.setModel(model);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        lstComponentes1 = new javax.swing.JList<>();
        groupTipoEntidad = new javax.swing.ButtonGroup();
        mnuComponentes = new javax.swing.JPopupMenu();
        jPanel13 = new javax.swing.JPanel();
        pnlVistaPrevia = new javax.swing.JPanel();
        tabPnlEntidad = new javax.swing.JTabbedPane();
        scrollPropiedades = new javax.swing.JScrollPane();
        pnlInspector = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txtEntidadNombre = new javax.swing.JTextField();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        pnlListaComponentes = new javax.swing.JPanel();
        brnAgregarComponente = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        cboEntidades = new javax.swing.JComboBox<>();
        pnlMaterial = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        pnlMaterialEdit = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        lstMaterials = new javax.swing.JList<>(new DefaultListModel());
        pnlContMaterial = new javax.swing.JPanel();
        btnNuevoMatBAS = new javax.swing.JButton();
        btnGuardarMaterial = new javax.swing.JButton();
        btnAbrir = new javax.swing.JButton();
        btnNuevoMatPBR = new javax.swing.JButton();
        scrollGeometria = new javax.swing.JScrollPane();
        pnlGeometria = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstGeometrias = new javax.swing.JList<>();
        tabGeometria = new javax.swing.JTabbedPane();
        scrollVertices = new javax.swing.JScrollPane();
        pnlVertices = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        lstVertices = new javax.swing.JList<>();
        jLabel16 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txtVerticeX = new javax.swing.JTextField();
        txtVerticeY = new javax.swing.JTextField();
        txtVerticeZ = new javax.swing.JTextField();
        btnVerticeEliminar = new javax.swing.JButton();
        scrollCaras = new javax.swing.JScrollPane();
        pnlCaras = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        lstCaras = new javax.swing.JList<>();
        btnCaraEliminar = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JSeparator();
        chkCaraSuave = new javax.swing.JCheckBox();
        jLabel21 = new javax.swing.JLabel();
        cboMaterial = new javax.swing.JComboBox<>();

        lstComponentes1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };

            public int getSize() {
                return strings.length;
            }

            public String getElementAt(int i) {
                return strings[i];
            }
        });

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("Vista Previa"));

        pnlVistaPrevia.setBackground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout pnlVistaPreviaLayout = new javax.swing.GroupLayout(pnlVistaPrevia);
        pnlVistaPrevia.setLayout(pnlVistaPreviaLayout);
        pnlVistaPreviaLayout.setHorizontalGroup(
                pnlVistaPreviaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE));
        pnlVistaPreviaLayout.setVerticalGroup(
                pnlVistaPreviaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 137, Short.MAX_VALUE));

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
                jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(pnlVistaPrevia, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        jPanel13Layout.setVerticalGroup(
                jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel13Layout.createSequentialGroup()
                                .addComponent(pnlVistaPrevia, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)));

        jLabel8.setText("Nombre:");

        txtEntidadNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEntidadNombreActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlListaComponentesLayout = new javax.swing.GroupLayout(pnlListaComponentes);
        pnlListaComponentes.setLayout(pnlListaComponentesLayout);
        pnlListaComponentesLayout.setHorizontalGroup(
                pnlListaComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE));
        pnlListaComponentesLayout.setVerticalGroup(
                pnlListaComponentesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 294, Short.MAX_VALUE));

        brnAgregarComponente.setText("Agregar Componente...");
        brnAgregarComponente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                brnAgregarComponenteActionPerformed(evt);
            }
        });

        jLabel1.setText("Padre:");

        cboEntidades.setModel(
                new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboEntidades.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboEntidadesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlInspectorLayout = new javax.swing.GroupLayout(pnlInspector);
        pnlInspector.setLayout(pnlInspectorLayout);
        pnlInspectorLayout.setHorizontalGroup(
                pnlInspectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlInspectorLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(pnlInspectorLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(pnlListaComponentes, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(pnlInspectorLayout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jSeparator6)
                                                .addComponent(jSeparator4)
                                                .addGroup(pnlInspectorLayout.createSequentialGroup()
                                                        .addComponent(jLabel8)
                                                        .addPreferredGap(
                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(txtEntidadNombre))
                                                .addGroup(pnlInspectorLayout.createSequentialGroup()
                                                        .addComponent(jLabel1)
                                                        .addPreferredGap(
                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(cboEntidades, 0, 232, Short.MAX_VALUE)))
                                        .addComponent(brnAgregarComponente, javax.swing.GroupLayout.DEFAULT_SIZE, 297,
                                                Short.MAX_VALUE))
                                .addContainerGap()));
        pnlInspectorLayout.setVerticalGroup(
                pnlInspectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlInspectorLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(pnlInspectorLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel8)
                                        .addComponent(txtEntidadNombre, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 4,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlInspectorLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(cboEntidades, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 4,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(brnAgregarComponente, javax.swing.GroupLayout.PREFERRED_SIZE, 18,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(pnlListaComponentes, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap()));

        scrollPropiedades.setViewportView(pnlInspector);

        tabPnlEntidad.addTab("Inspector", scrollPropiedades);

        lstMaterials.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        lstMaterials.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstMaterialsValueChanged(evt);
            }
        });
        jScrollPane4.setViewportView(lstMaterials);

        javax.swing.GroupLayout pnlContMaterialLayout = new javax.swing.GroupLayout(pnlContMaterial);
        pnlContMaterial.setLayout(pnlContMaterialLayout);
        pnlContMaterialLayout.setHorizontalGroup(
                pnlContMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE));
        pnlContMaterialLayout.setVerticalGroup(
                pnlContMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 245, Short.MAX_VALUE));

        btnNuevoMatBAS.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        btnNuevoMatBAS.setText("N. BAS");
        btnNuevoMatBAS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoMatBASActionPerformed(evt);
            }
        });

        btnGuardarMaterial.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        btnGuardarMaterial.setText("Guardar");
        btnGuardarMaterial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarMaterialActionPerformed(evt);
            }
        });

        btnAbrir.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        btnAbrir.setText("Abrir");

        btnNuevoMatPBR.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        btnNuevoMatPBR.setText("N. PBR");
        btnNuevoMatPBR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoMatPBRActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlMaterialEditLayout = new javax.swing.GroupLayout(pnlMaterialEdit);
        pnlMaterialEdit.setLayout(pnlMaterialEditLayout);
        pnlMaterialEditLayout.setHorizontalGroup(
                pnlMaterialEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlMaterialEditLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(btnNuevoMatBAS)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnNuevoMatPBR)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnGuardarMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, 73,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnAbrir)
                                .addContainerGap(28, Short.MAX_VALUE))
                        .addComponent(jScrollPane4)
                        .addComponent(pnlContMaterial, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        pnlMaterialEditLayout.setVerticalGroup(
                pnlMaterialEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlMaterialEditLayout.createSequentialGroup()
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 123,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlMaterialEditLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnNuevoMatBAS, javax.swing.GroupLayout.PREFERRED_SIZE, 18,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnGuardarMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, 18,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnAbrir, javax.swing.GroupLayout.PREFERRED_SIZE, 18,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnNuevoMatPBR, javax.swing.GroupLayout.PREFERRED_SIZE, 18,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pnlContMaterial, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(17, Short.MAX_VALUE)));

        jScrollPane5.setViewportView(pnlMaterialEdit);

        javax.swing.GroupLayout pnlMaterialLayout = new javax.swing.GroupLayout(pnlMaterial);
        pnlMaterial.setLayout(pnlMaterialLayout);
        pnlMaterialLayout.setHorizontalGroup(
                pnlMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane5));
        pnlMaterialLayout.setVerticalGroup(
                pnlMaterialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 379, Short.MAX_VALUE));

        tabPnlEntidad.addTab("Material", pnlMaterial);

        jLabel5.setText("Lista:");

        lstGeometrias.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        lstGeometrias.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };

            public int getSize() {
                return strings.length;
            }

            public String getElementAt(int i) {
                return strings[i];
            }
        });
        lstGeometrias.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstGeometriasValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(lstGeometrias);

        lstVertices.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        lstVertices.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };

            public int getSize() {
                return strings.length;
            }

            public String getElementAt(int i) {
                return strings[i];
            }
        });
        lstVertices.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstVerticesValueChanged(evt);
            }
        });
        jScrollPane6.setViewportView(lstVertices);

        jLabel16.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jLabel16.setText("X:");

        jLabel19.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jLabel19.setText("Y:");

        jLabel20.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jLabel20.setText("Z:");

        txtVerticeX.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        txtVerticeX.setText("0");
        txtVerticeX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtVerticeXActionPerformed(evt);
            }
        });

        txtVerticeY.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        txtVerticeY.setText("0");
        txtVerticeY.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtVerticeYActionPerformed(evt);
            }
        });

        txtVerticeZ.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        txtVerticeZ.setText("0");
        txtVerticeZ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtVerticeZActionPerformed(evt);
            }
        });

        btnVerticeEliminar.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        btnVerticeEliminar.setText("Eliminar");
        btnVerticeEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerticeEliminarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlVerticesLayout = new javax.swing.GroupLayout(pnlVertices);
        pnlVertices.setLayout(pnlVerticesLayout);
        pnlVerticesLayout.setHorizontalGroup(
                pnlVerticesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlVerticesLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(pnlVerticesLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(pnlVerticesLayout.createSequentialGroup()
                                                .addComponent(jLabel16)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtVerticeX, javax.swing.GroupLayout.PREFERRED_SIZE, 72,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(pnlVerticesLayout.createSequentialGroup()
                                                .addGroup(pnlVerticesLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel19)
                                                        .addComponent(jLabel20))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(pnlVerticesLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(txtVerticeY,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 73,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtVerticeZ,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 73,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap(236, Short.MAX_VALUE))
                        .addGroup(pnlVerticesLayout.createSequentialGroup()
                                .addComponent(btnVerticeEliminar)
                                .addGap(0, 0, Short.MAX_VALUE))
                        .addComponent(jScrollPane6));
        pnlVerticesLayout.setVerticalGroup(
                pnlVerticesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlVerticesLayout.createSequentialGroup()
                                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 90,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlVerticesLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel16)
                                        .addComponent(txtVerticeX, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(
                                        pnlVerticesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel19)
                                                .addComponent(txtVerticeY, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlVerticesLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel20)
                                        .addComponent(txtVerticeZ, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnVerticeEliminar)));

        scrollVertices.setViewportView(pnlVertices);

        tabGeometria.addTab("Vértices", scrollVertices);

        lstCaras.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        lstCaras.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };

            public int getSize() {
                return strings.length;
            }

            public String getElementAt(int i) {
                return strings[i];
            }
        });
        lstCaras.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstCarasValueChanged(evt);
            }
        });
        jScrollPane8.setViewportView(lstCaras);

        btnCaraEliminar.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        btnCaraEliminar.setText("Eliminar");
        btnCaraEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCaraEliminarActionPerformed(evt);
            }
        });

        chkCaraSuave.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        chkCaraSuave.setText("Suave");
        chkCaraSuave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkCaraSuaveActionPerformed(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jLabel21.setText("Material:");

        cboMaterial.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        cboMaterial.setModel(
                new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboMaterial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboMaterialActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlCarasLayout = new javax.swing.GroupLayout(pnlCaras);
        pnlCaras.setLayout(pnlCarasLayout);
        pnlCarasLayout.setHorizontalGroup(
                pnlCarasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSeparator5)
                        .addGroup(pnlCarasLayout.createSequentialGroup()
                                .addComponent(jLabel21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboMaterial, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(chkCaraSuave)
                        .addComponent(btnCaraEliminar)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 264,
                                javax.swing.GroupLayout.PREFERRED_SIZE));
        pnlCarasLayout.setVerticalGroup(
                pnlCarasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlCarasLayout.createSequentialGroup()
                                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 107,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnCaraEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 18,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chkCaraSuave)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(pnlCarasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel21)
                                        .addComponent(cboMaterial, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 9, Short.MAX_VALUE)));

        scrollCaras.setViewportView(pnlCaras);

        tabGeometria.addTab("Caras", scrollCaras);

        javax.swing.GroupLayout pnlGeometriaLayout = new javax.swing.GroupLayout(pnlGeometria);
        pnlGeometria.setLayout(pnlGeometriaLayout);
        pnlGeometriaLayout.setHorizontalGroup(
                pnlGeometriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(tabGeometria)
                        .addComponent(jLabel5)
                        .addComponent(jScrollPane1));
        pnlGeometriaLayout.setVerticalGroup(
                pnlGeometriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlGeometriaLayout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 91,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tabGeometria, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)));

        scrollGeometria.setViewportView(pnlGeometria);

        tabPnlEntidad.addTab("Editor Geometría", scrollGeometria);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(tabPnlEntidad, javax.swing.GroupLayout.Alignment.TRAILING,
                                javax.swing.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
                        .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tabPnlEntidad, javax.swing.GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE)
                                .addContainerGap()));
    }// </editor-fold>//GEN-END:initComponents

    private void lstMaterialsValueChanged(javax.swing.event.ListSelectionEvent evt) {// GEN-FIRST:event_lstMaterialsValueChanged
        if (lstMaterials.getSelectedIndex() >= 0) {
            activeMaterial = editingMaterial.get(lstMaterials.getSelectedIndex());
            // pnlMaterialControl.setVisible(true);
            if (activeMaterial instanceof QMaterialBas) {
                pnlEditorMaterial.populateMaterialControl((QMaterialBas) activeMaterial);
            } else {
                // aca va la edicion del material pbr
            }
        }
    }// GEN-LAST:event_lstMaterialsValueChanged

    private void lstGeometriasValueChanged(javax.swing.event.ListSelectionEvent evt) {// GEN-FIRST:event_lstGeometriasValueChanged
        try {
            geomActual = listaGeometrias.get(lstGeometrias.getSelectedIndex());
            DefaultListModel verticesModel = (DefaultListModel) lstVertices.getModel();
            DefaultListModel carasModel = (DefaultListModel) lstCaras.getModel();
            verticesModel.clear();
            carasModel.clear();

            if (geomActual != null) {
                int c = 0;
                for (QVertex vertice : geomActual.vertices) {
                    // verticesModel.addElement(vertice);
                    verticesModel.addElement(c);
                    c++;
                }

                c = 0;
                for (QPrimitiva poligono : geomActual.primitivas) {
                    // verticesModel.addElement(vertice);
                    carasModel.addElement(c);
                    c++;
                }
            }
        } catch (Exception e) {

        }
    }// GEN-LAST:event_lstGeometriasValueChanged

    private void lstVerticesValueChanged(javax.swing.event.ListSelectionEvent evt) {// GEN-FIRST:event_lstVerticesValueChanged
        try {
            verticeActual = geomActual.vertices[lstVertices.getSelectedIndex()];
            if (verticeActual != null) {
                txtVerticeX.setText(String.valueOf(verticeActual.location.x));
                txtVerticeY.setText(String.valueOf(verticeActual.location.y));
                txtVerticeZ.setText(String.valueOf(verticeActual.location.z));
            }
        } catch (Exception e) {

        }
    }// GEN-LAST:event_lstVerticesValueChanged

    private void txtVerticeXActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtVerticeXActionPerformed
        if (verticeActual != null) {
            verticeActual.location.x = Float.valueOf(txtVerticeX.getText());
        }
    }// GEN-LAST:event_txtVerticeXActionPerformed

    private void txtVerticeYActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtVerticeYActionPerformed
        if (verticeActual != null) {
            verticeActual.location.y = Float.valueOf(txtVerticeY.getText());
        }
    }// GEN-LAST:event_txtVerticeYActionPerformed

    private void txtVerticeZActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtVerticeZActionPerformed
        if (verticeActual != null) {
            verticeActual.location.z = Float.valueOf(txtVerticeZ.getText());
        }
    }// GEN-LAST:event_txtVerticeZActionPerformed

    private void btnVerticeEliminarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnVerticeEliminarActionPerformed
        if (verticeActual != null) {
            geomActual.eliminarVertice(lstVertices.getSelectedIndex());
            lstGeometriasValueChanged(null);
        }
    }// GEN-LAST:event_btnVerticeEliminarActionPerformed

    private void btnCaraEliminarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnCaraEliminarActionPerformed
        if (poligonoActual != null) {
            geomActual.eliminarPoligono(lstCaras.getSelectedIndex());
            lstGeometriasValueChanged(null);
        }
    }// GEN-LAST:event_btnCaraEliminarActionPerformed

    private void lstCarasValueChanged(javax.swing.event.ListSelectionEvent evt) {// GEN-FIRST:event_lstCarasValueChanged
        try {

            QPrimitiva tmp = geomActual.primitivas[lstCaras.getSelectedIndex()];
            if (tmp instanceof QPoligono) {
                this.poligonoActual = (QPoligono) tmp;
            } else {
                poligonoActual = null;
            }
            if (poligonoActual != null) {
                chkCaraSuave.setSelected(poligonoActual.isSmooth());
                if (poligonoActual.material != null) {
                    cboMaterial.setSelectedItem(poligonoActual.material.getNombre());
                }
                // txtVerticeX.setText(String.valueOf(verticeActual.x));
                // txtVerticeY.setText(String.valueOf(verticeActual.y));
                // txtVerticeZ.setText(String.valueOf(verticeActual.z));
            }
        } catch (Exception e) {

        }
    }// GEN-LAST:event_lstCarasValueChanged

    private void chkCaraSuaveActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_chkCaraSuaveActionPerformed
        if (poligonoActual != null) {
            poligonoActual.setSmooth(chkCaraSuave.isSelected());
        }
    }// GEN-LAST:event_chkCaraSuaveActionPerformed

    private void btnNuevoMatBASActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnNuevoMatBASActionPerformed
        QMaterialBas nuevoMat = new QMaterialBas();
        editingMaterial.add(nuevoMat);
        DefaultListModel tmp = (DefaultListModel) lstMaterials.getModel();
        tmp.addElement("(BAS) " + nuevoMat.getNombre());
        lstMaterials.setSelectedIndex(tmp.getSize() - 1);
        activeMaterial = nuevoMat;

        // pnlMaterialControl.setVisible(true);
        pnlEditorMaterial.populateMaterialControl(nuevoMat);
        actualizarCboMaterial();
    }// GEN-LAST:event_btnNuevoMatBASActionPerformed

    private void cboMaterialActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cboMaterialActionPerformed
        try {
            if (poligonoActual != null) {
                poligonoActual.material = editingMaterial.get(cboMaterial.getSelectedIndex());
            }
        } catch (Exception e) {

        }
    }// GEN-LAST:event_cboMaterialActionPerformed

    private void brnAgregarComponenteActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_brnAgregarComponenteActionPerformed
        mnuComponentes.show(brnAgregarComponente, brnAgregarComponente.getLocationOnScreen().x,
                brnAgregarComponente.getLocationOnScreen().x);
    }// GEN-LAST:event_brnAgregarComponenteActionPerformed

    private void cboEntidadesActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cboEntidadesActionPerformed
        if (cboEntidades.getSelectedIndex() == 0) {
            if (entity.getParent() != null) {
                entity.getParent().removeChild(entity);
            }
            entity.setParent(null);
        } else {
            this.listaPadres.get(cboEntidades.getSelectedIndex()).addChild(entity);
        }
        Principal.instancia.actualizarArbolEscena();
    }// GEN-LAST:event_cboEntidadesActionPerformed

    private void txtEntidadNombreActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtEntidadNombreActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtEntidadNombreActionPerformed

    private void btnNuevoMatPBRActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnNuevoMatPBRActionPerformed
        MaterialNode nuevoMat = new MaterialNode();
        editingMaterial.add(nuevoMat);
        DefaultListModel tmp = (DefaultListModel) lstMaterials.getModel();
        tmp.addElement("(PBR) " + nuevoMat.getNombre());
        lstMaterials.setSelectedIndex(tmp.getSize() - 1);
        activeMaterial = nuevoMat;

        // pnlEditorMaterial.populateMaterialControl(nuevoMat);
        actualizarCboMaterial(); // TODO add your handling code here:
    }// GEN-LAST:event_btnNuevoMatPBRActionPerformed

    private void btnGuardarMaterialActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnGuardarMaterialActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_btnGuardarMaterialActionPerformed

    private JMenu mnuGeometria;
    private JMenu mnuFisica;
    private JMenu mnuEfectos;
    private JMenu mnuSonido;
    private JMenu mnuAnimacion;
    private JMenu mnuComportamiento;
    private JMenu mnuTerreno;
    private JMenu mnuEntorno;
    private JMenu mnuIluminacion;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton brnAgregarComponente;
    private javax.swing.JButton btnAbrir;
    private javax.swing.JButton btnCaraEliminar;
    private javax.swing.JButton btnGuardarMaterial;
    private javax.swing.JButton btnNuevoMatBAS;
    private javax.swing.JButton btnNuevoMatPBR;
    private javax.swing.JButton btnVerticeEliminar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cboEntidades;
    private javax.swing.JComboBox<String> cboMaterial;
    private javax.swing.JCheckBox chkCaraSuave;
    private javax.swing.ButtonGroup groupTipoEntidad;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JList<String> lstCaras;
    private javax.swing.JList<String> lstComponentes1;
    private javax.swing.JList<String> lstGeometrias;
    private javax.swing.JList<String> lstMaterials;
    private javax.swing.JList<String> lstVertices;
    private javax.swing.JPopupMenu mnuComponentes;
    private javax.swing.JPanel pnlCaras;
    private javax.swing.JPanel pnlContMaterial;
    private javax.swing.JPanel pnlGeometria;
    private javax.swing.JPanel pnlInspector;
    private javax.swing.JPanel pnlListaComponentes;
    private javax.swing.JPanel pnlMaterial;
    private javax.swing.JPanel pnlMaterialEdit;
    private javax.swing.JPanel pnlVertices;
    private javax.swing.JPanel pnlVistaPrevia;
    private javax.swing.JScrollPane scrollCaras;
    private javax.swing.JScrollPane scrollGeometria;
    private javax.swing.JScrollPane scrollPropiedades;
    private javax.swing.JScrollPane scrollVertices;
    private javax.swing.JTabbedPane tabGeometria;
    private javax.swing.JTabbedPane tabPnlEntidad;
    private javax.swing.JTextField txtEntidadNombre;
    private javax.swing.JTextField txtVerticeX;
    private javax.swing.JTextField txtVerticeY;
    private javax.swing.JTextField txtVerticeZ;
    // End of variables declaration//GEN-END:variables
}
