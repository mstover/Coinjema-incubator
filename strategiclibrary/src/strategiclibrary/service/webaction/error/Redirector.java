/*
 * Created on Sep 2, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package strategiclibrary.service.webaction.error;

import strategiclibrary.service.webaction.HandlerData;

/**
 * The Redirector service is responsible for matching up error messages with URL's to redirect to.  It first searches the given HandlerData
 * object for matches to the errors that exist in the ErrorsBean.NAME key.  If no matches are found, it will then search for matches
 * in from the configuration script.
 * 
 * Sample Configuration:
 * <service role="strategiclibrary.service.webaction.error.Redirector" class="strategiclibrary.service.webaction.error.DefaultRedirectorr">
 *  <exception message=""">URL to redirect to</exception>
 * </service>
 */
public interface Redirector
{
    public static final String REDIRECT_ON = "redirectOn_";
	public static String ROLE = Redirector.class.getName();
    
    public String getRedirectPage(HandlerData requestInfo);
    
    public String getLogicalRedirect(HandlerData requestInfo);
}
