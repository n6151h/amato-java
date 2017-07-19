/**
 * $Id: Opera.java,v 1.19 2005/09/14 18:55:44 nicks Exp nicks $
 * 
 * Originally part of the AMATO application.
 * 
 * @author Nick Seidenman <nick@seidenman.net>
 * @version $Revision: 1.19 $
 *
 * $Log: Opera.java,v $
 * Revision 1.19  2005/09/14 18:55:44  nicks
 * Completely re-worked Opera.toJPanel.  Now uses pure java; no html.
 * This also means that line-wrap works in the synopsis area.
 *
 * Revision 1.18  2005/09/13 15:15:44  nicks
 * "changed" flag is now set for any add or delete operation.
 *
 * Revision 1.17  2005/09/12 17:36:44  nicks
 * If a singer is being cast in more than one role, a pop-up warning
 * is displayed and the user is asked if this is ok.  If it is, the singer
 * is (n-tuple cast).
 *
 * Revision 1.16  2005/09/08 15:37:40  nicks
 * Seasons, Productions, Performances, Constituents, and Operas are now
 * all editable!
 *
 * Revision 1.15  2005/08/31 16:21:31  nicks
 * Fixed problem where role/voice wasn't being added in AddOperaPanel.
 *
 * Revision 1.14  2005/08/21 19:06:23  nicks
 * Changed comment header to conform with javadoc conventions.
 *
 * Revision 1.13  2005/08/15 14:26:28  nicks
 * Changed as_* to to* (more java-esque)
 *
 * Revision 1.12  2005/07/22 22:26:54  nicks
 * CLEAN COMPILATION w/ 1.5 compiler (javac -Xlint *.java)!
 *
 * Revision 1.11  2005/07/22 22:12:39  nicks
 * Started work on Drag-n-Drop within RosterPanel.
 * Cleaned up nearly all warnings.  Only a few left in Opera.java, Operas.java,
 * and Performance.java and these pertain to strict type-checking ala 1.5.
 * Just wanted to check everything in now as a check point.  I'll continue
 * to work on getting a totally clean compilation.
 *
 * Revision 1.10  2005/07/17 11:48:35  nicks
 * toJPanel instead of as_JLabel (instead of toHTML).
 *
 * Revision 1.9  2005/07/15 19:27:39  nicks
 * Details are now displayed as JLabels, not html.  (Actually, I'm
 * taking advantage of the new JLabel capability that lets you
 * supply html as the label content.)
 *
 * Revision 1.8  2005/07/12 19:17:30  nicks
 * Add Productions function works.
 *
 * Revision 1.7  2005/06/20 22:34:38  nicks
 * SeasonPanel is now working in a cheesy, demo-only sort of way.  It's
 * at least enough for me to get the idea across to Irene and talk
 * through how it will work when finished.d
 *
 * Revision 1.6  2005/06/18 22:41:51  nicks
 * RosterPanel now working.
 * Several changes to the toHTML methods.
 * Fixed parsing in Musician so that list of instruments is properley
 *  split along comma (,) delimiters.
 *
 * Revision 1.5  2005/06/18 04:19:02  nicks
 * Have OperaPanel and RosterPanel (cheesy versions) working.
 * This should be suitable for demo to Irene sometime soon.
 * I just want to more or less dummy up a SeasonPanel and I'll
 * be ready to demo.
 *
 * Revision 1.4  2005/06/17 17:11:19  nicks
 * First (cheesy) integration of GUI (OperaPanel) with the database (AmatoDB).
 *
 * Revision 1.3  2005/06/16 15:25:05  nicks
 * Added object_name (HashMap aMap) constructor so that I can simply pass
 * the attribute map from the parser without the parser itself ever
 * needing to know such details as which attributes are required and
 * which are optional.  This is a slicker, cleaner, more OO way to
 * handle XML tag attributes.
 *
 * Revision 1.2  2005/06/15 17:28:18  nicks
 * Rearranged the toXML output a bit to make it more sensical.
 *
 * Revision 1.1  2005/06/15 14:51:08  nicks
 * Initial revision
 *
 */


