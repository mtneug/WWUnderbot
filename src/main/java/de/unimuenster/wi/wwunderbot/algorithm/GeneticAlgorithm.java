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

import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * @author Alexander
 * @author Frederik
 * @author Marco
 * @author Matthias
 */
public class GeneticAlgorithm extends AbstractAlgorithm<Moves[]> {

  /**
   * List of all allowed moves that can be used as a mutation. Basically it is the list of all moves available, however
   * "skip" and "drop" have been eliminated.
   * "skip" is only available after certain combo moves and thus would not always be applicable; "drop" is undesirable
   * since it would just finish the round (and place the piece).
   */
  public final static MoveType[] ALLOWED_MUTATIONS = {
      MoveType.DOWN,
      MoveType.LEFT,
      MoveType.RIGHT,
      MoveType.TURNLEFT,
      MoveType.TURNRIGHT
  };

  /**
   * Remaining time in milliseconds at which the genetic algorithm will stop working to return a result in time.
   */
  public final static long BREAK_MS = 15;

  /**
   * Maximum allowed number of iterations. Experiments have shown that this number usually is not reached which leaves
   * sufficient potential for special cases where more computations are possible.
   */
  public final static int MAX_NUMBER_OF_ITERATIONS = 100000;

  /**
   * Number of individuals per generation that will be generated.
   */
  public final static int NUMBER_OF_INDIVIDUALS = 30;

  /**
   * Number of parents that will be transferred from one generation to another.
   */
  public final static int NUMBER_OF_SELECTION = 7;

  /**
   * Maximum allowed mutation rate (mutation rate itself is dynamic). Ensures that reasonable results are created.
   */
  public final static double MAX_MUTATION_RATE = 0.4;

  /**
   * Maximum allowed add-mutation rate (likelihood that an additional move is added).
   */
  public final static double MAX_MUTATION_ADD_RATE = 0.4;

  /**
   * Maximum allowed deletion-mutation rate (likelihood that a move is deleted).
   */
  public final static double MAX_MUTATION_REMOVE_RATE = 0.1;

  /**
   * Percentage of the length of the initial solution (the one identified by the heuristic) that a genome should have
   * before {@code mutateRemoveMoves} is executed.
   * This ensures that chromosomes are not shortened far under their needed length.
   * If for example the {@code initialIndividual} would have 10 moves, at least 8 moves would be allowed for the
   * mentioned function to still apply its functionality (deleting a part of the chromosome).
   */
  public final static double MUTATION_MIN_LENGTH_PERCENTAGE = 0.8;

  /**
   * Percentage of the length of the initial solution (the one identified by the heuristic) that a genome should have
   * before {@code mutateAddMoves} is executed.
   * This ensures that chromosomes are not extended far over their needed length.
   * If for example the {@code initialIndividual} would have 10 moves, at most 20 moves would be allowed for the
   * mentioned function to still apply its functionality (adding an additional part to the chromosome).
   */
  public final static double MUTATION_MAX_LENGTH_PERCENTAGE = 2;

  /**
   * Random number generator used to randomize the selection as well as the mutation of individuals respectively
   * genomes.
   */
  private final Random random = new Random();

  /**
   * {@code List} of {@code MovesArrayIndividual}s that later on represent the parent population (each
   * {@code MovesArrayIndividual} that represents one potential [parent] solution).
   */
  private final List<MovesArrayIndividual> parents = new ArrayList<>();

  /**
   * {@code PriorityQueue} that is used to automatically sort the population of {@code MovesArrayIndividual}s based
   * on the score they have reached. It is initialized in reverse order, to have the individual with the highest score
   * on the first place of the queue.
   */
  private final PriorityQueue<MovesArrayIndividual> population = new PriorityQueue<>(Comparator.reverseOrder());

  /**
   * The initial individual that is the solution obtained in the heuristic that preceeds the genetic algorithm.
   */
  private final MovesArrayIndividual initialIndividual;

  /**
   * Starting index for each shape in the {@code initialIndividual}. The starting index is the index with the first
   * "down" move. This reduces the number of unuseful solutions, as significant changes can only be reached once the
   * shape is moved to the ground. This holds true as all rotations and placements on the x-axis have already been
   * tested by the heuristic. The genetic algorithm should only try to fit the shape into gaps, the heuristic could
   * not find.
   */
  private final int[] mutationStartIndex;

