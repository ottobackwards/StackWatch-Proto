/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.otto;

import java.util.Deque;
import java.util.LinkedList;
import org.apache.commons.lang3.StringUtils;

/**
 * This class will be a stopwatch, but one that produces
 * named splits, such that at the end, it can produce
 * total time + N named subtimes
 * Where subtimes may be nested.
 * Each nesting will have a 'level'
 * such that if we printed and indented by level we would get an execution tree.
 *
 * can we also track total time 'in' as well
 * meaning that below, TIME2 has 250 total, with 150 of that 'in' TIME3
 * can we say TIME2 : 250 : 100?
 *
 * If each time is it's own stopwatch, and we can use split and total from that watch... then
 * we can do that.
 *
 * Each time should produce a record
 *
 *
 * START
 * TIME1 : 500
 *  TIME2 : 250
 *    TIME3 : 150
 *  TIME4 : 250
 *
 *
 * Try first just wrapping the Apache StopWatch from commons
 * We will calculate time at the end.
 *
 *
 */
public class StackWatch {

  private Deque<TimeRecordNode> deque = new LinkedList<>();
  private String rootName;
  private TimeRecordNode rootNode;

  public StackWatch(String name) {
    if (StringUtils.isEmpty(name)) {
      this.rootName = "root";
    }
    this.rootName = name;
  }

  public void startTime(String name) {
    // if the deque is empty, then this is a child of root
    TimeRecordNode parentNode = null;
    if (deque.isEmpty()) {
      rootNode = new TimeRecordNode(null, rootName);
      deque.push(rootNode);
      parentNode = rootNode;
      parentNode.start();
    } else {
      if (deque.peek().isRunning()) {
        // this is a nested start, add as child to current running
        parentNode = deque.peek();
      } else {
        throw new IllegalStateException(
            String.format("Node on queue is not running: %s", deque.peek().getPath()));
      }
    }
    TimeRecordNode node = parentNode.createChild(name);
    node.start();
    deque.push(node);
  }

  public void stopTime() {
    if (deque.isEmpty()) {
      throw new IllegalStateException("Trying to stop time, there are no running records in deque");
    }
    deque.pop().stop();
  }

  public void close() {
    deque.clear();
    rootNode = null;
  }

  public void visit(TimeRecordNodeVisitor visitor) {
    rootNode.visit(0, visitor);
  }
}
