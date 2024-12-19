/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.renderer.shadow.procesadores;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.ligth.QDirectionalLigth;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.scene.Camera;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.shadow.QProcesadorSombra;
import net.qoopo.engine.renderer.shadow.SoftwareShadowRender;

/**
 * Procesador de sombras en cascada para Luces Direccionales.
 *
 * @author alberto
 */
public class QSombraDireccionalCascada extends QProcesadorSombra {

    // varios rende de sombras
    private SoftwareShadowRender[] renderSombra;
    private int numeroCascadas = 3;

    public QSombraDireccionalCascada(int numeroCascadas, Scene mundo, QDirectionalLigth luz, Camera camaraRender,
            int ancho, int alto) {
        super(mundo, luz, ancho, alto);
        this.numeroCascadas = numeroCascadas;
        renderSombra = new SoftwareShadowRender[numeroCascadas];
        for (int i = 0; i < numeroCascadas; i++) {
            try {
                renderSombra[i] = new SoftwareShadowRender(SoftwareShadowRender.DIRECIONALES, mundo, luz, camaraRender, ancho,
                        alto);
                renderSombra[i].setCascada(true);
                renderSombra[i].setCascada_indice(i + 1);
                // renderSombra[i].setCascada_indice(i);
                renderSombra[i].setCascada_tamanio(numeroCascadas);
                renderSombra[i].setNombre("Cascada " + (i + 1));
                renderSombra[i].resize();
                renderSombra[i].clean();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public float factorSombra(QVector3 vector, Entity entity) {
        float factor = 0;
        // float tmp = 0;
        // int n = 0;
        try {
            for (int i = 0; i < numeroCascadas; i++) {
                factor += renderSombra[i].factorSombra(vector, entity);
                // if (vector.z < renderSombra[i].getDistanciaCascada()) {
                // factor = renderSombra[i].factorSombra(vector, entity);
                // if (factor > 0) {
                // break;
                // }
                // tmp = renderSombra[i].factorSombra(vector, entity);
                // factor += tmp;
                // if (tmp > 0) {
                // n++;
                // }
            }
            // }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return factor / n;
        return factor / numeroCascadas;
        // return factor;
    }

    @Override
    public void generarSombras() {
        try {
            if (actualizar || dinamico) {
                for (int i = 0; i < numeroCascadas; i++) {
                    renderSombra[i].update();
                }
            }
            actualizar = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actualizarDireccion(QVector3 direccion) {
        try {
            for (int i = 0; i < numeroCascadas; i++) {
                renderSombra[i].setDireccion(direccion);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destruir() {
        renderSombra = null;
    }

}
