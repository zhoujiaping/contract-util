package com.sirenia.contract.contract.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.core.env.ConfigurablePropertyResolver;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySourcesPropertyResolver;

public class PropResolverUtils {
    public static <T> T resolve(T t,Object source){
        JSONObject map = (JSONObject) JSONObject.toJSON(source);
        map.put("source",source );
        MapPropertySource propSrc = new MapPropertySource("map",map);
        MutablePropertySources pss = new MutablePropertySources();
        ConfigurablePropertyResolver resolver = new PropertySourcesPropertyResolver(pss);
        resolver.setPlaceholderPrefix("{{");
        resolver.setPlaceholderSuffix("}}");
        pss.addFirst(propSrc);
        return JSONObject.parseObject(resolver.resolvePlaceholders(JSON.toJSONString(t)),(Class<T>)t.getClass());
    }
}
