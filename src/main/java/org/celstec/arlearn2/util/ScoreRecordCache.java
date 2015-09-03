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

import org.celstec.arlearn2.beans.run.ScoreRecord;

import net.sf.jsr107cache.Cache;

public class ScoreRecordCache {

	private static ScoreRecordCache instance;
	private Cache cache;

	private static final Logger logger = Logger.getLogger(ScoreRecordCache.class
			.getName());

	private ScoreRecordCache() {
		cache = FusionCache.getInstance().getCache();
	}

	public static ScoreRecordCache getInstance() {
		if (instance == null)
			instance = new ScoreRecordCache();
		return instance;
	}
	
	private static String SCORERECORD_PREFIX = "ScoreRecord";


	public int getScoreRecordTableIdForRun(String authToken, Long runId) {
		Integer returnInt = (Integer) cache.get(SCORERECORD_PREFIX+authToken+runId);
		if (returnInt == null) returnInt = -1;
		return returnInt;
	}
	
	public void putScoreDefinitionTableIdForRun(String authToken, Long runId, Integer tableId) {
		cache.put(SCORERECORD_PREFIX+authToken+runId, tableId);
	}
	
	
	public ScoreRecord getScoreRecord(Long runId, String actionId, String scope, String email, String teamId) {
		if ("all".equals(scope)) {
			return (ScoreRecord)cache.get(SCORERECORD_PREFIX+actionId+scope);
		} else if ("team".equals(scope)) {
			return (ScoreRecord)cache.get(SCORERECORD_PREFIX+actionId+scope+teamId);
		} else if ("user".equals(scope)) {
			return (ScoreRecord)cache.get(SCORERECORD_PREFIX+actionId+scope+email);
		}
		return null;
	}

	public void putScoreRecord(Long runId, ScoreRecord pr) {
		if (pr != null) {
			if ("all".equals(pr.getScope())) {
				cache.put(SCORERECORD_PREFIX+pr.getAction()+pr.getScope(), pr);
			} else if ("team".equals(pr.getScope())) {
				cache.put(SCORERECORD_PREFIX+pr.getAction()+pr.getScope()+pr.getTeamId(), pr);
			} else if ("user".equals(pr.getScope())) {
				cache.put(SCORERECORD_PREFIX+pr.getAction()+pr.getScope()+pr.getEmail(), pr);
			}
		}
	}
	
	public void removeScoreRecord(Long runId, String actionId, String scope, String email, String teamId) {
		if ("all".equals(scope)) {
			cache.remove(SCORERECORD_PREFIX+actionId+scope);
		} else if ("team".equals(scope)) {
			cache.remove(SCORERECORD_PREFIX+actionId+scope+teamId);
		} else if ("user".equals(scope)) {
			cache.remove(SCORERECORD_PREFIX+actionId+scope+email);
		}
	}
	
	public Long getScore(Long runId, String scope, String email, String teamId) {
		if ("all".equals(scope)) {
			return (Long)cache.get(SCORERECORD_PREFIX+scope+teamId);
		} else if ("team".equals(scope)) {
			return (Long)cache.get(SCORERECORD_PREFIX+scope+teamId);
		} else if ("user".equals(scope)) {
			return (Long)cache.get(SCORERECORD_PREFIX+scope+email);
		}
		return null;
	}

	public void putScore(Long runId, String scope, String email, String teamId, Long score) {
		if ("all".equals(scope)) {
			cache.put(SCORERECORD_PREFIX+scope+teamId, score);
		} else if ("team".equals(scope)) {
			cache.put(SCORERECORD_PREFIX+scope+teamId, score);
		} else if ("user".equals(scope)) {
			cache.put(SCORERECORD_PREFIX+scope+email, score);
		}
	}

	public void removeScore(Long runId, String scope, String email, String teamId) {
		if ("all".equals(scope)) {
			cache.remove(SCORERECORD_PREFIX+scope+teamId);
		} else if ("team".equals(scope)) {
			cache.remove(SCORERECORD_PREFIX+scope+teamId);
		} else if ("user".equals(scope)) {
			cache.remove(SCORERECORD_PREFIX+scope+email);
		}
	}



}
