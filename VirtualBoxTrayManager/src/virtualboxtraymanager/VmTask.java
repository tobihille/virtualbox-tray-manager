package virtualboxtraymanager;

import it.sauronsoftware.cron4j.Task;
import it.sauronsoftware.cron4j.TaskExecutionContext;

public class VmTask extends Task
{
  
  private String action = "";
  private String identifier = "";

  public VmTask(String action, String identifier) 
  {
    this.action = action;
    this.identifier = identifier;
  }
  
  @Override
  public boolean canBePaused() {
    return false;
  }

  @Override
  public boolean canBeStopped() {
    return false;
  }

  @Override
  public void execute(TaskExecutionContext context) throws RuntimeException 
  {
    System.out.println("Running cron task...");
    
    XpcomTask task = new XpcomTask(action, "Cron Startup Error", VirtualBoxTrayManager.settings);
    task.identifier = this.identifier;
    task.start();
  }
  
}
