/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.editor.entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import net.qoopo.engine.core.texture.procesador.QProcesadorTextura;

/**
 *
 * @author alberto
 */
public class FrmVistaPrevia extends javax.swing.JFrame {

    private BufferedImage mapaDifusoActual = null;

    /**
     * Creates new form FrmVistaPrevia
     *
     * @param textura
     */
    public FrmVistaPrevia(QProcesadorTextura textura) {
        // this.mapaDifusoActual = img;
        initComponents();
        mapaDifusoActual = new BufferedImage(pnlTexture.getWidth(), pnlTexture.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        mapaDifusoActual.getGraphics().drawImage(textura.getTexture(pnlTexture.getSize()), 0, 0, pnlTexture);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlTexture = new javax.swing.JPanel() {
            public void paint(Graphics g) {
                super.paint(g);
                if (mapaDifusoActual != null)
                    g.drawImage(mapaDifusoActual, 0, 0, this);
            }
        };

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        pnlTexture.setBackground(new java.awt.Color(0, 0, 0));
        pnlTexture.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlTextureMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnlTextureLayout = new javax.swing.GroupLayout(pnlTexture);
        pnlTexture.setLayout(pnlTextureLayout);
        pnlTextureLayout.setHorizontalGroup(
                pnlTextureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 745, Short.MAX_VALUE));
        pnlTextureLayout.setVerticalGroup(
                pnlTextureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 424, Short.MAX_VALUE));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(pnlTexture, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(pnlTexture, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap()));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void pnlTextureMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_pnlTextureMouseClicked
        pnlTexture.getGraphics().drawImage(mapaDifusoActual, 0, 0, this);
    }// GEN-LAST:event_pnlTextureMouseClicked

    /**
     * @param args the command line arguments
     */
    // public static void main(String args[]) {
    // /* Set the Nimbus look and feel */
    // //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code
    // (optional) ">
    // /* If Nimbus (introduced in Java SE 6) is not available, stay with the
    // default look and feel.
    // * For details see
    // http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
    // */
    // try {
    // for (javax.swing.UIManager.LookAndFeelInfo info :
    // javax.swing.UIManager.getInstalledLookAndFeels()) {
    // if ("Nimbus".equals(info.getName())) {
    // javax.swing.UIManager.setLookAndFeel(info.getClassName());
    // break;
    // }
    // }
    // } catch (ClassNotFoundException ex) {
    // java.util.logging.Logger.getLogger(FrmVistaPrevia.class.getName()).log(java.util.logging.Level.SEVERE,
    // null, ex);
    // } catch (InstantiationException ex) {
    // java.util.logging.Logger.getLogger(FrmVistaPrevia.class.getName()).log(java.util.logging.Level.SEVERE,
    // null, ex);
    // } catch (IllegalAccessException ex) {
    // java.util.logging.Logger.getLogger(FrmVistaPrevia.class.getName()).log(java.util.logging.Level.SEVERE,
    // null, ex);
    // } catch (javax.swing.UnsupportedLookAndFeelException ex) {
    // java.util.logging.Logger.getLogger(FrmVistaPrevia.class.getName()).log(java.util.logging.Level.SEVERE,
    // null, ex);
    // }
    // //</editor-fold>
    //
    // /* Create and display the form */
    // java.awt.EventQueue.invokeLater(new Runnable() {
    // public void run() {
    // new FrmVistaPrevia().setVisible(true);
    // }
    // });
    // }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel pnlTexture;
    // End of variables declaration//GEN-END:variables
}
