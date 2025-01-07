package net.qoopo.engine.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.transform.QTransformacion;
import net.qoopo.engine.core.math.Cuaternion;
import net.qoopo.engine.core.math.QMatriz3;
import net.qoopo.engine.core.math.QMatriz4;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.util.ComponentUtil;
import net.qoopo.engine.core.util.TempVars;

public class Entity implements Serializable {

    transient private static final HashSet<Integer> usedId = new HashSet<>();

    private static synchronized int findId() {
        int result = 0;
        while (usedId.contains(result)) {
            result++;
        }
        return result;
    }

    public static void printTree(Entity entity, String espacios) {
        System.out.println(espacios + entity.name);
        QTransformacion tg = new QTransformacion();
        tg.desdeMatrix(entity.getMatrizTransformacion(System.currentTimeMillis()));
        System.out.println(espacios + "TL [" + entity.transformacion.toString() + "]");
        System.out.println(espacios + "TG [" + tg.toString() + "]");
        espacios += "    ";
        if (entity.getChilds() != null) {
            for (Entity hijo : entity.getChilds()) {
                printTree((Entity) hijo, espacios);
            }
        }
    }

    protected final List<EntityComponent> components = new ArrayList<>();

    // unico componente que no esta en la lista, todos necesitan este componente
    protected QTransformacion transformacion = new QTransformacion();
    protected String name;
    protected Entity parent;
    protected List<Entity> childs = new ArrayList<>();
    protected boolean toRender = true;
    protected boolean toDelete = false;
    protected long cached_time = 0;
    protected QMatriz4 cachedMatriz;

    // indica si es una entity billboard
    /**
     * Las entidades billboard son planos que siempre apuntan a la cámara. Son
     * usados en los sistemas de particulas En el momento de la trasnformación
     * se calcula automaticamente la rotación para que siempre apunte a la
     * cámara
     */
    protected boolean billboard;

    public Entity() {
        this(null);
    }

    public Entity(String name) {
        this(name, true);
    }

    public Entity(String name, boolean nuevoId) {
        if (nuevoId) {
            int newId = findId();
            usedId.add(newId);
            if (name == null) {
                this.name = "Objeto " + newId;
            } else {
                this.name = name + " " + newId;
            }
        } else {
            if (name == null) {
                int newId = findId();
                usedId.add(newId);
                this.name = "Objeto " + newId;
            } else {
                this.name = name;
            }
        }
    }

    public void addComponent(EntityComponent componente) {
        if (componente == null) {
            return;
        }
        componente.setEntity(this);
        components.add(componente);
    }

    public void removeComponent(EntityComponent componente) {
        components.remove(componente);
    }

    public EntityComponent getComponent(Class<? extends EntityComponent> className) {
        return ComponentUtil.getComponent(this, className);
    }

    public List<? extends EntityComponent> getComponents(Class<? extends EntityComponent> className) {
        return ComponentUtil.getComponents(this, className);
    }

    public void addChild(Entity hijo) {
        if (!childs.contains(hijo)) {
            hijo.setParent(this);
            childs.add(hijo);
        }
    }

    public boolean removeChild(Entity hijo) {
        return childs.remove(hijo);
    }

    @Override
    public Entity clone() {
        // no clono los componentes
        Entity nuevo = new Entity(name, false);
        nuevo.transformacion = this.transformacion.clone();
        for (EntityComponent comp : components) {
            if (comp instanceof Mesh) {
                nuevo.addComponent(((Mesh) comp).clone());
            }
        }
        return nuevo;
    }

    public void move(float x, float y, float z) {
        this.transformacion.getTraslacion().set(x, y, z);
    }

    public void move(QVector3 nuevaPosicion) {
        move(nuevaPosicion.x, nuevaPosicion.y, nuevaPosicion.z);
    }

    public void scale(QVector3 vector) {
        transformacion.getEscala().set(vector);
    }

    public void scale(float x, float y, float z) {
        transformacion.getEscala().set(x, y, z);
    }

