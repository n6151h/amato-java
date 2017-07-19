/**
 * $Id: Seasons.java,v 1.8 2005/08/21 19:06:23 nicks Exp nicks $
 * 
 * Container class for Season objects.
 *
 * Originally part of the AMATO application.
 * 
 * Nick Seidenman <nick@seidenman.net>
 *
 * $Log: Seasons.java,v $
 * Revision 1.8  2005/08/21 19:06:23  nicks
 * Changed comment header to conform with javadoc conventions.
 *
 * Revision 1.7  2005/08/15 14:26:29  nicks
 * Changed as_* to to* (more java-esque)
 *
 * Revision 1.6  2005/07/17 11:48:35  nicks
 * toJPanel instead of as_JLabel (instead of toHTML).
 *
 * Revision 1.5  2005/07/15 19:27:39  nicks
 * Details are now displayed as JLabels, not html.  (Actually, I'm
 * taking advantage of the new JLabel capability that lets you
 * supply html as the label content.)
 *
 * Revision 1.4  2005/07/14 18:21:50  nicks
 * Seasons (and the SeasonPanel.tree structure) are now managed using
 * a TreeModel paradigm.  Works VERY nice.  This is the JAVA way to do things!
 *
 * Revision 1.3  2005/06/30 15:39:39  nicks
 * Added serialVersionUID to keep the 5.0 compiler happy.
 * (sheesh!)
 *
 * Revision 1.2  2005/06/18 04:19:02  nicks
 * Have OperaPanel and RosterPanel (cheesy versions) working.
 * This should be suitable for demo to Irene sometime soon.
 * I just want to more or less dummy up a SeasonPanel and I'll
 * be ready to demo.
 *
 * Revision 1.1  2005/06/15 20:22:57  nicks
 * Initial revision
 *
 */

import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;

public class Seasons extends DefaultMutableTreeNode
{
    private static final long serialVersionUID = 20050630112730L;

    private SeasonTreeModel treeModel;

    public Seasons ()
    {
	super ("Seasons");
	
	treeModel = new SeasonTreeModel (this);
    }

    public String toXML ()
    {
	String result = new String ("<seasons>\n");
	int nodeCount = getChildCount ();

	for (int i = 0; i < nodeCount; i++) {
	    Season s = (Season) getChildAt (i);
	    result += s.toXML ();
	    result += "\n";
	}
	
	result += "</seasons>";

	return result;
    }

    final public String toHTML ()
    {
	String result = "<html><h1>Seasons</h1>\n<hr>\n</html>";
	
	return result;
    }

    public JPanel toJPanel ()
    {
	JPanel p = new JPanel ();
	JLabel l = new JLabel (toHTML(), JLabel.CENTER);
	
	p.add (l);
	p.setBorder (BorderFactory.createTitledBorder (toString()));

	return p;
    }

    public void add (Season s)
    {

	int nodeCount = getChildCount ();
	int i = 0;
	int[] j = new int[1];

	while (i < nodeCount) {
	    if (getChildAt (i).toString().compareTo (s.toString()) >= 0) {
		break;
	    }
	    ++i;
	}

	treeModel.insertNodeInto (s, this, i);
    }

    final public Object[] toArray ()
    {
	int nodeCount = getChildCount ();
	Object[] result = new Object[nodeCount];

	for (int i = 0; i < nodeCount; i++) {
	    result[i] = getChildAt (i);
	}
	
	return result;
    }

    public void addTreeModelListener (TreeModelListener tml)
    {
	treeModel.addTreeModelListener (tml);
    }
    
    public ArrayList<Performance> getPerformances () 
    {
	ArrayList<Performance> pList = new ArrayList<Performance>(200);
	
	Enumeration pEnum = preorderEnumeration();
	Object obj = null;
	while (pEnum.hasMoreElements()) {
	    obj = pEnum.nextElement ();
	    if (obj instanceof Performance) {
		pList.add ((Performance) obj);
	    }
	}

	return pList;
    }
	    
}
    
