package com.example.demo;

import com.encryption.KeyStores.KeyAliasNotFoundException;
import com.encryption.KeyStores.KeyStoreBuilder;
import com.encryption.KeyStores.KeyStoreManager;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.KeyStoreBuilderParameters;
import javax.net.ssl.X509ExtendedKeyManager;
import java.io.IOException;
import java.security.*;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStore.ProtectionParameter;
import java.security.KeyStore.SecretKeyEntry;
import java.security.cert.CertificateException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testng.AssertJUnit.fail;

/**
 * Unit test for simple App.
 */
public class KeyStoreManagerTest
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    //The key store gets created when you call get key if it is not already there
    @Test
    public void createKeyStore() {
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
        try {
            String ksName = System.getProperty("user.dir") + "\\keystore4.bks";
            KeyStoreManager ksm = new KeyStoreManager.Builder()
                    .withPassword("somelongpassword")
                    .withType("BKS") //BC only // does not support probing with key tool, changes password
                    //.withType("PKCS12") //changes password, BC does not support non-private keys
                    //.withType("JKS") //neither supports non-private keys
                    //.withType("BCFKS") //BC only,  keytool probing not supported, changes password
                    //.withType("UBER") //does not support probing with keytool, changes password, BC only
                    //.withType("JCEKS") //storing keys with different password changes the keystore password //works with sun
                    //.withType("BCPKCS12") //BC does not support non-private keys, BC only
                    .withStoreFileName(ksName)
                    .build();

            SecretKey key = ksm.getAESKey("aeskey", "somelongpassword2", true);
            SecretKey key2 = ksm.getAESKey("aeskey2", "somelongpassword3", true);
            if(!key.getAlgorithm().equals("AES")) {
                fail();
            }

            System.out.println("Default type is: " + KeyStore.getDefaultType());
            String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
            System.out.println("Base64 encoded key is: " + encodedKey);
            encodedKey = Base64.getEncoder().encodeToString(key2.getEncoded());
            System.out.println("Base64 encoded key is: " + encodedKey);
            Set<KeyStore.Entry.Attribute> algo = ksm.getKeyStore().getEntry("aeskey", new KeyStore.PasswordProtection("somelongpassword2".toCharArray())).getAttributes();
            System.out.println(algo);
            SecretKeyEntry entry = (SecretKeyEntry) ksm.getKeyStore().getEntry("aeskey", new KeyStore.PasswordProtection("somelongpassword2".toCharArray()));
            KeyStore keystore = ksm.getKeyStore();

        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (KeyStoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (CertificateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnrecoverableEntryException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (KeyAliasNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testWithBuilder() throws GeneralSecurityException, IOException {
        char[] password1 = "password1".toCharArray();
        char[] password2 = "password2".toCharArray();
        Map<String, ProtectionParameter> passwordsMap = new HashMap<>();
        passwordsMap.put("rsaentry", new PasswordProtection(password1));
        passwordsMap.put("dsaentry", new PasswordProtection(password2));

        KeyStore keyStore = generateStore();
        KeyStore.Builder builder = new KeyStoreBuilder(() -> keyStore, alias -> {
            // alias is lowercased keystore alias with prefixed numbers :-/
            // parse the alias
            int firstDot = alias.indexOf('.');
            int secondDot = alias.indexOf('.', firstDot + 1);
            if ((firstDot == -1) || (secondDot == firstDot)) {
                // invalid alias
                return null;
            }
            String keyStoreAlias = alias.substring(secondDot + 1);
            return passwordsMap.get(keyStoreAlias);
        });

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("NewSunX509");
        kmf.init(new KeyStoreBuilderParameters(builder));
        X509ExtendedKeyManager keyManager = (X509ExtendedKeyManager) kmf.getKeyManagers()[0];

        String rsaAlias = keyManager.chooseServerAlias("RSA", null, null);
        assertTrue((rsaAlias).contains("rsaentry"));
        PrivateKey rsaPrivateKey = keyManager.getPrivateKey(rsaAlias);
        assertTrue((rsaPrivateKey) != null); // can get password

        String dsaAlias = keyManager.chooseServerAlias("DSA", null, null);
        assertTrue((dsaAlias).contains("dsaentry"));
        PrivateKey dsaPrivateKey = keyManager.getPrivateKey(dsaAlias);
        assertTrue((dsaPrivateKey) != null); // can get password
    }

    public static KeyStore generateStore() {
        try {
            KeyStore ks = null;
            try {
                ks = KeyStore.getInstance("BKS","BC");
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            }
            return ks;
        } catch (KeyStoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
}