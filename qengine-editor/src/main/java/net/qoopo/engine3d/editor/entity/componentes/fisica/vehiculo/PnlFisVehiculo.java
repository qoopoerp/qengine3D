/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.editor.entity.componentes.fisica.vehiculo;

import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;

import net.qoopo.engine.core.entity.Entity;
import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.entity.component.mesh.Mesh;
import net.qoopo.engine.core.entity.component.mesh.primitive.Vertex;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.AABB;
import net.qoopo.engine.core.entity.component.physics.vehiculo.QRueda;
import net.qoopo.engine.core.entity.component.physics.vehiculo.QVehiculo;
import net.qoopo.engine.core.math.QVector3;
import net.qoopo.engine.core.scene.Scene;
import net.qoopo.engine.core.util.ComponentUtil;

/**
 *
 * @author alberto
 */
public class PnlFisVehiculo extends javax.swing.JPanel {

    private QVehiculo componente;
    private Map<String, Entity> mapa = new HashMap<>();
    private DefaultListModel modeloLista = new DefaultListModel();

    /**
     * Creates new form PnlCubo
     * 
     * @param componente
     * @param escena
     */
    public PnlFisVehiculo(QVehiculo componente, Scene escena) {
        initComponents();
        this.componente = componente;

        DefaultComboBoxModel modelo = new DefaultComboBoxModel<>();
        for (Entity entidad : escena.getEntities()) {
            for (EntityComponent comp : entidad.getComponents()) {
                if (comp instanceof Mesh) { // que tenga una geometria
                    modelo.addElement(entidad.getName());
                    mapa.put(entidad.getName(), entidad);
                }
            }
        }
        lstRuedas.setModel(modeloLista);
        cboRueda.setModel(modelo);
        // if (componente.getB() != null) {
        // cboFisico.setSelectedItem(componente.getB().entity.nombre);
        // }
        aplicarCambios();
        actualizarListaRuedas();
    }

