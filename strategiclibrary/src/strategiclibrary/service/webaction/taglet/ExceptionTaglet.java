/*
 * Created on Oct 27, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package strategiclibrary.service.webaction.taglet;

import java.util.Map;

import com.sun.javadoc.Tag;
import com.sun.tools.doclets.Taglet;

/**
 * @author Michael Stover
 * @version $Revision: 1.1 $
 */
public class ExceptionTaglet implements Taglet
{

    /* (non-Javadoc)
     * @see com.sun.tools.doclets.Taglet#inField()
     */
    public boolean inField()
    {
        return false;
    }

    /* (non-Javadoc)
     * @see com.sun.tools.doclets.Taglet#inConstructor()
     */
    public boolean inConstructor()
    {
        return false;
    }

    /* (non-Javadoc)
     * @see com.sun.tools.doclets.Taglet#inMethod()
     */
    public boolean inMethod()
    {
        return false;
    }

    /* (non-Javadoc)
     * @see com.sun.tools.doclets.Taglet#inOverview()
     */
    public boolean inOverview()
    {
        return false;
    }

    /* (non-Javadoc)
     * @see com.sun.tools.doclets.Taglet#inPackage()
     */
    public boolean inPackage()
    {
        return false;
    }

    /* (non-Javadoc)
     * @see com.sun.tools.doclets.Taglet#inType()
     */
    public boolean inType()
    {
        return true;
    }

    /* (non-Javadoc)
     * @see com.sun.tools.doclets.Taglet#isInlineTag()
     */
    public boolean isInlineTag()
    {
        return false;
    }

    /* (non-Javadoc)
     * @see com.sun.tools.doclets.Taglet#getName()
     */
    public String getName()
    {
        return "actionException";
    }
    
    /**
     * Register this Taglet.
     * @param tagletMap  the map to register this tag to.
     */
    public static void register(Map tagletMap) {
       ExceptionTaglet tag = new ExceptionTaglet();
       Taglet t = (Taglet) tagletMap.get(tag.getName());
       if (t != null) {
           tagletMap.remove(tag.getName());
       }
       tagletMap.put(tag.getName(), tag);
    }

    /**
     * @see com.sun.tools.doclets.Taglet#toString(com.sun.javadoc.Tag)
     */
    public String toString(Tag tag)
    {
        try
        {
            String[] parts = tag.text().split(" ",2);
            StringBuffer result = new StringBuffer("<b>");
            result.append(parts[0]);
            result.append("</b>");
            result.append(" - ");
            result.append(parts[1]);
            return result.toString();
        }
        catch (RuntimeException e)
        {
            e.printStackTrace();
            return "error occurred: "+e;
        }
    }
    /**
     * @see com.sun.tools.doclets.Taglet#toString(com.sun.javadoc.Tag)
     */
    public String toString(Tag[] tag)
    {
        if(tag == null  || tag.length == 0)
        {
            return "";
        }
        StringBuffer result = new StringBuffer("<dt><b>Possible Exceptions:</b><dd>");
        for (int i = 0; i < tag.length; i++)
        {
            result.append("<li>");
            result.append(toString(tag[i]));
            result.append("</li>");
        }
        return result.toString();
    }

}
