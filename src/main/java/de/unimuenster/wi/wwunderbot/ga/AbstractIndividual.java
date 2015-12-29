/*
 * Copyright (c) 2015. WWUnderbot team
 */

package de.unimuenster.wi.wwunderbot.ga;

/**
 * @author Alexander
 * @author Frederik
 * @author Marco
 * @author Matthias
 */
public abstract class AbstractIndividual<T> {
  public T object;
  public double score;

  public AbstractIndividual(T object) {
    this.object = object;
  }

  public AbstractIndividual(T object, double score) {
    this.object = object;
    this.score = score;
  }
}
