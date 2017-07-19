/**
 * $Id: OperaPanel.java,v 1.21 2005/09/12 17:36:44 nicks Exp $
 * 
 * The OperaPanel contains a JTree and a pane to display the operas.
 * It will also manage the adding, deleting, and 
 * editing of Opera  in the AMATO database.
 *
 * Originally part of the AMATO application.
 * 
 * @author Nick Seidenman <nick@seidenman.net>
 * @version $Revision: 1.21 $
 *
 * $Log: OperaPanel.java,v $
 * Revision 1.21  2005/09/12 17:36:44  nicks
 * If a singer is being cast in more than one role, a pop-up warning
 * is displayed and the user is asked if this is ok.  If it is, the singer
 * is (n-tuple cast).
 *
 * Revision 1.20  2005/09/09 23:26:26  nicks
 * Before loading another db or exiting, changed flag is checked and
 * the user is prompted to save (or not) the current db.
 *
 * Revision 1.19  2005/09/08 15:37:40  nicks
 * Seasons, Productions, Performances, Constituents, and Operas are now
 * all editable!
 *
 * Revision 1.18  2005/08/31 16:21:31  nicks
 * Fixed problem where role/voice wasn't being added in AddOperaPanel.
 *
 * Revision 1.17  2005/08/21 19:06:23  nicks
 * Changed comment header to conform with javadoc conventions.
 *
 * Revision 1.16  2005/08/15 14:26:28  nicks
 * Changed as_* to to* (more java-esque)
 *
 * Revision 1.15  2005/07/22 22:12:39  nicks
 * Started work on Drag-n-Drop within RosterPanel.
 * Cleaned up nearly all warnings.  Only a few left in Opera.java, Operas.java,
 * and Performance.java and these pertain to strict type-checking ala 1.5.
 * Just wanted to check everything in now as a check point.  I'll continue
 * to work on getting a totally clean compilation.
 *
 * Revision 1.14  2005/07/18 22:53:22  nicks
 * Added a separator between add and delete in the
 * popup menu.
 *
 * Revision 1.13  2005/07/18 22:44:01  nicks
 * Added add/delete pop-up to RosterPanel and OperaPanel.
 * Still need to get RosterPanel fully working, but this
 * will depend (or, rather, how I do it will depend) on whether
 * I end up implementing some of the ideas mentioned in the TODO list.
 * (e.g. have subtrees under Singers for different voices.)
 *
 * Revision 1.12  2005/07/17 11:48:35  nicks
 * toJPanel instead of as_JLabel (instead of toHTML).
 *
 * Revision 1.11  2005/07/15 19:27:39  nicks
 * Details are now displayed as JLabels, not html.  (Actually, I'm
 * taking advantage of the new JLabel capability that lets you
 * supply html as the label content.)
 *
 * Revision 1.10  2005/07/08 20:10:24  nicks
 * Changed the base class of Operas to AbstractListModel in preparation
 * for integrating AddOperaPanel.  It made more sense to think of the
 * opera collection as a list, rather than a tree.  (Really, just
 * a checkpoint here so I have something I can revert back to.)
 *
 * Revision 1.9  2005/06/30 15:39:39  nicks
 * Added serialVersionUID to keep the 5.0 compiler happy.
 * (sheesh!)
 *
 * Revision 1.8  2005/06/28 21:27:28  nicks
 * Added controls to set the scrolling area to top-left.
 *
 * Revision 1.7  2005/06/22 20:03:05  nicks
 * Cleaned out some sizing cruft from the *Panel objects.
 *
 * Revision 1.6  2005/06/21 21:16:02  nicks
 * EUREKA!  I've got the thing to open with the size I want
 * and the TabbedPanel has expanded to fill the frame and resized
 * when I resize the frame.  In other words, I have something I can
 * now show Irene!!!
 *
 * Revision 1.5  2005/06/21 18:43:06  nicks
 * Now have a menubar.  Starting to integrate components.
 *
 * Revision 1.4  2005/06/20 22:34:38  nicks
 * SeasonPanel is now working in a cheesy, demo-only sort of way.  It's
 * at least enough for me to get the idea across to Irene and talk
 * through how it will work when finished.d
 *
 * Revision 1.3  2005/06/18 04:19:02  nicks
 * Have OperaPanel and RosterPanel (cheesy versions) working.
 * This should be suitable for demo to Irene sometime soon.
 * I just want to more or less dummy up a SeasonPanel and I'll
 * be ready to demo.
 *
 */

