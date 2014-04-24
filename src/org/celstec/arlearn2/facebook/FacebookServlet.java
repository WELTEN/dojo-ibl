package org.celstec.arlearn2.facebook;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


//import com.restfb.Connection;
//import com.restfb.DefaultFacebookClient;
//import com.restfb.FacebookClient;
//import com.restfb.types.User;
import org.celstec.arlearn2.oauth.OauthFbWorker;

/**
 * ****************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * <p/>
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Contributors: Stefaan Ternier
 * ****************************************************************************
 */
@Deprecated
public class FacebookServlet extends HttpServlet {
//    private FacebookClient facebookClient;
    private static final Logger log = Logger.getLogger(FacebookServlet.class.getName());

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Enumeration<String> names = request.getParameterNames();
        PrintWriter writer = response.getWriter();

        while (names.hasMoreElements()) {
            String name = names.nextElement();
            log.log(Level.SEVERE, "name  "+name+ " "+request.getParameter(name));
            writer.print("name  " + name + " " + request.getParameter(name));

        }
        writer.close();
        String code = request.getParameter("code");
        OauthFbWorker fbWorker = new OauthFbWorker();
        fbWorker.setCode(code);
        fbWorker.setResponse(response);
        fbWorker.exchangeCodeForAccessToken();
        writer.print("<script> top.location.href='https://apps.facebook.com/122952504562527'</script>");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String signedRequest = (String) request.getParameter("signed_request");
        log.log(Level.SEVERE, "signedRequest "+signedRequest);

        String oauthToken = null;
        log.log(Level.SEVERE, "getOauth_token "+oauthToken);

        PrintWriter writer = response.getWriter();
        if(oauthToken == null) {

            for (Cookie cookie: request.getCookies()){
                writer.print("cookie "+ cookie.getName()+ " " + cookie.getValue());
                writer.close();
            }

        }else {

//            facebookClient = new DefaultFacebookClient(oauthToken);
//            User user = facebookClient.fetchObject("me", User.class);
//            writer.print("<img src=\"https://graph.facebook.com/" + user.getId() + "/picture\"/> I am " + user.getName() +" - " + user.getId() + "");
//            writer.close();

        }

    }

}
