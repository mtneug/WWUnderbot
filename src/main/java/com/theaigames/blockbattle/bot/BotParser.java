/*
 * Copyright (c) 2015. WWUnderbot team
 */

package com.theaigames.blockbattle.bot;

import com.theaigames.blockbattle.models.FieldFactory;
import com.theaigames.blockbattle.models.MoveType;
import com.theaigames.blockbattle.models.Moves;

import java.util.Scanner;
import java.util.StringJoiner;

/**
 * This class will keep reading output from the engine. It will either update
 * the bot state or get actions.
 *
 * @author Jim van Eeden <jim@starapple.nl>
 * @author Alexander
 * @author Frederik
 * @author Marco
 * @author Matthias
 */
public class BotParser {
  private final Scanner scan;
  private final AbstractBot bot;
  private BotState currentState;

  public BotParser(final AbstractBot bot) {
    this(bot, new FieldFactory());
  }

  public BotParser(final AbstractBot bot, FieldFactory fieldFactory) {
    this.scan = new Scanner(System.in);
    this.bot = bot;
    this.currentState = new BotState(fieldFactory);
  }

  public void run() {
    while (scan.hasNextLine()) {
      final String line = scan.nextLine().trim();
      if (line.length() == 0) continue;
      handleLine(line);
    }
  }

  public void handleLine(String line) {
    final String[] parts = line.split(" ");
    switch (parts[0]) {
      case "settings":
        currentState.updateSettings(parts[1], parts[2]);
        break;

      case "update":
        currentState.updateState(parts[1], parts[2], parts[3]);
        break;

      case "action":
        currentState.setTimebank(Long.valueOf(parts[2]));
        currentState.startTimer();
        Moves moves = bot.getMoves(currentState);

        final StringJoiner output = new StringJoiner(",");
        if (moves.size() > 0)
          for (final MoveType move : moves)
            output.add(move.toString());
        else
          output.add("no_moves");

        System.out.println(output.toString());
        System.out.flush();

        System.err.println("   Bot: " + output.toString());
        break;

      default:
        System.err.printf("Unable to parse line '%s'\n", line);
    }
  }
}
