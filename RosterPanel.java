/**
 * $Id: RosterPanel.java,v 1.22 2005/09/13 15:15:44 nicks Exp nicks $
 * 
 * The RosterPanel contains a JTree and a pane to display the rosters.
 * It will also manage the adding, deleting, and 
 * editing of Roster  in the AMATO database.
 *
 * Originally part of the AMATO application.
 * 
 * @author Nick Seidenman <nick@seidenman.net>
 * @version $Revision: 1.22 $
 *
 * $Log: RosterPanel.java,v $
 * Revision 1.22  2005/09/13 15:15:44  nicks
 * "changed" flag is now set for any add or delete operation.
 *
 * Revision 1.21  2005/09/12 21:25:04  nicks
 * Performances listed under productions in the season panel are now
 * color-coded to indicate the level of casting.  A yellow dot indicates
 * the performance is not cast at all, a red dot indicates that one or
 * more (but not all) roles have been cast, and a green dot indicates
 * that all roles have been cast.  Thanks to Bengi Mayone for this
 * idea.
 *
 * Revision 1.20  2005/09/12 17:44:29  nicks
 * Converted JOptionDialog calls to Amato.confirmPopup calls.
 *
 * Revision 1.19  2005/09/08 15:37:40  nicks
 * Seasons, Productions, Performances, Constituents, and Operas are now
 * all editable!
 *
 * Revision 1.18  2005/08/21 19:06:23  nicks
 * Changed comment header to conform with javadoc conventions.
 *
 * Revision 1.17  2005/08/15 14:26:28  nicks
 * Changed as_* to to* (more java-esque)
 *
 * Revision 1.16  2005/08/03 18:33:53  nicks
 * Now able to add Singers, Musicians, Conductors, Directors, Staff on RosterPanel pop-up menu.
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
 * Revision 1.12  2005/07/18 16:55:56  nicks
 * Add/Delete popup now working in SeasonPanel.  This is a total hack, but
 * it works well.  I'm going to refine this into something more java-esque
 * and then add this capability to RosterPanel and Opera panel, too.
 * Once I've done this, I can get rid of the "Actions" menubar menu item.
 *
 * Revision 1.11  2005/07/17 11:48:35  nicks
 * toJPanel instead of as_JLabel (instead of toHTML).
 *
 * Revision 1.10  2005/06/30 15:39:39  nicks
 * Added serialVersionUID to keep the 5.0 compiler happy.
 * (sheesh!)
 *
 * Revision 1.9  2005/06/28 21:27:28  nicks
 * Added controls to set the scrolling area to top-left.
 *
 * Revision 1.8  2005/06/22 20:03:05  nicks
 * Cleaned out some sizing cruft from the *Panel objects.
 *
 * Revision 1.7  2005/06/22 18:34:48  nicks
 * Changed Other category to Staff .  This will include StageManager,
 * HouseManager, Designer, etc objects if/when I add them later.
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
 * Revision 1.4  2005/06/20 22:46:53  nicks
 * Now uses RosterInfo objects for top lever container and subfolders. (This)
 * just makes things a little neater and more consistent when you click
 * on a folder/container in the left-hand (tree) panel.)
 *
 * Revision 1.3  2005/06/20 22:34:38  nicks
 * SeasonPanel is now working in a cheesy, demo-only sort of way.  It's
 * at least enough for me to get the idea across to Irene and talk
 * through how it will work when finished.d
 *
 * Revision 1.2  2005/06/18 22:41:51  nicks
 * RosterPanel now working.
 * Several changes to the toHTML methods.
 * Fixed parsing in Musician so that list of instruments is properley
 *  split along comma (,) delimiters.
 *
 * Revision 1.1  2005/06/18 04:18:44  nicks
 * Initial revision
 *
 */


import java.io.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.border.*;

import java.net.URL;

