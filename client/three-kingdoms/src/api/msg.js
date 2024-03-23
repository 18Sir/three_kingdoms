import request from '@/util/request.js'
import qs from 'querystring'

//获取全部帖子
export const getMsgsService = (pageCurrent,pageSize)=>{
    return request.get('msgs/all',{
        params:{
            current:pageCurrent,
            size:pageSize
        }
    })
}

//根据id获取
export const getMsgByMidService = (mid)=>{
    return request.get(`msgs/m/${mid}`)
}

//根据用户id获取
export const getMsgByUidService = (uid)=>{
    return request.get(`msgs/u/${uid}`)
}

//根据标题查找
export const getMsgsByTitleService = (title,pageCurrent,pageSize)=>{
    return request.get(`msgs/t/${title}`,{
        params:{
            current:pageCurrent,
            size:pageSize
        }
    })
}

//根据类型筛选
export const getMsgsByTypeService = (type,pageCurrent,pageSize)=>{
    return request.get(`msgs/t`,{
        params:{
            current:pageCurrent,
            size:pageSize,
            type:type
        }
    })
}
