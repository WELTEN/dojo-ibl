package org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors;

import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.YoutubeObject;
import org.celstec.arlearn2.portal.client.author.ui.gi.i18.GeneralItemConstants;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.VLayout;

public class YoutubeObjectEditor extends VLayout implements ExtensionEditor{
	private static GeneralItemConstants constants = GWT.create(GeneralItemConstants.class);
	protected DynamicForm form;
    private TextItem videoText;

    public YoutubeObjectEditor() {
		form = new DynamicForm();
		videoText = new TextItem(YoutubeObject.YOUTUBE_URL, constants.youtubeURL()+" http://www.youtube.com/watch?v=");

        videoText.addChangedHandler(new ChangedHandler() {
            @Override
            public void onChanged(ChangedEvent event) {
                String url = ""+event.getValue();
                if (url.contains("youtube.com/watch?v=")) {
                    url = url.substring(url.indexOf("=")+1);
                    videoText.setValue(url);
                }
                if (url.contains("youtu.be/")) {
                    url = url.substring(url.indexOf(".be/")+4);
                    videoText.setValue(url);
                }
                if (url.contains("www.youtube.com/share_popup?v=")) {
                    url = url.substring(url.indexOf("=")+1);
                    videoText.setValue(url);
                }
            }
        });
		form.setFields(videoText);
		form.setWidth100();
		addMember(form);
	}

    private void setValue(String urlRemainder) {
        if (urlRemainder.contains("&")) urlRemainder = urlRemainder.substring(0, urlRemainder.indexOf("&"));
        videoText.setValue(urlRemainder);
    }
	public YoutubeObjectEditor(GeneralItem gi) {
		this();
        String youUrl = gi.getValueAsString(YoutubeObject.YOUTUBE_URL);
		form.setValue(YoutubeObject.YOUTUBE_URL, youUrl.substring(youUrl.indexOf("=")+1));
	}

//	public void writeMetadataToObject(GeneralItem gi) {
//		gi.setString(YoutubeObject.YOUTUBE_URL, form.getValueAsString(YoutubeObject.YOUTUBE_URL));
//	}

	@Override
	public void saveToBean(GeneralItem gi) {
		gi.setString(YoutubeObject.YOUTUBE_URL, "http://www.youtube.com/watch?v="+form.getValueAsString(YoutubeObject.YOUTUBE_URL));
		
	}
	
	@Override
	public boolean validate() {
		return true;
	}
}
