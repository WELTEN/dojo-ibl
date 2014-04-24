package org.celstec.arlearn2.portal.client.author.ui.run;

import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.data.*;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.*;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.ContactModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.RunModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.UserModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.RunDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.UserDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.UserClient;
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
public class ExportUsers extends Window {

    public long runId = 0l;


    public ExportUsers(){
        HLayout hLayout = new HLayout(10);
        hLayout.addMember(getFrom());
        hLayout.addMember(getTo());
        setWidth(500);
        setHeight(300);
        addItem(hLayout);
        centerInPage();
        setCanDragResize(true);

    }

    private VLayout getFrom() {
        VLayout from = new VLayout(10);
        from.setWidth("50%");
        DynamicForm form = new DynamicForm();
        SelectItem selectRun = new SelectItem("from" , "Run to export from");
        selectRun.setOptionDataSource(RunDataSource.getInstance());
        selectRun.setDisplayField(RunModel.RUNTITLE_FIELD);
        selectRun.setValueField(RunModel.RUNID_FIELD);

        Criteria crit = new Criteria();
        crit.addCriteria("deleted", false);


        selectRun.setPickListCriteria(crit);

        final ListGrid playersGrid = new ListGrid();

        selectRun.addChangedHandler(new ChangedHandler() {
            @Override
            public void onChanged(ChangedEvent event) {
                System.out.println("select "+event.getValue());
                Criteria criteria = new Criteria();
                criteria.addCriteria(RunModel.RUNID_FIELD,""+ event.getValue());
                criteria.addCriteria(GameModel.DELETED_FIELD, false);
                playersGrid.setCriteria(criteria);
            }
        });

        form.setFields(selectRun);

        playersGrid.setWidth100();
        playersGrid.setHeight("*");
        playersGrid.setAutoFetchData(true);
        playersGrid.setCanDragRecordsOut(true);
        playersGrid.setCanAcceptDroppedRecords(false);
        playersGrid.setCanReorderFields(true);
        playersGrid.setDragDataAction(DragDataAction.COPY);
        playersGrid.setDataSource(UserDataSource.getInstance());
        Criteria criteria = new Criteria();
        criteria.addCriteria(RunModel.RUNID_FIELD,""+ 0);
        criteria.addCriteria(GameModel.DELETED_FIELD, false);
        playersGrid.setCriteria(criteria);

        ListGridField pictureField = new ListGridField(UserModel.PICTURE_FIELD, " ", 40);
        pictureField.setAlign(Alignment.CENTER);
        pictureField.setType(ListGridFieldType.IMAGE);

        ListGridField nameField = new ListGridField(UserModel.NAME_FIELD, "name");
        ListGridField emailField = new ListGridField(UserModel.EMAIL_FIELD, "email");
        ListGridField runField = new ListGridField(RunModel.RUNID_FIELD, "runId");

        playersGrid.setFields(new ListGridField[] {pictureField, nameField, emailField, runField });

        from.addMember(form);
        from.addMember(playersGrid);
        return from;
    }

    private VLayout getTo() {
        VLayout to = new VLayout(10);
        to.setWidth("50%");
        DynamicForm form = new DynamicForm();
        SelectItem selectRun = new SelectItem("to" , "Run to export to");
        final DataSource dSource = new DataSource();
        dSource.setClientOnly(true);
        selectRun.setOptionDataSource(RunDataSource.getInstance());

        form.setFields(selectRun);
        selectRun.setDisplayField(RunModel.RUNTITLE_FIELD);
        selectRun.setValueField(RunModel.RUNID_FIELD);
        Criteria crit = new Criteria();
        crit.addCriteria("deleted", false);
        selectRun.setPickListCriteria(crit);
        to.addMember(form);

        final ListGrid playersGrid = new ListGrid();
        selectRun.addChangedHandler(new ChangedHandler() {
            @Override
            public void onChanged(ChangedEvent event) {
                System.out.println("select "+event.getValue());
                Criteria criteria = new Criteria();
                criteria.addCriteria(RunModel.RUNID_FIELD,""+ event.getValue());
                criteria.addCriteria(GameModel.DELETED_FIELD, false);
                playersGrid.setCriteria(criteria);
                runId = Long.parseLong(""+event.getValue());
            }
        });
        playersGrid.setWidth100();
        playersGrid.setHeight("*");
        playersGrid.setAutoFetchData(true);
        playersGrid.setCanDragRecordsOut(false);
        playersGrid.setCanAcceptDroppedRecords(true);
        playersGrid.setCanReorderFields(true);
        playersGrid.setDataSource(dSource);

        Criteria criteria = new Criteria();
        criteria.addCriteria(RunModel.RUNID_FIELD,""+ 0);
        criteria.addCriteria(GameModel.DELETED_FIELD, false);
        playersGrid.setCriteria(criteria);

        ListGridField pictureField = new ListGridField(UserModel.PICTURE_FIELD, " ", 40);
        pictureField.setAlign(Alignment.CENTER);
        pictureField.setType(ListGridFieldType.IMAGE);

        ListGridField nameField = new ListGridField(UserModel.NAME_FIELD, "name");
        ListGridField emailField = new ListGridField(UserModel.EMAIL_FIELD, "email");
        ListGridField runField = new ListGridField(RunModel.RUNID_FIELD, "runId");

        playersGrid.setFields(new ListGridField[] {pictureField, nameField, emailField, runField });
        to.addMember(playersGrid);

        IButton button = new IButton("export");
        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                System.out.println("click ");
                dSource.fetchData(null, new DSCallback() {
                    public void execute(DSResponse response, Object rawData, DSRequest request) {

                        for (Record rec: response.getData()) {
                            System.out.println("rec "+rec.getAttributeAsString(UserModel.FULL_ACCOUNT_FIELD) );
                            User u = new User();
                            u.setRunId(runId);
                            u.setFullIdentifier(rec.getAttributeAsString(UserModel.FULL_ACCOUNT_FIELD));

                            UserClient.getInstance().createUser(u, new JsonCallback(){
                                public void onJsonReceived(JSONValue jsonValue) {
                                    UserDataSource.getInstance().loadDataFromWeb(runId);
                                }

                            });
                        }
                    }
                });
            }
        });
        to.addMember(button);
        return to;
    }


}
