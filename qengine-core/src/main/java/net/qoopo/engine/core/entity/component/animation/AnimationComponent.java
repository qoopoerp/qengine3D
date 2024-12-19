/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.animation;

import java.util.ArrayList;
import java.util.List;

import net.qoopo.engine.core.animation.AnimationFrame;
import net.qoopo.engine.core.animation.AnimationPair;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.entity.component.transform.QTransformacion;
import net.qoopo.engine.core.math.QMatriz4;
import net.qoopo.engine.core.util.QGlobal;

/**
 * Componente de Animación Controla la animacion de cada entity. Contiene un
 * conjunto de frames llamadas KeyFrames, los cuales marcan las transformaciones
 * en una determinada marca de tiempo
 *
 * @author alberto
 */
public class AnimationComponent extends EntityComponent {

    private String nombre = "Animación";

    /**
     * Duracion de la animacion
     */
    private float duracion;
    /**
     * El tiempo actual de la animacion
     */
    private float tiempo = 0;

    /**
     * Bandera que indica que si la animacion debe repetirse
     */
    private boolean loop = true;

    /**
     * Lista de los frames/keyframes de la animacion
     */
    private List<AnimationFrame> listaFrames = new ArrayList<>();

    /**
     * El frame actual con las transformaciones a aplicar
     */
    private AnimationFrame frameActual = new AnimationFrame();

    public AnimationComponent() {
    }

    public void reiniciar() {
        tiempo = 0;
    }

    /**
     * Crea una animacion que contiene las contrasformaciones originales de los
     * huesos, para mostrar la posicion POSE
     *
     * @param esqueleto
     * @return
     */
    public static AnimationComponent crearAnimacionPose(Skeleton esqueleto) {
        AnimationComponent animacion = new AnimationComponent();
        animacion.setNombre("Pose");
        animacion.setLoop(true);
        AnimationFrame qFrame = new AnimationFrame(0.00f);
        for (Bone hueso : esqueleto.getHuesos()) {
            QMatriz4 mat4 = hueso.getTransformacion().toTransformMatriz();
            QTransformacion transformacion = new QTransformacion();
            transformacion.desdeMatrix(mat4);
            qFrame.agregarPar(new AnimationPair(hueso, transformacion));
        }
        animacion.addFrame(qFrame);
        return animacion;
    }

    public AnimationComponent(float duracion) {
        this.duracion = duracion;
    }

    /**
     * Agrea un frame a la lista de frames
     *
     * @param animacion
     */
    public void addFrame(AnimationFrame animacion) {
        listaFrames.add(animacion);
    }

    public void deleteFrame(AnimationFrame animacion) {
        listaFrames.remove(animacion);
    }

    public List<AnimationFrame> getListaFrames() {
        return listaFrames;
    }

    public void setListaFrames(List<AnimationFrame> listaFrames) {
        this.listaFrames = listaFrames;
    }

    /**
     * Realiza los calculos para cambiar de frame
     *
     * @param marcaTiempo
     */
    public void updateAnim(float marcaTiempo) {
        // float tiempoPrevio = tiempo;
        this.tiempo = marcaTiempo;
        // calcula el tiempo transcurrido
        if (tiempo > duracion) {
            if (loop) {
                tiempo %= duracion;
            } else {
                // tiempo = tiempoPrevio;
                tiempo = listaFrames.get(listaFrames.size() - 1).getMarcaTiempo();
            }
        }
        calcularFrameActual();
        procesarFrame(getFrameActual());
    }

    // /**
    // *
    // *
    // * @param deltaSegundos
    // */
    // public void incrementar(float deltaSegundos) {
    // tiempo += deltaSegundos;
    // updateAnim(tiempo);
    // }
    /**
     * Realiza la aplicacion de la transformacion que corresponde al frame
     * actual de la animacion
     *
     * @param frame
     */
    public void procesarFrame(AnimationFrame frame) {
        if (frame == null) {
            return;
        }

        // ahora recorremos los pares para cambiar la posicion y rotacion de cada uno
        frame.getParesModificarAnimacion().forEach(par -> {
            par.getEntidad().setTransformacion(par.getTransformacion());
        });
    }

