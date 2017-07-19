/**
 * Add members to roster.  This will be extended in other classes
 * to be able to add singers, musicians, etc.  
 *
 * The basic frame will look like:
 * <pre>
 *  +----------------------------------+
 *  | +------------------------------+ |
 *  | |                              | |
 *  | |                              | |
 *  | |     (basic information)      | |
 *  | |                              | |
 *  | |                              | |
 *  | +------------------------------+ |
 *  | +------------------------------+ |
 *  | /      subclass-specific       / |
 *  | +------------------------------+ |
 *  | +------------------------------+ |
 *  | | [OK]                [CANCEL] | |
 *  | +------------------------------+ |
 *  +----------------------------------+
 * </pre>
 *
 *
 * Originally part of the AMATO application.
 * 
 * @author Nick Seidenman <nick@seidenman.net>
 * @version $Revision: 1.6 $
 *
 * $Id: AddConstituentFrame.java,v 1.6 2005/09/08 15:37:40 nicks Exp $
 *
 * $Log: AddConstituentFrame.java,v $
 * Revision 1.6  2005/09/08 15:37:40  nicks
 * Seasons, Productions, Performances, Constituents, and Operas are now
 * all editable!
 *
 * Revision 1.5  2005/08/15 14:26:28  nicks
 * Changed as_* to to* (more java-esque)
 *
 * Revision 1.4  2005/08/02 21:40:36  nicks
 * added the addMap method, which will be overridden by subclasses to
 * add attributes to the HashMap and construct and actual Constituent
 * subclass instance to add to database.
 *
 * Revision 1.3  2005/08/02 19:33:16  nicks
 * *** empty log message ***
 *
 * Revision 1.2  2005/08/02 19:31:39  nicks
 * Tuning.  Added javadoc-style comment tags.
 *
 * Revision 1.1  2005/07/26 20:49:09  nicks
 * Initial revision
 *
 */

import java.util.*;
import java.io.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.border.*;
import javax.swing.event.*;

