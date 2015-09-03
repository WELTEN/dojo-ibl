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
package org.celstec.arlearn2.download;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.celstec.arlearn2.beans.GamePackage;
import org.celstec.arlearn2.beans.RunPackage;
import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.game.ScoreDefinitionList;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.run.Team;
import org.celstec.arlearn2.beans.run.TeamList;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.beans.run.UserList;
import org.celstec.arlearn2.delegators.GameDelegator;
import org.celstec.arlearn2.delegators.GeneralItemDelegator;
import org.celstec.arlearn2.delegators.RunDelegator;
import org.celstec.arlearn2.delegators.ScoreDefinitionDelegator;
import org.celstec.arlearn2.delegators.TeamsDelegator;
import org.celstec.arlearn2.delegators.UsersDelegator;

import java.io.*;
import java.util.ListIterator;

public class DownloadServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, FileNotFoundException {
        String fileName = "test";
        if (request.getParameter("runId") != null) fileName = "run." + request.getParameter("runId");
        if (request.getParameter("gameId") != null) fileName = "game." + request.getParameter("gameId");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + ".json\"");
        ServletContext ctx = getServletContext();

        String type = request.getParameter("type");
        String returnString = "";
        if ("run".equals(type)) returnString = returnRun(request);
        if ("game".equals(type)) returnString = returnGame(request);
        PrintWriter pw = response.getWriter();
        pw.write(returnString);
        pw.close();
    }


    private String returnGame(HttpServletRequest request) {
        long gameId = Long.parseLong(request.getParameter("gameId"));
        String auth = "auth=" + request.getParameter("auth");
        GamePackage gp = new GamePackage();
        UsersDelegator qu = new UsersDelegator(auth);
        Account account = qu.getCurrentAccount();
        GameDelegator gd = new GameDelegator(account, auth);
        gp.setGame(gd.getGame(gameId));

        GeneralItemDelegator gid = new GeneralItemDelegator(gd);

        gp.setGeneralItems(gid.getGeneralItems(gameId).getGeneralItems());
        for (ListIterator<GeneralItem> iter = gp.getGeneralItems().listIterator(gp.getGeneralItems().size()); iter.hasPrevious(); ) {
            if (iter.previous().getDeleted() == true) {
                iter.remove();
            }
        }
//			List<GeneralItem> cloneList = gp.getGeneralItems().c
//			for (GeneralItem gi : cloneList) {
//				if (gi.getDeleted() == true) {
//					gp.getGeneralItems().remove(gi);
//				}
//			}
        ScoreDefinitionDelegator sd = new ScoreDefinitionDelegator(gd);
        ScoreDefinitionList list = new ScoreDefinitionList();
        gp.setScoreDefinitions(sd.getScoreDefinitionsList(gameId, null, null, null));

        return gp.toString();
    }

    private String returnRun(HttpServletRequest request) {
        long runId = Long.parseLong(request.getParameter("runId"));
        String auth = "auth=" + request.getParameter("auth");

        RunPackage rp = new RunPackage();
        RunDelegator rd = new RunDelegator(auth);
        rp.setRun(rd.getRun(runId));

        TeamsDelegator td = new TeamsDelegator(rd);
        TeamList tl = td.getTeams(runId);
        if (tl != null) rp.setTeams(tl.getTeams());

        UsersDelegator ud = new UsersDelegator(rd);
        UserList ul = ud.getUsers(runId);
        if (ul != null) {
            for (User u : ul.getUsers()) {
                for (Team t : rp.getTeams()) {
                    if (t.getTeamId().equals(u.getTeamId())) {
                        u.setTeamId(null);
                        t.addUser(u);
                    }
                }
            }
            for (Team t : rp.getTeams()) {
                t.setTeamId(null);
            }
        }

        rp.getRun().setGame(null);
        return rp.toString();
    }


}
