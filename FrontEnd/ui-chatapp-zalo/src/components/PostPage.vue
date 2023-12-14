<template>
    <div class="px-8 py-6 bg-gray-100 overflow-y-auto max-h-[737.5px]">
        <div class="max-w-none w-auto mx-auto grid grid-cols-4 gap-4">
            <div class="main-left col-span-1">
                <div class="p-4 bg-white border border-gray-200 text-center rounded-lg">
                    <img src="https://i.imgur.com/gEKsypv.jpg" class="w-[200px] h-[200px] rounded-full mx-auto mb-6">

                    <p><strong>{{ user.fullName }}</strong></p>

                    <div class="mt-6 flex space-x-8 justify-around">
                        <p class="text-xs text-gray-500">182 friends</p>
                        <p class="text-xs text-gray-500">120 posts</p>
                    </div>

                </div>
            </div>

            <div class="main-center col-span-2 space-y-4">
                <div class="bg-white border border-gray-200 rounded-lg">
                    <div class="p-4">
                        <span class="p-4 bg-gray-100 rounded-lg cursor-pointer d-block w-100" @click="showPostOption">Bạn
                            đang nghĩ gì?</span>
                    </div>
                </div>

                <div class="p-4 bg-white border border-gray-200 rounded-lg">
                    <div class="mb-6 flex items-center justify-between">
                        <div class="flex items-center space-x-6">
                            <img src="https://i.imgur.com/gEKsypv.jpg"
                                class="w-[50px] h-[50px] rounded-full flex justify-center align-center mr-4">

                            <p><strong>{{ user.fullName }}</strong></p>
                        </div>

                        <p class="text-gray-600">17:50 14/12/2023</p>
                    </div>

                    <p class="mb-3">Ảnh đẹp dữ!</p>

                    <img src="https://i.imgur.com/gEKsypv.jpg" class="w-full h-[500px] rounded-lg">

                    <div class="my-6 flex justify-between">
                        <div class="flex space-x-6">
                            <div class="flex items-center space-x-2">
                                <font-awesome-icon icon="fa-regular fa-heart" />
                                <span class="text-gray-500 text-xs">100 Likes</span>
                            </div>

                            <div class="flex items-center space-x-2">
                                <font-awesome-icon icon="fa-regular fa-comment" />
                                <span class="text-gray-500 text-xs">17 Bình luận</span>
                            </div>
                        </div>
                        <div>
                            <font-awesome-icon icon="fa-solid fa-ellipsis-vertical" />
                        </div>
                    </div>
                </div>

                <div class="p-4 bg-white border border-gray-200 rounded-lg">
                    <div class="mb-6 flex items-center justify-between">
                        <div class="flex items-center space-x-6">
                            <img src="https://i.imgur.com/gEKsypv.jpg"
                                class="w-[50px] h-[50px] rounded-full flex justify-center align-center mr-4">

                            <p><strong>{{ user.fullName }}</strong></p>
                        </div>

                        <p class="text-gray-600">30 phút trước</p>
                    </div>

                    <p class="mb-3">Ảnh đẹp quá!</p>

                    <img src="https://i.imgur.com/gEKsypv.jpg" class="w-full h-[500px] rounded-lg">

                    <div class="my-6 flex justify-between">
                        <div class="flex space-x-6">
                            <div class="flex items-center space-x-2">
                                <font-awesome-icon icon="fa-regular fa-heart" />
                                <span class="text-gray-500 text-xs">100 Likes</span>
                            </div>

                            <div class="flex items-center space-x-2">
                                <font-awesome-icon icon="fa-regular fa-comment" />
                                <span class="text-gray-500 text-xs">17 Bình luận</span>
                            </div>
                        </div>
                        <div>
                            <font-awesome-icon icon="fa-solid fa-ellipsis-vertical" />
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <v-dialog class="dialog-container" v-model="showPostVisible" max-width="500px" @click:outside="closePostOption">
        <v-card class="dialog-component">
            <v-card-title class="dialog-title">
                <h2 class="title">Tạo bài viết
                    <div class="icon-close" @click="closePostOption"><font-awesome-icon icon="fa-solid fa-x" /></div>
                </h2>
            </v-card-title>
            <hr style="border: none; border-bottom: 1px solid #ccc;">
            <v-card-text class="dialog-content">
                <div style="display: flex; align-items: center; justify-content: space-between;">
                    <div class="friend-info">
                        <div class="avatar-container">
                            <div class="avatar-wrapper">
                                <img src="https://i.imgur.com/gEKsypv.jpg" class="avatar">
                            </div>
                        </div>
                        <div class="detail">
                            <p><strong>{{ user.fullName }}</strong></p>
                            <select v-model="privateSetting" name="privateSetting" class="form-control cursor-pointer">
                                <option v-for="option in allChosen" :key="option" class="cursor-pointer">{{ option }}
                                </option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="pt-4 pl-4 pr-4">
                    <textarea class="p-4 w-full bg-gray-100 rounded-lg border" placeholder="Bạn đang nghĩ gì?"></textarea>
                </div>
                <div class="p-4">
                    <div class="p-4 bg-white border border-gray-200 text-center rounded-lg flex">
                        <span class="flex-none mr-20">Thêm vào bài viết của bạn </span>
                        <div class="flex-1">
                            <font-awesome-icon icon="fa-regular fa-images" class="text-sea-green cursor-pointer" />
                        </div>
                        <div class="flex-1">
                            <font-awesome-icon icon="fa-solid fa-user-tag" class="text-leaf-green cursor-pointer" />
                        </div>
                    </div>
                </div>
                <div class="profile-action">
                    <div class="mx-4 flex items-center justify-center cursor-pointer bg-blue-400 rounded-lg h-8">
                        Đăng
                    </div>
                </div>
            </v-card-text>
        </v-card>
    </v-dialog>
