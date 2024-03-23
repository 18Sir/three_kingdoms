import request from '@/util/request.js'
import qs from 'querystring'

//查询全部事件
export const getAllEventsService = (pageCurrent, pageSize) => {
    return request.get('/events', {
        params: {
            current: pageCurrent,
            size: pageSize
        }
    })
}

//根据事件名检索事件
export const getEventsByNameService = (name,pageCurrent,pageSize)=>{
    return request.get(`/events/n/${name}`,{
        params:{
            current:pageCurrent,
            size:pageSize
        }
    })
}

//查询单个事件
export const getEventByIdService = (id,pageCurrent,pageSize)=>{
    return request.get(`/events/${id}`,{
        params:{
            current:pageCurrent,
            size:pageSize
        }
    })
}

//不分页查询所有事件
export const getAllEventsNoPageService = ()=>{
    return request.get("/events/all")
}

//新增事件
export const saveEventService = (data)=>{
    return request.post("/events",data)
}

//修改事件
export const updateEventService = (data)=>{
    return request.put("/events",data)
}

//删除事件
export const delEventService = (eid)=>{
    return request.delete(`/events/${eid}`)
}

//删除多个事件
export const delEventsService = (eids) => {
    return request.delete("/events/more",{data:eids})
}