  /**
   * {@code BotState} object that stores all relevant information about the game.
   */
  private final BotState state;

  /**
   * Holds a {@code BotStateEvaluationFunction} object that enables the genetic algorithm to evaluate the current state
   * of the bot. This means that the contained object will help to evaluate the current field state by the attributes
   * that have been proposed by this algorithm (complete lines, aggregated height, bumpiness and holes).
   */
  private final BotStateEvaluationFunction evaluationFunction;

  /**
   * {@code Array} holding the {@code Shape}s representing the current and the next shape in the game.
   */
  private final Shape[] shapes;

  /**
   * {@code boolean} control parameter to detect changes to the {@code List} of parent individuals. If changes occurred
   * the total weighting used for randomly selecting a parent for reproduction has to be updated.
   */
  private boolean parentsChanged = true;

  /**
   * {@code double} Initial weighting used to use weighted random selection to find parents for reproduction.
   */
  private double totalWeight = 0;

  /**
   * {@code int} representing the number of iterations, the genetic algorithm already ran.
   */
  private int iterations = 0;

  /**
   * Constructor for the {@code GeneticAlgorithm}.
   *
   * @param initialMoves  {@code Array} of {@code Moves} being the solution obtained by the {@code HeuristicAlgorithm}.
   * @param state {@code BotState} object that stores all relevant information about the game.
   * @param evaluationFunction  {@code BotStateEvaluationFunction} object that enabling the evaluation of the current
   *                                                              state of the bot.
   */
  public GeneticAlgorithm(final Moves[] initialMoves, final BotState state, final BotStateEvaluationFunction evaluationFunction) {
    this.state = state;
    this.evaluationFunction = evaluationFunction;

    //  Setup the initial individual based on the moves obtained from the heuristic.
    this.initialIndividual = new MovesArrayIndividual(initialMoves);

    //  Setup the array of mutation start indices. For each of the (normally two) shapes represented by the initialMoves
    //  array, the first "down" command is searched and marked as the mutation start index.
    mutationStartIndex = new int[initialMoves.length];
    loop:
    for (int m = 0; m < initialMoves.length; m++) {
      for (int i = 0; i < initialMoves[m].size(); i++)
        if (initialMoves[m].get(i) == MoveType.DOWN) {
          mutationStartIndex[m] = i;
          continue loop;
        }
    }

    //  Setup the shapes array by obtaining the current and the next shape from the BotState object.
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
      for (int i = 0; i < MAX_NUMBER_OF_ITERATIONS; i++) {
        // Generate offspring and evaluate it
        iterations++;
        populateAndEvaluate();
        select();
      }
    } catch (TimeoutException ignored) {
    }

    System.err.println(iterations);

