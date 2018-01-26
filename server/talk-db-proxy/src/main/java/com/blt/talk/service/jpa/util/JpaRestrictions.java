package com.blt.talk.service.jpa.util;

import java.util.Collection;

import org.hibernate.criterion.MatchMode;
import org.springframework.util.StringUtils;

import com.blt.talk.service.jpa.util.Criterion.Operator;

/**
 * 条件构造器 <br>
 * 用于创建条件表达式，多表关联查询请手写HQL处理
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
public class JpaRestrictions {

    /**
     * 等于 <br>
     * 不支持1：N的子表查询
     * 
     * @param fieldName 字段名
     * @param value 字段值
     * @param ignoreNull 是否忽然空值
     * @return 条件表达式
     */
    public static SimpleExpression eq(String fieldName, Object value, boolean ignoreNull) {
        if (StringUtils.isEmpty(value))
            return null;
        return new SimpleExpression(fieldName, value, Operator.EQ);
    }

    /**
     * 不等于
     * 
     * @param fieldName 字段名
     * @param value 字段值
     * @param ignoreNull 是否忽然空值
     * @return 条件表达式
     */
    public static SimpleExpression ne(String fieldName, Object value, boolean ignoreNull) {
        if (StringUtils.isEmpty(value))
            return null;
        return new SimpleExpression(fieldName, value, Operator.NE);
    }

    /**
     * 模糊匹配
     * 
     * @param fieldName 字段名
     * @param value 字段值
     * @param ignoreNull 是否忽然空值
     * @return 条件表达式
     */
    public static SimpleExpression like(String fieldName, String value, boolean ignoreNull) {
        if (StringUtils.isEmpty(value))
            return null;
        return new SimpleExpression(fieldName, value, Operator.LIKE);
    }

    /**
     * 模糊匹配
     * 
     * @param fieldName 字段名
     * @param value 字段值
     * @param matchMode 匹配类型
     * @param ignoreNull 是否忽然空值
     * @return 条件表达式
     * @return
     */
    public static SimpleExpression like(String fieldName, String value, MatchMode matchMode, boolean ignoreNull) {
        if (StringUtils.isEmpty(value))
            return null;
        SimpleExpression expression;
        switch (matchMode) {
            case START:
                expression = new SimpleExpression(fieldName, value, Operator.LLIKE);
                break;
            case END:
                expression = new SimpleExpression(fieldName, value, Operator.RLIKE);
                break;
            case ANYWHERE:
                expression = new SimpleExpression(fieldName, value, Operator.LIKE);
                break;
            default:
                expression = new SimpleExpression(fieldName, value, Operator.LIKE);
                break;
        }
        
        return expression;
    }

    /**
     * 大于
     * 
     * @param fieldName 字段名
     * @param value 字段值
     * @param ignoreNull 是否忽然空值
     * @return 条件表达式
     */
    public static SimpleExpression gt(String fieldName, Object value, boolean ignoreNull) {
        if (StringUtils.isEmpty(value))
            return null;
        return new SimpleExpression(fieldName, value, Operator.GT);
    }

    /**
     * 小于
     * 
     * @param fieldName 字段名
     * @param value 字段值
     * @param ignoreNull 是否忽然空值
     * @return 条件表达式
     */
    public static SimpleExpression lt(String fieldName, Object value, boolean ignoreNull) {
        if (StringUtils.isEmpty(value))
            return null;
        return new SimpleExpression(fieldName, value, Operator.LT);
    }

    /**
     * 小于等于
     * 
     * @param fieldName 字段名
     * @param value 字段值
     * @param ignoreNull 是否忽然空值
     * @return 条件表达式
     */
    public static SimpleExpression lte(String fieldName, Object value, boolean ignoreNull) {
        if (StringUtils.isEmpty(value))
            return null;
        return new SimpleExpression(fieldName, value, Operator.LTE);
    }

    /**
     * 大于等于
     * 
     * @param fieldName 字段名
     * @param value 字段值
     * @param ignoreNull 是否忽然空值
     * @return 条件表达式
     */
    public static SimpleExpression gte(String fieldName, Object value, boolean ignoreNull) {
        if (StringUtils.isEmpty(value))
            return null;
        return new SimpleExpression(fieldName, value, Operator.GTE);
    }

    /**
     * 并且
     * 
     * @param criterions 多条件
     * @return 条件表达式（条件1 AND 条件2 AND 条件3...）
     */
    public static LogicalExpression and(Criterion... criterions) {
        return new LogicalExpression(criterions, Operator.AND);
    }

    /**
     * 或者
     * 
     * @param criterions 多条件
     * @return 条件表达式（条件1 OR 条件2 OR 条件3...）
     */
    public static LogicalExpression or(Criterion... criterions) {
        return new LogicalExpression(criterions, Operator.OR);
    }

    /**
     * 包含于
     * 
     * @param fieldName 字段名
     * @param value 字段值
     * @param ignoreNull 是否忽然空值
     * @return 条件表达式
     */
    @SuppressWarnings("rawtypes")
    public static LogicalExpression in(String fieldName, Collection value, boolean ignoreNull) {
        if (ignoreNull && (value == null || value.isEmpty())) {
            return null;
        }
        SimpleExpression[] ses = new SimpleExpression[value.size()];
        int i = 0;
        for (Object obj : value) {
            ses[i] = new SimpleExpression(fieldName, obj, Operator.EQ);
            i++;
        }
        return new LogicalExpression(ses, Operator.OR);
    }

    /**
     * 空
     * 
     * @param fieldName 字段名
     * @return 条件表达式
     */
    public static SimpleExpression isNull(String fieldName) {
        return new SimpleExpression(fieldName, Operator.ISNULL);
    }

    /**
     * 非空
     * 
     * @param fieldName 字段名
     * @return 条件表达式
     */
    public static SimpleExpression isNotNull(String fieldName) {
        return new SimpleExpression(fieldName, Operator.NOTNULL);
    }

    /**
     * 模糊匹配
     * 
     * @param fieldName 字段名
     * @param value 字段值
     * @param ignoreNull 是否忽然空值
     * @return 条件表达式
     */
    public static SimpleExpression rlike(String fieldName, String value, boolean ignoreNull) {
        if (StringUtils.isEmpty(value))
            return null;
        return new SimpleExpression(fieldName, value, Operator.RLIKE);
    }

    /**
     * 模糊匹配
     * 
     * @param fieldName 字段名
     * @param value 字段值
     * @param ignoreNull 是否忽然空值
     * @return 条件表达式
     */
    public static SimpleExpression llike(String fieldName, String value, boolean ignoreNull) {
        if (StringUtils.isEmpty(value))
            return null;
        return new SimpleExpression(fieldName, value, Operator.LLIKE);
    }
}
