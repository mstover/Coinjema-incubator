/*
 * This class originally written by Giles Cope from users.sourceforge.net.  It is public domain
 * code.
 */

package strategiclibrary.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Utility class to handle text files as a single lump of text.
 * <p>
 * Note this is just as memory-inefficient as handling a text file can be. Use
 * with restraint.
 * 
 * @author Giles Cope (gilescope at users.sourceforge.net)
 * @author Michael Stover (mstover1 at apache.org)
 * @author <a href="mailto:jsalvata@apache.org">Jordi Salvat i Alabart</a>
 * @version $Revision: 1.1 $ updated on $Date: 2006/10/23 18:20:37 $
 */
public class TextFile extends File
{
	private static final long serialVersionUID = 1;

    /**
     * File encoding. null means use the platform's default.
     */
    private String encoding= null;
    
    /**
     * Create a TextFile object to handle the named file with the given encoding.
     * 
     * @param filename File to be read & written through this object.
     * @param encoding Encoding to be used when reading & writing this file.
     */
    public TextFile(File filename, String encoding)
    {
        super(filename.toString());
        setEncoding(encoding);
    }
    
    /**
     * Create a TextFile object to handle the named file with the platform
     * default encoding.
     * 
     * @param filename File to be read & written through this object.
     */
    public TextFile(File filename)
    {
        super(filename.toString());
    }

    /**
     * Create a TextFile object to handle the named file with the platform
     * default encoding.
     * 
     * @param filename Name of the file to be read & written through this object.
     */
    public TextFile(String filename)
    {
        super(filename);
    }

    /**
     * Create a TextFile object to handle the named file with the given
     * encoding.
     * 
     * @param filename Name of the file to be read & written through this object.
     * @param encoding Encoding to be used when reading & writing this file.
     */
    public TextFile(String filename, String encoding)
    {
        super(filename);
    }

    /**
     * Create the file with the given string as content -- or replace it's
     * content with the given string if the file already existed.
     * 
     * @param body New content for the file.
     */
    public void setText(String body)
    {
        Writer writer = null;
        try
        {
            if (encoding == null)
            {
                writer = new FileWriter(this);
            }
            else
            {
                writer = new OutputStreamWriter(
                    new FileOutputStream(this),
                    encoding);
            }
            writer.write(body);
            writer.flush();
            writer.close();
        }
        catch (IOException ioe)
        {
        	try {
				if (writer != null) {
					writer.close();
				}
				} catch (IOException e) {}
        }
    }
    
    public String[] getLines()
    {
    	Collection<String> lines = new LinkedList<String>();
    	BufferedReader file = null;
    	try
    	{
    		file = getBufferedReader();
    		String line = null;
    		while((line = file.readLine()) != null)
    		{
    			lines.add(line);
    		}
    	}
        catch (IOException ioe)
        {
        }
        finally
		{
	        if (file != null)
				try {file.close();} catch (IOException e) {}
		}
        return lines.toArray(new String[lines.size()]);    	
    }

    /**
     * Read the whole file content and return it as a string.
     *  
     * @return the content of the file
     */
    public String getText()
    {
        String lineEnd = System.getProperty("line.separator");
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        try
        {
            br = getBufferedReader();
            String line = "NOTNULL";
            while (line != null)
            {
                line = br.readLine();
                if (line != null)
                {
                    sb.append(line + lineEnd);
                }
            }
        }
        catch (IOException ioe)
        {
        }
        finally
		{
	        if (br != null)
				try {br.close();} catch (IOException e) {}
		}

        return sb.toString();
    }

	private BufferedReader getBufferedReader() throws FileNotFoundException, UnsupportedEncodingException {
		Reader reader = null;
		if (encoding == null)
		{
		    reader= new FileReader(this);
		}
		else
		{
		    reader= new InputStreamReader(
		        new FileInputStream(this),
		        encoding);
		}
		BufferedReader br = new BufferedReader(reader);
		return br;
	}

    /**
     * @return Encoding being used to read & write this file.
     */
    public String getEncoding()
    {
        return encoding;
    }

    /**
     * @param string Encoding to be used to read & write this file.
     */
    public void setEncoding(String string)
    {
        encoding= string;
    }
}
