import request from '../util/request.js'

// 查询所有帖子类型
export const getAllMsgTypeService = () => {
    return request.get("/msgTypes")
}

// 增加帖子类型
export const saveMsgTypeService = (msgType) => {
    return request.post("/msgTypes", msgType)
}

// 删除帖子类型
export const delMsgTypeService = (tid) => {
    return request.delete(`/msgTypes/${tid}`)
}

//删除更多帖子类型
export const delMsgTypesService = (tidList) => {
    return request.delete("/msgTypes", { data: tidList })
}

// 修改帖子类型
export const updateMsgTypeService = (msgType) => {
    return request.put("/msgTypes", msgType)
}