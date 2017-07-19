/**
 * $Id: ListEditorTextField.java,v 1.3 2005/09/08 15:37:40 nicks Exp $
 *
 * Derived from ListEditor class, this class adds
 * a text field to enter items into the list.
 * 
 * Originally part of the AMATO application.
 * 
 * @author Nick Seidenman <nick@seidenman.net>
 * @version $Revision: 1.3 $
 *
 * $Log: ListEditorTextField.java,v $
 * Revision 1.3  2005/09/08 15:37:40  nicks
 * Seasons, Productions, Performances, Constituents, and Operas are now
 * all editable!
 *
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

public class ListEditorTextField extends ListEditor
{
    private static final long serialVersionUID = 20050725172500L;

    public ListEditorTextField ()
    {
	super ();

	JTextField e = new JTextField ();
	setEditor ((JComponent) e);
    }

    public ListEditorTextField (String[] current)
    {
	super ();

	JTextField e = new JTextField ();
	for (String s: current) addItem (s);
	setEditor ((JComponent) e);
    }

    /**
     * Add the String objects in sList to the current set.
     *
     * @arg sList Strings to be added.
     */
    public void add (String[] sList)
    {
	for (String s: sList) addItem (s);
    }

    static public void main (String[] args)
    {
	JFrame f1 = new JFrame ("List Editor Combobox");
	f1.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);

	String[] opts = {"this", "that", "other"};

	ListEditorTextField le1 = new ListEditorTextField ();

	le1.setBorder (BorderFactory.createTitledBorder ("List Editor 1"));

	f1.add (le1);
	f1.pack ();
	f1.setVisible (true);
    }
}
