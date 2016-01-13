/*
 * Copyright (c) 2015. WWUnderbot team
 */

package com.theaigames.blockbattle.bot;

import com.theaigames.blockbattle.models.*;
import com.theaigames.blockbattle.util.Timer;

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
  private final FieldFactory fieldFactory;
  private int fieldWidth;
  private int fieldHeight;
  private HashMap<String, Player> players = new HashMap<>();
  private int round = 0;

  private Timer timer;
  private long maxTimebank;
  private long timePerMove;
  private long timebank;

  private Player myBot;
  private Shape currentShape;
  private Shape nextShape;
  private ShapeType currentShapeType;
  private ShapeType nextShapeType;
  private Point shapeLocation;

  public BotState(FieldFactory fieldFactory) {
    this.fieldFactory = fieldFactory;
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
        maxTimebank = Integer.parseInt(value);
        timebank = maxTimebank;
        break;

      // settings time_per_move t: Time in milliseconds that is added to your bot's time bank each move
      case "time_per_move":
        timePerMove = Integer.parseInt(value);
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
        fieldWidth = Integer.parseInt(value);
        break;

      // settings field_height i: The height of the field, i.e. number of column-cells
      case "field_height":
        fieldHeight = Integer.parseInt(value);
        break;

      default:
        System.err.printf("Cannot parse settings with key \"%s\"\n", key);
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
        players.get(player).setField(fieldFactory.newField(fieldWidth, fieldHeight, value));
        break;

      // update game this_piece_position i,i: The starting position in the field for
      //                                      the current piece (top left corner of the piece bounding box)
      case "this_piece_position":
        // TODO: set location of next piece
        final String[] split = value.split(",");
        shapeLocation = new Point(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
        break;

      default:
        System.err.printf("Cannot parse updates with key \"%s\"\n", key);
    }
  }

  public void initShapes() {
    currentShape = new Shape(currentShapeType, getMyField(), shapeLocation);
    nextShape = new Shape(nextShapeType, getMyField(), shapeLocation);
  }

  public Player getMe() {
    return myBot;
  }

  public Player getOpponent() {
    for (Map.Entry<String, Player> entry : players.entrySet())
      if (!entry.getKey().equals(myBot.getName()))
        return entry.getValue();
    return null;
  }

  public Field getMyField() {
    return myBot.getField();
  }

  public Field getOpponentField() {
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

  public FieldFactory getFieldFactory() {
    return fieldFactory;
  }

  public int getFieldWidth() {
    return fieldWidth;
  }

  public int getFieldHeight() {
    return fieldHeight;
  }

  public long getMaxTimebank() {
    return maxTimebank;
  }

  public long getTimePerMove() {
    return timePerMove;
  }

  public long getTimebank() {
    return timebank;
  }

  void setTimebank(long timebank) {
    this.timebank = timebank;
  }

  public Timer getTimer() {
    return timer;
  }

  void startTimer() {
    timer = new Timer(timePerMove);
  }
}
