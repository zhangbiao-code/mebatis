package com.biao.mebatis.v2.session;

import com.biao.mebatis.v2.Test;
import com.biao.mebatis.v2.annotation.Select;
import com.biao.mebatis.v2.binding.MappedStatement;
import com.biao.mebatis.v2.binding.MapperRegistry;
import com.biao.mebatis.v2.executor.CachingExecutor;
import com.biao.mebatis.v2.executor.Executor;
import com.biao.mebatis.v2.executor.SimpleExecutor;
import com.biao.mebatis.v2.plugin.Interceptor;
import com.biao.mebatis.v2.plugin.InterceptorChain;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class Configuration {

    public static final MapperRegistry MAPPER_REGISTRY = new MapperRegistry();

    public static final ResourceBundle sqlMappings;

    public static final ResourceBundle properties;

    private InterceptorChain interceptorChain = new InterceptorChain(); // 插件


    private List<Class<?>> mapperList = new ArrayList<>();

    private List<String> classPaths = new ArrayList<>();

    public Map<String, MappedStatement> mappedStatements = new HashMap<>();

    static {
        sqlMappings = ResourceBundle.getBundle("userMapper");
        properties = ResourceBundle.getBundle("mebatis");
    }

    /**
     * 初始化时解析全局配置文件
     */
    public Configuration() {

        // 1. 解析mapper文件
        for (String key : sqlMappings.keySet()) {
            String statementId = sqlMappings.getString(key).split("--")[0];
            String pojoPath = sqlMappings.getString(key).split("--")[1];
            try {
                Class mapper = Class.forName(key.substring(0, key.lastIndexOf(".")));
                Class pojo = Class.forName(pojoPath);
                MAPPER_REGISTRY.addMapper(mapper, pojo);
                mappedStatements.put(key, new MappedStatement(statementId));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        // 2. 解析mapper接口配置,扫描注册
        String mapperPath = properties.getString("mapper.path");
        scanPackage(mapperPath);
        for (Class<?> mapper : mapperList) {
            parsingClass(mapper);
        }

        // 3.解析插件，可配置多个插件
        String pluginPathValue = properties.getString("plugin.path");
        String[] pluginPaths = pluginPathValue.split(",");
        if (pluginPaths != null) {
            // 将插件添加到interceptorChain中
            for (String plugin : pluginPaths) {
                Interceptor interceptor = null;
                try {
                    interceptor = (Interceptor) Class.forName(plugin).newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                interceptorChain.addInterceptor(interceptor);
            }
        }
    }

    public <T> T getMapper(Class<T> clazz, SqlSession sqlSession) {
        return MAPPER_REGISTRY.getMapper(clazz, sqlSession);
    }

    /**
     * 根据全局配置文件中配置的接口路径,扫描全部的接口
     *
     * @param mapperPath
     */
    private void scanPackage(String mapperPath) {

        String classPath = Test.class.getResource("/").getPath();
        mapperPath = mapperPath.replace(".", File.separator);
        String mainPath = classPath + mapperPath;
        doPath(new File(mainPath));

        for (String className : classPaths) {
            className = className.replace(classPath.replace("/", "\\").replaceFirst("\\\\", ""), "").replace("\\", ".").replace(".class", "");
            Class<?> clazz = null;
            try {
                clazz = Class.forName(mapperPath.replace("/", ".") + "." + className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (clazz.isInterface()) {
                mapperList.add(clazz);
            }
        }
    }

    /**
     * 获取文件或文件夹下所有的.class文件
     *
     * @param file
     */
    private void doPath(File file) {

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                doPath(f);
            }
        } else {
            if (file.getName().endsWith(".class")) {
                classPaths.add(file.getName());
            }
        }
    }

    /**
     * 创建执行器，当开启缓存时使用缓存装饰
     * 当配置插件时，使用插件代理
     *
     * @return
     */
    public Executor newExecutor() {
        Executor executor = null;
        if (properties.getString("cache.enabled").equals("true")) {
            executor = new CachingExecutor(new SimpleExecutor());
        } else {
            executor = new SimpleExecutor();
        }

        // 目前只拦截了Executor，所有的插件都对Executor进行代理，没有对拦截类和方法签名进行判断
        if (interceptorChain.hasPlugin()) {
            return (Executor) interceptorChain.pluginAll(executor);
        }
        return executor;
    }

    /**
     * 解析接口上配置的注解
     *
     * @param mapper
     */
    private void parsingClass(Class<?> mapper) {

        // 解析方法上的注解
        Method[] methods = mapper.getMethods();
        for (Method method : methods) {
            // 解析@Select注解的SQL语句
            if (method.isAnnotationPresent(Select.class)) {
                for (Annotation annotation : method.getDeclaredAnnotations()) {
                    if (annotation.annotationType().equals(Select.class)) {
                        // 注册接口类型+方法名和SQL语句的映射关系
                        String statement = method.getDeclaringClass().getName() + "." + method.getName();
                        mappedStatements.put(statement, new MappedStatement(((Select) annotation).value()));
                    }
                }
            }
        }
    }
}
