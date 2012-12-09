package com.anudeep.eventmanager.util;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;


/**
 * "Msgcrypt" Class mainly performs encryption and decryption functions.
 *  It is related to cryptographic Password based crypting feature.
 */

public class CryptoHelper{

	private Cipher pbeCipher; 				// Cipher Object 
	private PBEParameterSpec pbeParamSpec;	// Password Based Encryption Parameters
	private SecretKey pbeKey ;				// SecretKey Object 
	private static final String algo = "PBEWithMD5AndDES"; 

	/**
	 * Initialise KeyFactory with default salt and Password given as input 
	 * @param pwd Password for PasswordBased Encryption Algorithm
	 * @throws Exception
	 */
	private void initKeyFactory(String pwd) throws Exception{

		// Salt (a )
		byte[] salt = {
				(byte)0xc7, (byte)0x73, (byte)0x21, (byte)0x8c,
				(byte)0x7e, (byte)0xc8, (byte)0xee, (byte)0x99
		};

		// Iteration count
		int count = 20;

		// Create PBE parameter set
		pbeParamSpec = new PBEParameterSpec(salt, count);

		// Prompt user for encryption password.
		// Collect user password as a string  and convert
		// it into a SecretKey object, using a PBE key
		// factory.

		char a[]=pwd.toCharArray();
		PBEKeySpec pbeKeySpec = new PBEKeySpec(a);
		SecretKeyFactory keyFac = SecretKeyFactory.getInstance(algo);
		pbeKey = keyFac.generateSecret(pbeKeySpec);

		// Create PBE Cipher
		pbeCipher = Cipher.getInstance(algo);

	}
	/**
	 * Encrypt an input byte-form with the input password string specified 
	 * based on Password Based Encryption method
	 * @param inpByteForm -- Input ByteArray to be encrypted 
	 * @param pwd -- Password String for Encryption 
	 */
	public byte[] encryptBytes(byte[] inpByteForm,String pwd) throws Exception{

		// init salt and key factory 
		initKeyFactory(pwd);
		
		// Initialize PBE Cipher with key and parameters in ENCRYPT Mode
		pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);
		
		// Encrypt input bytes 
		byte[] outByteForm = pbeCipher.doFinal(inpByteForm);

		return outByteForm;

	}

	/**
	 * Decrypt an input byte-form with the input password string specified 
	 * based on Password Based Encryption method
	 * @param inpByteForm -- Input ByteArray to be decrypted 
	 * @param pwd -- Password String for Encryption 
	 */

	public byte[] decryptBytes(byte[] inpByteForm,String pwd) throws Exception{

		// init salt and key factory 
		initKeyFactory(pwd);
		
		// Initialize PBE Cipher with key and parameters
		pbeCipher.init(Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec);
		
		// Decrypt input bytes 
		byte[] outByteForm = pbeCipher.doFinal(inpByteForm);

		return outByteForm;

	}

	/**
	 * Encrypt an input String with the input password string specified 
	 * @param inp -- Input plain string to be encrypted 
	 * @param pwd -- Password for Encryption 
	 * @return encStr -- Returns Encrypted String in Hexadecimal Form 
	 * @throws Exception
	 */
	public String encryptString(String inp,String pwd) throws Exception{
		return byteArrayToHexString(encryptBytes(inp.getBytes(), pwd));
	}

	/**
	 * Decrypt an input String with the input password string specified 
	 * @param inp -- Input cipher Hex string to be decrypted 
	 * @param pwd -- Password for Decryption 
	 * @return decStr -- Returns Decrypted String 
	 * @throws Exception
	 */
	public String decryptString(String inp,String pwd) throws Exception{
		//Convert HexString to ByteArray and  Decrypt input bytes 
		return new String (decryptBytes(hexStringToByteArray(inp),pwd));
	}

	/**
	 * Convert Input ByteArray TO Hexadecimal String 
	 * @param b Input ByteArray
	 * @return Hexadecimal String [In UpperCase ]
	 */
	private String byteArrayToHexString(byte[] b){
		StringBuffer sb = new StringBuffer(b.length * 2);
		for (int i = 0; i < b.length; i++){
			int v = b[i] & 0xff;
			if (v < 16) {
				sb.append('0');
			}
			sb.append(Integer.toHexString(v));
		}
		return sb.toString().toUpperCase();
	}

	/**
	 * Convert Hexadecimal String To ByteArray  
	 * @param s Input Hexadecimal String 
	 * @return ByteArray 
	 */
	private  byte[] hexStringToByteArray(String s) {
		byte[] b = new byte[s.length() / 2];
		for (int i = 0; i < b.length; i++){
			int index = i * 2;
			int v = Integer.parseInt(s.substring(index, index + 2), 16);
			b[i] = (byte)v;
		}
		return b;
	}


}
