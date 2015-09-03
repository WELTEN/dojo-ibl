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
package org.celstec.arlearn2.upload;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.celstec.arlearn2.beans.GamePackage;
import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.dependencies.ActionDependency;
import org.celstec.arlearn2.beans.dependencies.BooleanDependency;
import org.celstec.arlearn2.beans.dependencies.Dependency;
import org.celstec.arlearn2.beans.dependencies.TimeDependency;
import org.celstec.arlearn2.beans.game.Config;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.notification.GameModification;
import org.celstec.arlearn2.beans.notification.authoring.GameCreationStatus;
import org.celstec.arlearn2.delegators.GameDelegator;
import org.celstec.arlearn2.delegators.GeneralItemDelegator;
import org.celstec.arlearn2.delegators.NotificationDelegator;
import org.celstec.arlearn2.delegators.UsersDelegator;


public class GameUnpacker {

	private GamePackage gamePackage;
	private String auth;
	private Game createdGame;
	private List<GeneralItem> generalItems;
	private HashMap<Long, Long> identifierMapping = new HashMap<Long, Long>();
	private List<GeneralItem> manualItems; 
	private Account accountBean;

	public GameUnpacker(GamePackage arlPackage, String auth) {
		this.gamePackage = arlPackage;
		this.auth = auth;
	}

	public void unpack() {
			GameCreationStatus status = new GameCreationStatus();
			UsersDelegator qu = new UsersDelegator(auth);
			String myAccount = qu.getCurrentUserAccount();
			accountBean = new Account();
			accountBean.setFullid(myAccount);
			createGame(true, false);
			status.setGameId(createdGame.getGameId());
			status.setStatus(GameCreationStatus.GAME_CREATED);
			NotificationDelegator not = new NotificationDelegator();
			
			not.broadcast(status, myAccount);	

//			ChannelNotificator.getInstance().notify(myAccount, status);
			if (createdGame == null)
				return;
			updateGameId();
			insertGeneralItems(true);
			updateIdentifiers();
			status.setStatus(GameCreationStatus.IDENTIFIERS_UPDATED);
//			ChannelNotificator.getInstance().notify(myAccount, status);
			not.broadcast(status, myAccount);	
			insertGeneralItems(false);
			updateManualItems();
			status.setStatus(GameCreationStatus.PROCESSED_MANUAL_ITEMS);
//			ChannelNotificator.getInstance().notify(myAccount, status);
			not.broadcast(status, myAccount);	
			createGame(false, true);
			status.setStatus(100);
//			ChannelNotificator.getInstance().notify(myAccount, status);
			not.broadcast(status, myAccount);	

	}

	private void updateManualItems() {
		Game game = gamePackage.getGame();
		Config config = game.getConfig();
		if (manualItems == null) return;
		for (GeneralItem generalItem : manualItems) {
			generalItem.setId(identifierMapping.get(generalItem.getId()));
		}
		game.getConfig().setManualItems(manualItems);
	}

	private void createGame(boolean resetId, boolean notify) {
		Game game = gamePackage.getGame();
		if (game != null) {
			GameDelegator gd = new GameDelegator(accountBean, auth);
			if (resetId) {
				game.setGameId(null);
				if (game.getConfig() != null && game.getConfig().getManualItems() != null) {
					manualItems = game.getConfig().getManualItems();
					game.getConfig().setManualItems(null);
				}
			}
			createdGame = gd.createGame(game, GameModification.CREATED, notify);
		}
	}

	private void updateGameId() {
		generalItems = gamePackage.getGeneralItems();
		for (GeneralItem gi : generalItems) {
			gi.setGameId(createdGame.getGameId());
		}
	}

	private void insertGeneralItems(boolean resetId){
		GeneralItemDelegator gid = new GeneralItemDelegator(accountBean, auth);

//		CreateGeneralItems cr = new CreateGeneralItems(auth);
		for (GeneralItem generalItem : generalItems) {
			generalItem.getScope();
			Long oldId = generalItem.getId();
			if (resetId)
				generalItem.setId(null);
			Long newId = gid.createGeneralItem(generalItem).getId();
			if (resetId)
				identifierMapping.put(oldId, newId);

		}
	}

	private void updateIdentifiers() {
		for (GeneralItem generalItem : generalItems) {
			updateIdentifiers(generalItem.getDependsOn());
			updateIdentifiers(generalItem.getDisappearOn());
		}
	}

	private void updateIdentifiers(Dependency dep) {
		if (dep instanceof ActionDependency) {
			ActionDependency ad = (ActionDependency) dep;
			if (identifierMapping.containsKey(ad.getGeneralItemId()))
				ad.setGeneralItemId(identifierMapping.get(ad.getGeneralItemId()));
		}
		if (dep instanceof TimeDependency) {
			TimeDependency td = (TimeDependency) dep;
			if (td.getOffset() != null)
				updateIdentifiers(td.getOffset());
		}

		if (dep instanceof BooleanDependency) {
			BooleanDependency ad = (BooleanDependency) dep;
			for (Dependency d : ad.getDependencies()) {
				updateIdentifiers(d);
			}
		}
	}

}
