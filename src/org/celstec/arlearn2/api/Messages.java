package org.celstec.arlearn2.api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.celstec.arlearn2.beans.run.*;
import org.celstec.arlearn2.beans.run.Thread;
import org.celstec.arlearn2.delegators.MessageDelegator;

import org.celstec.arlearn2.delegators.ThreadDelegator;

@Path("/messages")
public class Messages extends Service {

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/userId/{userId}")
	public String sendMessage(@HeaderParam("Authorization") String token, 
			String messageString, 
			@PathParam("userId") String userId,
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType, 
			@HeaderParam("Accept") String accept)  {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		
		Object inRun = deserialise(messageString, Message.class, contentType);
		if (inRun instanceof java.lang.String)
			return serialise(getBeanDoesNotParseException((String) inRun), accept);
		Message message = (Message) inRun;
		
		MessageDelegator rd = new MessageDelegator(this);
		return serialise(rd.sendMessage(message, userId), accept);
	}

    @POST
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/thread")
    public String createThread(
            @HeaderParam("Authorization") String token,
            String thread,
            @DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
            @DefaultValue("application/json") @HeaderParam("Accept") String accept)
             {
        if (!validCredentials(token))
            return serialise(getInvalidCredentialsBean(), accept);

        Object inThread = deserialise(thread, org.celstec.arlearn2.beans.run.Thread.class, contentType);
        if (inThread instanceof java.lang.String)
            return serialise(getBeanDoesNotParseException((String) inThread), accept);

        ThreadDelegator td = new ThreadDelegator(this);
        return serialise(td.createThread((Thread)inThread), accept);

    }

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/thread/runId/{runId}")
    public String getThreads(@HeaderParam("Authorization") String token, @PathParam("runId") Long runId,
                             @DefaultValue("application/json") @HeaderParam("Accept") String accept)
             {
        if (!validCredentials(token))
            return serialise(getInvalidCredentialsBean(), accept);
        ThreadDelegator td = new ThreadDelegator(this);


        return serialise(td.getThreads(runId), accept);
    }

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/thread/runId/{runId}/default")
    public String getDefaultThread(@HeaderParam("Authorization") String token, @PathParam("runId") Long runId,
                             @DefaultValue("application/json") @HeaderParam("Accept") String accept)
             {
        if (!validCredentials(token))
            return serialise(getInvalidCredentialsBean(), accept);
        ThreadDelegator td = new ThreadDelegator(this);


        return serialise(td.getDefaultThread(runId), accept);
    }


    @POST
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/message")
    public String createMessage(
            @HeaderParam("Authorization") String token,
            String message,
            @DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
            @DefaultValue("application/json") @HeaderParam("Accept") String accept)
             {
        if (!validCredentials(token))
            return serialise(getInvalidCredentialsBean(), accept);

        Object inMessage = deserialise(message, org.celstec.arlearn2.beans.run.Message.class, contentType);
        if (inMessage instanceof java.lang.String)
            return serialise(getBeanDoesNotParseException((String) inMessage), accept);

        MessageDelegator messageDelegator = new MessageDelegator(this);

        ((Message) inMessage).setSenderProviderId(getAccount().getAccountType());
        ((Message) inMessage).setSenderId(getAccount().getLocalId());
        return serialise(messageDelegator.createMessage((Message) inMessage), accept);

    }

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/threadId/{threadId}")
    public String getMessagesForThread(@HeaderParam("Authorization") String token,
                                       @PathParam("threadId") Long threadId,
                                       @QueryParam("from") Long from,
                                       @QueryParam("until") Long until,
                                       @QueryParam("resumptionToken") String cursor,
                                       @DefaultValue("application/json") @HeaderParam("Accept") String accept)
             {
        if (!validCredentials(token))
            return serialise(getInvalidCredentialsBean(), accept);
        MessageDelegator md = new MessageDelegator(this);
        if (from == null && until == null) {
            return serialise(md.getMessagesForThread(threadId), accept);
        }
        return serialise(md.getMessagesForThread(threadId, from, until, cursor), accept);

    }

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/runId/{runId}/default")
    public String getMessagesForDefaultThread(@HeaderParam("Authorization") String token,
                                       @PathParam("runId") Long runId,
                                       @QueryParam("from") Long from,
                                       @QueryParam("until") Long until,
                                       @QueryParam("resumptionToken") String cursor,
                                       @DefaultValue("application/json") @HeaderParam("Accept") String accept)
             {
        if (!validCredentials(token))
            return serialise(getInvalidCredentialsBean(), accept);
        MessageDelegator md = new MessageDelegator(this);
        if (from == null && until == null) {
            return serialise(md.getMessagesForDefaultThread(runId), accept);
        }
        return serialise(md.getMessagesForDefaultThread(runId, from, until, cursor), accept);
    }

}
