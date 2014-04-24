package org.celstec.arlearn2.portal.client.debug;

import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.ActionModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GeneralItemModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GeneralItemsVisibilityModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.ActionsDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GeneralItemDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GeneralItemVisibilityDataSource;
import org.celstec.arlearn2.portal.client.account.AccountManager;
import org.celstec.arlearn2.portal.client.toolbar.ToolBar;

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
public class DebugPage {

    ToolBar toolbar;// = new Toolbar();

    public void loadPage() {
        AccountManager accountManager = AccountManager.getInstance();
        accountManager.setAccountNotification(new AccountManager.NotifyAccountLoaded() {

            @Override
            public void accountLoaded(boolean success) {
                if (success) {
                    buildUi();
                } else {
                    SC.say("Credentials are invalid. Log in again.");
                }
            }
        });
    }

    private void buildUi() {
        toolbar = new ToolBar(false);
        String type  = com.google.gwt.user.client.Window.Location.getParameter("type");

        if ("actions".equals(type)) {
            buildActionUi(Long.parseLong(com.google.gwt.user.client.Window.Location.getParameter("runId")));
        }
        if ("giVis".equals(type)) {
            buildGiVisUi(Long.parseLong(com.google.gwt.user.client.Window.Location.getParameter("runId")));
        }

    }

    private void buildGiVisUi(long runId) {
        ListGrid masterList = new ListGrid();
        masterList.setDataSource(GeneralItemVisibilityDataSource.getInstance());
        GeneralItemVisibilityDataSource.getInstance().loadDataFromWeb(runId);
        ListGridField giField = new ListGridField(GeneralItemsVisibilityModel.GENERALITEMID_FIELD, "gi");
        ListGridField statusField = new ListGridField(GeneralItemsVisibilityModel.STATUS_FIELD, "status");
        ListGridField accountField = new ListGridField(GeneralItemsVisibilityModel.ACCOUNT_FIELD, "account");
        ListGridField timeField = new ListGridField(GeneralItemsVisibilityModel.TIMESTAMP_FIELD, "time");
        ListGridField timePrettyField = new ListGridField(GeneralItemsVisibilityModel.TIMESTAMP_PRETTY_FIELD, "time pretty");

        ListGridField lastModField = new ListGridField(GeneralItemsVisibilityModel.LASTMODIFICATION_FIELD, "last modification");
        timeField.setType(ListGridFieldType.FLOAT);

        ListGridField lastModPretty = new ListGridField(GeneralItemsVisibilityModel.LASTMODIFICATIONPRETTY_FIELD, "last mod");

        masterList.setFields(new ListGridField[]{giField,statusField,accountField, timeField, timePrettyField, lastModField, lastModPretty});
        masterList.setAutoFetchData(true);
        masterList.setWidth100();
        masterList.setHeight100();
        masterList.setShowFilterEditor(true);
        masterList.setEditByCell(true);
        masterList.setCanEdit(true);

        RootPanel.get("debug").add(masterList);
    }

    private void buildActionUi(long runId) {
        ListGrid masterList = new ListGrid();
        masterList.setDataSource(ActionsDataSource.getInstance());
        ActionsDataSource.getInstance().loadDataFromWeb(runId);
        ListGridField actionField = new ListGridField(ActionModel.ACTION_FIELD, "action");
        ListGridField generalItemField = new ListGridField(ActionModel.GENERALITEMID_FIELD, "generalItem");
        ListGridField runIdField = new ListGridField(ActionModel.RUNID_FIELD, "runId");
        ListGridField accountField = new ListGridField(ActionModel.ACCOUNT_FIELD, "account");
        ListGridField timeField = new ListGridField(ActionModel.TIMESTAMP_FIELD, "time");
        timeField.setType(ListGridFieldType.FLOAT);

        ListGridField timePrettyField = new ListGridField(ActionModel.TIMESTAMP_PRETTY_FIELD, "time");


        masterList.setFields(new ListGridField[]{actionField,generalItemField, runIdField, timePrettyField,accountField, timeField});
        masterList.setAutoFetchData(true);
        masterList.setWidth100();
        masterList.setHeight100();
        masterList.setShowFilterEditor(true);
        masterList.setEditByCell(true);
        masterList.setCanEdit(true);

        RootPanel.get("debug").add(masterList);




    }
}
