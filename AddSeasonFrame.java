/***
 * $Id: AddSeasonFrame.java,v 1.9 2005/09/09 22:23:59 nicks Exp $
 * 
 * Dialog that acquires information about new season being added.
 *
 * Originally part of the AMATO application.
 * 
 * @author Nick Seidenman <nick@seidenman.net>
 * @version $Revision: 1.9 $
 *
 * $Log: AddSeasonFrame.java,v $
 * Revision 1.9  2005/09/09 22:23:59  nicks
 * Now has date sanity checks:
 *   - season start must come before season end
 *   - production start must come before season end
 *   - production dates must fall within season dates
 *   - performance date must fall within production dates
 *
 * Revision 1.8  2005/09/08 15:37:40  nicks
 * Seasons, Productions, Performances, Constituents, and Operas are now
 * all editable!
 *
 * Revision 1.7  2005/08/15 15:22:50  nicks
 * Converted date pickers to UICDateEdit.  (better widget)
 *
 * Revision 1.6  2005/08/02 21:46:55  nicks
 * Frame now comes up more or less in the middle of the user's screen.
 *
 * Revision 1.5  2005/07/22 22:12:39  nicks
 * Started work on Drag-n-Drop within RosterPanel.
 * Cleaned up nearly all warnings.  Only a few left in Opera.java, Operas.java,
 * and Performance.java and these pertain to strict type-checking ala 1.5.
 * Just wanted to check everything in now as a check point.  I'll continue
 * to work on getting a totally clean compilation.
 *
 * Revision 1.4  2005/07/14 18:21:50  nicks
 * Seasons (and the SeasonPanel.tree structure) are now managed using
 * a TreeModel paradigm.  Works VERY nice.  This is the JAVA way to do things!
 *
 * Revision 1.3  2005/07/12 15:37:58  nicks
 * Start/End date fields are now buttons that activate a DatePicker.
 *
 * Revision 1.2  2005/07/06 20:57:18  nicks
 * Took out debug cruft.
 *
 * Revision 1.1  2005/07/06 20:53:30  nicks
 * Initial revision
 *
 */

import java.io.*;
import java.util.*;
import java.text.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import uic.*;
import uic.widgets.*;
import uic.widgets.calendar.*;

public class AddSeasonFrame extends JFrame
				     implements ActionListener
{
    private static final long serialVersionUID = 20050702162740L;
    
    private JLabel season_name_lbl;
    private JLabel season_start_lbl;
    private JLabel season_end_lbl;

    private JTextField season_name;
    private UICDateEdit season_start;
    private UICDateEdit season_end;
    
    private JButton okButton;
    private JButton cancelButton;

    private JOptionPane pane;

    private Season replacing;

    public AddSeasonFrame ()
    {
	super ("Add Season");

	replacing = null;

	setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
	
	Calendar today = Calendar.getInstance ();
	String dateStr = (today.get (Calendar.MONTH) + 1) + "/" +
	    today.get (Calendar.DATE) + "/" + today.get (Calendar.YEAR);
	
	season_name_lbl = new JLabel ("Season Name", JLabel.CENTER);
	season_start_lbl = new JLabel ("Season Start", JLabel.CENTER);
	season_end_lbl = new JLabel ("Season End", JLabel.CENTER);

	season_name = new JTextField (40);
	season_name.setEditable (true);

	season_start = new UICDateEdit (Locale.US, Calendar.getInstance ());
	season_end = new UICDateEdit (Locale.US, Calendar.getInstance ());

	okButton = new JButton ("OK");
	cancelButton = new JButton ("CANCEL");

	JPanel sdp = new JPanel (new GridLayout (0,2));
	sdp.add (season_start_lbl);
	sdp.add (season_start);
				 
	JPanel sep = new JPanel (new GridLayout (0,2));
	sep.add (season_end_lbl);
	sep.add (season_end);
				 
	JPanel asp = new JPanel (new GridLayout (0, 1));

	asp.add (season_name_lbl);
	asp.add (season_name);

	asp.add (sdp);
	asp.add (sep);

	JPanel buttons = new JPanel (new GridLayout (0, 2));

	okButton.addActionListener (this);
	buttons.add (okButton);

	cancelButton.addActionListener (this);
	buttons.add (cancelButton);

	asp.add (buttons);
	
	getContentPane().add (asp);

	pack ();

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	Dimension panelSize = getContentPane().getSize ();
	setLocation (screenSize.width/2 - (panelSize.width/2), 
		     screenSize.height/2 - (panelSize.height/2));

	setVisible (true);
    }

    public void actionPerformed (ActionEvent e)
    {
	if (e.getSource() == okButton) {
	    String n = season_name.getText();

	    // Make sure we set something for the season name.
	    n.trim();
	    if (n.length() == 0) {
		Amato.errorPopup ("Season name cannot be blank!");
		return;
	    }

	    // Make sure the end date is after the start date.
	    Date sDate = season_start.getSelectedCalendar().getTime();
	    Date eDate = season_end.getSelectedCalendar().getTime();

	    if (eDate.before (sDate)) {
		Amato.errorPopup ("Season end date must come\nAFTER\nseason start date.");
		return;
	    }

	    HashMap<String,String> h = new HashMap<String,String> ();
	    h.put ("name", season_name.getText());
	    h.put ("startDate", Amato.getDateText (sDate));
	    h.put ("endDate", Amato.getDateText (eDate));
	    
	    Season s = new Season (h);

	    Amato.db.getSeasons().add (s);

	    // Get rid of the original if we're editing.
	    if (replacing != null) {
		// Copy all the productions here.
		int children = replacing.getChildCount ();
		while (children > 0) {
		    DefaultMutableTreeNode tn = (DefaultMutableTreeNode) replacing.getChildAt (0);
		    replacing.remove (tn);
		    s.add (tn);
		    children = replacing.getChildCount ();
		}
		Amato.db.getSeasons().remove (replacing);
		replacing = null;
		Amato.redraw();
	    }

	    dispose ();
	}

	else if (e.getSource() == cancelButton) {
	    dispose ();
	}
	
    }

    /**
     * After creating the frame, use this method to populate it
     * with values from an existing Season instance.  This way
     * we can edit the entries.
     */
    public void populate (Season s)
    {
	// Fill in all the basic (read: easy) stuff.
        season_name.setText (s.getName());
	
	// Fill in the opening/closing dates.
	SimpleDateFormat df = new SimpleDateFormat (Amato.dateFormatStr);
	Date d1 = df.parse (s.getStartDateText(), new ParsePosition (0));
	Calendar c1 = df.getCalendar();
	season_start.setSelectedCalendar (c1);

	Date d2 = df.parse (s.getEndDateText(), new ParsePosition (0));
	Calendar c2 = df.getCalendar();
	season_end.setSelectedCalendar (c2);


	// Change the titlebar title.
	setTitle ("Edit Season: " + s.getName());
	replacing = s;
    }

    public static void main (String[] args)
    {
	AddSeasonFrame asf = new AddSeasonFrame ();
    }
}


