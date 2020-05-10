/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.test.generaEjemplos.impl.simple;

import java.io.File;
import net.qoopo.engine3d.componentes.QEntidad;
import net.qoopo.engine3d.core.escena.QEscena;
import net.qoopo.engine3d.core.math.QVector3;
import net.qoopo.engine3d.core.util.QMallaUtil;
import net.qoopo.engine3d.componentes.geometria.util.QUnidadMedida;
import net.qoopo.engine3d.core.textura.mapeo.QMaterialUtil;
import net.qoopo.engine3d.test.generaEjemplos.GeneraEjemplo;
import net.qoopo.engine3d.componentes.geometria.primitivas.formas.QCaja;
import net.qoopo.engine3d.componentes.geometria.primitivas.formas.QEsfera;
import net.qoopo.engine3d.core.util.QGlobal;
import net.qoopo.engine3d.core.recursos.QGestorRecursos;
import net.qoopo.engine3d.core.material.basico.QMaterialBas;
import net.qoopo.engine3d.engines.render.QMotorRender;

/**
 *
 * @author alberto
 */
public class EjmDivision extends GeneraEjemplo {

    public void iniciar(QEscena mundo) {
        this.mundo = mundo;

        QEntidad obj1 = new QEntidad("esfera1");
        obj1.agregarComponente(QMaterialUtil.aplicarMaterial(new QEsfera(5), new QMaterialBas(QGestorRecursos.cargarTextura("asd1", new File(QGlobal.RECURSOS + "texturas/planetas/tierra/text3/earthmap1k.jpg")))));

        mundo.agregarEntidad(obj1);

        QEntidad obj2 = new QEntidad("esfera2");
        obj2.agregarComponente(QMallaUtil.subdividir(QMaterialUtil.aplicarMaterial(new QEsfera(5), new QMaterialBas(QGestorRecursos.getTextura("asd1"))), 1));

        mundo.agregarEntidad(obj2);

        obj1.mover(new QVector3(-3, 0, 0));
        obj2.mover(new QVector3(3, 0, 0));

        QEntidad piso = new QEntidad("piso");

        piso.agregarComponente(QMallaUtil.subdividir(QMaterialUtil.aplicarMaterial(new QCaja(mundo.UM.convertirPixel(0.1f),
                        mundo.UM.convertirPixel(500, QUnidadMedida.METRO),
                        mundo.UM.convertirPixel(500, QUnidadMedida.METRO), 100),
                new QMaterialBas(QGestorRecursos.cargarTextura("asd2", new File(QGlobal.RECURSOS + "texturas/piso/cesped/cesped3.jpg")))),
                4)
        );

        mundo.agregarEntidad(piso);

    }

    @Override
    public void accion(int numAccion, QMotorRender render) {
    }

}
