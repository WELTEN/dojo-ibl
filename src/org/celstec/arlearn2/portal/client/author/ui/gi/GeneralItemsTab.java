package org.celstec.arlearn2.portal.client.author.ui.gi;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.grid.events.*;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.AbstractRecord;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GeneralItemModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GameDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GeneralItemDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.generalItem.GeneralItemsClient;
import org.celstec.arlearn2.gwtcommonlib.client.objects.Game;
import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.portal.client.account.AccountManager;
import org.celstec.arlearn2.portal.client.author.ui.VerticalMasterDetailTab;
import org.celstec.arlearn2.portal.client.author.ui.gi.i18.GeneralItemConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONValue;
import com.google.maps.gwt.client.LatLng;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabDeselectedEvent;
import com.smartgwt.client.widgets.tab.events.TabDeselectedHandler;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;

public class GeneralItemsTab extends VerticalMasterDetailTab {

	private static GeneralItemConstants constants = GWT.create(GeneralItemConstants.class);

	private Game game;
	private ListGrid generalItemsGrid;
//	private boolean recordSelection = true;
	GeneralItemMapView mapLayout;// = new GeneralItemMapView();
	private Tab mapTab;
	private LatLng mapCenter;
	private Double zoom;

	private GeneralItemDetail giDetail;
	private TabSet mapListTabSet;

	private HLayout masterLayout;
	private CreateReuseGeneralItem createReuseItemsPane;
	private boolean mapSelected = false;

	public GeneralItemsTab(Game g) {
		super(g.getString(GameModel.GAME_TITLE_FIELD));
		this.game = g;
		createReuseItemsPane = new CreateReuseGeneralItem((GeneralItemDetail) getDetail(), this);
		createGeneralItemsGrid();
		createListMapTabSet();

		setCanClose(true);
		addTabSelectedHandler(new TabSelectedHandler() {

			@Override
			public void onTabSelected(TabSelectedEvent event) {
				if (mapLayout != null) rebuildMapBecauseTabSelected();
			}
		});
		addTabDeselectedHandler(new TabDeselectedHandler() {
			@Override
			public void onTabDeselected(TabDeselectedEvent event) {
				if (mapLayout != null) removeMapItemsBecauseDeselected();
				if (giDetail != null) giDetail.deselect();
			}
		});
	}

	public Game getGame() {
		return game;
	}
	private void rebuildMapBecauseTabSelected() {
			mapLayout = new GeneralItemMapView(game) {
				public void loadItemDetail(GeneralItem gi){
					giDetail.deselect();
					giDetail.viewGeneralItem(gi, true, mapTab);
				}
				public void loadNewCoordinates(LatLng newCoordinates){
					giDetail.loadCoordinates(newCoordinates);
				}

			};
			mapTab.setPane(mapLayout);
			mapLayout.createMap(mapCenter, zoom);
	}
	
	private void removeMapItemsBecauseDeselected() {
			mapLayout.removeMarkers();
			mapCenter = mapLayout.getMapWidget().getMap().getCenter();
			zoom = mapLayout.getMapWidget().getMap().getZoom();
	}

	protected void tabSelect() {
		game = new Game(((AbstractRecord) GameDataSource.getInstance().getRecord(game.getGameId())).getCorrespondingJsonObject());

		masterLayout.removeMembers(masterLayout.getMembers());
		if (game.getMapAvailable()) {
			masterLayout.addMember(mapListTabSet);
            giDetail.setMapAvailable(true);
		} else {
			masterLayout.addMember(generalItemsGrid);
            giDetail.setMapAvailable(false);
		}
		masterLayout.addMember(createReuseItemsPane);
	}

	@Override
	public Canvas getMaster() {
		masterLayout = new HLayout();
		masterLayout.setMembersMargin(5);
		return masterLayout;
	}

	public Canvas createListMapTabSet() {
		mapListTabSet = new TabSet();
		mapListTabSet.setWidth("*");
		mapListTabSet.setTabBarPosition(Side.LEFT);

		Tab lTab1 = new Tab();
		lTab1.setIcon("/images/list_icon.png", 16);
		lTab1.setPane(generalItemsGrid);
		lTab1.addTabSelectedHandler(new TabSelectedHandler() {

			@Override
			public void onTabSelected(TabSelectedEvent event) {
				mapSelected = false;
			}
		});

		mapTab = new Tab();
		mapTab.setIcon("/images/icon_maps.png", 16);

		mapTab.addTabSelectedHandler(new TabSelectedHandler() {

			@Override
			public void onTabSelected(TabSelectedEvent event) {
				rebuildMapBecauseTabSelected();
				mapSelected = true;
			}
		});
		mapTab.addTabDeselectedHandler(new TabDeselectedHandler() {

			@Override
			public void onTabDeselected(TabDeselectedEvent event) {
				if (mapLayout != null) removeMapItemsBecauseDeselected();
				if (giDetail != null) giDetail.deselect();
			}
		});

		mapListTabSet.addTab(lTab1);
		mapListTabSet.addTab(mapTab);

		return mapListTabSet;
	}

