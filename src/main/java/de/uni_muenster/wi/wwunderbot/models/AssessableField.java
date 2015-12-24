/*
 * Copyright (c) 2015. WWUnderbot team
 */

package de.uni_muenster.wi.wwunderbot.models;

import com.theaigames.blockbattle.models.CellType;
import com.theaigames.blockbattle.models.Field;
import com.theaigames.blockbattle.models.Shape;

/**
 * Representation of a field which can assess itself.
 *
 * @author Alexander
 * @author Frederik
 * @author Marco
 * @author Matthias
 */
public class AssessableField extends Field {
  private int[] columnHeights;
  private boolean changed = true;

  public AssessableField(int width, int height, String fieldString) {
    super(width, height, fieldString);
    columnHeights = new int[width];
  }

  @Override
  public void addShape(Shape shape) {
    super.addShape(shape);
    changed = true;
  }

  @Override
  public AssessableField clone() {
    final AssessableField cloned = (AssessableField) super.clone();
    cloned.columnHeights = new int[getWidth()];
    System.arraycopy(columnHeights, 0, cloned.columnHeights, 0, getWidth());

    return cloned;
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
    calculateHeights();
    int height = 0;
    for (int x = 0; x < getWidth(); x++)
      height = height + columnHeights[x];
    return height;
  }

  /**
   * Calculate for every column separately the number of holes by counting every empty cell after a Block.
   *
   * @return
   */
  public int getHoles() {
    calculateHeights();
    int holes = 0;
    for (int x = 0; x < getWidth(); x++)
      for (int y = getHeight() - columnHeights[x]; y < getHeight(); y++)
        if (getCell(x, y).getState() == CellType.EMPTY)
          holes++;
    return holes;
  }

  /**
   * Start for every row at the very left and check every cell if it is a Block and add one to x until it reaches the
   * end of the field. By that one complete Line is found.
   *
   * @return
   */
  public int getCompleteness() {
    int x, completeLines = 0;
    for (int y = 0; y < getHeight(); y++) {
      for (x = 0; x < getWidth() && getCell(x, y).getState() == CellType.BLOCK; x++)
        ;
      if (x == getWidth()) completeLines++;
    }
    return completeLines;
  }

  /**
   * Calculate Bumpiness by adding all absolute differences between neighbored columns.
   *
   * @return
   */
  public int getBumpiness() {
    calculateHeights();
    int bumpiness = 0;
    for (int x = 0; x < getWidth() - 1; x++)
      bumpiness = bumpiness + Math.abs(columnHeights[x] - columnHeights[x + 1]);
    return bumpiness;
  }

  /**
   * Calculates heights of all columns.
   */
  private void calculateHeights() {
    if (!changed) return;
    int y;
    for (int x = 0; x < getWidth(); x++) {
      for (y = 0; y < getHeight() && getCell(x, y).getState() == CellType.EMPTY; y++)
        ;
      columnHeights[x] = getHeight() - y;
    }
    changed = false;
  }
}
