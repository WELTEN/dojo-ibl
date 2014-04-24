package org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors;

import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.AudioObject;
import org.celstec.arlearn2.portal.client.account.AccountManager;
import org.celstec.arlearn2.portal.client.author.ui.gi.i18.GeneralItemConstants;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.VLayout;

public class AudioExtensionEditor  extends VLayout implements ExtensionEditor{
	private static GeneralItemConstants constants = GWT.create(GeneralItemConstants.class);

	protected DynamicForm form;

	
	public AudioExtensionEditor() {
		this(false);
	}

    public AudioExtensionEditor(boolean editing) {
        form = new DynamicForm();
        final TextItem audioFeedText = new TextItem(AudioObject.AUDIO_FEED, constants.audioURL());
        audioFeedText.setWidth("100%");

        final CheckboxItem autoPlay = new CheckboxItem(AudioObject.AUTO_PLAY, constants.autoPlayAudio());
        final TextItem md5Text = new TextItem(AudioObject.MD5_HASH, "MD5 Hash");
        md5Text.setWidth("100%");
        if (editing && AccountManager.getInstance().isAdvancedUser()) {
            form.setFields(audioFeedText,autoPlay, md5Text);
        } else {
            if (editing) {
                form.setFields(audioFeedText,autoPlay);
            } else {
                form.setFields(audioFeedText);
            }
        }
        form.setWidth100();
        addMember(form);
    }
	
	public AudioExtensionEditor(GeneralItem gi) {
		this(true);
		form.setValue(AudioObject.AUDIO_FEED, gi.getValueAsString(AudioObject.AUDIO_FEED));
        form.setValue(AudioObject.AUTO_PLAY, gi.getBoolean(AudioObject.AUTO_PLAY));
        form.setValue(AudioObject.MD5_HASH, gi.getValueAsString(AudioObject.MD5_HASH));
	}

	@Override
	public void saveToBean(GeneralItem gi) {
		gi.setString(AudioObject.AUDIO_FEED, form.getValueAsString(AudioObject.AUDIO_FEED));
        Boolean autoPlayValue = (Boolean) form.getValue(AudioObject.AUTO_PLAY);
        if (autoPlayValue == null) autoPlayValue =false;
        gi.setBoolean(AudioObject.AUTO_PLAY, autoPlayValue);
        String md5Hash = form.getValueAsString(AudioObject.MD5_HASH);
        if (md5Hash != null && !md5Hash.trim().equals("")) {
            gi.setString(AudioObject.MD5_HASH, md5Hash);
        }
	}

	@Override
	public boolean validate() {
		return true;
	}
}