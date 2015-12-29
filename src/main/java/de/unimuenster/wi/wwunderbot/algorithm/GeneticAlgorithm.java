/*
 * Copyright (c) 2015. WWUnderbot team
 */

package de.unimuenster.wi.wwunderbot.algorithm;

import com.theaigames.blockbattle.bot.BotState;
import com.theaigames.blockbattle.models.MoveType;
import de.unimuenster.wi.wwunderbot.ga.BotStateEvaluationFunction;
import de.unimuenster.wi.wwunderbot.ga.MovesIndividual;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeoutException;

/**
 * @author Alexander
 * @author Frederik
 * @author Marco
 * @author Matthias
 */
public class GeneticAlgorithm extends AbstractAlgorithm<ArrayList<MoveType>, ArrayList<MoveType>> {
  public final static long BREAK_MS = 15;

  public final static int NUMBER_OF_ITERATIONS  = 100;
  public final static int NUMBER_OF_INDIVIDUALS = 30;
  public final static int NUMBER_OF_SELECTION   = 4;
  public final static double MUTATION_RATE      = 0.05;

  private BotState state;
  private BotStateEvaluationFunction evaluationFunction;

  private ArrayList<Double> weightings;
  private boolean changed = true;
  private double totalWeight = 0;

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
    ArrayList<MovesIndividual> parents     = new ArrayList<>();
    ArrayList<MovesIndividual> individuals = new ArrayList<>();
    for (int i = 0; i < NUMBER_OF_SELECTION; i++) {
      individuals.add(bestIndividual.clone());
      parents.add(individuals.get(i));
    }


    for (int i = 0; i < NUMBER_OF_INDIVIDUALS - NUMBER_OF_SELECTION; i++) {

      MovesIndividual parentOne = getWeightedRandomIndividual(parents);
      MovesIndividual parentTwo = getWeightedRandomIndividual(parents);


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

  /**
   * Based on:
   *  <url>http://stackoverflow.com/a/6737362</url>
   *
   * @param base
   * @return
   */
  private MovesIndividual getWeightedRandomIndividual(ArrayList<MovesIndividual> base) {
    if(changed) {
      totalWeight = 0;
      for(MovesIndividual mi : base) {
        totalWeight = totalWeight + mi.score;
      }
      /*for(MovesIndividual mi : base) {
        weightings.add(mi.score / totalWeight);
      }*/
      changed = false;
    }
    //totalWeight = 1;

    int randomIndex = -1;
    double random = Math.random() * totalWeight;

    for(int i = 0; i < base.size(); i++) {
      random -= base.get(i).score;
      if(random <= 0.0d) {
        randomIndex = i;
        break;
      }
    }

    return base.get(randomIndex);
  }

  private MovesIndividual mutate(MovesIndividual mi) {
    Random r   = new Random();
    int random = r.nextInt(100);

    if((random / 100.0d) < MUTATION_RATE ) {
      ArrayList<MoveType> moves = mi.object;
    }
  }

  private double simulateIndividual(MovesIndividual bestIndividual) {
    return 0;
  }

  private void evaluateIndividual(MovesIndividual bestIndividual) {
    evaluationFunction
  }


}
