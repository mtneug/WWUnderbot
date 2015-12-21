/*
 * Copyright (c) 2015. WWUnderbot team
 */

package wwunderbot.bot;

import wwunderbot.field.Cell;
import wwunderbot.field.CellType;
import wwunderbot.field.Field;

public class AssessField {

  private Field field;
  private Cell[][] grid;
  private int width;
  private int height;
  private int[] columnHeights;

  public AssessField(Field field){
    this.field = field;
    this.grid = field.getGrid();
    this.width = field.getWidth();
    this.height = field.getHeight();
    this.columnHeights = new int[this.width];
  }
  /**
   * Function to calculate the aggregated height of the current field. The aggregated height describes the summed up
   * height of all columns.
   * The height is determined as the difference between the ground of the field and the highest block in the column.
   *
   * More information can be found here:
   *  <url>https://codemyroad.wordpress.com/2013/04/14/tetris-ai-the-near-perfect-player/</url>
   *
   * @return  int containing the aggregated height of the grid and the number of holes in it.
   */
  private int getAggregateHeight() {
    //  Create and initialize variables for aggregated height and hole count (0 at beginning).
    int height = 0;

    //  Iterate over the grid column by column and calculate the heights.
    for(int x = 0; x < this.width; x++) {
      int columnHeight = heightOfColumn(x);
      height = height + columnHeight;
      this.columnHeights[x] = columnHeight;
    }

    return height;
  }

  // Calculate for every column separately the number of holes by counting every empty cell after a Block.
  private int getHoles() {
    int holes  = 0;
    boolean closed = false;
    for(int x = 0; x < field.getWidth(); x++){
      for(int y = 0; y < field.getHeight(); y++){
        if(grid[x][y].getState() == CellType.BLOCK){
          closed = true;
        }else if(closed && grid[x][y].getState() == CellType.EMPTY){
          holes++;
        }
      }
      closed = false;
    }
    return holes;
  }

  // Start for every row at the very left and check every cell if it is a Block and add one to x until it reaches the
  // end of the field. By that one complete Line is found.
  private int getCompleteness() {
    int completeLines = 0;
    for(int y = 0; y < this.height; y++) {
      int x = 0;
      while(grid[x][y].getState() == CellType.BLOCK && x < this.width) {
        x++;
      }
      if(x == this.width){
        completeLines++;
      }
    }
    return completeLines;
  }

  // Calculate Bumpiness by adding all absolute differences between neighbored columns.
  private int getBumpiness() {
    int bumpiness = 0;
    for(int x = 0; x < field.getWidth() - 1; x++) {
      bumpiness = bumpiness + Math.abs(columnHeights[x] - columnHeights[x + 1]);
    }
    return bumpiness;
  }

  // Calculates the height of a given column x.
  private int heightOfColumn(int x) {
    int y = 0;
    if(x < field.getWidth()) {
      while(grid[x][y].getState() == CellType.EMPTY) {
        y++;
      }
      return (this.height - y);
    }
  }

  // Function for executing all methods (in the right order).
  public int[] assessField() {
    int height = getAggregateHeight();
    int holes = getHoles();
    int completeness = getCompleteness();
    int bumpiness = getBumpiness();

    return new int[]{height, holes, completeness, bumpiness}
  }
}

}
