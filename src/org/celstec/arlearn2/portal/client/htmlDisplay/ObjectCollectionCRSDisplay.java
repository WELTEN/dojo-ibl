package org.celstec.arlearn2.portal.client.htmlDisplay;

import com.google.gwt.user.client.Random;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import org.celstec.arlearn2.gwtcommonlib.client.objects.*;

import java.util.HashMap;
import java.util.List;
import com.smartgwt.client.widgets.Window;
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
public class ObjectCollectionCRSDisplay extends GeneralItemDisplay{
    private static ObjectCollectionDisplay objectCollectionDisplay;

    private HLayout hLayout;
    private Canvas[] canvasesCorrect;
    private Canvas[] canvasesWrong;
    private ObjectCollectionDisplay.Zone[] zones;

    private boolean showWrong = true;

    private HashMap<String, Action> actionMap = new HashMap<String, Action>();

    public ObjectCollectionCRSDisplay(){}


    public static void showAlert() {
        Window w = new Window();
        w.setWidth(525);
        w.setHeight(275);
        w.setIsModal(true);
        w.setShowModalMask(true);
        w.setAutoCenter(true);
        w.show();
        HTMLPane pane = new HTMLPane();
        pane.setContents(objectCollectionDisplay.getRichText());
        w.addItem(pane);
        w.setTitle(objectCollectionDisplay.getTitle());
    }


    public ObjectCollectionCRSDisplay(ObjectCollectionDisplay objectCollectionDisplay){
        this.objectCollectionDisplay = objectCollectionDisplay;


        hLayout = new HLayout();
        hLayout.setPadding(3);
        hLayout.setWidth100();
        hLayout.setHeight100();
        String description = objectCollectionDisplay.getRichText();

        List zonesList = objectCollectionDisplay.getZones();
        canvasesCorrect = new Canvas[zonesList.size()];
        if (showWrong) canvasesWrong = new Canvas[zonesList.size()];
        zones = new ObjectCollectionDisplay.Zone[zonesList.size()];


        int j = 0;

        for (ObjectCollectionDisplay.Zone zone: objectCollectionDisplay.getZones()) {

            if (showWrong) {
                Canvas canvas = createAnswerCanvas("wrong"+j);
                canvas.setBorder("5px solid #ff4040");
                canvas.setShowEdges(false);
                canvasesWrong [j] = canvas;
            }

            Canvas canvas = createAnswerCanvas(""+j);
            canvas.setBorder("5px solid #40ff40");
            zones[j] = zone;
            canvasesCorrect[j++] = canvas;

        }
        for (int i = 0; i<canvasesCorrect.length; i++) {
            VLayout layout = new VLayout(10);
            layout.setMembersMargin(10);
            layout.setWidth((100/canvasesCorrect.length)+"%");
            layout.setPadding(2);
            HTMLPane html = new HTMLPane();
            html.setContents("<h2>"+zones[i].getTitle()+"</h2>");
            html.setHeight(60);
            layout.addMember(html);
            layout.addMember(canvasesCorrect[i]);
            if (showWrong)  layout.addMember(canvasesWrong [i]);

            canvasesCorrect[i].setCanAcceptDrop(true);
            canvasesCorrect[i].setCanDrag(true);

//            canvases[i].setDropLineThickness(4);
            hLayout.addMember(layout);
        }

    }

    private Canvas createAnswerCanvas(String id) {
        Canvas canvases = new Canvas("canvas_"+id);
        canvases.setTop(40);
        canvases.setWidth100();
        canvases.setHeight("*");
//        canvases.setShowEdges(true);
        return canvases;
    }

    @Override
    public Canvas getCanvas() {
        return hLayout;
    }

    @Override
    public void handleResponse(Response response) {

    }

    @Override
    public void handleAction(Action action) {
        actionMap.put(action.getAction(), action);
        int j = 0;
        HashMap<Long, Canvas> wrongCanvas = new HashMap<Long, Canvas>();
        for (ObjectCollectionDisplay.Zone zone : zones) {

            for (ObjectCollectionDisplay.DisplayObject displayObject : zone.getZones()) {
                if (displayObject.getGeneralItemId() != null) {
                    wrongCanvas.put(displayObject.getGeneralItemId(), canvasesWrong[j]);


                }

            }
            j++;
        }

            j = 0;
        for (ObjectCollectionDisplay.Zone zone: zones) {




            for (ObjectCollectionDisplay.DisplayObject displayObject : zone.getZones()) {
                if (action.getGeneralItemId()!= null && displayObject.getGeneralItemId()!= null) {
                    if (action.getGeneralItemId().equals(displayObject.getGeneralItemId())) {
                        if (action.getAction().equals(displayObject.getAction())) {
                            drawPicture(canvasesCorrect[j], displayObject.getUrl(), action.getAccount());
                        }
                    } else {
                        if (action.getAction().equals(displayObject.getAction())) {
                            if (showWrong) drawPicture(wrongCanvas.get(action.getGeneralItemId()), displayObject.getUrl(), action.getAccount());
                        }
                    }

                }

            }
            j++;
        }

    }

    private void drawPicture(Canvas cubeBin, String url, String accountId) {
        if (url == null || "".equals(url.trim())) {
            url = "blue.png";
        }
        if (cubeBin != null) {
            int width = cubeBin.getWidth();

            DrawableObjectWithAccount object = new DrawableObjectWithAccount(url,Random.nextInt(width - 100), Random.nextInt(240), cubeBin);
            object.setAccount(accountMap.get(accountId));
            object.redraw();
        }
    }


}
