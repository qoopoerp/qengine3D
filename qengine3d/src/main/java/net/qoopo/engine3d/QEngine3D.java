/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import net.qoopo.engine.core.animation.AnimationEngine;
import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.audio.AudioEngine;
import net.qoopo.engine.core.engine.Engine;
import net.qoopo.engine.core.engine.EngineTime;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.entity.component.UpdatableComponent;
import net.qoopo.engine.core.entity.component.gui.QTecladoReceptor;
import net.qoopo.engine.core.entity.component.ligth.QDirectionalLigth;
import net.qoopo.engine.core.entity.component.particles.Particle;
import net.qoopo.engine.core.entity.component.particles.ParticleEmissor;
import net.qoopo.engine.core.ia.IAEngine;
import net.qoopo.engine.core.input.QInputManager;
import net.qoopo.engine.core.lwjgl.audio.LwjglAudioLoader;
import net.qoopo.engine.core.lwjgl.audio.engine.OpenALAudioEngine;
import net.qoopo.engine.core.lwjgl.renderer.OpenGlRenderer;
import net.qoopo.engine.core.physic.PhysicsEngine;
import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.renderer.RendererFactory;
import net.qoopo.engine.core.renderer.superficie.QJPanel;
import net.qoopo.engine.core.renderer.superficie.Superficie;
import net.qoopo.engine.core.scene.Camera;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.util.QGlobal;
import net.qoopo.engine.core.util.image.ImgUtil;
import net.qoopo.engine.java3d.renderer.QRenderJava3D;
import net.qoopo.engine.jbullet.JBulletPhysicsEngine;
import net.qoopo.engine.renderer.SoftwareRenderer;
import net.qoopo.engine3d.core.asset.model.DefaultModelLoader;
import net.qoopo.engine3d.core.sky.QCielo;
import net.qoopo.engine3d.core.util.Accion;
import net.qoopo.engine3d.core.util.GC;
import net.qoopo.engine3d.engines.ciclodia.QMotorCicloDia;
import net.qoopo.engine3d.engines.fisica.interno.InternalPhysicsEngine;
import net.qoopo.engine3d.engines.ia.DefaultIA;
import net.qoopo.engine3d.engines.render.GuiUTIL;

/**
 * Motor 3D QEngine.
 *
 * Contiene y gestiona los demás motores
 *
 * @author alberto
 */
public class QEngine3D extends Engine implements Runnable {

    // private static Logger logger = Logger.getLogger("test");
    /**
     *
     * Curso LWJGL (Ingles) (Java)
     * https://lwjglgamedev.gitbooks.io/3d-game-development-with-lwjgl/content/
     *
     * Cursos engine 3d en espa;ol (C) https://brakeza.com/indice
     *
     * Curso completo OpenGL C (Ingles) http://ogldev.atspace.co.uk/index.html
     *
     * Transformaciones 3D http://www.songho.ca/opengl/gl_transform.html
     */
    public static QEngine3D INSTANCIA;
    public String titulo = "QMotor3D";

    // private boolean ejecutando = false;
    /**
     * Indica si se realiza alguna modificacion en el monitor para no realizar
     * actualizaciones hasta que la bandera este en false
     */
    private boolean modificando = false;

    // Escena a usar en todos los motores
    private Scene escena;
    // motores
    private PhysicsEngine physicsEngine = null;// el motor de fisica
    private RenderEngine renderEngine = null; // motor de renderizado
    private AudioEngine audioEngine = null;
    private AnimationEngine animationEngine = null;// el motor que procesa las animaciones
    private List<RenderEngine> rendererList = null; // lista de renderizadores, en caso de un editor puede usar varios
                                                    // renderers
    private QMotorCicloDia motorDiaNoche = null;// el motor que procesa el ciclo de dia y noche
    private IAEngine motorInteligencia = null;

    // banderas que indican que motor se va a iniciar cuando se llame al método
    // iniciar del motor 3d
    private boolean iniciarFisica = true;
    private boolean iniciarAnimaciones = true;
    private boolean iniciarDiaNoche = true;
    private boolean iniciarAudio = true;
    private boolean iniciarInteligencia = true;

    private final Thread hiloPrincipal;

    private List<Accion> accionesEjecucion = new ArrayList<>();
    private float horaDelDia = 0;

    {

        AssetManager.get().setModelLoader(new DefaultModelLoader());
        AssetManager.get().setRendererFactory(new RendererFactory() {

            @Override
            public RenderEngine createRenderEngine(Scene scene, String nombre, Superficie superficie, int ancho,
                    int alto) {

                return new SoftwareRenderer(scene, nombre, superficie, ancho, ancho);
            }

        });
    }

