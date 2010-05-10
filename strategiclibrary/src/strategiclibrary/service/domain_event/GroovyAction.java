/*
 * Created on Sep 16, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package strategiclibrary.service.domain_event;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

import java.io.InputStream;

import org.codehaus.groovy.control.CompilationFailedException;


/**
 * @author mstover
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GroovyAction extends AbstractAction implements EventAction
{
   String groovyScript;
   Binding groovyBinding;
   Script script;
   /* (non-Javadoc)
    * @see strategiclibrary.service.domain_event.EventAction#executeAction(strategiclibrary.service.domain_event.DomainEvent)
    */
   public void executeAction(DomainEvent event) throws Exception
   {
      groovyBinding = new Binding(getComponents());
     groovyBinding.setVariable("event",event);
     getLog().info("Groovy binding = " + groovyBinding);
     script.setBinding(groovyBinding);
     script.run();
   }
   
   public void setScript(InputStream in) throws CompilationFailedException
   {
       script = new GroovyShell().parse(in);
   }

}
