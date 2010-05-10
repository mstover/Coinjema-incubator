/*
 * Created on Oct 24, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package strategiclibrary.service.sql;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import strategiclibrary.util.Converter;


/**
 * @author Michael Stover
 * @version $Revision: 1.1 $
 */
public class SqlConverter implements Serializable
{
	private static final long serialVersionUID = 1;
    public String escapeSql(String value)
    {
        if(value == null)
        {
            return "";
        }
        int index = 0;
        StringBuffer escapedValue = new StringBuffer();
        for(int i = 0;i < value.length();i++)
        {
            escapedValue.append(value.charAt(i));
            if(value.charAt(i) == '\'')
            {
                escapedValue.append("'");
            }
        }
        return escapedValue.toString();
    }
    
    public String insertSpaceBreaks(String v, String insertion)
    {
       return Converter.insertSpaceBreaks(v,insertion);
    }
    
    public String calToString(Calendar cal,String pattern)
    {
        if(cal == null)
        {
            return "";
        }
        DateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(cal.getTime());
    }
    
    public String toString(Date d,String pattern)
    {
        if(d == null)
        {
            return "";
        }
        DateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(d);
    }
    
    public Calendar getCalendar(int daysAgo)
    {
       Calendar cal = Calendar.getInstance();
       cal.add(Calendar.DATE, (-1) * daysAgo);
       return cal;
    }
    
    public String getCalendarString(int daysAgo,String pattern)
    {
       return calToString(getCalendar(daysAgo),pattern);
    }
}
