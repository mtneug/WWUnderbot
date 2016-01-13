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

  private static final double DEFAULT_HEIGHT_WEIGHT = -0.510066;
  private static final double DEFAULT_COMPLETENESS_WEIGHT = 0.760666;
  private static final double DEFAULT_HOLES_WEIGHT = -0.35663;
  private static final double DEFAULT_BUMPINESS_WEIGHT = -0.184483;

  private Genome genome;

  public static void main(String[] args) {
    if (args.length == 0) {
      new Main().run();
    } else {
      double[] weights = {DEFAULT_HEIGHT_WEIGHT, DEFAULT_COMPLETENESS_WEIGHT, DEFAULT_HOLES_WEIGHT,
          DEFAULT_BUMPINESS_WEIGHT};
      int i = 0;
      try {
        while (i < args.length) {
          switch (args[i]) {
            case "-h":
              weights[0] = Double.parseDouble(args[++i]);
              break;
            case "-c":
              weights[1] = Double.parseDouble(args[++i]);
              break;
            case "-o":
              weights[2] = Double.parseDouble(args[++i]);
              break;
            case "-b":
              weights[3] = Double.parseDouble(args[++i]);
              break;
            default:
              break;
          }
          ++i;
        }
        new Main(weights[0], weights[1], weights[2], weights[3]).run();
      } catch (NumberFormatException e) {
        System.err.println("Could not parse paramater number: "+e.getMessage());
      }
    }
  }

  public Main() {
    this(DEFAULT_HEIGHT_WEIGHT, DEFAULT_COMPLETENESS_WEIGHT, DEFAULT_HOLES_WEIGHT,
        DEFAULT_BUMPINESS_WEIGHT);
  }

  public Main(double heightWeight, double completenessWeight, double holesWeight,
      double bumpinessWeight) {
    genome = new Genome(heightWeight, completenessWeight, holesWeight, bumpinessWeight);
  }

  private void run() {
    AbstractBot bot = new WWUnderbot(genome);
    new BotParser(bot, new FieldFactory(true)).run();
  }

}
