package org.celstec.arlearn2.portal.client.author.ui.gi;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.Callback;
import com.google.gwt.geolocation.client.Geolocation;
import com.google.gwt.geolocation.client.Position;
import com.google.gwt.geolocation.client.PositionError;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.AbstractRecord;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GeneralItemModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GeneralItemDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Game;
import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.portal.client.author.ui.generic.maps.MapWidget;

import com.google.gwt.json.client.JSONValue;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.LatLngBounds;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.Marker.DragEndHandler;
import com.google.maps.gwt.client.MarkerOptions;
import com.google.maps.gwt.client.MouseEvent;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.layout.VLayout;

public class GeneralItemMapView extends VLayout {
	private Game game;
	MapWidget mapWidget;
	private HashMap<Long, Marker> markerMap = new HashMap<Long, Marker>();
	public GeneralItemMapView(Game game) {
		this.game = game;
		setBorder("1px solid gray");
		setCanDragResize(true);

	}

	public void createMap(LatLng mapCenter, Double zoom) {
		// Canvas layout = new Canvas();


		boolean makeFitBounds = false;
		mapWidget = MapWidget.getInstance();
		mapWidget.setSize("100%", "100%");
		if (mapCenter != null ) {

			mapWidget.getMap().setCenter(mapCenter);
			mapWidget.getMap().setZoom(zoom);
		} else {
            Geolocation geolocation = Geolocation.getIfSupported();
            if (geolocation != null) {
                Geolocation.getIfSupported().getCurrentPosition(new Callback<Position, PositionError>() {
                    @Override
                    public void onFailure(PositionError reason) {

                    }

                    @Override
                    public void onSuccess(Position result) {
                        mapWidget.getMap().setCenter(LatLng.create(result.getCoordinates().getLatitude(),result.getCoordinates().getLongitude()));
                    }
                });
            }
			makeFitBounds = true;
		}
		addMember(mapWidget);
		loadGeneralItems(makeFitBounds);
	}

	public MapWidget getMapWidget() {
		return mapWidget;
	}

	public void loadGeneralItems(boolean makeFitBounds) {
		LatLngBounds bounds = LatLngBounds.create();
		Criteria criteria = new Criteria();
		criteria.addCriteria(GameModel.GAMEID_FIELD, game.getGameId());
		criteria.addCriteria(GameModel.DELETED_FIELD, false);
		HashMap<Long, Record> records = GeneralItemDataSource.getInstance().getRecords(game.getGameId());
		if (records != null) {
			for (Map.Entry<Long, Record> rec : records.entrySet()) {
				GeneralItem gi = recordToGeneralItem(rec.getValue());
				if (!gi.getBoolean(GameModel.DELETED_FIELD)) {
					Double lat = rec.getValue().getAttributeAsDouble(GeneralItemModel.LAT_FIELD);
					Double lng = rec.getValue().getAttributeAsDouble(GeneralItemModel.LNG_FIELD);
					if (lat != null && lng != null) {
						LatLng latlng = LatLng.create(lat, lng);
						addMarker(rec.getValue().getAttributeAsLong(GeneralItemModel.GENERALITEMID_FIELD), 
							rec.getValue().getAttributeAsString(GeneralItemModel.NAME_FIELD), 
							latlng,
							gi);
						bounds = bounds.extend(latlng);
					}
				}
			}
		}
		
		if (makeFitBounds) {
			mapWidget.getMap().setZoom(12);
			mapWidget.getMap().fitBounds(bounds);
		}
		
	}

	public void addMarker(final Long id, String name, LatLng latlng, final GeneralItem gi) {
//		if (lat == null)
//			lat = 0d;
//		if (lng == null)
//			lng = 0d;
		if (!markerMap.containsKey(id)) {
			MapOptions myOptions = MapOptions.create();
			myOptions.setZoom(4.0);
			myOptions.setCenter(LatLng.create(-25.363882, 131.044922));
			myOptions.setMapTypeId(MapTypeId.ROADMAP);

			MarkerOptions newMarkerOpts = MarkerOptions.create();
			newMarkerOpts.setPosition(latlng);
			newMarkerOpts.setMap(mapWidget.getMap());
			String iconUrl = gi.getString("iconUrl") ;
			if ("".equals(iconUrl)) {
				iconUrl = "https://chart.googleapis.com/chart?chst=d_bubble_text_small&chld=bb|" + name + "|C6EF8C|000000";
			} 
			newMarkerOpts.setIcon(iconUrl);
			newMarkerOpts.setDraggable(true);

			Marker marker = Marker.create(newMarkerOpts);
			marker.addClickListener(new com.google.maps.gwt.client.Marker.ClickHandler(){

				@Override
				public void handle(MouseEvent arg0) {
					loadItemDetail(gi);
				}
				
			});
			marker.addDragEndListener(new DragEndHandler() {

				@Override
				public void handle(MouseEvent mouseEvent) {
					LatLng latlng = mouseEvent.getLatLng();
					gi.setDouble(GeneralItemModel.LAT_FIELD, latlng.lat());
					gi.setDouble(GeneralItemModel.LNG_FIELD, latlng.lng());
					gi.writeToCloud(new JsonCallback(){
						public void onJsonReceived(JSONValue jsonValue) {
							GeneralItemDataSource.getInstance().loadDataFromWeb(gi.getValueAsLong(GameModel.GAMEID_FIELD));
						}

					});
					loadNewCoordinates(latlng);
				}
			});
			markerMap.put(id, marker);
		}

	}

	private GeneralItem recordToGeneralItem(Record record) {
		return GeneralItem.createObject(((AbstractRecord) GeneralItemDataSource.getInstance().getRecord(record.getAttributeAsLong(GeneralItemModel.ID_FIELD))).getCorrespondingJsonObject());
	}

	public void removeMarkers() {
		for (Marker m : markerMap.values()) {
			m.setMap((GoogleMap) null);
		}

	}
	
//	public void setMapCenter(LatLng center) {
//		
//	}
	public void loadItemDetail(GeneralItem gi){
		
	}
	
	public void loadNewCoordinates(LatLng newCoordinates){}

	

		

}
