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
package org.celstec.arlearn2.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class VisibleItemsList {

	private List<VisibleItem> visibleItems = new ArrayList<VisibleItem>();
	private HashMap<String, VisibleItem> map = new HashMap<String, VisibleItem>();
	
	
//	public void log(Logger log) {
//		
//for (Iterator iterator = visibleItems.iterator(); iterator.hasNext();) {
//	VisibleItem type = (VisibleItem) iterator.next();
//	log.severe("contains "+type.getGeneralItemId());
//}
//		
//	}
	
	public List<VisibleItem> getVisibleItems() {
		return visibleItems;
	}
	public void setVisibleItems(List<VisibleItem> visibleItems) {
		this.visibleItems = visibleItems;
		map = new HashMap<String, VisibleItem>();
		for(VisibleItem vi: visibleItems) {
			map.put(vi.getGeneralItemId(), vi);
		}
	}
	
	public VisibleItem getVisibleItem(String generalItemId){
		return map.get(generalItemId);
	}
	
	public void merge(VisibleItemsList vil) {
		visibleItems.addAll(vil.visibleItems);
		for(VisibleItem vi: vil.visibleItems) {
			map.put(vi.getGeneralItemId(), vi);
		}
	}
	
	
	
}
