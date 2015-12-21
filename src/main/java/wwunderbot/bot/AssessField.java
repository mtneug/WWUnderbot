/*
 * Copyright (c) 2015. WWUnderbot team
 */

package wwunderbot.bot;

import wwunderbot.field.Cell;
import wwunderbot.field.CellType;
import wwunderbot.field.Field;

/**
 * Created by frederikelischberger on 21/12/15.
 */
public class AssessField {

  private Field field;
  private Cell[][] grid;
  private int width;
  private int height;

  public AssessField(Field field){
    this.field = field;
    this.grid = field.getGrid();
    this.width = field.getWidth();
    this.height = field.getHeight();
  }
  /**
   * Function to calculate the aggregated height of the current field. The aggregated height describes the summed up
   * height of all columns.
   * The height is determined as the difference between the ground of the field and the highest block in the column.
   *
   * More information can be found here:
   *  <url>https://codemyroad.wordpress.com/2013/04/14/tetris-ai-the-near-perfect-player/</url>
   *
   * @return  int[2] containing the aggregated height of the grid and the number of holes in it.
   */
  public int getAggregateHeightAndHoles() {
    //  Create and initialize variables for aggregated height and hole count (0 at beginning).
    int height = 0;

    //  Iterate over the grid column by column and calculate the heights.
    for(int x = 0; x < this.width; x++) {
      height = height + heightOfColumn(x);
    }

    return height;
  }

  public int getHoles() {
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
  // end of the field. By that one complete Line is found
  public int getCompleteness() {
    int completeLines = 0;
    for(int y = 0; y < this.width; y++) {
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

  public int getBumpiness() {
    int bumpiness = 0;
    for(int x = 0; x < field.getWidth() - 1; x++) {
      bumpiness = bumpiness + Math.abs(heightOfColumn(x) - heightOfColumn(x + 1));
    }
    return bumpiness;
  }

  public int heightOfColumn(int x) {
    int y = 0;
    if(x < field.getWidth()) {
      while(grid[x][y].getState() == CellType.EMPTY) {
        y++;
      }
      return (this.height - y);
    }
  }

}

}
