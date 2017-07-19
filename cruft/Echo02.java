
import java.io.*;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;


public class Echo02 extends DefaultHandler
{
    public static void main (String argv[])
    {
	if (argv.length != 1) {
	    System.err.println ("Usage: cmd filename");
	    System.exit(1);
	}

	// Use an instance of ourselves as the SAX event handler
	DefaultHandler handler = new Echo02();

	// Use the default (non-validating) parser
	SAXParserFactory factory = SAXParserFactory.newInstance();

	try {
	    // Set up output stream
	    out = new OutputStreamWriter (System.out , "UTF8");
	    
	    // Parse the input
	    SAXParser saxParser = factory.newSAXParser();
	    saxParser.parse (new File (argv[0]), handler);
			     
	} catch (Throwable t) {
	    t.printStackTrace ();
	}
	System.exit (0);
    }

    static private Writer out;
    
    private String indentString = "    "; // Amount to indent
    private int indentLevel = 0;

    public void startDocument() throws SAXException
    {
	nl();
	nl();
	emit ("START DOCUMENT");
	nl();
	emit ("<?xml version='1.0' encoding 'UTF8'?>");
    }

    public void endDocument () throws SAXException
    {
	nl();
	emit ("END DOCUMENT");
	try {
	    out.flush();

	} catch (IOException e) {
	    throw new SAXException ("I/O error", e);
	}
    }


    public void startElement (String namespaceURI,
			      String sName, // simple name (localName)
			      String qName, //qualified name
			      Attributes attrs) throws SAXException
    {
	String eName = sName; // element name
	
	if ("".equals (eName)) eName = qName; // namespaceAware = false
	
	++indentLevel;
	nl(); emit ("ELEMENT: ");
	emit ("<" + eName + ">");
	
	if (attrs != null) {
	    for (int i = 0; i < attrs.getLength(); i++) {
		String aName = attrs.getLocalName (i); // Attr name
		if ("".equals (aName)) aName = attrs.getQName (i);
		nl();
		emit ("    ATTR: ");
		//emit (attrs.getLocalName (i));
		emit (aName);
		emit ("\t\"");
		emit (attrs.getValue (i));
		emit ("\"");
		
	    }
	}
	//if (attrs.getLength() > 0); nl();
    }

    public void endElement (String namespaceURI,
			    String sName, // simple name
			    String qName // qualified name
			    ) throws SAXException
    {
	String eName = sName; // element name
	
	if ("".equals (eName)) eName = qName; // namespaceAware = false

	nl();
	emit ("END_ELM: ");
	emit ("</" + eName + ">");
	--indentLevel;
    }

    public void characters (char buf[], int offset, int len) 
	throws SAXException
    {
	nl(); emit ("CHARS: ");
	String s = new String (buf, offset, len);
	if (!s.trim().equals("")) emit (s);
    }

    private void emit (String s) throws SAXException
    {
	try {
	    out.write (s);
	    out.flush ();
	} catch (IOException e) {
	    throw new SAXException ("I/O error", e);
	}
    }
    
    private void nl () throws SAXException
    {
	String lineEnd = System.getProperty ("line.separator");
	try {
	    out.write (lineEnd);
	    for (int i=0; i < indentLevel; i++) out.write (indentString);

	} catch (IOException e) {
	    throw new SAXException ("I/O error", e);
	}
    }
}
