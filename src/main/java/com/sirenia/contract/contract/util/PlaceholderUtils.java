package com.sirenia.contract.contract.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.ValueFilter;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PlaceholderUtils {
    private static final SpelExpressionParser parser = new SpelExpressionParser();
    private static final ParserContext context = new TemplateParserContext("{{","}}");
    private static final InheritableThreadLocal<Object[]> tl = new InheritableThreadLocal();
    private static final SerializeFilter filter = (ValueFilter) (object, name, value) -> {
        Object[] arr = tl.get();
        if(arr.length==2){
            Set<String> propSet = (Set<String>) arr[1];
            if(!propSet.contains(name)){
                return value;
            }
            if(!(value instanceof String)){//null instanceof String=>false
                return value;
            }
            String v = value.toString();
            Expression exp = parser.parseExpression(v,context);
            return exp.getValue(arr[0]);
        }else{
            String v = value.toString();
            Expression exp = parser.parseExpression(v,context);
            return exp.getValue(arr[0]);
        }
    };
    /**
     * 场景：
     * A模块提供服务service，B模块调用该服务service。
     * service需要一些参数，其中某些参数是需要配置的。我们配置在A模块。
     * 但是组装其他参数，需要用到这些配置（不知道为什么有这种耦合的设计）。
     * 有一种方案是提供一个获取配置的接口（需要额外调一次接口）。
     * 这里提供另一种方案，参数中允许使用占位符，A模块将占位符替换掉。
     * 在A的视角，他知道哪些参数他没提供，但是这些参数A模块提供了。
     *
     * @param src
     * @param values
     * @param props
     * @param <T>
     * @return
     */
    public static <T> T replace(T src, Object values, String... props){
        Set<String> propSet = new HashSet<>();
        propSet.addAll(Arrays.asList(props));
        try {
            tl.set(new Object[]{values,propSet});
            String json = JSON.toJSONString(src, filter);
            return JSONObject.parseObject(json,(Class<T>)src.getClass());
        }finally{
            tl.remove();
        }
    }
    public static <T> T replaceAll(T src, Object values){
        try {
            tl.set(new Object[]{values});
            String json = JSON.toJSONString(src, filter);
            return JSONObject.parseObject(json,(Class<T>)src.getClass());
        }finally{
            tl.remove();
        }
    }
}
