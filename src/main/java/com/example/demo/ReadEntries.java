package com.example.demo;
import java.util.ArrayList;

class ReadEntries extends ArrayList<DataListSerialized> {
    void print() {
        DataListSerialized.printLabels();
        for (int i = 0; i < size(); i++) get(i).print();
        System.out.println();
    }
}
