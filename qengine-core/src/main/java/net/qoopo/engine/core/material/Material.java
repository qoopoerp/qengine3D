package net.qoopo.engine.core.material;

import lombok.Getter;
import lombok.Setter;
import net.qoopo.engine.core.entity.component.environment.EnvProbe;
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
    /**
     * Controla qué tan difusa o brillante es la superficie.
     * Si es completamente mate, la rugosidad tendrá un valor alto (cercano a 1.0).
     * Si es brillante, será más baja (cercano a 0.0).
     */

    private float roughness = 0.8f;

    /**
     * Indica si la superficie es metalica o dieléctrica
     * 
     * Las superficies metálicas siguen los mismos principios de reflexión y
     * refracción, pero toda la luz refractada se absorbe directamente sin
     * dispersarse.
     * 
     * Esto significa que las superficies metálicas solo dejan luz reflejada o
     * especular;
     * 
     * Las superficies metálicas no muestran colores difusos. Debido a esta aparente
     * distinción entre metales y dieléctricos, ambos reciben un tratamiento
     * diferente en el proceso de PBR
     */
    private float metallic = 0.0f;

    // Propiedades heredadas de la iluminación Phong
    private float emision = 0;

    // ***************** TRANSPARENCIA *********************
    private boolean transparencia = false;
    // transparencia, 0 transparente 1 solido
    private float transAlfa = 1.0f;
    private QColor alphaColour = null;

    // ***************** ESPECULARIDAD *********************
    // nivel de brillo, maximo 500
    private int specularExponent = 50;
    // Color de luz especular
    private QColor colorEspecular = QColor.WHITE.clone();

    // ***************** ENTORNO *********************
    private int envMapType = EnvProbe.FORMATO_MAPA_CUBO;// 1. Mapa cubico, 2. Mapa HDRI,

    // ***************** REFLEXION *********************
    private boolean reflexion = false;

    // ***************** REFRACCIÓN *********************
    private boolean refraccion = false;
    private float ior = 0.0f;// indice de refraccion, aire 1, agua 1.33, vidrio 1.52, diamante 2.42,
    // ---------------------------------------------------

    // ***************** SOMBRAS *********************
    private boolean sombrasProyectar = true;
    private boolean sombrasRecibir = true;
    private boolean sombrasSoloProyectarSombra = false;// renderiza la sombra pero no renderiza el objeto, como si fuera
                                                       // 100% transparente

    // ***************** MAPAS **********************
    // mapas
    private Texture colorMap;// ok
    private Texture normalMap;// ok
    private Texture roughnessMap;
    private Texture metallicMap;
    private Texture aoMap;// Oclusion ambiental (sombras)
    private Texture envMap;// ok. textura usada para el mapeo del entorno, reflexiones y refracciones
    private Texture hdrMap;// textura usada para la iluminacion de parte del entorno (PBR)
    private Texture emissiveMap;// ok
    private Texture alphaMap;// ok

    private Texture mapaEspecular;

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
        this.colorMap = textura;
    }

    public Material(Texture textura) {
        this.colorMap = textura;
    }

    /** Valida que el valor esté en estos rangos 0.1-0.8 */
    public void setRoughness(float roughness) {
        this.roughness = Math.min(roughness, 0.95f);
        this.roughness = Math.max(this.roughness, 0.05f);
    }

    /** Valida que el valor esté en estos rangos */
    public void setMetallic(float metallic) {
        this.metallic = Math.min(metallic, 0.99f);
        this.metallic = Math.max(this.metallic, 0.f);
    }

    public void destroy() {
        color = null;
        if (colorMap != null) {
            colorMap.destroy();
            colorMap = null;
        }
        if (normalMap != null) {
            normalMap.destroy();
            normalMap = null;
        }
    }

    public void clearColorMap() {
        colorMap = null;
    }

    public void clearNormalMap() {
        normalMap = null;
    }

    public void clearSpecularMap() {
        mapaEspecular = null;
    }

    public void clearEmisionMap() {
        emissiveMap = null;
    }

    public void clearEnviromentMap() {
        envMap = null;
    }

    public void clearSaoMap() {
        aoMap = null;
    }

    public void clearMetallicMap() {
        metallicMap = null;
    }

    public void clearTransparencyMap() {
        alphaMap = null;
    }

    public void clearRugoussMap() {
        roughnessMap = null;
    }

    public void clearIrradationMap() {
        hdrMap = null;
    }

}
