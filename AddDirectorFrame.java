/**
 * $Id: AddDirectorFrame.java,v 1.2 2005/09/08 15:37:40 nicks Exp $
 *
 * Based on AddConstituentFrame, this one's for adding Directors
 * to the roster.
 *
 * Originally part of the AMATO application.
 * 
 * @author Nick Seidenman <nick@seidenman.net>
 * @version $Revision: 1.2 $
 *
 * $Log: AddDirectorFrame.java,v $
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

public class AddDirectorFrame extends AddConstituentFrame
{
    private static final long serialVersionUID = 20050802143200L;

    public AddDirectorFrame () 
    {
	super ("Add Director");

	realize ();
    }

    public Constituent addMap (HashMap<String,String> h)
    {
	return (Constituent) new Director (h);
    }

    /**
     * After creating the frame, use this method to populate it
     * with values from an existing Director instance.  This way
     * we can edit the entries.
     */
    public void populate (Director s)
    {
	super.populate ((Constituent) s);
    }

    static public void main (String[] args)
    {
	AddDirectorFrame asf = new AddDirectorFrame ();
    }
}
