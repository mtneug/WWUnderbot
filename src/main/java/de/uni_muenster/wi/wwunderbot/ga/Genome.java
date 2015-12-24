/*
 * Copyright (c) 2015. WWUnderbot team
 */

package de.uni_muenster.wi.wwunderbot.ga;

/**
 * Representation of a Genome.
 *
 * @author Alexander, Frederik, Marco, Matthias
 */
public class Genome {
  private double heightWeight;
  private double completenessWeight;
  private double holesWeight;
  private double bumpinessWeight;

  public Genome(double heightWeight, double completenessWeight, double holesWeight, double bumpinessWeight) {
    this.heightWeight = heightWeight;
    this.completenessWeight = completenessWeight;
    this.holesWeight = holesWeight;
    this.bumpinessWeight = bumpinessWeight;
  }

  public double getBumpinessWeight() {
    return bumpinessWeight;
  }

  public void setBumpinessWeight(double bumpinessWeight) {
    this.bumpinessWeight = bumpinessWeight;
  }

  public double getHeightWeight() {
    return heightWeight;
  }

  public void setHeightWeight(double heightWeight) {
    this.heightWeight = heightWeight;
  }

  public double getCompletenessWeight() {
    return completenessWeight;
  }

  public void setCompletenessWeight(double completenessWeight) {
    this.completenessWeight = completenessWeight;
  }

  public double getHolesWeight() {
    return holesWeight;
  }

  public void setHolesWeight(double holesWeight) {
    this.holesWeight = holesWeight;
  }
}
