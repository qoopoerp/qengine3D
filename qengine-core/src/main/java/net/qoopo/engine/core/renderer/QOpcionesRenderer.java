/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.renderer;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author alberto
 */
@Getter
@Setter
public class QOpcionesRenderer {

    public static final int VISTA_WIRE = 0;
    public static final int VISTA_FLAT = 1;
    public static final int VISTA_PHONG = 2;

    private int tipoVista = VISTA_PHONG;
    private boolean material;
    private boolean sombras;
    private boolean dibujarGrid = true;
    private boolean dibujarLuces = false;
    private boolean dibujarCarasTraseras = false;// inverso de BackFaceCulling
    private boolean forzarSuavizado = false;
    private boolean forzarResolucion = false;
    private int ancho = -1;
    private int alto = -1;
    private boolean zSort = true;
    private boolean normalMapping = true;
    // si es diferido, pinta despues al finalizar el raster
    private boolean defferedShadding = true;

    public QOpcionesRenderer() {
        tipoVista = VISTA_PHONG;
        material = true;
        sombras = false;
    }

    public QOpcionesRenderer(boolean material, boolean sombras) {
        this.material = material;
        this.sombras = sombras;
    }

}
