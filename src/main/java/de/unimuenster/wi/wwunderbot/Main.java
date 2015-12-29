/*
 * Copyright (c) 2015. WWUnderbot team
 */

package de.unimuenster.wi.wwunderbot;

import com.theaigames.blockbattle.bot.AbstractBot;
import com.theaigames.blockbattle.bot.BotParser;
import com.theaigames.blockbattle.models.FieldFactory;
import de.unimuenster.wi.wwunderbot.bot.WWUnderbot;
import de.unimuenster.wi.wwunderbot.ga.Genome;

/**
 * Main entry point.
 *
 * @author Alexander
 * @author Frederik
 * @author Marco
 * @author Matthias
 */
public class Main {
  public static void main(String[] args) {
    new Main().run();
  }

  private void run() {
    AbstractBot bot = new WWUnderbot(new Genome(-0.510066, 0.760666, -0.35663, -0.184483));
    new BotParser(bot, new FieldFactory(true)).run();
  }
}
