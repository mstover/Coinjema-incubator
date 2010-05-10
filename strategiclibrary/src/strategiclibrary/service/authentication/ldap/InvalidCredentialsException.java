/*
 * Created on Oct 23, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package strategiclibrary.service.authentication.ldap;


/**
 * @author Michael Stover
 * @version $Revision: 1.1 $
 */
public class InvalidCredentialsException extends RuntimeException
{ 
	private static final long serialVersionUID = 1;

    /**
     * 
     */
    public InvalidCredentialsException() {
        super();
    }

    /**
     * @param message
     * @param cause
     */
    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     */
    public InvalidCredentialsException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public InvalidCredentialsException(Throwable cause) {
        super(cause);
    }

}
