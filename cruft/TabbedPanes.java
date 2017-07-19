// Tabbed Panes 10.6
// pg 284

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TabbedPanes extends JFrame {

    public TabbedPanes() {
	super ("Tabbed Panes");
	setSize (400, 300);
	setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
	
	JPanel productionSettings = new JPanel ();
	JPanel performanceSettings = new JPanel ();
	JPanel operaSettings = new JPanel ();
	JPanel talentSettings = new JPanel ();

	JTabbedPane tabs = new JTabbedPane ();
	tabs.add ("Operas", operaSettings);
	tabs.add ("Productions", productionSettings);
	tabs.add ("Performances", performanceSettings);
	tabs.add ("Talent", talentSettings);

	JPanel pane = new JPanel ();
	tabs.setSize (400, 300);

	BorderLayout bord = new BorderLayout ();
	pane.setLayout (bord);
	pane.add ("Center", tabs);

	JTextArea edit = new JTextArea (8, 40);
	JScrollPane scroll = new JScrollPane (edit);
	operaSettings.add (scroll);

	setContentPane (pane);
	show ();
    }

    public static void main (String[] args) {
	TabbedPanes t = new TabbedPanes();
	t.pack ();
	t.setVisible (true);
    }
}
