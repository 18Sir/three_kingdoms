import request from '@/util/request.js'
import qs from 'querystring'

//分页获取全部帖子
export const getMsgsService = (pageCurrent, pageSize) => {
    return request.get('msgs/all', {
        params: {
            current: pageCurrent,
            size: pageSize
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

//根据用户id获取
export const getMsgByUidService = (uid) => {
    return request.get(`msgs/u/${uid}`)
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
