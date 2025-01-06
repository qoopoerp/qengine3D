package net.qoopo.engine.core.engine;

import java.time.Duration;

/**
 *
 * @author Alberto
 */
public class EngineTime {

    public static int FPS;
    public static long deltaNano;
    public static long deltaMS;
    public static long deltaS;

    private static long frameStartTime = System.nanoTime();
    private static long frameLastTime = System.nanoTime();
    private static int framesCount;

    public static void update() {
        long currentFrameTime = System.nanoTime();
        if (currentFrameTime - frameStartTime >= 1000000000) {
            FPS = framesCount;
            framesCount = 0;
            frameStartTime = currentFrameTime;
        } else {
            framesCount++;
        }
        deltaNano = currentFrameTime - frameLastTime;
        Duration duration = Duration.ofNanos(deltaNano);
        deltaMS = duration.toMillis(); // deltaNano / 10000;
        deltaS = duration.toSeconds();// deltaNano / 1000000;
        frameLastTime = currentFrameTime;
    }

}
