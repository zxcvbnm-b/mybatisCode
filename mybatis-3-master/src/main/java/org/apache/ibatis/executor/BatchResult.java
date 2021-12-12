/*
 *    Copyright 2009-2021 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.executor;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.mapping.MappedStatement;

/**
 * 代表一个批次
 * @author Jeff Butler
 */
public class BatchResult {
  /*这个批次的 mappedStatement*/
  private final MappedStatement mappedStatement;
  /*这个批次的 sql 已经进行了属性的输入和参数的解析了的*/
  private final String sql;
  /*批量执行的参数 比如一个sql多次执行，那么他肯定有多个参数的*/
  private final List<Object> parameterObjects;
  /*更新后返回相应行数量*/
  private int[] updateCounts;

  public BatchResult(MappedStatement mappedStatement, String sql) {
    super();
    this.mappedStatement = mappedStatement;
    this.sql = sql;
    this.parameterObjects = new ArrayList<>();
  }

  public BatchResult(MappedStatement mappedStatement, String sql, Object parameterObject) {
    this(mappedStatement, sql);
    addParameterObject(parameterObject);
  }

  public MappedStatement getMappedStatement() {
    return mappedStatement;
  }

  public String getSql() {
    return sql;
  }

  @Deprecated
  public Object getParameterObject() {
    return parameterObjects.get(0);
  }

  public List<Object> getParameterObjects() {
    return parameterObjects;
  }

  public int[] getUpdateCounts() {
    return updateCounts;
  }

  public void setUpdateCounts(int[] updateCounts) {
    this.updateCounts = updateCounts;
  }

  public void addParameterObject(Object parameterObject) {
    this.parameterObjects.add(parameterObject);
  }

}
