package com.eplugger.utils;


import com.aspose.words.Document;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.regex.Pattern;


/**
 * Word操作工具类
 * @author Administrator
 *
 */
public class WordUtils {
    public final static Pattern REGEX_PROPERTY = Pattern.compile("\\^\\{(.*?)\\}");

    /**
     * 获得要操作的word文档对象
     * @param src 要操作的word文件
     * @return Document word文档
     * @throws Exception
     */
    public static Document getDoc(File src) throws Exception {
        Assert.isTrue(src.exists());
        return new Document(new FileInputStream(src));
    }

    /**
     * 查找匹配字符
     * @param doc Document
     * @param o Object
     * @return 替换后的文档 doc
     * @throws Exception
     */
    public static Document replaceStr4Obj(Document doc, final Object o){
        Assert.notNull(doc);
        Assert.notNull(o);
        try {
            ReplacingCallback rc = new ReplacingCallback(o);
            doc.getRange().replace(REGEX_PROPERTY, rc , false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
    }

    /**
     * 替换文字内容
     * @param doc Document
     * @param regex 要替换的文字
     * @param replacement 替换成什么
     * @return doc 替换后文件
     * @throws Exception
     */
    public static Document replaceStr(Document doc,String regex,String replacement) throws Exception{
        Assert.notNull(doc);
        Assert.notNull(regex);
        Assert.notNull(replacement);
        Pattern p = Pattern.compile(regex);
        doc.getRange().replace(p, replacement);
        return doc;
    }

}

