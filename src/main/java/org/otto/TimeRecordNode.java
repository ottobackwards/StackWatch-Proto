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

import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;

/**
 * The tree node to track time and children
 */
public class TimeRecordNode {

  public static final String pathFmt = "%s/%s";

  String parentName;
  String name;


  List<TimeRecordNode> children = new LinkedList<>();
  StopWatch stopWatch = new StopWatch();

  public TimeRecordNode(String parentName, String name) {
    if (StringUtils.isEmpty(name)) {
      throw new IllegalArgumentException("Argument name is missing");
    }
    this.name = name;

    if (StringUtils.isNotEmpty(parentName)) {
      this.parentName = parentName;
    }
  }

  public String getParentName() {
    return parentName;
  }

  public String getName() {
    return name;
  }

  public boolean isRunning() {
    return stopWatch.isStarted();
  }

  public void start() {
    stopWatch.start();
  }

  public void stop() {
    stopWatch.stop();
  }

  public long getTime() {
    return stopWatch.getTime();
  }

  public String getPath() {
    if (parentName == null) {
      return name;
    }
    return String.format(pathFmt, parentName, name);
  }

  public Iterable<TimeRecordNode> getChildren() {
    return children;
  }

  public TimeRecordNode createChild(String childName) throws IllegalStateException{
    if (!stopWatch.isStarted()) {
      throw new IllegalStateException("Adding a child to a non-started parent");
    }
    TimeRecordNode child = new TimeRecordNode(this.getPath(), childName);
    children.add(child);
    return child;
  }

  protected void visit(int level, TimeRecordNodeVisitor visitor) {
    visitor.visitRecord(level, this);
    children.forEach((n) -> n.visit(level + 1, visitor));
  }
}
