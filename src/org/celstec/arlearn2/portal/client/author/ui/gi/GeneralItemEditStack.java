package org.celstec.arlearn2.portal.client.author.ui.gi;

import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.portal.client.author.ui.FileReferencesEditor;
import org.celstec.arlearn2.portal.client.author.ui.gi.dependency.AdvancedDependenciesEditor;
import org.celstec.arlearn2.portal.client.author.ui.gi.dependency.forms.DependencyEditor;
import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.DataCollectionEditor;
import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.ExtensionEditor;
import org.celstec.arlearn2.portal.client.author.ui.gi.i18.GeneralItemConstants;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;

public class GeneralItemEditStack extends SectionStack {
	
	private static GeneralItemConstants constants = GWT.create(GeneralItemConstants.class);

	
	private SectionStackSection extensionStackSection;
	private SectionStackSection appearStackSection;
	private SectionStackSection disappearStackSection;
	private SectionStackSection dataCollectionStackSection;
	private SectionStackSection jsonStackSection;
	
	public GeneralItemEditStack(){
		
	}

	public void setExtensionStack(GeneralItem gi, ExtensionEditor extensionEditor){
		if (extensionStackSection != null) {
			removeSection(extensionStackSection.getID());
		}
		extensionStackSection = new SectionStackSection(gi.getHumanReadableName());
		extensionStackSection.setItems((Canvas)extensionEditor);
		addSection(extensionStackSection, 0);
	}
	
	public void setDataCollection(DataCollectionEditor dataCollectionEditor) {
		if (dataCollectionStackSection != null) {
			removeSection(dataCollectionStackSection.getID());
		}
		dataCollectionStackSection = new SectionStackSection(constants.dataCollection());

		dataCollectionStackSection.setItems(dataCollectionEditor);
		addSection(dataCollectionStackSection);

	}
	
	public void setAppearStack(DependencyEditor dependencyEditor) {
		if (appearStackSection != null) {
			removeSection(appearStackSection.getID());
		}
		appearStackSection = new SectionStackSection(constants.appear());
		appearStackSection.setItems(dependencyEditor);
		addSection(appearStackSection);
	}
	
	public void setDisappearStack(DependencyEditor depEditor) {
		depEditor.setShowCountDownOption();
		if (disappearStackSection != null) {
			removeSection(disappearStackSection.getID());
		}
		disappearStackSection = new SectionStackSection(constants.disappear());
		disappearStackSection.setItems(depEditor);
		addSection(disappearStackSection);
	}

	public void setJsonEditor(JsonEditor jsonEditor) {
		addSection(jsonEditor);		
	}

    public void setFileReferenceEditor(FileReferencesEditor fileReferencesEditor) {
        addSection(fileReferencesEditor);
    }
}
