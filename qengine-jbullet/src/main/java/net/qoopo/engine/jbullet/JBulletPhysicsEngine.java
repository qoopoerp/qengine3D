/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.jbullet;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.bulletphysics.BulletGlobals;
import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.broadphase.AxisSweep3_32;
import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.broadphase.SimpleBroadphase;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.narrowphase.PersistentManifold;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.constraintsolver.Generic6DofConstraint;
import com.bulletphysics.dynamics.constraintsolver.HingeConstraint;
import com.bulletphysics.dynamics.constraintsolver.Point2PointConstraint;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.dynamics.vehicle.RaycastVehicle;
import com.bulletphysics.linearmath.Transform;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.physics.collision.listeners.CollisionListener;
import net.qoopo.engine.core.entity.component.physics.dinamica.QObjetoDinamico;
import net.qoopo.engine.core.entity.component.physics.dinamica.QObjetoRigido;
import net.qoopo.engine.core.entity.component.physics.restricciones.QRestriccion;
import net.qoopo.engine.core.entity.component.physics.restricciones.QRestriccionAmortiguador;
import net.qoopo.engine.core.entity.component.physics.restricciones.QRestriccionBisagra;
import net.qoopo.engine.core.entity.component.physics.restricciones.QRestriccionFija;
import net.qoopo.engine.core.entity.component.physics.restricciones.QRestriccionGenerica6DOF;
import net.qoopo.engine.core.entity.component.physics.restricciones.QRestriccionPunto2Punto;
import net.qoopo.engine.core.entity.component.physics.vehiculo.QRueda;
import net.qoopo.engine.core.entity.component.physics.vehiculo.QVehiculo;
import net.qoopo.engine.core.physic.PhysicsEngine;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.util.ComponentUtil;
import net.qoopo.engine.core.util.QGlobal;
import net.qoopo.engine.core.util.QVectMathUtil;

/**
 * Implementacion de JButllet para QEngine
 *
 * @author alberto
 */
public class JBulletPhysicsEngine extends PhysicsEngine {

    private static Logger logger = Logger.getLogger("qjbullet");

    /**
     * interface with Broadphase types
     */
    public enum BroadphaseType {

        /**
         * basic Broadphase
         */
        SIMPLE,
        /**
         * better Broadphase, needs worldBounds , max Object number = 16384
         */
        AXIS_SWEEP_3,
        /**
         * better Broadphase, needs worldBounds , max Object number = 65536
         */
        AXIS_SWEEP_3_32,
        /**
         * Broadphase allowing quicker adding/removing of physics objects
         */
        DBVT;
    }

    protected DecimalFormat df = new DecimalFormat("0.00");

    // collision configuration contains default setup for memory, collision setup.
    // Advanced users can create their own configuration.
    private CollisionConfiguration collisionConfiguration;
    // use the default collision dispatcher. For parallel processing you can use a
    // diffent dispatcher (see Extras/BulletMultiThreaded)
    private CollisionDispatcher dispatcher;

    // the maximum size of the collision world. Make sure objects stay
    // within these boundaries
    // Don't make the world AABB size too large, it will harm simulation
    // quality and performance
    private Vector3f worldAabbMin = new Vector3f(-1000, -1000, -1000);
    private Vector3f worldAabbMax = new Vector3f(1000, 1000, 1000);
    // private final int maxProxies = 1024;
    // private AxisSweep3 overlappingPairCache;
    private BroadphaseInterface broadphase;
    // the default constraint solver. For parallel processing you can use a
    // different solver (see Extras/BulletMultiThreaded)
    private SequentialImpulseConstraintSolver solver;
    private DiscreteDynamicsWorld mundoDinamicoDiscreto;

    // keep track of the shapes, we release memory at exit. make sure to re-use
    // collision shapes among rigid bodies whenever possible!
    // private ObjectArrayList<CollisionShape> formasColision = new
    // ObjectArrayList<>();
    private final Map<String, Entity> mapaAgregados = new HashMap<>();
    private final Map<String, RigidBody> mapaRigidos = new HashMap<>();
    private final Map<String, RaycastVehicle> mapaRayCastVehiculos = new HashMap<>();
    private final Map<String, Integer> mapaRestricciones = new HashMap<>();// mapa para no volver a agregar las
                                                                           // restricciones

