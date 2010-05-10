/*
 * Created on Apr 28, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package strategiclibrary.util;

/**
 * @author mstover
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class MutableFloat
{
   public float value;
   
   public MutableFloat()
   {
      value = 0;
   }
   
   public MutableFloat(float v)
   {
      value = v;
   }
   
   public String toString()
   {
      return Float.toString(value);
   }
   /**
    * @return Returns the value.
    */
   public float getValue()
   {
      return value;
   }
   /**
    * @param value The value to set.
    */
   public void setValue(float value)
   {
      this.value = value;
   }
}
