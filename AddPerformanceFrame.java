/**
 *
 * $Id: AddPerformanceFrame.java,v 1.13 2005/09/12 17:36:44 nicks Exp nicks $
 * 
 * This frame is used to add or edit performance information.  It
 * features some of the more sophisticated objects on the whole AMATO
 * application, including the sortable, DnD SingerTable and the
 * DropLabel targets for the draggable table rows.  In a way, this 
 * represents the culmination of all I've learned about Java so far.
 * Also, this is the last component required to make this even barely 
 * usable, in a production sense.  What remains after this component
 * is to refine and enhance the existing functions.  (I'm considering
 * the edit capability to be a refinement, not an addition.)
 *
 * Originally part of the AMATO application.
 * 
 * @author Nick Seidenman <nick@seidenman.net>
 * @version $Revision: 1.13 $
 *
 * $Log: AddPerformanceFrame.java,v $
 * Revision 1.13  2005/09/12 17:36:44  nicks
 * If a singer is being cast in more than one role, a pop-up warning
 * is displayed and the user is asked if this is ok.  If it is, the singer
 * is (n-tuple cast).
 *
 * Revision 1.12  2005/09/09 22:23:59  nicks
 * Now has date sanity checks:
 *   - season start must come before season end
 *   - production start must come before season end
 *   - production dates must fall within season dates
 *   - performance date must fall within production dates
 *
 * Revision 1.11  2005/09/09 21:24:51  nicks
 * All objects now store date/time in a Date instance.  This gets me
 * ready to be able to do some of the date/time sanity checks mentioned
 * in the TODO list.  (E.g., check to make sure performance date
 * is within the season start/end bounds.)
 *
 * Revision 1.10  2005/09/08 15:37:40  nicks
 * Seasons, Productions, Performances, Constituents, and Operas are now
 * all editable!
 *
 * Revision 1.9  2005/09/07 15:47:08  nicks
 * Now able to edit performances.  Will proceed to add this capability
 * to other Add*Frame.java components.
 *
 * Revision 1.8  2005/08/22 21:55:21  nicks
 * Added confirmation dialog for when role's voice doesn't match singer's
 * voice(s) in AddPerformanceFrame.
 *
 * Revision 1.7  2005/08/21 19:28:03  nicks
 * Added conductor selector combobox.
 *
 * Revision 1.6  2005/08/16 02:18:02  nicks
 * Cast was adding itself to Performance.  Now all instances
 * of this call Performance.add_cast.
 *
 * Revision 1.5  2005/08/16 02:08:35  nicks
 * AddPerformanceFrame added.  All the basic elements can now be adde
 * or deleted.  (Now need to add Edit function.)
 *
 * Revision 1.4  2005/08/15 15:36:03  nicks
 * Replaced DateButton with UICDateEdit.
 *
 * Revision 1.3  2005/08/15 14:26:28  nicks
 * Changed as_* to to* (more java-esque)
 *
 * Revision 1.2  2005/08/15 13:53:40  nicks
 * First, very early cut.  Basic, DnD functionality working well.
 *
 * Revision 1.1  2005/08/12 20:40:21  nicks
 * Initial revision
 *
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

import uic.*;
import uic.widgets.*;
import uic.widgets.calendar.*;

public class AddPerformanceFrame 
    extends JFrame
    implements ActionListener
{
    private static final long serialVersionUID = 20050812000010L;

    private Roster rost;
    private Production prod;

    private JButton okBtn;
    private JButton cancelBtn;
    private UICDateEdit dateBtn;
    //private UICDateTimeSpinner timeBtn;
    private TimeComboBox timeBtn;
    static private String[] times = { "00:00", "00:15", "00:30", "00:45",
				      "01:00", "01:15", "01:30", "01:45",
				      "02:00", "02:15", "02:30", "02:45",
				      "03:00", "03:15", "03:30", "03:45",
				      "04:00", "04:15", "04:30", "04:45",
				      "05:00", "05:15", "05:30", "05:45",
				      "06:00", "06:15", "06:30", "06:45",
				      "07:00", "07:15", "07:30", "07:45",
				      "08:00", "08:15", "08:30", "08:45",
				      "09:00", "09:15", "09:30", "09:45",
				      "10:00", "10:15", "10:30", "10:45",
				      "11:00", "11:15", "11:30", "11:45",
				      "12:00", "12:15", "12:30", "12:45",
				      "13:00", "13:15", "13:30", "13:45",
				      "14:00", "14:15", "14:30", "14:45",
				      "15:00", "15:15", "15:30", "15:45",
				      "16:00", "16:15", "16:30", "16:45",
				      "17:00", "17:15", "17:30", "17:45",
				      "18:00", "18:15", "18:30", "18:45",
				      "19:00", "19:15", "19:30", "19:45",
				      "20:00", "20:15", "20:30", "20:45",
				      "21:00", "21:15", "21:30", "21:45",
				      "22:00", "22:15", "22:30", "22:45",
				      "23:00", "23:15", "23:30", "23:45" };

    private JComboBox cond;
    private DefaultComboBoxModel condModel;

    private SimpleDateFormat dateFormat;
    private SimpleDateFormat timeFormat;
				      
    private HashMap<Role,RoleDropLabel> castMap;

    private Performance replacing;

    public AddPerformanceFrame (Production prod, Roster rost)
    {
	super ("Add Performance");
	setLayout (new BoxLayout (getContentPane(), BoxLayout.Y_AXIS));

	// If we're editing an existing Performance, this will
	// point to that Performance instance later, when we call
	// populate().
	replacing = null;

	this.prod = prod;
	this.rost = rost;
	dateFormat = new SimpleDateFormat (Amato.dateFormatStr);
	timeFormat = new SimpleDateFormat (Amato.timeFormatStr);

	setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
	setDefaultLookAndFeelDecorated (true);

	// Set up the main info area for this performance.
	//  (date, time, conductor.)
	JPanel mainInfo = new JPanel (new GridLayout (0, 2));
	mainInfo.setBorder (BorderFactory.createTitledBorder ("Production Information"));

	mainInfo.add (new JLabel ("Opera: ", JLabel.RIGHT));
	mainInfo.add (new JLabel (prod.toString()));

	mainInfo.add (new JLabel ("Conductor: ", JLabel.RIGHT));
	condModel = rost.createConductorComboBoxModel ();
	cond = new JComboBox (condModel);
	cond.setEditable (false);
	mainInfo.add (cond);
	

	// Date and time ...
	mainInfo.add (new JLabel ("Date: ", JLabel.RIGHT));
	Calendar oCal = Calendar.getInstance();
	oCal.setTime (prod.getOpenDate());
	dateBtn = new UICDateEdit (Locale.US, oCal); 
	mainInfo.add (dateBtn);

	mainInfo.add (new JLabel ("Time: ", JLabel.RIGHT));
	timeBtn = new TimeComboBox (times);
	timeBtn.setSelectedIndex (78);  // 19:30 -- yes, it's a total HACK.
	mainInfo.add (timeBtn);

	// Add the cast area.  
	// Iterate through the roles for this opera, creating
	// RoleDropLabel's for each one.

	JPanel castPanel = new JPanel (new GridLayout (0, 2));
	castPanel.setBorder (BorderFactory.createTitledBorder ("Cast"));
	
	RoleDropLabel dl;
	castMap = new HashMap<Role,RoleDropLabel> ();
	for (Role role: prod.getOpera().getRoles()) {
	    castPanel.add (new JLabel (role.getName()));
	    dl = new RoleDropLabel (role, "NOT CAST", this);
	    dl.setBorder (new BevelBorder (BevelBorder.LOWERED));
	    castMap.put (role, dl);
	    castPanel.add (dl);
	}
	
	// Add the SingerTable from which we'll drag Singer
	// objects to the RoleDropLabels in the cast area.
	JPanel singerTablePanel = new JPanel ();
	singerTablePanel.setBorder (BorderFactory.createTitledBorder ("Roster"));
	JScrollPane stsp = new JScrollPane (new SingerTable (rost));
	singerTablePanel.add (stsp);

	// OK and CANCEL Buttons ...
	JPanel buttons = new JPanel (new GridLayout (0, 2));

	okBtn = new JButton ("OK");
	cancelBtn = new JButton ("CANCEL");

	okBtn.addActionListener (this);
	buttons.add (okBtn);

	cancelBtn.addActionListener (this);
	buttons.add (cancelBtn);

	// Put it all together.
	getContentPane ().add (mainInfo);
	getContentPane ().add (castPanel);
	getContentPane ().add (singerTablePanel);
	getContentPane().add (buttons);

	
	pack ();

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	Dimension panelSize = getContentPane().getSize ();
	setLocation (screenSize.width/2 - (panelSize.width/2), 
		     screenSize.height/2 - (panelSize.height/2));

	setVisible (true);
    }

    /**
     * After creating an instance of the form, fill in the fields
     * with the values found in the Performance instance p.
     * 
     * @arg p - Existing performance instance the values of which
     *          will be taken to fill in the form's fields.
     */
    public void populate (Performance p)
    {
	// Fill in the date and time.

	Date date = p.getDate();
	Calendar cal = Calendar.getInstance();
	cal.setTime (date);
	dateBtn.setSelectedCalendar (cal);
	timeBtn.setValue (Amato.getTimeText (date));

	// Fill in the cond[uctor].
	cond.setSelectedItem (p.getConductor());

	// Fill in the cast.
	RoleDropLabel dl;
	for (Role role: prod.getOpera().getRoles()) {
	    dl = castMap.get (role);
	    Cast cm = p.getCastByRole (role);
	    if (cm != null) {
		Constituent cnst = cm.getActor ();
		if (cnst != null && cnst.isValid()) {
		    dl.setObject (cnst);
		}
		else {
		    dl.setObject ("NOT CAST");
		}
	    }
	}

	// Set the replacement performance to p.
	replacing = p;

	// Set the frame's title.
	setTitle ("Edit Performance: " + p);
    }
    
    public void actionPerformed (ActionEvent e)
    {
	if (e.getSource() == okBtn) {

	    // Digest the date/time and make sure the specified date
	    // is within the bounds of the production open/close dates.
	    Date pDate = dateBtn.getSelectedCalendar().getTime();
	    
	    if (pDate.before (prod.getOpenDate()) || pDate.after (prod.getCloseDate())) {
		Amato.errorPopup ("You tried to schedule a performance outside\n" +
				  "the production start (" + 
				  prod.getOpenDateText() + ")\n" +
				  "and production end (" + 
				  prod.getCloseDateText() + ")\n" +
				  "dates.");
		return;
	    }
	    
	    // Ok, everything seems to check out at this point.

	    HashMap<String,String> h = new HashMap<String,String> ();
	    
	    h.put ("date", Amato.getDateText (pDate) + " " + timeBtn.getValue());
	    
	    Object ob = cond.getSelectedItem ();
	    
	    if (ob instanceof Conductor) {
		Conductor cnd = (Conductor) ob;
		h.put ("conductor", cnd.getLastName());
	    }
	    else {
		h.put ("conductor", null);
	    }

	    Performance p = new Performance (prod, rost, h);

	    for (Role r: castMap.keySet()) {
		RoleDropLabel dl = castMap.get(r);
		Singer s = (dl == null) ? null : (Singer) dl.getObject();
		if (s != null) {
		    HashMap<String,String> cm = new HashMap<String,String>();
		    cm.put ("role", r.getName());
		    cm.put ("first_name", s.getFirstName());
		    cm.put ("last_name", s.getLastName());
		    cm.put ("call", "on");

		    p.addCast (new Cast (p, rost, cm));

		    // Update the constituent's list of 
		    // performances.replacing
		    if (replacing != null) {
			s.removePerformance (replacing);
			s.addPerformance (p);
		    }
		}
	    }

	    // If we're editing, the instance we just created
	    // will replace the one that was used to fill in the 
	    // form when we first drew it.

	    if (replacing != null) {
		prod.remove (replacing);
		replacing = null;
	    }
	    
	    prod.add (p);

	    Amato.redraw ();
	    
	    dispose ();
	}

	else if (e.getSource() == cancelBtn) {
	    dispose ();
	}
    }

    public boolean isCast (Singer s) 
    {
	for (RoleDropLabel rdl: castMap.values()) {
	    if (rdl == null) continue;

	    if (rdl.getObject() instanceof Singer) {
		Singer cs = (Singer) rdl.getObject();

		if (cs.toString().equals (s.toString())) {
		    return true;
		}
	    }
	}
 
	return false;
    }

    class TimeComboBox extends JComboBox
    {
	private static final long serialVersionUID = 200509062000010L;

	public TimeComboBox (String[] ta)
	{
	    super ((Object[]) ta);
	    setEditable (true);
	}

	public String getValue ()
	{
	    return (String) getSelectedItem ();
	}

	public void setValue (String s)
	{
	    setSelectedItem (s);
	}
    }


    static public void main (String[] args)
    {
	if (args.length <= 0) return;

	AmatoDB adb = new AmatoDB (new File (args[0]));
	Roster rost = adb.getRoster ();
	Seasons szns = adb.getSeasons ();
	Season fs = (Season) szns.getChildAt (0);
	Production p1 = (Production) fs.getChildAt (0);
	
	// We'll need to pass a Production instance to this.
	AddPerformanceFrame pf = new AddPerformanceFrame (p1, rost);
    }
}
