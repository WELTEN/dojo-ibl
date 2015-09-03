package org.celstec.arlearn2.portal.client.author.ui.gi;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONNumber;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GeneralItemModel;
import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;

import com.google.gwt.json.client.JSONString;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.util.StringUtil;
import com.smartgwt.client.widgets.RichTextEditor;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.layout.VLayout;
import org.celstec.arlearn2.portal.client.account.AccountManager;
import org.celstec.arlearn2.portal.client.author.ui.gi.i18.GeneralItemConstants;

public class BasicMetadataEditor extends VLayout {
    private static GeneralItemConstants constants = GWT.create(GeneralItemConstants.class);

    DynamicForm form = new DynamicForm();
	protected RichTextEditor richTextEditor;
	protected TextAreaItem textAreaItem;
	protected boolean showRichtText = true;
    protected TextItem titleText;

    protected TextItem orderText;
    protected TextItem section;

	public BasicMetadataEditor(boolean showTitle, boolean showDescription) {
		createTextArea();
		createRichTextArea();
		createForm();

		LayoutSpacer vSpacer = new LayoutSpacer();
		vSpacer.setWidth(10);
		addMember(vSpacer);
		
		addMember(richTextEditor);
        setHeight("*");
//        setBorder("1px dashed blue");
	}
	
	protected void createForm() {
        titleText = new TextItem(GeneralItemModel.NAME_FIELD, constants.title());
        titleText.setStartRow(true);
        orderText = new TextItem(GeneralItemModel.SORTKEY_FIELD, constants.order());
        if (AccountManager.getInstance().isAdvancedUser()) {
            section = new TextItem(GeneralItemModel.SECTION, constants.section());
        }
        if (AccountManager.getInstance().isAdvancedUser()) {
            form.setFields(titleText, orderText, section, textAreaItem);
        }   else {
		    form.setFields(titleText, orderText, textAreaItem);
        }
        form.setNumCols(4);
		form.setWidth100();
        form.setHeight(40);
		addMember(form);
	}

	protected void createTextArea() {
		textAreaItem = new TextAreaItem(GeneralItemModel.RICH_TEXT_FIELD, "TextArea");  
        textAreaItem.setShowTitle(false);
        textAreaItem.setColSpan(6);
        textAreaItem.setWidth("100%");
        textAreaItem.setHeight("*");
        textAreaItem.setShowIfCondition(formIf);
	}
	
	protected void createRichTextArea() {
		richTextEditor = new RichTextEditor();
		richTextEditor.setHeight100();

		richTextEditor.setWidth100();
		richTextEditor.setBorder("1px  grey");
		richTextEditor.setControlGroups(new String[] {
                "fontControls",
				"formatControls",
                "styleControls" });
	}
	
	public void toggleHtml() {
		showRichtText = !showRichtText;
		if (showRichtText) {
			richTextEditor.setVisibility(Visibility.INHERIT);
            richTextEditor.setHeight100();
			richTextEditor.setValue(textAreaItem.getValueAsString());
            form.setHeight(40);


        } else {
			richTextEditor.setVisibility(Visibility.HIDDEN);
            richTextEditor.setHeight(0);
			textAreaItem.setValue(richTextEditor.getValue());
            form.setHeight100();

        }
		form.redraw();
	}
	
	FormItemIfFunction formIf = new FormItemIfFunction() {
		public boolean execute(FormItem item, Object value, DynamicForm form) {
			return !showRichtText;
		}

	};
	
	public void saveToBean(GeneralItem ni) {
        try {
            ni.getJsonRep().put(GeneralItemModel.SORTKEY_FIELD, new JSONNumber(Integer.parseInt(form.getValueAsString(GeneralItemModel.SORTKEY_FIELD))));
        } catch (NumberFormatException e) {
            //eat it
        }

        ni.getJsonRep().put(GeneralItemModel.NAME_FIELD, new JSONString(form.getValueAsString(GeneralItemModel.NAME_FIELD)));
		String richText ="";
		if (showRichtText) {
			richText = richTextEditor.getValue();
		} else {
			richText = form.getValueAsString(GeneralItemModel.RICH_TEXT_FIELD); 
		}
        if (form.getValue(GeneralItemModel.SECTION) != null &&  !form.getValueAsString(GeneralItemModel.SECTION).equals("")) {
            ni.setString(GeneralItemModel.SECTION, form.getValueAsString(GeneralItemModel.SECTION));
        }
		ni.getJsonRep().put(GeneralItemModel.RICH_TEXT_FIELD, new JSONString(richText));
		ni.getJsonRep().put("description", new JSONString(StringUtil.unescapeHTML(richText)));

	}

	public void loadGeneralItem(GeneralItem gi) {
        if (AccountManager.getInstance().isAdvancedUser()) {
            form.setValue(GeneralItemModel.SECTION, gi.getString(GeneralItemModel.SECTION));
        }
        form.setValue(GeneralItemModel.SORTKEY_FIELD, gi.getInteger(GeneralItemModel.SORTKEY_FIELD));
        form.setValue(GeneralItemModel.NAME_FIELD, gi.getString(GeneralItemModel.NAME_FIELD));
		form.setValue(GeneralItemModel.RICH_TEXT_FIELD, gi.getString(GeneralItemModel.RICH_TEXT_FIELD));
		richTextEditor.setValue(gi.getString(GeneralItemModel.RICH_TEXT_FIELD));
		
	}

}
