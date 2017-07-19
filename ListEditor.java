/**
 * $Id: ListEditor.java,v 1.4 2005/09/13 18:07:02 nicks Exp $
 * 
 * Originally part of the AMATO application.
 * 
 * @author Nick Seidenman <nick@seidenman.net>
 * @version $Revision: 1.4 $
 *
 * $Log: ListEditor.java,v $
 * Revision 1.4  2005/09/13 18:07:02  nicks
 * Entry field (JTextArea) is now cleared when the Add button is pressed.
 *
 * Revision 1.3  2005/08/17 20:35:23  nicks
 * Converted comment header to comply with javadoc conventions.
 *
 * Revision 1.2  2005/07/26 21:10:47  nicks
 * Removed debug cruft.
 *
 * Revision 1.1  2005/07/25 21:41:40  nicks
 * Initial revision
 *
 */

import java.util.*;
import java.io.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;


public class ListEditor 
    extends JPanel
    implements ActionListener, ListSelectionListener
{
    private static final long serialVersionUID = 20050725142500L;


    private JButton add_btn;
    private JButton rem_btn;

    private JList le_list;
    private JScrollPane le_scroller;

    private ListEditorList lem;

    private int selectedListIndex;

    private JPanel entry_panel;
    private JComponent entry_component;

    public ListEditor ()
    {
	super (new BorderLayout());

	selectedListIndex = -1;

	// Create "ADD" and REMOVE buttons.
	add_btn = new JButton ("ADD");
	rem_btn = new JButton ("REMOVE");

	add_btn.addActionListener (this);
	rem_btn.addActionListener (this);

	JPanel le_btns = new JPanel (new GridLayout (0,2));
	le_btns.add (add_btn);
	le_btns.add (rem_btn);
	
	// Leave room for the entry component.
	 entry_panel = new JPanel (new GridLayout (0, 1));
	
	// Create the (scrolling) list.
	lem = new ListEditorList ();
	
	le_list =  new JList (lem);
	le_list.setCellRenderer (new ListEditorCellRenderer());
	le_list.getSelectionModel().setSelectionMode 
	    (ListSelectionModel.SINGLE_SELECTION);
	le_list.addListSelectionListener (this);

	le_scroller = new JScrollPane (le_list);
	
	JPanel le_main = new JPanel (new GridLayout (0, 1));
	le_main.add (le_scroller);

	// Put 'em all together.
	add (le_main, BorderLayout.NORTH);
	add (entry_panel, BorderLayout.CENTER);
	add (le_btns, BorderLayout.SOUTH);
    }
    
    public void setEditor (JComponent ec)
    {
	entry_panel.add (ec);
	entry_component = ec;
    }

    public void addItem (String s)
    {
	lem.add (s);
    }

    public void addOption (String s)
    {
	if (entry_component instanceof JComboBox)
	    ((JComboBox) entry_component).addItem (s);
    }

    class ListEditorCellRenderer extends JLabel implements ListCellRenderer
    {
	private static final long serialVersionUID = 20050725112660L;

	public Component getListCellRendererComponent (JList l,
						       Object v,
						       int ndx,
						       boolean isSelected,
						       boolean cellHasFocus)
	{
	    setText ((String) v);
	    
	    if (isSelected) {
		setBackground (l.getSelectionBackground());
		setForeground (l.getSelectionForeground());
	    }
	    else {
		setBackground (l.getBackground());
		setForeground (l.getForeground());
	    }
	    
	    setEnabled (l.isEnabled());
	    setFont (l.getFont());
	    setOpaque (true);

	    return this;
	}
    }
  
    /** Required by ListSelectionListener interface. **/

    public void valueChanged (ListSelectionEvent e) 
    {
	JList jl = (JList) e.getSource ();
	
	selectedListIndex = jl.getSelectedIndex ();
	//	System.out.println ("selectedListIndex: " + selectedListIndex);
    }
    
    /** Required for ActionListener interface. **/

    public void actionPerformed (ActionEvent e) {

	if ("ADD".equals (e.getActionCommand())) {
	    addNode (e);
	}

	else if ("REMOVE".equals (e.getActionCommand())) {
	    deleteNode (e);
	}

	else {
	    System.err.println ("Shouldn't happen!");
	}
    }
    
    private void addNode (ActionEvent e)
    {
	if (entry_component instanceof JComboBox) {
	    int si = ((JComboBox) entry_component).getSelectedIndex ();
	    lem.add ((String) ((JComboBox) entry_component).getItemAt (si));
	}
	
	else if (entry_component instanceof JTextField) {
	    lem.add ((String) ((JTextField) entry_component).getText());
	    ((JTextField) entry_component).setText ("");
	}

	else {
	    System.err.println ("addNode: Unknown entry_component: " + 
			     entry_component.getClass());
	}

	le_list.updateUI ();
    }
    
    private void deleteNode (ActionEvent e)
    {
	//	System.out.println ("REMOVE button pressed: " + selectedListIndex);
	
	if (selectedListIndex >= 0) {
	    lem.remove (selectedListIndex);
	}
	
	selectedListIndex = -1;
	
	le_list.updateUI ();
    }

    private class ListEditorList extends AbstractListModel
    {
	private static final long serialVersionUID = 20050708112710L;
	
	private ArrayList<String> sList;
	
	public ListEditorList ()
	{
	    super ();
	    sList = new ArrayList<String> ();
	}
	
	public ListEditorList (String[] cl)
	{
	    super ();
	    sList = new ArrayList<String> ();
	    for (String s: cl) sList.add (s);
	}
	
	public void add (String s)
	{
	    sList.add (s);
	    
	    fireIntervalAdded (s, sList.size(), sList.size());
	}
	
	public void remove (int ndx)
	{
	    String s = sList.get (ndx);
	    
	    sList.remove (ndx);
	    fireIntervalRemoved (s, 0, sList.size());
	}
	
	public Object getElementAt (int ndx)
	{
	    if (ndx < 0 || ndx > sList.size()) return null;
	    
	    return sList.get (ndx);
	}
	
	final public int getSize () { return sList.size(); }

	
	final public String toString ()
	{
	    return sList.toString().substring(1, sList.toString().length()-1);
	}
	
    }

    final public String toString ()
    {
	return lem.toString();
    }
    
    private class EntryField extends JTextField
    {
	private static final long serialVersionUID = 20050725162500L;

	public EntryField () { super (); }

	public EntryField (String s) { super (s); }

	public Object getItemAt (int ignored) { return getText (); }
    }
   

    static public void main (String[] args)
    {
	JFrame f1 = new JFrame ("List Editor");
	f1.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);

	String[] opts = {"this", "that", "other"};

	ListEditor le1 = new ListEditor ();

	//JComboBox e = new JComboBox (opts);

	JTextField e = new JTextField ();

	le1.setEditor ((JComponent) e);

	le1.setBorder (BorderFactory.createTitledBorder ("List Editor 1"));

	f1.add (le1);
	f1.pack ();
	f1.setVisible (true);

    }
}

