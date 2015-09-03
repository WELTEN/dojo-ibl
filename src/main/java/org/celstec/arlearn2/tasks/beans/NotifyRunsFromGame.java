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

import java.util.List;

import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.delegators.RunDelegator;


public class NotifyRunsFromGame extends GenericBean {

	private long gameId;
	private int modificationType;
	private String gi;
	
	public NotifyRunsFromGame() {
		
	}
	
	public NotifyRunsFromGame(String token, Long gameId,  GeneralItem gi, Integer modificationType) {
		super(token);
		this.gameId = gameId;
		if (gi != null) this.gi = gi.toString();
		this.modificationType = modificationType;
	}

	
	public long getGameId() {
		return gameId;
	}


	public void setGameId(long gameId) {
		this.gameId = gameId;
	}


	public String getGi() {
		return gi;
	}


	public void setGi(String gi) {
		this.gi = gi;
	}

	public int getModificationType() {
		return modificationType;
	}

	public void setModificationType(int modificationType) {
		this.modificationType = modificationType;
	}

	@Override
	public void run() {
			RunDelegator rd = new RunDelegator("auth=" + getToken());
			List<Run> runList = rd.getRunsForGame(gameId);
			for (Run r: runList) {
				if (r.getDeleted() == null || !r.getDeleted()) (new NotifyUsersFromGame(getToken(), r.getRunId(), gi, modificationType)).scheduleTask();

			}

		
	}
}
