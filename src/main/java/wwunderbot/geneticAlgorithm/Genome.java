/*
 * Copyright (c) 2015. WWUnderbot team
 */

package wwunderbot.geneticAlgorithm;

/**
 * Created by Marco on 20.12.2015.
 */
public class Genome {

  private double heightWeight;
  private double completeness;
  private double holesWeight;
  private double bumpinessWeight;

  public Genome(double heightWeight, double completeness, double holesWeight, double bumpinessWeight) {
    this.heightWeight = heightWeight;
    this.completeness = completeness;
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

  public double getCompleteness() {
    return completeness;
  }

  public void setCompleteness(double completeness) {
    this.completeness = completeness;
  }

  public double getHolesWeight() {
    return holesWeight;
  }

  public void setHolesWeight(double holesWeight) {
    this.holesWeight = holesWeight;
  }
}
