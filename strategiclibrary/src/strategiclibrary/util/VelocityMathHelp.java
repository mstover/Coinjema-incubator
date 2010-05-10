/*
 * Created on Apr 9, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package strategiclibrary.util;


/**
 * @author mstover
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class VelocityMathHelp
{

   public boolean equals(Number value1, Number value2)
   {
      if(value1 == null || value2 == null)
      {
         return false;
      }
      return value1.equals(value2);
   }

   public boolean equals(float value1, int value2)
   {
      return value1 == value2;
   }
}
