package com.eplugger.core.query;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统查询对象
 *
 * @auther: SunAFei
 * @date: 2018/5/19 13:55
 */
public class QueryCriteria<T> implements Specification<T> {
    private List<Criterion> criterions = new ArrayList<Criterion>();

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        // 默认加入创建人的条件
        // criterions.add(new Expression("createUserID", "=", UserDetailsUtils.getUserDetails().getUserId()));
        List<Predicate> predicates = new ArrayList<Predicate>();
        for (Criterion c : criterions) {
            predicates.add(c.toPredicate(root, query, builder));
        }
        // 将所有条件用 and 联合起来
        if (predicates.size() > 0) {
            return builder.and(predicates.toArray(new Predicate[predicates.size()]));
        }
        return builder.conjunction();
    }

    public void add(Criterion criterion) {
        if (criterion != null) {
            criterions.add(criterion);
        }
    }
}