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
package org.celstec.arlearn2.gwtcommonlib.client.network;

import org.celstec.arlearn2.gwtcommonlib.client.auth.Authentication;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class JsonCallback {

	protected String type;
	protected JSONArray jsonValue;
	protected ReadyCallback rc;
	
	public JsonCallback() {
		
	}
	
	public void setReadyCallback(ReadyCallback rc) {
		this.rc = rc;
	}
	
	public JsonCallback(String type) {
		this.type = type;
	}
	
	public void onJsonReceivedNoProcessing(JSONValue jsonValue) {
		if (type != null && jsonValue.isObject().get(type) != null && jsonValue.isObject().get(type).isArray() != null) {
			this.jsonValue = jsonValue.isObject().get(type).isArray();
		}
		if (jsonValue.isObject() != null && jsonValue.isObject().get("error") != null){
			Authentication.getInstance().disAuthenticate();
		}
	}
	
	public void onJsonReceived(JSONValue jsonValue) {
			if (type != null && jsonValue.isObject().get(type) != null && jsonValue.isObject().get(type).isArray() != null) {
				this.jsonValue = jsonValue.isObject().get(type).isArray();
			}
			if (jsonValue.isObject() != null && jsonValue.isObject().get("error") != null){
				Authentication.getInstance().disAuthenticate();
			}
			onReceived();
	}
	
	public ListGridRecord processArrayEntry(int i) {
		return null;
	}
	
	public void onError(){
	
	}
	//todo make abstract
	public void onReceived(){};
	
	public int size(){
		if (jsonValue == null) return 0;
		return jsonValue.size();
	}
	
	public String getAttributeString(int i, String attributeName) {
			if (jsonValue == null) return null;
//			Window.alert("jsonValue string"+jsonValue);
//			Window.alert("jsonValue string"+i+ " "+jsonValue.get(i));
			JSONObject game = jsonValue.get(i).isObject();
			if (game == null) return null;
			if (!game.containsKey(attributeName)) return null;
			if (game.get(attributeName).isArray()!=null) {
				JSONArray ar = game.get(attributeName).isArray();
				String returnString = "";
				if (ar.size()>=1) returnString = ar.get(0).isString().stringValue();
				for (int j = 1; j< ar.size(); j++) {
					returnString += ", "+ar.get(j).isString().stringValue();
				}
				return returnString;
			}
			return game.get(attributeName).isString().stringValue();
	}
	
	public Boolean getAttributeBoolean(int i, String attributeName) {
		if (jsonValue == null) return null;
//		Window.alert("jsonValue "+jsonValue);
//		Window.alert("jsonValue "+i+ " "+jsonValue.get(i));
		JSONObject game = jsonValue.get(i).isObject();
		if (game == null) return null;
//		Window.alert("game "+game);

		if (!game.containsKey(attributeName)) return null;
//		Window.alert("game "+game.get(attributeName));
		return game.get(attributeName).isBoolean().booleanValue();
	}
	
	public int getAttributeInteger(int i, String attributeName) {
			if (jsonValue == null) return -1;
			JSONObject game = jsonValue.get(i).isObject();
			if (game == null) return -1;
//			System.out.println("attribute: "+attributeName +" game");
			if (!game.containsKey(attributeName) ) return 0;
			return (int) game.get(attributeName).isNumber().doubleValue();
	}
	
	public long getAttributeLong(int i, String attributeName) {
		if (jsonValue == null) return -1;
		JSONObject game = jsonValue.get(i).isObject();
		if (game == null) return -1;
//		System.out.println("attribute: "+attributeName +" game");
		if (!game.containsKey(attributeName) ) return 0;
		return (long) game.get(attributeName).isNumber().doubleValue();
}
	
	public Double getAttributeDouble(int i, String attributeName) {
		if (jsonValue == null) return null;
		JSONObject game = jsonValue.get(i).isObject();
		if (game == null) return null;
		if (!game.containsKey(attributeName) ) return null;
		return game.get(attributeName).isNumber().doubleValue();
}
	
	public JSONObject getGameConfig(int i) {
		if (jsonValue == null) return null;
		JSONObject game = jsonValue.get(i).isObject();
		if (game == null) return null;
		if (game.get("config") == null) return null;
		return game.get("config").isObject();
	}

//	protected ListGrid lg;
//	
//	public void setFetchListGrid(ListGrid lg) {
//		this.lg = lg;
//		
//	}
	
}
