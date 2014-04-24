package org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors;

import com.smartgwt.client.widgets.form.fields.TextItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.MultipleChoiceImage;
import org.celstec.arlearn2.gwtcommonlib.client.objects.MultipleChoiceImageAnswerItem;

public class MultipleChoiceImageExtensionEditor  extends SingleChoiceImageExtensionEditor{

	public MultipleChoiceImageExtensionEditor() {
		super();
	}
	
	public MultipleChoiceImageExtensionEditor(GeneralItem gi) {
		super();

        questionForm.setGroupTitle("Question");
        final TextItem questionItem = new TextItem("audioQuestion", "question as audio");
        final TextItem columnsItem = new TextItem("columns", "# columns");
        final TextItem md5HashItem = new TextItem("md5Hash", "md5 hash");


        questionForm.setFields(questionItem, columnsItem, md5HashItem);
        questionItem.setValue(gi.getString("audioQuestion"));
        md5HashItem.setValue(gi.getString("md5Hash"));
        if (gi.getJsonRep().containsKey("columns") ) {
            columnsItem.setValue(gi.getInteger("columns"));
        } else {
            columnsItem.setValue(3);
        }
        addMember(questionForm);

		MultipleChoiceImage scTest = (MultipleChoiceImage) gi;
		
		for (MultipleChoiceImageAnswerItem answer: scTest.getAnswers()) {
			AnswerForm aForm = new AnswerForm(answer, forms.size());
			forms.add(aForm);
			addMember(aForm);
		}
		addAdditionalAnswer();
		
	}
}
