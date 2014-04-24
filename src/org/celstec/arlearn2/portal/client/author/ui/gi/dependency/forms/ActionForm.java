package org.celstec.arlearn2.portal.client.author.ui.gi.dependency.forms;

import java.util.LinkedHashMap;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameRoleModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GeneralItemModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GameRolesDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GeneralItemDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.objects.*;
import org.celstec.arlearn2.portal.client.author.ui.gi.GeneralItemsTab;
import org.celstec.arlearn2.portal.client.author.ui.gi.dependency.nodes.ActionDependencyNode;
import org.celstec.arlearn2.portal.client.author.ui.gi.i18.GeneralItemConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeNode;

public class ActionForm extends DynamicForm {
	private static GeneralItemConstants constants = GWT.create(GeneralItemConstants.class);

	protected CheckboxItem hasDependencyCheckboxItem;
	protected SelectItem selectAction;
	protected TextItem selectActionString;
	protected SelectItem selectGeneralItem;
	protected SelectItem selectRole;
	protected SelectItem selectScope;
	private boolean scantagStringAppear = false;
	private boolean hasDependency = true;
    private boolean showHasDependencyCheckBox = true;
	private boolean saveToTree = false;
	

	private final String ACTION_DEP = "action";
	private final String ACTION_DEP_STRING = "actionString";
	private final String GENITEM_DEP = "generalItemId";
	private final String SCOPE_DEP = "scope";
	private final String ROLE_DEP = "role";

	private TreeNode actionTreeNode;
	private Tree actionTree;
	private long gameId;

	public ActionForm(boolean withSaveButton, long gameId) {
		this.gameId = gameId;
		setGroupTitle(constants.actionBasedDependency());
		setIsGroup(true);
		initHasDependency();
		initGeneralItem();
		initAction();
		initScope();
		initRole();
		

		if (withSaveButton) {
			saveToTree = true;
		} else {
			hasDependency = false;
			hasDependencyCheckboxItem.setValue(false);
			
		}
		setFields(hasDependencyCheckboxItem, selectGeneralItem, selectAction, selectActionString, selectScope, selectRole);
		redraw();
		ActionForm.this.setVisibility(Visibility.HIDDEN);
	}

	public void onSave() {
		if (actionTreeNode == null)
			return;
		for (TreeNode tn : actionTree.getChildren(actionTreeNode)) {
			if (tn.getAttribute("type").equals(ActionDependencyNode.ACTION)) {
				tn.setAttribute("Name", constants.action()+" = " + getValueAsString(ACTION_DEP));
				if (scantagStringAppear) {
					tn.setAttribute("Value", getValueAsString(ACTION_DEP_STRING));
				} else {
					tn.setAttribute("Value", getValueAsString(ACTION_DEP));
				}
			}
			if (tn.getAttribute("type").equals(ActionDependencyNode.GENERALITEM)) {
				tn.setAttribute("Name", constants.itemId()+" = " + getValueAsString(GENITEM_DEP));
				tn.setAttribute("Value", getValueAsString(GENITEM_DEP));
			}
			if (tn.getAttribute("type").equals(ActionDependencyNode.SCOPE)) {
				tn.setAttribute("Name", constants.scope()+" = " + getValueAsString(SCOPE_DEP));
				tn.setAttribute("Value", getValueAsString(SCOPE_DEP));
			}
			if (tn.getAttribute("type").equals(ActionDependencyNode.ROLE)) {
				tn.setAttribute("Name", constants.role() +" = " + getValueAsString(ROLE_DEP));
				tn.setAttribute("Value", getValueAsString(ROLE_DEP));
			}
		}

//		actionTreeNode = null;
	}

