import { createRouter, createWebHashHistory } from 'vue-router'
import Main from "../pages/Main.vue"

// 路由配置
const routes = [
    {
        // 页面访问路径
        path: "/",
        // 页面的文件
        component: Main
    },
    {
        path: "/login",
        // 异步引入，当页面不显示时，不提前渲染组件，缩短首页的加载时间
        component: () => import("../pages/Login.vue")
    },
    {
        path: "/actor",
        redirect:"/actor/more/all",
        component: () => import("../pages/Actor.vue"),
        
        children: [
            {
                path: "more/:name",
                component: () => import("../pages/ActorMore.vue")
            },
            {
                path: "info/:id/:name",
                component: () => import("../pages/ActorInfo.vue")
            }
        ]
    },
    {
        path: "/event",
        redirect:"/event/more/all",
        component: () => import("../pages/Event.vue"),
        children: [
            {
                path: "more/:name",
                component: () => import("../pages/EventMore.vue")
            },
            {
                path: "info/:id/:name",
                component: () => import("../pages/EventInfo.vue")
            }
        ]
    },
    {
        path: "/discuss",
        redirect:"/discuss/more",
        component: () => import("../pages/Discuss.vue"),
        children: [
            {
                path: "more",
                component: () => import("../pages/DiscussMore.vue")
            },
            {
                path: "info/:id",
                component: () => import("../pages/DiscussInfo.vue")
            }
        ]
    },
    {
        path: "/power",
        component: () => import("../pages/Power.vue")
    },
    {
        path: "/user",
        component: () => import("../pages/User.vue")
    },
    {
        path:"/admin",
        redirect:"/admin/actor",
        component: () => import("../pages/admin/Home.vue"),
        children: [
            {
                path: "actor",
                component: () => import("../pages/admin/ActorTable.vue")
            },
            {
                path: "event",
                component: () => import("../pages/admin/EventTable.vue")
            }
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

export default router