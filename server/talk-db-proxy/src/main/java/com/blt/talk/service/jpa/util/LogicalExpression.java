package com.blt.talk.service.jpa.util;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * 逻辑条件表达式 用于复杂条件时使用，如但属性多对应值的OR查询等 
 * 
 * @author 袁贵
 * @version 1.0
 * @since  1.0
 */
public class LogicalExpression implements Criterion {
    
    /** 逻辑表达式中包含的表达式 */
    private Criterion[] criterion;
    /** 计算符 */
    private Operator operator;

    /**
     * 逻辑条件表达式构造方法
     * @param criterions 条件表达式
     * @param operator 计算符
     */
    public LogicalExpression(Criterion[] criterions, Operator operator) {
        this.criterion = criterions;
        this.operator = operator;
    }

    @Override
    public Predicate toPredicate(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<Predicate>();
        for (int i = 0; i < this.criterion.length; i++) {
            predicates.add(this.criterion[i].toPredicate(root, query, builder));
        }
        switch (operator) {
        case OR:
            return builder.or(predicates.toArray(new Predicate[predicates.size()]));
        default:
            return null;
        }
    }
}
