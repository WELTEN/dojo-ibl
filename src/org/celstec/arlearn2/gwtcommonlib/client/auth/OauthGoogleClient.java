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
package org.celstec.arlearn2.gwtcommonlib.client.auth;

public class OauthGoogleClient extends OauthClient {
	private static  String client_id_google ;
	private static  String redirect_uri ;

	public static void init(String clientId, String redirectUri) {
		client_id_google = clientId;
		redirect_uri = redirectUri;
	}
	
	public String getLoginRedirectURL() {
		 return "https://accounts.google.com/o/oauth2/auth?redirect_uri=" + redirect_uri +
                 "&response_type=code&client_id=" + client_id_google + "&approval_prompt=force" +
                 "&scope=profile+email";
	}

    public String getLoginRedirectURLWithGlass() {
        //https://accounts.google.com/o/oauth2/auth?access_type=offline&approval_prompt=force&
        // client_id=279764831309-02t5m59sjr824ojv2sm8vk864jk5porg.apps.googleusercontent.com&
        // redirect_uri=http://localhost:8080/oauth2callback
        // &response_type=code
        // &scope=https://www.googleapis.com/auth/glass.timeline%20https://www.googleapis.com/auth/glass.location%20https://www.googleapis.com/auth/userinfo.profile
        return "https://accounts.google.com/o/oauth2/auth?redirect_uri=" + redirect_uri +
                "&response_type=code&client_id=" + client_id_google + "&approval_prompt=force" +
                "&scope=https://www.googleapis.com/auth/glass.timeline https://www.googleapis.com/auth/glass.location https://www.googleapis.com/auth/userinfo.profile  https://www.googleapis.com/auth/userinfo.email";
    }

}
