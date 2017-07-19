/**
 * $Id: Constituent.java,v 1.17 2005/09/13 15:15:44 nicks Exp nicks $
 * 
 * Originally part of the AMATO application.
 * 
 * @author Nick Seidenman <nick@seidenman.net>
 * @version $Revision: 1.17 $
 *
 * $Log: Constituent.java,v $
 * Revision 1.17  2005/09/13 15:15:44  nicks
 * "changed" flag is now set for any add or delete operation.
 *
 * Revision 1.16  2005/09/08 15:37:40  nicks
 * Seasons, Productions, Performances, Constituents, and Operas are now
 * all editable!
 *
 * Revision 1.15  2005/09/07 17:07:53  nicks
 * Deprecated the explicit argument form of the Constituent constructor.
 *
 * Revision 1.14  2005/08/16 21:17:27  nicks
 * Changed comment header to comply with javadoc convention.
 *
 * Revision 1.13  2005/08/15 14:26:28  nicks
 * Changed as_* to to* (more java-esque)
 *
 * Revision 1.12  2005/08/15 13:47:15  nicks
 * Several changes to support AddPerformance.
 * Changed as_* to to* (more java-esque.)
 *
 * Revision 1.11  2005/08/03 18:33:53  nicks
 * Now able to add Singers, Musicians, Conductors, Directors, Staffs
 *  on RosterPanel pop-up menu.
 *
 * Revision 1.10  2005/08/02 21:48:41  nicks
 * Added constructor that takes a (single) HasMap for its argument.
 *
 * Revision 1.9  2005/07/22 22:12:39  nicks
 * Started work on Drag-n-Drop within RosterPanel.
 * Cleaned up nearly all warnings.  Only a few left in Opera.java, Operas.java,
 * and Performance.java and these pertain to strict type-checking ala 1.5.
 * Just wanted to check everything in now as a check point.  I'll continue
 * to work on getting a totally clean compilation.
 *
 * Revision 1.8  2005/07/17 11:48:35  nicks
 * toJPanel instead of as_JLabel (instead of toHTML).
 *
 * Revision 1.7  2005/06/30 15:39:39  nicks
 * Added serialVersionUID to keep the 5.0 compiler happy.
 * (sheesh!)
 *
 * Revision 1.6  2005/06/22 18:34:48  nicks
 * Fixed getFunction.
 *
 * Revision 1.5  2005/06/18 22:41:51  nicks
 * RosterPanel now working.
 * Several changes to the toHTML methods.
 * Fixed parsing in Musician so that list of instruments is properley
 *  split along comma (,) delimiters.
 *
 * Revision 1.4  2005/06/18 04:19:02  nicks
 * Have OperaPanel and RosterPanel (cheesy versions) working.
 * This should be suitable for demo to Irene sometime soon.
 * I just want to more or less dummy up a SeasonPanel and I'll
 * be ready to demo.
 *
 * Revision 1.3  2005/06/15 21:31:47  nicks
 * All tags seem to be working.
 *
 * Revision 1.2  2005/06/15 17:28:05  nicks
 * *** empty log message ***
 *
 * Revision 1.1  2005/06/15 14:51:08  nicks
 * Initial revision
 *
 */


import java.util.*;
import java.io.*;
import java.text.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.border.*;



/** Base class for other types of roster members. */

public class Constituent extends DefaultMutableTreeNode
{
    private static final long serialVersionUID = 20050630112700L;

    private String first_name;
    private String last_name;
    private ArrayList<String> phone;
    private ArrayList<String> email;
    private String street;
    private String city;
    private String state;
    private String zip;
    private String country;
    private String status; // active, inactive, alumnus, deceased, etc.
    private boolean valid;
    private String function = null;
    transient private ArrayList<Performance> performances;
    
    public Constituent (HashMap h)
    {
	super ();

	String pl[] = ((String) h.get ("phone")).split (",\\s*");
	String el[] = ((String) h.get ("email")).split (",\\s*");

	this.function = null;
	this.first_name = (String) h.get ("first_name");
	this.last_name = (String) h.get ("last_name");
	this.phone = new ArrayList<String>();
	for (String s: pl) this.phone.add (s);
	this.email = new ArrayList<String>();
	for (String s: el) this.email.add (s);
	this.street = (String) h.get ("street");	
	this.city = (String) h.get ("city");
	this.state = (String) h.get ("state");
	this.zip = (String) h.get ("zip");
	this.country = (String) h.get ("country");
	this.status = (String) h.get ("status");
	this.performances = new ArrayList<Performance>();
	this.valid = true;
    }
    
