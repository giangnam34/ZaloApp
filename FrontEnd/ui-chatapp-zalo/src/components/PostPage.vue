<template>
    <div class="px-8 py-6 bg-gray-100 overflow-y-auto max-h-[737.5px]">
        <div class="max-w-none w-auto mx-auto grid grid-cols-4 gap-4">
            <div class="main-left col-span-1">
                <div class="p-4 bg-white border border-gray-200 text-center rounded-lg">
                    <img :src="user.imageAvatarUrl" class="w-[200px] h-[200px] rounded-full mx-auto mb-6">

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

                    <img :src="feed.files[0]" class="w-full h-[500px] rounded-lg cursor-pointer"
                        v-if="feed.files.length > 0" @click="openFeedInfo(feed)" />

                    <div v-if="feed.files.length > 1" class="image-container-feed cursor-pointer"
                        @click="openFeedInfo(feed)">
                        <img :src="feed.files[1]" class="w-full h-[500px] rounded-lg" />
                        <div class="overlay-feed" v-if="feed.files.length !== 2">
                            <div class="overlay-content-feed">
                                +{{ feed.files.length - 2 }}
                            </div>
                        </div>
                    </div>

                    <div class="my-3 flex">
                        <div v-if="!feed.isLike" class="flex-1 flex items-center mr-2">
                            <font-awesome-icon icon="fa-regular fa-thumbs-up" class="text-lg text-blue mr-2" />
                            <span class="text-gray-500 text-lg hover:underline cursor-pointer">{{ feed.likeCount }}</span>
                        </div>
                        <div v-else class="flex-1 flex items-center mr-2">
                            <font-awesome-icon icon="fa-regular fa-thumbs-up" class="text-lg text-blue mr-2" />
                            <span class="text-gray-500 text-lg hover:underline cursor-pointer">Bạn và {{ feed.likeCount }}
                                người khác</span>
                        </div>
                        <div class="flex-1 flex items-center justify-end">
                            <div class="flex items-center space-x-2 ml-auto hover:underline cursor-pointer"
                                @click="openFeedInfo(feed)">
                                <font-awesome-icon icon="fa-regular fa-comment" />
                                <span class="text-gray-500 text-lg">{{ feed.commentCount }} bình luận</span>
                            </div>
                        </div>
                    </div>
                    <hr style="border: none; border-bottom: 1px solid #ccc;">
                    <div class="flex justify-between items-center">
                        <button v-if="!feed.isLike"
                            class="button p-2 text-black cursor-pointer flex-1 justify-between items-center"
                            @mouseover="handleMouseOver" @mouseout="handleMouseOut"
                            @click="likePost(feed)"><font-awesome-icon icon="fa-regular fa-thumbs-up"
                                class="mr-2" />Thích</button>
                        <button v-else
                            class="button p-2 text-black cursor-pointer flex-1 justify-between items-center text-blue"
                            @mouseover="handleMouseOver" @mouseout="handleMouseOut"
                            @click="likePost(feed)"><font-awesome-icon icon="fa-regular fa-thumbs-up"
                                class="mr-2" />Thích</button>
                        <button class="button p-2 text-black cursor-pointer flex-1 justify-between items-center"
                            @click="openFeedInfo(feed)" @mouseover="handleMouseOver"
                            @mouseout="handleMouseOut"><font-awesome-icon icon="fa-regular fa-comment" class="mr-2" />Bình
                            luận</button>
                        <button class="button p-2 text-black cursor-pointer flex-1 justify-between items-center"
                            @mouseover="handleMouseOver" @mouseout="handleMouseOut"><font-awesome-icon
                                icon="fa-solid fa-gear" class="mr-2" />Thiết lập bài
                            viết</button>
                    </div>
                    <hr style="border: none; border-bottom: 1px solid #ccc;">
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
                        <textarea class="p-4 w-full bg-gray-100 rounded-lg border" placeholder="Bạn đang nghĩ gì?"
                            v-model="newFeed.content"></textarea>
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
                <div class="profile-action" @click="createPost">
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
                <div class="profile-action" @click="closeChooseTagDialog">
                    <div class="items-center text-center cursor-pointer bg-blue-400 rounded-lg h-10 mx-4 px-4 py-2">
                        Xong
                    </div>
                </div>
            </v-card-text>
        </v-card>
    </v-dialog>
    <v-dialog class="dialog-container-feed" v-model="showVisibleInfoFeed" max-width="800px" @click:outside="closeFeedInfo">
        <v-card class="dialog-component-1">
            <v-card-title class="dialog-title">
                <h2 class="title">Bài viết của Thoại
                    <div class="icon-close" @click="closeFeedInfo"><font-awesome-icon icon="fa-solid fa-x" /></div>
                </h2>
            </v-card-title>
            <hr style="border: none; border-bottom: 1px solid #ccc;">
            <v-card-text class="dialog-content py-0 px-0 overflow-auto">
                <div class="bg-white p-4">
                    <div class="mb-6 flex items-center justify-between">
                        <div class="flex items-center space-x-6">
                            <img :src="showingFeed.avatar"
                                class="w-[50px] h-[50px] rounded-full flex justify-center align-center mr-4">
                            <div class="wrap-title">
                                <p><strong>{{ showingFeed.name }}</strong></p>
                                <div class="wrap-icon">
                                    <div v-if="showingFeed.audience === 'AllFriend'">
                                        <font-awesome-icon icon="fa-solid fa-user-group" />
                                        Bạn bè
                                    </div>
                                    <div v-if="showingFeed.audience === 'OnlyMe'">
                                        <font-awesome-icon icon="fa-solid fa-lock" />
                                        Chỉ mình tôi
                                    </div>
                                    <div v-if="showingFeed.audience === 'SomeOneCanSee'">
                                        <font-awesome-icon icon="fa-solid fa-user" />
                                        Một số bạn bè
                                    </div>
                                    <div v-if="showingFeed.audience === 'AllExceptSomeOne'">
                                        <font-awesome-icon icon="fa-solid fa-user-minus" />
                                        Bạn bè ngoại trừ
                                    </div>
                                </div>
                            </div>
                        </div>

                        <p class="text-gray-600">{{ formatDate(showingFeed.updated_date) }}</p>
                    </div>

                    <p class="mb-3">{{ showingFeed.content }}</p>

                    <img :src="showingFeed.files[0]" class="w-full h-[500px] rounded-lg cursor-pointer"
                        v-if="showingFeed.files.length > 0" @click="openFeedInfo(showingFeed)" />

                    <div v-if="showingFeed.files.length > 1" class="image-container-feed cursor-pointer"
                        @click="openFeedInfo(showingFeed)">
                        <img :src="showingFeed.files[1]" class="w-full h-[500px] rounded-lg" />
                        <div class="overlay-feed" v-if="showingFeed.files.length !== 2">
                            <div class="overlay-content-feed">
                                +{{ showingFeed.files.length - 2 }}
                            </div>
                        </div>
                    </div>

                    <div class="my-3 flex">
                        <div v-if="!showingFeed.isLike" class="flex-1 flex items-center mr-2">
                            <font-awesome-icon icon="fa-regular fa-thumbs-up" class="text-lg text-blue mr-2" />
                            <span class="text-gray-500 text-lg hover:underline cursor-pointer">{{ showingFeed.likeCount
                            }}</span>
                        </div>
                        <div v-else class="flex-1 flex items-center mr-2">
                            <font-awesome-icon icon="fa-regular fa-thumbs-up" class="text-lg text-blue mr-2" />
                            <span class="text-gray-500 text-lg hover:underline cursor-pointer">Bạn và {{
                                showingFeed.likeCount }}
                                người khác</span>
                        </div>
                        <div class="flex-1 flex items-center justify-end">
                            <div class="flex items-center space-x-2 ml-auto hover:underline cursor-pointer"
                                @click="openFeedInfo(showingFeed)">
                                <font-awesome-icon icon="fa-regular fa-comment" />
                                <span class="text-gray-500 text-lg">{{ showingFeed.commentCount }} bình luận</span>
                            </div>
                        </div>
                    </div>
                    <hr style="border: none; border-bottom: 1px solid #ccc;">
                    <div class="flex justify-between items-center">
                        <button v-if="!showingFeed.isLike"
                            class="button p-2 text-black cursor-pointer flex-1 justify-between items-center"
                            @mouseover="handleMouseOver" @mouseout="handleMouseOut"
                            @click="likePost(showingFeed)"><font-awesome-icon icon="fa-regular fa-thumbs-up"
                                class="mr-2" />Thích</button>
                        <button v-else
                            class="button p-2 text-black cursor-pointer flex-1 justify-between items-center text-blue"
                            @mouseover="handleMouseOver" @mouseout="handleMouseOut"
                            @click="likePost(showingFeed)"><font-awesome-icon icon="fa-regular fa-thumbs-up"
                                class="mr-2" />Thích</button>
                        <button class="button p-2 text-black cursor-pointer flex-1 justify-between items-center"
                            @click="focusComment" @mouseover="handleMouseOver" @mouseout="handleMouseOut"><font-awesome-icon
                                icon="fa-regular fa-comment" class="mr-2" />Bình
                            luận</button>
                        <button class="button p-2 text-black cursor-pointer flex-1 justify-between items-center"
                            @mouseover="handleMouseOver" @mouseout="handleMouseOut"><font-awesome-icon
                                icon="fa-solid fa-gear" class="mr-2" />Thiết lập bài
                            viết</button>
                    </div>
                    <hr style="border: none; border-bottom: 1px solid #ccc;">
                </div>
                <div class="comment-container mr-3 ml-3">
                    <div v-for="comment in comments" v-bind:key="comment.id">
                        <div class="mb-2 items-center justify-between">
                            <div class="flex items-center">
                                <img :src="comment.avatar"
                                    class="w-[50px] h-[50px] rounded-full flex justify-center align-center mr-2">
                                <div class="comment-content rounded-lg bg-gray-200 p-2">
                                    <div>
                                        <p><strong>{{ comment.fullName }}</strong></p>
                                    </div>
                                    <div>
                                        <p>{{ comment.content }}</p>
                                    </div>
                                </div>
                            </div>
                            <div class="ml-15">
                                <div class="comment-image">
                                    <img class="file-comment rounded-lg" :src="comment.file" alt="Selected Image" />
                                    <!-- <img class="upload-file-comment" v-if="isImage(comment.file)"
                                        :src="getUrl(comment.file)" alt="Selected Image" />
                                    <video v-else controls width="300" class="upload-file-comment">
                                        <source :src="getUrl(newComment.file)" type="video/mp4" />
                                        Your browser does not support the video tag.
                                    </video> -->
                                </div>
                                <p class="text-gray-600">{{ formatDate(comment.updatedDate) }}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </v-card-text>
        </v-card>
        <v-card class="dialog-component-2">
            <v-card-title class="dialog-title flex items-center justify-between">
                <div class="pr-2 flex-1">
                    <textarea ref="comment" class="p-4 w-full bg-gray-100 rounded-lg border comment-input"
                        placeholder="Viết bình luận..." v-model="newComment.content"></textarea>
                </div>
                <div class="flex flex-none items-center space-x-2">
                    <input type="file" @change="handleFileChange" ref="fileInputComment" style="display: none;" />
                    <v-btn v-if="newComment.file === null" @click="openFilePicker">Chọn file</v-btn>
                    <div v-else class="cursor-pointer" @click="openFilePicker">
                        <img class="upload-file-comment" v-if="isImage(newComment.file)" :src="getUrl(newComment.file)"
                            alt="Selected Image" />
                        <video v-else controls width="300" class="upload-file-comment">
                            <source :src="getUrl(newComment.file)" type="video/mp4" />
                            Your browser does not support the video tag.
                        </video>
                    </div>
                    <v-btn @click="postComment">Đăng</v-btn>
                </div>
            </v-card-title>
        </v-card>
    </v-dialog>
