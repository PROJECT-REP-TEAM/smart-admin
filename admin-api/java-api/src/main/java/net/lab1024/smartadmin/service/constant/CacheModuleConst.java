package net.lab1024.smartadmin.service.constant;

/**
 * @author 罗伊
 * @date 2021-01-31 0:00
 */
public class CacheModuleConst {

    public static class IdGenerator {
        public static final String ID_GENERATOR_CACHE = "id_generator_cache";
    }


    public static class Employee {
        /**
         * 某个部门下的员工缓存
         */
        public static final String DEPARTMENT_EMPLOYEE_CACHE = "department_employee_cache";

        /**
         * 员工缓存
         */
        public static final String EMPLOYEE_CACHE = "employee_cache";

        /**
         * 员工角色缓存
         */
        public static final String EMPLOYEE_ROLE_CACHE = "employee_role_cache";
    }

    public static class Department {

        /**
         * 部门树
         */
        public static final String DEPARTMENT_CACHE = "department_cache";

        /**
         * 部门树
         */
        public static final String DEPARTMENT_TREE_CACHE = "department_tree_cache";

        /**
         * 某个部门以及下级的id列表
         */
        public static final String DEPARTMENT_SELF_CHILDREN_CACHE = "department_self_children_cache";

        public static final String DEPARTMENT_ROUTE_CACHE = "department_route_cache";

    }

    public static class Category {

        /**
         * 类目
         */
        public static final String CATEGORY = "category_cache";

        /**
         * 类目子级
         */
        public static final String CATEGORY_SUB = "category_sub_cache";

        /**
         * 类目层级树 缓存
         */
        public static final String CATEGORY_TREE = "category_tree_cache";

    }
}
