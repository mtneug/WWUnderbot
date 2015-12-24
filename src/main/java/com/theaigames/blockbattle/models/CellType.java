/*
 * Copyright (c) 2015. WWUnderbot team
 */

package com.theaigames.blockbattle.models;

/**
 * Enum of all the possible Cell types.
 *
 * @author Jim van Eeden <jim@starapple.nl>
 * @author Alexander
 * @author Frederik
 * @author Marco
 * @author Matthias
 */
public enum CellType {
  EMPTY(0),
  SHAPE(1),
  BLOCK(2),
  SOLID(3);

  private final int code;

  CellType(int code) {
    this.code = code;
  }

  public int getCode() {
    return this.code;
  }
}
