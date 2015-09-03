package org.celstec.arlearn2.portal.client.author.ui.gi.dependency.forms;


import org.celstec.arlearn2.portal.client.author.ui.gi.dependency.AdvancedDependenciesEditor;
import org.celstec.arlearn2.portal.client.author.ui.gi.dependency.nodes.ActionDependencyNode;
import org.celstec.arlearn2.portal.client.author.ui.gi.i18.GeneralItemConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;

public class DependencyEditor extends VLayout {
	
	private static GeneralItemConstants constants = GWT.create(GeneralItemConstants.class);

	protected HLayout buttonLayout;
	protected IButton advButton;

	protected AdvancedDependenciesEditor advEditor;
	protected ActionForm simpleEditor;
	protected long gameId;
	protected Tab mapTab;
	
	private DynamicForm countDownForm;
	
	public DependencyEditor(long gameId, Tab mapTab) {
		this.mapTab = mapTab;
		this.gameId = gameId;
		createAdvDepButton();
		createShowCountDownForm();
		simpleEditor = new ActionForm(false, gameId);
		simpleEditor.showActionForm();
		addMember(simpleEditor);
		addMember(countDownForm);
		createButtonLayout(advButton);
		addMember(buttonLayout);
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

	private void createAdvDepButton() {
		advButton = new IButton(constants.advanced());
		advButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				toggle(true);
			}
		});
	}
	
	private void createShowCountDownForm() {
		countDownForm = new DynamicForm();
		countDownForm.setIsGroup(true);
		countDownForm.setGroupTitle(constants.countDown());
		CheckboxItem checkBox = new CheckboxItem("showCountDown", constants.showCountDown());
		countDownForm.setFields(checkBox);
		countDownForm.setVisibility(Visibility.HIDDEN);
		
	}

	public void toggle(boolean loadDep) {
		if (advEditor == null) {
			removeMember(simpleEditor);
			JSONObject dep = simpleEditor.getJsonObject();
			simpleEditor.destroy();
			simpleEditor = null;
			advEditor = new AdvancedDependenciesEditor(gameId, mapTab);
			addMember(advEditor,0);
			advButton.setTitle(constants.simple());
			advEditor.setHeight100();
			if (loadDep && dep !=null) advEditor.loadJson(dep);
		} else {
			removeMember(advEditor);
			advEditor.destroy();
			JSONObject dep = advEditor.getJson();
			advEditor = null;
			simpleEditor = new ActionForm(false, gameId);
			simpleEditor.showActionForm();
			simpleEditor.setHeight100();
			addMember(simpleEditor,0);
			advButton.setTitle("advanced");
			if (loadDep &&  dep.get("type").isString().stringValue().equals(ActionDependencyNode.DEP_TYPE)) {
				simpleEditor.loadJson(dep);
			}
			
		}
	}

	public JSONObject getJson() {
		if (advEditor != null) {
			return advEditor.getJson();
		}
		if (simpleEditor != null) {
			return simpleEditor.getJsonObject();
		}
		return null;
	}
	
	public JSONBoolean getCountDown() {
		if (countDownForm.getVisibility() == Visibility.INHERIT) {
			return JSONBoolean.getInstance(countDownForm.getValue("showCountDown")==null?false:(Boolean)countDownForm.getValue("showCountDown"));
		}
		return null;
	}
	

	public void loadGeneralItem(JSONObject object) {
		if (!object.get("type").isString().stringValue().equals(ActionDependencyNode.DEP_TYPE)) {
			toggle(false);
		}
		if (advEditor != null) {
			advEditor.loadJson(object);
		} else {
			simpleEditor.loadJson(object);
		}
		
		
	}

	public void deselect() {
		if (advEditor != null) advEditor.deselect();
		
	}

	public void setShowCountDownOption() {
		countDownForm.setVisibility(Visibility.INHERIT);
		
	}

	public void setShowCountDown(JSONValue jsonValue) {
				countDownForm.setValue("showCountDown", jsonValue.isBoolean().booleanValue());		
	}

}
