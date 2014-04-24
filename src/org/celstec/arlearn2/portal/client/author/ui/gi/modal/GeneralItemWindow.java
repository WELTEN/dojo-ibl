package org.celstec.arlearn2.portal.client.author.ui.gi.modal;

import com.google.gwt.core.client.GWT;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GeneralItemModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GeneralItemDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.generalItem.GeneralItemsClient;
import org.celstec.arlearn2.gwtcommonlib.client.objects.AudioObject;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Game;
import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.MozillaOpenBadge;
import org.celstec.arlearn2.gwtcommonlib.client.objects.MultipleChoiceImage;
import org.celstec.arlearn2.gwtcommonlib.client.objects.MultipleChoiceTest;
import org.celstec.arlearn2.gwtcommonlib.client.objects.ScanTagObject;
import org.celstec.arlearn2.gwtcommonlib.client.objects.SingleChoiceImage;
import org.celstec.arlearn2.gwtcommonlib.client.objects.SingleChoiceTest;
import org.celstec.arlearn2.gwtcommonlib.client.objects.VideoObject;
import org.celstec.arlearn2.gwtcommonlib.client.objects.YoutubeObject;
import org.celstec.arlearn2.portal.client.author.ui.gi.BasicMetadataEditor;
import org.celstec.arlearn2.portal.client.author.ui.gi.GeneralItemsTab;

import com.google.gwt.json.client.JSONValue;
import com.google.maps.gwt.client.LatLng;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import org.celstec.arlearn2.portal.client.author.ui.gi.i18.GeneralItemConstants;

public abstract class GeneralItemWindow extends Window {
	protected BasicMetadataEditor editor;
	protected IButton submitButton;
	protected IButton toggleHtml;

	protected GeneralItemsTab generalItemsTab;
	protected Game game;
	LatLng coordinate;
	protected HLayout buttonLayout;

    private static GeneralItemConstants constants = GWT.create(GeneralItemConstants.class);

	public GeneralItemWindow(GeneralItemsTab generalItemsTab, LatLng coordinate) {
		this.game = generalItemsTab.getGame();
		this.generalItemsTab = generalItemsTab;
		this.coordinate = coordinate;
		initComponent();
		setWidth(360);
		setHeight(200);
		setIsModal(true);
		setShowModalMask(true);
//		centerInPage();
		setAutoCenter(true);
	}

	public void initComponent() {
		createMetadataEditor();
		createToggleButton();
		createSubmitButton();
		
		createButtonLayout(toggleHtml, submitButton);

		addItem(editor);
		
		Canvas extensions = getMetadataExtensions();
		if (extensions != null) {
			addItem(extensions);
		}
		addItem(buttonLayout);

	}
	
	protected Canvas getMetadataExtensions() {
		return null;
	}

	private void createMetadataEditor() {
		editor = new BasicMetadataEditor(true, true);
		editor.setHeight("350");
	}

	private void createToggleButton() {
		toggleHtml = new IButton(constants.toggleHtml());
		toggleHtml.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				editor.toggleHtml();
			}
		});
	}

	private void createSubmitButton() {
		submitButton = new IButton(constants.create());
		submitButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				createClick();
			}
		});
	}

	private void createButtonLayout(IButton... buttons) {
		buttonLayout = new HLayout();
		buttonLayout.setAlign(Alignment.CENTER);
		buttonLayout.setLayoutMargin(6);
		buttonLayout.setMembersMargin(6);
		for (IButton but : buttons) {
			buttonLayout.addMember(but);
		}

	}

	protected void createClick() {
		if (!validate()) return;
		submitButton.setDisabled(true);
		
		final GeneralItem ni = createItem();
		if (coordinate != null) {
			ni.setDouble(GeneralItemModel.LAT_FIELD, coordinate.lat());
			ni.setDouble(GeneralItemModel.LNG_FIELD, coordinate.lng());
		}
		ni.setLong(GeneralItemModel.SORTKEY_FIELD, 0);
		editor.saveToBean(ni);
		ni.linkToGame(game);
		GeneralItemsClient.getInstance().createGeneralItem(ni, new JsonCallback(){
			public void onJsonReceived(JSONValue jsonValue) {
				ni.setJsonRep(jsonValue.isObject());
				GeneralItemWindow.this.destroy();
				GeneralItemDataSource.getInstance().loadDataFromWeb(game.getGameId());
				generalItemsTab.addMarker(ni);
			}
		});
		
	}
	
	protected abstract GeneralItem createItem() ;

	public static void initWindow(String valueAsString, GeneralItemsTab generalItemsTab, LatLng coordinate) {
		if ("Narrator Item".equals(valueAsString)) {
			new NarratorItemWindow(generalItemsTab, coordinate).show();
		} else if (VideoObject.HUMAN_READABLE_NAME.equals(valueAsString)) {
			new VideoObjectWindow(generalItemsTab, coordinate).show();
		} else if (YoutubeObject.HUMAN_READABLE_NAME.equals(valueAsString)) {
			new YoutubeObjectWindow(generalItemsTab, coordinate).show();
		} else if (ScanTagObject.HUMAN_READABLE_NAME.equals(valueAsString)) {
			new ScanTagObjectWindow(generalItemsTab, coordinate).show();
		} else if (SingleChoiceTest.HUMAN_READABLE_NAME.equals(valueAsString)) {
			new SingleChoiceTestWindow(generalItemsTab, coordinate).show();
		} else if (MultipleChoiceTest.HUMAN_READABLE_NAME.equals(valueAsString)) {
			new MultipleChoiceTestWindow(generalItemsTab, coordinate).show();
		} else if (AudioObject.HUMAN_READABLE_NAME.equals(valueAsString)) {
			new AudioObjectWindow(generalItemsTab, coordinate).show();
		} else if (MultipleChoiceImage.HUMAN_READABLE_NAME.equals(valueAsString)) {
			new MultipleChoiceImageWindow(generalItemsTab, coordinate).show();
		} else if (SingleChoiceImage.HUMAN_READABLE_NAME.equals(valueAsString)) {
			new SingleChoiceImageWindow(generalItemsTab, coordinate).show();
		} else if (MozillaOpenBadge.HUMAN_READABLE_NAME.equals(valueAsString)) {
			new MozillaOpenBadgeWindow(generalItemsTab, coordinate).show();
		} 
	}
	
	public boolean validate() {
		return true;
	}

}
