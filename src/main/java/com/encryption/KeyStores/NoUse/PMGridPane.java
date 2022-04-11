package com.encryption.KeyStores.NoUse;

import javafx.scene.layout.GridPane;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;


public class PMGridPane extends GridPane {
    public PMGridPane(PasswordProtector pp) {
        this.pp = pp;

        // row #0: buttons
        add(new LoadButton(), 0, 0);
        add(new StoreButton(), 1, 0);
        add(masterPWF, 2, 0, 2, 1); // spanning two columns

        // row #1: labels
        add(new Label("Site"), 0, 1);
        add(new Label("URL"), 1, 1);
        add(new Label("Username"), 2, 1);
        add(new Label("Password"), 3, 1);

        // rows #2,3,4: gadgets with password record data
        updateGadgets();

        // column constraints
        ColumnConstraints siteCC = new ColumnConstraints();
        siteCC.setPercentWidth(20);
        ColumnConstraints urlCC = new ColumnConstraints();
        urlCC.setPercentWidth(25);
        ColumnConstraints usernameCC = new ColumnConstraints();
        usernameCC.setPercentWidth(25);
        ColumnConstraints passwordCC = new ColumnConstraints();
        passwordCC.setPercentWidth(30);
        getColumnConstraints().addAll(siteCC, urlCC, usernameCC, passwordCC);

        // row constraints
        RowConstraints gadgetRC = new RowConstraints();
        gadgetRC.setPercentHeight(20);
        getRowConstraints().addAll(gadgetRC, gadgetRC, gadgetRC, gadgetRC, gadgetRC);

        // spacing
        setHgap(20);
        setVgap(20);
        setPadding(new Insets(30, 30, 30, 30));

        // font and font size
        setFonts();
    }

    PasswordTable pt = new PasswordTable();
    PasswordProtector pp;
    MasterPasswordField masterPWF = new MasterPasswordField();

    // load() and store() uses a password table
    // as an intermediate data format
    // between the GUI gadgets and the encrypted password file

    // load(): loads passwords from file
    // calls PasswordProtector.load() (which reads + decrypts)
    // then reads password table and (over-)writes values in gadgets
    // and finally deletes masterpassword from gadget

    void load() {
        pt = pp.load();
        updateGadgets();
        masterPWF.setText("");
    }

    // store(): stores passwords in file
    // reads password data from gadgets and inserts in password table
    // then calls PasswordProtector.store() to encrypt and write password table to file

    void store() {
        updatePasswordTable();
        pp.store(pt);
    }

    void updatePasswordTable() {
        pt.clear();
        for (int i = 0; i < 3; i++) {
            // pt.set(i, new PasswordRecord(((TextField) getNode(0, i+2)).getText()),"","","");
            PasswordRecord pr = new PasswordRecord(((TextField) getNode(0, i+2)).getText(),
                    ((TextField) getNode(1, i+2)).getText(),
                    ((TextField) getNode(2, i+2)).getText(),
                    ((TextField) getNode(3, i+2)).getText());
            pt.add(pr);
        }
    };

    void updateGadgets() {
        int row;
        for (int i = 0; i < 3; i++) {
            row = i+2;
            freshGadgetRow(row);
            if (pt.size() > i) {
                PasswordRecord pr = pt.get(i);
                ((TextField) getNode(0, row)).setText(pr.site);
                ((TextField) getNode(1, row)).setText(pr.url);
                ((TextField) getNode(2, row)).setText(pr.username);
                ((TextField) getNode(3, row)).setText(pr.password);
            }
        }
    }

    void freshGadgetRow(int row) {
        Node n;
        for (int col = 0; col < 4; col++) {
            n = getNode(col, row);
            if (n != null) getChildren().remove(n);
        }
        add(new TextField(), 0, row);
        add(new TextField(), 1, row);
        add(new TextField(), 2, row);
        add(new PasswordField(), 3, row);
        setFonts();
    }

    // A generic helper method: getNode(c, r)

    Node getNode(int c, int r) {
        Integer row = Integer.valueOf(r);
        Integer col = Integer.valueOf(c);
        for (Node node : getChildren()) {
            if(getColumnIndex(node) == col && getRowIndex(node) == row){
                return node;
            }
        }
        return null;
    }

    // setting fonts to 20

    void setFonts() {
        Font font = new Font("Arial", 20);
        for (Object node : getChildren()) {
            if (node instanceof TextField) ((TextField) node).setFont(font);
            if (node instanceof Label) ((Label) node).setFont(font);
            if (node instanceof Button) ((Button) node).setFont(font);
        }
    }
} // PMGridPane


class LoadButton extends Button {
    LoadButton() {
        setText("Load");
        setOnAction(e -> {
            PMGridPane pm = (PMGridPane) getParent();
            pm.load();
        });
    }
} // LoadButton

class StoreButton extends Button {
    StoreButton() {
        setText("Store");
        setOnAction(e -> {
            PMGridPane pm = (PMGridPane) getParent();
            pm.store();
        });
    }
} // StoreButton

class MasterPasswordField extends PasswordField {
    MasterPasswordField() {
        setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(5), null)));
    }
} // MasterPasswordField