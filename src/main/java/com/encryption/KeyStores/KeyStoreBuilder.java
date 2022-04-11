package com.encryption.KeyStores;

import java.security.*;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class KeyStoreBuilder extends KeyStore.Builder {
    private final Supplier<KeyStore> keyStoreSupplier;
    private final Function<String, KeyStore.ProtectionParameter> passwordFunction;

    public KeyStoreBuilder(Supplier<KeyStore> keyStoreSupplier, Function<String, KeyStore.ProtectionParameter> passwordFunction) {
        Objects.requireNonNull(keyStoreSupplier);
        Objects.requireNonNull(passwordFunction);
        this.keyStoreSupplier = keyStoreSupplier;
        this.passwordFunction = passwordFunction;
    }

    @Override
    public KeyStore getKeyStore() throws KeyStoreException {
        return keyStoreSupplier.get();
    }

    @Override
    public KeyStore.ProtectionParameter getProtectionParameter(String alias) throws KeyStoreException {
        Objects.requireNonNull(alias);
        return passwordFunction.apply(alias);
    }

}