    /**
     * Calcula el frame actual interpolando las posiciones de los keyFrames
     */
    public void calcularFrameActual() {
        if (listaFrames != null) {
            AnimationFrame[] previoYsiguiente = obtenerPrevioySiguiente();
            if (QGlobal.ANIMACION_INTERPOLAR) {
                float progresion = calcularProgresion(previoYsiguiente[0], previoYsiguiente[1]);
                frameActual = interpolar(previoYsiguiente[0], previoYsiguiente[1], progresion);
            } else {
                frameActual = previoYsiguiente[0];
            }
        }
    }

    /**
     * Calcula un Frame interpolando dos frames
     *
     * @param previo
     * @param siguiente
     * @param progresion
     * @return
     */
    private AnimationFrame interpolar(AnimationFrame previo, AnimationFrame siguiente, float progresion) {
        AnimationFrame nuevo = new AnimationFrame(tiempo);
        for (int i = 0; i < previo.getParesModificarAnimacion().size(); i++) {
            if (siguiente.getParesModificarAnimacion().size() > i) {
                // si tiene el mismo tama;o (deberia ser asi)
                Entity hueso = previo.getParesModificarAnimacion().get(i).getEntidad();
                QTransformacion origen = previo.getParesModificarAnimacion().get(i).getTransformacion();
                QTransformacion destino = siguiente.getParesModificarAnimacion().get(i).getTransformacion();
                if (siguiente.getParesModificarAnimacion().get(i).getEntidad().getName().equals(hueso.getName())) {
                    nuevo.agregarPar(new AnimationPair(hueso, QTransformacion.interpolar(origen, destino, progresion)));
                } else {
                    System.out.println(
                            "ERROR AL INTERPOLAR, LAS ENTIDADES NO ESTAN EN LAS MISMAS POSICIONES EN LOS DOS KEYFRAMES");
                }
            } else {
                // si tienen diferetes tamanios (no deberia ser asi, a menos que el sigiente
                // frame no se mueva el hueso y no se agrego a la animacion)
                System.out.println("ERROR LA INTERPOLAR, LOS KEYFRAMES NO TIENEN LOS MISMOS PARES ");
            }
        }
        return nuevo;
    }

    /**
     * Calcula la progresion que se usara para la interpolacion de las
     * transformaciones
     *
     * @param previo
     * @param siguiente
     * @return
     */
    private float calcularProgresion(AnimationFrame previo, AnimationFrame siguiente) {
        float totalTime = Math.abs(siguiente.getMarcaTiempo() - previo.getMarcaTiempo());
        float currentTime = tiempo - previo.getMarcaTiempo();
        return currentTime / totalTime;
    }

    /**
     * Obtiene los frames previo y siguiente de la animacion de acuerdo al
     * tiempo actual. Con estos 2 keyframes se interpolara sus transformaciones
     * para obtener una animacion mas fluida
     *
     * @return
     */
    private AnimationFrame[] obtenerPrevioySiguiente() {
        if (listaFrames != null) {
            AnimationFrame previousFrame = listaFrames.get(0);
            AnimationFrame nextFrame = listaFrames.get(0);
            for (int i = 1; i < listaFrames.size(); i++) {
                nextFrame = listaFrames.get(i);
                if (nextFrame.getMarcaTiempo() >= tiempo) {
                    break;
                }
                previousFrame = listaFrames.get(i);
            }
            return new AnimationFrame[] { previousFrame, nextFrame };
        }
        return null;
    }

    public AnimationFrame getFrame(int frame) {
        return listaFrames.get(frame);
    }

    /**
     * Devuelve el frame actual con las transformaciones a ser aplicadas
     *
     * @return
     */
    public AnimationFrame getFrameActual() {
        return frameActual;
    }

    public float getDuracion() {
        return duracion;
    }

    public void setDuracion(float duracion) {
        this.duracion = duracion;
    }

    public float getTiempo() {
        return tiempo;
    }

    public void setTiempo(float tiempo) {
        this.tiempo = tiempo;
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public void destruir() {
        try {
            for (AnimationFrame frame : listaFrames) {
                frame.destruir();
            }
            listaFrames.clear();
        } catch (Exception e) {

        }
        listaFrames = null;
    }

}
