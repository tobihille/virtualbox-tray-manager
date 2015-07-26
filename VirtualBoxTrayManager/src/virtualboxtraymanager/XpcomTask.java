package virtualboxtraymanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Properties;

import static virtualboxtraymanager.VirtualBoxTrayManager.errorBox;

public class XpcomTask extends Thread
{

  private ArrayList<String> cmd = null;
  private String errorMessage = null;
  
  ArrayList<String> output = new ArrayList();
  public String identifier = null;
  public String modifier = null;
  
  public XpcomTask(String cmd, String errorMessage, Properties settings)
  {
    if (cmd == null || cmd.isEmpty())
    {
      throw new IllegalArgumentException("cmd cannnot be emtpy");
    }
    
    if (settings == null || settings.isEmpty())
    {
      throw new IllegalArgumentException("settings cannnot be emtpy");
    }
    
    String classpath = ManagementFactory.getRuntimeMXBean().getClassPath();
    
    this.cmd = new ArrayList();
    this.cmd.add("java");
    this.cmd.add("-cp");
    this.cmd.add(classpath);
    this.cmd.add("-Dvbox.home=" + settings.getProperty("vboxhome"));
    this.cmd.add("WorkerJob.Worker");
    this.cmd.add(cmd);
    
    this.errorMessage = errorMessage;
  }
  
  @Override
  public void run() {
    //super.run();

    if (identifier != null)
    {
      cmd.add(identifier);
    }
    
    if (modifier != null)
    {
      cmd.add(modifier);
    }
    
    output = new ArrayList();
    try
    {
      System.out.println(cmd);
      
      ProcessBuilder pb = new ProcessBuilder(cmd);
      
      Process process = pb.start();
      process.waitFor();
      
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

      String line;			
			while ( (line = reader.readLine()) != null ) 
      {
        System.out.println(line);
				output.add(line);
			}
      
      reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

      while ( (line = reader.readLine()) != null ) 
      {
        System.err.println(line);
				output.add(line);
			}

    }
    catch (IOException ex)
    {
      errorBox(errorMessage + ": " + ex.getMessage(), "XPCOM Error");
    } 
    catch (InterruptedException ex) 
    {
      //does not effectively happen
    }
  }
}
