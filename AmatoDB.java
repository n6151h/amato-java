/**
 * $Id: AmatoDB.java,v 1.24 2005/09/14 18:55:44 nicks Exp nicks $
 * 
 * Instantiate an AMATO database and initialize it using
 * the XML content of the specified file.
 *
 * Originally part of the AMATO application.
 * 
 * @author Nick Seidenman <nick@seidenman.net>
 * @version $Revision: 1.24 $
 *
 * $Log: AmatoDB.java,v $
 * Revision 1.24  2005/09/14 18:55:44  nicks
 * Completely re-worked Opera.toJPanel.  Now uses pure java; no html.
 * This also means that line-wrap works in the synopsis area.
 *
 * Revision 1.23  2005/09/13 15:15:44  nicks
 * "changed" flag is now set for any add or delete operation.
 *
 * Revision 1.22  2005/09/09 23:59:54  nicks
 * Added AmatoWindowListener class to Amato so that I can intercept
 * the titlebar "kill" button and prompt the user to save a db
 * if it's been modified.
 *
 * Revision 1.21  2005/09/09 23:26:26  nicks
 * Before loading another db or exiting, changed flag is checked and
 * the user is prompted to save (or not) the current db.
 *
 * Revision 1.20  2005/09/09 22:23:59  nicks
 * Now has date sanity checks:
 *   - season start must come before season end
 *   - production start must come before season end
 *   - production dates must fall within season dates
 *   - performance date must fall within production dates
 *
 * Revision 1.19  2005/08/16 21:17:27  nicks
 * Changed comment header to comply with javadoc convention.
 *
 * Revision 1.18  2005/08/16 02:18:02  nicks
 * Cast was adding itself to Performance.  Now all instances
 * of this call Performance.add_cast.
 *
 * Revision 1.17  2005/08/15 14:26:28  nicks
 * Changed as_* to to* (more java-esque)
 *
 * Revision 1.16  2005/07/28 21:43:19  nicks
 * Added staff tag.
 *
 * Revision 1.15  2005/07/22 22:12:39  nicks
 * Started work on Drag-n-Drop within RosterPanel.
 * Cleaned up nearly all warnings.  Only a few left in Opera.java, Operas.java,
 * and Performance.java and these pertain to strict type-checking ala 1.5.
 * Just wanted to check everything in now as a check point.  I'll continue
 * to work on getting a totally clean compilation.
 *
 * Revision 1.14  2005/07/17 11:48:35  nicks
 * toJPanel instead of as_JLabel (instead of toHTML).
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
 * Revision 1.9  2005/06/28 20:30:42  nicks
 * Moved "new" DB xml into AmatoDB, where it really belongs.  Now Amato class
 * knows nothing about how a DB is initialized.  (It's better that way. :-)
 *
 * Revision 1.8  2005/06/23 20:36:35  nicks
 * Save operation not checks to see if file already exists
 * and pops up a confirmation dialog to see if it's ok to overwrite.
 *
 * Revision 1.7  2005/06/23 19:43:28  nicks
 * File operations (other than Print) now working.
 *
 * Revision 1.6  2005/06/23 15:22:39  nicks
 * Cleaned up a few minor problems.
 *
 * Revision 1.5  2005/06/20 22:34:38  nicks
 * SeasonPanel is now working in a cheesy, demo-only sort of way.  It's
 * at least enough for me to get the idea across to Irene and talk
 * through how it will work when finished.d
 *
 * Revision 1.4  2005/06/20 17:31:30  nicks
 * SeasonPanel (dummy'd up) now working.
 *
 * Revision 1.3  2005/06/18 04:19:02  nicks
 * Have OperaPanel and RosterPanel (cheesy versions) working.
 * This should be suitable for demo to Irene sometime soon.
 * I just want to more or less dummy up a SeasonPanel and I'll
 * be ready to demo.
 *
 * Revision 1.2  2005/06/17 17:11:19  nicks
 * First (cheesy) integration of GUI (OperaPanel) with the database (AmatoDB).
 *
 * Revision 1.1  2005/06/16 17:29:21  nicks
 * Initial revision
 *
 */


