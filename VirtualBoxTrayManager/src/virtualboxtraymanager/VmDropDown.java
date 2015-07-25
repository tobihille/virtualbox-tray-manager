package virtualboxtraymanager;

import java.util.Iterator;
import java.util.Set;
/**
 *
 * @author neo
 */
public class VmDropDown extends javax.swing.JComboBox {
  
  public VmDropDown()
  {
    Set<String> vmList = VirtualBoxTrayManager.vms.keySet();
    
    Iterator<String> it = vmList.iterator();
    while ( it.hasNext() )
    {
      this.addItem(it.next());
    }
    
    this.setEditable(false);
  }
  
}
