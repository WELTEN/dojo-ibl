package org.celstec.arlearn2.portal.client;

import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.widgets.layout.VLayout;
import org.celstec.arlearn2.gwtcommonlib.client.network.AccountClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.CollaborationClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window.Location;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VStack;
import org.celstec.arlearn2.portal.client.account.AccountManager;
import org.celstec.arlearn2.portal.client.toolbar.ToolBar;

public class AddContactPage {

    ToolBar toolStrip;
	public void loadPage() {
        AccountManager accountManager = AccountManager.getInstance();
        accountManager.setAccountNotification(new AccountManager.NotifyAccountLoaded() {

            @Override
            public void accountLoaded(boolean success) {
                if (success) {
                    toolStrip = new ToolBar(false);
                    LayoutSpacer vSpacer = new LayoutSpacer();
                    vSpacer.setWidth(50);
                    vSpacer.setHeight(10);

                    VLayout vertical = new VLayout();
                    vertical.setWidth("98%");
                    vertical.setHeight("100%");
                    vertical.addMember(toolStrip);
                    vertical.addMember(vSpacer);

                    RootPanel.get("contact").add(vertical);
                    AccountClient.getInstance().accountDetails(new JsonCallback(){
                        public void onJsonReceived(JSONValue jsonValue) {
                            loadContactDetails(jsonValue.isObject());
                        }
                    });
                } else {
                    SC.say("Credentials are invalid. Log in again.");
                }
            }
        });


	}

    private void loadContactDetails(final JSONObject self) {
        final String addContactToken = Location.getParameter("id");

        CollaborationClient.getInstance().getContactDetails(addContactToken, new JsonCallback() {
            public void onJsonReceived(JSONValue jsonValue) {
                if (jsonValue.isObject().containsKey("error")) {
                    SC.say("Error", "This invitation is no longer valid");
                } else {
                    JSONObject contact = jsonValue.isObject();
                    if (self.get("localId").isString().stringValue().equals(contact.get("localId").isString().stringValue()) && self.get("accountType").isNumber().equals(contact.get("accountType").isNumber())) {
                        SC.say("You cannot add your own account as a contact. <br> Login with a different account to ARLearn to accept this invitation.");
                    } else {
                        buildPage(jsonValue.isObject(), addContactToken);
                    }

                }
            }
        });
    }
	
	public void buildPage(JSONObject contactJson, final String addContactToken) {
		
		final Window winModal = new Window();  
        winModal.setWidth(360);  
        winModal.setHeight(140);  
        winModal.setTitle("Add contact");  
        winModal.setShowMinimizeButton(false);  
        winModal.setIsModal(true);  
        winModal.setShowModalMask(true);  
        winModal.centerInPage();  
        
        winModal.addCloseClickHandler(new CloseClickHandler() {  
           

			@Override
			public void onCloseClick(CloseClickEvent event) {
				// TODO Auto-generated method stub
				
			}  
        });  

        VStack hStack = new VStack();
        hStack.setHeight(100);
        HTMLPane paneLink = new HTMLPane();
        paneLink.setHeight(75);
        String displayString = "<img style=\"float:left;margin:0 5px 0 0;\" height=\"50\" src=\""+contactJson.get("picture").isString().stringValue()+"\"/>Do you want to add "+contactJson.get("name").isString().stringValue()+ " as a contact?";
        paneLink.setContents(displayString);  
        hStack.addMember(paneLink);
        winModal.addItem(hStack);
        final IButton addContactButton = new IButton("Add Contact");    
        addContactButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				addContact(addContactToken);
			}
		});  
        final IButton ignoreButton = new IButton("Ignore");    
        ignoreButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				ignore();
			}
		});
        LayoutSpacer vSpacer = new LayoutSpacer();
		vSpacer.setWidth(5);
        HStack horStack = new HStack();
        horStack.setAlign(Alignment.CENTER);
        horStack.addMember(addContactButton);
        horStack.addMember(vSpacer);
        horStack.addMember(ignoreButton);
        horStack.setHeight(20);

        hStack.addMember(horStack);
        winModal.show();
		
	}
	
	public void addContact(final String addContactToken) {
		CollaborationClient.getInstance().confirmAddContact(addContactToken, new JsonCallback(){
			public void onJsonReceived(JSONValue jsonValue) {
				com.google.gwt.user.client.Window.open("/index.html", "_self", "");
			}
		});
	}
	
	public void ignore() {
		com.google.gwt.user.client.Window.open("/index.html", "_self", "");

	}

}
