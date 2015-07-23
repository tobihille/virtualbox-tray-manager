
package WorkerJob;

import java.util.Iterator;
import java.util.List;
import javax.swing.JOptionPane;
import org.mozilla.xpcom.Mozilla;
import org.virtualbox_5_0.IMachine;
import org.virtualbox_5_0.IVirtualBox;
import org.virtualbox_5_0.VirtualBoxManager;

/**
 *
 * @author neo
 */
public class Worker {
  
  public static void main(String[] args) {
    if (args.length < 1)
    {
      System.out.println("No argument given, quitting... Allowed arguments: {list|start|stop|backup}");
      System.exit(0);
    }
    
    if ( args[0].equals("list") )
    {
      Worker w = new Worker();
      System.out.println( w.list() );
    }

    System.out.println("end");
  }
  
  private String list()
  {
    String out = "";
    System.out.println(1);
    VirtualBoxManager mgr = VirtualBoxManager.createInstance(null);
    IVirtualBox vbox = mgr.getVBox();
    System.out.println("VirtualBox version: " + vbox.getVersion() + "\n");

    /*
    Mozilla m = Mozilla.getInstance();
    System.out.println(2);
    VirtualBoxManager vboxManager = VirtualBoxManager.createInstance(null);
    System.out.println(3);
    IVirtualBox vbox = vboxManager.getVBox();
    System.out.println(4);
    List<IMachine> boxes = vbox.getMachines();
    System.out.println(5);
    
    System.out.println( boxes.size() );

    Iterator<IMachine> it = boxes.iterator();
    while ( it.hasNext() )
    {
      IMachine machine = it.next();
      out += "\n" + "\"" + machine.getName() + "\" " + "{" + machine.getId() + "}" );
    }
    */
    return out;
  }
  
}
