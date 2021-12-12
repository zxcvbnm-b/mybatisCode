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

import java.lang.reflect.Array;
import java.util.List;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.session.Configuration;

/**
 * @author Andrew Gustafson
 */
public class ResultExtractor {
  private final Configuration configuration;
  private final ObjectFactory objectFactory;

  public ResultExtractor(Configuration configuration, ObjectFactory objectFactory) {
    this.configuration = configuration;
    this.objectFactory = objectFactory;
  }
  /*从列表中提取一个对象*/
  public Object extractObjectFromList(List<Object> list, Class<?> targetType) {
    Object value = null;
    /*如果是list类型，那么直接赋值就行*/
    if (targetType != null && targetType.isAssignableFrom(list.getClass())) {
      value = list;
      /*如果是结合类型，新建然后给只，就行*/
    } else if (targetType != null && objectFactory.isCollection(targetType)) {
      value = objectFactory.create(targetType);
      MetaObject metaObject = configuration.newMetaObject(value);
      metaObject.addAll(list);
      /*如果是数组，那么新建要给数组，然后遍历给就行了*/
    } else if (targetType != null && targetType.isArray()) {
      Class<?> arrayComponentType = targetType.getComponentType();
      Object array = Array.newInstance(arrayComponentType, list.size());
      /*是否是原始的数组--什么叫原始的数组*/
      if (arrayComponentType.isPrimitive()) {
        for (int i = 0; i < list.size(); i++) {
          Array.set(array, i, list.get(i));
        }
        value = array;
      } else {
        value = list.toArray((Object[])array);
      }
      /*再不行就直接拿第一个就行*/
    } else {
      if (list != null && list.size() > 1) {
        throw new ExecutorException("Statement returned more than one row, where no more than one was expected.");
      } else if (list != null && list.size() == 1) {
        value = list.get(0);
      }
    }
    return value;
  }
}
