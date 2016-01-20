/*
 * Copyright (c) 2015. WWUnderbot team
 */

package de.unimuenster.wi.wwunderbot.ga;

import com.theaigames.blockbattle.bot.BotState;
import com.theaigames.blockbattle.models.CellType;
import com.theaigames.blockbattle.models.Field;

/**
 * @author Alexander
 * @author Frederik
 * @author Marco
 * @author Matthias
 */
public class HeuristicEvaluationFunction extends BotStateEvaluationFunction {
  private final Genome genome;

  public HeuristicEvaluationFunction(Genome genome) {
    this.genome = genome;
  }

  private static int max(int[] ints) {
    int max = ints[0];

    for (int i = 1; i < ints.length; i++)
      if (ints[i] > max)
        max = ints[i];

    return max;
  }

  @Override
  public double evaluate(BotState state) {
    double score = evaluateField(state.getMyField());
    if (state.hasLostLookahead()) score = Double.NEGATIVE_INFINITY;
    return score;
  }

  public double evaluateField(Field field) {
    final int[] columnHeights = calculateHeights(field);
    return genome.getHeightWeight() * getAggregateHeight(columnHeights)
        + genome.getCompletenessWeight() * getCompleteness(field, columnHeights)
        + genome.getHolesWeight() * getHoles(field, columnHeights)
        + genome.getBumpinessWeight() * getBumpiness(columnHeights);
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
  private int getAggregateHeight(int[] columnHeights) {
    int height = 0;
    for (int x = 0; x < columnHeights.length; x++)
      height = height + columnHeights[x];
    return height;
  }

  /**
   * Calculate for every column separately the number of holes by counting every empty cell after a Block.
   *
   * @param field
   * @return
   */
  private int getHoles(Field field, int[] columnHeights) {
    int holes = 0;
    for (int x = 0; x < field.getWidth(); x++)
      for (int y = field.getHeight() - columnHeights[x]; y < field.getHeight(); y++)
        if (field.getCell(x, y).getState() == CellType.EMPTY)
          holes++;
    return holes;
  }

  /**
   * Start for every row at the very left and check every cell if it is a Block and add one to x until it reaches the
   * end of the field. By that one complete Line is found.
   *
   * @param field
   * @param columnHeights
   * @return
   */
  private int getCompleteness(Field field, int[] columnHeights) {
    int x, completeLines = 0;
    for (int y = 0; y < field.getHeight(); y++) {
      for (x = 0; x < field.getWidth()
          && (field.getCell(x, y).getState() == CellType.BLOCK
          || field.getCell(x, y).getState() == CellType.SHAPE)
          ; x++)
        ;
      if (x == field.getWidth()) completeLines++;
    }
    if (completeLines == 1 && max(columnHeights) < 1.0 / 2.0 * field.getHeight())
      completeLines--;
    return completeLines;
  }

  /**
   * Calculate Bumpiness by adding all absolute differences between neighbored columns.
   *
   * @return
   */
  private int getBumpiness(int[] columnHeights) {
    int bumpiness = 0;
    for (int x = 0; x < columnHeights.length - 1; x++)
      bumpiness = bumpiness + Math.abs(columnHeights[x] - columnHeights[x + 1]);
    return bumpiness;
  }

  /**
   * Calculates heights of all columns.
   */
  private int[] calculateHeights(Field field) {
    int y;
    int[] columnHeights = new int[field.getWidth()];
    for (int x = 0; x < field.getWidth(); x++) {
      for (y = 0; y < field.getHeight() && field.getCell(x, y).getState() == CellType.EMPTY; y++)
        ;
      columnHeights[x] = field.getHeight() - y;
    }
    return columnHeights;
  }
}
