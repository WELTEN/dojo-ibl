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
package org.celstec.arlearn2.delegators;

import org.celstec.arlearn2.api.Service;
import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.game.Config;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.run.*;
import org.celstec.arlearn2.jdo.classes.RunAccessJDO;
import org.celstec.arlearn2.jdo.manager.RunAccessManager;
import org.celstec.arlearn2.jdo.manager.RunManager;
import org.celstec.arlearn2.jdo.manager.UserManager;
import org.celstec.arlearn2.tasks.beans.*;
import org.celstec.arlearn2.util.RunsCache;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
//import org.celstec.arlearn2.tasks.beans.DeleteInventoryRecords;
//import org.celstec.arlearn2.tasks.beans.DeleteProgressRecord;

//TODO migrate and adapt cache
public class RunDelegator extends GoogleDelegator {
    private static final Logger logger = Logger.getLogger(RunDelegator.class.getName());

    public RunDelegator(String authtoken) {
        super(authtoken);
    }

    public RunDelegator(GenericBean bean) {
        super(bean);
    }

    public RunDelegator(Service service) {
        super(service);
    }

    public RunDelegator(GoogleDelegator gd) {
        super(gd);
    }

    public RunDelegator(Account account, String authToken) {
        super(account, authToken);
    }

    public Run getRun(Long runId) {
        Run r = RunsCache.getInstance().getRun(runId);
        if (r == null) {
            List<Run> runList = RunManager.getRuns(runId, null, null, null, null, null);
            if (runList.isEmpty())
                return null;
            r = runList.get(0);
            RunsCache.getInstance().putRun(runId, r);
        }
        GameDelegator gd = new GameDelegator(this);
        r.setGame(gd.getUnOwnedGame(r.getGameId()));
        return r;
    }

    public RunList getRuns() {
        RunList rl = new RunList();
        UsersDelegator qu = new UsersDelegator(this);
        String myAccount = qu.getCurrentUserAccount();

        if (myAccount == null) {
            rl.setError("login to retrieve your list of games");
        } else {
            rl.setRuns(RunManager.getRuns(null, null, myAccount, null, null, null));
        }
        return rl;
    }

    public long getRunDuration(Long runId) {
        Run r = getRun(runId);
        if (r == null) {
            System.out.println("runid is null");
            return 0;
        }
        Long runStartTime = r.getStartTime();
        if (runStartTime == null)
            return 0;
        return System.currentTimeMillis() - r.getStartTime();
    }

    public RunList getParticipateRuns() {
        UsersDelegator qu = new UsersDelegator(this);
        String myAccount = qu.getCurrentUserAccount();
        // TODO migrate this method to UserQuery
        // TODO add this to cache
        // TODO migrate RunsCache
        Iterator<User> it = UserManager.getUserList(null, myAccount, null, null).iterator();
        RunList rl = new RunList();
        while (it.hasNext()) {
            User user = (User) it.next();
            Run r = getRun(user.getRunId());
            if (r != null) {
                if (r.getDeleted() == null || r.getDeleted() == false) r.setDeleted(user.getDeleted());
                rl.addRun(r);
            } else {
                logger.severe("following run does not exist" + user.getRunId());

            }
        }
        rl.setServerTime(System.currentTimeMillis());

        return rl;
    }

    public RunList getParticipateRuns(Long gameId) {
        UsersDelegator qu = new UsersDelegator(this);
        String myAccount = qu.getCurrentUserAccount();
        Iterator<User> it = UserManager.getUserList(null, myAccount, null, null, gameId).iterator();
        RunList rl = new RunList();
        while (it.hasNext()) {
            User user = (User) it.next();
            Run r = getRun(user.getRunId());
            if (r != null) {
                if (r.getDeleted() == null || r.getDeleted() == false) r.setDeleted(user.getDeleted());
                rl.addRun(r);
            } else {
                logger.severe("following run does not exist" + user.getRunId());

            }
        }
        rl.setServerTime(System.currentTimeMillis());

        return rl;
    }

    public RunList getRuns(String accountId) {
        UsersDelegator qu = new UsersDelegator(this);

        // TODO migrate this method to UserQuery
        // TODO add this to cache
        // TODO migrate RunsCache
        Iterator<User> it = UserManager.getUserList(null, accountId, null, null).iterator();
        RunList rl = new RunList();
        while (it.hasNext()) {
            User user = (User) it.next();
            Run r = getRun(user.getRunId());
            if (r != null) {
                if (r.getDeleted() == null || r.getDeleted() == false) r.setDeleted(user.getDeleted());
                rl.addRun(r);
            } else {
                logger.severe("following run does not exist" + user.getRunId());

            }
        }
        rl.setServerTime(System.currentTimeMillis());

        return rl;
    }

