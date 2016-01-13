/*
 * Copyright (c) 2015. WWUnderbot team
 */

package de.unimuenster.wi.wwunderbot.bot;

import com.theaigames.blockbattle.bot.AbstractBot;
import com.theaigames.blockbattle.bot.BotState;
import com.theaigames.blockbattle.models.Moves;
import de.unimuenster.wi.wwunderbot.algorithm.GeneticAlgorithm;
import de.unimuenster.wi.wwunderbot.algorithm.HeuristicAlgorithm;
import de.unimuenster.wi.wwunderbot.ga.Genome;
import de.unimuenster.wi.wwunderbot.ga.HeuristicEvaluationFunction;

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



    // Genetically modify solution
    final GeneticAlgorithm ga = new GeneticAlgorithm(heuristicSolution, state, evaluationFunction);
    final Moves[] solution = ga.run();

    // TODO: Maybe we can save the next moves and use them as a basis for the heuristic as well?
    return solution[0];
  }
}
