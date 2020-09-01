/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.editor.entidad.componentes.procesadores.agua;

import java.util.ArrayList;
import java.util.List;
import net.qoopo.engine3d.componentes.QComponente;
import net.qoopo.engine3d.componentes.QUtilComponentes;
import net.qoopo.engine3d.componentes.geometria.QGeometria;
import net.qoopo.engine3d.componentes.geometria.primitivas.QPrimitiva;
import net.qoopo.engine3d.componentes.modificadores.procesadores.agua.QProcesadorAguaSimple;
import net.qoopo.engine3d.core.util.QGlobal;
import net.qoopo.engine3d.core.recursos.QGestorRecursos;
import net.qoopo.engine3d.core.material.basico.QMaterialBas;
import net.qoopo.engine3d.core.math.QColor;
import net.qoopo.engine3d.core.textura.QTextura;
import net.qoopo.engine3d.core.textura.mapeo.QMaterialUtil;
import net.qoopo.engine3d.core.textura.procesador.QProcesadorSimple;

/**
 *
 * @author alberto
 */
public class PnlAguaSimple extends javax.swing.JPanel {

    private QProcesadorAguaSimple agua;

    /**
     * Creates new form PnlAguaSimple
     */
    public PnlAguaSimple(QProcesadorAguaSimple agua) {
        initComponents();
        this.agua = agua;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel15 = new javax.swing.JLabel();
        txtAguaAncho = new javax.swing.JTextField();
        txtAguaAlto = new javax.swing.JTextField();
        btnAgregaAgua = new javax.swing.JButton();
        btnQuitarAgua = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Agua Simple", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 9))); // NOI18N

        jLabel15.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jLabel15.setText("Resolución:");

        txtAguaAncho.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        txtAguaAncho.setText("800");

        txtAguaAlto.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        txtAguaAlto.setText("600");

        btnAgregaAgua.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        btnAgregaAgua.setText("Aplicar");
        btnAgregaAgua.setMaximumSize(new java.awt.Dimension(68, 18));
        btnAgregaAgua.setMinimumSize(new java.awt.Dimension(68, 18));
        btnAgregaAgua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregaAguaActionPerformed(evt);
            }
        });

        btnQuitarAgua.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        btnQuitarAgua.setText("Quitar");
        btnQuitarAgua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitarAguaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(btnAgregaAgua, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnQuitarAgua)
                .addContainerGap(21, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jLabel15)
                .addGap(3, 3, 3)
                .addComponent(txtAguaAncho, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAguaAlto, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txtAguaAncho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAguaAlto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAgregaAgua, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnQuitarAgua)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregaAguaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregaAguaActionPerformed
        QMaterialBas material = new QMaterialBas("Lago");
        material.setTransparencia(true);
        material.setTransAlfa(0.4f);//40% ( transparencia del 60%)
        material.setColorDifusa(new QColor(1, 0, 0, 0.7f));
        material.setSpecularExponent(64);
//        material.setDifusaProyectada(true); //el mapa de reflexion es proyectado

//        QTextura mapaNormal = null;
//
//        //        material.getMapaDifusa().setModo(QProcesadorTextura.MODO_COMBINAR);//para que combine con el color azul del material
//        try {
//            //            mapaNormal = new QTextura(ImageIO.read(new File("res/texturas/agua/normalMap.png")));
//            mapaNormal = QGestorRecursos.cargarTextura("texnormal", QGlobal.RECURSOS + "texturas/agua/matchingNormalMap.png");
//            mapaNormal.setMuestrasU(10);
//            mapaNormal.setMuestrasV(10);
//            material.setMapaNormal(new QProcesadorSimple(mapaNormal));
//        } catch (Exception e) {
//        }
        agua.construir(null, Integer.parseInt(txtAguaAncho.getText()), Integer.parseInt(txtAguaAlto.getText()));
        //puedo agregar la razon que sea necesaria no afectara a  la textura de reflexixon porq esta calcula las coordenadas UV en tiempo de renderizado

        material.setMapaNormal(new QProcesadorSimple(agua.getTextNormal()));
        material.setMapaDifusa(agua.getTextSalida());

        List<QMaterialBas> lst = new ArrayList<>();
        //ahora  recorro todos los materiales del objeto y le agrego la textura de reflexion
        for (QComponente componente : agua.entidad.getComponentes()) {
            if (componente instanceof QGeometria) {
                QMaterialUtil.aplicarMaterial((QGeometria) componente, material);
            }
        }

    }//GEN-LAST:event_btnAgregaAguaActionPerformed

    private void btnQuitarAguaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitarAguaActionPerformed
        QUtilComponentes.eliminarComponenteAguaSimple(agua.entidad);

        List<QMaterialBas> lst = new ArrayList<>();
        //ahora  recorro todos los materiales del objeto y le quito la textura de reflexion
        for (QComponente componente : agua.entidad.getComponentes()) {
            if (componente instanceof QGeometria) {
                for (QPrimitiva poligono : ((QGeometria) componente).listaPrimitivas) {
                    if (poligono.material instanceof QMaterialBas) {
                        if (!lst.contains((QMaterialBas) poligono.material)) {
                            lst.add((QMaterialBas) poligono.material);
                        }
                    }
                }
            }
        }

        for (QMaterialBas mat : lst) {
            mat.setMapaDifusa(null);
        }

    }//GEN-LAST:event_btnQuitarAguaActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregaAgua;
    private javax.swing.JButton btnQuitarAgua;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JTextField txtAguaAlto;
    private javax.swing.JTextField txtAguaAncho;
    // End of variables declaration//GEN-END:variables
}
