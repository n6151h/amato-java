/*
 * $Id: SeasonPanel.java,v 1.37 2005/09/13 15:15:44 nicks Exp nicks $
 * 
 * The SeasonPanel will display and manage information about the seasons
 * in the following hierarchy:
 *
 *    Seasons
 *       Season 1 Name (e.g. 2005-2006)
 *          Production 1 (Opera Name)
 *             Performance 1 (Date)
 *             Performance 2 (date)
 *          Production 2 (Opera Name)
 *              :
 *          Production n (Opera Name)
 *            :
 *       Season N Name
 *
 * Selecting a Season will display a list of that seaon's productions 
 * in the right-hand panel.
 *
 * Selecting a production will display a table of performances for that
 * that production.  This is the grail that Irene originally wanted.  This
 * table is probably the most sophisiticated object in the application.
 * More detail about it can be found in PerformanceTable.java.
 *
 * Selecting a particular performance, either from the table heading 
 * or from the list under the performance catagory in the tree will
 * bring up the details for that particular performance.
 *
 * Originally part of the AMATO application.
 * 
 * @author Nick Seidenman <nick@seidenman.net>
 * @version $Revision: 1.37 $
 *
 * $Log: SeasonPanel.java,v $
 * Revision 1.37  2005/09/13 15:15:44  nicks
 * "changed" flag is now set for any add or delete operation.
 *
 * Revision 1.36  2005/09/12 21:25:04  nicks
 * Performances listed under productions in the season panel are now
 * color-coded to indicate the level of casting.  A yellow dot indicates
 * the performance is not cast at all, a red dot indicates that one or
 * more (but not all) roles have been cast, and a green dot indicates
 * that all roles have been cast.  Thanks to Bengi Mayone for this
 * idea.
 *
 * Revision 1.35  2005/09/12 17:44:29  nicks
 * Converted JOptionDialog calls to Amato.confirmPopup calls.
 *
 * Revision 1.34  2005/09/08 15:37:40  nicks
 * Seasons, Productions, Performances, Constituents, and Operas are now
 * all editable!
 *
 * Revision 1.33  2005/09/07 15:47:08  nicks
 * Now able to edit performances.  Will proceed to add this capability
 * to other Add*Frame.java components.
 *
 * Revision 1.32  2005/08/21 19:45:14  nicks
 * Started adding Edit capability (beginning with Performance instances.)
 *
 * Revision 1.31  2005/08/21 19:06:23  nicks
 * Changed comment header to conform with javadoc conventions.
 *
 * Revision 1.30  2005/08/16 02:08:35  nicks
 * AddPerformanceFrame added.  All the basic elements can now be adde
 * or deleted.  (Now need to add Edit function.)
 *
 * Revision 1.29  2005/08/15 14:26:29  nicks
 * Changed as_* to to* (more java-esque)
 *
 * Revision 1.28  2005/07/28 19:58:12  nicks
 * Got rid of the getClass() == ... construct.  Now uses instanceof.
 *
 * Revision 1.27  2005/07/22 23:25:22  nicks
 * Modified the info box that tells Add Performance isn
 * 't added yet.
 *
 * Revision 1.26  2005/07/22 22:12:39  nicks
 * Started work on Drag-n-Drop within RosterPanel.
 * Cleaned up nearly all warnings.  Only a few left in Opera.java, Operas.java,
 * and Performance.java and these pertain to strict type-checking ala 1.5.
 * Just wanted to check everything in now as a check point.  I'll continue
 * to work on getting a totally clean compilation.
 *
 * Revision 1.25  2005/07/18 22:53:22  nicks
 * Added a separator between add and delete in the
 * popup menu.
 *
 * Revision 1.24  2005/07/18 22:44:01  nicks
 * Added add/delete pop-up to RosterPanel and OperaPanel.
 * Still need to get RosterPanel fully working, but this
 * will depend (or, rather, how I do it will depend) on whether
 * I end up implementing some of the ideas mentioned in the TODO list.
 * (e.g. have subtrees under Singers for different voices.)
 *
 * Revision 1.23  2005/07/18 19:53:08  nicks
 * AddPerformancePanel hooks.  Stubbed with a MessageDialog for now.
 *
 * Revision 1.22  2005/07/18 19:40:25  nicks
 * Can now add/delete seasons and productions.
 * Cleaned up code in SeasonPanel so that now this object implements
 * the ActionListener interface, rather than defining an internal class
 * to do it.  Also, AddProductionFrame will now take a Season instance
 * for its constructor argument so that the selected season will display
 * by default in the Season combobox.
 *
 * Revision 1.21  2005/07/18 16:55:56  nicks
 * Add/Delete popup now working in SeasonPanel.  This is a total hack, but
 * it works well.  I'm going to refine this into something more java-esque
 * and then add this capability to RosterPanel and Opera panel, too.
 * Once I've done this, I can get rid of the "Actions" menubar menu item.
 *
 * Revision 1.20  2005/07/17 11:48:35  nicks
 * toJPanel instead of as_JLabel (instead of toHTML).
 *
 * Revision 1.19  2005/07/15 19:27:39  nicks
 * Details are now displayed as JLabels, not html.  (Actually, I'm
 * taking advantage of the new JLabel capability that lets you
 * supply html as the label content.)
 *
 * Revision 1.18  2005/07/14 22:27:40  nicks
 * Replaced println messages with ErrorDialog box.
 *
 * Revision 1.17  2005/07/14 18:21:50  nicks
 * Seasons (and the SeasonPanel.tree structure) are now managed using
 * a TreeModel paradigm.  Works VERY nice.  This is the JAVA way to do things!
 *
 * Revision 1.16  2005/07/12 19:17:30  nicks
 * Add Productions function works.
 *
 * Revision 1.15  2005/07/12 15:37:58  nicks
 * Removed debug cruft.
 *
 * Revision 1.14  2005/07/08 21:14:05  nicks
 * Commented out some debug cruft.
 *
 * Revision 1.13  2005/07/06 21:09:27  nicks
 * Removed debug cruft.
 *
 * Revision 1.12  2005/07/06 20:53:30  nicks
 * Can now add a season to the SeasonPanel!
 *
 * Revision 1.11  2005/07/05 20:44:44  nicks
 * Playing around with pop-ups.  Have figured out how to capture
 * mouse click events and map this to the tree/database object
 * clicked on.  Right clicks will eventually pop-up a
 * menu that will let the user add/delete/edit the selected
 * node.
 *
 * Revision 1.10  2005/07/01 21:30:27  nicks
 * htmlView now "homes" the cursor correctly.
 *
 * Revision 1.9  2005/06/30 15:39:39  nicks
 * Added serialVersionUID to keep the 5.0 compiler happy.
 * (sheesh!)
 *
 * Revision 1.8  2005/06/28 21:24:34  nicks
 * Added controls to set the scrolling area to top-left.
 *
 * Revision 1.7  2005/06/28 20:14:30  nicks
 * Prelim stuff to support printing.  Mostly code to provide the name
 * of the current panel.  The panel itself will have a routine that returns
 * the Printable to the PrinterJob object in Tabbed panes.
 *
 * Revision 1.6  2005/06/22 20:03:05  nicks
 * Cleaned out some sizing cruft from the *Panel objects.
 *
 * Revision 1.5  2005/06/21 21:16:02  nicks
 * EUREKA!  I've got the thing to open with the size I want
 * and the TabbedPanel has expanded to fill the frame and resized
 * when I resize the frame.  In other words, I have something I can
 * now show Irene!!!
 *
 * Revision 1.4  2005/06/21 18:43:06  nicks
 * Now have a menubar.  Starting to integrate components.
 *
 * Revision 1.3  2005/06/20 22:34:38  nicks
 * SeasonPanel is now working in a cheesy, demo-only sort of way.  It's
 * at least enough for me to get the idea across to Irene and talk
 * through how it will work when finished.d
 *
 * Revision 1.2  2005/06/20 17:31:30  nicks
 * SeasonPanel (dummy'd up) now working.
 *
 */

