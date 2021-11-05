package net.lab1024.smartadmin.service.module.system.role.service;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import net.lab1024.smartadmin.service.common.code.UserErrorCode;
import net.lab1024.smartadmin.service.common.domain.ResponseDTO;
import net.lab1024.smartadmin.service.module.system.role.domain.entity.RoleDataScopeEntity;
import net.lab1024.smartadmin.service.module.system.role.dao.RoleDataScopeDao;
import net.lab1024.smartadmin.service.module.system.role.domain.form.RoleDataScopeUpdateForm;
import net.lab1024.smartadmin.service.module.system.role.domain.vo.RoleDataScopeVO;
import net.lab1024.smartadmin.service.common.util.SmartBeanUtil;

import java.util.List;

/**
 * @author zhuoda
 * @Date 2021-10-22
 */
@Service
public class RoleDataScopeService {

    @Autowired
    private RoleDataScopeDao roleDataScopeDao;


    /**
     * 获取某个角色的数据范围设置信息
     *
     * @param roleId
     * @return
     */
    public ResponseDTO<List<RoleDataScopeVO>> getRoleDataScopeList(Long roleId) {
        List<RoleDataScopeEntity> roleDataScopeEntityList = roleDataScopeDao.listByRoleId(roleId);
        if (CollectionUtils.isEmpty(roleDataScopeEntityList)) {
            return ResponseDTO.ok(Lists.newArrayList());
        }
        List<RoleDataScopeVO> roleDataScopeList = SmartBeanUtil.copyList(roleDataScopeEntityList, RoleDataScopeVO.class);
        return ResponseDTO.ok(roleDataScopeList);
    }

    /**
     * 批量设置某个角色的数据范围设置信息
     *
     * @param roleDataScopeUpdateForm
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> updateRoleDataScopeList(RoleDataScopeUpdateForm roleDataScopeUpdateForm) {
        List<RoleDataScopeUpdateForm.RoleUpdateDataScopeListFormItem> batchSetList = roleDataScopeUpdateForm.getDataScopeItemList();
        if (CollectionUtils.isEmpty(batchSetList)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "缺少配置信息");
        }
        List<RoleDataScopeEntity> roleDataScopeEntityList = SmartBeanUtil.copyList(batchSetList, RoleDataScopeEntity.class);
        roleDataScopeEntityList.forEach(e -> e.setRoleId(roleDataScopeUpdateForm.getRoleId()));
        roleDataScopeDao.deleteByRoleId(roleDataScopeUpdateForm.getRoleId());
        roleDataScopeDao.batchInsert(roleDataScopeEntityList);
        return ResponseDTO.ok();
    }
}
