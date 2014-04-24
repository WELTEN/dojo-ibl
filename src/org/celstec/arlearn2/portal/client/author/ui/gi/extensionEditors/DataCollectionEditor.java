package org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors;

import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.portal.client.AuthoringConstants;
import org.celstec.arlearn2.portal.client.author.ui.gi.i18.GeneralItemConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public class DataCollectionEditor extends VLayout implements ExtensionEditor{
	private static GeneralItemConstants constants = GWT.create(GeneralItemConstants.class);

	protected DynamicForm form;
	private CheckboxItem openQuestionCBItem ;
	private CheckboxItem openQuestionWithImageCBItem ;
	private CheckboxItem openQuestionWithVideoCBItem ;
	private CheckboxItem openQuestionWithAudioCBItem ;
	private CheckboxItem openQuestionWithTextCBItem ;
	private TextItem openQuestionTextDescItem;
	private CheckboxItem openQuestionWithValueCBItem ;
	private TextItem openQuestionValueDescItem;
	
	private final static String OPENQUESTIONWITHAUTDIO = "openQuestionWithAudio";
	private final static String OPENQUESTIONWITHTEXT = "openQuestionWithText";
	private final static String OPENQUESTIONWITHVALUE = "openQuestionWithValue";
	private final static String OPENQUESTIONVALUEDESC = "valueDescription";
	private final static String OPENQUESTIONTEXTDESC = "textDescription";
	
	public DataCollectionEditor() {
		createIsOpenQuestion();
		createOpenQuestionWithImage();
		createOpenQuestionWithVideo();
		createOpenQuestionWithAudio();
		createOpenQuestionWithText();
		createOpenQuestionWithValue();
		form = new DynamicForm();
		form.setNumCols(4);
		form.setWidth100();
		form.setFields(openQuestionCBItem, openQuestionWithImageCBItem, openQuestionWithVideoCBItem, openQuestionWithAudioCBItem, openQuestionWithTextCBItem, openQuestionTextDescItem, openQuestionWithValueCBItem, openQuestionValueDescItem);
		addMember(form);
	}
	
	private void createIsOpenQuestion() {
		openQuestionCBItem = new CheckboxItem();
		openQuestionCBItem.setName("isOpenQuestion");
		openQuestionCBItem.setTitle(constants.enableDataCollection());
		openQuestionCBItem.setRedrawOnChange(true);
		openQuestionCBItem.setEndRow(true);
		openQuestionCBItem.setStartRow(true);
	}
	
	private void createOpenQuestionWithImage() {
		openQuestionWithImageCBItem = new CheckboxItem();
		openQuestionWithImageCBItem.setName("openQuestionWithImage");
		openQuestionWithImageCBItem.setTitle(constants.answerWithPicture());
		openQuestionWithImageCBItem.setShowIfCondition(new FormItemIfFunction() {  
            public boolean execute(FormItem item, Object value, DynamicForm form) {  
            	if (form.getValue("isOpenQuestion") == null) return false;
                return form.getValue("isOpenQuestion").equals(Boolean.TRUE);  
            }

        });  
//		openQuestionWithImageCBItem.addChangedHandler(new ChangedHandler() {
//
//			@Override
//			public void onChanged(ChangedEvent event) {
//				if ((Boolean)form.getValue("openQuestionWithImage")) form.setValue("openQuestionWithVideo", false);
//			}
//		});
		openQuestionWithImageCBItem.setStartRow(true);
	}
	
	private void createOpenQuestionWithVideo() {
		openQuestionWithVideoCBItem = new CheckboxItem();
		openQuestionWithVideoCBItem.setName("openQuestionWithVideo");
		openQuestionWithVideoCBItem.setTitle(constants.answerWithVideo());
		openQuestionWithVideoCBItem.setShowIfCondition(new FormItemIfFunction() {  
            public boolean execute(FormItem item, Object value, DynamicForm form) {  
            	if (form.getValue("isOpenQuestion") == null) return false;
                return form.getValue("isOpenQuestion").equals(Boolean.TRUE);  
            }

        });  
//		openQuestionWithVideoCBItem.addChangedHandler(new ChangedHandler() {
//
//			@Override
//			public void onChanged(ChangedEvent event) {
//				if ((Boolean)form.getValue("openQuestionWithVideo")) form.setValue("openQuestionWithImage", false);
//			}
//		});
		openQuestionWithVideoCBItem.setStartRow(true);

	}
	
	private void createOpenQuestionWithAudio() {
		openQuestionWithAudioCBItem = new CheckboxItem();
		openQuestionWithAudioCBItem.setName(OPENQUESTIONWITHAUTDIO);
		openQuestionWithAudioCBItem.setTitle(constants.answerWithAudio());
		openQuestionWithAudioCBItem.setShowIfCondition(new FormItemIfFunction() {  
            public boolean execute(FormItem item, Object value, DynamicForm form) {
            	if (form.getValue("isOpenQuestion") == null) return false;
                return form.getValue("isOpenQuestion").equals(Boolean.TRUE);  
            }

        });
//		openQuestionWithAudioCBItem.addChangedHandler(new ChangedHandler() {
//
//			@Override
//			public void onChanged(ChangedEvent event) {
//				if (form.getValue(OPENQUESTIONWITHTEXT) == null) form.setValue(OPENQUESTIONWITHTEXT, false);
//
//				if ((Boolean)form.getValue(OPENQUESTIONWITHTEXT)) form.setValue(OPENQUESTIONWITHTEXT, false);
//			}
//		});
		openQuestionWithAudioCBItem.setStartRow(true);

	}
	
	private void createOpenQuestionWithText() {
		openQuestionWithTextCBItem = new CheckboxItem();
		openQuestionWithTextCBItem.setName(OPENQUESTIONWITHTEXT);
		openQuestionWithTextCBItem.setTitle(constants.answerWithText());
		openQuestionWithTextCBItem.setEndRow(false);
		openQuestionWithTextCBItem.setStartRow(true);
		openQuestionWithTextCBItem.setRedrawOnChange(true);

		openQuestionWithTextCBItem.setShowIfCondition(new FormItemIfFunction() {  
            public boolean execute(FormItem item, Object value, DynamicForm form) {
            	if (form.getValue("isOpenQuestion") == null) return false;
                return form.getValue("isOpenQuestion").equals(Boolean.TRUE);  
            }

        });  
//		openQuestionWithTextCBItem.addChangedHandler(new ChangedHandler() {
//
//			@Override
//			public void onChanged(ChangedEvent event) {
//				if (form.getValue(OPENQUESTIONWITHAUTDIO) == null) form.setValue(OPENQUESTIONWITHAUTDIO, false);
//				if ((Boolean)form.getValue(OPENQUESTIONWITHAUTDIO)) form.setValue(OPENQUESTIONWITHAUTDIO, false);
//			}
//		});
		
		openQuestionTextDescItem = new TextItem(OPENQUESTIONTEXTDESC, "Message");
		openQuestionTextDescItem.setShowTitle(true);
		openQuestionTextDescItem.setEndRow(true);
		openQuestionTextDescItem.setStartRow(false);
		openQuestionTextDescItem.setShowIfCondition(new FormItemIfFunction() {  
            public boolean execute(FormItem item, Object value, DynamicForm form) {
            	if (form.getValue("isOpenQuestion") == null || form.getValue(OPENQUESTIONWITHTEXT) == null) return false;
                return form.getValue("isOpenQuestion").equals(Boolean.TRUE) && form.getValue(OPENQUESTIONWITHTEXT).equals(Boolean.TRUE);  
            }

        });  
		
	}
	private void createOpenQuestionWithValue() {
		openQuestionWithValueCBItem = new CheckboxItem();
		openQuestionWithValueCBItem.setName(OPENQUESTIONWITHVALUE);
		openQuestionWithValueCBItem.setTitle(constants.answerWithValue());
		openQuestionWithValueCBItem.setRedrawOnChange(true);
		openQuestionWithValueCBItem.setStartRow(true);
		openQuestionWithValueCBItem.setShowIfCondition(new FormItemIfFunction() {  
            public boolean execute(FormItem item, Object value, DynamicForm form) {
            	if (form.getValue("isOpenQuestion") == null) return false;
                return form.getValue("isOpenQuestion").equals(Boolean.TRUE);  
            }

        });  
		
		openQuestionValueDescItem = new TextItem(OPENQUESTIONVALUEDESC, "Message");
		openQuestionValueDescItem.setShowTitle(true);
		openQuestionValueDescItem.setEndRow(true);
		openQuestionValueDescItem.setStartRow(false);
		openQuestionValueDescItem.setShowIfCondition(new FormItemIfFunction() {  
            public boolean execute(FormItem item, Object value, DynamicForm form) {
            	if (form.getValue("isOpenQuestion") == null || form.getValue(OPENQUESTIONWITHVALUE) == null) return false;
                return form.getValue("isOpenQuestion").equals(Boolean.TRUE) && form.getValue(OPENQUESTIONWITHVALUE).equals(Boolean.TRUE) ;  
            }

        });  
	}
	
	
	public DataCollectionEditor(GeneralItem gi) {
		this();
		if (gi.getJsonRep().get("openQuestion") != null) {
			form.setValue("isOpenQuestion", true);
			JSONObject openQuestion = gi.getJsonRep().get("openQuestion").isObject();
			form.setValue(OPENQUESTIONWITHAUTDIO, openQuestion.get("withAudio").isBoolean().booleanValue());
			
			boolean hasText = false;
			if (openQuestion.containsKey("withText")) hasText= openQuestion.get("withText").isBoolean().booleanValue();
			form.setValue(OPENQUESTIONWITHTEXT, hasText);
			
			if (openQuestion.containsKey(OPENQUESTIONTEXTDESC)) 
				form.setValue(OPENQUESTIONTEXTDESC, openQuestion.get(OPENQUESTIONTEXTDESC).isString().stringValue());
			
			boolean hasValue = false;
			if (openQuestion.containsKey("withValue")) hasValue= openQuestion.get("withValue").isBoolean().booleanValue();
			form.setValue(OPENQUESTIONWITHVALUE, hasValue);
			
			if (openQuestion.containsKey(OPENQUESTIONVALUEDESC)) 
				form.setValue(OPENQUESTIONVALUEDESC, openQuestion.get(OPENQUESTIONVALUEDESC).isString().stringValue());
			
			form.setValue("openQuestionWithImage", openQuestion.get("withPicture").isBoolean().booleanValue());
			boolean hasVideo = false;
			if (openQuestion.containsKey("withVideo")) hasVideo= openQuestion.get("withVideo").isBoolean().booleanValue();
			form.setValue("openQuestionWithVideo", hasVideo);
		}
	}


	
	@Override
	public void saveToBean(GeneralItem gi) {
		
		if (form.getValue("isOpenQuestion") != null && (Boolean) form.getValue("isOpenQuestion")) {
			JSONObject openQuestion = new JSONObject();
			openQuestion.put("withPicture", JSONBoolean.getInstance( form.getValue("openQuestionWithImage")==null?false:(Boolean) form.getValue("openQuestionWithImage")) );
			openQuestion.put("withText", JSONBoolean.getInstance( form.getValue(OPENQUESTIONWITHTEXT)==null?false:(Boolean) form.getValue(OPENQUESTIONWITHTEXT)) );
			openQuestion.put("withValue", JSONBoolean.getInstance( form.getValue(OPENQUESTIONWITHVALUE)==null?false:(Boolean) form.getValue(OPENQUESTIONWITHVALUE)) );
			openQuestion.put("withAudio", JSONBoolean.getInstance( form.getValue(OPENQUESTIONWITHAUTDIO)==null?false:(Boolean) form.getValue(OPENQUESTIONWITHAUTDIO)) );
			openQuestion.put("withVideo", JSONBoolean.getInstance( form.getValue("openQuestionWithVideo")==null?false:(Boolean) form.getValue("openQuestionWithVideo")) );
			openQuestion.put(OPENQUESTIONVALUEDESC,  new JSONString(form.getValueAsString(OPENQUESTIONVALUEDESC)==null?"":form.getValueAsString(OPENQUESTIONVALUEDESC)));
			openQuestion.put(OPENQUESTIONTEXTDESC,  new JSONString(form.getValueAsString(OPENQUESTIONTEXTDESC)==null?"":form.getValueAsString(OPENQUESTIONTEXTDESC)));
			gi.getJsonRep().put("openQuestion", openQuestion);
		} else {
            if (gi.getJsonRep().containsKey("openQuestion")) {
                gi.getJsonRep().put("openQuestion", null);
            }
        }
	}
	
	@Override
	public boolean validate() {
		return true;
	}


}
