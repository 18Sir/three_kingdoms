import request from '@/util/request.js'
import qs from 'querystring'

// 查找帖子评论
export const getMsgsCommentService = (mid) => {
    return request.get(`msgs/c/${mid}`,)
}
//根据id查评论
export const getCommentByIdService = (cid) => {
    return request.get(`comments/${cid}`,)
}
//发表评论
export const saveCommentService = (commentData) => {
    return request.post('comments',commentData)
}
//删除评论
export const delCommentService = (cid) => {
    return request.delete(`msgs/${cid}`,)
}
//点赞
export const addCommentLikesService = (cid) => {
    return request.put(`comments/1/${cid}`,)
}
//取消点赞
export const reCommentLikesService = (cid) => {
    return request.put(`comments/0/$${cid}`,)
}