package com.dariuszpaluch.java.utils;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.value.ObservableLongValue;

public class BindingNumberUtils {
  public static NumberBinding calculateProgress(final ObservableLongValue totalSize, final ObservableLongValue progressSize) {
    return new DoubleBinding() {
      {
        super.bind(totalSize, progressSize);
      }

      @Override
      protected double computeValue() {
        if (totalSize.doubleValue() > 0 && progressSize.doubleValue() > 0) {
          return progressSize.doubleValue() / totalSize.doubleValue();
        }

        return -1.0;
      }
    };
  }
}
