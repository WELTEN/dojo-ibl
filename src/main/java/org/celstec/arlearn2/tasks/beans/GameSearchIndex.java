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
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.search.*;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.delegators.GameDelegator;
import org.celstec.arlearn2.delegators.GeneralItemDelegator;
import org.celstec.arlearn2.delegators.NotificationDelegator;

public class GameSearchIndex extends GenericBean {

	private String gameTitle;
	private String gameAuthor;
	private Integer sharingType;
	private Long gameId;
    private Double lng;
    private Double lat;

	public GameSearchIndex() {
		super();
	}

	public GameSearchIndex(String gameTitle, String gameAuthor, Integer sharingType, Long gameId, Double lat, Double lng) {
		super();
		this.gameTitle = gameTitle;
		this.gameAuthor = gameAuthor;
		this.sharingType = sharingType;
		this.gameId = gameId;
        this.lat = lat;
        this.lng = lng;
	}

	public String getGameTitle() {
		return gameTitle;
	}

	public void setGameTitle(String gameTitle) {
		this.gameTitle = gameTitle;
	}

	public String getGameAuthor() {
		return gameAuthor;
	}

	public void setGameAuthor(String gameAuthor) {
		this.gameAuthor = gameAuthor;
	}

	public Integer getSharingType() {
		return sharingType;
	}

	public void setSharingType(Integer sharingType) {
		this.sharingType = sharingType;
	}

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }


	private static final Logger log = Logger.getLogger(NotificationDelegator.class.getName());
	@Override
	public void run() {
		log.log(Level.WARNING, "GameSearchIndex run ");

		try {

			if (sharingType != null && sharingType.equals(Game.PUBLIC)) {
				log.log(Level.WARNING, "adding to index  ");
				addToIndex();
			} else {
				removeFromIndex();
			}
			addGisToIndex();
		} catch (PutException e) {
			if (StatusCode.TRANSIENT_ERROR.equals(e.getOperationResult().getCode())) {
				// retry putting the document
				throw e;
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, "error", e);
			e.printStackTrace();
		}
	}

	private void addGisToIndex() {
		GameDelegator gd = new GameDelegator();
		GeneralItemDelegator gid = new GeneralItemDelegator(gd);
		for (GeneralItem gi :gid.getGeneralItems(getGameId()).getGeneralItems()){
			GeneralItemSearchIndex.scheduleGiTask(gi);
		}
	}

	private void removeFromIndex() {
		ArrayList<String> docIds = new ArrayList<String>();
		docIds.add("game:"+gameId);
		getIndex().delete(docIds);
	}

	private void addToIndex() throws PutException {
		Document.Builder builder = Document.newBuilder()
                .setId("game:" + gameId)
                .addField(Field.newBuilder().setName("gameId").setText(""+getGameId()))
                .addField(Field.newBuilder().setName("title").setText(getGameTitle()));

        if (getLat() != null && getLng() != null) {
            builder.addField(Field.newBuilder().setName("location").setGeoPoint(new GeoPoint(getLat(), getLng())));
        }
        Document doc = builder.build();
		log.log(Level.WARNING, "doc is "+doc);
		getIndex().put(doc);
		log.log(Level.WARNING, "index is  "+getIndex());
	}

	public Index getIndex() {
		IndexSpec indexSpec = IndexSpec.newBuilder().setName("game_index").build();
		return SearchServiceFactory.getSearchService().getIndex(indexSpec);
	}

}
