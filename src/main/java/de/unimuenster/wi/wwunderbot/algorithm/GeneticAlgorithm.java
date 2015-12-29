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
  public final static int NUMBER_OF_ITERATIONS = 100;
  public final static int NUMBER_OF_INDIVIDUALS = 30;
  public final static int NUMBER_OF_SELECTION = 4;
  public final static double MUTATION_RATE = 0.05;
  private final Random random = new Random();
  private final ArrayList<MovesIndividual> parents = new ArrayList<>();
  private final PriorityQueue<MovesIndividual> population = new PriorityQueue<>(Comparator.reverseOrder());
  private final MovesIndividual initialIndividual;
  private final BotState state;
  private final BotStateEvaluationFunction evaluationFunction;
  private boolean changed = true;
  private double totalWeight = 0;

  public GeneticAlgorithm(final ArrayList<MoveType> initialMoves, final BotState state, final BotStateEvaluationFunction evaluationFunction) {
    this.state = state;
    this.evaluationFunction = evaluationFunction;
    this.initialIndividual = new MovesIndividual(initialMoves);
  }

  @Override
  public ArrayList<MoveType> generate() {
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
      MovesIndividual parentOne = selectParent().clone();
      MovesIndividual parentTwo = selectParent().clone();

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
    if (changed) updateTotalWeight();
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
    changed = false;
  }

  protected MovesIndividual crossover(MovesIndividual parentOne, MovesIndividual parentTwo) {
    final ArrayList<MoveType> offspring = new ArrayList<>();
    final int parentOneLimit1 = random.nextInt(parentOne.object.size() - 1);
    final int parentOneLimit2 = parentOneLimit1 + 1 + random.nextInt(parentOne.object.size() - parentOneLimit1 - 1);
    final int parentTwoLimit1 = random.nextInt(parentTwo.object.size() - 1);
    final int parentTwoLimit2 = parentTwoLimit1 + 1 + random.nextInt(parentTwo.object.size() - parentTwoLimit1 - 1);

    if (random.nextBoolean()) {
      offspring.addAll(parentOne.object.subList(0, parentOneLimit1));
      offspring.addAll(parentTwo.object.subList(parentTwoLimit1, parentTwoLimit2));
      offspring.addAll(parentOne.object.subList(parentOneLimit2, parentOne.object.size()));
    } else {
      offspring.addAll(parentTwo.object.subList(0, parentTwoLimit1));
      offspring.addAll(parentOne.object.subList(parentOneLimit1, parentOneLimit2));
      offspring.addAll(parentTwo.object.subList(parentTwoLimit2, parentTwo.object.size()));
    }

    return new MovesIndividual(offspring);
  }

  protected MovesIndividual mutate(MovesIndividual individual) {
    mutateChangeMoves(individual);
    mutateAddMoves(individual);
    mutateRemoveMoves(individual);

    return null;
  }

  private void mutateChangeMoves(MovesIndividual individual) {
    for (int i = 0; i < individual.object.size(); i++) {
      if (random.nextDouble() < MUTATION_RATE) {
        MoveType move = ALLOWED_MUTATIONS[random.nextInt(ALLOWED_MUTATIONS.length)];
        individual.object.set(i, move);
      }
    }
  }

  private void mutateAddMoves(MovesIndividual individual) {

  }

  private void mutateRemoveMoves(MovesIndividual individual) {

  }

  protected void select() {
    // Select the four best population
    parents.clear();
    int j = 0;
    for (MovesIndividual individual : population) {
      if (j++ >= NUMBER_OF_SELECTION) break;
      parents.add(individual);
    }
    changed = true;

    // Remove bad population
    population.clear();
    population.addAll(parents);
  }

  protected void evaluate(MovesIndividual individual) throws TimeoutException {
    if (state.getTimer().remainingTimeMillis() < BREAK_MS)
      throw new TimeoutException("Timeout");

    final Field field = state.getMyField();
    final Shape shape = state.getCurrentShape();
    final Point originalLocation = shape.getLocation();
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
  }
}
