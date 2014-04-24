package org.celstec.arlearn2.portal.client.author.ui.gi;


import org.celstec.arlearn2.gwtcommonlib.client.datasource.GameModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GeneralItemModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.QueryGeneralItemDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.generalItem.GeneralItemsClient;
import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.portal.client.author.ui.gi.i18.GeneralItemConstants;
import org.celstec.arlearn2.portal.client.author.ui.gi.modal.GeneralItemWindow;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONValue;
import com.google.maps.gwt.client.LatLng;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;

public class CreateReuseGeneralItem extends VLayout {
	private static GeneralItemConstants constants = GWT.create(GeneralItemConstants.class);

	QueryGeneralItemDataSource searchDs = new QueryGeneralItemDataSource();
	GeneralItemsTab generalItemsTab;
	
	public CreateReuseGeneralItem(final GeneralItemDetail giDetail, GeneralItemsTab generalItemsTab) {
		this.generalItemsTab = generalItemsTab;
		setWidth(300);
		setPadding(5);
		setMembersMargin(10);

		setBorder("1px solid #d6d6d6");

		CreateItemForm cif = new CreateItemForm();
		cif.setWidth100();
		addMember(cif);
		SearchForm sf = new SearchForm();
		sf.setWidth100();
		addMember(sf);
		ListGrid lg = new ListGrid();
		lg.setDataSource(searchDs);
		lg.setAutoFetchData(true);
		lg.setCanDragRecordsOut(true);


		lg.setHeight("*");

		ListGridField titlefield = new ListGridField(GeneralItemModel.NAME_FIELD, constants.title());
		
		lg.addRecordClickHandler(new RecordClickHandler() {
			public void onRecordClick(RecordClickEvent event) {
				long itemId = event.getRecord().getAttributeAsLong(GeneralItemModel.ID_FIELD);
				long gameId = event.getRecord().getAttributeAsLong(GameModel.GAMEID_FIELD);
				GeneralItemsClient.getInstance().getGeneralItem(gameId, itemId, new JsonCallback(){
					public void onJsonReceived(JSONValue jsonValue) {
						GeneralItem gi = GeneralItem.createObject
								(jsonValue.isObject());
						giDetail.viewGeneralItem(gi, false, null);
					}

				});

			}
		});
		lg.setFields(new ListGridField[] { titlefield });
		
		
		addMember(lg);
	}
	
	class CreateItemForm extends DynamicForm {
		public CreateItemForm() {
			setNumCols(6);

			final SelectItem itemTypeSelect = new SelectItem("itemType");
			itemTypeSelect.setShowTitle(false);
			itemTypeSelect.setColSpan(5);
			itemTypeSelect.setEndRow(false);
			itemTypeSelect.setStartRow(true);
			itemTypeSelect.setWidth(220);
			String [] itemTypes = GeneralItemsManagement.getItemTypes(false);
			itemTypeSelect.setValueMap(itemTypes);
			
			itemTypeSelect.setPickListWidth(220);
			itemTypeSelect.setValueField("itemType");
			itemTypeSelect.setValue(itemTypes[0]);
			

			ButtonItem findItem = new ButtonItem(constants.create());
			findItem.setStartRow(false);
			findItem.setEndRow(true);

			findItem.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
				
				@Override
				public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
					LatLng coordinate = null;
					if (generalItemsTab.mapSelected())  {
						coordinate = generalItemsTab.getMapCenter();
					}
					GeneralItemWindow.initWindow(itemTypeSelect.getValueAsString(), CreateReuseGeneralItem.this.generalItemsTab, coordinate);
				}
			});

			setItems(itemTypeSelect, findItem);
		}
	}

	class SearchForm extends DynamicForm {

		private ButtonItem findItem;

		public SearchForm() {
			setNumCols(6);

			final TextItem searchText = new TextItem("searchValue");
			searchText.setShowTitle(false);
			searchText.setColSpan(5);
			searchText.setEndRow(false);
			searchText.setStartRow(true);
			searchText.setWidth(220);
			searchText.addKeyPressHandler(new KeyPressHandler() {
				
				@Override
				public void onKeyPress(KeyPressEvent event) {
					if ( event.getKeyName().equals("Enter")) {
						searchDs.search(searchText.getValueAsString());

					}
					
				}
			});


			findItem = new ButtonItem(constants.find());
			findItem.setIcon("/images/find.png");
			findItem.setStartRow(false);
			findItem.setEndRow(true);

			findItem.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
				@Override
				public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
					searchDs.search(searchText.getValueAsString());
				}
			});

			setItems(searchText, findItem);

		}

	}
}
