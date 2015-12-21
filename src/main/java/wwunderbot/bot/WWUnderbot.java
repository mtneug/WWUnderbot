/*
 * Copyright (c) 2015. WWUnderbot team
 */

//    Licensed under the Apache License, Version 2.0 (the "License");
//    you may not use this file except in compliance with the License.
//    You may obtain a copy of the License at

//        http://www.apache.org/licenses/LICENSE-2.0

package wwunderbot.bot;

import wwunderbot.field.Field;
import wwunderbot.field.Shape;
import wwunderbot.geneticAlgorithm.Genome;
import wwunderbot.moves.MoveType;

import java.util.*;

/**
 * WWUnderbot class
 * <p>
 * This class is where the main logic should be. Implement getMoves() to return
 * something better than random moves.
 *
 * @author Jim van Eeden <jim@starapple.nl>
 */
public class WWUnderbot {

  private BotState state;
  private Genome genome;

  public WWUnderbot(Genome genome) {
    this.genome = genome;
  }

  /**
   * Returns a random amount of random moves
   *
   * @param state   current state of the bot
   * @param timeout time to respond
   * @return a list of moves to execute
   */
  public ArrayList<MoveType> getMoves(BotState state, long timeout) {
    ArrayList<MoveType> moves = new ArrayList<MoveType>();

    return moves;
  }

  public Shape findBestMove(Field field, int shapeIndex) throws CloneNotSupportedException {
    double bestScore = 0;

    Shape currentShape = getWorkingShape(shapeIndex, field);
    Shape bestShape = currentShape;

    //Try all possible rotations
    Shape copy_currentShape = currentShape.clone();

    for(int i = 0; i < 3; i++){
      copy_currentShape.turnRight();

      //Initialize position of current shape at top left corner
      while(field.isValid(copy_currentShape)) {
        copy_currentShape.oneLeft();
      }
      copy_currentShape.oneRight();

      while(field.isValid(copy_currentShape)) {
        Shape copy2_currentShape = copy_currentShape.clone();
        while(field.isValid(copy_currentShape)) {
          copy2_currentShape.oneDown();
        }
        copy2_currentShape.oneUp();

        Field copy_field = (Field) field.clone();
        copy_field.addShape(copy2_currentShape);

        double score = 0;
        if(shapeIndex == 0) {
          score = findBestMove(copy_field, shapeIndex + 1).getScore();
        } else {
          score = calculateScore();
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

  public BotState getState() {
    return state;
  }

  public void setState(BotState state) {
    this.state = state;
  }

  private double calculateScore() {
    int height = state.getAssessField().assessField()[0];
    int holes = state.getAssessField().assessField()[1];
    int completeness = state.getAssessField().assessField()[2];
    int bumpiness = state.getAssessField().assessField()[3];

    return - genome.getHeightWeight() * height
      + genome.getHolesWeight() * holes
      - genome.getCompleteness() * completeness
      - genome.getBumpinessWeight() * bumpiness;
  }

  private Shape getWorkingShape(int i, Field field) {
    if(i == 0) {
      return new Shape(state.getCurrentShapeType(), field, state.getShapeLocation());
    } else {
      return new Shape(state.getNextShapeType(), field, state.getShapeLocation());
    }
  }
}
