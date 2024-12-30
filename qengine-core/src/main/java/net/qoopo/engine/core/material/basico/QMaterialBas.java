package net.qoopo.engine.core.material.basico;

import net.qoopo.engine.core.entity.component.cubemap.CubeMap;
import net.qoopo.engine.core.material.AbstractMaterial;
import net.qoopo.engine.core.math.QColor;
import net.qoopo.engine.core.texture.QTextura;
import net.qoopo.engine.core.texture.procesador.QProcesadorSimple;
import net.qoopo.engine.core.texture.procesador.QProcesadorTextura;

/**
 * Materiales básicos
 *
 * @author alberto
 */
public class QMaterialBas extends AbstractMaterial {

    // propiedades basadas en PBR
    private float metalico = 0.0f;// reflectividad. 1= max reflectivo menos color difuso, 0 no reflectivo mas
                                  // color difuso
    private float rugosidad = 0.5f;
    private float especular = 0.0f;

    // ***************** SOMBREADO *********************
    // Color base del material
    private QColor colorBase = QColor.WHITE.clone();

    // factor de luz emitida del propio material
    private float factorEmision = 0;
    private float factorNormal = 1;

    // ***************** TRANSPARENCIA *********************
    private boolean transparencia = false;
    // transparencia, 0 transparente 1 solido
    private float transAlfa = 1.0f;
    private QColor colorTransparente = null;

    // ***************** ESPECULARIDAD *********************
    // nivel de brillo, maximo 500
    private int specularExponent = 50;
    // Color de luz especular
    // private QColor colorEspecular = QColor.WHITE.clone();

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
    private QProcesadorTextura mapaColor;// ok
    private QProcesadorTextura mapaNormal;// ok
    private QProcesadorTextura mapaEmisivo;// ok
    private QProcesadorTextura mapaTransparencia;// ok
    private QProcesadorTextura mapaDesplazamiento;// Muerto
    private QProcesadorTextura mapaEntorno;// ok. textura usada para el mapeo del entorno, reflexiones y refracciones
    private QProcesadorTextura mapaIrradiacion;// textura usada para la iluminacion de parte del entorno (PBR)
    private QProcesadorTextura mapaSAO;// Oclusion ambiental (sombras)
    private QProcesadorTextura mapaRugosidad;
    private QProcesadorTextura mapaEspecular;
    private QProcesadorTextura mapaMetalico;

    public QMaterialBas() {
    }

    public QMaterialBas(QColor difusa, int exponenteEspecular) {
        this.colorBase = difusa;
        this.specularExponent = exponenteEspecular;
    }

    public QMaterialBas(String name) {
        this.nombre = name;
    }

    /**
     * Crea un material usando la textura dada. Para la textura se crea un proxy
     * simple sin proceso
     *
     * @param textura
     * @param specularExponent
     */
    public QMaterialBas(QTextura textura, int specularExponent) {
        this.specularExponent = specularExponent;
        this.mapaColor = new QProcesadorSimple(textura);
    }

    public QMaterialBas(QTextura textura) {
        this.mapaColor = new QProcesadorSimple(textura);
    }

    public QProcesadorTextura getMapaColor() {
        return mapaColor;
    }

    public void setMapaColor(QProcesadorTextura mapaColor) {
        this.mapaColor = mapaColor;
    }

    public void setMapaColor(QTextura textura) {
        this.mapaColor = new QProcesadorSimple(textura);
    }

    public QProcesadorTextura getMapaNormal() {
        return mapaNormal;
    }

    public void setMapaNormal(QProcesadorTextura mapaNormal) {
        this.mapaNormal = mapaNormal;
    }

    public void setMapaNormal(QTextura textura) {
        this.mapaNormal = new QProcesadorSimple(textura);
    }

    public void destruir() {
        colorBase = null;
        if (mapaColor != null) {
            mapaColor.destruir();
            mapaColor = null;
        }
        if (mapaNormal != null) {
            mapaNormal.destruir();
            mapaNormal = null;
        }
    }

    public QColor getColorTransparente() {
        return colorTransparente;
    }

    public void setColorTransparente(QColor colorTransparente) {
        this.colorTransparente = colorTransparente;
    }

    public float getFactorEmision() {
        return factorEmision;
    }

    public void setFactorEmision(float factorEmision) {
        this.factorEmision = factorEmision;
    }

    public QColor getColorBase() {
        return colorBase;
    }

    public void setColorBase(QColor colorBase) {
        this.colorBase = colorBase;
    }

    public boolean isTransparencia() {
        return transparencia;
    }

    public void setTransparencia(boolean transparencia) {
        this.transparencia = transparencia;
    }

    public float getTransAlfa() {
        return transAlfa;
    }

    public void setTransAlfa(float transAlfa) {
        this.transAlfa = transAlfa;
    }

    public float getFactorNormal() {
        return factorNormal;
    }

    public void setFactorNormal(float factorNormal) {
        this.factorNormal = factorNormal;
    }

    public int getSpecularExponent() {
        return specularExponent;
    }

    public void setSpecularExponent(int specularExponent) {
        this.specularExponent = specularExponent;
    }

