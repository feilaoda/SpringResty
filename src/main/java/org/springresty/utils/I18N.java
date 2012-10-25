package org.springresty.utils;

import java.util.Locale;

import java.util.ResourceBundle;

public class I18N {

	public final static Locale DefaultLocale = new Locale("en","US");
	
	/*
	 * 根据Key获得资源包对应Value
	 */
	public static String T(String key) {
		String message = getMessagesByType(key,"messages");
		return message;
	}
	
	public static String T(String key, String value) {
		String fmt = getMessagesByType(key,"messages");
		int pos = fmt.indexOf("?");
		if(pos > 0){
			return fmt.substring(0, pos) + value + fmt.substring(pos+1);
		}		
		return fmt;
	}

 
	
	private static String getMessagesByType(String key,String type) {
		Locale locale = DefaultLocale;
		ResourceBundle bundle = ResourceBundle.getBundle("i18n."+type, locale);

		if(bundle.containsKey(key)){
			String message = bundle.getString(key);
			return message;
		}else{
			return key;
		}

	}
}
