package com.xupt.outpatientms.mapper.typeHandler;

import com.xupt.outpatientms.enumeration.BaseEnum;
import com.xupt.outpatientms.enumeration.GenderEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@MappedJdbcTypes(JdbcType.TINYINT)
@MappedTypes({GenderEnum.class})
public class IntegerEnumHandler<T extends BaseEnum> extends BaseTypeHandler<T> {

    private static final Logger logger = LoggerFactory.getLogger(IntegerEnumHandler.class);

    private Map<Integer,T> enumTypeMap = new HashMap<>();

    public IntegerEnumHandler(){ }

    public IntegerEnumHandler(Class<T> type){
        logger.info("invoke construct method.  class<E>={} === Class<E> type={}",type.getEnumConstants(),type);
        T[] enumConstants = type.getEnumConstants();
        if (enumConstants == null){
            throw new IllegalArgumentException("no such enum");
        }
        for (T e : enumConstants){
            enumTypeMap.put(e.value(),e);
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, T t, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, t.value());
    }

    @Override
    public T getNullableResult(ResultSet resultSet, String s) throws SQLException {
        logger.info("invoke==>getNullableResult:columnName={}",s);
        return enumTypeMap.get(resultSet.getInt(s));
    }

    @Override
    public T getNullableResult(ResultSet resultSet, int i) throws SQLException {
        logger.info("invoke==>getNullableResult:columnIndex={},resultSet={}",i,resultSet);
        return enumTypeMap.get(resultSet.getInt(i));
    }

    @Override
    public T getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        logger.info("invoke==>getNullableResult:columnIndex={},callableStatement={}",i,callableStatement);
        return enumTypeMap.get(callableStatement.getInt(i));
    }
}
