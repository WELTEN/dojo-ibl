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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.celstec.arlearn2.beans.game.ProgressDefinition;
import org.celstec.arlearn2.beans.run.ProgressRecord;

import net.sf.jsr107cache.Cache;

public class ProgressRecordCache extends RunCache{
	private static ProgressRecordCache instance;
	private Cache cache;

	private static final Logger logger = Logger.getLogger(ProgressRecordCache.class
			.getName());

	private ProgressRecordCache() {
		cache = FusionCache.getInstance().getCache();
	}

	public static ProgressRecordCache getInstance() {
		if (instance == null)
			instance = new ProgressRecordCache();
		return instance;
		
	}
	private static String PROGRESSRECORD_PREFIX = "ProgressRecord";

	public ProgressRecord getProgressRecord(Object... args) {
		return (ProgressRecord) cache.get(FusionCache.getCacheKey(PROGRESSRECORD_PREFIX, args));
	}
	
	public void putProgressRecord(ProgressRecord pr, Object... args) {
		cache.put(FusionCache.getCacheKey(PROGRESSRECORD_PREFIX, args), pr);
	}

	public void removeProgressRecord(ProgressRecord pr, Object... args) {
		cache.remove(FusionCache.getCacheKey(PROGRESSRECORD_PREFIX, args));
	}
	
	public List<ProgressRecord> getProgressRecordList(Long runId, Object... args) {
		String cacheKey = FusionCache.getCacheKey(PROGRESSRECORD_PREFIX, runId, args); 
		if (!cacheKeyExists(runId, cacheKey)) return null;
		return (List<ProgressRecord>) cache.get(FusionCache.getCacheKey(PROGRESSRECORD_PREFIX, args));
	}
	
	public void putProgressRecordList(List<ProgressRecord> pr, Long runId, Object... args) {
		String cachekey = FusionCache.getCacheKey(PROGRESSRECORD_PREFIX, runId, args); 
		storeCacheKey(runId, cachekey);
		cache.put(cachekey, pr);
	}
	
	@Deprecated
	public ProgressRecord getProgressRecord(Long runId, String actionId, String scope, String email, String teamId) {
		if ("all".equals(scope)) {
			return (ProgressRecord)cache.get(PROGRESSRECORD_PREFIX+actionId+scope);
		} else if ("team".equals(scope)) {
			return (ProgressRecord)cache.get(PROGRESSRECORD_PREFIX+actionId+scope+teamId);
		} else if ("user".equals(scope)) {
			return (ProgressRecord)cache.get(PROGRESSRECORD_PREFIX+actionId+scope+email);
		}
		return null;
	}
	
	@Deprecated
	public void putProgressRecord(Long runId, ProgressRecord pr) {
		if (pr != null) {
			if ("all".equals(pr.getScope())) {
				cache.put(PROGRESSRECORD_PREFIX+pr.getAction()+pr.getScope(), pr);
			} else if ("team".equals(pr.getScope())) {
				cache.put(PROGRESSRECORD_PREFIX+pr.getAction()+pr.getScope()+pr.getTeamId(), pr);
			} else if ("user".equals(pr.getScope())) {
				cache.put(PROGRESSRECORD_PREFIX+pr.getAction()+pr.getScope()+pr.getEmail(), pr);
			}
		}
	}
	@Deprecated
	public void removeProgressRecord(Long runId, String actionId, String scope, String email, String teamId) {
		if ("all".equals(scope)) {
			cache.remove(PROGRESSRECORD_PREFIX+actionId+actionId);
		} else if ("team".equals(scope)) {
			cache.remove(PROGRESSRECORD_PREFIX+actionId+actionId+teamId);
		} else if ("user".equals(scope)) {
			cache.remove(PROGRESSRECORD_PREFIX+actionId+actionId+email);
		}
	}
	
	
	public Long getProgress(Long runId, String scope, String email, String teamId) {
		if ("all".equals(scope)) {
			return (Long)cache.get(PROGRESSRECORD_PREFIX+scope+teamId);
		} else if ("team".equals(scope)) {
			return (Long)cache.get(PROGRESSRECORD_PREFIX+scope+teamId);
		} else if ("user".equals(scope)) {
			return (Long)cache.get(PROGRESSRECORD_PREFIX+scope+email);
		}
		return null;
	}

	public void putProgress(Long runId, String scope, String email, String teamId, Long progress) {
		if ("all".equals(scope)) {
			cache.put(PROGRESSRECORD_PREFIX+scope+teamId, progress);
		} else if ("team".equals(scope)) {
			cache.put(PROGRESSRECORD_PREFIX+scope+teamId, progress);
		} else if ("user".equals(scope)) {
			cache.put(PROGRESSRECORD_PREFIX+scope+email, progress);
		}
	}

	public void removeProgress(Long runId, String scope, String email, String teamId) {
		if ("all".equals(scope)) {
			cache.remove(PROGRESSRECORD_PREFIX+scope+teamId);
		} else if ("team".equals(scope)) {
			cache.remove(PROGRESSRECORD_PREFIX+scope+teamId);
		} else if ("user".equals(scope)) {
			cache.remove(PROGRESSRECORD_PREFIX+scope+email);
		}
	}

	@Override
	protected Cache getCache() {
		return cache;
	}

	@Override
	protected String getType() {
		return "PrRec";
	}

		

}
