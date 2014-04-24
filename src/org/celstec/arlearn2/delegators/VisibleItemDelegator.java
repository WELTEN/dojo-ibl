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
package org.celstec.arlearn2.delegators;

//import java.util.List;
//
//import org.celstec.arlearn2.beans.run.VisibleItem;
//import org.celstec.arlearn2.cache.VisibleItemsCache;
//import org.celstec.arlearn2.delegators.generalitems.VisibleItemsList;
//
//import org.celstec.arlearn2.jdo.manager.VisibleItemsManager;
//import org.celstec.arlearn2.util.FusionCache;
//
//import com.google.gdata.util.AuthenticationException;

public class VisibleItemDelegator {//extends GoogleDelegator {
	
//	public VisibleItemDelegator(String authtoken) throws AuthenticationException {
//		super(authtoken);
//	}
//
//	public VisibleItemDelegator(GoogleDelegator gd) {
//		super(gd);
//	}
	
//	public VisibleItemsList getVisibleItems(Long runId, String itemId, String userEmail, String teamId) {
//		List<VisibleItem> visibleItems = null;
//		if (itemId == null) visibleItems = VisibleItemsCache.getInstance().getVisibleItems(runId, userEmail, teamId);
//		if (visibleItems == null) {
//			visibleItems = VisibleItemsManager.getVisibleItems(runId, itemId, userEmail, teamId);
//			VisibleItemsCache.getInstance().putVisibleItems(visibleItems, runId, userEmail, teamId);
//		}
//		VisibleItemsList vil = new VisibleItemsList();
//		vil.setVisibleItems(visibleItems);
//		return vil;
//	}
//	
//	public void addVisibleItem(Long runId, Long generalItemId, String email, String teamId) {
//		VisibleItemsManager.addVisibleItem(runId, generalItemId, email, teamId);
//		List<VisibleItem> visibleItems = VisibleItemsCache.getInstance().getVisibleItems(runId, email, teamId);
//		if (visibleItems != null) {
//			VisibleItemsCache.getInstance().removeVisibleItems(runId, email, teamId);
//		}
//	}
//	
//	public void deleteVisibleItems(Long runId) {
//		VisibleItemsManager.deleteVisibleItems(runId);
//		//TODO clean cache
//		FusionCache.getInstance().clearAll();
//	}
}
