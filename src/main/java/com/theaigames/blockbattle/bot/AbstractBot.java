/*
 * Copyright (c) 2015. WWUnderbot team
 */

package com.theaigames.blockbattle.bot;

import com.theaigames.blockbattle.models.Moves;

/**
 * An abstract bot.
 *
 * @author Alexander
 * @author Frederik
 * @author Marco
 * @author Matthias
 */
public abstract class AbstractBot {
  public abstract Moves getMoves(final BotState state);
}
