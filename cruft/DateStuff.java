/**
 *
 * $Id$
 * 
 * Play around with Date and DateFormat classes.  This was spurred by an
 * article on builder.com (http://builder.com/5102-6370-1046410.html) on 
 * how to use these (and the Calendar) classes.
 *
 * Originally part of the AMATO application.
 * 
 * @author Nick Seidenman <nick@seidenman.net>
 * @version $Revision$
 *
 * $Log$
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


public class DateStuff
{
    public DateStuff () {}

    static public void main (String[] args)
    {
	SimpleDateFormat dp = new SimpleDateFormat ("m/d/y");
	Date d = null;

	try { d = dp.parse (args[0]); }
	catch (Throwable e) { System.err.println (e); System.exit (1); }

	System.out.println (d.getTime());

	SimpleDateFormat df = new SimpleDateFormat ("mm-dd-yyyy");

	System.out.println (df.format (d));
    }
}