	public void deleteRecord(final ListGridRecord record) {
		SC.ask("do you want to delete this item", new BooleanCallback() {

			@Override
			public void execute(Boolean value) {
				if (value) {
					GeneralItemsClient.getInstance().deleteGeneralItem(record.getAttributeAsLong(GameModel.GAMEID_FIELD), record.getAttributeAsLong(GeneralItemModel.GENERALITEMID_FIELD), new JsonCallback() {
						public void onJsonReceived(JSONValue jsonValue) {
							GeneralItemDataSource.getInstance().removeRecordWithKey(record.getAttributeAsLong(GeneralItemModel.GENERALITEMID_FIELD));
						}

					});
				}

			}
		});
	}

	public Canvas createGeneralItemsGrid() {
		generalItemsGrid = new ListGrid() {
			protected Canvas createRecordComponent(final ListGridRecord record, Integer colNum) {
				String fieldName = this.getFieldName(colNum);
				if (fieldName.equals("deleteField")) {
					ImgButton deleteImg = createDeleteImg();
					deleteImg.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							deleteRecord(record);
						}
					});
					return deleteImg;
				}
				return null;
			}
		};
		generalItemsGrid.setShowRecordComponentsByCell(true);

		generalItemsGrid.setShowAllRecords(true);
		generalItemsGrid.setShowRecordComponents(true);
		generalItemsGrid.setCanAcceptDroppedRecords(true);

		generalItemsGrid.setHeight(350);
		generalItemsGrid.setWidth100();
		generalItemsGrid.setHeight100();
		generalItemsGrid.setAutoFetchData(true);
		generalItemsGrid.setCanEdit(false);
        if (AccountManager.getInstance().isAdvancedUser()) {
            generalItemsGrid.setShowFilterEditor(true);
            generalItemsGrid.setAllowFilterExpressions(true);
        }

		initGrid();

		generalItemsGrid.addRecordDropHandler(new RecordDropHandler() {

			@Override
			public void onRecordDrop(RecordDropEvent event) {
				for (final Record r : event.getDropRecords()) {
					GeneralItemsClient.getInstance().getGeneralItem(r.getAttributeAsLong(GameModel.GAMEID_FIELD), r.getAttributeAsLong(GeneralItemModel.ID_FIELD), new JsonCallback() {
						public void onJsonReceived(JSONValue jsonValue) {
							GeneralItemDataSource.getInstance().removeData(r);
							GeneralItem item = GeneralItem.createObject(jsonValue.isObject());
							item.removeAttribute(GeneralItemModel.ID_FIELD);
							item.setLong(GameModel.GAMEID_FIELD, game.getGameId());
							item.removeAttribute("dependsOn");
							item.removeAttribute("disappearOn");
							GeneralItemsClient.getInstance().createGeneralItem(item, new JsonCallback() {
								public void onJsonReceived(JSONValue jsonValue) {
									GeneralItemDataSource.getInstance().loadDataFromWeb(game.getGameId());
								}
							});
						}
					});

				}

			}
		});

		generalItemsGrid.addRecordClickHandler(new RecordClickHandler() {
			public void onRecordClick(RecordClickEvent event) {
					masterRecordClick(event);
			}
		});

        generalItemsGrid.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
            @Override
            public void onRecordDoubleClick(RecordDoubleClickEvent event) {
                masterRecordDoubleClick(event);
            }
        });

		GeneralItemDataSource.getInstance().loadDataFromWeb(game.getGameId());
		Criteria criteria = new Criteria();
		criteria.addCriteria(GameModel.GAMEID_FIELD, game.getGameId());
		criteria.addCriteria(GameModel.DELETED_FIELD, false);
		generalItemsGrid.filterData(criteria);

		// masterList.addRemoveRecordClickHandler(new RemoveRecordClickHandler()
		// {
		//
		// @Override
		// public void onRemoveRecordClick(RemoveRecordClickEvent event) {
		// GeneralItemsClient.getInstance().deleteGeneralItem(masterList.getRecord(event.getRowNum()).getAttributeAsLong(GameModel.GAMEID_FIELD),
		// masterList.getRecord(event.getRowNum()).getAttributeAsLong(GeneralItemModel.ID_FIELD),
		// new JsonCallback() {
		//
		// });
		// }
		// });
		// masterList.addCellClickHandler(new CellClickHandler() {
		// public void onCellClick(CellClickEvent event) {
		//
		// ListGridRecord record = event.getRecord();
		// int colNum = event.getColNum();
		// ListGridField field = masterList.getField(colNum);
		// String fieldName = masterList.getFieldName(colNum);
		// String fieldTitle = field.getTitle();
		// System.out.println("Clicked <b>" + fieldTitle + ":" +
		// record.getAttribute(fieldName) +
		// "</b> (Country:" + record.getAttribute("countryName") + ")");
		//
		// }
		// });
		return generalItemsGrid;
	}

	@Override
	public Canvas getDetail() {
		if (giDetail == null)
			giDetail = new GeneralItemDetail();
		return giDetail;
	}

	protected void initGrid() {
		generalItemsGrid.setDataSource(GeneralItemDataSource.getInstance());
		ListGridField idField = new ListGridField(GeneralItemModel.ID_FIELD, "id ");
		idField.setWidth(60);
		idField.setCanEdit(false);
		idField.setHidden(true);

		ListGridField orderField = new ListGridField(GeneralItemModel.SORTKEY_FIELD, constants.order());
		orderField.setWidth(40);

		ListGridField giTitleField = new ListGridField(GeneralItemModel.NAME_FIELD, constants.title());
		ListGridField simpleNameField = new ListGridField(GeneralItemModel.SIMPLE_NAME_FIELD, constants.simpleName());

		ListGridField deleteField = new ListGridField(GeneralItemModel.DELETE_ICON, " ");
        deleteField.setWidth(20);
        deleteField.setAlign(Alignment.CENTER);
        deleteField.setType(ListGridFieldType.IMAGE);
        deleteField.setImageURLSuffix(".png");



        if (AccountManager.getInstance().isAdvancedUser()) {
            ListGridField tagsField = new ListGridField(GeneralItemModel.TAGS, constants.tags());
            ListGridField sectionField = new ListGridField(GeneralItemModel.SECTION, constants.section());
            generalItemsGrid.setFields( idField, orderField, giTitleField, simpleNameField, tagsField, sectionField, deleteField );
        } else {
		    generalItemsGrid.setFields(idField, orderField, giTitleField, simpleNameField, deleteField );
        }
        generalItemsGrid.addCellClickHandler(new CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                if (GeneralItemModel.DELETE_ICON.equals(generalItemsGrid.getFieldName(event.getColNum()))) {
                    GeneralItemsTab.this.deleteRecord(event.getRecord());
                }
            }
        });

	}

	protected void masterRecordClick(RecordClickEvent event) {
		giDetail.viewGeneralItem(recordToGeneralItem(event.getRecord()), true, mapTab);
	}

    protected void masterRecordDoubleClick(RecordDoubleClickEvent event) {
        GeneralItem gi =  recordToGeneralItem(event.getRecord());
//        giDetail.viewGeneralItem(gi, true, mapTab);
        giDetail.editGeneralItem(gi, mapTab);
    }

	public static GeneralItem recordToGeneralItem(Record record) {
		String idAsString = record.getAttributeAsString(GeneralItemModel.ID_FIELD);
//		return GeneralItem.createObject(((AbstractRecord) GeneralItemDataSource.getInstance().getRecord(record.getAttributeAsLong(GeneralItemModel.ID_FIELD))).getCorrespondingJsonObject());
		return GeneralItem.createObject(((AbstractRecord) GeneralItemDataSource.getInstance().getRecord(Long.parseLong(idAsString))).getCorrespondingJsonObject());
	}

	public void hideDetail() {
		// // giDetail.setVisibility(Visibility.HIDDEN);
		// mapLayout.destroy();
		giDetail.eraseView();
	}

	public boolean mapSelected() {
		return mapSelected;
	}

	public LatLng getMapCenter() {
		if (mapLayout != null) return mapLayout.mapWidget.getMap().getCenter();
		return null;
	}

	public void addMarker(GeneralItem gi) {
		if (mapLayout != null) { {
			Double lat = gi.getDouble(GeneralItemModel.LAT_FIELD);
			Double lng = gi.getDouble(GeneralItemModel.LNG_FIELD);
			if (lat != null && lng != null) {
			LatLng latlng = LatLng.create(lat, lng);
				mapLayout.addMarker(-1*System.currentTimeMillis(), 
						gi.getString(GeneralItemModel.NAME_FIELD), 
						latlng,
						gi);
				}
			}
		}
		
	}
}
