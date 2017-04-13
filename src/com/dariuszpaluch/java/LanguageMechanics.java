package com.dariuszpaluch.java;

import javafx.scene.Node;
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
        Node node;
        String bundleKey;

        ViewItem(Node node, String bundleKey) {
            this.node = node;
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

    static public void addItem(Node node, String bundleKey) {
        elementsWithSetText.add(new ViewItem(node, bundleKey));
    }

    static public void updateAllItems() {
        ResourceBundle bundle = ResourceBundle.getBundle("bundles.messages", locale);

        for(ViewItem item: elementsWithSetText) {
            if(item.node instanceof Text) {
                ((Text)item.node).setText(bundle.getString(item.bundleKey));
            }
            else if(item.node instanceof  Button) {
                ((Button)item.node).setText(bundle.getString(item.bundleKey));
            }
        }

        stageItem.stage.setTitle(bundle.getString(stageItem.bundleKey));
    }

}
