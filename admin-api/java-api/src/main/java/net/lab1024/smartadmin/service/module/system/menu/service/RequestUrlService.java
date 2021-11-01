package net.lab1024.smartadmin.service.module.system.menu.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
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
import net.lab1024.smartadmin.service.common.constant.CommonConst;
import net.lab1024.smartadmin.service.module.system.menu.domain.vo.RequestUrlVO;
import net.lab1024.smartadmin.service.common.util.SmartStringUtil;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 分离前后台权限URL
 *
 * @author lihaifan
 * @date 2021/9/1 20:14
 */
@Service
public class RequestUrlService {

    /**
     * 系统所有requestUrl
     */
    private CopyOnWriteArrayList<RequestUrlVO> requestUrlVOS = Lists.newCopyOnWriteArrayList();

    @Autowired
    private WebApplicationContext applicationContext;

    @PostConstruct
    public void initAllUrl() {
        this.requestUrlVOS.clear();

        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        //获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
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
                RequestUrlVO requestUrlVO = new RequestUrlVO();
                requestUrlVO.setUrl(url);
                requestUrlVO.setName(name);
                requestUrlVO.setComment(methodComment);
                this.requestUrlVOS.add(requestUrlVO);
            }

        });
    }

    private Set<String> getUrlSet(Set<String> patterns) {
        Set<String> urlSet = Sets.newHashSet();
        for (String url : patterns) {
            for (String ignoreUrl : CommonConst.CommonCollection.IGNORE_URL_MAPPING) {
                if (url.startsWith(ignoreUrl)) {
                    urlSet.add(url.substring(ignoreUrl.length() - 1));
                } else {
                    urlSet.add(url);
                }
            }
        }
        return urlSet;
    }

    public List<RequestUrlVO> getPrivilegeList() {
        return this.requestUrlVOS;
    }

}
