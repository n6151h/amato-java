/**
 * $Id: Season.java,v 1.17 2005/09/12 17:36:44 nicks Exp $
 * 
 * Originally part of the AMATO application.
 * 
 * @author Nick Seidenman <nick@seidenman.net>
 * @version $Revision: 1.17 $
 *
 * $Log: Season.java,v $
 * Revision 1.17  2005/09/12 17:36:44  nicks
 * If a singer is being cast in more than one role, a pop-up warning
 * is displayed and the user is asked if this is ok.  If it is, the singer
 * is (n-tuple cast).
 *
 * Revision 1.16  2005/09/09 21:24:51  nicks
 * All objects now store date/time in a Date instance.  This gets me
 * ready to be able to do some of the date/time sanity checks mentioned
 * in the TODO list.  (E.g., check to make sure performance date
 * is within the season start/end bounds.)
 *
 * Revision 1.15  2005/09/08 15:37:40  nicks
 * Seasons, Productions, Performances, Constituents, and Operas are now
 * all editable!
 *
 * Revision 1.14  2005/08/21 19:06:23  nicks
 * Changed comment header to conform with javadoc conventions.
 *
 * Revision 1.13  2005/08/15 14:26:29  nicks
 * Changed as_* to to* (more java-esque)
 *
 * Revision 1.12  2005/07/22 22:12:39  nicks
 * Started work on Drag-n-Drop within RosterPanel.
 * Cleaned up nearly all warnings.  Only a few left in Opera.java, Operas.java,
 * and Performance.java and these pertain to strict type-checking ala 1.5.
 * Just wanted to check everything in now as a check point.  I'll continue
 * to work on getting a totally clean compilation.
 *
 * Revision 1.11  2005/07/17 11:48:35  nicks
 * toJPanel instead of as_JLabel (instead of toHTML).
 *
 * Revision 1.10  2005/07/15 19:27:39  nicks
 * Details are now displayed as JLabels, not html.  (Actually, I'm
 * taking advantage of the new JLabel capability that lets you
 * supply html as the label content.)
 *
 * Revision 1.9  2005/07/14 18:21:50  nicks
 * Seasons (and the SeasonPanel.tree structure) are now managed using
 * a TreeModel paradigm.  Works VERY nice.  This is the JAVA way to do things!
 *
 * Revision 1.8  2005/07/12 16:20:51  nicks
 * Added toString method.
 *
 * Revision 1.7  2005/06/20 22:34:38  nicks
 * SeasonPanel is now working in a cheesy, demo-only sort of way.  It's
 * at least enough for me to get the idea across to Irene and talk
 * through how it will work when finished.d
 *
 * Revision 1.6  2005/06/20 18:27:58  nicks
 * Now sorts cast members, production names, performance date/time.
 *
 * Revision 1.5  2005/06/20 17:31:30  nicks
 * SeasonPanel (dummy'd up) now working.
 *
 * Revision 1.4  2005/06/18 04:19:02  nicks
 * Have OperaPanel and RosterPanel (cheesy versions) working.
 * This should be suitable for demo to Irene sometime soon.
 * I just want to more or less dummy up a SeasonPanel and I'll
 * be ready to demo.
 *
 * Revision 1.3  2005/06/16 15:25:05  nicks
 * Added object_name (HashMap aMap) constructor so that I can simply pass
 * the attribute map from the parser without the parser itself ever
 * needing to know such details as which attributes are required and
 * which are optional.  This is a slicker, cleaner, more OO way to
 * handle XML tag attributes.
 *
 * Revision 1.2  2005/06/15 20:47:04  nicks
 * Everything (but Roster) is now working fine.  I can digest an AmatoBase,
 * spit it back out, digest it again, spit it out again, and the third
 * gen matches the second gen.
 *
 * Revision 1.1  2005/06/15 14:51:08  nicks
 * Initial revision
 *
 */


import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;

public class Season extends DefaultMutableTreeNode
{
    private static final long serialVersionUID = 20050722112880L;

    private String name;
    private Date startDate;
    private Date endDate;
    private Seasons seasons;
    private SeasonTreeModel treeModel;

    public Season (HashMap aMap)
    {
	super ();

	name = (String) aMap.get ("name");
	startDate = Amato.parseDate ((String) aMap.get ("startDate"));
	endDate = Amato.parseDate ((String) aMap.get ("endDate"));
	
	treeModel = new SeasonTreeModel (this);
    }

    public void add (Production p)
    {
	int nodeCount = getChildCount ();
	int i = 0;

	while (i < nodeCount) {
	    if (getChildAt (i).toString().compareTo (p.toString()) >= 0) {
		break;
	    }
	    ++i;
	}
	
	p.getOpera().add_prod (p);
	treeModel.insertNodeInto (p, this, i);
    }

    public String toXML ()
    {
	String result;
	int nodeCount = getChildCount();
	
	result = "<season name=\"" + name + "\"" +
	    " startDate=\"" + Amato.getDateText (startDate) + "\"" +
	    " endDate=\"" + Amato.getDateText (endDate) + "\">\n";

	// Emit the productions;
	result += "<productions>\n";
	for (int i = 0; i < nodeCount; i++) {
	    Production p = (Production) getChildAt (i);
	    result += p.toXML ();
	    result += "\n";
	}
	result += "</productions>";

	// Emit the end tag
	result += "\n</season>";
	
	return result;
    }

    public String toHTML ()
    {
	String result;
	int nodeCount = getChildCount();
	
	result = "<html><h1>" + name + "</h1>" +
	    "<h2>" + 
	    Amato.getDateText (startDate) + " - " + 
	    Amato.getDateText (endDate) + "</h2>\n" +
	    "<hr>\n" + 
	    "<h2>Productions</h2>";

	// Emit the productions;
	result += "<ul>\n";
	for (int i = 0; i < nodeCount; i++) {
	    //System.out.println ("Season.toHTML: i=" + i);
	    TreeNode tn = getChildAt (i);
	    //System.out.println ("Season.toHTML: tn=" + tn);
	    result += "<li>" + tn.toString() + "</li>";
	}
	result += "</ul></html>";

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

    final public String getName () { return name; }

    final public String getStartDateText () { return Amato.getDateText (startDate); }

    final public String getEndDateText () { return Amato.getDateText (endDate); }

    final public Date getStartDate () { return startDate; }

    final public Date getEndDate () { return endDate; }

    final public boolean isLeaf () { return false; }

    final public String toString () { return name; }
}

