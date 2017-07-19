/**
 *
 * $Id: SingerTable.java,v 1.3 2005/09/12 17:36:44 nicks Exp nicks $
 * 
 * Depict the roster of singers as a table of (sortable) columns 
 * that include the singer's name, function, voice type, level, and pub
 * status.  The table is draggable, i.e. a row from the table can
 * be DnD'd into another component, such as a casting panel.
 * 
 * Originally part of the AMATO application.
 * 
 * @author Nick Seidenman <nick@seidenman.net>
 * @version $Revision: 1.3 $
 *
 * $Log: SingerTable.java,v $
 * Revision 1.3  2005/09/12 17:36:44  nicks
 * If a singer is being cast in more than one role, a pop-up warning
 * is displayed and the user is asked if this is ok.  If it is, the singer
 * is (n-tuple cast).
 *
 * Revision 1.2  2005/08/11 22:16:22  nicks
 * Commented out debug println cruft.
 *
 * Revision 1.1  2005/08/11 22:12:20  nicks
 * Initial revision
 *
 *
 *
 */ 

import java.util.*;
import java.io.*;

import uic.*;
import uic.model.*;
import uic.widgets.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.border.*;

class SingerTable 
    extends UICTable
    implements Serializable, DragSourceListener, DragGestureListener
{
    private static final long serialVersionUID = 20050809000010L;
    
    private SingerTableModel stm;
    private TableRowSorter trs;
    private int selRow;
    private int draggedRow;

    public SingerTable (Roster rost)
    {
	super ();

	stm = new SingerTableModel (rost);

	trs = new TableRowSorter (stm);
	setModel (trs);

	trs.addMouseListenerToHeaderInTable (this);
	trs.addSortedColumn (1, true);
	trs.addSortedColumn (2, true);
	trs.addSortedColumn (3, true);

	this.getSelectionModel().setSelectionMode (ListSelectionModel.SINGLE_SELECTION);

	DragSource ds = DragSource.getDefaultDragSource ();
	
	ds.createDefaultDragGestureRecognizer (this, DnDConstants.ACTION_COPY, this);

	setAutoResizeMode (AUTO_RESIZE_ALL_COLUMNS);
	
	selRow = -1;

	doLayout ();

	// Adjust the dimensions of the table a little.
	Dimension td = new Dimension (getPreferredScrollableViewportSize ());
	int height = java.lang.Math.min (getRowCount() , 5) * getRowHeight();
	td.setSize (td.getWidth(), height);
	setPreferredScrollableViewportSize(td);
    }
    
    /** Overload JTable's method with a little action of our own. **/
    public void valueChanged (ListSelectionEvent e)
    {
	super.valueChanged (e);
	
	selRow = getSelectedRow ();
	if (selRow < 0) return;
	
	// Because TableRowSorter keeps an array of indexes into
	// the table model, rather than its own copy, we need to 
	// translate the selected row index into the index of the
	// row in the model proper. Hence ...
	selRow = trs.getOriginalIndex (selRow);
    }   

    /** Overload tableChanged method in TableRowSorter **/
    //    public void tableChanged (TableModelEvent tme)
    //    {
    //	super.tableChanged (tme);
    //    }

    public void dragDropEnd (DragSourceDropEvent e) {}

    public void dragEnter (DragSourceDragEvent e) {}

    public void dragExit (DragSourceEvent e) {}

    public void dragOver (DragSourceDragEvent e) {}

    public void dragAction (DragSourceDragEvent e) 
    { 
	//System.out.println ("dragAction: " + e); 
    }
    
    public void dragGestureRecognized (DragGestureEvent e)
    {
	if (selRow < 0) { // No row selected (yet): do nothing.
	    return;
	}

	// Drag anything.
	//System.out.println ("dragging " + this.stm.getSingerAt (selRow));
	draggedRow = selRow;
	e.startDrag (DragSource.DefaultCopyDrop,
		     new SingerTableTransferable (this.stm.getSingerAt (selRow)),
		     this);
    }
    
    public void dropActionChanged (DragSourceDragEvent e) 
    { 
	//System.out.println ("dropActionChanged: " + e);
    } 

    public class SingerDataFlavor 
	extends DataFlavor
    {
	private static final long serialVersionUID = 20050809000020L;
	
	public SingerDataFlavor () { 
	    super (Singer.class, "AMATO Singer Object");
	}
    }
    
    
    class SingerTableTransferable
	implements Transferable
    {
	private static final long serialVersionUID = 20050809000030L;
	
	transient private Singer singer;
	
	public SingerTableTransferable (Singer s) 
	{ 
	    this.singer = s;
	}
	
	public Object getTransferData (DataFlavor df)
	{
	    return (Object) singer;
	}
	
	final public DataFlavor[] getTransferDataFlavors () 
	{
	    return new DataFlavor[] { new SingerDataFlavor() };
	}
	
	public boolean isDataFlavorSupported (DataFlavor df)
	{
	    return df instanceof SingerDataFlavor;
	}
    }
    
    
    class SingerTableModel
	extends AbstractTableModel
	implements Serializable
    {
	private static final long serialVersionUID = 20050808000010L;
	
	private DefaultMutableTreeNode singers;
	private String[] columnHeadings = {"Name", "Voice Type", "Level", "Status"};
	
	public SingerTableModel (Roster rost)
	{
	    super ();
	    
	    singers = rost.getSingers ();
	}
	
	public int getColumnCount () { return columnHeadings.length; }
	
	public int getRowCount () { return singers.getChildCount (); }
	
	public String getColumnName (int col) 
	{ 
	    //System.out.println (columnHeadings[col]);
	    return columnHeadings[col]; 
	}
	
	public Object getValueAt (int r, int c) 
	{
	    Singer s = (Singer) singers.getChildAt (r);
	    
	    if (c == 0) { // Name
		return (Object) s.toString();
	    }
	    
	    else if (c == 1) { // Voice Type
		return (Object) s.getVoices ();
	    }
	    
	    else if (c == 2) { // Level
		return (Object) s.getProficiency ();
	    }
	    
	    else if (c ==3) { // Status
		return (Object) s.getStatus ();
	    }
	    
	    else { // Should never happen.
		System.err.println ("WARNING: column out of bounds.");
		return null;
	    }
	}	
	
	/* So that we can get the Singer instance associated with 
	 * particular row.
	 */
	public Singer getSingerAt (int row)
	{
	    // range check ...
	    if (row < 0 || row >= singers.getChildCount())
		return null;
	    //System.out.println ("getSingerAt(" + row + ")");
	    return (Singer) singers.getChildAt (row);
	}
    }
}



	    
