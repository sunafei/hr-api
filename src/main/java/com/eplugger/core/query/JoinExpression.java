package com.eplugger.core.query;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * 联合表达式
 *
 * @auther: SunAFei
 * @date: 2018/5/19 10:21
 */
@NoArgsConstructor
@AllArgsConstructor
public class JoinExpression<T> implements Criterion<T> {
    private Criterion[] criterion;   // 连接条件 多个
    private String relation;        // 连接类型 and/or

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<Predicate>();
        for (int i = 0; i < this.criterion.length; i++) {
            predicates.add(this.criterion[i].toPredicate(root, query, builder));
        }
        switch (relation) {
            case OR:
                return builder.or(predicates.toArray(new Predicate[predicates.size()]));
            case AND:
                return builder.and(predicates.toArray(new Predicate[predicates.size()]));
            default:
                return null;
        }
    }
}
