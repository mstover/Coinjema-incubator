/*
 * Created on Aug 16, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package strategiclibrary.service.template.groovy;

import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mstover
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class GroovyTemplateEngine
{
   SimpleTemplateEngine engine = new SimpleTemplateEngine();
   Map templates = new HashMap();
   File[] paths;

   public GroovyTemplateEngine(String[] paths)
   {
      this.paths = new File[paths.length];
      for(int i = 0;i < paths.length;i++)
      {
         this.paths[i] = new File(paths[i]);
      }
   }
   
   public Template getTemplate(Reader resource) throws Exception
   {
      return engine.createTemplate(resource);
   }
   
   public Template getTemplate(String resource)
   {
      if(!templates.containsKey(resource))
      {
         Reader r = null;
         for(int i = 0;i < paths.length;i++)
         {
            File f = new File(paths[i],resource);
            if(f.exists() && f.isFile())
            {
               try
               {
                  r = new FileReader(f);
                  templates.put(resource,engine.createTemplate(r));
                  break;
               }
               catch(Exception e)
               {
                  //just try again
               }
            }
         }
      }
      return (Template)templates.get(resource);
   }
}
