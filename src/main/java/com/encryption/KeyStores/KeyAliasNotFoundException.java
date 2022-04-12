package com.encryption.KeyStores;

public class KeyAliasNotFoundException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 6680218941224906599L;

    public KeyAliasNotFoundException(String alias) {
        super("The alias " + alias + " does not exist in the keystore.");
    }
}