public class RosterPanel 
    extends JPanel 
    implements TreeSelectionListener, TreeModelListener, ActionListener
{
    private static final long serialVersionUID = 20050630112630L;
    private JTree tree;
    private static boolean DEBUG = false;
    private JSplitPane splitPane;
    private Roster roster;

    private DefaultMutableTreeNode selectedTreeNode;

    private int lastButtonClicked = 0;

    private JPopupMenu popup;
    private JMenuItem delIt;
    private JMenuItem editIt;

    public RosterPanel(Roster rost) {
	super (new GridLayout (1, 0));
	
	// Create a tree that allows one selection at a time.
	roster = Amato.db.getRoster();
	tree = new JTree (roster);
	tree.getSelectionModel().setSelectionMode 
	    (TreeSelectionModel.SINGLE_TREE_SELECTION);

	// Enable toll tips.
	ToolTipManager.sharedInstance().registerComponent (tree);

	//Set the icon for leaf nodes here.  For now just using the
	// defaults.

	// Listen for when the selection changes.
	tree.addTreeSelectionListener (this);
	roster.addTreeModelListener (this);

	// Create the scroll pane and add the tree to it.
	JScrollPane treeView = new JScrollPane (tree);
	
	// Add the scroll panes to a split pane.
	splitPane = new JSplitPane (JSplitPane.HORIZONTAL_SPLIT);
	splitPane.setLeftComponent (treeView);
	showDetails (roster.toJPanel());
	splitPane.setDividerLocation (250);

	// Add a pop-up menu for adding and deleting Roster elements.

	popup = new JPopupMenu();
	JMenu addMenu = new JMenu ("Add ...");
	JMenuItem addIt;

	addIt = new JMenuItem ("Add Singer");
	addMenu.add (addIt);
	addIt.setHorizontalTextPosition (JMenuItem.RIGHT);
	addIt.addActionListener (this);

	addIt = new JMenuItem ("Add Musician");
	addMenu.add (addIt);
	addIt.setHorizontalTextPosition (JMenuItem.RIGHT);
	addIt.addActionListener (this);

	addIt = new JMenuItem ("Add Conductor");
	addMenu.add (addIt);
	addIt.setHorizontalTextPosition (JMenuItem.RIGHT);
	addIt.addActionListener (this);

	addIt = new JMenuItem ("Add Director");
	addMenu.add (addIt);
	addIt.setHorizontalTextPosition (JMenuItem.RIGHT);
	addIt.addActionListener (this);

	addIt = new JMenuItem ("Add Staff");
	addMenu.add (addIt);
	addIt.setHorizontalTextPosition (JMenuItem.RIGHT);
	addIt.addActionListener (this);
	
	popup.add (addMenu);
	
	popup.addSeparator ();

	editIt = new JMenuItem ("Edit");
	editIt.setHorizontalTextPosition (JMenuItem.RIGHT);
	editIt.setEnabled (false);
	editIt.addActionListener (this);
	
	popup.add (editIt);
	
	popup.addSeparator ();

	delIt = new JMenuItem ("Delete");
	popup.add (delIt);
	delIt.setHorizontalTextPosition (JMenuItem.RIGHT);
	delIt.addActionListener (this);

	popup.setBorder (new BevelBorder (BevelBorder.RAISED));

	tree.addMouseListener (new MousePopupListener());

	// Add the split pane to this panel.
	add (splitPane);
    }

    final public String getName () { return "Roster"; }
 
    /** Required by TreeSelectionListener interface. **/
    public void valueChanged (TreeSelectionEvent e) 
    {
	Object node = 
	    (Object) tree.getLastSelectedPathComponent ();

	if (node == null) 
	    return;

	else if (node instanceof Roster) {
	    showDetails (((Roster) node).toJPanel());
	}

	else if (node instanceof Singer) {
	    showDetails (((Singer) node).toJPanel());
	}
	
	else if (node instanceof Musician) {
	    showDetails (((Musician) node).toJPanel());
	}

	else if (node instanceof Conductor) {
	    showDetails (((Conductor) node).toJPanel());
	}

	else if (node instanceof Director) {
	    showDetails (((Director) node).toJPanel ());
	}

	else if (node instanceof Staff) {
	    showDetails (((Staff) node).toJPanel ());
	}

	else if (node instanceof DefaultMutableTreeNode) {
	    showDetails (roster.getSubtreeJLabel (node.toString()));
	}

	else 
	    showDetails (((Constituent) node).toJPanel());
    }


    private void showDetails (JLabel det)
    {
	splitPane.setRightComponent (new JScrollPane (det));
	splitPane.setDividerLocation (250);
    }

    private void showDetails (JPanel det)
    {
	splitPane.setRightComponent (new JScrollPane (det));
	splitPane.setDividerLocation (250);
    }

    public void redraw () { tree.updateUI(); }

    private DefaultMutableTreeNode findTreeNodeUsingEvent (MouseEvent e)
    {
	// This is how we determine if the event involved
	// a node in the tree (as opposed to someplace out
	// in the nether reaches of non-JTree space.)  
	
	// Step 1: Find out where we clicked.
	int x = e.getX();
	int y = e.getY();
	
	// Step 2: find closest actual tree node to those coords.
	TreePath tp = tree.getClosestPathForLocation (x, y);
	Rectangle r = tree.getPathBounds (tp);

	// Step 3: find out if the coords are actually inside the
	//         rectangle that represents that node.
	if (x > r.x && x < (r.x + r.width) && 
	    y > r.y && y < (r.y + r.height)) {
	    // Right-clicked on an actual node.

	    return (DefaultMutableTreeNode) tp.getLastPathComponent();
	}

	return null;  // No particular node.
    }

    /** Required by TreeModelListener **/
    public void treeNodesChanged (TreeModelEvent e)
    {
	// Since the nodes in the tree proper are not
	// editable, we should never come here, and if
	// for some odd reason we do, we had better not
	// do a damn thing.
	System.err.println ("treeNodesChanged: why are we here?");
    }
    
    public void treeNodesInserted (TreeModelEvent e)
    {
	redraw ();
    }
    
    public void treeNodesRemoved (TreeModelEvent e)
    {
	redraw ();
    }
    
    public void treeStructureChanged (TreeModelEvent e)
    {
	redraw ();
    }

    private DefaultMutableTreeNode getNode (TreeModelEvent e)
    {
	DefaultMutableTreeNode node;
	node = (DefaultMutableTreeNode) (e.getTreePath().getLastPathComponent());
	//System.out.println ("getNode: " + node); // DEBUG
	int[] indices = e.getChildIndices();
	//System.out.println ("getNode: " + indices.length);  // DEBUG
	try { 
	    int index = e.getChildIndices () [0];
	    node = (DefaultMutableTreeNode) (node.getChildAt (index));
	}
	catch (NullPointerException exc) {}

	return node;
    }

    /** Required for ActionListener interface. **/

    public void actionPerformed (ActionEvent e) {

	if ("Add".equals (e.getActionCommand().substring (0, 3))) {
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
    
    public void addNode (ActionEvent e) 
    {
	Runnable showAddConstituentDialog = null;
	String cmd = e.getActionCommand ().substring (4);

	//System.out.println ("adding " + cmd);
	
	if (cmd.equals ("Singer")) {
	    showAddConstituentDialog = new Runnable () {
		    public void run() { 
			AddSingerFrame addDialog = new AddSingerFrame ();
			addDialog.realize ();
		    }
		};
	}
	
	else if (cmd.equals ("Musician")) {
	    showAddConstituentDialog = new Runnable () {
		    public void run() { 
			AddMusicianFrame addDialog = new AddMusicianFrame ();
			addDialog.realize ();
		    }
		};
	}

	else if (cmd.equals ("Conductor")) {
	    showAddConstituentDialog = new Runnable () {
		    public void run() { 
			AddConductorFrame addDialog = new AddConductorFrame ();
			addDialog.realize ();
		    }
		};
	}

	else if (cmd.equals ("Director")) {
	    showAddConstituentDialog = new Runnable () {
		    public void run() { 
			AddDirectorFrame addDialog = new AddDirectorFrame ();
			addDialog.realize ();
		    }
		};
	}

	else if (cmd.equals ("Staff")) {
	    showAddConstituentDialog = new Runnable () {
		    public void run() { 
			AddStaffFrame addDialog = new AddStaffFrame ();
			addDialog.realize ();
		    }
		};
	}

	if (showAddConstituentDialog != null) 
	    SwingUtilities.invokeLater (showAddConstituentDialog);
    }
    
    public void editNode (ActionEvent e) 
    {
	Runnable showAddConstituentDialog = null;
	String cmd = e.getActionCommand ().substring (4);

	//System.out.println ("adding " + cmd);
	
	if (selectedTreeNode instanceof Singer) {
	    showAddConstituentDialog = new Runnable () {
		    public void run() { 
			AddSingerFrame addDialog = new AddSingerFrame ();
			addDialog.populate ((Singer) selectedTreeNode);
			addDialog.realize ();
		    }
		};
	}
	
	if (selectedTreeNode instanceof Musician) {
	    showAddConstituentDialog = new Runnable () {
		    public void run() { 
			AddMusicianFrame addDialog = new AddMusicianFrame ();
			addDialog.populate ((Musician) selectedTreeNode);
			addDialog.realize ();
		    }
		};
	}
	
	if (selectedTreeNode instanceof Conductor) {
	    showAddConstituentDialog = new Runnable () {
		    public void run() { 
			AddConductorFrame addDialog = new AddConductorFrame ();
			addDialog.populate ((Conductor) selectedTreeNode);
			addDialog.realize ();
		    }
		};
	}
	
	if (selectedTreeNode instanceof Director) {
	    showAddConstituentDialog = new Runnable () {
		    public void run() { 
			AddDirectorFrame addDialog = new AddDirectorFrame ();
			addDialog.populate ((Director) selectedTreeNode);
			addDialog.realize ();
		    }
		};
	}
	
	if (selectedTreeNode instanceof Staff) {
	    showAddConstituentDialog = new Runnable () {
		    public void run() { 
			AddStaffFrame addDialog = new AddStaffFrame ();
			addDialog.populate ((Staff) selectedTreeNode);
			addDialog.realize ();
		    }
		};
	}
	
	if (showAddConstituentDialog != null)
	    SwingUtilities.invokeLater (showAddConstituentDialog);
    }

    public void deleteNode (ActionEvent e) 
    {
	//System.out.println ("delete");

	if (selectedTreeNode == null) {
	    return;  // This should never happen.
	}
	
	if (selectedTreeNode instanceof Constituent) {
	    // Do they really want to delete this roster member?
	    if (Amato.confirmPopup ("Do you really want to delete \n" +
				    selectedTreeNode.toString() + 
				    "\nfrom the roster?",
				    "Delete " + selectedTreeNode.toString() + "?") != 0)
		return; // Operation cancelled.
	}

	else { // We only delete "Constituent" nodes (leaves).
	    System.err.println ("WARNING: tried to delete a folder.");
	    return;
	}
	
	// Chop it outta there!
	DefaultMutableTreeNode p = (DefaultMutableTreeNode) selectedTreeNode.getParent ();
	if (p != null) {
	    //System.out.println ("removing " + selectedTreeNode);
	    Constituent cnst = (Constituent) selectedTreeNode;
	    cnst.setValid (false);
	    p.remove (selectedTreeNode);
	    selectedTreeNode = null;
	    Amato.db.setChanged(true);
	    redraw ();
	}
    }

    // An inner class to check whether mouse events are the popup trigger.
    class MousePopupListener extends MouseAdapter 
    {
	public void mousePressed (MouseEvent e) { checkPopup (e); dnd (e); }
	public void mouseClicked (MouseEvent e) { checkPopup (e); dnd (e); }
	public void mouseReleased (MouseEvent e) { checkPopup (e); dnd(e); }

	private void checkPopup (MouseEvent e) 
	{
	    if (e.isPopupTrigger()) {
		selectedTreeNode = findTreeNodeUsingEvent (e);
		if (selectedTreeNode == null) {
		    // Right-clicked somewhere other than on a node.
		    //System.out.println ("no particular node: add category?"); //DEBUG
		    delIt.setEnabled (false);
		    editIt.setEnabled (false);
		}

		else if (selectedTreeNode instanceof Constituent) {
		    // Right-clicked on a node.
		    delIt.setEnabled (true);
		    editIt.setEnabled (true);
		}

		else {
		    delIt.setEnabled (false);
		    editIt.setEnabled (false);
		}

		popup.show (RosterPanel.this, e.getX(), e.getY());

	    }

	}

	private void dnd (MouseEvent e)
	{
	    if (e.getButton() == MouseEvent.BUTTON1) {
		//System.out.println (findTreeNodeUsingEvent (e));
	    }
	}
    }
}
