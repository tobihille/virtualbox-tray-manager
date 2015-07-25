package virtualboxtraymanager;

/**
 *
 * @author neo
 */
public class VmActions extends javax.swing.JComboBox {
  
  public VmActions()
  {
    this.addItem("start");
    this.addItem("stop");
    this.addItem("backup");
    
    this.setEditable(false);
  }
  
}
