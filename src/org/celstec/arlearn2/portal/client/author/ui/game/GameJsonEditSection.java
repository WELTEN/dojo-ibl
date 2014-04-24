package org.celstec.arlearn2.portal.client.author.ui.game;

import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.game.GameClient;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Game;
import org.celstec.arlearn2.portal.client.author.ui.SectionConfig;

import com.google.gwt.json.client.JSONParser;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;

public class GameJsonEditSection extends SectionConfig {

	// private DynamicForm basicMetadata;
	DynamicForm form;
	// RichTextCanvas canvas;
	private Game game;
	TextAreaItem textAreaItem;

	public GameJsonEditSection() {
		super("Edit json representation");
		DynamicForm form = new DynamicForm();
		form.setWidth100();
		form.setHeight100();
		textAreaItem = new TextAreaItem();
		textAreaItem.setShowTitle(false);
		textAreaItem.setTitle("TextArea");
		textAreaItem.setWidth("*");
		textAreaItem.setHeight("*");
		textAreaItem.setColSpan(2);
		
		
		ButtonItem saveButton = new ButtonItem("Save");
		saveButton.setTitle("Save");
		saveButton.setColSpan(2);
		saveButton.setAlign(Alignment.CENTER);
//		saveButton.setColSpan(1);
//		saveButton.setStartRow(false);
		
		saveButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				GameClient.getInstance().createGame(new Game(JSONParser.parseLenient(textAreaItem.getValueAsString()).isObject()), new JsonCallback());
			}
		});
		
		ButtonItem reindentButton = new ButtonItem("PrettyPrint");
		reindentButton.setTitle("Pretty Print");
		reindentButton.setColSpan(2);
		reindentButton.setAlign(Alignment.CENTER);
//		reindentButton.setColSpan(1);
//		reindentButton.setStartRow(false);
		
		reindentButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				
				textAreaItem.setValue(indent(textAreaItem.getValueAsString()));
			}
		});
		
		form.setFields(textAreaItem, saveButton, reindentButton);

		setItems(form);

	}

	public void loadDataFromRecord(Game game) {
		this.game = game;
		GameClient.getInstance().getGame(game.getGameId(), new JsonCallback(){
			
		});
		
		textAreaItem.setValue(indent(game.getJSON().toString()));	
		
		// basicMetadata.setValue(GameModel.GAME_TITLE_FIELD, game.getGameId());
	}

	public static native String indent(String json) /*-{
	  return JSON.stringify(eval(+'(' +json+')'), undefined, 5);
	}-*/;
}
