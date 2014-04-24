package org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.validator.CustomValidator;
import com.smartgwt.client.widgets.layout.VLayout;
import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.VideoObject;
import org.celstec.arlearn2.portal.client.account.AccountManager;
import org.celstec.arlearn2.portal.client.author.ui.gi.i18.GeneralItemConstants;

public class VideoObjectEditor extends VLayout implements ExtensionEditor{
	private static GeneralItemConstants constants = GWT.create(GeneralItemConstants.class);

	protected DynamicForm form;

    public VideoObjectEditor() {
        this (false);
    }
	public VideoObjectEditor(boolean editing) {
		form = new DynamicForm();
		final TextItem videoText = new TextItem(VideoObject.VIDEO_FEED, constants.videoURL());
        videoText.setWidth("100%");
        final CheckboxItem autoPlay = new CheckboxItem(VideoObject.AUTO_PLAY, constants.autoPlayVideo());
        final TextItem md5Text = new TextItem(VideoObject.MD5_HASH, "MD5 Hash");
        md5Text.setWidth("100%");
        videoText.setValidators(urlValidator);
        if (editing && AccountManager.getInstance().isAdvancedUser()) {
            form.setFields(videoText,autoPlay, md5Text);
        } else {
            if (editing) {
                form.setFields(videoText,autoPlay);
            } else {
                form.setFields(videoText);
            }
        }
		form.setWidth100();
		addMember(form);
	}
	
	public VideoObjectEditor(GeneralItem gi) {
		this(true);
		form.setValue(VideoObject.VIDEO_FEED, gi.getValueAsString(VideoObject.VIDEO_FEED));
        form.setValue(VideoObject.AUTO_PLAY, gi.getBoolean(VideoObject.AUTO_PLAY));
        form.setValue(VideoObject.MD5_HASH, gi.getValueAsString(VideoObject.MD5_HASH));


    }
	
	@Override
	public void saveToBean(GeneralItem gi) {
		gi.setString(VideoObject.VIDEO_FEED, form.getValueAsString(VideoObject.VIDEO_FEED));
        Boolean autoPlayValue = (Boolean) form.getValue(VideoObject.AUTO_PLAY);
        if (autoPlayValue == null) autoPlayValue =false;
        gi.setBoolean(VideoObject.AUTO_PLAY, autoPlayValue);
        String md5Hash = form.getValueAsString(VideoObject.MD5_HASH);
        if (md5Hash != null && !md5Hash.trim().equals("")) {
            gi.setString(VideoObject.MD5_HASH, md5Hash);
        }

	}
	
	public boolean validate() {
		return form.validate();
	}
	
	
	protected CustomValidator urlValidator = new CustomValidator() {
		
		@Override
		protected boolean condition(Object value) {
			if (value == null) return false;
			String stringValue = (""+value);
			if ((""+value).trim().equals("")) return false;
			if (!stringValue.startsWith("http://")) return false;
			return true;
		}
	};
}
