package org.celstec.arlearn2.portal.client.author.ui.gi.dependency.forms;

import org.celstec.arlearn2.portal.client.author.ui.generic.maps.MapWidget;
import org.celstec.arlearn2.portal.client.author.ui.gi.dependency.nodes.ProximityDependencyNode;

import com.google.maps.gwt.client.Circle;
import com.google.maps.gwt.client.CircleOptions;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.Marker.DragEndHandler;
import com.google.maps.gwt.client.MarkerOptions;
import com.google.maps.gwt.client.MouseEvent;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.VStack;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeNode;


public class ProximityDependencyEditor extends VStack {


    protected TextItem radiusTextItem;
    protected TextItem latTextItem;
	protected TextItem lngTextItem;
	protected DynamicForm form;

	private final String LAT = "lat";
	private final String LNG = "lng";
    private final String RADIUS = "radius";

	protected Marker marker;
	protected CircleOptions populationOptions;
	protected Circle circle;

	protected LatLng location;

    private TreeNode proxTreeNode;
	private Tree proxTree;
	private Tab mapTab;
	
	public ProximityDependencyEditor(Tab mapTab) {
		this.mapTab = mapTab;
		createRadiusTextEdit();
		createLatTextEdit();
		createLngTextEdit();
		setPadding(10);
        radiusTextItem.addChangedHandler(new ChangedHandler() {
            @Override
            public void onChanged(ChangedEvent event) {
                try {
                int value = Integer.parseInt(""+event.getValue());
                if (circle != null) circle.setRadius(value);
                onSave();
                } catch (Exception e) {
                }
            }
        });
		form = new DynamicForm();
		form.setFields(radiusTextItem, latTextItem, lngTextItem);
//		addMember(radiusSlider);
		addMember(form);
		
//		setWidth100();
		ProximityDependencyEditor.this.setVisibility(Visibility.HIDDEN);

	}
	
	private void createLngTextEdit() {
		latTextItem = new TextItem(LAT);
		latTextItem.setTitle("latitude");
		latTextItem.setWrapTitle(false);
		latTextItem.setStartRow(true);
//		latTextItem.addChangedHandler(new ChangedHandler() {
//
//			@Override
//			public void onChanged(ChangedEvent event) {
//				onSave();
//			}
//		});

        latTextItem.addChangedHandler(new ChangedHandler() {
            @Override
            public void onChanged(ChangedEvent event) {
                try {
                    double value = Double.parseDouble(""+event.getValue());
                    if (circle != null) circle.setCenter(LatLng.create(value, circle.getCenter().lng()));
                    if (marker != null) marker.setPosition(LatLng.create(value, circle.getCenter().lng()));

                    onSave();
                } catch (Exception e) {
                }
            }
        });
	}

