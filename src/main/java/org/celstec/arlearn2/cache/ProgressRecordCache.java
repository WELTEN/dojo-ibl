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

import org.celstec.arlearn2.beans.run.ProgressRecord;

import com.google.appengine.api.utils.SystemProperty;

public class ProgressRecordCache extends RunCache{

	private static ProgressRecordCache instance;

	private static String PROGRESS_RECORD_PREFIX = SystemProperty.applicationVersion.get()+"ProgressRecord";

	private ProgressRecordCache() {
	}

	public static ProgressRecordCache getInstance() {
		if (instance == null)
			instance = new ProgressRecordCache();
		return instance;

	}

	@Override
	protected String getType() {
		return "ProgressRecordCache";
	}
	
	public List<ProgressRecord> getProgressRecordList(Long runId, String scope, Object... args) {
		String cacheKey = generateCacheKey(PROGRESS_RECORD_PREFIX, runId, scope, args); 
		if (!cacheKeyExistsForScope(runId, scope, cacheKey)) return null;
		return (List<ProgressRecord>) getCache().get(cacheKey);
	}
	
	public void putProgressRecordList(List<ProgressRecord> pr, Long runId, String scope, Object... args) {
		String cacheKey = generateCacheKey(PROGRESS_RECORD_PREFIX, runId, scope, args); 
		storeCacheKeyForScope(runId, scope, cacheKey);
		getCache().put(cacheKey, pr);
	}
	
	public void removeProgress(Long runId, String scope) {
		removeKeysForRun(runId, scope);
	}

	
}
