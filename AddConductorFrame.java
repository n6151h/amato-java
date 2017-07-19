/**
 * $Id: AddConductorFrame.java,v 1.2 2005/09/08 15:37:40 nicks Exp $
 *
 * Based on AddConstituentFrame, this one's for adding Conductors
 * to the roster.
 *
 * Originally part of the AMATO application.
 * 
 * @author Nick Seidenman <nick@seidenman.net>
 * @version $Revision: 1.2 $
 *
 * $Log: AddConductorFrame.java,v $
 * Revision 1.2  2005/09/08 15:37:40  nicks
 * Seasons, Productions, Performances, Constituents, and Operas are now
 * all editable!
 *
 * Revision 1.1  2005/08/02 21:39:59  nicks
 * Initial revision
 *
 *
 */

import java.util.*;
import java.io.*;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

public class AddConductorFrame extends AddConstituentFrame
{
    private static final long serialVersionUID = 20050802142600L;

    public AddConductorFrame () 
    {
	super ("Add Conductor");

	realize ();
    }

    public Constituent addMap (HashMap<String,String> h)
    {
	return (Constituent) new Conductor (h);
    }

    /**
     * After creating the frame, use this method to populate it
     * with values from an existing Conductor instance.  This way
     * we can edit the entries.
     */
    public void populate (Conductor s)
    {
	super.populate ((Constituent) s);
    }

    static public void main (String[] args)
    {
	AddConductorFrame asf = new AddConductorFrame ();
    }
}
