<!--
 * @Author: zhuoda
 * @Date: 2021-08-11 14:11:28
 * @LastEditTime: 2021-12-13
 * @LastEditors: zhuoda
 * @Description: 菜单新增编辑抽屉
 * @FilePath: /smart-admin/src/views/system/menu/components/menu-operate-modal.vue
-->
<template>
  <el-drawer
    :title="form.menuId ? '编辑' : '添加'"
    :size='900'
    v-model='visible'
    @close='onClose'
  >
    <el-form ref='formRef' :model='form' :rules='rules' layout='vertical'>
      <el-form-item label='上级菜单'>
        <MenuTreeSelect :data='menuTreeData' v-model='form.parentId' />
      </el-form-item>
      <el-form-item label='菜单类型' name='menuType'>
        <el-radio-group v-model='form.menuType'>
          <el-radio v-for='item in MENU_TYPE_ENUM' :key='item.value' :label='item.value'>
            {{ item.desc }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <!--      目录 菜单 start   -->
      <template
        v-if='
          form.menuType == MENU_TYPE_ENUM.CATALOG.value ||
          form.menuType == MENU_TYPE_ENUM.MENU.value
        '
      >
        <el-row :gutter='16'>
          <el-col :span='12'>
            <el-form-item label='菜单名称' prop='menuName'>
              <el-input v-model='form.menuName' placeholder='请输入菜单名称' />
            </el-form-item>
          </el-col>
          <el-col :span='12'>
            <el-form-item label='菜单图标' prop='icon'>
              <IconSelect @updateIcon='selectIcon'>
                <template #iconSelect>
                  <el-input v-model='form.icon' placeholder='请输入菜单图标'/>
                </template>
              </IconSelect>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter='16'>
          <el-col :span='12'>
            <el-form-item label='路由地址' prop='path'>
              <el-input v-model='form.path' placeholder='请输入路由地址' />
            </el-form-item>
          </el-col>
          <el-col :span='12'>
            <el-form-item label='排序' name='sort'>
              <el-input-number
                style='width: 100%'
                placeholder='请输入排序'
                v-model='form.sort'
                :min='0'
                :max='9999'
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item
          v-if='form.menuType == MENU_TYPE_ENUM.MENU.value'
          label='组件地址'
          name='component'
        >
          <el-input
            v-model='form.component'
            placeholder='请输入组件地址 默认带有开头/@/views'
          />
        </el-form-item>
        <el-row :gutter='16'>
          <el-col :span='12' v-if='form.menuType == MENU_TYPE_ENUM.MENU.value'>
            <el-form-item label='是否缓存' prop='cacheFlag'>
              <el-radio-group v-model='form.cacheFlag'>
                <el-radio :label='true'>是</el-radio>
                <el-radio :label='false'>否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span='12'>
            <el-form-item label='是否外链' prop='frameFlag'>
              <el-radio-group v-model='form.frameFlag'>
                <el-radio :label='true'>是</el-radio>
                <el-radio :label='false'>否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span='12'>
            <el-form-item label='显示状态' prop='frameFlag'>
              <el-radio-group v-model='form.visibleFlag'>
                <el-radio :label='true'>显示</el-radio>
                <el-radio :label='false'>隐藏</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span='12'>
            <el-form-item label='菜单状态' prop='frameFlag'>
              <el-radio-group v-model='form.disabledFlag'>
                <el-radio :label='false'>启用</el-radio>
                <el-radio :label='true'>禁用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </template>
      <!--      目录 菜单 end   -->
      <template v-if='form.menuType == MENU_TYPE_ENUM.POINTS.value'>
        <el-row :gutter='16'>
          <el-col :span='12'>
            <el-form-item label='功能点名称' prop='menuName'>
              <el-input v-model='form.menuName' placeholder='请输入功能点名称' />
            </el-form-item>
          </el-col>
          <el-col :span='12'>
            <el-form-item label='功能点关联菜单'>
              <MenuTreeSelect :data='menuTreeData' v-model='form.contextMenuId' />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter='16'>
          <el-col :span='12'>
            <el-form-item label='权限字符' prop='permsIdentifier'>
              <el-input v-model='form.permsIdentifier' placeholder='请输入权限字符' />
            </el-form-item>
          </el-col>
          <el-col :span='12'>
            <el-form-item label='功能点状态' prop='frameFlag'>
              <el-radio-group v-model='form.disabledFlag'>
                <el-radio :value='false'>启用</el-radio>
                <el-radio :value='true'>禁用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter='16'>
          <el-col :span='12'>
            <el-form-item label='接口权限(saAuth模式)' prop='permsList'>
              <el-select
                v-model='form.permsList'
                :multiple='true'
                style='width: 100%'
                placeholder='请选择接口权限'
              >
                <el-option v-for='item in allUrlData'
                                  :key='item.name'
                                  :value='item.name'
                                  :label='item.url'>
                  {{ item.url }}
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

      </template>
    </el-form>
    <template #footer>
      <el-button style='margin-right: 8px' @click='onClose'>取消</el-button>
      <el-button type='primary' @click='onSubmit'>提交</el-button>
    </template>
  </el-drawer>
</template>
<script setup>
  import { reactive, ref, watch } from 'vue';
  import { MENU_TYPE_ENUM } from '/@/constants/system/menu-enum';
  import MenuTreeSelect from './menu-tree-select.vue';
  import IconSelect from '/@/components/icon-select/index.vue';
  import { menuApi } from '/@/api/system/menu-api';
  import { useSpinStore } from '/@/store/modules/spin';
  import _ from 'lodash';
  import { ElMessage } from 'element-plus';

  // ----------------------- 以下是字段定义 emits props ------------------------
  // emit
  const emit = defineEmits(['reloadList']);

  // 组件ref
  const formRef = ref();
  let menuTreeData = ref([]);
  let allUrlData = ref([]);

  const formDefault = {
    menuId: undefined,
    menuName: undefined,
    menuType: MENU_TYPE_ENUM.CATALOG.value,
    icon: undefined,
    parentId: undefined,
    path: undefined,
    permsIdentifier: undefined,
    permsList: undefined,
    sort: undefined,
    visibleFlag: true,
    cacheFlag: false,
    component: undefined,
    contextMenuId: undefined,
    disabledFlag: false,
    frameFlag: false,
  };
  let form = reactive({ ...formDefault });
  const rules = {
    menuType: [{ required: true, message: '菜单类型不能为空' }],
    menuName: [
      { required: true, message: '菜单名称不能为空' },
      { max: 20, message: '菜单名称不能大于20个字符', trigger: 'blur' },
    ],
    path: [
      { required: true, message: '路由地址不能为空' },
      { max: 100, message: '路由地址不能大于100个字符', trigger: 'blur' },
    ],
    permsIdentifier: [{ required: true, message: '权限字符不能为空' }],
  };
  // 是否展示抽屉
  const visible = ref(false);

  // ----------------------- 以下是计算属性 watch监听 ------------------------
  watch(visible, (e) => {
    if (e) {
      queryMenuTree();
      getAuthUrl();
    }
  });
  // ----------------------- 以下是生命周期 ------------------------

  // ----------------------- 以下是方法 ------------------------
  async function showDrawer(rowData) {
    Object.assign(form, formDefault);
    if (rowData && !_.isEmpty(rowData)) {
      Object.assign(form, rowData);
    }
    visible.value = true;
  }

  async function queryMenuTree() {
    let res = await menuApi.queryMenuTree(true);
    menuTreeData.value = res.data;
  }

  async function getAuthUrl() {
    let res = await menuApi.getAuthUrl();
    allUrlData.value = res.data;
  }

  function onClose() {
    Object.assign(form, formDefault);
    formRef.value.resetFields();
    visible.value = false;
  }

  function validateForm(formRef) {
    return new Promise((resolve) => {
      formRef
        .validate()
        .then(() => {
          resolve(true);
        })
        .catch(() => {
          resolve(false);
        });
    });
  }

  const onSubmit = async () => {
    let validateFormRes = await validateForm(formRef.value);
    if (!validateFormRes) {
      message.error('参数验证错误，请仔细填写表单数据!');
      return;
    }
    useSpinStore().show();
    try {
      let params = _.cloneDeep(form);
      // 若无父级ID 默认设置为0
      if (!params.parentId) {
        params.parentId = 0;
      }
      if (params.menuId) {
        await menuApi.updateMenu(params);
      } else {
        await menuApi.addMenu(params);
      }
      ElMessage.success(`${params.menuId ? '修改' : '添加'}成功`);
      onClose();
      emit('reloadList');
    } catch (error) {
      console.log(error);
    } finally {
      useSpinStore().hide();
    }
  };

  function selectIcon(icon) {
    form.icon = icon;
  }

  // ----------------------- 以下是暴露的方法内容 ------------------------
  defineExpose({
    showDrawer,
  });
</script>
