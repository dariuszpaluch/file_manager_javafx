package com.dariuszpaluch.java.utils;

import com.dariuszpaluch.java.controllers.FilesBrowserController;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
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

  static private Set<FilesBrowserController> filesBrowsersSet = new HashSet<>();

  public static void addFilesBrowser(FilesBrowserController filesBrowser) {
    filesBrowsersSet.add(filesBrowser);
  }

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

  public static String getValueOfKey(String bundleKey) {
    ResourceBundle bundle = ResourceBundle.getBundle("bundles.messages", locale);

    System.out.println(bundleKey);
    return bundle.getString(bundleKey);
  }

  static private void setText(ViewItem item) {
    ResourceBundle bundle = ResourceBundle.getBundle("bundles.messages", locale);

    if (item.object instanceof Text) {
      ((Text) item.object).setText(bundle.getString(item.bundleKey));
    } else if (item.object instanceof Button) {
      ((Button) item.object).setText(bundle.getString(item.bundleKey));
    } else if (item.object instanceof Tooltip) {
      ((Tooltip) item.object).setText(bundle.getString(item.bundleKey));
    } else if (item.object instanceof TableColumn) {
      ((TableColumn) item.object).setText(bundle.getString(item.bundleKey));
    }
  }

  static public void updateAllItems() {
    ResourceBundle bundle = ResourceBundle.getBundle("bundles.messages", locale);

    for (ViewItem item : elementsWithSetText) {
      setText(item);
    }

    for(FilesBrowserController item : filesBrowsersSet) {
      item.updateAll();
    }

    stageItem.stage.setTitle(bundle.getString(stageItem.bundleKey));
  }

}
