/*
 * Copyright (c) 2015. WWUnderbot team
 */

//    Licensed under the Apache License, Version 2.0 (the "License");
//    you may not use this file except in compliance with the License.
//    You may obtain a copy of the License at

//        http://www.apache.org/licenses/LICENSE-2.0

package wwunderbot.bot;

import wwunderbot.geneticAlgorithm.Genome;
import wwunderbot.moves.MoveType;

import java.util.*;

/**
 * WWUnderbot class
 * <p>
 * This class is where the main logic should be. Implement getMoves() to return
 * something better than random moves.
 *
 * @author Jim van Eeden <jim@starapple.nl>
 */
public class WWUnderbot {

  private BotState state;
  private Genome genome;

  public WWUnderbot(Genome genome) {
    this.genome = genome;
  }

  /**
   * Returns a random amount of random moves
   *
   * @param state   current state of the bot
   * @param timeout time to respond
   * @return a list of moves to execute
   */
  public ArrayList<MoveType> getMoves(BotState state, long timeout) {
    ArrayList<MoveType> moves = new ArrayList<MoveType>();
    Random rnd = new Random();

    int nrOfMoves = rnd.nextInt(41);
    List<MoveType> allMoves = Collections.unmodifiableList(Arrays.asList(MoveType.values()));
    for (int n = 0; n <= nrOfMoves; n++) {
      moves.add(allMoves.get(rnd.nextInt(allMoves.size())));
    }

    return moves;
  }

  public void findBestMove() {

  }

  public BotState getState() {
    return state;
  }

  public void setState(BotState state) {
    this.state = state;
  }
}
