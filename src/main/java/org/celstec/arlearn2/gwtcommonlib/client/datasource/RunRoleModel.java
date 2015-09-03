package org.celstec.arlearn2.gwtcommonlib.client.datasource;

public class RunRoleModel extends DataSourceModel {

	public static final String ROLE_FIELD = "role";

	public RunRoleModel(DataSourceAdapter dataSourceAdapter) {
		super(dataSourceAdapter);
	}

	@Override
	protected void initFields() {
		addField(INTEGER_DATA_TYPE, RunModel.RUNID_FIELD, true, true);
		addField(STRING_DATA_TYPE, ROLE_FIELD, true, true);

	}

//	@Override
//	protected void registerForNotifications() {
//		
//	}
}
