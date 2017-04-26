package com.dariuszpaluch.java.utils;

import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

public class LanguageMechanics {

  private static class ViewItem {
    Object object;
    String bundleKey;

    ViewItem(Object object, String bundleKey) {
      this.object = object;
      this.bundleKey = bundleKey;
    }
  }

  private static class StageItem {
    Stage stage;
    String bundleKey;

    public StageItem(Stage stage, String bundleKey) {
      this.stage = stage;
      this.bundleKey = bundleKey;
    }
  }

  static private Set<ViewItem> elementsWithSetText = new HashSet<ViewItem>();
  static private StageItem stageItem = null;
  static private Locale locale = Locale.getDefault();

  public static Locale getLocale() {
    return locale;
  }

  public static void setState(Stage appStage, String bundleKey) {
    stageItem = new StageItem(appStage, bundleKey);
  }

  public static void setLocale(Locale locale) {
    LanguageMechanics.locale = locale;
    updateAllItems();
  }

  static public void addItem(Object object, String bundleKey) {
    ViewItem item = new ViewItem(object, bundleKey);

    setText(item);
    elementsWithSetText.add(item);
  }

  static private void setText(ViewItem item) {
    ResourceBundle bundle = ResourceBundle.getBundle("bundles.messages", locale);

    if (item.object instanceof Text) {
      ((Text) item.object).setText(bundle.getString(item.bundleKey));
    } else if (item.object instanceof Button) {
      ((Button) item.object).setText(bundle.getString(item.bundleKey));
    } else if (item.object instanceof Tooltip) {
      ((Tooltip) item.object).setText(bundle.getString(item.bundleKey));
    }
  }

  static public void updateAllItems() {
    ResourceBundle bundle = ResourceBundle.getBundle("bundles.messages", locale);

    for (ViewItem item : elementsWithSetText) {
      setText(item);
    }

    stageItem.stage.setTitle(bundle.getString(stageItem.bundleKey));
  }

}
