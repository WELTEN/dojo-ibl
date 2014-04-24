package org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.AbstractRecord;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameFileModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameRoleModel;
import org.celstec.arlearn2.gwtcommonlib.client.network.GenericClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.game.GameClient;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Game;

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
public class GameFilesDataSource extends GenericDataSource {

    public static GameFilesDataSource instance;

    public static GameFilesDataSource getInstance() {
        if (instance == null)
            instance = new GameFilesDataSource();
        return instance;
    }

    public GameFilesDataSource() {
        super();
        setDataSourceModel(new GameFileModel(this));
    }

    public GenericClient getHttpClient() {
        return null;
    }

    @Override
    public void loadDataFromWeb() {

    }

    @Override
    public void processNotification(JSONObject bean) {
    }

    public void addGameFile (long gameId, String fileId, String fileUrl) {
        String pk = gameId+":"+fileId;
        if (getRecord(pk) == null) {
            AbstractRecord record = createRecord();
            record.setCorrespondingJsonObject(new JSONObject());
            record.getCorrespondingJsonObject().put(GameRoleModel.ROLE_PK_FIELD, new JSONString(pk));
            record.setAttribute(GameModel.GAMEID_FIELD, gameId);
            record.setAttribute(GameFileModel.PRIMARY_KEY, pk);
            record.setAttribute(GameFileModel.FILE_ID, fileId);
            record.setAttribute(GameFileModel.FILE_URL, fileUrl);
            saveRecord(record);
        }
    }

    public void loadGameFiles(long gameId) {
        Game game = GameDataSource.getInstance().getGame(gameId);
        if (game != null) {
            loadGameFiles(game);
        } else {
            GameClient.getInstance().getGame(gameId, new JsonCallback() {
                public void onJsonReceived(JSONValue jsonValue) {
                    loadGameFiles(new Game(jsonValue.isObject()));
                }
            });
        }
    }

    public void loadGameFiles(Game game) {

    }

}