	public void setTreeNode(TreeNode tn, Tree tree) {
        showHasDependencyCheckBox = false;
		actionTreeNode = tn;
		actionTree = tree;
		for (TreeNode node : actionTree.getChildren(actionTreeNode)) {
			if (node.getAttribute("type").equals(ActionDependencyNode.ACTION)) {
				setValue(ACTION_DEP, node.getAttribute("Value"));
                setValue(ACTION_DEP_STRING, node.getAttribute("Value"));

            }
			if (node.getAttribute("type").equals(ActionDependencyNode.GENERALITEM)) {
				setValue(GENITEM_DEP, node.getAttribute("Value"));
				if (node.getAttribute("Value")  != null) {
					loadGeneralItemOptions(Long.parseLong(node.getAttribute("Value"))) ;
				}
				
			}
			if (node.getAttribute("type").equals(ActionDependencyNode.SCOPE)) {
				setValue(SCOPE_DEP, node.getAttribute("Value"));
			}
			if (node.getAttribute("type").equals(ActionDependencyNode.ROLE)) {
				setValue(ROLE_DEP, node.getAttribute("Value"));
			}

		}
		redraw();

	}

	private void initHasDependency() {
		hasDependencyCheckboxItem = new CheckboxItem("hasDep", constants.hasDependency());
		hasDependencyCheckboxItem.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				hasDependency = (Boolean) event.getValue();
				redraw();
				
			}
		});
        hasDependencyCheckboxItem.setShowIfCondition(new FormItemIfFunction() {
            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return showHasDependencyCheckBox;
            }
        });
	}
	
	private void initAction() {
		selectAction = new SelectItem(ACTION_DEP);
		selectAction.setTitle(constants.action());
		selectAction.setValueMap(createSimpleDependencyValues());
		selectAction.setWrapTitle(false);
		 selectAction.setShowIfCondition(actionFixedString);
		selectAction.setStartRow(true);
		selectAction.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				if (saveToTree) onSave();

			}
		});
		
		selectActionString = new TextItem(ACTION_DEP_STRING);
		selectActionString.setTitle(constants.action());
		selectActionString.setWrapTitle(false);
		selectActionString.setShowIfCondition(actionFreeString);
		selectActionString.setStartRow(true);
		selectActionString.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				if (saveToTree) onSave();

				
			}
		});
	}

	private void initScope() {
		selectScope = new SelectItem(SCOPE_DEP);
		selectScope.setTitle(constants.scope());
		selectScope.setValueMap(createScopeDependencyValues());
		if (selectScope.getValue() == null)
			selectScope.setValue(0);
		selectScope.setShowIfCondition(hasDependencyFuction);

		// selectScope.setShowIfCondition(formIf);
		selectScope.setStartRow(true);
		selectScope.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				if (saveToTree) onSave();
				
			}
		});
	}

	private void initRole() {

		selectRole = new SelectItem(ROLE_DEP);
		selectRole.setTitle(constants.role());
		selectRole.setValueField(GameRoleModel.ROLE_FIELD);
		selectRole.setDisplayField(GameRoleModel.ROLE_FIELD);
		selectRole.setOptionDataSource(GameRolesDataSource.getInstance());
		selectRole.setAllowEmptyValue(true);
		selectRole.setShowIfCondition(hasDependencyFuction);

		selectRole.setStartRow(true);
		selectRole.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				if (saveToTree) onSave();
				
			}
		});
	}

	private void initGeneralItem() {
		selectGeneralItem = new SelectItem(GENITEM_DEP);
		selectGeneralItem.setTitle(constants.item());
		selectGeneralItem.setWrapTitle(false);
		selectGeneralItem.setDisplayField(GeneralItemModel.NAME_FIELD);
		Criteria crit = new Criteria();
		crit.addCriteria("deleted", false);
		
		crit.addCriteria(GameModel.GAMEID_FIELD, gameId);
		selectGeneralItem.setPickListCriteria(crit);
		selectGeneralItem.setValueField("id");
		selectGeneralItem.setOptionDataSource(GeneralItemDataSource.getInstance());
		selectGeneralItem.setShowIfCondition(hasDependencyFuction);
		selectGeneralItem.setStartRow(true);
		selectGeneralItem.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				String id = selectGeneralItem.getValueAsString();
				long recId = Long.parseLong(id);
				
				loadGeneralItemOptions(recId);
				if (saveToTree) onSave();
				
			}
		});
	}
	
	private void loadGeneralItemOptions(long recId) {
		com.smartgwt.client.data.Record rec = GeneralItemDataSource.getInstance().getRecord((long)recId);
        if (rec != null) {
		GeneralItem gi = GeneralItemsTab.recordToGeneralItem(rec);
		scantagStringAppear = false;	
		if (gi instanceof SingleChoiceTest || gi instanceof MultipleChoiceTest) {
			LinkedHashMap<String, String> map = createSimpleDependencyValues();
			for (MultipleChoiceAnswer answer :((SingleChoiceTest) gi).getAnswers()) {
				map.put("answer_"+answer.getString(MultipleChoiceAnswer.ID), answer.getString(MultipleChoiceAnswer.ANSWER));
                map.put("answer_correct", constants.correctAnswer());
                map.put("answer_wrong", constants.wrongAnswer());
			}
			selectAction.setValueMap(map);
		}
        if (gi instanceof AudioObject) {
            LinkedHashMap<String, String> map = createSimpleDependencyValues();
            map.put("complete", constants.completePlaying());
            selectAction.setValueMap(map);
        }
		if (gi instanceof ScanTagObject) {
			scantagStringAppear = true;	
			
		}
		redraw();
        }
	}

	public void showActionForm() {
		ActionForm.this.setVisibility(Visibility.INHERIT);

	}

	public void hideActionForm() {
		ActionForm.this.setVisibility(Visibility.HIDDEN);

	}

	public LinkedHashMap<String, String> createSimpleDependencyValues() {
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
		valueMap.put("read", constants.read()); 
		valueMap.put("answer_given", constants.answerGiven()); // TODO
		return valueMap;
	}

	public LinkedHashMap<String, String> createScopeDependencyValues() {
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
		valueMap.put("0", constants.user());
		valueMap.put("1", constants.team());
		valueMap.put("2", constants.all());
		return valueMap;
	}

	public void loadJson(JSONObject object) {
		hasDependency = true;
        showHasDependencyCheckBox = true;
		hasDependencyCheckboxItem.setValue(true);
		if (object.containsKey("generalItemId")) {
			loadGeneralItemOptions((long) object.get("generalItemId").isNumber().doubleValue());	
		}
		
		if (object.containsKey("action")){
			setValue(ACTION_DEP, object.get("action").isString().stringValue());
			setValue(ACTION_DEP_STRING, object.get("action").isString().stringValue());
		}
		if (object.containsKey("generalItemId")){
			setValue(GENITEM_DEP, (long) object.get("generalItemId").isNumber().doubleValue());

		}
        if (object.containsKey("scope")) {
            setValue(SCOPE_DEP, ""+(int)object.get("scope").isNumber().doubleValue());
        }
	}
	
	public JSONObject getJsonObject(){
		if (!(Boolean)getValue("hasDep")) return null;
		JSONObject dep = new JSONObject();
		dep.put("type", new JSONString(ActionDependencyNode.DEP_TYPE));
		if (scantagStringAppear) {
			if (getValue(ACTION_DEP_STRING)!=null) dep.put("action", new JSONString(getValueAsString(ACTION_DEP_STRING)));
		} else {
			if (getValue(ACTION_DEP)!=null) dep.put("action", new JSONString(getValueAsString(ACTION_DEP)));	
		}
		
		if (getValue(GENITEM_DEP)!=null) dep.put("generalItemId", new JSONNumber(Long.parseLong(getValueAsString(GENITEM_DEP))));
//		if (getValue(GENITEM_DEP)!=null) dep.put("generalItemId", new JSONNumber((Long)getValue(GENITEM_DEP)));

		if (getValue(SCOPE_DEP)!=null) dep.put("scope", new JSONNumber(Integer.parseInt(getValueAsString(SCOPE_DEP))));
		if (getValue(ROLE_DEP)!=null) dep.put("role", new JSONString(getValueAsString(ROLE_DEP)));
		return dep;
	}
	
	FormItemIfFunction actionFreeString = new FormItemIfFunction() {
		public boolean execute(FormItem item, Object value, DynamicForm form) {
	
			return hasDependency&&scantagStringAppear;
		}

	};
	FormItemIfFunction actionFixedString = new FormItemIfFunction() {
		public boolean execute(FormItem item, Object value, DynamicForm form) {
			return hasDependency&& !scantagStringAppear;
		}

	};
	
	FormItemIfFunction hasDependencyFuction = new FormItemIfFunction() {
		public boolean execute(FormItem item, Object value, DynamicForm form) {
			return hasDependency;
		}

	};
}
