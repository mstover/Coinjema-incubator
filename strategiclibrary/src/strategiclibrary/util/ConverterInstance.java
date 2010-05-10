package strategiclibrary.util;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

@CoinjemaObject
public class ConverterInstance {
	Logger log;

		/**
		 * Convert the given value object to an object of the given type
		 * 
		 * @param value
		 * @param toType
		 * @return Object
		 */
		public  Object convert(Object value, Class toType) {
			return Converter.convert(value, toType);
		}

		public void registerDateFormat(DateFormat df) {
			Converter.registerDateFormat(df);
		}

		/**
		 * Converts the given object to a calendar object. Defaults to the current
		 * date if the given object can't be converted.
		 * 
		 * @param date
		 * @return Calendar
		 */
		public Calendar getCalendar(Object date, Calendar defaultValue) {
			return Converter.getCalendar(date, defaultValue);
		}

		public Calendar getCalendar(Object o) {
			return Converter.getCalendar(o);
		}

		public Date getDate(Object date) {
			return Converter.getDate(date);
		}

		public  String urlEncode(Object toEncode) {
			return Converter.urlEncode(toEncode);
		}

		public  Date getDate(Object date, Date defaultValue) {
			return Converter.getDate(date, defaultValue);
		}

		public  String formatNumber(float num, String pattern) {
			getLog().info("Formatting number with " + num + " and " + pattern);
			return Converter.formatNumber(num, pattern);
		}

		public  String formatNumber(Object num, String pattern) {
			getLog().info("Formatting number with " + num + " and " + pattern);
			return Converter.formatNumber(num, pattern);
		}

		public  String formatNumber(double num, String pattern) {
			getLog().info("Formatting number with " + num + " and " + pattern);
			return Converter.formatNumber(num, pattern);
		}

		public  String encodeHtml(String input) {
			return Converter.encodeHtml(input);
		}

		public  String encodeHtml(Object input) {
			return Converter.encodeHtml(input);
		}

		public  float getFloat(Object o, float defaultValue) {
			return Converter.getFloat(o, defaultValue);
		}

		public  float getFloat(Object o) {
			return Converter.getFloat(o);
		}

		public  boolean getBoolean(Object o) {
			return Converter.getBoolean(o);
		}

		public  double getDouble(Object o, double defaultValue) {
			return Converter.getDouble(o, defaultValue);
		}

		public  double getDouble(Object o) {
			return Converter.getDouble(o);
		}

		public  boolean getBoolean(Object o, boolean defaultValue) {
			return Converter.getBoolean(o, defaultValue);
		}

		/**
		 * Convert object to integer, return defaultValue if object is not
		 * convertible or is null.
		 * 
		 * @param o
		 * @param defaultValue
		 * @return int
		 */
		public  int getInt(Object o, int defaultValue) {
			return Converter.getInt(o, defaultValue);
		}

		public  char getChar(Object o) {
			return Converter.getChar(o);
		}

		public  char getChar(Object o, char defaultValue) {
			return Converter.getChar(o, defaultValue);
		}

		/**
		 * Converts object to an integer, defaults to 0 if object is not convertible
		 * or is null.
		 * 
		 * @param o
		 * @return int
		 */
		public  int getInt(Object o) {
			return Converter.getInt(o);
		}

		/**
		 * Converts object to a long, return defaultValue if object is not
		 * convertible or is null.
		 * 
		 * @param o
		 * @param defaultValue
		 * @return long
		 */
		public  long getLong(Object o, long defaultValue) {
			return Converter.getLong(o, defaultValue);
		}

		/**
		 * Converts object to a long, defaults to 0 if object is not convertible or
		 * is null
		 * 
		 * @param o
		 * @return long
		 */
		public  long getLong(Object o) {
			return Converter.getLong(o);
		}

		public  String formatDate(Date date, String pattern) {
			return Converter.formatDate(date, pattern);
		}

		public  String formatDate(java.sql.Date date, String pattern) {
			return Converter.formatDate(date, pattern);
		}

		public  String formatDate(String date, String pattern) {
			return Converter.formatDate(date, pattern);
		}

		public  String formatDate(Calendar date, String pattern) {
			return Converter.formatDate(date, pattern);
		}

		public  String formatCalendar(Calendar date, String pattern) {
			return Converter.formatCalendar(date, pattern);
		}

		/**
		 * Converts object to a String, return defaultValue if object is null.
		 * 
		 * @param o
		 * @param defaultValue
		 * @return String
		 */
		public  String getString(Object o, String defaultValue) {
			return Converter.getString(o, defaultValue);
		}

		public  String insertLineBreaks(String v, String insertion) {
			return Converter.insertLineBreaks(v, insertion);
		}

		public  String insertSpaceBreaks(String v, String insertion) {
			return Converter.insertSpaceBreaks(v,insertion);
		}

		/**
		 * Converts object to a String, defaults to empty string if object is null.
		 * 
		 * @param o
		 * @return String
		 */
		public  String getString(Object o) {
			return Converter.getString(o);
		}

		public  String capitalizeAll(Object s) {
			return Converter.capitalizeAll(s);
		}

		
		Logger getLog() {
			return log;
		}

		@CoinjemaDependency(alias="log4j")
		public void setLog(Logger log) {
			this.log = log;
		}
}
