package virtualboxtraymanager;

import com.sun.nio.zipfs.ZipPath;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

public class VirtualBoxTrayManager {

  public static VirtualBoxTrayManager appInstance = null;
  
  public static final java.io.File propertiesFile = new java.io.File(System.getProperty("user.home") + "/virtual-box-manager.properties");
  
  public static SystemTray tray = null;
  public static HashMap<String, String> vms = new HashMap();
  public static JPopupMenu popup = null;
  
  public static Properties settings = new Properties();
  
  public CliTask xpcomInstance = null;
  
  /**
   * @param args the command line arguments
   * @throws java.lang.InterruptedException
   */
  public static void main(String[] args) throws InterruptedException 
  {
    VirtualBoxTrayManager tm =  new VirtualBoxTrayManager();
    appInstance = tm;
    appInstance.readConfig();

    appInstance.startXpcom();
    Thread.sleep(1500); //give some space to actually start service
    appInstance.readVMs();
    
    if ( SystemTray.isSupported() )
    {
      appInstance.initTray();
    }
    else
    {
      errorBox("Cannot create a tray-entry, will run in Window-Mode", "Startup-Error");
      SchedulerDialog sched = new SchedulerDialog(null, false);
      sched.setVisible(true);
    }
    
    while (true) //let application not end
    {
      try
      {
        Thread.sleep(25);
      }
      catch (InterruptedException e)
      {
        //don't care
      }
    }
  }
  
  public static void infoBox(String infoMessage, String titleBar)
  {
    JOptionPane.showMessageDialog(null, infoMessage, titleBar, JOptionPane.INFORMATION_MESSAGE);
  }
  
  public static void errorBox(String infoMessage, String titleBar)
  {
    JOptionPane.showMessageDialog(null, infoMessage, titleBar, JOptionPane.ERROR_MESSAGE);
  }
  
  public static List<String> startCommand(String VMName)
  {
    List<String> params = java.util.Arrays.asList("/usr/bin/VBoxManage", "startvm",  "\"" + VMName + "\"", "gui");
    return params;
    //return "VBoxManage startvm \"" + VMName + "\" gui";
  }
  
  public static List<String> shutdownCommand(String VMName)
  {
    List<String> params = java.util.Arrays.asList("VBoxManage", "controlvm", "\"" + VMName + "\"", "acpipowerbutton");
    return params;
    //return "VBoxManage controlvm \"" + VMName + "\" acpipowerbutton";
  }
  
  public static List<String> backupCommand(String VMName, String backupDir)
  {
    List<String> params = java.util.Arrays.asList("VBoxManage", "export", "\"" + VMName + "\"", "-o", backupDir);
    return params; 
    //return "VBoxManage export \"" + VMName + "\" -o "+ backupDir;
  }
  
  private void startXpcom()
  {
    if (settings.getProperty("xpcomfile") == null || settings.getProperty("xpcomfile").equals("") )
    {
      infoBox("Please provide executable for xpcomserver", "Configuration needed");
      SettingsDialog sd = new SettingsDialog(null, true);
      sd.settings = settings;
      sd.setVisible(true);
      settings = sd.settings;
      writeConfig();
      sd.dispose();
    }
    
    if (settings.getProperty("librarypath") == null || settings.getProperty("librarypath").equals("") )
    {
      infoBox("Please provide library path", "Configuration needed");
      SettingsDialog sd = new SettingsDialog(null, true);
      sd.settings = settings;
      sd.setVisible(true);
      settings = sd.settings;
      writeConfig();
      sd.dispose();
    }
    
    List<String> params = java.util.Arrays.asList(settings.getProperty("xpcomfile"));
    xpcomInstance = new CliTask(params, "Error on listing VMs");
    xpcomInstance.waitFor = false;
    xpcomInstance.start();
  }
  
  private void readVMs()
  {
    //Here inside this thread because we need it JUST NOW and LOCKING
    try
    {
      XpcomTask vmList = new XpcomTask("list", "Error listing VMs", settings);
      vmList.start();
      
      while ( vmList.isAlive() )
      {
        Thread.sleep(25);
      }
      
      ArrayList<String> output = vmList.output;
      Iterator<String> it = output.iterator();
      
      while ( it.hasNext() ) 
      {
        String line = it.next();
				int l = line.length();
        int nameEnd = l-39; //UUID of VM is { + 36 chars + } and preceeded by 1 space > equals 39
        String vmName = line.substring(0, nameEnd + 1);
        String vmUuid = line.substring(nameEnd);
        vms.put( vmName.replace("\"", "").trim(), vmUuid.trim() );
			}
    }
    catch (InterruptedException ex) 
    {
      //does in reality not happen
    }
  }
  
