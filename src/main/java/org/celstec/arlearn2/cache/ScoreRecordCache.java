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
package org.celstec.arlearn2.cache;

import java.util.List;

import org.celstec.arlearn2.beans.run.ScoreRecord;

import com.google.appengine.api.utils.SystemProperty;

public class ScoreRecordCache extends RunCache{

	private static ScoreRecordCache instance;

	private static String SCORE_RECORD_PREFIX = SystemProperty.applicationVersion.get()+"ScoreRecord";

	private ScoreRecordCache() {
	}

	public static ScoreRecordCache getInstance() {
		if (instance == null)
			instance = new ScoreRecordCache();
		return instance;

	}

	@Override
	protected String getType() {
		return "ScoreRecordCache";
	}
	
	public List<ScoreRecord> getScoreRecordList(Long runId, String scope, Object... args) {
		String cacheKey = generateCacheKey(SCORE_RECORD_PREFIX, runId, scope, args); 
		if (!cacheKeyExistsForScope(runId, scope, cacheKey)) return null;
		return (List<ScoreRecord>) getCache().get(cacheKey);
	}
	
	public void putScoreRecordList(List<ScoreRecord> pr, Long runId, String scope, Object... args) {
		String cacheKey = generateCacheKey(SCORE_RECORD_PREFIX, runId, scope, args); 
		storeCacheKeyForScope(runId, scope, cacheKey);
		getCache().put(cacheKey, pr);
	}
	
	public void removeScore(Long runId, String scope) {
		removeKeysForRun(runId, scope);
	}

	
}
