package com.example.verification.security.ecdsa;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.annotation.PostConstruct;

import com.example.verification.exception.JwtInitializationException;
import com.example.verification.util.Base64Util;
import com.example.verification.util.ResourceUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
@RequiredArgsConstructor
public class KeyProvider {
//  private final static String P256 = "P-256";
//  private final static byte[] TEST = "test".getBytes(StandardCharsets.UTF_8);

  private final ResourceUtil resourceUtil;

  @Getter
  private PrivateKey privateKey;

  @Getter
  private PublicKey publicKey;

  @PostConstruct
  public void init() {
    privateKey = readKey(
        "classpath:keys/ecdsa.key",
        this::privateKeySpec,
        this::privateKeyGenerator
    );

    publicKey = readKey(
        "classpath:keys/ecdsa.pub",
        this::publicKeySpec,
        this::publicKeyGenerator
    );
  }

//  public static void main(String[] args) throws GeneralSecurityException, IOException {
//    Security.addProvider(new BouncyCastleProvider());
////    KeyPair keyPair = generateKeyPair(P256);
////
////    PublicKey publicKey = keyPair.getPublic();
////    PrivateKey privateKey = keyPair.getPrivate();
//
//    PublicKey publicKey = loadPublicKey("ecdsa.pub");
//    PrivateKey privateKey = loadPrivateKey("ecdsa.key");
//
//    System.out.println(publicKey.getFormat());
//    System.out.println(privateKey.getFormat());
//
////    storeKey(publicKey, "ecdsa.pub");
////    storeKey(privateKey, "ecdsa.key");
//    byte[] signed = generateSignature(privateKey, TEST);
//    boolean isValid = verifySignature(publicKey, TEST, signed);
//    System.out.println(isValid);
//  }

//  public static KeyPair generateKeyPair(String curveName) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
//    KeyPairGenerator keyPair = KeyPairGenerator.getInstance("EC", BouncyCastleProvider.PROVIDER_NAME);
//    keyPair.initialize(new ECGenParameterSpec(curveName));
//    return keyPair.generateKeyPair();
//  }

//  public static void storeKey(Key key, String name) {
//    // assume name has correct extension
//    // .key for private and .pub for public
//    try (FileOutputStream fos = new FileOutputStream(name)) {
//      fos.write(key.getEncoded());
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }

  private <T extends Key> T readKey(String resourcePath, Function<byte[], EncodedKeySpec> keySpec, BiFunction<KeyFactory, EncodedKeySpec, T> keyGenerator) {
    try {
      byte[] data = resourceUtil.asByte(resourcePath);

      return keyGenerator.apply(KeyFactory.getInstance("EC"), keySpec.apply(data));
    } catch(NoSuchAlgorithmException | IOException e) {
      throw new JwtInitializationException(e);
    }
  }

//  public static PublicKey loadPublicKey(String name) throws IOException, NoSuchAlgorithmException {
//    File keyFile = new File(name);
//    byte[] keyBytes = Files.readAllBytes(keyFile.toPath());
//    PublicKey publicKey =  publicKeyGenerator(KeyFactory.getInstance("EC"), publicKeySpec(keyBytes));
//    return publicKey;
//  }
//
//  public static PrivateKey loadPrivateKey(String name) throws IOException, NoSuchAlgorithmException {
//    File keyFile = new File(name);
//    byte[] keyBytes = Files.readAllBytes(keyFile.toPath());
//    PrivateKey privateKey =  privateKeyGenerator(KeyFactory.getInstance("EC"), privateKeySpec(keyBytes));
//    return privateKey;
//  }

  private EncodedKeySpec privateKeySpec(byte[] data) {
    return new PKCS8EncodedKeySpec(data);
  }

  private PrivateKey privateKeyGenerator(KeyFactory kf, EncodedKeySpec spec) {
    try {
      return kf.generatePrivate(spec);
    } catch(InvalidKeySpecException e) {
      throw new JwtInitializationException(e);
    }
  }

  private PublicKey publicKeyGenerator(KeyFactory kf, EncodedKeySpec spec) {
    try {
      return kf.generatePublic(spec);
    } catch (InvalidKeySpecException e) {
      throw new JwtInitializationException(e);
    }
  }

  private EncodedKeySpec publicKeySpec(byte[] data) {
    return new X509EncodedKeySpec(data);
  }

//  public static byte[] generateSignature(PrivateKey ecPrivate, byte[] input) throws GeneralSecurityException {
//    Signature signature = Signature.getInstance("SHA256withECDSA", BouncyCastleProvider.PROVIDER_NAME);
//    signature.initSign(ecPrivate);
//    signature.update(input);
//    return signature.sign();
//  }
//
//  public static boolean verifySignature(PublicKey ecPublic, byte[] input, byte[] encSignature) throws GeneralSecurityException {
//    Signature signature = Signature.getInstance("SHA256withECDSA", BouncyCastleProvider.PROVIDER_NAME);
//    signature.initVerify(ecPublic);
//    signature.update(input);
//    return signature.verify(encSignature);
//  }
}
