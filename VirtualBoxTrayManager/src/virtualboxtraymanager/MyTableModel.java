package virtualboxtraymanager;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author neo
 */
public class MyTableModel extends DefaultTableModel
{
  @Override
  public Class getColumnClass(int columnIndex)
  {
    if (columnIndex == 5) //Actions
      return VmActions.class;
    
    if (columnIndex == 6) //VMs
      return VmDropDown.class;
    
    return Integer.class;
  }
}