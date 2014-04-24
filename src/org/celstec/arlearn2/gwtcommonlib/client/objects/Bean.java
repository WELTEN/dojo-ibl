package org.celstec.arlearn2.gwtcommonlib.client.objects;

import com.google.gwt.json.client.*;

public abstract class Bean {

	protected JSONObject jsonRep;

	public Bean(JSONObject json) {
		this.jsonRep = json;
	}
	
	public void setJsonRep(JSONObject json) {
		this.jsonRep = json;
	}
	
	public Bean() {
		this.jsonRep = new JSONObject();
		this.jsonRep.put("type", new JSONString(getType()));
	}
	
	public String getString(String fieldName) {
		if (jsonRep.containsKey(fieldName)) {
			return jsonRep.get(fieldName).isString().stringValue();
		}
		return "";
	}
	
	public Double getDouble(String fieldName) {
		if (jsonRep.containsKey(fieldName)) {
			return jsonRep.get(fieldName).isNumber().doubleValue();
		}
		return null;
	}
	
	public boolean getBoolean(String fieldName) {
		if (jsonRep.containsKey(fieldName)) {
			return jsonRep.get(fieldName).isBoolean().booleanValue();
		}
		return false;
	}
	
	public int getInteger(String fieldName) {
		if (jsonRep.containsKey(fieldName)) {
			return (int) jsonRep.get(fieldName).isNumber().doubleValue();
		}
		return 0;
	}
	
	public long getLong(String fieldName) {
		if (jsonRep.containsKey(fieldName)) {
			return (long) jsonRep.get(fieldName).isNumber().doubleValue();
		}
		return 0;
	}
	
	public void setString(String fieldName, String value) {
		jsonRep.put(fieldName, new JSONString(value));
	}
	
	public void setBoolean(String fieldName, boolean value) {
		jsonRep.put(fieldName, JSONBoolean.getInstance(value));
	}
	
	public void setLong(String fieldName, long value) {
		jsonRep.put(fieldName, new JSONNumber(value));
	}
	
	public void setDouble(String fieldName, double value) {
		jsonRep.put(fieldName, new JSONNumber(value));
	}
	
	public String getValueAsString(String key) {
		if (jsonRep.containsKey(key)) {
			return jsonRep.get(key).isString().stringValue();
		}
		return null;
	}

    public String[] getValues(String key) {
        if (jsonRep.containsKey(key)) {
            JSONArray array = jsonRep.get(key).isArray();
            String[] returnString = new String[array.size()];
            for (int i = 0 ; i<array.size(); i++) {
                returnString[i] = array.get(i).isString().stringValue();
            }
             return returnString;
        }
        return new String[0];
    }

    public void setArray(String field, String[] values ) {
        JSONArray array = new JSONArray();
        for (String value : values) {
            array.set(array.size(), new JSONString(value));
        }
        jsonRep.put(field, array);
    }
	
	public long getValueAsLong(String key) {
		if (jsonRep.containsKey(key)) {
			return (long) jsonRep.get(key).isNumber().doubleValue();
		}
		return 0l;
	}
	
	public JSONObject getJsonRep(){
		return jsonRep;
	}
	
	public void removeAttribute(String idField) {
		if (jsonRep.containsKey(idField)) {
			jsonRep.put(idField, null);
		}
		
	}
	public abstract String getType();
}
