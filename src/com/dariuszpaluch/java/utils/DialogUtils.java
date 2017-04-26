package com.dariuszpaluch.java.utils;

import javafx.scene.control.Alert;

import java.util.ResourceBundle;

public class DialogUtils {

  public static void showDialogNoFileSelect() {
    ResourceBundle bundle = ResourceBundle.getBundle("bundles.messages", LanguageMechanics.getLocale());
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle(bundle.getString("warning"));
    alert.setHeaderText(null);
    alert.setContentText(bundle.getString("emptyFileWarningDialogContent"));

    alert.showAndWait();
  }
}
