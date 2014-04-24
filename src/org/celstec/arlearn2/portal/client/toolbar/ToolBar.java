package org.celstec.arlearn2.portal.client.toolbar;

import java.util.LinkedHashMap;

import org.celstec.arlearn2.gwtcommonlib.client.LocalSettings;
import org.celstec.arlearn2.gwtcommonlib.client.auth.OauthClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.AccountClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Account;
import org.celstec.arlearn2.portal.client.account.AccountManager;
import org.celstec.arlearn2.portal.client.i18.PortalConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class ToolBar extends ToolStrip{
	private static PortalConstants constants = GWT.create(PortalConstants.class);

    protected ToolStripButton homeButton;
	protected ToolStripButton profileButton;
	protected SelectItem languageSelectItem;
	
	
	protected boolean i18 = false;
//	public ToolBar(){
//
//		createProfileButton();
//        createHomeButton();
//		createLanguageButton();
//
//	}
	
	public ToolBar(boolean i18){
		this.i18 = i18;
		if (i18) createLanguageButton();
        createHomeButton();
		createProfileButton();
	}
	
	private void createLanguageButton() {
		
		String localeTemp = Window.Location.getParameter("locale");
		if (localeTemp == null) localeTemp = "en";
		final String locale = localeTemp;
		
		languageSelectItem = new SelectItem();  
        languageSelectItem.setName("language");  
        languageSelectItem.setShowTitle(false);  
        languageSelectItem.setWidth(100);    
        languageSelectItem.setDefaultValue(locale);  
  
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put("en", constants.english());
        valueMap.put("nl", constants.dutch());
        languageSelectItem.setValueMap(valueMap);
        languageSelectItem.setImageURLPrefix("flags/16/");
        languageSelectItem.setImageURLSuffix(".png");

        LinkedHashMap<String, String> valueIcons = new LinkedHashMap<String, String>();
        valueIcons.put("en", "UK");
        valueIcons.put("nl", "NL");
        languageSelectItem.setValueIcons(valueIcons);
        languageSelectItem.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				if (!event.getValue().equals(locale)) {
					String url = Window.Location.getHref();
					if (url.contains("?"))
						url = url.substring(0, url.indexOf("?"));
					String newUrl = url + "?locale=" + event.getValue();
					Window.open(newUrl, "_self", "");
					LocalSettings.getInstance().setLocale("" + event.getValue());
				}

			}
		});
        
		
	}

    private void createHomeButton() {
        homeButton = new ToolStripButton();
        if (AccountManager.getInstance().getAccount()!= null) {
            homeButton.setIcon("home.png");
//            profileButton.setTitle(AccountManager.getInstance().getAccount().getName());
        }
        homeButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                            Window.open("/index.html", "_self", "");


            }
        });
    }

	private void createProfileButton() {
		profileButton = new ToolStripButton();  
		if (AccountManager.getInstance().getAccount()!= null) {
		profileButton.setIcon(AccountManager.getInstance().getAccount().getPicture());  
        profileButton.setTitle(AccountManager.getInstance().getAccount().getName());
		}
        loadButtons();
		profileButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				SC.ask("Logout?", new BooleanCallback() {
					
					@Override
					public void execute(Boolean value) {
						if (value) {
							OauthClient.disAuthenticate();
							Window.open("/oauth.html", "_self", "");
						}
						
					}
				});
				
			}
		});
	}
	
	protected void loadButtons() {
		addButtons();
		setWidth100();
		addFill();

        if (homeButton != null) {
            addButton(homeButton);
        }
		if (i18)  addFormItem(languageSelectItem);

        addButton(profileButton);
	}
	
	protected void addButtons() {
		
	}
}
