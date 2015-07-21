package virtualboxtraymanager;

import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.security.CodeSource;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

/**
 *
 * @author neo
 */
public class VirtualBoxTrayManager {

  public static SystemTray tray = null;
  public static HashMap<String, String> vms = new HashMap();
  public static JPopupMenu popup = null;
  
  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    
    try
    {
      Process vmList = Runtime.getRuntime().exec("VBoxManage list vms");
      BufferedReader reader = new BufferedReader(new InputStreamReader(vmList.getInputStream()));
      String line;			
			while ( (line = reader.readLine()) != null ) 
      {
				int l = line.length();
        int nameEnd = l-39; //UUID of VM is { + 36 chars + } and preceeded by 1 space > equals 39
        String vmName = line.substring(0, nameEnd + 1);
        String vmUuid = line.substring(nameEnd);
        vms.put( vmUuid.trim(), vmName.replace("\"", "") );
			}
    }
    catch (IOException e)
    {
      errorBox("Error on listing VMs: " + e.getMessage(), "Startup-Error");
    }
    
    try
    {
      if ( SystemTray.isSupported() )
      {
        //get current code-path
        CodeSource src = VirtualBoxTrayManager.class.getProtectionDomain().getCodeSource();
        URL jar = src.getLocation();
        Path trayIconFile;
        
        if (jar.toString().endsWith(".jar"))
        {
          //access jar and read tray-icon
          FileSystem zipfs = FileSystems.newFileSystem(jar.toURI(), null, null);
          trayIconFile = zipfs.getPath("/resources/gnome-mdi.png"); 
        }
        else
        {
          //debug-process -> read file from filesystem
          trayIconFile = new java.io.File(jar.toString().replace("file:", "") + "gnome-mdi.png").toPath();
        }
        //access tray and add icon
        tray = SystemTray.getSystemTray();
        java.io.File f = trayIconFile.toFile();
        Image trayIconImage = ImageIO.read( f );
        TrayIcon ti = new TrayIcon(trayIconImage);
        ti.setImageAutoSize(true);
        ti.setToolTip("VirtualBox Tray Manager");
        
        popup = new JPopupMenu();

        JMenuItem closeItem = new JMenuItem("Close");
        closeItem.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            System.exit(0);
          }
        });
        
        popup.add( closeItem );
        
        //ti.setPopupMenu(popup);
        ti.addMouseListener(new MouseAdapter()
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
        });

        
        tray.add(ti);
      }
    }      
    catch (Exception e)
    {
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
    
    while (true)
    {
      try
      {
        Thread.sleep(25);
      }
      catch (Exception e)
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
}
