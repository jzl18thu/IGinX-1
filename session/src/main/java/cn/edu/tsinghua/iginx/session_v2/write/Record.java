/*
 * IGinX - the polystore system with high performance
 * Copyright (C) Tsinghua University
 * TSIGinX@gmail.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package cn.edu.tsinghua.iginx.session_v2.write;

import cn.edu.tsinghua.iginx.session_v2.Arguments;
import cn.edu.tsinghua.iginx.thrift.DataType;
import cn.edu.tsinghua.iginx.utils.TagKVUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Record {

  private final long key;

  private final List<String> measurements;

  private final List<Map<String, String>> tagsList;

  private final List<DataType> dataTypes;

  private final List<Object> values;

  private Record(
      long key,
      List<String> measurements,
      List<Map<String, String>> tagsList,
      List<DataType> dataTypes,
      List<Object> values) {
    this.key = key;
    this.measurements = Collections.unmodifiableList(measurements);
    this.tagsList = tagsList;
    this.dataTypes = Collections.unmodifiableList(dataTypes);
    this.values = Collections.unmodifiableList(values);
  }

  public static Record.Builder builder() {
    return new Record.Builder();
  }

  public long getKey() {
    return key;
  }

  public List<String> getMeasurements() {
    return measurements;
  }

  public String getMeasurement(int index) {
    return measurements.get(index);
  }

  public String getFullName(int index) {
    String measurement = measurements.get(index);
    Map<String, String> tags = tagsList.get(index);
    return TagKVUtils.toFullName(measurement, tags);
  }

  public List<DataType> getDataTypes() {
    return dataTypes;
  }

  public DataType getDataType(int index) {
    return dataTypes.get(index);
  }

  public List<Object> getValues() {
    return values;
  }

  public Object getValue(int index) {
    return values.get(index);
  }

  public int getLength() {
    return measurements.size();
  }

  public static class Builder {

    private final Map<String, Integer> fieldIndexMap;
    private final List<Object> values;
    private final List<DataType> dataTypes;
    private final List<String> fields;

    private final List<Map<String, String>> tagsList;
    private long key;
    private String measurement;

    private Builder() {
      this.key = -1;
      this.measurement = null;
      this.fields = new ArrayList<>();
      this.values = new ArrayList<>();
      this.dataTypes = new ArrayList<>();
      this.fieldIndexMap = new HashMap<>();
      this.tagsList = new ArrayList<>();
    }

    public Record.Builder key(long key) {
      this.key = key;
      return this;
    }

    public Record.Builder now() {
      this.key = System.currentTimeMillis();
      return this;
    }

    public Record.Builder measurement(String measurement) {
      Arguments.checkNotNull(measurement, "measurement");
      this.measurement = measurement;
      return this;
    }

    public Record.Builder addIntField(String field, Integer value) {
      return addIntField(field, value, null);
    }

    public Record.Builder addIntField(String field, Integer value, Map<String, String> tags) {
      Arguments.checkNotNull(field, "field");
      int index = fieldIndexMap.getOrDefault(field, -1);
      if (index == -1) {
        this.fieldIndexMap.put(field, this.fields.size());
        this.fields.add(field);
        this.values.add(value);
        this.tagsList.add(tags);
        this.dataTypes.add(DataType.INTEGER);
      } else {
        this.values.set(index, value);
        this.dataTypes.set(index, DataType.INTEGER);
        this.tagsList.set(index, tags);
      }
      return this;
    }

    public Record.Builder addLongField(String field, Long value) {
      return addLongField(field, value, null);
    }

    public Record.Builder addLongField(String field, Long value, Map<String, String> tags) {
      Arguments.checkNotNull(field, "field");
      int index = fieldIndexMap.getOrDefault(field, -1);
      if (index == -1) {
        this.fieldIndexMap.put(field, this.fields.size());
        this.fields.add(field);
        this.values.add(value);
        this.tagsList.add(tags);
        this.dataTypes.add(DataType.LONG);
      } else {
        this.values.set(index, value);
        this.dataTypes.set(index, DataType.LONG);
        this.tagsList.set(index, tags);
      }
      return this;
    }

    public Record.Builder addFloatField(String field, Float value) {
      return addFloatField(field, value, null);
    }

    public Record.Builder addFloatField(String field, Float value, Map<String, String> tags) {
      Arguments.checkNotNull(field, "field");
      int index = fieldIndexMap.getOrDefault(field, -1);
      if (index == -1) {
        this.fieldIndexMap.put(field, this.fields.size());
        this.fields.add(field);
        this.values.add(value);
        this.tagsList.add(tags);
        this.dataTypes.add(DataType.FLOAT);
      } else {
        this.values.set(index, value);
        this.dataTypes.set(index, DataType.FLOAT);
        this.tagsList.set(index, tags);
      }
      return this;
    }

    public Record.Builder addDoubleField(String field, Double value) {
      return addDoubleField(field, value, null);
    }

    public Record.Builder addDoubleField(String field, Double value, Map<String, String> tags) {
      Arguments.checkNotNull(field, "field");
      int index = fieldIndexMap.getOrDefault(field, -1);
      if (index == -1) {
        this.fieldIndexMap.put(field, this.fields.size());
        this.fields.add(field);
        this.values.add(value);
        this.tagsList.add(tags);
        this.dataTypes.add(DataType.DOUBLE);
      } else {
        this.values.set(index, value);
        this.dataTypes.set(index, DataType.DOUBLE);
        this.tagsList.set(index, tags);
      }
      return this;
    }

    public Record.Builder addBooleanField(String field, Boolean value) {
      return addBooleanField(field, value, null);
    }

    public Record.Builder addBooleanField(String field, Boolean value, Map<String, String> tags) {
      Arguments.checkNotNull(field, "field");
      int index = fieldIndexMap.getOrDefault(field, -1);
      if (index == -1) {
        this.fieldIndexMap.put(field, this.fields.size());
        this.fields.add(field);
        this.values.add(value);
        this.tagsList.add(tags);
        this.dataTypes.add(DataType.BOOLEAN);
      } else {
        this.values.set(index, value);
        this.dataTypes.set(index, DataType.BOOLEAN);
        this.tagsList.set(index, tags);
      }
      return this;
    }

    public Record.Builder addBinaryField(String field, byte[] value) {
      return addBinaryField(field, value, null);
    }

    public Record.Builder addBinaryField(String field, byte[] value, Map<String, String> tags) {
      Arguments.checkNotNull(field, "field");
      int index = fieldIndexMap.getOrDefault(field, -1);
      if (index == -1) {
        this.fieldIndexMap.put(field, this.fields.size());
        this.fields.add(field);
        this.values.add(value);
        this.tagsList.add(tags);
        this.dataTypes.add(DataType.BINARY);
      } else {
        this.values.set(index, value);
        this.dataTypes.set(index, DataType.BINARY);
        this.tagsList.set(index, tags);
      }
      return this;
    }

    public Record build() {
      if (key < 0) {
        key = System.currentTimeMillis();
      }
      List<String> measurements = fields;
      if (measurement != null) {
        measurements = fields.stream().map(e -> measurement + "." + e).collect(Collectors.toList());
      }
      return new Record(key, measurements, tagsList, dataTypes, values);
    }
  }
}
