package org.celstec.arlearn2.api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.celstec.arlearn2.beans.game.VariableDefinition;
import org.celstec.arlearn2.beans.game.VariableEffectDefinition;
import org.celstec.arlearn2.beans.run.VariableInstance;
import org.celstec.arlearn2.delegators.VariableDelegator;


@Path("/variables")
public class Variables extends Service {

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/definition")
	public String sendMessage(@HeaderParam("Authorization") String token, 
			String messageString, 
			@DefaultValue("application/json") @HeaderParam("Content-Type") String contentType, 
			@HeaderParam("Accept") String accept)  {
		if (!validCredentials(token))
			return serialise(getInvalidCredentialsBean(), accept);
		
		Object inRun = deserialise(messageString, VariableDefinition.class, contentType);
		if (inRun instanceof java.lang.String)
			return serialise(getBeanDoesNotParseException((String) inRun), accept);
		VariableDefinition variableDef = (VariableDefinition) inRun;
		
		VariableDelegator rd = new VariableDelegator(this);
		return serialise(rd.createVariableDefinition(variableDef), accept);
	}

    @POST
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/effectDefinition")
    public String createEffectDefinition(@HeaderParam("Authorization") String token,
                              String messageString,
                              @DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
                              @HeaderParam("Accept") String accept)  {
        if (!validCredentials(token))
            return serialise(getInvalidCredentialsBean(), accept);

        Object inRun = deserialise(messageString, VariableEffectDefinition.class, contentType);
        if (inRun instanceof java.lang.String)
            return serialise(getBeanDoesNotParseException((String) inRun), accept);
        VariableEffectDefinition variableDef = (VariableEffectDefinition) inRun;

        VariableDelegator rd = new VariableDelegator(this);
        return serialise(rd.createVariableEffectDefinition(variableDef), accept);
    }

    @GET
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/effectDefinition/id/{id}")
    public String getEffectDefinition(@HeaderParam("Authorization") String token,
                              @PathParam("id") Long id,
                              @DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
                              @HeaderParam("Accept") String accept)  {
        if (!validCredentials(token))
            return serialise(getInvalidCredentialsBean(), accept);
        VariableDelegator rd = new VariableDelegator(this);
        return serialise(rd.getVariableEffectDefinition(id), accept);
    }

    @POST
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/instance")
    public String createVariableInstance(@HeaderParam("Authorization") String token,
                                         String messageString,
                                         @DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
                                         @HeaderParam("Accept") String accept)  {
        if (!validCredentials(token))
            return serialise(getInvalidCredentialsBean(), accept);

        Object varInst = deserialise(messageString, VariableInstance.class, contentType);
        if (varInst instanceof java.lang.String)
            return serialise(getBeanDoesNotParseException((String) varInst), accept);
        VariableInstance variableInstance = (VariableInstance) varInst;

        VariableDelegator rd = new VariableDelegator(this);
        return serialise(rd.createVariableInstance(variableInstance), accept);
    }

    @GET
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/instance/gameId/{gameId}/runId/{runId}/name/{name}")
    public String getVariable(@HeaderParam("Authorization") String token,
                              @PathParam("gameId") Long gameId,
                              @PathParam("runId") Long runId,
                              @PathParam("name") String name,
                              @DefaultValue("application/json") @HeaderParam("Content-Type") String contentType,
                              @HeaderParam("Accept") String accept)  {
        if (!validCredentials(token))
            return serialise(getInvalidCredentialsBean(), accept);
        VariableDelegator rd = new VariableDelegator(this);
        return serialise(rd.getVariableInstance(gameId, runId, name), accept);
    }
}
