import request from '@/util/request.js'
import qs from 'querystring'

//分页获取所有人物
export const getAllActorService = (pageCurrent, pageSize) => {
    const config = {
        url: "/actors",
        method: "get",
        params: {
            current: pageCurrent,
            size: pageSize
        }
    }
    return request.request(config)
}

//直接获取所有人物
export const getAllActorNoPageService = () => {
    return request.get("/actors/all")
}

//根据名字检索所有相关人物
export const getAllActorByNameService = (name, pageCurrent, pageSize) => {
    const config = {
        url: "/actors/n/" + name,
        method: "get",
        params: {
            current: pageCurrent,
            size: pageSize
        }
    }

    return request.request(config)
}

//根据id查询人物具体信息
export const getActorInfoById = (id) => {
    return request.get(`/actors/${id}`)
}
//查询人物参与的事件信息
export const getActorEvents = (id) => {
    return request.get(`/actors/e/${id}`)
}

//查询人物的出生地
export const getActorAddr = (id) => {
    return request.get(`/actors/a/${id}`)
}

//根据名字，势力信息联合筛选人物
export const getActorByNamePowerService = (name, pageCurrent, pageSize, powers) => {
    const config = {
        url: "/actors/p/" + name,
        method: "get",
        params: {
            current: pageCurrent,
            size: pageSize,
            powers: powers + ''
        }
    }

    return request.request(config)
}

//增加人物
export const saveActorService = (actorData) => {
    return request.post("/actors", actorData);
}

//删除人物
export const delActorByIdService = (aid) => {
    return request.delete(`/actors/${aid}`)
}

//删除多个人物
export const delActorMoreService = (aids) => {
    return request.delete("/actors/more",{data:aids})
}

//修改人物
export const updateActorService = (actorData) => {
    return request.put("/actors", actorData)
}
