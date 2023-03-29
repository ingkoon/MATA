<template>
    <div :class="[$store.state.layout_style, $store.state.menu_style]">
        <component v-bind:is="layout"></component>
    </div>
</template>
<script setup>
    import { computed } from 'vue';

    import '@/assets/sass/app.scss';

    import { useMeta } from '@/composables/use-meta';
    import { useStore } from 'vuex';

    useMeta({ title: 'Sales Admin' });

    const store = useStore();

    const layout = computed(() => {
        console.log("app compute start")
        console.log('app compute done')
        return store.getters.layout;
        
    });
</script>
<script>
    // layouts
    import appLayout from './layouts/app-layout.vue';
    import authLayout from './layouts/auth-layout.vue';
    import heroLayout from '@/layouts/hero-layout.vue';

    export default {
        components: {
            app: appLayout,
            auth: authLayout,
            hero: heroLayout,
        },
    };
</script>
