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
package org.apache.ibatis.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.UnknownTypeHandler;

/**
 * 为构造函数参数指定映射定义的注释。一般在构造参数的resultMap中使用
 *
 * @see ConstructorArgs
 * @author Clinton Begin
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(ConstructorArgs.class)
public @interface Arg {

  /**
   * 是否是ID列
   *
   * @return {@code true} if id column; {@code false} if otherwise
   */
  boolean id() default false;

  /**
   * 返回映射到此参数的列名(或列标签)。
   *
   * @return the column name(or column label)
   */
  String column() default "";

  /**
   *返回这个java类型是什么
   *
   * @return the java type
   */
  Class<?> javaType() default void.class;

  /**
   * 返回映射到此参数的列的jdbc类型。
   *
   * @return the jdbc type
   */
  JdbcType jdbcType() default JdbcType.UNDEFINED;

  /**
   * 使用的类型处理器
   *
   * @return the {@link TypeHandler} type
   */
  Class<? extends TypeHandler> typeHandler() default UnknownTypeHandler.class;

  /**
   *一个命名空间+id值（代表一条sql）
   *
   * @return the statement id
   */
  String select() default "";

  /**
   * 一个resultMap的ID
   *
   * @return the result map id
   */
  String resultMap() default "";

  /**
   * 返回应用此映射的参数名称。
   *
   * @return the parameter name
   * @since 3.4.3
   */
  String name() default "";

  /**
   * Returns the column prefix that use when applying {@link #resultMap()}.
   *
   * @return the column prefix
   * @since 3.5.0
   */
  String columnPrefix() default "";
}
