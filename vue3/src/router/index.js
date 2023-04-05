import { createRouter, createWebHistory } from 'vue-router';

import Home from '../views/index.vue';
import Welcome from '@/views/pages/welcome.vue';
import store from '../store';

// Vue.use(VueRouter);
// import { createApp } from 'vue';
// const app = createApp();
// app.use(createRouter);

const routes = [
    //dashboard
    { path: '/', name: 'Welcome', component: Welcome, meta: { layout: 'hero' }},
    { path: '/service/:id', name: 'Home', component: Home, meta: { layout: 'app' } },
    { path: '/index2', name: 'index2', component: () => import(/* webpackChunkName: "index2" */ '../views/index2.vue'), },
    { path: '/barchart', name: 'barchart', component: () => import(/* webpackChunkName: "index2" */ '../views/component_bar_chart.vue'), },
    { path: '/piechart', name: 'piechart', component: () => import(/* webpackChunkName: "index2" */ '../views/charts/pieChart.vue'), },
    { path: '/index2', name: 'index2', component: () => import(/* webpackChunkName: "index2" */ '../views/index2.vue'), },
    { path: '/index3', name: 'index3', component: () => import(/* webpackChunkName: "index3" */ '../views/index3.vue'), },
    { path: '/linechart', name: 'linechart', component: () => import(/* webpackChunkName: "index3" */ '../views/linechart.vue'), },
    
    //auth
    {
        path: '/auth/login',
        name: 'login',
        component: () => import(/* webpackChunkName: "auth-login" */ '../views/auth/login.vue'),
        meta: { layout: 'auth' },
    },
    {
        path: '/auth/register',
        name: 'register',
        component: () => import(/* webpackChunkName: "auth-register" */ '../views/auth/register.vue'),
        meta: { layout: 'auth' },
    },

    //users
    // {
    //     path: '/users/profile',
    //     name: 'profile',
    //     component: () => import(/* webpackChunkName: "users-profile" */ '../views/users/profile.vue'),
    // },
    // {
    //     path: '/users/account-setting',
    //     name: 'account-setting',
    //     component: () => import(/* webpackChunkName: "users-account-setting" */ '../views/users/account_setting.vue'),
    // },
    {
        path:"/user",
        name:"getting-started",
        component:()=>import('../views/users/getting_started.vue'),
    },
    {
        path:"/user/add-app",
        name:"add-app",
        component:()=>import('../views/users/add_app.vue'),
    },
    
    //charts
    // {
    //     path: '/charts/apex-chart',
    //     name: 'apex-chart',
    //     component: () => import(/* webpackChunkName: "charts-apex-chart" */ '../views/charts/apex_chart.vue'),
    // },

    //components
    
    {
        path: '/pages/error404',
        name: 'error404',
        component: () => import(/* webpackChunkName: "pages-error404" */ '../views/pages/error404.vue'),
        meta: { layout: 'auth' },
    },
    {
        path: '/pages/error500',
        name: 'error500',
        component: () => import(/* webpackChunkName: "pages-error500" */ '../views/pages/error500.vue'),
        meta: { layout: 'auth' },
    },
    {
        path: '/pages/error503',
        name: 'error503',
        component: () => import(/* webpackChunkName: "pages-error503" */ '../views/pages/error503.vue'),
        meta: { layout: 'auth' },
    },
    {
        path: '/pages/maintenence',
        name: 'maintenence',
        component: () => import(/* webpackChunkName: "pages-maintenence" */ '../views/pages/maintenence.vue'),
        meta: { layout: 'auth' },
    },
    {
        path: '/pages/blank-page',
        name: 'blank-page',
        component: () => import(/* webpackChunkName: "pages-blank-page" */ '../views/pages/blank_page.vue'),
    },
];

const router = new createRouter({
    // mode: 'history',
    history: createWebHistory(),
    linkExactActiveClass: 'active',
    routes,
    scrollBehavior(to, from, savedPosition) {
        if (savedPosition) {
            return savedPosition;
        } else {
            return { left: 0, top: 0 };
        }
    },
});

router.beforeEach((to, from, next) => {
    if (to.meta && to.meta.layout && to.meta.layout == 'auth') {
        store.commit('setLayout', 'auth');
    } else if (to.meta.layout == 'hero') {
        store.commit('setLayout', 'hero');
    } else {
        store.commit('setLayout', 'app');
    }
    next(true);
});

export default router;
