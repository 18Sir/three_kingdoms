import request from '@/util/request.js'
import qs from 'querystring'

//查询势力列表
export const getAllPowerService = ()=>{
    return request.get('/powers')
}

//按id查询单个势力
export const getPowerByIdService = (pid)=>{
    return request.get(`/powers/${pid}`)
}

//增加势力
export const savePowerService = (powerData)=>{
    return request.post('/powers',powerData)
}

//删除势力
export const delpowerService = (pid)=>{
    return request.delete(`/powers/${pid}`)
}

//修改势力
export const updatedPowerService = (powerData)=>{
    return request.put('/powers',powerData)
}