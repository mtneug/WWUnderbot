/*
 * Copyright (c) 2015. WWUnderbot team
 */

package wwunderbot.geneticAlgorithm;

/**
 * Created by Marco on 20.12.2015.
 */
public class Genome {

  private double heightWeight;
  private double linesWeight;
  private double holesWeight;
  private double bumpinessWeight;

  public Genome(double heightWeight, double linesWeight, double holesWeight, double bumpinessWeight) {
    this.heightWeight = heightWeight;
    this.linesWeight = linesWeight;
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

  public double getLinesWeight() {
    return linesWeight;
  }

  public void setLinesWeight(double linesWeight) {
    this.linesWeight = linesWeight;
  }

  public double getHolesWeight() {
    return holesWeight;
  }

  public void setHolesWeight(double holesWeight) {
    this.holesWeight = holesWeight;
  }
}
