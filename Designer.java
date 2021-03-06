/**
 * $Id: Designer.java,v 1.5 2005/09/07 17:06:46 nicks Exp $
 * 
 * Originally part of the AMATO application.
 * 
 * @author Nick Seidenman <nick@seidenman.net>
 * @version $Revision: 1.5 $
 *
 * $Log: Designer.java,v $
 * Revision 1.5  2005/09/07 17:06:46  nicks
 * Deprecated the explicit argument form of the Constituent constructor.
 * Now all constructors just pass the HashMap aMap instance.
 *
 * Revision 1.4  2005/08/17 20:35:23  nicks
 * Converted comment header to comply with javadoc conventions.
 *
 * Revision 1.3  2005/08/15 14:26:28  nicks
 * Changed as_* to to* (more java-esque)
 *
 * Revision 1.2  2005/07/22 22:12:39  nicks
 * Started work on Drag-n-Drop within RosterPanel.
 * Cleaned up nearly all warnings.  Only a few left in Opera.java, Operas.java,
 * and Performance.java and these pertain to strict type-checking ala 1.5.
 * Just wanted to check everything in now as a check point.  I'll continue
 * to work on getting a totally clean compilation.
 *
 * Revision 1.1  2005/06/22 20:47:43  nicks
 * Initial revision
 *
 *
 */


import java.io.*;
import java.util.*;

public class Designer extends Constituent
{
    private static final long serialVersionUID = 20050722112710L;

    private String function = "designer";

    public Designer (HashMap aMap)
    {
	super (aMap);
    }

    public String toXML ()
    {
	String result = new String ("<designer ");
	
	result += super.toXML ();

	result += "/>";

	return result;
    }

    final public String getFunction () { return function; }
}
