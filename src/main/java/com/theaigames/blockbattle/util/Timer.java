/*
 * Copyright (c) 2015. WWUnderbot team
 */

package com.theaigames.blockbattle.util;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

/**
 * Modified version of the {@code ElapsedCpuTimer} class part of the
 * General Video Game AI Competition demo bots.
 *
 * @author diego
 * @author Alexander
 * @author Frederik
 * @author Marco
 * @author Matthias
 */
public class Timer implements Cloneable {
  private ThreadMXBean bean = ManagementFactory.getThreadMXBean();
  private TimerType type;
  private long oldTime;
  private long maxTime;

  public Timer(long maxTime) {
    this(TimerType.WALL_TIME, maxTime);
  }

  public Timer(TimerType type, long maxTime) {
    this.type = type;
    oldTime = getTime();
    setMaxTimeMillis(maxTime);
  }

  @Override
  public Timer clone() {
    Timer cloned;
    try {
      cloned = (Timer) super.clone();
    } catch (CloneNotSupportedException e) {
      // this shouldn't happen, since we are Cloneable
      throw new InternalError(e);
    }
    return cloned;
  }

  public long elapsed() {
    return getTime() - oldTime;
  }

  public long elapsedNanos() {
    return (long) (elapsed() / 1000.0);
  }

  public long elapsedMillis() {
    return (long) (elapsed() / 1000000.0);
  }

  public double elapsedSeconds() {
    return elapsedMillis() / 1000.0;
  }

  public double elapsedMinutes() {
    return elapsedMillis() / 1000.0 / 60.0;
  }

  public double elapsedHours() {
    return elapsedMinutes() / 60.0;
  }

  public long remainingTimeMillis() {
    long diff = maxTime - elapsed();
    return (long) (diff / 1000000.0);
  }

  public boolean exceededMaxTime() {
    return elapsed() > maxTime;
  }

  public void setMaxTimeMillis(long time) {
    maxTime = time * 1000000;
  }

  public String toString() {
    return elapsed() / 1000000.0 + " ms elapsed";
  }

  private long getTime() {
    switch (type) {
      case CPU_TIME:
        return getCpuTime();

      case USER_TIME:
        return getUserTime();
    }
    return getWallTime();
  }

  private long getWallTime() {
    return System.nanoTime();
  }

  private long getCpuTime() {
    if (bean.isCurrentThreadCpuTimeSupported())
      return bean.getCurrentThreadCpuTime();
    else
      throw new RuntimeException("CpuTime not supported");
  }

  private long getUserTime() {
    if (bean.isCurrentThreadCpuTimeSupported())
      return bean.getCurrentThreadUserTime();
    else
      throw new RuntimeException("UserTime not supported");
  }

  public enum TimerType {
    WALL_TIME, CPU_TIME, USER_TIME
  }
}