import java.io.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.*;
import javax.swing.border.*;

import java.net.URL;

public class OperaPanel 
    extends JPanel 
    implements ListSelectionListener, ActionListener
{
    private static final long serialVersionUID = 20050630112660L;
    private JList list;
    private static boolean DEBUG = false;
    private JSplitPane splitPane;

    private Operas operas;

    private JPopupMenu popup;
    private JMenuItem addIt;
    private JMenuItem editIt;
    private JMenuItem delIt;

    private Opera selectedListNode;

    public OperaPanel(Operas ops) {
	super (new GridLayout (1, 0));

	operas = ops;
	selectedListNode = null;

	// Create a list that allows one selection at a time.
	list = new JList (operas);
	list.setCellRenderer (new OperaListCellRenderer());

	list.getSelectionModel().setSelectionMode 
	    (ListSelectionModel.SINGLE_SELECTION);

	// Enable toll tips.
	ToolTipManager.sharedInstance().registerComponent (list);

	//Set the icon for leaf nodes here.  Just using the 
	// defaults for now.

	// Listen for when the selection changes.
	list.addListSelectionListener (this);

	// Create the scroll pane and add the list to it.
	JScrollPane listView = new JScrollPane (list);

	// Add the scroll panes to a split pane.
	splitPane = new JSplitPane (JSplitPane.HORIZONTAL_SPLIT);
	splitPane.setLeftComponent (listView);

	showDetails (operas.toJPanel());
	splitPane.setDividerLocation (250);
	
	// Add a pop-up menu for adding and deleting Roster elements.

	popup = new JPopupMenu();

	JMenuItem item;
	addIt = new JMenuItem ("Add");
	popup.add (addIt);
	addIt.setHorizontalTextPosition (JMenuItem.RIGHT);
	addIt.addActionListener (this);

	popup.addSeparator ();

	editIt = new JMenuItem ("Edit");
	popup.add (editIt);
	editIt.setHorizontalTextPosition (JMenuItem.RIGHT);
	editIt.addActionListener (this);

	popup.addSeparator ();

	delIt = new JMenuItem ("Delete");
	popup.add (delIt);
	delIt.setHorizontalTextPosition (JMenuItem.RIGHT);
	delIt.addActionListener (this);

	popup.setBorder (new BevelBorder (BevelBorder.RAISED));

	list.addMouseListener (new MousePopupListener());

	// Add the split pane to this panel.
	add (splitPane);
    }

    class OperaListCellRenderer extends JLabel implements ListCellRenderer
    {
	private static final long serialVersionUID = 20050722112660L;

	public Component getListCellRendererComponent (JList l,
						       Object v,
						       int ndx,
						       boolean isSelected,
						       boolean cellHasFocus)
	{
	    Opera op = (Opera) v;
	    setText (op.getName() + " (" + op.getProductionCount() + ")");
	    
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
	Opera op = (Opera) jl.getSelectedValue ();

	if (op != null) showDetails (op.toJPanel());
    }
    
    private void showDetails (JPanel det)
    {
	splitPane.setRightComponent (new JScrollPane (det));
	splitPane.setDividerLocation (250);
    }
	
    private void showDetails (JLabel det)
    {
	JScrollPane jsp = new JScrollPane (det);
	jsp.setHorizontalScrollBarPolicy (ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	splitPane.setRightComponent (jsp);
	splitPane.setDividerLocation (250);
    }


    /** Required for ActionListener interface. **/

    public void actionPerformed (ActionEvent e) {

	if ("Add".equals (e.getActionCommand())) {
	    addNode (e);
	}

	else if ("Edit".equals (e.getActionCommand())) {
	    editNode (e);
	}

	else if ("Delete".equals (e.getActionCommand())) {
	    deleteNode (e);
	}

	else {
	    System.err.println ("ERROR: Unknown popup menu command: " +
				e.getActionCommand ());
	}
    }
    
    // An inner class to check whether mouse events are the popup trigger.
    class MousePopupListener extends MouseAdapter 
    {
	private static final long serialVersionUID = 20050722112670L;

	public void mousePressed (MouseEvent e) { checkPopup (e); }
	public void mouseClicked (MouseEvent e) { checkPopup (e); }
	public void mouseReleased (MouseEvent e) { checkPopup (e); }

	private void checkPopup (MouseEvent e) 
	{

	    if (e.isPopupTrigger()) {
		
		selectedListNode = findOperaUsingEvent (e);

		if (selectedListNode == null) {
		    // Right-clicked somewhere other than on a node.
		    delIt.setEnabled (false);
		    editIt.setEnabled (false);
		}
		else {
		    // Right-clicked on a node.
		    delIt.setEnabled (true);
		    editIt.setEnabled (true);
		}

		popup.show (OperaPanel.this, e.getX(), e.getY());

	    }
	}
	
	private Opera findOperaUsingEvent (MouseEvent e)
	{
	    Opera op = null;

	    // This is how we determine if the event involved
	    // a node in the tree (as opposed to someplace out
	    // in the nether reaches of non-JTree space.)  
	    
	    // Step 1: Find out where we clicked.
	    int x = e.getX();
	    int y = e.getY();

	    // Step 2: find closest actual tree node to those coords.
	    Point p = new Point (x, y);
	    int locus = list.locationToIndex (p);
	    Rectangle r = list.getBounds ();
	    
	    // Step 3: find out if the coords are actually inside the
	    //         rectangle that represents that node.
	    if (x > r.x && x < (r.x + r.width) && 
		y > r.y && y < (r.y + r.height)) {
		// Right-clicked on an actual node.
		list.setSelectedIndex (locus);
		op = (Opera) list.getSelectedValue ();
	    }
	    else {
		list.setSelectedIndex (-1);
	    }
		
	    return op;
	}
    }

    public void addNode (ActionEvent e) {
	Runnable showAddOperaDialog = new Runnable () {
		public void run() { 
		    AddOperaFrame asf = new AddOperaFrame ();
		}
	    };
	SwingUtilities.invokeLater (showAddOperaDialog);
    }
	
    public void editNode (ActionEvent e) {
	Runnable showAddOperaDialog = new Runnable () {
		public void run() { 
		    AddOperaFrame asf = new AddOperaFrame ();
		    asf.populate ((Opera) selectedListNode);
		}
	    };
	SwingUtilities.invokeLater (showAddOperaDialog);
    }
	
    public void deleteNode (ActionEvent e) 
    {
	if (selectedListNode == null) {
	    return;  // This should never happen.
	}
	
	Opera op = (Opera) selectedListNode;

	// Check to see if this opera is featured in any productions.
	// If it is, pop up a warning to let the user know that
	// these productions must either be deleted, or the featured
	// opera must be changed.

	if (op.getProductionCount() > 0) {
	    String prodList = new String();
	    for (Production pr: op.getProductions()) {
		prodList += "\n  " + pr.getSeason().toString();
	    }

	    Amato.errorPopup ("This opera is featured in " +
			      op.getProductionCount() + " productions\n" +
			      "in the following seaons:" + prodList +
			      "\nYou must delete those productions before you\n" +
			      "can delete this opera from the list.");
	    return;
	}
	    
	if (Amato.confirmPopup ("Do you really want to delete\n" +
				op.toString() + "?", "Delete Opera") != 0) {
	    return; // Operation cancelled.
	}
	
	// Hack away!
	operas.remove (selectedListNode);
	selectedListNode = null;
	redraw ();
    }

    final public String getName () { return "Operas"; }

    public void redraw () { list.updateUI(); }
}


