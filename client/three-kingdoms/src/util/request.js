//定制请求的实例

//导入axios
import axios from 'axios'
import stores from '@/stores';
import { ElMessage } from 'element-plus';
import router from '@/router';

// const baseUrl = 'http://localhost:8080';

const instance = axios.create({
    baseURL: '/api',
    timeout:10000
})

//添加响应拦截器
// instance.interceptors.response.use(
//     result=>{
//         return result.data
//     },
//     err=>{
//         // ElMessage.error(result.data.msg ? result.data.msg:'服务异常')
//         ElMessage.error('服务异常')
//         //异步的状态转换成失败的状态
//         return Promise.reject(err);
//     }
// )

instance.interceptors.response.use((res) => {
    return new Promise((resolve,reject)=>{
        resolve(res.data)
    })
}, err => {
    console.log(err);
    if(err.response.status === 401){
        ElMessage.error('请先登录')
        if(router.currentRoute.value.path !== '/'){
            router.push({
                path:'/login'
            })
        }
        return Promise.reject(err)
    }
    ElMessage.error('服务异常')
    // ElMessage.error('服务异常')
    //异步的状态转换成失败的状态
    return Promise.reject(err)
})

// 添加请求拦截器
// instance.interceptors.request.use(
//     (config)=>{
//         //请求前的回调
//         //添加token
//         const token = stores.state.token;
//         if(token){
//             config.headers.Authorization = token;
//         }
//         return config;
//     },
//     (err)=>{
//         //请求错误的回调
//         return Promise.reject(err)
//     }
// )

instance.interceptors.request.use((config) => {
    //请求前的回调
        // 添加token
        const token = stores.state.token;
        if(token){
            config.headers.Authorization = token;
        }
    return config
}, err => {
    return Promise.reject(err)
})

export default instance;