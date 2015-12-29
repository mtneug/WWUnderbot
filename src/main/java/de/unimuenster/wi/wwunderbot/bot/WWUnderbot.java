/*
 * Copyright (c) 2015. WWUnderbot team
 */

package de.unimuenster.wi.wwunderbot.bot;

import com.theaigames.blockbattle.bot.AbstractBot;
import com.theaigames.blockbattle.bot.BotState;
import com.theaigames.blockbattle.models.MoveType;
import de.unimuenster.wi.wwunderbot.algorithm.GeneticAlgorithm;
import de.unimuenster.wi.wwunderbot.algorithm.HeuristicAlgorithm;
import de.unimuenster.wi.wwunderbot.ga.Genome;
import de.unimuenster.wi.wwunderbot.ga.HeuristicEvaluationFunction;

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
   * @return a list of moves to execute
   */
  @Override
  public ArrayList<MoveType> getMoves(final BotState state) {
    state.initShapes();

    // Create evaluation function
    HeuristicEvaluationFunction evaluationFunction = new HeuristicEvaluationFunction(genome);

    // Generate initial solution
    HeuristicAlgorithm heuristic = new HeuristicAlgorithm(state, evaluationFunction);
    ArrayList<MoveType> heuristicSolution = heuristic.generate();
    return heuristicSolution;

    // Genetically modify solution
    //GeneticAlgorithm ga = new GeneticAlgorithm(heuristicSolution, state, evaluationFunction);
    //return ga.generate();
  }
}