    public RunList getParticipateRuns(Long from, Long until) {
        UsersDelegator qu = new UsersDelegator(this);
        String myAccount = qu.getCurrentUserAccount();
        // TODO migrate this method to UserQuery
        // TODO add this to cache
        // TODO migrate RunsCache
        Iterator<User> it = UserManager.getUserList(myAccount, from, until).iterator();
        RunList rl = new RunList();
        while (it.hasNext()) {
            User user = (User) it.next();
            Run r = getRun(user.getRunId());
            if (r != null) {
                if (user.getDeleted() != null && user.getDeleted()) r.setDeleted(user.getDeleted());
                rl.addRun(r);
            } else {
                logger.severe("following run does not exist" + user.getRunId());

            }
        }
        rl.setServerTime(System.currentTimeMillis());
        return rl;
    }

    public Run createRun(Run run) {
        if (run.getRunId() != null) RunsCache.getInstance().removeRun(run.getRunId());
        if (run.getStartTime() == null) {
            run.setStartTime(System.currentTimeMillis());
            run.setServerCreationTime(run.getStartTime());
        }
        String myAccount = "";
        if (account != null) {
            UsersDelegator qu = new UsersDelegator(this);
            myAccount = qu.getCurrentUserAccount();

            if (myAccount == null) {
                run.setError("login to create a game");
                return run;
            }
        }
        GameDelegator cg = new GameDelegator(this);
        Game game = cg.getGame(run.getGameId());
        if (game == null) {
            run.setError("Game with id '" + run.getGameId() + "' does not exist");
            return run;
        }

//		if (account != null) {
        return createRunWithAccount(run);
//		} else {
//			run.setRunId(RunManager.addRun(run.getTitle(), myAccount, game.getGameId(), run.getRunId(), run.getStartTime(), run.getServerCreationTime(), run));
//			return run;
//		}
    }

    private Run createRunWithAccount(Run run) {

        /************************************************************************
         Generation of a random code. As it must be unique we perform a select
         query by its code. If the code exists in the database we perform the
         query again until we find another unique code.
         ***********************************************************************/
        String code = generateRandomCode();
        Iterator<Run> it  = RunManager.getRuns(null, null, null, null, null, code).iterator();

        while (it.hasNext()){
            code = generateRandomCode();
            it = RunManager.getRuns(null, null, null, null, null, code).iterator();
        }
        run.setCode(code);

        run.setRunId(RunManager.addRun(run));

        RunAccessDelegator rd = new RunAccessDelegator(this);
        rd.provideAccess(run.getRunId(), account, RunAccessJDO.OWNER);
        if (this.account != null) {
            new NotificationDelegator(this).broadcast(run, account.getFullId());
        }

        (new UpdateVariableInstancesForAll(authToken, this.account, run.getRunId(), run.getGameId(), 1)).scheduleTask();
        (new UpdateVariableEffectInstancesForAll(authToken, this.account, run.getRunId(), run.getGameId(), 1)).scheduleTask();

        return run;
    }

    private static String generateRandomCode(){
        char[] chars = "abcdefghijklmnopqrstuvwxyzABSDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
        Random r = new Random(System.currentTimeMillis());
        char[] id = new char[5];
        for (int i = 0;  i < 5;  i++) {
            id[i] = chars[r.nextInt(chars.length)];
        }
        return String.valueOf(id).toUpperCase();
    }

    public Run updateRun(Run run, long runId) {
        UsersDelegator qu = new UsersDelegator(this);
        String myAccount = qu.getCurrentUserAccount();

        if (myAccount == null) {
            run.setError("login to update a run");
            return run;
        }
        Run oldRun = getRun(runId);
        if (oldRun == null) {
            run.setError("run with id '" + runId + "' does not exist");
            return run;
        }
        GameDelegator cg = new GameDelegator(this);
        Game game = cg.getGame(run.getGameId());
        if (game == null) {
            run.setError("Game with id '" + run.getGameId() + "' does not exist");
            return run;
        }
        RunsCache.getInstance().removeRun(runId);
        RunManager.updateRun(runId, run);
        return run;
    }

    public Run deleteRun(Long runId) {
        UsersDelegator qu = new UsersDelegator(this);
        String myAccount = qu.getCurrentUserAccount();
        return deleteRun(getRun(runId), myAccount);
    }

