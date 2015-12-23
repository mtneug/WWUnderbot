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

import java.awt.*;

/**
 * Field class
 * <p>
 * Represents the playing field for one player. Has some basic methods already
 * implemented.
 *
 * @author Jim van Eeden <jim@starapple.nl>
 */
public class Field implements Cloneable {
  private final int width;
  private final int height;
  private Cell[][] grid;

  public Field(final int width, final int height, final String fieldString) {
    this.width = width;
    this.height = height;
    parse(fieldString);
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

    final Cell[][] cloneGrid = new Cell[width][height];
    cloned.grid = cloneGrid;

    for (int x = 0; x < width; x++)
      for (int y = 0; y < height; y++)
        cloneGrid[x][y] = grid[x][y].clone();

    return cloned;
  }

  /**
   * Parses the input string to get a grid with Cell objects
   *
   * @param fieldString input string
   */
  private void parse(final String fieldString) {
    grid = new Cell[width][height];

    // get the separate rows
    final String[] rows = fieldString.split(";");
    for (int y = 0; y < height; y++) {
      final String[] cells = rows[y].split(",");

      // parse each cell of the row
      for (int x = 0; x < width; x++) {
        final int cellCode = Integer.parseInt(cells[x]);
        grid[x][y] = new Cell(new Point(x, y), CellType.values()[cellCode]);
      }
    }
  }

  public void addShape(final Shape shape) {
    for (int i = 0; i < shape.getBlocks().length; i++) {
      final Point location = shape.getBlocks()[i].getLocation();
      getCell(location).setState(CellType.BLOCK);
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

  public boolean canBeAdded(final Cell cell) {
    if (cell.getLocation() == null)
      throw new IllegalArgumentException("No location is set");

    if (isOutOfBoundaries(cell.getLocation()))
      return false;

    final Cell fieldCell = getCell(cell.getLocation());
    return !(cell.getState() == CellType.SHAPE && (fieldCell.isSolid() || fieldCell.isBlock()));
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

  public Cell[][] getGrid() {
    return grid;
  }
}