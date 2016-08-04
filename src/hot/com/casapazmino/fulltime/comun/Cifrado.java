package com.casapazmino.fulltime.comun;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class Cifrado {

	final static String MD2 = "MD2";

	final static String MD5 = "MD5";

	final static String SHA1 = "SHA-1";

	final static String SHA256 = "SHA-256";

	final static String SHA384 = "SHA-384";

	final static String SHA512 = "SHA-512";

	public String toHexadecimal(byte[] digest){

        String hash = "";

        for(byte aux : digest) {

            int b = aux & 0xff;

            if (Integer.toHexString(b).length() == 1) hash += "0";

            hash += Integer.toHexString(b);

        }

        return hash;
    }
    
	public byte[] hexStringToByteArray(String s) {
    	
    	return DatatypeConverter.parseHexBinary(s);
    	
    }
    
	public byte[] cifrar(String sinCifrar) throws Exception {
    	
    	byte[] bytes = sinCifrar.getBytes("UTF-8");
    	Cipher aes = obtieneCipher(true);
    	byte[] cifrado = aes.doFinal(bytes);
    	
    	return cifrado;
    }
     
	public String descifrar(byte[] cifrado) throws Exception {
    	
    	Cipher aes = obtieneCipher(false);
    	byte[] bytes = aes.doFinal(cifrado);
    	String sinCifrar = new String(bytes, "UTF-8");
    	
    	return sinCifrar;
    	
    }
     
	public Cipher obtieneCipher(boolean paraCifrar) throws Exception {
    	
    	String frase = "Fu$$T&M3$YsT3&#5";
    	MessageDigest digest = MessageDigest.getInstance(SHA256);
    	digest.update(frase.getBytes("UTF-8"));
    	SecretKeySpec key = new SecretKeySpec(digest.digest(), 0, 16, "AES");
     
    	final Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
    	if (paraCifrar) {
    		aes.init(Cipher.ENCRYPT_MODE, key);
    	} else {
    		aes.init(Cipher.DECRYPT_MODE, key);
    	}
     
    	return aes;
    	
    }
	
}
