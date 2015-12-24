/*
 * Copyright (c) 2015. WWUnderbot team
 */

package com.theaigames.blockbattle.models;

/**
 * Represents one Cell in the playing field.
 *
 * @author Jim van Eeden <jim@starapple.nl>
 * @author Alexander
 * @author Frederik
 * @author Marco
 * @author Matthias
 */
public class Cell implements Cloneable {
  private Point location;
  private CellType state;

  public Cell() {
    this(null, CellType.EMPTY);
  }

  public Cell(final Point location, final CellType type) {
    this.location = location;
    this.state = type;
  }

  @Override
  public Cell clone() {
    Cell cloned;
    try {
      cloned = (Cell) super.clone();
    } catch (CloneNotSupportedException e) {
      // this shouldn't happen, since we are Cloneable
      throw new InternalError(e);
    }

    if (cloned.location != null)
      cloned.setLocation(location.clone());

    return cloned;
  }

  public boolean isShape() {
    return state == CellType.SHAPE;
  }

  public boolean isSolid() {
    return state == CellType.SOLID;
  }

  public boolean isBlock() {
    return state == CellType.BLOCK;
  }

  public boolean isEmpty() {
    return state == CellType.EMPTY;
  }

  public CellType getState() {
    return state;
  }

  void setState(CellType state) {
    this.state = state;
  }

  public Point getLocation() {
    return location;
  }

  void setLocation(Point point) {
    this.location = point;
  }
}
