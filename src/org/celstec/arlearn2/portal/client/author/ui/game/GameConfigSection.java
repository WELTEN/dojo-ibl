package org.celstec.arlearn2.portal.client.author.ui.game;

import com.smartgwt.client.widgets.form.fields.SelectItem;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Game;
import org.celstec.arlearn2.portal.client.author.ui.SectionConfig;
import org.celstec.arlearn2.portal.client.author.ui.game.i18.GameConstants;
import org.celstec.arlearn2.portal.client.author.ui.generic.canvas.RichTextCanvas;
import org.celstec.arlearn2.portal.client.author.ui.generic.canvas.RichTextCanvas.HtmlSaver;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import org.celstec.arlearn2.portal.client.i18.PortalConstants;

import java.util.LinkedHashMap;

public class GameConfigSection extends SectionConfig {

	private static GameConstants constants = GWT.create(GameConstants.class);
    private static PortalConstants portalConstants = GWT.create(PortalConstants.class);

    RichTextCanvas canvas;
	private Game game;
	private DynamicForm form;
    protected SelectItem languageSelectItem;

    public GameConfigSection() {
		super(constants.aboutThisGame());
		form = new DynamicForm();
		final TextItem titleText = new TextItem(GameModel.GAME_TITLE_FIELD, constants.title());
		titleText.setWidth("100%");

        languageSelectItem = new SelectItem(GameModel.LANGUAGE, "language");
        languageSelectItem.setWidth(100);
        languageSelectItem.setDefaultValue("en");

        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        valueMap.put("en", portalConstants.english());
        valueMap.put("nl", portalConstants.dutch());
        languageSelectItem.setValueMap(valueMap);
        languageSelectItem.setImageURLPrefix("flags/16/");
        languageSelectItem.setImageURLSuffix(".png");

		form.setFields(titleText,languageSelectItem);
		form.setWidth100();
	
		canvas = new RichTextCanvas("", "", new HtmlSaver() {
			
			@Override
			public void htmlReady(String html) {
				if (game != null) {
					game.setDescription(html);
					game.setString(GameModel.GAME_TITLE_FIELD, form.getValueAsString(GameModel.GAME_TITLE_FIELD));
                    game.setString(GameModel.LANGUAGE, form.getValueAsString(GameModel.LANGUAGE));
					game.writeToCloud(new JsonCallback(){
						public void onJsonReceived(JSONValue jsonValue) {
							GameConfigSection.this.setExpanded(false);
						}
					});
				}
			}
		});
		setItems(form, canvas);

	}

	public void loadDataFromRecord(Game game) {
		this.game = game;
		form.setValue(GameModel.GAME_TITLE_FIELD, game.getString(GameModel.GAME_TITLE_FIELD));
        form.setValue(GameModel.LANGUAGE, game.getString(GameModel.LANGUAGE));
        canvas.updateHtml(game.getDescription());
	}

}
