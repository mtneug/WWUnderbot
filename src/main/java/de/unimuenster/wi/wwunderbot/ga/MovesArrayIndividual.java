/*
 * Copyright (c) 2015. WWUnderbot team
 */

package de.unimuenster.wi.wwunderbot.ga;

import com.theaigames.blockbattle.models.Moves;

/**
 * @author Alexander
 * @author Frederik
 * @author Marco
 * @author Matthias
 */
public class MovesArrayIndividual extends AbstractIndividual<Moves[]> implements Cloneable {
  public MovesArrayIndividual(Moves[] moves) {
    super(moves);
  }

  public MovesArrayIndividual(Moves[] moves, double score) {
    super(moves, score);
  }

  @Override
  public MovesArrayIndividual clone() {
    MovesArrayIndividual cloned;
    try {
      cloned = (MovesArrayIndividual) super.clone();
    } catch (CloneNotSupportedException e) {
      // this shouldn't happen, since we are Cloneable
      throw new InternalError(e);
    }
    cloned.object = new Moves[object.length];
    for (int i = 0; i < object.length; i++)
      cloned.object[i] = object[i].clone();

    return cloned;
  }
}
