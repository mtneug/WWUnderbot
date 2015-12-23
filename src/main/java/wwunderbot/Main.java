/*
 * Copyright (c) 2015. WWUnderbot team
 */

package wwunderbot;

import wwunderbot.bot.BotParser;
import wwunderbot.bot.WWUnderbot;
import wwunderbot.geneticAlgorithm.Genome;

/**
 * @author Alexander, Frederik, Marco, Matthias
 */
public class Main {
  public static void main(String[] args) {
    new Main().run();
  }

  private void run() {
    Genome genome = new Genome(-0.510066, 0.760666, -0.35663, -0.184483);
    BotParser botParser = new BotParser(new WWUnderbot(genome));
    botParser.run();
  }
}
