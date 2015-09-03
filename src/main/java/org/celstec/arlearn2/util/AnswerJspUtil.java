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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.celstec.arlearn2.beans.run.Action;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.serializer.json.JsonBeanSerialiser;
import org.celstec.arlearn2.beans.run.Response;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.jdo.manager.ActionManager;
import org.celstec.arlearn2.jdo.manager.GeneralItemManager;
import org.celstec.arlearn2.jdo.manager.ResponseManager;
import org.celstec.arlearn2.jdo.manager.RunManager;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;



public class AnswerJspUtil {
	
	public static HashMap<Long, List<Response>> getReponses(Long runId, String account) {
		HashMap<Long, List<Response>> result = new HashMap<Long, List<Response>>();

		for (Iterator iterator = ResponseManager.getResponse(runId, null, account, null, false).iterator(); iterator.hasNext();) {
			Response r = (Response) iterator.next();
			if (r.getGeneralItemId() == null) r.setGeneralItemId(-1l);
			if (result.get(r.getGeneralItemId()) == null) {
				result.put(r.getGeneralItemId(), new ArrayList<Response>())	;		
			}
			result.get(r.getGeneralItemId()).add(r);
			
		}
		return result;
	}
	
	public static String getAudioUrl(Response r) {
		String value = r.getResponseValue();
		if (value == null) return null;
		try {
			JSONObject json = new JSONObject(value);
			if (json.has("audioUrl")) return json.getString("audioUrl").replace("10.0.2.2", "localhost");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Response getReponse(String account, Long runId, Long timestamp) {
		for (Iterator iterator = ResponseManager.getResponse(runId, null, account, null, false).iterator(); iterator.hasNext();) {
			Response r = (Response) iterator.next();
			if (r.getTimestamp().longValue() == timestamp.longValue()) return r;
		}
		return null;
	}

	public static String getAudioEmbeddedPlayer(Response r) {
		String url = getAudioUrl(r);
		if (url == null) return "geen audio";
		String result = "<embed src=\""+ url+"\" autostart=\"false\" loop=\"false\"> </embed>";
		return result;
	}
	
	public static String getMap(Response r) {
		try {
			JSONObject json = new JSONObject(r.getResponseValue());
			String latString = null;
			String lngString = null;
			if (json.has("lat")) latString =  json.getString("lat");
			if (json.has("lng")) lngString =  json.getString("lng");
			if (latString== null || lngString == null) return "";
			String mapUrl = "<img src=\"http://maps.googleapis.com/maps/api/staticmap?center="+latString+","+lngString+"&zoom=15&size=400x400&sensor=false&markers=color:blue%7Clabel:S%7C"+latString+","+lngString+"\">";
			return mapUrl;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	public static String getImageUrl(Response r) {
		String value = r.getResponseValue();
		if (value == null) return null;
		try {
			JSONObject json = new JSONObject(value);
			if (json.has("imageUrl")) return json.getString("imageUrl").replace("10.0.2.2", "localhost");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getImageUrlEmbedded(Response r) {
		String url = getImageUrl(r);
		if (url == null) return "geen figuur";
		String result = "<img src=\""+ url+"\" />";
		return result;
	}
	
	public static String[][] getRunTitles(Long runId, String account) {
		List<Run> runs = RunManager.getRuns(runId, null, null, null, null);
		if (runs.isEmpty()) return null;
		Long gameId = runs.get(0).getGameId();
		List<GeneralItem> l = GeneralItemManager.getGeneralitems(gameId, null, null);
		String[][] result = new String[l.size()][5];
		int i = 0;
		for (Iterator iterator = l.iterator(); iterator.hasNext();) {
			GeneralItem generalItem = (GeneralItem) iterator.next();
			result[i][0] = generalItem.getName();
			result[i][2] = ""+generalItem.getId();
			result[i][3] = ""+generalItem.getLat();
			result[i][4] = ""+generalItem.getLng();
			try {
				JsonBeanSerialiser jbs = new JsonBeanSerialiser(generalItem);
				result[i++][1] = (jbs.serialiseToJson()).getString("html");
			} catch (JSONException e) {
				result[i-1][1] = "";
			}
		}
	
		return result;
	}
	
	public static HashMap<Long, String> getActions(Long runId) {
		HashMap<Long, String> result = new HashMap<Long, String>();
		List<Action> actions = ActionManager.getActions(runId, null, null, null, null);
		for (Iterator iterator = actions.iterator(); iterator.hasNext();) {
			Action action = (Action) iterator.next();
			if (action.getGeneralItemId() != null) {
				Long key = action.getGeneralItemId();
				if (result.get(key)== null){
					result.put(key, action.getAction()+"<br>");
				} else {
					result.put(key, result.get(key)+action.getAction()+"<br>");
				}
			}
		}
		return result;
		
	}

}
