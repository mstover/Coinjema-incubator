package strategiclibrary.service.classfinder;



import java.util.List;

import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

/**
 * This class finds classes that implement one or more specified interfaces.
 *
 * @author  <a href="mailto:mstover@glbx.net">Michael Stover</a>
 * @version $Revision: 1.1 $
 */
@CoinjemaObject(type="classfinder")
public class DefaultClassFinderService implements ClassFinderService
{
    List<String> paths;
    ClassFinder cf;
    
    public DefaultClassFinderService()
    {
    	cf = new ClassFinder();
    }


    /* (non-Javadoc)
     * @see org.apache.avalon.framework.configuration.Configurable#configure(org.apache.avalon.framework.configuration.Configuration)
     */
    @CoinjemaDependency(method="paths")
    public void setPaths(List<String> paths)
    {
        this.paths = paths;
    }

   /* (non-Javadoc)
    * @see strategiclibrary.service.classfinder.ClassFinderService#findClassesThatExtend(java.lang.Class[], boolean)
    */
   public List findClassesThatExtend(Class[] superClasses, boolean innerClasses) throws Exception
   {
      return cf.findClassesThatExtend(superClasses, paths, innerClasses);
   }
   /* (non-Javadoc)
    * @see strategiclibrary.service.classfinder.ClassFinderService#findClassesThatExtend(java.lang.Class[])
    */
   public List findClassesThatExtend(Class[] superClasses) throws Exception
   {
      return cf.findClassesThatExtend(superClasses, paths, false);
   }
}