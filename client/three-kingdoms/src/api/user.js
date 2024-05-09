import request from '../util/request.js'

// 注册
export const userRegisterService = (userData) => {
    // 借助于urlSerachParams完成参数传递
    const params = new URLSearchParams()
    for (let key in userData) {
        params.append(key, userData[key])
    }
    return request.post('/users/r', params);
}

// 登录
export const userLoginService = (userData) => {
    const params = new URLSearchParams()
    for (let key in userData) {
        params.append(key, userData[key])
    }
    return request.post('/users/l', params);
}

// 获取用户信息
export const getUsersServices = () => {
    return request.get('users');
}

// 获取全部用户信息
export const getAllUsersServices = () => request.get('/users/all')

// 修改用户信息
export const updateUserServices = (user) => {
    return request.put("/users", user)
}

//上传头像
export const uploadAvatarService = (fileData) => {
    return request.post("/users/upload", fileData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
}

//发送邮箱验证码
export const sendEmailCodeService = (userData) => {
    const params = new URLSearchParams()
    for (let key in userData) {
        params.append(key, userData[key])
    }
    return request.post('/users/email/send', params);
}

// 验证邮箱验证码
export const verifyEmailCodeService = (code)=>{
    return request.get(`/users/email/verify?code=${code}`)
}
//验证邮箱验证码成功后绑定邮箱
export const vwuService = (user)=>{
    return request.get(`/users/email/vwu?code=${user.code}&email=${user.email}`)
}

// 修改密码
export const updatePasswordService = (userData)=>{
    const params = new URLSearchParams()
    for (let key in userData) {
        params.append(key, userData[key])
    }
    return request.post('/users/password',params)
}

// 校验密码
export const verifyPasswordService = (password)=>{
    const params = new URLSearchParams()
    params.append('password',password)

    return request.post('/users/password/verify',params)
}

// 注销自己
export const delUserService = () => {
    return request.delete("/users")
}

// 注销其他
export const delAnotherUserService = (uid) => {
    return request.delete(`/users/${uid}`)
}

//封禁用户
export const blockUserService = (params)=>{
    return request.post("/users/block",null,{
        params:{
            blockUid:params.uid,
            time:params.time,
            reason:params.reason
        }
    });
}

//解封用户
export const unBlockUserService = (blockUid)=>{
    return request.delete(`/users/block/${blockUid}`)
}

//查看解封时间
export const getUserBlockService = ()=>{
    return request.get("/users/block")
}

//向用户发送消息
export const sendUserMessageService = (params)=>{
    return request.post("/users/message/send",null,{
        params:{
            uid:params.uid,
            title:params.title,
            content:params.content,
            type:params.type
        }
    })
}

//用户删除信息
export const userDelMessageService = (params)=>{
    return request.delete("/users/message",{
        params:params.hashKey,
        type:params.type
    })
}

//新消息提示
export const hasNewMessageService = ()=>{
    return request.get("/users/message/alert")
}

//用户访问自己的消息
export const getUserMessageService = (type)=>{
    return request.get(`/users/message/${type}`)
}