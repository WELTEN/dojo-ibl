package org.celstec.arlearn2.gwtcommonlib.client;

import com.google.gwt.user.client.Cookies;

public class LocalSettings {
	
	private static LocalSettings instance;
	private static final String LOCALE_COOKIE = "arlearn.locale";
	private static final String ON_BEHALF_OF_COOKIE = "arlearn.onBehalfOf";

	private LocalSettings(){
		
	}
	
	public static  LocalSettings getInstance() {
		if (instance == null) {
			 instance = new LocalSettings();
		}
		return instance;
	}
	
	public void setLocale(String locale) {
		if (locale == null) {
			Cookies.removeCookie(LOCALE_COOKIE);
		} else {
			Cookies.setCookie(LOCALE_COOKIE, locale);
		}
	}
	
	public String getLocateExtension() {
		if (Cookies.getCookie(LOCALE_COOKIE) == null) {
			return "";
		} else {
			String locale = Cookies.getCookie(LOCALE_COOKIE);
			return "?locale="+locale;
		}
	}
	
	public void setOnBehalfOf(String onBehalfOf) {
		if (onBehalfOf == null) {
			Cookies.removeCookie(ON_BEHALF_OF_COOKIE);
		} else {
			Cookies.setCookie(ON_BEHALF_OF_COOKIE, onBehalfOf);
		}
	}
	
	public String getOnBehalfOf() {
		if (Cookies.getCookie(ON_BEHALF_OF_COOKIE) == null) {
			return null;
		} else {
			return Cookies.getCookie(ON_BEHALF_OF_COOKIE);
			
		}
	}
}
