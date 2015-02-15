package org.celstec.arlearn2.portal.client.author.ui.game;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SubmitItem;
import com.smartgwt.client.widgets.form.fields.UploadItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.AbstractRecord;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GameDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.game.GameClient;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Game;
import org.celstec.arlearn2.gwtcommonlib.client.ui.grid.GenericListGrid;
import org.celstec.arlearn2.portal.client.author.ui.game.i18.GameConstants;

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
public class UploadGameSplashPictureWindow  extends Window {
    private static GameConstants constants = GWT.create(GameConstants.class);

    private VLayout target;
    private HTMLPane pane;
    private DynamicForm form;
    public UploadGameSplashPictureWindow(){
        HLayout hLayout = new HLayout(10);
        hLayout.addMember(getGamesGrid());
        hLayout.addMember(getTarget());
        setTitle("Upload Game thumbnail");
        setWidth(500);
        setHeight(300);
        addItem(hLayout);
        centerInPage();
        setCanDragResize(true);

    }

    private VLayout getGamesGrid() {
        VLayout from = new VLayout(10);
        from.setWidth("50%");
        ListGrid masterList = new GenericListGrid(false, false, false, false, false);
        masterList.setAutoFetchData(true);
        masterList.setDataSource(GameDataSource.getInstance());
        ListGridField titleGameField = new ListGridField(GameModel.GAME_TITLE_FIELD, constants.title());
        masterList.setFields(new ListGridField[]{titleGameField});
        from.addMember(masterList);
        masterList.addCellClickHandler(new CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                Game existingGame = new Game(((AbstractRecord)GameDataSource.getInstance().getRecord(event.getRecord().getAttributeAsLong(GameModel.GAMEID_FIELD))).getCorrespondingJsonObject());
                pane.setContents("Game "+existingGame.getGameId());
                setTarget(existingGame);
                setDynamicForm(existingGame);
            }
        });
        return from;
    }

    private VLayout getTarget() {
        target = new VLayout(10);
        target.setWidth("50%");
        pane = new HTMLPane();
        pane.setContents("<b>Select a game</b>");
        target.addMember(pane);
        return target;
    }

    private void setTarget(Game g) {
        if (pane !=null) {
            target.removeChild(pane);
        }
        pane = new HTMLPane();
        pane.setContents("<b>Upload an splash image for game "+g.getString(GameModel.GAME_TITLE_FIELD)+"</b>");
        target.addMember(pane);

    }

    private void setDynamicForm(Game game) {
        if (form !=null) {
            target.removeChild(form);
        }
        form = new DynamicForm();
        form.setEncoding(Encoding.MULTIPART);
        form.setCanSubmit(true);
        form.setTarget("hidden_frame");


        UploadItem uploadItem = new UploadItem("thumbnailfile", "Thumbnail image");
        SubmitItem button = new SubmitItem("Submitimage", "Submit");
        form.setFields(uploadItem, button);

        GameClient.getInstance().getSplashUrl(game.getGameId(), new JsonCallback(){
            public void onJsonReceived(JSONValue jsonValue) {
                System.out.println(jsonValue);
                if (jsonValue.isObject() !=null) {
                    form.setAction(jsonValue.isObject().get("uploadUrl").isString().stringValue());
                }
            }
        });
        target.addMember(form);
    }


}
