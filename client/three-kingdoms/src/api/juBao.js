import request from '@/util/request.js';

//直接获取全部帖子的举报信息
export const getAlljuBaoMsgService = () => {
    return request.get("/juBaoMsgs/all")
}

//添加举报信息
export const savejuBaomsgService = (juBaoMsgData)=>{
    return request.post("/juBaoMsgs", juBaoMsgData)
}

//修改举报信息
export const updatejuBaomsgService = (juBaoMsgData)=>{
    return request.put("/juBaoMsgs", juBaoMsgData)
}

//删除举报信息
export const deletejuBaomsgService = (jid)=>{
    return request.delete(`/juBaoMsgs/${jid}`)
}

// 删除更多举报信息
export const deletejuBaoMsgsService = (jids) => {
    return request.delete("/juBaoMsgs/more", { data: jids })
}

//直接获取全部评论的举报信息
export const getAlljuBaoCommentService = () => {
    return request.get("/juBaoComments/all")
}

//添加举报信息
export const savejuBaoCommentService = (juBaoMsgData)=>{
    return request.post("/juBaoComments", juBaoMsgData)
}

//修改举报信息
export const updatejuBaoCommentService = (juBaoMsgData)=>{
    return request.put("/juBaoComments", juBaoMsgData)
}

//删除举报信息
export const deletejuBaoCommentService = (jid)=>{
    return request.delete(`/juBaoComments/${jid}`)
}

// 删除更多举报信息
export const deletejuBaoCommentsService = (jids) => {
    return request.delete("/juBaoComments/more", { data: jids })
}