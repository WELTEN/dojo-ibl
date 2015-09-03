package org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.AbstractRecord;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.DataSourceAdapter;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.DataSourceModel;

import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.fields.DataSourceBooleanField;
import com.smartgwt.client.data.fields.DataSourceEnumField;
import com.smartgwt.client.data.fields.DataSourceFloatField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public abstract class GenericDataSource extends DataSource implements
		DataSourceAdapter {

	private DataSourceModel model;
	protected static long lastSyncDate = 0;
	protected HashMap<Object, AbstractRecord> recordMap = new HashMap<Object, AbstractRecord>();


	protected GenericDataSource() {
		setClientOnly(true);
//        setID(this.getClassName());
	}

	@Override
	public void setDataSourceModel(DataSourceModel model) {
		this.model = model;
	}

	@Override
	public DataSourceModel getDataSourceModel() {
		return model;
	}

	@Override
	public void addDataSourceTextField(String attributeName, boolean hidden,
			boolean primaryKey) {
		addField(new DataSourceTextField(attributeName), hidden, primaryKey);
	}

	@Override
	public void addDataSourceIntegerField(String attributeName, boolean hidden,
			boolean primaryKey) {
		addField(new DataSourceIntegerField(attributeName), hidden, primaryKey);
	}

	@Override
	public void addDataSourceDoubleField(String attributeName, boolean hidden,
			boolean primaryKey) {
		addField(new DataSourceFloatField(attributeName), hidden, primaryKey);
	}

	@Override
	public void addDataSourceBooleanField(String attributeName, boolean hidden,
			boolean primaryKey) {
		addField(new DataSourceBooleanField(attributeName), hidden, primaryKey);
	}
	
	@Override
	public void addDataSourceEnumField(String attributeName, boolean hidden,
			boolean primaryKey) {
		addField(new DataSourceEnumField(attributeName), hidden, primaryKey);
	}

	private void addField(DataSourceField field, boolean hidden,
			boolean primaryKey) {
		field.setHidden(hidden);
		field.setPrimaryKey(primaryKey);
		addField(field);
	}

	@Override
	public AbstractRecord createRecord() {
		return new MyAbstractRecord();
	}

	@Override
	public void saveRecord(AbstractRecord record) {
		addData((MyAbstractRecord) record);
		Object key = model.getPrimaryKey(record);
		if (key != null) recordMap.put(key, (MyAbstractRecord) record);
		
	}

	public void setServerTime(long serverTime) {
		lastSyncDate = serverTime;
	}
	
	protected long getLastSyncDate() {
		return lastSyncDate;
	}
	
	private class MyAbstractRecord extends ListGridRecord implements
			AbstractRecord {
		private JSONObject json;

		@Override
		public void setCorrespondingJsonObject(JSONObject object) {
			this.json = object;
		}

		@Override
		public JSONObject getCorrespondingJsonObject() {
			return json;
		}

		
	}
	
	@Override
	public void removeRecord(AbstractRecord record) {
		Record toRemove = getRecord(model.getPrimaryKey(record));
		if (toRemove != null) removeData(toRemove);
	}
	
	public Record getRecord(Object id) {
		return (Record) recordMap.get(id);
	}

    public Record[] allRecords() {
        return recordMap.values().toArray(new Record[]{});
    }

	public void removeRecordWithKey(Object id) {
		removeRecord((AbstractRecord)getRecord(id));
	}
}
