/*
 * Copyright (c) 2015. WWUnderbot team
 */

import com.theaigames.blockbattle.bot.BotParser;
import com.theaigames.blockbattle.models.Point;
import com.theaigames.blockbattle.models.Shape;
import com.theaigames.blockbattle.models.ShapeType;
import de.uni_muenster.wi.wwunderbot.bot.WWUnderbot;
import de.uni_muenster.wi.wwunderbot.ga.Genome;
import de.uni_muenster.wi.wwunderbot.models.AssessableField;

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
    //Genome genome = new Genome(-0.510066, 0.760666, -0.35663, -0.184483);
    Genome genome = new Genome(-0.310066, 1000000, -0.35663, -0.184483);
    BotParser botParser = new BotParser(new WWUnderbot(genome));
    botParser.run();
  }

  private void test() {
    Genome genome = new Genome(-0.310066, 10, -0.35663, -0.184483);
    AssessableField f1 = new AssessableField(10, 20, "0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0;0,0,0,0,0,0,0,0,0,0");
    printResults(genome, f1);

    Shape i1 = new Shape(ShapeType.S, f1, new Point(3,0));
    i1.drop(f1);
    f1.addShape(i1);
    printResults(genome, f1);

    Shape i2 = new Shape(ShapeType.L, f1, new Point(3,0));
    i2.drop(f1);
    f1.addShape(i2);
    printResults(genome, f1);

  }

  private void printResults(Genome genome, AssessableField field) {
    System.out.println(field.getAggregateHeight());
    System.out.println(field.getCompleteness());
    System.out.println(field.getBumpiness());
    System.out.println(field.getHoles());
    System.out.println(calculateScore(field, genome));
  }

  private double calculateScore(final AssessableField field, final Genome genome) {
    return genome.getHeightWeight() * field.getAggregateHeight()
      + genome.getCompletenessWeight() * field.getCompleteness()
      + genome.getHolesWeight() * field.getHoles()
      + genome.getBumpinessWeight() * field.getBumpiness();
  }
}
