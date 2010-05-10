/*
 * Created on Jan 6, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package strategiclibrary.service.authentication.ldap;

import java.security.Principal;


class LdapUser implements Principal {
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.security.Principal#getName()
	 */
	public String getName() {
		return username;
	}

	String username;

	String realPassword;

	String password;

	String dn;

	String searchBase;

	String ldapUrl;

	public LdapUser(String username) {
		this.username = username;
	}

	public LdapUser() {
	}
}