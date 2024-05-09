import { createRouter, createWebHashHistory } from 'vue-router'
import Main from "../pages/Main.vue"

// 路由配置
const routes = [
    {
        // 页面访问路径
        path: "/",
        // 页面的文件
        component: Main,
        meta: { title: '首页' }
    },
    {
        path: "/login",
        // 异步引入，当页面不显示时，不提前渲染组件，缩短首页的加载时间
        component: () => import("../pages/Login.vue")
    },
    {
        path: "/actor",
        redirect: "/actor/more/all",
        component: () => import("../pages/actor/Actor.vue"),
        meta: { title: '人物传记' },
        children: [
            {
                path: "more/:name",
                component: () => import("../pages/actor/ActorMore.vue"),
                meta: { title: '更多人物' },
            },
            {
                path: "info/:id/:name",
                component: () => import("../pages/actor/ActorInfo.vue"),
                meta: { title: '人物详情' },
            }
        ]
    },
    {
        path: "/event",
        redirect: "/event/more/all",
        component: () => import("../pages/event/Event.vue"),
        meta: { title: '历史事件' },
        children: [
            {
                path: "more/:name",
                component: () => import("../pages/event/EventMore.vue"),
                meta: { title: '更多事件' },
            },
            {
                path: "info/:id/:name",
                component: () => import("../pages/event/EventInfo.vue"),
                meta: { title: '事件详情' },
            }
        ]
    },
    {
        path: "/discuss",
        redirect: "/discuss/more",
        component: () => import("../pages/discuss/Discuss.vue"),
        meta: { title: '交流专区' },
        children: [
            {
                path: "more",
                component: () => import("../pages/discuss/DiscussMore.vue"),
                meta: { title: '更多帖子' },
            },
            {
                path: "info/:id",
                component: () => import("../pages/discuss/DiscussInfo.vue"),
                meta: { title: '帖子详情' },
            }
        ]
    },
    {
        path: "/power",
        component: () => import("../pages/power/Power.vue"),
        meta: { title: '势力分布' },
    },
    {
        path: "/user",
        component: () => import("../pages/userInfo/User.vue"),
        meta: { title: '个人中心' },
        children: [
            {
                path: "setting",
                component: () => import("../pages/userInfo/UserSetting.vue"),
                meta: { title: '个人信息' },
            },
            {
                path: "face",
                component: () => import("../pages/userInfo/UserFace.vue"),
                meta: { title: '我的头像' },
            },
            {
                path: "secure",
                component: () => import("../pages/userInfo/UserSecure.vue"),
                meta: { title: '账户安全' },
            },
            {
                path: "msg",
                component: () => import("../pages/userInfo/UserMsg.vue"),
                meta: { title: '我的帖子' },
            },
        ]
    },
    {
        path: "/message",
        redirect: "/message/system",
        component: () => import("../pages/message/Message.vue"),
        meta: { title: '消息中心' },
        children: [
            {
                path: "system",
                component: () => import("../pages/message/SysMessage.vue"),
                meta: { title: '系统消息' },
            },
        ]
    },
    {
        path: "/admin",
        redirect: "/admin/actor",
        component: () => import("../pages/admin/Home.vue"),
        children: [
            {
                path: "actor",
                component: () => import("../pages/admin/ActorTable.vue")
            },
            {
                path: "event",
                component: () => import("../pages/admin/EventTable.vue")
            },
            {
                path: "addr",
                component: () => import("../pages/admin/AddrTable.vue")
            },
            {
                path: "power",
                component: () => import("../pages/admin/PowerTable.vue")
            },
            {
                path: "msg/list",
                component: () => import("../pages/admin/msg/MsgListTable.vue")
            },
            {
                path: "msg/type",
                component: () => import("../pages/admin/msg/MsgTypeTable.vue")
            },
            {
                path: "jubao/msg",
                component: () => import("../pages/admin/jubao/MsgJuBaoTable.vue")
            },
            {
                path: "jubao/comment",
                component: () => import("../pages/admin/jubao/CommentJuBaoTable.vue")
            },
            {
                path: "user",
                component: () => import("../pages/admin/UserTable.vue")
            },
        ]
    }

]

const router = createRouter({
    /**两种方法
     * createWebHistory
     * 访问路径：Home： http://localhost:5173/
     *          About: http://localhost:5173/about
     * 原理：H5的 PushState()
     * 需要后台配合重定向，否则会出现404问题
     */

    /**
     * createWebHashHistory
     * 访问路径：Home： http://localhost:5173/#/
     *          About: http://localhost:5173/#/about
     * 原理：a标签的锚点连接
     * 不需要后台配合重定向
     */

    history: createWebHashHistory(),
    routes
})

router.beforeEach((to, from, next) => {
    if (to.path === '/user') {
        next({ path: 'user/setting' })
    } else {
        next()
    }

})

export default router