package org.celstec.arlearn2.gwtcommonlib.client.objects;

import com.google.gwt.json.client.JSONObject;

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
public class FileReference extends Bean {

    public  final static String TYPE = "org.celstec.arlearn2.beans.generalItem.FileReference";
    public  final static String KEY = "key";
    public  final static String MD5_HASH = "md5Hash";
    public  final static String FILE_REFERENCE = "fileReference";

    public FileReference() {
        super();
    }

    public FileReference(JSONObject object) {
        super(object);
    }

    public String getType() {
        return TYPE;
    }

}