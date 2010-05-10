/*
 * Created on Oct 24, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package strategiclibrary.service.template;


/**
 * @author Michael Stover
 * @version $Revision: 1.1 $
 */
public class NoSuchTemplateException extends RuntimeException
{
	private static final long serialVersionUID = 1;

    /**
     * 
     */
    public NoSuchTemplateException() {
        super();
    }

    /**
     * @param message
     * @param cause
     */
    public NoSuchTemplateException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     */
    public NoSuchTemplateException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public NoSuchTemplateException(Throwable cause) {
        super(cause);
    }

}
