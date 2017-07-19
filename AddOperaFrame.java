/**
 * $Id: AddOperaFrame.java,v 1.14 2005/09/14 18:55:44 nicks Exp $
 * 
 * Dialog that acquires information about opera being added.
 *
 * Originally part of the AMATO application.
 * 
 * @author Nick Seidenman <nick@seidenman.net>
 * @version $Revision: 1.14 $
 *
 * $Log: AddOperaFrame.java,v $
 * Revision 1.14  2005/09/14 18:55:44  nicks
 * Completely re-worked Opera.toJPanel.  Now uses pure java; no html.
 * This also means that line-wrap works in the synopsis area.
 *
 * Revision 1.13  2005/09/12 17:36:44  nicks
 * If a singer is being cast in more than one role, a pop-up warning
 * is displayed and the user is asked if this is ok.  If it is, the singer
 * is (n-tuple cast).
 *
 * Revision 1.12  2005/09/08 15:37:40  nicks
 * Seasons, Productions, Performances, Constituents, and Operas are now
 * all editable!
 *
 * Revision 1.11  2005/08/31 16:21:31  nicks
 * Fixed problem where role/voice wasn't being added in AddOperaPanel.
 *
 * Revision 1.10  2005/08/16 21:17:27  nicks
 * Changed comment header to comply with javadoc convention.
 *
 * Revision 1.9  2005/08/01 22:14:29  nicks
 * Added code to more or less center the frame on the screen.
 *
 * Revision 1.8  2005/07/22 22:12:39  nicks
 * Started work on Drag-n-Drop within RosterPanel.
 * Cleaned up nearly all warnings.  Only a few left in Opera.java, Operas.java,
 * and Performance.java and these pertain to strict type-checking ala 1.5.
 * Just wanted to check everything in now as a check point.  I'll continue
 * to work on getting a totally clean compilation.
 *
 * Revision 1.7  2005/07/19 19:32:11  nicks
 * Removed some debug cruft.
 *
 * Revision 1.6  2005/07/12 19:14:03  nicks
 * Removed some debug cruft.
 *
 * Revision 1.5  2005/07/08 20:46:21  nicks
 * Added escape () method to cleanse the synopsis text of "bad" HTML
 * chars (e.g. '&').
 *
 * Revision 1.4  2005/07/08 20:21:28  nicks
 * Add Opera works!!!
 * ** Need to add code to scrub text data to convert things like '<' and '&'
 * ** to their XML-entity equiv's: &lt; and &amp;
 *
 * Revision 1.3  2005/07/08 14:50:32  nicks
 * Add roles w/ combobox for voice column working.
 * Minor bug: if a voice isn't explicitly set, it will be set to
 * whatever the row above it is.  Need to figure out how to pre-set
 * the values.
 *
 * Revision 1.2  2005/07/07 23:53:09  nicks
 * Have this mostly working.  Can even add roles, and have the whole
 * thing spit out as xml on "OK".  Trying to put JComboBox objects in the
 * Voice column so that there's less chance of a typo when entering the
 * voice type.  Also need to add a third column that will have "Move Up"
 * "Move Down" and "Delete" buttons in it.  The "order" attribute in the
 * XML record will be based on the relative position of the roles.  Being
 * able to move the roles up and down once entered is, therefore, highly
 * desireable.
 *
 * Revision 1.1  2005/07/06 22:08:58  nicks
 * Initial revision
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
import javax.swing.table.*;
import java.text.StringCharacterIterator;

import uic.*;
import uic.widgets.*;
import uic.widgets.calendar.*;

public class AddOperaFrame extends JFrame
				     implements ActionListener
{
    private static final long serialVersionUID = 20050706162740L;
    
    private JTextField title;
    private JTextField composer;
    private JTextField librettist;
    private JTextField language;
    private JTextField run_time;
    private JTextArea synopsis;
    
    private JButton okButton;
    private JButton cancelButton;
    private JButton addRoleButton;
    //private DefaultTableModel rtm;
    private RoleTableModel rtm;
    
    private JPanel aop;

    private String[] voxen = {"coloratura", "soprano", 
			      "mezzo", "alto", 
			      "tenor", "baritone", "bass" };

    private Opera replacing;

    public AddOperaFrame ()
    {
	super ("Add Opera");
	
	replacing = null;

	setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
	
	GridBagLayout gb = new GridBagLayout ();
	GridBagConstraints gbc = new GridBagConstraints ();

	gbc.ipadx = 5;
	gbc.ipady = 5;
	gbc.insets = new Insets (5, 2, 2, 2);
	gbc.fill = GridBagConstraints.BOTH;

	aop = new JPanel (gb);

	JLabel title_lbl = new JLabel ("Title", JLabel.RIGHT);
	JLabel composer_lbl = new JLabel ("Composer ", JLabel.RIGHT);
	JLabel librettist_lbl = new JLabel ("Librettist ", JLabel.RIGHT);
	JLabel language_lbl = new JLabel ("Language ", JLabel.RIGHT);
	JLabel run_time_lbl = new JLabel ("Running Time ", JLabel.RIGHT);

	JLabel synopsis_lbl = new JLabel ("Synopsis", JLabel.CENTER);
	synopsis_lbl.setBorder ((Border)BorderFactory.createRaisedBevelBorder());

	addRoleButton = new JButton ("Roles    (Click to add role)");

	title = new JTextField (40);
	title.setEditable (true);

	composer = new JTextField (40);
	composer.setEditable (true);

	librettist = new JTextField (40);
	librettist.setEditable (true);

	language = new JTextField (20);
	language.setEditable (true);

	run_time = new JTextField (8);
	run_time.setEditable (true);

	synopsis = new JTextArea (10, 50);
	synopsis.setEditable (true);
	synopsis.setLineWrap (true);
	synopsis.setWrapStyleWord (true);
	JScrollPane synsp = new JScrollPane (synopsis);
	synsp.setBorder ((Border)BorderFactory.createLoweredBevelBorder());

	//String[] cols = {"Role", "Voice", "Move / Delete"};
	String[] cols = {"Role", "Voice"};

	rtm = new RoleTableModel ();
	JTable rtbl = new JTable (rtm);
	//UICTable rtbl = new UICTable (rtm);
	TableColumn col = rtbl.getColumnModel().getColumn (1);
	col.setCellEditor (new VoiceComboBoxEditor (voxen));
	col.setCellRenderer (new VoiceComboBoxRenderer (voxen));
	JScrollPane rtblp = new JScrollPane (rtbl);
	rtblp.setBorder ((Border)BorderFactory.createLoweredBevelBorder());

	okButton = new JButton ("OK");
	cancelButton = new JButton ("CANCEL");

	JPanel buttons = new JPanel (new GridLayout (0, 2));

	okButton.addActionListener (this);
	buttons.add (okButton);

	addRoleButton.addActionListener (this);
	buttons.add (addRoleButton);
	
	cancelButton.addActionListener (this);
	buttons.add (cancelButton);
	
	gbc.gridwidth = GridBagConstraints.RELATIVE;
	gb.setConstraints (title_lbl, gbc);
	aop.add (title_lbl);
	gbc.gridwidth = GridBagConstraints.REMAINDER;
	gb.setConstraints (title, gbc);
	aop.add (title);

	gbc.gridwidth = GridBagConstraints.RELATIVE;
	gb.setConstraints (composer_lbl, gbc);
	aop.add (composer_lbl);
	gbc.gridwidth = GridBagConstraints.REMAINDER;
	gb.setConstraints (composer, gbc);
	aop.add (composer);

	gbc.gridwidth = GridBagConstraints.RELATIVE;
	gb.setConstraints (librettist_lbl, gbc);
	aop.add (librettist_lbl);
	gbc.gridwidth = GridBagConstraints.REMAINDER;
	gb.setConstraints (librettist, gbc);
	aop.add (librettist);

	JPanel ep2 = new JPanel ();
	ep2.add (language_lbl);
	ep2.add (language);
	ep2.add (run_time_lbl);
	ep2.add (run_time);

	gbc.weightx = 0.0;
	gbc.gridx = 0;
	gbc.gridwidth = GridBagConstraints.REMAINDER;
	gb.setConstraints (ep2, gbc);
	aop.add (ep2);

	gbc.insets = new Insets (2, 0, 0, 0);
	gb.setConstraints (synopsis_lbl, gbc);
	aop.add (synopsis_lbl);
	gbc.insets = new Insets (0, 0, 2, 0);
	gb.setConstraints (synsp, gbc);
	aop.add (synsp);

	gbc.insets = new Insets (2, 0, 0, 0);
	gb.setConstraints (addRoleButton, gbc);
	aop.add (addRoleButton);
	gbc.weighty = 0.0;
	gbc.insets = new Insets (0, 0, 2, 0);
	gb.setConstraints (rtblp, gbc);
	aop.add (rtblp);

	gbc.insets = new Insets (5, 5, 5, 5);
	gb.setConstraints (buttons, gbc);
	aop.add (buttons);
	
	getContentPane().add (aop);

	pack ();

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	Dimension panelSize = getContentPane().getSize ();
	setLocation (screenSize.width/2 - (panelSize.width/2), 
		     screenSize.height/2 - (panelSize.height/2));

	setVisible (true);
    }

    class RoleTableModel extends AbstractTableModel
    {
	private static final long serialVersionUID = 20050706162742L;

	private ArrayList<String> rnames;
	private ArrayList<String> rvoxen;

	public RoleTableModel ()
	{
	    super ();
	    rnames = new ArrayList<String>();
	    rvoxen = new ArrayList<String>();
	}

	public int getColumnCount() { return 2; }

	public int getRowCount ()
	{
	    return rnames.size();
	}

	public Object getValueAt (int row, int col)
	{
	    //System.out.println ("getValueAt ("+ row + "," + col + ")");
	    if (row >= 0 && row < rnames.size()) {
		if (col == 0)
		    return (Object) rnames.get (row);
		else if (col == 1) 
		    return (Object) rvoxen.get(row);
	    }

	    return null;
	}

	public void setValueAt (Object v, int row, int col)
	{
	    //System.out.println ("setValueAt ("+ row + "," + col + "): " + v);
	    if (col == 0) {
		if (row >= 0 && row < rnames.size())
		    rnames.set (row, (String) v);
		else
		    rnames.add ((String) v);
	    }
	    else if (col == 1) {
		if (row >= 0 && row < rvoxen.size())
		    rvoxen.set (row, (String) v);
		else
		    rvoxen.add ((String) v);
	    }    
	}

        final public String getColumnName (int col)
	{
	    if (col == 0) return "Role";
	    if (col == 1) return "Voice";
	    return null;
	}
	
        final public Class getColumnClass (int col)
	{
	    //System.out.println ("getColumnClass: " + col);
	    if (col == 0) return String.class;
	    if (col == 1) return JComboBox.class;
	    return null;
	}
	
	public boolean isCellEditable (int row, int col)
	{
	    boolean well = ((col == 0 || col == 1) && row >= 0 && row < rnames.size());
	    //System.out.println ("isCellEditable: " + row + "," + col + " ... " + well);
	    return well;
	}

	public void addRow ()
	{
	    //System.out.println ("addRow");
	    rnames.add ("");
	    rvoxen.add ("");
	    fireTableRowsInserted (rnames.size()-1, rnames.size()-1);
	}
    }
    
    public class VoiceComboBoxRenderer extends JComboBox implements TableCellRenderer
    {

	private static final long serialVersionUID = 20050706162744L;

	public VoiceComboBoxRenderer (String[] voxen)
	{
	    super (voxen);

	    setEditable (true);
	    setSelectedIndex (0);
	}

	public Component getTableCellRendererComponent (JTable t, Object v,
							boolean isSelected, 
							boolean hasFocus,
							int row, int col)
	{
	    if (isSelected) {
		setForeground (t.getSelectionForeground ());
		setBackground (t.getSelectionBackground ());
	    }
	    else {
		setForeground (t.getForeground ());
		setBackground (t.getBackground ());
	    }
	    
	    setSelectedItem (v);
	    return this;
	}
    }

    public class VoiceComboBoxEditor extends DefaultCellEditor 
    {
	private static final long serialVersionUID = 20050706162746L;

	public VoiceComboBoxEditor (String[] voxen)
	{
	    super (new JComboBox (voxen));
	    JComboBox vcb = (JComboBox) this.getComponent ();

	    vcb.setEditable (true);
	    vcb.setSelectedIndex (0);
	}
    }

    public void actionPerformed (ActionEvent e)
    {
	if (e.getSource() == okButton) {

	    HashMap<String,String> h = new HashMap<String,String> ();
	    h.put ("name", title.getText());
	    h.put ("composer", composer.getText ());
	    h.put ("librettist", librettist.getText ());
	    h.put ("language", language.getText ());
	    h.put ("runtime", run_time.getText ());
	    
	    Opera op = new Opera (h);

	    op.add_synopsis (new Synopsis ("Englsh", synopsis.getText()));

	    int rc = rtm.getRowCount ();
	    for (int i = 0; i < rc; i++) {
		String rn = (String) rtm.getValueAt (i, 0);
		String rv = (String) rtm.getValueAt (i, 1);

		rn.trim();
		HashMap<String,String> h2 = new HashMap<String,String> ();
		h2.put ("name", rn);		
		h2.put ("voice", rv);
		h2.put ("order", (i+1) + "");
		if (rn.length() > 0)
		    op.add_role (new Role (op, h2));
	    }

	    
	    Amato.db.getOperas().add (op);

	    // If we're editing, get rid of the original.
	    if (replacing != null) {
		// Copy over the list of productions of this Opera.
		for (Production prod: replacing.getProductions()) {
		    op.add_prod (prod);
		}
	    
		Amato.db.getOperas().remove (replacing);
		replacing = null;
	    }

	    dispose ();
	}
	else if (e.getSource() == addRoleButton) {
	    rtm.addRow ();
	}
	else { // Must be the CANCEL button.
	    dispose ();
	}
    }

    /**
     * After creating the frame, use this method to populate it
     * with values from an existing Opera instance.  This way
     * we can edit the entries.
     */
    public void populate (Opera op)
    {
	// Fill in all the basic (read: easy) stuff.
	title.setText (op.toString());
	composer.setText (op.getComposer());
	librettist.setText (op.getLibrettist());
	language.setText (op.getLanguage());
	run_time.setText (op.getRuntime());
	synopsis.setText (op.getSynopsisText());

	// Fill in the roles.
	int row = 0;
	for (Role r: op.getRoles ()) {
	    rtm.addRow ();
	    rtm.setValueAt (r.getName (), row, 0);
	    rtm.setValueAt (r.getVoice (), row++, 1);
	}

	
	setTitle ("Edit Opera: " + op);
	replacing = op;
    }

    public static void main (String[] args)
    {
	AddOperaFrame asf = new AddOperaFrame ();
    }
}