  private void initTray()
  {
    try
    {
      //get current code-path
      CodeSource src = VirtualBoxTrayManager.class.getProtectionDomain().getCodeSource();
      URL jar = src.getLocation();
      Path trayIconFilePath;
      java.io.File trayIconFile;
      Image trayIconImage;
      
      if (jar.toString().endsWith(".jar"))
      {
        trayIconImage = ImageIO.read( this.getClass().getResourceAsStream("/gnome-mdi.png") );        
      }
      else
      {
        //debug-process -> read file from filesystem
        trayIconFilePath = new java.io.File(jar.toString().replace("file:", "") + "gnome-mdi.png").toPath();
        trayIconFile = trayIconFilePath.toFile();
        trayIconImage = ImageIO.read( trayIconFile );
      }
      //access tray and add icon
      tray = SystemTray.getSystemTray();
      TrayIcon ti = new TrayIcon(trayIconImage);
      ti.setImageAutoSize(true);
      ti.setToolTip("VirtualBox Tray Manager");

      popup = new JPopupMenu();

      ti.addMouseListener(new MouseAdapter() //add the more fancy/less ugly swing-popup-menu -> see http://bugs.java.com/bugdatabase/view_bug.do?bug_id=6285881
        {
          @Override
          public void mouseReleased(MouseEvent e)
          {
            if ( e.isPopupTrigger() )
            {
              popup.setLocation(e.getX(), e.getY());
              popup.setInvoker(popup);
              popup.setVisible(true);
            }
          }

          @Override
          public void mousePressed(MouseEvent e)
          {
            if ( e.isPopupTrigger() )
            {
              popup.setLocation(e.getX(), e.getY());
              popup.setInvoker(popup);
              popup.setVisible(true);
            }
          }
        }
      );

      Iterator<String> it = vms.keySet().iterator();
      while (it.hasNext())
      {
        String name = it.next();
        String uuid = vms.get(name);

        javax.swing.JMenu itemActionContainer = new javax.swing.JMenu(name);

        JMenuItem start = new JMenuItem("Start");
        start.addActionListener(new ActionListener() 
          {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
              String name = ((javax.swing.JMenu) ((JPopupMenu) ( (JMenuItem) e.getSource() ).getParent()).getInvoker()).getText();
              XpcomTask vmStart = new XpcomTask("start", "Error starting VM " + name, settings);
              vmStart.identifier = name;
              vmStart.start();
              infoBox("VM Started, please note that all VMs are started in headless mode.\nIf you want to acces them you need to open the VirtualBox GUI and make them visible.", "VM Started, Information");
            }
          }
        );
        
        JMenuItem shutdown = new JMenuItem("Shutdown");
        shutdown.addActionListener(new ActionListener() 
          {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
              String name = ((javax.swing.JMenu) ((JPopupMenu) ( (JMenuItem) e.getSource() ).getParent()).getInvoker()).getText();
              XpcomTask vmEnd = new XpcomTask("stop", "Error  on shutdown of VM " + name, settings);
              vmEnd.identifier = name;
              vmEnd.start();
            }
          }
        );

        JMenuItem backup = new JMenuItem("Backup");
        backup.addActionListener(new ActionListener() 
          {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
              String name = ((javax.swing.JMenu) ((JPopupMenu) ( (JMenuItem) e.getSource() ).getParent()).getInvoker()).getText();
              String dir = settings.getProperty("backupdir");
              if ( dir == null || dir.isEmpty()  )
              {
                errorBox("Error on backup of VM " + name + " - you need to set the backup-directory first", "Backup error");
              }

              XpcomTask vmBackup = new XpcomTask("backup", "Error  on shutdown of VM " + name, settings);
              vmBackup.identifier = name;
              vmBackup.start();
              infoBox("Backup successfully started, running ...", "Backup info");
            }
          }
        );
        
        itemActionContainer.add(start);
        itemActionContainer.add(shutdown);
        itemActionContainer.add(backup);
        
        popup.add(itemActionContainer);
      }

      JMenuItem schedulerItem = new JMenuItem("Scheduler");
      schedulerItem.addActionListener(new ActionListener() 
        {
          @Override
          public void actionPerformed(ActionEvent e) 
          {
            SchedulerDialog s = new SchedulerDialog(null, true);
            s.settings = settings;
            s.setVisible(true);
            settings = s.settings;
            appInstance.writeConfig();
            s.dispose();
          }
        }
      );
      popup.add( schedulerItem );
      
      JMenuItem settingsItem = new JMenuItem("Settings");
      settingsItem.addActionListener(new ActionListener() 
        {
          @Override
          public void actionPerformed(ActionEvent e) 
          {
            SettingsDialog s = new SettingsDialog(null, true);
            s.settings = settings;
            s.setVisible(true);
            settings = s.settings;
            appInstance.writeConfig();
            s.dispose();
          }
        }
      );
      popup.add( settingsItem );

      JMenuItem spacer = new JMenuItem("________________");
      spacer.setEnabled(false);
      popup.add(spacer);
      
      JMenuItem closeItem = new JMenuItem("Close");
      closeItem.addActionListener(new ActionListener() 
        {
          @Override
          public void actionPerformed(ActionEvent e) 
          {
            try {
              xpcomInstance.close();
              xpcomInstance.join(500);
              appInstance.writeConfig();
              System.exit(0);
            } 
            catch (InterruptedException ex) 
            {
              //another InterruptedException
            }
          }
        }
      );
      popup.add( closeItem );

      tray.add(ti);
    }      
    catch (/* URISyntaxException | */ IOException | AWTException e)
    {
      System.out.println(e.getMessage());
      System.out.println("Class: " + e.getClass().getCanonicalName());
    }
  }
  
  private void readConfig()
  {
    try 
    {
      if ( !propertiesFile.exists() )
      {
        propertiesFile.createNewFile();
      }

      InputStreamReader is = new FileReader(propertiesFile);
      settings.load(is);
    }
    catch (FileNotFoundException ex) 
    {
      Logger.getLogger(VirtualBoxTrayManager.class.getName()).log(Level.SEVERE, null, ex);
    }
    catch (IOException ex) 
    {
      Logger.getLogger(VirtualBoxTrayManager.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
  public void writeConfig()
  {
    try
    {
      FileOutputStream fos = new FileOutputStream(propertiesFile);
      settings.store(fos, "Settings of Virtual Box Tray Manager. If you delete this file, all schedules and settings will be gone.");
    }
    catch (FileNotFoundException ex) 
    {
      Logger.getLogger(VirtualBoxTrayManager.class.getName()).log(Level.SEVERE, null, ex);
    } 
    catch (IOException ex) 
    {
      Logger.getLogger(VirtualBoxTrayManager.class.getName()).log(Level.SEVERE, null, ex);
    } 
  }
  
}
