/*
 * Created on Oct 23, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package strategiclibrary.service.authentication;

import java.security.Principal;

/**
 * @author Michael Stover
 * @version $Revision: 1.1 $
 */
public interface AuthenticationService
{
    /**
     * Login in a user.  Return null if login info is invalid.
     * @param username
     * @param password
     * @return
     * Principle
     */
    public Principal login(String username,String password);
}
