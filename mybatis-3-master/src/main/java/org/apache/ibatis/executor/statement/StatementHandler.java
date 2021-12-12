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
package org.apache.ibatis.executor.statement;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.ResultHandler;

/**
 * 1.StatementHandler处理器没有什么好看的，主要是看处理后结果集的处理
 * 2.包括了key获取，（在调用构造函数构建 BaseStatementHandler对象的时候已经调用了 keyGenerator.processBefore设置值，在更新方法调用之后
 * 调用了 keyGenerator.processAfter

 * ）
 * @author Clinton Begin
 */
public interface StatementHandler {
  /*执行准备阶段：准备*/
  Statement prepare(Connection connection, Integer transactionTimeout)
      throws SQLException;
  /*statement 参数设置 比如把?设置参数*/
  void parameterize(Statement statement)
      throws SQLException;
  /*批量处理操作*/
  void batch(Statement statement)
      throws SQLException;
  /*更新*/
  int update(Statement statement)
      throws SQLException;
   /*查询*/
  <E> List<E> query(Statement statement, ResultHandler resultHandler)
      throws SQLException;

  <E> Cursor<E> queryCursor(Statement statement)
      throws SQLException;

  BoundSql getBoundSql();

  ParameterHandler getParameterHandler();

}
