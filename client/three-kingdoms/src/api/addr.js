import request from '@/util/request.js'
import qs from 'querystring'

// 分页获取所有地点
export const getAllAddrsToPageService = (current,size) => {
    return request.get(`/addrs?current=${current}&size=${size}`)
}

// 获取所有地点
export const getAllAddrsNoPageService = () => {
    return request.get("/addrs/all")
}

// 根据id获取地点
export const getAddrByIdService = (addrId)=>{
    return request.get(`/addrs/${addrId}`)
}

//根据名称获取地点
export const getAddrByNameService = (name)=>{
    return request.get(`/addrs/name/${name}`)
}

// 获取州的所有郡
export const getAllAddrCountryService = (stateName) => {
    return request.get(`/addrs/country?stateName=${stateName}`)
}

// 获取所有州
export const getAllAddrStateService = ()=>{
    return request.get("/addrs/state")
}

// 以级联方式获取所有州
export const getAllAddrStateToCascadeService = ()=>{
    return request.get("/addrs/state/cascade")
}

// 增加地点
export const saveAddrService = (addrData)=>{
    return request.post("/addrs",addrData)
}

// 修改地点
export const updateAddrService = (addrData)=>{
    return request.put("/addrs",addrData)
}

// 删除地点
export const delAddrService = (addrId)=>{
    return request.delete(`/addrs/${addrId}`)
}

// 删除多个地点
export const delAddrsService = (addrIds)=>{
    return request.delete("/addrs/more",{data:addrIds})
}