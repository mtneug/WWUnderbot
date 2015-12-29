/*
 * Copyright (c) 2015. WWUnderbot team
 */

package de.unimuenster.wi.wwunderbot.algorithm;

import com.theaigames.blockbattle.bot.BotState;
import com.theaigames.blockbattle.models.MoveType;
import de.unimuenster.wi.wwunderbot.ga.BotStateEvaluationFunction;
import de.unimuenster.wi.wwunderbot.ga.MovesIndividual;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

/**
 * @author Alexander
 * @author Frederik
 * @author Marco
 * @author Matthias
 */
public class GeneticAlgorithm extends AbstractAlgorithm<ArrayList<MoveType>, ArrayList<MoveType>> {
  public final static long BREAK_MS = 15;

  public final static int NUMBER_OF_ITERATIONS = 100;
  public final static int NUMBER_OF_INDIVIDUALS = 30;
  public final static int NUMBER_OF_SELECTION = 4;

  private BotState state;
  private BotStateEvaluationFunction evaluationFunction;

  public GeneticAlgorithm(BotState state, BotStateEvaluationFunction evaluationFunction) {
    this.state = state;
    this.evaluationFunction = evaluationFunction;
  }

  @Override
  public ArrayList<MoveType> generate(ArrayList<MoveType> initialMoves) {
    return microbial(initialMoves);
  }

  private ArrayList<MoveType> microbial(ArrayList<MoveType> initialMoves) {
    // Set initial moves as best individual
    MovesIndividual bestIndividual = new MovesIndividual(initialMoves);
    bestIndividual.score = simulateIndividual(bestIndividual);

    // Initialize individuals
    ArrayList<MovesIndividual> individuals = new ArrayList<>();
    for (int i = 0; i < NUMBER_OF_SELECTION; i++)
      individuals.add(bestIndividual.clone());


    for (int i = 0; i < NUMBER_OF_INDIVIDUALS - NUMBER_OF_SELECTION; i++) {

    }


    // 1. motation
    // 2. crossover
    // 3. evaluation
    // 4. select 4
    //



    loop:
    for (int i = 0; i < iterations; i++) {

      try {
      } catch (TimeoutException e) {
        break loop;
      }
    }


    return null;
  }

  private double simulateIndividual(MovesIndividual bestIndividual) {
    return 0;
  }

  private void evaluateIndividual(MovesIndividual bestIndividual) {
    evaluationFunction
  }


}
