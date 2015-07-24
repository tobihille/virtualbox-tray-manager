package virtualboxtraymanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static virtualboxtraymanager.VirtualBoxTrayManager.errorBox;

public class XpcomTask extends Thread
{

  private List<String> cmd = null;
  private String errorMessage = null;
//  private Properties settings = null;
  
  private final static String PS = System.getProperty("path.separator");
  
  ArrayList<String> output = new ArrayList();
  
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
    /*
    if ( classpath.contains(PS) )
    {
      classpath = classpath.substring(classpath.indexOf(PS) + 1);
    }
    */
    
    this.cmd = java.util.Arrays.asList("java", "-cp", classpath, "-Dvbox-home", settings.getProperty("vboxhome"), "WorkerJob.Worker", cmd);
//    this.settings = settings;
    this.errorMessage = errorMessage;
  }
  
  @Override
  public void run() {
    //super.run();
    
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
