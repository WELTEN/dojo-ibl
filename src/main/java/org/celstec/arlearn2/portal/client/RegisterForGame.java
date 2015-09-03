package org.celstec.arlearn2.portal.client;

import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.layout.HLayout;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.ContactModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameRoleModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GameRolesDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.network.AccountClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.UserClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.run.RunClient;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.game.GameClient;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Account;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Game;
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
public class RegisterForGame {
    private Game game;
    private long gameId;
    private Run run;
    Account myAccount;
    String fullIdentifier;
    private String[] selectedRoles;

    public void loadPage() {
        String gameIdAsString = Window.Location.getParameter("gameId");
        gameId = Long.parseLong(gameIdAsString);
        AccountClient.getInstance().accountDetails(new JsonCallback(){
            @Override
            public void onJsonReceived(JSONValue jsonValue) {
                myAccount = new Account(jsonValue.isObject());
                fullIdentifier = myAccount.getAccountType()+":"+myAccount.getLocalId();
                System.out.println(myAccount.getJsonRep());
                proceedToRetrievingGame();
            }
        });

    }


    private void proceedToRetrievingGame() {
        GameClient.getInstance().getGame(gameId, new JsonCallback(){
            @Override
            public void onJsonReceived(JSONValue jsonValue) {
                game = new Game(jsonValue.isObject());
                System.out.println(game.getJsonRep());
                if (game.getRoles()!= null && game.getRoles().length != 0) {
                    RoleWindow rw = new RoleWindow(game.getRoles());
                    rw.show();

                } else {
                    System.out.println("bef creation");
                    proceedToRunCreation();
                }
            }
        });
    }
    private void proceedToRunCreation() {
        System.out.println("in run creation");
        RunClient.getInstance().createRun(game.getGameId(), game.getString(GameModel.GAME_TITLE_FIELD), new JsonCallback(){
            @Override
            public void onJsonReceived(JSONValue jsonValue) {
                System.out.println("after creation");
                run = new Run(jsonValue.isObject());
//                System.out.println(run.getJsonRep());
                proceedToAddUser();
            }


        });
    }
    private void proceedToAddUser() {
        User u = new User();
        u.setRunId(run.getRunId());
        u.setFullIdentifier(fullIdentifier);
        if (selectedRoles != null) u.setRoles(selectedRoles);
        UserClient.getInstance().createUser(u, new JsonCallback(){
            @Override
            public void onJsonReceived(JSONValue jsonValue) {
                SC.say("Registration successfull",
                        "You have been added to this game. Now log in to ARLearn on your mobile device to play the run");
            }
        });

    }

    public class RoleWindow extends com.smartgwt.client.widgets.Window {
        private SelectItem roleSelect;
        private HLayout buttonLayout;
        private DynamicForm form;

        public RoleWindow(String roles[]){
            GameRolesDataSource.getInstance().addRole(gameId, roles);
            createForm();;
            createButtonLayout();
            createUI();

            setWidth(200);
            setHeight(100);
            setIsModal(true);
            setShowModalMask(true);
            setAutoCenter(true);
            setTitle("Select Role");
        }

        private void createUI() {
            addItem(form);
            addItem(buttonLayout);
        }

        private void createForm() {
            form = new DynamicForm();

            roleSelect = new SelectItem("roleSelect", "role");
            roleSelect.setAllowEmptyValue(true);
            roleSelect.setOptionDataSource(GameRolesDataSource.getInstance());
            roleSelect.setDisplayField(GameRoleModel.ROLE_FIELD);
            roleSelect.setValueField(GameRoleModel.ROLE_FIELD);
            roleSelect.setMultiple(true);


            form.setFields(roleSelect);
        }
        private void createButtonLayout() {
            final IButton addRoles = new IButton("Add roles");

            buttonLayout = new HLayout();
            buttonLayout.setAlign(Alignment.CENTER);
            buttonLayout.setLayoutMargin(6);
            buttonLayout.setMembersMargin(6);
                buttonLayout.addMember(addRoles);
            addRoles.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    addRoles.setDisabled(true);
                    selectedRoles = roleSelect.getValues();
                    RoleWindow.this.destroy();
                    proceedToRunCreation();
                }
            });

        }

    }
}
