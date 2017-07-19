/**
 * $Id: FilePicker.java,v 1.3 2005/08/17 20:35:23 nicks Exp $
 *
 * Instantiate a JFileChooser object and obtain the user's
 * file choice.  Since we're using this to either open a single
 * file, save to a single file, or create a new, single file, there's
 * no need to allow multiple file selection.
 * 
 * Originally part of the AMATO application.
 * 
 * @author Nick Seidenman <nick@seidenman.net>
 * @version $Revision: 1.3 $
 *
 * $Log: FilePicker.java,v $
 * Revision 1.3  2005/08/17 20:35:23  nicks
 * Converted comment header to comply with javadoc conventions.
 *
 * Revision 1.2  2005/06/23 19:43:28  nicks
 * File operations (other than Print) now working.
 *
 * Revision 1.1  2005/06/23 16:47:07  nicks
 * Initial revision
 *
 */

import java.io.File;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.*;

public class FilePicker
{
    File file;

    public FilePicker (String label)
    {
	JFileChooser fc = new JFileChooser ();
	File cwd = new File (".");

	fc.setFileSelectionMode (JFileChooser.DIRECTORIES_ONLY);
	fc.setFileSelectionMode (JFileChooser.FILES_AND_DIRECTORIES);

	FileExtensionFilter fcfilter = new FileExtensionFilter ("xml");
	fcfilter.add ("adb");
	fc.setFileFilter (fcfilter);
	fc.setCurrentDirectory (cwd);

	int returnVal = fc.showDialog (fc, label);
	
	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    file = fc.getSelectedFile ();
	}
	else {
	    file = null;
	}
    }

    final public String getSelectedFilePath () 
    {
	if (file == null) return null;

	try {
	    return file.getCanonicalPath();
	}
	catch (Throwable e) {
	    return null;
	}
    }
}
