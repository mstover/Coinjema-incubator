/*
 * Created on Nov 6, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package strategiclibrary.util;

/**
 * @author ano ano
 * @version $Revision: 1.1 $
 */
public class OrderedObject implements Comparable
{
    Object value;
    float order;
    
    public OrderedObject(Object v, float o)
    {
        value = v;
        order = o;
    }

    public int compareTo(Object o)
    {
        if (o instanceof OrderedObject)
        {
            OrderedObject oo = (OrderedObject) o;
            return (order - oo.order > 0) ? 1 : ((order-oo.order == 0) ? 0 : -1);
        }
        else
        {
            return -1;
        }
    }

    /**
     * @return
     * int
     */
    public float getOrder()
    {
        return order;
    }

    /**
     * @return
     * Object
     */
    public Object getValue()
    {
        return value;
    }

    /**
     * @param i
     * void
     */
    public void setOrder(float i)
    {
        order = i;
    }

    /**
     * @param object
     * void
     */
    public void setValue(Object object)
    {
        value = object;
    }

}