import java.io.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class Opera
    implements Serializable
{
    private static final long serialVersionUID = 20050722112840L;
    
    private String name;
    private ArrayList<AltName> alts; // Alternate names/languages.
    private String composer;
    private String librettist;
    private String language;
    private String runtime;
    private ArrayList<Synopsis> syns;
    private ArrayList<Role> roles;
    
    private ArrayList<Production> prods;

    public Opera (HashMap aMap)
    {
	this.name = (String) aMap.get ("name");
	this.composer = (String) aMap.get ("composer");
	this.librettist = (String) aMap.get ("librettist");
	this.language = (String) aMap.get ("language");
	this.runtime = (String) aMap.get ("runtime");

	this.alts = new ArrayList<AltName> ();
	this.roles = new ArrayList<Role> ();
	this.syns = new ArrayList<Synopsis> ();
	this.prods = new ArrayList<Production> ();
    }

    public void add_role (Role r)
    {
	this.roles.add (r);
	this.sortRoles ();
    }

    public void add_synopsis (Synopsis syn)
    {
	this.syns.add (syn);
    }

    public void add_alternate_name (AltName n)
    {
	this.alts.add (n);
    }
 
    public String toXML ()
    {
	String result;
	
	// Emit the opera tag
	result = "<opera name=\"" + name + "\"" +
	    " composer=\"" + composer + "\"" + 
	    " librettist=\"" + librettist + "\"" + 
	    " language=\"" + language + "\"" + 
	    " runtime=\"" + runtime + "\">\n"; 
	
	
	// Emit the alternate names, if any.
	for (AltName a: alts) {
	    result += a.toXML ();
	    result += "\n";
	}
	    
	// Emit the synopses, if any.
	result += "<synopses>\n";
	for (Synopsis s: syns) {
	    result += s.toXML ();
	    result += "\n";
	}
	result += "</synopses>\n";
	    
	// Emit the roles
	result += "<roles>\n";
	for (Role r: roles) {
	    result += r.toXML ();
	    result += "\n";
	}
	result += "</roles>\n";
	    
	// Emit the end tag
	result += "</opera>";
	
	return result;
    }

    final public String getName () { return name; }

    final public String toString () { return name; }

    final public String getComposer () { return composer; }

    final public String getLibrettist () { return librettist; }

    final public String getRuntime () { return runtime; }

    final public String getLanguage () { return language; }

    final public String getSynopsisText () 
    {
	Synopsis s = syns.get(0);
	return (s == null) ? "" : s.getText();
    }

    final public String toHTML ()
    {
	String result = new String ("<html><h1>" + name + "</h1>\n");
	
	// Emit the general information.
	result += "<b>Composer:</b> " + composer + "<br>" + 
	    " <b>Librettist:</b> " + librettist +  "<br>" +
	    " <b>Language:</b> " + language +  "<br>" +
	    " <b>Running Time:</b> " + runtime + "<p><hr>\n"; 
	    
	// Emit the synopses, if any.
	for (Synopsis s: syns) {
	    result += "<h2>Synopsis:</h2>" + s.toXML ();
	    result += "<p>\n";
	}
	    
	// Emit the roles
	result += "<h2>Roles</h2><table><tr><th align='left'>Name</th><th align='right'>Voice</th></tr>\n";
	for (Role r: roles) {
	    result += r.toHTML_tr ();
	}
	result += "</table></html>";
	    
	return result;
    }

    final public JPanel toJPanel ()
    {
	JPanel p = new JPanel ();
	p.setLayout (new BoxLayout (p, BoxLayout.Y_AXIS));
	
	// Name.
	JPanel p0 = new JPanel (new FlowLayout());
	JLabel l = new JLabel (name, JLabel.LEFT);
	l.setFont (Amato.H2);
	p0.add (l);
	p.add (p0);

	// Compposer, Librettist.
	JPanel p11 = new JPanel(new FlowLayout());
	l = new JLabel ("Composer: ");
	l.setFont (Amato.BOLD);
	p11.add (l);
	l = new JLabel ((composer == null) ? "" : composer);
	l.setFont (Amato.PLAIN);
	p11.add (l);
	p.add (p11);

	JPanel p12 = new JPanel(new FlowLayout());
	l = new JLabel ("Librettist: ");
	l.setFont (Amato.BOLD);
	p12.add (l);
	l = new JLabel ((librettist == null) ? "" : librettist);
	l.setFont (Amato.PLAIN);
	p12.add (l);
	p.add(p12);
	
	// Language, running time.
	JPanel p21 = new JPanel(new FlowLayout());
	l = new JLabel ("Language: ");
	l.setFont (Amato.BOLD);
	p21.add (l);
	l = new JLabel ((language == null) ? "" : language);
	l.setFont (Amato.PLAIN);
	p21.add (l);
	p.add(p21);
	
	JPanel p22 = new JPanel(new FlowLayout());
	l = new JLabel ("Running Time: ");
	l.setFont (Amato.BOLD);
	p22.add (l);
	l = new JLabel ((runtime == null) ? "" : runtime);
	l.setFont (Amato.PLAIN);
	p22.add (l);
	p.add(p22);
	

	// Synopsis.
	if (syns.size() > 0) {
	    JPanel p31 = new JPanel (new FlowLayout());
	    l = new JLabel ("Synopsis:");
	    l.setFont (Amato.BOLD);
	    p31.add(l);
	    p.add(p31);
	    p.add (syns.get(0).toJPanel());
	}
       
	// Roles.
	JPanel p41 = new JPanel (new FlowLayout());
	l = new JLabel ("Roles:");
	l.setFont (Amato.BOLD);
	p41.add (l);
	p.add (p41);

	JPanel p42 = new JPanel (new GridLayout (0, 2));

	l = new JLabel ("Role", JLabel.CENTER);
	l.setFont (Amato.BOLD);
	p42.add (l);

	l = new JLabel ("Voice", JLabel.CENTER);
	l.setFont (Amato.BOLD);
	p42.add (l);

	for (Role r: getRoles ()) {
	    l = new JLabel (r.getName(), JLabel.CENTER);
	    l.setFont (Amato.PLAIN);
	    p42.add (l);

	    l = new JLabel (r.getVoice(), JLabel.CENTER);
	    l.setFont (Amato.PLAIN);
	    p42.add (l);
	}

	p.add (p42);

	p.setBorder (BorderFactory.createTitledBorder (toString()));

	return p;
    }

    class RoleComparer implements Comparator<Role>
    {
	public int compare (Role r1, Role r2)
	{
	    return r1.getOrder() - r1.getOrder();
	}
    }

    public void sortRoles ()
    {
	Collections.sort (roles, new RoleComparer ());
    }

    public Role getRoleByName (String n)
    {
	for (Role r: roles) {
	    if (r.getName().equals (n)) {
		return r;
	    }
	}

	return null;
    }

    public ArrayList<Role> getRoles () { return roles; }

    // The next three methods are for maintaining a list of 
    // productions that feature this opera.  When the opera
    // instance is changed the new instance must inherit the
    // old instances production list.  If the opera is being
    // deleted, we must first check to see that it is not
    // associated with any productions.

    public ArrayList<Production> getProductions () { return prods; }

    public void add_prod (Production p) { prods.add (p); }

    public void remove_prod (Production p) 
    { 
	prods.remove (p); 
	Amato.db.setChanged(true);
    }

    public int getProductionCount () { return prods.size(); }
    
    int getRoleCount () { return roles.size(); }
}

