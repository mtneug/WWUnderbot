/*
 * Copyright (c) 2015. WWUnderbot team
 */

package wwunderbot.bot;

import wwunderbot.models.grid.Field;
import wwunderbot.models.grid.Shape;
import wwunderbot.geneticAlgorithm.Genome;
import wwunderbot.models.moves.MoveType;

import java.util.*;

/**
 * WWUnderbot class
 *
 * @author Alexander, Frederik, Marco, Matthias
 */
public class WWUnderbot {
  private Genome genome;

  public WWUnderbot(Genome genome) {
    this.genome = genome;
  }

  /**
   * TODO: ...
   *
   * @param state   current state of the bot
   * @param timeout time to respond
   * @return a list of moves to execute
   */
  public ArrayList<MoveType> getMoves(BotState state, long timeout) {
    state.initShapes();

    ArrayList<MoveType> moves = new ArrayList<>();

    try {
      findBestMove(state, state.getMyField(), 0);
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }

    return moves;
  }

  public Shape findBestMove(BotState state, Field field, int shapeIndex) throws CloneNotSupportedException {
    double bestScore = 0;

    Shape currentShape = getWorkingShape(state, shapeIndex);
    Shape bestShape = currentShape;

    //Try all possible rotations
    Shape copy_currentShape = currentShape.clone();

    for(int i = 0; i < 3; i++){
      copy_currentShape.rotateRight();

      //Initialize position of current shape at top left corner
      while(field.canBeAdded(copy_currentShape)) {
        copy_currentShape.oneLeft();
      }
      copy_currentShape.oneRight();

      while(field.canBeAdded(copy_currentShape)) {
        Shape copy2_currentShape = copy_currentShape.clone();
        while(field.canBeAdded(copy_currentShape)) {
          copy2_currentShape.oneDown();
        }
        copy2_currentShape.oneUp();

        Field copy_field = (Field) field.clone();
        copy_field.addShape(copy2_currentShape);

        double score = 0;
        if(shapeIndex == 0) {
          score = findBestMove(state, copy_field, shapeIndex + 1).getScore();
        } else {
          score = calculateScore(state.getMyField());
          copy_currentShape.setScore(score);
        }

        if(score > bestScore || bestScore == 0) {
          bestScore = score;
          bestShape = copy_currentShape;
        }

        copy_currentShape.oneRight();
      }
    }
    return bestShape;
  }

  private double calculateScore(AssessableField field) {
    final int height = field.getAggregateHeight();
    final int holes = field.getHoles();
    final int completeness = field.getCompleteness();
    final int bumpiness = field.getBumpiness();

    return genome.getHeightWeight() * height
      + genome.getHolesWeight() * holes
      + genome.getCompletenessWeight() * completeness
      + genome.getBumpinessWeight() * bumpiness;
  }

  private Shape getWorkingShape(BotState state, int i) {
    if (i == 0) {
      return state.getCurrentShape();
    } else {
      return state.getNextShape();
    }
  }
}
