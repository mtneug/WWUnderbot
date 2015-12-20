/*
 * Copyright (c) 2015. WWUnderbot team
 */

package wwunderbot;

import wwunderbot.bot.BotParser;
import wwunderbot.bot.WWUnderbot;
import wwunderbot.geneticAlgorithm.Genome;

/**
 * Created by Marco on 20.12.2015.
 */
public class Main {

  public static void main(String[] args) {
    Main main = new Main();
    main.init();
  }

  private void init() {
    Genome genome = new Genome(0.510066, 0.760666, 0.35663, 0.184483);
    BotParser botParser = new BotParser(new WWUnderbot(genome));

    botParser.run();
  }

}
