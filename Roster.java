/**
 * $Id: Roster.java,v 1.19 2005/09/13 15:15:44 nicks Exp nicks $
 *
 * Container class form maintaining information about singers, conductors,
 * directors and other production participants.
 * 
 * Originally part of the AMATO application.
 * 
 * @author Nick Seidenman <nick@seidenman.net>
 * @version $Revision: 1.19 $
 *
 * $Log: Roster.java,v $
 * Revision 1.19  2005/09/13 15:15:44  nicks
 * "changed" flag is now set for any add or delete operation.
 *
 * Revision 1.18  2005/09/07 18:02:00  nicks
 * Got rid of clueless nullArray construct.
 *
 * Revision 1.17  2005/08/21 19:28:03  nicks
 * Added conductor selector combobox.
 *
 * Revision 1.16  2005/08/15 14:26:28  nicks
 * Changed as_* to to* (more java-esque)
 *
 * Revision 1.15  2005/08/15 13:52:19  nicks
 * Several mods to support DnD, AddPerformanceFrame.
 * Changed as_* to to* (more java-esque)
 *
 * Revision 1.14  2005/08/08 19:09:53  nicks
 * Comments modified to comply with javadoc stds.
 *
 * Revision 1.13  2005/08/03 18:33:53  nicks
 * Now able to add Singers, Musicians, Conductors, Directors, Staff on RosterPanel pop-up menu.
 *
 * Revision 1.12  2005/07/22 22:12:39  nicks
 * Started work on Drag-n-Drop within RosterPanel.
 * Cleaned up nearly all warnings.  Only a few left in Opera.java, Operas.java,
 * and Performance.java and these pertain to strict type-checking ala 1.5.
 * Just wanted to check everything in now as a check point.  I'll continue
 * to work on getting a totally clean compilation.
 *
 * Revision 1.11  2005/07/19 18:14:36  nicks
 * Retired PerformerList objects.  Now working on a Population
 * object class that will become the base class for objects
 * that can return lists of constituents according to all
 * sorts of criteria.  Stay tuned ...
 *
 * Revision 1.10  2005/07/18 03:26:43  nicks
 * Save function was broken due to re-wiring of the Roster tree.
 * Fixed.
 *
 * Revision 1.9  2005/07/17 11:48:35  nicks
 * toJPanel instead of as_JLabel (instead of toHTML).
 *
 * Revision 1.8  2005/06/30 15:39:39  nicks
 * Added serialVersionUID to keep the 5.0 compiler happy.
 * (sheesh!)
 *
 * Revision 1.7  2005/06/29 19:51:37  nicks
 * Added conductor(s) to performance(s).
 *
 * Revision 1.6  2005/06/27 20:55:45  nicks
 * voice attribute of Singer is now an ArrayList.  This lets one define a
 * singer who can sing a range of voices, not just one.  For example,
 * Some sopranos can also sing mezzo, or some baritones can also do (low)
 * tenor roles.  Voices are specified as a comma-separated list.
 *
 * Revision 1.5  2005/06/25 19:07:17  nicks
 * Fixed a nullpointer situation that would arise when the role
 * called for a voice for which there were no singers in the roster.
 *
 * Revision 1.4  2005/06/23 15:22:39  nicks
 * Cleaned up a few minor problems.
 *
 * Revision 1.3  2005/06/22 21:41:53  nicks
 * Cheap, demo version of cast selection working. (Will use real
 * java stuff, not this crappy html vaporware.)
 * Had to make the AmatoDB object globally visible for this
 * sort of thing to work.  I hate doing it, but I guess there's no
 * harm for the moment since this is a self-contained application.
 *
 * Revision 1.2  2005/06/18 04:19:02  nicks
 * Have OperaPanel and RosterPanel (cheesy versions) working.
 * This should be suitable for demo to Irene sometime soon.
 * I just want to more or less dummy up a SeasonPanel and I'll
 * be ready to demo.
 *
 * Revision 1.1  2005/06/15 14:51:08  nicks
 * Initial revision
 *
 */


