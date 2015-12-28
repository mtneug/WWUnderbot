/*
 * Copyright (c) 2015. WWUnderbot team
 */

package de.unimuenster.wi.wwunderbot.util;

import de.unimuenster.wi.wwunderbot.bot.WWUnderbot;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by Marco on 25.12.2015.
 */
public class CSVwriter {
  private static final String COMMA_DELIMITER = ",";
  private static final String NEW_LINE_SEPARATOR = System.getProperty("line.separator");
  private static final String CSV_HEADER = "status (round, etc.), x, y, rotation, score, best_x, best_y, best_rotation, best_score";
  private static CSVwriter csvwriter;
  private Writer writer;

  /*private CSVwriter() {
    try {
      this.writer = new FileWriter("results.csv");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }*/

  private CSVwriter() {
    this.writer = new StringWriter();
  }

  public static CSVwriter getCSVwriterInstance() {
    if (csvwriter == null) {
      csvwriter = new CSVwriter();
    }
    return csvwriter;
  }

  public void writeResult(WWUnderbot.ShapeStateAssessment shapeStateAssessment) {
    try {
      this.writer.append("");
      this.writer.append(COMMA_DELIMITER);
      this.writer.append(String.valueOf(shapeStateAssessment.location.getX()));
      this.writer.append(COMMA_DELIMITER);
      this.writer.append(String.valueOf(shapeStateAssessment.location.getY()));
      this.writer.append(COMMA_DELIMITER);
      this.writer.append(String.valueOf(shapeStateAssessment.rotation));
      this.writer.append(COMMA_DELIMITER);
      this.writer.append(Double.toString(shapeStateAssessment.score));
      this.writer.append(NEW_LINE_SEPARATOR);
      this.writer.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void writeStatusResult(String status, WWUnderbot.ShapeStateAssessment shapeStateAssessment) {
    try {
      this.writer.append(status);
      this.writer.append(COMMA_DELIMITER);
      this.writer.append(String.valueOf(shapeStateAssessment.location.getX()));
      this.writer.append(COMMA_DELIMITER);
      this.writer.append(String.valueOf(shapeStateAssessment.location.getY()));
      this.writer.append(COMMA_DELIMITER);
      this.writer.append(String.valueOf(shapeStateAssessment.rotation));
      this.writer.append(COMMA_DELIMITER);
      this.writer.append(Double.toString(shapeStateAssessment.score));
      this.writer.append(NEW_LINE_SEPARATOR);
      this.writer.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void writeBestResult(WWUnderbot.ShapeStateAssessment shapeStateAssessment) {
    try {
      this.writer.append("");
      this.writer.append(COMMA_DELIMITER);
      this.writer.append("");
      this.writer.append(COMMA_DELIMITER);
      this.writer.append("");
      this.writer.append(COMMA_DELIMITER);
      this.writer.append("");
      this.writer.append(COMMA_DELIMITER);
      this.writer.append("");
      this.writer.append(COMMA_DELIMITER);
      this.writer.append(String.valueOf(shapeStateAssessment.location.getX()));
      this.writer.append(COMMA_DELIMITER);
      this.writer.append(String.valueOf(shapeStateAssessment.location.getY()));
      this.writer.append(COMMA_DELIMITER);
      this.writer.append(String.valueOf(shapeStateAssessment.rotation));
      this.writer.append(COMMA_DELIMITER);
      this.writer.append(Double.toString(shapeStateAssessment.score));
      this.writer.append(NEW_LINE_SEPARATOR);
      this.writer.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void writeStatus(String status) {
    try {
      this.writer.append(status);
      this.writer.append(NEW_LINE_SEPARATOR);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void writerClose() {
    try {
      this.writer.flush();
      this.writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
