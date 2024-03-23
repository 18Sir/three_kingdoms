import { getMsgsCommentService } from '@/api/comment'
import {createStore} from 'vuex'

export default createStore({
    state:{
        token:localStorage.getItem("token") ? localStorage.getItem("token") : "",
        comment:[],
        mapName:"map",
        mapIndex:0,
        pid:10000,
        adminSearch:'人物'
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
        }
    },
    actions:{
        getCommentAction({commit},mid){
            getMsgsCommentService(mid).then((res) => {
                commit('getComment',res.data)
                // comment = res.data;
              });
        }
    }
})