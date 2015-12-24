/*
 * Copyright (c) 2015. WWUnderbot team
 */

package de.uni_muenster.wi.wwunderbot.bot;

import com.theaigames.blockbattle.bot.AbstractBot;
import com.theaigames.blockbattle.bot.BotState;
import com.theaigames.blockbattle.models.Field;
import com.theaigames.blockbattle.models.MoveType;
import com.theaigames.blockbattle.models.Point;
import com.theaigames.blockbattle.models.Shape;
import de.uni_muenster.wi.wwunderbot.ga.Genome;
import de.uni_muenster.wi.wwunderbot.models.AssessableField;

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
  private Genome genome;
  private final Point topLeftLocation = new Point(0, 0);

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
  public ArrayList<MoveType> getMoves(BotState state, long timeout) {
    state.initShapes();
    Shape[] shapes = new Shape[]{
        state.getCurrentShape(),
        state.getNextShape()
    };
    return findBestMoves(state.getMyField(), shapes, 0).getMoves(state.getCurrentShape().getLocation());
  }

  private ShapeStateAssessment findBestMoves(AssessableField field, Shape[] shapes, int shapeIndex) {
    boolean hasColumnsToAssess = true;
    Shape shape = shapes[shapeIndex];
    ShapeStateAssessment bestShapeStateAssessment = new ShapeStateAssessment(shape, 0, 0);
    ShapeStateAssessment shapeStateAssessment;
    Point originalLocation = shape.getLocation().clone();

    // Try all possible rotations
    for (int rotation = 0; rotation < 4; rotation++) {
      // Set position to top left corner
      shape.moveToOrigin();

      // Try all possible columns
      while (hasColumnsToAssess) {
        if (shape.isRight(field)) hasColumnsToAssess = false;
        if (field.canBeAdded(shape)) continue;

        Point locationAfterRotation = shape.getLocation().clone();
        shape.drop(field);

        // Calculate score of field assuming we add the shape to it
        field.addShape(shape);
        if (shapeIndex == 0)
          shapeStateAssessment = findBestMoves(field, shapes, shapeIndex + 1);
        else
          shapeStateAssessment = new ShapeStateAssessment(shape, rotation, calculateScore(field));
        field.removeShape(shape);

        // Is the score better?
        if (shapeStateAssessment.score > bestShapeStateAssessment.score)
          bestShapeStateAssessment = shapeStateAssessment;

        shape.setLocation(locationAfterRotation);
        shape.oneRight();
      }

      // Rotate shape
      shape.rotateRight();
    }

    shape.setLocation(originalLocation);
    return bestShapeStateAssessment;
  }

  private double calculateScore(AssessableField field) {
    return genome.getHeightWeight() * field.getAggregateHeight()
        + genome.getHolesWeight() * field.getHoles()
        + genome.getCompletenessWeight() * field.getCompleteness()
        + genome.getBumpinessWeight() * field.getBumpiness();
  }

  private class ShapeStateAssessment {
    public Point location;
    public int rotation;
    public double score;

    public ShapeStateAssessment(Shape shape, int rotation, double score) {
      this(shape.getLocation(), rotation, score);
    }

    public ShapeStateAssessment(Point location, int rotation, double score) {
      this.location = location;
      this.rotation = rotation;
      this.score = score;
    }

    public ArrayList<MoveType> getMoves(Point targetLocation) {
      ArrayList<MoveType> moves = new ArrayList<>();

      // Move to target column
      int currentColumn = location.x;
      while (currentColumn != targetLocation.x) {
        if (currentColumn < targetLocation.x) {
          moves.add(MoveType.RIGHT);
          currentColumn++;
        } else {
          moves.add(MoveType.LEFT);
          currentColumn--;
        }
      }

      // Rotate
      for (int i = 0; i < rotation; i++)
        moves.add(MoveType.RIGHT);

      // Move to bottom
      moves.add(MoveType.DROP);

      return moves;
    }
  }
}
