package strategiclibrary.service.webaction.taglet;
import java.util.Map;

import com.sun.javadoc.Tag;
import com.sun.tools.doclets.Taglet;
/**
 * @author Administrator
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 */
public class ParamTaglet implements Taglet
{
	/**
	 * @see com.sun.tools.doclets.Taglet#inField()
	 */
	public boolean inField()
	{
		return false;
	}
	/**
	 * @see com.sun.tools.doclets.Taglet#inConstructor()
	 */
	public boolean inConstructor()
	{
		return false;
	}
	/**
	 * @see com.sun.tools.doclets.Taglet#inMethod()
	 */
	public boolean inMethod()
	{
		return false;
	}
	/**
	 * @see com.sun.tools.doclets.Taglet#inOverview()
	 */
	public boolean inOverview()
	{
		return false;
	}
	/**
	 * @see com.sun.tools.doclets.Taglet#inPackage()
	 */
	public boolean inPackage()
	{
		return false;
	}
	/**
	 * @see com.sun.tools.doclets.Taglet#inType()
	 */
	public boolean inType()
	{
		return true;
	}
	/**
	 * @see com.sun.tools.doclets.Taglet#isInlineTag()
	 */
	public boolean isInlineTag()
	{
		return false;
	}
	/**
	 * @see com.sun.tools.doclets.Taglet#getName()
	 */
	public String getName()
	{
		return "requestParam";
	}
	/**
     * Register this Taglet.
     * @param tagletMap  the map to register this tag to.
     */
    public static void register(Map tagletMap) {
       Taglet tag = new ParamTaglet();
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
		StringBuffer result = new StringBuffer("<dt><b>Request Parameters:</b><dd>");
		for (int i = 0; i < tag.length; i++)
		{
            result.append("<li>");
			result.append(toString(tag[i]));
			result.append("</li>");
		}
		return result.toString();
	}
}
