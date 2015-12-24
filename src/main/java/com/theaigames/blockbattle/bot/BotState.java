/*
 * Copyright (c) 2015. WWUnderbot team
 */

package com.theaigames.blockbattle.bot;

import com.theaigames.blockbattle.models.Player;
import com.theaigames.blockbattle.models.Point;
import com.theaigames.blockbattle.models.Shape;
import com.theaigames.blockbattle.models.ShapeType;
import de.uni_muenster.wi.wwunderbot.models.AssessableField;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to store all information about the game.
 *
 * @author Jim van Eeden <jim@starapple.nl>
 * @author Alexander
 * @author Frederik
 * @author Marco
 * @author Matthias
 */
public class BotState {
  private HashMap<String, Player> players = new HashMap<>();
  private int round = 0;
  private int timebank;
  private Player myBot;
  private ShapeType currentShapeType;
  private ShapeType nextShapeType;
  private Point shapeLocation;

  private int MAX_TIMEBANK;
  private int TIME_PER_MOVE;
  private int FIELD_WIDTH;
  private int FIELD_HEIGHT;
  private Shape currentShape;
  private Shape nextShape;

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
        MAX_TIMEBANK = Integer.parseInt(value);
        timebank = MAX_TIMEBANK;
        break;

      // settings time_per_move t: Time in milliseconds that is added to your bot's time bank each move
      case "time_per_move":
        TIME_PER_MOVE = Integer.parseInt(value);
        break;

      // settings player_names [b,...]: A list of all player names in this match, including your bot's name
      case "player_names":
        for (final String playerName : value.split(","))
          players.put(playerName, new Player(playerName));
        break;

      // settings your_bot b: The name of your bot for this match
      case "your_bot":
        myBot = players.get(value);
        break;

      // settings field_width i: The width of the field, i.e. number of row-cells
      case "field_width":
        FIELD_WIDTH = Integer.parseInt(value);
        break;

      // settings field_height i: The height of the field, i.e. number of column-cells
      case "field_height":
        FIELD_HEIGHT = Integer.parseInt(value);
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
        round = Integer.parseInt(value);
        break;

      // update game this_piece_type s: The type of the piece that has just spawned on the field
      case "this_piece_type":
        currentShapeType = ShapeType.valueOf(value);
        break;

      // update game next_piece_type s: The type of the piece that will spawn the next round
      case "next_piece_type":
        nextShapeType = ShapeType.valueOf(value);
        break;

      // update b row_points i: The amount of row points the given player has scored so far
      case "row_points":
        players.get(player).setPoints(Integer.parseInt(value));
        break;

      // update b combo i: The height of the current combo for the given player
      case "combo":
        players.get(player).setCombo(Integer.parseInt(value));
        break;

      // update b skips i: The amount of skips the given player has available
      case "skips":
        players.get(player).setSkips(Integer.parseInt(value));
        break;

      // update b field [[c,...];...]: The complete playing field of the given player
      case "field":
        players.get(player).setField(new AssessableField(this.FIELD_WIDTH, this.FIELD_HEIGHT, value));
        break;

      // update game this_piece_position i,i: The starting position in the field for
      //                                      the current piece (top left corner of the piece bounding box)
      case "this_piece_position":
        final String[] split = value.split(",");
        shapeLocation = new Point(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
        break;

      default:
        System.err.printf("Cannot parse updates with key \"%s\"\n", key);
        break;
    }
  }

  public void initShapes() {
    currentShape = new Shape(currentShapeType, getMyField(), shapeLocation);
    nextShape = new Shape(nextShapeType, getMyField(), shapeLocation);
  }

  public Player getOpponent() {
    for (Map.Entry<String, Player> entry : players.entrySet())
      if (entry.getKey().equals(myBot.getName()))
        return entry.getValue();
    return null;
  }

  public AssessableField getMyField() {
    return myBot.getField();
  }

  public AssessableField getOpponentField() {
    return getOpponent().getField();
  }

  public ShapeType getCurrentShapeType() {
    return currentShapeType;
  }

  public Shape getCurrentShape() {
    return currentShape;
  }

  public ShapeType getNextShapeType() {
    return nextShapeType;
  }

  public Shape getNextShape() {
    return nextShape;
  }

  public Point getShapeLocation() {
    return shapeLocation;
  }

  public int getRound() {
    return round;
  }
}
