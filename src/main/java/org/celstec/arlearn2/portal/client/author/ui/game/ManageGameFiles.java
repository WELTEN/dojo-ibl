package org.celstec.arlearn2.portal.client.author.ui.game;

import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.types.MultipleAppearance;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameFileModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameRoleModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GeneralItemModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GameDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GameFilesDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GameRolesDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.ui.grid.GenericListGrid;

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
public class ManageGameFiles extends Window {

    private GenericListGrid listGrid;

    public ManageGameFiles() {
        VLayout vLayout = new VLayout(10);
//        vLayout.addMember(form);

        DynamicForm gameForm = new DynamicForm();
        SelectItem rolesSelectItem = new SelectItem("games", "select game");

        rolesSelectItem.setDisplayField(GameModel.GAME_TITLE_FIELD);
        rolesSelectItem.setValueField(GameModel.GAMEID_FIELD);
        rolesSelectItem.setOptionDataSource(GameDataSource.getInstance());
        Criteria crit = new Criteria();
        crit.addCriteria(GameModel.DELETED_FIELD, false);
        rolesSelectItem.setPickListCriteria(crit);
        gameForm.setItems(rolesSelectItem);

        rolesSelectItem.addChangedHandler(new ChangedHandler() {
            @Override
            public void onChanged(ChangedEvent event) {
                ManageGameFiles.this.destroy();
                new ManageGameFiles(Long.parseLong(""+event.getValue())).show();
            }
        });

        vLayout.addMember(gameForm);
        addItem(vLayout);
        setWidth(320);
        setHeight(75);
        centerInPage();
    }

    public ManageGameFiles(long gameId) {
        VLayout vLayout = new VLayout(10);
        addItem(vLayout);
        setWidth(320);
        setHeight(175);
        centerInPage();

        listGrid = new GenericListGrid(false, true, false, false, false){
            protected void deleteItem(ListGridRecord rollOverRecord) {

            }
        };
        listGrid.setWidth100();
        listGrid.setHeight("*");
        listGrid.setShowRollOverCanvas(true);

        listGrid.setAutoFetchData(true);

        listGrid.setDataSource(GameFilesDataSource.getInstance());
        ListGridField keyField = new ListGridField(GameFileModel.FILE_ID, "key");
        ListGridField urlField = new ListGridField(GameFileModel.FILE_URL, "url");
        listGrid.setFields(new ListGridField[] { keyField, urlField });

        Criteria criteria = new Criteria();
        criteria.addCriteria(GameModel.GAMEID_FIELD,""+ gameId);
        listGrid.setCriteria(criteria);

        vLayout.addMember(listGrid);
        GameFilesDataSource.getInstance().addGameFile(gameId, "aKey", "http://test/test");
    }

}
