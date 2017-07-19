/**
 * $Id: FileExtensionFilter.java,v 1.3 2005/08/17 20:35:23 nicks Exp $
 *
 * FileFilter objects.
 * 
 * Originally part of the AMATO application.
 * 
 * @uthor Nick Seidenman <nick@seidenman.net>
 * @version $Revision: 1.3 $
 *
 * $Log: FileExtensionFilter.java,v $
 * Revision 1.3  2005/08/17 20:35:23  nicks
 * Converted comment header to comply with javadoc conventions.
 *
 * Revision 1.2  2005/07/22 22:12:39  nicks
 * Started work on Drag-n-Drop within RosterPanel.
 * Cleaned up nearly all warnings.  Only a few left in Opera.java, Operas.java,
 * and Performance.java and these pertain to strict type-checking ala 1.5.
 * Just wanted to check everything in now as a check point.  I'll continue
 * to work on getting a totally clean compilation.
 *
 * Revision 1.1  2005/06/23 19:42:48  nicks
 * Initial revision
 *
 */

import java.io.File;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.*;

public class FileExtensionFilter extends FileFilter
{
    private HashMap<String,String> exts;

    public FileExtensionFilter () 
    { 
	super (); 

	exts = new HashMap<String,String> ();
    }

    public FileExtensionFilter (String fs) 
    {
	super ();
	
	exts = new HashMap<String,String> ();
	exts.put (fs.toLowerCase(), fs.toLowerCase());
    }

    public void add (String fs) 
    { 
	exts.put (fs.toLowerCase(), fs.toLowerCase());
    }
    
    final public boolean accept (File f) 
    {
	if (f.isDirectory()) return true;
	
	String ext = null;
	String s = f.getName ();
	int i = s.lastIndexOf ('.');
	
	if (i > 0 && i < s.length() - 1) {
	    ext = s.substring (i+1).toLowerCase();
	    if (exts.get (ext) == null)
		return false;
	    else
		return true;
	}
	
	return false;
    }
    
    final public String getDescription ()
    {
	return "Filter file names that end with " + exts;
    }
}
