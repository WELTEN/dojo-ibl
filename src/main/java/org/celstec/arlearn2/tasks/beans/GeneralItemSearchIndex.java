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
package org.celstec.arlearn2.tasks.beans;

import java.util.ArrayList;

import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.delegators.GameDelegator;
import org.celstec.arlearn2.delegators.GeneralItemDelegator;

import com.google.appengine.api.search.Document;

import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.PutException;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.search.StatusCode;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class GeneralItemSearchIndex  extends GenericBean {
	
	private String generalItemTitle;
	private Long generalItemId;
	private Long gameId;
	
	public GeneralItemSearchIndex() {
		super();
	}
	
	public static void scheduleGiTask(GeneralItem gi) {
		new GeneralItemSearchIndex(gi.getName(), gi.getId(), gi.getGameId()).scheduleTask();
	}

	public GeneralItemSearchIndex(String generalItemTitle, Long generalItemId, Long gameId) {
		super();
		this.generalItemTitle = generalItemTitle;
		this.generalItemId = generalItemId;
		this.gameId = gameId;
	}



	public String getGeneralItemTitle() {
		return generalItemTitle;
	}



	public void setGeneralItemTitle(String generalItemTitle) {
		this.generalItemTitle = generalItemTitle;
	}

	public Long getGeneralItemId() {
		return generalItemId;
	}



	public void setGeneralItemId(Long generalItemId) {
		this.generalItemId = generalItemId;
	}



	public Long getGameId() {
		return gameId;
	}



	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}



	@Override
	public void run() {
		try {
			GameDelegator gd = new GameDelegator();
			Game g = gd.getGameWithoutAccount(getGameId());
			if (g.getSharing() != null && g.getSharing().equals(Game.PUBLIC)) {
				GeneralItemDelegator gid = new GeneralItemDelegator(gd);
				GeneralItem gi = gid.getGeneralItemForGame(getGameId(), getGeneralItemId());
				addToIndex(gi);
			} else {
				removeFromIndex();
			}
		} catch (PutException e) {
			if (StatusCode.TRANSIENT_ERROR.equals(e.getOperationResult().getCode())) {
				// retry putting the document
				throw e;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void removeFromIndex() {
		ArrayList<String> docIds = new ArrayList<String>();
		docIds.add("gi:"+getGeneralItemId());
		getIndex().delete(docIds);
	}
	
	public Index getIndex() {
		IndexSpec indexSpec = IndexSpec.newBuilder().setName("generalItem_index").build();
		return SearchServiceFactory.getSearchService().getIndex(indexSpec);
	}
	private void addToIndex(GeneralItem gi) throws PutException {
		System.out.println(gi);
//		JSONObject giObject = JSONParser.parseStrict(gi.toString()).isObject();
		String richText ="";
		try {
			JSONObject giObject = new JSONObject(gi.toString());
			if (giObject.has("richText")){
				richText = giObject.getString("richText");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Document doc = Document.newBuilder()
				.setId("gi:" + generalItemId)
				.addField(Field.newBuilder().setName("gameId").setNumber(getGameId()))
				.addField(Field.newBuilder().setName("generalItemId").setNumber(getGeneralItemId()))
				.addField(Field.newBuilder().setName("title").setText(getGeneralItemTitle()))
				.addField(Field.newBuilder().setName("richText").setText(richText))
				.build();
		getIndex().put(doc);
	}
}
