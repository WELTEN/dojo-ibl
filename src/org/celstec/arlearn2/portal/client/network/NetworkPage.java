package org.celstec.arlearn2.portal.client.network;

import org.celstec.arlearn2.gwtcommonlib.client.network.GenericClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.portal.client.account.AccountManager;
import org.celstec.arlearn2.portal.client.account.AccountManager.NotifyAccountLoaded;

import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.HeaderItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class NetworkPage {
	NetworkToolbar toolStrip;
//    NetworkToolbar toolStrip;
	TextItem pathTextItem;
	TextItem onBehalfOfTextItem;

	
	public void loadPage() {
		AccountManager accountManager = AccountManager.getInstance();
		accountManager.setAccountNotification(new NotifyAccountLoaded() {
			
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
		setGetPane();
		setPostPane();
		// createToolstrip();
		toolStrip = new NetworkToolbar(this);

		VLayout vertical = new VLayout();
		vertical.setWidth100();
		vertical.setHeight100();
		vertical.addMember(toolStrip);
		createNetworkPane();

		VLayout verticalGrid = new VLayout();

		verticalGrid.setLayoutAlign(Alignment.CENTER);
		verticalGrid.setAlign(Alignment.CENTER);
		verticalGrid.setDefaultLayoutAlign(Alignment.CENTER);

		verticalGrid.setWidth100();
		verticalGrid.setHeight("*");
		verticalGrid.addMember(networkLayout);

		vertical.addMember(verticalGrid);
		RootPanel.get("network").add(vertical);

	}

	VLayout networkLayout = new VLayout();

	private void createNetworkPane() {

		final DynamicForm selectForm = new DynamicForm();
		selectForm.setNumCols(8);

		HeaderItem header = new HeaderItem();
		header.setDefaultValue("Web service selection");

		pathTextItem = new TextItem();
		pathTextItem.setName("path");
		pathTextItem.setTitle("http://" + Window.Location.getHostName()
				+ "/rest/");
		if (Window.Location.getParameter("path") != null) pathTextItem.setValue(Window.Location.getParameter("path"));

		ButtonItem getButton = new ButtonItem();
		getButton.setTitle("GET");
		getButton.setStartRow(false);
		getButton.setEndRow(false);
		getButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String value = (String) selectForm.getValue("path");
				PathClient pc = new PathClient();
				
				if (onBehalfOfTextItem.getValueAsString() != null && !onBehalfOfTextItem.getValueAsString().equals("")){
					String onBehalfOfString = onBehalfOfTextItem.getValueAsString();
					if (onBehalfOfString.contains(":")) {
						pc.getPath(value, onBehalfOfTextItem.getValueAsString());
					} else {
						pc.getPath(value, onBehalfOfTextItem.getValueAsString()+":"+AccountManager.getInstance().getAccount().getAccountType()+":"+AccountManager.getInstance().getAccount().getLocalId());
					}
				} else {
					pc.getPath(value);	
				}

			}
		});

		ButtonItem post = new ButtonItem();
		post.setTitle("POST");
		post.setStartRow(false);
		post.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String value = (String) selectForm.getValue("path");
				PathClient pc = new PathClient();
				
				if (onBehalfOfTextItem.getValueAsString() != null && !onBehalfOfTextItem.getValueAsString().equals("") && AccountManager.getInstance().getAccount() != null){
					String onBehalfOfString = onBehalfOfTextItem.getValueAsString();
					if (onBehalfOfString.lastIndexOf(":")>15) {
						pc.postPath(value, postForm.getValueAsString("toMessage"), onBehalfOfTextItem.getValueAsString());
					} else {
						pc.postPath(value, postForm.getValueAsString("toMessage"),  onBehalfOfTextItem.getValueAsString()+":"+AccountManager.getInstance().getAccount().getAccountType()+":"+AccountManager.getInstance().getAccount().getLocalId());
					}
				} else {
					pc.postPath(value, postForm.getValueAsString("toMessage"));

				}
			}
		});
		
		onBehalfOfTextItem = new TextItem("onBehalfOf", "onBehalfOf Token");

		selectForm.setFields(header, pathTextItem, getButton, post, onBehalfOfTextItem);
		networkLayout.addMember(selectForm);
		HLayout panes = new HLayout();
		panes.addMember(postForm);
		panes.addMember(getForm);

		networkLayout.addMember(panes);

	}

	private DynamicForm postForm;
	private DynamicForm getForm;

	private void setGetPane() {
		getForm = new DynamicForm();

		getForm.setIsGroup(true);
		getForm.setGroupTitle("Message from server");
		getForm.setWidth100();
		getForm.setHeight100();
		getForm.setNumCols(2);
		getForm.setColWidths(60, "*");
		// form.setBorder("1px solid blue");
		getForm.setPadding(5);
		getForm.setCanDragResize(true);
		getForm.setResizeFrom("R");

		TextAreaItem messageItem = new TextAreaItem("fromMessage");
		messageItem.setShowTitle(false);
		messageItem.setLength(500000);
		messageItem.setColSpan(2);
		messageItem.setWidth("*");
		messageItem.setHeight("*");

		getForm.setFields(messageItem);

		getForm.draw();

	}

	private void setPostPane() {
		postForm = new DynamicForm();

		postForm.setIsGroup(true);
		postForm.setGroupTitle("Message to server");
		postForm.setWidth100();
		postForm.setHeight100();
		postForm.setNumCols(2);
		postForm.setColWidths(60, "*");
		// form.setBorder("1px solid blue");
		postForm.setPadding(5);
		postForm.setCanDragResize(true);
		postForm.setResizeFrom("R");

		TextAreaItem messageItem = new TextAreaItem("toMessage");
		messageItem.setShowTitle(false);
		messageItem.setLength(500000);
		messageItem.setColSpan(2);
		messageItem.setWidth("*");
		messageItem.setHeight("*");

		postForm.setFields(messageItem);

		postForm.draw();

	}

	public class PathClient extends GenericClient {

		JsonCallback dummyCb = new JsonCallback() {
			public void onJsonReceived(JSONValue jsonValue) {
				getForm.setValue("fromMessage", indent(jsonValue.toString()));
			}

			public void onError() {

			}

		};

		public void getPath(String path) {
			invokeJsonGET(path, dummyCb);
		}

		public void getPath(String path, String onBehalfOf) {
			invokeJsonGET(path, onBehalfOf, dummyCb);
			
		}

		public void getPath(String path, JsonCallback jc) {
			invokeJsonGET(path, jc);
		}

		public void postPath(String path, String postMessage) {
			invokeJsonPOST(path, postMessage, dummyCb);
		}
		
		public void postPath(String path, String postMessage, String onBehalfOf) {
			invokeJsonPOST(path, postMessage, onBehalfOf, dummyCb);
		}

		public String getUrl() {
			return super.getUrl() + "";
		}

	}

	public void pastePath(String string) {
		pathTextItem.setValue(string);
	}
	
	public void pastePost(String postMessage) {
		postForm.setValue("toMessage", indent(postMessage));
	}

	public static native String indent(String json) /*-{
		return JSON.stringify(eval(+'(' + json + ')'), undefined, 5);
	}-*/;
}
