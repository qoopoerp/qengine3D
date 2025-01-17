/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.editor.entity.componentes.fisica.restricciones;

import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.entity.component.physics.dinamica.QObjetoRigido;
import net.qoopo.engine.core.entity.component.physics.restricciones.QRestriccionFija;
import net.qoopo.engine3d.editor.Principal;

/**
 *
 * @author alberto
 */
public class PnlFisRestriccionFija extends javax.swing.JPanel {

    private QRestriccionFija componente;
    private Map<String, QObjetoRigido> mapa = new HashMap<String, QObjetoRigido>();

    /**
     * Creates new form PnlCubo
     */
    public PnlFisRestriccionFija(QRestriccionFija componente) {
        initComponents();
        this.componente = componente;

        DefaultComboBoxModel modelo = new DefaultComboBoxModel<>();
        for (Entity entidad : Principal.instancia.getEscena().getEntities()) {
            for (EntityComponent comp : entidad.getComponents()) {
                if (comp instanceof QObjetoRigido) {
                    modelo.addElement(entidad.getName());
                    mapa.put(entidad.getName(), (QObjetoRigido) comp);
                }
            }
        }
        cboFisico.setModel(modelo);
        if (componente.getB() != null) {
            cboFisico.setSelectedItem(componente.getB().getEntity().getName());
        }
        aplicarCambios();
        // cboFisico.setSelectedItem(forma.getMalla().nombre);
        // txtRadio.setText(String.valueOf(esfera.getRadio()));
    }

    private void aplicarCambios() {
        componente.setB(mapa.get(cboFisico.getSelectedItem().toString()));
        // componente.setRadio(Float.parseFloat(txtRadio.getText()));
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

        jLabel1 = new javax.swing.JLabel();
        cboFisico = new javax.swing.JComboBox<>();

        setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Restricción Fija",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new java.awt.Font("Dialog", 0, 9))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jLabel1.setText("Cuerpo físico atado:");

        cboFisico.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        cboFisico.setModel(
                new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboFisico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboFisicoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
                        .addComponent(cboFisico, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboFisico, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));
    }// </editor-fold>//GEN-END:initComponents

    private void cboFisicoActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cboFisicoActionPerformed
        aplicarCambios();
    }// GEN-LAST:event_cboFisicoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cboFisico;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
