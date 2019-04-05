/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import keeptoo.Drag;

/**
 *
 * @author Windows 10
 */
public class Cashier_Transact extends javax.swing.JFrame {

    /**
     * Creates new form Frm_Login
     */
    public Cashier_Transact() {
        initComponents();
        this.setBackground(Color.WHITE);
        this.addWindowListener(new WindowAdapter()
        {
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
        
        try{
        tblTable.addMouseListener(new MouseAdapter() {
        public void mousePressed(MouseEvent mouseEvent) {
            JTable table =(JTable) mouseEvent.getSource();
            Point point = mouseEvent.getPoint();
            int row = table.rowAtPoint(point);
            if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {}
        }
        });
        }
        catch(Exception ex){JOptionPane.showMessageDialog(null, "Enter number only.");}
        AutoCompletion.enable(this.cmbAll);
        AutoCompletion.enable(this.cmbAccounts);
        AutoCompletion.enable(this.cmbPatients);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        populateAll();
        populateAccounts();
        populateNames();
        lblName.setText("");
        addColumns();
        tblTable.setModel(list);
        tblTable.getTableHeader().setBackground(custom1);
        list.removeRow(0);
        list.removeRow(0);
        btnCash.setEnabled(false);
    }

    private static DecimalFormat dec = new DecimalFormat("0.00");
    Color custom1 = new Color (25,115,125 );
    DefaultTableModel table = new DefaultTableModel()
    {
        public boolean isCellEditable(int row, int column)
        {
            return false;
        }
    };
    
    DefaultTableModel list = new DefaultTableModel()
    {
        public boolean isCellEditable(int row, int column)
        {
            return false;
        }
    };
    
    int selected = -1;
    
    public void populateAll()
    {
        try
        {
            Connection con = DatabaseConnect.con;
            PreparedStatement command = con.prepareStatement("select * from qpd_items order by ItemName");  
            ResultSet result = command.executeQuery();
            
            while(result.next())
            {
                cmbAll.addItem(result.getString("ItemName") + " / " + result.getString("ItemPrice")); 
            }
        }
        catch(Exception ex){
                JOptionPane.showMessageDialog(null, ex);}
    }
    
    public void populateAccounts()
    {
        try
        {
            Connection con = DatabaseConnect.con;
            PreparedStatement command = con.prepareStatement("select * from qpd_items where ItemType LIKE '%Account%' order by ItemName");  
            ResultSet result = command.executeQuery();
            
            while(result.next())
            {
                cmbAccounts.addItem(result.getString("ItemName") + " / " + result.getString("ItemPrice")); 
            }
        }
        catch(Exception ex){
                JOptionPane.showMessageDialog(null, ex);}
    }
    
    ArrayList<String> ids = new ArrayList<String>();
    String type = "Cash";
    Authentication auth = new Authentication();
    
    public void populateNames()
    {
        try
        {
            Connection con = DatabaseConnect.con;
            PreparedStatement command = con.prepareStatement("select * from qpd_patient order by CreationDate");
            ResultSet result = command.executeQuery();
            
            while(result.next())
            {
                cmbPatients.addItem(result.getString("lastname") + ", " + result.getString("firstname") + " " + result.getString("middlename")); 
                ids.add(result.getString("patientid"));
            }
        }
        catch(Exception ex){
                JOptionPane.showMessageDialog(null, ex);}
    }
    
    private void addColumns()
    {
        list.addColumn("Item ID");
        list.addColumn("Item Name");
        list.addColumn("Price");
        list.addColumn("Quantity");
        list.addColumn("Discount (%)");
        list.addColumn("Total");
    }
    
    private void setTable(ResultSet result)
    {
        try
        {
            int rows=list.getRowCount();
            int z=0;
            
            while(result.next())
            {
                list.addRow(new Object[]{
                    result.getString("ItemID"),
                    result.getString("ItemName"),
                    result.getString("ItemPrice"),
                    "1",
                    "0",
                    result.getString("ItemPrice"),
                }); 
            }
            setTotal();
        }
        catch(Exception ex)
        {
        }
    }
    
    String itemID = "", itemQTY = "", itemDISC = "";
    private void getAll()
    {
        int rows=list.getRowCount();
        int z=0;

        while(z<rows)
        {
            itemID += tblTable.getValueAt(z, 0) + ", ";
            itemQTY += tblTable.getValueAt(z, 3) + ", ";
            itemDISC += tblTable.getValueAt(z, 4) + ", ";
            z++;
        }
        
        itemID = itemID.trim();
        itemQTY = itemQTY.trim();
        
        if(itemID.charAt(itemID.length()-1) == ',')
        {
            itemID = itemID.substring(0, itemID.length()-1);
        }
        
        if(itemQTY.charAt(itemQTY.length()-1) == ',')
        {
            itemQTY = itemQTY.substring(0, itemQTY.length()-1);
        }
        
        if(itemDISC.charAt(itemDISC.length()-1) == ',')
        {
            itemDISC = itemDISC.substring(0, itemDISC.length()-1);
        }
    }
    
    private void setTotal()
    {
        
            int rows=list.getRowCount();
            int z=0;
            double total = 0;
            while(z<rows)
            {
                total += Double.parseDouble(list.getValueAt(z, 5).toString());
                z++;
            }
            
            lblTotal.setText(""+dec.format(total));
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlMain = new keeptoo.KGradientPanel();
        kGradientPanel2 = new keeptoo.KGradientPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        cmbAll = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        cmbAccounts = new javax.swing.JComboBox<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblTable = new javax.swing.JTable();
        kButton1 = new keeptoo.KButton();
        kButton2 = new keeptoo.KButton();
        kButton3 = new keeptoo.KButton();
        kButton4 = new keeptoo.KButton();
        lblName = new javax.swing.JLabel();
        cmbPatients = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        lblName1 = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        kButton5 = new keeptoo.KButton();
        btnCash = new keeptoo.KButton();
        btnAccount = new keeptoo.KButton();
        jLabel15 = new javax.swing.JLabel();
        btnCash1 = new keeptoo.KButton();
        btnCash2 = new keeptoo.KButton();
        btnCash3 = new keeptoo.KButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        pnlMain.setkEndColor(new java.awt.Color(255, 255, 255));
        pnlMain.setkStartColor(new java.awt.Color(255, 255, 255));

        kGradientPanel2.setkEndColor(new java.awt.Color(45, 210, 228));
        kGradientPanel2.setkStartColor(new java.awt.Color(24, 47, 89));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/qis/icons8_Exit_25px.png"))); // NOI18N
        jLabel3.setText("LOGOUT");
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel3MousePressed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/qis/icons8_Exit_25px.png"))); // NOI18N
        jLabel4.setText("Transact");
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel4MousePressed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/qis/icons8_Exit_25px.png"))); // NOI18N
        jLabel5.setText("Transaction List");
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel5MousePressed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/qis/icons8_Exit_25px.png"))); // NOI18N
        jLabel6.setText("HMO List");
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel6MousePressed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/qis/icons8_Exit_25px.png"))); // NOI18N
        jLabel7.setText("Sales Report");
        jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel7MousePressed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/qis/icons8_Exit_25px.png"))); // NOI18N
        jLabel8.setText("User:");
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel8MousePressed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Cashier");
        jLabel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel9MousePressed(evt);
            }
        });

