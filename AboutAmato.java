/**
 * $Id: AboutAmato.java,v 1.10 2005/09/13 15:36:01 nicks Exp $
 *
 * The requisite "about ..." popup.
 * 
 * Originally part of the AMATO application.
 * 
 * @author Nick Seidenman <nick@seidenman.net>
 * @version $Revision: 1.10 $
 *
 * $Log: AboutAmato.java,v $
 * Revision 1.10  2005/09/13 15:36:01  nicks
 * Added a nice "Italian" green border.
 *
 * Revision 1.9  2005/09/01 21:28:36  nicks
 * Background image wasn't loading from jar file.  Figured out how to
 * do this.  Can't just use a straight file name.  Need to turn
 * it into a URL instance and then pass this to ImageIcon.
 *
 * Revision 1.8  2005/08/16 20:55:29  nicks
 * Converted header comments to javadoc conventions.
 *
 * Revision 1.7  2005/07/13 16:33:11  nicks
 * Figured out how to get the background image working and how to center
 * it, align the text overlays, etc.
 *
 * Revision 1.6  2005/06/30 15:39:39  nicks
 * Added serialVersionUID to keep the 5.0 compiler happy.
 * (sheesh!)
 *
 * Revision 1.5  2005/06/25 18:52:56  nicks
 * Played around some more with the layout, trying to get
 * the image to appear as a true background image.
 *
 * Revision 1.4  2005/06/25 05:38:32  nicks
 * Removed debug line.
 *
 * Revision 1.3  2005/06/24 15:18:58  nicks
 * Added Tony's image (tonyback.jpg from amato.org page).  Made
 * a splash page from this.
 *
 * Revision 1.2  2005/06/22 20:48:28  nicks
 * A bit more complex now.
 *
 * Revision 1.1  2005/06/22 19:20:56  nicks
 * Initial revision
 *
 */

import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import java.net.*;

public class AboutAmato extends JPanel 
{
    private static final long serialVersionUID = 20050630112740L;

    public AboutAmato ()
    {
	super ();

	// AMATO Acronym part: 
	JPanel acronym = new JPanel (new GridLayout (0, 2));
	// A ...author
	JLabel b11 = new JLabel ("A", JLabel.RIGHT);
	b11.setFont (new Font ("Serif", Font.BOLD, 20));
	b11.setForeground (Color.red);
	JLabel b12 = new JLabel ("utomated", JLabel.LEFT);
	acronym.add (b11);
	acronym.add (b12);
	// M ...
	JLabel b21 = new JLabel ("M", JLabel.RIGHT);
	b21.setFont (new Font ("Serif", Font.BOLD, 20));
	b21.setForeground (Color.red);
	JLabel b22 = new JLabel ("anagement of", JLabel.LEFT);
	acronym.add (b21);
	acronym.add (b22);
	// A ...
	JLabel b31 = new JLabel ("A", JLabel.RIGHT);
	b31.setFont (new Font ("Serif", Font.BOLD, 20));
	b31.setForeground (Color.red);
	JLabel b32 = new JLabel ("ll", JLabel.LEFT);
	acronym.add (b31);
	acronym.add (b32);
	// T ...
	JLabel b41 = new JLabel ("T", JLabel.RIGHT);
	b41.setFont (new Font ("Serif", Font.BOLD, 20));
	b41.setForeground (Color.red);
	JLabel b42 = new JLabel ("ony's", JLabel.LEFT);
	acronym.add (b41);
	acronym.add (b42);
	// O ...
	JLabel b51 = new JLabel ("O", JLabel.RIGHT);
	b51.setFont (new Font ("Serif", Font.BOLD, 20));
	b51.setForeground (Color.red);
	JLabel b52 = new JLabel ("peras", JLabel.LEFT);
	acronym.add (b51);
	acronym.add (b52);

	acronym.setBackground (Color.white);
	acronym.setOpaque (false);

	// Credit/copyright, etc.
	JPanel credit = new JPanel (new GridLayout (0, 1));
	credit.add (new JLabel ("", JLabel.CENTER));
	credit.add (new JLabel ("", JLabel.CENTER));
	credit.add (new JLabel ("", JLabel.CENTER));
	credit.add (new JLabel ("", JLabel.CENTER));
	credit.add (new JLabel ("", JLabel.CENTER));
	credit.add (new JLabel ("", JLabel.CENTER));
	credit.add (new JLabel ("A labor of love and learning", JLabel.CENTER));
	credit.add (new JLabel ("by Nick Seidenman", JLabel.CENTER));
	credit.add (new JLabel ("", JLabel.CENTER));
	credit.add (new JLabel ("Copyright (c) 2005  Nick Seidenman", JLabel.CENTER));
	credit.add (new JLabel ("All Rights Reserved.", JLabel.CENTER));
	credit.add (new JLabel ("", JLabel.CENTER));

	credit.setBackground (Color.white);
	credit.setOpaque (false);

	// Tony Amato graphic:
	URL url = this.getClass().getResource ("images/tonyback.jpg");
	ImageIcon ti = new ImageIcon (url);
	JLabel tony_graphic =  new JLabel (ti);
	tony_graphic.setOpaque (false);
	//tony_graphic.setBounds (0, 0, ti.getIconWidth(), ti.getIconHeight());
	tony_graphic.setBounds (0, 0, getWidth(), getHeight());

	JPanel tony_pane = new JPanel (new BorderLayout ());
	tony_pane.add (tony_graphic, BorderLayout.CENTER);

	// Panel to hold the acronym and credit panes.
	JPanel ap3 = new JPanel (new BorderLayout ());
	ap3.add (acronym, BorderLayout.WEST);
	ap3.add (credit, BorderLayout.EAST);
	ap3.setBorder (BorderFactory.createLineBorder (new Color (0x008000), 5));
	ap3.setOpaque (false);

	// Start loading them into the final arrangement.
	OverlayLayout ol = new OverlayLayout (this);
	setLayout (ol);
	//ap3.setBounds (0, 0, ti.getIconWidth(), ti.getIconHeight());


	add (ap3);
	add (tony_pane);
	
	setBackground (Color.white);
	setVisible (true);
    }

    public static void main (String[] args)
    {
	JFrame frame = new JFrame ("About Amato");
	frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
	frame.setSize(400, 300);

	AboutAmato aa = new AboutAmato ();

	BorderLayout bl = new BorderLayout();

	frame.getContentPane().setLayout(bl);
	frame.getContentPane().add (aa, BorderLayout.CENTER);
	
	frame.setVisible (true);
	
    }
}

