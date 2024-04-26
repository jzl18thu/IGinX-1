/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package cn.edu.tsinghua.iginx.engine.shared.function.system;

import cn.edu.tsinghua.iginx.engine.physical.memory.execute.Table;
import cn.edu.tsinghua.iginx.engine.shared.data.read.Field;
import cn.edu.tsinghua.iginx.engine.shared.data.read.Header;
import cn.edu.tsinghua.iginx.engine.shared.data.read.Row;
import cn.edu.tsinghua.iginx.engine.shared.function.*;
import cn.edu.tsinghua.iginx.thrift.DataType;
import cn.edu.tsinghua.iginx.utils.DataTypeUtils;
import cn.edu.tsinghua.iginx.utils.Pair;
import java.util.List;

public class Sum implements SetMappingFunction {

  public static final String SUM = "sum";

  private static final Sum INSTANCE = new Sum();

  private Sum() {}

  public static Sum getInstance() {
    return INSTANCE;
  }

  @Override
  public FunctionType getFunctionType() {
    return FunctionType.System;
  }

  @Override
  public MappingType getMappingType() {
    return MappingType.SetMapping;
  }

  @Override
  public String getIdentifier() {
    return SUM;
  }

  @Override
  public Row transform(Table table, FunctionParams params) throws Exception {
    Pair<List<Field>, List<Integer>> pair = FunctionUtils.getFieldAndIndices(table, params, this);
    List<Field> targetFields = pair.k;
    List<Field> fields = table.getHeader().getFields();
    List<Integer> indices = pair.v;

    for (Field field : targetFields) {
      if (!DataTypeUtils.isNumber(field.getType())) {
        throw new IllegalArgumentException("only number can calculate sum");
      }
    }

    Object[] targetValues = new Object[targetFields.size()];
    for (int i = 0; i < targetFields.size(); i++) {
      Field targetField = targetFields.get(i);
      if (targetField.getType() == DataType.LONG) {
        targetValues[i] = 0L;
      } else {
        targetValues[i] = 0.0D;
      }
    }
    for (Row row : table.getRows()) {
      for (int i = 0; i < indices.size(); i++) {
        int index = indices.get(i);
        Object value = row.getValue(index);
        if (value == null) {
          continue;
        }
        switch (fields.get(index).getType()) {
          case INTEGER:
            targetValues[i] = ((long) targetValues[i]) + (int) value;
            break;
          case LONG:
            targetValues[i] = ((long) targetValues[i]) + (long) value;
            break;
          case FLOAT:
            targetValues[i] = ((double) targetValues[i]) + (float) value;
            break;
          case DOUBLE:
            targetValues[i] = ((double) targetValues[i]) + (double) value;
            break;
          default:
            throw new IllegalStateException(
                "Unexpected field type: " + fields.get(index).getType().toString());
        }
      }
    }
    return new Row(new Header(targetFields), targetValues);
  }
}
