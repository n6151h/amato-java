/**
 * $Id: MenuBar.java,v 1.16 2005/09/09 23:59:54 nicks Exp $
 * 
 * Set up and manage menubar stuff.
 *
 * Originally part of the AMATO application.
 * 
 * @author Nick Seidenman <nick@seidenman.net>
 * @version $revision$
 *
 * $Log: MenuBar.java,v $
 * Revision 1.16  2005/09/09 23:59:54  nicks
 * Added AmatoWindowListener class to Amato so that I can intercept
 * the titlebar "kill" button and prompt the user to save a db
 * if it's been modified.
 *
 * Revision 1.15  2005/09/09 23:26:26  nicks
 * Before loading another db or exiting, changed flag is checked and
 * the user is prompted to save (or not) the current db.
 *
 * Revision 1.14  2005/08/21 19:06:23  nicks
 * Changed comment header to conform with javadoc conventions.
 *
 * Revision 1.13  2005/07/18 20:02:12  nicks
 * Removed "Action" pulldown menu.
 *
 * Revision 1.12  2005/07/12 19:17:30  nicks
 * Add Productions function works.
 *
 * Revision 1.11  2005/07/08 20:21:28  nicks
 * Add Opera works!!!
 * ** Need to add code to scrub text data to convert things like '<' and '&'
 * ** to their XML-entity equiv's: &lt; and &amp;
 *
 * Revision 1.10  2005/07/06 20:53:30  nicks
 * Can now add a season to the SeasonPanel!
 *
 * Revision 1.9  2005/07/01 19:15:16  nicks
 * Added invokeLater construct to make the AboutAmato window thread-safe.
 * It doesn't really need this, but I'm learning how to make swing
 * code thread safe and this seemed like as good as any a place to
 * experiment.
 *
 * Revision 1.8  2005/06/30 15:39:39  nicks
 * Added serialVersionUID to keep the 5.0 compiler happy.
 * (sheesh!)
 *
 * Revision 1.7  2005/06/28 20:14:30  nicks
 * Prelim stuff to support printing.  Mostly code to provide the name
 * of the current panel.  The panel itself will have a routine that returns
 * the Printable to the PrinterJob object in Tabbed panes.
 *
 * Revision 1.6  2005/06/25 19:10:46  nicks
 * Commented out or deleted several debug lines.
 *
 * Revision 1.5  2005/06/25 18:53:51  nicks
 * Now instantiates an AboutAmato object in the About action.
 *
 * Revision 1.4  2005/06/23 20:36:35  nicks
 * Save operation not checks to see if file already exists
 * and pops up a confirmation dialog to see if it's ok to overwrite.
 *
 * Revision 1.3  2005/06/23 19:43:28  nicks
 * File operations (other than Print) now working.
 *
 * Revision 1.2  2005/06/22 20:48:28  nicks
 * Removed the deprecated constructor (the one that didn't use
 * the aMap argument.)
 *
 * Revision 1.1  2005/06/21 18:43:06  nicks
 * Initial revision
 *
 */


import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.*;

public class MenuBar extends JPanel {

    private static final long serialVersionUID = 20050630112670L;

    public JMenuBar mb;

