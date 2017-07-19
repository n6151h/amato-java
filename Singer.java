/**
 * $Id: Singer.java,v 1.16 2005/09/12 17:36:44 nicks Exp $
 *
 * Originally part of the AMATO application.
 * 
 * @author Nick Seidenman <nick@seidenman.net>
 * @version $Revision: 1.16 $
 *
 * $Log: Singer.java,v $
 * Revision 1.16  2005/09/12 17:36:44  nicks
 * If a singer is being cast in more than one role, a pop-up warning
 * is displayed and the user is asked if this is ok.  If it is, the singer
 * is (n-tuple cast).
 *
 * Revision 1.15  2005/09/08 15:37:40  nicks
 * Seasons, Productions, Performances, Constituents, and Operas are now
 * all editable!
 *
 * Revision 1.14  2005/09/07 17:06:46  nicks
 * Deprecated the explicit argument form of the Constituent constructor.
 * Now all constructors just pass the HashMap aMap instance.
 *
 * Revision 1.13  2005/08/21 19:06:23  nicks
 * Changed comment header to conform with javadoc conventions.
 *
 * Revision 1.12  2005/08/15 13:48:17  nicks
 * Several mods to support AddPerformanceFrame.
 * Changed as_* to to* (more java-esque.)
 *
 * Revision 1.11  2005/08/03 18:33:53  nicks
 * Now able to add Singers, Musicians, Conductors, Directors, Staff on RosterPanel pop-up menu.
 *
 * Revision 1.10  2005/07/28 21:42:21  nicks
 * Added comment header.
 *
 */

import java.util.*;
import java.io.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

public class Singer extends Constituent
{
    private static final long serialVersionUID = 20050722112720L;
    
    private ArrayList<String> voice;
    private String fach;
    private String level;
    private String function = "singer";

    public Singer (HashMap aMap)
    {
	super (aMap);

	fach = (String) aMap.get ("fach");
	level = (String) aMap.get ("level");

	voice = new ArrayList<String>();

	String[] voxen = ((String) aMap.get ("voice")).split (",[ \t]*");

	for (String v : voxen) {
	    add_voice (v);
	}
    }

    public void add_voice (String vox)
    {
	voice.add (vox);
    }

    public String toXML ()
    {
	String result = new String ("<singer");
	String vlist = (String) voice.toString();

	result += super.toXML ();

	result += " voice=\"" + 
	    getVoicesText() + "\"" + 
	    " fach=\"" + ((fach != null) ? fach : "") + "\"" + 
	    " level=\"" + ((level != null) ? level : "") + "\"";

	result += "/>";

	return result;
    }

    public String toHTML ()
    {
	String result[] = super.toHTML ().split ("\\</h1\\>");
	String res;

	res = result[0] + "</h1>\n<b>Voice:</b> " + 
	    getVoicesText() + 
	    ((fach != null) ? "<br><b>Fach:</b> " + fach + "<br>\n" : "<br>\n") +
	    ((level != null) ? "<b>Level:</b> " + level : "") + 
	     "<p>\n" + result[1];
	    
	return res;
    }
	
    final public String getFunction () { return function; }

    final public String getVoicesText () 
    { 
	return voice.toString().substring(1, voice.toString().length()-1);
    }

    final public String[] getVoiceArray ()
    {
	return voice.toArray (new String[0]);
    }

    final public String getProficiency () { return (level != null) ? level : ""; }

    final public ArrayList<String> getVoices () { return voice; }

    final boolean hasVoice (String v)
    {
	for (String vox: voice) {
	    if (v.equals (vox))
		return true;
	}
	
	return false;
    }
}
