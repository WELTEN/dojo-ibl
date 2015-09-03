package org.celstec.arlearn2.portal.client.htmlDisplay;

import com.google.gwt.user.client.Random;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GeneralItemModel;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Action;
import org.celstec.arlearn2.gwtcommonlib.client.objects.MatrixCollectionDisplay;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Response;


import java.util.HashMap;
import java.util.List;

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
public class MatrixCollectionCRSDisplay extends GeneralItemDisplay {
    private static MatrixCollectionDisplay matrixCollectionDisplay;

    private HLayout hLayout;

//    private MatrixCollectionDisplay.Column[] columns;
//    private MatrixCollectionDisplay.Row[] rows;
    private ActionItemCanvas[][] canvases;

    private HashMap<String, Action> actionMap = new HashMap<String, Action>();

    private List<MatrixCollectionDisplay.Column> columnsList;
    private List<MatrixCollectionDisplay.Row> rowList;

    public MatrixCollectionCRSDisplay(){}

    public static native void exportStaticMethod() /*-{
        $wnd.showAlert =
            $entry(@org.celstec.arlearn2.portal.client.htmlDisplay.MatrixCollectionCRSDisplay::showAlert());
    }-*/;

    public static void showAlert() {
        Window w = new Window();
        w.setWidth(525);
        w.setHeight(275);
        w.setIsModal(true);
        w.setShowModalMask(true);
        w.setAutoCenter(true);
        w.show();
        HTMLPane pane = new HTMLPane();
        pane.setContents(matrixCollectionDisplay.getRichText());
        w.addItem(pane);
        w.setTitle(matrixCollectionDisplay.getTitle());
    }

    public MatrixCollectionCRSDisplay(MatrixCollectionDisplay matrixCollectionDisplay) {
        this.matrixCollectionDisplay = matrixCollectionDisplay;

        hLayout = new HLayout();
        hLayout.setPadding(3);
        hLayout.setWidth100();
        hLayout.setHeight100();
        String description = matrixCollectionDisplay.getRichText();

        columnsList = matrixCollectionDisplay.getColumns();
        rowList = matrixCollectionDisplay.getRows();
        canvases = new ActionItemCanvas[columnsList.size()+1][rowList.size()];
//        columns = new  MatrixCollectionDisplay.Column[columnsList.size()];
//        rows = new MatrixCollectionDisplay.Row[rowList.size()];

//        int j = 0;

//        for (MatrixCollectionDisplay.Column column: columnsList) {
//            Canvas canvas = createAnswerCanvas(""+j);
//            canvas.setBorder("5px solid #40ff40");
//            columns[j] = column;
//            canvases[j++] = canvas;
//
//        }
        for (int j = 0; j< canvases[0].length; j++) {
            ActionItemCanvas canvas = createRowHeaderCanvas(
                    rowList.get(j).getString("imageUrl"), rowList.get(j).getString("audioUrl"), j);
            canvases[0][j] = canvas;
        }
        for (int i = 0; i< canvases.length-1; i++) {

            for (int j = 0; j< canvases[0].length; j++) {
                System.out.println("create i "+i+" j "+j);
                ActionItemCanvas canvas = createAnswerCanvas("" + i + "_" + j, columnsList.get(i).getItemId(), rowList.get(j).getAction());
                canvas.setBorder("5px solid #40ff40");
                canvases[i+1][j] = canvas;
            }
        }

        for (int i = 0; i< canvases.length; i++) {
            VLayout layout = new VLayout(10);
            layout.setMembersMargin(10);
//            layout.setWidth((100/ canvases.length)+"%");
            layout.setWidth("100%");
            layout.setPadding(2);

            HTMLPane html = new HTMLPane();
            if (i!=0) {
                if (columnsList.get(i - 1).getString("imageUrl") != null) {
                    html.setContents("<img width=50 height=50 src=\"" + columnsList.get(i - 1).getString("imageUrl") + "\"></img>");
                } else {
                    html.setContents("<h2>" + columnsList.get(i - 1).getItemId() + "</h2>");
                }
            } else {
                layout.setWidth(50);
                html.setWidth(50);
                html.setContents("");
            }
            html.setHeight(60);
            layout.addMember(html);
            for (int j = 0; j< canvases[0].length; j++) {
                layout.addMember(canvases[i][j]);
                canvases[i][j].setCanAcceptDrop(true);
                canvases[i][j].setCanDrag(true);
            }
            hLayout.addMember(layout);
        }


    }
        @Override
    public Canvas getCanvas() {
        return hLayout;
    }



