module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires org.bouncycastle.provider;
    requires org.apache.commons.io;
    requires org.apache.commons.lang3;


    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
    exports com.encryption.KeyStores;
    opens com.encryption.KeyStores to javafx.fxml;
    exports com.encryption.KeyStores.NoUse;
    opens com.encryption.KeyStores.NoUse to javafx.fxml;
}