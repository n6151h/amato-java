/**
 *
 * $Id: RoleDropLabel.java,v 1.3 2005/09/12 17:36:44 nicks Exp nicks $
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
 * $Log: RoleDropLabel.java,v $
 * Revision 1.3  2005/09/12 17:36:44  nicks
 * If a singer is being cast in more than one role, a pop-up warning
 * is displayed and the user is asked if this is ok.  If it is, the singer
 * is (n-tuple cast).
 *
 * Revision 1.2  2005/09/07 15:47:08  nicks
 * Now able to edit performances.  Will proceed to add this capability
 * to other Add*Frame.java components.
 *
 * Revision 1.1  2005/08/22 21:55:21  nicks
 * Initial revision
 *
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


public class RoleDropLabel
    extends DropLabel
{
    private static final long serialVersionUID = 20050822000010L;

    private Object ob;
    private Role role;

    private AddPerformanceFrame apf;
    
    public RoleDropLabel (Role role, String text, AddPerformanceFrame apf)
    {
	super (text);

	this.role = role;
	this.apf = apf;

	new DropTarget (this, DnDConstants.ACTION_COPY, this);
    }
    
    public RoleDropLabel (Role role, String text, int hAlign, 
			  AddPerformanceFrame apf)
    {
	super (text, hAlign);

	this.role = role;
	this.apf = apf;

	new DropTarget (this, DnDConstants.ACTION_COPY, this);
    }
    
    public Object getObject () { return ob; }

    public void setObject (Object ob) { 
	this.ob = ob; 
	this.setText (ob.toString());
    }

    public void drop (DropTargetDropEvent e) 
    {
	Transferable stt = e.getTransferable ();

	if (stt == null) return;

	//System.out.println ("stt.class: "  + stt.getClass());

	try {
	    Object tob = (Object) stt.getTransferData (new SingerDataFlavor());
	    //System.out.println ("tob: " + tob);
	    if (tob instanceof Singer) {
		// Pop up a confirmation dialog if the selected singer
		// isn't registered as having a voice type compatible
		// with the role's voice type.  (e.g.  Don't want
		// a baritone being dropped into a soprano role.
		Singer s = (Singer) tob;
		//System.out.println ("singer: " + s);
		String v = role.getVoice ();
		if (! s.hasVoice (v)) {
		    if (Amato.confirmPopup ("This role is for " +
					    v + "s\n" +
					    "but " + s +
					    " doesn't sing in that " +
					    "voice.\n\n  Assign anyway?",
					    "Confirm Role Assignment") != 0) {
			e.dropComplete (false);
			e.rejectDrop ();
			return; // Operation cancelled.
		    }
		}
		
		// Now we check to see if this singer is already cast.
		if (apf.isCast (s)) {
		    if (Amato.confirmPopup (s.toString() + " is already cast " +
					    " in another\nrole in this performance.\n" +
					    "Assign anyway?",
					    "Confirm Role Assignment") != 0) {
			e.dropComplete (false);
			e.rejectDrop ();
			return; // Operation cancelled.
		    }
		}

		// If we got this far then we're casting the 
		// the this role with the singer "dropped" into
		// this slot.
		e.acceptDrop (DnDConstants.ACTION_COPY);
		ob = tob;
		this.setText (tob.toString ());
		e.dropComplete (true);
	    }
	    e.dropComplete (true);
	}
	catch (Throwable t) {
	    System.err.println ("drop: " + stt);
	    t.printStackTrace ();
	}
    }
    
    public void dragEnter (DropTargetDragEvent e) {}
    public void dragExit (DropTargetEvent e) {}
    public void dragOver (DropTargetDragEvent e) {}
    public void dropActionChanged (DropTargetDragEvent e) {}
}
