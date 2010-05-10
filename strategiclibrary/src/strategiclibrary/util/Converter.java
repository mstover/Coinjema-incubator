/*
 * Created on Oct 22, 2003
 * 
 * To change the template for this generated file go to Window>Preferences>Java>Code Generation>Code and Comments
 */
package strategiclibrary.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.StringTokenizer;

/**
 * @author Michael Stover
 * @version $Revision: 1.1 $
 */
public class Converter {

	/**
	 * Convert the given value object to an object of the given type
	 * 
	 * @param value
	 * @param toType
	 * @return Object
	 */
	public static Object convert(Object value, Class toType) {
		if (value == null) {
			value = "";
		} else if (toType.isAssignableFrom(value.getClass())) {
			return value;
		} else if (toType.equals(float.class) || toType.equals(Float.class)) {
			return new Float(getFloat(value));
		} else if (toType.equals(double.class) || toType.equals(Double.class)) {
			return new Double(getDouble(value));
		} else if (toType.equals(String.class)) {
			return getString(value);
		} else if (toType.equals(int.class) || toType.equals(Integer.class)) {
			return new Integer(getInt(value));
		} else if (toType.equals(char.class) || toType.equals(Character.class)) {
			return new Character(getChar(value));
		} else if (toType.equals(long.class) || toType.equals(Long.class)) {
			return new Long(getLong(value));
		} else if (toType.equals(boolean.class) || toType.equals(Boolean.class)) {
			return new Boolean(getBoolean(value));
		} else if (toType.equals(java.util.Date.class)) {
			return getDate(value);
		} else if (toType.equals(Calendar.class)) {
			return getCalendar(value);
		} else if (toType.equals(Class.class)) {
			try {
				return Class.forName(value.toString());
			} catch (Exception e) {
				// don't do anything
			}
		}
		return value;
	}

	static Collection<DateFormat> formats = new LinkedList<DateFormat>();
	static {
		formats.add(DateFormat.getDateInstance(DateFormat.SHORT));
		formats.add(DateFormat.getDateInstance(DateFormat.MEDIUM));
		formats.add(DateFormat.getDateInstance(DateFormat.LONG));
		formats.add(DateFormat.getDateInstance(DateFormat.FULL));
		formats.add(new SimpleDateFormat("yyyy-MM-dd"));
		formats.add(new SimpleDateFormat("yyyy-MMM-dd"));
		formats.add(new SimpleDateFormat("dd-MMM-yyyy"));
		formats.add(new SimpleDateFormat("yyyyMMdd"));
		formats.add(new SimpleDateFormat("yyyy/MM/dd"));
		formats.add(new SimpleDateFormat("dd/MM/yyyy"));
		formats.add(new SimpleDateFormat("dd/MM/yy"));
	}

	public static void registerDateFormat(DateFormat df) {
		formats.add(df);
	}

	/**
	 * Converts the given object to a calendar object. Defaults to the current
	 * date if the given object can't be converted.
	 * 
	 * @param date
	 * @return Calendar
	 */
	public static Calendar getCalendar(Object date, Calendar defaultValue) {
		Calendar cal = new GregorianCalendar();
		if (date != null && date instanceof java.util.Date) {
			cal.setTime((java.util.Date) date);
			return cal;
		} else if (date != null && date instanceof java.sql.Timestamp) {
			cal.setTime((java.sql.Timestamp) date);
			return cal;
		} else if (date != null) {
			java.util.Date d = null;
			for (DateFormat format : formats) {
				try {
					d = format.parse(date.toString());
					if (d != null)
						break;
				} catch (Exception e) {
				}

			}
			if (d == null)
				cal = defaultValue;
			else
				cal.setTime(d);
		} else {
			cal = defaultValue;
		}
		return cal;
	}

	public static Calendar getCalendar(Object o) {
		return getCalendar(o, new GregorianCalendar());
	}

	public static Date getDate(Object date) {
		return getDate(date, Calendar.getInstance().getTime());
	}

