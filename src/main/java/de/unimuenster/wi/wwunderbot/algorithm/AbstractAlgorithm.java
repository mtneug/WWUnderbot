/*
 * Copyright (c) 2015. WWUnderbot team
 */

package de.unimuenster.wi.wwunderbot.algorithm;

/**
 * @author Alexander
 * @author Frederik
 * @author Marco
 * @author Matthias
 */
public abstract class AbstractAlgorithm<I, O> {
  public abstract O generate(I input);
}
