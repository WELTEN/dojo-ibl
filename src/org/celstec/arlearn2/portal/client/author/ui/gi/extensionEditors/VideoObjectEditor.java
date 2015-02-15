package org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SubmitItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.UploadItem;
import com.smartgwt.client.widgets.form.validator.CustomValidator;
import com.smartgwt.client.widgets.layout.VLayout;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GeneralItemModel;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.generalItem.GeneralItemsClient;
import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.VideoObject;
import org.celstec.arlearn2.portal.client.account.AccountManager;
import org.celstec.arlearn2.portal.client.author.ui.generic.UploadItemForm;
import org.celstec.arlearn2.portal.client.author.ui.gi.i18.GeneralItemConstants;

public class VideoObjectEditor extends VLayout implements ExtensionEditor{
	private static GeneralItemConstants constants = GWT.create(GeneralItemConstants.class);

	protected DynamicForm form;
    private GeneralItem generalItem;
    private UploadItemForm uploadForm;
    public void setGeneralItem(GeneralItem generalItem) {
        this.generalItem = generalItem;
    }
    public VideoObjectEditor() {
        this (false, null);
    }

    public VideoObjectEditor(boolean editing, final GeneralItem generalItem) {
		form = new DynamicForm();
        this.generalItem = generalItem;
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

        uploadForm = new UploadItemForm("formtest"){

            @Override
            public void onUploadCompleteEvent() {
                uploadForm.setVisibility(Visibility.HIDDEN);
                videoText.setValue("http://streetlearn.appspot.com/game/"+generalItem.getLong(GameModel.GAMEID_FIELD)+"/generalItems/"+generalItem.getLong(GeneralItem.ID)+"/video");
            }
        };
        addMember(uploadForm);

//        RootPanel.get().add(uploadForm.getIframe());

        if (generalItem != null) {
            final UploadItem uploadItem = new UploadItem("Videofile", "Select audio file");


            SubmitItem button = new SubmitItem("Submitimage", "Submit");
            uploadForm.setFields(uploadItem, button);

            GeneralItemsClient.getInstance().getMediaUploadUrl(generalItem.getLong(GameModel.GAMEID_FIELD), generalItem.getLong(GeneralItemModel.ID_FIELD), "video", new JsonCallback() {
                public void onJsonReceived(JSONValue jsonValue) {
                    if (jsonValue.isObject() != null) {
                        uploadForm.setAction(jsonValue.isObject().get("uploadUrl").isString().stringValue());
                    }
                }
            });
//        GameClient.getInstance().getPictureUrl(5946158883012608l, new JsonCallback(){
//            public void onJsonReceived(JSONValue jsonValue) {
//                if (jsonValue.isObject() !=null) {
//                    uploadForm.setAction(jsonValue.isObject().get("uploadUrl").isString().stringValue());
//                }
//            }
//        });

            uploadForm.addEventListener();

        }
	}
	
	public VideoObjectEditor(GeneralItem gi) {
		this(true, gi);
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
