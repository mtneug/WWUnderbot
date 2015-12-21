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

import wwunderbot.field.Field;
import wwunderbot.field.ShapeType;
import wwunderbot.player.Player;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * BotState class
 * <p>
 * In this class all the information about the game is stored.
 *
 * @author Jim van Eeden <jim@starapple.nl>
 */
public class BotState {
  private int round;
  private int timebank;
  private HashMap<String, Player> players;
  private Player myBot;
  private ShapeType currentShapeType;
  private ShapeType nextShapeType;
  private Point shapeLocation;
  private AssessField assessField;

  private int MAX_TIMEBANK;
  private int TIME_PER_MOVE;
  private int FIELD_WIDTH;
  private int FIELD_HEIGHT;

  public BotState() {
    this.round = 0;
    this.players = new HashMap<String, Player>();
  }

  /**
   * Initially set all necessary variables of our objects
   *
   * @param key   Contains one of the following predefined keywords returned from the game engine.
   * @param value Actual value of the key.
   */
  public void updateSettings(String key, String value) {
    switch (key) {
      // settings timebank t: Maximum time in milliseconds that your bot can have in its time bank
      case "timebank":
        this.MAX_TIMEBANK = Integer.parseInt(value);
        timebank = MAX_TIMEBANK;
        break;

      // settings time_per_move t: Time in milliseconds that is added to your bot's time bank each move
      case "time_per_move":
        this.TIME_PER_MOVE = Integer.parseInt(value);
        break;

      // settings player_names [b,...]: A list of all player names in this match, including your bot's name
      case "player_names":
        String[] playerNames = value.split(",");
        for (String playerName : playerNames)
          players.put(playerName, new Player(playerName));
        break;

      // settings your_bot b: The name of your bot for this match
      case "your_bot":
        this.myBot = players.get(value);
        break;

      // settings field_width i: The width of the field, i.e. number of row-cells
      case "field_width":
        this.FIELD_WIDTH = Integer.parseInt(value);
        break;

      // settings field_height i: The height of the field, i.e. number of column-cells
      case "field_height":
        this.FIELD_HEIGHT = Integer.parseInt(value);
        break;

      default:
        System.err.printf("Cannot parse settings with key \"%s\"\n", key);
        break;
    }
  }

  /**
   * Frequent updates of our bot about current points, skips, combos etc.
   *
   * @param player Name {b} of a bot (or "game").
   * @param key    Contains one of the following predefined keywords returned from the game engine.
   * @param value  Actual value of the key.
   */
  public void updateState(String player, String key, String value) {
    switch (key) {
      // update game round i: The number of the current round
      case "round":
        this.round = Integer.parseInt(value);
        break;

      // update game this_piece_type s: The type of the piece that has just spawned on the field
      case "this_piece_type":
        this.currentShapeType = ShapeType.valueOf(value);
        break;

      // update game next_piece_type s: The type of the piece that will spawn the next round
      case "next_piece_type":
        this.nextShapeType = ShapeType.valueOf(value);
        break;

      // update b row_points i: The amount of row points the given player has scored so far
      case "row_points":
        this.players.get(player).setPoints(Integer.parseInt(value));
        break;

      // update b combo i: The height of the current combo for the given player
      case "combo":
        this.players.get(player).setCombo(Integer.parseInt(value));
        break;

      // update b skips i: The amount of skips the given player has available
      case "skips":
        this.players.get(player).setSkips(Integer.parseInt(value));
        break;

      // update b field [[c,...];...]: The complete playing field of the given player
      case "field":
        this.players.get(player).setField(new Field(this.FIELD_WIDTH, this.FIELD_HEIGHT, value));
        this.assessField = new AssessField(this.players.get(player).getField());
        break;

      // update game this_piece_position i,i: The starting position in the field for
      //                                      the current piece (top left corner of the piece bounding box)
      case "this_piece_position":
        String[] split = value.split(",");
        this.shapeLocation = new Point(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
        break;

      default:
        System.err.printf("Cannot parse updates with key \"%s\"\n", key);
        break;
    }
  }

  public Player getOpponent() {
    for (Map.Entry<String, Player> entry : this.players.entrySet())
      if (entry.getKey() != this.myBot.getName())
        return entry.getValue();
    return null;
  }

  public Field getMyField() {
    return this.myBot.getField();
  }

  public Field getOpponentField() {
    return getOpponent().getField();
  }

  public ShapeType getCurrentShapeType() {
    return this.currentShapeType;
  }

  public ShapeType getNextShapeType() {
    return this.nextShapeType;
  }

  public Point getShapeLocation() {
    return this.shapeLocation;
  }

  public int getRound() {
    return this.round;
  }
}