import java.io.*;
import java.util.*;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.tree.*;

public class AmatoDB extends DefaultHandler
{
    static private Operas operas;
    static private Roster roster;
    static private Seasons seasons;

    static private String dbFileName;

    static private Conductor c_conductor;
    static private Director c_director;
    static private Musician c_musician;
    static private Opera c_opera;
    static private Performance c_performance;
    static private Production c_production;
    static private Role c_role;
    static private Season c_season;
    static private Singer c_singer;

    static private String c_language;
    static private String c_text;

    static private boolean changed;

    static private String fresh_db =
	"<?xml version='1.0' encoding='utf-8'?>" +
	"<amatobase version=\"1.0\">" +
	"<operas>" +
	"</operas>" +
	"<roster>" +
	"</roster>" +
	"<seasons>" +
	"</seasons>" +
	"</amatobase>";

    public AmatoDB (File dbFile)
    {

	operas = new Operas ();
	roster = new Roster();
	seasons = new Seasons ();
	
	changed = false;

	try {
	    this.dbFileName = dbFile.getCanonicalPath();
	}
	catch (Throwable t) {
	    Amato.errorPopup ("Couldn't determine canonical path for " + dbFile);
	}

	// Use an instance of ourselves as the SAX event handler
	DefaultHandler handler = this;
	
	// Use the default (non-validating) parser
	SAXParserFactory factory = SAXParserFactory.newInstance();

	try {

	    // Parse the input
	    SAXParser saxParser = factory.newSAXParser();

	    saxParser.parse (dbFileName, handler);

	} catch (Throwable t) {
	    t.printStackTrace ();
	}
    }

    public AmatoDB ()
    {
	operas = new Operas ();
	roster = new Roster();
	seasons = new Seasons ();
	
	this.dbFileName = null;

	// Use an instance of ourselves as the SAX event handler
	DefaultHandler handler = this;
	
	// Use the default (non-validating) parser
	SAXParserFactory factory = SAXParserFactory.newInstance();

	try {
	    // Parse the input
	    SAXParser saxParser = factory.newSAXParser();

	    saxParser.parse (new InputSource (new StringReader (fresh_db)), handler);

	} catch (Throwable t) {
	    t.printStackTrace ();
	}
    }
    
    static public String toXML ()
    {
	String result = "<?xml version='1.0' encoding='utf-8'?>\n";

	result += "<amatobase version=\"1.0\">\n";
	result += operas.toXML() + "\n";
	result += roster.toXML() + "\n";
	result += seasons.toXML() + "\n";
	
	result += "</amatobase>";
	
	return result;
    }

    static public void save_as (String fname)
    {
	File outputFile = new File (fname);

	try {
	    FileWriter outf = new FileWriter (outputFile);
	    outf.write (toXML() + "\n");
	    outf.close ();

	    dbFileName = fname;
	    setChanged (false);

	    Amato.setTitle (outputFile.getName());
	    

	}
	catch (Throwable t) {
	    Amato.errorPopup ("Failed trying to save " + fname);
	    System.err.println (t);
	}
    }
    
    static public void save ()
    {
	save_as (dbFileName);
    }
    
    public void startDocument() throws SAXException
    {
	// Clear everything out and start anew.
	c_conductor = null;
	c_director = null;
	c_musician = null;
	c_opera = null;
	c_performance = null;
	c_production = null;
	c_role = null;
	c_season = null;
	c_language = null;
	c_text = "";
    }

    public void endDocument () throws SAXException
    {
	// Finish up and make sure our database is now ready to use.
    }