    public void scale(float valor) {
        transformacion.getEscala().set(valor, valor, valor);
    }

    public void rotate(double angX, double angY, double angZ) {
        rotate((float) angX, (float) angY, (float) angZ);
    }

    public void rotate(float angX, float angY, float angZ) {
        transformacion.getRotacion().rotarX(angX);
        transformacion.getRotacion().rotarY(angY);
        transformacion.getRotacion().rotarZ(angZ);
    }

    public void aumentarX(float aumento) {
        this.transformacion.getTraslacion().x += aumento;
    }

    public void aumentarY(float aumento) {
        this.transformacion.getTraslacion().y += aumento;

    }

    public void aumentarZ(float aumento) {
        this.transformacion.getTraslacion().z += aumento;
    }

    public void aumentarEscalaX(float aumento) {
        this.transformacion.getEscala().x += aumento;
    }

    public void aumentarEscalaY(float aumento) {
        this.transformacion.getEscala().y += aumento;

    }

    public void aumentarEscalaZ(float aumento) {
        this.transformacion.getEscala().z += aumento;

    }

    public void aumentarRotX(float aumento) {
        this.transformacion.getRotacion().aumentarRotX(aumento);
    }

    public void aumentarRotY(float aumento) {
        this.transformacion.getRotacion().aumentarRotY(aumento);
    }

    public void aumentarRotZ(float aumento) {
        this.transformacion.getRotacion().aumentarRotZ(aumento);
    }

    public QVector3 getDirection() {
        return transformacion.getRotacion().getCuaternion().getRotationColumn(Cuaternion.DIRECCION);
    }

    public QVector3 getLeft() {
        return transformacion.getRotacion().getCuaternion().getRotationColumn(Cuaternion.IZQUIERDA);
    }

    public QVector3 getUp() {
        return transformacion.getRotacion().getCuaternion().getRotationColumn(Cuaternion.ARRIBA);
    }

    /**
     * Mueve el objeto hacia adelante si valor es positivo, o atras si es
     * negativo
     *
     * @param valor
     */
    public void moveForward(float valor) {
        QVector3 tmp = transformacion.getTraslacion().clone();
        tmp.add(getDirection().multiply(-valor));
        move(tmp);
        // la diferencia la suma a los hijos
    }

    /**
     * Mueve el objeto izquierda -derecha
     *
     * @param valor
     */
    public void moveLeft(float valor) {
        QVector3 tmp = transformacion.getTraslacion().clone();
        tmp.add(getLeft().multiply(valor));
        move(tmp);
    }

    /**
     * Mueve el objeto izquierda -derecha
     *
     * @param valor
     */
    public void moveUp(float valor) {
        QVector3 tmp = transformacion.getTraslacion().clone();
        tmp.add(getUp().multiply(valor));
        move(tmp);
    }

    public Entity getParent() {
        return parent;
    }

    /**
     * Devuelve el padre raíz del arbol
     *
     * @return
     */
    public Entity getPadreRaiz() {
        if (parent != null) {
            return parent.getPadreRaiz();
        } else {
            return this;
        }
    }

    public String getRutaPadres() {
        if (parent != null) {
            return parent.getRutaPadres() + "/" + this.name;
        } else {
            return this.name;
        }
    }

    public void setParent(Entity padre) {
        this.parent = padre;
    }

    public List<Entity> getChilds() {
        return childs;
    }

    public void setChilds(List<Entity> hijos) {
        this.childs = hijos;
    }

