/**
 * $Id: Role.java,v 1.9 2005/08/21 19:06:23 nicks Exp nicks $
 * 
 * Originally part of the AMATO application.
 * 
 * @author Nick Seidenman <nick@seidenman.net>
 * @version $Revision: 1.9 $
 *
 * $Log: Role.java,v $
 * Revision 1.9  2005/08/21 19:06:23  nicks
 * Changed comment header to conform with javadoc conventions.
 *
 * Revision 1.8  2005/08/15 14:26:28  nicks
 * Changed as_* to to* (more java-esque)
 *
 * Revision 1.7  2005/06/22 21:41:53  nicks
 * Cheap, demo version of cast selection working. (Will use real
 * java stuff, not this crappy html vaporware.)
 * Had to make the AmatoDB object globally visible for this
 * sort of thing to work.  I hate doing it, but I guess there's no
 * harm for the moment since this is a self-contained application.
 *
 * Revision 1.6  2005/06/20 22:34:38  nicks
 * SeasonPanel is now working in a cheesy, demo-only sort of way.  It's
 * at least enough for me to get the idea across to Irene and talk
 * through how it will work when finished.d
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
 * Revision 1.3  2005/06/17 17:11:19  nicks
 * First (cheesy) integration of GUI (OperaPanel) with the database (AmatoDB).
 *
 * Revision 1.2  2005/06/16 15:25:05  nicks
 * Added object_name (HashMap aMap) constructor so that I can simply pass
 * the attribute map from the parser without the parser itself ever
 * needing to know such details as which attributes are required and
 * which are optional.  This is a slicker, cleaner, more OO way to
 * handle XML tag attributes.
 *
 * Revision 1.1  2005/06/15 14:51:08  nicks
 * Initial revision
 *
 */


import java.io.*;
import java.util.*;



public class Role
    implements Serializable
{
    private static final long serialVersionUID = 20050915163020L;

    private Opera opera;
    private String name;
    private String voice;
    private String order;

    public Role (Opera opera, HashMap aMap)
    {
	this.opera = opera;

	name = (String) aMap.get ("name");
	voice = (String) aMap.get ("voice");
	order = (String) aMap.get ("order");
    }

    public String toXML ()
    {
	String result;
	
	result = "<role name=\"" + name + "\"" +
	    " voice=\"" + voice + "\"" + 
	    " order=\"" + order + "\"/>"; 
	
	return result;
    }
    
    public String toHTML_tr ()
    {
	return new String ("<tr><td>" + name + 
			   "</td><td align='right'>" + 
			   voice + "</td></tr>");
    }

    public int getOrder ()
    {
	return Integer.parseInt (order);
    }

    public String getName ()
    {
	return name;
    }
    
    public String getVoice ()
    {
	return voice;
    }
    
    public Opera getOpera ()
    {
	return opera;
    }
}

