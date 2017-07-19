/**
 * $Id: Operas.java,v 1.14 2005/09/13 15:15:44 nicks Exp $
 * 
 * Container class for Opera objects.
 *
 * Originally part of the AMATO application.
 * 
 * @author Nick Seidenman <nick@seidenman.net>
 * @version $Revision: 1.14 $
 *
 * $Log: Operas.java,v $
 * Revision 1.14  2005/09/13 15:15:44  nicks
 * "changed" flag is now set for any add or delete operation.
 *
 * Revision 1.13  2005/09/12 17:36:44  nicks
 * If a singer is being cast in more than one role, a pop-up warning
 * is displayed and the user is asked if this is ok.  If it is, the singer
 * is (n-tuple cast).
 *
 * Revision 1.12  2005/08/21 19:07:34  nicks
 * Changed comment header to conform to javadoc convention.
 *
 * Revision 1.11  2005/08/15 14:26:28  nicks
 * Changed as_* to to* (more java-esque)
 *
 * Revision 1.10  2005/07/22 22:26:54  nicks
 * CLEAN COMPILATION w/ 1.5 compiler (javac -Xlint *.java)!
 *
 * Revision 1.9  2005/07/22 22:12:39  nicks
 * Started work on Drag-n-Drop within RosterPanel.
 * Cleaned up nearly all warnings.  Only a few left in Opera.java, Operas.java,
 * and Performance.java and these pertain to strict type-checking ala 1.5.
 * Just wanted to check everything in now as a check point.  I'll continue
 * to work on getting a totally clean compilation.
 *
 * Revision 1.8  2005/07/18 22:44:01  nicks
 * Added add/delete pop-up to RosterPanel and OperaPanel.
 * Still need to get RosterPanel fully working, but this
 * will depend (or, rather, how I do it will depend) on whether
 * I end up implementing some of the ideas mentioned in the TODO list.
 * (e.g. have subtrees under Singers for different voices.)
 *
 * Revision 1.7  2005/07/12 19:17:30  nicks
 * Add Productions function works.
 *
 * Revision 1.6  2005/07/08 20:45:09  nicks
 * Was erroneously subtracting 1 from lastIndexOf return in add method.
 * This was causing an exception to be thrown.  Fixed.
 *
 * Revision 1.5  2005/07/08 20:10:24  nicks
 * Changed the base class of Operas to AbstractListModel in preparation
 * for integrating AddOperaPanel.  It made more sense to think of the
 * opera collection as a list, rather than a tree.  (Really, just
 * a checkpoint here so I have something I can revert back to.)
 *
 * Revision 1.4  2005/06/30 15:39:39  nicks
 * Added serialVersionUID to keep the 5.0 compiler happy.
 * (sheesh!)
 *
 * Revision 1.3  2005/06/20 22:34:38  nicks
 * SeasonPanel is now working in a cheesy, demo-only sort of way.  It's
 * at least enough for me to get the idea across to Irene and talk
 * through how it will work when finished.d
 *
 * Revision 1.2  2005/06/18 04:19:02  nicks
 * Have OperaPanel and RosterPanel (cheesy versions) working.
 * This should be suitable for demo to Irene sometime soon.
 * I just want to more or less dummy up a SeasonPanel and I'll
 * be ready to demo.
 *
 * Revision 1.1  2005/06/15 20:22:43  nicks
 * Initial revision
 *
 */

import java.io.*;
import java.util.*;
import javax.swing.*;


public class Operas extends AbstractListModel
{
    private static final long serialVersionUID = 20050708112710L;

    private ArrayList<Opera> oList;

    public Operas ()
    {
	super ();
	oList = new ArrayList<Opera> ();
    }

    public void add (Opera o)
    {
	oList.add (o);
	sort ();

	int ondx = oList.lastIndexOf (o);

	fireIntervalAdded (o, ondx, ondx);
	if (Amato.db != null) Amato.db.setChanged(true);
    }

    public void remove (Opera o)
    {
	oList.remove (o);
	Amato.db.setChanged (true);
	fireIntervalRemoved (o, 0, oList.size());
    }

    public Object getElementAt (int ndx)
    {
	if (ndx < 0 || ndx > oList.size()) return null;

	return oList.get (ndx);
    }

    final public int getSize () { return oList.size(); }

    public String toXML ()
    {
	String result = new String ("<operas>\n");

	for (Opera s: oList) {
	    result += s.toXML ();
	    result += "\n";
	}
	
	result += "</operas>";

	return result;
    }

    final public String toHTML ()
    {
	String result = "<html><h1>Operas</h1>\n<hr>\n</html>";
	
	return result;
    }

    public JPanel toJPanel ()
    {
	JPanel p = new JPanel ();
	JLabel l = new JLabel (toHTML(), JLabel.CENTER);
	
	p.add (l);

	p.setBorder (BorderFactory.createTitledBorder (toString()));
	return p;
    }

    class Comparer implements Comparator<Opera> 
    {
	public int compare (Opera o1, Opera o2)
	{
	    return o1.getName().compareTo (o2.getName());
	}
    }

    public void sort ()
    {
	Collections.sort (oList, new Comparer ());
    }

    public Opera getByName (String n)
    {
	for (int i=0; i < oList.size(); i++) {
	    Opera op = (Opera) oList.get (i);
	    if (n.equals (op.getName()))
		return op;
	}

	return null;
    }

    public String toString () { return "Operas"; }

    public Object[] toArray () { return oList.toArray(); }
}
