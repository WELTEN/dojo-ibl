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
package org.celstec.arlearn2.util;

import java.util.logging.Logger;

import net.sf.jsr107cache.Cache;

public class ResponseCache {

	private static ResponseCache instance;
	private Cache cache;

	private static final Logger logger = Logger.getLogger(ResponseCache.class
			.getName());

	private ResponseCache() {
		cache = FusionCache.getInstance().getCache();
	}

	public static ResponseCache getInstance() {
		if (instance == null)
			instance = new ResponseCache();
		return instance;
	}
	
	private static String RESPONSE_PREFIX = "responseprefix";

	public long getResponseTableIdForRun(Long runId) {
		Long responseLong = (Long) cache.get(RESPONSE_PREFIX+runId);
		if (responseLong == null) responseLong = -1l;
		return responseLong;
	}
	
	public void putResponseTableIdForRun(Long runId, Long tableId) {
		cache.put(RESPONSE_PREFIX+runId, tableId);
	}
}
