/*
 * Copyright (c) 2015. WWUnderbot team
 */

package de.unimuenster.wi.wwunderbot;

import com.theaigames.blockbattle.models.*;
import de.unimuenster.wi.wwunderbot.algorithm.HeuristicAlgorithm;
import de.unimuenster.wi.wwunderbot.ga.Genome;
import de.unimuenster.wi.wwunderbot.ga.HeuristicEvaluationFunction;
import de.unimuenster.wi.wwunderbot.util.Visualize;

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
    String encodedField = "0,0,0,0,1,1,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,2,0,0,0,0,2,2,0,0;0,2,2,0,0,2,2,2,0,0";
    Field field = new FieldFactory(true).newField(10, 20, encodedField);

    Shape[] shapes = {
        new Shape(ShapeType.O, field, new Point(3, -1)),
        new Shape(ShapeType.J, field, new Point(3, -1))
    };

    //    +--------------------++--------------------++--------------------+
    //    |                    ||                    ||                    |
    //    |                    ||                    ||                    |
    //    |                    ||                    ||                    |
    //    |                    ||                    ||                    |
    //    |                    ||                    ||                    |
    //    |                    ||                    ||                    |
    //    |                    ||                    ||                    |
    //    |                    ||                    ||                    |
    //    |                    ||                    ||                    |
    //    |                    ||                    ||                    |
    //    |                    ||                    ||                    |
    //    |                    ||                    ||                    |
    //    |                    ||                    ||                    |
    //    |                    ||                    ||                    |
    //    |                    ||                    ||                    |
    //    |                    ||  ░░░░              ||                    |
    //    |                    ||  ░░                ||                    |
    //    |                    ||  ░░                ||                    |
    //    |  ▐▌        ▐▌▐▌    ||  ▐▌        ▐▌▐▌░░░░||  ▐▌        ▐▌▐▌    |
    //    |  ▐▌▐▌    ▐▌▐▌▐▌    ||  ▐▌▐▌    ▐▌▐▌▐▌░░░░||  ▐▌▐▌    ▐▌▐▌▐▌    |
    //    +--------------------++--------------------++--------------------+

    HeuristicEvaluationFunction evaluationFunction = new HeuristicEvaluationFunction(new Genome(-0.510066, 0.760666, -0.35663, -0.184483));
    HeuristicAlgorithm algo = new HeuristicAlgorithm(null, evaluationFunction);

    Moves[] movesArray = algo.reconstructMoves(shapes, algo.findTargetShapeState(field, shapes, 0));

    Field field2 = field.clone();
    for (Moves moves : movesArray) {
      moves.checkAndApply();
      field2.addShape(moves.shape);
    }

    Visualize.fields(new Field[]{
        field,
        field2
    });
  }
}
