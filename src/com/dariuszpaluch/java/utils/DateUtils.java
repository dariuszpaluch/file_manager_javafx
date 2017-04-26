package com.dariuszpaluch.java.utils;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
  static public String getStringDate(Long date) {
    SimpleDateFormat sdf;
    Locale locale = LanguageMechanics.getLocale();
    if (LanguageMechanics.getLocale().toString().equals("pl")) {
      sdf = new SimpleDateFormat("dd.MM.yyyy");
    } else {
      sdf = new SimpleDateFormat("dd/MM/yyyy");
    }

    return sdf.format(date);
  }
}
