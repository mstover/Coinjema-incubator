/*
 * Created on Nov 25, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package strategiclibrary.nontest.service.sql.mock;


/**
 * @author ano ano
 * @version $Revision: 1.1 $
 */
public class Status
{

    long id = -1;
    String objectType;
    /**
     * 
     */
    public Status()
    {
        setObjectType("Status");
    }

    /**
     * @param id
     * @param displayName
     */
    public Status(long id, String displayName)
    {
        setId(id);
        setObjectType("Status");
    }

    /**
     * @return Returns the id.
     */
    public long getId() {
        return id;
    }

    /**
     * @param id The id to set.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return Returns the objectType.
     */
    public String getObjectType() {
        return objectType;
    }

    /**
     * @param objectType The objectType to set.
     */
    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }
}
