/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.renderer.shadow.procesadores;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.ligth.QPointLigth;
import net.qoopo.engine.core.math.Vector3;
import net.qoopo.engine.core.scene.Camera;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.shadow.QProcesadorSombra;
import net.qoopo.engine.renderer.shadow.SoftwareShadowRender;

/**
 * Procesador de sombras para luces puntuales. Crea un mapa cúbico
 *
 * @author alberto
 */
public class QSombraOmnidireccional extends QProcesadorSombra {

    /**
     * Se realiza un Mapeo cubico, 6 renderizadores de sombras uno por cada lado
     * del cubo y 6 mapa de sombras
     */
    private SoftwareShadowRender[] renderSombras;
    private Vector3[] direcciones;

    public QSombraOmnidireccional(Scene mundo, QPointLigth luz, Camera camaraRender, int ancho, int alto) {
        super(mundo, luz, ancho, alto);
        direcciones = new Vector3[6];
        // -------------------------------------------------------
        direcciones[0] = Vector3.of(0, 1, 0); // arriba
        direcciones[1] = Vector3.of(0, -1, 0); // abajo
        direcciones[2] = Vector3.of(0, 0, 1); // adelante
        direcciones[3] = Vector3.of(0, 0, -1); // atras
        direcciones[4] = Vector3.of(-1, 0, 0); // izquierda
        direcciones[5] = Vector3.of(1, 0, 0); // derecha

        Vector3[] direccionesArriba = new Vector3[6];
        direccionesArriba[0] = Vector3.of(0, 0, -1); // arriba
        direccionesArriba[1] = Vector3.of(0, 0, 1); // abajo
        direccionesArriba[2] = Vector3.of(0, 1, 0); // adelante
        direccionesArriba[3] = Vector3.of(0, 1, 0); // atras
        direccionesArriba[4] = Vector3.of(0, 1, 0); // izquierda
        direccionesArriba[5] = Vector3.of(0, 1, 0); // derecha

        String[] nombres = { "Arriba", "Abajo", "Frente", "Atras", "Izquierda", "Derecha" };
        renderSombras = new SoftwareShadowRender[6];
        for (int i = 0; i < 6; i++) {
            renderSombras[i] = new SoftwareShadowRender(SoftwareShadowRender.NO_DIRECCIONALES, mundo, luz, camaraRender, ancho,
                    alto, direcciones[i], direccionesArriba[i]);
            renderSombras[i].setName(nombres[i]);
            renderSombras[i].resize();
            renderSombras[i].clean();
        }
    }

    @Override
    public float factorSombra(Vector3 vector, Entity entity) {
        float factor = 1.0f;
        float factorTmp = 0;
        int c = 0;
        /**
         * Tenemos un inconveniente. Cuando el punto no esta en el lado del
         * cubo, el factor viene como 1 indicando que debe iluminarse, sin
         * embargo no deberiamos tomarlo en cuenta pues sabemos que 1 significa
         * q no esta en ese lado y no deberia iluminarse
         */

        // System.out.println("");
        // System.out.println("");
        // System.out.println("");
        // System.out.println("voy a tomar los valores para un punto");
        try {
            for (int i = 0; i < 6; i++) {
                factorTmp = renderSombras[i].factorSombra(vector, entity);
                if (factorTmp != 1.0f) {
                    c++;
                    factor = factorTmp;
                }
                // factor += factorTmp;
                // System.out.println("I=" + i + " dio factor:" + factorTmp);
                // if (factorTmp != 1.0f) {
                // c++;
                // }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // System.out.println("los puntos q me dieron diferentes valores son " + c);
        return factor;
        // return factor / 6.0f;
        // return factor / c;
    }

    @Override
    public void generarSombras() {
        if (actualizar || dinamico) {
            for (int i = 0; i < 6; i++) {
                try {
                    renderSombras[i].update();
                } catch (Exception e) {

                }
            }
        }
        actualizar = false;
    }

    @Override
    public void destruir() {
        renderSombras = null;
    }

}
