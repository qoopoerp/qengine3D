/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.juegotest.generadores;

import java.io.File;
import java.io.FileNotFoundException;

import net.qoopo.engine.core.assets.AssetManager;
import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.util.QGlobal;

/**
 * Clase para crear personajes del juego
 *
 * @author alberto
 */
public class GeneradorArmas {

    public boolean shift = false;

    public static Entity crearAK47() {

        // QGeometria geometria = CargaEstatica.cargarWaveObject(new
        // File("assets/"+
        // "models/obj/ARMAS/AK47_Free/ak_body/aknewlow.obj")).get(0);
        //
        // Entity arma = new Entity("pistola");
        // arma.agregarComponente(geometria);
        // return arma;
        Entity arma = null;
        try {
            arma = AssetManager.get()
                    .loadModel(new File("assets/models/obj/ARMAS/AK47_Free/ak_body/aknewlow.obj"));
            arma.setName("Pistola");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return arma;
    }

}
