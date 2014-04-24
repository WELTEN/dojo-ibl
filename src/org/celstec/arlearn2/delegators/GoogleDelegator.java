/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
package org.celstec.arlearn2.delegators;

import java.util.StringTokenizer;
import java.util.logging.Logger;

import org.celstec.arlearn2.api.Service;
import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.jdo.classes.ApplicationAccessKeyJDO;
import org.celstec.arlearn2.jdo.manager.AccountManager;
import org.celstec.arlearn2.jdo.manager.ApplicationKeyManager;

import org.celstec.arlearn2.tasks.beans.GenericBean;

public class GoogleDelegator {
	protected static final Logger logger = Logger.getLogger(GoogleDelegator.class.getName());

	protected String authToken;
	protected boolean onBehalfOf = false;
	protected Account account;

	public GoogleDelegator(String authToken) {
		if (authToken == null) {
			this.authToken = null;
		} else if (authToken.contains("auth=")) {
			authToken = authToken.substring(authToken.indexOf("auth=") + 5);
			this.authToken = authToken;
		} else if (authToken.startsWith("onBehalfOf")) {
			StringTokenizer st = new StringTokenizer(authToken, ":");
			String onBehalfOfToken = "";
			String accountType = "";
			String accountLocalId = "";
			if (st.hasMoreTokens()) st.nextToken();
			if (st.hasMoreTokens()) onBehalfOfToken = st.nextToken();
			if (st.hasMoreTokens()) accountType = st.nextToken();
			if (st.hasMoreTokens()) accountLocalId = st.nextToken();
			boolean tokenExists = ApplicationKeyManager.getConfigurationObject(onBehalfOfToken);
			if (tokenExists) {
				onBehalfOf = true;
				try {
					account = AccountManager.getAccount(accountType+":"+accountLocalId);
				} catch (Exception e){
					System.out.println("account does not exist");
				}
			}
			
		} else {
            this.authToken =authToken;
        }
	}

	public GoogleDelegator(Account account, String token){
		this.authToken = token;
		this.account = account;

	}
	
	public GoogleDelegator(GoogleDelegator gd) {
		this.authToken = gd.authToken;
		this.account = gd.account;
	}

//    public GoogleDelegator(String token, String account) {
//        this.authToken = token;
//        JsonBeanDeserializer jbd = new JsonBeanDeserializer(account);
//        this.account = (Account) jbd.deserialize(Account.class);
//
//    }

	public GoogleDelegator() {
	}

    public GoogleDelegator(GenericBean bean) {
        this.authToken = bean.getToken();
        this.account = bean.getAccountBean();
    }

	public GoogleDelegator(Service service) {
		this.authToken = service.getToken();
		this.account = service.getAccount();
	}

	public String getAuthToken() {
		return authToken;
	}

}
