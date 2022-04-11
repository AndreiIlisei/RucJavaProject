package com.encryption.KeyStores.NoUse;

import java.util.ArrayList;

public class PasswordTable extends ArrayList<PasswordRecord> {

    void print() {
        PasswordRecord.printLabels();
        for (int i=0; i<size() ; i++) get(i).print();
        System.out.println();
    }
}