/**
 * $Id: AddSingerFrame.java,v 1.3 2005/09/08 15:37:40 nicks Exp $
 *
 * Based on AddConstituentFrame, this one's for adding Singers
 * to the roster. (Did you honestly need me to tell you that?) 
 *
 * Originally part of the AMATO application.
 * 
 * Nick Seidenman <nick@seidenman.net>
 *
 * $Log: AddSingerFrame.java,v $
 * Revision 1.3  2005/09/08 15:37:40  nicks
 * Seasons, Productions, Performances, Constituents, and Operas are now
 * all editable!
 *
 * Revision 1.2  2005/08/02 21:47:28  nicks
 * Properly subclasses off of AddConstituentFrame.java.
 * Uses javadoc commenting conventions.
 *
 *
 */

import java.util.*;
import java.io.*;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

public class AddSingerFrame extends AddConstituentFrame
{
    private static final long serialVersionUID = 20050802142700L;

    private static ListEditorTextField voice;
    private static JComboBox lvl;

    public AddSingerFrame () 
    {
	super ("Add Singer");

	JPanel vp = new JPanel (new FlowLayout ());
	voice = new ListEditorTextField ();
	vp.setBorder (BorderFactory.createTitledBorder ("Voice Type(s)"));
	vp.add (voice);

	JPanel lvl_pan = new JPanel (new GridLayout (0,1));
	lvl_pan.setBorder (BorderFactory.createTitledBorder ("Proficiency"));
	String[] lvl_str = {"principal", "comprimario", "chorus"};
	lvl = new JComboBox (lvl_str);
	lvl.setFont (Amato.H3);
	lvl_pan.add (lvl);

	vp.add (lvl_pan);

	injectPanel (vp);
	realize ();
    }

    public Constituent addMap (HashMap<String,String> h)
    {
	h.put ("voice", voice.toString ());
	h.put ("level", lvl.getSelectedItem().toString());

	return (Constituent) new Singer (h);
    }


    /**
     * After creating the frame, use this method to populate it
     * with values from an existing Singer instance.  This way
     * we can edit the entries.
     */
    public void populate (Singer s)
    {
	super.populate ((Constituent) s);
	voice.add (s.getVoiceArray());
	lvl.setSelectedItem (s.getProficiency());
    }

    static public void main (String[] args)
    {
	AddSingerFrame asf = new AddSingerFrame ();
    }
}