</template>

<script>
import axios from 'axios'

export default {
    name: 'FeedView',

    components: {
    },
    created() {
        const userString = localStorage.getItem('user');
        if (userString) {
            this.user = JSON.parse(userString);
        }
    },
    data() {
        return {
            posts: [],
            user: null,
            body: '',
            showPostVisible: false,
            privateSetting: 'Công khai',
            allChosen: [
                'Công khai',
                'Bạn bè',
                'Một số bạn bè',
                'Chỉ mình tôi'
            ]
        }
    },

    mounted() {
        this.getFeed()
    },

    methods: {
        getFeed() {
            axios
                .get('/api/posts/')
                .then(response => {
                    console.log('data', response.data)

                    this.posts = response.data
                })
                .catch(error => {
                    console.log('error', error)
                })
        },
        deletePost(id) {
            this.posts = this.posts.filter(post => post.id !== id)
        },
        showPostOption() {
            this.showPostVisible = true;
        },
        closePostOption() {
            this.showPostVisible = false;
        }
    }
}
</script>

<style scoped lang = "scss">
.dialog-container {

    width: 500px;

    .dialog-component {
        .dialog-title {
            .title {
                display: flex;
                justify-content: space-between;
                align-items: center;

                .icon-close {
                    cursor: pointer;
                }
            }
        }

        .dialog-content {
            padding: 0;
            display: flex;
            flex-direction: column;
            position: relative;
            height: auto;

            .text-sea-green {
                color: #0077cc;
            }

            .text-leaf-green {
                color: #00aa33;
            }

            .avatar-container {
                height: 80px;
                display: flex;
                align-items: center;
                margin-left: 14px;
            }

            .avatar-wrapper {
                width: 50px;
                height: 50px;
                display: flex;
                justify-content: center;
                align-items: center;
                overflow: hidden;
                border-radius: 50%;
                margin-right: 16px;
            }

            .avatar {
                width: 100%;
                height: 100%;
                object-fit: cover;
            }

            .detail {
                display: flex;
                flex-direction: column;
                justify-content: center;

                .form-control {
                    background-color: #cccc;
                    border-radius: 5%;
                    text-align: center;
                }
            }

            .friend-info {
                display: flex;
                align-items: center;
            }

            .profile-action {
                height: 40px;
            }
        }
    }
}
</style>
