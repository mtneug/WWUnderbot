/*
 * Copyright (c) 2015. WWUnderbot team
 */

package de.unimuenster.wi.wwunderbot.algorithm;

import com.theaigames.blockbattle.bot.BotState;
import com.theaigames.blockbattle.models.Field;
import com.theaigames.blockbattle.models.MoveType;
import com.theaigames.blockbattle.models.Point;
import com.theaigames.blockbattle.models.Shape;
import de.unimuenster.wi.wwunderbot.ga.BotStateEvaluationFunction;

import java.util.ArrayList;

/**
 * @author Alexander
 * @author Frederik
 * @author Marco
 * @author Matthias
 */
public class HeuristicAlgorithm extends AbstractAlgorithm<Void, ArrayList<MoveType>> {
  private final BotState state;
  private final BotStateEvaluationFunction evaluationFunction;

  public HeuristicAlgorithm(final BotState state, final BotStateEvaluationFunction evaluationFunction) {
    this.state = state;
    this.evaluationFunction = evaluationFunction;
  }

  @Override
  public ArrayList<MoveType> generate(Void v) {
    final Shape[] shapes = {
        state.getCurrentShape(),
        state.getNextShape()
    };
    return findTargetShapeState(shapes, 0).getMoves(shapes[0].getLocation());
  }

  private ShapeStateAssessment findTargetShapeState(Shape[] shapes, final int shapeIndex) {
    final Field field = state.getMyField();
    final Shape shape = shapes[shapeIndex];
    final Point originalLocation = shape.getLocation().clone();
    double score;
    boolean hasColumnsToAssess;
    ShapeStateAssessment bestShapeStateAssessment = new ShapeStateAssessment(shape, Integer.MIN_VALUE);

    // Try all possible rotations
    for (int rotation = 0; rotation < 4; rotation++) {
      // Set position to top left corner
      shape.moveToOrigin();

      // Try all possible columns
      hasColumnsToAssess = true;
      while (hasColumnsToAssess) {
        if (shape.isRight()) hasColumnsToAssess = false;
        if (!field.canBeAdded(shape)) continue;

        Point locationAfterRotation = shape.getLocation().clone();
        shape.drop();

        // Calculate score of field assuming we add the shape to it
        field.addShape(shape);
        if (shapeIndex == shapes.length - 1)
          score = evaluationFunction.evaluate(state);
        else
          score = findTargetShapeState(shapes, shapeIndex + 1).score;
        field.removeShape(shape);

        // Is the score better?
        if (score > bestShapeStateAssessment.score)
          bestShapeStateAssessment = new ShapeStateAssessment(shape, score);

        shape.setLocation(locationAfterRotation);
        shape.oneRight();
      }

      // Rotate shape
      shape.rotateRight();
    }

    shape.setLocation(originalLocation);
    return bestShapeStateAssessment;
  }

  public class ShapeStateAssessment {
    public final Point location;
    public final int rotation;
    public final double score;

    public ShapeStateAssessment(final Shape shape, final double score) {
      this(shape.getLocation().clone(), shape.getRotation(), score);
    }

    public ShapeStateAssessment(final Point location, final int rotation, final double score) {
      this.location = location;
      this.rotation = rotation;
      this.score = score;
    }

    public ArrayList<MoveType> getMoves(final Point currentLocation) {
      final ArrayList<MoveType> moves = new ArrayList<>();

      // Rotate
      switch (rotation) {
        case 2:
          moves.add(MoveType.TURNRIGHT);
          // fall-through intended
        case 1:
          moves.add(MoveType.TURNRIGHT);
          break;
        case 3:
          moves.add(MoveType.TURNLEFT);
          break;
        default:
          throw new IllegalStateException("Rotation cannot be " + rotation);
        case 0:
      }

      // Move to target column
      int currentColumn = currentLocation.x;
      while (currentColumn != location.x) {
        if (currentColumn < location.x) {
          moves.add(MoveType.RIGHT);
          currentColumn++;
        } else {
          moves.add(MoveType.LEFT);
          currentColumn--;
        }
      }

      // Move to bottom
      moves.add(MoveType.DROP);

      return moves;
    }

    @Override
    public String toString() {
      return "ShapeStateAssessment{" +
          "location=" + location +
          ", rotation=" + rotation +
          ", score=" + score +
          '}';
    }
  }
}
