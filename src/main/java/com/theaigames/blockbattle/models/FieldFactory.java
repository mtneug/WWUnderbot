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
public class FieldFactory {
  private boolean removeShapes = false;

  public FieldFactory() {
  }

  public FieldFactory(boolean removeShapes) {
    this.removeShapes = removeShapes;
  }

  public Field newField(final int width, final int height) {
    return new Field(width, height);
  }

  public Field newField(final int width, final int height, final String fieldString) {
    Field field = newField(width, height);
    parse(field, fieldString);
    return field;
  }

  /**
   * Parses the input string to get a grid with Cell objects
   *
   * @param field
   * @param fieldString input string
   */
  protected void parse(final Field field, final String fieldString) {
    // get the separate rows
    final String[] rows = fieldString.split(";");
    for (int y = 0; y < field.getHeight(); y++) {
      final String[] cells = rows[y].split(",");

      // parse each cell of the row
      for (int x = 0; x < field.getWidth(); x++) {
        int cellCode = Integer.parseInt(cells[x]);
        if (removeShapes && cellCode == CellType.SHAPE.getCode())
          cellCode = CellType.EMPTY.getCode();
        field.getCell(x, y).setState(CellType.values()[cellCode]);
      }
    }
  }

  public boolean isRemoveShapes() {
    return removeShapes;
  }

  public void setRemoveShapes(boolean removeShapes) {
    this.removeShapes = removeShapes;
  }
}
