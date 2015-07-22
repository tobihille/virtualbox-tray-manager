/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
      Process process = pb.start();
      
      process.waitFor();
      
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
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
  
}
