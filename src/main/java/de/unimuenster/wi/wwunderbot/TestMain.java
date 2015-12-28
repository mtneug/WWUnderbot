/*
 * Copyright (c) 2015. WWUnderbot team
 */

package de.unimuenster.wi.wwunderbot;

import com.theaigames.blockbattle.bot.AbstractBot;
import com.theaigames.blockbattle.bot.BotParser;
import com.theaigames.blockbattle.bot.BotState;
import com.theaigames.blockbattle.models.MoveType;
import com.theaigames.blockbattle.models.Point;
import com.theaigames.blockbattle.models.Shape;
import com.theaigames.blockbattle.models.ShapeType;
import de.unimuenster.wi.wwunderbot.bot.WWUnderbot;
import de.unimuenster.wi.wwunderbot.ga.Genome;
import de.unimuenster.wi.wwunderbot.models.AssessableField;
import de.unimuenster.wi.wwunderbot.models.AssessableFieldFactory;

import java.util.ArrayList;

/**
 * Main entry point.
 *
 * @author Alexander
 * @author Frederik
 * @author Marco
 * @author Matthias
 */
public class TestMain {
  public void main(String[] args) {
    new TestMain().test2();
  }

  private void test2() {
    AbstractBot bot = new WWUnderbot(new Genome(-0.510066, 0.760666, -0.35663, -0.184483));
    BotState state = new BotState(AssessableFieldFactory.getInstance());
    BotParser botParser = new BotParser(bot, state);
    String[] commands = {
        "settings timebank 10000",
        "settings time_per_move 500",
        "settings player_names player1,player2",
        "settings your_bot player1",
        "settings field_width 10",
        "settings field_height 20",

        "update game round 1",
        "update game this_piece_type O",
        "update game next_piece_type L",
        "update game this_piece_position 4,-1",
        "update player1 row_points 0",
        "update player1 combo 0",
        "update player1 skips 0",
        "update player1 field 0,0,0,0,1,1,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0",
        "update player2 field 0,0,0,0,1,1,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0",
        "update player2 row_points 0",
        "update player2 combo 0",
        "update player2 skips 0",
        "action moves 10000",

        "update game round 2",
        "update game this_piece_type L",
        "update game next_piece_type O",
        "update game this_piece_position 3,-1",
        "update player1 row_points 0",
        "update player1 combo 0",
        "update player1 skips 0",
        "update player1 field 0,0,0,1,1,1,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,2,2,0,0,0,0;0,0,0,0,2,2,0,0,0,0",
        "update player2 field 0,0,0,1,1,1,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,2,2,0,0;0,0,0,0,0,0,2,2,0,0",
        "update player2 row_points 0",
        "update player2 combo 0",
        "update player2 skips 0",
        "action moves 10000",

        "update game round 3",
        "update game this_piece_type O",
        "update game next_piece_type O",
        "update game this_piece_position 4,-1",
        "update player1 row_points 0",
        "update player1 combo 0",
        "update player1 skips 0",
        "update player1 field 0,0,0,0,1,1,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,2,0,0,0,0;0,0,0,2,2,2,0,0,0,0;0,0,0,0,2,2,0,0,0,0;0,0,0,0,2,2,0,0,0,0",
        "update player2 field 0,0,0,0,1,1,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,2,0;0,0,0,0,0,0,2,2,2,0;0,0,0,0,0,0,2,2,2,2",
        "update player2 row_points 0",
        "update player2 combo 0",
        "update player2 skips 0",
        "action moves 10000",

        "update game round 4",
        "update game this_piece_type O",
        "update game next_piece_type L",
        "update game this_piece_position 4,-1",
        "update player1 row_points 0",
        "update player1 combo 0",
        "update player1 skips 0",
        "update player1 field 0,0,0,0,1,1,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,2,2,0,0,0,0;0,0,0,0,2,2,0,0,0,0;0,0,0,0,0,2,0,0,0,0;0,0,0,2,2,2,0,0,0,0;0,0,0,0,2,2,0,0,0,0;0,0,0,0,2,2,0,0,0,0",
        "update player2 field 0,0,0,0,1,1,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,2,2,0,0,0;0,0,0,0,0,2,2,0,2,0;0,0,0,0,0,0,2,2,2,0;0,0,0,0,0,0,2,2,2,2",
        "update player2 row_points 0",
        "update player2 combo 0",
        "update player2 skips 0",
        "action moves 10000",

        "update game round 5",
        "update game this_piece_type L",
        "update game next_piece_type T",
        "update game this_piece_position 3,-1",
        "update player1 row_points 0",
        "update player1 combo 0",
        "update player1 skips 0",
        "update player1 field 0,0,0,1,1,1,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,2,2,0,0,0,0;0,0,0,0,2,2,0,0,0,0;0,0,0,0,2,2,0,0,0,0;0,0,0,0,2,2,0,0,0,0;0,0,0,0,0,2,0,0,0,0;0,0,0,2,2,2,0,0,0,0;0,0,0,0,2,2,0,0,0,0;0,0,0,0,2,2,0,0,0,0",
        "update player2 field 0,0,0,1,1,1,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,2,2,0,0,0;0,0,0,0,0,2,2,0,2,0;0,0,0,2,2,0,2,2,2,0;0,0,0,2,2,0,2,2,2,2",
        "update player2 row_points 0",
        "update player2 combo 0",
        "update player2 skips 0",
        "action moves 10000",

        "update game round 6",
        "update game this_piece_type T",
        "update game next_piece_type T",
        "update game this_piece_position 3,-1",
        "update player1 row_points 0",
        "update player1 combo 0",
        "update player1 skips 0",
        "update player1 field 0,0,0,1,1,1,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,2,0,0,0,0;0,0,0,2,2,2,0,0,0,0;0,0,0,0,2,2,0,0,0,0;0,0,0,0,2,2,0,0,0,0;0,0,0,0,2,2,0,0,0,0;0,0,0,0,2,2,0,0,0,0;0,0,0,0,0,2,0,0,0,0;0,0,0,2,2,2,0,0,0,0;0,0,0,0,2,2,0,0,0,0;0,0,0,0,2,2,0,0,0,0",
        "update player2 field 0,0,0,1,1,1,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,2,2,0,0,0;0,2,0,0,0,2,2,0,2,0;0,2,0,2,2,0,2,2,2,0;0,2,2,2,2,0,2,2,2,2",
        "update player2 row_points 0",
        "update player2 combo 0",
        "update player2 skips 0",
        "action moves 10000",

        "update game round 7",
        "update game this_piece_type T",
        "update game next_piece_type L",
        "update game this_piece_position 3,-1",
        "update player1 row_points 0",
        "update player1 combo 0",
        "update player1 skips 0",
        "update player1 field 0,0,0,1,1,1,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,2,0,0,0,0,0;0,0,0,2,2,2,0,0,0,0;0,0,0,0,0,2,0,0,0,0;0,0,0,2,2,2,0,0,0,0;0,0,0,0,2,2,0,0,0,0;0,0,0,0,2,2,0,0,0,0;0,0,0,0,2,2,0,0,0,0;0,0,0,0,2,2,0,0,0,0;0,0,0,0,0,2,0,0,0,0;0,0,0,2,2,2,0,0,0,0;0,0,0,0,2,2,0,0,0,0;0,0,0,0,2,2,0,0,0,0",
        "update player2 field 0,0,0,1,1,1,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,2,0,0,0,0,0,0;0,0,2,2,0,2,2,0,0,0;0,2,0,2,0,2,2,0,2,0;0,2,0,2,2,0,2,2,2,0;0,2,2,2,2,0,2,2,2,2",
        "update player2 row_points 0",
        "update player2 combo 0",
        "update player2 skips 0",
        "action moves 10000",

        "update game round 8",
        "update game this_piece_type L",
        "update game next_piece_type Z",
        "update game this_piece_position 3,-1",
        "update player1 row_points 0",
        "update player1 combo 0",
        "update player1 skips 0",
        "update player1 field 0,0,0,1,1,1,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,2,0,0,0,0,0;0,0,0,2,2,2,0,0,0,0;0,0,0,0,2,0,0,0,0,0;0,0,0,2,2,2,0,0,0,0;0,0,0,0,0,2,0,0,0,0;0,0,0,2,2,2,0,0,0,0;0,0,0,0,2,2,0,0,0,0;0,0,0,0,2,2,0,0,0,0;0,0,0,0,2,2,0,0,0,0;0,0,0,0,2,2,0,0,0,0;0,0,0,0,0,2,0,0,0,0;0,0,0,2,2,2,0,0,0,0;0,0,0,0,2,2,0,0,0,0;0,0,0,0,2,2,0,0,0,0",
        "update player2 field 0,0,0,1,1,1,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,2,2,2,0,0,0;0,0,0,2,0,2,0,0,0,0;0,0,2,2,0,2,2,0,0,0;0,2,0,2,0,2,2,0,2,0;0,2,0,2,2,0,2,2,2,0;0,2,2,2,2,0,2,2,2,2",
        "update player2 row_points 0",
        "update player2 combo 0",
        "update player2 skips 0",
        "action moves 10000",

        "update game round 9",
        "update game this_piece_type Z",
        "update game next_piece_type L",
        "update game this_piece_position 3,-1",
        "update player1 row_points 0",
        "update player1 combo 0",
        "update player1 skips 0",
        "update player1 field 0,0,0,0,1,1,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,2,0,0,0,0;0,0,0,2,2,2,0,0,0,0;0,0,0,0,2,0,0,0,0,0;0,0,0,2,2,2,0,0,0,0;0,0,0,0,2,0,0,0,0,0;0,0,0,2,2,2,0,0,0,0;0,0,0,0,0,2,0,0,0,0;0,0,0,2,2,2,0,0,0,0;0,0,0,0,2,2,0,0,0,0;0,0,0,0,2,2,0,0,0,0;0,0,0,0,2,2,0,0,0,0;0,0,0,0,2,2,0,0,0,0;0,0,0,0,0,2,0,0,0,0;0,0,0,2,2,2,0,0,0,0;0,0,0,0,2,2,0,0,0,0;0,0,0,0,2,2,0,0,0,0",
        "update player2 field 0,0,0,0,1,1,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,2,2,0,0,0,0,0,0;0,0,0,2,0,0,0,0,0,0;0,0,0,2,2,2,2,0,0,0;0,0,0,2,0,2,0,0,0,0;0,0,2,2,0,2,2,0,0,0;0,2,0,2,0,2,2,0,2,0;0,2,0,2,2,0,2,2,2,0;0,2,2,2,2,0,2,2,2,2",
        "update player2 row_points 0",
        "update player2 combo 0",
        "update player2 skips 0",
        "action moves 10000",

        "update game round 10",
        "update game this_piece_type L",
        "update game next_piece_type J",
        "update game this_piece_position 3,-1",
        "update player1 row_points 0",
        "update player1 combo 0",
        "update player1 skips 0",
        "update player1 field 0,0,0,1,1,1,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,2,0,0,0,0;0,0,0,0,2,2,0,0,0,0;0,0,0,0,2,2,0,0,0,0;0,0,0,2,2,2,0,0,0,0;0,0,0,0,2,0,0,0,0,0;0,0,0,2,2,2,0,0,0,0;0,0,0,0,2,0,0,0,0,0;0,0,0,2,2,2,0,0,0,0;0,0,0,0,0,2,0,0,0,0;0,0,0,2,2,2,0,0,0,0;0,0,0,0,2,2,0,0,0,0;0,0,0,0,2,2,0,0,0,0;0,0,0,0,2,2,0,0,0,0;0,0,0,0,2,2,0,0,0,0;0,0,0,0,0,2,0,0,0,0;0,0,0,2,2,2,0,0,0,0;0,0,0,0,2,2,0,0,0,0;0,0,0,0,2,2,0,0,0,0",
        "update player2 field 0,0,0,1,1,1,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,2,2,0,0,0,0,0,0,0;0,0,2,2,0,0,0,0,0,0;0,0,2,2,0,0,0,0,0,0;0,0,0,2,0,0,0,0,0,0;0,0,0,2,2,2,2,0,0,0;0,0,0,2,0,2,0,0,0,0;0,0,2,2,0,2,2,0,0,0;0,2,0,2,0,2,2,0,2,0;0,2,0,2,2,0,2,2,2,0;0,2,2,2,2,0,2,2,2,2",
        "update player2 row_points 0",
        "update player2 combo 0",
        "update player2 skips 0",
        "action moves 10000",
    };
    for (String command : commands) {
      botParser.handleLine(command);
    }
  }

