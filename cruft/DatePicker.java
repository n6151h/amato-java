// $Id: DatePicker.java,v 1.9 2005/08/01 22:19:45 nicks Exp $
//
// Date picker pop-up.  This is a brutal, ugly, first-hack at
// such a tool.  It's not the final form, but I was able to grok 
// this up on my own in just a few hours. I'll learn the right (java)
// way to do this at some point.  Remember ... this whole project is a
// LEARNING experience.  I'm indeed learning what I need to learn more
// about ... (sigh!)
// 
// Originally part of the AMATO application.
// 
// Nick Seidenman <nick@seidenman.net>
//
// $Log: DatePicker.java,v $
// Revision 1.9  2005/08/01 22:19:45  nicks
// Added code to center the frame more or less within the user's display.
//
// Revision 1.8  2005/07/27 18:53:16  nicks
// Cleaned out some debug cruft.
//
// Revision 1.7  2005/07/23 00:29:13  nicks
// Date offset problem(s) solved!
//
// Revision 1.6  2005/07/22 22:12:39  nicks
// Started work on Drag-n-Drop within RosterPanel.
// Cleaned up nearly all warnings.  Only a few left in Opera.java, Operas.java,
// and Performance.java and these pertain to strict type-checking ala 1.5.
// Just wanted to check everything in now as a check point.  I'll continue
// to work on getting a totally clean compilation.
//
// Revision 1.5  2005/07/18 22:44:01  nicks
// Added add/delete pop-up to RosterPanel and OperaPanel.
// Still need to get RosterPanel fully working, but this
// will depend (or, rather, how I do it will depend) on whether
// I end up implementing some of the ideas mentioned in the TODO list.
// (e.g. have subtrees under Singers for different voices.)
//
// Revision 1.4  2005/07/14 20:33:03  nicks
// Thought I could use the complete() method of Calendar, but it's protected.
//
// Revision 1.3  2005/07/14 20:21:02  nicks
// Buttons look less like buttons.  This looks more like a calendar now.
// Odd bug in setting the offset for the first day of the month.  See
// comments toward actionPerformed for more details.
//
// Revision 1.2  2005/07/12 15:37:58  nicks
// Now checks to see if text attribute of the JButton is non-null.  If so,
// it initializes the date from that.
//
// Revision 1.1  2005/07/11 21:53:41  nicks
// Initial revision
//
//

import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.color.*;
import java.awt.event.*;
import javax.swing.table.*;