	private void createLatTextEdit() {
		lngTextItem = new TextItem(LNG);
		lngTextItem.setTitle("longitude");
		lngTextItem.setWrapTitle(false);
		lngTextItem.setStartRow(true);
		lngTextItem.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				onSave();
			}
		});
        lngTextItem.addChangedHandler(new ChangedHandler() {
            @Override
            public void onChanged(ChangedEvent event) {
                try {
                    double value = Double.parseDouble(""+event.getValue());
                    if (circle != null) circle.setCenter(LatLng.create(circle.getCenter().lat(), value));
                    if (marker != null) marker.setPosition(LatLng.create(circle.getCenter().lat(), value));
                        onSave();
                } catch (Exception e) {
                }
            }
        });

    }

	public void createRadiusTextEdit(){
        radiusTextItem = new TextItem(RADIUS);
        radiusTextItem.setTitle("radius (m)");
        radiusTextItem.setWrapTitle(false);
        radiusTextItem.setStartRow(true);


        radiusTextItem.addChangedHandler(new ChangedHandler() {
            @Override
            public void onChanged(ChangedEvent event) {
                try {
                    int value = Integer.parseInt(""+event.getValue());
                    if (circle != null) circle.setRadius(value);
                    onSave();
                } catch (Exception e) {
                }
            }
        });

//		radiusSlider = new Slider("radius");
//		radiusSlider.setVertical(false);
//		radiusSlider.setVertical(false);
//		radiusSlider.setMinValue(10);
//		radiusSlider.setMaxValue(100);
//		radiusSlider.setWidth100();

	}

	public void setLocation(LatLng location) {
		this.location = location;
		
	}
	
	public void onSave() {
		if (proxTreeNode == null)
			return;
		
		for (TreeNode tn : proxTree.getChildren(proxTreeNode)) {
			if (tn.getAttribute("type").equals(ProximityDependencyNode.LAT) && form.getValue(LAT) != null) {
				tn.setAttribute("Name", "Lat = " + form.getValue(LAT));
				tn.setAttribute("Value", ""+form.getValue(LAT));
			}
			if (tn.getAttribute("type").equals(ProximityDependencyNode.LNG)&& form.getValue(LNG)  != null) {
				tn.setAttribute("Name", "Lng = " + form.getValue(LNG));
				tn.setAttribute("Value", ""+form.getValue(LNG));
			}
//			if (tn.getAttribute("type").equals(ProximityDependencyNode.RADIUS)) {
//				tn.setAttribute("Name", "Radius = " + radiusSlider.getValue());
//				tn.setAttribute("Value", ""+radiusSlider.getValue());
//			}

            if (tn.getAttribute("type").equals(ProximityDependencyNode.RADIUS)) {
                tn.setAttribute("Name", "Radius = " + form.getValue(RADIUS));
                tn.setAttribute("Value", ""+form.getValue(RADIUS));
            }
		}

//		proxTreeNode = null;
	}
	
	
	
	private void drawMarker(GoogleMap map) {
		MarkerOptions newMarkerOpts = MarkerOptions.create();
		newMarkerOpts.setPosition(location);
		newMarkerOpts.setMap(map);
		newMarkerOpts.setDraggable(true);
		
		
		if (marker == null) {
            marker = Marker.create(newMarkerOpts);

            marker.addDragEndListener(new DragEndHandler() {

                @Override
                public void handle(MouseEvent event) {
                    latTextItem.setValue(event.getLatLng().lat());
                    lngTextItem.setValue(event.getLatLng().lng());
                    location = event.getLatLng();
                    circle.setCenter(ProximityDependencyEditor.this.location);
                    onSave();

                }
            });
        }
        if (circle == null) {
            populationOptions = CircleOptions.create();
            populationOptions.setStrokeColor("#ff0000");
            populationOptions.setStrokeOpacity(0.8);
            populationOptions.setStrokeWeight(2);
            populationOptions.setFillColor("#ff0000");
            populationOptions.setFillOpacity(0.35);
            populationOptions.setMap(map);
            populationOptions.setCenter(location);
            populationOptions.setRadius(5);
            circle = Circle.create(populationOptions);
        }
	}
	public void showProximityDependencyNode() {
//		map = MapWidget.getInstance();
//		addMember(map);


        mapTab.getTabSet().selectTab(mapTab);
		if (location == null) location = MapWidget.getMapInstance().getCenter();
		if (latTextItem.getValue()!= null && !"".equals(latTextItem.getValue())) {
			double lat = Double.parseDouble(latTextItem.getValueAsString());
			location = LatLng.create(lat, location.lng());
		}
		if (lngTextItem.getValue()!= null && !"".equals(lngTextItem.getValue())) {
			double lng = Double.parseDouble(lngTextItem.getValueAsString());
			location = LatLng.create(location.lat(), lng);
		}
		drawMarker(MapWidget.getMapInstance());
        if (radiusTextItem.getValue()!= null && !"".equals(radiusTextItem.getValue())) {
            double radius = Double.parseDouble(radiusTextItem.getValueAsString());
            circle.setRadius(radius);
        }
		ProximityDependencyEditor.this.setVisibility(Visibility.INHERIT);

	}

	public void hideProximityDependencyNode() {
		ProximityDependencyEditor.this.setVisibility(Visibility.HIDDEN);
		if (marker != null) marker.setMap((GoogleMap)null);
		if (circle != null) circle.setMap((GoogleMap)null);
		marker = null;
		circle = null;
	}

	public void setTreeNode(TreeNode tn, Tree tree) {
		proxTreeNode = tn;
		proxTree = tree;
		for (TreeNode node : proxTree.getChildren(proxTreeNode)) {
			if (node.getAttribute("type")!= null && node.getAttribute("type").equals(ProximityDependencyNode.RADIUS)) {	
				if (node.getAttribute("Value") != null) radiusTextItem.setValue(""+(int) Float.parseFloat(node.getAttributeAsString("Value")));
			}
			if (node.getAttribute("type")!= null && node.getAttribute("type").equals(ProximityDependencyNode.LAT)) {	
				if (node.getAttribute("Value") != null)latTextItem.setValue(node.getAttribute("Value"));
			}
			if (node.getAttribute("type")!= null && node.getAttribute("type").equals(ProximityDependencyNode.LNG)) {	
				if (node.getAttribute("Value") != null)lngTextItem.setValue(node.getAttribute("Value"));
			}
		}
	}

	public void removeMarkers() {
		if (marker != null) marker.setMap((GoogleMap) null);
		if (circle != null) circle.setMap((GoogleMap) null);
	}
}
