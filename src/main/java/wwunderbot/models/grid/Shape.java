// Copyright 2015 theaigames.com (developers@theaigames.com)

//    Licensed under the Apache License, Version 2.0 (the "License");
//    you may not use this file except in compliance with the License.
//    You may obtain a copy of the License at

//        http://www.apache.org/licenses/LICENSE-2.0

//    Unless required by applicable law or agreed to in writing, software
//    distributed under the License is distributed on an "AS IS" BASIS,
//    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//    See the License for the specific language governing permissions and
//    limitations under the License.
//
//    For the full copyright and license information, please view the LICENSE
//    file that was distributed with this source code.

package wwunderbot.models.grid;

import wwunderbot.models.moves.MoveType;

import java.awt.*;
import java.util.ArrayList;

/**
 * Shape class
 * <p>
 * Represents the shapes that appear in the field. Some basic methods have
 * already been implemented, but actual move actions, etc. should still be
 * created.
 *
 * @author Jim van Eeden <jim@starapple.nl>
 */
public class Shape implements Cloneable {
  public ShapeType type;
  /**
   * 2-dimensional bounding box: a matrix that contains the block-cells of the shape
   */
  private Cell[][] shape;

  /**
   * Array that contains only the block-cells of the shape
   */
  private Cell[] blocks = new Cell[4];

  private int size;

  /**
   * Upper left position of the cell in the field
   */
  private Point location;
  private Field field;
  private ArrayList<MoveType> moves = new ArrayList<>();
  private double score = 0;

  public Shape(final ShapeType type, final Field field, final Point location) {
    if (type == null || field == null || location == null)
      throw new IllegalArgumentException("type, field and location cannot be null");

    this.type = type;
    this.field = field;
    this.location = location;

    setShape();
    setBlockLocations();
  }

  /**
   * Set shape in square box. Creates new Cells that can be checked against the
   * actual playing field.
   * shape[y][x]
   */
  private void setShape() {
    switch (type) {
      case I:
        size = 4;
        initializeShape();
        blocks[0] = shape[0][1];
        blocks[1] = shape[1][1];
        blocks[2] = shape[2][1];
        blocks[3] = shape[3][1];
        break;

      case J:
        size = 3;
        initializeShape();
        blocks[0] = shape[0][0];
        blocks[1] = shape[0][1];
        blocks[2] = shape[1][1];
        blocks[3] = shape[2][1];
        break;

      case L:
        size = 3;
        initializeShape();
        blocks[0] = shape[2][0];
        blocks[1] = shape[0][1];
        blocks[2] = shape[1][1];
        blocks[3] = shape[2][1];
        break;

      case O:
        size = 2;
        initializeShape();
        blocks[0] = shape[0][0];
        blocks[1] = shape[1][0];
        blocks[2] = shape[0][1];
        blocks[3] = shape[1][1];
        break;

      case S:
        size = 3;
        initializeShape();
        blocks[0] = shape[1][0];
        blocks[1] = shape[2][0];
        blocks[2] = shape[0][1];
        blocks[3] = shape[1][1];
        break;

      case T:
        size = 3;
        initializeShape();
        blocks[0] = shape[1][0];
        blocks[1] = shape[0][1];
        blocks[2] = shape[1][1];
        blocks[3] = shape[2][1];
        break;

      case Z:
        size = 3;
        initializeShape();
        blocks[0] = shape[0][0];
        blocks[1] = shape[1][0];
        blocks[2] = shape[1][1];
        blocks[3] = shape[2][1];
        break;
    }

    for (final Cell block : blocks)
      block.setState(CellType.SHAPE);
  }

  /**
   * Creates the matrix for the shape
   */
  private void initializeShape() {
    shape = new Cell[size][size];
    for (int y = 0; y < size; y++)
      for (int x = 0; x < size; x++)
        shape[x][y] = new Cell();
  }

  @Override
  public Shape clone() {
    Shape cloned;
    try {
      cloned = (Shape) super.clone();
    } catch (CloneNotSupportedException e) {
      // this shouldn't happen, since we are Cloneable
      throw new InternalError(e);
    }

    cloned.location = (Point) location.clone();
    cloned.field = field.clone();

    final Cell[][] cloneShape = new Cell[size][size];
    cloned.shape = cloneShape;
    for (int x = 0; x < size; x++)
      for (int y = 0; y < size; y++)
        cloneShape[x][y] = shape[x][y].clone();

    final Cell[] cloneBlocks = new Cell[4];
    cloned.blocks = cloneBlocks;
    for (int x = 0; x < size; x++)
      cloneBlocks[x] = blocks[x].clone();

    return cloned;
  }

  // ACTIONS (no checks for errors are performed in the actions!)

  /**
   * Rotates the shape counter-clockwise
   */
  public void rotateLeft() {
    final Cell[][] temp = transposeShape();
    for (int y = 0; y < size; y++)
      for (int x = 0; x < size; x++)
        shape[x][y] = temp[x][size - y - 1];

    setBlockLocations();
  }

  /**
   * Rotates the shape clockwise
   */
  public void rotateRight() {
    final Cell[][] temp = transposeShape();
    for (int x = 0; x < size; x++)
      shape[x] = temp[size - x - 1];

    setBlockLocations();
  }

  public void oneDown() {
    location.y++;
    setBlockLocations();
  }

  public void oneUp() {
    location.y--;
    setBlockLocations();
  }

  public void oneRight() {
    location.x++;
    setBlockLocations();
  }

  public void oneLeft() {
    location.x--;
    setBlockLocations();
  }

  /**
   * Used for rotations
   *
   * @return transposed matrix of current shape box
   */
  private Cell[][] transposeShape() {
    final Cell[][] temp = new Cell[size][size];
    for (int y = 0; y < size; y++)
      for (int x = 0; x < size; x++)
        temp[y][x] = shape[x][y];

    return temp;
  }

  /**
   * Uses the shape's current orientation and position to set the actual
   * location of the block-type cells on the field
   */
  private void setBlockLocations() {
    for (int y = 0; y < size; y++)
      for (int x = 0; x < size; x++)
        shape[x][y].setLocation(new Point(location.x + x, location.y + y));
  }

  public Cell[] getBlocks() {
    return blocks;
  }

  public Point getLocation() {
    return location;
  }

  public void setLocation(Point location) {
    if (location == null)
      throw new IllegalArgumentException("location cannot be null");

    this.location = location;
    setBlockLocations();
  }

  public ShapeType getType() {
    return type;
  }

  public Field getField() {
    return field;
  }

  public Cell[][] getShape() {
    return shape;
  }

  public int getSize() {
    return size;
  }

  public ArrayList<MoveType> getMoves() {
    return moves;
  }

  public void addMove(MoveType move) {
    moves.add(move);
  }

  public double getScore() {
    return score;
  }

  public void setScore(double score) {
    this.score = score;
  }
}
