import { getMsgsCommentForUserService } from '@/api/comment'
import { getUsersServices } from '@/api/user'
import { createStore } from 'vuex'

var store;

export default store = createStore({
    state: {
        token: localStorage.getItem("token") ? localStorage.getItem("token") : "",
        comment: [],
        mapName: "map",
        mapIndex: 0,
        pid: 10000,
        adminSearch: '人物',
        userData: { uid: 1 },
        msgData:{},
        commentSort:'hot',
        msgSort:'hot'
    },
    getters: {
        getToken(state) {
            return state.token === ""
        },
        getComment(state) {
            return state.comment
        },
        getMapName(state) {
            return state.mapName
        },
        getMapIndex(state) {
            return state.mapIndex
        },
        getPid(state) {
            return state.pid
        },
        getAdminSearch(state) {
            return state.adminSearch
        },
        getUserData(state) {
            return state.userData
        },
        getMsgData(state){
            return state.msgData
        },
        getCommentSort(state){
            return state.commentSort
        },
        getMsgSort(state){
            return state.msgSort
        }
    },
    mutations: {
        setToken(state, token) {
            state.token = token
            localStorage.setItem("token", token)
        },
        removeToken(state) {
            state.token = ""
            localStorage.removeItem("token")
        },
        getComment(state, data) {
            state.comment = data
        },
        setMap(state, map) {
            state.mapName = map.name
            state.mapIndex = map.index
        },
        setPid(state, pid) {
            state.pid = pid
        },
        setAdminSearch(state, value) {
            state.adminSearch = value
        },
        setUserData(state, userData) {
            state.userData = userData;
        },
        setMsgData(state, msgData) {
            state.msgData = msgData;
        },
        setCommentSort(state,sort){
            state.commentSort = sort;
        },
        setMsgSort(state,sort){
            state.msgSort = sort;
        }
    },
    actions: {
        getCommentAction({ commit,rootGetters },mid) {
            getMsgsCommentForUserService(mid,rootGetters.getCommentSort).then((res) => {
                let data = res.data;
                for (let d of data) {
                    d.isReply = false;
                }
                commit('getComment', data)
            });
        },
        setUserData({ commit }) {
            getUsersServices().then((res) => [
                commit('setUserData', res.data)
            ])
        }
    }
})