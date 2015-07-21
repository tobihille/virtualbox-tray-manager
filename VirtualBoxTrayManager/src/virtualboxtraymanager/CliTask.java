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

/**
 *
 * @author neo
 */
public class CliTask extends Thread
{

  private List<String> cmd = null;
  private String uuid = null;
  private String name = null;
  private String errorMessage = null;
  
  ArrayList<String> output = new ArrayList();
  
  public CliTask(List<String> cmd, String uuid, String name, String errorMessage)
  {
    if (cmd == null || cmd.isEmpty())
    {
      throw new IllegalArgumentException("cmd cannnot be emtpy");
    }
    
    if (uuid == null || uuid.isEmpty())
    {
      throw new IllegalArgumentException("uuid cannnot be emtpy");
    }
    
    if (name == null || name.isEmpty())
    {
      throw new IllegalArgumentException("name cannnot be emtpy");
    }
    
    this.cmd = cmd;
    this.uuid = uuid;
    this.name = name;
    this.errorMessage = errorMessage;
  }
  
  @Override
  public void run() {
    super.run();
    
    output = new ArrayList();
    try
    {
      System.out.println(cmd);
      
      ProcessBuilder pb = new ProcessBuilder(cmd);
      pb.redirectOutput(ProcessBuilder.Redirect.PIPE);
      Process process = pb.start();
      /*
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String line;			
      
			while ( (line = reader.readLine()) != null ) 
      {
        System.out.println(line);
				output.add(line);
			}
              */
    }
    catch (IOException ex)
    {
      errorBox(errorMessage + ": " + ex.getMessage(), "VM start error");
    }
  }
  
}
