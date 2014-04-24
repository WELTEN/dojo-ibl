package org.celstec.arlearn2.portal.client.author.ui.gi.dependency.forms;

import org.celstec.arlearn2.portal.client.author.ui.gi.dependency.nodes.TimeDependencyNode;

import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeNode;

public class TimeDepEditor extends DynamicForm {

	protected TextItem selectTimeTextItem;

	private final String TIMEDELTA_DEP = "timeDelta";
	
	private TreeNode actionTreeNode;
	private Tree actionTree;

	public TimeDepEditor() {
		setGroupTitle("Action based Dependency");
		setIsGroup(true);
		initSetTimeField();
		setFields(selectTimeTextItem);
		redraw();
		TimeDepEditor.this.setVisibility(Visibility.HIDDEN);
	}
	
	private void initSetTimeField() {
		selectTimeTextItem = new TextItem(TIMEDELTA_DEP);
		selectTimeTextItem.setTitle("time delta");
		selectTimeTextItem.setWrapTitle(false);
		selectTimeTextItem.setStartRow(true);
		selectTimeTextItem.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				onSave();

				
			}
		});
		
	}
	public void onSave() {
		if (actionTreeNode == null)
			return;
		for (TreeNode tn : actionTree.getChildren(actionTreeNode)) {
			if (tn.getAttribute("type")!= null && tn.getAttribute("type").equals(TimeDependencyNode.TIMEDELTA)) {
				tn.setAttribute("Name", "timeDelta = " + getValueAsString(TIMEDELTA_DEP));
				tn.setAttribute("Value", getValueAsString(TIMEDELTA_DEP));
			}
		}
		
	}
	
	public void setTreeNode(TreeNode tn, Tree tree) {
		actionTreeNode = tn;
		actionTree = tree;
		for (TreeNode node : actionTree.getChildren(actionTreeNode)) {
			if (node.getAttribute("type")!= null && node.getAttribute("type").equals(TimeDependencyNode.TIMEDELTA)) {
				setValue(TIMEDELTA_DEP, node.getAttribute("Value"));	
			}
		}
		redraw();
	}

	public void showForm() {
		setVisibility(Visibility.INHERIT);

	}

	public void hideForm() {
		setVisibility(Visibility.HIDDEN);

	}
}
