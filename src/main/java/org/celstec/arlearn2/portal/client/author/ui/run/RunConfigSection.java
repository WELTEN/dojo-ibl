package org.celstec.arlearn2.portal.client.author.ui.run;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.RunModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.RunDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.TeamDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Run;
import org.celstec.arlearn2.portal.client.author.ui.SectionConfig;
import org.celstec.arlearn2.portal.client.author.ui.run.i18.RunConstants;

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
public class RunConfigSection extends SectionConfig {

    private static RunConstants constants = GWT.create(RunConstants.class);

    private Run run;
    private DynamicForm form;

    public RunConfigSection() {
        super("Config run");
        HLayout hLayout = new HLayout();
        form = new DynamicForm();
        final TextItem titleText = new TextItem(RunModel.RUNTITLE_FIELD, constants.runTitle());
        titleText.setWidth("100%");
        form.setFields(titleText);
        form.setWidth("*");
        Button button = new Button("save");
        button.setAlign(Alignment.CENTER);
        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                run.setTitle(form.getValueAsString(RunModel.RUNTITLE_FIELD));
                run.writeToCloud(new JsonCallback(){
                    public void onJsonReceived(JSONValue jsonValue) {
                           RunConfigSection.this.setExpanded(false);
                        RunDataSource.getInstance().loadRun(run.getRunId());

                    }
                });
            }
        });
        hLayout.addMember(form);
        hLayout.addMember(button);
        setItems(hLayout);
        hLayout.setHeight(40);
    }

    public void loadRun(Run run) {
        this.run = run;
        form.setValue(RunModel.RUNTITLE_FIELD, run.getTitle());
    }
}
