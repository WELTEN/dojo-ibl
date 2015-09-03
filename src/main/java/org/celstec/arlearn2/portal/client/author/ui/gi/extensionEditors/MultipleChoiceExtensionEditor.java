package org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.MultipleChoiceAnswer;
import org.celstec.arlearn2.gwtcommonlib.client.objects.MultipleChoiceTest;

public class MultipleChoiceExtensionEditor extends SingleChoiceExtensionEditor {

	public MultipleChoiceExtensionEditor(GeneralItem gi) {
		super();
		MultipleChoiceTest scTest = (MultipleChoiceTest) gi;
		createIsFeedback();

		feedbackItem.setValue(gi.getBoolean("showFeedback"));
		feedbackItem.addChangedHandler(new ChangedHandler() {
			public void onChanged(ChangedEvent event) {
				for (DynamicForm formAnswer : forms) {
					formAnswer.redraw();
				}
			}
		});

		form.setNumCols(4);
		form.setWidth100();
		form.setFields(feedbackItem);

		addMember(form);

		for (MultipleChoiceAnswer answer : scTest.getAnswers()) {
			AnswerForm aForm = new AnswerForm(answer, forms.size());
			forms.add(aForm);
			addMember(aForm);
		}
		addAdditionalAnswer();

	}

	public MultipleChoiceExtensionEditor() {
		super();
	}
	
}
