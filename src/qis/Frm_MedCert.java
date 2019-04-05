package qis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
public class Frm_MedCert extends javax.swing.JFrame
{
    
private static DecimalFormat dec = new DecimalFormat("0.00");
DefaultTableModel table = new DefaultTableModel()
{
    public boolean isCellEditable(int row, int column)
    {
        return false;
    }
};
private String name, empID, type;

    public Frm_MedCert() 
    {
        this.setUndecorated(true);
        initComponents();
        addColumns();
        setTable();
        
        tblTable.addMouseListener(new MouseAdapter() {
        public void mousePressed(MouseEvent mouseEvent) {
            JTable table =(JTable) mouseEvent.getSource();
            Point point = mouseEvent.getPoint();
            int row = table.rowAtPoint(point);
            if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                btnAdd1.doClick();
            }
        }
    });
        this.addWindowListener(new WindowAdapter()
        {
            @Override
                public void windowOpened(WindowEvent e) {
                    setTable();
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
        Color custom1 = new Color (45,210,228 );  //creates your new color
        getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, custom1));
        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblTable.getColumnModel().getColumn(0).setCellRenderer(rightRenderer);
        tblTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
        tblTable.getColumnModel().getColumn(0).setHeaderRenderer(rightRenderer);
        tblTable.getColumnModel().getColumn(1).setHeaderRenderer(rightRenderer);
        this.setLocationRelativeTo(null);
    }
    String fullname1 = "";
    
    JFrame origin;
    public void form(JFrame orig)
    {
        origin = orig;
    }
    
    String user = "";
    
    public void who(String user2)
    {
        user = user2;
    }
    
    
    public void data(String fullname)
    {
        fullname1 = fullname;
    }
    
    private void updateEntries2()
    {
        int[] rows = tblTable.getSelectedRows();
        JOptionPane.showMessageDialog(null, "Please select one.");
        setTable();
    }
    
    private void addColumns()
    {
        table.addColumn("Diagnosis");
        table.addColumn("Date Created");
    }
    
    private void setTable()
    {
        try
        {
            Connection con = DatabaseConnect.con;
            PreparedStatement command = con.prepareStatement("select * from qpd_medcert where fullname1 = '"+txtFull.getText().replace(",", "")+"'");
            ResultSet result = command.executeQuery();
            
            int rows=table.getRowCount();
            int z=0;
            while(z<rows)
            {
                table.removeRow(0);
                z++;
            }
            
            while(result.next())
            {
                table.addRow(new Object[]{
                    result.getString("diagnosis"),
                    result.getString("dateCreated"),
                }); 
            }
        }
        catch(Exception ex)
        {
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        btnAdd1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtFull = new javax.swing.JLabel();

        jMenu1.setText("jMenu1");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1280, 720));

        jButton2.setFont(new java.awt.Font("Copperplate Gothic Bold", 0, 12)); // NOI18N
        jButton2.setText("CLOSE");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        btnAdd1.setFont(new java.awt.Font("Copperplate Gothic Bold", 0, 12)); // NOI18N
        btnAdd1.setText("PRINT");
        btnAdd1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdd1ActionPerformed(evt);
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

        jLabel2.setFont(new java.awt.Font("Copperplate Gothic Bold", 0, 24)); // NOI18N
        jLabel2.setText("Medical Certificate");

        jLabel1.setFont(new java.awt.Font("Copperplate Gothic Bold", 0, 18)); // NOI18N
        jLabel1.setText("Name:");

        txtFull.setFont(new java.awt.Font("Copperplate Gothic Bold", 0, 16)); // NOI18N
        txtFull.setText("1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(btnAdd1, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 489, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(txtFull, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(27, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtFull, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdd1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(43, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton2ActionPerformed
    {//GEN-HEADEREND:event_jButton2ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnAdd1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnAdd1ActionPerformed
    {//GEN-HEADEREND:event_btnAdd1ActionPerformed
        try{
            if(tblTable.getSelectedRowCount() > 1)
            {
                updateEntries();
            }
            else if(tblTable.getSelectedRowCount() < 1)
            {
                updateEntries2();
            }
            else
            {
            int row = tblTable.getSelectedRow();

            }
        }
        catch(Exception ex){}
    }//GEN-LAST:event_btnAdd1ActionPerformed

    private void tblTableMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tblTableMouseClicked
    {//GEN-HEADEREND:event_tblTableMouseClicked

    }//GEN-LAST:event_tblTableMouseClicked

    private void tblTableKeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_tblTableKeyPressed
    {//GEN-HEADEREND:event_tblTableKeyPressed
        if(tblTable.getSelectedRow() != -1)
        updateEntries();
    }//GEN-LAST:event_tblTableKeyPressed

    private void updateEntries()
    {
        int[] rows = tblTable.getSelectedRows();
        JOptionPane.showMessageDialog(null, "Please only select one.");
        setTable();
    }
    
    public static void main(String args[]) 
    {

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
            java.util.logging.Logger.getLogger(Frm_MedCert.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Frm_MedCert.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Frm_MedCert.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Frm_MedCert.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        java.awt.EventQueue.invokeLater(new Runnable() 
        {
            public void run() 
            {
                new Frm_MedCert().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblTable;
    public javax.swing.JLabel txtFull;
    // End of variables declaration//GEN-END:variables
}
