/*
 * Copyright 2001-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package strategiclibrary.service.webaction.tags;

import java.io.Reader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.Tag;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;
import org.coinjema.context.CoinjemaContext;

/**
 *  <p>
 *  Simple implementation of JSP tag to allow 
 *  use of VTL in JSP's.
 *  </p>
 *
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id: VelocityTag.java,v 1.1 2006/10/23 18:20:37 mikes Exp $ 
 */

public class VelocityTag implements BodyTag
{
    protected Tag          parent = null;
    protected BodyContent  bodyContent = null;
    protected PageContext  pageContext = null;

    /*
     *  strictaccess : determines if the JSPContext is used
     *  to autofetch information from the 'scopes' 
     *  or if the scopetool() is used
     */
    protected boolean      strictAccess = false;
    VelTagHandler vs;

    /**
     *  CTOR : current implementation uses the Singleton
     *  model for velocity. 
     */
    public VelocityTag()
    {
    }

    /**
     *  switch for strictaccess
     *
     *  @param sa if true, then normal VelocityContext is used
     *            and template must directly get beans from scopes
     *            Otherwise, the JSPContext is used which searches for
     *            objects/beans in the scopes automatically.
     */
    public void setStrictaccess( boolean sa )
    {
        this.strictAccess = sa;
    }

    public Tag getParent()
    {
        return parent;
    }

    public void setParent( Tag parent)
    {
        this.parent = parent;
        return;
    }

    public int doStartTag()
        throws JspException
    {
        return EVAL_BODY_TAG;
    }

    public void setBodyContent( BodyContent bc )
    {
        this.bodyContent = bc;
        return;
    }

    public void setPageContext( PageContext pc )
    {
        this.pageContext = pc;
        return;
    }

    public void doInitBody()
        throws JspException
    {
        return;
    }
   
    public int doAfterBody()
        throws JspException
    {
        return 0;
    }

    public void release()            
    {
        return;
    } 

    /**
     *  This is the real worker for this taglib. 
     *  There are efficiencies to be added - the plan 
     *  is to cache the AST to avoid reparsing every
     *  time.
     */
    public int doEndTag()
        throws JspException
    {
        /*
         *  if there is no body, we are done
         */

        if ( bodyContent == null)
            return EVAL_PAGE;
        if(vs == null)
        {
        	String cntxt = ((HttpServletRequest)pageContext.getRequest()).getRequestURI();
			if(cntxt.startsWith("/")) cntxt = cntxt.substring(1);
			vs = new VelTagHandler(new CoinjemaContext(cntxt));
        }
        try
        {
            JspWriter writer = pageContext.getOut();

            /*
             *  get our body
             */

            Reader bodyreader = bodyContent.getReader();

            /*
             * now make a JSPContext
             */

            Context vc = null;

            /*
             *  if strictAccess == true, then we want to use a regular
             *  VelocityContext, as we assume that the template will
             *  bring into the context any beans using the scope tool
             *  or the like
             */
            if (strictAccess)
            {
                vc = (Context) new VelocityContext();
            }
            else
            {
               vc = (Context)  new JSPContext( pageContext );
            }

            /*
             *  add the scope tool
             */

            vc.put( "scopetool", new ScopeTool( pageContext ) );

            vs.evaluate( vc , writer, "JSP for me!", bodyreader );
        }
        catch( Exception e )
        {
            System.out.println( e.toString() );
        }

        return EVAL_PAGE;
    }
}
