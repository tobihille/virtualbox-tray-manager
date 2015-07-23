package virtualboxtraymanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static virtualboxtraymanager.VirtualBoxTrayManager.errorBox;

public class CliTask extends Thread
{

  private List<String> cmd = null;
  private String errorMessage = null;
  public boolean waitFor = true;
  private Process runningInstance = null;
  
  ArrayList<String> output = new ArrayList();
  
  public CliTask(List<String> cmd, String errorMessage)
  {
    if (cmd == null || cmd.isEmpty())
    {
      throw new IllegalArgumentException("cmd cannnot be emtpy");
    }
    
    this.cmd = cmd;
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
      //pb.redirectOutput(ProcessBuilder.Redirect.PIPE);
      runningInstance = pb.start();
      
      if (waitFor)
      {
        runningInstance.waitFor();
      }
      
      BufferedReader reader = new BufferedReader(new InputStreamReader(runningInstance.getInputStream()));
      String line;			
      
			while ( (line = reader.readLine()) != null ) 
      {
        System.out.println(line);
				output.add(line);
			}
    }
    catch (IOException ex)
    {
      errorBox(errorMessage + ": " + ex.getMessage(), "VM start error");
    } 
    catch (InterruptedException ex) 
    {
      //does not effectively happen
    }
  }
  
  public void close()
  {
    runningInstance.destroy();
  }
  
}
