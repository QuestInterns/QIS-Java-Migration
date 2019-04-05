/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qis;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Windows 10
 */
public class Cashier_Hold extends javax.swing.JFrame {

    /**
     * Creates new form Frm_Log
     */
    public Cashier_Hold() {
        this.setUndecorated(true);
        this.setBackground(new Color(0,0,0,0));
        initComponents();
        this.setBackground(new Color(0,0,0,0));
        this.getContentPane().setBackground(new Color(0,0,0,0));
        this.setLocationRelativeTo(null);
        kGradientPanel1.setOpaque(false);
        
        tblTable.addMouseListener(new MouseAdapter() {
        public void mousePressed(MouseEvent mouseEvent) {
            JTable table =(JTable) mouseEvent.getSource();
            Point point = mouseEvent.getPoint();
            int row = table.rowAtPoint(point);
            if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {}
        }
        });
        addColumns();
        
         this.addWindowListener(new WindowAdapter()
        {
            @Override
                public void windowOpened(WindowEvent e) {
                    try
                    {
                    Connection con = DatabaseConnect.con;
                    PreparedStatement command = con.prepareStatement("SELECT f.*, t.* FROM qpd_patient f, qpd_trans t WHERE f.PatientID = t.PatientID AND status = '0' ORDER BY `t`.`TransactionDate` DESC");
                    ResultSet result = command.executeQuery();

                    setTable(result);
                    }catch(Exception ex){JOptionPane.showMessageDialog(null, ex);}
                }
                public void windowClosing(WindowEvent e)
                {
                    if (JOptionPane.showConfirmDialog(null, "Are you sure?", "EXIT",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                    {
                        System.exit(0);
                    }
                    else{}
//                    String drop = "drop table tbltemp;";
//                    DatabaseConnect.runQuery(drop);
//                    System.exit(0);
                }
        });
         
    }

    DefaultTableModel table = new DefaultTableModel()
    {
        public boolean isCellEditable(int row, int column)
        {
            return false;
        }
    };
    
    private void addColumns()
    {
        table.addColumn("Transaction ID");
        table.addColumn("Patient Name");
        table.addColumn("Items");
        table.addColumn("Transaction Date");
    }
    
    private void setTable(ResultSet result)
    {
        try
        {
            int rows=table.getRowCount();
            int z=0;
            
            while(result.next())
            {
                if (!result.getString("ItemID").equals(""))
                {
                    String[] itemID = result.getString("ItemID").split(",");
                    String items = "";
                    z=0;
                    while(z<itemID.length)
                    {
                        Connection con = DatabaseConnect.con;
                        PreparedStatement command = con.prepareStatement("select * from qpd_items where itemID = " + Integer.parseInt(itemID[z]));
                        ResultSet result2 = command.executeQuery(); 
                        while(result2.next())
                        {
                            items += result2.getString("ItemName")+"//";
                        }
                        z++;
                    }

                    items = items.replace("//", ", ");
                    items = items.trim();
                    if(items.charAt(items.length()-1) == ',')
                    {
                        items = items.substring(0, items.length()-1);
                    }

                    table.addRow(new Object[]{
                        result.getString("TransactionID"),
                        result.getString("lastname") + ", " + result.getString("firstname") + " " + result.getString("middlename"),
                        items,
                        result.getString("TransactionDate"),
                    }); 
                }
            }
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        kGradientPanel1 = new keeptoo.KGradientPanel();
        jLabel3 = new javax.swing.JLabel();
        kButton2 = new keeptoo.KButton();
        kButton1 = new keeptoo.KButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0,0,0,0));

        kGradientPanel1.setkBorderRadius(50);
        kGradientPanel1.setkEndColor(new java.awt.Color(24, 47, 89));
        kGradientPanel1.setkStartColor(new java.awt.Color(45, 210, 228));
        kGradientPanel1.setPreferredSize(new java.awt.Dimension(780, 572));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Held Transactions");

        kButton2.setText("CONFIRM");
        kButton2.setkEndColor(new java.awt.Color(45, 210, 228));
        kButton2.setkFillButton(false);
        kButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton2ActionPerformed(evt);
            }
        });

        kButton1.setText("CANCEL");
        kButton1.setkEndColor(new java.awt.Color(45, 210, 228));
        kButton1.setkFillButton(false);
        kButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton1ActionPerformed(evt);
            }
        });

        tblTable.setFont(new java.awt.Font("Copperplate Gothic Bold", 0, 14)); // NOI18N
        tblTable.setModel(table);
        tblTable.setRowHeight(25);
        tblTable.getTableHeader().setReorderingAllowed(false);
        tblTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTableMouseClicked(evt);
            }
        });
        tblTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblTableKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(tblTable);

        javax.swing.GroupLayout kGradientPanel1Layout = new javax.swing.GroupLayout(kGradientPanel1);
        kGradientPanel1.setLayout(kGradientPanel1Layout);
        kGradientPanel1Layout.setHorizontalGroup(
            kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel1Layout.createSequentialGroup()
                .addGap(94, 94, 94)
                .addComponent(kButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(kButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(107, 107, 107))
            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                .addContainerGap(63, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 653, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(64, Short.MAX_VALUE))
            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        kGradientPanel1Layout.setVerticalGroup(
            kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel1Layout.createSequentialGroup()
                .addContainerGap(38, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addGroup(kGradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(kButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(kButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(51, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void kButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_kButton1ActionPerformed

    private void tblTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTableMouseClicked

    }//GEN-LAST:event_tblTableMouseClicked

    private void tblTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblTableKeyPressed

    }//GEN-LAST:event_tblTableKeyPressed

    private void kButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton2ActionPerformed
        try{
            if(tblTable.getSelectedRowCount() > 1)
            {
                JOptionPane.showMessageDialog(null, "Please select only one.");
            }
            else if(tblTable.getSelectedRowCount() < 1)
            {
                JOptionPane.showMessageDialog(null, "Please select one.");
            }
            else
            {
                int row = tblTable.getSelectedRow();

            }
        }
        catch(Exception ex){}
    }//GEN-LAST:event_kButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Cashier_Hold.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Cashier_Hold.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Cashier_Hold.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Cashier_Hold.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        DatabaseConnect.connect();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Cashier_Hold().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane2;
    private keeptoo.KButton kButton1;
    private keeptoo.KButton kButton2;
    private keeptoo.KGradientPanel kGradientPanel1;
    private javax.swing.JTable tblTable;
    // End of variables declaration//GEN-END:variables
}