    //  Return the initial individual whenever no population exists (timeout); in all other cases return the first
    //  element of the population (which will have the highest score).
    return (population.size() == 0)
        ? initialIndividual.object
        : population.peek().object;
  }

  /**
   * Method to initially populate the {@code parents} {@code List} which then forms the basis for filling up the
   * {@code population}. The function adds the initial individual to both, the parents as well as the population
   * DTOs, and then adds a number of mutated initial individuals (as many as to fill up the {@code parents}
   * {@code List}.
   *
   * @throws TimeoutException
   */
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

  /**
   * Method that fills up the {@code population} {@code PriorityQueue}. As long as it does not contain a sufficient
   * number of individuals two parent individuals are selected and used to create an additional offspring.
   *
   * @throws TimeoutException
   */
  protected void populateAndEvaluate() throws TimeoutException {
    while (population.size() < NUMBER_OF_INDIVIDUALS) {
      // Create new offspring
      MovesArrayIndividual parentOne = selectParent();
      MovesArrayIndividual parentTwo = selectParent();

      //MovesArrayIndividual offspring = crossover(parentOne, parentTwo);
      MovesArrayIndividual offspring;
      if (random.nextBoolean())
        offspring = parentOne.clone();
      else
        offspring = parentTwo.clone();
      //offspring = crossover(parentOne, parentTwo);
      mutate(offspring);

      // Evaluate
      evaluate(offspring);

      // Add to sorted list
      population.add(offspring);
    }
  }

  /**
   * Method to have a parent selection based on weighted randomness. This means that parents having a higher score
   * are more likely to get drawn than parents with a smaller score. However a certain degree of randomness is
   * maintained.
   *
   * @see <a href="http://stackoverflow.com/a/6737362">Stack Overflow answer<a/>
   *
   * @return  {@code MovesArrayIndividual} being the randomly selected parent
   */
  protected MovesArrayIndividual selectParent() {
    //  If the parents list changed during the last iteration, the weights have to be updated.
    if (parentsChanged) updateTotalWeight();

    //  Weighed-randomly draw a parent object.
    double r = random.nextDouble() * totalWeight;
    for (MovesArrayIndividual parent : parents) {
      r -= parent.score;
      if (r <= 0.0) return parent;
    }
    return parents.get(0);
  }

  /**
   * Helper-method that updates the total weight of all parent objects used for weighted-random selection. Basically
   * sums up all scores of all parent individuals.
   */
  private void updateTotalWeight() {
    totalWeight = 0;
    for (MovesArrayIndividual individual : parents)
      totalWeight = totalWeight + individual.score;
    parentsChanged = false;
  }

  /**
   * Method to apply a crossover to two {@code MovesArrayIndividuals}. Splits both individuals into three parts and
   * recombines two elements of each with one element of the other ([1-2-1] and [2-1-2]).
   * Which of the two combinations will be used is decided by chance.
   *
   * @param parentOne {@code MovesArrayIndividual} representing the first parent used for crossover
   * @param parentTwo {@code MovesArrayIndividual} representing the second parent used for crossover
   * @return  {@code MovesArrayIndividual} representing the newly combined individual.
   */
  protected MovesArrayIndividual crossover(final MovesArrayIndividual parentOne, final MovesArrayIndividual parentTwo) {
    //  Determine the number of shapes.
    final int size = shapes.length;

    //  Create a new Moves-array for the offspring that will be created.
    final Moves[] offspringMovesArray = new Moves[size];

    //  Create a new move-sequence for each shape which are then merged into the new MovesArrayIndividual.
    for (int i = 0; i < size; i++) {
      //  Obtain the Moves sequences of the two parent individuals.
      final Moves movesParentOne = parentOne.object[i];
      final Moves movesParentTwo = parentTwo.object[i];

      //  Determine the minimum size of the two sequences.
      final int minSize = Math.min(movesParentOne.size(), movesParentTwo.size());

      //  If size is smaller or equal than two - indicating the nearby end of the game - the crossover step is 
      //  omitted since for two elements a split into three would not work.
      //  In this case the higher ranked individual gets returned.
      if (minSize <= 2)
        if (parentOne.score < parentTwo.score) {
          offspringMovesArray[i] = movesParentTwo.clone();
          continue;
        } else {
          offspringMovesArray[i] = movesParentOne.clone();
          continue;
        }

      //  Initialize a new Moves object for a given shape that will be used for crossover.
      final Moves offspringMoves = new Moves(shapes[i]);

      //  Based on the length of the two Moves sequences and a random variable determine the positions for the 
      //  crossover split.
      final int limit1 = random.nextInt(minSize - 1);
      final int limit2 = limit1 + 1 + random.nextInt(minSize - limit1 - 1);

      //  Depending on chance create the new offspring via crossover of either two parts of parent one with one
      //  element from parent two or vice versa.
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

  /**
   * Method that invokes all three mutation operations on a given individual.
   * 
   * @param individual {@code MovesArrayIndividual} that is going to be mutated.
   */
  protected void mutate(final MovesArrayIndividual individual) {
    mutateChangeMoves(individual);
    mutateAddMoves(individual);
    mutateRemoveMoves(individual);
  }

  /**
   * Method that randomly changes moves in the given move sequence of an {@code MovesArrayIndividual}.
   * This is applied for both shapes in the given individual.
   * Whether a given move will be changed or not is random with a chance not greater than 40%.
   * 
   * @param individual {@code MovesArrayIndividual} that is going to be mutated.
   */
  private void mutateChangeMoves(final MovesArrayIndividual individual) {
    //  Iterate over all shapes in MovesArrayIndividual.
    for (int m = 0; m < individual.object.length; m++) {
      Moves moves = individual.object[m];
      // mutate every move except the last one (DROP)
      for (int i = mutationStartIndex[m]; i < moves.size() - 1; i++)
        if (random.nextDouble() < mutationRate(i, moves.size() - 1, MAX_MUTATION_RATE)) {
          MoveType move = ALLOWED_MUTATIONS[random.nextInt(ALLOWED_MUTATIONS.length)];
          moves.set(i, move);
        }
    }
  }

  /**
   * Method that adds random moves whenever the given move sequence of an individual is not already too long.
   * Every sequence being already two times the size of the initial individual is considered too long.
   * Whether a given move will be added or not is random with a chance not greater than 40%.
   * 
   * @param individual {@code MovesArrayIndividual} that is going to be mutated.
   */
  private void mutateAddMoves(final MovesArrayIndividual individual) {
    for (int m = 0; m < individual.object.length; m++) {
      Moves moves = individual.object[m];
      // mutate every move except the last one (DROP)
      for (int i = mutationStartIndex[m]; i < moves.size() - 1; i++) {
        // not to large
        if (moves.size() > MUTATION_MAX_LENGTH_PERCENTAGE * initialIndividual.object[m].size())
          return;
        if (random.nextDouble() < mutationRate(i, moves.size() - 1, MAX_MUTATION_ADD_RATE)) {
          MoveType move = ALLOWED_MUTATIONS[random.nextInt(ALLOWED_MUTATIONS.length)];
          moves.add(i, move);
        }
      }
    }
  }

  /**
   * Method that removes random moves whenever the given move sequence of an individual is not already too short.
   * Every sequence being only 0.8 times the size of the initial individual is considered too short.
   * Whether a given move will be removed or not is random with a chance not greater than 10%.
   * 
   * @param individual {@code MovesArrayIndividual} that is going to be mutated.
   */
  private void mutateRemoveMoves(final MovesArrayIndividual individual) {
    for (int m = 0; m < individual.object.length; m++) {
      Moves moves = individual.object[m];
      // mutate every move except the last one (DROP)
      for (int i = mutationStartIndex[m]; i < moves.size() - 1; i++) {
        // not to small
        if (moves.size() < MUTATION_MIN_LENGTH_PERCENTAGE * initialIndividual.object[m].size())
          return;
        if (random.nextDouble() < mutationRate(i, moves.size() - 1, MAX_MUTATION_REMOVE_RATE))
          moves.remove(i);
      }
    }
  }

  /**
   * Method to dynamically adjust the mutation rate based on the position in the moves sequence, the size of the 
   * very same as well as the maximum allowed mutation rate.
   * Towards the end of a move sequence the likelihood for a mutation rises. This makes sense in two ways:
   *  1.  it is advisable to change the later moves rather than the earlier ones, since only there improvements can be
   *      achieved (by finding holes the heuristic omitted)
   *  2.  by having a variable mutation rate that increases, it is more likeli that a mutation takes place
   * 
   * @param i {@code int} being the position in the array of moves
   * @param length {@code int} being the length of the moves array (total number of all moves)
   * @param max {@code double} being the maximum allowed mutation rate.
   * @return [description]
   */
  protected double mutationRate(int i, int length, double max) {
    return max * Math.pow((double) i / length, 4);
  }

  /**
   * Method that selects the best individuals in the population of the current iteration. The number of best individuals
   * to be selected is specified in {@code NUMBER_OF_SELECTION}.
   */
  protected void select() {
    // Select the best individuals of the population
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

  /**
   * Method that evaluates the solutions generated by the genetic algorithm. First ensures that they represent valid
   * solutions at all (no placement outside the field or in the middle of the air).
   * Afterwards the score is calculated to make the generated solution comparable to the others.
   * 
   * @param individual {@code MovesArrayIndividual} to be evaluated
   * @throws TimeoutException whenever the time is insufficient to complete the evaluation
   */
  protected void evaluate(final MovesArrayIndividual individual) throws TimeoutException {
    //  Check if sufficient time remains to evaluate the individual.
    if (state.getTimer().remainingTimeMillis() < BREAK_MS)
      throw new TimeoutException("Timeout");

    //  Obtain the field needed for evaluation.
    final Field field = state.getMyField();

    //  Check whether given move sequence is valid and add the shape to the field for evaluation.
    for (Moves moves : individual.object) {
      moves.checkAndApply();
      field.addShape(moves.shape);
    }

    //  Calculate the score of the solution with the given evaluation function.
    individual.score = evaluationFunction.evaluate(state);

    //  Remove the shapes again to allow further evaluation in the future.
    for (Moves moves : individual.object) {
      field.removeShape(moves.shape);
      moves.reset();
    }
  }
}
