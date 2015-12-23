// Copyright 2015 theaigames.com (developers@theaigames.com)

//    Licensed under the Apache License, Version 2.0 (the "License");
//    you may not use this file except in compliance with the License.
//    You may obtain a copy of the License at

//        http://www.apache.org/licenses/LICENSE-2.0

//    Unless required by applicable law or agreed to in writing, software
//    distributed under the License is distributed on an "AS IS" BASIS,
//    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//    See the License for the specific language governing permissions and
//    limitations under the License.
//
//    For the full copyright and license information, please view the LICENSE
//    file that was distributed with this source code.

package wwunderbot.bot;

import wwunderbot.models.moves.MoveType;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringJoiner;

/**
 * BotParser class
 * <p>
 * WWUnderbot class that will keep reading output from the engine. Will either update
 * the bot state or get actions.
 *
 * @author Jim van Eeden <jim@starapple.nl>
 */
public class BotParser {
  private final Scanner scan;
  private final WWUnderbot bot;
  private BotState currentState;

  public BotParser(final WWUnderbot bot) {
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
