/**
 * 
 */
package com.coinjema.client.comments;

import java.io.Serializable;

/**
 * @author mstover
 * 
 */
public class ClientUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String name;

	public ClientUser() {

	}

	/**
	 * 
	 */
	public ClientUser(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
