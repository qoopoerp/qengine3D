/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.scene;

import net.qoopo.engine.core.renderer.RenderEngine;
import net.qoopo.engine.core.scene.Scene;

/**
 *
 * @author alberto
 */
public abstract class MakeTestScene {

    protected Scene scene;

    public abstract void make(Scene mundo);

    public abstract void accion(int numAccion, RenderEngine render);
}