    public void startElement (String namespaceURI,
			      String sName, // simple name (localName)
			      String qName, //qualified name
			      Attributes attrs) throws SAXException
    {
	String eName = sName; // element name

	if ("".equals (eName)) eName = qName; // namespaceAware = false
	
	// Create a name->value map for the attrs.
	// Each of the objects will have a constructor that accepts
	// the HashMap aMap as its sole (or, at least first) argument
	// and will itself know which attributes are optional and
	// which are mandatory.  (Slick, eh?  :-)
	HashMap<String,String> aMap = new HashMap<String,String>();
	if (attrs != null) {
	    for (int i = 0; i < attrs.getLength(); i++) {
		String aName = attrs.getLocalName (i); // Attr name
		if ("".equals (aName)) aName = attrs.getQName (i);
		aMap.put (aName, attrs.getValue(i));
	    }
	}

	// See what kind of element we have and instantiate 
	// an appropriate object to represent it. (I wonder if
	// there's a way to do this as I would in python, where I'd
	// simply map the tags to object classes and call the
	// constructor based on a dictionary (or HashMap) look-up.)
	if (eName.equals ("opera")) {
	    c_opera = new Opera (aMap);
	}
	
	else if (eName.equals ("synopsis")) {
	    c_language = (String) aMap.get ("language");
	}
					   
	else if (eName.equals ("role")) {
	    c_opera.add_role (new Role (c_opera, aMap));
	}

	else if (eName.equals ("season")) {
	    c_season = new Season (aMap);
	}

	else if (eName.equals ("production")) {
	    c_production = new Production (c_season, operas, aMap);
	}
	
	else if (eName.equals ("performance")) {
	    c_performance = new Performance (c_production, roster, aMap);
	}

	else if (eName.equals ("cast")) {
	    Cast cm = new Cast (c_performance, roster, aMap);
	    if (cm.getActor() != null)
		c_performance.addCast (cm);
	}

	else if (eName.equals ("alternateName")) {
	    c_opera.add_alternate_name (new AltName (aMap));
	}

	// Roster constituents ...

	else if (eName.equals ("conductor")) {
	    roster.add (new Conductor (aMap));
	}

	else if (eName.equals ("director")) {
	    roster.add (new Director (aMap));
	}

	else if (eName.equals ("musician")) {
	    roster.add (new Musician (aMap));
	}
			
	else if (eName.equals ("singer")) {
	    roster.add (new Singer (aMap));
	}
			
	else if (eName.equals ("staff")) {
	    roster.add (new Staff (aMap));
	}
			
	else {
	    //System.err.println ("unknown tag: " + eName); // DEBUG
	}
    }

    public void endElement (String namespaceURI,
			    String sName, // simple name
			    String qName // qualified name
			    ) throws SAXException
    {
	String eName = sName;
	if ("".equals (eName)) eName = qName;

	if (eName.equals ("synopsis")) {
	    c_opera.add_synopsis (new Synopsis (c_language.trim(), c_text.trim()));
	}

	else if (eName.equals ("opera")) {
	    this.operas.add (c_opera);
	    c_opera = null;
	}

	else if (eName.equals ("season")) {
	    this.seasons.add (c_season);
	    c_season = null;
	}

	else if (eName.equals ("production")) {
	    this.c_season.add (c_production);
	    c_production = null;
	}

	else if (eName.equals ("performance")) {
	    this.c_production.add (c_performance);
	    c_performance = null;
	}

	// Clear c_text;
	c_text = "";
    }

    public void characters (char buf[], int offset, int len) 
	throws SAXException
    {
	c_text += new String (buf, offset, len);
    }
    
    public Operas getOperas ()
    {
	return operas;
    }

    public Roster getRoster ()
    {
	return roster;
    }

    public Seasons getSeasons ()
    {
	return seasons;
    }

    final public String getDBFileName () { return dbFileName; }

    final public boolean isChanged () { return changed; }

    static public void setChanged (boolean s) { changed = s; Amato.resetTitle(); }

    static public void checkToSave (String s)
    {
	if (Amato.db.isChanged()) {
	    if (Amato.confirmPopup ("Save changes to " +
				    Amato.db.getDBFileName() + "?", 
				    "Save Changes?") == 0) {
		
		String f = Amato.db.getDBFileName();
		
		if (f == null) {
		    FilePicker fp = new FilePicker (s);
		    f = fp.getSelectedFilePath ();
		}
		
		save_as (f);
	    }
	}
    }
}
