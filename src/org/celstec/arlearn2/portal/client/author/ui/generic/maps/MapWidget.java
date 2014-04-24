package org.celstec.arlearn2.portal.client.author.ui.generic.maps;

//import org.celstec.arlearn2.mobileclient.client.common.datasource.mobile.GenericDataSource.MyAbstractRecord;
//import org.celstec.arlearn2.mobileclient.client.images.ImageResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.MapTypeId;
import com.google.maps.gwt.client.MarkerOptions;
import com.google.maps.gwt.client.ZoomControlOptions;
import com.google.maps.gwt.client.ZoomControlStyle;

public class MapWidget extends Composite {

	interface MapWidgetUiBinder extends UiBinder<Widget, MapWidget> {
	}

	private MapWidgetUiBinder uiBinder = GWT.create(MapWidgetUiBinder.class);

	@UiField
	HTMLPanel mapContainer;

	private GoogleMap map;
	private MarkerOptions newMarkerOpts;

	private static MapWidget instance;

	public static GoogleMap getMapInstance() {
		if (instance != null) {
			return instance.map;
		}
		return null;
	}
	public static MapWidget getInstance() {
		if (instance == null) {
			instance = new MapWidget();
		} else {
			instance.removeFromParent();
		}
		return instance;// new MapWidget();
	}

	private MapWidget() {
		
		initWidget(uiBinder.createAndBindUi(this));
		MapOptions opts = MapOptions.create();
		opts.setScaleControl(true);
		opts.setZoom(5.0);
		opts.setMapTypeId(MapTypeId.ROADMAP);
		opts.setCenter(LatLng.create(51, 5.7266));
		opts.setZoom(8.0);

		ZoomControlOptions myZoomOptions = ZoomControlOptions.create();
		myZoomOptions.setStyle(ZoomControlStyle.LARGE);
		opts.setZoomControlOptions(myZoomOptions);
		map = GoogleMap.create(mapContainer.getElement(), opts);

		newMarkerOpts = MarkerOptions.create();

		newMarkerOpts.setMap(map);
//		newMarkerOpts
//				.setIcon(ImageResources.INSTANCE.multipleChoice().getURL());
		newMarkerOpts.setTitle("test");

		setHeight("" + Window.getClientHeight());
	}

	public void resize() {
		map.triggerResize();
	}
	
	public GoogleMap getMap() {
		return map;
	}

}