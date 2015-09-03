package org.celstec.arlearn2.portal.client.author.ui.game;

import java.util.ArrayList;

import org.celstec.arlearn2.gwtcommonlib.client.auth.Authentication;
import org.celstec.arlearn2.gwtcommonlib.client.auth.OauthClient;
import org.celstec.arlearn2.gwtcommonlib.client.notification.NotificationHandler;
import org.celstec.arlearn2.gwtcommonlib.client.notification.NotificationSubscriber;
import org.celstec.arlearn2.portal.client.author.AuthorConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.Progressbar;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.HeaderItem;
import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.RowSpacerItem;
import com.smartgwt.client.widgets.form.fields.UploadItem;
import com.smartgwt.client.widgets.layout.VLayout;

public class ImportGame extends Window implements NotificationHandler {
	private AuthorConstants constants = GWT.create(AuthorConstants.class);
	
	private Progressbar progressBar;
	private DynamicForm form;
	public ImportGame(){
		
		createForm();
		createProgressBar();
		
		VLayout vLayout = new VLayout(10);
		vLayout.addMember(form);
		vLayout.addMember(progressBar);
		addItem(vLayout);
		setWidth(320);
		setHeight(175);
		centerInPage();
	}
	
	private void createProgressBar() {
		progressBar = new Progressbar();
		progressBar.setVertical(false);
		progressBar.setHeight(24);
		progressBar.setWidth("*");
		progressBar.setVisibility(Visibility.HIDDEN);
	}
	
	private void createForm() {
		final UploadItem fileItem = new UploadItem("uploadRun");
		fileItem.setTitle(constants.chooseAGame());
		fileItem.setWidth(150);
		fileItem.setWrapTitle(false);


		HiddenItem authItem = new HiddenItem("auth");
		authItem.setValue("auth="+ OauthClient.checkAuthentication().getAccessToken());

		ButtonItem button = new ButtonItem("submit", constants.upload());
		// button.setStartRow(true);
		button.setWidth(80);
		button.setStartRow(false);
		button.setEndRow(false);
		button.setColSpan(4);
		button.setAlign(Alignment.CENTER);
		
		
		
		form = getForm(constants.uploadGame(), fileItem, new RowSpacerItem(),
				button, authItem);
		form.setEncoding(Encoding.MULTIPART);

		NotificationSubscriber.getInstance().addNotificationHandler("org.celstec.arlearn2.beans.notification.authoring.GameCreationStatus", this);

		button.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			public void onClick(
					com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				form.setCanSubmit(true);
				form.submit();
				progressBar.setPercentDone(0);
				progressBar.setVisibility(Visibility.INHERIT);
			}
		});

		form.setTarget("hidden_frame");
		form.setAction("/uploadGame/fileUpload.html");
	}
	
	protected DynamicForm getForm(String headerString, FormItem... fields) {
		final DynamicForm form = new DynamicForm();
//		form.setBorder("1px solid");
		form.setWidth(280);

		HeaderItem header = new HeaderItem();
		header.setDefaultValue(headerString);
		ArrayList<FormItem> list = new ArrayList<FormItem>(fields.length+1);
		list.add(header);
		for (FormItem field: fields) {
			list.add(field);
		}
		
		form.setFields(list.toArray(new FormItem[]{}));
		return form;
	}
	
	@Override
	public void onNotification(JSONObject bean) {
		int status = (int) bean.get("status").isNumber().doubleValue();

		progressBar.setPercentDone((int) (100/3*(status+1)));  
		if (status == 100) {
			progressBar.setVisibility(Visibility.HIDDEN);
			ImportGame.this.destroy();
		}

	}
}