public class AddConstituentFrame 
    extends JFrame
    implements ActionListener
{
    private static final long serialVersionUID = 20050725173000L;

    private JTextField first_name;
    private JTextField last_name;
    private JTextField street;
    private JTextField city;
    private JTextField state;
    private JTextField zip;
    private JTextField country;

    private ListEditorTextField phone;
    private ListEditorTextField email;

    private JButton okButton;
    private JButton cancelButton;

    private Constituent replacing;

    public AddConstituentFrame ()
    {
	super ("Add Constituent");

	addConstituentCommon ();
    }

    /**
     * @param fname string to put in title bar */
    public AddConstituentFrame (String fname)
    {
	super (fname);

	addConstituentCommon ();
    }

    private void addConstituentCommon ()
    {
	replacing = null;

	setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
	getContentPane().setLayout (new BorderLayout ());

	JPanel main_panel = new JPanel ();
	//main_panel.setBorder (BorderFactory.createLineBorder (Color.BLACK));
	main_panel.setLayout (new BoxLayout (main_panel, BoxLayout.PAGE_AXIS));
	main_panel.setAlignmentX (Component.LEFT_ALIGNMENT);
	
	JPanel name_panel = new JPanel ();
	name_panel.setLayout (new BoxLayout (name_panel, BoxLayout.LINE_AXIS));
	name_panel.setAlignmentX (Component.LEFT_ALIGNMENT);
	name_panel.add (new JLabel ("First Name"));
	first_name = new JTextField (20);
	name_panel.add (first_name);

	name_panel.add (Box.createRigidArea (new Dimension (5, 0)));

	name_panel.add (new JLabel ("Last Name"));
	last_name = new JTextField (20);
	name_panel.add (last_name);
	main_panel.add (name_panel);

	JPanel street_panel = new JPanel ();
	street_panel.setLayout (new BoxLayout (street_panel, BoxLayout.LINE_AXIS));
	street_panel.setAlignmentX (Component.LEFT_ALIGNMENT);
	street_panel.add (new JLabel ("Street", Label.LEFT));
	street = new JTextField (30);
	street_panel.add (street);
	main_panel.add (street_panel);

	JPanel csz_panel = new JPanel ();
	csz_panel.setLayout (new BoxLayout (csz_panel, BoxLayout.LINE_AXIS));
	csz_panel.setAlignmentX (Component.LEFT_ALIGNMENT);
	csz_panel.add (new JLabel ("City"));
	city = new JTextField (20);
	csz_panel.add (city);
    
	csz_panel.add (Box.createRigidArea (new Dimension (5, 0)));

	csz_panel.add (new JLabel ("State"));
	state = new JTextField (2);
	csz_panel.add (state);
    
	csz_panel.add (Box.createRigidArea (new Dimension (5, 0)));

	csz_panel.add (new JLabel ("ZIP"));
	zip = new JTextField (10);
	csz_panel.add (zip);

	csz_panel.add (Box.createRigidArea (new Dimension (5, 0)));

	csz_panel.add (new JLabel ("Country"));
	country = new JTextField (6);
	csz_panel.add (country);
	main_panel.add (csz_panel);

	JPanel email_phone_panel = new JPanel (new GridLayout (0, 2));

	phone = new ListEditorTextField ();
	phone.setBorder (BorderFactory.createTitledBorder ("Phone Number(s)"));
	
	email = new ListEditorTextField ();
	email.setBorder (BorderFactory.createTitledBorder ("Email Address(es)"));
	
	email_phone_panel.add (phone);
	email_phone_panel.add (email);
	
	JPanel buttons = new JPanel (new GridLayout (0, 2));
	okButton = new JButton ("OK");
	okButton.addActionListener (this);
	buttons.add (okButton);
	cancelButton = new JButton ("CANCEL");
	cancelButton.addActionListener (this);
	buttons.add (cancelButton);
	buttons.setBorder (BorderFactory.createLineBorder (Color.BLACK));

	//JPanel basic_info = new JPanel (new GridLayout (0, 1));
	JPanel basic_info = new JPanel (new BorderLayout());
	//basic_info.setLayout (new BoxLayout (basic_info, BoxLayout.PAGE_AXIS));
	
	basic_info.add (main_panel, BorderLayout.NORTH);
	basic_info.add (email_phone_panel, BorderLayout.CENTER);
	getContentPane().add (basic_info, BorderLayout.NORTH);
	getContentPane().add (buttons, BorderLayout.SOUTH);
    }

    /**
     * Called by classes derived from this one so that they
     * can add constituent-type-specific info to the HashMap
     * used to build the new database object.
     *
     * @param h HashMap passed to Constituent subclass constructors.
     */
    public Constituent addMap (HashMap<String,String> h) { return new Constituent (h); }

    public void actionPerformed (ActionEvent e)
    {
	if (e.getSource() == okButton) {
	    
	    // Fill in basic information, common to all Constituents.
	    HashMap<String,String> h = new HashMap<String,String> ();
	    h.put ("first_name", first_name.getText());
	    h.put ("last_name", last_name.getText ());
	    h.put ("street", street.getText ());
	    h.put ("city", city.getText ());
	    h.put ("state", state.getText ());
	    h.put ("zip", zip.getText ());
	    h.put ("country", country.getText ());

	    h.put ("phone", phone.toString());
	    h.put ("email", email.toString());

	    // Fill in function-specific information.  (subclasses
	    // will override this method.)
	    Constituent c = addMap (h);
	    
	    try {
		Amato.db.getRoster().add (c);
	    }
	    catch (NullPointerException npe) {
		System.err.println ("No database to add to!");
		System.err.println (c.toXML ());
	    }

	    if (replacing != null) {
		Amato.db.getRoster().remove (replacing);
		replacing = null;
	    }

	    dispose ();
	}

	else { // Must be the CANCEL button.
	    dispose ();
	}
    }

    /**
     * The base class builds the top (basic information) and bottom
     * (OK / Cancel buttons) of the form.  For derived frames, such
     * as AddSingerFrame, the middle portion (e.g. voice, level) must
     * be provided.
     * 
     * @param p panel containing the "middle" portion.
     */
    public void injectPanel (JPanel p)
    {
	getContentPane().add (p, BorderLayout.CENTER);
    }
	
    /**
     * Since the frame's construction will happen in two parts, we
     * we need to defer actual sizing, positioning, and visualization
     * until after the middle portion has been "injected".
     */
    public void realize ()
    {
	pack ();

	// Display more or less center-screen.
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	Dimension panelSize = getContentPane().getSize ();
	setLocation (screenSize.width/2 - (panelSize.width/2), 
		     screenSize.height/2 - (panelSize.height/2));

	setVisible (true);
    }

    /**
     * Fills in the fields/labels/etc in this form from an
     * existing Constituent instance, presumably in preparation
     * for editing it.
     * 
     * @arg c existing Constituent instance used to populate the fields.
     */
    public void populate (Constituent c)
    {
	first_name.setText (c.getFirstName ());
	last_name.setText (c.getLastName ());
	street.setText (c.getStreet());
	city.setText (c.getCity());
	state.setText (c.getState ());
	zip.setText (c.getZip ());
	country.setText (c.getCountry());
	
	phone.add (c.getPhoneList());
	email.add (c.getEmailList());

	replacing = c;
	
	setTitle ("Editing record for " + c);
    }

    public static void main (String[] args)
    {
        AddConstituentFrame me = new AddConstituentFrame ();
	me.realize ();
    }
}
