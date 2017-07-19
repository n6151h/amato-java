/**
 * $Id: ListEditorComboBox.java,v 1.2 2005/08/17 20:35:23 nicks Exp $
 *
 * Derived from ListEditor class, this class adds
 * a combobox to enter items into the list.
 * 
 * Originally part of the AMATO application.
 * 
 * @author Nick Seidenman <nick@seidenman.net>
 * @version $Revision: 1.2 $
 *
 * $Log: ListEditorComboBox.java,v $
 * Revision 1.2  2005/08/17 20:35:23  nicks
 * Converted comment header to comply with javadoc conventions.
 *
 * Revision 1.1  2005/07/25 21:41:40  nicks
 * Initial revision
 *
 */

import java.util.*;
import java.io.*;

import javax.swing.*;
import javax.swing.event.*;


public class ListEditorComboBox extends ListEditor
{
    private static final long serialVersionUID = 20050725162500L;

    public ListEditorComboBox ()
    {
	super ();

	JComboBox e = new JComboBox ();
	e.setEditable (false);
	setEditor ((JComponent) e);
    }

    public ListEditorComboBox (String[] opts)
    {
	super ();

	JComboBox e = new JComboBox ();
	for (String s: opts) e.addItem (s);
	e.setEditable (false);
	setEditor ((JComponent) e);
    }

    static public void main (String[] args)
    {
	JFrame f1 = new JFrame ("List Editor Combobox");
	f1.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);

	String[] opts = {"this", "that", "other"};

	ListEditorComboBox le1 = new ListEditorComboBox (opts);

	le1.setBorder (BorderFactory.createTitledBorder ("List Editor 1"));

	f1.add (le1);
	f1.pack ();
	f1.setVisible (true);
    }
}
