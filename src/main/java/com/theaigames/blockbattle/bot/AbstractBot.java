/*
 * Copyright (c) 2015. WWUnderbot team
 */

package com.theaigames.blockbattle.bot;

import com.theaigames.blockbattle.models.MoveType;

import java.util.ArrayList;

/**
 * An abstract bot.
 *
 * @author Alexander
 * @author Frederik
 * @author Marco
 * @author Matthias
 */
public abstract class AbstractBot {
  public abstract ArrayList<MoveType> getMoves(final BotState state, final long timeout);
}
