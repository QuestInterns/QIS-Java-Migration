/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tabletopdf_csv;


import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 *
 * @author Administrator
 */
public class TBL2PDF_CSV11 extends javax.swing.JFrame {

    /**
     * Creates new form TBL2PDF_CSV
     */
    String dp,name;
    PdfPTable tab = new PdfPTable(4);
    Label label;
    WritableSheet wsheet;
    String choice;
    String USER_PASS ;
    String OWNER_PASS = "j2sdk";
    public TBL2PDF_CSV11() {
        initComponents();
   
        
    }
    public void createCSV(){
         try{ 
       
        WritableWorkbook wworkbook = Workbook.createWorkbook(new File(dp+ ".xls"));
           
        //Sheet name
        wsheet = wworkbook.createSheet("First Sheet", 0);
  
        //******** SET HEADER FOR CSV ***************
        int row1=0,col1=0;
        label =new Label(row1++, col1, "LASTNAME");
        wsheet.addCell(label);
        label = new Label(row1++, col1,"LASTNAME");
        wsheet.addCell(label);
        label = new Label(row1++, col1,"MIDDLENAME");
        wsheet.addCell(label);
        label = new Label(row1++, col1,"COMPANY");
        wsheet.addCell(label);
        //**** GET THE VALUE from TABLE ***********
        tbl_loop();
        
        wworkbook.write();
        wworkbook.close();
         }catch(Exception ex){
        System.out.println(ex.getMessage());
       }
    }
   public void passwordCSV(){
       USER_PASS = new String(security.getPassword()); 
         
       try{
        FileInputStream fileInput = new FileInputStream(dp+".xls");
        BufferedInputStream bufferInput = new BufferedInputStream(fileInput);
        POIFSFileSystem poiFileSystem = new POIFSFileSystem(bufferInput);
        Biff8EncryptionKey.setCurrentUserPassword(USER_PASS);
        HSSFWorkbook workbook = new HSSFWorkbook(poiFileSystem, true);
        FileOutputStream fileOut = new FileOutputStream(dp+".xls");
        workbook.writeProtectWorkbook(Biff8EncryptionKey.getCurrentUserPassword(), "");
        workbook.write(fileOut);
        dp="";
       }catch(Exception ex)
       {
           System.out.println(ex.getMessage());
       }
   }
    public void createPDF(){
     USER_PASS = new String(security.getPassword());   
     try{
         //********** SET PDF **************
        com.itextpdf.text.Document document=new com.itextpdf.text.Document();
        OutputStream file = new FileOutputStream(new File(dp+".pdf"));
        PdfWriter writer = PdfWriter.getInstance(document, file);
        writer.setEncryption(USER_PASS.getBytes(), OWNER_PASS.getBytes(),
        PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128);
        document.open();
        
        //******** SET HEADER FOR TABLE ***************
        tab.addCell("LASTNAME");
        tab.addCell("FIRSTNAME");
        tab.addCell("MIDDLENAME");
        tab.addCell("COMPANY");
       
        
        //**** GET THE VALUE from TABLE ***********
        tbl_loop();
        
        //***** ADD TO TABLE ROW AND COLUMN *********
        document.add(tab);
        document.close();
        
       }
       catch(Exception e){
        System.out.println(e.getMessage());
       }
        
        
    }
    
    
    
    // ************ SELECT PATH *******************
    public void select_path(){
        JFrame parentFrame = new JFrame();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a File to Save");   
        int userSelection = fileChooser.showSaveDialog(parentFrame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            dp = String.valueOf(fileToSave.getAbsolutePath());
        }
    }
    
