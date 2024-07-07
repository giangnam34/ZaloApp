<template>
    <div id="main" class="w-screen h-screen flex">
        <!-- Side bar -->
        <div class="w-[400px] h-full bg-gray-200 text-white" v-show="showSide">
            <div class="h-[50px] bg-gray-900 flex justify-start  items-center ">
                <div class="px-[20px]">
                    <h3 class="font-bold text-xl">Admin</h3>
                </div>
            </div>
            <div class="h-[calc(100vh-50px)] bg-gray-800 py-[20px]">
                <div class="flex flex-col justify-between h-full px-[20px] space-y-[10px]">
                    <AdminSidebarNav @pageSelected="updateChosenPage"></AdminSidebarNav>
                </div>
            </div>
        </div>
        <div class="w-full h-full bg-gray-400">
            <div class="h-[50px] bg-gray-100 flex items-center shadow-sm px-[20px] w-full py-[10px] z-10 border-b ">
                <!-- Hambuger menu -->
                <div class="cursor-pointer w-[30px]" @click="toggleSideBar">
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 448 512" class=" w-[25px] h-[25px]">
                        <!--! Font Awesome Pro 6.2.0 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license (Commercial License) Copyright 2022 Fonticons, Inc. -->
                        <path
                            d="M0 96C0 78.3 14.3 64 32 64H416c17.7 0 32 14.3 32 32s-14.3 32-32 32H32C14.3 128 0 113.7 0 96zM0 256c0-17.7 14.3-32 32-32H416c17.7 0 32 14.3 32 32s-14.3 32-32 32H32c-17.7 0-32-14.3-32-32zM448 416c0 17.7-14.3 32-32 32H32c-17.7 0-32-14.3-32-32s14.3-32 32-32H416c17.7 0 32 14.3 32 32z" />
                    </svg>
                </div>

                <div class="w-[calc(100%-30px)] flex">
                    <div class="w-[calc(100%-200px)] flex justify-center ">
                        <form class="flex items-center w-[500px]">
                        </form>
                    </div>
                    <div class="w-[200px] ">
                        <div class="flex items-center justify-start space-x-4 cursor-pointer" @click="toggleDrop">
                            <img class="w-10 h-10 rounded-full border-2 border-gray-50" :src="user.imageAvatarUrl" alt="">
                            <div class="font-semibold dark:text-white text-left">
                                <div>{{ user.fullName }}</div>
                                <div class="text-xs text-gray-500 dark:text-gray-400">Admin</div>
                            </div>
                        </div>
                        <div v-show="showDropDown"
                            class="absolute right-[10px] z-10 mt-2 w-56 origin-top-right rounded-md bg-white shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none"
                            role="menu" aria-orientation="vertical" aria-labelledby="menu-button" tabindex="-1">
                            <div class="py-1 text-left" role="none">
                                <div class="text-gray-700 block w-full px-4 py-2 text-left text-sm cursor-pointer"
                                    role="menuitem" tabindex="-1" id="menu-item-3" @click="logout()">Đăng xuất</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="h-[calc(100vh-50px)] bg-gray-50 p-[10px]">
                <div class="border border-gray-300 rounded-md h-full pl-[20px] pt-[5px] pr-[20px]">
                    <div v-if="chosenPage === 1" class="width-100">
                        <GeneralPage></GeneralPage>
                    </div>
                    <div v-if="chosenPage === 2" class="width-100">
                        <UsersManage></UsersManage>
                    </div>
                    <div v-if="chosenPage === 3" class="width-100">
                        <PostsManage></PostsManage>
                    </div>
                    <div v-if="chosenPage === 4" class="width-100">
                        <GroupsManage></GroupsManage>
                    </div>
                </div>
            </div>
        </div>
        <!-- Main  -->
    </div>
</template>
<script>
import axios from 'axios';
import AdminSidebarNav from './AdminSidebarNav.vue';
import UsersManage from './UsersManage.vue';
import PostsManage from './PostsManage.vue';
import GroupsManage from './GroupsManage.vue';
import GeneralPage from './GeneralPage.vue';
export default {
    components: {
        AdminSidebarNav,
        UsersManage,
        PostsManage,
        GroupsManage,
        GeneralPage
    },
    data() {
        return {
            showDropDown: false,
            showSide: true,
            user: null,
            chosenPage: 1,
        }
    },
    emits: ['userLoggedIn'],
    created() {
        // Lấy user từ localStorage
        const userString = localStorage.getItem('user');

        // Chuyển đổi chuỗi JSON thành đối tượng JavaScript
        if (userString) {
            this.user = JSON.parse(userString);
        }

        this.fetchAvatar();
    },
    methods: {
        // hide show side bar
        toggleSideBar() {
            this.showSide = !this.showSide

        },
        // toggle user 
        toggleDrop() {
            this.showDropDown = !this.showDropDown

        },
        logout() {
            localStorage.removeItem("token");
            localStorage.setItem("isValid", false);
            localStorage.removeItem("user");
            this.$emit("userLoggedIn", '');
            event.preventDefault();
            event.stopPropagation();
        },
        fetchAvatar() {
            axios.get(`users/imageAvatar`, {
                headers: {
                    'Authorization': localStorage.getItem("token"),
                },
                responseType: 'blob',
            }).then(response => {
                if (this.user.imageAvatarUrl) {
                    URL.revokeObjectURL(this.user.imageAvatarUrl);
                }
                this.user.imageAvatarUrl = URL.createObjectURL(response.data);
            }).catch(error => {
                console.error('Error fetching avatar:', error);
            });
            //console.log('user.imageAvatarUrl changed:', this.user.imageAvatarUrl);
        },
        updateChosenPage(page) {
            this.chosenPage = page;
        },
    }
}
</script>
  
<style>
#main {
    font-family: Avenir, Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    color: #2c3e50;
}
</style>