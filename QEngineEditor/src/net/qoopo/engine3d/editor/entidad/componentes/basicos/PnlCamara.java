/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.editor.entidad.componentes.basicos;

import net.qoopo.engine3d.componentes.QUtilComponentes;
import net.qoopo.engine3d.core.escena.QCamara;
import net.qoopo.engine3d.componentes.camara.QCamaraControl;
import net.qoopo.engine3d.componentes.camara.QCamaraOrbitar;
import net.qoopo.engine3d.componentes.camara.QCamaraPrimeraPersona;
import net.qoopo.engine3d.editor.Principal;

/**
 *
 * @author alberto
 */
public class PnlCamara extends javax.swing.JPanel {

    private QCamara camara;
    private boolean lock = false;

    /**
     * Creates new form pnlTransformacion
     */
    public PnlCamara(QCamara camara) {
        initComponents();
        this.camara = camara;
        leerValores();
    }

    private void leerValores() {
        lock = true;
        optVistaOrtogonal.setSelected(camara.isOrtogonal());
        if (camara.isOrtogonal()) {
            lblFOV.setText("Escala:");
            sldAngle.setMaximum(500);// puede ser cualquier numero mayor incluso
            sldAngle.setValue((int) camara.getEscalaOrtogonal());
        } else {
            sldAngle.setMaximum(120);//120 grados
            lblFOV.setText("FOV:");
            sldAngle.setValue((int) Math.toDegrees(camara.getFOV()));
        }

        txtCamaraCerca.setText(String.valueOf(camara.frustrumCerca));
        txtCamaraLejos.setText(String.valueOf(camara.frustrumLejos));

//        camara.setFOV((float) (sldAngle.getValue() * Math.PI / 120));
        lblCameraAngle.setText(sldAngle.getValue() + "");

//        spnLocX.setValue((float) camara.getTraslacion().x);
//        spnLocY.setValue(camara.getTraslacion().y);
//        spnLocZ.setValue(camara.getTraslacion().z);
//        spnRotX.setValue(camara.getRotacion().getAngulos().getAnguloX() * 120 / Math.PI);
//        spnRotY.setValue(camara.getRotacion().getAngulos().getAnguloY() * 120 / Math.PI);
//        spnRotZ.setValue(camara.getRotacion().getAngulos().getAnguloZ() * 120 / Math.PI);
//        spnScaleX.setValue(camara.getEscala().x);
//        spnScaleY.setValue(camara.getEscala().y);
//        spnScaleZ.setValue(camara.getEscala().z);
        lock = false;
    }

//    private void applyObjectControl() {
//        if (!lock) {
//            camara.getTraslacion().setXYZ(getFloatFromSpinner(spnLocX), getFloatFromSpinner(spnLocY), getFloatFromSpinner(spnLocZ));
//            camara.getRotacion().rotarX((float) (getFloatFromSpinner(spnRotX) * Math.PI / 120));
//            camara.getRotacion().rotarY((float) (getFloatFromSpinner(spnRotY) * Math.PI / 120));
//            camara.getRotacion().rotarZ((float) (getFloatFromSpinner(spnRotZ) * Math.PI / 120));
//
//            camara.getEscala().setXYZ(getFloatFromSpinner(spnScaleX), getFloatFromSpinner(spnScaleY), getFloatFromSpinner(spnScaleZ));
//            if (entidad instanceof QGeometria) {
//                for (QPoligono face : ((QGeometria) entidad).listaPoligonos) {
//                    face.smooth = btnObjectoSuavizado.isSelected();
//                }
//            }
//        }
//    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        optVistaPerspectiva = new javax.swing.JRadioButton();
        optVistaOrtogonal = new javax.swing.JRadioButton();
        lblFOV = new javax.swing.JLabel();
        sldAngle = new javax.swing.JSlider();
        lblCameraAngle = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtCamaraCerca = new javax.swing.JTextField();
        txtCamaraLejos = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        btnEscoger = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cámara", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 9))); // NOI18N

