
package virtualboxtraymanager;

import java.io.File;
import java.io.FilenameFilter;
/**
 *
 * @author neo
 */
public class FileNameFilter implements FilenameFilter
{
  private String extension = null;
  
  public FileNameFilter(String fileExtension)
  {
    if (fileExtension == null || fileExtension.isEmpty())
    {
      throw new IllegalArgumentException("fileExtension cannot be null or empty.");
    }
    extension = fileExtension.replace(".", ""); //knwon and allowed: if you want to load a file without extension use just "."
  }
  
  @Override
  public boolean accept(File dir, String name) 
  {
    if ( name.lastIndexOf('.') > 0 )
    {
      int lastIndex = name.lastIndexOf('.');
      String str = name.substring(lastIndex + 1);
      if(str.equals(extension))
      {
        return true;
      }
    }
    return false;
  }
}
