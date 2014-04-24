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
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "actions")
@XmlType(name="Actions", namespace="http://celstec.org/arlearn2")
public class ActionList {
	

	//TODO: migrate this code
	
//	public List<Action> getMatchingActions(DependsOn dOn){
//		ArrayList<Action> returnList = new ArrayList<Action>();
//		for (Action a: actions) {
//			if (dependsOnConditionMatches(a, dOn)) returnList.add(a);
//		}
//		return returnList;
//	}
//	
//	private boolean dependsOnConditionMatches(Action action, DependsOn dOn) {
//		if (dOn.getAction() != null && !dOn.getAction().equals(action.getAction())) return false; 
//		if (dOn.getGeneralItemId() != null && !dOn.getGeneralItemId().equals(action.getGeneralItemId())) return false; 
//		if (dOn.getGeneralItemType() != null && !dOn.getGeneralItemType().equals(action.getGeneralItemType())) return false; 
//		return true;
//	}

}
