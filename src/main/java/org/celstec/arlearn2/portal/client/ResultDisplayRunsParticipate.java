package org.celstec.arlearn2.portal.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.RunModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.RunDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.RunParticipateDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.ui.grid.GenericListGrid;
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
public class ResultDisplayRunsParticipate {
//    ToolBar toolStrip;
    GenericListGrid masterList;

    public void loadPage() {
        RunParticipateDataSource.getInstance().loadDataFromWeb();

//		createToolstrip();
//        toolStrip = new ToolBar();


        VLayout vertical = new VLayout();
        vertical.setWidth100();
        vertical.setHeight100();
//        vertical.addMember(toolStrip);
        createRunsDataSource();

        VLayout verticalGrid = new VLayout();

        verticalGrid.setLayoutAlign(Alignment.CENTER);
        verticalGrid.setAlign(Alignment.CENTER);
        verticalGrid.setDefaultLayoutAlign(Alignment.CENTER);

        verticalGrid.setWidth100();
        verticalGrid.setHeight("*");
        verticalGrid.addMember(masterList);

        vertical.addMember(verticalGrid);
        RootPanel.get("resultDisplayRunsParticipate").add(vertical);

    }

    private void createRunsDataSource() {
        masterList = new GenericListGrid(false, false, false, false, false);
        masterList.setShowRecordComponentsByCell(true);
//		masterList.setCanRemoveRecords(true);
        masterList.setShowRollOverCanvas(true);

        masterList.setShowAllRecords(true);
        masterList.setShowRecordComponents(true);

        masterList.setHeight(400);
        masterList.setWidth(800);
//		masterList.setHeight("40%");
        masterList.setAutoFetchData(true);
        masterList.setCanEdit(true);


        masterList.addRecordClickHandler(new RecordClickHandler() {
            public void onRecordClick(RecordClickEvent event) {
                Window.open("/ResultDisplay.html?runId=" + event.getRecord().getAttribute(RunModel.RUNID_FIELD) + "&version=1", "_self", "");

            }
        });

        masterList.setDataSource(RunParticipateDataSource.getInstance());

        ListGridField idField = new ListGridField(RunModel.RUNID_FIELD, "id ");
        idField.setWidth(30);
        idField.setCanEdit(false);
        idField.setHidden(true);

        ListGridField gameIdField = new ListGridField(RunModel.GAMEID_FIELD, "GameId ");
        gameIdField.setCanEdit(false);
        gameIdField.setHidden(true);

        ListGridField titleRunField = new ListGridField(RunModel.RUNTITLE_FIELD, "Run Title ");
        ListGridField titleGameField = new ListGridField(RunModel.GAME_TITLE_FIELD, "Game Title ");
        titleGameField.setCanEdit(false);

//        ListGridField accessRunField = new ListGridField(RunModel.RUN_ACCESS_STRING, "Run access ");
//        accessRunField.setCanEdit(false);
//        accessRunField.setWidth(100);

        masterList.setFields(new ListGridField[] { idField, gameIdField, titleRunField,  titleGameField });

    }
}