    // -------------------------------
    // private BroadphaseType broadphaseType = BroadphaseType.DBVT;
    private BroadphaseType broadphaseType = BroadphaseType.AXIS_SWEEP_3;

    public JBulletPhysicsEngine(Scene universo) {
        super(universo);
        tiempoPrevio = System.currentTimeMillis();
        iniciarMundoFisico();
    }

    public JBulletPhysicsEngine(Scene universo, Vector3f worldAabbMin, Vector3f worldAabbMax) {
        super(universo);
        tiempoPrevio = System.currentTimeMillis();
        this.worldAabbMax = worldAabbMax;
        this.worldAabbMin = worldAabbMin;
        iniciarMundoFisico();
    }

    private void iniciarMundoFisico() {
        collisionConfiguration = new DefaultCollisionConfiguration();
        dispatcher = new CollisionDispatcher(collisionConfiguration);
        // BroadphaseInterface overlappingPairCache = new SimpleBroadphase(maxProxies);
        // the default constraint solver. For parallel processing you can use a
        // different solver (see Extras/BulletMultiThreaded)
        solver = new SequentialImpulseConstraintSolver();
        // overlappingPairCache = new AxisSweep3(worldAabbMin, worldAabbMax,
        // maxProxies);
        switch (broadphaseType) {
            case SIMPLE:
                broadphase = new SimpleBroadphase();
                break;
            case AXIS_SWEEP_3:
                broadphase = new AxisSweep3(worldAabbMin, worldAabbMax);
                break;
            case AXIS_SWEEP_3_32:
                broadphase = new AxisSweep3_32(worldAabbMin, worldAabbMax);
                break;
            case DBVT:
                broadphase = new DbvtBroadphase();
                break;
        }
        mundoDinamicoDiscreto = new DiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfiguration);
        mundoDinamicoDiscreto.setGravity(QVectMathUtil.convertirVector3f(universo.gravity));
        // broadphase.getOverlappingPairCache().setInternalGhostPairCallback(new
        // GhostPairCallback());
    }

    @Override
    public void start() {
        ejecutando = true;
    }

    @Override
    public long update() {
        actualizarMundoDinamico();
        try {
            mundoDinamicoDiscreto.stepSimulation(getDelta() / 1000.0f, 10);
        } catch (Exception e) {
            System.out.println("MF. Error=" + e.getMessage());
            e.printStackTrace();
        }
        actualizarUniversoQEngine();
        limpiarFuerzas();
        detectarColisiones();
        // logger.info("MF-->" + df.format(getFPS()) + " FPS");
        tiempoPrevio = System.currentTimeMillis();
        return tiempoPrevio;
    }

    /**
     * Actualizo la lista de los objetos dinamicos para que corresponda al
     * universo del engine
     */
    private void actualizarMundoDinamico() {
        try {
            actualizarABBS();
            QObjetoRigido actual;
            // primero se agregan objetos rigidos
            for (Entity objeto : universo.getEntities()) {
                if (objeto.isToRender()) {
                    for (EntityComponent componente : objeto.getComponents()) {
                        // objeto rígido
                        if (componente instanceof QObjetoRigido && !((QObjetoRigido) componente).isUsado()) {
                            actual = (QObjetoRigido) componente;
                            if (!mapaAgregados.containsKey(objeto.getName())) {
                                agregarRigido(actual);
                                mapaAgregados.put(objeto.getName(), objeto);
                            } else {
                                actualizarRigido(actual);
                            }
                        } else if (componente instanceof QVehiculo) {
                            if (!mapaRayCastVehiculos.containsKey(objeto.getName())) {
                                RigidBody chasis = agregarRigido(((QVehiculo) componente).getChasis());
                                RaycastVehicle vehiculo = QJBulletUtil.crearVehiculo((QVehiculo) componente, chasis,
                                        mundoDinamicoDiscreto);
                                mapaRayCastVehiculos.put(objeto.getName(), vehiculo);
                                mundoDinamicoDiscreto.addVehicle(vehiculo);
                            } else {
                                // Actualiza la aceleración y frenos del vehículo (actualmente ase asume 4
                                // ruedas
                                RaycastVehicle vehiculo = mapaRayCastVehiculos.get(objeto.getName());
                                QVehiculo comp = (QVehiculo) componente;
                                int c = 0;
                                for (QRueda rueda : comp.getRuedas()) {
                                    if (rueda.isFrontal()) {
                                        // detalenteras
                                        vehiculo.setSteeringValue(comp.getgVehicleSteering(), c);
                                    }
                                    vehiculo.applyEngineForce(comp.getgEngineForce(), c);// aplica aceleracion
                                    vehiculo.setBrake(comp.getgBreakingForce(), c); // aplica freno
                                    c++;
                                }
                            }
                        }
                    }
                }
            }

            // luego se agregan restricciones, pues es necesario que existan los objetos
            // rigidos previamente
            String nombreA;
            String nombreB;
            String idRes;
            // com.bulletphysics.dynamics.constraintsolver.
            for (Entity objeto : universo.getEntities()) {
                if (objeto.isToRender()) {
                    for (EntityComponent componente : objeto.getComponents()) {
                        if (componente instanceof QRestriccion) {
                            nombreA = ((QRestriccion) componente).getA().getEntity().getName();
                            nombreB = ((QRestriccion) componente).getB().getEntity().getName();
                            idRes = nombreA + "x" + nombreB;
                            if (componente instanceof QRestriccionFija) {

                            } else if (componente instanceof QRestriccionPunto2Punto) {
                                if (!mapaRestricciones.containsKey(idRes)) {
                                    Point2PointConstraint joint = new Point2PointConstraint(
                                            mapaRigidos.get(nombreA),
                                            mapaRigidos.get(nombreB),
                                            QVectMathUtil.convertirVector3f(
                                                    ((QRestriccionPunto2Punto) componente).getA().getEntity()
                                                            .getMatrizTransformacion(QGlobal.tiempo)
                                                            .toTranslationVector()),
                                            QVectMathUtil.convertirVector3f(
                                                    ((QRestriccionPunto2Punto) componente).getB().getEntity()
                                                            .getMatrizTransformacion(QGlobal.tiempo)
                                                            .toTranslationVector()));
                                    mundoDinamicoDiscreto.addConstraint(joint);
                                    mapaRestricciones.put(idRes, 1);
                                }
                            } else if (componente instanceof QRestriccionBisagra) {
                                if (!mapaRestricciones.containsKey(idRes)) {
                                    HingeConstraint joint = new HingeConstraint(
                                            mapaRigidos.get(nombreA),
                                            mapaRigidos.get(nombreB),
                                            QJBulletUtil.getTransformacion(
                                                    ((QRestriccionBisagra) componente).getA().getEntity()),
                                            QJBulletUtil.getTransformacion(
                                                    ((QRestriccionBisagra) componente).getB().getEntity()));
                                    mundoDinamicoDiscreto.addConstraint(joint);
                                    mapaRestricciones.put(idRes, 1);
                                }
                            } else if (componente instanceof QRestriccionAmortiguador) {
                            } else if (componente instanceof QRestriccionGenerica6DOF) {

                                if (!mapaRestricciones.containsKey(idRes)) {
                                    Generic6DofConstraint joint = new Generic6DofConstraint(
                                            mapaRigidos.get(nombreA),
                                            mapaRigidos.get(nombreB),
                                            QJBulletUtil.getTransformacion(
                                                    ((QRestriccionGenerica6DOF) componente).getA().getEntity()),
                                            QJBulletUtil.getTransformacion(
                                                    ((QRestriccionGenerica6DOF) componente).getB().getEntity()),
                                            ((QRestriccionGenerica6DOF) componente).isUsarReferenciaLinearA());
                                    // tomado del ejemplo de ragDoll
                                    Vector3f tmp = new Vector3f();
                                    tmp.set(-BulletGlobals.SIMD_PI * 0.8f, -BulletGlobals.SIMD_EPSILON,
                                            -BulletGlobals.SIMD_PI * 0.5f);
                                    joint.setAngularLowerLimit(tmp);
                                    tmp.set(BulletGlobals.SIMD_PI * 0.8f, BulletGlobals.SIMD_EPSILON,
                                            BulletGlobals.SIMD_PI * 0.5f);
                                    joint.setAngularUpperLimit(tmp);
                                    mundoDinamicoDiscreto.addConstraint(joint);
                                    mapaRestricciones.put(idRes, 1);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Crea los contenedores abbs para los que no se definio uno
    private void actualizarABBS() {
        try {
            for (Entity objeto : universo.getEntities()) {
                if (objeto.isToRender()) {
                    QObjetoRigido componenteFisico = ComponentUtil.getFisicoRigido(objeto);
                    // componenteFisico.tieneColision = false;
                    if (componenteFisico != null && componenteFisico.formaColision == null) {
                        Mesh geometria = ComponentUtil.getMesh(objeto);
                        if (geometria != null) {
                            System.out.println(" Se va a crear objeto de colision para " + objeto.getName());
                            componenteFisico.crearContenedorColision(geometria, objeto.getTransformacion());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Agrega un nuevo rígido al mundo dinamico
     *
     * @param rigido
     * @return
     */
    public RigidBody agregarRigido(QObjetoRigido rigido) {
        switch (rigido.tipoDinamico) {
            case QObjetoDinamico.DINAMICO:
            case QObjetoDinamico.ESTATICO:

                RigidBody body = QJBulletUtil.crearRigido(rigido);
                mapaRigidos.put(rigido.getEntity().getName(), body);
                mundoDinamicoDiscreto.addRigidBody(body);
                return body;

            case QObjetoDinamico.CINEMATICO:
                break;

            default:
                break;
        }
        return null;
    }

    /**
     * Actualiza las fuerzas que se hayan agregado al rigido mediante
     * programación y elimina del mundo a las entidades que se eliminaron del
     * universo del engine
     *
     * @param rigido
     */
    private void actualizarRigido(QObjetoRigido rigido) {
        try {
            RigidBody body = mapaRigidos.get(rigido.getEntity().getName());
            if (body != null) {
                // si es de eliminar, lo eliminamos y saltamos actualizaciones
                if (rigido.getEntity().isToDelete()) {
                    logger.info("QJBullet - Eliminando objeto rigido " + rigido.getEntity().getName());
                    mundoDinamicoDiscreto.removeRigidBody(body);
                    mapaRigidos.remove(rigido.getEntity().getName());
                } else {
                    body.applyCentralImpulse(QVectMathUtil.convertirVector3f(rigido.fuerzaTotal));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Actualiza las información de las entidades desde el mundo dinámico al
     * universo de QEngine
     */
    private void actualizarUniversoQEngine() {
        try {
            // ----------------- ACTUALIZACIÓN DE RÍGIDOS
            for (int j = mundoDinamicoDiscreto.getNumCollisionObjects() - 1; j >= 0; j--) {
                CollisionObject obj = mundoDinamicoDiscreto.getCollisionObjectArray().getQuick(j);
                RigidBody body = RigidBody.upcast(obj);
                actualizarEntidadQEngine(body);
            }
            // ----------------- ACTUALIZACIÓN DE VEHÍCULOS
            QVehiculo veh;
            for (Entity objeto : universo.getEntities()) {
                if (objeto.isToRender()) {
                    veh = ComponentUtil.getVehiculo(objeto);
                    if (veh != null) {
                        RaycastVehicle vehiculo = mapaRayCastVehiculos.get(objeto.getName());
                        int c = 0;
                        for (QRueda rueda : veh.getRuedas()) {
                            vehiculo.updateWheelTransform(c, true);
                            Transform trans = vehiculo.getWheelInfo(c).worldTransform;
                            actualizaTransformacionEntidad(trans, rueda.getEntidadRueda());
                            c++;
                        }
                        // actualizo la transformación del rídigo del vehículo
                        // RigidBody body = vehiculo.getRigidBody();
                        // actualizarEntidadQEngine(body);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Actualiza la entity que contiene al componente rígido
     *
     * @param body
     */
    private void actualizarEntidadQEngine(RigidBody body) {
        if (body != null && body.getMotionState() != null) {
            Transform trans = new Transform();
            body.getMotionState().getWorldTransform(trans);
            Object objeto = body.getUserPointer();
            if (objeto != null) {
                actualizaTransformacionEntidad(trans, ((Entity) objeto));
            } else {
                System.out.println("NO TIENE ASOCIADO UN OBJETO DEL ENGINE NUESTRO !!!!");
            }
        }
    }

    /**
     * Actualiza la transformación desde el mundo dinámico al mundo del engine
     *
     * @param trans
     * @param entity
     */
    private void actualizaTransformacionEntidad(Transform trans, Entity entity) {
        Quat4f rotacion = new Quat4f();
        try {
            // actualizacion de la posicion
            entity.move(trans.origin.x, trans.origin.y, trans.origin.z);
            // actualizacion de la rotación
            trans.getRotation(rotacion);
            entity.getTransformacion().getRotacion().getCuaternion().set(rotacion.x, rotacion.y, rotacion.z,
                    rotacion.w);
            entity.getTransformacion().getRotacion().actualizarAngulos();
            // aplico control de posicion propio de este engine
            entity.comprobarMovimiento();
        } catch (Exception e) {
            System.out.println("error:" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Detecta las colisiones y lanza los disparadores
     */
    private void detectarColisiones() {
        try {
            int numManiFold = mundoDinamicoDiscreto.getDispatcher().getNumManifolds();
            for (int i = 0; i < numManiFold; i++) {
                PersistentManifold contacto = mundoDinamicoDiscreto.getDispatcher().getManifoldByIndexInternal(i);

                RigidBody body0 = RigidBody.upcast((CollisionObject) contacto.getBody0());
                RigidBody body1 = RigidBody.upcast((CollisionObject) contacto.getBody1());

                Object objeto0 = body0.getUserPointer();
                Object objeto1 = body1.getUserPointer();
                if (objeto0 != null) {
                    List<CollisionListener> lista = ComponentUtil.getColisionListeners((Entity) objeto0);
                    for (CollisionListener list : lista) {
                        list.colision((Entity) objeto0, (Entity) objeto1);
                    }
                }
                if (objeto1 != null) {
                    List<CollisionListener> lista = ComponentUtil.getColisionListeners((Entity) objeto1);
                    for (CollisionListener list : lista) {
                        list.colision((Entity) objeto1, (Entity) objeto0);
                    }
                }

                // //puntos de contacto
                // int numContacts = contacto.getNumContacts();
                // for (int j = 0; j < numContacts; j++) {
                // ManifoldPoint pt = contacto.getContactPoint(j);
                // if (pt.getDistance() < 0.f) {
                // Vector3f ptA = new Vector3f();
                // ptA = pt.getPositionWorldOnA(ptA);
                // Vector3f ptB = new Vector3f();
                // ptB = pt.getPositionWorldOnB(ptB);
                //
                // Vector3f normalOnB = pt.normalWorldOnB;
                // }
                // }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Limpia las fuerzas que se agregaron mediante código
     */
    private void limpiarFuerzas() {
        try {
            QObjetoRigido actual;
            for (Entity objeto : universo.getEntities()) {
                if (objeto.isToRender()) {
                    QObjetoRigido rigido = ComponentUtil.getFisicoRigido(objeto);
                    if (rigido != null) {
                        rigido.limpiarFuezas();
                    }
                }
            }
        } catch (Exception e) {
        }
    }
}
