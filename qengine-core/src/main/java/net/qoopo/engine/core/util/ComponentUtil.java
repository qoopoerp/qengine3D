/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.util;

import java.util.ArrayList;
import java.util.List;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.entity.component.animation.AnimationComponent;
import net.qoopo.engine.core.entity.component.animation.QCompAlmacenAnimaciones;
import net.qoopo.engine.core.entity.component.animation.Skeleton;
import net.qoopo.engine.core.entity.component.camera.CameraController;
import net.qoopo.engine.core.entity.component.camera.CameraOrbiter;
import net.qoopo.engine.core.entity.component.camera.CameraFirstPersonController;
import net.qoopo.engine.core.entity.component.cubemap.CubeMap;
import net.qoopo.engine.core.entity.component.gui.QTecladoReceptor;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.physics.collision.detector.CollisionShape;
import net.qoopo.engine.core.entity.component.physics.collision.listeners.CollisionListener;
import net.qoopo.engine.core.entity.component.physics.dinamica.QObjetoRigido;
import net.qoopo.engine.core.entity.component.physics.vehiculo.QVehiculo;
import net.qoopo.engine.core.entity.component.terrain.Terrain;
import net.qoopo.engine.core.entity.component.water.Water;

/**
 *
 * @author alberto
 */
public class ComponentUtil {

    // public void eliminarComponenteAnimacion(Entity entity,Class<T>
    // controlType) {
    // List<QComponente> tmp = new ArrayList<>();
    // for (QComponente componente : entity.componentes) {
    // if (controlType.isAssignableFrom(componente.getClass())) {
    // tmp.add(componente);
    // }
    // }
    // entity.componentes.removeAll(tmp);
    // }

    public static EntityComponent getComponent(Entity entity, Class<? extends EntityComponent> className) {
        for (EntityComponent componente : entity.getComponents()) {
            if(className.isAssignableFrom(componente.getClass())){
            // if (componente.getClass().getName().equals(className.getClass().getName())) {
                return componente;
            }
        }
        return null;
    }

    public static EntityComponent getComponent(Entity entity, String clase) {
        for (EntityComponent componente : entity.getComponents()) {
            if (componente.getClass().getName().equals(clase)) {
                return componente;
            }
        }
        return null;
    }

    public static void eliminarComponenteTeclado(Entity entity) {
        List<EntityComponent> tmp = new ArrayList<>();
        for (EntityComponent componente : entity.getComponents()) {
            if (componente instanceof QTecladoReceptor) {
                componente.destruir();
                tmp.add(componente);
            }
        }
        entity.getComponents().removeAll(tmp);
    }

    public static void eliminarComponenteGeometria(Entity entity) {
        List<EntityComponent> tmp = new ArrayList<>();
        for (EntityComponent componente : entity.getComponents()) {
            if (componente instanceof Mesh) {
                componente.destruir();
                tmp.add(componente);
            }
        }
        entity.getComponents().removeAll(tmp);
    }

    public static void eliminarComponenteAnimacion(Entity entity) {
        List<EntityComponent> tmp = new ArrayList<>();
        for (EntityComponent componente : entity.getComponents()) {
            if (componente instanceof AnimationComponent) {
                componente.destruir();
                tmp.add(componente);
            }
        }
        entity.getComponents().removeAll(tmp);
    }

    public static void eliminarComponenteCamaraControl(Entity entity) {
        List<EntityComponent> tmp = new ArrayList<>();
        for (EntityComponent componente : entity.getComponents()) {
            if (componente instanceof CameraController) {
                componente.destruir();
                tmp.add(componente);
            }
        }
        entity.getComponents().removeAll(tmp);
    }

    public static void eliminarComponenteCamaraOrbitar(Entity entity) {
        List<EntityComponent> tmp = new ArrayList<>();
        for (EntityComponent componente : entity.getComponents()) {
            if (componente instanceof CameraOrbiter) {
                componente.destruir();
                tmp.add(componente);
            }
        }
        entity.getComponents().removeAll(tmp);
    }

    public static void eliminarComponenteCamaraPrimeraPersona(Entity entity) {
        List<EntityComponent> tmp = new ArrayList<>();
        for (EntityComponent componente : entity.getComponents()) {
            if (componente instanceof CameraFirstPersonController) {
                componente.destruir();
                tmp.add(componente);
            }
        }
        entity.getComponents().removeAll(tmp);
    }

