package net.lab1024.smartadmin.service.module.system.menu.service;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import net.lab1024.smartadmin.service.common.util.SmartStringUtil;
import net.lab1024.smartadmin.service.module.system.menu.domain.vo.MenuUrlVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * 分离前后台权限URL
 *
 * @author lihaifan
 * @date 2021/9/1 20:14
 */
@Service
public class MenuUrlService {

    private static final Set<String> IGNORE_URL = ImmutableSet.of("/swagger", "Excel");
    /**
     * 系统所有requestUrl
     */
    private List<MenuUrlVO> urlList = null;

    @Autowired
    private WebApplicationContext applicationContext;

    @PostConstruct
    public synchronized void initAllUrl() {
        if (urlList != null) {
            return;
        }

        ArrayList<MenuUrlVO> tempUrlList = new ArrayList<>();

        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        // 获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        // 校验是否存在相同的 controller和name
        HashMap<String, HandlerMethod> sameNameMap = new HashMap<>();

        map.forEach((info, handlerMethod) -> {
            //只对Rest 服务进行权限验证
            RestController restAnnotation = AnnotationUtils.findAnnotation(handlerMethod.getMethod().getDeclaringClass(), RestController.class);
            if (restAnnotation == null) {
                ResponseBody responseBody = handlerMethod.getMethod().getAnnotation(ResponseBody.class);
                if (responseBody == null) {
                    return;
                }
            }
            //获取url的Set集合，一个方法可能对应多个url
            Set<String> patterns = info.getPatternsCondition().getPatterns();
            if (CollectionUtils.isEmpty(patterns)) {
                return;
            }
            String className = handlerMethod.getMethod().getDeclaringClass().getName();
            String methodName = handlerMethod.getMethod().getName();
            List<String> list = SmartStringUtil.splitConvertToList(className, "\\.");
            String controllerName = list.get(list.size() - 1);
            String name = controllerName + "." + methodName;

            HandlerMethod sameNameHandlerMethod = sameNameMap.get(name);
            if (sameNameHandlerMethod != null) {
                throw new ExceptionInInitializerError("存在相同的Controller名字和方法名字，这样会导致权限的perms存在重复问题: " + handlerMethod + " : " + sameNameHandlerMethod);
            }
            sameNameMap.put(name, handlerMethod);

            ApiOperation apiOperation = handlerMethod.getMethod().getAnnotation(ApiOperation.class);
            String methodComment = null;
            if (apiOperation != null) {
                methodComment = apiOperation.value();
            } else {
                ApiModelProperty apiModelProperty = handlerMethod.getMethod().getAnnotation(ApiModelProperty.class);
                if (apiModelProperty != null) {
                    methodComment = apiModelProperty.value();
                } else {
                    methodComment = handlerMethod.getMethod().getName();
                }
            }
            Set<String> urlSet = this.getUrlSet(patterns);
            for (String url : urlSet) {
                MenuUrlVO menuUrlVO = new MenuUrlVO();
                menuUrlVO.setUrl(url);
                menuUrlVO.setName(name);
                menuUrlVO.setComment(methodComment);
                tempUrlList.add(menuUrlVO);
            }
        });

        this.urlList = Collections.unmodifiableList(tempUrlList);
    }

    private Set<String> getUrlSet(Set<String> patterns) {
        Set<String> urlSet = Sets.newHashSet();
        for (String url : patterns) {
            for (String ignoreUrl : IGNORE_URL) {
                if (url.startsWith(ignoreUrl)) {
                    urlSet.add(url.substring(ignoreUrl.length() - 1));
                } else {
                    urlSet.add(url);
                }
            }
        }
        return urlSet;
    }

    public List<MenuUrlVO> getMenuUrlList() {
        return this.urlList;
    }

}
