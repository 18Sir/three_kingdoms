import { getMsgsCommentService } from '@/api/comment'
import { getUsersServices } from '@/api/user'
import {createStore} from 'vuex'

var store;

export default store = createStore({
    state:{
        token:localStorage.getItem("token") ? localStorage.getItem("token") : "",
        comment:[],
        mapName:"map",
        mapIndex:0,
        pid:10000,
        adminSearch:'人物',
        userData:{}
    },
    getters:{
        getToken(state){
            return state.token === ""
        },
        getComment(state){
            return state.comment
        },
        getMapName(state){
            return state.mapName
        },
        getMapIndex(state){
            return state.mapIndex
        },
        getPid(state){
            return state.pid
        },
        getAdminSearch(state){
            return state.adminSearch
        },
        getUserData(state){
            return state.userData
        }
    },
    mutations:{
        setToken(state,token){
            state.token = token
            localStorage.setItem("token",token)
        },
        removeToken(state){
            state.token = ""
            localStorage.removeItem("token")
        },
        getComment(state,data){
            state.comment = data
        },
        setMap(state,map){
            state.mapName = map.name
            state.mapIndex = map.index
        },
        setPid(state,pid){
            state.pid = pid
        },
        setAdminSearch(state,value){
            state.adminSearch = value
        },
        setUserData(state,userData){
            state.userData = userData;
        }
    },
    actions:{
        getCommentAction({commit},mid){
            getMsgsCommentService(mid).then((res) => {
                commit('getComment',res.data)
                // comment = res.data;
              });
        },
        setUserData({commit}){
            getUsersServices().then((res)=>[
                commit('setUserData',res.data)
            ])
        }
    }
})