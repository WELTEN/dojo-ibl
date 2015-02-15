package org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors;

import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.form.fields.*;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GeneralItemModel;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.game.GameClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.generalItem.GeneralItemsClient;
import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.AudioObject;
import org.celstec.arlearn2.portal.client.account.AccountManager;
import org.celstec.arlearn2.portal.client.author.ui.generic.UploadItemForm;
import org.celstec.arlearn2.portal.client.author.ui.gi.i18.GeneralItemConstants;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.layout.VLayout;

public class AudioExtensionEditor  extends VLayout implements ExtensionEditor{
	private static GeneralItemConstants constants = GWT.create(GeneralItemConstants.class);
    private GeneralItem generalItem;
	protected DynamicForm form;

    private UploadItemForm uploadForm;
	
	public AudioExtensionEditor() {
		this(false, null);
	}

    public AudioExtensionEditor(boolean editing, final GeneralItem generalItem) {
        this.generalItem = generalItem;
        form = new DynamicForm();
        final TextItem audioFeedText = new TextItem(AudioObject.AUDIO_FEED, constants.audioURL());
        audioFeedText.setWidth("100%");
        audioFeedText.setCanEdit(false);

//        final HiddenItem audioFeedText = new HiddenItem(AudioObject.AUDIO_FEED);
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

        uploadForm = new UploadItemForm("formtest"){

            @Override
            public void onUploadCompleteEvent() {
                uploadForm.setVisibility(Visibility.HIDDEN);
                audioFeedText.setValue("http://streetlearn.appspot.com/game/"+generalItem.getLong(GameModel.GAMEID_FIELD)+"/generalItems/"+generalItem.getLong(GeneralItem.ID)+"/audio");
            }
        };
        addMember(uploadForm);

//        RootPanel.get().add(uploadForm.getIframe());

        if (generalItem != null) {
            final UploadItem uploadItem = new UploadItem("thumbnailfile", "Select audio file");


            SubmitItem button = new SubmitItem("Submitimage", "Submit");
            uploadForm.setFields(uploadItem, button);

            GeneralItemsClient.getInstance().getMediaUploadUrl(generalItem.getLong(GameModel.GAMEID_FIELD), generalItem.getLong(GeneralItemModel.ID_FIELD), "audio", new JsonCallback() {
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

    public void setGeneralItem(GeneralItem generalItem) {
        this.generalItem = generalItem;
    }

    public AudioExtensionEditor(GeneralItem gi) {
		this(true, gi);
		form.setValue(AudioObject.AUDIO_FEED, gi.getValueAsString(AudioObject.AUDIO_FEED));
        form.setValue(AudioObject.AUTO_PLAY, gi.getBoolean(AudioObject.AUTO_PLAY));
        form.setValue(AudioObject.MD5_HASH, gi.getValueAsString(AudioObject.MD5_HASH));
	}

	@Override
	public void saveToBean(GeneralItem gi) {
        if (generalItem != null) {
            gi.setLong(GeneralItemModel.ID_FIELD, generalItem.getLong(GeneralItemModel.ID_FIELD));
        }
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
		return form.getValueAsString(AudioObject.AUDIO_FEED)!= null

                && !"".equals(form.getValueAsString(AudioObject.AUDIO_FEED));
	}
}