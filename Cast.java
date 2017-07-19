/**
 * $Id: Cast.java,v 1.18 2005/09/09 22:32:46 nicks Exp nicks $
 *
 * Contains information about cast member for particular performance.
 * Performance is essentially a container class (among other things)
 * for Casts.
 * 
 * Originally part of the AMATO application.
 * 
 * @author Nick Seidenman <nick@seidenman.net>
 * @version $Revision: 1.18 $
 *
 * $Log: Cast.java,v $
 * Revision 1.18  2005/09/09 22:32:46  nicks
 * Changed all remaining explicit calls to JOptionPanel to Amato.errorPopup.
 *
 * Revision 1.17  2005/09/07 15:47:08  nicks
 * Now able to edit performances.  Will proceed to add this capability
 * to other Add*Frame.java components.
 *
 * Revision 1.16  2005/08/16 21:17:27  nicks
 * Changed comment header to comply with javadoc convention.
 *
 * Revision 1.15  2005/08/16 02:18:02  nicks
 * Cast was adding itself to Performance.  Now all instances
 * of this call Performance.add_cast.
 *
 * Revision 1.14  2005/08/15 14:26:28  nicks
 * Changed as_* to to* (more java-esque)
 *
 * Revision 1.13  2005/07/22 22:12:39  nicks
 * Started work on Drag-n-Drop within RosterPanel.
 * Cleaned up nearly all warnings.  Only a few left in Opera.java, Operas.java,
 * and Performance.java and these pertain to strict type-checking ala 1.5.
 * Just wanted to check everything in now as a check point.  I'll continue
 * to work on getting a totally clean compilation.
 *
 * Revision 1.12  2005/07/17 11:48:35  nicks
 * toJPanel instead of as_JLabel (instead of toHTML).
 *
 * Revision 1.11  2005/07/14 22:27:40  nicks
 * Replaced println messages with ErrorDialog box.
 *
 * Revision 1.10  2005/06/29 19:51:37  nicks
 * Added conductor(s) to performance(s).
 *
 * Revision 1.9  2005/06/24 22:31:14  nicks
 * Added constructor for making copies of Cast instances.
 *
 * Revision 1.8  2005/06/23 19:43:28  nicks
 * File operations (other than Print) now working.
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
 * Revision 1.1  2005/06/15 20:23:07  nicks
 * Initial revision
 *
 */
			       

import java.io.*;
import java.util.*;
import javax.swing.*;

public class Cast
    implements Serializable
{ 
    private static final long serialVersionUID = 20050915163000L;
    
    private Performance perf;
    private Opera opera;
    private Role role;
    private Constituent actor;
    private String call;
    private boolean isCover;
    
    public Cast (Performance perf, Roster roster, HashMap aMap) 
    {
	this.perf = perf;

	String first_name = (String) aMap.get ("first_name");
	String last_name = (String) aMap.get ("last_name");
	actor = null;

	Object[] cmList = roster.findByName (first_name, last_name);
	if (cmList.length == 0) {
	    Amato.errorPopup ("Cast member \"" + first_name + " " + last_name + "\"" +
			      " isn't on the roster. Ignored.");
	    return;
	}
	// Just take the first person we find. 
	actor = (Constituent) cmList[0];

	call = (String) aMap.get ("call");
	isCover = false;

	if (call == null || !call.equals ("cover")) {
	    call = new String ("on");
	    isCover = false;
	}
	else {
	    call = new String ("cover");
	    isCover = true;
	}
	
	opera = this.perf.getProduction().getOpera();
	if (opera != null) {
	    role = opera.getRoleByName ((String) aMap.get ("role"));
	}
	else {
	    Amato.errorPopup ("No entry found for specified opera.\n" +
			      "Ignored.");
	    return;
	}
    }

    public Cast (Cast c) // Make a copy of Cast instance.
    {
	this.perf = c.perf;
	actor = c.actor;
	call = c.call;

	if (call == null || (!(call.equals ("on") || call.equals ("cover")))) {
	    call = new String ("on");
	    isCover = false;
	}
	else {
	    call = new String ("cover");
	    isCover = true;
	}

	opera = c.opera;
	role = c.role;
    }

    final public String toXML ()
    {
	String result;
	
	result = "<cast first_name=\"" + getFirstName() + "\"" +
	    " last_name=\"" + getLastName() + "\"" +
	    " role=\"" + role.getName() + "\"" +
	    " call=\"" + call + "\"/>";
	
	return result;
    }

    final public Role getRole ()
    {
	return role;
    }

    final public String getFirstName () { return actor.getFirstName(); }

    final public String getLastName () { return actor.getLastName(); }

    final public String getName () { return actor.toString(); }

    final public Constituent getActor () { return actor; }

    final public String getCall () { return call; }

    final public boolean equals (Constituent c) { return actor.equals (c); }
    
    final public boolean isCover () { return isCover; }
}

