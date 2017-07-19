/**
 * $Id: Conductor.java,v 1.9 2005/09/07 17:06:46 nicks Exp $
 *
 * Information about Conductors. (Subclass of Constituent.)
 * 
 * Originally part of the AMATO application.
 * 
 * @author Nick Seidenman <nick@seidenman.net>
 * @version $Revision: 1.9 $
 *
 * $Log: Conductor.java,v $
 * Revision 1.9  2005/09/07 17:06:46  nicks
 * Deprecated the explicit argument form of the Constituent constructor.
 * Now all constructors just pass the HashMap aMap instance.
 *
 * Revision 1.8  2005/08/16 21:17:27  nicks
 * Changed comment header to comply with javadoc convention.
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
 * Revision 1.5  2005/06/22 20:48:28  nicks
 * Removed the deprecated constructor (the one that didn't use
 * the aMap argument.)
 *
 * Revision 1.4  2005/06/22 18:34:48  nicks
 * Fixed getFunction.
 *
 * Revision 1.3  2005/06/18 22:41:51  nicks
 * RosterPanel now working.
 * Several changes to the toHTML methods.
 * Fixed parsing in Musician so that list of instruments is properley
 *  split along comma (,) delimiters.
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

public class Conductor extends Constituent
{
    private static final long serialVersionUID = 20050722112800L;

    private String function = "conductor";

    public Conductor (HashMap aMap)
    {
	super (aMap);
    }

    public String toXML ()
    {
	String result = new String ("<conductor");
	
	result += super.toXML ();

	result += "/>";

	return result;
    }

    final public String getFunction () { return function; }
}