    public QEngine3D() {
        ImgUtil.iniciar();
        escena = new Scene();
        hiloPrincipal = new Thread(this, "QENGINE_PRINCIPAL");
        INSTANCIA = this;
    }

    public QEngine3D(String titulo) {
        ImgUtil.iniciar();
        escena = new Scene();
        this.titulo = titulo;
        hiloPrincipal = new Thread(this, "QENGINE_PRINCIPAL");
        INSTANCIA = this;
    }

    public QEngine3D(Scene escena) {
        ImgUtil.iniciar();
        this.escena = escena;
        hiloPrincipal = new Thread(this, "QENGINE_PRINCIPAL");
        INSTANCIA = this;
    }

    public QEngine3D(Scene escena, String titulo) {
        ImgUtil.iniciar();
        this.escena = escena;
        this.titulo = titulo;
        hiloPrincipal = new Thread(this, "QENGINE_PRINCIPAL");
        INSTANCIA = this;
    }

    private void cargar() {
        try {
            ejecutando = true;
            GC.iniciar();// inicia el recolector de basura

            if (iniciarAudio) {
                startAudio();
            }

            animationEngine = new AnimationEngine(getEscena());
            if (iniciarAnimaciones) {
                startAnimation();
            }
            if (iniciarFisica) {
                iniciarFisica();
            }
            if (iniciarInteligencia) {
                startAI();
            }

            renderEngine.start();
            if (rendererList != null && !rendererList.isEmpty()) {
                for (RenderEngine rend : rendererList) {
                    rend.start();
                }
            }

            // el motor de dianoche siempre se ejecuta independientemente
            if (motorDiaNoche != null) {
                motorDiaNoche.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loop() {
        long t = 1000 / (int) QGlobal.MOTOR_RENDER_FPS;
        long t2;
        while (ejecutando) {
            try {
                if (!modificando) {
                    update();
                }
            } catch (Exception e) {

            } finally {
                try {
                    t2 = getDelta();
                    if (t2 < t) {
                        Thread.sleep(t - t2);// disminuye uso de cpu,
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * Ejecuta una actualización
     *
     * @return
     */
    @Override
    public long update() {
        try {
            // ejecuta los componentes que realizan modificadiones
            ejecutarComponentes();
            // ejecuta los motores
            if (animationEngine != null && animationEngine.isEjecutando()) {
                animationEngine.update();
            }
            if (audioEngine != null) {
                audioEngine.update();
            }
            if (physicsEngine != null) {
                physicsEngine.update();
            }

            // ejecuta acciones de usuario
            if (accionesEjecucion != null && !accionesEjecucion.isEmpty()) {
                for (Accion accion : accionesEjecucion) {
                    accion.ejecutar();
                }
            }

            if (rendererList != null && !rendererList.isEmpty()) {
                rendererList.forEach(renderer -> {
                    renderer.update();
                });
            } else {
                if (renderEngine != null) {
                    renderEngine.update();
                }
            }

            // elimina las entidades que estan marcadas para eliminarse (no vivas)
            escena.eliminarEntidadesNoVivas();
            EngineTime.update();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // logger.info("M3D-->" + DF.format(getFPS()) + " FPS");
        tiempoPrevio = System.currentTimeMillis();
        return tiempoPrevio;
    }

    @Override
    public void run() {
        cargar();
        loop();
    }

    public Scene getEscena() {
        return escena;
    }

    public void setEscena(Scene escena) {
        this.escena = escena;
    }

    public AnimationEngine getAnimationEngine() {
        return animationEngine;
    }

    public void setAnimationEngine(AnimationEngine motorAnimacion) {
        this.animationEngine = motorAnimacion;
    }

    public QMotorCicloDia getMotorDiaNoche() {
        return motorDiaNoche;
    }

    public void setMotorDiaNoche(QMotorCicloDia motorDiaNoche) {
        this.motorDiaNoche = motorDiaNoche;
    }

    public AudioEngine getAudioEngine() {
        return audioEngine;
    }

    public void setAudioEngine(AudioEngine motorAudio) {
        this.audioEngine = motorAudio;
    }

    public PhysicsEngine getPhysicsEngine() {
        return physicsEngine;
    }

    public void setPhysicsEngine(PhysicsEngine motorFisica) {
        this.physicsEngine = motorFisica;
    }

    public RenderEngine getRenderEngine() {
        return renderEngine;
    }

    public void setRenderEngine(RenderEngine renderer) {
        this.renderEngine = renderer;
    }

    /**
     * Inicia el motor de física
     */
    public void iniciarFisica() {
        // iniciarFisica(QMotorFisica.FISICA_INTERNO); //interno
        iniciarFisica(PhysicsEngine.FISICA_JBULLET);// jbullet
    }

    /**
     * Inicia el motor de fisica
     *
     * @param simulador
     */
    public void iniciarFisica(int simulador) {
        switch (simulador) {
            case PhysicsEngine.FISICA_INTERNO:
            default:
                // usa el motor interno
                physicsEngine = new InternalPhysicsEngine(getEscena());
                break;
            case PhysicsEngine.FISICA_JBULLET:
                physicsEngine = new JBulletPhysicsEngine(getEscena());
                break;
        }
        physicsEngine.start();
    }

    public void detenerFisica() {
        if (physicsEngine != null) {
            physicsEngine.stop();
            physicsEngine = null;
        }
    }

    /**
     * Inicial el motor de Audio OpenAL
     */
    public void startAudio() {
        audioEngine = new OpenALAudioEngine(escena);
        audioEngine.start();

        AssetManager.get().setAudioLoader(new LwjglAudioLoader());
    }

    public void stopAudio() {
        if (audioEngine != null) {
            audioEngine.stop();
        }
    }

    /**
     * Inicia el motor de animaciones
     */
    public void startAnimation() {
        animationEngine.start();
    }

    public void stopAnimation() {
        if (animationEngine != null) {
            animationEngine.stop();
            // motorAnimacion = null;
        }
    }

    /**
     * Inicia el motor de inteligencia
     */
    public void startAI() {
        motorInteligencia = new DefaultIA(getEscena());
        motorInteligencia.start();
    }

    public void stopAI() {
        if (animationEngine != null) {
            animationEngine.stop();
        }
    }

    /**
     * Configura el motor del ciclo del dia
     *
     * @param cielo
     * @param intervalo
     * @param sol
     * @param horaInicial
     */
    public void configDayCycle(QCielo cielo, long intervalo, QDirectionalLigth sol, float horaInicial) {
        motorDiaNoche = new QMotorCicloDia(cielo, getRenderEngine(), intervalo, sol, horaInicial);
    }

    public void stopDayCycle() {
        if (motorDiaNoche != null) {
            motorDiaNoche.stop();
        }
    }

    /**
     * Configura un renderer em modo pantalla completa
     *
     * @param camara
     */
    public void configurarRendererFullScreen(Camera camara) {
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        configRenderer(device.getDisplayMode().getWidth(), device.getDisplayMode().getHeight(), camara, true);
    }

    /**
     * Configura un renderer en modo pantalla completa
     *
     * @param camara
     * @param tipoRenderer
     */
    public void configurarRendererFullScreen(Camera camara, int tipoRenderer) {
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        configRenderer(device.getDisplayMode().getWidth(), device.getDisplayMode().getHeight(), tipoRenderer, camara,
                true);
    }

    /**
     * Configura un renderer en modo ventana
     *
     * @param ancho
     * @param alto
     * @param camara
     */
    public void configRenderer(int ancho, int alto, Camera camara) {
        configRenderer(ancho, alto, camara, false);
    }

    /**
     * Configura un renderer interno
     *
     * @param ancho
     * @param alto
     * @param camara
     * @param pantallaCompleta
     */
    public void configRenderer(int ancho, int alto, Camera camara, boolean pantallaCompleta) {
        configRenderer(ancho, alto, RenderEngine.RENDER_INTERNO, camara, pantallaCompleta);
    }

    /**
     * Inicia el motor 3D
     */
    @Override
    public void start() {
        hiloPrincipal.start();
    }

    @Override
    public void stop() {
        getRenderEngine().setCargando(true);
        AssetManager.get().free();
        detenerFisica();
        stopAnimation();
        stopAudio();
        stopDayCycle();
        GC.detener();
        ejecutando = false;
        try {
            Thread.sleep(300);
        } catch (InterruptedException ex) {

        }
        // System.exit(0);
    }

    /**
     * Configura un renderer
     *
     * @param ancho
     * @param alto
     * @param tipoRenderer
     * @param camara
     * @param pantallaCompleta
     */
    public void configRenderer(int ancho, int alto, int tipoRenderer, Camera camara, boolean pantallaCompleta) {
        JFrame ventana = new JFrame(titulo);
        ventana.setSize(ancho, alto);
        ventana.setPreferredSize(new Dimension(ancho, alto));
        ventana.setResizable(true);
        ventana.setLayout(new BorderLayout());
        ventana.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        ventana.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                stop();
                super.windowClosing(e); // To change body of generated methods, choose Tools | Templates.
            }
        });
        QJPanel panelDibujo = new QJPanel();
        ventana.add(panelDibujo, BorderLayout.CENTER);
        if (pantallaCompleta) {
            try {
                GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                ventana.setUndecorated(true);
                device.setFullScreenWindow(ventana);
            } catch (Exception e) {
            }
        } else {
            // centra la ventana
            GuiUTIL.centrarVentana(ventana);
        }

        switch (tipoRenderer) {
            default:
            case RenderEngine.RENDER_INTERNO:
                renderEngine = new SoftwareRenderer(escena, new Superficie(panelDibujo), ancho, alto);
                break;
            case RenderEngine.RENDER_JAVA3D:
                renderEngine = new QRenderJava3D(escena, new Superficie(panelDibujo), ancho, alto);
                break;
            case RenderEngine.RENDER_OPENGL:
                renderEngine = new OpenGlRenderer(escena, new Superficie(panelDibujo), ancho, alto);
                break;
        }

        renderEngine.opciones.setForzarResolucion(true);
        renderEngine.setCamara(camara);
        renderEngine.resize();
        ventana.setVisible(true);
        ventana.pack();
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public boolean isIniciarFisica() {
        return iniciarFisica;
    }

    public void setIniciarFisica(boolean iniciarFisica) {
        this.iniciarFisica = iniciarFisica;
    }

    public boolean isIniciarAnimaciones() {
        return iniciarAnimaciones;
    }

    public void setIniciarAnimaciones(boolean iniciarAnimaciones) {
        this.iniciarAnimaciones = iniciarAnimaciones;
    }

    public boolean isIniciarDiaNoche() {
        return iniciarDiaNoche;
    }

    public void setIniciarDiaNoche(boolean iniciarDiaNoche) {
        this.iniciarDiaNoche = iniciarDiaNoche;
    }

    public boolean isIniciarAudio() {
        return iniciarAudio;
    }

    public void setIniciarAudio(boolean iniciarAudio) {
        this.iniciarAudio = iniciarAudio;
    }

    public boolean isIniciarInteligencia() {
        return iniciarInteligencia;
    }

    public void setIniciarInteligencia(boolean iniciarInteligencia) {
        this.iniciarInteligencia = iniciarInteligencia;
    }

    public IAEngine getMotorInteligencia() {
        return motorInteligencia;
    }

    public void setMotorInteligencia(IAEngine motorInteligencia) {
        this.motorInteligencia = motorInteligencia;
    }

    /**
     * Configura que el motor se detenga al presionar ESC
     */
    public void exitonEsc() {
        QInputManager.addKeyboardListener(new QTecladoReceptor() {
            @Override
            public void keyPressed(KeyEvent evt) {
                switch (evt.getKeyCode()) {
                    case KeyEvent.VK_ESCAPE:
                        stop();
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent evt) {

            }
        });
    }

    /**
     * Ejecuta los componentes que modifican geometrias y textures
     *
     */
    private void ejecutarComponentes() {
        try {
            ParticleEmissor emisor; // emisor de particula
            for (Entity entity : escena.getEntities()) {
                for (EntityComponent componente : entity.getComponents()) {
                    if (componente instanceof UpdatableComponent) {
                        if (((UpdatableComponent) componente).isRequierepdate())
                            ((UpdatableComponent) componente).update(getRenderEngine(), escena);
                        // particulas
                    } else if (componente instanceof ParticleEmissor) {
                        emisor = (ParticleEmissor) componente;
                        emisor.emitir(EngineTime.delta);
                        for (Particle particula : emisor.getParticulasNuevas()) {
                            escena.addEntity(particula.objeto);
                        }
                    }

                    // for (Particle particula : emisor.getParticulasEliminadas()) {
                    // escena.eliminarGeometria(particula.objeto);
                    // particula.objeto.renderizar=false;
                    // modificado = true;
                    // }
                    // agrego las particulas nueva y elimino las viejas
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<RenderEngine> getRendererList() {
        return rendererList;
    }

    public void setRendererList(List<RenderEngine> rendererList) {
        this.rendererList = rendererList;
    }

    public List<Accion> getAccionesEjecucion() {
        return accionesEjecucion;
    }

    public void setAccionesEjecucion(List<Accion> accionesEjecucion) {
        this.accionesEjecucion = accionesEjecucion;
    }

    public boolean isModificando() {
        return modificando;
    }

    public void setModificando(boolean modificando) {
        this.modificando = modificando;
    }

    public float getHoraDelDia() {
        return horaDelDia;
    }

    public void setHoraDelDia(float horaDelDia) {
        this.horaDelDia = horaDelDia;
    }

}
