/**
 * $Id: Musician.java,v 1.11 2005/09/12 17:36:44 nicks Exp $
 * 
 * Originally part of the AMATO application.
 * 
 * @author Nick Seidenman <nick@seidenman.net>
 * @version $Revision: 1.11 $
 *
 * $Log: Musician.java,v $
 * Revision 1.11  2005/09/12 17:36:44  nicks
 * If a singer is being cast in more than one role, a pop-up warning
 * is displayed and the user is asked if this is ok.  If it is, the singer
 * is (n-tuple cast).
 *
 * Revision 1.10  2005/09/08 15:37:40  nicks
 * Seasons, Productions, Performances, Constituents, and Operas are now
 * all editable!
 *
 * Revision 1.9  2005/09/07 17:06:46  nicks
 * Deprecated the explicit argument form of the Constituent constructor.
 * Now all constructors just pass the HashMap aMap instance.
 *
 * Revision 1.8  2005/08/21 19:06:23  nicks
 * Changed comment header to conform with javadoc conventions.
 *
 * Revision 1.7  2005/08/15 14:26:28  nicks
 * Changed as_* to to* (more java-esque)
 *
 * Revision 1.6  2005/07/22 22:12:39  nicks
 * Started work on Drag-n-Drop within RosterPanel.
 * Cleaned up nearly all warnings.  Only a few left in Opera.java, Operas.java,
 * and Performance.java and these pertain to strict type-checking ala 1.5.
 * Just wanted to check everything in now as a check point.  I'll continue
 * to work on getting a totally clean compilation.
 *
 * Revision 1.5  2005/06/22 18:34:48  nicks
 * Fixed getFunction.
 *
 * Revision 1.4  2005/06/18 22:41:51  nicks
 * RosterPanel now working.
 * Several changes to the toHTML methods.
 * Fixed parsing in Musician so that list of instruments is properley
 *  split along comma (,) delimiters.
 *
 * Revision 1.3  2005/06/16 15:25:05  nicks
 * Added object_name (HashMap aMap) constructor so that I can simply pass
 * the attribute map from the parser without the parser itself ever
 * needing to know such details as which attributes are required and
 * which are optional.  This is a slicker, cleaner, more OO way to
 * handle XML tag attributes.
 *
 * Revision 1.2  2005/06/15 21:31:47  nicks
 * All tags seem to be working.
 *
 * Revision 1.1  2005/06/15 14:51:08  nicks
 * Initial revision
 *
 */


import java.io.*;
import java.util.*;

public class Musician extends Constituent
{
    private static final long serialVersionUID = 20050722112730L;
    
    private ArrayList<String> instruments;
    private String function = "musician";
    
    public Musician (HashMap aMap)
    {
	super (aMap);

	instruments = new ArrayList<String>();
	
	String insts[] = ((String) aMap.get ("instrument")).split (",[ \t]*");
	//	for (int i=0; i < insts.length; i++) {
	for (String inst : insts) {
	    add_instrument (inst);
	}
    }

    public void add_instrument (String inst)
    {
	instruments.add (inst);
	Collections.sort (instruments);
    }

    public String toXML ()
    {
	String result = new String ("<musician");
	String inst = "" + instruments;

	result += super.toXML ();

	result += " instrument=\"" + inst.substring(1,inst.length()-1) + "\"";
	result += "/>";

	return result;
    }

    final public String[] getInstrumentsArray () 
    {
	return instruments.toArray (new String[0]);
    }
    
    public String toHTML ()
    {
	String result[] = super.toHTML ().split ("\\</h1\\>");
	String res;

	res = result[0] + "</h1>\n" + "<b>Instrument(s):</b> \n<ul>";

	for (String inst: instruments) {
	    res += "<li>" + inst + "</li>\n";
	}
	
	res += "</ul>\n" + result[1];

	return res;
    }

    final public String getFunction () { return function; }
}
