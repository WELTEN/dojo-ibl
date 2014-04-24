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


import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.celstec.arlearn2.beans.AuthResponse;
import org.celstec.arlearn2.delegators.MailDelegator;
import org.celstec.arlearn2.jdo.UserLoggedInManager;

//import com.google.gdata.client.ClientLoginAccountType;
//import com.google.gdata.client.GoogleAuthTokenFactory.UserToken;
//import com.google.gdata.client.GoogleService;
//import com.google.gdata.util.AuthenticationException;

@Deprecated
@Path("/login")
public class ClientLogin extends Service {
	private static final Logger log = Logger.getLogger(ClientLogin.class.getName());

//	private GoogleService service;
	
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes("text/plain")
	public String clientData(String data, @DefaultValue("application/json") @HeaderParam("Accept") String accept) {
//		String username = data.substring(0, data.indexOf("\n"));
//		String password = data.substring(data.indexOf("\n")+1);
//		service = new GoogleService("fusiontables", "ARLearn2");
//		AuthResponse ar = new AuthResponse();
//		try {
//			service.setUserCredentials(username, password,ClientLoginAccountType.GOOGLE);
//		} catch (AuthenticationException e) {
//			log.log(Level.SEVERE, "failed "+e.getMessage());
//			ar.setError("Authentication failed: "+e.getMessage());
//			return null;
//		}
//		String token = ((UserToken)service.getAuthTokenFactory().getAuthToken()).getValue();
//		ar.setAuth(token);
//		UserLoggedInManager.submitUser(username, token);
//		return serialise(ar, accept);
        return null;
	}
	
	@POST
	@Path("/instructions/{account}")
	public String createRole(@HeaderParam("Authorization") String token,  
			@PathParam("account") String account, @DefaultValue("application/json") @HeaderParam("Accept") String accept) {
//		if (!validCredentials(token))
//			return serialise(getInvalidCredentialsBean(), accept);
//		MailDelegator md = new MailDelegator(token);
//		md.sendInstructionMail("stefaan.ternier@gmail.com", "Stefaan Ternier", account);
		return null;
	}
}
