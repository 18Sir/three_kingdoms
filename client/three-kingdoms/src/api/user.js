import request from '../util/request.js'

export const userRegisterService = (userData) =>{
    // 借助于urlSerachParams完成参数传递
    const params = new URLSearchParams()
    for (let key in userData) {
        params.append(key,userData[key])
    }
    return request.post('/users/r',params);
}

export const userLoginService = (userData)=>{
    const params = new URLSearchParams()
    for (let key in userData) {
        params.append(key,userData[key])
    }
    return request.post('/users/l',params);
}

export const getUsersServices = ()=>{
    return request.get('users');
}

export const getAllUsersServices = ()=> request.get('/users/all')

