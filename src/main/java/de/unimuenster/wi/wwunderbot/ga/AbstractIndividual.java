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
public abstract class AbstractIndividual<T> implements Comparable<AbstractIndividual> {
  public T object;
  public double score;

  public AbstractIndividual(T object) {
    this.object = object;
  }

  public AbstractIndividual(T object, double score) {
    this.object = object;
    this.score = score;
  }

  @Override
  public int compareTo(AbstractIndividual individual) {
    return Double.compare(score, individual.score);
  }

  public T getObject() {
    return object;
  }

  public void setObject(T object) {
    this.object = object;
  }

  public double getScore() {
    return score;
  }

  public void setScore(double score) {
    this.score = score;
  }
}
