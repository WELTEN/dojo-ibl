package org.celstec.arlearn2.portal.client.htmlDisplay;

import com.google.gwt.user.client.Random;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.VStack;

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
public class DrawableObject extends HStack {

    public DrawableObject(String url, int left, int top, Canvas parentObject) {
        setCanDragReposition(true);
        setLeft(left);
        setTop(top);
        setParentElement(parentObject);
        setHeight(48);

        final Img img = new Img();
        img.setSrc(url);
        img.setHeight(48);
        img.setWidth(48);
        addMember(img);


    }
}
