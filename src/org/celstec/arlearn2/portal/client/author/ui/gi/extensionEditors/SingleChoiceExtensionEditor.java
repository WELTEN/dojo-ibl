package org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors;

import java.util.ArrayList;
import java.util.UUID;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.layout.VStack;
import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.MultipleChoiceAnswer;
import org.celstec.arlearn2.gwtcommonlib.client.objects.SingleChoiceTest;
import org.celstec.arlearn2.portal.client.author.ui.gi.i18.GeneralItemConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public class SingleChoiceExtensionEditor  extends VStack implements ExtensionEditor{
	private static GeneralItemConstants constants = GWT.create(GeneralItemConstants.class);

	protected  ArrayList<AnswerForm> forms = new ArrayList<AnswerForm>();
	

	
	public SingleChoiceExtensionEditor() {
        setOverflow(Overflow.AUTO);
        setWidth100();
        setHeight100();
	}
	
	public SingleChoiceExtensionEditor(GeneralItem gi) {
		this();
		SingleChoiceTest scTest = (SingleChoiceTest) gi;
		
		for (MultipleChoiceAnswer answer: scTest.getAnswers()) {
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
			MultipleChoiceAnswer answer = form.getMultipleChoiceAnswer();
			if (answer != null) array.set(i++, answer.getJsonRep());
		}
		gi.getJsonRep().put("answers", array);
		

	}
	
	protected void addAdditionalAnswer() {
		AnswerForm aForm = new AnswerForm(null, forms.size());
		forms.add(aForm);
		addMember(aForm);
	}
	
	
	protected class AnswerForm extends DynamicForm {
		
		public AnswerForm(MultipleChoiceAnswer answer, final int position) {
			final TextItem answerItem = new TextItem("answer", constants.answer()+" "+(position+1));
			if (answer != null) answerItem.setValue(answer.getString("answer"));
			answerItem.setStartRow(true);
			answerItem.addChangedHandler(new ChangedHandler() {
				
				@Override
				public void onChanged(ChangedEvent event) {
					if (event.getValue() != null && !((String) event.getValue()).equals("")) {
						if (position +1 == forms.size()) {
							addAdditionalAnswer();
						}
					}
					
				}
			});

			final CheckboxItem isCorrect = new CheckboxItem("isCorrect", constants.isCorrect());
			if (answer != null) isCorrect.setValue(answer.getBoolean("isCorrect"));
			isCorrect.setColSpan(1);
			
			HiddenItem hiddenId = new HiddenItem("id");
			if (answer != null) hiddenId.setValue(answer.getString("id"));

			setFields(answerItem, isCorrect, hiddenId);
			
			setNumCols(4);

		}
		
		public MultipleChoiceAnswer getMultipleChoiceAnswer() {
			MultipleChoiceAnswer result = new MultipleChoiceAnswer();
			String answerString = getValueAsString("answer");
			if (answerString == null || answerString.equals("")) return null;
			result.setString("answer", getValueAsString("answer"));
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
	
	@Override
	public boolean validate() {
		return true;
	}

	public static class UUID {
		private static final char[] CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray(); 
		/**
		 * Generate a random uuid of the specified length. Example: uuid(15) returns
		 * "VcydxgltxrVZSTV"
		 * 
		 * @param len
		 *            the desired number of characters
		 */
		public static String uuid(int len) {
			return uuid(len, CHARS.length);
		}
		/**
		 * Generate a random uuid of the specified length, and radix. Examples:
		 * <ul>
		 * <li>uuid(8, 2) returns "01001010" (8 character ID, base=2)
		 * <li>uuid(8, 10) returns "47473046" (8 character ID, base=10)
		 * <li>uuid(8, 16) returns "098F4D35" (8 character ID, base=16)
		 * </ul>
		 * 
		 * @param len
		 *            the desired number of characters
		 * @param radix
		 *            the number of allowable values for each character (must be <=
		 *            62)
		 */
		public static String uuid(int len, int radix) {
			if (radix > CHARS.length) {
				throw new IllegalArgumentException();
			}
			char[] uuid = new char[len];
			// Compact form
			for (int i = 0; i < len; i++) {
				uuid[i] = CHARS[(int)(Math.random()*radix)];
			}
			return new String(uuid);
		}
		/**
		 * Generate a RFC4122, version 4 ID. Example:
		 * "92329D39-6F5C-4520-ABFC-AAB64544E172"
		 */
		public static String uuid() {
			char[] uuid = new char[36];
			int r;

			// rfc4122 requires these characters
			uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
			uuid[14] = '4';

			// Fill in random data.  At i==19 set the high bits of clock sequence as
			// per rfc4122, sec. 4.1.5
			for (int i = 0; i < 36; i++) {
				if (uuid[i] == 0) {
					r = (int) (Math.random()*16);
					uuid[i] = CHARS[(i == 19) ? (r & 0x3) | 0x8 : r & 0xf];
				}
			}
			return new String(uuid);
		}
	}
}
