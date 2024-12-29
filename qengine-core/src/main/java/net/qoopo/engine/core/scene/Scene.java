/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.scene;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.entity.component.gui.QMouseReceptor;
import net.qoopo.engine.core.entity.component.gui.QTecladoReceptor;
import net.qoopo.engine.core.entity.component.mesh.util.QUnidadMedida;
import net.qoopo.engine.core.input.QInputManager;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.util.QGlobal;

/**
 * Esta clase describe el universo a procesar. Contiene los objetos del
 * universo: Geometrías, Luces y Cámaras
 *
 * @author alberto
 */
public class Scene implements Serializable {

    private QColor ambientColor = QColor.BLACK;
    private final CopyOnWriteArrayList<Entity> entities = new CopyOnWriteArrayList<>();
    // objetos a ser usados por el motor de fisica
    public QVector3 gravity = QVector3.gravedad.clone();
    public QUnidadMedida UM;
    private boolean blocked = false;
    // para pruebasm luego se agregara el control necesario y lugar correcto
    public Fog fog = Fog.NOFOG;// sin neblina

    public Scene() {
        UM = new QUnidadMedida();
        UM.inicializar(1, QUnidadMedida.METRO);
        gravity = QUnidadMedida.velocidad(QVector3.of(0, UM.convertirUnidades(-10, QUnidadMedida.METRO), 0));
        // actualizo como variable global
        QGlobal.gravedad = this.gravity;
        // INSTANCIA = this;
    }

    public Scene(QUnidadMedida unidadMedida) {
        this.UM = unidadMedida;
        gravity = QUnidadMedida.velocidad(QVector3.of(0, UM.convertirUnidades(-10, QUnidadMedida.METRO), 0));
        QGlobal.gravedad = this.gravity;
        // INSTANCIA = this;
    }

    public Entity getEntity(String name) {
        for (Entity entity : entities) {
            if (entity.getName().equals(name))
                return entity;
        }
        return null;
    }

    public void addEntity(Entity entity) {
        if (entity == null) {
            return;
        }
        if (!entities.contains(entity)) {
            entities.add(entity);
        }
        addChilds(entity);
        // recorro los componentes para ver si no hay que agregar listeners al universo
        if (entity.getComponents() != null) {
            for (EntityComponent componenete : entity.getComponents()) {
                if (componenete instanceof QTecladoReceptor) {
                    QInputManager.addKeyboardListener((QTecladoReceptor) componenete);
                } else if (componenete instanceof QMouseReceptor) {
                    QInputManager.addMouseListener((QMouseReceptor) componenete);
                }
            }
        }
    }

    private void addChilds(Entity objeto) {
        try {
            for (Entity hijo : objeto.getChilds()) {
                if (hijo != null) {
                    addEntity(hijo);
                }
            }
        } catch (Exception e) {

        }
    }

    public void removeEntities() {
        for (Entity entity : getEntities()) {
            removeEntity(entity);
        }
    }

    public void removeEntity(Entity entity) {
        entity.destruir();
        entities.remove(entity);
        // agrego los hijos al universo
        if (entity.getChilds() != null) {
            for (Entity hijo : entity.getChilds()) {
                hijo.setParent(null);
                addEntity(hijo);

            }
            entity.getChilds().clear();
        }

        entity.setChilds(null);
        System.gc();
    }

    public void removeEntityAndChilds(Entity entity) {
        entity.destruir();
        entities.remove(entity);
        // agrego los hijos al universo
        for (Entity hijo : entity.getChilds()) {
            hijo.setParent(null);
            removeEntityAndChilds(hijo);
        }
        entity.getChilds().clear();
        entity.setChilds(null);
        System.gc();
    }

    public void eliminarEntidadSindestruir(Entity entity) {
        entities.remove(entity);
        System.gc();
    }

    // public CopyOnWriteArrayList<Entity> getListaEntidades() {
    public List<Entity> getEntities() {
        esperarDesbloqueo();// espera que se desbloquee
        return entities;
    }

    public void eliminarEntidadesNoVivas() {
        blocked = true;
        synchronized (this) {
            List<Entity> tmp = new ArrayList<>();
            for (Entity entity : this.entities) {
                if (entity.isToDelete()) {
                    tmp.add(entity);
                }
            }
            entities.removeAll(tmp);
        }
        blocked = false;
    }

    public void esperarDesbloqueo() {
        try {
            while (blocked) {
                Thread.sleep(5);// espera mientras se desbloquea
            }
        } catch (Exception e) {

        }
    }

    public QColor getAmbientColor() {
        return ambientColor;
    }

    public void setAmbientColor(QColor colorAmbiente) {
        this.ambientColor = colorAmbiente;
    }

}
