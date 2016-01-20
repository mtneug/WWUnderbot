/*
 * Copyright (c) 2015. WWUnderbot team
 */

package com.theaigames.blockbattle.models;

/**
 * Represents the playing field for one player. Has some basic methods already
 * implemented.
 *
 * @author Jim van Eeden <jim@starapple.nl>
 * @author Alexander
 * @author Frederik
 * @author Marco
 * @author Matthias
 */
public class Field implements Cloneable {
  private final int width;
  private final int height;
  private Cell[][] grid;

  public Field(final int width, final int height) {
    this.width = width;
    this.height = height;
    initGrid();
  }

  private void initGrid() {
    grid = new Cell[width][height];
    for (int x = 0; x < width; x++)
      for (int y = 0; y < height; y++)
        grid[x][y] = new Cell(new Point(x, y));
  }

  @Override
  public Field clone() {
    Field cloned;
    try {
      cloned = (Field) super.clone();
    } catch (CloneNotSupportedException e) {
      // this shouldn't happen, since we are Cloneable
      throw new InternalError(e);
    }

    cloned.grid = new Cell[width][height];
    for (int x = 0; x < width; x++)
      for (int y = 0; y < height; y++)
        cloned.grid[x][y] = grid[x][y].clone();

    return cloned;
  }

  public void addShape(final Shape shape) {
    for (int i = 0; i < shape.getBlocks().length; i++) {
      final Point location = shape.getBlocks()[i].getLocation();
      if (!isOutOfBoundaries(location))
        getCell(location).setState(CellType.SHAPE);
    }
  }

  public void removeShape(final Shape shape) {
    for (int i = 0; i < shape.getBlocks().length; i++) {
      final Point location = shape.getBlocks()[i].getLocation();
      if (!isOutOfBoundaries(location))
        getCell(location).setState(CellType.EMPTY);
    }
  }

  /**
   * Checks if a point lies outside of the boundaries of this field.
   *
   * @param point the point
   * @return {@code true} if it is outside of the boundaries, {@code false}
   * otherwise.
   */
  public boolean isOutOfBoundaries(final Point point) {
    return isOutOfBoundaries(point.x, point.y);
  }

  public boolean isOutOfBoundaries(final int x, final int y) {
    return 0 > x || x >= width || 0 > y || y >= height;
  }

  public boolean isOutOfBoundaries(final Shape shape) {
    for (final Cell block : shape.getBlocks())
      if (isOutOfBoundaries(block.getLocation()))
        return true;

    return false;
  }

  public boolean canBeAdded(final Cell cell) {
    if (cell.getLocation() == null)
      throw new IllegalArgumentException("No location is set");

    if (isOutOfBoundaries(cell.getLocation()))
      return false;

    final Cell fieldCell = getCell(cell.getLocation());
    return !(cell.getState() == CellType.SHAPE && (fieldCell.isShape() || fieldCell.isSolid() || fieldCell.isBlock()));
  }

  public boolean canBeAdded(final Shape shape) {
    for (final Cell block : shape.getBlocks())
      if (!canBeAdded(block))
        return false;

    return true;
  }

  public Cell getCell(final Point point) {
    return getCell(point.x, point.y);
  }

  public Cell getCell(final int x, final int y) {
    if (isOutOfBoundaries(x, y))
      throw new IndexOutOfBoundsException("point is not in field.");

    return grid[x][y];
  }

  public int getHeight() {
    return height;
  }

  public int getWidth() {
    return width;
  }
}