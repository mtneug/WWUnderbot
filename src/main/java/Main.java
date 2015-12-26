/*
 * Copyright (c) 2015. WWUnderbot team
 */

import com.theaigames.blockbattle.bot.BotParser;
import de.uni_muenster.wi.wwunderbot.bot.WWUnderbot;
import de.uni_muenster.wi.wwunderbot.ga.Genome;

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
    Genome genome = new Genome(-0.510066, 0.760666, -0.35663, -0.184483);
    BotParser botParser = new BotParser(new WWUnderbot(genome));
    botParser.run();
  }
}
