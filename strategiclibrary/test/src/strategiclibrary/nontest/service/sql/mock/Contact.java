/*
 * Created on Sep 10, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package strategiclibrary.nontest.service.sql.mock;

import java.util.Set;
import java.util.TreeSet;

/**
 * Represents a taskmaster contact.
 * @author Michael Stover
 * @version $Revision: 1.1 $
 */
public class Contact
{
    private String username,email,fax,pager,homePhone,workPhone,cellPhone,firstName,lastName;
    private Contact manager;
    private Todo todo;
    private boolean retrieved = false;
    long id = -1;
    String objectType = "";
    
	/**
	 * @return Returns the todo.
	 */
	public Todo getTodo() {
		return todo;
	}
	/**
	 * @param todo The todo to set.
	 */
	public void setTodo(Todo todo) {
		this.todo = todo;
	}
    private Set underlings = new TreeSet();
    private boolean superUser = false;
    
    /**
     * 
     */
    public Contact()
    {
        super();
        setObjectType("Contact");
    }

    /**
     * @param id
     * @param displayName
     */
    public Contact(long id, String displayName)
    {
        setId(id);
        setObjectType("Contact");
    }
    
    public Contact(long id,String displayName,String username)
    {
        this(id,displayName);
        setUsername(username);
    }

    /**
     * @return
     * String
     */
    public String getCellPhone()
    {
        return cellPhone;
    }

    /**
     * @return
     * String
     */
    public String getEmail()
    {
        return email;
    }

    /**
     * @return
     * String
     */
    public String getFax()
    {
        return fax;
    }

    /**
     * @return
     * String
     */
    public String getFirstName()
    {
        return firstName;
    }

    /**
     * @return
     * String
     */
    public String getHomePhone()
    {
        return homePhone;
    }

    /**
     * @return
     * String
     */
    public String getLastName()
    {
        return lastName;
    }

    /**
     * @return
     * String
     */
    public String getPager()
    {
        return pager;
    }

    /**
     * @return
     * String
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * @return
     * String
     */
    public String getWorkPhone()
    {
        return workPhone;
    }

    /**
     * @param string
     * void
     */
    public void setCellPhone(String string)
    {
        cellPhone = string;
    }

    /**
     * @param string
     * void
     */
    public void setEmail(String string)
    {
        email = string;
    }

    /**
     * @param string
     * void
     */
    public void setFax(String string)
    {
        fax = string;
    }

    /**
     * @param string
     * void
     */
    public void setFirstName(String string)
    {
        firstName = string;
    }

    /**
     * @param string
     * void
     */
    public void setHomePhone(String string)
    {
        homePhone = string;
    }

    /**
     * @param string
     * void
     */
    public void setLastName(String string)
    {
        lastName = string;
    }

    /**
     * @param string
     * void
     */
    public void setPager(String string)
    {
        pager = string;
    }

    /**
     * @param string
     * void
     */
    public void setUsername(String string)
    {
        username = string;
    }

    /**
     * @param string
     * void
     */
    public void setWorkPhone(String string)
    {
        workPhone = string;
    }

    /* (non-Javadoc)
     * @see strategiclibrary.taskmaster.service.dbObjects.NamedObject#getDisplayName()
     */
    public String getDisplayName()
    {
        return getLastName() + ", " + getFirstName();
    }

    /**
     * @return
     * boolean
     */
    public boolean isSuperUser()
    {
        return superUser;
    }

    /**
     * @return
     * Set
     */
    public Set getUnderlings()
    {
        return underlings;
    }

    /**
     * @param b
     * void
     */
    public void setSuperUser(boolean b)
    {
        superUser = b;
    }

    /**
     * @param set
     * void
     */
    public void setUnderlings(Set set)
    {
        underlings = set;
    }

   /**
    * @return Returns the manager.
    */
   public Contact getManager()
   {
      return manager;
   }
   /**
    * @param manager The manager to set.
    */
   public void setManager(Contact manager)
   {
      this.manager = manager;
   }
    /**
     * @return Returns the retrieved.
     */
    public boolean isRetrieved() {
        return retrieved;
    }
    /**
     * @param retrieved The retrieved to set.
     */
    public void setRetrieved(boolean retrieved) {
        this.retrieved = retrieved;
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
