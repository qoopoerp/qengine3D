/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.editor.entidad.componentes.fisica.colision;

import net.qoopo.engine3d.componentes.fisica.colisiones.detectores.contenedores.primitivas.QColisionCilindroX;

/**
 *
 * @author alberto
 */
public class PnlColisionCilindroX extends javax.swing.JPanel {

    private QColisionCilindroX capsula;

    /**
     * Creates new form PnlCubo
     */
    public PnlColisionCilindroX(QColisionCilindroX capsula) {
        initComponents();
        this.capsula = capsula;

        txtRadio.setText(String.valueOf(capsula.getRadio()));
        txtAltura.setText(String.valueOf(capsula.getAltura()));
    }

    private void aplicarCambios() {
        capsula.setRadio(Float.parseFloat(txtRadio.getText()));
        capsula.setAltura(Float.parseFloat(txtAltura.getText()));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtRadio = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtAltura = new javax.swing.JTextField();

        setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Colisión Cilindro", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 9))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jLabel1.setText("Radio:");

        txtRadio.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        txtRadio.setText("1");
        txtRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRadioActionPerformed(evt);
            }
        });
        txtRadio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtRadioKeyReleased(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jLabel2.setText("Altura;");

        txtAltura.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        txtAltura.setText("1");
        txtAltura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAlturaActionPerformed(evt);
            }
        });
        txtAltura.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAlturaKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtAltura)
                    .addComponent(txtRadio, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtRadio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtAltura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRadioActionPerformed
        aplicarCambios();
    }//GEN-LAST:event_txtRadioActionPerformed

    private void txtAlturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAlturaActionPerformed
        aplicarCambios();
    }//GEN-LAST:event_txtAlturaActionPerformed

    private void txtRadioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRadioKeyReleased
        aplicarCambios();        // TODO add your handling code here:
    }//GEN-LAST:event_txtRadioKeyReleased

    private void txtAlturaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAlturaKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAlturaKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField txtAltura;
    private javax.swing.JTextField txtRadio;
    // End of variables declaration//GEN-END:variables
}
