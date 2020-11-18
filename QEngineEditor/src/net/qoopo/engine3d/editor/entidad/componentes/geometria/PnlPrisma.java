/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.editor.entidad.componentes.geometria;

import net.qoopo.engine3d.componentes.geometria.primitivas.formas.QPrisma;

/**
 *
 * @author alberto
 */
public class PnlPrisma extends javax.swing.JPanel {

    private QPrisma forma;

    /**
     * Creates new form PnlCubo
     */
    public PnlPrisma(QPrisma forma) {
        initComponents();
        this.forma = forma;
        txtRadio.setText(String.valueOf(forma.getRadioSuperior()));
        txtAlto.setText(String.valueOf(forma.getAlto()));
        txtSecciones.setText(String.valueOf(forma.getSecciones()));
    }

    private void aplicarCambios() {
        forma.setRadioSuperior(Float.parseFloat(txtRadio.getText()));
        forma.setRadioInferior(forma.getRadioSuperior());
        forma.setAlto(Float.parseFloat(txtAlto.getText()));
        forma.setSecciones(Integer.parseInt(txtSecciones.getText()));
        forma.construir();
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
        txtAlto = new javax.swing.JTextField();
        txtSecciones = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Prisma", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 9))); // NOI18N

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
        jLabel2.setText("Alto:");

        txtAlto.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        txtAlto.setText("1");
        txtAlto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAltoActionPerformed(evt);
            }
        });
        txtAlto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAltoKeyReleased(evt);
            }
        });

        txtSecciones.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        txtSecciones.setText("1");
        txtSecciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSeccionesActionPerformed(evt);
            }
        });
        txtSecciones.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSeccionesKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSeccionesKeyReleased(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jLabel3.setText("Secciones:");

        jButton1.setFont(new java.awt.Font("Dialog", 0, 8)); // NOI18N
        jButton1.setText("Aplicar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtAlto, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                    .addComponent(txtRadio)
                    .addComponent(txtSecciones)))
            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                    .addComponent(txtAlto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(txtSecciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRadioActionPerformed
//        aplicarCambios();
    }//GEN-LAST:event_txtRadioActionPerformed

    private void txtAltoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAltoActionPerformed
//        aplicarCambios();
    }//GEN-LAST:event_txtAltoActionPerformed

    private void txtRadioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRadioKeyReleased
//        aplicarCambios();        // TODO add your handling code here:
    }//GEN-LAST:event_txtRadioKeyReleased

    private void txtAltoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAltoKeyReleased
//        aplicarCambios();        // TODO add your handling code here:
    }//GEN-LAST:event_txtAltoKeyReleased

    private void txtSeccionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSeccionesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSeccionesActionPerformed

    private void txtSeccionesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSeccionesKeyReleased
//         aplicarCambios();  
    }//GEN-LAST:event_txtSeccionesKeyReleased

    private void txtSeccionesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSeccionesKeyPressed
//         aplicarCambios();  
    }//GEN-LAST:event_txtSeccionesKeyPressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        aplicarCambios();          // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField txtAlto;
    private javax.swing.JTextField txtRadio;
    private javax.swing.JTextField txtSecciones;
    // End of variables declaration//GEN-END:variables
}
