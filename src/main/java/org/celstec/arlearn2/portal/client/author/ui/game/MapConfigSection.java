package org.celstec.arlearn2.portal.client.author.ui.game;

import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Game;
import org.celstec.arlearn2.portal.client.author.ui.SectionConfig;

import com.google.gwt.core.client.GWT;
import org.celstec.arlearn2.portal.client.author.ui.game.i18.GameConstants;
import org.celstec.arlearn2.portal.client.author.ui.generic.canvas.RichTextCanvas;

import java.util.LinkedHashMap;

public class MapConfigSection extends SectionConfig {


    private GameConstants constants = GWT.create(GameConstants.class);



    RadioGroupItem messageViewOptions;
    RadioGroupItem mapOptions;

    DynamicForm messageViewTypeForm;
    DynamicForm mapTypeForm;
    RichTextCanvas canvas;

    Game game;


    public MapConfigSection() {
        super("Maps, message lists, etc");
        HStack layout = new HStack();

        layout.addMember(getAccessForm());

        LayoutSpacer vSpacer = new LayoutSpacer();
        vSpacer.setWidth(10);
        layout.addMember(vSpacer);
        layout.addMember(getMapTypesForm());

        vSpacer.setWidth(10);
        layout.addMember(vSpacer);
        layout.addMember(getCustomHtmlForm());

        layout.setAlign(Alignment.CENTER);
        layout.setPadding(5);
        setItems(layout);
    }



    private Canvas getAccessForm() {
        messageViewOptions = new RadioGroupItem();
        messageViewOptions.setName("messageViews");
        messageViewOptions.setTitle(constants.messageViews());
        messageViewOptions.setValueMap(new String[]{
                constants.messagesOnly(),
                constants.messagesAndMap(),
                constants.mapViewOnly(),
                constants.customHtml()});
        messageViewOptions.addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {
                int messageViewType = Game.MESSAGE_MAP;
                if (event.getValue() != null) {
                    if (event.getValue().equals(constants.messagesOnly())) {
                        messageViewType = Game.MESSAGE_LIST;
                        mapTypeForm.setVisibility(Visibility.HIDDEN);

                        canvas.setVisibility(Visibility.HIDDEN);


                    } else if (event.getValue().equals(constants.messagesAndMap())) {
                        messageViewType = Game.MESSAGE_MAP;
                        mapTypeForm.setVisibility(Visibility.INHERIT);
                        canvas.setVisibility(Visibility.HIDDEN);


                    } else if (event.getValue().equals(constants.mapViewOnly())) {
                        messageViewType = Game.MAP_VIEW;
                        mapTypeForm.setVisibility(Visibility.INHERIT);
                        canvas.setVisibility(Visibility.HIDDEN);

                    } else if (event.getValue().equals(constants.customHtml())) {
                        messageViewType = Game.CUSTOM_HTML;
                        mapTypeForm.setVisibility(Visibility.HIDDEN);
                        canvas.setVisibility(Visibility.INHERIT);

                    }
                }
                game.setMessageView(messageViewType);
                game.writeToCloud(new JsonCallback() {
                    public void onJsonReceived(JSONValue jsonValue) {

                    }

                });
            }
        });
        messageViewTypeForm = new DynamicForm();
        messageViewTypeForm.setGroupTitle(constants.selectMessageView());
        messageViewTypeForm.setIsGroup(true);
        messageViewTypeForm.setWidth(300);

        messageViewTypeForm.setFields(messageViewOptions);
        return messageViewTypeForm;
    }


    private Canvas getMapTypesForm() {
        LinkedHashMap<String, String> licenseMap = new LinkedHashMap<String, String>();
        licenseMap.put("GoogleMaps", constants.googleMaps());
        licenseMap.put("OSM", constants.osm());

        mapOptions = new RadioGroupItem();
        mapOptions.setName("mapType");
        mapOptions.setTitle(constants.mapTypes());
        mapOptions.setValueMap(licenseMap);

        mapOptions.addChangedHandler(new ChangedHandler() {

            @Override
            public void onChanged(ChangedEvent event) {

            }
        });

        mapTypeForm = new DynamicForm();
        mapTypeForm.setGroupTitle(constants.selectMapType());
        mapTypeForm.setIsGroup(true);
        mapTypeForm.setFields();
        mapTypeForm.setWidth(500);
        mapTypeForm.setVisibility(Visibility.HIDDEN);
        mapTypeForm.setFields(mapOptions);

        return mapTypeForm;
    }

    private Canvas getCustomHtmlForm() {
        canvas = new RichTextCanvas("", "", new RichTextCanvas.HtmlSaver() {

            @Override
            public void htmlReady(String html) {
                if (game != null) {
                    game.setHtmlMessageList(html);
                    game.writeToCloud(new JsonCallback(){
                        public void onJsonReceived(JSONValue jsonValue) {
                        }
                    });
                }
            }
        }, 500);
        canvas.getForm().setIsGroup(true);
        canvas.getForm().setGroupTitle("Custom html");
        canvas.setVisibility(Visibility.HIDDEN);
        return canvas;  //To change body of created methods use File | Settings | File Templates.
    }



    private void setMessageViewType(int messageViewType) {
        if (messageViewOptions != null) {
            switch (messageViewType) {
                case Game.MESSAGE_LIST:
                    messageViewOptions.setValue(constants.messagesOnly());
                    mapTypeForm.setVisibility(Visibility.HIDDEN);
                    canvas.setVisibility(Visibility.HIDDEN);
                    break;
                case Game.MAP_VIEW:
                    messageViewOptions.setValue(constants.mapViewOnly());
                    mapTypeForm.setVisibility(Visibility.INHERIT);
                    canvas.setVisibility(Visibility.HIDDEN);
                    break;
                case Game.MESSAGE_MAP:
                    messageViewOptions.setValue(constants.messagesAndMap());
                    mapTypeForm.setVisibility(Visibility.INHERIT);
                    canvas.setVisibility(Visibility.HIDDEN);
                    break;
                case Game.CUSTOM_HTML:
                    messageViewOptions.setValue(constants.customHtml());
                    mapTypeForm.setVisibility(Visibility.HIDDEN);
                    canvas.setVisibility(Visibility.INHERIT);
                    break;
            }
            messageViewTypeForm.redraw();
        }
    }

    public void loadDataFromRecord(Game game) {
        this.game = game;
        game.getMessageViews();
        Integer messageViewType = game.getMessageViews();
        canvas.updateHtml(game.getHtmlMessageList());

        setMessageViewType(messageViewType);
    }
}
