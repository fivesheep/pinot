/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.pinot.broker.requesthandler;

import com.google.common.collect.ImmutableSet;
import org.apache.pinot.common.request.PinotQuery;
import org.apache.pinot.segment.spi.AggregationFunctionType;
import org.apache.pinot.sql.parsers.CalciteSqlParser;
import org.testng.Assert;
import org.testng.annotations.Test;


public class DistinctCountRewriteTest {

  @Test
  public void testSql() {
    String sql = "SELECT distinctCount(col1) FROM myTable";
    PinotQuery pinotQuery = CalciteSqlParser.compileToPinotQuery(sql);
    BaseBrokerRequestHandler.handleSegmentPartitionedDistinctCountOverride(pinotQuery, ImmutableSet.of("col2", "col3"));
    Assert.assertEquals(pinotQuery.getSelectList().get(0).getFunctionCall().getOperator(),
        AggregationFunctionType.DISTINCTCOUNT.name().toLowerCase());
    BaseBrokerRequestHandler.handleSegmentPartitionedDistinctCountOverride(pinotQuery,
        ImmutableSet.of("col1", "col2", "col3"));
    Assert.assertEquals(pinotQuery.getSelectList().get(0).getFunctionCall().getOperator(),
        AggregationFunctionType.SEGMENTPARTITIONEDDISTINCTCOUNT.name().toLowerCase());
  }
}
