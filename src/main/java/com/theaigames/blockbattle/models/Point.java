/*
 * Copyright (c) 2015. WWUnderbot team
 */

package com.theaigames.blockbattle.models;

/**
 * Representation of a Point.
 *
 * @author Alexander
 * @author Frederik
 * @author Marco
 * @author Matthias
 */
public class Point extends java.awt.Point {
  public Point() {
    super();
  }

  public Point(Point p) {
    super(p.x, p.y);
  }

  public Point(int x, int y) {
    super(x, y);
  }

  @Override
  public Point clone() {
    return (Point) super.clone();
  }
}
