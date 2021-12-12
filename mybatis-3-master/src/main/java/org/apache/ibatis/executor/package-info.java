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
/**执行器包是mybatis用来处理从数据库查询出来的数据结果的
 *
 * 问题：org.apache.ibatis.executor.BaseExecutor#deferLoad(org.apache.ibatis.mapping.MappedStatement, org.apache.ibatis.reflection.MetaObject, java.lang.String, org.apache.ibatis.cache.CacheKey, java.lang.Class) 要来感受
 * Contains the statement executors.
 */
package org.apache.ibatis.executor;
