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
package org.celstec.arlearn2.api;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.run.Action;
import org.celstec.arlearn2.beans.serializer.json.JsonBeanSerialiser;
import org.celstec.arlearn2.delegators.UsersDelegator;
import org.codehaus.jettison.json.JSONException;

public class Service {
	
	protected Account account;
	protected String token;

	protected UsersDelegator verifyCredentials(String authToken) {
		UsersDelegator qu = new UsersDelegator(authToken);
		if (qu.getCurrentUserAccount()== null) return null;
		return qu;
	}
	
	protected Object getInvalidCredentialsBean() {
		Bean error = new Bean();
		error.setError("credentials are invalid");
		error.setErrorCode(Bean.INVALID_CREDENTIALS);
		return error;
	}
	
	protected Object getBeanDoesNotParseException(String specificErrorMessage) {
		Bean error = new Bean();
		error.setError("Could not parse bean: "+specificErrorMessage);
		return error;
	}

	protected boolean validCredentials(String authToken) {
		UsersDelegator qu;
			qu = new UsersDelegator(authToken);
			account = qu.getCurrentAccount();
			token = qu.getAuthToken();
			if (account != null) return true;
			return (qu.getCurrentUserAccount() != null);

	}

    public boolean isAdministrator(String authToken) {
        return validCredentials(authToken) && account.isAdministrator();
    }
	
	protected String toJson(Object bean) {
		if (bean == null) return "";
		return bean.toString();
	}
	
	protected String serialise(Object bean, String accept) {
		if  ("application/json".equalsIgnoreCase(accept)) return toJson(bean);
		if  (accept != null && accept.contains("application/json")) return toJson(bean);
		if  ("*/*".equalsIgnoreCase(accept)) return toJson(bean);
		return toJson(bean);
//		return accept + " is not yet supported\n";
	}
	
	protected Object deserialise(String beanString, Class beanClass, String contentType) {
		if  (contentType == null || "application/json".equalsIgnoreCase(contentType) ||  "*/*".equalsIgnoreCase(contentType)) 
			return jsonDeserialise(beanString, beanClass);
		if (contentType != null && contentType.contains("application/json"))
			return jsonDeserialise(beanString, beanClass);
		return contentType + " is not yet supported\n";
	}
	
	protected Object jsonDeserialise(String beanString, Class beanClass) {
		JsonBeanDeserializer jbd;
		try {
			jbd = new JsonBeanDeserializer(beanString);
			return (Bean) jbd.deserialize(beanClass);
		} catch (JSONException e) {
			return e.getMessage();
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	
}
