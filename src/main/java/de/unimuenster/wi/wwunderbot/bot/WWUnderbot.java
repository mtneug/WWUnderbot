/*
 * Copyright (c) 2015. WWUnderbot team
 */

package de.unimuenster.wi.wwunderbot.bot;

import com.theaigames.blockbattle.bot.AbstractBot;
import com.theaigames.blockbattle.bot.BotState;
import com.theaigames.blockbattle.models.MoveType;
import com.theaigames.blockbattle.models.Point;
import com.theaigames.blockbattle.models.Shape;
import de.unimuenster.wi.wwunderbot.ga.Genome;
import de.unimuenster.wi.wwunderbot.models.AssessableField;

import java.util.ArrayList;

/**
 * Implementation of the WWUnderbot bot.
 *
 * @author Alexander
 * @author Frederik
 * @author Marco
 * @author Matthias
 */
public class WWUnderbot extends AbstractBot {
  private final Genome genome;

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
  @Override
  public ArrayList<MoveType> getMoves(final BotState state, final long timeout) {
    state.initShapes();
    final Shape[] shapes = {
        state.getCurrentShape(),
        state.getNextShape()
    };
    return findTargetShapeState((AssessableField) state.getMyField(), shapes, 0).
        getMoves(state.getCurrentShape().getLocation());
  }

  public ShapeStateAssessment findTargetShapeState(final AssessableField field, final Shape[] shapes) {
    return findTargetShapeState(field, shapes, 0);
  }

  private ShapeStateAssessment findTargetShapeState(final AssessableField field, final Shape[] shapes, final int shapeIndex) {
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
        if (shape.isRight(field)) hasColumnsToAssess = false;
        if (!field.canBeAdded(shape)) continue;

        Point locationAfterRotation = shape.getLocation().clone();
        shape.drop(field);

        // Calculate score of field assuming we add the shape to it
        field.addShape(shape);
        if (shapeIndex == shapes.length - 1)
          score = calculateScore(field);
        else
          score = findTargetShapeState(field, shapes, shapeIndex + 1).score;
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

  public double calculateScore(final AssessableField field) {
    return genome.getHeightWeight() * field.getAggregateHeight()
        + genome.getCompletenessWeight() * field.getCompleteness()
        + genome.getHolesWeight() * field.getHoles()
        + genome.getBumpinessWeight() * field.getBumpiness();
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
      //for (int i = 0; i < rotation; i++)
      //  moves.add(MoveType.TURNRIGHT);
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
