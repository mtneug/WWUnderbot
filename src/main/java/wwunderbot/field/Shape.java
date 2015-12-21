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

package wwunderbot.field;

import wwunderbot.moves.MoveType;

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
  private Cell[][] shape; // 2-dimensional bounding box: a matrix that contains the block-cells of the shape
  private Cell[] blocks; // array that contains only the block-cells of the shape
  private int size;
  private Point location; // Upper left position of the cell in the field
  private Field field;
  private ArrayList<MoveType> moves;
  private double score = 0;

  public Shape(ShapeType type, Field field, Point location) {
    this.type = type;
    this.field = field;
    this.blocks = new Cell[4];
    this.location = location;
    this.moves = new ArrayList<MoveType>();

    setShape();
    setBlockLocations();
  }


  @Override
  public Shape clone() throws CloneNotSupportedException {
    Shape cloned = (Shape) super.clone();
    //cloned.setType((ShapeType) cloned.getType().clone());
    Cell[][] cloneShape = new Cell[size][size];
    for(int x = 0; x < size; x++) {
      for(int y = 0; y < size; y++) {
        cloneShape[x][y] = (Cell) cloned.getShape()[x][y].clone();
      }
    }
    cloned.setShape(cloneShape);
    Cell[] cloneBlocks = new Cell[4];
    for(int x = 0; x < size; x++) {
      cloneBlocks[x] = (Cell) cloned.getBlocks()[x].clone();
    }
    cloned.setBlocks(cloneBlocks);
    cloned.setLocation((Point) cloned.getLocation().clone());
    cloned.setField((Field) cloned.getField().clone());
    return cloned;
  }

  // ACTIONS (no checks for errors are performed in the actions!)

  /**
   * Rotates the shape counter-clockwise
   */
  public void turnLeft() {
    Cell[][] temp = this.transposeShape();
    for (int y = 0; y < size; y++) {
      for (int x = 0; x < size; x++) {
        this.shape[x][y] = temp[x][size - y - 1];
      }
    }

    this.setBlockLocations();
  }

  /**
   * Rotates the shape clockwise
   */
  public void turnRight() {
    Cell[][] temp = this.transposeShape();
    for (int x = 0; x < size; x++) {
      this.shape[x] = temp[size - x - 1];
    }

    this.setBlockLocations();
  }

  public void oneDown() {
    this.location.y++;
    this.setBlockLocations();
  }

  public void oneUp() {
    this.location.y--;
    this.setBlockLocations();
  }

  public void oneRight() {
    this.location.x++;
    this.setBlockLocations();
  }

  public void oneLeft() {
    this.location.x--;
    this.setBlockLocations();
  }

  /**
   * Used for rotations
   *
   * @return transposed matrix of current shape box
   */
  private Cell[][] transposeShape() {
    Cell[][] temp = new Cell[size][size];
    for (int y = 0; y < size; y++) {
      for (int x = 0; x < size; x++) {
        temp[y][x] = shape[x][y];
      }
    }
    return temp;
  }

  /**
   * Uses the shape's current orientation and position to set the actual
   * location of the block-type cells on the field
   */
  private void setBlockLocations() {
    for (int y = 0; y < size; y++) {
      for (int x = 0; x < size; x++) {
        //if (shape[x][y].isShape()) {
          shape[x][y].setLocation(location.x + x, location.y + y);
        //}
      }
    }
  }

  /**
   * Set shape in square box. Creates new Cells that can be checked against the
   * actual playing field.
   *  shape[y][x]
   */
  private void setShape() {
    switch (this.type) {
      case I:
        this.size = 4;
        this.shape = initializeShape();
        this.blocks[0] = this.shape[0][1];
        this.blocks[1] = this.shape[1][1];
        this.blocks[2] = this.shape[2][1];
        this.blocks[3] = this.shape[3][1];
        break;

      case J:
        this.size = 3;
        this.shape = initializeShape();
        this.blocks[0] = this.shape[0][0];
        this.blocks[1] = this.shape[0][1];
        this.blocks[2] = this.shape[1][1];
        this.blocks[3] = this.shape[2][1];
        break;

      case L:
        this.size = 3;
        this.shape = initializeShape();
        this.blocks[0] = this.shape[2][0];
        this.blocks[1] = this.shape[0][1];
        this.blocks[2] = this.shape[1][1];
        this.blocks[3] = this.shape[2][1];
        break;

      case O:
        this.size = 2;
        this.shape = initializeShape();
        this.blocks[0] = this.shape[0][0];
        this.blocks[1] = this.shape[1][0];
        this.blocks[2] = this.shape[0][1];
        this.blocks[3] = this.shape[1][1];
        break;

      case S:
        this.size = 3;
        this.shape = initializeShape();
        this.blocks[0] = this.shape[1][0];
        this.blocks[1] = this.shape[2][0];
        this.blocks[2] = this.shape[0][1];
        this.blocks[3] = this.shape[1][1];
        break;

      case T:
        this.size = 3;
        this.shape = initializeShape();
        this.blocks[0] = this.shape[1][0];
        this.blocks[1] = this.shape[0][1];
        this.blocks[2] = this.shape[1][1];
        this.blocks[3] = this.shape[2][1];
        break;

      case Z:
        this.size = 3;
        this.shape = initializeShape();
        this.blocks[0] = this.shape[0][0];
        this.blocks[1] = this.shape[1][0];
        this.blocks[2] = this.shape[1][1];
        this.blocks[3] = this.shape[2][1];
        break;
    }

    // set type to SHAPE
    for (Cell block : blocks) {
      block.setShape();
    }
  }

  /**
   * Creates the matrix for the shape
   *
   * @return
   */
  private Cell[][] initializeShape() {
    Cell[][] newShape = new Cell[size][size];
    for (int y = 0; y < this.size; y++) {
      for (int x = 0; x < this.size; x++) {
        newShape[x][y] = new Cell();
      }
    }
    return newShape;
  }

  public void setLocation(int x, int y) {
    this.location = new Point(x, y);
  }

  public Cell[] getBlocks() {
    return this.blocks;
  }

  public Point getLocation() {
    return this.location;
  }

  public ShapeType getType() {
    return this.type;
  }

  public void setType(ShapeType type) {
    this.type = type;
  }

  public Field getField() {
    return field;
  }

  public void setField(Field field) {
    this.field = field;
  }

  public Cell[][] getShape() {
    return shape;
  }

  public void setShape(Cell[][] shape) {
    this.shape = shape;
  }

  public void setBlocks(Cell[] blocks) {
    this.blocks = blocks;
  }

  public void setLocation(Point location) {
    this.location = location;
  }

  public int getSize() { return size; }

  public void setSize(int size) { this.size = size; }

  public ArrayList<MoveType> getMoves() {
    return moves;
  }

  public void addMove(MoveType move) {
    this.moves.add(move);
  }

  public double getScore() {
    return score;
  }

  public void setScore(double score) {
    this.score = score;
  }
}