    public MenuBar() {
	
	FileMenuAction a_new = new FileMenuAction ("New");
	JMenuItem mi_new = new JMenuItem(a_new);

	FileMenuAction a_open = new FileMenuAction ("Open");
	JMenuItem mi_open = new JMenuItem(a_open);

	FileMenuAction a_save = new FileMenuAction ("Save");
	JMenuItem mi_save = new JMenuItem(a_save);

	FileMenuAction a_save_as = new FileMenuAction ("Save As");
	JMenuItem mi_save_as = new JMenuItem(a_save_as);

	FileMenuAction a_print = new FileMenuAction ("Print");
	JMenuItem mi_print = new JMenuItem(a_print);

	FileMenuAction a_exit = new FileMenuAction ("Exit");
	JMenuItem mi_exit = new JMenuItem(a_exit);

	JMenu m_file = new JMenu ("File");

	m_file.add (mi_new);
	m_file.add (mi_open);
	m_file.addSeparator ();
	m_file.add (mi_save);
	m_file.add (mi_save_as);
	m_file.addSeparator ();
	m_file.add (mi_print);
	m_file.addSeparator ();
	m_file.add (mi_exit);

	JMenuItem mi_about = new JMenuItem(new HelpMenuAction ("About"));
	JMenu m_help = new JMenu ("Help");

	m_help.add (mi_about);

	mb = new JMenuBar ();
	mb.setBorder (new BevelBorder (BevelBorder.RAISED));

	mb.add (m_file);
	mb.add (m_help);

	//setSize (700, 20);
	setVisible (true);

    }

    class FileMenuAction extends AbstractAction 
    {
	private static final long serialVersionUID = 20050630112671L;
	public FileMenuAction (String text)
	{
	    super (text);
	}

	public void actionPerformed (ActionEvent e)
	{
	    String cmd = e.getActionCommand ();

	    if (cmd.equals ("Exit")) {
		Amato.db.checkToSave (cmd);

		System.exit (0);
	    }

	    else if (cmd.equals ("New")) {
		Amato.loadAndGo (null);
	    }

	    else if (cmd.equals ("Open")) {
		Amato.db.checkToSave (cmd);

		FilePicker fp = new FilePicker (cmd);
		String f = fp.getSelectedFilePath ();
		
		if (f != null) {
		    Amato.loadAndGo (f);
		}
	    }

	    else if (cmd.equals ("Save As")) {
		FilePicker fp = new FilePicker (cmd);
		String f = fp.getSelectedFilePath ();

		save_as (f);
	    }
	    
	    else if (cmd.equals ("Save")) {
		String f = Amato.db.getDBFileName();

		if (f == null) {
		    FilePicker fp = new FilePicker (cmd);
		    f = fp.getSelectedFilePath ();
		}

		save_as (f);
	    }
	    
	    else if (cmd.equals ("Print")) {
		Amato.getTabbedPanes().print ();
	    }

	    else {
		System.out.println ("should never get here!");
	    }
	}

	private void save_as (String fn)
	{
	    if (fn == null) return;

	    String dbfn = Amato.db.getDBFileName ();
	    File outputFile = new File (fn);
	    //System.err.println ("dbfn: " + dbfn);
	    //System.err.println ("  fn: " + fn);

	    if (outputFile.exists() && !fn.equals (dbfn)) {

		if (Amato.confirmPopup (outputFile.getName() + " exists\nOverwrite?",
					"Overwrite?") != 0)
		    return; // Operation cancelled.
	    }
	    
	    // Looks like we can (over)write this 'un.
	    Amato.db.save_as (fn);
	}

    }


    class HelpMenuAction extends AbstractAction 
    {
	private static final long serialVersionUID = 20050630112673L;
	public HelpMenuAction (String text)
	{
	    super (text);
	}

	public void actionPerformed (ActionEvent e)
	{
	    String addend = e.getActionCommand ();

	    if (addend.equals ("About")) {
		// Make this thread-safe.  (Like we really need to?)
		// Mostly doing this for my own education and edification.
		Runnable showAboutDialog = new Runnable () {
			public void run() { 
			    JOptionPane.showMessageDialog (null, 
							   new AboutAmato (),
							   "About AMATO",
							   JOptionPane.PLAIN_MESSAGE);
			}
		    };
		SwingUtilities.invokeLater (showAboutDialog);
	    }
	}
    }


    public static void main (String[] args) {

	MenuBar m = new MenuBar ();

	JFrame frame = new JFrame ("Menu Bar");

	frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
	frame.setSize (700, 500);
	frame.setJMenuBar (m.mb);
	frame.getContentPane().add (m, BorderLayout.NORTH);
	frame.setVisible (true);
    }
}
