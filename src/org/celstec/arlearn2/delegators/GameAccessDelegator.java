package org.celstec.arlearn2.delegators;

import java.util.Iterator;
import java.util.StringTokenizer;

import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.account.AccountList;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.game.GameAccess;
import org.celstec.arlearn2.beans.game.GameAccessList;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.run.RunAccess;
import org.celstec.arlearn2.beans.run.RunList;
import org.celstec.arlearn2.jdo.UserLoggedInManager;
import org.celstec.arlearn2.jdo.classes.GameAccessJDO;
import org.celstec.arlearn2.jdo.manager.GameAccessManager;
import org.celstec.arlearn2.jdo.manager.RunAccessManager;


public class GameAccessDelegator extends GoogleDelegator {

	public GameAccessDelegator(String authtoken)  {
		super(authtoken);
	}

	public GameAccessDelegator(GoogleDelegator gd) {
		super(gd);
	}

	public void provideAccess(Long gameId, Account account, int accessRights) {
//		StringTokenizer st = new StringTokenizer(account, ":");
//		int accountType = 0;
//		String localID = null;
//		if (st.hasMoreTokens()) {
//			accountType = Integer.parseInt(st.nextToken());
//		}
//		if (st.hasMoreTokens()) {
//			localID = st.nextToken();
//		}
		GameAccessManager.addGameAccess(account.getLocalId(), account.getAccountType(), gameId, accessRights);
	}
	
	public void provideAccess(Long gameId, String accountString, int accessRights) {
		StringTokenizer st = new StringTokenizer(accountString, ":");
		int accountType = 0;
		String localID = null;
		if (st.hasMoreTokens()) {
			accountType = Integer.parseInt(st.nextToken());
		}
		if (st.hasMoreTokens()) {
			localID = st.nextToken();
		}
		GameAccessManager.addGameAccess(localID, accountType, gameId, accessRights);
		new NotificationDelegator(this).broadcast(new Game(), accountString);

	}

	public void provideAccessWithCheck(Long gameIdentifier, Account account, Integer accessRight) {
		provideAccess(gameIdentifier, account, accessRight);

	}
	
	public void provideAccessWithCheck(Long gameIdentifier, String account, Integer accessRight) {
		provideAccess(gameIdentifier, account, accessRight);
		

	}

	public void removeAccessWithCheck(Long gameIdentifier, String account) {
		StringTokenizer st = new StringTokenizer(account, ":");
		int accountType = 0;
		String localID = null;
		if (st.hasMoreTokens()) {
			accountType = Integer.parseInt(st.nextToken());
		}
		if (st.hasMoreTokens()) {
			localID = st.nextToken();
		}
		GameAccessManager.removeGameAccess(localID, accountType, gameIdentifier);
	}

	public GameAccessList getGamesAccess(String account, Long from, Long until) {
		StringTokenizer st = new StringTokenizer(account, ":");
		int accountType = 0;
		String localID = null;
		if (st.hasMoreTokens()) {
			accountType = Integer.parseInt(st.nextToken());
		}
		if (st.hasMoreTokens()) {
			localID = st.nextToken();
		}
		Iterator<GameAccess> it = GameAccessManager.getGameList(accountType, localID, from, until).iterator();
		GameAccessList rl = new GameAccessList();
		while (it.hasNext()) {
			GameAccess ga = (GameAccess) it.next();
			rl.addGameAccess(ga);
		}
		rl.setServerTime(System.currentTimeMillis());
		return rl;
	}

	public GameAccessList getGamesAccess(Long from, Long until) {
		GameAccessList gl = new GameAccessList();
		String myAccount = null;
		if (account != null) {
			myAccount = account.getFullId();
		} else
			myAccount = UserLoggedInManager.getUser(authToken);
		if (myAccount == null) {
			gl.setError("login to retrieve your list of games");
			return gl;
		}
		return getGamesAccess(myAccount, from, until);
	}

	public GameAccessList getAccessList(Long gameIdentifier) {
		GameAccessList returnList = new GameAccessList();
		returnList.setGameAccess(GameAccessManager.getGameList(gameIdentifier));
		return returnList;

	}

	public boolean isOwner(String myAccount, Long gameId) {
		try {
			return GameAccessManager.getAccessById(myAccount + ":" + gameId).getAccessRights() == GameAccessJDO.OWNER;
		} catch (Exception e) {
			return false;
		}

	}

    public boolean canEdit(String myAccount, Long gameId) {
        try {
            int accessRights =     GameAccessManager.getAccessById(myAccount + ":" + gameId).getAccessRights();
            return  accessRights == GameAccessJDO.OWNER || accessRights == GameAccessJDO.CAN_EDIT;
        } catch (Exception e) {
            return false;
        }

    }
	
	public void broadcastGameUpdate(Game game) {
		for (GameAccess ga :GameAccessManager.getGameList(game.getGameId())){
			new NotificationDelegator(this).broadcast(game, ga.getAccount());
		}
	}

}
