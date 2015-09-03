package org.celstec.arlearn2.portal.client;

import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.events.SubmitValuesEvent;
import com.smartgwt.client.widgets.form.events.SubmitValuesHandler;
import com.smartgwt.client.widgets.form.fields.SubmitItem;
import com.smartgwt.client.widgets.form.fields.UploadItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.game.GameClient;
import org.celstec.arlearn2.portal.client.author.ui.generic.UploadItemForm;

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
public  class ContentUploadPage {
    private UploadItemForm form;
    public void loadPage() {
        VLayout verticalGrid = new VLayout();

        form = new UploadItemForm("form1"){

            @Override
            public void onUploadCompleteEvent() {
                form.setVisibility(Visibility.HIDDEN);
            }
        };
        verticalGrid.addMember(form);
//        verticalGrid.addMember(form.getIframe());

        final UploadItem uploadItem = new UploadItem("thumbnailfile", "Thumbnail image");


        SubmitItem button = new SubmitItem("Submitimage", "Submit");
        form.setFields(uploadItem, button);

        GameClient.getInstance().getPictureUrl(5946158883012608l, new JsonCallback(){
            public void onJsonReceived(JSONValue jsonValue) {
                if (jsonValue.isObject() !=null) {
                    form.setAction(jsonValue.isObject().get("uploadUrl").isString().stringValue());
                }
            }
        });

        RootPanel.get("testContentUpload").add(verticalGrid);
        form.addEventListener();
    }

}
