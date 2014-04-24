package org.celstec.arlearn2.portal.client.game;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Game;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.viewer.DetailViewer;
import com.smartgwt.client.widgets.viewer.DetailViewerField;
import com.smartgwt.client.widgets.viewer.DetailViewerRecord;

public class GameDisplay extends HLayout {
	
	private VLayout gameDetailAndButtons;
	private HLayout buttonLayout;
	private Game game;
	private DetailViewer gameDetailViewer;
	
	public GameDisplay(Game g) {
		this.game = g;
		setBorder("1px solid #d6d6d6");
		setWidth100();
		setHeight100();
		
		createGameDetailAndButtons();
		

		addMember(gameDetailAndButtons);
		
		DetailViewerRecord[] rec = new DetailViewerRecord[1];
		rec[0] = new DetailViewerRecord();
		rec[0].setAttribute(GameModel.GAME_TITLE_FIELD, game.getValueAsString(GameModel.GAME_TITLE_FIELD));
		rec[0].setAttribute(GameModel.GAME_DESCRIPTION_FIELD, game.getValueAsString(GameModel.GAME_DESCRIPTION_FIELD));
		gameDetailViewer.setData(rec);
		
	}
	
	public void createGameDetailAndButtons() {
		createButtons();
		createGameDetailViewer();
		gameDetailAndButtons = new VLayout();
		gameDetailAndButtons.addMember(gameDetailViewer);
		gameDetailAndButtons.addMember(buttonLayout);
		gameDetailAndButtons.setWidth("30%");
		
	}
	
	public void createButtons() {
		final IButton createRunButton = new IButton("Create Run");
		final IButton copyButton = new IButton("Copy Game");
		
		buttonLayout = new HLayout();
		buttonLayout.setAlign(Alignment.CENTER);
		buttonLayout.setLayoutMargin(6);
		buttonLayout.setMembersMargin(6);
		buttonLayout.setHeight(30);
		buttonLayout.addMember(createRunButton);
		buttonLayout.addMember(copyButton);
		
		createRunButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				
			}
		});
	}
	
	public void createGameDetailViewer() {
		gameDetailViewer = new DetailViewer();
		
		gameDetailViewer.setFields(
                new DetailViewerField(GameModel.GAME_TITLE_FIELD, "Title"),
                new DetailViewerField(GameModel.GAME_DESCRIPTION_FIELD, "Description")
				);
	}

}
