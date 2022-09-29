package com.example.findr;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

//Generates SHA-256 Hash
public class SHAHelper {

    private String hashedPassword;
    private String salt;

    public SHAHelper(String passwordToHash) throws NoSuchAlgorithmException {

        this.salt = generateSalt();
        this.hashedPassword = generateHashPassword(passwordToHash,this.salt);
    }
    public SHAHelper(String passwordToHash, String salt) throws NoSuchAlgorithmException{
        this.hashedPassword = generateHashPassword(passwordToHash,salt);
        this.salt = salt;
    }

    private String generateHashPassword(String passwordToHash, String salt){
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
                        .substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    private String generateSalt() throws NoSuchAlgorithmException{
        byte[] salt = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return salt.toString();
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getSalt() {
        return salt;
    }
}
