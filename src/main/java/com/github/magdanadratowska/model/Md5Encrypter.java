package com.github.magdanadratowska.model;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Encrypter {

    public String encrypt(String input) throws NoSuchAlgorithmException {
        byte[] inputBytes = input.getBytes();

        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.reset();
        messageDigest.update(inputBytes);
        byte[] encryptBytes = messageDigest.digest();
        String hash = DatatypeConverter.printHexBinary(encryptBytes).toLowerCase();
        return hash;
    }
}