	public static String urlEncode(Object toEncode) {
		try {
			return URLEncoder.encode(toEncode.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return toEncode.toString();
		}
	}

	public static Date getDate(Object date, Date defaultValue) {
		Date val = null;
		if (date != null && date instanceof java.util.Date) {
			return (Date) date;
		} else if (date != null) {
			DateFormat formatter = DateFormat.getDateInstance(DateFormat.SHORT);
			java.util.Date d = null;
			try {
				val = formatter.parse(date.toString());
			} catch (ParseException e) {
				formatter = DateFormat.getDateInstance(DateFormat.MEDIUM);
				try {
					val = formatter.parse((String) date);
				} catch (ParseException e1) {
					formatter = DateFormat.getDateInstance(DateFormat.LONG);
					try {
						val = formatter.parse((String) date);
					} catch (ParseException e2) {
						formatter = DateFormat.getDateInstance(DateFormat.FULL);
						try {
							val = formatter.parse((String) date);
						} catch (ParseException e3) {
							return defaultValue;
						}
					}
				}
			}
		} else {
			return defaultValue;
		}
		return val;
	}

	public static String formatNumber(float num, String pattern) {
		try {
			NumberFormat format = new DecimalFormat(pattern);
			return format.format((double) num);
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	public static String formatNumber(Object num, String pattern) {
		try {
			return formatNumber(getDouble(num), pattern);
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	public static String formatNumber(double num, String pattern) {
		try {
			NumberFormat format = new DecimalFormat(pattern);
			return format.format(num);
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	public static String encodeHtml(String input) {
		return input.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}

	public static String encodeHtml(Object input) {
		return encodeHtml(input.toString());
	}

	public static float getFloat(Object o, float defaultValue) {
		try {
			if (o == null) {
				return defaultValue;
			}
			if (o instanceof Number) {
				return ((Number) o).floatValue();
			} else {
				return Float.parseFloat(parseNumberEnding(o.toString()));
			}
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	public static float getFloat(Object o) {
		return getFloat(o, 0);
	}

	public static boolean getBoolean(Object o) {
		return getBoolean(o, false);
	}

	public static double getDouble(Object o, double defaultValue) {
		try {
			if (o == null) {
				return defaultValue;
			}
			if (o instanceof Number) {
				return ((Number) o).doubleValue();
			} else {
				return Double.parseDouble(parseNumberEnding(o.toString()));
			}
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
	
	private static String parseNumberEnding(String number)
	{
		if(number != null && number.length() > 1)
		{
			if(number.endsWith("K") || number.endsWith("k"))
				number = number.substring(0,number.length()-1) + "000";
		}
		return number;
	}

	public static double getDouble(Object o) {
		return getDouble(o, 0);
	}

	public static boolean getBoolean(Object o, boolean defaultValue) {
		if (o == null) {
			return defaultValue;
		} else if (o instanceof Boolean) {
			return ((Boolean) o).booleanValue();
		} else
			return new Boolean(o.toString()).booleanValue();
	}

	/**
	 * Convert object to integer, return defaultValue if object is not
	 * convertible or is null.
	 * 
	 * @param o
	 * @param defaultValue
	 * @return int
	 */
	public static int getInt(Object o, int defaultValue) {
		try {
			if (o == null) {
				return defaultValue;
			}
			if (o instanceof Number) {
				return ((Number) o).intValue();
			} else {
				return Integer.parseInt(parseNumberEnding(o.toString()));
			}
		} catch (NumberFormatException e) {
			try {
				return (int)Float.parseFloat(o.toString());
			}catch (NumberFormatException err) {}
			return defaultValue;
		}
	}

	public static char getChar(Object o) {
		return getChar(o, ' ');
	}

	public static char getChar(Object o, char defaultValue) {
		try {
			if (o == null) {
				return defaultValue;
			}
			if (o instanceof Character) {
				return ((Character) o).charValue();
			} else if (o instanceof Byte) {
				return (char) ((Byte) o).byteValue();
			} else if (o instanceof Integer) {
				return (char) ((Integer) o).intValue();
			} else {
				String s = o.toString();
				if (s.length() > 0) {
					return o.toString().charAt(0);
				} else
					return defaultValue;
			}
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * Converts object to an integer, defaults to 0 if object is not convertible
	 * or is null.
	 * 
	 * @param o
	 * @return int
	 */
	public static int getInt(Object o) {
		return getInt(o, 0);
	}

	/**
	 * Converts object to a long, return defaultValue if object is not
	 * convertible or is null.
	 * 
	 * @param o
	 * @param defaultValue
	 * @return long
	 */
	public static long getLong(Object o, long defaultValue) {
		try {
			if (o == null) {
				return defaultValue;
			}
			if (o instanceof Number) {
				return ((Number) o).longValue();
			} else {
				return Long.parseLong(parseNumberEnding(o.toString()));
			}
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	/**
	 * Converts object to a long, defaults to 0 if object is not convertible or
	 * is null
	 * 
	 * @param o
	 * @return long
	 */
	public static long getLong(Object o) {
		return getLong(o, 0);
	}

	public static String formatDate(Date date, String pattern) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}

	public static String formatDate(java.sql.Date date, String pattern) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}

	public static String formatDate(String date, String pattern) {
		return formatDate(getCalendar(date, null), pattern);
	}

	public static String formatDate(Calendar date, String pattern) {
		return formatCalendar(date, pattern);
	}

	public static String formatCalendar(Calendar date, String pattern) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date.getTime());
	}

	/**
	 * Converts object to a String, return defaultValue if object is null.
	 * 
	 * @param o
	 * @param defaultValue
	 * @return String
	 */
	public static String getString(Object o, String defaultValue) {
		if (o == null) {
			return defaultValue;
		}
		return o.toString();
	}

	public static String insertLineBreaks(String v, String insertion) {
		if (v == null) {
			return "";
		} else {
			StringBuffer replacement = new StringBuffer();
			StringTokenizer tokens = new StringTokenizer(v, "\n", true);
			while (tokens.hasMoreTokens()) {
				String token = tokens.nextToken();
				if (token.compareTo("\n") == 0) {
					replacement.append(insertion);
				} else {
					replacement.append(token);
				}
			}
			return replacement.toString();
		}
	}

	public static String insertSpaceBreaks(String v, String insertion) {
		return v.trim().replaceAll("\\s+", insertion);
	}

	/**
	 * Converts object to a String, defaults to empty string if object is null.
	 * 
	 * @param o
	 * @return String
	 */
	public static String getString(Object o) {
		return getString(o, "");
	}

	public static String capitalizeAll(Object s) {
		if (s == null)
			return "";
		else {
			String[] words = s.toString().replaceAll("_", " ").split(" ");
			StringBuffer buf = new StringBuffer();
			for (int i = 0; i < words.length; i++) {
				if (words[i].length() > 1) {
					words[i] = words[i].substring(0, 1).toUpperCase()
							+ words[i].substring(1);
				} else if (words[i].length() == 1) {
					words[i] = words[i].toUpperCase();
				}
				buf.append(words[i]);
				if (i + 1 < words.length)
					buf.append(" ");
			}
			return buf.toString();
		}
	}

}