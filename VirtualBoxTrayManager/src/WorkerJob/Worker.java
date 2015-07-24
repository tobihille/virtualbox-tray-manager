
package WorkerJob;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.virtualbox_5_0.IAppliance;
import org.virtualbox_5_0.IConsole;
import org.virtualbox_5_0.IMachine;
import org.virtualbox_5_0.IProgress;
import org.virtualbox_5_0.ISession;
import org.virtualbox_5_0.IVirtualBox;
import org.virtualbox_5_0.LockType;
import org.virtualbox_5_0.MachineState;
import org.virtualbox_5_0.SessionState;
import org.virtualbox_5_0.VirtualBoxManager;

/**
 *
 * @author neo
 */
public class Worker {
  
  public static void main(String[] args) throws Exception {
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

    if ( args[0].equals("start") )
    {
      if (args.length < 2)
      {
        System.out.println("VM to launch not given, please provide Name or ID");
        System.exit(0);
      }

      Worker w = new Worker();
      w.launch(args[1]);
    }

    if ( args[0].equals("stop") )
    {
      if (args.length < 2)
      {
        System.out.println("VM to stop not given, please provide Name or ID");
        System.exit(0);
      }

      Worker w = new Worker();
      w.shutdown(args[1]);
    }
    
    if ( args[0].equals("backup") )
    {
      if (args.length < 2)
      {
        System.out.println("VM to backup not given, please provide Name or ID");
        System.exit(0);
      }

      Worker w = new Worker();
      w.backup(args[1]);
    }

  }
  
  private String list()
  {
    String out = "";
    VirtualBoxManager mgr = VirtualBoxManager.createInstance(null);
    IVirtualBox vbox = mgr.getVBox();
    
    List<IMachine> boxes = vbox.getMachines();
    
    Iterator<IMachine> it = boxes.iterator();
    while ( it.hasNext() )
    {
      IMachine machine = it.next();
      out += "\n" + "\"" + machine.getName() + "\" " + "{" + machine.getId() + "}";
    }
    
    return out.trim();
  }

  private void launch(String identifier) throws InterruptedException, Exception
  {
    String status = "";
    
    VirtualBoxManager mgr = VirtualBoxManager.createInstance(null);
    try
    {
      IVirtualBox vbox = mgr.getVBox();

      IMachine machine = vbox.findMachine(identifier);
      ISession session = mgr.getSessionObject();

      try
      {
        if ( !machine.getState().equals(MachineState.Running) )
        {
          IProgress p = machine.launchVMProcess(session, "headless", null);
          p.waitForCompletion(-1);
          if (p.getResultCode() != 0) 
          {
             System.out.println("Machine failed to start: " + p.getErrorInfo().getText());
          }
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      finally
      {
        if ( session.getMachine().getState().equals(MachineState.Running) )
        {
          status = "running";
        }
        else
        {
          status = "not running";
        }
        
        if ( session.getState().equals(SessionState.Locked) )
        {
          session.unlockMachine();
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      mgr.cleanup();
    }
    
    System.out.println("Machine start finished, status: " + status);
  }

  private void shutdown(String identifier)
  {
    String status = "";
    
    VirtualBoxManager mgr = VirtualBoxManager.createInstance(null);
    try
    {
      IVirtualBox vbox = mgr.getVBox();

      IMachine machine = vbox.findMachine(identifier);
      ISession session = mgr.getSessionObject();
      
      try
      {
        if ( machine.getState().equals(MachineState.Running) )
        {
          machine.lockMachine(session, LockType.Shared);
          IConsole console = session.getConsole();

          console.powerButton();
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      finally
      {
        if ( session.getMachine().getState().equals(MachineState.Running) )
        {
          status = "running";
        }
        else
        {
          status = "not running";
        }
        
        if ( session.getState().equals(SessionState.Locked) )
        {
          session.unlockMachine();
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      mgr.cleanup();
    }
    
    System.out.println("Powerbutton pressed, client is handling ...");

  }
  
  private void backup(String identifier) throws Exception
  {
    Properties p = new Properties();
    p.load(new FileInputStream(virtualboxtraymanager.VirtualBoxTrayManager.propertiesFile));
    String exportPath = p.getProperty( "backupdir", System.getProperty("user.home") );
    
    VirtualBoxManager mgr = VirtualBoxManager.createInstance(null);
    
    try
    {
      IVirtualBox vbox = mgr.getVBox();

      IMachine machine = vbox.findMachine(identifier);
      ISession session = mgr.getSessionObject();

      exportPath = exportPath + System.getProperty("directory.separator") + machine.getName() + ".ova";
      
      try
      {
        if ( machine.getState().equals(MachineState.PoweredOff) )
        {
          machine.lockMachine(session, LockType.Shared);
          
          IAppliance ia = vbox.createAppliance();
          machine.exportTo(ia, exportPath);
          IProgress prog = ia.write("ovf-1.0", null, exportPath);
          
          System.out.println("Backup running, saving into " + exportPath);
          
          while ( !prog.getCompleted() && !prog.getCompleted() )
          {
            Thread.sleep(10000);
            System.out.println( prog.getPercent() );
          }
          //prog.waitForCompletion(-1);
          if (prog.getResultCode() != 0) 
          {
             System.out.println("Machine failed to backup: " + prog.getErrorInfo().getText());
          }
          
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      finally
      {
        if ( session.getState().equals(SessionState.Locked) )
        {
          session.unlockMachine();
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      mgr.cleanup();
    }
    System.out.println("Backup complete");
  }
}

//LESSONS
// Use VM Package from Oracle, at least Version 5.0
//
