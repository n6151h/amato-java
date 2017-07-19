/**
 * $Id: Production.java,v 1.30 2005/09/13 15:15:44 nicks Exp nicks $
 * 
 * Production object manages information about a production, such as the
 * opening and closing dates of the production, the name of the opera,
 * and the score (Schirmer, Ricordi, Boosey-Hawkes, etc.)  It also acts
 * as a container for all of the Performance object instances representing
 * the performances of this production.
 * 
 * Originally part of the AMATO application.
 *
 * @author Nick Seidenman <nick@seidenman.net>
 * @version $Revision: 1.30 $
 *
 * $Log: Production.java,v $
 * Revision 1.30  2005/09/13 15:15:44  nicks
 * "changed" flag is now set for any add or delete operation.
 *
 * Revision 1.29  2005/09/12 17:36:44  nicks
 * If a singer is being cast in more than one role, a pop-up warning
 * is displayed and the user is asked if this is ok.  If it is, the singer
 * is (n-tuple cast).
 *
 * Revision 1.28  2005/09/09 23:26:26  nicks
 * Before loading another db or exiting, changed flag is checked and
 * the user is prompted to save (or not) the current db.
 *
 * Revision 1.27  2005/09/09 22:32:46  nicks
 * Changed all remaining explicit calls to JOptionPanel to Amato.errorPopup.
 *
 * Revision 1.26  2005/09/09 21:24:51  nicks
 * All objects now store date/time in a Date instance.  This gets me
 * ready to be able to do some of the date/time sanity checks mentioned
 * in the TODO list.  (E.g., check to make sure performance date
 * is within the season start/end bounds.)
 *
 * Revision 1.25  2005/09/08 15:37:40  nicks
 * Seasons, Productions, Performances, Constituents, and Operas are now
 * all editable!
 *
 * Revision 1.24  2005/09/07 15:47:08  nicks
 * Now able to edit performances.  Will proceed to add this capability
 * to other Add*Frame.java components.
 *
 * Revision 1.23  2005/08/16 02:08:35  nicks
 * AddPerformanceFrame added.  All the basic elements can now be adde
 * or deleted.  (Now need to add Edit function.)
 *
 * Revision 1.22  2005/08/15 15:35:46  nicks
 * Replaced DateButton with UICDateEdit.
 *
 * Revision 1.21  2005/08/15 14:26:28  nicks
 * Changed as_* to to* (more java-esque)
 *
 * Revision 1.20  2005/08/15 13:51:14  nicks
 * Several mods to support AddPerformanceFrame.
 * Changed as_* to to* (more java-esque)
 *
 * Revision 1.19  2005/08/03 17:42:19  nicks
 * Got alignment working right in  toJPanel.
 *
 * Revision 1.18  2005/07/23 00:30:19  nicks
 * Started adding code to support editable panels.
 * (Just experimenting a little, here.)
 *
 * Revision 1.17  2005/07/22 22:12:39  nicks
 * Started work on Drag-n-Drop within RosterPanel.
 * Cleaned up nearly all warnings.  Only a few left in Opera.java, Operas.java,
 * and Performance.java and these pertain to strict type-checking ala 1.5.
 * Just wanted to check everything in now as a check point.  I'll continue
 * to work on getting a totally clean compilation.
 *
 * Revision 1.16  2005/07/19 18:14:36  nicks
 * Retired PerformerList objects.  Now working on a Population
 * object class that will become the base class for objects
 * that can return lists of constituents according to all
 * sorts of criteria.  Stay tuned ...
 *
 * Revision 1.15  2005/07/17 11:48:35  nicks
 * toJPanel instead of as_JLabel (instead of toHTML).
 *
 * Revision 1.14  2005/07/15 19:27:39  nicks
 * Details are now displayed as JLabels, not html.  (Actually, I'm
 * taking advantage of the new JLabel capability that lets you
 * supply html as the label content.)
 *
 * Revision 1.13  2005/07/14 22:27:40  nicks
 * Replaced println messages with ErrorDialog box.
 *
 * Revision 1.12  2005/07/14 18:21:50  nicks
 * Seasons (and the SeasonPanel.tree structure) are now managed using
 * a TreeModel paradigm.  Works VERY nice.  This is the JAVA way to do things!
 *
 * Revision 1.11  2005/07/12 19:17:30  nicks
 * Add Productions function works.
 *
 * Revision 1.10  2005/06/29 19:51:37  nicks
 * Added conductor(s) to performance(s).
 *
 * Revision 1.9  2005/06/22 18:19:04  nicks
 * Created new container class Performances to hold/manage Performance
 * objects.
 *
 * Revision 1.8  2005/06/20 22:34:38  nicks
 * SeasonPanel is now working in a cheesy, demo-only sort of way.  It's
 * at least enough for me to get the idea across to Irene and talk
 * through how it will work when finished.d
 *
 * Revision 1.7  2005/06/20 18:27:58  nicks
 * Now sorts cast members, production names, performance date/time.
 *
 * Revision 1.6  2005/06/20 18:07:48  nicks
 * A few minor tweaks.
 *
 * Revision 1.5  2005/06/20 17:31:30  nicks
 * SeasonPanel (dummy'd up) now working.
 *
 * Revision 1.4  2005/06/16 15:25:05  nicks
 * Added object_name (HashMap aMap) constructor so that I can simply pass
 * the attribute map from the parser without the parser itself ever
 * needing to know such details as which attributes are required and
 * which are optional.  This is a slicker, cleaner, more OO way to
 * handle XML tag attributes.
 *
 * Revision 1.3  2005/06/15 21:59:42  nicks
 * Added libretto attribute to Production class.
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
import java.text.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.border.*;
import javax.swing.event.*;

import uic.widgets.calendar.*;


public class Production extends DefaultMutableTreeNode
{
    private static final long serialVersionUID = 20050722112850L;

    private Season season;
    private Opera opera;
    private Date openDate;
    private Date closeDate;
    private String libretto;

    private SeasonTreeModel treeModel;

    private UICDateEdit oDateBtn;
    private UICDateEdit cDateBtn;

    public Production (Season season, Operas operas, HashMap aMap)
    {
	super ();

	this.season = season;

	// Need to make sure the opera we're looking for is
	// already listed in the opera database.
	String oName = (String) aMap.get ("name");
	
	if (oName == null) {
	    Amato.errorPopup ("Missing opera name in <production> tag.\n" +
			      "Entry ignored.");
	    this.opera = null;
	    return;
	}
	else {
	    this.opera = operas.getByName(oName);
	    if (this.opera == null) {
		Amato.errorPopup ("Specified opera \"" + oName + 
				  "\" not found in database.\n" +
				  "All <opera> definitions must come " + 
				  "before any <production> definitions.\nIgnored.");
		return;
	    }
	}

	libretto = (String) aMap.get ("libretto");

	openDate = Amato.parseDate ((String) aMap.get ("openDate"));
	closeDate = Amato.parseDate ((String) aMap.get ("closeDate"));

	treeModel = new SeasonTreeModel (this);
    }

    public void add (Performance p)
    {
	int nodeCount = getChildCount ();
	int i = 0;

	while (i < nodeCount) {
	    if (getChildAt (i).toString().compareTo (p.toString()) >= 0) {
		break;
	    }
	    ++i;
	}

	treeModel.insertNodeInto (p, this, i);
    }

    /**
     * Remove the performance from the database.
     *
     * @arg p Performance instance to be removed.
     */
    public void remove (Performance p)
    {
	//super.remove (p);
	for (Cast c: p.getCast()) {
	    System.out.println ("Performance.remove: " + c.getActor());
	    if (c.getActor() != null && c.getActor().isValid()) {
		c.getActor().clearPerformances();
	    }
	}
	treeModel.removeNodeFromParent ((MutableTreeNode) p);
	Amato.db.setChanged(true);
    }

    final public String toXML ()
    {
	String result;
	int nodeCount = getChildCount ();
	int i;

	result = "<production name=\"" + opera.getName() + "\"" +
	    " composer=\"" + opera.getComposer() + "\"" +
	    " libretto=\"" + libretto + "\"" +
	    " openDate=\"" + getOpenDateText () + "\"" +
	    " closeDate=\"" + getCloseDateText () + "\">\n";

	// Emit the performances
	result += "<performances>\n";

	for (i = 0; i < nodeCount; i++) {
	    Performance p = (Performance) getChildAt (i);
	    result += p.toXML ();
	    result += "\n";
	}
	result += "</performances>";

	// Emit the end tag
	result += "\n</production>";
	
	return result;
    }

    final public String toHTML ()
    {
	String result;
	int nodeCount = getChildCount ();
	int i = 0;

	result = "<html><h1>" + opera.getName() + "</h1>\n" +
	    "<h2>" + getOpenDateText () + 
	    " through " + getCloseDateText () + "</h2>\n" +
	    "<hr>\n" + 
	    "<h2>Performances</h2>\n";

	// Emit the performances.
	Roster rost = Amato.db.getRoster ();

	// This is a totally temporary hack.  In the real version this will
	// be replace by a PerformanceTable object that will ... well,
	// it'll do lots more stuff.  NLS
	
	result += "<table border='1'>" +
	    "<tr><th align='left'>Role / Performance</th>";

	for (i = 0; i < nodeCount; i++) {
	    TreeNode tn = getChildAt (i);

	    // Column headings are the date/time of each performance.
	    result += "<th align='left'>" + tn + "</th>";
	}

	result += "</tr>\n";

	for (Role role: opera.getRoles ()) {
	    // Row heading is the name of the role.
	    result += "<tr><td><b>" + role.getName() + "</b></td>";
	    
	    for (i = 0; i < nodeCount; i++) {
		Performance p = (Performance) getChildAt (i);
		Cast cm = p.getCastByRole (role);
		if (cm == null) {
		    result += "<td>Not<br>Cast></td>";
		}
		else {
		    result += "<td>" + cm.getFirstName() + "<br>" + 
			cm.getLastName() + "</td>";
		}
	    }

	    result += "</tr>\n";
	}

	// Add the conductor row.
	result += "<tr><td><b>Conductor</b></td>";
	
	for (i = 0; i < nodeCount; i++) {
	    Performance p = (Performance) getChildAt (i);
	    Conductor cnd = p.getConductor();
	    if (cnd == null)
		result += "<td>TBD</td>";
	    else
		result += "<td>" + cnd.getLastName() + "</td>";
	}
	
	result += "</tr>\n";

	// EOT
	result += "</table></html>";

	return result;
    }

    private JLabel makeColumnHeader (String text)
    {
	JLabel cHead = new JLabel (text);

	cHead.setFont (Amato.BOLD);
	cHead.setBorder (BorderFactory.createLineBorder (Color.BLACK, 1));

	return cHead;
    }

    private JLabel makeRowHeader (String text)
    {
	JLabel rHead = new JLabel (text);

	rHead.setFont (Amato.BOLD);
	rHead.setBorder (BorderFactory.createLineBorder (Color.BLACK, 1));

	return rHead;
    }

    private JLabel makeTableElement (String text)
    {
	JLabel rSlot;

	rSlot = new JLabel (text, JLabel.LEFT);
	rSlot.setFont (Amato.PLAIN);
	rSlot.setBorder (BorderFactory.createLineBorder (Color.BLACK, 1));
		
	return rSlot;
    }

    private JMenu makeMenuTableElement (String text)
    {
	JMenu m = new JMenu(text);
	
	m.add (new PerformerMenuAction (text));
	m.add (new PerformerMenuAction ("TBD"));
	
	return m;
    }

    private JComboBox makeComboBoxTableElement (Object[] c, String current)
    {
	JComboBox te = makeComboBoxTableElement (c);
	int nodeCount = te.getItemCount();
	
	for (int i = 0; i < nodeCount; i++) {
	    if (current.equals (te.getItemAt(i)))
		te.setSelectedIndex (i);
	}
		
	return te;
    }

    private JComboBox makeComboBoxTableElement (Object[] c)
    {
	JComboBox m = new JComboBox();
	
	m.addItem ("Not Cast");

	
	for (int i = 0; i < c.length; i++) {
	    m.addItem (((Constituent) c[i]).getLastName());
	}
	
	return m;
    }

    private class PerformerMenuAction extends AbstractAction 
    {
	private static final long serialVersionUID = 20050720112671L;
	public PerformerMenuAction (String text)
	{
	    super (text);
	}

	public void actionPerformed (ActionEvent e)
	{
	    String performer = e.getActionCommand ();
	}
    }

    public JPanel toJPanel ()
    {
	// Assume non-editable display.
	return toJPanel (false);
    }

    public JPanel toJPanel (boolean isEditable)
    {
	// If isEditable is true, then use editable components.  Otherwise
	// the components should display statically (i.e. uneditable.)

	Roster rost = Amato.db.getRoster();

	// Main Panel 
	JPanel main_panel = new JPanel ();
	main_panel.setLayout (new BoxLayout (main_panel, BoxLayout.Y_AXIS));
	main_panel.setBorder (BorderFactory.createBevelBorder (BevelBorder.RAISED));
	main_panel.setAlignmentX (Component.LEFT_ALIGNMENT);
	main_panel.setAlignmentY (Component.TOP_ALIGNMENT);

	//  General info in the top half, schedule in the bottom.

	// Production (Opera) Name ...
	JPanel prod_info = new JPanel ();
	prod_info.setLayout (new BoxLayout (prod_info, BoxLayout.Y_AXIS));
	prod_info.setAlignmentX (Component.LEFT_ALIGNMENT);
	prod_info.setAlignmentY (Component.TOP_ALIGNMENT);
	prod_info.setBorder (BorderFactory.createEmptyBorder (5, 5, 5, 5));

	JLabel pName = new JLabel (opera.getName(), JLabel.LEFT);
	pName.setFont (Amato.H1);
	pName.setAlignmentX (Component.LEFT_ALIGNMENT);
	pName.setAlignmentY (Component.TOP_ALIGNMENT);
	prod_info.add (pName);
	
	// Opening date ...
	JPanel oPane = new JPanel ();
	oPane.setLayout (new BoxLayout (oPane, BoxLayout.X_AXIS));
	oPane.setAlignmentX (Component.LEFT_ALIGNMENT);
	oPane.setAlignmentY (Component.TOP_ALIGNMENT);

	JLabel oLabel = new JLabel ("Opens: ", JLabel.LEFT);
	oLabel.setFont (Amato.H2);
	oLabel.setAlignmentX (Component.LEFT_ALIGNMENT);
	oPane.add (oLabel);
	if (isEditable) {
	    Calendar oCal = Calendar.getInstance ();
	    oCal.setTime (openDate);
	    oDateBtn = new UICDateEdit (Locale.US, oCal);
	    oDateBtn.setFont (Amato.H2);
	    oDateBtn.setAlignmentX (Component.LEFT_ALIGNMENT);
	    oPane.add (oDateBtn);
	}
	else {
	    JLabel oDateLabel = new JLabel (Amato.getDateText (openDate), 
					    JLabel.LEFT);
	    oDateLabel.setFont (Amato.H2);
	    oPane.add (oDateLabel);
	}
	prod_info.add (oPane);
	
	// Closing date ...
	JPanel cPane = new JPanel (new FlowLayout());
	cPane.setLayout (new BoxLayout (cPane, BoxLayout.X_AXIS));
	cPane.setAlignmentX (Component.LEFT_ALIGNMENT);
	cPane.setAlignmentY (Component.TOP_ALIGNMENT);
	JLabel cLabel = new JLabel ("Closes: ", JLabel.LEFT);
	cLabel.setFont (Amato.H2);
	cPane.add (cLabel);
	if (isEditable) {
	    Calendar cCal = Calendar.getInstance();
	    cCal.setTime (closeDate);
	    cDateBtn = new UICDateEdit (Locale.US, cCal);
	    cDateBtn.setFont (Amato.H2);
	    cPane.add (cDateBtn);
	}
	else {
	    JLabel cDateLabel = new JLabel (Amato.getDateText (closeDate),
					    JLabel.LEFT);
	    cDateLabel.setFont (Amato.H2);
	    cPane.add (cDateLabel);
	}
	prod_info.add (cPane);

	// Performance schedule.
	// -- This will be an Pn+1 x Rn+2 matrix where Pn is the number
	//    of performances and Rn is the number of roles.  The additions
	//    account for the column/row headings and for the conductor
	//    listing (Rn+2).
	JPanel prod_sched = new JPanel ();
	prod_sched.setBorder (BorderFactory.createEmptyBorder (5, 5, 5, 5));
	prod_sched.setAlignmentX (Component.LEFT_ALIGNMENT);
	prod_sched.setAlignmentY (Component.TOP_ALIGNMENT);

	int Pn = getChildCount ();
	int Rn = opera.getRoleCount ();

	JPanel perf_mx = new JPanel (new GridLayout (Rn+2, Pn+1));

	// If we don't do this check, we'll get an empty box
	// above the first role when there are no performances.
	// Better to simply not have it there at all if we can help it.
	if (Pn > 0)
	    perf_mx.add (makeColumnHeader (""));  // Empty upper-left corner.

	for (int i = 0; i < Pn; i++) {
	    Performance tn = (Performance) getChildAt (i);
	    
	    // Column headings are the date/time of each performance.
	    perf_mx.add (makeColumnHeader ("<html>" + tn.getDateText() + "<br>" + 
				       tn.getTimeText() + "</html>"));
	}

	// Iterate through the performance matrix in row-major order,
	// filling in the last name of the singer for each role for
	// each date.
	for (Role role: opera.getRoles()) {
	    // Row heading is the name of the role.
	    perf_mx.add (makeRowHeader (role.getName()));
		     
	    for (int i = 0; i < Pn; i++) {
		Performance p = (Performance) getChildAt (i);
		Cast cm = p.getCastByRole (role);
		String currentCast;

		Object[] c = rost.getVoice (role.getVoice ());

		if (cm != null) {
		    currentCast = cm.getFirstName() + "<br>" + cm.getLastName();
		}
		else {
		    currentCast = "Not<br>Cast";
		}

		if (isEditable) {
		    perf_mx.add (makeComboBoxTableElement (c, currentCast));
		}
		else {
		    perf_mx.add (makeTableElement ("<html>" + currentCast + "</html>"));
		}
	    }
	}

	// Add the conductor row.
	perf_mx.add (makeRowHeader ("Conductor"));

	for (int i = 0; i < Pn; i++) {
	    Performance p = (Performance) getChildAt (i);
	    Conductor cnd = p.getConductor();
	    if (cnd == null) {
		if (isEditable) {
		    perf_mx.add (makeComboBoxTableElement (rost.getConductors()));
		}
		else {
		    perf_mx.add (makeTableElement ("TBA"));
		}
	    }
	    else {
		if (isEditable) {
		    perf_mx.add (makeComboBoxTableElement (rost.getConductors(), 
						       cnd.getLastName()));
		}
		else {
		    perf_mx.add (makeTableElement (cnd.getLastName()));
		}
	    }
	}

	prod_sched.add (perf_mx);

	// Put it all together.
	main_panel.add (prod_info);
	main_panel.add (prod_sched);

	return main_panel;
    }

    final public String getName () { return opera.getName(); }

    final public String getLibretto () { return libretto; }

    final public Date getOpenDate () { return openDate; }

    final public String getOpenDateText () { return Amato.getDateText (openDate); }

    final public Date getCloseDate () { return closeDate; }

    final public String getCloseDateText () { return Amato.getDateText (closeDate); }

    final public Season getSeason () { return season; }

    public void setSeason (Season s) { this.season = s; }

    public Opera getOpera () {return opera; }

    public void setOpera (Opera o) { opera = o; }

    final public String toString () { return opera.getName(); }

    final public boolean isLeaf () { return false; }

}
