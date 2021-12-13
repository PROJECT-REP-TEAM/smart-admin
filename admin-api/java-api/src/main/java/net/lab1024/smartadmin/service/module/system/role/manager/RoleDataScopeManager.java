package net.lab1024.smartadmin.service.module.system.role.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import net.lab1024.smartadmin.service.common.code.UserErrorCode;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.common.util.SmartBeanUtil;
import net.lab1024.smartadmin.service.module.system.role.dao.RoleDataScopeDao;
import net.lab1024.smartadmin.service.module.system.role.domain.entity.RoleDataScopeEntity;
import net.lab1024.smartadmin.service.module.system.role.domain.form.RoleDataScopeUpdateForm;
import net.lab1024.smartadmin.service.module.system.role.domain.vo.RoleDataScopeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 
 * [  ]
 * 
 * @author 罗伊
 * @date
 */
@Service
public class RoleDataScopeManager extends ServiceImpl<RoleDataScopeDao,RoleDataScopeEntity> {

}
