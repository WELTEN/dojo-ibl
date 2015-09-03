package org.celstec.arlearn2.api;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Created by str on 28/05/14.
 */
@Path("/ElggProxy")
public class ElggProxy extends Service {

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @CacheControlHeader("no-cache")
    public String proxyGet(
            @HeaderParam("Authorization") String token,
            @DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
            @DefaultValue("application/json") @HeaderParam("Accept") String accept,
            @Context final HttpServletRequest request

    ) {
        if (!validCredentials(token))
            return serialise(getInvalidCredentialsBean(), accept);
        String result = "";
        try {
            System.out.println(request.getPathInfo());
            String urlString = "http://inquiry.wespot.net/services/api/rest/json/?";
            for (Object paramObject :  request.getParameterMap().entrySet()) {
                Map.Entry<String, String[]> param = (Map.Entry<String, String[]>) paramObject;
                urlString += param.getKey() + "=" + request.getParameter(param.getKey()) + "&";
            }
            System.out.println(urlString);
            URL url = new URL(urlString);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;

            while ((line = reader.readLine()) != null) {
                result += line;
            }
            reader.close();

        } catch (MalformedURLException e) {
            // ...
        } catch (IOException e) {
            // ...
        }
        return result;

    }

    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @CacheControlHeader("no-cache")
    public String proxyPut(String body,
                           @HeaderParam("Authorization") String token,
                           @DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
                           @DefaultValue("application/json") @HeaderParam("Accept") String accept,
                           @Context final HttpServletRequest request

    ) {
        if (!validCredentials(token))
            return serialise(getInvalidCredentialsBean(), accept);
        String result = "";
        try {
            System.out.println(request.getPathInfo());
            String urlString = "http://inquiry.wespot.net/services/api/rest/json/?";
            for (Object paramObject : request.getParameterMap().entrySet()) {
                Map.Entry<String, String[]> param = (Map.Entry<String, String[]>) paramObject;
                urlString += param.getKey() + "=" + request.getParameter(param.getKey()) + "&";
            }
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            // important: get output stream before input stream
            OutputStream out = connection.getOutputStream();
            out.write(body.getBytes());
            out.close();

            // now you can get input stream and read.
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = null;

            while ((line = reader.readLine()) != null) {
                result += line;
            }

        } catch (MalformedURLException e) {
            // ...
        } catch (IOException e) {
            // ...
        }
        return result;

    }

    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @CacheControlHeader("no-cache")
    @Path("/dev")
    public String proxyPutDev(String body,
                           @HeaderParam("Authorization") String token,
                           @DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
                           @DefaultValue("application/json") @HeaderParam("Accept") String accept,
                           @Context final HttpServletRequest request

    ) {
        if (!validCredentials(token))
            return serialise(getInvalidCredentialsBean(), accept);
        String result = "";
        try {
            System.out.println(request.getPathInfo());
            String urlString = "http://dev.inquiry.wespot.net/services/api/rest/json/?";
            for (Object paramObject : request.getParameterMap().entrySet()) {
                Map.Entry<String, String[]> param = (Map.Entry<String, String[]>) paramObject;
                urlString += param.getKey() + "=" + request.getParameter(param.getKey()) + "&";
            }
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            // important: get output stream before input stream
            OutputStream out = connection.getOutputStream();
            out.write(body.getBytes());
            out.close();

            // now you can get input stream and read.
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = null;

            while ((line = reader.readLine()) != null) {
                result += line;
            }

        } catch (MalformedURLException e) {
            // ...
        } catch (IOException e) {
            // ...
        }
        return result;

    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @CacheControlHeader("no-cache")
    @Path("/dev")
    public String proxyGetDev(
            @HeaderParam("Authorization") String token,
            @DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
            @DefaultValue("application/json") @HeaderParam("Accept") String accept,
            @Context final HttpServletRequest request

    ) {
        if (!validCredentials(token))
            return serialise(getInvalidCredentialsBean(), accept);
        String result = "";
        try {
            System.out.println(request.getPathInfo());
            String urlString = "http://dev.inquiry.wespot.net/services/api/rest/json/?";
            for (Object paramObject : request.getParameterMap().entrySet()) {
                Map.Entry<String, String[]> param = (Map.Entry<String, String[]>) paramObject;
                urlString += param.getKey() + "=" + request.getParameter(param.getKey()) + "&";
            }
            System.out.println(urlString);
            URL url = new URL(urlString);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;

            while ((line = reader.readLine()) != null) {
                result += line;
            }
            reader.close();

        } catch (MalformedURLException e) {
            // ...
        } catch (IOException e) {
            // ...
        }
        return result;

    }

}
