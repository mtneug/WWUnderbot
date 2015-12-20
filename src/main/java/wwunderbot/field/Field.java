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

/**
 * Field class
 * <p>
 * Represents the playing field for one player. Has some basic methods already
 * implemented.
 *
 * @author Jim van Eeden <jim@starapple.nl>
 */
public class Field implements Cloneable{
  private int width;
  private int height;
  private Cell[][] grid;

  public Field(int width, int height, String fieldString) {
    this.width = width;
    this.height = height;

    parse(fieldString);
  }
  
  @Override
  protected Object clone() throws CloneNotSupportedException {
    Field cloned = (Field) super.clone();
    Cell[][] cloneShape = new Cell[this.width][this.height];
    for(int x = 0; x < this.width; x++) {
      for(int y = 0; y < this.height; y++) {
        cloneShape[x][y] = (Cell) cloned.getGrid()[x][y].clone();
      }
    }
    return cloned;
  }

  /**
   * Parses the input string to get a grid with Cell objects
   *
   * @param fieldString input string
   */
  private void parse(String fieldString) {
    this.grid = new Cell[this.width][this.height];

    // get the separate rows
    String[] rows = fieldString.split(";");
    for (int y = 0; y < this.height; y++) {
      String[] rowCells = rows[y].split(",");

      // parse each cell of the row
      for (int x = 0; x < this.width; x++) {
        int cellCode = Integer.parseInt(rowCells[x]);
        this.grid[x][y] = new Cell(x, y, CellType.values()[cellCode]);
      }
    }
  }

  public Cell getCell(int x, int y) {
    if (x < 0 || x >= this.width || y < 0 || y >= this.height)
      return null;

    return this.grid[x][y];
  }

  public int getHeight() {
    return this.height;
  }

  public int getWidth() {
    return this.width;
  }

  public boolean isValid(Shape shape) {
    Cell[] blocks = shape.getBlocks();

    for(Cell block : blocks) {
      if(block.hasCollision(this)) {
        return false;
      };
    }
    return true;
  }

  public Cell[][] getGrid() {
    return grid;
  }

  public void setGrid(Cell[][] grid) {
    this.grid = grid;
  }

  /**
   * Function to calculate the aggregated height of the current field. The aggregated height describes the summed up
   * height of all columns.
   * The height is determined as the difference between the ground of the field and the highest block in the column.
   *
   * More information can be found here:
   *  <url>https://codemyroad.wordpress.com/2013/04/14/tetris-ai-the-near-perfect-player/</url>
   *
   * @return  int with the aggregated height of the grid.
   */
  public int getAggregateHeight() {
    //  Create variable to capture position of the heighest block on the stack.
    int heighestBlock;

    //  Create and initialize variable for aggregated height (0 at beginning).
    int height = 0;

    //  Iterate over the grid column by column and calculate the heights.
    for(int x = 0; x < this.width; x++) {

      //  Initialize/Reset the variable for heighest block position - (-1) taken as it is no valid position.
      //  (allows easy checking whether the topmost block has been detected)
      heighestBlock = -1;

      for(int y = 0; y < this.height; y++) {

        //  Check if a given grid cell represents is either solid or a block (contributes to the height).
        if((grid[x][y].getState() == CellType.SOLID)
            || (grid[x][y].getState() == CellType.BLOCK)){

          // The first found cell determines the highest block in the current column and the position is stored.
          heighestBlock = y;

          //  Since we want to determine the height of the column, no further iterations are needed, since the height
          //  is determined by the ground and the highest block.
          y = this.height;
        }
      }

      //  If a block is given in the column heighestBlock will be greater than 0 and height will have to be added
      //  to the aggregate height.
      if(heighestBlock >= 0) {
        height = height + (this.height - heighestBlock);
      }

    }

    return height;
  }
}
