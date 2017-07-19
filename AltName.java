/**
 * $Id: AltName.java,v 1.4 2005/08/16 21:17:27 nicks Exp nicks $
 * 
 * Originally part of the AMATO application.
 * 
 * @author Nick Seidenman <nick@seidenman.net>
 * @version $Revision: 1.4 $
 *
 * $Log: AltName.java,v $
 * Revision 1.4  2005/08/16 21:17:27  nicks
 * Changed comment header to comply with javadoc convention.
 *
 * Revision 1.3  2005/08/15 14:26:28  nicks
 * Changed as_* to to* (more java-esque)
 *
 * Revision 1.2  2005/06/16 15:25:05  nicks
 * Added object_name (HashMap aMap) constructor so that I can simply pass
 * the attribute map from the parser without the parser itself ever
 * needing to know such details as which attributes are required and
 * which are optional.  This is a slicker, cleaner, more OO way to
 * handle XML tag attributes.
 *
 * Revision 1.1  2005/06/15 17:27:04  nicks
 * Initial revision
 *
 */

import java.io.*;
import java.util.*;

public class AltName
    implements Serializable
{
    private static final long serialVersionUID = 20050915163030L;

    private String name;
    private String language;

    public AltName (String name, String language)
    {
	this.name = name;
	this.language = language;
    }

    public AltName (HashMap aMap)
    {
	name = (String) aMap.get ("name");
	language = (String) aMap.get ("language");
    }

    public String toXML ()
    {
	String result;
	
	result = "<alternateName name=\"" + name + "\"" +
	    " language=\"" + language + "\"/>";
	
	return result;
    }
}


