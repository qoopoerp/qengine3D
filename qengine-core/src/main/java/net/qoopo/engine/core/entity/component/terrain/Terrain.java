package net.qoopo.engine.core.entity.component.terrain;

import java.util.logging.Logger;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.material.basico.QMaterialBas;
import net.qoopo.engine.core.math.QVector3;

@Getter
@Setter
public abstract class Terrain extends EntityComponent {

    protected static Logger logger = Logger.getLogger("terrain");

    protected QMaterialBas material = new QMaterialBas("terreno");

    // arreglo que tiene la altura, para averigurar la altura en tiempo de ejecucion
    protected float[][] altura;

    protected float width = 0;
    protected float height = 0;
    protected float inicioX = 0;
    protected float inicioZ = 0;

    protected int widthTiles = 0;
    protected int heightTiles = 0;
    protected boolean smooth=true;

    public abstract void build();

    public float getAltura(float x, float z) {
        try {
            float terrenoX = x - this.inicioX;
            float terrenoZ = z - this.inicioZ;
            // if (terrenoX < 0 || terrenoX > ancho) {
            // return 0;
            // }
            // if (terrenoZ < 0 || terrenoZ > largo) {
            // return 0;
            // }

            float gridSquareSize = width / ((float) altura.length - 1);
            int gridX = (int) Math.floor(terrenoX / gridSquareSize);
            int gridZ = (int) Math.floor(terrenoZ / gridSquareSize);
            if (gridX < 0 || gridX > altura.length - 1) {
                return 0;
            }
            if (gridZ < 0 || gridZ > altura.length - 1) {
                return 0;
            }
            float xCoord = (terrenoX % gridSquareSize) / gridSquareSize;
            float zCoord = (terrenoZ % gridSquareSize) / gridSquareSize;
            if (xCoord <= (1 - zCoord)) {
                return barryCentric(QVector3.of(0, altura[gridX][gridZ], 0),
                        QVector3.of(1, altura[gridX + 1][gridZ], 0), QVector3.of(0, altura[gridX][gridZ + 1], 1),
                        QVector3.of(xCoord, zCoord, 0));
            } else {
                return barryCentric(QVector3.of(1, altura[gridX + 1][gridZ], 0),
                        QVector3.of(1, altura[gridX + 1][gridZ + 1], 1), QVector3.of(0, altura[gridX][gridZ + 1], 1),
                        QVector3.of(xCoord, zCoord, 0));
            }
        } catch (Exception e) {
            return Float.NEGATIVE_INFINITY;
        }

    }

    private static float barryCentric(QVector3 p1, QVector3 p2, QVector3 p3, QVector3 pos) {
        float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
        float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
        float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
        float l3 = 1.0f - l1 - l2;
        return l1 * p1.y + l2 * p2.y + l3 * p3.y;
    }

    @Override
    public void destruir() {

    }

}
