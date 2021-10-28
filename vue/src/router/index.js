import {createRouter, createWebHistory} from 'vue-router'
import Layout from "@/layout/Layout";

const routes = [
    {
        path: '/',
        name: 'Layout',
        component: Layout,
        redirect: '/user',      //页面的自动跳转，当什么都不输入的时候 需要显示主体区域
        children: [             //这里用children进行二次路由，可以创建不同的新的主体，在保持原有header和aside的情况下。
            {
                path: '/user',
                name: 'User',
                component: () => import("@/views/User")
            },
            {
                path: '/book',
                name: 'Book',
                component: () => import("@/views/Book")
            },
            {
                path: '/news',
                name: 'News',
                component: () => import("@/views/News")
            },
            {
                path: '/person',
                name: 'Person',
                component: () => import("@/views/Person")
            }
        ]
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import("@/views/Login")
    },
    {
        path: '/register',
        name: 'Register',
        component: () => import("@/views/Register")
    }
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

export default router
