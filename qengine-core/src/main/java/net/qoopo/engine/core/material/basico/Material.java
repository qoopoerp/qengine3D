package net.qoopo.engine.core.material.basico;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.entity.component.cubemap.CubeMap;
import net.qoopo.engine.core.material.AbstractMaterial;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.texture.Texture;

/**
 * Materiales básicos
 *
 * @author alberto
 */
@Getter
@Setter
public class Material extends AbstractMaterial {

    // propiedades basadas en PBR
    private float metalico = 0.0f;// reflectividad. 1= max reflectivo menos color difuso, 0 no reflectivo mas
                                  // color difuso
    private float rugosidad = 0.5f;
    // private float especular = 0.0f;

    // factor de luz emitida del propio material
    private float factorEmision = 0;
    // private float factorNormal = 1;

    // ***************** TRANSPARENCIA *********************
    private boolean transparencia = false;
    // transparencia, 0 transparente 1 solido
    private float transAlfa = 1.0f;
    private QColor colorTransparente = null;

    // ***************** ESPECULARIDAD *********************
    // nivel de brillo, maximo 500
    private int specularExponent = 50;
    // Color de luz especular
    private QColor colorEspecular = QColor.WHITE.clone();

    // ***************** ENTORNO *********************
    private int tipoMapaEntorno = CubeMap.FORMATO_MAPA_CUBO;// 1. Mapa cubico, 2. Mapa HDRI,

    // ***************** REFLEXION *********************
    private boolean reflexion = false;

    // ***************** REFRACCIÓN *********************
    private boolean refraccion = false;
    private float indiceRefraccion = 0.0f;// indice de refraccion, aire 1, agua 1.33, vidrio 1.52, diamante 2.42,
    // ---------------------------------------------------

    // ***************** SOMBRAS *********************
    private boolean sombrasProyectar = true;
    private boolean sombrasRecibir = true;
    private boolean sombrasSoloProyectarSombra = false;// renderiza la sombra pero no renderiza el objeto, como si fuera
                                                       // 100% transparente

    // ***************** MAPAS **********************
    // mapas
    private Texture mapaColor;// ok
    private Texture mapaNormal;// ok
    private Texture mapaEmisivo;// ok
    private Texture mapaTransparencia;// ok
    private Texture mapaDesplazamiento;// Muerto
    private Texture mapaEntorno;// ok. textura usada para el mapeo del entorno, reflexiones y refracciones
    private Texture mapaIrradiacion;// textura usada para la iluminacion de parte del entorno (PBR)
    private Texture mapaSAO;// Oclusion ambiental (sombras)
    private Texture mapaRugosidad;
    private Texture mapaEspecular;
    private Texture mapaMetalico;

    public Material() {
    }

    public Material(QColor difusa, int exponenteEspecular) {
        this.color = difusa;
        this.specularExponent = exponenteEspecular;
    }

    public Material(String name) {
        this.nombre = name;
    }

    /**
     * Crea un material usando la textura dada. Para la textura se crea un proxy
     * simple sin proceso
     *
     * @param textura
     * @param specularExponent
     */
    public Material(Texture textura, int specularExponent) {
        this.specularExponent = specularExponent;
        this.mapaColor = textura;
    }

    public Material(Texture textura) {
        this.mapaColor = textura;
    }

    public void destruir() {
        color = null;
        if (mapaColor != null) {
            mapaColor.destruir();
            mapaColor = null;
        }
        if (mapaNormal != null) {
            mapaNormal.destruir();
            mapaNormal = null;
        }
    }

    public void clearColorMap() {
        mapaColor = null;
    }

    public void clearNormalMap() {
        mapaNormal = null;
    }

    public void clearSpecularMap() {
        mapaEspecular = null;
    }

    public void clearEmisionMap() {
        mapaEmisivo = null;
    }

    public void clearEnviromentMap() {
        mapaEntorno = null;
    }

    public void clearSaoMap() {
        mapaSAO = null;
    }

    public void clearMetallicMap() {
        mapaMetalico = null;
    }

    public void clearDisplacementMap() {
        mapaDesplazamiento = null;
    }

    public void clearTransparencyMap() {
        mapaTransparencia = null;
    }

    public void clearRugoussMap() {
        mapaRugosidad = null;
    }

    public void clearIrradationMap() {
        mapaIrradiacion = null;
    }
}
