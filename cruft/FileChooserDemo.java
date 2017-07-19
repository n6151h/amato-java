// $Id: FileChooserDemo.java,v 1.1 2005/06/21 14:47:20 nicks Exp $
// 
// Learning how to use JFileChooser.
//
// Originally part of the AMATO application.
// 
// Nick Seidenman <nick@seidenman.net>
//
// $Log: FileChooserDemo.java,v $
// Revision 1.1  2005/06/21 14:47:20  nicks
// Initial revision
//
//

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.*;

public class FileChooserDemo extends JPanel 
				     implements ActionListener
						
{
    static private final String newline = "\n";
    JButton openButton, saveButton;
    JTextArea log;
    JFileChooser fc;

    public FileChooserDemo ()
    {
	super (new BorderLayout ());

	// Create the log first -- action listeners need to refer to it.
	log = new JTextArea (5, 20);
	log.setMargin (new Insets (5, 5, 5, 5));
	log.setEditable (false);
	JScrollPane logScrollPane = new JScrollPane (log);

	// Create a file chooser
	fc = new JFileChooser ();

	fc.setFileSelectionMode (JFileChooser.DIRECTORIES_ONLY);
	fc.setFileSelectionMode (JFileChooser.FILES_AND_DIRECTORIES);

	// Create the open button
	openButton = new JButton ("Open ...");
	openButton.addActionListener (this);

	// Create the save button
	saveButton = new JButton ("Save ...");
	saveButton.addActionListener (this);

	// Put the buttons in a separate panel.
	JPanel buttonPanel = new JPanel();  //use FlowLayout.
	buttonPanel.add (openButton);
	buttonPanel.add (saveButton);

	// Add the buttons and the log to this panel.
	add (buttonPanel, BorderLayout.PAGE_START);
	add(logScrollPane, BorderLayout.CENTER);
    }

    public void actionPerformed (ActionEvent e) 
    {
	// Handle open button action
	if (e.getSource() == openButton) {
	    int returnVal =- fc.showOpenDialog (FileChooserDemo.this);
	    
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
		File file = fc.getSelectedFile ();
		// This is where a real application would open the file.
		log.append ("Opening: " + file.getName() + ".\n");
	    }
	    else {
		log.append ("Open command cancelled by user.\n");
	    }
	    log.setCaretPosition (log.getDocument().getLength());
	}

	// handle seve button action.
	else if (e.getSource() == saveButton) {
	    int returnVal =- fc.showOpenDialog (FileChooserDemo.this);
	    
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
		File file = fc.getSelectedFile ();
		// This is where a real application would open the file.
		log.append ("Saving: " + file.getName() + ".\n");
	    }
	    else {
		log.append ("Save command cancelled by user.\n");
	    }
	    log.setCaretPosition (log.getDocument().getLength());
	}
    }

    private static void createAndShowGUI () 
    {
	JFrame.setDefaultLookAndFeelDecorated (true);
	JDialog.setDefaultLookAndFeelDecorated(true);

	JFrame frame = new JFrame ("FileChooserDemo");
	frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);

	JComponent newContentPane = new FileChooserDemo ();
	newContentPane.setOpaque (true);
	frame.setContentPane (newContentPane);

	frame.pack();
	frame.setVisible (true);
    }

    public static void main (String[] args)
    {
	javax.swing.SwingUtilities.invokeLater (new Runnable() {
		public void run () {
		    createAndShowGUI ();
		}
	    });
	
    }
}