    private ActionItemCanvas createRowHeaderCanvas(String imageUrl, String audioUrl, int j) {
        ActionItemCanvas canvases = new ActionItemCanvas("canvas_row_"+j);
        canvases.setWidth(50);
        canvases.setHeight("*");

        HTMLPane html = new HTMLPane();
        if (imageUrl!=null) {
            html.setContents("<img width=50 height=50 src=\""+imageUrl+"\" onclick=\"playSound('"+audioUrl+"');\"></img>");
        }

        html.setHeight(60);
        canvases.addChild(html);
        return canvases;
    }

    private ActionItemCanvas createAnswerCanvas(String id, long itemId, String actionId) {
        ActionItemCanvas canvases = new ActionItemCanvas("canvas_"+id);
        canvases.setTop(40);
        canvases.setWidth100();
        canvases.setHeight("*");
        canvases.setActionId(actionId);
        canvases.setItemId(itemId);
//        canvases.setShowEdges(true);
        return canvases;
    }

    @Override
    public void handleResponse(Response response) {
//        System.out.println("response "+response.getString("responseValue"));
    }

    @Override
    public void handleAction(Action action) {
        actionMap.put(action.getAction(), action);

//        System.out.println("action "+action.getAction() +" " +action.getGeneralItemId());

        for (int i = 0; i< canvases.length; i++) {
            for (int j = 0; j< canvases[0].length; j++) {
                System.out.println(canvases[i][j].getItemId()+ ""+canvases[i][j].getActionId());
                if (canvases[i][j].getItemId() == action.getGeneralItemId()
                        && canvases[i][j].getActionId().equals(action.getAction()))
                    drawPicture(canvases[i][j], "blue.png", action.getAccount());
            }
        }

//        HashMap<Long, Canvas> wrongCanvas = new HashMap<Long, Canvas>();
//        for (ObjectCollectionDisplay.Zone zone : zones) {
//
//            for (ObjectCollectionDisplay.DisplayObject displayObject : zone.getZones()) {
//                if (displayObject.getGeneralItemId() != null) {
//                    wrongCanvas.put(displayObject.getGeneralItemId(), canvasesWrong[j]);
//
//
//                }
//
//            }
//            j++;
//        }
//
//        j = 0;
//        for (ObjectCollectionDisplay.Zone zone: zones) {
//
//
//
//
//            for (ObjectCollectionDisplay.DisplayObject displayObject : zone.getZones()) {
//                if (action.getGeneralItemId()!= null && displayObject.getGeneralItemId()!= null) {
//                    if (action.getGeneralItemId().equals(displayObject.getGeneralItemId())) {
//                        if (action.getAction().equals(displayObject.getAction())) {
////                            drawPicture(canvasesCorrect[j], displayObject.getUrl(), action.getAccount());
//                        }
//                    } else {
//                        if (action.getAction().equals(displayObject.getAction())) {
////                            if (showWrong) drawPicture(wrongCanvas.get(action.getGeneralItemId()), displayObject.getUrl(), action.getAccount());
//                        }
//                    }
//
//                }
//
//            }
//            j++;
//        }

    }

    @Override
    public void exportMethod() {
        exportStaticMethod2();
    }

    public static native void exportStaticMethod2() /*-{
        $wnd.showAlert =
            $entry(@org.celstec.arlearn2.portal.client.htmlDisplay.MatrixCollectionCRSDisplay::showAlert());
    }-*/;

    private void drawPicture(Canvas cubeBin, String url, String accountId) {
        if (url == null || "".equals(url.trim())) {
            url = "blue.png";
        }
        if (cubeBin != null) {
            int width = cubeBin.getWidth();

//            DrawableObjectWithAccount object = new DrawableObjectWithAccount(url, Random.nextInt(width - 100), Random.nextInt(240), cubeBin);
            if (accountMap.get(accountId) != null && accountMap.get(accountId).getPicture()!=null){
                url = accountMap.get(accountId).getPicture();
            }
            DrawableObject object = new DrawableObject(url,Random.nextInt(width - 100), Random.nextInt(240), cubeBin);
//            object.setAccount(accountMap.get(accountId));
            object.redraw();
        }
    }


    public class ActionItemCanvas extends Canvas {

        private long itemId;
        private String actionId;

        public ActionItemCanvas(String id){
            super(id);
        }

        public long getItemId() {
            return itemId;
        }

        public void setItemId(long itemId) {
            this.itemId = itemId;
        }

        public String getActionId() {
            return actionId;
        }

        public void setActionId(String actionId) {
            this.actionId = actionId;
        }
    }
}
