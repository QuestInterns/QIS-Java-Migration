/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package table;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Windows 10
 */
public class FrmTable extends javax.swing.JFrame {

    /**
     * Creates new form FrmTable
     */
    public FrmTable() {
        initComponents();
        addColumns();
        tblTable.setModel(list);
        
        try
        {
        Connection con = DatabaseConnect.con;
        PreparedStatement command = con.prepareStatement("SELECT f.*, t.* FROM qpd_patient f, qpd_trans t WHERE f.PatientID = t.PatientID AND status = '1' ORDER BY `t`.`TransactionDate` DESC");
        ResultSet result = command.executeQuery();

        setTable(result);
        }catch(Exception ex){JOptionPane.showMessageDialog(null, ex);}
    }

    DefaultTableModel list = new DefaultTableModel()
    {
        public boolean isCellEditable(int row, int column)
        {
            return false;
        }
    };
    
    private void addColumns()
    {
        list.addColumn("Date/Time");
        list.addColumn("Transaction Type");
        list.addColumn("Transaction No.");
        list.addColumn("Customer Name");
        list.addColumn("Company");
        list.addColumn("Package");
    }
    
    private void setTable(ResultSet result)
    {
        try
        {
            int rows=list.getRowCount();
            int z=0;

            while(result.next())
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
                
                list.addRow(new Object[]{
                    result.getString("CreationDate"),
                    result.getString("TransactionType"),
                    result.getString("TransactionID"),
                    result.getString("LastName") + ", " + result.getString("FirstName") + " " + result.getString("MiddleName"),
                    result.getString("CompanyName"),
                    items,
                });
            }
        }
        catch(Exception ex)
        {
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

        jScrollPane3 = new javax.swing.JScrollPane();
        tblTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tblTable.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tblTable.setModel(list);
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
        jScrollPane3.setViewportView(tblTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(54, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 753, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(55, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(80, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 476, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(97, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTableMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblTableMouseClicked

    private void tblTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblTableKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblTableKeyPressed

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
            java.util.logging.Logger.getLogger(FrmTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmTable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        DatabaseConnect.connect();
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmTable().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable tblTable;
    // End of variables declaration//GEN-END:variables
}