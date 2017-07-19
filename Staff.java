/**
 * $Id: Staff.java,v 1.3 2005/08/21 19:06:23 nicks Exp $
 * 
 * Originally part of the AMATO application.
 * 
 * @author Nick Seidenman <nick@seidenman.net>
 * @version $Revision: 1.3 $
 *
 * $Log: Staff.java,v $
 * Revision 1.3  2005/08/21 19:06:23  nicks
 * Changed comment header to conform with javadoc conventions.
 *
 * Revision 1.2  2005/08/15 14:26:29  nicks
 * Changed as_* to to* (more java-esque)
 *
 * Revision 1.1  2005/07/28 21:42:51  nicks
 * Initial revision
 *
 */

import java.util.*;
import java.io.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

public class Staff extends Constituent
{
    private static final long serialVersionUID = 20050728112720L;
    
    private ArrayList<String> voice;
    private String fach;
    private String level;
    private String function = "staff";

    public Staff (HashMap aMap)
    {
	super (aMap);
    }

    public String toXML ()
    {
	String result = new String ("<staff ");

	result += super.toXML ();

	result += "/>";

	return result;
    }

    public String toHTML ()
    {
	return super.toHTML ();
    }
	
    final public String getFunction () { return function; }
    
}
