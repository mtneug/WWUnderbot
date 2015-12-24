/*
 * Copyright (c) 2015. WWUnderbot team
 */

package com.theaigames.blockbattle.models;

import de.uni_muenster.wi.wwunderbot.models.AssessableField;

/**
 * Represents one of the players. Stores some data about them.
 *
 * @author Jim van Eeden <jim@starapple.nl>
 * @author Alexander
 * @author Frederik
 * @author Marco
 * @author Matthias
 */
public class Player {
  private AssessableField field;
  private String name;
  private int points;
  private int combo;
  private int skips;

  public Player(String name) {
    this.name = name;
  }

  public AssessableField getField() {
    return this.field;
  }

  public void setField(AssessableField field) {
    this.field = field;
  }

  public String getName() {
    return this.name;
  }

  public int getPoints() {
    return this.points;
  }

  public void setPoints(int points) {
    this.points = points;
  }

  public int getCombo() {
    return this.combo;
  }

  public void setCombo(int combo) {
    this.combo = combo;
  }

  public int getSkips() {
    return this.skips;
  }

  public void setSkips(int skips) {
    this.skips = skips;
  }
}
