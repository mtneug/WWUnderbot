/*
 * Copyright (c) 2015. WWUnderbot team
 */

package com.theaigames.blockbattle.models;

/**
 * @author Alexander
 * @author Frederik
 * @author Marco
 * @author Matthias
 */
public abstract class AbstractFieldFactory {

  public abstract Field newField(final int width, final int height);

  public Field newField(final int width, final int height, final String fieldString) {
    return newField(width, height, fieldString, false);
  }

  public Field newField(final int width, final int height, final String fieldString, final boolean removeShapes) {
    Field field = newField(width, height);
    parse(field, fieldString, removeShapes);
    return field;
  }

  /**
   * Parses the input string to get a grid with Cell objects
   *
   * @param field
   * @param fieldString input string
   * @param removeShapes
   */
  protected void parse(final Field field, final String fieldString, final boolean removeShapes) {
    // get the separate rows
    final String[] rows = fieldString.split(";");
    for (int y = 0; y < field.getHeight(); y++) {
      final String[] cells = rows[y].split(",");

      // parse each cell of the row
      for (int x = 0; x < field.getWidth(); x++) {
        int cellCode = Integer.parseInt(cells[x]);
        if (cellCode == CellType.SHAPE.getCode())
          cellCode = CellType.EMPTY.getCode();
        field.getCell(x, y).setState(CellType.values()[cellCode]);
      }
    }
  }
}
