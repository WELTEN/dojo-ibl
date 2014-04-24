package org.celstec.arlearn2.portal.client.htmlDisplay;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Img;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Account;

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
public class DrawableObjectWithAccount extends DrawableObject {

    private Img objectImage;
    private Img accountImage;

    public DrawableObjectWithAccount(String url, int left, int top, Canvas parentObject) {
        super(url, left, top, parentObject);
        setCanDragReposition(true);
        setLeft(left);
        setTop(top);
        setParentElement(parentObject);

//        objectImage = new Img();
//        objectImage.setSrc(url);
//
//        addMember(objectImage);
        setShowEdges(true);
    }

    public void setAccount(Account account) {
        accountImage = new Img();
        accountImage.setHeight(48);
        accountImage.setWidth(48);
        accountImage.setSrc(account.getPicture());
        addMember(accountImage);
    }


}
