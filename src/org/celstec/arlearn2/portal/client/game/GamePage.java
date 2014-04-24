package org.celstec.arlearn2.portal.client.game;

import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.game.GameClient;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Game;
import org.celstec.arlearn2.portal.client.account.AccountManager;
import org.celstec.arlearn2.portal.client.account.AccountManager.NotifyAccountLoaded;

import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.util.SC;
import org.celstec.arlearn2.portal.client.toolbar.ToolBar;

public class GamePage {
	
	ToolBar toolbar;// = new Toolbar();
	
	public void loadPage() {
		AccountManager accountManager = AccountManager.getInstance();
		accountManager.setAccountNotification(new NotifyAccountLoaded() {
			
			@Override
			public void accountLoaded(boolean success) {
				if (success) {
					buildUi();
				} else {
					SC.say("Credentials are invalid. Log in again.");
				}
			}
		});
	}
	
	private void buildUi() {
		toolbar = new ToolBar(false);
		long gameId = Long.parseLong(com.google.gwt.user.client.Window.Location.getParameter("gameId"));
		GameClient.getInstance().getGame(gameId, new JsonCallback(){
			@Override
			public void onJsonReceived(JSONValue jsonValue) {
				Game g = new Game(jsonValue.isObject());
				GameDisplay gd = new GameDisplay(g);
				RootPanel.get("game").add(gd);
			}
		});
		
	}

}
