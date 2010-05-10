/*
 * Created on Oct 23, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package strategiclibrary.service.authentication.ldap;

import java.security.Principal;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;

import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

import strategiclibrary.service.authentication.AuthenticationService;

/**
 * @author Michael Stover
 * @version $Revision: 1.1 $
 */
@CoinjemaObject(type="ldapService")
public class LdapAuthenticator implements AuthenticationService {

    protected LdapProperties[] ldapProperties;

    Map backdoor = new HashMap();
    
    @CoinjemaDependency(method="servers")
    public void setLdapServers(LdapProperties[] servers)
    {
        this.ldapProperties = servers;
    }
    
    @CoinjemaDependency(method="backdoorUsers",hasDefault=true)
    public void setBackdoorUsers(Map<String,String> backdoors)
    {
        this.backdoor = backdoors;
    }

    protected boolean findLdapUser(LdapUser user, LdapProperties ldap,
            boolean singlePass) {
        Hashtable srchEnv = new Hashtable(11);
        srchEnv.put(Context.INITIAL_CONTEXT_FACTORY,
                "com.sun.jndi.ldap.LdapCtxFactory");
        srchEnv.put(Context.PROVIDER_URL, user.ldapUrl);
        srchEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
        srchEnv.put(Context.SECURITY_PRINCIPAL, user.dn);
        srchEnv.put(Context.SECURITY_CREDENTIALS, user.password);

        String[] returnAttribute = { "distinguishedName" };

        SearchControls srchControls = new SearchControls();
        srchControls.setReturningAttributes(returnAttribute);
        srchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        String searchFilter = "(" + ldap.getUserAttribute() + "="
                + user.username + ")";

        getLog().debug("checking ldap with " + srchEnv);
        try {
            DirContext srchContext = new InitialDirContext(srchEnv);
            if (singlePass)
                return true;
            NamingEnumeration srchResponse = srchContext.search(ldap
                    .getSearchBase(), searchFilter, srchControls);

            if (srchResponse.hasMore()) {
                String distName = srchResponse.nextElement().toString();
                getLog().debug(
                        "found dn for " + user.username + " of " + distName);
                user.dn = distName.substring(
                        distName.indexOf("distinguishedName:") + 18,
                        distName.length() - 1).trim();
                user.password = user.realPassword;
                return findLdapUser(user, ldap, true);
            } else {
                return false;
            }

        } catch (Exception e) {
            getLog().info("Failure to login: ", e);
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see strategiclibrary.service.authentication.AuthenticationService#login(java.lang.String,
     *      java.lang.String)
     */
    public Principal login(String username, String password) {
        if (backdoor.containsKey(username)
                && (!backdoor.containsKey(username + ":password") || password
                        .equals(backdoor.get(username + ":password")))) {
            return new LdapUser((String) backdoor.get(username));
        }
        if (password == null || password.length() == 0) {
            throw new InvalidCredentialsException("Problem authenticating with LDAP");
        }
        for (int i = 0; i < ldapProperties.length; i++) {
            if (ldapProperties[i] != null && ldapProperties[i].getName() != null
                    && ldapProperties[i].getName().length() > 0) {
                LdapUser user = ldapProperties[i].makeSearchUser(username,
                        password);
                try {
                    boolean success = findLdapUser(user, ldapProperties[i],
                            ldapProperties[i].isSinglePass());
                    if (success) {
                        getLog().debug("username = " + user.username);
                        return user;
                    } else {
                        getLog().debug(
                                "Failed to log in to server "
                                        + ldapProperties[i].getName());
                    }
                } catch (Exception ex) {
                    getLog().debug(
                            "Failed to log in to server "
                                    + ldapProperties[i].getName());
                }
            }
        }
        throw new InvalidCredentialsException("Invalid Credentials");
    }
    
    private Logger log;

    @CoinjemaDependency(alias="log4j")
    public void setLog(Logger l)
    {
        log = l;
    }
    
    protected Logger getLog()
    {
        return log;
    }

}
