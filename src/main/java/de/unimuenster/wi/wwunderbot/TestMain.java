/*
 * Copyright (c) 2015. WWUnderbot team
 */

package de.unimuenster.wi.wwunderbot;

import com.theaigames.blockbattle.models.*;
import de.unimuenster.wi.wwunderbot.ga.Genome;
import de.unimuenster.wi.wwunderbot.ga.HeuristicEvaluationFunction;

import java.util.Arrays;

import static com.theaigames.blockbattle.models.MoveType.*;

/**
 * Main entry point.
 *
 * @author Alexander
 * @author Frederik
 * @author Marco
 * @author Matthias
 */
public class TestMain {

  public void main(String[] args) {
    Field field = new Field(10, 20);
    HeuristicEvaluationFunction evaluationFunction = new HeuristicEvaluationFunction(new Genome(-0.510066, 0.760666, -0.35663, -0.184483));

    Moves[] movesArray = {
        new Moves(
            new Shape(ShapeType.J, field, new Point(3, -1)),
            Arrays.asList(
                LEFT,
                LEFT,
                LEFT,
                LEFT,
                LEFT,
                DOWN,
                DOWN,
                DOWN,
                DOWN,
                LEFT,
                DOWN,
                DOWN,
                TURNRIGHT,
                TURNRIGHT,
                DOWN,
                DOWN,
                DOWN,
                DOWN,
                DOWN,
                DOWN,
                DOWN,
                DROP
            )
        ),
        new Moves(
            new Shape(ShapeType.I, field, new Point(4, -1)),
            Arrays.asList(
                DOWN,
                DOWN,
                DOWN,
                DOWN,
                DOWN,
                RIGHT,
                DOWN,
                DOWN,
                DOWN,
                DOWN,
                RIGHT,
                RIGHT,
                DOWN,
                DOWN,
                DOWN,
                TURNLEFT,
                TURNLEFT,
                DOWN,
                DROP
            )
        )
    };

    for (int i = 0; i < 10; i++) {
      for (Moves moves : movesArray) {
        moves.checkAndApply();
        field.addShape(moves.shape);
      }

      System.out.println(evaluationFunction.evaluateField(field));

      for (Moves moves : movesArray) {
        field.removeShape(moves.shape);
        moves.reset();
      }
    }
  }
}
