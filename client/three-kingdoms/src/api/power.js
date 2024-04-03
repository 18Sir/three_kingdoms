import request from '@/util/request.js'
import qs from 'querystring'

//查询势力列表
export const getAllPowerService = () => {
    return request.get('/powers')
}

//按id查询单个势力
export const getPowerByIdService = (pid) => {
    return request.get(`/powers/${pid}`)
}

//增加势力
export const savePowerService = (powerData) => {
    return request.post('/powers', powerData)
}

//删除势力
export const delPowerService = (pid) => {
    return request.delete(`/powers/${pid}`)
}

// 删除多个势力
export const delPowersService = (pids) => {
    return request.delete("/powers/more", { data: pids })
}

//修改势力
export const updatePowerService = (powerData) => {
    return request.put('/powers', powerData)
}