    public boolean isSombrasProyectar() {
        return sombrasProyectar;
    }

    public void setSombrasProyectar(boolean sombrasProyectar) {
        this.sombrasProyectar = sombrasProyectar;
    }

    public boolean isSombrasRecibir() {
        return sombrasRecibir;
    }

    public void setSombrasRecibir(boolean sombrasRecibir) {
        this.sombrasRecibir = sombrasRecibir;
    }

    public boolean isSombrasSoloProyectarSombra() {
        return sombrasSoloProyectarSombra;
    }

    public void setSombrasSoloProyectarSombra(boolean sombrasSoloProyectarSombra) {
        this.sombrasSoloProyectarSombra = sombrasSoloProyectarSombra;
    }

    public QProcesadorTextura getMapaEmisivo() {
        return mapaEmisivo;
    }

    public void setMapaEmisivo(QProcesadorTextura mapaEmisivo) {
        this.mapaEmisivo = mapaEmisivo;
    }

    public void setMapaEmisivo(QTextura textura) {
        this.mapaEmisivo = new QProcesadorSimple(textura);
    }

    public QProcesadorTextura getMapaEspecular() {
        return mapaEspecular;
    }

    public void setMapaEspecular(QProcesadorTextura mapaEspecular) {
        this.mapaEspecular = mapaEspecular;
    }

    public void setMapaEspecular(QTextura textura) {
        this.mapaEspecular = new QProcesadorSimple(textura);
    }

    public QProcesadorTextura getMapaDesplazamiento() {
        return mapaDesplazamiento;
    }

    public void setMapaDesplazamiento(QProcesadorTextura mapaDesplazamiento) {
        this.mapaDesplazamiento = mapaDesplazamiento;
    }

    public void setMapaDesplazamiento(QTextura textura) {
        this.mapaDesplazamiento = new QProcesadorSimple(textura);
    }

    public QProcesadorTextura getMapaEntorno() {
        return mapaEntorno;
    }

    public void setMapaEntorno(QProcesadorTextura mapaEntorno) {
        this.mapaEntorno = mapaEntorno;
    }

    public void setMapaEntorno(QTextura textura) {
        this.mapaEntorno = new QProcesadorSimple(textura);
    }

    public boolean isReflexion() {
        return reflexion;
    }

    public void setReflexion(boolean reflexion) {
        this.reflexion = reflexion;
    }

    public boolean isRefraccion() {
        return refraccion;
    }

    public void setRefraccion(boolean refraccion) {
        this.refraccion = refraccion;
    }

    public float getIndiceRefraccion() {
        return indiceRefraccion;
    }

    public void setIndiceRefraccion(float indiceRefraccion) {
        this.indiceRefraccion = indiceRefraccion;
    }

    public QProcesadorTextura getMapaTransparencia() {
        return mapaTransparencia;
    }

    public void setMapaTransparencia(QProcesadorTextura mapaTransparencia) {
        this.mapaTransparencia = mapaTransparencia;
    }

    public void setMapaTransparencia(QTextura textura) {
        this.mapaTransparencia = new QProcesadorSimple(textura);
    }

    public int getTipoMapaEntorno() {
        return tipoMapaEntorno;
    }

    public void setTipoMapaEntorno(int tipoMapaEntorno) {
        this.tipoMapaEntorno = tipoMapaEntorno;
    }

    public QProcesadorTextura getMapaSAO() {
        return mapaSAO;
    }

    public void setMapaSAO(QProcesadorTextura mapaSAO) {
        this.mapaSAO = mapaSAO;
    }

    public void setMapaSAO(QTextura textura) {
        this.mapaSAO = new QProcesadorSimple(textura);
    }

    public QProcesadorTextura getMapaRugosidad() {
        return mapaRugosidad;
    }

    public void setMapaRugosidad(QProcesadorTextura mapaRugosidad) {
        this.mapaRugosidad = mapaRugosidad;
    }

    public void setMapaRugosidad(QTextura textura) {
        this.mapaRugosidad = new QProcesadorSimple(textura);
    }

    public QProcesadorTextura getMapaMetalico() {
        return mapaMetalico;
    }

    public void setMapaMetalico(QProcesadorTextura mapaMetalico) {
        this.mapaMetalico = mapaMetalico;
    }

    public void setMapaMetalico(QTextura textura) {
        this.mapaMetalico = new QProcesadorSimple(textura);
    }

    public float getMetalico() {
        return metalico;
    }

    public void setMetalico(float metalico) {
        this.metalico = metalico;
    }

    public float getRugosidad() {
        return rugosidad;
    }

    public void setRugosidad(float rugosidad) {
        this.rugosidad = rugosidad;
    }

    public float getEspecular() {
        return especular;
    }

    public void setEspecular(float especular) {
        this.especular = especular;
    }

    public QProcesadorTextura getMapaIrradiacion() {
        return mapaIrradiacion;
    }

    public void setMapaIrradiacion(QProcesadorTextura mapaIrradiacion) {
        this.mapaIrradiacion = mapaIrradiacion;
    }

    public void setMapaIrradiacion(QTextura textura) {
        this.mapaIrradiacion = new QProcesadorSimple(textura);
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
