/*
 * Copyright (c) 2015. WWUnderbot team
 */

package de.unimuenster.wi.wwunderbot.bot;

import com.theaigames.blockbattle.bot.AbstractBot;
import com.theaigames.blockbattle.bot.BotState;
import com.theaigames.blockbattle.models.Field;
import com.theaigames.blockbattle.models.Moves;
import de.unimuenster.wi.wwunderbot.algorithm.GeneticAlgorithm;
import de.unimuenster.wi.wwunderbot.algorithm.HeuristicAlgorithm;
import de.unimuenster.wi.wwunderbot.ga.Genome;
import de.unimuenster.wi.wwunderbot.ga.HeuristicEvaluationFunction;
import de.unimuenster.wi.wwunderbot.util.Visualize;

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
   * @param state current state of the bot
   * @return a list of moves to execute
   */
  @Override
  public Moves getMoves(final BotState state) {
    state.initShapes();

    // TODO: check for skip moves

    // Create evaluation function
    final HeuristicEvaluationFunction evaluationFunction = new HeuristicEvaluationFunction(genome);

    // Generate initial solution
    final HeuristicAlgorithm heuristic = new HeuristicAlgorithm(state, evaluationFunction);
    final Moves[] heuristicSolution = heuristic.run();
    
    return heuristicSolution[0];

    // Genetically modify solution
    // final GeneticAlgorithm ga = new GeneticAlgorithm(heuristicSolution, state,
    // evaluationFunction);
    // final Moves[] geneticSolution = ga.run();
    //
    // if (heuristicSolution != geneticSolution)
    // compare(state, heuristicSolution, geneticSolution, evaluationFunction);
    //
    // // TODO: Maybe we can save the next moves and use them as a basis for the heuristic as well?
    // return geneticSolution[0];
  }

  private void compare(BotState state, Moves[] heuristicSolution, Moves[] geneticSolution,
      final HeuristicEvaluationFunction evaluationFunction) {
    System.err.println("Current Shape: " + state.getCurrentShapeType());
    System.err.println("Next Shape:    " + state.getNextShapeType());
    System.err.println();
    System.err.println("Heuristic: " + evaluateMoves(state, heuristicSolution, evaluationFunction));
    System.err.println("Genetic:   " + evaluateMoves(state, geneticSolution, evaluationFunction));

    Visualize.fields(new Field[] {state.getMyField(), performMoves(state, heuristicSolution),
        performMoves(state, geneticSolution)});
  }

  private Field performMoves(final BotState state, Moves[] movesArray) {
    final Field field = state.getMyField();
    for (Moves moves : movesArray) {
      moves.checkAndApply();
      field.addShape(moves.shape);
    }
    final Field f = field.clone();
    for (Moves moves : movesArray) {
      field.removeShape(moves.shape);
      moves.reset();
    }
    return f;
  }

  private double evaluateMoves(final BotState state, Moves[] movesArray,
      final HeuristicEvaluationFunction evaluationFunction) {
    final Field field = state.getMyField();
    double score;
    for (Moves moves : movesArray) {
      moves.checkAndApply();
      field.addShape(moves.shape);
    }
    score = evaluationFunction.evaluateField(field);
    for (Moves moves : movesArray) {
      field.removeShape(moves.shape);
      moves.reset();
    }
    return score;
  }
}
