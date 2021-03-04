package com.eplugger.utils;

import com.aspose.words.*;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 文本替换回调
 *
 * date 2016年8月11日 上午10:50:22
 * @author zhanghaoji
 */
public class ReplacingCallback implements IReplacingCallback {
    private static final Logger LOG = LoggerFactory.getLogger(ReplacingCallback.class);

    private Object val;

    public ReplacingCallback(Object val) {
        this.val = val;
    }

    @Override
    public int replacing(ReplacingArgs ra) throws Exception {
        String p = StringUtils.trim(ra.getMatch().group(1));
        if (StringUtils.isNotBlank(p)) {
            String[] levelPropertyStr = StringUtils.split(p, ".");
            Object vo = this.val;
            Object prop = null;

            boolean isListFill = false;// 是否集合填充（对应表格多行单元格）
            for (int i = 0; i < levelPropertyStr.length; i++) {
                String lp = levelPropertyStr[i];
                try {
                    prop = PropertyUtils.getProperty(vo, lp);
                } catch (Exception e) {
                    LOG.debug("找不到指定属性 obj.class:{}, property:{}", vo.getClass().getName(), lp);
                    prop = null;
                }
                if (prop instanceof List && i < (levelPropertyStr.length - 1)) {// 还存在下一层（集合后只支持1级，即
                    // 不能写
                    // members.persons.name）
                    Cell cell = getParent(ra.getMatchNode(), Cell.class);
                    lineUp(cell.getParentRow(), ((List<?>) prop).size());// 补行
                    if (cell != null) {
                        cellFillDown(cell, (List<?>) prop, StringUtils
                                .join(ArrayUtils.subarray(levelPropertyStr, i + 1, levelPropertyStr.length), "."));
                        isListFill = true;
                        break;
                    }
                }
            }
            if (isListFill) {
                return ReplaceAction.SKIP;
            } else {
                String viewVal = getVal(vo, p);
                ra.setReplacement(viewVal);
                return ReplaceAction.REPLACE;
            }

        } else {
            ra.setReplacement("");// 无内容则进行清理 ,对应 ^{}
            return ReplaceAction.REPLACE;
        }
    }

    /**
     * 向上寻找指定的父节点
     * @param n 节点
     * @param cls 父节点名称
     * @return 找不到返回null
     */
    private static <T> T getParent(Node n,Class<T> cls){
        Node parentNode = n.getParentNode();
        while(parentNode != null && !StringUtils.equals(parentNode.getClass().getName(), cls.getName())){
            parentNode = parentNode.getParentNode();
        }
        return parentNode == null ? null : cls.cast(parentNode);
    }

    /**
     * 为word中的表格补充行,会在当前行上进行扩展满足最大行数要求
     * @param firstRow 首行
     * @param maxSize 最大行数
     */
    private static void lineUp(Row firstRow,int maxSize){
        int colCount = firstRow.getCells().getCount();
        Table t = getParent(firstRow, Table.class);
        Row newRow = null;
        Node curRow = firstRow;
        Node nextRow = null;
        for (int i = 1; i < maxSize; i++) {//从第二行开始计算
            nextRow = curRow.getNextSibling();
            if(nextRow == null || !(nextRow instanceof Row) || Row.class.cast(nextRow).getCount() != colCount){
                if(newRow == null){
                    newRow = cloneNewRowAndClear(firstRow);
                }
                int curRowIndex = t.indexOf(curRow);
                t.getRows().insert(curRowIndex + 1, newRow);
                nextRow = newRow;
            }
            if(nextRow != null){
                curRow = nextRow;
            }
        }
    }

    /**
     * 克隆一个表格行，并清空
     * @return 新的表行
     */
    private static Row cloneNewRowAndClear(Row r){
        Row newRow = (Row)r.deepClone(true);
        for (int i = 0; i < newRow.getCount(); i++) {
            Cell c = newRow.getCells().get(i);
            c.getFirstParagraph().removeAllChildren();
        }
        return newRow;
    }

    /**
     * 表格的单元格向下填充（下一行的同位置单元格） 一直填到无数据或不是单元格的情况。
     * @param c 单元格
     * @param ls 集合
     * @param levelProperty 集合的层级属性
     */
    private void cellFillDown(Cell c,List<?> ls,final String levelProperty){
        int cellIndex = c.getParentRow().indexOf(c);
        int colCount = c.getParentRow().getCount();
        Cell operCell = null;
        for (Object vo : ls) {
            if(operCell == null){//首行cell
                operCell = c;
            }else{//下一行cell
                Node n = operCell.getParentRow().getNextSibling();
                if(n == null || !(n instanceof Row) || Row.class.cast(n).getCount() != colCount){
                    break;
                }
                operCell = ((Row)n).getCells().get(cellIndex);
                if(operCell == null){
                    break;
                }
            }
            try {
                PropertyUtils.getProperty(vo,levelProperty);
                String viewVal = getVal(vo,levelProperty);
                operCell.getFirstParagraph().removeAllChildren();
                operCell.getFirstParagraph().appendChild(new Run(operCell.getDocument(), viewVal));//赋值
            } catch (Exception e) {
                LOG.debug("找不到指定属性 obj.class:{}, property:{}",vo.getClass().getName(),levelProperty);
                operCell.getFirstParagraph().removeAllChildren();//清空
                return;//不再继续寻找下一行
            }
        }
    }

    /**
     * 获取业务对象Object的属性值
     * @param obj 业务对象
     * @param propertyName 对象属性，不支持多个层级（如person.name）
     * @return 获取值
     */
    private String getVal(Object obj, String propertyName) {
        Object value = null;
        try {
            value = PropertyUtils.getProperty(obj, propertyName);
        } catch (Exception e) {

        }
        String v = value == null ? "" : String.valueOf(value);
        return v;
    }

}
