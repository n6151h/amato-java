/**
 * $Id: AddProductionFrame.java,v 1.9 2005/09/09 22:23:59 nicks Exp $
 * 
 * Dialog that acquires information about new productions.
 *
 * Originally part of the AMATO application.
 * 
 * @author Nick Seidenman <nick@seidenman.net>
 * @version $Revision: 1.9 $
 *
 * $Log: AddProductionFrame.java,v $
 * Revision 1.9  2005/09/09 22:23:59  nicks
 * Now has date sanity checks:
 *   - season start must come before season end
 *   - production start must come before season end
 *   - production dates must fall within season dates
 *   - performance date must fall within production dates
 *
 * Revision 1.8  2005/09/09 21:24:51  nicks
 * All objects now store date/time in a Date instance.  This gets me
 * ready to be able to do some of the date/time sanity checks mentioned
 * in the TODO list.  (E.g., check to make sure performance date
 * is within the season start/end bounds.)
 *
 * Revision 1.7  2005/09/08 15:37:40  nicks
 * Seasons, Productions, Performances, Constituents, and Operas are now
 * all editable!
 *
 * Revision 1.6  2005/08/15 15:22:50  nicks
 * Converted date pickers to UICDateEdit.  (better widget)
 *
 * Revision 1.5  2005/08/01 22:14:29  nicks
 * Added code to more or less center the frame on the screen.
 *
 * Revision 1.4  2005/07/22 22:12:39  nicks
 * Started work on Drag-n-Drop within RosterPanel.
 * Cleaned up nearly all warnings.  Only a few left in Opera.java, Operas.java,
 * and Performance.java and these pertain to strict type-checking ala 1.5.
 * Just wanted to check everything in now as a check point.  I'll continue
 * to work on getting a totally clean compilation.
 *
 * Revision 1.3  2005/07/18 19:40:25  nicks
 * Can now add/delete seasons and productions.
 * Cleaned up code in SeasonPanel so that now this object implements
 * the ActionListener interface, rather than defining an internal class
 * to do it.  Also, AddProductionFrame will now take a Season instance
 * for its constructor argument so that the selected season will display
 * by default in the Season combobox.
 *
 * Revision 1.2  2005/07/14 18:21:50  nicks
 * Seasons (and the SeasonPanel.tree structure) are now managed using
 * a TreeModel paradigm.  Works VERY nice.  This is the JAVA way to do things!
 *
 * Revision 1.1  2005/07/12 19:17:30  nicks
 * Initial revision
 *
 *
 */

import java.io.*;
import java.util.*;
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

