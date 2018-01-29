package net.ccfish.talk.admin.jpa;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * 条件接口 <br>
 * 用户提供条件表达式接口
 * 
 * @author 袁贵
 * @version 1.0
 * @since 1.0
 */
public interface Criterion {
    
    /** 操作类型 */
    public enum Operator {
        EQ, NE, LIKE, GT, LT, GTE, LTE, AND, OR, ISNULL, NOTNULL, RLIKE, LLIKE
    }

    /**
     * 条件表达式描述（断言）
     * @param root 根
     * @param query 条件
     * @param builder 编译类
     * @return 表达式描述
     * @since  1.0
     */
    public Predicate toPredicate(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder builder);
}
