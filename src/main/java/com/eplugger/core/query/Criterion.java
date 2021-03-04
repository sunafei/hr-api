package com.eplugger.core.query;

import org.springframework.data.jpa.domain.Specification;

/**
 * 条件表达式接口
 *
 * @auther: SunAFei
 * @date: 2018/5/19 13:47
 */
public interface Criterion<T> extends Specification<T> {
    final static String EQ = "=";                // 等于
    final static String NE = "!=";               // 不等于
    final static String LIKE = "like";          // like 自动加入%
    final static String NOTLIKE = "notLike";   // notlike 自动加入%
    final static String IN = "in";              // in 包含
    final static String NOTIN = "notIn";       // notin 不包含
    final static String LT = "<";               // 小于
    final static String GT = ">";               // 大于
    final static String LTE = "<=";             // 小于等于
    final static String GTE = ">=";             // 大于等于
    final static String RANGE = "range";       // 范围 不包含两边
    final static String RANGEIN = "rangeIn";  // 范围 包含两边

    final static String OR = "or";
    final static String AND = "and";
}
