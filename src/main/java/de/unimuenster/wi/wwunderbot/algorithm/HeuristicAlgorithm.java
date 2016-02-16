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
 *
 *
 * @author Alexander
 * @author Frederik
 * @author Marco
 * @author Matthias
 */
public class HeuristicAlgorithm extends AbstractAlgorithm<Moves[]> {
  /**
   * Contains a {@code BotState} object holding information about the current game (especially of the currently playing
   * bot). This includes information about the current and next shape, a field factory, the methods to update the state
   * via the game engine commands etc..
   */
  private final BotState state;

  /**
   * Holds a {@code BotStateEvaluationFunction} object that enables the Heuristic to evaluate the current state of the
   * bot. This means that the contained object will help to evaluate the current field state by the attributes
   * that have been proposed by this algorithm (complete lines, aggregated height, bumpiness and holes).
   */
  private final BotStateEvaluationFunction evaluationFunction;

  /**
   * Constructor for the {@code HeuristicAlgorithm}.
   *
   * @param state containing a {@code BotState} object containing the game information of a given bot/player
   * @param evaluationFunction containing a {@code BotStateEvaluationFunction} to evaluate the given field for the
   *                           heuristic.
   */
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

    ShapeStateAssessment shapeStateAssessment = findTargetShapeState(shapes, 0);
    return reconstructMoves(shapes, shapeStateAssessment);
  }

  public Moves[] reconstructMoves(final Shape[] shapes, ShapeStateAssessment shapeStateAssessment) {
    final Moves[] moves = new Moves[shapes.length];

    for (int i = 0; i < shapes.length; i++) {
      moves[i] = new Moves(shapes[i], shapeStateAssessment.getMoves(shapes[i]));
      shapeStateAssessment = shapeStateAssessment.next;
    }
    return moves;
  }

  /**
   * Method that evaluates all potential placements of the current as well as the next Shape on the field.
   * Does so by trying to add each Shape in all potential rotations as well as in all potential x-axis locations (y-axis
   * placement determined by previously added shapes).
   * After both shapes have been added the field is evaluated to obtain a score based on the applied evaluation
   * heuristic. Based on this score two {@code ShapeStateAssessment}s (objects containing the state of the current shape
   * as well as the {@code ShapeStateAssessment} of the next shape) can be compared. This way the optimal placement of
   * the given shapes can be identified as in each round only the best possible {@code ShapeStateAssessment} is kept.
   * (Here it is noteworthy to say that all these steps are also done with only one shape during the recursive call
   * of this function to add the next shape.)
   *
   * @param shapes {@code Array} of {@code Shape}s that are going to be placed in the field
   * @param shapeIndex {@code int} being the index of the {@code Array} that denotes the {@code Shape} that is supposed
   *                              to be added to the field in the current iteration
   * @return  {@code ShapeStateAssessment} containing the best possible placement of the current {@code Shape}(s)
   */
  private ShapeStateAssessment findTargetShapeState(final Shape[] shapes, final int shapeIndex) {
    //  Obtain the current field.
    final Field field = state.getMyField();

    //  Obtain the shape that is to be added to the field.
    final Shape shape = shapes[shapeIndex];

    //  Clone the original location of the shape to return it to that position after target position finding.
    final Point originalLocation = shape.getLocation().clone();

    //  Initialize variable to save the score of the target state.
    double score;

    //  Boolean test variable that controls whether all columns of the field have been traversed.
    boolean hasColumnsToAssess;

    //  ShapeStateAssessment for the current shape (so the one that has to be placed in the current step).
    ShapeStateAssessment bestShapeStateAssessment = new ShapeStateAssessment(shape, Double.NEGATIVE_INFINITY, null);

    //  ShapeStateAssessment for the next shape (the one that will have to be placed in the next step).
    ShapeStateAssessment bestShapeStateAssessmentNextShape = null;

    //  Try all possible rotations
    for (int rotation = 0; rotation < 4; rotation++) {
      //  Set position to top left corner
      //  IMPROVEMENT: check if we even come here
      shape.moveToOrigin().oneLeft();

      //  Try all possible columns
      hasColumnsToAssess = true;
      while (hasColumnsToAssess) {
        shape.oneRight();

        //  When the right border of the field is reached, the while loop does not need to be continued.
        if (shape.isRight()) hasColumnsToAssess = false;

        //  IF the Shape cannot be added to the field at this stage, the while loop is aborted and another rotation is
        //  tested next.
        if (!field.canBeAdded(shape)) continue;

        //  Save the position after the rotation and the move to the right to continue their after the current
        //  execution of the loop.
        Point locationAfterRotation = shape.getLocation().clone();

        //  After deciding the rotation and x-axis position the shape is dropped (to "add it to the field").
        shape.drop();

        //  Add Shape to the field.
        field.addShape(shape);

        //  Calculate score of field assuming we add the shape to it. First case actually calculates the score after
        //  both shapes (the current and the next shape) have been added to the field.
        //  This is necessary to access the state that currently has to be assumed to be the best possible state after
        //  the next two iterations.
        //  If the second shape has not been added yet (second case), the findTargetShapeState is called again for this
        //  shape.
        if (shapeIndex == shapes.length - 1)
          score = evaluationFunction.evaluate(state);
        else
          score = (bestShapeStateAssessmentNextShape = findTargetShapeState(shapes, shapeIndex + 1)).score;

        //  Remove the shape from the field again to test all other potential placements (optimal placement not yet
        //  found so final adding not possible yet).
        field.removeShape(shape);

        // Is the score better?
        if (score > bestShapeStateAssessment.score)
          bestShapeStateAssessment = new ShapeStateAssessment(shape, score, bestShapeStateAssessmentNextShape);

        //  Reset the Shape to its location after rotation and previous move to the right.
        shape.setLocation(locationAfterRotation);
      }

      // Rotate shape
      // IMPROVEMENT: check if we can do this
      shape.turnRight();
    }

    //  Return the Shape to its original location to later on add it correctly after the best possible placement has
    //  been found.
    shape.setLocation(originalLocation);

    return bestShapeStateAssessment;
  }

  /**
   * Helper class used to save the "Add"-location and rotation of a shape (so basically place where it has been added
   * to the field for evaluation), as well as the associated score and the {@code ShapeStateAssessment} of the
   * following {@code Shape} ({@code null} if no further shape exists).
   */
  public class ShapeStateAssessment {
    /**
     * Final location of a given {@code Shape} placed for evaluation.
     */
    public final Point location;

    /**
     * Final rotation of a given {@code Shape} placed for evaluation.
     */
    public final int rotation;

    /**
     * Score of the {@code Shape} placement on the field. Later used to find the optimal solution.
     */
    public final double score;

    /**
     * {@code ShapeStateAssessment} of the next shape or {@code null} if no next shape exists. Helps to also have access
     * to the placement of the second shape (the so called "next shape").
     */
    public final ShapeStateAssessment next;

    /**
     * Constructor for a {@code ShapeStateAssessment} object.
     *
     * @param shape {@code Shape} whichs assessment is stored in this object.
     * @param score {@code double} being the score reached by the {@code Shape} in the add-location represented by this
     *                            object.
     * @param next  {@code ShapeStateAssessment} containing the assessment of the next shape or {@code null} if none is
     *                                          given (e.g. when the evaluated shape is the next shape).
     */
    public ShapeStateAssessment(final Shape shape, final double score, ShapeStateAssessment next) {
      this(shape.getLocation().clone(), shape.getRotation(), score, next);
    }

    /**
     * Constructor for a {@code ShapeStateAssessment} object.
     *
     * @param location  {@code Point} with final location of a given {@code Shape} placed for evaluation.
     * @param rotation  {@code int} with final rotation of a given {@code Shape} placed for evaluation.
     * @param score     {@code double} being the score reached by the {@code Shape} in the add-location represented by
     *                                this object.
     * @param next      {@code ShapeStateAssessment} containing the assessment of the next shape or {@code null} if none
     *                                              is given (e.g. when the evaluated shape is the next shape).
     */
    public ShapeStateAssessment(final Point location, final int rotation, final double score, ShapeStateAssessment next) {
      this.location = location;
      this.rotation = rotation;
      this.score = score;
      this.next = next;
    }

    /**
     * Method to obtain the steps that led to the placement of a {@code Shape} in its field. Necessary helper method as
     * the {@code ShapeStateAssessment} only saves position information but not the steps that were applied to reach it.
     * However from the location and rotation data stored in a given instance of the {@code ShapeStateAssessment} the
     * necessary moves can be recomputed.
     *
     * @param shape {@code Shape} for which the necessary placement moves shall be obtained.
     * @return  {@code List} of {@code MoveType}s necessary to place the given {@code Shape} at its designated target
     */
    public List<MoveType> getMoves(final Shape shape) {
      final List<MoveType> moves = new ArrayList<>();

      // Rotate
      final int originalRotation = shape.getRotation();
      switch (rotation) {
        case 2:
          shape.turnRight();
          moves.add(MoveType.TURNRIGHT);
          // fall-through intended
        case 1:
          shape.turnRight();
          moves.add(MoveType.TURNRIGHT);
          break;
        case 3:
          shape.turnLeft();
          moves.add(MoveType.TURNLEFT);
          break;
        default:
          throw new IllegalStateException("Rotation cannot be " + rotation);
        case 0:
      }

      // Move to target column
      final Point originalLocation = shape.getLocation().clone();
      int currentColumn = shape.getLocation().x;
      while (currentColumn != location.x) {
        if (currentColumn < location.x) {
          shape.oneRight();
          moves.add(MoveType.RIGHT);
          currentColumn++;
        } else {
          shape.oneLeft();
          moves.add(MoveType.LEFT);
          currentColumn--;
        }
      }

      // Move to bottom
      while (canPerformMove(shape.oneDown()))
        moves.add(MoveType.DOWN);
      if (moves.size() > 0 && moves.get(moves.size() - 1) == MoveType.DOWN)
        moves.remove(moves.size() - 1);
      moves.add(MoveType.DROP);

      shape.setRotation(originalRotation);
      shape.setLocation(originalLocation);

      return moves;
    }

    /**
     * Helper method that checks whether a shape can still be added to a given field. Can e.g. be used to check whether
     * a move that shall be performed will be legal.
     *
     * @param shape {@code Shape} that needs to be evaluated for being legally positioned in its field.
     * @return  {@code boolean} being {@code true} if {@code Shape} could be added and {@code false} if not.
     */
    private boolean canPerformMove(Shape shape) {
      return shape.getField().canBeAdded(shape) ||
          (shape.getLocation().y == -1
              && 0 <= shape.getShape()[shape.getEmptyCellsLeft()][0].getLocation().x
              && shape.getShape()[shape.getSize() - 1 - shape.getEmptyCellsRight()][0].getLocation().x < shape.getField().getWidth()
          );
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
