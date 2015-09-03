package org.celstec.arlearn2.portal.client.author;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.RunDataSource;
import org.celstec.arlearn2.portal.client.account.AccountManager;
import org.celstec.arlearn2.portal.client.account.AccountManager.NotifyAccountLoaded;
import org.celstec.arlearn2.portal.client.author.ui.game.GamesTab;
import org.celstec.arlearn2.portal.client.author.ui.run.RunsTab;
import org.celstec.arlearn2.portal.client.author.ui.tabs.TabManager;

import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;

public class AuthorPage {

	AuthorToolBar toolStrip;
	
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
	
	private void buildUi() {
		toolStrip = new AuthorToolBar(this);


		TabManager tabManager = TabManager.getInstance();
		tabManager.addTab(new GamesTab(tabManager));
		tabManager.addTab(new RunsTab());
		LayoutSpacer vSpacer = new LayoutSpacer();
		vSpacer.setWidth(50);
		vSpacer.setHeight(10);
		
		VLayout vertical = new VLayout();
		vertical.setWidth("98%");
		vertical.setHeight("100%");
		vertical.addMember(toolStrip);
		vertical.addMember(vSpacer);
		vertical.addMember(tabManager.getDrawableWidget());
		
		RootPanel.get("author").add(vertical);

		RunDataSource.getInstance().loadDataFromWeb();
	}

}
