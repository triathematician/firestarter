package com.googlecode.blaisemath.util;

import java.awt.*;
import java.util.Random;
import javax.swing.JLabel;

public class MPanelTestFrame extends javax.swing.JFrame {

    public MPanelTestFrame() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mPanel1 = new com.googlecode.blaisemath.util.MPanel();
        jToolBar1 = new javax.swing.JToolBar();
        changeText = new javax.swing.JButton();
        addPanel = new javax.swing.JButton();
        clearRollup = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        rollup = new com.googlecode.blaisemath.util.RollupPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().add(mPanel1, java.awt.BorderLayout.SOUTH);

        jToolBar1.setRollover(true);

        changeText.setText("Change text");
        changeText.setFocusable(false);
        changeText.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        changeText.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        changeText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeTextActionPerformed(evt);
            }
        });
        jToolBar1.add(changeText);

        addPanel.setText("Add panel to rollup");
        addPanel.setFocusable(false);
        addPanel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addPanel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        addPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPanelActionPerformed(evt);
            }
        });
        jToolBar1.add(addPanel);

        clearRollup.setText("Clear rollup");
        clearRollup.setFocusable(false);
        clearRollup.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        clearRollup.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        clearRollup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearRollupActionPerformed(evt);
            }
        });
        jToolBar1.add(clearRollup);

        getContentPane().add(jToolBar1, java.awt.BorderLayout.PAGE_START);

        jScrollPane1.setViewportView(rollup);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void clearRollupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearRollupActionPerformed
        rollup.removeAll();
        rollup.revalidate();
        rollup.repaint();
    }//GEN-LAST:event_clearRollupActionPerformed

    private void addPanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPanelActionPerformed
        rollup.add(new JLabel("here is some text"), randomTitle());
        rollup.revalidate();
        jScrollPane1.repaint();
    }//GEN-LAST:event_addPanelActionPerformed

    private void changeTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeTextActionPerformed
        ((JLabel) mPanel1.getPrimaryComponent()).setText(randomText());
        if (rollup.getComponentCount() > 0) {
            int i = new Random().nextInt(rollup.getComponentCount());
            if (rollup.getComponent(i) instanceof MPanel) {
                JLabel label = (JLabel) ((MPanel) rollup.getComponent(i)).getPrimaryComponent();
                label.setText(randomText());
            }
        }
    }//GEN-LAST:event_changeTextActionPerformed

    private static String randomTitle() {
        String[] opts = { "alpha", "beta", "gamma", "delta", "epsilon" };
        int i = new Random().nextInt(5);
        return opts[i];
    }
    
    private static String randomText() {
        Random r = new Random();
        String[] opts = { "alpha", "beta", "gamma", "delta", "epsilon" };
        StringBuilder sb = new StringBuilder();
        if (r.nextBoolean()) {
            sb.append("<html>");
        }
        sb.append(opts[r.nextInt(5)]);
        for (int i = 0; i < r.nextInt(20); i++) {
            sb.append(" ").append(opts[r.nextInt(5)]);
        }
        return sb.toString();
    }

    public static void main(String args[]) {
        EventQueue.invokeLater(() -> new MPanelTestFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addPanel;
    private javax.swing.JButton changeText;
    private javax.swing.JButton clearRollup;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private com.googlecode.blaisemath.util.MPanel mPanel1;
    private com.googlecode.blaisemath.util.RollupPanel rollup;
    // End of variables declaration//GEN-END:variables
}
