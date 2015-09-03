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

import java.util.Iterator;

import org.celstec.arlearn2.beans.GamePackage;
import org.celstec.arlearn2.beans.RunPackage;
import org.celstec.arlearn2.beans.notification.authoring.GameCreationStatus;
import org.celstec.arlearn2.beans.notification.authoring.RunCreationStatus;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.run.Team;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.delegators.RunDelegator;
import org.celstec.arlearn2.delegators.TeamsDelegator;
import org.celstec.arlearn2.delegators.UsersDelegator;
import org.celstec.arlearn2.delegators.notification.ChannelNotificator;


public class RunUnpacker {
	
	
	private RunPackage runPackage;
	private String auth;
	private long gameId;

	public RunUnpacker(RunPackage arlPackage, String auth, long gameId) {
		this.runPackage = arlPackage;
		this.auth = auth;
		this.gameId = gameId;
	}

	public void unpack() {
			RunCreationStatus status = new RunCreationStatus();

			Run run = runPackage.getRun();
			run.setGameId(gameId);
			if (run != null) {
				RunDelegator rd = new RunDelegator(auth);
				UsersDelegator qu = new UsersDelegator(auth);
				String myAccount = qu.getCurrentUserAccount();
				
				run.setRunId(null);
				run = rd.createRun(run);
				
				status.setGameId(run.getGameId());
				status.setRunId(run.getRunId());
				status.setStatus(RunCreationStatus.RUN_CREATED);
				ChannelNotificator.getInstance().notify(myAccount, status);
				
				if (run.getRunId() != null) {
					TeamsDelegator td = new TeamsDelegator(rd);
					for (Iterator iterator = runPackage.getTeams().iterator(); iterator
							.hasNext();) {
						Team t = (Team) iterator.next();
						t.setRunId(run.getRunId());
						Team tDb = td.createTeam(t);
						
						status.setStatus(RunCreationStatus.FIRST_TEAM_CREATED);
						ChannelNotificator.getInstance().notify(myAccount, status);
						
						if (tDb.getTeamId() != null) {
							UsersDelegator ud = new UsersDelegator(rd);
							for (Iterator iterator2 = t.getUsers().iterator(); iterator2
									.hasNext();) {
								status.setStatus(RunCreationStatus.FIRST_USER_CREATED);
								ChannelNotificator.getInstance().notify(myAccount, status);
								
								
								User u = (User) iterator2.next();
								u.setTeamId(tDb.getTeamId());
								u.setRunId(run.getRunId());
								ud.createUser(u);

							}
						}
					}

				}
				status.setStatus(100);
				ChannelNotificator.getInstance().notify(myAccount, status);
			}	

	}
}