import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;

public class Roster 
    extends DefaultMutableTreeNode
{
    private static final long serialVersionUID = 20050630112720L;

    private DefaultTreeModel treeModel;

    private DefaultMutableTreeNode singers = null;
    private DefaultMutableTreeNode conductors = null;
    private DefaultMutableTreeNode musicians = null;
    private DefaultMutableTreeNode directors = null;
    private DefaultMutableTreeNode staff = null;

    public Roster ()
    {
	super ("Roster");

	treeModel = new DefaultTreeModel (this);

	singers = new DefaultMutableTreeNode ("Singers");
	conductors = new DefaultMutableTreeNode ("Conductors");
	musicians = new DefaultMutableTreeNode ("Musicians");
	directors = new DefaultMutableTreeNode ("Directors");
	staff = new DefaultMutableTreeNode ("Staff");

	treeModel.insertNodeInto (staff, this, 0); 
	treeModel.insertNodeInto (directors, this, 0); 
	treeModel.insertNodeInto (musicians, this, 0); 
	treeModel.insertNodeInto (conductors, this, 0); 
	treeModel.insertNodeInto (singers, this, 0); 
    }

    public void add (Constituent c)
    {

	int i = 0;
	int[] j = new int[1];
	DefaultMutableTreeNode subtree = determineSubtree (c);
	int nodeCount = subtree.getChildCount ();

	while (i < nodeCount) {
	    Constituent kid = (Constituent) subtree.getChildAt (i);
	    //System.out.println ("Roster.add: kid(" + i + "): " + kid);
	    if (kid.toString().compareTo (c.toString()) >= 0) {
		break;
	    }
	    ++i;
	}

	treeModel.insertNodeInto (c, subtree, i);
	if (Amato.db != null) Amato.db.setChanged(true);
    }

    public void remove (Constituent c)
    {
	for (Performance p: c.getPerformances()) {
	    p.removeCast (c);
	}
	c.setValid (false);

	treeModel.removeNodeFromParent ((MutableTreeNode) c);
	
	Amato.db.setChanged(true);
    }

    private DefaultMutableTreeNode determineSubtree (Constituent c)
    {
	if (c instanceof Singer)
	    return singers;

	else if (c instanceof Musician)
	    return musicians;

	else if (c instanceof Director)
	    return directors;

	else if (c instanceof Conductor)
	    return conductors;

	else 
	    return staff;
    }

    public String toXML ()
    {
	String result;
	int nodeCount = getChildCount();

	result = "<roster>\n";

	// Emit the constituents
	// This will work for now.  If we go with more levels within 
	// the tree (e.g. voice subtree under singers) then this will 
	// have to be rewritten.
	for (int i = 0; i < nodeCount; i++) {
	    DefaultMutableTreeNode tn = (DefaultMutableTreeNode) getChildAt (i);
	    int subtreeNodeCount = tn.getChildCount();
	    for (int j = 0; j < subtreeNodeCount; j++) {
		DefaultMutableTreeNode tn2 = (DefaultMutableTreeNode) tn.getChildAt (j);
		Constituent c = (Constituent) tn2;
		result += c.toXML ();
		result += "\n";
	    }
	}

	// Emit the end tag
	result += "</roster>";
	
	return result;
    }

    final public String toHTML ()
    {
	String result = "<html><h1>Roster</h1>\n<hr>\n</html>";
	
	return result;
    }

    public JPanel toJPanel ()
    {
	JPanel p = new JPanel (new FlowLayout ());
	p.setBorder (BorderFactory.createEmptyBorder (10, 10, 10, 10));
	JLabel l = new JLabel (toHTML(), JLabel.LEFT);
	
	p.add (l);

	p.setBorder (BorderFactory.createTitledBorder (toString()));
	return p;
    }

    final public JLabel getSubtreeJLabel (String stName)
    {
	JLabel l = new JLabel ("<html><h1>" + stName + "</h1><hr></html>", 
			       JLabel.CENTER);
	
	l.setBorder (BorderFactory.createTitledBorder (stName));
	return l;
    }

    final public Object[] findCastMember (Cast c) 
    {
	return findByName (c.getFirstName(), c.getLastName());
    }
    
    final public Object[] getVoice (String voice)
    {
	Vector<Singer> v = new Vector<Singer>();
	int i = 0;
	int nodeCount = 0;

	//	System.out.println ("voice: " + voice);
	nodeCount = singers.getChildCount ();

	for (i = 0; i < nodeCount; i++) {
	    Singer s = (Singer) singers.getChildAt (i);

	    if (s.hasVoice (voice)) {
		v.add (s);
	    }
	}
	
	return (Object[]) v.toArray();
    }

    final public Object[] getConductors ()
    {
	Vector<Conductor> v = new Vector<Conductor>();
	int i = 0;
	int nodeCount = 0;
	
	nodeCount = conductors.getChildCount ();
	for (i = 0; i < nodeCount; i++) {
	    v.add ((Conductor) conductors.getChildAt (i));
	}

	return v.toArray();
    }


    final public DefaultComboBoxModel createConductorComboBoxModel ()
    {
	DefaultComboBoxModel ccbm = new DefaultComboBoxModel (getConductors());
	String tba = new String ("TBA");

	ccbm.addElement (tba);
	ccbm.setSelectedItem (tba);
	return ccbm;
    }

    final public Object[] findByName (String lastName)
    {
	Vector<Constituent> v = new Vector<Constituent>();
	int i = 0;
	int nodeCount = 0;

	// Return a zero-length array.
	if (lastName == null) return new Object[0];

	nodeCount = singers.getChildCount ();
	for (i = 0; i < nodeCount; i++) {
	    Constituent m = (Constituent) singers.getChildAt (i);

	    if (lastName.equals (m.getLastName())) {
		v.add (m);
	    }
	}

	nodeCount = musicians.getChildCount ();
	for (i = 0; i < nodeCount; i++) {
	    Constituent m = (Constituent) musicians.getChildAt (i);

	    if (lastName.equals (m.getLastName())) {
		v.add (m);
	    }
	}

	nodeCount = conductors.getChildCount ();
	for (i = 0; i < nodeCount; i++) {
	    Constituent m = (Constituent) conductors.getChildAt (i);

	    if (lastName.equals (m.getLastName())) {
		v.add (m);
	    }
	}

	nodeCount = directors.getChildCount ();
	for (i = 0; i < nodeCount; i++) {
	    Constituent m = (Constituent) directors.getChildAt (i);

	    if (lastName.equals (m.getLastName())) {
		v.add (m);
	    }
	}

	nodeCount = staff.getChildCount ();
	for (i = 0; i < nodeCount; i++) {
	    Constituent m = (Constituent) staff.getChildAt (i);

	    if (lastName.equals (m.getLastName())) {
		v.add (m);
	    }
	}

	return v.toArray();
    }

    final public Object[] findByName (String firstName, String lastName)
    {
	Object[] cArray = findByName (lastName);
	Vector<Constituent> v = new Vector<Constituent>();

	for (int i = 0; i < cArray.length; i++ ) {
	    Constituent m = (Constituent) cArray[i];

	    if (lastName.equals (m.getLastName()) && 
		firstName.equals (m.getFirstName())) {
		v.add (m);
	    }
	}

	return v.toArray ();
    }

    public void addTreeModelListener (TreeModelListener tml)
    {
	treeModel.addTreeModelListener (tml);
    }

    final public boolean isLeaf() { return false; }

    public DefaultMutableTreeNode getSingers () { return singers; }
}