public class AddProductionFrame 
    extends JFrame
    implements ActionListener
{
    private static final long serialVersionUID = 20050712162740L;
    
    private JLabel season_name_lbl;
    private JComboBox season_name;

    private JLabel opera_name_lbl;
    private JComboBox opera_name;

    private JLabel libretto_lbl;
    private JComboBox libretto;
    private String[] libretto_list = {"Schirmer", "Ricordi", "Kalmus", "Boosey-Hawkes"};

    private JLabel production_start_lbl;
    private JLabel production_end_lbl;
    private UICDateEdit production_start;
    private UICDateEdit production_end;
    
    private JButton okButton;
    private JButton cancelButton;

    private JOptionPane pane;

    private Season selectedSeason;

    private Production replacing;

    public AddProductionFrame ()
    {
	super ("Add Production");

	AddProductionFrameCommon (null);
    }

    public AddProductionFrame (Season season)
    {
	super ("Add Production");

	AddProductionFrameCommon (season);
    }

    private void AddProductionFrameCommon (Season s)
    {
	selectedSeason = s;

	replacing = null;

	setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
	
	season_name_lbl = new JLabel ("Season", JLabel.CENTER);
	production_start_lbl = new JLabel ("Opening Date", JLabel.CENTER);
	production_end_lbl = new JLabel ("Closing Date", JLabel.CENTER);

	season_name = new JComboBox (Amato.db.getSeasons().toArray());
	if (selectedSeason != null)
	    season_name.setSelectedItem ((Object) selectedSeason);

	opera_name_lbl = new JLabel ("Opera Name", JLabel.CENTER);
	opera_name = new JComboBox (Amato.db.getOperas().toArray());

	libretto_lbl = new JLabel ("Libretto", JLabel.CENTER);
	libretto = new JComboBox (libretto_list);
	libretto.setEditable (true);

	Calendar sDate = Calendar.getInstance ();
	sDate.setTime (s.getStartDate ());
	production_start = new UICDateEdit (Locale.US, sDate);
	production_end = new UICDateEdit (Locale.US, sDate);

	okButton = new JButton ("OK");
	cancelButton = new JButton ("CANCEL");

	JPanel sdp = new JPanel (new GridLayout (0,2));
	sdp.add (production_start_lbl);
	sdp.add (production_start);
				 
	JPanel sep = new JPanel (new GridLayout (0,2));
	sep.add (production_end_lbl);
	sep.add (production_end);
				 
	JPanel asp = new JPanel (new GridLayout (0, 1));

	asp.add (season_name_lbl);
	asp.add (season_name);

	asp.add (opera_name_lbl);
	asp.add (opera_name);

	asp.add (libretto_lbl);
	asp.add (libretto);

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
	    
	    // Make sure the start date preceeds the end date.
	    Date sDate = production_start.getSelectedCalendar().getTime();
	    Date eDate = production_end.getSelectedCalendar().getTime();

	    if (eDate.before (sDate)) {
		Amato.errorPopup ("Production closing date must come\n" +
				  "AFTER\n production opening date.");
		return;
	    }

	    // Make sure that the start and end dates fall within the 
	    // season start/end dates.
	    Date sStart = selectedSeason.getStartDate();
	    Date sEnd = selectedSeason.getEndDate();
	    if (sDate.before (sStart) || sDate.after (sEnd) ||
		eDate.before (sStart) || eDate.after (sEnd)) {
		Amato.errorPopup ("You tried to set production dates that\n" +
				  "fall outside the season start/end dates:\n" +
				  Amato.getDateText (sStart) + " - " + 
				  Amato.getDateText (sEnd));
		return;
	    }

	    HashMap<String,String> h = new HashMap<String,String> ();
	    Opera op = (Opera) opera_name.getSelectedItem();
	    h.put ("name", op.getName());
	    h.put ("libretto", (String) libretto.getSelectedItem());
	    h.put ("openDate", Amato.getDateText (sDate));
	    h.put ("closeDate", Amato.getDateText (eDate));

	    Season s = (Season) season_name.getSelectedItem();
	    Production p = new Production (s, Amato.db.getOperas(), h);

	    s.add (p);

	    // Get rid of the original if we're editing it.
	    if (replacing != null) {
		// Copy all the performances here.
		int children = replacing.getChildCount ();
		while (children > 0) {
		    DefaultMutableTreeNode tn = (DefaultMutableTreeNode) replacing.getChildAt (0);
		    replacing.remove (tn);
		    p.add (tn);
		    ((Production) tn).setSeason (s);
		    children = replacing.getChildCount ();
		}
		s.remove (replacing);
		Amato.redraw();
		replacing = null;
	    }

	    Amato.redraw ();
	    
	    dispose ();
	}

	else if (e.getSource() == cancelButton) {
	    dispose ();
	}
    }

    /**
     * After creating the frame, use this method to populate it
     * with values from an existing Production instance.  This way
     * we can edit the entries.
     */
    public void populate (Production p)
    {
	// Fill in all the basic (read: easy) stuff.
	season_name.setSelectedItem (p.getSeason().getName());
        opera_name.setSelectedItem (p.getOpera());
        libretto.setSelectedItem (p.getLibretto());
	
	// Fill in the opening/closing dates.
	Calendar sCal = Calendar.getInstance ();
	sCal.setTime (p.getOpenDate());
	production_start.setSelectedCalendar (sCal);
	Calendar cCal = Calendar.getInstance ();
	cCal.setTime (p.getCloseDate());
	production_end.setSelectedCalendar (cCal);

	// Change the titlebar title.
	setTitle ("Edit Production: " + p.getName());
	replacing = p;
    }

    public static void main (String[] args)
    {
	AddProductionFrame asf = new AddProductionFrame ();
    }
}


