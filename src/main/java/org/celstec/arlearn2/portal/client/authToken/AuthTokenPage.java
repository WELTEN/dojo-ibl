/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 *
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors: Stefaan Ternier
 ******************************************************************************/
package org.celstec.arlearn2.portal.client.authToken;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.VLayout;
import org.celstec.arlearn2.gwtcommonlib.client.auth.OauthClient;
import org.celstec.arlearn2.portal.client.account.AccountManager;
public class AuthTokenPage {

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

    public void buildUi() {
        VLayout vertical = new VLayout();
        vertical.setWidth100();
        vertical.setHeight100();


        TextItem textItem = new TextItem("Token", "Token");
        textItem.setValue(OauthClient.readFromCookie().getAccessToken());
        DynamicForm form = new DynamicForm();
        form.setWidth100();
        form.setFields(textItem);

        vertical.addMember(form);
        RootPanel.get("authToken").add(vertical);
    }
}
