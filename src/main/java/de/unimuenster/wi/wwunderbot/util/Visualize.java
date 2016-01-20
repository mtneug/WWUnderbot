/*
 * Copyright (c) 2016. WWUnderbot team
 */

package de.unimuenster.wi.wwunderbot.util;

import com.theaigames.blockbattle.models.Field;

/**
 * @author Matthias Neugebauer
 */
public final class Visualize {

  private Visualize() {
  }

  public static void fields(Field[] field) {
    String line = "";
    for (Field f : field) {
      line += "+";
      for (int x = 0; x < f.getWidth(); x++) line += "--";
      line += "+";
    }

    // HEADER
    System.err.println(line);

    // BODY
    for (int y = 0; y < field[0].getHeight(); y++) {
      for (Field f : field) {
        System.err.print("|");
        for (int x = 0; x < f.getWidth(); x++)
          switch (f.getCell(x, y).getState()) {
            case SHAPE:
              System.err.print("░░");
              break;
            case BLOCK:
              System.err.print("▐▌");
              break;
            case SOLID:
              System.err.print("██");
              break;
            case EMPTY:
              System.err.print("  ");
              break;
          }
        System.err.print("|");
      }
      System.err.println();
    }

    // FOOTER
    System.err.println(line);
  }
}
