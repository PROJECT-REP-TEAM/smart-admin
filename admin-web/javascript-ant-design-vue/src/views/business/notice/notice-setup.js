import {PAGE_SIZE} from "/@/constants/common-const";
import {reactive, ref} from "vue";
import {noticeApi} from "/@/api/business/notice/notice-api";

export function noticeSetup(){
    const queryFormState = {
        keywords: "",
        noticeType: undefined,
        noticeBelongType: undefined,
        startTime: undefined,
        endTime: undefined,
        disabledFlag: undefined,
        pageNum: 1,
        pageSize: PAGE_SIZE,
    };
    const queryForm = reactive({...queryFormState});
    const tableLoading = ref(false);
    const tableData = ref([]);
    const total = ref(0);

    async function ajaxQuery() {
        try {
            tableLoading.value = true;
            let responseModel = await noticeApi.queryNotice(queryForm);
            const list = responseModel.data.list;
            total.value = responseModel.data.total;
            tableData.value = list;
        } catch (e) {
            console.log(e);
        } finally {
            tableLoading.value = false;
        }
    }

    return {
        queryFormState,
        queryForm,
        tableLoading,
        tableData,
        total,
        ajaxQuery
    }
}
