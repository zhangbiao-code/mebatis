package com.biao.mebatis.v2.binding;

import com.biao.mebatis.v2.session.Configuration;
import com.biao.mebatis.v2.session.SqlSession;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

public class MapperMethod {

    private final SqlCommand sqlCommand;

    private final MethodSignature method;

    public MapperMethod(Class<?> mapperInterface, Method method, Configuration configuration) {
        this.sqlCommand = new SqlCommand(configuration, mapperInterface, method);
        this.method = new MethodSignature(method);
    }

    public Object execute(SqlSession sqlSession, Object[] args) {
        Object result = null;
        switch (sqlCommand.type) {

            case INSERT: {
                break;
            }
            case DELETE: {
                break;
            }
            case UPDATE: {
                break;
            }
            case SELECT: {
                if (method.returnsMany) {

                } else if (method.returnsMap) {

                } else {
                    result = sqlSession.selectOne(sqlCommand.name, args, method.returnType);
                }
                break;
            }
            default:
                throw new RuntimeException("Unknown execution method for: " + sqlCommand.name);
        }
        return result;
    }

    public static class SqlCommand {

        private final String name;

        private final SqlCommandType type;

        public SqlCommand(Configuration configuration, Class<?> mapperInterface, Method method) {
            String methodName = method.getName();
            MappedStatement ms = resolveMappedStatement(mapperInterface, methodName, configuration);
            this.name = ms.getId();
            this.type = ms.getType();
        }

        private MappedStatement resolveMappedStatement(Class<?> mapperInterface, String methodName, Configuration configuration) {
            String statementId = mapperInterface.getName() + "." + methodName;
            return configuration.mappedStatements.get(statementId);
        }
    }

    public static class MethodSignature {

        private final boolean returnsMany;
        private final boolean returnsMap;
        private final Class<?> returnType;

        public MethodSignature(Method method) {
            Type resolvedReturnType = method.getGenericReturnType();
            if (resolvedReturnType instanceof Class) {
                this.returnType = (Class) resolvedReturnType;
            } else if (resolvedReturnType instanceof ParameterizedType) {
                this.returnType = (Class) ((ParameterizedType) resolvedReturnType).getRawType();
            } else {
                this.returnType = method.getReturnType();
            }

            this.returnsMap = isMap(this.returnType);
            this.returnsMany = isCollection(this.returnType) || this.returnType.isArray();
        }

        public <T> boolean isCollection(Class<T> type) {
            return Collection.class.isAssignableFrom(type);
        }

        public <T> boolean isMap(Class<T> type) {
            return Map.class.isAssignableFrom(type);
        }
    }
}
