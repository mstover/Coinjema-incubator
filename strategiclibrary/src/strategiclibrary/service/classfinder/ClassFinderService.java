/*
 * Created on Aug 28, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package strategiclibrary.service.classfinder;

import java.util.List;

/**
 * The ClassFinder service finds classes that implement or extend other classes.  With this service, it is possible to find all implementations of
 * a given interface.
 * 
 */
public interface ClassFinderService
{
    
    /**
     * Find classes that extend the given class objects.
     * @param superClasses
     * @return A list of Class objects.
     */
    public List findClassesThatExtend(Class[] superClasses) throws Exception;
    
    /**
     * Find classes that extend the given class objects.  An extra boolean argument indicates whether inner classes should be included.
     * @param superClasses
     * @param innerClasses
     * @return A list of Class objects.
     */
    public List findClassesThatExtend(Class[] superClasses,boolean innerClasses) throws Exception;
}
