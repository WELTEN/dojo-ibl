package org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors;

import java.util.ArrayList;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONString;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.layout.VStack;
import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.MultipleChoiceAnswer;
import org.celstec.arlearn2.gwtcommonlib.client.objects.MultipleChoiceImageAnswerItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.SingleChoiceImage;
import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.SingleChoiceExtensionEditor.AnswerForm;
import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.SingleChoiceExtensionEditor.UUID;

import com.google.gwt.json.client.JSONArray;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public class SingleChoiceImageExtensionEditor extends VStack implements ExtensionEditor{

	protected  ArrayList<AnswerForm> forms = new ArrayList<AnswerForm>();
    protected DynamicForm questionForm = new DynamicForm();

    public SingleChoiceImageExtensionEditor() {
        setOverflow(Overflow.AUTO);
        setWidth100();
        setHeight100();
	}
	
	public SingleChoiceImageExtensionEditor(GeneralItem gi) {
		this();


        questionForm.setGroupTitle("Question");
        final TextItem questionItem = new TextItem("audioQuestion", "question as audio");
        final TextItem columnsItem = new TextItem("columns", "# columns");
        final TextItem md5HashItem = new TextItem("md5Hash", "md5 hash");

        questionForm.setFields(questionItem, columnsItem, md5HashItem);
        questionItem.setValue(gi.getString("audioQuestion"));
        if (gi.getJsonRep().containsKey("columns") ) {
            columnsItem.setValue(gi.getInteger("columns"));
        } else {
            columnsItem.setValue(3);
        }
        addMember(questionForm);

		SingleChoiceImage scTest = (SingleChoiceImage) gi;
		for (MultipleChoiceImageAnswerItem answer: scTest.getAnswers()) {
			AnswerForm aForm = new AnswerForm(answer, forms.size());
			forms.add(aForm);
			addMember(aForm);
		}
		addAdditionalAnswer();
		
	}
	
	@Override
	public void saveToBean(GeneralItem gi) {
		JSONArray array = new JSONArray();
		int i = 0;
		for (AnswerForm form: forms){
			MultipleChoiceImageAnswerItem answer = form.getMultipleChoiceAnswer();
			if (answer != null) array.set(i++, answer.getJsonRep());
		}
		gi.getJsonRep().put("answers", array);
        String audioQuestion = questionForm.getValueAsString("audioQuestion");
        if (audioQuestion != null && !"".equals(audioQuestion)){
            gi.getJsonRep().put("audioQuestion", new JSONString(audioQuestion));
        }
        String columnsString = questionForm.getValueAsString("columns");
        if (columnsString != null && !"".equals(columnsString)){
            Integer columns = Integer.parseInt(columnsString);
            gi.getJsonRep().put("columns", new JSONNumber(columns));
        }

        String md5HashString = questionForm.getValueAsString("md5Hash");
        if (md5HashString != null && !"".equals(md5HashString)){
            gi.getJsonRep().put("md5Hash", new JSONString(md5HashString));

        }
		

	}
	
	@Override
	public boolean validate() {
		return true;
	}
	
	protected void addAdditionalAnswer() {
		AnswerForm aForm = new AnswerForm(null, forms.size());
		forms.add(aForm);
		addMember(aForm);
	}
	
	protected class AnswerForm extends DynamicForm {
		
		public AnswerForm(MultipleChoiceImageAnswerItem answer, final int position) {
			setGroupTitle("answer "+(position+1));
			setIsGroup(true);
            final TextItem labelItem = new TextItem(MultipleChoiceImageAnswerItem.LABEL, "Label");
            if (answer != null) labelItem.setValue(answer.getString(MultipleChoiceImageAnswerItem.LABEL));
            labelItem.setStartRow(true);

			final TextItem imageItem = new TextItem(MultipleChoiceImageAnswerItem.IMAGE_URL, "picture url "+" "+(position+1));
            imageItem.setWidth("100%");

            if (answer != null) imageItem.setValue(answer.getString(MultipleChoiceImageAnswerItem.IMAGE_URL));
			
			imageItem.setStartRow(true);
			imageItem.addChangedHandler(new ChangedHandler() {
				
				@Override
				public void onChanged(ChangedEvent event) {
					if (event.getValue() != null && !((String) event.getValue()).equals("")) {
						if (position +1 == forms.size()) {
							addAdditionalAnswer();
						}
					}
					
				}
			});
            final TextItem imageMd5Item = new TextItem(MultipleChoiceImageAnswerItem.IMAGE_MD5, "md5 hash image");
            if (answer != null) imageMd5Item.setValue(answer.getString(MultipleChoiceImageAnswerItem.IMAGE_MD5));
            imageMd5Item.setStartRow(true);

            final TextItem audioItem = new TextItem(MultipleChoiceImageAnswerItem.AUDIO_URL, "audio url ");
			if (answer != null) audioItem.setValue(answer.getString(MultipleChoiceImageAnswerItem.AUDIO_URL));
			
			audioItem.setStartRow(true);
            audioItem.setWidth("100%");

            audioItem.addChangedHandler(new ChangedHandler() {
				
				@Override
				public void onChanged(ChangedEvent event) {
					if (event.getValue() != null && !((String) event.getValue()).equals("")) {
						if (position +1 == forms.size()) {
							addAdditionalAnswer();
						}
					}
					
				}
			});

            final TextItem audioMd5Item = new TextItem(MultipleChoiceImageAnswerItem.AUDIO_MD5, "md5 hash audio");
            if (answer != null) audioMd5Item.setValue(answer.getString(MultipleChoiceImageAnswerItem.AUDIO_MD5));
            audioMd5Item.setStartRow(true);



            final CheckboxItem isCorrect = new CheckboxItem("isCorrect", "is Correct");
			if (answer != null) isCorrect.setValue(answer.getBoolean("isCorrect"));
			isCorrect.setColSpan(1);
			
			HiddenItem hiddenId = new HiddenItem("id");
			if (answer != null) hiddenId.setValue(answer.getString("id"));

			setFields(labelItem, imageItem, imageMd5Item, audioItem, audioMd5Item, isCorrect, hiddenId);
			
			setNumCols(4);

		}
		
		public MultipleChoiceImageAnswerItem getMultipleChoiceAnswer() {
			MultipleChoiceImageAnswerItem result = new MultipleChoiceImageAnswerItem();
            String labelString = getValueAsString(MultipleChoiceImageAnswerItem.LABEL);
            if (!(labelString == null || labelString.equals(""))){
                result.setString(MultipleChoiceImageAnswerItem.LABEL, getValueAsString(MultipleChoiceImageAnswerItem.LABEL));
            }
            String imageUrlString = getValueAsString(MultipleChoiceImageAnswerItem.IMAGE_URL);
			if (imageUrlString == null || imageUrlString.equals("")) return null;
			result.setString(MultipleChoiceImageAnswerItem.IMAGE_URL, getValueAsString(MultipleChoiceImageAnswerItem.IMAGE_URL));

            String imageMd5String = getValueAsString(MultipleChoiceImageAnswerItem.IMAGE_MD5);
            if (!(imageMd5String == null || imageMd5String.equals(""))) {
            result.setString(MultipleChoiceImageAnswerItem.IMAGE_MD5, getValueAsString(MultipleChoiceImageAnswerItem.IMAGE_MD5));
            }
			String audioUrlString = getValueAsString(MultipleChoiceImageAnswerItem.AUDIO_URL);
			if (audioUrlString == null || audioUrlString.equals("")) return null;
			result.setString(MultipleChoiceImageAnswerItem.AUDIO_URL, getValueAsString(MultipleChoiceImageAnswerItem.AUDIO_URL));

            String audioMd5String = getValueAsString(MultipleChoiceImageAnswerItem.AUDIO_MD5);
            if (!(audioMd5String == null || audioMd5String.equals(""))) {
                result.setString(MultipleChoiceImageAnswerItem.AUDIO_MD5, getValueAsString(MultipleChoiceImageAnswerItem.AUDIO_MD5));
            }

			if (getValueAsString("id") != null && !getValueAsString("id").equals("")) {
				result.setString("id", getValueAsString("id"));
			} else {
				result.setString("id", UUID.uuid(15));
			}
			Boolean isCorrectValue = (Boolean) getValue("isCorrect");
			if (isCorrectValue == null) isCorrectValue = false;
			result.setBoolean("isCorrect", isCorrectValue); 
			
			return result;
		}
	}
}