        javax.swing.GroupLayout kGradientPanel2Layout = new javax.swing.GroupLayout(kGradientPanel2);
        kGradientPanel2.setLayout(kGradientPanel2Layout);
        kGradientPanel2Layout.setHorizontalGroup(
            kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel2Layout.createSequentialGroup()
                .addGroup(kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(kGradientPanel2Layout.createSequentialGroup()
                            .addGroup(kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(kGradientPanel2Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(kGradientPanel2Layout.createSequentialGroup()
                                    .addGap(25, 25, 25)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(80, 80, 80))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel2Layout.createSequentialGroup()
                            .addGroup(kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(47, 47, 47)))
                    .addGroup(kGradientPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        kGradientPanel2Layout.setVerticalGroup(
            kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel2Layout.createSequentialGroup()
                .addGap(77, 77, 77)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(kGradientPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45))
        );

        cmbAll.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { }));
        cmbAll.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbAllItemStateChanged(evt);
            }
        });
        cmbAll.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                cmbAllCaretPositionChanged(evt);
            }
        });
        cmbAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbAllActionPerformed(evt);
            }
        });
        cmbAll.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                cmbAllPropertyChange(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel10.setText("ACCOUNTS ITEM");
        jLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel10MousePressed(evt);
            }
        });

        cmbAccounts.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { }));
        cmbAccounts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbAccountsActionPerformed(evt);
            }
        });

        tblTable.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
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
        jScrollPane3.setViewportView(tblTable);

        kButton1.setText("CANCEL");
        kButton1.setkEndColor(new java.awt.Color(255, 204, 153));
        kButton1.setkStartColor(new java.awt.Color(255, 102, 0));
        kButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton1ActionPerformed(evt);
            }
        });

        kButton2.setText("DISCARD");
        kButton2.setkEndColor(new java.awt.Color(255, 204, 204));
        kButton2.setkStartColor(new java.awt.Color(255, 51, 51));
        kButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton2ActionPerformed(evt);
            }
        });

        kButton3.setText("SAVE & PRINT");
        kButton3.setkEndColor(new java.awt.Color(24, 47, 89));
        kButton3.setkStartColor(new java.awt.Color(45, 210, 228));
        kButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton3ActionPerformed(evt);
            }
        });

        kButton4.setText("HOLD");
        kButton4.setkEndColor(new java.awt.Color(0, 255, 204));
        kButton4.setkStartColor(new java.awt.Color(0, 153, 102));
        kButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton4ActionPerformed(evt);
            }
        });

        lblName.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblName.setText("Patient Name");
        lblName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblNameMousePressed(evt);
            }
        });

        cmbPatients.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { }));
        cmbPatients.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbPatientsItemStateChanged(evt);
            }
        });
        cmbPatients.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                cmbPatientsCaretPositionChanged(evt);
            }
        });
        cmbPatients.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbPatientsActionPerformed(evt);
            }
        });
        cmbPatients.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                cmbPatientsPropertyChange(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel12.setText("PATIENT NAME");
        jLabel12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel12MousePressed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel13.setText("No.: ");
        jLabel13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel13MousePressed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel14.setText("97153026");
        jLabel14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel14MousePressed(evt);
            }
        });

        lblName1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblName1.setText("Total:");
        lblName1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblName1MousePressed(evt);
            }
        });

        lblTotal.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTotal.setText("0.00");
        lblTotal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblTotalMousePressed(evt);
            }
        });

        kButton5.setText("HELD TRANSACTIONS");
        kButton5.setkEndColor(new java.awt.Color(204, 204, 255));
        kButton5.setkStartColor(new java.awt.Color(153, 51, 255));
        kButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kButton5ActionPerformed(evt);
            }
        });

        btnCash.setText("CASH");
        btnCash.setkEndColor(new java.awt.Color(102, 204, 255));
        btnCash.setkStartColor(new java.awt.Color(36, 107, 221));
        btnCash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCashActionPerformed(evt);
            }
        });

        btnAccount.setText("ACCOUNT");
        btnAccount.setkEndColor(new java.awt.Color(255, 204, 204));
        btnAccount.setkStartColor(new java.awt.Color(255, 102, 102));
        btnAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccountActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel15.setText("TYPE:");
        jLabel15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel15MousePressed(evt);
            }
        });

        btnCash1.setText("10% DISCOUNT");
        btnCash1.setkEndColor(new java.awt.Color(102, 204, 255));
        btnCash1.setkStartColor(new java.awt.Color(36, 107, 221));
        btnCash1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCash1ActionPerformed(evt);
            }
        });

        btnCash2.setText("20% DISCOUNT");
        btnCash2.setkEndColor(new java.awt.Color(102, 204, 255));
        btnCash2.setkStartColor(new java.awt.Color(36, 107, 221));
        btnCash2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCash2ActionPerformed(evt);
            }
        });

        btnCash3.setText("CUSTOM DISCOUNT");
        btnCash3.setkEndColor(new java.awt.Color(102, 204, 255));
        btnCash3.setkStartColor(new java.awt.Color(36, 107, 221));
        btnCash3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCash3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlMainLayout = new javax.swing.GroupLayout(pnlMain);
        pnlMain.setLayout(pnlMainLayout);
        pnlMainLayout.setHorizontalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainLayout.createSequentialGroup()
                .addComponent(kGradientPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlMainLayout.createSequentialGroup()
                        .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblName, javax.swing.GroupLayout.PREFERRED_SIZE, 518, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cmbAll, javax.swing.GroupLayout.PREFERRED_SIZE, 540, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(pnlMainLayout.createSequentialGroup()
                                        .addGap(202, 202, 202)
                                        .addComponent(jLabel10)))
                                .addComponent(cmbAccounts, javax.swing.GroupLayout.PREFERRED_SIZE, 540, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
                        .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlMainLayout.createSequentialGroup()
                                .addComponent(btnCash1, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnCash2, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnCash3, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(cmbPatients, javax.swing.GroupLayout.PREFERRED_SIZE, 540, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(pnlMainLayout.createSequentialGroup()
                                    .addComponent(jLabel12)
                                    .addGap(214, 214, 214))
                                .addGroup(pnlMainLayout.createSequentialGroup()
                                    .addComponent(jLabel15)
                                    .addGap(18, 18, 18)
                                    .addComponent(btnCash, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel13)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel14)))))
                    .addComponent(jScrollPane3)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlMainLayout.createSequentialGroup()
                        .addComponent(kButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(kButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(kButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlMainLayout.createSequentialGroup()
                                .addComponent(lblName1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlMainLayout.createSequentialGroup()
                                .addComponent(kButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(kButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        pnlMainLayout.setVerticalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 695, Short.MAX_VALUE)
            .addGroup(pnlMainLayout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnAccount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnCash, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cmbAll, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbPatients, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbAccounts, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnCash1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnCash2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnCash3, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblName1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(kButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(kButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(kButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(kButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(kButton5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        populateAll();
        populateAll();
        populateAll();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMain, javax.swing.GroupLayout.DEFAULT_SIZE, 1432, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMain, javax.swing.GroupLayout.DEFAULT_SIZE, 695, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel7MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel7MousePressed

    private void jLabel6MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MousePressed
        Cashier_HMOList main = new Cashier_HMOList();
        main.setVisible(true);
        this.dispose();
        main.toFront();
    }//GEN-LAST:event_jLabel6MousePressed

    private void jLabel5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MousePressed
        Cashier_TransactionList main = new Cashier_TransactionList();
        main.setVisible(true);
        this.dispose();
        main.toFront();
    }//GEN-LAST:event_jLabel5MousePressed

    private void jLabel4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MousePressed

    }//GEN-LAST:event_jLabel4MousePressed

    private void jLabel3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MousePressed
        Frm_Log main = new Frm_Log();
        main.setVisible(true);
        this.dispose();
        main.toFront();
    }//GEN-LAST:event_jLabel3MousePressed

    private void jLabel8MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel8MousePressed

    private void jLabel9MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel9MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel9MousePressed

    private void tblTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTableMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblTableMouseClicked

    private void tblTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblTableKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblTableKeyPressed

    private void kButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton1ActionPerformed
        Cashier_Transact main = new Cashier_Transact();
        main.setVisible(true);
        this.dispose();
        main.toFront();
    }//GEN-LAST:event_kButton1ActionPerformed

    private void kButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton2ActionPerformed
        try
        {
            int rows=list.getRowCount();
            int z=0;
            
            while(z<rows)
            {
                list.removeRow(0);
                z++;
            }
        }
        catch(Exception ex)
        {
        }
    }//GEN-LAST:event_kButton2ActionPerformed

    private void kButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton3ActionPerformed
        if(!lblName.getText().isEmpty() && tblTable.getRowCount() != 0)
        {
            try
            {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd 00:00:00");
                LocalDateTime now = LocalDateTime.now();
                String query = "INSERT INTO qpd_trans( TransactionRef, PatientID, TransactionType, Cashier, ItemID, ItemQTY, Biller, TotalPrice, Discount, GrandTotal, TransactionDate, SalesType, status, PaidIn, PaidOut) VALUES ('0000', '"+ids.get(selected)+"', '"+type+"', '"+auth.getAuth()+"', '"+itemID+"', '"+itemQTY+"', '', '', '"+itemDISC+"', '"+lblTotal.getText()+"', '"+dtf.format(now)+"', 'sales', '1', '', '')";
                DatabaseConnect.runQuery(query);

                JOptionPane.showMessageDialog(null, "Transaction added successfully.");
            }
            catch(Exception ex){
                    JOptionPane.showMessageDialog(null, ex);}
        }
    }//GEN-LAST:event_kButton3ActionPerformed

    private void kButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton4ActionPerformed
        if(!lblName.getText().isEmpty() && tblTable.getRowCount() != 0)
        {
            try
            {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd 00:00:00");
                LocalDateTime now = LocalDateTime.now();
                String query = "INSERT INTO qpd_trans( TransactionRef, PatientID, TransactionType, Cashier, ItemID, ItemQTY, Biller, TotalPrice, Discount, GrandTotal, TransactionDate, SalesType, status, PaidIn, PaidOut) VALUES ('0000', '"+ids.get(selected)+"', '"+type+"', '"+auth.getAuth()+"', '"+itemID+"', '"+itemQTY+"', '', '', '"+itemDISC+"', '"+lblTotal.getText()+"', '"+dtf.format(now)+"', 'sales', '0', '', '')";
                DatabaseConnect.runQuery(query);

                JOptionPane.showMessageDialog(null, "Transaction added successfully.");
            }
            catch(Exception ex){
                    JOptionPane.showMessageDialog(null, ex);}
        }
    }//GEN-LAST:event_kButton4ActionPerformed

    private void lblName1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblName1MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_lblName1MousePressed

    private void lblTotalMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTotalMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_lblTotalMousePressed

    private void jLabel13MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel13MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel13MousePressed

    private void jLabel14MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel14MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel14MousePressed

    private void lblNameMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblNameMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_lblNameMousePressed

    private void jLabel12MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel12MousePressed

    private void cmbPatientsPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_cmbPatientsPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbPatientsPropertyChange

    private void cmbPatientsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbPatientsActionPerformed
        lblName.setText(cmbPatients.getSelectedItem().toString());
        selected = cmbPatients.getSelectedIndex();
    }//GEN-LAST:event_cmbPatientsActionPerformed

    private void cmbPatientsCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_cmbPatientsCaretPositionChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbPatientsCaretPositionChanged

    private void cmbPatientsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbPatientsItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbPatientsItemStateChanged

    private void cmbAccountsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbAccountsActionPerformed
        try{
            String item = cmbAccounts.getSelectedItem().toString();
            String name = item.split("/")[0].trim();
            String price = item.split("/")[1].trim();
            Connection con = DatabaseConnect.con;
            PreparedStatement command = con.prepareStatement("select * from qpd_items where ItemName = '"+name+"' AND ItemPrice = '"+price+"'");
            ResultSet result = command.executeQuery();
            setTable(result);
        }
        catch(Exception ex){JOptionPane.showMessageDialog(null, ex);}
    }//GEN-LAST:event_cmbAccountsActionPerformed

    private void jLabel10MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel10MousePressed

    private void cmbAllPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_cmbAllPropertyChange

    }//GEN-LAST:event_cmbAllPropertyChange

    private void cmbAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbAllActionPerformed
        try{
            String item = cmbAll.getSelectedItem().toString();
            String name = item.split("/")[0].trim();
            String price = item.split("/")[1].trim();
            Connection con = DatabaseConnect.con;
            PreparedStatement command = con.prepareStatement("select * from qpd_items where ItemName = '"+name+"' AND ItemPrice = '"+price+"'");
            ResultSet result = command.executeQuery();
            setTable(result);
        }
        catch(Exception ex){JOptionPane.showMessageDialog(null, ex);}
    }//GEN-LAST:event_cmbAllActionPerformed

    private void cmbAllCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_cmbAllCaretPositionChanged

    }//GEN-LAST:event_cmbAllCaretPositionChanged

    private void cmbAllItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbAllItemStateChanged

    }//GEN-LAST:event_cmbAllItemStateChanged

    private void kButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kButton5ActionPerformed
        Cashier_Hold main = new Cashier_Hold();
        main.setVisible(true);
        main.toFront();
    }//GEN-LAST:event_kButton5ActionPerformed

    private void btnCashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCashActionPerformed
        type = "Cash";
        btnCash.setEnabled(false);
    }//GEN-LAST:event_btnCashActionPerformed

    private void btnAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccountActionPerformed
        type = "Account";
        btnAccount.setEnabled(false);
    }//GEN-LAST:event_btnAccountActionPerformed

    private void jLabel15MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel15MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel15MousePressed

    private void btnCash1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCash1ActionPerformed
        int rows = tblTable.getRowCount();
        int z = 0;
        while (z<rows)
        {
            list.setValueAt("10", z, 4);
            double price = Double.parseDouble(list.getValueAt(z, 2).toString()) * Double.parseDouble(list.getValueAt(z, 3).toString()) - (Double.parseDouble(list.getValueAt(z, 2).toString()) * Double.parseDouble(list.getValueAt(z, 3).toString()) * Double.parseDouble(list.getValueAt(z, 4).toString()))/100;
            list.setValueAt(dec.format(price), z, 5);
            z++;
        }
        
        setTotal();
    }//GEN-LAST:event_btnCash1ActionPerformed

    private void btnCash2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCash2ActionPerformed
        int rows = tblTable.getRowCount();
        int z = 0;
        while (z<rows)
        {
            list.setValueAt("20", z, 4);
            double price = Double.parseDouble(list.getValueAt(z, 2).toString()) * Double.parseDouble(list.getValueAt(z, 3).toString()) - (Double.parseDouble(list.getValueAt(z, 2).toString()) * Double.parseDouble(list.getValueAt(z, 3).toString()) * Double.parseDouble(list.getValueAt(z, 4).toString()))/100;
            list.setValueAt(dec.format(price), z, 5);
            z++;
        }
        setTotal();
    }//GEN-LAST:event_btnCash2ActionPerformed

    private void btnCash3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCash3ActionPerformed
        String val = JOptionPane.showInputDialog("Enter discount:");
        
        try{
        Double.parseDouble(val);
        int rows = tblTable.getRowCount();
        int z = 0;
        while (z<rows)
        {
            list.setValueAt(val, z, 4);
            double price = Double.parseDouble(list.getValueAt(z, 2).toString()) * Double.parseDouble(list.getValueAt(z, 3).toString()) - (Double.parseDouble(list.getValueAt(z, 2).toString()) * Double.parseDouble(list.getValueAt(z, 3).toString()) * Double.parseDouble(list.getValueAt(z, 4).toString()))/100;
            list.setValueAt(dec.format(price), z, 5);
            z++;
        }
        setTotal();
        }
        catch(Exception ex){JOptionPane.showMessageDialog(null, "Input numbers only.");}
    }//GEN-LAST:event_btnCash3ActionPerformed

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
            java.util.logging.Logger.getLogger(Cashier_Transact.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Cashier_Transact.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Cashier_Transact.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Cashier_Transact.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Cashier_Transact().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private keeptoo.KButton btnAccount;
    private keeptoo.KButton btnCash;
    private keeptoo.KButton btnCash1;
    private keeptoo.KButton btnCash2;
    private keeptoo.KButton btnCash3;
    private javax.swing.JComboBox<String> cmbAccounts;
    private javax.swing.JComboBox<String> cmbAll;
    private javax.swing.JComboBox<String> cmbPatients;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane3;
    private keeptoo.KButton kButton1;
    private keeptoo.KButton kButton2;
    private keeptoo.KButton kButton3;
    private keeptoo.KButton kButton4;
    private keeptoo.KButton kButton5;
    private keeptoo.KGradientPanel kGradientPanel2;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblName1;
    private javax.swing.JLabel lblTotal;
    private keeptoo.KGradientPanel pnlMain;
    private javax.swing.JTable tblTable;
    // End of variables declaration//GEN-END:variables
}
