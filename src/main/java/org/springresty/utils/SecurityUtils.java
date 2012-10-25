package org.springresty.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;



public class SecurityUtils {
	private static final String HEX_CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String HEX_CHARACTERS_UC = HEX_CHARACTERS.toUpperCase();
	
	public static String PasswordSplit = "|";
	
	public static String randomString(Random rng, String characters, int length)
	{
	    char[] text = new char[length];
	    for (int i = 0; i < length; i++)
	    {
	        text[i] = characters.charAt(rng.nextInt(characters.length()));
	    }
	    return new String(text);
	}
	
	/**
     * @see #toHexString(byte[])
     */
    public static String toHexString(byte[] bytes)
    {
        return toHexString(bytes, false);
    }

    /**
     * Convert a byte array to a hexadecimal string.
     * 
     * @param bytes The bytes to format.
     * @param uppercase When <code>true</code> creates uppercase hex characters
     *            instead of lowercase (the default).
     * @return A hexadecimal representation of the specified bytes.
     */
    public static String toHexString(byte[] bytes, boolean uppercase)
    {
        if (bytes == null)
        {
            return null;
        }

        int numBytes = bytes.length;
        StringBuffer str = new StringBuffer(numBytes * 2);

        String table = (uppercase ? HEX_CHARACTERS_UC : HEX_CHARACTERS);

        for (int i = 0; i < numBytes; i++)
        {
            str.append(table.charAt(bytes[i] >>> 4 & 0x0f));
            str.append(table.charAt(bytes[i] & 0x0f));
        }

        return str.toString();
    }
	
	public static String toSHA1(byte[] convertme) {
	    MessageDigest md = null;
	    try {
	        md = MessageDigest.getInstance("SHA-1");
	    }
	    catch(NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    } 
	    return  toHexString(md.digest(convertme));
	}
	
	
	public static String encryptPassword(String pwd){
		String salt = RandomStringUtils.randomAlphabetic(6);
		String encryptSalt = SecurityUtils.toSHA1(salt.getBytes());
		String sha1 = SecurityUtils.toSHA1((encryptSalt+pwd).getBytes());
		return salt+PasswordSplit+sha1;
	}
	
	public static boolean validatePassword(String dbPassword, String password){
		int pos = dbPassword.indexOf(PasswordSplit);
		if(pos <= 0){
			return false;
		}
		String salt = dbPassword.substring(0, pos);
		String encryptSalt = SecurityUtils.toSHA1(salt.getBytes());
		String newpwd = SecurityUtils.toSHA1((encryptSalt+password).getBytes());
		return newpwd.equals(dbPassword.substring(pos+1));
	}
	
}