public class DatePicker extends JFrame 
    implements ActionListener
{
    private static final long serialVersionUID = 20050722112700L;

    private String[] months = {"January", "February", "March",
			       "April", "May", "June",
			       "July", "August", "September",
			       "October", "November", "December" };
    
    private int[] daysInMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    
    private String[] years = {"2000", "2001", "2002", "2003", "2004", "2005", "2006",
			      "2007", "2008", "2009", "2010", "2011", "2012", "2013"};

    private String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

    private JPanel month_panel;

    private JComboBox monthCB;
    private JComboBox yearCB;
    private JTable monthTbl;

    private JButton okButton;
    private JButton cancelButton;

    private JToggleButton lastDayButton;

    private int mo;
    private int dy;
    private int yr;

    private String dateStr;
    
    JButton dbs;

    final public static Color blankLabelBG = new Color (0x206020);

    final public static Color dateFG = new Color (0x000000);
    final public static Color dateBG = new Color (0xd0d0d0);

    final public static Color selectedDateFG = new Color (0xffffff);
    final public static Color selectedDateBG = new Color (0x902020);


    public DatePicker (JButton dbs)
    {
	super ();

	this.dbs = dbs;

	lastDayButton = null;

	Calendar today = Calendar.getInstance();

	if (dbs.getText() != null) {
	    String[] mdy = dbs.getText().split ("(/|-)");
	    
	    today.set(Calendar.MONTH, new Integer (mdy[0])-1);
	    today.set(Calendar.DAY_OF_MONTH, new Integer (mdy[1]));
	    today.set(Calendar.YEAR, new Integer (mdy[2]));
	}

	mo = today.get(Calendar.MONTH);
	dy = today.get(Calendar.DAY_OF_MONTH);
	yr = today.get(Calendar.YEAR);
	
	dateStr = "" + (mo + 1) + "/" + dy + "/" + yr;

	setTitle (dateStr);

	monthCB = new JComboBox (months);
	monthCB.setSelectedIndex (today.get (Calendar.MONTH));
	yearCB = new JComboBox (years);
	yearCB.setSelectedIndex (today.get (Calendar.YEAR) - 2000);

	setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
	
	GridBagLayout gb = new GridBagLayout ();
	GridBagConstraints gbc = new GridBagConstraints ();

	setLayout (gb);

	JPanel my_panel = new JPanel (new GridLayout (0, 2));

	my_panel.add (monthCB);
	my_panel.add (yearCB);

	monthCB.addActionListener (this);
	yearCB.addActionListener (this);
	
	gbc.fill = GridBagConstraints.BOTH;
	gbc.gridwidth = GridBagConstraints.REMAINDER;
	gb.setConstraints (my_panel, gbc);
	add (my_panel);

	okButton = new JButton ("OK");
	cancelButton = new JButton ("CANCEL");
	
	month_panel = new JPanel (new GridLayout (0, 7));
	
	setMonth (month_panel, today.get (Calendar.DAY_OF_WEEK), 
		  daysInMonth[today.get (Calendar.MONTH)]);

	gbc.fill = GridBagConstraints.BOTH;
	gbc.gridwidth = GridBagConstraints.REMAINDER;
	gb.setConstraints (month_panel, gbc);
	add (month_panel);

	JPanel buttons = new JPanel (new GridLayout (0, 2));

	okButton.addActionListener (this);
	buttons.add (okButton);

	cancelButton.addActionListener (this);
	buttons.add (cancelButton);
	
	gbc.fill = GridBagConstraints.BOTH;
	gbc.gridwidth = GridBagConstraints.REMAINDER;
	gb.setConstraints (buttons, gbc);
	add (buttons);

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	Dimension widgetSize = getContentPane().getSize ();
	setLocation (screenSize.width/2 - (widgetSize.width/2), 
		     screenSize.height/2 - (widgetSize.height/2));

	pack ();
	setVisible (true);
    }

    public void setMonth (JPanel mp, int offset, int days)     {
	mp.setVisible (false);
	mp.removeAll();

	// Day-of-Week for the column headings.
	for (int i = 0; i < 7; i++) {
	    JLabel hl = new JLabel (daysOfWeek[i]);
	    hl.setHorizontalAlignment (SwingConstants.CENTER);
	    hl.setForeground (Color.white);
	    hl.setBackground (Color.black);
	    hl.setOpaque (true);
	    mp.add (hl);
	}

	// Offset the days prior to the first in the month.
	for (int i = 1; i < offset; i++) {
	    JLabel l = new JLabel ("");
	    l.setBackground (blankLabelBG);
	    l.setOpaque (true);
	    mp.add (l);
	}

	// Fill in the days of the month.
	for (int i = 0; i < days; i++) {
	    JToggleButton dl = new JToggleButton ("" + (i+1));
	    dl.setHorizontalAlignment (SwingConstants.RIGHT);
	    dl.addActionListener (this);
	    dl.setOpaque (true);
	    if (i == (dy - 1)) {
		dl.setSelected (true);
		dl.setForeground (selectedDateFG);
		dl.setBackground (selectedDateBG);
		lastDayButton = dl;
	    }
	    else {
		dl.setSelected (false);
		dl.setForeground (dateFG);
		dl.setBackground (dateBG);
	    }
	    mp.add (dl);
	    
	}

	// Fill in a few more just for good measure (and to leave room
	// for resizing when the month or year changes.
	for (int i = 0; i < (42 - offset - days + 1); i++) {
	    JLabel l = new JLabel ("");
	    l.setBackground (blankLabelBG);
	    l.setOpaque (true);
	    mp.add (l);
	}

	mp.setVisible (true);
    }

    public void printDate ()
    {
	setTitle (dateStr);
	// This is such an AWFUL hack, but it serves the purpose -- for now.
	// I fully intend to go back and REDO this the right (i.e., java) way
	// ... someday.
    }
    
    public void actionPerformed (ActionEvent e)
    {
	if (e.getSource() == okButton) {
	    printDate ();
	    dbs.setText (dateStr);
	    dispose ();
	}
	
	else if (e.getSource() == cancelButton) {
	    dispose ();
	}

	else if (e.getSource() == monthCB || e.getSource() == yearCB) {
	    Calendar choice = Calendar.getInstance ();

	    // Initialize a Calendar instance with the currently-selected
	    // month and year. Set the day of the month to 1.
	    choice.set (Calendar.DAY_OF_MONTH, 1);
	    choice.set (Calendar.MONTH, monthCB.getSelectedIndex());
	    choice.set (Calendar.YEAR, yearCB.getSelectedIndex() + 2000);
	    
	    // Set the new values for our internal concept of the month and year.
	    mo = choice.get(Calendar.MONTH);
	    yr = choice.get(Calendar.YEAR);

	    // We'll need these to compute leap year day, and to
	    // see if we can leave the current day-of-month selected or
	    // if we must change it due to it being greater than
	    // the number of days in the selected month.
	    int days = daysInMonth[choice.get (Calendar.MONTH)]; 

	    // Check for leap year.
	    if ((choice.get (Calendar.MONTH) == Calendar.FEBRUARY) &&
		(yr % 4) == 0 && (yr % 100) != 0) days += 1;

	    // The selected month has fewer days that the ordinal value
	    // of the selected day.
	    if (days < dy) dy = 1;
	    choice.set (Calendar.DAY_OF_MONTH, dy);

	    dateStr = "" + (mo + 1) + "/" + dy + "/" + yr;

	    setMonth (month_panel, choice.get(Calendar.DAY_OF_WEEK), days);
	    printDate ();
	}

	else {
	    lastDayButton.setSelected (false);
	    lastDayButton.setBackground (dateBG);
	    lastDayButton.setForeground (dateFG);

	    lastDayButton = (JToggleButton) e.getSource ();
	    lastDayButton.setSelected (true);
	    lastDayButton.setForeground (selectedDateFG);
	    lastDayButton.setBackground (selectedDateBG);

	    lastDayButton.updateUI ();

	    dy = new Integer (lastDayButton.getText());

	    dateStr = "" + (mo + 1) + "/" + dy + "/" + yr;
	    printDate ();
	}
    }

    final public String getDateString () { return dateStr; }

    final public String toString () { return dateStr; }
}

