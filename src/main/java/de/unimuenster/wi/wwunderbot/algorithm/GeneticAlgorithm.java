/*
 * Copyright (c) 2015. WWUnderbot team
 */

package de.unimuenster.wi.wwunderbot.algorithm;

import com.theaigames.blockbattle.bot.BotState;
import com.theaigames.blockbattle.models.Field;
import com.theaigames.blockbattle.models.MoveType;
import com.theaigames.blockbattle.models.Point;
import com.theaigames.blockbattle.models.Shape;
import de.unimuenster.wi.wwunderbot.ga.BotStateEvaluationFunction;
import de.unimuenster.wi.wwunderbot.ga.MovesIndividual;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.concurrent.TimeoutException;

/**
 * @author Alexander
 * @author Frederik
 * @author Marco
 * @author Matthias
 */
public class GeneticAlgorithm extends AbstractAlgorithm<ArrayList<MoveType>> {
  public final static MoveType[] ALLOWED_MUTATIONS = {
      MoveType.DOWN,
      MoveType.LEFT,
      MoveType.RIGHT,
      MoveType.TURNLEFT,
      MoveType.TURNRIGHT
  };
  public final static long BREAK_MS = 15;
  public final static int NUMBER_OF_ITERATIONS = 1000;
  public final static int NUMBER_OF_INDIVIDUALS = 30;
  public final static int NUMBER_OF_SELECTION = 4;
  public final static double MUTATION_RATE = 0.05;
  private final Random random = new Random();
  private final ArrayList<MovesIndividual> parents = new ArrayList<>();
  private final PriorityQueue<MovesIndividual> population = new PriorityQueue<>(Comparator.reverseOrder());
  private final MovesIndividual initialIndividual;
  private final BotState state;
  private final BotStateEvaluationFunction evaluationFunction;
  private boolean parentsChanged = true;
  private double totalWeight = 0;

  public GeneticAlgorithm(final ArrayList<MoveType> initialMoves, final BotState state, final BotStateEvaluationFunction evaluationFunction) {
    this.state = state;
    this.evaluationFunction = evaluationFunction;
    this.initialIndividual = new MovesIndividual(initialMoves);
  }

  @Override
  public ArrayList<MoveType> run() {
    try {
      // Initialize population
      initPopulation();

      // For each iteration
      for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
        // Generate offspring and evaluate it
        populateAndEvaluate();
        select();
      }
    } catch (TimeoutException ignored) {
    }

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
      MovesIndividual individual = initialIndividual.clone();

      mutate(individual);
      evaluate(individual);

      population.add(individual);
      parents.add(individual);
    }
  }

  protected void populateAndEvaluate() throws TimeoutException {
    while (population.size() < NUMBER_OF_INDIVIDUALS) {
      // Create new offspring
      MovesIndividual parentOne = selectParent();
      MovesIndividual parentTwo = selectParent();

      MovesIndividual offspring = crossover(parentOne, parentTwo);
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
  protected MovesIndividual selectParent() {
    if (parentsChanged) updateTotalWeight();
    double r = random.nextDouble() * totalWeight;
    for (MovesIndividual parent : parents) {
      r -= parent.score;
      if (r <= 0.0) return parent;
    }
    return parents.get(0);
  }

  private void updateTotalWeight() {
    totalWeight = 0;
    for (MovesIndividual individual : parents)
      totalWeight = totalWeight + individual.score;
    parentsChanged = false;
  }

  protected MovesIndividual crossover(MovesIndividual parentOne, MovesIndividual parentTwo) {
    final int minSize = Math.min(parentOne.object.size(), parentTwo.object.size());

    if (minSize <= 2)
      if (parentOne.score < parentTwo.score)
        return parentTwo.clone();
      else
        return parentOne.clone();

    final ArrayList<MoveType> offspring = new ArrayList<>();

    final int limit1 = random.nextInt(minSize - 1);
    final int limit2 = limit1 + 1 + random.nextInt(minSize - limit1 - 1);

    if (random.nextBoolean()) {
      offspring.addAll(parentOne.object.subList(0, limit1));
      offspring.addAll(parentTwo.object.subList(limit1, limit2));
      offspring.addAll(parentOne.object.subList(limit2, parentOne.object.size()));
    } else {
      offspring.addAll(parentTwo.object.subList(0, limit1));
      offspring.addAll(parentOne.object.subList(limit1, limit2));
      offspring.addAll(parentTwo.object.subList(limit2, parentTwo.object.size()));
    }

    return new MovesIndividual(offspring);
  }

  protected void mutate(MovesIndividual individual) {
    mutateChangeMoves(individual);
    mutateAddMoves(individual);
    mutateRemoveMoves(individual);
  }

  private void mutateChangeMoves(MovesIndividual individual) {
    // mutate every move except the last one (DROP)
    for (int i = 0; i < individual.object.size() - 1; i++)
      if (random.nextDouble() < MUTATION_RATE) {
        MoveType move = ALLOWED_MUTATIONS[random.nextInt(ALLOWED_MUTATIONS.length)];
        individual.object.set(i, move);
      }
  }

  private void mutateAddMoves(MovesIndividual individual) {

  }

  private void mutateRemoveMoves(MovesIndividual individual) {
    // not to small

  }

  protected void select() {
    // Select the four best population
    parents.clear();
    int j = 0;
    for (MovesIndividual individual : population) {
      if (j++ >= NUMBER_OF_SELECTION) break;
      parents.add(individual);
    }
    parentsChanged = true;

    // Remove bad population
    population.clear();
    population.addAll(parents);
  }

  protected void evaluate(MovesIndividual individual) throws TimeoutException {
    if (state.getTimer().remainingTimeMillis() < BREAK_MS)
      throw new TimeoutException("Timeout");

    final Field field = state.getMyField();
    final Shape shape = state.getCurrentShape();
    final Point originalLocation = shape.getLocation().clone();
    final int originalRotation = shape.getRotation();

    for (MoveType move : individual.object)
      checkAndPerformMove(field, shape, move);

    field.addShape(shape);
    individual.score = evaluationFunction.evaluate(state);

    field.removeShape(shape);
    shape.setLocation(originalLocation);
    shape.setRotation(originalRotation);
  }

  private void checkAndPerformMove(Field field, Shape shape, MoveType move) {
    switch (move) {
      case DOWN:
        if (!field.canBeAdded(shape.oneDown()))
          shape.oneUp();
        break;

      case DROP:
        shape.drop();
        break;

      case LEFT:
        if (!field.canBeAdded(shape.oneLeft()))
          shape.oneRight();
        break;

      case RIGHT:
        if (!field.canBeAdded(shape.oneRight()))
          shape.oneLeft();
        break;

      case TURNLEFT:
        if (!field.canBeAdded(shape.turnLeft()))
          shape.turnRight();
        break;

      case TURNRIGHT:
        if (!field.canBeAdded(shape.turnRight()))
          shape.turnLeft();
        break;
    }

    shape.drop();
  }
}
