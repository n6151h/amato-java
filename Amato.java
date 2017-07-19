/**
 * $Id: Amato.java,v 1.29 2005/09/14 18:55:44 nicks Exp nicks $
 * 
 * AMATO: Automated Management of All Tony's Operas
 *
 * This is where it all starts.  THE main() function is here. 
 * The app starts here.  Constants are defined here.
 *
 * Originally part of the AMATO application.
 * 
 * @author Nick Seidenman <nick@seidenman.net>
 * @version $Revision: 1.29 $
 *
 * $Log: Amato.java,v $
 * Revision 1.29  2005/09/14 18:55:44  nicks
 * Completely re-worked Opera.toJPanel.  Now uses pure java; no html.
 * This also means that line-wrap works in the synopsis area.
 *
 * Revision 1.28  2005/09/13 15:15:44  nicks
 * "changed" flag is now set for any add or delete operation.
 *
 * Revision 1.27  2005/09/12 21:25:04  nicks
 * Performances listed under productions in the season panel are now
 * color-coded to indicate the level of casting.  A yellow dot indicates
 * the performance is not cast at all, a red dot indicates that one or
 * more (but not all) roles have been cast, and a green dot indicates
 * that all roles have been cast.  Thanks to Bengi Mayone for this
 * idea.
 *
 * Revision 1.26  2005/09/09 23:59:54  nicks
 * Added AmatoWindowListener class to Amato so that I can intercept
 * the titlebar "kill" button and prompt the user to save a db
 * if it's been modified.
 *
 * Revision 1.25  2005/09/09 23:26:26  nicks
 * Before loading another db or exiting, changed flag is checked and
 * the user is prompted to save (or not) the current db.
 *
 * Revision 1.24  2005/09/09 22:23:59  nicks
 * Now has date sanity checks:
 *   - season start must come before season end
 *   - production start must come before season end
 *   - production dates must fall within season dates
 *   - performance date must fall within production dates
 *
 * Revision 1.23  2005/09/09 21:24:51  nicks
 * All objects now store date/time in a Date instance.  This gets me
 * ready to be able to do some of the date/time sanity checks mentioned
 * in the TODO list.  (E.g., check to make sure performance date
 * is within the season start/end bounds.)
 *
 * Revision 1.22  2005/08/16 02:08:35  nicks
 * AddPerformanceFrame added.  All the basic elements can now be adde
 * or deleted.  (Now need to add Edit function.)
 *
 * Revision 1.21  2005/08/02 21:45:01  nicks
 * Moved some "constants" from Production.java to here.  Made more sense
 * to have them here where there were globally "obvious".
 *
 * Revision 1.20  2005/07/22 22:12:39  nicks
 * Started work on Drag-n-Drop within RosterPanel.
 * Cleaned up nearly all warnings.  Only a few left in Opera.java, Operas.java,
 * and Performance.java and these pertain to strict type-checking ala 1.5.
 * Just wanted to check everything in now as a check point.  I'll continue
 * to work on getting a totally clean compilation.
 *
 * Revision 1.19  2005/07/21 11:03:05  nicks
 * Splash and main windows are now centered on screen
 * on start-up.
 *
 * Revision 1.18  2005/06/28 20:30:42  nicks
 * Moved "new" DB xml into AmatoDB, where it really belongs.  Now Amato class
 * knows nothing about how a DB is initialized.  (It's better that way. :-)
 *
 * Revision 1.17  2005/06/28 20:14:30  nicks
 * Prelim stuff to support printing.  Mostly code to provide the name
 * of the current panel.  The panel itself will have a routine that returns
 * the Printable to the PrinterJob object in Tabbed panes.
 *
 * Revision 1.16  2005/06/24 15:34:32  nicks
 * Adjusted sizes.  Made the splash and main windows appear
 * (roughly) in the center of the screen.
 *
 * Revision 1.15  2005/06/24 15:18:58  nicks
 * Added Tony's image (tonyback.jpg from amato.org page).  Made
 * a splash page from this.
 *
 * Revision 1.14  2005/06/23 20:36:35  nicks
 * Save operation not checks to see if file already exists
 * and pops up a confirmation dialog to see if it's ok to overwrite.
 *
 * Revision 1.13  2005/06/23 19:43:28  nicks
 * File operations (other than Print) now working.
 *
 * Revision 1.12  2005/06/22 21:41:53  nicks
 * Cheap, demo version of cast selection working. (Will use real
 * java stuff, not this crappy html vaporware.)
 * Had to make the AmatoDB object globally visible for this
 * sort of thing to work.  I hate doing it, but I guess there's no
 * harm for the moment since this is a self-contained application.
 *
 * Revision 1.11  2005/06/22 18:19:04  nicks
 * Created new container class Performances to hold/manage Performance
 * objects.
 *
 * Revision 1.10  2005/06/21 21:16:02  nicks
 * EUREKA!  I've got the thing to open with the size I want
 * and the TabbedPanel has expanded to fill the frame and resized
 * when I resize the frame.  In other words, I have something I can
 * now show Irene!!!
 *
 * Revision 1.9  2005/06/21 18:43:06  nicks
 * Now have a menubar.  Starting to integrate components.
 *
 * Revision 1.8  2005/06/18 04:19:02  nicks
 * Have OperaPanel and RosterPanel (cheesy versions) working.
 * This should be suitable for demo to Irene sometime soon.
 * I just want to more or less dummy up a SeasonPanel and I'll
 * be ready to demo.
 *
 * Revision 1.7  2005/06/16 17:29:50  nicks
 * Ripped out all of the XML parser code and put it into
 * a new object called AmatoDB.  The Amato object is now becoming more
 * and more the application's shell class.  The AmatoDB class
 * knows how to load and save the database from/to and XML-encoded
 * file.
 *
 * Revision 1.6  2005/06/16 15:25:05  nicks
 * Added object_name (HashMap aMap) constructor so that I can simply pass
 * the attribute map from the parser without the parser itself ever
 * needing to know such details as which attributes are required and
 * which are optional.  This is a slicker, cleaner, more OO way to
 * handle XML tag attributes.
 *
 * Revision 1.5  2005/06/15 21:59:42  nicks
 * Added libretto attribute to Production class.
 *
 * Revision 1.4  2005/06/15 21:31:47  nicks
 * All tags seem to be working.
 *
 * Revision 1.3  2005/06/15 20:47:04  nicks
 * Everything (but Roster) is now working fine.  I can digest an AmatoBase,
 * spit it back out, digest it again, spit it out again, and the third
 * gen matches the second gen.
 *
 * Revision 1.2  2005/06/15 17:27:30  nicks
 * Ingests multiple operas from multiple files (one per file.)
 *
 * Revision 1.1  2005/06/15 14:51:08  nicks
 * Initial revision
 *
 */


