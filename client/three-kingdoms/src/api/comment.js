import request from '@/util/request.js'
import qs from 'querystring'

// 查找帖子评论
export const getMsgsCommentService = (mid) => {
    return request.get(`msgs/c/${mid}`,)
}
// 用户端查找帖子评论
export const getMsgsCommentForUserService = (mid,sort) => {
    return request.get(`msgs/c/u/${mid}`,{
        params:{
            sort
        }
    })
}
//根据id查评论
export const getCommentByIdService = (cid) => {
    return request.get(`comments/${cid}`,)
}
//发表评论
export const saveCommentService = (commentData) => {
    return request.post('comments', commentData)
}
//回复评论
export const replyCommentService = (commentData) => {
    return request.post('comments/reply', commentData)
}
//删除评论
export const delCommentService = (cid) => {
    return request.delete(`comments/${cid}`)
}
//点赞
export const updateCommentLikesService = (cid, b) => {
    return request.put('comments/likes', null, {
        params: {
            cid,
            b
        }
    })
}

//点踩
export const updateCommentUnLikesService = (cid, b) => {
    return request.put('comments/un/likes', null, {
        params: {
            cid,
            b
        }
    })
}