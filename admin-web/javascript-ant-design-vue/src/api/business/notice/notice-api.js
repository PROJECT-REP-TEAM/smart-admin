import {getRequest, postRequest} from "/@/lib/axios";

export const noticeApi = {
    // 分页查询通知公告 by 善逸
    queryNotice: (param) => {
        return postRequest('/notice/page/query', param);
    },
    // 新建通知公告 by 善逸
    createNotice: (param) => {
        return postRequest('/notice/create', param);
    },
    // 编辑通知公告 by 善逸
    updateNotice: (param) => {
        return postRequest('/notice/update', param);
    },
    // 查询通知公告详情 by 善逸
    getNoticeDetail: (noticeId) => {
        return getRequest(`/notice/get/${noticeId}`);
    },
    // 删除通知公告 by 善逸
    deleteNotice: (param) => {
        return postRequest('/notice/delete', param);
    },
    // 更新观看量 by 善逸
    watchNotice: (noticeId) => {
        return getRequest(`/notice/watch/${noticeId}`);
    },
};
