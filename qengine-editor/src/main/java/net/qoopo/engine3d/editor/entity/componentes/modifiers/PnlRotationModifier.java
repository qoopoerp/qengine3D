/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.editor.entity.componentes.modifiers;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import net.qoopo.engine.core.entity.component.modifier.deform.RotationModifier;
import net.qoopo.engine3d.engines.render.GuiUTIL;

/**
 *
 * @author alberto
 */
public class PnlRotationModifier extends javax.swing.JPanel {

    private RotationModifier modifier;
    private JLabel lblX;
    private JLabel lblY;
    private JLabel lblZ;

    private JTextField txtX;
    private JTextField txtY;
    private JTextField txtZ;
    private JButton btnAplicar;

    public PnlRotationModifier(RotationModifier modifier) {
        initComponents();
        this.modifier = modifier;
        txtX.setText(String.valueOf(Math.toDegrees(modifier.getRadiansX())));
        txtY.setText(String.valueOf(Math.toDegrees(modifier.getRadiansY())));
        txtZ.setText(String.valueOf(Math.toDegrees(modifier.getRadiansZ())));
    }

    private void aplicarCambios() {
        modifier.setRadiansX((float)Math.toRadians(Float.valueOf(txtX.getText())));
        modifier.setRadiansY((float)Math.toRadians(Float.valueOf(txtY.getText())));
        modifier.setRadiansZ((float)Math.toRadians(Float.valueOf(txtZ.getText())));
        modifier.changed();
        // modifier.build();
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

        lblX = new JLabel("X:");
        txtX = new JTextField();

        lblY = new JLabel("Y:");
        txtY = new JTextField();

        lblZ = new JLabel("Z:");
        txtZ = new JTextField();

        setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Rotar malla",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new java.awt.Font("Dialog", 0, 9))); // NOI18N

        btnAplicar = new JButton("Aplicar");
        btnAplicar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                aplicarCambios();
            }

        });

        this.setLayout(new GridLayout(4, 2));
        this.add(lblX);
        this.add(txtX);
        this.add(lblY);
        this.add(txtY);
        this.add(lblZ);
        this.add(txtZ);
        this.add(GuiUTIL.crearJLabel(""));
        this.add(btnAplicar);
    }

}
