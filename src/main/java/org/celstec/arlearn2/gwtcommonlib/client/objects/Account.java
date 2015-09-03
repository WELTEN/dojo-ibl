package org.celstec.arlearn2.gwtcommonlib.client.objects;

import com.google.gwt.json.client.JSONObject;

public class Account extends Bean{
	
	public  final static String PICTURE = "picture";
	public  final static String NAME = "name";
	public  final static String ACCOUNT_LEVEL = "accountLevel";
	public  final static String ACCOUNT_TYPE = "accountType";
	public  final static String LOCAL_ID = "localId";
	
	public static final int ADMINISTRATOR = 1;
	public static final int USER = 2;
	public static final int ADVANCED_USER = 3;
	
	
	public Account(JSONObject json) {
		super(json);
	}
	
	public Account(){
		super();
	}
	@Override
	public String getType() {
		return "org.celstec.arlearn2.beans.account.Account";
	}
	
	public String getPicture() {
		return getString(PICTURE);
	}
	
	public String getName(){
		return getString(NAME);
	}
	public int getAccountType() {
		return getInteger(ACCOUNT_TYPE);
	}
	
	public void setAccountType(int type) {
		setLong(ACCOUNT_TYPE, type);
	}
	
	public String getLocalId() {
		return getString(LOCAL_ID);
	}
	
	public void setLocalId(String localId) {
		setString(LOCAL_ID, localId);
	}

    public String getFullId() {
        return getAccountType()+":"+getLocalId();
    }
	
	public Integer getAccountLevel() {
		return getInteger(ACCOUNT_LEVEL);
	}
	
	public boolean isAdministrator() {
		return getAccountLevel() == ADMINISTRATOR;
	}
	
	public boolean isUser() {
		return getAccountLevel() == USER;
	}
	
	public boolean isAdvancedUser() {
		int level = getAccountLevel();
		return level == ADVANCED_USER || level == ADMINISTRATOR;
	}
}