import java.io.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
import javax.swing.border.*;

import java.net.URL;

public class SeasonPanel 
    extends JPanel 
    implements TreeSelectionListener, TreeModelListener, ActionListener
{
    private static final long serialVersionUID = 20050630112620L;
    private JLabel seasonsLabel;
    private JScrollPane treeView;
    private JTree tree;
    private static boolean DEBUG = false;

    private JSplitPane splitPane;

    private int lastButtonClicked = 0;
    private DefaultMutableTreeNode selectedTreeNode;

    private JPopupMenu popup;
    private JMenuItem addIt;
    private JMenuItem editIt;
    private JMenuItem delIt;

    private Seasons seasons;

    private ImageIcon redball;
    private ImageIcon yellowball;
    private ImageIcon greenball;

    public SeasonPanel(Seasons seasons) {
	super (new GridLayout (1, 0));

	selectedTreeNode = null;

	seasons = Amato.db.getSeasons();

	// Initialize globally-used ImageIcons.
	URL url = this.getClass().getResource ("images/redball.png");
	redball = new ImageIcon (url);
	url = this.getClass().getResource ("images/yellowball.png");
	yellowball = new ImageIcon (url);
	url = this.getClass().getResource ("images/greenball.png");
	greenball = new ImageIcon (url);

	// Create a tree that allows one selection at a time.
	tree = new JTree (seasons);
	tree.getSelectionModel().setSelectionMode 
	    (TreeSelectionModel.SINGLE_TREE_SELECTION);
	tree.setDragEnabled (true);
	tree.setCellRenderer (new SeasonTreeNodeRenderer());

	// Enable tool tips.
	ToolTipManager.sharedInstance().registerComponent (tree);

	//Set the icon for leaf nodes here.  Just using the 
	// defaults for now.

	// Listen for when the selection changes.
	tree.addTreeSelectionListener (this);
	seasons.addTreeModelListener (this);

	// Create the scroll pane and add the tree to it.
	treeView = new JScrollPane (tree);
	
	// Add the scroll panes to a split pane.
	splitPane = new JSplitPane (JSplitPane.HORIZONTAL_SPLIT);
	splitPane.setLeftComponent (treeView);
	showDetails (seasons.toJPanel());
	splitPane.setDividerLocation (250);

	// Add a pop-up menu for adding and deleting season elements.
	// This includes Season, Production, and Performance instances.

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

	tree.addMouseListener (new MousePopupListener());

	// Add the split pane to this panel.
	add (splitPane);
    }

    /** Required by TreeSelectionListener interface. **/
    public void valueChanged (TreeSelectionEvent e) 
    {
	Object node = 
	    (Object) tree.getLastSelectedPathComponent ();

	if (node == null) 
	    return;

	else if (node instanceof Seasons) {
	    showDetails (((Seasons) node).toJPanel());
	}

	else if (node instanceof Season) {
	    showDetails (((Season) node).toJPanel());
	}
	
	else if (node instanceof Production) {
	    showDetails (((Production) node).toJPanel());
	}

	else if (node instanceof Performance) {
	    showDetails (((Performance) node).toJPanel());
	}

	else {
	    System.out.println ("SeasonPanel.valueChanged: unknown class: " + 
				node.getClass());
	}
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
	//System.out.println ("Popup menu item[" + 
	//e.getActionCommand() + "] was pressed.");
	//System.out.println ("selectedTreeNode: " + selectedTreeNode);

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
    
    public void addNode (ActionEvent e) {
	if (selectedTreeNode == null || 
	    selectedTreeNode instanceof Seasons) {
	    Runnable showAddSeasonDialog = new Runnable () {
		    public void run() { 
			AddSeasonFrame asf = new AddSeasonFrame ();
		    }
		};
	    SwingUtilities.invokeLater (showAddSeasonDialog);
	}
	
	else if (selectedTreeNode instanceof Season) {
	    Runnable showAddProductionDialog = new Runnable () {
		    public void run() { 
			AddProductionFrame apf = new AddProductionFrame ((Season) selectedTreeNode);
		    }
		};
	    SwingUtilities.invokeLater (showAddProductionDialog);
	}
	else if (selectedTreeNode instanceof Production) {
	    final Production p = (Production) selectedTreeNode;

	    Runnable showAddPerformanceDialog = new Runnable () {
		    public void run() { 
			AddPerformanceFrame apf = new AddPerformanceFrame (p, Amato.db.getRoster());
		    }
		};
	    SwingUtilities.invokeLater (showAddPerformanceDialog);

	}
    }
    
    public void editNode (ActionEvent e) {
	if (selectedTreeNode == null) { // Should never happen.
	    return;
	}
	
	if (selectedTreeNode instanceof Season) {
	    final Season s = (Season) selectedTreeNode;

	    Runnable showAddSeasonDialog = new Runnable () {
		    public void run() { 
			AddSeasonFrame asf = new AddSeasonFrame ();
			asf.populate (s);
		    }
		};
	    SwingUtilities.invokeLater (showAddSeasonDialog);
	}

	else if (selectedTreeNode instanceof Production) {
	    final Production p = (Production) selectedTreeNode;
	    final Season s = p.getSeason();

	    Runnable showAddProductionDialog = new Runnable () 	{	
		    public void run() { 		
	    	AddProductionFrame apf = new AddProductionFrame (s);
	    	apf.populate (p);
		    };	
	    }
	    SwingUtilities.invokeLater (showAddProductionDialog);
	}

	else if (selectedTreeNode instanceof Performance) {
	    final Performance p = (Performance) selectedTreeNode;
	    final Production prod = p.getProduction ();

	    Runnable showAddPerformanceDialog = new Runnable () {
		    public void run() { 
			AddPerformanceFrame apf = new AddPerformanceFrame (prod, Amato.db.getRoster());
			apf.populate (p);
		    }
		};
	    SwingUtilities.invokeLater (showAddPerformanceDialog);
	}
    }
    
    public void deleteNode (ActionEvent e) {
	if (selectedTreeNode == null) {
	    return;  // This should never happen.
	}
	
	if (selectedTreeNode instanceof Seasons) {
	    // Do they really want to delete all
	    // Seaons, productions, performances ... everything?
	    if (Amato.confirmPopup ("Do you really want to delete all\n" +
				    "Seasons, Productions, and " +
				    "Performances?",
				    "Delete EVERYTHING?") != 0)
		return; // Operation cancelled.
	}
	
	else if (selectedTreeNode instanceof Season) {
	    // Put up a confirmation dialog.
	    if (Amato.confirmPopup ("Do you really want to delete \n" + 
				    "this season, including all of\n" +
				    "its productions and performances?",
				    "Delete Season?") != 0)
		return; // Operation cancelled.
	    
	    // If confirmed, go ahead and delete the node (and
	}
	
	else if (selectedTreeNode instanceof Production) {
	    if (Amato.confirmPopup ("Do you really want to delete \n" +
				    "this production and all of its\n" +
				    "performances?",
				    "Delete Production?") != 0)
		return; // Operation cancelled.
	}
	
	else if (selectedTreeNode instanceof Performance) {
	    if (Amato.confirmPopup ("Delete this performance?",
				    "Delete Performance?") != 0)
		return; // Operation cancelled.
	    }
	}
	
	else {
	    // Should EVER get here.
	    System.err.println ("ERROR: attempted to delete unkonwn node.");
	    return;
	}
	
	// Chop it outta there!
	DefaultMutableTreeNode p = (DefaultMutableTreeNode) selectedTreeNode.getParent ();
	if (p != null) {
	    p.remove (selectedTreeNode);
	    selectedTreeNode = null;
	    Amato.db.setChanged(true);  
	    redraw ();
	}
	else if (selectedTreeNode instanceof Seasons) {
	    selectedTreeNode.removeAllChildren ();
	    Amato.db.setChanged(true);
	    redraw ();
	}
    }
    
    
    // An inner class to check whether mouse events are the popup trigger.
    class MousePopupListener extends MouseAdapter 
    {
	public void mousePressed (MouseEvent e) { checkPopup (e); }
	public void mouseClicked (MouseEvent e) { checkPopup (e); }
	public void mouseReleased (MouseEvent e) { checkPopup (e); }

	private void checkPopup (MouseEvent e) 
	{

	    if (e.isPopupTrigger()) {
		selectedTreeNode = findTreeNodeUsingEvent (e);
		editIt.setEnabled (false);
		delIt.setEnabled (false);

		if (selectedTreeNode instanceof Performance ||
		    selectedTreeNode instanceof Production ||
		    selectedTreeNode instanceof Season) {
		    delIt.setEnabled (true);
		    editIt.setEnabled (true);
		}

		popup.show (SeasonPanel.this, e.getX(), e.getY());

	    }
	}
    }

    class SeasonTreeNodeRenderer 
	extends DefaultTreeCellRenderer {
	
	private static final long serialVersionUID = 20050912150000L;

	public SeasonTreeNodeRenderer ()
	{
	    super ();
	}

	public Component getTreeCellRendererComponent (JTree tree,
						       Object value,
						       boolean sel,
						       boolean expanded,
						       boolean leaf,
						       int row,
						       boolean hasFocus)
	{
	    super.getTreeCellRendererComponent (tree, value, sel, expanded,
						leaf, row, hasFocus);

	    if (leaf && (value instanceof Performance)) {
		Performance p = (Performance) value;
		int rc = p.getRoleCount ();
		int rf = p.getRolesFilled ();
		
		if (rf == 0) {  // No roles cast yet.
		    setIcon (yellowball);
		}

		else if (rf < rc) { // Some, but not all roles cast.
		    setIcon (redball);
		}

		else if (rf == rc) { // All roles cast.
		    setIcon (greenball);
		}

		else { // WTF!!
		    System.err.println ("ERROR: more roles filled than roles cast!");
		}
	    }

	    return this;
	}
    }

    /** Need to call this when we add/delete nodes to/from JTree instance. **/
    public void redraw ()
    {
	tree.updateUI();
    }

    final public String getName () { return "Seasons"; }

}

