package net.qoopo.engine.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.AABB;
import net.qoopo.engine.core.entity.component.transform.Transform;
import net.qoopo.engine.core.math.Cuaternion;
import net.qoopo.engine.core.math.Matrix3;
import net.qoopo.engine.core.math.Matrix4;
import net.qoopo.engine.core.math.Vector3;
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
        Transform tg = new Transform();
        tg.fromMatrix(entity.getMatrizTransformacion(System.currentTimeMillis()));
        System.out.println(espacios + "TL [" + entity.transform.toString() + "]");
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
    protected Transform transform = new Transform();
    protected String name;
    protected Entity parent;
    protected List<Entity> childs = new ArrayList<>();
    protected boolean toRender = true;
    protected boolean toDelete = false;
    protected long cached_time = 0;
    protected Matrix4 cachedMatriz;

    protected AABB aabb;

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
        nuevo.transform = this.transform.clone();
        for (EntityComponent comp : components) {
            if (comp instanceof Mesh) {
                nuevo.addComponent(((Mesh) comp).clone());
            }
        }
        return nuevo;
    }

    public void move(float x, float y, float z) {
        this.transform.getLocation().set(x, y, z);
    }

    public void move(Vector3 location) {
        move(location.x, location.y, location.z);
    }

    public void scale(Vector3 vector) {
        transform.getScale().set(vector);
    }

    public void scale(float x, float y, float z) {
        transform.getScale().set(x, y, z);
    }

    public void scale(float valor) {
        transform.getScale().set(valor, valor, valor);
    }

    public void rotate(double angX, double angY, double angZ) {
        rotate((float) angX, (float) angY, (float) angZ);
    }

    public void rotate(float angX, float angY, float angZ) {
        // transform.getRotation().rotarX(angX);
        // transform.getRotation().rotarY(angY);
        // transform.getRotation().rotarZ(angZ);
        transform.getRotation().getEulerAngles().set(angX, angY, angZ);
        transform.getRotation().updateCuaternion();

    }

    public void rotate(Vector3 angles) {
        transform.getRotation().setEulerAngles(angles);
    }

    public void aumentarX(float aumento) {
        this.transform.getLocation().x += aumento;
    }

    public void aumentarY(float aumento) {
        this.transform.getLocation().y += aumento;

    }

    public void aumentarZ(float aumento) {
        this.transform.getLocation().z += aumento;
    }

    public void aumentarEscalaX(float aumento) {
        this.transform.getScale().x += aumento;
    }

    public void aumentarEscalaY(float aumento) {
        this.transform.getScale().y += aumento;

    }

    public void aumentarEscalaZ(float aumento) {
        this.transform.getScale().z += aumento;

    }

    public void aumentarRotX(float aumento) {
        this.transform.getRotation().aumentarRotX(aumento);
    }

    public void aumentarRotY(float aumento) {
        this.transform.getRotation().aumentarRotY(aumento);
    }

    public void aumentarRotZ(float aumento) {
        this.transform.getRotation().aumentarRotZ(aumento);
    }

    public Vector3 getDirection() {
        return transform.getRotation().getCuaternion().getRotationColumn(Cuaternion.DIRECCION);
    }

    public Vector3 getLeft() {
        return transform.getRotation().getCuaternion().getRotationColumn(Cuaternion.IZQUIERDA);
    }

    public Vector3 getUp() {
        return transform.getRotation().getCuaternion().getRotationColumn(Cuaternion.ARRIBA);
    }

    /**
     * Mueve el objeto hacia adelante si valor es positivo, o atras si es
     * negativo
     *
     * @param valor
     */
    public void moveForward(float valor) {
        Vector3 tmp = transform.getLocation().clone();
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
        Vector3 tmp = transform.getLocation().clone();
        tmp.add(getLeft().multiply(valor));
        move(tmp);
    }

    /**
     * Mueve el objeto izquierda -derecha
     *
     * @param valor
     */
    public void moveUp(float valor) {
        Vector3 tmp = transform.getLocation().clone();
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
    public void actualizarRotacionBillboard(Matrix4 camaraVista) {
        if (isBillboard()) {
            TempVars tv = TempVars.get();
            try {
                transform.getRotation().updateCuaternion();

                tv.vector3f1.set(camaraVista.toTranslationVector());
                tv.vector3f1.add(this.transform.getLocation().clone().multiply(-1));
                // tv.vector3f1.normalize();
                // transformacion.getRotacion().getCuaternion().tv.vect1At(tv.vector3f1,
                // getArriba());

                // metodo tomado de jme3engine
                // coopt left for our own purposes.
                Vector3 xzp = getLeft();
                // The xzp vector is the projection of the tv.vector3f1 vector on the xz plane
                xzp.set(tv.vector3f1.x, 0, tv.vector3f1.z);

                tv.vector3f1.normalize();
                xzp.normalize();
                float cosp = tv.vector3f1.dot(xzp);

                Matrix3 orient = new Matrix3();
                orient.set(0, 0, xzp.z);
                orient.set(0, 1, xzp.x * -tv.vector3f1.y);
                orient.set(0, 2, xzp.x * cosp);
                orient.set(1, 0, 0);
                orient.set(1, 1, cosp);
                orient.set(1, 2, tv.vector3f1.y);
                orient.set(2, 0, -xzp.x);
                orient.set(2, 1, xzp.z * -tv.vector3f1.y);
                orient.set(2, 2, xzp.z * cosp);
                transform.getRotation().getCuaternion().fromRotationMatrix(orient);

                transform.getRotation().updateEuler();
            } finally {
                tv.release();
            }
        }
    }

    public Matrix4 getMatrizTransformacion(long time) {
        try {
            if (time != cached_time || cachedMatriz == null) {
                if (transform == null) {
                    transform = new Transform();
                }
                cachedMatriz = transform.toMatrix();
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
            transform = null;
            if (components != null) {
                for (EntityComponent comp : components) {
                    comp.destroy();
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

    public Transform getTransform() {
        return transform;
    }

    public void setTransform(Transform transformacion) {
        this.transform = transformacion;
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

    public Matrix4 getCachedMatriz() {
        return cachedMatriz;
    }

    public void setCachedMatriz(Matrix4 cachedMatriz) {
        this.cachedMatriz = cachedMatriz;
    }

    public List<EntityComponent> getComponents() {
        return components;
    }

    public AABB getAABB() {
        if (aabb == null) {
            List<EntityComponent> lstComponents = (List<EntityComponent>) getComponents(Mesh.class);
            if (lstComponents != null && !lstComponents.isEmpty()) {
                Mesh[] meshList = new Mesh[lstComponents.size()];
                for (int i = 0; i < lstComponents.size(); i++) {
                    meshList[i] = (Mesh) lstComponents.get(i);
                }
                if (meshList.length > 0) {
                    aabb = new AABB(meshList);
                }
            }
        }
        return aabb;
    }
}
