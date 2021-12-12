package org.apache.ibatis.domain.blog.mappers;

import org.apache.ibatis.domain.blog.TestEntity;

import java.util.List;

public interface TestMapper {
  List<TestEntity> selectText();
}
