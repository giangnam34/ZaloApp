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
                        <span class="p-4 bg-gray-100 rounded-lg cursor-pointer d-block w-100" @click="showPostOption">{{
                            user.fullName }} ơi, bạn đang nghĩ gì thế?</span>
                    </div>
                </div>

                <div v-for="feed in feeds" v-bind:key="feed.user_id" class="p-4 bg-white border border-gray-200 rounded-lg">
                    <div class="mb-6 flex items-center justify-between">
                        <div class="flex items-center space-x-6">
                            <img :src="feed.avatar"
                                class="w-[50px] h-[50px] rounded-full flex justify-center align-center mr-4">
                            <div class="wrap-title">
                                <p><strong>{{ feed.name }}</strong></p>
                                <div class="wrap-icon">
                                    <div v-if="feed.audience === 'AllFriend'">
                                        <font-awesome-icon icon="fa-solid fa-user-group" />
                                        Bạn bè
                                    </div>
                                    <div v-if="feed.audience === 'OnlyMe'">
                                        <font-awesome-icon icon="fa-solid fa-lock" />
                                        Chỉ mình tôi
                                    </div>
                                    <div v-if="feed.audience === 'SomeOneCanSee'">
                                        <font-awesome-icon icon="fa-solid fa-user" />
                                        Một số bạn bè
                                    </div>
                                    <div v-if="feed.audience === 'AllExceptSomeOne'">
                                        <font-awesome-icon icon="fa-solid fa-user-minus" />
                                        Bạn bè ngoại trừ
                                    </div>
                                </div>
                            </div>
                        </div>

                        <p class="text-gray-600">{{ formatDate(feed.updated_date) }}</p>
                    </div>

                    <p class="mb-3">{{ feed.content }}</p>

                    <img :src="feed.image" class="w-full h-[500px] rounded-lg">

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
                <div class="file-list-container" :style="{ height: fileListHeight }">
                    <div class="pl-4 pr-4">
                        <textarea class="p-4 w-full bg-gray-100 rounded-lg border"
                            placeholder="Bạn đang nghĩ gì?"></textarea>
                    </div>
                    <div class="pl-4 pr-4">
                        <div v-for="(file, index) in newFeed.files" :key="index">
                            <div v-if="index === 0" class="cursor-pointer" @click="showUpdateFileDialog">
                                <img class="upload-file" v-if="isImage(file)" :src="getUrl(file)" alt="Selected Image" />
                                <video v-else controls width="300" class="upload-file">
                                    <source :src="getUrl(file)" type="video/mp4" />
                                    Your browser does not support the video tag.
                                </video>
                            </div>
                            <div v-if="index === 1" class="cursor-pointer" @click="showUpdateFileDialog">
                                <div class="image-container">
                                    <img class="upload-file" v-if="isImage(file)" :src="getUrl(file)"
                                        alt="Selected Image" />
                                    <video v-else controls width="300" class="upload-file">
                                        <source :src="getUrl(file)" type="video/mp4" />
                                        Your browser does not support the video tag.
                                    </video>
                                    <div class="overlay" v-if="newFeed.files.length > 1 && newFeed.files.length !== 2">
                                        +{{ newFeed.files.length - 2 }}
                                    </div>
                                </div>
                            </div>
                            <div class="profile-action pt-1" v-if="index === newFeed.files.length - 1">
                                <div class="flex items-center justify-center cursor-pointer bg-blue-400 rounded-lg h-8"
                                    @click="removeFiles">
                                    Xóa tất cả ảnh/video
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="pb-2 pl-4 pr-4">
                    <div class="p-4 bg-white border border-gray-200 text-center rounded-lg flex">
                        <span class="flex-none mr-20">Thêm vào bài viết của bạn </span>
                        <div class="flex-1">
                            <label for="fileInput" class="text-sea-green cursor-pointer">
                                <font-awesome-icon icon="fa-regular fa-images" />
                            </label>
                            <input type="file" id="fileInput" ref="fileInput" @change="onFileSelected"
                                style="display: none;" multiple accept="image/*,video/*">
                        </div>
                        <div class="flex-1">
                            <font-awesome-icon icon="fa-solid fa-user-tag" class="text-leaf-green cursor-pointer"
                                @click="showChooseTag" />
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
    <v-dialog class="dialog-container" v-model="showUpdateFile" max-width="500px" @click:outside="closeUpdateFileDialog">
        <v-card class="dialog-component">
            <v-card-title class="dialog-title">
                <h2 class="title">Chỉnh sửa ảnh và video tải lên
                    <div class="icon-close" @click="closeUpdateFileDialog"><font-awesome-icon icon="fa-solid fa-x" /></div>
                </h2>
            </v-card-title>
            <hr style="border: none; border-bottom: 1px solid #ccc;">
            <v-card-text class="dialog-content">
                <div class="pt-4 pl-4 pr-4">
                    <div class="update-file-container">
                        <div v-for="(file, index) in newFeed.files" :key="index" class="position-relative">
                            <img class="upload-file" v-if="isImage(file)" :src="getUrl(file)" alt="Selected Image" />
                            <video v-else controls width="300" class="upload-file">
                                <source :src="getUrl(file)" type="video/mp4" />
                                Your browser does not support the video tag.
                            </video>
                            <div class="close-icon" @click="removeFile(index)"><font-awesome-icon icon="fa-solid fa-x" />
                            </div>
                        </div>
                    </div>
                </div>
                <div class="mt-4"></div>
                <div class="profile-action">
                    <label for="addFileInput" class="cursor-pointer">
                        <div class="items-center text-center cursor-pointer bg-blue-400 rounded-lg h-10 mx-4">
                            Thêm ảnh/video
                        </div>
                    </label>
                    <input type="file" id="addFileInput" ref="fileInput" @change="addFile" style="display: none;" multiple
                        accept="image/*,video/*">
                </div>
            </v-card-text>
        </v-card>
    </v-dialog>
    <v-dialog class="dialog-container" v-model="hasTagFriend" max-width="500px" @click:outside="closeChooseTagDialog">
        <v-card class="dialog-component">
            <v-card-title class="dialog-title">
                <h2 class="title">Gắn thẻ người khác
                    <div class="icon-close" @click="closeChooseTagDialog"><font-awesome-icon icon="fa-solid fa-x" /></div>
                </h2>
            </v-card-title>
            <hr style="border: none; border-bottom: 1px solid #ccc;">
            <v-card-text class="dialog-content">
                <div class="pt-4 pl-4 pr-4">
                    <input type="text" v-model="searchText" placeholder="Tìm kiếm theo tên" class="search-input" />
                    <div v-if="newFeed.friendTag.length !== 0"><span>Đã gắn thẻ</span></div>
                    <div class="update-file-container" style="height:100px" v-if="newFeed.friendTag.length !== 0">
                        <div v-for="friend in newFeed.friendTag" v-bind:key="friend.phoneNumber" class="position-relative">
                            <div class="friend-info cursor-pointer m-2" @click="deleteFriendTag(friend)">
                                <div :class="{ 'wrap': shouldWrap }" class="detail" style="border: 1px solid #ccc;
                                                           border-radius: 8px;
                                                           display: flex;
                                                           justify-content: space-between;
                                                           align-items: center;
                                                           padding: 8px;">
                                    <span class="color-blue">{{ friend.name }}
                                        <font-awesome-icon icon="fa-solid fa-x" /></span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div><span>Danh sách bạn bè</span></div>
                    <div class="friend-list-container">
                        <div v-for="friend in filteredFriends" v-bind:key="friend.phoneNumber" class="position-relative">
                            <div class="friend-info friend-container" @click="addFriendTag(friend)">
                                <div class="avatar-container">
                                    <div class="avatar-wrapper">
                                        <img :src="friend.avatar" class="avatar">
                                    </div>
                                </div>
                                <div class="detail">
                                    <span>{{ friend.name }}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="mt-4"></div>
                <div class="profile-action">
                    <div class="items-center text-center cursor-pointer bg-blue-400 rounded-lg h-10 mx-4">
                        Xong
                    </div>
                </div>
            </v-card-text>
        </v-card>
    </v-dialog>
