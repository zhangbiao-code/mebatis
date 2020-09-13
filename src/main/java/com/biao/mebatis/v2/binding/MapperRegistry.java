package com.biao.mebatis.v2.binding;

import com.biao.mebatis.v2.session.SqlSession;

import java.util.HashMap;
import java.util.Map;

/**
 * 维护接口和工厂类的关系，用于获取MapperProxy代理对象
 * 工厂类指定了POJO类型，用于处理结果集返回
 */
public class MapperRegistry {

    // 接口和工厂类映射关系
    private final Map<Class<?>, MapperProxyFactory> knownMappers = new HashMap<>();

    /**
     * 在Configuration中解析接口上的注解时，存入接口和工厂类的映射关系
     * 此处传入pojo类型，是为了最终处理结果集的时候将结果转换为POJO类型
     */
    public <T> T getMapper(Class<T> clazz, SqlSession sqlSession) {
        MapperProxyFactory mapperProxyFactory = knownMappers.get(clazz);
        if (mapperProxyFactory == null) {
            throw new RuntimeException("Type : " + clazz + " can not find");
        }
        return (T) mapperProxyFactory.newInstance(sqlSession);
    }

    public <T> void addMapper(Class<T> mapper, Class pojo) {
        knownMappers.put(mapper, new MapperProxyFactory(mapper, pojo));
    }
}
