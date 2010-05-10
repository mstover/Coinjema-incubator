/*
 * Created on Jan 6, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package strategiclibrary.service.authentication.ldap;


import java.util.Properties;


/**
 * @author mike
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LdapProperties
{
   boolean singlePass;
   String dn;
   String userAttribute;
   String searchBase;
   String password;
   String url;
   String name;
   Properties connectionProperties;
   
   public LdapProperties()
   {
      
   }
   
   /**
    * @return Returns the dn.
    */
   public String getDn()
   {
      return dn;
   }
   /**
    * @param dn The dn to set.
    */
   public void setDn(String dn)
   {
      this.dn = dn;
   }
   /**
    * @return Returns the password.
    */
   public String getPassword()
   {
      return password;
   }
   /**
    * @param password The password to set.
    */
   public void setPassword(String password)
   {
      this.password = password;
   }
   /**
    * @return Returns the searchBase.
    */
   public String getSearchBase()
   {
      return searchBase;
   }
   /**
    * @param searchBase The searchBase to set.
    */
   public void setSearchBase(String searchBase)
   {
      this.searchBase = searchBase;
   }
   /**
    * @return Returns the singlePass.
    */
   public boolean isSinglePass()
   {
      return singlePass;
   }
   /**
    * @param singlePass The singlePass to set.
    */
   public void setSinglePass(boolean singlePass)
   {
      this.singlePass = singlePass;
   }
   /**
    * @return Returns the url.
    */
   public String getUrl()
   {
      return url;
   }
   /**
    * @param url The url to set.
    */
   public void setUrl(String url)
   {
      this.url = url;
   }
   /**
    * @return Returns the userAttribute.
    */
   public String getUserAttribute()
   {
      return userAttribute;
   }
   /**
    * @param userAttribute The userAttribute to set.
    */
   public void setUserAttribute(String userAttribute)
   {
      this.userAttribute = userAttribute;
   }
   
   /**
    * Make the default user for this ldap server for searching and initial bind purposes.
    * @param username
    * @param password
    * @return
    */
   LdapUser makeSearchUser(String username, String password) {
      LdapUser searchUser = new LdapUser(username);
      searchUser.dn = getDn();
      searchUser.realPassword = password;
      if (singlePass) {
         searchUser.dn = new StringBuffer(getUserAttribute()).append("=").append(
               searchUser.username).append(",").append(searchUser.dn)
               .append(",").append(getSearchBase()).toString();
      }
      searchUser.password = (getPassword().length() > 0) ? getPassword() : password;
      if (searchUser.password == null || searchUser.password.length() == 0) {
         searchUser.password = password;
      }
      searchUser.ldapUrl = getUrl();
      searchUser.searchBase = getSearchBase();
      return searchUser;
   }
   /**
    * @return Returns the name.
    */
   public String getName()
   {
      return name;
   }
   /**
    * @param name The name to set.
    */
   public void setName(String name)
   {
      this.name = name;
   }

/**
 * @return Returns the connectionProperties.
 */
public Properties getConnectionProperties() {
    return connectionProperties;
}

/**
 * @param connectionProperties The connectionProperties to set.
 */
public void setConnectionProperties(Properties connectionProperties) {
    this.connectionProperties = connectionProperties;
}
}
