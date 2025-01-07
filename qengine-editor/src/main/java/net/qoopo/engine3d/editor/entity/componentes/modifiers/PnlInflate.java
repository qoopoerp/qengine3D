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

import net.qoopo.engine.core.entity.component.modifier.generate.InflateModifier;
import net.qoopo.engine3d.engines.render.GuiUTIL;

/**
 *
 * @author alberto
 */
public class PnlInflate extends javax.swing.JPanel {

    private JTextField txtRadious;
    private InflateModifier modifier;
    private JLabel lblRadious;

    private JButton btnAplicar;

    public PnlInflate(InflateModifier modifier) {
        initComponents();
        this.modifier = modifier;
        txtRadious.setText(String.valueOf(modifier.getRadio()));
    }

    private void aplicarCambios() {
        modifier.setRadio(Float.valueOf(txtRadious.getText()));
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

        lblRadious = new JLabel("Radio :");
        txtRadious = new JTextField();

        setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Inflar",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new java.awt.Font("Dialog", 0, 9))); // NOI18N

        btnAplicar = new JButton("Aplicar");
        btnAplicar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                aplicarCambios();
            }

        });

        this.setLayout(new GridLayout(3, 2));
        this.add(lblRadious);
        this.add(txtRadious);
        this.add(GuiUTIL.crearJLabel(""));
        this.add(btnAplicar);
    }

}
