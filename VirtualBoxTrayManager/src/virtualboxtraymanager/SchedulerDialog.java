package virtualboxtraymanager;

import java.util.Iterator;
import java.util.Properties;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;

public class SchedulerDialog extends javax.swing.JDialog {

  public Properties settings = null;
  
  private JTable cronContent = null;
  public static MyTableModel mtm = null;
  public boolean standaloneMode = false;
  private boolean loadError = false;
  
  /**
   * Creates new form SchedulerDialog
   */
  public SchedulerDialog(java.awt.Frame parent, boolean modal) {
    super(parent, modal);
    initComponents();
  }

  /**
   * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
   * content of this method is always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jToolBar1 = new javax.swing.JToolBar();
    jButton1 = new javax.swing.JButton();
    jButton2 = new javax.swing.JButton();
    filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
    jButton3 = new javax.swing.JButton();
    jPanel1 = new javax.swing.JPanel();
    jScrollPane1 = new javax.swing.JScrollPane();

    setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
    setTitle("Cron settings");
    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent evt) {
        formWindowClosing(evt);
      }
      public void windowOpened(java.awt.event.WindowEvent evt) {
        formWindowOpened(evt);
      }
    });

    jToolBar1.setRollover(true);

    jButton1.setText("+");
    jButton1.setFocusable(false);
    jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton1ActionPerformed(evt);
      }
    });
    jToolBar1.add(jButton1);

    jButton2.setText("-");
    jButton2.setFocusable(false);
    jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jButton2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton2ActionPerformed(evt);
      }
    });
    jToolBar1.add(jButton2);
    jToolBar1.add(filler1);

    jButton3.setText("OK");
    jButton3.setFocusable(false);
    jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    jButton3.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton3ActionPerformed(evt);
      }
    });
    jToolBar1.add(jButton3);

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jScrollPane1)
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 608, Short.MAX_VALUE)
      .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
    int selRow = cronContent.getSelectedRow();
    if (selRow > -1)
    {
      mtm.removeRow(selRow);
    }
  }//GEN-LAST:event_jButton2ActionPerformed

  private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    mtm.addRow( new String[7] );
  }//GEN-LAST:event_jButton1ActionPerformed

  private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
    cronContent = new JTable();
    jScrollPane1.add(cronContent);
    
    mtm = new MyTableModel();
    cronContent.setModel(mtm);
    cronContent.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    jScrollPane1.setViewportView(cronContent);
    
    mtm.addColumn("minute");
    mtm.addColumn("hour");
    mtm.addColumn("day");
    mtm.addColumn("month");
    mtm.addColumn("year");
    mtm.addColumn("action");
    mtm.addColumn("VM");
    
    cronContent.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(new VmActions()));
    cronContent.getColumnModel().getColumn(6).setCellEditor(new DefaultCellEditor(new VmDropDown()));
    
    cronContent.getColumnModel().getColumn(5).setWidth(150);
    cronContent.getColumnModel().getColumn(6).setWidth(300);
    
    populateGrid();
    
    if (loadError)
    {
      VirtualBoxTrayManager.errorBox("Error on loading tasks, some elements could not be loaded.\n"
        + "Please correct the errors in " + VirtualBoxTrayManager.propertiesFile.getAbsolutePath() + " and restart the application.\n"
        + "Entries starting with \"cron\" need to be checked.", "Load error");
    }
  }//GEN-LAST:event_formWindowOpened

  private void populateGrid()
  {
    java.util.Set<Object> settingSet = VirtualBoxTrayManager.settings.keySet();
    Iterator<Object> it = settingSet.iterator();
    
    while (it.hasNext())
    {
      String key = (String) it.next();
      if ( key.startsWith("cron") )
      {
        String[] expression = VirtualBoxTrayManager.settings.getProperty(key).split(" ");
        
        if (expression.length != 7)
        {
          loadError = true;
          break;
        }
        
        Object[] content = new Object[7];
        
        for (int i = 0; i < content.length; i++)
        {
          if ( mtm.getColumnClass(i).equals(Integer.class) )
          {
            try
            {
              content[i] = Integer.valueOf(expression[i]);
            }
            catch (NumberFormatException nfe)
            {
              loadError = true;
              return;
            }
          }
          else
          {
            content[i] = expression[i];
          }
        }
        
        mtm.addRow(content);
      }
    }
  }
  
  private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
    formWindowClosing(null);
  }//GEN-LAST:event_jButton3ActionPerformed

  private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
    if (loadError) //do not save > avoid corrupted crons
    {
      setVisible(false);
      return;
    }
    
    int row = 0, col;
    boolean saveError = false;
    
    //first delete all old settings
    while (row < mtm.getRowCount())
    {
      VirtualBoxTrayManager.settings.remove("cron" + row);
      row++;
    }
    
    row = 0;
    
    //then add new settings
    while (row < mtm.getRowCount())
    {
      col = 0;
      String rowString = "";
      
      while (col < mtm.getColumnCount())
      {
        String value;
        if ( mtm.getColumnClass(col).equals(Integer.class) )
        {
          if (mtm.getValueAt(row, col) == null)
          {
            value = "*";
          }
          else
          {
            value = ( (Integer) mtm.getValueAt(row, col)).toString();
          }
        }
        else
        {
          value = (String) mtm.getValueAt(row, col);
        }
        
        if (value == null)
        {
          VirtualBoxTrayManager.errorBox("Empty value not allowed here.", "Invalid value provided");
          cronContent.changeSelection(row, col, false, false);
          saveError = true;
          return;
        }
        
        rowString += value + " ";
        
        col++;
      }
      
      VirtualBoxTrayManager.settings.setProperty("cron" + row, rowString.trim());
      row++;
    }
    
    if (!saveError)
    {
      VirtualBoxTrayManager.writeConfig();
      
      if (evt != null)
      {
        setVisible(false);
      }
    }
  }//GEN-LAST:event_formWindowClosing

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.Box.Filler filler1;
  private javax.swing.JButton jButton1;
  private javax.swing.JButton jButton2;
  private javax.swing.JButton jButton3;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JToolBar jToolBar1;
  // End of variables declaration//GEN-END:variables
}
