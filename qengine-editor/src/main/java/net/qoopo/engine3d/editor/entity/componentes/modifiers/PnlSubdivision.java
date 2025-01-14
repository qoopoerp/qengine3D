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
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import net.qoopo.engine.core.entity.component.modifier.generate.SubdivisionModifier;
import net.qoopo.engine.core.util.array.StringArray;
import net.qoopo.engine3d.engines.render.GuiUTIL;

/**
 *
 * @author alberto
 */
public class PnlSubdivision extends javax.swing.JPanel {

    private JComboBox<String> cmbType;
    private JTextField txtTimes;
    private SubdivisionModifier modifier;
    private JLabel lblType;
    private JLabel lblTimes;

    private JButton btnAplicar;

    public PnlSubdivision(SubdivisionModifier modifier) {
        initComponents();
        this.modifier = modifier;
        txtTimes.setText(String.valueOf(modifier.getTimes()));
        cmbType.setSelectedIndex(modifier.getType());
    }

    private void aplicarCambios() {
        modifier.setTimes(Integer.valueOf(txtTimes.getText()));
        modifier.setType(cmbType.getSelectedIndex());
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

        lblTimes = new JLabel("Veces:");
        txtTimes = new JTextField();

        lblType = new JLabel("Tipo:");
        cmbType = new JComboBox<>(StringArray.of("Simple", "Catmull-Clark"));

        setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Subdivison",
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
        this.add(lblTimes);
        this.add(txtTimes);
        this.add(lblType);
        this.add(cmbType);
        this.add(GuiUTIL.crearJLabel(""));
        this.add(btnAplicar);
    }

}