package org.celstec.arlearn2.portal.client.author.ui.gi;

import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;

import com.google.maps.gwt.client.LatLng;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.LayoutSpacer;
import com.smartgwt.client.widgets.tab.Tab;

public class GeneralItemDetail extends HLayout {

	private GeneralItemDetailView view;
	private GeneralItemDetailEdit edit;
    private LayoutSpacer spacer;

	public GeneralItemDetail() {
		spacer = new LayoutSpacer();
		spacer.setWidth(24);
		addMember(spacer);
	}

    public void setMapAvailable(boolean mapAvailable) {
       if (mapAvailable) {
           spacer.setWidth(24);
       } else {
           spacer.setWidth(0);
       }
    }

	public void viewGeneralItem(final GeneralItem gi, final boolean canEdit, final Tab mapTab) {
		eraseView();
		view = new GeneralItemDetailView(canEdit) {
			protected void editClick() {
				editGeneralItem(gi, mapTab);
			}
		};
		view.loadGeneralItem(gi);
		addMember(view);
	}
	
	public void loadCoordinates(LatLng newCoordinates){
		if (edit != null) edit.coordinatesChanged(newCoordinates);
	}
	
	public void editGeneralItem(final GeneralItem gi, final Tab mapTab) {
		eraseView();

		edit = new GeneralItemDetailEdit(mapTab){
			protected boolean saveClick() {
				boolean res = super.saveClick();
				if (res) viewGeneralItem(gi, true, mapTab);
				return res;
			}
		};
		edit.loadGeneralItem(gi);
		addMember(edit);
	}
	
	public void eraseView() {
		if (view != null) removeMember(view);
		if (edit != null) removeMember(edit);
	}

	public void deselect() {
		if (edit != null) edit.deselect();
		
	}


}