</template>

<script>
import axios from 'axios';
import { format } from 'date-fns';

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
    computed: {
        filteredFriends() {
            const normalizedSearchText = this.searchText.toLowerCase();
            return this.friends.filter(friend =>
                friend.name.toLowerCase().includes(normalizedSearchText)
            );
        },
    },
    data() {
        return {
            posts: [],
            user: null,
            fileListHeight: "100px",
            body: '',
            newFeed: { content: '', files: [], audience: 'AllFriend', friendTag: [] },
            hasTagFriend: false,
            showPostVisible: false,
            privateSetting: 'Bạn bè',
            showUpdateFile: false,
            allChosen: [
                'Bạn bè',
                'Bạn bè ngoại trừ',
                'Một số bạn bè',
                'Chỉ mình tôi'
            ],
            feeds: [
                { id: 1, name: "Từ Thanh Thoại", user_id: 1, content: "Ảnh đẹp dữ", created_date: new Date("2002-12-17T17:50:00"), updated_date: new Date("2002-12-17T17:50:00"), image: 'https://i.imgur.com/gEKsypv.jpg', avatar: 'https://i.imgur.com/z9fdzMv.jpg', audience: 'AllFriend' },
                { id: 2, name: "Võ Giang Nam", user_id: 2, content: "Ảnh đẹp ghê ha", created_date: new Date("2002-12-17T18:50:00"), updated_date: new Date("2002-12-17T18:50:00"), image: 'https://i.imgur.com/gEKsypv.jpg', avatar: 'https://i.imgur.com/z9fdzMv.jpg', audience: 'OnlyMe' },
                { id: 3, name: "Kẻ Áo Đen", user_id: 3, content: "Ehehehehehehehe", created_date: new Date("2002-12-17T19:50:00"), updated_date: new Date("2002-12-17T19:50:00"), image: 'https://i.imgur.com/gEKsypv.jpg', avatar: 'https://i.imgur.com/z9fdzMv.jpg', audience: 'SomeOneCanSee' },
            ],
            friends: [
                { phoneNumber: '0965556651', name: "Võ Giang Nam", avatar: 'https://i.imgur.com/gEKsypv.jpg' },
                { phoneNumber: '0965556652', name: "Từ Thanh Thoại", avatar: 'https://i.imgur.com/gEKsypv.jpg' },
                { phoneNumber: '0965556653', name: "Kẻ Áo Đen", avatar: 'https://i.imgur.com/gEKsypv.jpg' },
                { phoneNumber: '0965556654', name: "Kẻ Áo Vàng", avatar: 'https://i.imgur.com/gEKsypv.jpg' },
                { phoneNumber: '0965556655', name: "Kẻ Áo Xanh", avatar: 'https://i.imgur.com/gEKsypv.jpg' },
                { phoneNumber: '0965556656', name: "Kẻ Áo Đỏ", avatar: 'https://i.imgur.com/gEKsypv.jpg' },
                { phoneNumber: '0965556657', name: "Kẻ Áo Tím", avatar: 'https://i.imgur.com/gEKsypv.jpg' },
                { phoneNumber: '0965556658', name: "Kẻ Áo Cam", avatar: 'https://i.imgur.com/gEKsypv.jpg' }
            ],
            searchText: "",
        }
    },
    mounted() {
        this.getFeed();
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
        },
        formatDate(date) {
            return format(date, 'HH:mm dd/MM/yyyy');
        },
        onFileSelected(event) {
            const files = event.target.files;
            if (files.length != 0) {
                this.newFeed.files = [];
                for (let i = 0; i < files.length; i++) {
                    const file = files[i];
                    this.newFeed.files.push(file);
                }
                this.fileListHeight = "400px";
            }

        },
        isImage(file) {
            return file.type.startsWith('image/');
        },
        getUrl(file) {
            return URL.createObjectURL(file);
        },
        showUpdateFileDialog() {
            this.showUpdateFile = true;
            this.showPostVisible = false;
        },
        closeUpdateFileDialog() {
            this.showUpdateFile = false;
            this.showPostVisible = true;
        },
        addFile(event) {
            const files = event.target.files;
            if (files.length != 0) {
                for (let i = 0; i < files.length; i++) {
                    const file = files[i];
                    this.newFeed.files.push(file);
                }
            }
        },
        removeFiles() {
            this.newFeed.files = [];
            this.fileListHeight = "100px";
        },
        removeFile(index) {
            this.newFeed.files.splice(index, 1);
            if (this.newFeed.files.length === 0) {
                this.fileListHeight = "100px";
            }
        },
        showChooseTag() {
            this.hasTagFriend = true;
            this.showPostVisible = false;
        },
        closeChooseTagDialog() {
            this.hasTagFriend = false;
            this.showPostVisible = true;
        },
        deleteFriendByPhoneNumber(phoneNumber) {
            this.friends = this.friends.filter(friend => friend.phoneNumber !== phoneNumber);
        },
        addFriendTag(friend) {
            this.newFeed.friendTag.push(friend);
            this.deleteFriendByPhoneNumber(friend.phoneNumber);
        },
        deleteFriendTag(friend) {
            const indexInTag = this.newFeed.friendTag.findIndex(taggedFriend => taggedFriend.phoneNumber === friend.phoneNumber);
            if (indexInTag !== -1) {
                this.newFeed.friendTag.splice(indexInTag, 1);
            }

            const indexInFriends = this.friends.findIndex(existingFriend => existingFriend.phoneNumber === friend.phoneNumber);
            if (indexInFriends === -1) {
                this.friends.push(friend);
            }
        },
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

    .upload-file {
        width: 372px;
        height: 200px;
    }

    .image-container {
        position: relative;
        display: inline-block;
    }

    .overlay {
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: center;
        background-color: rgba(0, 0, 0, 0.5);
        color: #fff;
        font-size: 24px;
    }

    .position-relative {
        position: relative;
    }

    .close-icon {
        position: absolute;
        top: 8px;
        right: 8px;
        width: 30px;
        height: 30px;
        background-color: white;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        cursor: pointer;
    }

    .close-icon font-awesome-icon {
        color: gray;
        font-size: 16px;
    }

    .friend-container {
        transition: box-shadow 0.3s, transform 0.3s;
        cursor: pointer;
    }

    .friend-container:hover {
        box-shadow: 0 0 5px rgba(0, 0, 0, 0.3);
        transform: scale(1.05);
    }

    .search-input {
        margin-top: 8px;
        padding: 4px;
        width: 100%;
        box-sizing: border-box;
    }

    .friend-list-container {
        height: 250px;
        overflow-y: auto;
        border: 1px solid #ccc;
        border-radius: 8px;
    }

    .update-file-container {
        height: 400px;
        overflow-y: scroll;
        border: 1px solid #ccc;
        border-radius: 8px;
    }

    .file-list-container {
        overflow-y: auto;
    }

    .color-blue {
        color: #007BFF;
    }
}
</style>
