package net.qoopo.engine.core.engine;

import java.time.Duration;

/**
 * Calcula el tiempo de ejcución de cada actualización del engine
 * 
 * @author Alberto
 */
public class EngineTime {

    public static int FPS;
    /**
     * Tiempo que ha pasado en nano segundos
     */
    public static long deltaNano;
    /**
     * Tiempo que ha pasado en milisgundos
     */
    public static long deltaMS;
    /**
     * Tiempo que ha pasado en segundos
     */
    public static float deltaS;

    private static long frameStartTime = System.nanoTime();
    private static long frameLastTime = System.nanoTime();
    private static int framesCount;

    public static void update() {
        long currentFrameTime = System.nanoTime();
        // si pasó un segundo
        if (currentFrameTime - frameStartTime >= 1000000000) {
            FPS = framesCount;
            framesCount = 0;
            frameStartTime = currentFrameTime;
        } else {
            framesCount++;
        }
        // Calcula los delta, o tiempo que pasó desde la última actualización
        deltaNano = currentFrameTime - frameLastTime;
        Duration duration = Duration.ofNanos(deltaNano);
        deltaMS = duration.toMillis(); 
        deltaS = deltaMS / 1000.0f;
        frameLastTime = currentFrameTime;
    }

}
