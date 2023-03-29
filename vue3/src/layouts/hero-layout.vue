<template>
    <div>
        <!--  BEGIN NAVBAR  -->
        <Header></Header>
        <!--  END NAVBAR  -->

        <!--  BEGIN MAIN CONTAINER  -->
        <div
            class="main-container"
            id="container"
            :class="['sidebar-closed sbar-open', $store.state.menu_style === 'collapsible-vertical' ? 'collapsible-vertical-mobile' : '']"
        >

            <!--  BEGIN CONTENT AREA  -->
            <div id="content" class="main-content">
                <router-view />

                <!-- BEGIN FOOTER -->
                <Footer></Footer>
                <!-- END FOOTER -->
            </div>
            <!--  END CONTENT AREA  -->

        </div>
        
    </div>
</template>

<script setup>
    
    import Header from '@/components/layout/header.vue';
    import Sidebar from '@/components/layout/sidebar.vue';
    import Footer from '@/components/layout/footer.vue';
</script>