    public static QCompAlmacenAnimaciones getAlmacenAnimaciones(Entity entity) {
        for (EntityComponent componente : entity.getComponents()) {
            if (componente instanceof QCompAlmacenAnimaciones) {
                return (QCompAlmacenAnimaciones) componente;
            }
        }
        return null;
    }

    // public static QShaderComponente getShader(Entity entity) {
    // for (QComponent componente : entity.getComponents()) {
    // if (componente instanceof QShaderComponente) {
    // return (QShaderComponente) componente;
    // }
    // }
    // return null;
    // }

    public static Skeleton getEsqueleto(Entity entity) {
        for (EntityComponent componente : entity.getComponents()) {
            if (componente instanceof Skeleton) {
                return (Skeleton) componente;
            }
        }
        return null;
    }

    public static CubeMap getMapaCubo(Entity entity) {
        for (EntityComponent componente : entity.getComponents()) {
            if (componente instanceof CubeMap) {
                return (CubeMap) componente;
            }
        }
        return null;
    }

    public static void eliminarComponenteAguaSimple(Entity entity) {
        List<EntityComponent> tmp = new ArrayList<>();
        for (EntityComponent componente : entity.getComponents()) {
            if (componente instanceof Water) {
                componente.destruir();
                tmp.add(componente);
            }
        }
        entity.getComponents().removeAll(tmp);
    }

    public static void eliminarComponenteMapaCubo(Entity entity) {
        List<EntityComponent> tmp = new ArrayList<>();
        for (EntityComponent componente : entity.getComponents()) {
            if (componente instanceof CubeMap) {
                componente.destruir();
                tmp.add(componente);
            }
        }
        entity.getComponents().removeAll(tmp);
    }

    public static Mesh getMesh(Entity entity) {
        for (EntityComponent componente : entity.getComponents()) {
            if (componente instanceof Mesh) {
                return (Mesh) componente;
            }
        }
        return null;
    }

    public static Terrain getTerreno(Entity entity) {
        for (EntityComponent componente : entity.getComponents()) {
            if (componente instanceof Terrain) {
                return (Terrain) componente;
            }
        }
        return null;
    }

    public static CollisionShape getColision(Entity entity) {
        for (EntityComponent componente : entity.getComponents()) {
            if (componente instanceof CollisionShape) {
                return (CollisionShape) componente;
            }
        }
        return null;
    }

    public static QObjetoRigido getFisicoRigido(Entity entity) {
        for (EntityComponent componente : entity.getComponents()) {
            if (componente instanceof QObjetoRigido) {
                return (QObjetoRigido) componente;
            }
        }
        return null;
    }

    public static QVehiculo getVehiculo(Entity entity) {
        for (EntityComponent componente : entity.getComponents()) {
            if (componente instanceof QVehiculo) {
                return (QVehiculo) componente;
            }
        }
        return null;
    }

    public static CameraController getCamaraControl(Entity entity) {
        for (EntityComponent componente : entity.getComponents()) {
            if (componente instanceof CameraController) {
                return (CameraController) componente;
            }
        }
        return null;
    }

    public static CameraOrbiter getCamaraOrbitar(Entity entity) {
        for (EntityComponent componente : entity.getComponents()) {
            if (componente instanceof CameraOrbiter) {
                return (CameraOrbiter) componente;
            }
        }
        return null;
    }

    public static CameraFirstPersonController getCamaraPrimeraPersona(Entity entity) {
        for (EntityComponent componente : entity.getComponents()) {
            if (componente instanceof CameraFirstPersonController) {
                return (CameraFirstPersonController) componente;
            }
        }
        return null;
    }

    public static List<CollisionListener> getColisionListeners(Entity entity) {
        List<CollisionListener> tmp = new ArrayList<>();
        for (EntityComponent componente : entity.getComponents()) {
            if (componente instanceof CollisionListener) {
                tmp.add((CollisionListener) componente);
            }
        }
        return tmp;
    }

    public static EntityComponent getComponente(Entity entity, String clase) {
        for (EntityComponent componente : entity.getComponents()) {
            if (componente.getClass().getName().equals(clase)) {
                return componente;
            }
        }
        return null;
    }
}
