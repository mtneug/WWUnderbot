/*
 * Copyright (c) 2015. WWUnderbot team
 */

package de.unimuenster.wi.wwunderbot.ga;

import com.theaigames.blockbattle.models.MoveType;

import java.util.ArrayList;

/**
 * @author Alexander
 * @author Frederik
 * @author Marco
 * @author Matthias
 */
public class MovesIndividual extends AbstractIndividual<ArrayList<MoveType>> implements Cloneable {
  public MovesIndividual(ArrayList<MoveType> moves) {
    super(moves);
  }

  public MovesIndividual(ArrayList<MoveType> moves, double score) {
    super(moves, score);
  }

  @Override
  @SuppressWarnings("unchecked")
  public MovesIndividual clone() {
    MovesIndividual cloned;
    try {
      cloned = (MovesIndividual) super.clone();
    } catch (CloneNotSupportedException e) {
      // this shouldn't happen, since we are Cloneable
      throw new InternalError(e);
    }
    cloned.object = (ArrayList<MoveType>) object.clone();
    return cloned;
  }
}
