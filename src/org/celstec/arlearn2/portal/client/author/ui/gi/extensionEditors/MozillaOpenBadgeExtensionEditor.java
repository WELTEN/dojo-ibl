package org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors;

import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.MozillaOpenBadge;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.VLayout;

public class MozillaOpenBadgeExtensionEditor extends VLayout implements ExtensionEditor{
	
	protected DynamicForm form;
	
	public MozillaOpenBadgeExtensionEditor() {
		form = new DynamicForm();
		final TextItem badgeUrlItem = new TextItem(MozillaOpenBadge.BADGE_URL, "badge url");
		final TextItem imageLocalUrlItem = new TextItem(MozillaOpenBadge.IMAGE, "image local url");
		
		form.setFields(badgeUrlItem, imageLocalUrlItem);
		form.setWidth100();
		addMember(form);
	}
	
	public MozillaOpenBadgeExtensionEditor(GeneralItem gi) {
		this();
		form.setValue(MozillaOpenBadge.BADGE_URL, gi.getValueAsString(MozillaOpenBadge.BADGE_URL));
		form.setValue(MozillaOpenBadge.IMAGE, gi.getValueAsString(MozillaOpenBadge.IMAGE));
	}
	
	@Override
	public void saveToBean(GeneralItem gi) {
		gi.setString(MozillaOpenBadge.BADGE_URL, form.getValueAsString(MozillaOpenBadge.BADGE_URL));
		gi.setString(MozillaOpenBadge.IMAGE, form.getValueAsString(MozillaOpenBadge.IMAGE));
	}
	
	@Override
	public boolean validate() {
		return true;
	}
}