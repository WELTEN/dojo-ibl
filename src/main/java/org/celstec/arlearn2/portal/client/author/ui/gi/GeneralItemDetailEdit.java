package org.celstec.arlearn2.portal.client.author.ui.gi;

import com.google.gwt.json.client.JSONArray;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GeneralItemDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.generalItem.GeneralItemsClient;
import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.portal.client.account.AccountManager;
import org.celstec.arlearn2.portal.client.author.ui.FileReferencesEditor;
import org.celstec.arlearn2.portal.client.author.ui.gi.dependency.forms.DependencyEditor;
import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.DataCollectionEditor;
import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.ExtensionEditor;
import org.celstec.arlearn2.portal.client.author.ui.gi.i18.GeneralItemConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.maps.gwt.client.LatLng;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;

public class GeneralItemDetailEdit extends VLayout {

	private static GeneralItemConstants constants = GWT.create(GeneralItemConstants.class);

	private GeneralItem gi;

	protected VLayout infoLayout;

	protected GeneralItemEditStack stack = new GeneralItemEditStack();
	protected HLayout buttonLayout;
	protected IButton saveButton;
	protected ExtensionEditor extensionEditor;

	protected BasicMetadataEditorPlus editor;
	protected DependencyEditor appearEdit;
	protected DependencyEditor disappearEdit;
	protected DataCollectionEditor dataCollectionEditor;
    protected FileReferencesEditor  fileReferencesEditor;
	protected Tab mapTab;

	public GeneralItemDetailEdit(Tab mapTab) {
		this.mapTab = mapTab;
		createEditButton();
		createButtonLayout(saveButton);
		createBasicMetadataEditor();


		HLayout layout = new HLayout();
		layout.addMember(stack);
        stack.setShowResizeBar(true);

        layout.addMember(editor);
		
		setAlign(Alignment.LEFT);
		// setBorder("1px dashed blue");
		
		
		addMember(layout);
		addMember(buttonLayout);
	}

	private void createEditButton() {
		saveButton = new IButton(constants.save());
		saveButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				saveClick();
			}
		});
	}

	protected boolean saveClick() {
		editor.saveToBean(gi);
		if (extensionEditor != null) {
			if  (!extensionEditor.validate()) return false;
			 
			extensionEditor.saveToBean(gi);
		}
		if (dataCollectionEditor != null) dataCollectionEditor.saveToBean(gi);
		if (appearEdit != null) {
			JSONObject appearDep = appearEdit.getJson();
			gi.getJsonRep().put("dependsOn", appearDep);
		}
		if (disappearEdit != null) {
			JSONObject disappearDep = disappearEdit.getJson();
			JSONBoolean showCountDown = disappearEdit.getCountDown();
			if (showCountDown != null) gi.getJsonRep().put("showCountDown", showCountDown);
			 gi.getJsonRep().put("disappearOn", disappearDep);
		}
        if (fileReferencesEditor != null) {
            JSONArray fileRefJson = fileReferencesEditor.getJson();
            gi.getJsonRep().put("fileReferences", fileRefJson);
        }

		GeneralItemsClient.getInstance().createGeneralItem(gi, new JsonCallback(){
			public void onJsonReceived(JSONValue jsonValue) {
				GeneralItemDataSource.getInstance().loadDataFromWeb(gi.getValueAsLong(GameModel.GAMEID_FIELD));
			}

		});
		return true;
	}

	private void createButtonLayout(IButton... buttons) {
		buttonLayout = new HLayout();
		buttonLayout.setAlign(Alignment.CENTER);
		buttonLayout.setLayoutMargin(6);
		buttonLayout.setMembersMargin(6);
		buttonLayout.setHeight(40);
		for (IButton but : buttons) {
			buttonLayout.addMember(but);
		}

	}

//	private void createSectionStack() {
//		createGeneralItemExensionMetadataSectionStack();
//
//		stack = new SectionStack();
//		stack.addSection(extensionstack);
//		SectionStackSection appearStack = new SectionStackSection("Appear");
//		appearStack.setItems(new AdvancedDependenciesEditor());
//		stack.addSection(appearStack);
//	}

	public void createBasicMetadataEditor() {
		editor = new BasicMetadataEditorPlus(true, true);
		editor.setHeight100();
	}

//	public void createGeneralItemExensionMetadataSectionStack() {
//		extensionstack = new SectionStackSection("Video Object");
//	}

	public void loadGeneralItem(GeneralItem gi) {
		this.gi = gi;
		editor.loadGeneralItem(gi);
		extensionEditor = (ExtensionEditor) gi.getMetadataExtensionEditor();
		if (gi.enableDataCollection()) {
			dataCollectionEditor = new DataCollectionEditor(gi);
			stack.setDataCollection(dataCollectionEditor);
		}
		disappearEdit = new DependencyEditor(gi.getLong(GameModel.GAMEID_FIELD), mapTab);
		if (gi.getJsonRep().containsKey("disappearOn")){
			disappearEdit.loadGeneralItem(gi.getJsonRep().get("disappearOn").isObject());
		}
		if (gi.getJsonRep().containsKey("showCountDown")){
			disappearEdit.setShowCountDown(gi.getJsonRep().get("showCountDown"));
		}
		appearEdit =  new DependencyEditor(gi.getLong(GameModel.GAMEID_FIELD), mapTab);
		if (gi.getJsonRep().containsKey("dependsOn")){
			appearEdit.loadGeneralItem(gi.getJsonRep().get("dependsOn").isObject());
		}
		if (extensionEditor != null) {
			stack.setExtensionStack(gi, extensionEditor);
		}
		stack.setAppearStack(appearEdit);
		stack.setDisappearStack(disappearEdit);
        if (AccountManager.getInstance().isAdvancedUser()) {
            fileReferencesEditor = new FileReferencesEditor();
            fileReferencesEditor.loadDataFromRecord(gi);
            stack.setFileReferenceEditor(fileReferencesEditor);
        }

		if (AccountManager.getInstance().isAdministrator()) {
			JsonEditor jsonEditor = new JsonEditor();
			jsonEditor.loadDataFromRecord(gi);
			stack.setJsonEditor(jsonEditor);
		}
		
	}

	public void coordinatesChanged(LatLng newCoordinates) {
		editor.coordinatesChanged(newCoordinates);
	}

	public void deselect() {
		if (disappearEdit != null) disappearEdit.deselect();
		if (appearEdit != null) appearEdit.deselect();
		
	}
}
