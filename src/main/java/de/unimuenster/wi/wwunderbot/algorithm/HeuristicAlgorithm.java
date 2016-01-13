/*
 * Copyright (c) 2015. WWUnderbot team
 */

package de.unimuenster.wi.wwunderbot.algorithm;

import com.theaigames.blockbattle.bot.BotState;
import com.theaigames.blockbattle.models.*;
import de.unimuenster.wi.wwunderbot.ga.BotStateEvaluationFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander
 * @author Frederik
 * @author Marco
 * @author Matthias
 */
public class HeuristicAlgorithm extends AbstractAlgorithm<Moves[]> {
  private final BotState state;
  private final BotStateEvaluationFunction evaluationFunction;

  public HeuristicAlgorithm(final BotState state, final BotStateEvaluationFunction evaluationFunction) {
    this.state = state;
    this.evaluationFunction = evaluationFunction;
  }

  @Override
  public Moves[] run() {
    final Shape[] shapes = {
        state.getCurrentShape(),
        state.getNextShape()
    };

    final Moves[] moves = new Moves[shapes.length];
    ShapeStateAssessment shapeStateAssessment = findTargetShapeState(shapes, 0);

    for (int i = 0; i < shapes.length; i++) {
      moves[i] = new Moves(shapes[i], shapeStateAssessment.getMoves(shapes[i]));
      shapeStateAssessment = shapeStateAssessment.next;
    }

    return moves;
  }

  private ShapeStateAssessment findTargetShapeState(Shape[] shapes, final int shapeIndex) {
    final Field field = state.getMyField();
    final Shape shape = shapes[shapeIndex];
    final Point originalLocation = shape.getLocation().clone();
    double score;
    boolean hasColumnsToAssess;
    ShapeStateAssessment bestShapeStateAssessment = new ShapeStateAssessment(shape, Double.NEGATIVE_INFINITY, null);
    ShapeStateAssessment bestShapeStateAssessmentNextShape = null;

    // Try all possible rotations
    for (int rotation = 0; rotation < 4; rotation++) {
      // Set position to top left corner
      // TODO: check if we even come here!
      shape.moveToOrigin().oneLeft();

      // Try all possible columns
      hasColumnsToAssess = true;
      while (hasColumnsToAssess) {
        shape.oneRight();
        if (shape.isRight()) hasColumnsToAssess = false;
        if (!field.canBeAdded(shape)) continue;

        Point locationAfterRotation = shape.getLocation().clone();
        shape.drop();

        // Calculate score of field assuming we add the shape to it
        field.addShape(shape);
        if (shapeIndex == shapes.length - 1)
          score = evaluationFunction.evaluate(state);
        else
          score = (bestShapeStateAssessmentNextShape = findTargetShapeState(shapes, shapeIndex + 1)).score;
        field.removeShape(shape);

        // Is the score better?
        if (score > bestShapeStateAssessment.score)
          bestShapeStateAssessment = new ShapeStateAssessment(shape, score, bestShapeStateAssessmentNextShape);

        shape.setLocation(locationAfterRotation);
      }

      // Rotate shape
      // TODO: check if we can do this
      shape.turnRight();
    }

    shape.setLocation(originalLocation);
    return bestShapeStateAssessment;
  }

  public class ShapeStateAssessment {
    public final Point location;
    public final int rotation;
    public final double score;
    public final ShapeStateAssessment next;

    public ShapeStateAssessment(final Shape shape, final double score, ShapeStateAssessment next) {
      this(shape.getLocation().clone(), shape.getRotation(), score, next);
    }

    public ShapeStateAssessment(final Point location, final int rotation, final double score, ShapeStateAssessment next) {
      this.location = location;
      this.rotation = rotation;
      this.score = score;
      this.next = next;
    }

    public List<MoveType> getMoves(final Shape shape) {
      final List<MoveType> moves = new ArrayList<>();

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
      Point originalLocation = shape.getLocation().clone();
      int currentColumn = shape.getLocation().x;
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
      while (state.getMyField().canBeAdded(shape.oneDown()))
        moves.add(MoveType.DOWN);
      moves.remove(moves.size() - 1);
      moves.add(MoveType.DROP);
      shape.setLocation(originalLocation);

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