    /**
     * Permite rotar el objeto para que apunte a la posicion de la camara.
     *
     * @param camaraVista
     */
    public void actualizarRotacionBillboard(QMatriz4 camaraVista) {
        if (isBillboard()) {
            TempVars tv = TempVars.get();
            try {
                transformacion.getRotacion().actualizarCuaternion();

                tv.vector3f1.set(camaraVista.toTranslationVector());
                tv.vector3f1.add(this.transformacion.getTraslacion().clone().multiply(-1));
                // tv.vector3f1.normalize();
                // transformacion.getRotacion().getCuaternion().tv.vect1At(tv.vector3f1,
                // getArriba());

                // metodo tomado de jme3engine
                // coopt left for our own purposes.
                QVector3 xzp = getLeft();
                // The xzp vector is the projection of the tv.vector3f1 vector on the xz plane
                xzp.set(tv.vector3f1.x, 0, tv.vector3f1.z);

                tv.vector3f1.normalize();
                xzp.normalize();
                float cosp = tv.vector3f1.dot(xzp);

                QMatriz3 orient = new QMatriz3();
                orient.set(0, 0, xzp.z);
                orient.set(0, 1, xzp.x * -tv.vector3f1.y);
                orient.set(0, 2, xzp.x * cosp);
                orient.set(1, 0, 0);
                orient.set(1, 1, cosp);
                orient.set(1, 2, tv.vector3f1.y);
                orient.set(2, 0, -xzp.x);
                orient.set(2, 1, xzp.z * -tv.vector3f1.y);
                orient.set(2, 2, xzp.z * cosp);
                transformacion.getRotacion().getCuaternion().fromRotationMatrix(orient);

                transformacion.getRotacion().actualizarAngulos();
            } finally {
                tv.release();
            }
        }
    }

    public QMatriz4 getMatrizTransformacion(long time) {
        try {
            if (time != cached_time || cachedMatriz == null) {
                if (transformacion == null) {
                    transformacion = new QTransformacion();
                }
                cachedMatriz = transformacion.toTransformMatriz();
                cached_time = time;
                if (parent != null) {
                    cachedMatriz = parent.getMatrizTransformacion(time).mult(cachedMatriz);
                }
            }
        } catch (Exception e) {
            System.out.println("Error en la entity " + name);
            e.printStackTrace();
        }
        return cachedMatriz;
    }

    /**
     * Este metodo sera llamado por el motor de fisica despues de actualizar el
     * movimiento de la entity Puede sobrecargar este metodo para personalizar
     * un comportamiento por ejemplo en un personaje de un video juego desee
     * comprobar colision de terreno sin la fisica pero si desea usar la fisica
     * para simular la gravedad y colision con otros objetos del entorno
     */
    public void comprobarMovimiento() {

    }

    public boolean isBillboard() {
        return billboard;
    }

    public void setBillboard(boolean billboard) {
        this.billboard = billboard;
    }

    public void destruir() {
        try {
            transformacion = null;
            if (components != null) {
                for (EntityComponent comp : components) {
                    comp.destruir();
                }
            }
            components.clear();

            // for (Entity hijo : hijos) {
            // hijo.destruir();
            // }
            parent = null;
            cachedMatriz = null;

        } catch (Exception e) {
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String nombre) {
        this.name = nombre;
    }

    public void toLista(List<Entity> lista) {
        if (lista == null) {
            lista = new ArrayList<>();
        }
        lista.add(this);
        if (this.getChilds() != null && !this.getChilds().isEmpty()) {
            for (Entity hijo : this.getChilds()) {
                hijo.toLista(lista);
            }
        }
    }

    public QTransformacion getTransformacion() {
        return transformacion;
    }

    public void setTransformacion(QTransformacion transformacion) {
        this.transformacion = transformacion;
    }

    public boolean isToRender() {
        return toRender;
    }

    public void setToRender(boolean renderizar) {
        this.toRender = renderizar;
    }

    public boolean isToDelete() {
        return toDelete;
    }

    public void setToDelete(boolean eliminar) {
        this.toDelete = eliminar;
    }

    public long getCached_time() {
        return cached_time;
    }

    public void setCached_time(long cached_time) {
        this.cached_time = cached_time;
    }

    public QMatriz4 getCachedMatriz() {
        return cachedMatriz;
    }

    public void setCachedMatriz(QMatriz4 cachedMatriz) {
        this.cachedMatriz = cachedMatriz;
    }

    public List<EntityComponent> getComponents() {
        return components;
    }

}
