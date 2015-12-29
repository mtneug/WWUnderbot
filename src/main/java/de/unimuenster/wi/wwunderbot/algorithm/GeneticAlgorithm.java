/*
 * Copyright (c) 2015. WWUnderbot team
 */

package de.unimuenster.wi.wwunderbot.algorithm;

import com.theaigames.blockbattle.bot.BotState;
import com.theaigames.blockbattle.models.MoveType;
import de.unimuenster.wi.wwunderbot.ga.BotStateEvaluationFunction;

import java.util.ArrayList;

/**
 * @author Alexander
 * @author Frederik
 * @author Marco
 * @author Matthias
 */
public class GeneticAlgorithm extends AbstractAlgorithm<ArrayList<MoveType>, ArrayList<MoveType>> {
  private BotState state;
  private BotStateEvaluationFunction evalutationFunction;

  public GeneticAlgorithm(BotState state, BotStateEvaluationFunction evalutationFunction) {
    this.state = state;
    this.evalutationFunction = evalutationFunction;
  }

  @Override
  public ArrayList<MoveType> generate(ArrayList<MoveType> initialMoves) {
    return initialMoves;
  }
}
