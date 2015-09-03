package org.celstec.arlearn2.portal.client.author.ui;

import org.celstec.arlearn2.gwtcommonlib.client.ui.grid.GenericListGrid;

import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;

public abstract class ListMasterSectionSectionStackDetailTab extends VerticalMasterDetailTab {

	private ListGrid masterList;
	private SectionConfiguration configuration;

	public ListMasterSectionSectionStackDetailTab(String name) {
		super(name);
	}

	protected abstract void initGrid();

	protected abstract void initConfigSections();

	protected abstract void masterRecordClick(RecordClickEvent event);

	protected abstract void deleteItem(ListGridRecord rollOverRecord);

//	protected Canvas createRecordComponent2(final ListGridRecord record, Integer colNum, String fieldName) {
//		if (fieldName.equals("deleteField")) {
//			ImgButton deleteImg = createDeleteImg();
//			deleteImg.addClickHandler(new ClickHandler() {
//				public void onClick(ClickEvent event) {
//					ListMasterSectionSectionStackDetailTab.this
//							.deleteItem(record);
//				}
//			});
//			return deleteImg;
//		}
//		return null;
//	}
	
	@Override
	public Canvas getMaster() {
		masterList = new GenericListGrid(false, false, false, false, false);
//        {
//			@Override
//			protected Canvas createRecordComponent(final ListGridRecord record, Integer colNum) {
//				return createRecordComponent2(record, colNum,  this.getFieldName(colNum));
//			}
//		};
		masterList.setShowRecordComponentsByCell(true);
		masterList.setShowRollOverCanvas(false);

		masterList.setShowAllRecords(true);
		masterList.setShowRecordComponents(true);

		masterList.setHeight(350);
		masterList.setWidth100();
		masterList.setHeight("40%");
		masterList.setAutoFetchData(true);
		masterList.setCanEdit(true);
		initGrid();

		masterList.addRecordClickHandler(new RecordClickHandler() {
			public void onRecordClick(RecordClickEvent event) {
				masterRecordClick(event);
			}
		});
		return masterList;
	}

	@Override
	public Canvas getDetail() {
		configuration = new SectionConfiguration();
		initConfigSections();
		return configuration;
	}
	
	public void hideDetail() {
		configuration.setVisibility(Visibility.HIDDEN);
	}
	
	public void showDetail() {
		configuration.setVisibility(Visibility.INHERIT);
	}

	public void addSectionDetail(SectionConfig detail) {
		configuration.add(detail);
	}

	public ListGrid getMasterListGrid() {
		return masterList;
	}

	protected SectionConfiguration getSectionConfiguration() {
		return configuration;
	}

}
