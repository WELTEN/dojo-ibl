package org.celstec.arlearn2.portal.client.author.ui;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.VStack;
import org.celstec.arlearn2.gwtcommonlib.client.objects.FileReference;
import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.MultipleChoiceAnswer;

import java.util.ArrayList;

/**
 * ****************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * <p/>
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Contributors: Stefaan Ternier
 * ****************************************************************************
 */
public class FileReferencesEditor extends SectionConfig {

    private GeneralItem gi;
    private VStack layout;

    protected ArrayList<FileReferenceForm> forms = new ArrayList<FileReferenceForm>();

    public FileReferencesEditor() {
        super("Edit File References");
        layout = new VStack();
        layout.setWidth100();
        layout.setHeight100();
        layout.setOverflow(Overflow.AUTO);

        setItems(layout);
    }

    public void loadDataFromRecord(GeneralItem gi) {
        this.gi = gi;
        FileReference[] refs = gi.getFileReferences();
        for (FileReference ref : gi.getFileReferences()) {
            FileReferenceForm fForm = new FileReferenceForm(ref, forms.size());
            forms.add(fForm);
            layout.addMember(fForm);
        }
        addAdditionalRow();

    }

    public JSONArray getJson() {
        JSONArray array = new JSONArray();
        int i = 0;
        for (FileReferenceForm form: forms){
            FileReference fileRef = form.getFileReference();
            if (fileRef != null) array.set(i++, fileRef.getJsonRep());
        }
        return array;
    }


    protected class FileReferenceForm extends DynamicForm {

        public FileReferenceForm(FileReference answer, final int position) {
            final TextItem keyItem = new TextItem(FileReference.KEY, "key" + " " + (position + 1));
            keyItem.setWidth("100%");
            final TextItem refItem = new TextItem(FileReference.FILE_REFERENCE, "url" + " " + (position + 1));
            refItem.setWidth("100%");
            final TextItem md5Item = new TextItem(FileReference.MD5_HASH, "md5 hash" + " " + (position + 1));
            refItem.setWidth("100%");
            if (answer != null) {
                keyItem.setValue(answer.getString(FileReference.KEY));



                refItem.setValue(answer.getString(FileReference.FILE_REFERENCE));
                md5Item.setValue(answer.getString(FileReference.MD5_HASH));

            }
            keyItem.setStartRow(true);
            keyItem.addChangedHandler(new ChangedHandler() {

                @Override
                public void onChanged(ChangedEvent event) {
                    if (event.getValue() != null && !((String) event.getValue()).equals("")) {
                        if (position + 1 == forms.size()) {
                            addAdditionalRow();
                        }
                    }

                }
            });

            setFields(keyItem, refItem, md5Item);
            setWidth100();
            setIsGroup(true);
            setGroupTitle("File Reference "+(position + 1));

        }

        public FileReference getFileReference() {
            FileReference result = new FileReference();
            String keyString = getValueAsString(FileReference.KEY);
            String refString = getValueAsString(FileReference.FILE_REFERENCE);
            String md5String = getValueAsString(FileReference.MD5_HASH);
            if (keyString == null || keyString.trim().equals("")) return null;
            if (refString == null || refString.trim().equals("")) return null;
            result.setString(FileReference.KEY, getValueAsString(FileReference.KEY));
            result.setString(FileReference.FILE_REFERENCE, getValueAsString(FileReference.FILE_REFERENCE));

            if (md5String != null && !md5String.trim().equals("")) {
                result.setString(FileReference.MD5_HASH, getValueAsString(FileReference.MD5_HASH));
            }
            return result;
        }
    }

    protected void addAdditionalRow() {
        FileReferenceForm aForm = new FileReferenceForm(null, forms.size());
        forms.add(aForm);
        layout.addMember(aForm);
    }
}
