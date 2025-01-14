/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.engines.ciclodia;

import java.util.logging.Logger;

import net.qoopo.engine.core.engine.Engine;
import net.qoopo.engine.core.entity.component.ligth.QDirectionalLigth;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine3d.QEngine3D;
import net.qoopo.engine3d.core.sky.QCielo;

/**
 * Controla el ciclo de día y noche en el mundo
 *
 * @author alberto
 */
public class QMotorCicloDia extends Engine {
    private static Logger logger = Logger.getLogger("day-engine");

    protected Scene scene;
    /**
     * Cielo que se modificara para rotar las nubes y cambiar el color de
     * acuerdo a la hora. Y mezclar las textures para el cambio
     */
    private QCielo cielo;

    protected boolean ejecutando = false;

    /**
     * Intervalo de dia entero en segundos
     */
    private float horaDelDia = 0;
    private boolean dia = true;
    private final float maximo = 0.5f;// iluminacion maxima al medio dia
    private final float minimo = 0.15f;
    private final QDirectionalLigth sol;
    private final Thread hiloActualizacion;
    private final float angulo = 360 / 24;
    private final QVector3 direccionSolOriginal = QVector3.of(0, 1, 0);

    public QMotorCicloDia(QCielo cielo, Scene scene, long duracionDiaEnSegundos, QDirectionalLigth sol,
            float horaInicial) {
        this.scene = scene;
        this.cielo = cielo;
        this.sol = sol;
        sol.getDirection().set(direccionSolOriginal);
        this.horaDelDia = horaInicial;
        QEngine3D.INSTANCIA.setHoraDelDia(horaDelDia);
        hiloActualizacion = new Thread(new Runnable() {
            @Override
            public void run() {
                // sera cada media hora segun intervalo
                // long t = 1000 * duracionHoraEnSegundos / 2;
                long t = 1000 * duracionDiaEnSegundos / 24 / 2;
                logger.info("MDN--> El intervalo de ejecución=" + t);
                while (ejecutando) {
                    update();
                    try {
                        Thread.sleep(t);// disminuye uso de cpu,
                    } catch (Exception e) {

                    }
                }
            }
        });
    }

    @Override
    public void start() {
        ejecutando = true;
        hiloActualizacion.start();
    }

    @Override
    public void stop() {
        ejecutando = false;
        try {
            Thread.sleep(300);
        } catch (Exception e) {

        }
    }

    /**
     * Calcula el valor de la ilumincaicon amiental y la dirección del sol
     */
    private void calcularIluminacionAmbiental() {
        try {
            dia = horaDelDia >= 6 && horaDelDia <= 18f;

            if (horaDelDia > 24) {
                horaDelDia = 0;
            }

            QEngine3D.INSTANCIA.setHoraDelDia(horaDelDia);
            // calculo la iluminacion
            if (dia) {
                // el dia tiene 12 horas
                // 2 periodos de 6 horas donde se va incrementando hasta el maximo y luego se va
                // disminuyendo
                sol.getDirection().set(direccionSolOriginal);
                sol.getDirection().rotateZ(-(float) Math.toRadians(horaDelDia * angulo));
                sol.setEnable(true);
                if (horaDelDia < 12) {
                    // antes del medio dia
                    float v = maximo * (horaDelDia - 6) / 12 + minimo;
                    scene.setAmbientColor(new QColor(v, v, v));
                } else {
                    float v = maximo - maximo * (horaDelDia - 12) / 12;
                    scene.setAmbientColor(new QColor(v, v, v));
                }

                // System.out.println("luz ambiente=" + render.ambient);
            } else {
                // si es de noche seteo una iluminacin global minima
                scene.setAmbientColor(new QColor(minimo, minimo, minimo));
                sol.setEnable(false);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void actualizarCielo() {
        // (float) Math.toRadians(horaDelDia * angulo)
        // rota el cielo de acuerod a la hora, dando la sencacion que el planeta rota
        // alrededor de su propio eje
        if (cielo != null) {
            cielo.getEntidad().rotate(0, Math.toRadians(horaDelDia * angulo), 0);
            // actualiza la textura del cielo
            actualizarTexturaCielo();
        }
    }

    private void actualizarTexturaCielo() {
        try {
            // valido rangos de transicion
            // madrugada
            if (cielo != null) {
                if (horaDelDia >= 5 && horaDelDia <= 7) {
                    // el tiempo de transicion es de 2 horas, por eso divido para 2, resto 5 por ser
                    // la hora inicial de la transicion
                    cielo.setRazon(1.0f - (horaDelDia - 5) / 2);
                } else if (horaDelDia >= 18 && horaDelDia <= 20) {
                    // el tiempo de transicion es de 2 horas, por eso divido para 2, resto 18 por
                    // ser la hora incial de la transicion, y resto de 1 porq quiero que sea
                    // descendente
                    cielo.setRazon((horaDelDia - 18) / 2);
                } else if (dia) {
                    // dia
                    cielo.setRazon(0);
                } else { // noche
                    cielo.setRazon(1);
                }

            }
        } catch (Exception e) {
        }
    }

    @Override
    public long update() {
        horaDelDia += 0.5f;
        calcularIluminacionAmbiental();
        actualizarCielo();
        QEngine3D.INSTANCIA.setHoraDelDia(horaDelDia);

        logger.info("MDN-->" + DF.format(getFPS()) + " FPS");
        tiempoPrevio = System.currentTimeMillis();
        return tiempoPrevio;
    }
}
