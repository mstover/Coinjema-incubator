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
public class ActionTaglet implements Taglet
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
		return "action";
	}
	
	/**
     * Register this Taglet.
     * @param tagletMap  the map to register this tag to.
     */
    public static void register(Map tagletMap) {
       ActionTaglet tag = new ActionTaglet();
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
		StringBuffer result = new StringBuffer("<dt><b>Action Name:</b><dd><code>");
		result.append(tag.text());
		result.append("</code>");
		return result.toString();
	}
	/**
	 * @see com.sun.tools.doclets.Taglet#toString(com.sun.javadoc.Tag)
	 */
	public String toString(Tag[] tag)
	{
		StringBuffer result = new StringBuffer("");
		for (int i = 0; i < tag.length; i++)
		{
			result.append(toString(tag[i]));
		}
		return result.toString();
	}
}
