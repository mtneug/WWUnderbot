/*
 * Copyright (c) 2015. WWUnderbot team
 */

package com.theaigames.blockbattle.bot;

import com.theaigames.blockbattle.models.MoveType;
import de.uni_muenster.wi.wwunderbot.bot.WWUnderbot;

import java.util.ArrayList;
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
    this.scan = new Scanner(System.in);
    this.bot = bot;
    this.currentState = new BotState();
  }

  public void run() {
    while (scan.hasNextLine()) {
      final String line = scan.nextLine().trim();
      if (line.length() == 0) continue;

      final String[] parts = line.split(" ");
      switch (parts[0]) {
        case "settings":
          currentState.updateSettings(parts[1], parts[2]);
          break;

        case "update":
          currentState.updateState(parts[1], parts[2], parts[3]);
          break;

        case "action":
          final Long timeout = Long.valueOf(parts[2]);
          ArrayList<MoveType> moves = bot.getMoves(currentState, timeout);

          final StringJoiner output = new StringJoiner(",");
          if (moves.size() > 0)
            for (final MoveType move : moves)
              output.add(move.toString());
          else
            output.add("no_moves");

          System.out.println(output.toString());
          System.out.flush();
          System.err.println(output.toString());
          break;

        default:
          System.err.printf("Unable to parse line '%s'\n", line);
      }
    }
  }
}
