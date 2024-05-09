import stores from '@/stores';
import request from '@/util/request.js';

//分页获取全部帖子
export const getMsgsService = (pageCurrent, pageSize) => {
    return request.get('msgs/all', {
        params: {
            current: pageCurrent,
            size: pageSize
        }
    })
}
//分页获取全部已审核帖子
export const getMsgsCheckedService = (pageCurrent, pageSize) => {
    return request.get('msgs/checked/all', {
        params: {
            current: pageCurrent,
            size: pageSize,
            sort:stores.getters.getMsgSort
        }
    })
}

// 直接获取所有帖子
export const getMsgsNoPageService = () => {
    return request.get("/msgs")
}

//根据id获取
export const getMsgByMidService = (mid) => {
    return request.get(`msgs/m/${mid}`)
}

//根据id获取并增加访问量
export const getMsgByMidWithViewsService = (mid) => {
    return request.get(`msgs/m/v/${mid}`)
}

//点赞
export const updateMsgLikesService = (mid, b) => {
    return request.put('msgs/likes',null ,{
        params:{
           mid:mid,
           b:b 
        }
    })
}

//点踩
export const updateMsgUnLikesService = (mid, b) => {
    return request.put('msgs/un/likes',null ,{
        params:{
           mid:mid,
           b:b 
        }
    })
}

//分享
export const updateMsgSharesService = (mid) => {
    return request.put('msgs/shares',null ,{
        params:{
           mid:mid
        }
    })
}

//根据用户id获取
export const getMsgByUidService = (uid) => {
    return request.get(`msgs/u/${uid}`)
}

//获取自己的帖
export const getMsgByTokenService = ()=>{
    return request.get(`msgs/u`)
}

//根据标题查找
export const getMsgsByTitleService = (title, pageCurrent, pageSize) => {
    return request.get(`msgs/t/${title}`, {
        params: {
            current: pageCurrent,
            size: pageSize
        }
    })
}
//根据标题查找已审核帖子
export const getMsgsCheckedByTitleService = (title, pageCurrent, pageSize) => {
    return request.get('msgs/checked/title', {
        params: {
            current: pageCurrent,
            size: pageSize,
            title: title,
            sort:stores.getters.getMsgSort
        }
    })
}

//根据类型筛选
export const getMsgsByTypeService = (type, pageCurrent, pageSize) => {
    return request.get(`msgs/t`, {
        params: {
            current: pageCurrent,
            size: pageSize,
            type: type
        }
    })
}

//根据类型筛选已审核帖子
export const getMsgsCheckedByTypeService = (type, pageCurrent, pageSize) => {
    return request.get(`msgs/checked/type`, {
        params: {
            current: pageCurrent,
            size: pageSize,
            type: type,
            sort:stores.getters.getMsgSort
        }
    })
}



// 上传帖子
export const uploadMsgService = (msgData) => {
    return request.post("/msgs", msgData)
}

// 删除帖子
export const deleteMsgService = (mid) => {
    return request.delete(`/msgs/${mid}`)
}

// 删除更多帖子
export const deleteMsgsService = (mids) => {
    return request.delete("/msgs/more", { data: mids })
}

// 修改帖子
export const updateMsgService = (msgData) => {
    return request.put("/msgs", msgData)
}

// 修改审核状态接口
export const updateMsgStatusService = (msgData) => {
    return request.put("/msgs/status", msgData)
}
