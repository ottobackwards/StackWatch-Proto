package org.otto;

import java.util.ArrayList;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class StackWatchTest extends TestCase {

  /**
   * Create the test case
   *
   * @param testName name of the test case
   */
  public StackWatchTest(String testName) {
    super(testName);
  }

  /**
   * @return the suite of tests being tested
   */
  public static Test suite() {
    return new TestSuite(StackWatchTest.class);
  }

  public void testStackWatch() throws Exception {
    StackWatch watch = new StackWatch("testStackWatch");
    watch.startTime("Test");
    functionOne(watch);
    functionTwo(watch);
    functionThree(watch);
    watch.stopTime();
    final ArrayList<Integer> levels = new ArrayList<>();
    watch.visit((l, n) -> {
      levels.add(l);
      System.out.println(String.format("%d -> %s : %d", l, n.getPath(), n.getTime()));
    });

    assertEquals(levels.size(),9);
  }

  public void testNonStartOuter() throws Exception {
    StackWatch watch = new StackWatch("testStackWatch");
    functionOne(watch);
    watch.stopTime();

    final ArrayList<Integer> levels = new ArrayList<>();
    watch.visit((l, n) -> {
      levels.add(l);
      System.out.println(String.format("%d -> %s : %d", l, n.getPath(), n.getTime()));
    });
    assertEquals(levels.size(), 4);
  }

  public void testDidNotStopAll() throws Exception {
    StackWatch watch = new StackWatch("testStackWatch");
    watch.startTime("Test");
    functionOne(watch);
    functionTwo(watch);
    functionThree(watch);
    functionNoStop(watch);
    watch.stopTime();
    final ArrayList<Integer> levels = new ArrayList<>();
    watch.visit((l, n) -> {
      levels.add(l);
      System.out.println(String.format("%d -> %s : %d", l, n.getPath(), n.getTime()));
    });

    assertEquals(levels.size(),10);
  }


  private void functionOne(StackWatch watch) throws Exception {
    watch.startTime("One");
    Thread.sleep(500);
    functionOneOne(watch);
    watch.stopTime();
  }

  private void functionOneOne(StackWatch watch) throws Exception {
    watch.startTime("OneOne");
    Thread.sleep(500);
    functionOneTwo(watch);
    watch.stopTime();

  }

  private void functionOneTwo(StackWatch watch) throws Exception {
    watch.startTime("OneTwo");
    Thread.sleep(500);
    watch.stopTime();
  }

  private void functionTwo(StackWatch watch) throws Exception {
    watch.startTime("Two");
    Thread.sleep(500);
    functionTwoOne(watch);
    watch.stopTime();
  }

  private void functionTwoOne(StackWatch watch) throws Exception {
    watch.startTime("TwoOne");
    Thread.sleep(500);
    functionTwoTwo(watch);
    watch.stopTime();
  }

  private void functionTwoTwo(StackWatch watch) throws Exception {
    watch.startTime("TwoTwo");
    Thread.sleep(500);
    watch.stopTime();
  }

  private void functionThree(StackWatch watch) throws Exception {
    watch.startTime("Three");
    Thread.sleep(500);
    watch.stopTime();
  }

  private void functionNoStop(StackWatch watch) throws Exception {
    watch.startTime("NoStop");
    Thread.sleep(500);
  }

}