    //********* TABLE LOOPING ******************
    public void tbl_loop(){
        int row = tbl_customers.getRowCount();
        int column = tbl_customers.getColumnCount();
        for (int j = 0; j  < row; j++) {
            for (int i = 0; i  < column; i++){
                name = String.valueOf(tbl_customers.getValueAt(j, i));
                
                // *** FOR PDF ***
                tab.addCell(name);
                
                //*** FOR CSV ****
                try{
                    label = new Label(i, j+1, name); 
                    wsheet.addCell(label);
                }catch(Exception ex){
                }
            }     
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

        SetPassword = new javax.swing.JFrame();
        jLabel1 = new javax.swing.JLabel();
        security = new javax.swing.JPasswordField();
        setPass = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_customers = new javax.swing.JTable();
        csv = new javax.swing.JButton();
        pdf = new javax.swing.JButton();

        SetPassword.setMinimumSize(new java.awt.Dimension(358, 245));

        jLabel1.setText("Set Password:");

        setPass.setText("OK");
        setPass.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                setPassMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout SetPasswordLayout = new javax.swing.GroupLayout(SetPassword.getContentPane());
        SetPassword.getContentPane().setLayout(SetPasswordLayout);
        SetPasswordLayout.setHorizontalGroup(
            SetPasswordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SetPasswordLayout.createSequentialGroup()
                .addGroup(SetPasswordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(SetPasswordLayout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(security, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(SetPasswordLayout.createSequentialGroup()
                        .addGap(113, 113, 113)
                        .addComponent(setPass, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        SetPasswordLayout.setVerticalGroup(
            SetPasswordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SetPasswordLayout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addGroup(SetPasswordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(security, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addComponent(setPass, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tbl_customers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"", "JOSHUA", "T", "CIGNAL"},
                {"RIVERA", "JHECK", "D", "HD CIGNAL"},
                {"DIONELA", "BEATRICE", " ", " "},
                {"NONAME", " ", "R", " "}
            },
            new String [] {
                "LASTNAME", "FIRSTNAME", "MIDDLENAME", "COMPANY"
            }
        ));
        jScrollPane1.setViewportView(tbl_customers);

        csv.setText("EXPORT CSV");
        csv.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                csvMouseClicked(evt);
            }
        });

        pdf.setText("EXPORT PDF");
        pdf.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pdfMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(56, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(pdf, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(52, 52, 52)
                        .addComponent(csv, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(95, 95, 95))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(csv, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pdf, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(131, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void pdfMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pdfMouseClicked
        // TODO add your handling code here:
        if (JOptionPane.showConfirmDialog(null, "Set Password", "Notification",
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                    this.setVisible(false);
                    choice = "PDF";
                    SetPassword.setVisible(true);
                    SetPassword.setLocationRelativeTo(null);
        }else{
        
        select_path();
        createPDF();
        }
        
    }//GEN-LAST:event_pdfMouseClicked

    private void csvMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_csvMouseClicked
        // TODO add your handling code here:
        if (JOptionPane.showConfirmDialog(null, "Set Password", "Notification",
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                    this.setVisible(false);
                    choice = "CSV";
                    SetPassword.setVisible(true);
                    SetPassword.setLocationRelativeTo(null);
        }else{
        
        select_path();
        createCSV();
       
      
       
         }
    }//GEN-LAST:event_csvMouseClicked

    private void setPassMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_setPassMouseClicked
        // TODO add your handling code here:
       select_path();
      if(choice.equals("PDF")){
       createPDF();
       }
      if(choice.equals("CSV")){
            createCSV();
            passwordCSV();
       }
      
        SetPassword.dispose();
        this.setVisible(true);
        
       
    }//GEN-LAST:event_setPassMouseClicked

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
            java.util.logging.Logger.getLogger(TBL2PDF_CSV11.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TBL2PDF_CSV11.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TBL2PDF_CSV11.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TBL2PDF_CSV11.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TBL2PDF_CSV11().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFrame SetPassword;
    private javax.swing.JButton csv;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton pdf;
    private javax.swing.JPasswordField security;
    private javax.swing.JButton setPass;
    private javax.swing.JTable tbl_customers;
    // End of variables declaration//GEN-END:variables
}
