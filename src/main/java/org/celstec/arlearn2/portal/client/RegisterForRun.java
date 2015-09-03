package org.celstec.arlearn2.portal.client;

import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.smartgwt.client.util.SC;
import org.celstec.arlearn2.gwtcommonlib.client.network.AccountClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.UserClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.run.RunClient;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Account;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Run;
import org.celstec.arlearn2.gwtcommonlib.client.objects.User;

/**
 * ****************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * <p/>
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Contributors: Stefaan Ternier
 * ****************************************************************************
 */
public class RegisterForRun {

    private long runId;
    private Run run;
    Account myAccount;
    String fullIdentifier;

    public void loadPage() {
        String gameIdAsString = Window.Location.getParameter("runId");
        runId = Long.parseLong(gameIdAsString);
        AccountClient.getInstance().accountDetails(new JsonCallback(){
            @Override
            public void onJsonReceived(JSONValue jsonValue) {
                myAccount = new Account(jsonValue.isObject());
                fullIdentifier = myAccount.getAccountType()+":"+myAccount.getLocalId();
                System.out.println(myAccount.getJsonRep());
                proceedToRetrievingRun();
            }
        });

    }


    private void proceedToRetrievingRun() {
        RunClient.getInstance().getRun(runId, new JsonCallback(){
            @Override
            public void onJsonReceived(JSONValue jsonValue) {
                run = new Run(jsonValue.isObject());
                System.out.println(run.getJSON());
                proceedToAddUser();

//                if (game.getRoles()!= null && game.getRoles().length != 0) {
//                    RoleWindow rw = new RoleWindow(game.getRoles());
//                    rw.show();
//
//                } else {
//
//                    proceedToRunCreation();
//                }
            }
        });
    }
//    private void proceedToRunCreation() {
//        System.out.println("in run creation");
//        RunClient.getInstance().createRun(game.getGameId(), game.getString(GameModel.GAME_TITLE_FIELD), new JsonCallback(){
//            @Override
//            public void onJsonReceived(JSONValue jsonValue) {
//                System.out.println("after creation");
//                run = new Run(jsonValue.isObject());
////                System.out.println(run.getJsonRep());
//                proceedToAddUser();
//            }
//
//
//        });
//    }
    private void proceedToAddUser() {
        User u = new User();
        u.setRunId(run.getRunId());
        u.setFullIdentifier(fullIdentifier);
        UserClient.getInstance().createUser(u, new JsonCallback(){
            @Override
            public void onJsonReceived(JSONValue jsonValue) {
                SC.say("Registration successful",
                        "You have been added to this run. Now log in to ARLearn on your mobile device to play the run");
            }
        });

    }


}
