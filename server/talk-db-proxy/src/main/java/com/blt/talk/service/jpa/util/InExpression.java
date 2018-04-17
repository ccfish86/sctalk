package com.blt.talk.service.jpa.util;

import java.util.Collection;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
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
public class InExpression implements Criterion {
    
    /** 逻辑表达式中包含的表达式 */
    private Criterion criterion;
    /** 值 */
    private Collection<? extends Object> value;

    /**
     * 逻辑条件表达式构造方法
     * @param criterion 条件表达式
     * @param value 值
     */
    public InExpression(Criterion criterion, Collection<? extends Object> value) {
        this.criterion = criterion;
        this.value = value;
    }

    @Override
    public Predicate toPredicate(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        In<Object> in = (In<Object>)this.criterion.toPredicate(root, query, builder);
        for (Object obj: this.value) {
            in.value(obj);
        }
        return in;
    }
}