    private Run deleteRun(Run r, String myAccount) {
        RunAccessDelegator gad = null;
        if (account != null) {
            gad = new RunAccessDelegator(this);
            if (!gad.isOwner(r.getRunId())) {
                Run run = new Run();
                run.setError("You are not the owner of this run");
                return run;
            }
        } else if (!r.getOwner().equals(myAccount)) {
            Run run = new Run();
            run.setError("You are not the owner of this run");
            return run;
        }
//		RunManager.deleteRun(r.getRunId());
        RunManager.setStatusDeleted(r.getRunId());
        RunAccessManager.resetGameAccessLastModificationDate(r.getRunId());
        RunsCache.getInstance().removeRun(r.getRunId());
        (new UpdateGeneralItemsVisibility(authToken, this.account, r.getRunId(), null, 2)).scheduleTask();

        (new UpdateVariableInstancesForAll(authToken, this.account, r.getRunId(), r.getGameId(), 2)).scheduleTask();
        (new UpdateVariableEffectInstancesForAll(authToken, this.account, r.getRunId(), r.getGameId(), 2)).scheduleTask();


//		(new DeleteVisibleItems(authToken, r.getRunId())).scheduleTask();
        (new DeleteActions(authToken, this.account, r.getRunId())).scheduleTask();
        (new DeleteTeams(authToken, this.account, r.getRunId(), null)).scheduleTask();

        (new DeleteUserAfterDeleteRun(getAuthToken(), r.getRunId())).scheduleTask();

        (new DeleteBlobs(authToken, this.account, r.getRunId())).scheduleTask();
        (new DeleteResponses(authToken, this.account, r.getRunId())).scheduleTask();

        if (this.account != null) {
            gad.broadcastRunUpdate(r);

        }
        return r;
    }

    public void deleteRuns(long gameId, String email) {
        List<Run> runList = RunManager.getRuns(null, gameId, null, null, null, null);
        for (Run r : runList) {
            deleteRun(r, email);
        }
    }

    public Config getConfig(Long runId) {
        Run r = getRun(runId);
        logger.log(Level.SEVERE, "gameId " + r.getGameId());
        GameDelegator gd = new GameDelegator(this);
        logger.log(Level.SEVERE, "game " + gd.getNotOwnedGame(r.getGameId()));
        return gd.getNotOwnedGame(r.getGameId()).getConfig();
    }

    public List<Run> getRunsForGame(long gameId) {
        return RunManager.getRuns(null, gameId, null, null, null, null);

    }

//    public List<Run> getRunsForGame(long gameId, Account account) {
//
//
//    }

//    public Run selfRegister(String tagId) {
//        UsersDelegator qu = new UsersDelegator(this);
//        String myAccount = qu.getCurrentUserAccount();
//
//        List<Run> runList = RunManager.getRuns(null, null, null, null, tagId);
//        if (!runList.isEmpty()) {
//            return selfRegister(runList.get(0), myAccount);
//        } else {
//            Run run = new Run();
//            run.setError("No run with tagid " + tagId + " exists");
//            return run;
//        }
//    }

    public RunList getTaggedRuns(String tagId) {
        RunList rl = new RunList();
        rl.setRuns(RunManager.getRuns(null, null, null, null, tagId, null));
        return rl;
    }


    private Run selfRegister(Run run) { //, String myAccount
        TeamsDelegator td = new TeamsDelegator(this);
        TeamList tl = td.getTeams(run.getRunId());
        for (Team team : tl.getTeams()) {
            if ("default".equals(team.getName())) {
                return selfRegister(run, team); //, myAccount
            }
        }
        if (!tl.getTeams().isEmpty()) {
            return selfRegister(run, tl.getTeams().get(0)); //, myAccount
        }
        Team team = td.createTeam(run.getRunId(), null, "default");
        return selfRegister(run,  team); //myAccount,
    }

    private Run selfRegister(Run run, Team team) { //, String myAccount
        UsersDelegator ud = new UsersDelegator(this);
        User u = new User();
        u.setRunId(run.getRunId());
        u.setEmail(account.getEmail());
        u.setName(account.getName());
        u.setTeamId(team.getTeamId());
        u.setFullIdentifier(account.getFullId());
        u.setGameId(run.getGameId());
        ud.selfRegister(u, run);

        return run;
    }

    public Run selfRegister(Long runId) {
        UsersDelegator qu = new UsersDelegator(this);
        String myAccount = qu.getCurrentUserAccount();


        List<Run> runList = RunManager.getRuns(runId, null, null, null, null, null);
        if (!runList.isEmpty() ) { //&& runList.get(0).getTagId() != null
            return selfRegister(runList.get(0));
        } else {
            Run run = new Run();
            run.setError("No run with runId " + runId + " exists");
            return run;
        }
    }



}
