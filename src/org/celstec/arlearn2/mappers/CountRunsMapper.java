package org.celstec.arlearn2.mappers;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Text;

import com.google.appengine.tools.mapreduce.Mapper;

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
public class CountRunsMapper extends Mapper<Entity, String, Long> {
    private static final Logger log = Logger.getLogger(CountRunsMapper.class
            .getName());

    @Override
    public void map(Entity entity) {
        log.log(Level.SEVERE, ""+entity.toString());

        Object property = entity.getProperty("gameId");
        Long date = (Long) entity.getProperty("lastModificationDateGame");
        if (date > (System.currentTimeMillis() - (10*24*60*60*1000))) {
            log.log(Level.SEVERE, ""+property);
            emit(""+property, 1l);
        }

    }
}
