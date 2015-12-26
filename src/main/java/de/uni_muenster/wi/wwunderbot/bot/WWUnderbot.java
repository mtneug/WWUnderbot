/*
 * Copyright (c) 2015. WWUnderbot team
 */

package de.uni_muenster.wi.wwunderbot.bot;

import com.theaigames.blockbattle.bot.AbstractBot;
import com.theaigames.blockbattle.bot.BotState;
import com.theaigames.blockbattle.models.MoveType;
import com.theaigames.blockbattle.models.Point;
import com.theaigames.blockbattle.models.Shape;
import de.uni_muenster.wi.wwunderbot.ga.Genome;
import de.uni_muenster.wi.wwunderbot.models.AssessableField;
import de.uni_muenster.wi.wwunderbot.util.CSVwriter;

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
  private final CSVwriter csvwriter;
  private int roundnr;

  public WWUnderbot(Genome genome) {
    this.genome = genome;
    this.csvwriter = CSVwriter.getCSVwriterInstance();
    this.roundnr = 1;
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
    final Shape[] shapes = new Shape[]{
        state.getCurrentShape(),
        state.getNextShape()
    };
    csvwriter.writeStatus("Round " + roundnr++);
    ShapeStateAssessment ssA = findTargetShapeState(state.getMyField(), shapes, 0);
    System.err.println("SHAPE STATE ASSESSMENT: " + ssA);
    return ssA.getMoves(state.getCurrentShape().getLocation());
  }

  private ShapeStateAssessment findTargetShapeState(final AssessableField field, final Shape[] shapes, final int shapeIndex) {
    final Shape shape = shapes[shapeIndex];
    final Point originalLocation = shape.getLocation().clone();

    boolean hasColumnsToAssess = true;
    ShapeStateAssessment bestShapeStateAssessment, shapeStateAssessment;
    bestShapeStateAssessment = new ShapeStateAssessment(shape, Integer.MIN_VALUE);

    // Try all possible rotations
    for (int rotation = 0; rotation < 4; rotation++) {
      // Set position to top left corner
      shape.moveToOrigin();

      // Try all possible columns
      while (hasColumnsToAssess) {
        if (shape.isRight(field)) hasColumnsToAssess = false;
        if (!field.canBeAdded(shape)) continue;

        Point locationAfterRotation = shape.getLocation().clone();
        shape.drop(field);

        // Calculate score of field assuming we add the shape to it
        field.addShape(shape);
        if (shapeIndex == shapes.length - 1) {
          shapeStateAssessment = new ShapeStateAssessment(shape, calculateScore(field));
          // <WICHTIG> csvwriter.writeStatusResult("non-recursive", shapeStateAssessment);
          csvwriter.writeStatus("non-recursive");
        }
        else {
          shapeStateAssessment = findTargetShapeState(field, shapes, shapeIndex + 1);
          // <WICHTIG> csvwriter.writeStatusResult("recursive", shapeStateAssessment);
          csvwriter.writeStatus("recursive");
        }
        field.removeShape(shape);

        // Is the score better?
        if (shapeStateAssessment.score > bestShapeStateAssessment.score) {
          this.csvwriter.writeStatusResult("Shape: ", shapeStateAssessment);
          this.csvwriter.writeStatusResult("Best-Shape: ", bestShapeStateAssessment);
          bestShapeStateAssessment = new ShapeStateAssessment(new Point((int) shapeStateAssessment.location.getX(), (int) shapeStateAssessment.location.getY()),
            shapeStateAssessment.rotation, shapeStateAssessment.score);

          this.csvwriter.writeStatusResult("New Best-Shape: ", bestShapeStateAssessment);
        }

        //System.err.println("SHAPE STATE ASSESSMENT AFTER MOVE: " + shapeStateAssessment);
        shape.setLocation(locationAfterRotation);
        shape.oneRight();
        //csvwriter.writeResult(shapeStateAssessment);
      }


      hasColumnsToAssess = true;

      // Rotate shape
      shape.rotateRight();
    }

    // <WICHTIG> csvwriter.writeBestResult(bestShapeStateAssessment);
    shape.setLocation(originalLocation);
    return bestShapeStateAssessment;
  }

  private double calculateScore(final AssessableField field) {
    System.out.println("ASCORE: " + genome.getHeightWeight() * field.getAggregateHeight()
      + genome.getCompletenessWeight() * field.getCompleteness()
      + genome.getHolesWeight() * field.getHoles()
      + genome.getBumpinessWeight() * field.getBumpiness());
    System.exit(-666);
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

    public ArrayList<MoveType> getMoves(final Point targetLocation) {
      final ArrayList<MoveType> moves = new ArrayList<>();

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
        moves.add(MoveType.TURNRIGHT);

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
