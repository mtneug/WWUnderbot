/*
 * Copyright (c) 2015. WWUnderbot team
 */

package com.theaigames.blockbattle.models;

/**
 * Enum for all possible move types.
 *
 * @author Jim van Eeden <jim@starapple.nl>
 * @author Alexander
 * @author Frederik
 * @author Marco
 * @author Matthias
 */
public enum MoveType {
  DOWN, LEFT, RIGHT, TURNLEFT, TURNRIGHT, DROP, SKIP;

  @Override
  public String toString() {
    return this.name().toLowerCase();
  }
}
