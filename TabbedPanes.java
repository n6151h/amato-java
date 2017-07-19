/**
 * $Id: TabbedPanes.java,v 1.14 2005/09/09 22:32:46 nicks Exp $
 * 
 * Proof-of-concept for the overall look & feel of the AMATO 
 * application.  I intend to use this as a "wooden model" to
 * demonstrate the PoC to Irene Kim, et al.
 *
 * Originally part of the AMATO application.
 *
 * Modelled on Tabbed Panes 10.6
 * pg 284 "Teach Yourself Jave in 24 Hours" (SAMS)
 * 
 * @author Nick Seidenman <nick@seidenman.net>
 * @version $Revision: 1.14 $
 *
 * $Log: TabbedPanes.java,v $
 * Revision 1.14  2005/09/09 22:32:46  nicks
 * Changed all remaining explicit calls to JOptionPanel to Amato.errorPopup.
 *
 * Revision 1.13  2005/08/21 19:06:23  nicks
 * Changed comment header to conform with javadoc conventions.
 *
 * Revision 1.12  2005/07/22 22:26:54  nicks
 * CLEAN COMPILATION w/ 1.5 compiler (javac -Xlint *.java)!
 *
 * Revision 1.11  2005/07/22 22:12:39  nicks
 * Started work on Drag-n-Drop within RosterPanel.
 * Cleaned up nearly all warnings.  Only a few left in Opera.java, Operas.java,
 * and Performance.java and these pertain to strict type-checking ala 1.5.
 * Just wanted to check everything in now as a check point.  I'll continue
 * to work on getting a totally clean compilation.
 *
 * Revision 1.10  2005/07/14 22:27:40  nicks
 * Replaced println messages with ErrorDialog box.
 *
 * Revision 1.9  2005/07/06 20:53:30  nicks
 * Can now add a season to the SeasonPanel!
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
 * Revision 1.6  2005/06/21 21:16:02  nicks
 * EUREKA!  I've got the thing to open with the size I want
 * and the TabbedPanel has expanded to fill the frame and resized
 * when I resize the frame.  In other words, I have something I can
 * now show Irene!!!
 *
 * Revision 1.5  2005/06/21 18:43:06  nicks
 * Now have a menubar.  Starting to integrate components.
 *
 * Revision 1.4  2005/06/20 22:34:38  nicks
 * SeasonPanel is now working in a cheesy, demo-only sort of way.  It's
 * at least enough for me to get the idea across to Irene and talk
 * through how it will work when finished.d
 *
 * Revision 1.3  2005/06/20 18:27:58  nicks
 * Now sorts cast members, production names, performance date/time.
 *
 * Revision 1.2  2005/06/20 17:31:30  nicks
 * SeasonPanel (dummy'd up) now working.
 *
 * Revision 1.1  2005/06/18 22:57:54  nicks
 * Initial revision
 *
 */



import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.awt.print.*;
import javax.swing.*;
import javax.swing.event.*;

public class TabbedPanes extends JTabbedPane 
{
    private static final long serialVersionUID = 20050630112680L;

    private ArrayList<?> panels;
    
    private SeasonPanel seasonPanel;
    private RosterPanel rosterPanel;
    private OperaPanel operaPanel;

    public TabbedPanes() {
	super ();

	seasonPanel = new SeasonPanel (Amato.db.getSeasons());
	rosterPanel = new RosterPanel (Amato.db.getRoster());
	operaPanel = new OperaPanel (Amato.db.getOperas());

	add (seasonPanel);
	add (rosterPanel);
	add (operaPanel);

	addChangeListener (new TPChangeListener());
	
	setVisible (true);
    }

    class TPChangeListener implements ChangeListener 
    {
	TPChangeListener () { };

	public void stateChanged (ChangeEvent e) 
	{
	    JTabbedPane p = (JTabbedPane) e.getSource ();
	}
    }


    final public String getCurrentPanelName ()
    {
	return getSelectedComponent().getName();
    }

    final public JComponent getCurrentPanel () 
    {
	return (JComponent) getSelectedComponent ();
    }

    public void print ()
    {
	System.out.println ("Printing " + getCurrentPanelName());
	/*
	PrinterJob pj = PrinterJob.getPrinterJob ();
	
	pj.setPrintable ((JPanel) panels.get (getSelectedIndex()).getPrintable ());

	if (pj.printDialog ()) {
	    try {
		pj.print ();
	    }
	    catch (PrinterException pe) {
		Amato.errorPopup ("Exception while Printing:\n" + pe);
	    }
	}
	*/
    }
    
    public void redraw () 
    {
	seasonPanel.redraw ();
	rosterPanel.redraw ();
	operaPanel.redraw ();
    }
}

