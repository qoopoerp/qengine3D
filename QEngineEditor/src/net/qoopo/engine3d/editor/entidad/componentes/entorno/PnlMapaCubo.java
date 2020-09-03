/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.editor.entidad.componentes.entorno;

import java.util.ArrayList;
import java.util.List;
import net.qoopo.engine3d.componentes.QComponente;
import net.qoopo.engine3d.componentes.QUtilComponentes;
import net.qoopo.engine3d.componentes.geometria.QGeometria;
import net.qoopo.engine3d.componentes.geometria.primitivas.QPrimitiva;
import net.qoopo.engine3d.componentes.reflexiones.QMapaCubo;
import net.qoopo.engine3d.core.material.basico.QMaterialBas;

/**
 *
 * @author alberto
 */
public class PnlMapaCubo extends javax.swing.JPanel {

    private QMapaCubo mapa;

    /**
     * Creates new form PnlMapaCubo
     *
     * @param mapa
     */
    public PnlMapaCubo(QMapaCubo mapa) {
        initComponents();
        txtFactorReflexion.setText(String.valueOf(mapa.getFactorReflexion()));
        txtFactorRefraccion.setText(String.valueOf(mapa.getIndiceRefraccion()));
        txtResolucion.setText(String.valueOf(mapa.getTamanio()));
        this.mapa = mapa;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grupoButton = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        txtResolucion = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtFactorReflexion = new javax.swing.JTextField();
        txtFactorRefraccion = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        optMapaCubo = new javax.swing.JRadioButton();
        optHDRI = new javax.swing.JRadioButton();
        btnAplicar = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        btnQuitar = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Entorno Mapa Cubo", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 9))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jLabel1.setText("Tamaño mapa:");

        txtResolucion.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        txtResolucion.setText("200");

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jLabel2.setText("Factor Reflexión:");

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jLabel3.setText("Índice de Refracción:");

        txtFactorReflexion.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        txtFactorReflexion.setText("0.8");

        txtFactorRefraccion.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        txtFactorRefraccion.setText("1.52");

        jLabel7.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jLabel7.setText("Tipo salida:");

        grupoButton.add(optMapaCubo);
        optMapaCubo.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        optMapaCubo.setSelected(true);
        optMapaCubo.setText("Mapa Cubo");
        optMapaCubo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optMapaCuboActionPerformed(evt);
            }
        });

        grupoButton.add(optHDRI);
        optHDRI.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        optHDRI.setText("HDRI");

        btnAplicar.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        btnAplicar.setText("Aplicar");
        btnAplicar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAplicarActionPerformed(evt);
            }
        });

        btnQuitar.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        btnQuitar.setText("Quitar");
        btnQuitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(btnAplicar)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(btnQuitar))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(optMapaCubo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(optHDRI, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtResolucion)
                            .addComponent(txtFactorReflexion)
                            .addComponent(txtFactorRefraccion, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtResolucion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtFactorReflexion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtFactorRefraccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(optMapaCubo)
                    .addComponent(optHDRI))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAplicar)
                    .addComponent(btnQuitar)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void optMapaCuboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optMapaCuboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_optMapaCuboActionPerformed

    private void btnAplicarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAplicarActionPerformed
        int ancho = 200;
        float factor = 0.75f;
        float indice = 0.0f;
        try {
            factor = Float.valueOf(txtFactorReflexion.getText());
        } catch (Exception e) {
        }
        try {
            indice = Float.valueOf(txtFactorRefraccion.getText());
        } catch (Exception e) {
        }
        try {
            ancho = Integer.parseInt(txtResolucion.getText());
        } catch (Exception e) {
        }

        mapa.construir(ancho);

        if (optMapaCubo.isSelected()) {
            mapa.aplicar(QMapaCubo.FORMATO_MAPA_CUBO, factor, indice);
        } else if (optHDRI.isSelected()) {
            mapa.aplicar(QMapaCubo.FORMATO_MAPA_HDRI, factor, indice);
        }
    }//GEN-LAST:event_btnAplicarActionPerformed

    private void btnQuitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitarActionPerformed
        QUtilComponentes.eliminarComponenteMapaCubo(mapa.entidad);
        List<QMaterialBas> lst = new ArrayList<>();
        //ahora  recorro todos los materiales del objeto y le quito la textura de reflexion
        for (QComponente componente : mapa.entidad.getComponentes()) {
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
            mat.setMapaEntorno(null);
            mat.setMetalico(0);
            mat.setIndiceRefraccion(0);
        }
    }//GEN-LAST:event_btnQuitarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAplicar;
    private javax.swing.JButton btnQuitar;
    private javax.swing.ButtonGroup grupoButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JRadioButton optHDRI;
    private javax.swing.JRadioButton optMapaCubo;
    private javax.swing.JTextField txtFactorReflexion;
    private javax.swing.JTextField txtFactorRefraccion;
    private javax.swing.JTextField txtResolucion;
    // End of variables declaration//GEN-END:variables
}
