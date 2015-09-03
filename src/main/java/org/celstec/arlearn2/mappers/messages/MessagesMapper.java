package org.celstec.arlearn2.mappers.messages;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.mapreduce.Mapper;
import org.celstec.arlearn2.jdo.manager.MessageManager;

/**
 * Created by str on 25/02/15.
 */
public class MessagesMapper extends Mapper<Entity, String, String> {

    @Override
    public void map(Entity entity) {
        Long identifier = (Long) entity.getProperty("messageId");
        MessageManager.updateDate(identifier);
    }
}