        buttonGroup1.add(optVistaPerspectiva);
        optVistaPerspectiva.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        optVistaPerspectiva.setSelected(true);
        optVistaPerspectiva.setText("Perspectiva");
        optVistaPerspectiva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optVistaPerspectivaActionPerformed(evt);
            }
        });

        buttonGroup1.add(optVistaOrtogonal);
        optVistaOrtogonal.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        optVistaOrtogonal.setText("Ortogonal");
        optVistaOrtogonal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optVistaOrtogonalActionPerformed(evt);
            }
        });

        lblFOV.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        lblFOV.setText("FOV:");

        sldAngle.setMaximum(180);
        sldAngle.setMinimum(1);
        sldAngle.setToolTipText("");
        sldAngle.setValue(60);
        sldAngle.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sldAngleStateChanged(evt);
            }
        });

        lblCameraAngle.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCameraAngle.setText("60");

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jLabel2.setText("Recorte:");

        jLabel5.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jLabel5.setText("Cercano:");

        txtCamaraCerca.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        txtCamaraCerca.setText("1.0");
        txtCamaraCerca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCamaraCercaActionPerformed(evt);
            }
        });
        txtCamaraCerca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCamaraCercaKeyReleased(evt);
            }
        });

        txtCamaraLejos.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        txtCamaraLejos.setText("1000");
        txtCamaraLejos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCamaraLejosActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jLabel8.setText("Lejano:");

        btnEscoger.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        btnEscoger.setText("Escoger");
        btnEscoger.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEscogerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(lblFOV, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(sldAngle, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblCameraAngle))
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCamaraLejos)
                    .addComponent(txtCamaraCerca)))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(optVistaPerspectiva)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(optVistaOrtogonal))
                    .addComponent(jLabel2))
                .addGap(0, 54, Short.MAX_VALUE))
            .addComponent(btnEscoger, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(optVistaPerspectiva)
                    .addComponent(optVistaOrtogonal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCameraAngle, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(sldAngle, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblFOV, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtCamaraCerca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtCamaraLejos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnEscoger))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void optVistaPerspectivaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optVistaPerspectivaActionPerformed
        camara.setOrtogonal(false);
        lock = true;
        if (camara.isOrtogonal()) {
            lblFOV.setText("Escala:");
            sldAngle.setMaximum(100);// puede ser cualquier numero mayor incluso
            sldAngle.setValue((int) camara.getEscalaOrtogonal());
        } else {
            sldAngle.setMaximum(120);//120 grados
            lblFOV.setText("FOV:");
            sldAngle.setValue((int) Math.toDegrees(camara.getFOV()));
        }
        lblCameraAngle.setText(sldAngle.getValue() + "");
//        camara.updateView();
        lock = false;
    }//GEN-LAST:event_optVistaPerspectivaActionPerformed

    private void optVistaOrtogonalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optVistaOrtogonalActionPerformed
        camara.setOrtogonal(true);
        lock = true;
        if (camara.isOrtogonal()) {
            lblFOV.setText("Escala:");
            sldAngle.setMaximum(100);// puede ser cualquier numero mayor incluso
            sldAngle.setValue((int) camara.getEscalaOrtogonal());
        } else {
            sldAngle.setMaximum(120);//120 grados
            lblFOV.setText("FOV:");
            sldAngle.setValue((int) Math.toDegrees(camara.getFOV()));
        }
        lblCameraAngle.setText(sldAngle.getValue() + "");
//        camara.updateView();
        lock = false;
    }//GEN-LAST:event_optVistaOrtogonalActionPerformed

    private void sldAngleStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sldAngleStateChanged
        if (lock) {
            return;
        }
        if (camara.isOrtogonal()) {
            camara.setEscalaOrtogonal((float) sldAngle.getValue());
        } else {
            camara.setFOV((float) Math.toRadians(sldAngle.getValue()));
        }

        lblCameraAngle.setText(sldAngle.getValue() + "");
//        camara.updateView();
    }//GEN-LAST:event_sldAngleStateChanged

    private void txtCamaraCercaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCamaraCercaActionPerformed
        try {
            camara.frustrumCerca = Float.parseFloat(txtCamaraCerca.getText());
            camara.updateCamera();
        } catch (Exception e) {
        }
    }//GEN-LAST:event_txtCamaraCercaActionPerformed

    private void txtCamaraLejosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCamaraLejosActionPerformed
        try {
            camara.frustrumLejos = Float.parseFloat(txtCamaraLejos.getText());
            camara.updateCamera();
        } catch (Exception e) {
        }
    }//GEN-LAST:event_txtCamaraLejosActionPerformed

    private void btnEscogerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEscogerActionPerformed

        //elimino los componentes usados para mover la camara
        QUtilComponentes.eliminarComponenteCamaraControl(Principal.instancia.getRenderer().getCamara());
        camara.agregarComponente(new QCamaraControl(camara));
//        QUtilComponentes.eliminarComponenteCamaraOrbitar(Principal.instancia.getRenderer().getCamara());
//        QUtilComponentes.eliminarComponenteCamaraPrimeraPersona(Principal.instancia.getRenderer().getCamara());
//        camara.agregarComponente(new QCamaraOrbitar(camara));
//        camara.agregarComponente(new QCamaraPrimeraPersona(camara));
        Principal.instancia.getRenderer().setCargando(true);
        Principal.instancia.getRenderer().setCamara(camara);
        Principal.instancia.getRenderer().resize();
        Principal.instancia.getRenderer().setCargando(false);
    }//GEN-LAST:event_btnEscogerActionPerformed

    private void txtCamaraCercaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCamaraCercaKeyReleased
        try {
            camara.frustrumCerca = Float.parseFloat(txtCamaraCerca.getText());
            camara.updateCamera();
        } catch (Exception e) {
        }
    }//GEN-LAST:event_txtCamaraCercaKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEscoger;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel lblCameraAngle;
    private javax.swing.JLabel lblFOV;
    private javax.swing.JRadioButton optVistaOrtogonal;
    private javax.swing.JRadioButton optVistaPerspectiva;
    private javax.swing.JSlider sldAngle;
    private javax.swing.JTextField txtCamaraCerca;
    private javax.swing.JTextField txtCamaraLejos;
    // End of variables declaration//GEN-END:variables
}
