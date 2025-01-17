/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.qoopo.engine3d.editor.entity.componentes.movimiento;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import net.qoopo.engine.core.entity.component.movement.JumpComponent;
import net.qoopo.engine3d.engines.render.GuiUTIL;

/**
 *
 * @author alberto
 */
public class PnlJumpingComponent extends javax.swing.JPanel {

    private JumpComponent modifier;
    private JLabel lblHeight;

    private JTextField txtHeight;

    private JButton btnAplicar;

    public PnlJumpingComponent(JumpComponent modifier) {
        initComponents();
        this.modifier = modifier;
        txtHeight.setText(String.valueOf(modifier.getHeight()));
    }

    private void aplicarCambios() {
        modifier.setHeight(Float.valueOf(txtHeight.getText()));
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

        lblHeight = new JLabel("Height:");
        txtHeight = new JTextField();

        setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Saltar",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new java.awt.Font("Dialog", 0, 9))); // NOI18N

        btnAplicar = new JButton("Aplicar");
        btnAplicar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                aplicarCambios();
            }

        });

        this.setLayout(new GridLayout(2, 2));
        this.add(lblHeight);
        this.add(txtHeight);

        this.add(GuiUTIL.crearJLabel(""));
        this.add(btnAplicar);
    }

}
