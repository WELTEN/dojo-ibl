package org.celstec.arlearn2.api;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.mapreduce.*;
import com.google.appengine.tools.mapreduce.inputs.DatastoreInput;
import com.google.appengine.tools.mapreduce.outputs.InMemoryOutput;
import org.celstec.arlearn2.beans.run.Message;
import org.celstec.arlearn2.beans.run.Thread;
import org.celstec.arlearn2.delegators.MessageDelegator;
import org.celstec.arlearn2.delegators.ThreadDelegator;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.Serializable;
import java.util.List;

@Path("/messages")
public class Messages extends Service implements Serializable{

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

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/messageId/{messageId}")
    public String getGame(@HeaderParam("Authorization") String token,
                          @PathParam("messageId") Long messageIdentifier,
                          @DefaultValue("application/json")
                          @HeaderParam("Accept") String accept)
    {
        MessageDelegator md = new MessageDelegator(this);
        Message g = md.getMessagesById(messageIdentifier);
        if (g.getError() != null) {
            return serialise(g, accept);
        }
        if (!validCredentials(token))
            return serialise(getInvalidCredentialsBean(), accept);

        return serialise(g, accept);
    }

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/runId/{runId}/recentFirst/{amount}")
    public String getRecentMessagesForDefaultThread(@HeaderParam("Authorization") String token,
                                              @PathParam("runId") Long runId,
                                              @PathParam("amount") Integer amount,
                                              @DefaultValue("application/json") @HeaderParam("Accept") String accept)
    {
        if (!validCredentials(token))
            return serialise(getInvalidCredentialsBean(), accept);
        MessageDelegator md = new MessageDelegator(this);
        return serialise(md.getRecentMessagesForDefaultThread(runId, amount), accept);
    }


    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Path("/updateLastModificationDates")
    public String startLomGeneration(
            @DefaultValue("application/json") @HeaderParam("Accept") String accept){
//        String id = MapReduceJob.start(
//                MapReduceSpecification.of(
//                        "Lom Map Reduce",
//                        new DatastoreInput("MessageJDO", 4),
//                        new MessagesMapper(),
//                        Marshallers.getStringMarshaller(),
//                        Marshallers.getStringMarshaller(),
//                        new LomReducer(),
//                        new LomOutput(4)),
//                getSettings());

        String id = MapReduceJob.start(getCreationJobSpec(50, 5, 5),new MapReduceSettings.Builder(getSettings()).setBucketName("streetlearn-mapreduce").build());
//        String id = MapJob.start(getCreationJobSpec(50, 5, 5), getSettings());

        return "{ 'jobId':"+id+"}";
    }

    private MapReduceSpecification<Entity, String, Long, KeyValue<String, Long>, List<List<KeyValue<String, Long>>>> getCreationJobSpec(int bytesPerEntity, int entities,
                                                                                                                                        int shardCount) {
        // [START mapSpec]
//        new DatastoreInput()
        DatastoreInput input = new DatastoreInput("MessageJDO", 4);
        MessageUpdater mapper = new MessageUpdater();

//        return new MapSpecification.Builder<Long, Void, Void>(input, mapper)
//                .setJobName("Update Message Entities")
//                .build();

       return new MapReduceSpecification.Builder<Entity, String, Long, KeyValue<String, Long>, List<List<KeyValue<String, Long>>>>(new DatastoreInput("MessageJDO", 4 ),
                new CountMapper(), new CountReducer(), new InMemoryOutput<KeyValue<String, Long>>())
                .setKeyMarshaller(Marshallers.getStringMarshaller())
                .setValueMarshaller(Marshallers.getLongMarshaller())
                .setJobName("MapReduceTest count")
                .setNumReducers(4)
                .build();
//        return spec;
    }

    class MessageUpdater extends MapOnlyMapper<Long, Void> {
        public MessageUpdater() {

        }

        @Override
        public void map(Long aLong) {
            System.out.println(aLong);
        }
    }

    class CountReducer extends Reducer<String, Long, KeyValue<String, Long>> {

        private static final long serialVersionUID = 1316637485625852869L;

        @Override
        public void reduce(String key, ReducerInput<Long> values) {
            long total = 0;
            while (values.hasNext()) {
                total += values.next();
            }
            emit(KeyValue.of(key, total));
        }
    }

    class CountMapper extends Mapper<Entity, String, Long> {

        private static final long serialVersionUID = 4973057382538885270L;

        private void incrementCounter(String name, long delta) {
            getContext().getCounter(name).increment(delta);
        }

        private void emitCharacterCounts(String s) {
//            HashMap<Character, Integer> counts = new HashMap<>();
//            for (int i = 0; i < s.length(); i++) {
//                char c = s.charAt(i);
//                Integer count = counts.get(c);
//                if (count == null) {
//                    counts.put(c, 1);
//                } else {
//                    counts.put(c, count + 1);
//                }
//            }
//            for (Entry<Character, Integer> kv : counts.entrySet()) {
//                emit(String.valueOf(kv.getKey()), Long.valueOf(kv.getValue()));
//            }
        }

        @Override
        public void map(Entity entity) {
            System.out.println(entity);
            Long lastModificationDate = (Long) entity.getProperty("lastModificationDate");
            if (lastModificationDate == null) {
                final DatastoreService dss = DatastoreServiceFactory.getDatastoreService();
                entity.setProperty("lastModificationDate", System.currentTimeMillis());
                dss.put(entity);
            }
//            incrementCounter("total entities", 1);
//            incrementCounter("map calls in shard " + getContext().getShardNumber(), 1);
//
//            String name = entity.getKey().getName();
//            if (name != null) {
//                incrementCounter("total entity key size", name.length());
//                emitCharacterCounts(name);
//            }
//
//            Text property = (Text) entity.getProperty("payload");
//            if (property != null) {
//                incrementCounter("total entity payload size", property.getValue().length());
//                emitCharacterCounts(property.getValue());
//            }
        }
    }
}