import java.io.*;
import java.util.*;
import java.net.*;

import java.text.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class Amato
{
    public static AmatoDB db;
    private static TabbedPanes tp;
    private static MenuBar m;
    private static JFrame frame;
    private static String titleText;

    // Used in JPanel (and elsewhere?)
    final public static Font H1 = new Font ("SansSerif", Font.BOLD, 40);
    final public static Font H2 = new Font ("SansSerif", Font.BOLD, 30);
    final public static Font H3 = new Font ("SansSerif", Font.BOLD, 20);
    final public static Font H3i = new Font ("SansSerif", Font.BOLD | Font.ITALIC, 16);
    final public static Font PLAIN = new Font ("SansSerif", Font.PLAIN, 16);
    final public static Font PLAIN_I = new Font ("SansSerif", Font.ITALIC, 16);
    final public static Font BOLD = new Font ("SansSerif", Font.BOLD, 16);
    final public static Font BOLD_I = new Font ("SansSerif", Font.BOLD|Font.ITALIC, 16);

    final public static String dateFormatStr = "MM/dd/yyyy";
    final public static String timeFormatStr = "HH:mm";
    final public static String dateTimeFormatStr = "MM/dd/yyyy HH:mm";
    
    public static void main (String[] args) 
    {
	// Put up a splash page ...
	JFrame splash = new JFrame ("AMATO");
	splash.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
	splash.setDefaultLookAndFeelDecorated (false);
	splash.setUndecorated (true);
	splash.getContentPane().setLayout (new BorderLayout());

	AboutAmato aa = new AboutAmato();
	splash.getContentPane().add (aa, BorderLayout.CENTER);
	splash.pack ();
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	Dimension splashSize = splash.getSize ();
	splash.setLocation (screenSize.width/2 - (splashSize.width/2), 
			    screenSize.height/2 - (splashSize.height/2));
	
	splash.setVisible (true);

	// Initializations ...
	
	String startupDBName;

	// We need to know what database to use.  Start with a "fresh"
	// one if we're not already given one.
	if (args.length < 1)
	    startupDBName = null;
	else
	    startupDBName = args[0];

	m = new MenuBar ();

	frame = new JFrame ("AMATO");
	frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
	frame.getContentPane().setLayout (new BorderLayout());
	frame.setSize (800, 600);
	Dimension frameSize = frame.getSize ();
	frame.setLocation (screenSize.width/2 - (frameSize.width/2), 
			    screenSize.height/2 - (frameSize.height/2));
	
	frame.setJMenuBar (m.mb);
	frame.addWindowListener (new AmatoWindowListener());

	loadAndGo (startupDBName);

	// Remove the splash page.
	splash.setVisible (false);
    }

    public static void loadAndGo (String dbFile)
    {
	if (dbFile == null) { // Initialize a new database.
	    db = new AmatoDB ();
	    setTitle ("<<new>>");
	}
	else {
	    // Load the specified database.
	    File dbf = new File (dbFile);
	    db = new AmatoDB (dbf);
	    setTitle (dbf.getName());
	}

	TabbedPanes ntp = new TabbedPanes ();
	ntp.setPreferredSize (new Dimension (800, 600));
	ntp.setVisible (true);

	// If this is the first time we've been run, the frame
	// has no TabbedPanel child, so we don't need to bother
	// removing it.
	if (tp != null) {
	    tp.setVisible (false);
	    frame.remove (tp);
	}

	// Create the tabbed panel display.
	frame.getContentPane().add (ntp, BorderLayout.CENTER);
	frame.setVisible (true);
	tp = ntp;
	db.setChanged (false);
    }

    public static void setTitle (String t)
    {
	titleText = t;

	if (db.isChanged()) 
	    frame.setTitle ("AMATO: " + t + " (modified)");
	else
	    frame.setTitle ("AMATO: " + t);
    }
    
    public static void resetTitle ()
    {
	if (db != null && db.isChanged()) 
	    frame.setTitle ("AMATO: " + titleText + " (modified)");
	else
	    frame.setTitle ("AMATO: " + titleText);
    }

    final static public TabbedPanes getTabbedPanes ()
    {
	return tp;
    }

    final static public void redraw () 
    { 
	tp.redraw (); 
	db.setChanged (true); 
	setTitle (titleText);
    }

    final public static String getDateText (Date d)
    {
	return (new SimpleDateFormat (dateFormatStr)).format (d);
    }

    final public static String getTimeText (Date d)
    {
	return (new SimpleDateFormat (timeFormatStr)).format (d);
    }

    final public static String getDateTimeText (Date d)
    {
	return (new SimpleDateFormat (dateTimeFormatStr)).format (d);
    }

    final public static Date parseDate (String s)
    {
	try {
	    SimpleDateFormat df = new SimpleDateFormat (Amato.dateFormatStr);
	    return df.parse (s, new ParsePosition (0));
	}
	catch (Throwable e) {
	    System.err.println (e);
	    return null;
	}
    }

    final public static Date parseTime (String s)
    {
	try {
	    SimpleDateFormat df = new SimpleDateFormat (Amato.timeFormatStr);
	    return df.parse (s, new ParsePosition (0));
	}
	catch (Throwable e) {
	    System.err.println (e);
	    return null;
	}
    }

    final public static Date parseDateTime (String s)
    {
	try {
	    SimpleDateFormat df = new SimpleDateFormat (Amato.dateTimeFormatStr);
	    return df.parse (s, new ParsePosition (0));
	}
	catch (Throwable e) {
	    System.err.println (e);
	    return null;
	}
    }

    static int confirmPopup (String msg, String query)
    {
	return JOptionPane.showConfirmDialog (null, msg, query, 
					      JOptionPane.YES_NO_OPTION);
    }

    static void errorPopup (String msg)
    {
	JOptionPane.showMessageDialog (null, "ERROR: " + msg, "ERROR",
				       JOptionPane.ERROR_MESSAGE);
    }

    static public String escape (String s) 
    {
	final StringBuffer result = new StringBuffer ();
	final StringBuffer source = new StringBuffer (s);
	final StringCharacterIterator i = new StringCharacterIterator (s);
	char c = i.current();
	while (c != StringCharacterIterator.DONE) {
	    if (c == '<') 
		result.append ("&lt;");
	    else if (c == '>')
		result.append ("&gt;");
	    else if (c == '&')
		result.append ("&amp;");
	    else if (c == '\"')
		result.append ("&quot;");
	    else if (c == '\'')
		result.append ("&#039;");
	    else if (c == '\\')
		result.append ("&#092;");
	    else
		result.append (c);
	    c = i.next();
	}
	 
	return result.toString();
    }

    static public String unescape (String s)
    {
	String result = new String (s);

	
	result.replace ("&lt;", "<");
	result.replace ("&gt;", ">");
	result.replace ("&amp;", "&");
	result.replace ("&quot;", "\"");

	// Really need a better way to do this.
	result.replace ("&#039;", "\'");
	result.replace ("&#092;", "\\");

	return result;
    }

    // Required for WindowListener interface. 

    static public class AmatoWindowListener
	extends Object
	implements WindowListener
    {
	public AmatoWindowListener () { super (); }

	public void windowClosing (WindowEvent e) 
	{
	    db.checkToSave ("Exit");
	    
	    System.exit (0);
	}
	
	public void windowActivated (WindowEvent e) {}
	
	public void windowClosed (WindowEvent e) {}
	
	public void windowDeactivated (WindowEvent e) {}
	
	public void windowDeiconified (WindowEvent e) {}
	
	public void windowIconified (WindowEvent e) {}
	
	public void windowOpened (WindowEvent e) {}
    }
}
