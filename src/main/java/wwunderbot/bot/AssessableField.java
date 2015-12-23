/*
 * Copyright (c) 2015. WWUnderbot team
 */

package wwunderbot.bot;

import wwunderbot.models.grid.CellType;
import wwunderbot.models.grid.Field;

/**
 * @author Alexander, Frederik, Marco, Matthias
 */
public class AssessableField extends Field {
  private int[] columnHeights;

  public AssessableField(int width, int height, String fieldString) {
    super(width, height, fieldString);
    columnHeights = new int[width];
  }

  /**
   * Function to calculate the aggregated height of the current field. The aggregated height describes the summed up
   * height of all columns.
   * The height is determined as the difference between the ground of the field and the highest block in the column.
   * <p>
   * More information can be found here:
   * <url>https://codemyroad.wordpress.com/2013/04/14/tetris-ai-the-near-perfect-player/</url>
   *
   * @return int containing the aggregated height of the grid and the number of holes in it.
   */
  public int getAggregateHeight() {
    //  Create and initialize variables for aggregated height and hole count (0 at beginning).
    int height = 0;

    //  Iterate over the grid column by column and calculate the heights.
    for (int x = 0; x < getWidth(); x++) {
      int columnHeight = heightOfColumn(x);
      height = height + columnHeight;
      this.columnHeights[x] = columnHeight;
    }

    return height;
  }

  // Calculate for every column separately the number of holes by counting every empty cell after a Block.
  public int getHoles() {
    int holes = 0;
    boolean closed = false;
    for (int x = 0; x < getWidth(); x++) {
      for (int y = 0; y < getHeight(); y++) {
        if (getCell(x, y).getState() == CellType.BLOCK) {
          closed = true;
        } else if (closed && getCell(x, y).getState() == CellType.EMPTY) {
          holes++;
        }
      }
      closed = false;
    }
    return holes;
  }

  // Start for every row at the very left and check every cell if it is a Block and add one to x until it reaches the
  // end of the field. By that one complete Line is found.
  public int getCompleteness() {
    int completeLines = 0;
    for (int y = 0; y < getHeight(); y++) {
      int x = 0;
      while (getCell(x, y).getState() == CellType.BLOCK && x < getWidth()) {
        x++;
      }
      if (x == getWidth()) {
        completeLines++;
      }
    }
    return completeLines;
  }

  // Calculate Bumpiness by adding all absolute differences between neighbored columns.
  public int getBumpiness() {
    int bumpiness = 0;
    for (int x = 0; x < getWidth() - 1; x++) {
      bumpiness = bumpiness + Math.abs(columnHeights[x] - columnHeights[x + 1]);
    }
    return bumpiness;
  }

  // Calculates the height of a given column x.
  private int heightOfColumn(int x) {
    int y = 0;
    if (x < getWidth()) {
      while (getCell(x, y).getState() == CellType.EMPTY) {
        y++;
      }
    }
    return getHeight() - y;
  }

  // Function for executing all methods (in the right order).
  public int[] assessField() {
    int height = getAggregateHeight();
    int holes = getHoles();
    int completeness = getCompleteness();
    int bumpiness = getBumpiness();

    return new int[]{height, holes, completeness, bumpiness};
  }
}
