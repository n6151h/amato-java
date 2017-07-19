/**
 * $Id: Performance.java,v 1.27 2005/09/12 21:25:04 nicks Exp nicks $
 * 
 * Originally part of the AMATO application.
 * 
 * @author Nick Seidenman <nick@seidenman.net>
 * @version $Revision: 1.27 $
 *
 * $Log: Performance.java,v $
 * Revision 1.27  2005/09/12 21:25:04  nicks
 * Performances listed under productions in the season panel are now
 * color-coded to indicate the level of casting.  A yellow dot indicates
 * the performance is not cast at all, a red dot indicates that one or
 * more (but not all) roles have been cast, and a green dot indicates
 * that all roles have been cast.  Thanks to Bengi Mayone for this
 * idea.
 *
 * Revision 1.26  2005/09/12 17:36:44  nicks
 * If a singer is being cast in more than one role, a pop-up warning
 * is displayed and the user is asked if this is ok.  If it is, the singer
 * is (n-tuple cast).
 *
 * Revision 1.25  2005/09/09 22:32:46  nicks
 * Changed all remaining explicit calls to JOptionPanel to Amato.errorPopup.
 *
 * Revision 1.24  2005/09/09 21:24:51  nicks
 * All objects now store date/time in a Date instance.  This gets me
 * ready to be able to do some of the date/time sanity checks mentioned
 * in the TODO list.  (E.g., check to make sure performance date
 * is within the season start/end bounds.)
 *
 * Revision 1.23  2005/09/08 15:37:40  nicks
 * Seasons, Productions, Performances, Constituents, and Operas are now
 * all editable!
 *
 * Revision 1.22  2005/09/07 15:47:08  nicks
 * Now able to edit performances.  Will proceed to add this capability
 * to other Add*Frame.java components.
 *
 * Revision 1.21  2005/08/16 02:08:35  nicks
 * AddPerformanceFrame added.  All the basic elements can now be adde
 * or deleted.  (Now need to add Edit function.)
 *
 * Revision 1.20  2005/08/15 14:26:28  nicks
 * Changed as_* to to* (more java-esque)
 *
 * Revision 1.19  2005/08/15 13:49:50  nicks
 * Several mods to support AddPerformanceFrame.
 * Changed as_* to to*.  (more java-esque)
 *
 * Revision 1.18  2005/07/22 22:26:54  nicks
 * CLEAN COMPILATION w/ 1.5 compiler (javac -Xlint *.java)!
 *
 * Revision 1.17  2005/07/22 22:12:39  nicks
 * Started work on Drag-n-Drop within RosterPanel.
 * Cleaned up nearly all warnings.  Only a few left in Opera.java, Operas.java,
 * and Performance.java and these pertain to strict type-checking ala 1.5.
 * Just wanted to check everything in now as a check point.  I'll continue
 * to work on getting a totally clean compilation.
 *
 * Revision 1.16  2005/07/17 11:48:35  nicks
 * toJPanel instead of as_JLabel (instead of toHTML).
 *
 * Revision 1.15  2005/07/15 19:27:39  nicks
 * Details are now displayed as JLabels, not html.  (Actually, I'm
 * taking advantage of the new JLabel capability that lets you
 * supply html as the label content.)
 *
 * Revision 1.14  2005/07/14 22:27:40  nicks
 * Replaced println messages with ErrorDialog box.
 *
 * Revision 1.13  2005/07/14 18:21:50  nicks
 * Seasons (and the SeasonPanel.tree structure) are now managed using
 * a TreeModel paradigm.  Works VERY nice.  This is the JAVA way to do things!
 *
 * Revision 1.12  2005/07/06 20:53:30  nicks
 * Can now add a season to the SeasonPanel!
 *
 * Revision 1.11  2005/06/29 19:51:37  nicks
 * Added conductor(s) to performance(s).
 *
 * Revision 1.10  2005/06/27 21:50:25  nicks
 * Roles that are not cast now show up as Not Cast, rather than being
 * omitted altogether.
 *
 * Revision 1.9  2005/06/23 15:22:39  nicks
 * Cleaned up a few minor problems.
 *
 * Revision 1.8  2005/06/20 22:54:42  nicks
 * Needed to call getRole.getName() now that we're referencing objects
 * in the tree directly.
 * Changed the look of the leaf toHTML.  If the cast member is a cover,
 * (cover) appears next to their name, rather than to have a "call"
 * column.
 *
 * Revision 1.7  2005/06/20 22:34:38  nicks
 * SeasonPanel is now working in a cheesy, demo-only sort of way.  It's
 * at least enough for me to get the idea across to Irene and talk
 * through how it will work when finished.d
 *
 * Revision 1.6  2005/06/20 18:27:58  nicks
 * Now sorts cast members, production names, performance date/time.
 *
 * Revision 1.5  2005/06/20 18:07:48  nicks
 * A few minor tweaks.
 *
 * Revision 1.4  2005/06/20 17:31:30  nicks
 * SeasonPanel (dummy'd up) now working.
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
import java.text.*;

import javax.swing.*;
import javax.swing.tree.*;

public class Performance extends DefaultMutableTreeNode
{
    private static final long serialVersionUID = 20050722112810L;

    private Production prod;
    private Date date;
    private Conductor conductor;
    private String venue;
    private ArrayList<Cast> cast;
    private ArrayList<Cast> covers;
    private HashMap<String,Cast> castMap;
    private HashMap<String,Cast> coverMap;
    private Roster roster;

    private SeasonTreeModel treeModel;

    public Performance (Production prod, Roster roster, HashMap<String,String> aMap)
    {
	super ();

	this.prod = prod;
	this.roster = roster;
	
	// Digest the date.
	date = Amato.parseDateTime (aMap.get ("date"));

	venue = (String) aMap.get ("venue");
	cast = new ArrayList<Cast> ();
	covers = new ArrayList<Cast> ();
	castMap = new HashMap<String,Cast> ();
	coverMap = new HashMap<String,Cast> ();
	// Look up the conductor by last name in the roster.
	String cond = (String) aMap.get ("conductor");
	
	Object[] conductors = roster.findByName (cond);

	if (cond != null) {
	    if (conductors.length == 0) {
		Amato.errorPopup ("Conductor \"" + cond + 
				  "\"\n isn't on the roster. Ignored.");
	    }
	    else {
		// XXX This is a hack -- need to account for multiple 
		// XXX conductors with same last name!  NLS
		conductor = (Conductor) conductors[0];
	    }
	}
	else
	    conductor = null;
	
	treeModel = new SeasonTreeModel (this);
    }

    public void addCast (Cast c)
    {
	//Object[] cArray = roster.findCastMember (c);
	Constituent cnst = c.getActor ();
	if (cnst == null || !cnst.isValid()) {
	    Amato.errorPopup (c.getFirstName() + " " +
			      c.getLastName() + 
			      "\nisn't on the roster. Ignored.");
	    return;
	}
	
	if (c.isCover()) {
	    covers.add (c);
	    coverMap.put (c.getRole().getName(), c);
	}
	else {
	    cast.add (c);
	    castMap.put (c.getRole().getName(), c);
	}
	    
	cnst.addPerformance (this);

	sort ();
    }

    public void removeCast (Constituent c)
    {
	Cast found = null;

	for (Cast cm: cast) {
	    System.out.println ("remove_cast: " + cm);
	    if (cm.getActor().equals (c)) {
		found = cm;
	    }
	}

	if (found == null) return;
	System.out.println ("removing: " + found);
	cast.remove (found);
	castMap.remove (found.getRole().getName());
    }

    final public String getVenue ()
    {
	return venue;
    }

    final public String toXML ()
    {
	String result;
	
	result = "<performance " +
	    " date=\"" + getDateTimeText() + "\"";
	if (venue != null) result += " venue=\"" + venue + "\"";
	if (conductor != null) result += " conductor=\"" + 
	    conductor.getLastName() + "\"";
	result += ">\n";

	// Emit the performers.
	for (Cast c: cast) {
	    result += c.toXML ();
	    result += "\n";
	}

	// Emit the end tag
	result += "</performance>";
	
	return result;
    }

    final public String toHTML ()
    {
	String result;
	Opera opera = prod.getOpera ();
	
	result = "<html><h1>" + opera.getName() + "</h1>\n" +
	    "<h2>" + getDateText() + " at " + getTimeText() + "</h2>\n";
	if (venue != null) 
	    result += "<h2>Venue: " +  venue + "</h2>\n";

	if (conductor != null) 
	    result += "<h2>Conductor: " +  
		conductor.getFirstName() + " " +
		conductor.getLastName() + "</h2>\n";
	else 
	    result += "<h2>Conductor: TBD</h2>\n";

	result += "<hr>\n";

	// Emit the roles.
	result += "<h2>Cast:</h2>\n<table>\n<tr><th align='left'>Role</th><th align='left'>Singer</th></tr>\n";

	for (Role r: opera.getRoles ()) {
	    String s = r.getName ();
	    Cast c = (Cast) castMap.get (s);
	    Constituent cnst = (c == null) ? null : c.getActor();
	    result += "<tr><td>" + s + "</td><td>";

	    if (c != null && cnst != null && cnst.isValid()) {
		result += c.getFirstName () + " " + c.getLastName();
		result += (c.isCover() ? " (cover)" : "");
	    }
	    else {
		if (cnst != null) { // cnst isn't valid.  Drop it from the cast.
		    castMap.remove (s);
		    cast.remove (c);
		}
		result += "Not Cast";
	    }
	    result += "</td></tr>\n";
	}
	result += "</table></html>";

	return result; 
    }

    public JPanel toJPanel ()
    {
	JLabel l = new JLabel (toHTML(), JLabel.CENTER);
	JPanel p = new JPanel ();

	p.add (l);
	p.setBorder (BorderFactory.createTitledBorder (prod.toString() + ": " + 
						       toString()));
	return p;
    }

    class Comparer implements Comparator<Cast> 
    {
	public int compare (Cast o1, Cast o2)
	{
	    String c1 = o1.getRole() +
		o1.getLastName() + o1.getFirstName();
	    String c2 = ((Cast) o2).getRole() +
		o2.getLastName() + o2.getFirstName();

	    return c1.compareTo (c2);
	    
	}
    }

    public void sort ()
    {
	Collections.sort (cast, new Comparer ());
	Collections.sort (covers, new Comparer ());
    }

    public ArrayList<Cast> getCast () {	return cast; }

    final public String getLastNameByRoleName (String r)
    {
	Cast c = (Cast) castMap.get (r);

	if (c == null) {
	    return "Not Cast";
	}
	else {
	    return c.getLastName ();
	}
    }

    final public Cast getCastByRole (Role r)
    {
	Cast c = (Cast) castMap.get (r.getName());
	
	return c;
    }

    public Production getProduction () { return prod; }

    public void setProduction (Production p) { this.prod = p; }

    final public Conductor getConductor () { return conductor; }

    final public String toString () { return getDateTimeText(); }

    final public String getDateText () { return Amato.getDateText (date); }

    final public String getTimeText () { return Amato.getTimeText (date); }

    final public String getDateTimeText () { return Amato.getDateTimeText (date); }

    final public Date getDate () { return date; }

    final public int getRoleCount () { return prod.getOpera().getRoleCount(); }

    final public int getRolesFilled () 
    {
	int rc = 0;

	for (Cast c: castMap.values()) if (c != null) ++rc;

	return rc;
    }
	
}

