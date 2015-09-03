package org.celstec.arlearn2.portal.client.author.ui.gi;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.viewer.DetailViewer;
import com.smartgwt.client.widgets.viewer.DetailViewerField;
import com.smartgwt.client.widgets.viewer.DetailViewerRecord;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.GeneralItemModel;
import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import org.celstec.arlearn2.portal.client.author.ui.gi.i18.GeneralItemConstants;

import java.util.LinkedHashMap;

public class GeneralItemDetailView extends VLayout {
    private static GeneralItemConstants constants = GWT.create(GeneralItemConstants.class);

    private SectionStack extendedStack;
    private SectionStack basicStack;
    private DetailViewer basicMetadataDetailViewer;
    private DetailViewer extendedMetadataDetailViewer;

	protected HLayout buttonLayout;
	protected IButton editButton;
	
	public GeneralItemDetailView(boolean canEdit) {
		if (canEdit) createEditButton();
		if (canEdit) createButtonLayout(editButton);

		createMetadataViews();
        createExtendedLayout();
        createBasicLayout();
		setHeight100();
		
		HLayout layout = new HLayout();
        layout.setCanDragResize(true);
		layout.addMember(extendedStack);
        layout.addMember(basicStack);
        setAlign(Alignment.LEFT);

		setBorder("1px solid #d6d6d6");
		setPadding(5);

		addMember(layout);
		if (canEdit) addMember(buttonLayout);
	}

    private void createBasicLayout() {
        basicStack = new SectionStack();
        SectionStackSection stackSection = new SectionStackSection();
        stackSection.setTitle(constants.basicMetadata());
        stackSection.setItems(basicMetadataDetailViewer);
        basicStack.addSection(stackSection);
        stackSection.setExpanded(true);
        stackSection.setCanCollapse(false);

    }
    private void createExtendedLayout() {
        extendedStack = new SectionStack();
        SectionStackSection extendedSection = new SectionStackSection();
        extendedSection.setTitle(constants.specificMetadata());
        extendedSection.setItems(extendedMetadataDetailViewer);
        extendedStack.addSection(extendedSection);
        extendedSection.setExpanded(true);
        extendedSection.setCanCollapse(false);
        extendedStack.setShowResizeBar(true);
    }
	
	private void createMetadataViews() {

        basicMetadataDetailViewer = new DetailViewer();
        basicMetadataDetailViewer.setWidth100();
        basicMetadataDetailViewer.setFields(
                new DetailViewerField(GeneralItemModel.NAME_FIELD, "Title"),
                new DetailViewerField(GeneralItemModel.SORTKEY_FIELD, "Order")  ,
                new DetailViewerField(GeneralItemModel.AUTO_LAUNCH, "Automatic Launch"),
                new DetailViewerField(GeneralItemModel.RICH_TEXT_FIELD, "Description"),
                new DetailViewerField("dependsOn", "Depends On")

        );

        extendedMetadataDetailViewer = new DetailViewer();
        extendedMetadataDetailViewer.setWidth100();
        extendedMetadataDetailViewer.setEmptyMessage("Select an item to view its details");

	}
	
	private void createEditButton() {
		editButton = new IButton("edit");
		editButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				editClick();
			}
		});
	}

	private void createButtonLayout(IButton... buttons) {
		buttonLayout = new HLayout();
		buttonLayout.setAlign(Alignment.CENTER);
		buttonLayout.setLayoutMargin(6);
		buttonLayout.setMembersMargin(6);
		buttonLayout.setHeight(40);
		for (IButton but : buttons) {
			buttonLayout.addMember(but);
		}

	}

	protected void editClick() {
		
	}
	
	
	
	public void loadGeneralItem(GeneralItem gi) {
        DetailViewerRecord[] rec = new DetailViewerRecord[1];
        rec[0] = new DetailViewerRecord();
        rec[0].setAttribute(GeneralItemModel.NAME_FIELD, gi.getTitle());
        rec[0].setAttribute(GeneralItemModel.RICH_TEXT_FIELD, gi.getRichText());
        rec[0].setAttribute(GeneralItemModel.AUTO_LAUNCH, gi.getBoolean(GeneralItemModel.AUTO_LAUNCH));
        rec[0].setAttribute(GeneralItemModel.SORTKEY_FIELD, gi.getInteger(GeneralItemModel.SORTKEY_FIELD));
        rec[0].setAttribute("dependsOn", gi.getDependsOn());
        basicMetadataDetailViewer.setData(rec);

        LinkedHashMap<String, String> sortedMap= gi.getMetadataFields();
        extendedMetadataDetailViewer.setFields(getFields(sortedMap));
        extendedMetadataDetailViewer.setData(getDetailViewerRecord(sortedMap));
	}

    private DetailViewerField[] getFields(LinkedHashMap<String, String> sortedMap) {
        DetailViewerField[] result = new DetailViewerField[sortedMap.size()];
        int i = 0;
        for (String s : sortedMap.keySet()) {
            result[i++] = new DetailViewerField(toValidJavaId(s),s);
        }
        return result;
    }

    private DetailViewerRecord[] getDetailViewerRecord(LinkedHashMap<String, String> sortedMap) {
        DetailViewerRecord result = new DetailViewerRecord();
        for (String s : sortedMap.keySet()) {
            result.setAttribute(toValidJavaId(s),sortedMap.get(s));
        }
        return new DetailViewerRecord[]{result};
    }

    private String toValidJavaId(String s) {
        return s.replace(" ", "_");
    }
}
