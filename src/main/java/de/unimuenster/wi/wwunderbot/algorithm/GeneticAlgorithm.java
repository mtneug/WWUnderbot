/*
 * Copyright (c) 2015. WWUnderbot team
 */

package de.unimuenster.wi.wwunderbot.algorithm;

import com.theaigames.blockbattle.bot.BotState;
import com.theaigames.blockbattle.models.Field;
import com.theaigames.blockbattle.models.MoveType;
import com.theaigames.blockbattle.models.Moves;
import com.theaigames.blockbattle.models.Shape;
import de.unimuenster.wi.wwunderbot.ga.BotStateEvaluationFunction;
import de.unimuenster.wi.wwunderbot.ga.MovesArrayIndividual;
import de.unimuenster.wi.wwunderbot.util.Visualize;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeoutException;

/**
 * @author Alexander
 * @author Frederik
 * @author Marco
 * @author Matthias
 */
public class GeneticAlgorithm extends AbstractAlgorithm<Moves[]> {
  public final static MoveType[] ALLOWED_MUTATIONS = {
      MoveType.DOWN,
      MoveType.LEFT,
      MoveType.RIGHT,
      MoveType.TURNLEFT,
      MoveType.TURNRIGHT
  };
  public final static long BREAK_MS = 15;
  public final static int NUMBER_OF_ITERATIONS = 10000;
  public final static int NUMBER_OF_INDIVIDUALS = 30;
  public final static int NUMBER_OF_SELECTION = 7;
  public final static double MUTATION_RATE = 0.5;
  private final Random random = new Random();
  private final List<MovesArrayIndividual> parents = new ArrayList<>();
  private final PriorityQueue<MovesArrayIndividual> population = new PriorityQueue<>(Comparator.reverseOrder());
  private final MovesArrayIndividual initialIndividual;
  private final BotState state;
  private final BotStateEvaluationFunction evaluationFunction;
  private final Shape[] shapes;
  private boolean parentsChanged = true;
  private double totalWeight = 0;
  private int iterations = 0;

  public GeneticAlgorithm(final Moves[] initialMoves, final BotState state, final BotStateEvaluationFunction evaluationFunction) {
    this.state = state;
    this.evaluationFunction = evaluationFunction;
    this.initialIndividual = new MovesArrayIndividual(initialMoves);
    this.shapes = new Shape[]{
        this.state.getCurrentShape(),
        this.state.getNextShape()
    };
  }

  @Override
  public Moves[] run() {
    try {
      // Initialize population
      initPopulation();

      // For each iteration
      for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
        // Generate offspring and evaluate it
        iterations++;
        populateAndEvaluate();
        select();
      }
    } catch (TimeoutException ignored) {
    }

    System.err.println(iterations);

    return (population.size() == 0)
        ? initialIndividual.object
        : population.peek().object;
  }

  protected void initPopulation() throws TimeoutException {
    // Add initial individual
    evaluate(initialIndividual);
    population.add(initialIndividual);
    parents.add(initialIndividual);

    // Mutate initial individual and add to list
    while (parents.size() < NUMBER_OF_SELECTION) {
      MovesArrayIndividual individual = initialIndividual.clone();

      mutate(individual);
      evaluate(individual);

      population.add(individual);
      parents.add(individual);
    }
  }

  protected void populateAndEvaluate() throws TimeoutException {
    while (population.size() < NUMBER_OF_INDIVIDUALS) {
      // Create new offspring
      MovesArrayIndividual parentOne = selectParent();
      MovesArrayIndividual parentTwo = selectParent();

      MovesArrayIndividual offspring = crossover(parentOne, parentTwo);
      mutate(offspring);

      // Evaluate
      evaluate(offspring);

      // Add to sorted list
      population.add(offspring);
    }
  }

  /**
   * @return
   * @see <a href="http://stackoverflow.com/a/6737362">Stack Overflow answer<a/>
   */
  protected MovesArrayIndividual selectParent() {
    if (parentsChanged) updateTotalWeight();
    double r = random.nextDouble() * totalWeight;
    for (MovesArrayIndividual parent : parents) {
      r -= parent.score;
      if (r <= 0.0) return parent;
    }
    return parents.get(0);
  }

  private void updateTotalWeight() {
    totalWeight = 0;
    for (MovesArrayIndividual individual : parents)
      totalWeight = totalWeight + individual.score;
    parentsChanged = false;
  }

  protected MovesArrayIndividual crossover(final MovesArrayIndividual parentOne, final MovesArrayIndividual parentTwo) {
    final int size = shapes.length;
    final Moves[] offspringMovesArray = new Moves[size];

    for (int i = 0; i < size; i++) {
      final Moves movesParentOne = parentOne.object[i];
      final Moves movesParentTwo = parentTwo.object[i];
      final int minSize = Math.min(movesParentOne.size(), movesParentTwo.size());

      if (minSize <= 2)
        if (parentOne.score < parentTwo.score) {
          offspringMovesArray[i] = movesParentTwo.clone();
          continue;
        } else {
          offspringMovesArray[i] = movesParentOne.clone();
          continue;
        }

      final Moves offspringMoves = new Moves(shapes[i]);

      final int limit1 = random.nextInt(minSize - 1);
      final int limit2 = limit1 + 1 + random.nextInt(minSize - limit1 - 1);

      if (random.nextBoolean()) {
        offspringMoves.addAll(movesParentOne.subList(0, limit1));
        offspringMoves.addAll(movesParentTwo.subList(limit1, limit2));
        offspringMoves.addAll(movesParentOne.subList(limit2, movesParentOne.size()));
      } else {
        offspringMoves.addAll(movesParentTwo.subList(0, limit1));
        offspringMoves.addAll(movesParentOne.subList(limit1, limit2));
        offspringMoves.addAll(movesParentTwo.subList(limit2, movesParentTwo.size()));
      }

      offspringMovesArray[i] = offspringMoves;
    }

    return new MovesArrayIndividual(offspringMovesArray);
  }

  protected void mutate(final MovesArrayIndividual individual) {
    mutateChangeMoves(individual);
    mutateAddMoves(individual);
    mutateRemoveMoves(individual);
  }

  private void mutateChangeMoves(final MovesArrayIndividual individual) {
    for (Moves moves : individual.object)
      // mutate every move except the last one (DROP)
      for (int i = 0; i < moves.size() - 1; i++)
        if (random.nextDouble() < MUTATION_RATE) {
          MoveType move = ALLOWED_MUTATIONS[random.nextInt(ALLOWED_MUTATIONS.length)];
          moves.set(i, move);
        }
  }

  private void mutateAddMoves(final MovesArrayIndividual individual) {

  }

  private void mutateRemoveMoves(final MovesArrayIndividual individual) {
    // not to small

  }

  protected void select() {
    // Select the four best population
    parents.clear();
    int j = 0;
    for (MovesArrayIndividual individual : population) {
      if (j++ >= NUMBER_OF_SELECTION) break;
      parents.add(individual);
    }
    parentsChanged = true;

    // Remove bad population
    population.clear();
    population.addAll(parents);
  }

  protected void evaluate(final MovesArrayIndividual individual) throws TimeoutException {
    if (state.getTimer().remainingTimeMillis() < BREAK_MS)
      throw new TimeoutException("Timeout");

    final Field field = state.getMyField();

    for (Moves moves : individual.object) {
      moves.checkAndApply();
      field.addShape(moves.shape);
    }

    individual.score = evaluationFunction.evaluate(state);

    for (Moves moves : individual.object) {
      field.removeShape(moves.shape);
      moves.reset();
    }
  }
}
