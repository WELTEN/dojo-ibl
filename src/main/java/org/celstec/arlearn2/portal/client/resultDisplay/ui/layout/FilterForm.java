package org.celstec.arlearn2.portal.client.resultDisplay.ui.layout;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.GeneralItemModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.RunRoleModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.TeamModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.UserModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.GeneralItemDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.OwnerResponseDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.RunRolesDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.TeamDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.UserDataSource;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;

public class FilterForm extends DynamicForm {

	public FilterForm() {
		super();
		
		setPadding(10);
		setGroupTitle("Search");
        setBackgroundColor("#f1f1f1");
		setDataSource(OwnerResponseDataSource.getInstance());
		setAutoFocus(false);
	    
	    setFields(addSelectTeamField(), addSelectUserField(), addSelectRolField(), addSelectItemField());
	}
	
	private SelectItem addSelectTeamField() {
		final SelectItem select = new SelectItem(TeamModel.TEAMID_FIELD, "team"); //TODO add internationalisation constant  
        select.setMultiple(true);  
        select.setWidth(150);
        select.setOptionDataSource(TeamDataSource.getInstance());
        select.setAutoFetchData(true);
        select.setValueField(TeamModel.TEAMID_FIELD);
        select.setDisplayField(TeamModel.NAME_FIELD);
        
		return select; 
	}
	
	private SelectItem addSelectItemField() {
		final SelectItem select = new SelectItem(GeneralItemModel.GENERALITEMID_FIELD, "item"); //TODO add internationalisation constant  
		select.setMultiple(true);  
		select.setWidth(150);
		
		select.setOptionDataSource(GeneralItemDataSource.getInstance());
				
		select.setAutoFetchData(true);
		select.setValueField(GeneralItemModel.GENERALITEMID_FIELD);
		select.setDisplayField(GeneralItemModel.NAME_FIELD);
		return select; 
	}
	
	private SelectItem addSelectRolField() {
		final SelectItem select = new SelectItem(RunRoleModel.ROLE_FIELD, "role"); //TODO add internationalisation constant  
        select.setMultiple(true);  
        select.setWidth(150);
        select.setOptionDataSource(RunRolesDataSource.getInstance());
        select.setAutoFetchData(true);
        select.setValueField(RunRoleModel.ROLE_FIELD);
        select.setDisplayField(RunRoleModel.ROLE_FIELD);
		return select; 
	}

	private SelectItem addSelectUserField() {
		final SelectItem select = new SelectItem(UserModel.FULL_ACCOUNT_FIELD, "user"); //TODO add internationalisation constant  
        select.setMultiple(true);  
        select.setWidth(150);
        select.setOptionDataSource(UserDataSource.getInstance());
        select.setAutoFetchData(true);
        select.setValueField(UserModel.FULL_ACCOUNT_FIELD);
        select.setDisplayField(UserModel.NAME_FIELD);
		return select; 
	}
}
