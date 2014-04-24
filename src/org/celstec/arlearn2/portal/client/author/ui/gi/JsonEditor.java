package org.celstec.arlearn2.portal.client.author.ui.gi;

import org.celstec.arlearn2.gwtcommonlib.client.network.generalItem.GeneralItemsClient;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GeneralItemModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GeneralItemDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.portal.client.author.ui.SectionConfig;

import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;

public class JsonEditor extends SectionConfig {

	// private DynamicForm basicMetadata;
	DynamicForm form;
	// RichTextCanvas canvas;
	private GeneralItem gi;
	TextAreaItem textAreaItem;

	public JsonEditor() {
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

		
		saveButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				gi.setJsonRep(JSONParser.parseLenient(textAreaItem.getValueAsString()).isObject());
				GeneralItemsClient.getInstance().createGeneralItem(gi, new JsonCallback() {
					public void onJsonReceived(JSONValue jsonValue) {
						GeneralItemDataSource.getInstance().loadDataFromWeb(gi.getLong(GameModel.GAMEID_FIELD));
					}

				});
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

	public void loadDataFromRecord(GeneralItem gi) {
		this.gi = gi;
		
		GeneralItemsClient.getInstance().getGeneralItem(
				gi.getLong(GameModel.GAMEID_FIELD), 
				gi.getLong(GeneralItemModel.ID_FIELD), 
				new JsonCallback(){
			
		});
		
		textAreaItem.setValue(indent(gi.getJsonRep().toString()));	
		
		// basicMetadata.setValue(GameModel.GAME_TITLE_FIELD, game.getGameId());
	}

	public static native String indent(String json) /*-{
	  return JSON.stringify(eval(+'(' +json+')'), undefined, 5);
	}-*/;
}
