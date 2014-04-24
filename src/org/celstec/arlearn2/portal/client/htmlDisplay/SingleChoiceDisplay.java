package org.celstec.arlearn2.portal.client.htmlDisplay;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.Random;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import org.celstec.arlearn2.gwtcommonlib.client.objects.*;

import java.util.HashMap;

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
public class SingleChoiceDisplay extends GeneralItemDisplay{

    private SingleChoiceTest sct;
    private MultipleChoiceAnswer[] answers;

    private Canvas[] canvases;
    private HashMap<String, Canvas> canvasHashMap = new HashMap<String, Canvas>();

    private HLayout hLayout;

    public SingleChoiceDisplay(SingleChoiceTest sct){
        this.sct = sct;
        answers = sct.getAnswers();
        canvases = new Canvas[answers.length];

        hLayout = new HLayout();
        hLayout.setPadding(3);
        hLayout.setWidth100();
        hLayout.setHeight100();

        int j = 0;
        for (MultipleChoiceAnswer answer : answers) {
            Canvas canvas = createAnswerCanvas(answer.getString(MultipleChoiceAnswer.ID));
            canvasHashMap.put(answer.getString(MultipleChoiceAnswer.ANSWER), canvas);
            canvases[j++] = canvas;
        }
        for (int i = 0; i<canvases.length; i++) {
            VLayout layout = new VLayout(10);
            layout.setMembersMargin(10);
            layout.setWidth((100/canvases.length)+"%");
            layout.setPadding(2);
            HTMLPane html = new HTMLPane();
            html.setContents("<h2>"+answers[i].getString(MultipleChoiceAnswer.ANSWER)+"</h2>");
            html.setHeight(60);
            layout.addMember(html);
            layout.addMember(canvases[i]);
            hLayout.addMember(layout);
        }
    }

    public SingleChoiceDisplay(SingleChoiceImage sci){
        this.sct = new SingleChoiceTest(sci.getJsonRep());
        answers = this.sct.getAnswers();
        canvases = new Canvas[answers.length];

        hLayout = new HLayout();
        hLayout.setPadding(3);
        hLayout.setWidth100();
        hLayout.setHeight100();

        int j = 0;
        for (MultipleChoiceAnswer answer : answers) {
            Canvas canvas = createAnswerCanvas(answer.getString(MultipleChoiceAnswer.ID));
            canvasHashMap.put(answer.getString(MultipleChoiceAnswer.ANSWER), canvas);
            canvases[j++] = canvas;
        }
        for (int i = 0; i<canvases.length; i++) {
            VLayout layout = new VLayout(10);
            layout.setMembersMargin(10);
            layout.setWidth((100/canvases.length)+"%");
            layout.setPadding(2);
            HTMLPane html = new HTMLPane();
            html.setContents("<h2>"+answers[i].getString(MultipleChoiceAnswer.ANSWER)+"</h2>");
            html.setHeight(60);
            layout.addMember(html);
            layout.addMember(canvases[i]);
            hLayout.addMember(layout);
        }
    }

    @Override
    public void handleAction(Action action) {}

    public void handleResponse(Response response) {
        String userId = response.getString(Response.FULL_ID);
        String responseValue = JSONParser.parseLenient(response.getString(Response.RESPONSE_VALUE)).isObject().get("answer").isString().stringValue();
        drawAccount(canvasHashMap.get(responseValue), accountMap.get(userId).getPicture());

    }

    private Canvas createAnswerCanvas(String id) {
        Canvas canvases = new Canvas("canvas_"+id);
        canvases.setTop(40);
        canvases.setWidth100();
        canvases.setHeight("*");
        canvases.setShowEdges(true);
        return canvases;
    }

    public Canvas getCanvas() {
        return hLayout;
    }

    private void drawAccount(Canvas cubeBin, String url) {
        if (url == null || "".equals(url.trim())) {
            url = "blue.png";
        }
        if (cubeBin != null) {
            int width = cubeBin.getWidth();
            final Img img = new Img();
            img.setLeft(Random.nextInt(width - 50));
            img.setTop(Random.nextInt(240));
            img.setWidth(48);
            img.setHeight(48);
            img.setParentElement(cubeBin);
            img.setSrc(url);
            img.setCanDragReposition(true);
//        img.addClickHandler(new ClickHandler() {
//            public void onClick(ClickEvent event) {
//                img.destroy();
//            }
//        });
            img.redraw();
        }
    }
}
