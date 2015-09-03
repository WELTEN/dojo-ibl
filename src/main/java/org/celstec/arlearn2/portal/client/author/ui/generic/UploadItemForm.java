package org.celstec.arlearn2.portal.client.author.ui.generic;

import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;

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
public abstract class UploadItemForm extends DynamicForm{

//    private String name;
    private static Canvas  iframe =  null;

    public UploadItemForm(String name) {
//        this.name = name;
        setEncoding(Encoding.MULTIPART);
        setCanSubmit(true);
        setTarget(iFrameName());
        getIframe();
    }

    public void getIframe() {
        if (iframe == null) {
            iframe = new Canvas();
            iframe.setID("fileUploadFrame");
            iframe.setContents("<IFRAME NAME=\"" + iFrameName() + "\"  style=\"width:0;height:0;border:0\"></IFRAME>");
            iframe.setVisibility(Visibility.VISIBLE);
            RootPanel.get().add(iframe);
        }

//        return iframe;
    }

    private String iFrameName() {
        return "fileUploadFrame";
    }

    public void addEventListener() {
        addEventListenerToIframe(this, iFrameName());

    }

    public abstract void onUploadCompleteEvent() ;

    public native void addEventListenerToIframe(UploadItemForm form, String name) /*-{
        $doc.onIframeEvent = function() {
            form.@org.celstec.arlearn2.portal.client.author.ui.generic.UploadItemForm::onUploadCompleteEvent()();
        }
        $doc.getElementsByName(name)[0].setAttribute('onload',  "javascript:document.onIframeEvent()");
    }-*/;



}
