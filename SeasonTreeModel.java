/**
 * $Id: SeasonTreeModel.java,v 1.3 2005/08/21 19:06:23 nicks Exp nicks $
 * 
 * Derived from DefaultTreeModel for use with the panel(s) that
 * depict information about Seaons / Productions / Performances.
 *
 * Originally part of the AMATO application.
 * 
 * @author Nick Seidenman <nick@seidenman.net>
 * @version $Revision: 1.3 $
 *
 * $Log: SeasonTreeModel.java,v $
 * Revision 1.3  2005/08/21 19:06:23  nicks
 * Changed comment header to conform with javadoc conventions.
 *
 * Revision 1.2  2005/07/22 22:12:39  nicks
 * Started work on Drag-n-Drop within RosterPanel.
 * Cleaned up nearly all warnings.  Only a few left in Opera.java, Operas.java,
 * and Performance.java and these pertain to strict type-checking ala 1.5.
 * Just wanted to check everything in now as a check point.  I'll continue
 * to work on getting a totally clean compilation.
 *
 * Revision 1.1  2005/07/14 18:20:26  nicks
 * Initial revision
 *
 */

import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.tree.*;


public class SeasonTreeModel extends DefaultTreeModel
{
    private static final long serialVersionUID = 20050722112860L;

    public SeasonTreeModel (DefaultMutableTreeNode parent)
    {
	super (parent);
    }
}


