// $Id: DateButton.java,v 1.1 2005/07/27 18:52:52 nicks Exp $
// 
// Makes DatePicker a little easier to use.  I'll probably wind up combining
// these at some point.
//
// Originally part of the AMATO application.
// 
// Nick Seidenman <nick@seidenman.net>
//
// $Log: DateButton.java,v $
// Revision 1.1  2005/07/27 18:52:52  nicks
// Initial revision
//
//

import java.util.*;
import java.io.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

public class DateButton extends JButton
    implements ActionListener
{
    private static final long serialVersionUID = 20050727112850L;
    
    private String dateStr;
    
    DateButton (String str)
    {
	super (str);
	
	dateStr = str;
	
	addActionListener (this);
    }
    
    public void actionPerformed (ActionEvent e)
    {
	final JButton me = this;
	
	Runnable getStartDateDialog = new Runnable () {
		public void run() { 
		    DatePicker dp = new DatePicker (me);
		}
	    };
	SwingUtilities.invokeLater (getStartDateDialog);
    }

    final public String toString () { return getText (); }
}

