/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.scene;

import net.qoopo.engine.core.math.QColor;

/**
 *
 * @author alberto
 */
public class Fog {

    /*
     * public QNeblina neblina = new QNeblina(true, QColor.GRAY, 0.015f);// como 100
     * metros
     * protected QNeblina neblina = new QNeblina(true, QColor.GRAY, 0.05f);// como a
     * 15 metros
     * protected QNeblina neblina = new QNeblina(true, QColor.GRAY, 0.1f);// como a
     * 2 metros
     */
    private boolean active;

    private QColor colour;

    private float density;

    public static Fog NOFOG = new Fog();

    public Fog() {
        active = false;
        this.colour = new QColor(0, 0, 0);
        this.density = 0;
    }

    public Fog(boolean active, QColor colour, float density) {
        this.active = active;
        this.colour = colour;
        this.density = density;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public QColor getColour() {
        return colour;
    }

    public void setColour(QColor colour) {
        this.colour = colour;
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = density;
    }

}