  private void test1() {
    Genome genome = new Genome(-0.510066, 0.760666, -0.35663, -0.184483);
    WWUnderbot bot = new WWUnderbot(genome);

    AssessableField field = new AssessableField(10, 20);
    Shape[] shapes = {
        new Shape(ShapeType.O, field, new Point(4, 0)),
        new Shape(ShapeType.J, field, new Point(3, 0)),
    };

    for (Shape shape : shapes) {
      ArrayList<MoveType> moves = bot.findTargetShapeState(field, new Shape[]{shape}).getMoves(shape.getLocation());
      System.err.println(moves);

      for (MoveType move : moves) {
        switch (move) {
          case DOWN:
            shape.oneDown();
            break;

          case DROP:
            shape.drop(field);
            break;

          case LEFT:
            shape.oneLeft();
            break;

          case RIGHT:
            shape.oneRight();
            break;

          case TURNLEFT:
            shape.rotateLeft();
            break;

          case TURNRIGHT:
            shape.rotateRight();
            break;
        }
      }

      field.addShape(shape);
    }
  }

  private void printResults(WWUnderbot bot, AssessableField field) {
    System.out.println(field.getAggregateHeight());
    System.out.println(field.getCompleteness());
    System.out.println(field.getBumpiness());
    System.out.println(field.getHoles());
    System.out.println(bot.calculateScore(field));
    System.out.println();
  }
}
