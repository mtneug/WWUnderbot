/*
 * Copyright (c) 2015. WWUnderbot team
 */

package com.theaigames.blockbattle.models;

/**
 * @author Alexander
 * @author Frederik
 * @author Marco
 * @author Matthias
 */
public class FieldFactory extends AbstractFieldFactory {
  private final static AbstractFieldFactory instance = new FieldFactory();

  private FieldFactory() {
  }

  public static AbstractFieldFactory getInstance() {
    return instance;
  }

  @Override
  public Field newField(int width, int height) {
    return new Field(width, height);
  }
}
