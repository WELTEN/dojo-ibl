package org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors;

import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.MultipleChoiceAnswer;
import org.celstec.arlearn2.gwtcommonlib.client.objects.MultipleChoiceTest;

public class MultipleChoiceExtensionEditor extends SingleChoiceExtensionEditor {

	public MultipleChoiceExtensionEditor(GeneralItem gi) {
		super();
		MultipleChoiceTest scTest = (MultipleChoiceTest) gi;

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
