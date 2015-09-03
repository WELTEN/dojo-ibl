package org.celstec.arlearn2.gwtcommonlib.client.ui.modal;

import org.celstec.arlearn2.gwtcommonlib.client.AuthoringConstants;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;

public class SessionTimeout extends Window {
	
	private AuthoringConstants constants = GWT.create(AuthoringConstants.class);

	public SessionTimeout() {
		setWidth(200);
		setHeight(150);
		setTitle(constants.timeOut());
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();
		
		Label l = new Label(constants.sessionTimeout());
		l.setHeight(50);
		
		final IButton submitButton = new IButton(constants.reload());
		submitButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				com.google.gwt.user.client.Window.Location.reload();
				SessionTimeout.this.destroy();
			}
		});
		
		HLayout buttonLayout = new HLayout();
		buttonLayout.setAlign(Alignment.CENTER);
		buttonLayout.setLayoutMargin(6);
		buttonLayout.setMembersMargin(6);
		buttonLayout.addMember(submitButton);
		buttonLayout.setHeight(50);
		
		addItem(l);
		addItem(buttonLayout);
	}

}
