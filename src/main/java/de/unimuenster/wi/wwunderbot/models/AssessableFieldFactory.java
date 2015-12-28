/*
 * Copyright (c) 2015. WWUnderbot team
 */

package de.unimuenster.wi.wwunderbot.models;

import com.theaigames.blockbattle.models.AbstractFieldFactory;
import com.theaigames.blockbattle.models.Field;

/**
 * @author Alexander
 * @author Frederik
 * @author Marco
 * @author Matthias
 */
public class AssessableFieldFactory extends AbstractFieldFactory {
  private static AssessableFieldFactory instance = new AssessableFieldFactory();

  private AssessableFieldFactory() {
  }

  public static AssessableFieldFactory getInstance() {
    return instance;
  }

  @Override
  public Field newField(int width, int height) {
    return new AssessableField(width, height);
  }

  @Override
  public Field newField(int width, int height, String fieldString) {
    return newField(width, height, fieldString, true);
  }
}