    public void add_phone (String p)
    {
	phone.add (p);
    }

    public void add_email (String e)
    {
	email.add (e);
    }
    
    final public boolean isValid () { return valid; }
    
    public void setValid (boolean v) { valid = v; }

    public String toXML ()
    {
	String result = new String (" first_name=\"");
	String e = "" + email;
	String p = "" + phone;
	
	result += first_name + "\"" +
	    " last_name=\"" + last_name + "\"" +
	    " street=\"" + street + "\"" +
	    " city=\"" + city + "\"" +
	    " state=\"" + state + "\"" +
	    " zip=\"" + zip + "\"" +
	    " country=\"" + country + "\"" + 
	    " email=\"" + e.substring(1,e.length()-1) + "\"" + 
	    " phone=\"" + p.substring(1,p.length()-1) + "\"" + 
	    " status=\"" + getStatus () + "\"";

	return result;
    }

    public String toHTML ()
    {
	String result = new String ("<html><h1>" + first_name + " " + 
				    last_name + "</h1>\n");
	String e = "" + email;
	String p = "" + phone;
	
	result += "<b>Address:</b><ul>" + 
	    street + "<br>" +
	    city + ", " + state + " " + zip + "<br>\n" +
	    country + "<p></ul>\n" +  
	    "<b>email:</b> " + e.substring(1,e.length()-1) + "<p>\n" + 
	    "<b>phone:</b> " + p.substring(1,p.length()-1) + "<p>\n" + 
	    "<b>status:</b> " + status + "<p>\n" +
	    "</html>";

	return result;
    }

    public JPanel toJPanel ()
    {
	JPanel p = new JPanel();
	BoxLayout pb = new BoxLayout (p, BoxLayout.Y_AXIS);
	p.setLayout (pb);

	JLabel l = new JLabel (toHTML(), JLabel.CENTER);

	p.add (l);
	p.setBorder (BorderFactory.createTitledBorder (getFirstName() + " " +
						       getLastName()));

	String perfStr = "<html><menu>";
	for (Performance perf: getPerformances()) {
	    perfStr += ("<li>" + perf.getProduction() + " - " +
			perf.toString () + "</li>");
	}

	p.add (new JLabel (perfStr, JLabel.CENTER));
	
	return p;
    }

    // Override.
    public boolean isLeaf () { return true; }

    final public String getLastName () { return last_name; }

    final public String getFirstName () { return first_name; }

    final public String getStatus () { return ((status != null) ? status : ""); }

    final public String getStreet () { return street; }

    final public String getCity () { return city; }

    final public String getState () { return state; }

    final public String getZip () { return zip; }

    final public String getCountry () { return country; }

    final public String[] getPhoneList () { return phone.toArray (new String[0] ); }

    final public String[] getEmailList () { return email.toArray (new String[0] ); }

    final public String toString ()
    {
	return first_name + " " + last_name;
    }

    public String getFunction () { return function; }

    public void addPerformance (Performance p)
    {
	if (performances == null) getPerformances();

	if (! performances.contains (p)) performances.add(p);
    }

    public void removePerformance (Performance p)
    {
	if (performances == null) getPerformances();

	//System.out.println ("performances: " + performances);
	if (performances.contains (p)) performances.remove(p);

    }

    final public ArrayList<Performance> getPerformances () 
    {
	// Are we just back from serialization?
	if (performances == null) {
	    performances = new ArrayList<Performance>();
	    ArrayList<Performance> pList = Amato.db.getSeasons().getPerformances();

	    for (Performance p: pList) {
		for (Cast c: p.getCast()) {
		    if (c.getActor().equals (this) && !performances.contains (p)) {
			System.out.println ("getPerformances: adding" + p);
			performances.add (p);
		    }
		}
	    }
	}

	return performances; 
    }

    final public int getPerformanceCount () 
    { 
	if (performances == null) getPerformances();

	return performances.size(); 
    }

    final public void clearPerformances () 
    { 
	System.out.println ("clearPerformances: " + this);
	performances = null; 
    }
}

