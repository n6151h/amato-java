/**
 * $Id: AddMusicianFrame.java,v 1.2 2005/09/08 15:37:40 nicks Exp $
 *
 * Based on AddConstituentFrame, this one's for adding Musicians
 * (i.e. orchestra members)  to the roster. (Did you honestly 
 * need me to tell you that?) 
 *
 * Originally part of the AMATO application.
 * 
 * @author Nick Seidenman <nick@seidenman.net>
 * @version $Revision: 1.2 $
 *
 * $Log: AddMusicianFrame.java,v $
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

public class AddMusicianFrame extends AddConstituentFrame
{
    private static final long serialVersionUID = 20050802143000L;

    private static ListEditorTextField instruments;

    public AddMusicianFrame () 
    {
	super ("Add Musician");

	JPanel vp = new JPanel (new FlowLayout ());
	instruments = new ListEditorTextField ();
	vp.setBorder (BorderFactory.createTitledBorder ("Instrument(s)"));
	vp.add (instruments);

	injectPanel (vp);
	realize ();
    }

    public Constituent addMap (HashMap<String,String> h)
    {
	h.put ("instrument", instruments.toString ());
	return (Constituent) new Musician (h);
    }

    /**
     * After creating the frame, use this method to populate it
     * with values from an existing Musician instance.  This way
     * we can edit the entries.
     */
    public void populate (Musician s)
    {
	super.populate ((Constituent) s);
	instruments.add (s.getInstrumentsArray());
    }

    static public void main (String[] args)
    {
	AddMusicianFrame asf = new AddMusicianFrame ();
    }
}