</template>

<script>
import axios from 'axios';
import { format } from 'date-fns';
import { useToast } from "vue-toastification";

export default {
    name: 'FeedView',

    components: {
    },
    setup() {
        // Get toast interface
        const toast = useToast();
        return { toast }
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
            newComment: { senderPhoneNumber: '', file: null, content: '' },
            user: null,
            fileListHeight: "100px",
            body: '',
            newFeed: { content: '', files: [], audience: 'AllFriend', friendTag: [] },
            hasTagFriend: false,
            showPostVisible: false,
            privateSetting: 'Bạn bè',
            showUpdateFile: false,
            showVisibleInfoFeed: false,
            showingFeed: null,
            allChosen: [
                'Bạn bè',
                'Bạn bè ngoại trừ',
                'Một số bạn bè',
                'Chỉ mình tôi'
            ],
            feeds: [
                {
                    id: 1, name: "Từ Thanh Thoại", phoneNumber: "0968322444", content: "Ảnh đẹp dữ", created_date: new Date("2002-12-17T17:50:00"), updated_date: new Date("2002-12-17T17:50:00"),
                    files: ['https://i.imgur.com/gEKsypv.jpg', 'https://i.imgur.com/gEKsypv.jpg', 'https://i.imgur.com/gEKsypv.jpg'], avatar: 'https://i.imgur.com/z9fdzMv.jpg', audience: 'AllFriend'
                    , likeCount: 2, liked: ["0968322555", "0968322666"], tagCount: 1, tagged: ["0968322555"], commentCount: 4, isLike: false
                },
                {
                    id: 2, name: "Võ Giang Nam", phoneNumber: "0968322555", content: "Ảnh đẹp ghê ha", created_date: new Date("2002-12-17T18:50:00"), updated_date: new Date("2002-12-17T18:50:00"),
                    files: ['https://i.imgur.com/gEKsypv.jpg', 'https://i.imgur.com/gEKsypv.jpg'], avatar: 'https://i.imgur.com/z9fdzMv.jpg', audience: 'OnlyMe'
                    , likeCount: 1, liked: ["0968322666"], tagCount: 2, tagged: ["0968322444", "0968322666"], commentCount: 4, isLike: false
                },
                {
                    id: 3, name: "Kẻ Áo Đen", phoneNumber: "0968322666", content: "Ehehehehehehehe", created_date: new Date("2002-12-17T19:50:00"), updated_date: new Date("2002-12-17T19:50:00"),
                    files: ['https://i.imgur.com/gEKsypv.jpg', 'https://i.imgur.com/gEKsypv.jpg'], avatar: 'https://i.imgur.com/z9fdzMv.jpg', audience: 'SomeOneCanSee'
                    , likeCount: 2, liked: ["0968322555", "0968322444"], tagCount: 0, tagged: [], commentCount: 4, isLike: false
                },
            ],
            comments: [
                {
                    id: 1, fullName: "Từ Thanh Thoại", content: "Ảnh đẹp vờ lờ", avatar: "https://i.imgur.com/gEKsypv.jpg",
                    file: "https://i.imgur.com/gEKsypv.jpg", createdDate: new Date("2002-12-17T18:50:00"), updatedDate: new Date("2002-12-17T18:50:00")
                },
                {
                    id: 2, fullName: "Võ Giang Nam", content: "Ảnh đẹp qtqd", avatar: "https://i.imgur.com/gEKsypv.jpg",
                    file: "https://i.imgur.com/gEKsypv.jpg", createdDate: new Date("2002-12-17T18:50:00"), updatedDate: new Date("2002-12-17T18:50:00")
                },
                {
                    id: 3, fullName: "Chú bé đần", content: "Ảnh của tui đẹp hơn", avatar: "https://i.imgur.com/gEKsypv.jpg",
                    file: "https://i.imgur.com/gEKsypv.jpg", createdDate: new Date("2002-12-17T18:50:00"), updatedDate: new Date("2002-12-17T18:50:00")
                },
                {
                    id: 4, fullName: "Thợ săn ảnh đẹp", content: "Ảnh này mới đẹp nè", avatar: "https://i.imgur.com/gEKsypv.jpg",
                    file: "https://i.imgur.com/gEKsypv.jpg", createdDate: new Date("2002-12-17T18:50:00"), updatedDate: new Date("2002-12-17T18:50:00")
                },
                {
                    id: 5, fullName: "Kaito Kid nè", content: "ảnh của m xấu điên", avatar: "https://i.imgur.com/gEKsypv.jpg",
                    file: "https://i.imgur.com/gEKsypv.jpg", createdDate: new Date("2002-12-17T18:50:00"), updatedDate: new Date("2002-12-17T18:50:00")
                },
            ]
            ,
            friends: [
                { phoneNumber: '0968322444', name: "Võ Giang Nam", avatar: 'https://i.imgur.com/gEKsypv.jpg' },
                { phoneNumber: '0965556652', name: "Từ Thanh Thoại", avatar: 'https://i.imgur.com/gEKsypv.jpg' },
                { phoneNumber: '0968322666', name: "Kẻ Áo Đen", avatar: 'https://i.imgur.com/gEKsypv.jpg' },
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
        this.fetchAvatar();
    },

    methods: {
        handleMouseOver(event) {
            event.target.classList.add("hovered");
        },
        handleMouseOut(event) {
            event.target.classList.remove("hovered");
        },
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
        async createPost() {
            try {

                if (this.privateSetting === 'Bạn bè') {
                    this.newFeed.audience = 'AllFriend'
                } else if (this.privateSetting === 'Chỉ mình tôi') {
                    this.newFeed.audience = 'OnlyMe'
                } else if (this.privateSetting === 'Một số bạn bè') {
                    this.newFeed.audience = 'SomeOneCanSee'
                } else {
                    this.newFeed.audience = 'AllExceptSomeOne'
                }

                const listFriendTag = [];

                for (let friendTagKey in this.newFeed.friendTag) {
                    if (Object.prototype.hasOwnProperty.call(this.newFeed.friendTag, friendTagKey)) {
                        const friendTagValue = this.newFeed.friendTag[friendTagKey];
                        listFriendTag.push(friendTagValue.phoneNumber);
                    }
                }

                console.log(listFriendTag)

                console.log(typeof (listFriendTag))

                const formData = new FormData();

                formData.append('content', this.newFeed.content);
                formData.append('audience', this.newFeed.audience);
                formData.append('userTagIDList', listFriendTag);

                console.log(this.newFeed.files)

                for (let i = 0; i < this.newFeed.files.length; i++) {
                    formData.append('files', this.newFeed.files[i]);
                }

                console.log(formData);

                console.log(localStorage.getItem("token"))

                const response = await axios.post(`social-media/create-new-post`, formData, {
                    headers: {
                        'Authorization': localStorage.getItem("token"),
                        'Content-Type': 'multipart/form-data',
                    }
                });

                if (response.status === 200) {
                    this.showPostVisible = false;
                    this.toast.success(response.data, { timeout: 3000 });
                } else {
                    this.toast.error(response.data, { timeout: 3000 });
                }
            } catch (error) {
                if (error.response) {
                    if (error.response.status === 400) {
                        this.toast.error(error.response.data, { timeout: 3000 });
                    } else {
                        this.toast.error(error.response.data, { timeout: 3000 });
                    }
                } else if (error.request) {
                    this.toast.error('Không nhận được phản hồi từ máy chủ. Vui lòng thử lại!', { timeout: 3000 });
                } else {
                    this.toast.error('Error setting up the request:' + error.message, { timeout: 3000 });
                }
            }
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
            console.log('user.imageAvatarUrl changed:', this.user.imageAvatarUrl);
        },
        likePost(feed) {
            const foundFeed = this.feeds.find(f => f.id === feed.id);

            if (foundFeed) {
                const updatedFeed = Object.assign({}, foundFeed, { isLike: !foundFeed.isLike });

                const index = this.feeds.findIndex(f => f.id === feed.id);

                if (this.showVisibleInfoFeed) {
                    this.showingFeed.isLike = updatedFeed.isLike;
                }

                if (index !== -1) {
                    this.feeds.splice(index, 1, updatedFeed);
                }
            }
        },
        openFeedInfo(feed) {
            this.showVisibleInfoFeed = true;
            this.showingFeed = feed;
            this.newComment.senderPhoneNumber = this.user.phoneNumber;
        },
        closeFeedInfo() {
            this.showVisibleInfoFeed = false;
        },
        handleFileChange(event) {
            const selectedFile = event.target.files[0];
            console.log(selectedFile);
            this.newComment.file = selectedFile;
        },
        openFilePicker() {
            const fileInput = this.$refs.fileInputComment;
            if (fileInput) {
                fileInput.click();
            }
        },
        focusComment() {
            this.$refs.comment.focus();
        },
        postComment() {

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
        overflow-x: hidden;
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
        overflow-x: hidden;
        height: 250px;
        overflow-y: auto;
        border: 1px solid #ccc;
        border-radius: 8px;
    }

    .update-file-container {
        height: 400px;
        overflow-x: hidden;
        overflow-y: scroll;
        border: 1px solid #ccc;
        border-radius: 8px;
    }

    .file-list-container {
        overflow-y: auto;
        overflow-x: hidden;
    }

    .color-blue {
        color: #007BFF;
    }
}

.dialog-container-feed {

    width: 800px;
    overflow-y: hidden;

    .dialog-component-1 {
        max-height: 1000px;
        height: 800px;

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
            height: 800px;

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

    .dialog-component-2 {
        max-height: 1000px;
        height: 280px;
        overflow-y: hidden;

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
            height: 800px;

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
        overflow-x: hidden;
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
        overflow-x: hidden;
        height: 250px;
        overflow-y: auto;
        border: 1px solid #ccc;
        border-radius: 8px;
    }

    .update-file-container {
        height: 400px;
        overflow-x: hidden;
        overflow-y: scroll;
        border: 1px solid #ccc;
        border-radius: 8px;
    }

    .file-list-container {
        overflow-y: auto;
        overflow-x: hidden;
    }

    .color-blue {
        color: #007BFF;
    }
}

.button {
    background-color: white;
}

.button:hover,
.button.hovered {
    background-color: #ccc;
    border: 1px solid #ccc;
    border-radius: 5px;
}

.image-container-feed {
    position: relative;
}

.overlay-feed {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.7);
    color: white;
    display: flex;
    align-items: center;
    border-radius: 4px;
    justify-content: center;
    font-size: 24px;
}

.overflow-auto {
    overflow-y: auto;
}

.comment-input {
    height: 70px;
    font-size: 18px;
}

.upload-file-comment {
    height: 75px;
    width: 120px;
}

.file-comment {
    max-height: 210px;
    max-width: 210px;
}
</style>
