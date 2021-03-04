package com.eplugger.core.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.*;
import java.util.Collection;

/**
 * 条件表达式
 *
 * @auther: SunAFei
 * @date: 2018/5/19 13:47
 */
@Data
@AllArgsConstructor
public class Expression<T> implements Criterion<T> {
    private String name;    //属性名
    private String exp;     //计算符
    private Object val;   //对应值

    public Expression(String name, String exp, Object[] val) {
        this(name, exp, (Object)val);
    }

    public Expression(String name, String exp, Collection<Object> val) {
        this(name, exp, (Object)val);
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Path expression = null;
        // eg 对象查询条件为employee.unit.id 则必须为expression.get(employee).get(unit).get(id)
        if (name.contains(".")) {
            String[] names = StringUtils.split(name, ".");
            expression = root.get(names[0]);
            for (int i = 1; i < names.length; i++) {
                expression = expression.get(names[i]);
            }
        } else {
            expression = root.get(name);
        }
        switch (exp) {
            case EQ:
                return builder.equal(expression, val);
            case NE:
                return builder.notEqual(expression, val);
            case IN:
                return expression.in(val);
            case NOTIN:
                return expression.in(val).not();
            case LIKE:
                return builder.like(expression, "%" + val + "%");
            case NOTLIKE:
                return builder.notLike(expression, "%" + val + "%");
            case LT:
                return builder.lessThan(expression, (Comparable) val);
            case GT:
                return builder.greaterThan(expression, (Comparable) val);
            case LTE:
                return builder.lessThanOrEqualTo(expression, (Comparable) val);
            case GTE:
                return builder.greaterThanOrEqualTo(expression, (Comparable) val);
            case RANGEIN:
                Object[] rangeinVal = (Object[]) val;
                if (rangeinVal[0] != null && rangeinVal[1] != null) {
                    return builder.between(expression, (Comparable)rangeinVal[0], (Comparable)rangeinVal[1]);
                } else if (rangeinVal[0] != null) {
                    return builder.lessThanOrEqualTo(expression, (Comparable) rangeinVal[0]);
                } else if (rangeinVal[1] != null) {
                    return builder.greaterThanOrEqualTo(expression, (Comparable) rangeinVal[1]);
                } else {
                    return null;
                }
            case RANGE:
                Object[] rangeVal = (Object[]) val;
                if (rangeVal[0] != null && rangeVal[1] != null) {
                    return builder.and(builder.lessThan(expression, (Comparable) rangeVal[0]), builder.greaterThan(expression, (Comparable) rangeVal[1]));
                } else if (rangeVal[0] != null) {
                    return builder.lessThan(expression, (Comparable) rangeVal[0]);
                } else if (rangeVal[1] != null) {
                    return builder.greaterThan(expression, (Comparable) rangeVal[1]);
                }
            default:
                return null;
        }
    }
}
