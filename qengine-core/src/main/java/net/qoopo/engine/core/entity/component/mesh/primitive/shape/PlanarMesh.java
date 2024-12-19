/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine.core.entity.component.mesh.primitive.shape;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.generator.height.HeightsGenerator;
import net.qoopo.engine.core.entity.component.mesh.primitive.QShape;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.texture.util.QMaterialUtil;
import net.qoopo.engine.core.util.mesh.QUtilNormales;

/**
 * Genera una malla en un plano que puede alterar sus vertices en función de un
 * generador de altura
 * 
 * @author alberto
 */

public class PlanarMesh extends QShape {

    public static final int EJE_Z = 1;
    public static final int EJE_Y = 2;
    private float ancho;
    private float largo;

    private float delta;
    private float delta2;

    private int eje = EJE_Z;

    private HeightsGenerator heightsGenerator;

    public PlanarMesh(float ancho, float largo, float seccionesAncho, float seccionesLargo) {
        nombre = "Malla";
        material = new QMaterialBas("Malla");
        this.ancho = ancho;
        this.largo = largo;
        this.delta = ancho / seccionesAncho;
        this.delta2 = largo / seccionesLargo;
        build();
    }

    public PlanarMesh(float ancho, float largo, float seccionesAncho, float seccionesLargo,
            HeightsGenerator heightsGenerator) {
        nombre = "Malla";
        material = new QMaterialBas("Malla");
        this.ancho = ancho;
        this.largo = largo;
        this.delta = ancho / seccionesAncho;
        this.delta2 = largo / seccionesLargo;
        this.heightsGenerator = heightsGenerator;
        build();
    }

    public PlanarMesh(boolean wire, float ancho, float largo, float seccionesAncho, float seccionesLargo) {
        nombre = "Malla";
        this.ancho = ancho;
        material = new QMaterialBas("Malla");
        this.largo = largo;
        this.delta = ancho / seccionesAncho;
        this.delta2 = largo / seccionesLargo;
        if (wire) {
            tipo = Mesh.GEOMETRY_TYPE_WIRE;
        }
        build();
    }

    public PlanarMesh(boolean wire, float ancho, float largo, float seccionesAncho, float seccionesLargo,
            HeightsGenerator heightsGenerator) {
        nombre = "Malla";
        this.ancho = ancho;
        material = new QMaterialBas("Malla");
        this.largo = largo;
        this.delta = ancho / seccionesAncho;
        this.delta2 = largo / seccionesLargo;
        if (wire) {
            tipo = Mesh.GEOMETRY_TYPE_WIRE;
        }
        this.heightsGenerator = heightsGenerator;
        build();
    }

    public PlanarMesh(int eje, float ancho, float largo, float seccionesAncho, float seccionesLargo) {
        nombre = "Malla";
        material = new QMaterialBas("Malla");
        this.ancho = ancho;
        this.largo = largo;
        this.delta = ancho / seccionesAncho;
        this.delta2 = largo / seccionesLargo;
        this.eje = eje;
        build();
    }

    public PlanarMesh(int eje, float ancho, float largo, float seccionesAncho, float seccionesLargo,
            HeightsGenerator heightsGenerator) {
        nombre = "Malla";
        material = new QMaterialBas("Malla");
        this.ancho = ancho;
        this.largo = largo;
        this.delta = ancho / seccionesAncho;
        this.delta2 = largo / seccionesLargo;
        this.eje = eje;
        this.heightsGenerator = heightsGenerator;
        build();
    }

    public PlanarMesh(int eje, boolean wire, float ancho, float largo, float seccionesAncho, float seccionesLargo) {
        nombre = "Malla";
        this.ancho = ancho;
        material = new QMaterialBas("Malla");
        this.largo = largo;
        this.delta = ancho / seccionesAncho;
        this.delta2 = largo / seccionesLargo;
        this.eje = eje;
        if (wire) {
            tipo = Mesh.GEOMETRY_TYPE_WIRE;
        }
        build();
    }

    public PlanarMesh(int eje, boolean wire, float ancho, float largo, float seccionesAncho, float seccionesLargo,
            HeightsGenerator heightsGenerator) {
        nombre = "Malla";
        this.ancho = ancho;
        material = new QMaterialBas("Malla");
        this.largo = largo;
        this.delta = ancho / seccionesAncho;
        this.delta2 = largo / seccionesLargo;
        this.eje = eje;
        if (wire) {
            tipo = Mesh.GEOMETRY_TYPE_WIRE;
        }
        this.heightsGenerator = heightsGenerator;
        build();
    }

    @Override
    public void build() {
        deleteData();
        try {
            float height = 0;
            switch (eje) {
                case EJE_Z:

                    for (float z = -largo / 2; z < largo / 2; z += delta2) {
                        for (float x = -ancho / 2; x < ancho / 2; x += delta) {
                            if (heightsGenerator != null) {
                                height = heightsGenerator.getHeight(((float) x + ancho / 2) / ancho,
                                        ((float) z + largo / 2) / largo);
                            }
                            this.addVertex(x,
                                    height,
                                    z,
                                    // textures uv
                                    ((float) x + ancho / 2) / ancho, 1.0f - ((float) z + largo / 2) / largo);

                        }
                    }
                    break;
                case EJE_Y:

                    for (float z = -largo / 2; z < largo / 2; z += delta2) {
                        for (float x = -ancho / 2; x < ancho / 2; x += delta) {
                            if (heightsGenerator != null) {
                                height = heightsGenerator.getHeight(((float) x + ancho / 2) / ancho,
                                        ((float) z + largo / 2) / largo);
                            }
                            this.addVertex(x,
                                    z,
                                    height,
                                    // textures uv
                                    ((float) x + ancho / 2) / ancho, ((float) z + largo / 2) / largo);

                        }
                    }
                    break;
            }

            int planosAncho = (int) (ancho / delta);
            int planosLargo = (int) (largo / delta2);

            for (int j = 0; j < planosLargo - 1; j++) {
                for (int i = 0; i < planosAncho - 1; i++) {
                    try {
                        this.addPoly(material,
                                j * planosAncho + i,
                                j * planosAncho + planosAncho + i,
                                j * planosAncho + i + 1);
                        this.addPoly(material,
                                j * planosAncho + i + 1,
                                j * planosAncho + planosAncho + i,
                                j * planosAncho + planosAncho + i + 1);
                    } catch (Exception ex) {
                        Logger.getLogger(PlanarMesh.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } finally {
            QUtilNormales.calcularNormales(this);
            QMaterialUtil.suavizar(this, true);
        }
    }

    public float getAncho() {
        return ancho;
    }

    public void setAncho(float ancho) {
        this.ancho = ancho;
    }

    public float getLargo() {
        return largo;
    }

    public void setLargo(float largo) {
        this.largo = largo;
    }

    public float getTamanio() {
        return delta;
    }

    public void setDelta(float delta) {
        this.delta = delta;
    }

}