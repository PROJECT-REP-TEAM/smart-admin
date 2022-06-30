import {PAGE_SIZE} from "/@/constants/common-const";
import {reactive, ref} from "vue";
import {noticeApi} from "/@/api/business/notice/notice-api";
import {NOTICE_TYPE_ENUM} from "/@/constants/business/notice-const";
import {useRouter} from "vue-router";
import {localRead, localSave} from '/@/utils/local-util';
import localKey from '/@/constants/local-storage-key-const';
import {useUserStore} from "/@/store/modules/system/user";

export function noticeSetup() {
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
            reloadReadFlag();
        } catch (e) {
            console.log(e);
        } finally {
            tableLoading.value = false;
        }
    }

    function reloadReadFlag(){
        // 回写已读信息
        let employeeId = useUserStore().employeeId;
        let { employeeNoticeReadRecord } = getReadNotice(employeeId);
        tableData.value.forEach(e => {
            e.readFlag = false;
            if (employeeNoticeReadRecord.includes(e.noticeId)) {
                e.readFlag = true;
            }
        })
    }

    //----------------------- 详情 ----------------------------
    const router = useRouter();

    /**
     * 通知公告详情
     * @param notice 通知信息
     * @param readFlag 是否已读
     * @returns {Promise<void>}
     */
    async function goDetail(notice, readFlag = false) {
        let noticeId = notice.noticeId;
        if (readFlag) {
            readNotice(noticeId);
        }
        reloadReadFlag();
        await noticeApi.watchNotice(noticeId);
        if (notice.noticeType == NOTICE_TYPE_ENUM.LINK.value) {
            window.open(notice.linkAddress);
            return;
        }
        router.push({path: '/business/notice/detail', query: {noticeId: noticeId}});
    }

    //----------------------- 已读 ----------------------------
    function readNotice(noticeId) {
        let employeeId = useUserStore().employeeId;
        let { noticeReadRecord,employeeNoticeReadRecord } = getReadNotice(employeeId);
        if(employeeNoticeReadRecord.includes(noticeId)){
            return;
        }
        employeeNoticeReadRecord.push(noticeId);
        noticeReadRecord[employeeId] = employeeNoticeReadRecord;
        localSave(localKey.NOTICE_READ, JSON.stringify(noticeReadRecord));
    }

    function getReadNotice(employeeId) {
        let noticeReadRecordJson = localRead(localKey.NOTICE_READ);
        let noticeReadRecord = {};
        if (noticeReadRecordJson) {
            noticeReadRecord = JSON.parse(noticeReadRecordJson);
        }
        let employeeNoticeReadRecord = [];
        if (noticeReadRecord.hasOwnProperty(employeeId)) {
            employeeNoticeReadRecord = noticeReadRecord[employeeId];
        }
        return {
            noticeReadRecord,
            employeeNoticeReadRecord
        };
    }

    return {
        queryFormState,
        queryForm,
        tableLoading,
        tableData,
        total,
        ajaxQuery,
        goDetail
    }
}
