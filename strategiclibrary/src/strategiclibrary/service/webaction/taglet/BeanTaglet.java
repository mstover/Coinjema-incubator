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
public class BeanTaglet implements Taglet
{
    static Map tagletMap;
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
		return "bean";
	}
	/**
     * Register this Taglet.
     * @param tagletMap  the map to register this tag to.
     */
    public static void register(Map tm) {
        tagletMap = tm;
       Taglet tag = new BeanTaglet();
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
			String[] parts = tag.text().split(" ",3);
			StringBuffer result = new StringBuffer("");
            result.append(getClassText(parts[0]));
			result.append(" (Referenced as \"");
			result.append(parts[1]);
			result.append("\")");
			result.append(" - ");
			result.append(parts[2]);
			return result.toString();
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			return "error occurred: "+e;
		}
	}
    
    private String getClassText(String beanClass)
    {
        StringBuffer result = new StringBuffer();
        if(beanClass == null || beanClass.length() < 1)
        {
            return "";
        }
        char start = beanClass.charAt(0);
        if(start == '[')
        {
            result.append("A Collection of <b>");
            result.append(beanClass.substring(1,beanClass.length()-1));
        }
        else if(start == '{')
        {
            result.append("A Map of <b>");
            result.append(beanClass.substring(1,beanClass.length()-1));
        }
        else
        {
            result.append("<b>");
            result.append(beanClass);
        }
        result.append("</b>");
        return result.toString();
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
		StringBuffer result = new StringBuffer("<dt><b>Response Beans returned:</b><dd>");
		for (int i = 0; i < tag.length; i++)
		{
            result.append("<li>");
			result.append(toString(tag[i]));
			result.append("</li>");
		}
		return result.toString();
	}
}
