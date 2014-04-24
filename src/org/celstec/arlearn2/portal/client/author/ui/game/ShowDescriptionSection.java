package org.celstec.arlearn2.portal.client.author.ui.game;

import org.celstec.arlearn2.gwtcommonlib.client.objects.Game;
import org.celstec.arlearn2.portal.client.author.ui.SectionConfig;
import org.celstec.arlearn2.portal.client.author.ui.game.i18.GameConstants;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.VStack;

public class ShowDescriptionSection extends SectionConfig {
	private Game game;
	HTMLPane paneLink;
	private static GameConstants constants = GWT.create(GameConstants.class);

	public ShowDescriptionSection() {
		super(constants.aboutThisGame());
		paneLink = new HTMLPane();
		HStack hstack = new HStack();
		paneLink.setWidth100();
		paneLink.setHeight100();
		
		hstack.addMember(paneLink);
		hstack.setAlign(Alignment.LEFT);
		setItems(hstack);

	}

	public void loadDataFromRecord(Game game) {
		this.game = game;
		String html = "<h2>"+constants.description()+"</h2><br>";
		html += game.getDescription();
		html += "<h2>Roles</h2>";
		paneLink.setContents(html);
	}

}
