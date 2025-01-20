/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.editor.entity.componentes.fisica.colision;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;

import net.qoopo.engine.core.entity.component.EntityComponent;
import net.qoopo.engine.core.entity.component.physics.collision.detector.CollisionShape;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.compuesta.QColisionCompuesta;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.compuesta.QColisionCompuestaHija;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.mallas.QColisionMallaConvexa;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.mallas.QColisionMallaIndexada;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.mallas.QColisionTerreno;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.AABB;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.QColisionCaja;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.QColisionCapsula;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.QColisionCilindro;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.QColisionCilindroX;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.QColisionCono;
import net.qoopo.engine.core.entity.component.physics.collision.detector.shape.primitivas.QColisionEsfera;
import net.qoopo.engine.core.math.Vector3;

/**
 *
 * @author alberto
 */
public class PnlColisionCompuesta extends javax.swing.JPanel {

    private QColisionCompuesta componente;
    private List<CollisionShape> lista = new ArrayList<>();

    private DefaultListModel modeloLista = new DefaultListModel();

    /**
     * Creates new form PnlCubo
     */
    public PnlColisionCompuesta(QColisionCompuesta componente) {
        initComponents();
        this.componente = componente;

        DefaultComboBoxModel modelo = new DefaultComboBoxModel<>();
        for (EntityComponent comp : componente.getEntity().getComponents()) {
            if (comp instanceof CollisionShape) {
                lista.add((CollisionShape) comp);
                if (comp instanceof QColisionCaja) {
                    modelo.addElement("Caja");
                } else if (comp instanceof QColisionCapsula) {
                    modelo.addElement("Cápsula");
                } else if (comp instanceof QColisionCilindro) {
                    modelo.addElement("Cilindro");
                } else if (comp instanceof QColisionCilindroX) {
                    modelo.addElement("Cilindro X");
                } else if (comp instanceof QColisionCono) {
                    modelo.addElement("Cono");
                } else if (comp instanceof QColisionEsfera) {
                    modelo.addElement("Esfera");
                } else if (comp instanceof QColisionTerreno) {
                    modelo.addElement("Terreno");
                } else if (comp instanceof QColisionMallaConvexa) {
                    modelo.addElement("Convexa " + ((QColisionMallaConvexa) comp).getMalla().name);
                } else if (comp instanceof QColisionMallaIndexada) {
                    modelo.addElement("Indexada " + ((QColisionMallaIndexada) comp).getMalla().name);
                } else if (comp instanceof QColisionCompuesta) {
                    modelo.addElement("Compuesta ");
                } else if (comp instanceof AABB) {
                    modelo.addElement("AABB");
                } else {
                    modelo.addElement("Foma colisión");
                }
            }
        }
        cboRueda.setModel(modelo);
        lstRuedas.setModel(modeloLista);

        // if (componente.getB() != null) {
        // cboFisico.setSelectedItem(componente.getB().entity.nombre);
        // }
        // aplicarCambios();
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
        jScrollPane1 = new javax.swing.JScrollPane();
        lstRuedas = new javax.swing.JList<>();
        btnAgregar = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        btnEliminarRueda = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        txtX = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtY = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtZ = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Colisión Compueta",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new java.awt.Font("Dialog", 0, 9))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jLabel1.setText("Agregar forma");

        cboRueda.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        cboRueda.setModel(
                new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboRueda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboRuedaActionPerformed(evt);
            }
        });

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
        jLabel3.setText("X:");

        txtX.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        txtX.setText("0.0");

        jLabel5.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jLabel5.setText("Y:");

        txtY.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        txtY.setText("0.0");

        jLabel6.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jLabel6.setText("Z:");

        txtZ.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        txtZ.setText("0.0");

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jLabel2.setText("Ubicación:");

        jButton1.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        jButton1.setText("Aplicar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(cboRueda, 0, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                Short.MAX_VALUE)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jScrollPane1)
                                        .addComponent(jSeparator1)
                                        .addComponent(btnEliminarRueda, javax.swing.GroupLayout.DEFAULT_SIZE, 203,
                                                Short.MAX_VALUE)
                                        .addComponent(jSeparator2)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel3)
                                                        .addComponent(jLabel5)
                                                        .addComponent(jLabel6))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(txtX)
                                                        .addComponent(txtY)
                                                        .addComponent(txtZ)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(btnAgregar)
                                                        .addComponent(jLabel2))
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 4,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboRueda, javax.swing.GroupLayout.PREFERRED_SIZE, 22,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(txtX, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel5)
                                        .addComponent(txtY, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel6)
                                        .addComponent(txtZ, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
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
                                .addComponent(btnEliminarRueda)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1)
                                .addContainerGap()));
    }// </editor-fold>//GEN-END:initComponents

    private void cboRuedaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cboRuedaActionPerformed
        // intenta calcular el ancho y el radi ode la rueda

    }// GEN-LAST:event_cboRuedaActionPerformed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAgregarActionPerformed
        CollisionShape formaColision = lista.get(cboRueda.getSelectedIndex());
        if (formaColision != null) {
            QColisionCompuestaHija rueda = new QColisionCompuestaHija(formaColision,
                    Vector3.of(Float.parseFloat(txtX.getText()),
                            Float.parseFloat(txtY.getText()),
                            Float.parseFloat(txtZ.getText())));

            componente.agregarHijo(rueda);
            actualizarListaRuedas();
        }
    }// GEN-LAST:event_btnAgregarActionPerformed

    private void btnEliminarRuedaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnEliminarRuedaActionPerformed
        try {
            componente.getHijos().remove(lstRuedas.getSelectedIndex());
            actualizarListaRuedas();
        } catch (Exception e) {
        }
    }// GEN-LAST:event_btnEliminarRuedaActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed
        aplicarCambios(); // TODO add your handling code here:
    }// GEN-LAST:event_jButton1ActionPerformed

    private void actualizarListaRuedas() {
        modeloLista.clear();
        try {
            for (QColisionCompuestaHija hijo : componente.getHijos()) {

                if (hijo.getForma() instanceof CollisionShape) {

                    if (hijo.getForma() instanceof QColisionCaja) {
                        modeloLista.addElement("Caja");
                    } else if (hijo.getForma() instanceof QColisionCapsula) {
                        modeloLista.addElement("Cápsula");
                    } else if (hijo.getForma() instanceof QColisionCilindro) {
                        modeloLista.addElement("Cilindro");
                    } else if (hijo.getForma() instanceof QColisionCono) {
                        modeloLista.addElement("Cono");
                    } else if (hijo.getForma() instanceof QColisionEsfera) {
                        modeloLista.addElement("Esfera");
                    } else if (hijo.getForma() instanceof QColisionTerreno) {
                        modeloLista.addElement("Terreno");
                    } else if (hijo.getForma() instanceof QColisionMallaConvexa) {
                        modeloLista
                                .addElement("Convexa " + ((QColisionMallaConvexa) hijo.getForma()).getMalla().name);
                    } else if (hijo.getForma() instanceof QColisionMallaIndexada) {
                        modeloLista
                                .addElement("Indexada " + ((QColisionMallaIndexada) hijo.getForma()).getMalla().name);
                    } else if (hijo.getForma() instanceof QColisionCompuesta) {
                        modeloLista.addElement("Compuesta ");
                    } else if (hijo.getForma() instanceof AABB) {
                        modeloLista.addElement("AABB");
                    } else {
                        modeloLista.addElement("Forma Colisión");
                    }
                }

            }
        } catch (Exception e) {

        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnEliminarRueda;
    private javax.swing.JComboBox<String> cboRueda;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JList<String> lstRuedas;
    private javax.swing.JTextField txtX;
    private javax.swing.JTextField txtY;
    private javax.swing.JTextField txtZ;
    // End of variables declaration//GEN-END:variables
}
