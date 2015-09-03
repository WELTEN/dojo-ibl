package org.celstec.arlearn2.portal.client.author.ui.gi;

import org.celstec.arlearn2.gwtcommonlib.client.objects.AudioObject;
import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.MozillaOpenBadge;
import org.celstec.arlearn2.gwtcommonlib.client.objects.MultipleChoiceImage;
import org.celstec.arlearn2.gwtcommonlib.client.objects.MultipleChoiceTest;
import org.celstec.arlearn2.gwtcommonlib.client.objects.ScanTagObject;
import org.celstec.arlearn2.gwtcommonlib.client.objects.SingleChoiceImage;
import org.celstec.arlearn2.gwtcommonlib.client.objects.SingleChoiceTest;
import org.celstec.arlearn2.gwtcommonlib.client.objects.VideoObject;
import org.celstec.arlearn2.gwtcommonlib.client.objects.YoutubeObject;
import org.celstec.arlearn2.portal.client.account.AccountManager;
import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.AudioExtensionEditor;
import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.MozillaOpenBadgeExtensionEditor;
import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.MultipleChoiceExtensionEditor;
import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.MultipleChoiceImageExtensionEditor;
import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.ScanTagEditor;
import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.SingleChoiceExtensionEditor;
import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.SingleChoiceImageExtensionEditor;
import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.VideoObjectEditor;
import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.YoutubeObjectEditor;

import com.smartgwt.client.widgets.Canvas;

public class GeneralItemsManagement {

	public static String[] getItemTypes(boolean hidden) {
		String result[] = new String[7];
		if (AccountManager.getInstance().isAdvancedUser()) { //
			result = new String[10];
		} 
		
		result[0] = "Narrator Item";
		result[1] = AudioObject.HUMAN_READABLE_NAME;
		result[2] = VideoObject.HUMAN_READABLE_NAME;
		result[3] = YoutubeObject.HUMAN_READABLE_NAME;
		result[4] = ScanTagObject.HUMAN_READABLE_NAME;
		result[5] = SingleChoiceTest.HUMAN_READABLE_NAME;
		result[6] = MultipleChoiceTest.HUMAN_READABLE_NAME;
		if (AccountManager.getInstance().isAdvancedUser()) { // 
			result[7] = MultipleChoiceImage.HUMAN_READABLE_NAME;
			result[8] = SingleChoiceImage.HUMAN_READABLE_NAME;
			result[9] = MozillaOpenBadge.HUMAN_READABLE_NAME;
		}
		return result;
	}

	public static Canvas getMetadataExtensionEditor(GeneralItem gi) {
		if (gi.getType().equals(VideoObject.TYPE)) {
			return new VideoObjectEditor(gi);
		} else if (gi.getType().equals(YoutubeObject.TYPE)) {
			return new YoutubeObjectEditor(gi);
		} else if (gi.getType().equals(ScanTagObject.TYPE)) {
			return new ScanTagEditor(gi);
		}  else if (gi.getType().equals(SingleChoiceTest.TYPE)) {
			return new SingleChoiceExtensionEditor(gi);
		}  else if (gi.getType().equals(MultipleChoiceTest.TYPE)) {
			return new MultipleChoiceExtensionEditor(gi);
		}  else if (gi.getType().equals(AudioObject.TYPE)){
			return new AudioExtensionEditor();
		} else if (gi.getType().equals(MultipleChoiceImage.TYPE)) {
			return new MultipleChoiceImageExtensionEditor(gi);
		} else if (gi.getType().equals(SingleChoiceImage.TYPE)) {
			return new SingleChoiceImageExtensionEditor(gi);
		} else if (gi.getType().equals(MozillaOpenBadge.TYPE)) {
			return new MozillaOpenBadgeExtensionEditor(gi);
		}
		return new VideoObjectEditor();
	}
}
