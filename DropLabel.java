/**
 *
 * $Id: DropLabel.java,v 1.3 2005/08/15 13:45:44 nicks Exp nicks $
 * 
 * Extension of JLabel that adds drop target capability. 
 * The original intent was to use these in the AddPerformance
 * widget, dragging performers from a table (See SingerTableModel)
 * to a DropLabel to perform the casting for a given role.
 * 
 * Originally part of the AMATO application.
 * 
 * @author Nick Seidenman <nick@seidenman.net>
 * @version $Revision: 1.3 $
 *
 * $Log: DropLabel.java,v $
 * Revision 1.3  2005/08/15 13:45:44  nicks
 * Added getObject.
 *
 * Revision 1.2  2005/08/11 22:16:22  nicks
 * Commented out debug println cruft.
 *
 * Revision 1.1  2005/08/11 22:12:20  nicks
 * Initial revision
 *
 *
 */ 

import java.util.*;
import java.io.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.border.*;


public class DropLabel
    extends JLabel
    implements DropTargetListener
{
    private static final long serialVersionUID = 20050810000010L;

    private Object ob;

    public DropLabel (String text)
    {
	super (text);

	new DropTarget (this, DnDConstants.ACTION_COPY, this);
    }
    
    public DropLabel (String text, int hAlign)
    {
	super (text, hAlign);

	new DropTarget (this, DnDConstants.ACTION_COPY, this);
    }
    
    public Object getObject () { return ob; }

    public void drop (DropTargetDropEvent e) 
    {
	Transferable stt = e.getTransferable ();

	try {
	    e.acceptDrop (DnDConstants.ACTION_COPY);
	    ob = (Object) stt.getTransferData (new SingerDataFlavor());
	    this.setText (ob.toString());
	    //System.out.println ("... and dropped on " + this.getName());
	    e.dropComplete (true);
	}
	catch (Throwable t) {
	    t.printStackTrace ();
	}
    }
    

    public void dragEnter (DropTargetDragEvent e) {}
    public void dragExit (DropTargetEvent e) {}
    public void dragOver (DropTargetDragEvent e) {}
    public void dropActionChanged (DropTargetDragEvent e) {}
}
