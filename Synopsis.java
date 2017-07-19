/**
 * $Id: Synopsis.java,v 1.6 2005/09/14 18:55:44 nicks Exp nicks $
 * 
 * Originally part of the AMATO application.
 * 
 * @author Nick Seidenman <nick@seidenman.net>
 * @version $Revision: 1.6 $
 *
 * $Log: Synopsis.java,v $
 * Revision 1.6  2005/09/14 18:55:44  nicks
 * Completely re-worked Opera.toJPanel.  Now uses pure java; no html.
 * This also means that line-wrap works in the synopsis area.
 *
 * Revision 1.5  2005/08/21 19:06:23  nicks
 * Changed comment header to conform with javadoc conventions.
 *
 * Revision 1.4  2005/08/15 14:26:29  nicks
 * Changed as_* to to* (more java-esque)
 *
 * Revision 1.3  2005/06/17 17:11:19  nicks
 * First (cheesy) integration of GUI (OperaPanel) with the database (AmatoDB).
 *
 * Revision 1.2  2005/06/15 14:52:59  nicks
 * *** empty log message ***
 *
 */


import java.util.*;
import java.io.*;
import java.text.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.border.*;


public class Synopsis
    implements Serializable
{
    private static final long serialVersionUID = 20050915163010L;

    private String language;
    private String text;

    public Synopsis (String language, String text)
    {
	this.language = language;
	this.text = Amato.unescape(text);
    }

    public String toXML ()
    {
	String result;
	
	result = "<synopsis language=\"" + language + "\">";
	result += Amato.escape (text);
	result += "</synopsis>";
	
	return result;
    }

    public String toHTML () {
	return text;
    }

    public JPanel toJPanel () {
	JPanel p = new JPanel (new GridLayout(0, 1));
	JTextArea jta = new JTextArea (text.trim());
	jta.setBackground(p.getBackground());
	//jta.setFont (Amato.PLAIN);
	jta.setEditable (false);
	jta.setLineWrap (true);
	jta.setWrapStyleWord (true);
	jta.setColumns (40);
	
	p.add (jta);
	return p;
    }

    public JTextArea toJTextArea () {
	JTextArea jta = new JTextArea (text);
	jta.setEditable (false);
	jta.setLineWrap (true);
	jta.setWrapStyleWord (true);
	return jta;
    }

    final public String getText () { return text; }
}

