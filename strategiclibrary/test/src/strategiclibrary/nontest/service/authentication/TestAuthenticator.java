/*
 * Created on Mar 8, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package strategiclibrary.nontest.service.authentication;

import java.security.Principal;

import javax.security.auth.x500.X500Principal;

import strategiclibrary.service.authentication.AuthenticationService;


/**
 * @author mstover
 *
 * Test authenticator for use when testing.  The test authenticator will do three things: if username=throw, it throws a 
 * ServiceException.  If username=fail, it returns null.  Otherwise, a new Principal object is return.
 */
public class TestAuthenticator implements AuthenticationService
{
   /* (non-Javadoc)
    * @see strategiclibrary.service.authentication.AuthenticationService#login(java.lang.String, java.lang.String)
    */
   public Principal login(String username, String password)
   {
      if(username.equals("fail"))
      {
         return null;
      }
      else if ("throw".equals(username))
      {
         throw new RuntimeException("Test authenticator throwing planned exception");
      }
      return new X500Principal(username);
   }
}