    private void aplicarCambios() {
        // componente.setB(mapa.get(cboFisico.getSelectedItem().toString()));
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
        cboRueda = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        lblChasis = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstRuedas = new javax.swing.JList<>();
        btnAgregar = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        btnEliminarRueda = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        txtRadio = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtAncho = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtFriccion = new javax.swing.JTextField();
        txtInflRodamiento = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtSusRestLenght = new javax.swing.JTextField();
        txtSusRigidez = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtSusRelajacion = new javax.swing.JTextField();
        txtSusCompresion = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        chkFrontal = new javax.swing.JCheckBox();
        txtUbicacion = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Vehículo"));

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jLabel1.setText("Agregar rueda:");

        cboRueda.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        cboRueda.setModel(
                new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboRueda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboRuedaActionPerformed(evt);
            }
        });

        jLabel2.setText("Chasis:");

        lblChasis.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        lblChasis.setText("N/A");

        lstRuedas.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        lstRuedas.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };

            public int getSize() {
                return strings.length;
            }

            public String getElementAt(int i) {
                return strings[i];
            }
        });
        jScrollPane1.setViewportView(lstRuedas);

        btnAgregar.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        btnAgregar.setText("Agregar");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        btnEliminarRueda.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        btnEliminarRueda.setText("Eliminar rueda");
        btnEliminarRueda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarRuedaActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jLabel3.setText("Radio:");

        txtRadio.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        txtRadio.setText("0.25");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("RUEDAS");
        jLabel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel5.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jLabel5.setText("Ancho:");

        txtAncho.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        txtAncho.setText("0.2");

        jLabel6.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jLabel6.setText("Fricción:");

        txtFriccion.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        txtFriccion.setText("1000");

        txtInflRodamiento.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        txtInflRodamiento.setText("0.1");

        jLabel7.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jLabel7.setText("I. Rodam:");

        jLabel8.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jLabel8.setText("Sus. Rest. Lenght:");

        txtSusRestLenght.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        txtSusRestLenght.setText("0.6");

        txtSusRigidez.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        txtSusRigidez.setText("20");

        jLabel9.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jLabel9.setText("Sus. Rigididez:");

        jLabel10.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jLabel10.setText("Sus. Relajación:");

        txtSusRelajacion.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        txtSusRelajacion.setText("2.3");

        txtSusCompresion.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        txtSusCompresion.setText("4.4");

        jLabel11.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jLabel11.setText("Sus. Compresión:");

        chkFrontal.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        chkFrontal.setText("Frontal");

        txtUbicacion.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        txtUbicacion.setText("0,0,0");

        jLabel13.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jLabel13.setText("Ubicación:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(cboRueda, 0, 249, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblChasis, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(jScrollPane1)
                        .addComponent(jSeparator1)
                        .addComponent(btnEliminarRueda, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSeparator2)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel5)
                                        .addComponent(jLabel6)
                                        .addComponent(jLabel7)
                                        .addComponent(jLabel8)
                                        .addComponent(jLabel9)
                                        .addComponent(jLabel10)
                                        .addComponent(jLabel11)
                                        .addComponent(jLabel13))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtRadio)
                                        .addComponent(txtAncho)
                                        .addComponent(txtFriccion)
                                        .addComponent(txtInflRodamiento)
                                        .addComponent(txtSusRestLenght)
                                        .addComponent(txtSusRigidez)
                                        .addComponent(txtSusRelajacion)
                                        .addComponent(txtSusCompresion)
                                        .addComponent(txtUbicacion)))
                        .addComponent(chkFrontal, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(btnAgregar)
                                .addGap(0, 0, Short.MAX_VALUE)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(lblChasis))
                                .addGap(9, 9, 9)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 4,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)
                                .addGap(7, 7, 7)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboRueda, javax.swing.GroupLayout.PREFERRED_SIZE, 22,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chkFrontal)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(txtRadio, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel5)
                                        .addComponent(txtAncho, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel6)
                                        .addComponent(txtFriccion, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel7)
                                        .addComponent(txtInflRodamiento, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel13)
                                        .addComponent(txtUbicacion, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel8)
                                        .addComponent(txtSusRestLenght, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel9)
                                        .addComponent(txtSusRigidez, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel10)
                                        .addComponent(txtSusRelajacion, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel11)
                                        .addComponent(txtSusCompresion, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 16,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 81,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnEliminarRueda)));
    }// </editor-fold>//GEN-END:initComponents

    private void cboRuedaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cboRuedaActionPerformed
        // intenta calcular el ancho y el radi ode la rueda

        Entity entidadRueda = mapa.get(cboRueda.getSelectedItem().toString());
        if (entidadRueda != null) {
            Mesh geometria = ComponentUtil.getMesh(entidadRueda);
            AABB tmp = new AABB(geometria.vertexList[0].clone(), geometria.vertexList[0].clone());

            for (Vertex vertice : geometria.vertexList) {
                if (vertice.location.x < tmp.aabMinimo.location.x) {
                    tmp.aabMinimo.location.x = vertice.location.x;
                }
                if (vertice.location.y < tmp.aabMinimo.location.y) {
                    tmp.aabMinimo.location.y = vertice.location.y;
                }
                if (vertice.location.z < tmp.aabMinimo.location.z) {
                    tmp.aabMinimo.location.z = vertice.location.z;
                }
                if (vertice.location.x > tmp.aabMaximo.location.x) {
                    tmp.aabMaximo.location.x = vertice.location.x;
                }
                if (vertice.location.y > tmp.aabMaximo.location.y) {
                    tmp.aabMaximo.location.y = vertice.location.y;
                }
                if (vertice.location.z > tmp.aabMaximo.location.z) {
                    tmp.aabMaximo.location.z = vertice.location.z;
                }
            }

            txtAncho.setText(String.valueOf(tmp.aabMaximo.location.x - tmp.aabMinimo.location.x));
            txtRadio.setText(String.valueOf((tmp.aabMaximo.location.y - tmp.aabMinimo.location.y) / 2));
        }
    }// GEN-LAST:event_cboRuedaActionPerformed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAgregarActionPerformed
        Entity entidadRueda = mapa.get(cboRueda.getSelectedItem().toString());
        if (entidadRueda != null) {
            QRueda rueda = new QRueda();
            rueda.setEntidadRueda(entidadRueda);
            rueda.setAncho(Float.parseFloat(txtAncho.getText()));
            rueda.setRadio(Float.parseFloat(txtRadio.getText()));
            rueda.setFrontal(chkFrontal.isSelected());
            rueda.setFriccion(Float.parseFloat(txtFriccion.getText()));
            rueda.setInfluenciaRodamiento(Float.parseFloat(txtInflRodamiento.getText()));
            rueda.setDampingCompression(Float.parseFloat(txtSusCompresion.getText()));
            rueda.setDampingRelaxation(Float.parseFloat(txtSusRelajacion.getText()));
            rueda.setSuspensionRestLength(Float.parseFloat(txtSusRestLenght.getText()));
            rueda.setSuspensionStiffness(Float.parseFloat(txtSusRigidez.getText()));
            try {
                String[] valores = txtUbicacion.getText().split(",");
                rueda.setUbicacion(QVector3.of(Float.parseFloat(valores[0]), Float.parseFloat(valores[1]),
                        Float.parseFloat(valores[2])));
            } catch (Exception e) {
            }
            componente.agregarRueda(rueda);
            actualizarListaRuedas();
        }
    }// GEN-LAST:event_btnAgregarActionPerformed

    private void btnEliminarRuedaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnEliminarRuedaActionPerformed
        try {
            componente.getRuedas().remove(lstRuedas.getSelectedIndex());
            actualizarListaRuedas();
        } catch (Exception e) {
        }
    }// GEN-LAST:event_btnEliminarRuedaActionPerformed

    private void actualizarListaRuedas() {
        modeloLista.clear();
        try {
            for (QRueda rueda : componente.getRuedas()) {
                modeloLista.addElement(rueda.getEntidadRueda().getName());
            }
        } catch (Exception e) {

        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnEliminarRueda;
    private javax.swing.JComboBox<String> cboRueda;
    private javax.swing.JCheckBox chkFrontal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblChasis;
    private javax.swing.JList<String> lstRuedas;
    private javax.swing.JTextField txtAncho;
    private javax.swing.JTextField txtFriccion;
    private javax.swing.JTextField txtInflRodamiento;
    private javax.swing.JTextField txtRadio;
    private javax.swing.JTextField txtSusCompresion;
    private javax.swing.JTextField txtSusRelajacion;
    private javax.swing.JTextField txtSusRestLenght;
    private javax.swing.JTextField txtSusRigidez;
    private javax.swing.JTextField txtUbicacion;
    // End of variables declaration//GEN-END:variables
}
