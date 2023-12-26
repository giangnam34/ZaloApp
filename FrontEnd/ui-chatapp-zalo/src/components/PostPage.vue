<template>
    <div class="post-container">
        <div class="container px-8 py-6 bg-gray-100 overflow-y-auto">
            <div class="max-w-none w-auto mx-auto grid grid-cols-4 gap-4">
                <div class="main-left col-span-1">
                    <div class="p-4 bg-white border border-gray-200 text-center rounded-lg">
                        <img :src="user.imageAvatarUrl" class="w-[200px] h-[200px] rounded-full mx-auto mb-6">

                        <p><strong>{{ user.fullName }}</strong></p>

                        <div class="mt-6 flex space-x-8 justify-around">
                            <p class="text-xs text-gray-500">{{ listFriends.length }} friends</p>
                            <p class="text-xs text-gray-500">{{ feeds.length }} posts</p>
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

                    <div v-for="feed in sortedFeeds" v-bind:key="feed.id"
                        class="p-4 bg-white border border-gray-200 rounded-lg">
                        <div v-if="feed.postFather === null">
                            <div class="mb-6 flex items-center justify-between">
                                <div class="flex items-center space-x-6">
                                    <img :src="feed.userPost.imageAvatar"
                                        class="w-[50px] h-[50px] rounded-full flex justify-center align-center mr-4">
                                    <div class="wrap-title flex">
                                        <div>
                                            <div class="flex">
                                                <p class="name"><strong>{{ feed.userPost.userName }}</strong></p>
                                                <p class="text-gray-600 ml-2 date">{{ formatTimeDifference(new
                                                    Date(feed.updatedAt))
                                                }}
                                                </p>
                                            </div>
                                            <div class="wrap-icon">
                                                <div v-if="feed.audience === 'Public'">
                                                    <font-awesome-icon icon="fa-solid fa-earth-americas" />
                                                    Công khai
                                                </div>
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
                                        <div style="margin-left: 307px;" id="more-icon">
                                            <div class="action cursor-pointer">
                                                <div class="popover-action-container" @click:outside="hidePopover">
                                                    <a id="ellipsis-icon"
                                                        @click="(event) => handleClickAction(event, feed)">
                                                        <font-awesome-icon icon="fa-solid fa-ellipsis-vertical" />
                                                    </a>
                                                    <div class="popoverAction"
                                                        v-show="showPopupVisible && !showVisibleInfoFeed && clickedFeed.userPost.phoneNumber === user.phoneNumber"
                                                        :style="{ right: popoverRight, top: popoverTop }">
                                                        <div class="popover-body">
                                                            <div class="popover-item"
                                                                @click="showFoundUserDialog(showingFeed.user_id)">
                                                                <div>
                                                                    Xóa bài viết
                                                                </div>
                                                            </div>
                                                            <div class="popover-item" @click="openUpdatePost(clickedFeed)">
                                                                <div>
                                                                    Chỉnh sửa bài viết
                                                                </div>
                                                            </div>
                                                            <div class="separator"></div>
                                                            <div class="popover-item" @click="hidePopover">
                                                                <div>
                                                                    Hủy
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <p class="mb-3">{{ feed.content }}</p>

                            <div v-if="feed.files.length > 0">
                                <img :src="feed.files[0]" class="w-full h-[500px] rounded-lg cursor-pointer"
                                    v-if="isImage(feed.files[0])" @click="openFeedInfo(feed)" />
                                <video v-else controls width="300" class="w-full h-[500px] rounded-lg cursor-pointer">
                                    <source :src="feed.files[0]" type="video/mp4" @click="openFeedInfo(feed)" />
                                    Your browser does not support the video tag.
                                </video>
                            </div>

                            <div v-if="feed.files.length > 1" class="image-container-feed cursor-pointer"
                                @click="openFeedInfo(feed)">
                                <img v-if="isImage(feed.files[1])" :src="feed.files[1]"
                                    class="w-full h-[500px] rounded-lg" />
                                <video v-else controls width="300" class="w-full h-[500px] rounded-lg">
                                    <source :src="feed.files[1]" type="video/mp4" />
                                    Your browser does not support the video tag.
                                </video>
                                <div class="overlay-feed" v-if="feed.files.length !== 2">
                                    <div class="overlay-content-feed">
                                        +{{ feed.files.length - 2 }}
                                    </div>
                                </div>
                            </div>

                            <div class="my-3 flex">
                                <div v-if="!feed.isLike" class="flex-1 flex items-center mr-2">
                                    <font-awesome-icon v-if="feed.userLikeList !== null && feed.userLikeList.length > 0"
                                        icon="fa-regular fa-thumbs-up" class="text-lg text-blue mr-2" />
                                    <span class="text-gray-500 text-lg hover:underline cursor-pointer">
                                        <template v-if="feed.userLikeList == null || feed.userLikeList.length === 0">

                                        </template>
                                        <template v-else>
                                            {{ feed.userLikeList.length }}
                                        </template>
                                    </span>
                                </div>
                                <div v-else class="flex-1 flex items-center mr-2">
                                    <font-awesome-icon v-if="feed.userLikeList !== null && feed.userLikeList.length > 0"
                                        icon="fa-regular fa-thumbs-up" class="text-lg text-blue mr-2" />
                                    <span class="text-gray-500 text-lg hover:underline cursor-pointer">
                                        <template v-if="feed.userLikeList.length === 1">
                                            {{ user.fullName }}
                                        </template>
                                        <template v-else>
                                            Bạn và {{ feed.userLikeList.length - 1 }} người khác
                                        </template>
                                    </span>
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
                                    @mouseout="handleMouseOut"><font-awesome-icon icon="fa-regular fa-comment"
                                        class="mr-2" />Bình
                                    luận</button>
                                <button class="button p-2 text-black cursor-pointer flex-1 justify-between items-center"
                                    @click="sharePost(feed)" @mouseover="handleMouseOver"
                                    @mouseout="handleMouseOut"><font-awesome-icon icon="fa-solid fa-share" class="mr-2" />
                                    Chia
                                    sẻ
                                </button>
                            </div>
                            <hr style="border: none; border-bottom: 1px solid #ccc;">
                        </div>
                        <div v-else>
                            <div v-if="feed.userLikeList.length === 0">
                                <div class="mb-6 flex items-center justify-between">
                                    <div class="flex items-center space-x-6">
                                        <img :src="feed.userPost.imageAvatar"
                                            class="w-[50px] h-[50px] rounded-full flex justify-center align-center mr-4">
                                        <div class="wrap-title flex">
                                            <div>
                                                <div class="flex">
                                                    <p class="name"><strong>{{ feed.userPost.userName }}</strong></p>
                                                    <p class="text-gray-600 ml-2 date"></p>
                                                </div>
                                                <div class="wrap-icon">
                                                    <div>
                                                        <font-awesome-icon icon="fa-solid fa-earth-americas" />
                                                        Công khai
                                                    </div>
                                                </div>
                                            </div>
                                            <div style="margin-left: 307px;" id="more-icon">
                                                <div class="action cursor-pointer">
                                                    <div class="popover-action-container" @click:outside="hidePopover">
                                                        <a id="ellipsis-icon"
                                                            @click="(event) => handleClickAction(event, feed)">
                                                            <font-awesome-icon icon="fa-solid fa-ellipsis-vertical" />
                                                        </a>
                                                        <div class="popoverAction"
                                                            v-show="showPopupVisible && !showVisibleInfoFeed && clickedFeed.userPost.phoneNumber === user.phoneNumber"
                                                            :style="{ right: popoverRight, top: popoverTop }">
                                                            <div class="popover-body">
                                                                <div class="popover-item"
                                                                    @click="showFoundUserDialog(showingFeed.user_id)">
                                                                    <div>
                                                                        Xóa bài viết
                                                                    </div>
                                                                </div>
                                                                <div class="separator"></div>
                                                                <div class="popover-item" @click="hidePopover">
                                                                    <div>
                                                                        Hủy
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="p-4 border rounded-lg">
                                    <div v-if="feed.postFather.files.length > 0" class="image-container-feed cursor-pointer mb-2"
                                        @click="openFeedInfo(feed.postFather)">
                                        <img :src="feed.postFather.files[0]"
                                            class="w-full h-[500px] rounded-lg cursor-pointer"
                                            v-if="isImage(feed.postFather.files[0])"
                                            @click="openFeedInfo(feed.postFather)" />
                                        <video v-else controls width="300"
                                            class="w-full h-[500px] rounded-lg cursor-pointer">
                                            <source :src="feed.postFather.files[0]" type="video/mp4"
                                                @click="openFeedInfo(feed.postFather)" />
                                            Your browser does not support the video tag.
                                        </video>
                                        <div class="overlay-feed"
                                            v-if="feed.postFather.files.length > 1 && feed.postFather.files.length !== 2">
                                            <div class="overlay-content-feed">
                                                +{{ feed.postFather.files.length - 1 }}
                                            </div>
                                        </div>
                                    </div>
                                    <div class="mb-6 flex items-center justify-between">
                                        <div class="flex items-center space-x-6">
                                            <img :src="feed.postFather.userPost.imageAvatar"
                                                class="w-[50px] h-[50px] rounded-full flex justify-center align-center mr-4">
                                            <div class="wrap-title flex">
                                                <div>
                                                    <div class="flex">
                                                        <p class="name"><strong>{{ feed.postFather.userPost.userName
                                                        }}</strong>
                                                        </p>
                                                        <p class="text-gray-600 ml-2 date">{{ formatTimeDifference(new
                                                            Date(feed.postFather.updatedAt))
                                                        }}
                                                        </p>
                                                    </div>
                                                    <div class="wrap-icon">
                                                        <div v-if="feed.postFather.audience === 'Public'">
                                                            <font-awesome-icon icon="fa-solid fa-earth-americas" />
                                                            Công khai
                                                        </div>
                                                        <div v-if="feed.postFather.audience === 'AllFriend'">
                                                            <font-awesome-icon icon="fa-solid fa-user-group" />
                                                            Bạn bè
                                                        </div>
                                                        <div v-if="feed.postFather.audience === 'OnlyMe'">
                                                            <font-awesome-icon icon="fa-solid fa-lock" />
                                                            Chỉ mình tôi
                                                        </div>
                                                        <div v-if="feed.postFather.audience === 'SomeOneCanSee'">
                                                            <font-awesome-icon icon="fa-solid fa-user" />
                                                            Một số bạn bè
                                                        </div>
                                                        <div v-if="feed.postFather.audience === 'AllExceptSomeOne'">
                                                            <font-awesome-icon icon="fa-solid fa-user-minus" />
                                                            Bạn bè ngoại trừ
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <p class="mb-3">{{ feed.postFather.content }}</p>
                                </div>


                                <div class="my-3 flex">
                                    <div v-if="!feed.isLike" class="flex-1 flex items-center mr-2">
                                        <font-awesome-icon v-if="feed.userLikeList !== null && feed.userLikeList.length > 0"
                                            icon="fa-regular fa-thumbs-up" class="text-lg text-blue mr-2" />
                                        <span class="text-gray-500 text-lg hover:underline cursor-pointer">
                                            <template v-if="feed.userLikeList == null || feed.userLikeList.length === 0">

                                            </template>
                                            <template v-else>
                                                {{ feed.userLikeList.length }}
                                            </template>
                                        </span>
                                    </div>
                                    <div v-else class="flex-1 flex items-center mr-2">
                                        <font-awesome-icon v-if="feed.userLikeList !== null && feed.userLikeList.length > 0"
                                            icon="fa-regular fa-thumbs-up" class="text-lg text-blue mr-2" />
                                        <span class="text-gray-500 text-lg hover:underline cursor-pointer">
                                            <template v-if="feed.userLikeList.length === 1">
                                                {{ user.fullName }}
                                            </template>
                                            <template v-else>
                                                Bạn và {{ feed.userLikeList.length - 1 }} người khác
                                            </template>
                                        </span>
                                    </div>

                                    <div class="flex-1 flex items-center justify-end">
                                        <div class="flex items-center space-x-2 ml-auto hover:underline cursor-pointer"
                                            @click="openFeedInfo(feed.postFather)">
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
                                        @mouseout="handleMouseOut"><font-awesome-icon icon="fa-regular fa-comment"
                                            class="mr-2" />Bình
                                        luận</button>
                                </div>
                                <hr style="border: none; border-bottom: 1px solid #ccc;">
                            </div>
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
                                <img :src="user.imageAvatarUrl" class="avatar">
                            </div>
                        </div>
                        <div class="detail">
                            <p><strong>{{ user.fullName }}</strong></p>
                            <select v-model="privateSetting" name="privateSetting" class="form-control cursor-pointer">
                                <option v-for="option in allChosen" :key="option" class="cursor-pointer">
                                    <!-- <i :class="getIconClassPostOption(option)"></i> -->
                                    {{ option }}
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
                                    Trình duyệt không hỗ trợ định dạng này
                                </video>
                            </div>
                            <div v-if="index === 1" class="cursor-pointer" @click="showUpdateFileDialog">
                                <div class="image-container">
                                    <img class="upload-file" v-if="isImage(file)" :src="getUrl(file)"
                                        alt="Selected Image" />
                                    <video v-else controls width="300" class="upload-file">
                                        <source :src="getUrl(file)" type="video/mp4" />
                                        Trình duyệt không hỗ trợ định dạng này
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
                <div v-if="!showUpdatePostVisible">
                    <div class="pt-4 pl-4 pr-4">
                        <div class="update-file-container">
                            <div v-for="(file, index) in newFeed.files" :key="index" class="position-relative">
                                <img class="upload-file" v-if="isImage(file)" :src="getUrl(file)" alt="Selected Image" />
                                <video v-else controls width="300" class="upload-file">
                                    <source :src="getUrl(file)" type="video/mp4" />
                                    Your browser does not support the video tag.
                                </video>
                                <div class="close-icon" @click="removeFile(index)"><font-awesome-icon
                                        icon="fa-solid fa-x" />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div v-else>
                    <div class="pt-4 pl-4 pr-4">
                        <div class="update-file-container">
                            <div v-for="(file, index) in updateFeed.files" :key="index" class="position-relative">
                                <div v-if="(typeof file) === 'string'">
                                    <img class="upload-file" v-if="isImage(file)" :src="file" alt="Selected Image" />
                                    <video v-else controls width="300" class="upload-file">
                                        <source :src="file" type="video/mp4" />
                                        Your browser does not support the video tag.
                                    </video>
                                    <div class="close-icon" @click="removeFile(index)"><font-awesome-icon
                                            icon="fa-solid fa-x" />
                                    </div>
                                </div>
                                <div v-else>
                                    <img class="upload-file" v-if="isImage(file)" :src="getUrl(file)"
                                        alt="Selected Image" />
                                    <video v-else controls width="300" class="upload-file">
                                        <source :src="getUrl(file)" type="video/mp4" />
                                        Your browser does not support the video tag.
                                    </video>
                                    <div class="close-icon" @click="removeFile(index)"><font-awesome-icon
                                            icon="fa-solid fa-x" />
                                    </div>
                                </div>
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
                <div v-if="!showUpdatePostVisible">
                    <div class="pt-4 pl-4 pr-4">
                        <input type="text" v-model="searchText" placeholder="Tìm kiếm theo tên" class="search-input" />
                        <div v-if="newFeed.friendTag.length !== 0"><span>Đã gắn thẻ</span></div>
                        <div class="update-file-container" style="height:100px" v-if="newFeed.friendTag.length !== 0">
                            <div v-for="friend in newFeed.friendTag" v-bind:key="friend.phoneNumber"
                                class="position-relative">
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
                            <div v-for="friend in filteredFriends" v-bind:key="friend.phoneNumber"
                                class="position-relative">
                                <div class="friend-info friend-container" @click="addFriendTag(friend)">
                                    <div class="avatar-container">
                                        <div class="avatar-wrapper">
                                            <img :src="friend.imageAvatar" class="avatar">
                                        </div>
                                    </div>
                                    <div class="detail">
                                        <span>{{ friend.userName }}</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="mt-4"></div>
                </div>
                <div v-else>
                    <div class="pt-4 pl-4 pr-4">
                        <input type="text" v-model="searchText" placeholder="Tìm kiếm theo tên" class="search-input" />
                        <div v-if="updateFeed.userTagList.length !== 0"><span>Đã gắn thẻ</span></div>
                        <div class="update-file-container" style="height:100px" v-if="updateFeed.userTagList.length !== 0">
                            <div v-for="friend in updateFeed.userTagList" v-bind:key="friend.phoneNumber"
                                class="position-relative">
                                <div class="friend-info cursor-pointer m-2" @click="deleteFriendTag(friend)">
                                    <div :class="{ 'wrap': shouldWrap }" class="detail" style="border: 1px solid #ccc;
                                                           border-radius: 8px;
                                                           display: flex;
                                                           justify-content: space-between;
                                                           align-items: center;
                                                           padding: 8px;">
                                        <span class="color-blue">{{ friend.userName }}
                                            <font-awesome-icon icon="fa-solid fa-x" /></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div><span>Danh sách bạn bè</span></div>
                        <div class="friend-list-container">
                            <div v-for="friend in filteredFriends" v-bind:key="friend.phoneNumber"
                                class="position-relative">
                                <div class="friend-info friend-container" @click="addFriendTag(friend)">
                                    <div class="avatar-container">
                                        <div class="avatar-wrapper">
                                            <img :src="friend.imageAvatar" class="avatar">
                                        </div>
                                    </div>
                                    <div class="detail">
                                        <span>{{ friend.userName }}</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="mt-4"></div>
                </div>
                <div class="profile-action" @click="closeChooseTagDialog">
                    <div class="items-center text-center cursor-pointer bg-blue-400 rounded-lg h-10 mx-4 px-4 py-2">
                        Xong
                    </div>
                </div>
            </v-card-text>
        </v-card>
    </v-dialog>
    <!-- Pop up bài viết người dùng -->
    <v-dialog class="dialog-container-feed" v-model="showVisibleInfoFeed" max-width="800px" @click:outside="closeFeedInfo">
        <v-card class="dialog-component-1">
            <v-card-title class="dialog-title">
                <h2 class="title">Bài viết của {{ showingFeed.userPost.userName }}
                    <div class="icon-close" @click="closeFeedInfo"><font-awesome-icon icon="fa-solid fa-x" /></div>
                </h2>
            </v-card-title>
            <hr style="border: none; border-bottom: 1px solid #ccc;">
            <v-card-text class="dialog-content py-0 px-0 overflow-auto">
                <div class="bg-white p-4">
                    <div class="mb-6 flex items-center justify-between">
                        <div class="flex items-center space-x-6">
                            <img :src="showingFeed.userPost.imageAvatar"
                                class="w-[50px] h-[50px] rounded-full flex justify-center align-center mr-4">
                            <div class="wrap-title flex">
                                <div>
                                    <div class="flex">
                                        <p class="name"><strong>{{ showingFeed.userPost.userName }}</strong></p>
                                        <p class="text-gray-600 ml-2 date">{{ formatTimeDifference(new
                                            Date(showingFeed.updatedAt))
                                        }}
                                        </p>
                                    </div>
                                    <div class="wrap-icon">
                                        <div v-if="showingFeed.audience === 'Public'">
                                            <font-awesome-icon icon="fa-solid fa-earth-americas" />
                                            Công khai
                                        </div>
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
                                <div style="margin-left: 307px;" id="more-icon">
                                    <div class="action cursor-pointer">
                                        <div class="popover-action-container" @click:outside="hidePopover">
                                            <a id="ellipsis-icon" @click="(event) => handleClickAction(event, showingFeed)">
                                                <font-awesome-icon icon="fa-solid fa-ellipsis-vertical" />
                                            </a>
                                            <div class="popoverAction"
                                                v-show="showPopupVisible && clickedFeed.userPost.phoneNumber === user.phoneNumber"
                                                style=" right: 252px; top: 40px; ">
                                                <div class="popover-body">
                                                    <div class="popover-item"
                                                        @click="showFoundUserDialog(showingFeed.user_id)">
                                                        <div>
                                                            Xóa bài viết
                                                        </div>
                                                    </div>
                                                    <div class="popover-item" @click="openUpdatePost(clickedFeed)">
                                                        <div>
                                                            Chỉnh sửa bài viết
                                                        </div>
                                                    </div>
                                                    <div class="separator"></div>
                                                    <div class="popover-item" @click="hidePopover">
                                                        <div>
                                                            Hủy
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <p class="mb-3">{{ showingFeed.content }}</p>

                    <!-- Hiển thị ảnh -->

                    <div>
                        <!-- Single Image -->
                        <!-- <img v-if="showingFeed.files.length === 1" :src="showingFeed.files[0]"
                            class="w-full h-[500px] rounded-lg cursor-pointer" @click="openFullImage(showingFeed.files)" /> -->

                        <!-- Multiple Images -->
                        <swiper class="swiper" :modules="modules">
                            <swiper-slide v-for="(image, index) in showingFeed.files" :key="index">
                                <div class="image-container-feed cursor-pointer"
                                    @click="openFullImage(showingFeed.files, index)">
                                    <img :src="image" class="w-full h-[500px] rounded-lg" />
                                </div>
                            </swiper-slide>
                        </swiper>
                    </div>

                    <!-- <img :src="showingFeed.files[0]" class="w-full h-[500px] rounded-lg cursor-pointer"
                        v-if="showingFeed.files.length > 0" @click="openFeedInfo(showingFeed)" />

                    <div v-if="showingFeed.files.length > 1" class="image-container-feed cursor-pointer"
                        @click="openFeedInfo(showingFeed)">
                        <img :src="showingFeed.files[1]" class="w-full h-[500px] rounded-lg" />
                        <div class="overlay-feed" v-if="showingFeed.files.length !== 2">
                            <div class="overlay-content-feed">
                                +{{ showingFeed.files.length - 2 }}
                            </div>
                        </div>
                    </div> -->

                    <div class="my-3 flex">
                        <div v-if="!showingFeed.isLike" class="flex-1 flex items-center mr-2">
                            <font-awesome-icon icon="fa-regular fa-thumbs-up" class="text-lg text-blue mr-2" />
                            <span class="text-gray-500 text-lg hover:underline cursor-pointer">{{
                                showingFeed.userLikeList.length
                            }}</span>
                        </div>
                        <div v-else class="flex-1 flex items-center mr-2">
                            <font-awesome-icon icon="fa-regular fa-thumbs-up" class="text-lg text-blue mr-2" />
                            <span class="text-gray-500 text-lg hover:underline cursor-pointer">
                                <template v-if="showingFeed.userLikeList === null || showingFeed.userLikeList.length === 0">
                                    {this.user.fullName}
                                </template>
                                <template v-else>
                                    Bạn và {{ showingFeed.userLikeList.length }} người khác
                                </template>
                            </span>
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
                            @click="sharePost(showingFeed)" @mouseover="handleMouseOver"
                            @mouseout="handleMouseOut"><font-awesome-icon icon="fa-solid fa-share" /> Chia sẻ
                        </button>
                    </div>
                    <hr style="border: none; border-bottom: 1px solid #ccc;">
                </div>
                <div class="comment-container mr-3 ml-3">
                    <div v-for="comment in comments" v-bind:key="comment.id">
                        <div class="mb-2 items-center justify-between">
                            <div class="flex items-center">
                                <img :src="comment.userComment.imageAvatar"
                                    class="w-[50px] h-[50px] rounded-full flex justify-center align-center mr-2">
                                <div class="comment-content rounded-lg bg-gray-200 p-2">
                                    <div>
                                        <p><strong>{{ comment.userComment.userName }}</strong></p>
                                    </div>
                                    <div>
                                        <p>{{ comment.content }}</p>
                                    </div>
                                </div>
                                <div style="position: relative; display: inline-block;">

                                    <!-- div show-more-option -->
                                    <div @click="showTableOption" class="show-more-option"
                                        style="cursor: pointer; margin-left: 10px;">
                                        <p>...</p>
                                    </div>

                                    <!-- div table-option-comment -->
                                    <div class="table-option-comment rounded-lg bg-gray-200 p-2"
                                        style="position: absolute; top: 100%; left: 0px; margin-left: 10px; min-width: 100px; display: none">
                                        <p>Chỉnh sửa</p>
                                        <p>Xóa</p>
                                    </div>

                                </div>

                            </div>
                            <div class="ml-15">
                                <div class="comment-image">
                                    <img class="upload-file" v-if="checkIsImageUrl(comment.contentMedia)"
                                        :src="getUrl(comment.contentMedia)" alt="Selected Image" />
                                    <video v-else controls width="300" class="upload-file">
                                        <source :src="getUrl(comment.contentMedia)" type="video/mp4" />
                                        Trình duyệt không hỗ trợ định dạng này
                                    </video>
                                    <!-- <img class="file-comment rounded-lg" :src="comment.contentMedia" /> -->
                                </div>
                                <p class="text-gray-600">{{ formatDate(comment.updatedAt) }}</p>
                            </div>
                            <div class="ml-15 info-comment">
                                <p class="like-button-comment" @click="likeComment(comment.idComment)"
                                    @mouseover="changeColorButtonComment('like-button-comment', true)"
                                    @mouseout="changeColorButtonComment('like-button-comment', false)"
                                    :class="{ 'like-button-comment-active': comment.isUserLike }">Thích </p>
                                <p class="reply-button-comment"
                                    @mouseover="changeColorButtonComment('reply-button-comment', true)"
                                    @mouseout="changeColorButtonComment('reply-button-comment', false)">Phản hồi</p>
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
                            Trình duyệt không hỗ trợ định dạng này
                        </video>
                    </div>
                    <v-btn @click="postComment(showingFeed.id)">Đăng</v-btn>
                </div>
            </v-card-title>
        </v-card>
    </v-dialog>
    <v-dialog class="dialog-container" v-model="showUpdatePostVisible" max-width="500px"
        @click:outside="closeUpdatePostOption">
        <v-card class="dialog-component">
            <v-card-title class="dialog-title">
                <h2 class="title">Chỉnh sửa bài viết
                    <div class="icon-close" @click="closeUpdatePostOption"><font-awesome-icon icon="fa-solid fa-x" /></div>
                </h2>
            </v-card-title>
            <hr style="border: none; border-bottom: 1px solid #ccc;">
            <v-card-text class="dialog-content">
                <div style="display: flex; align-items: center; justify-content: space-between;">
                    <div class="friend-info">
                        <div class="avatar-container">
                            <div class="avatar-wrapper">
                                <img :src="user.imageAvatarUrl" class="avatar">
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
                            v-model="updateFeed.content"></textarea>
                    </div>
                    <div class="pl-4 pr-4">
                        <div v-for="(file, index) in updateFeed.files" :key="index">
                            <div v-if="(typeof file) === 'string'">
                                <div v-if="index === 0" class="cursor-pointer" @click="showUpdateFileDialog">
                                    <img class="upload-file" v-if="isImage(file)" :src="file" alt="Selected Image" />
                                    <video v-else controls width="300" class="upload-file">
                                        <source :src="file" type="video/mp4" />
                                        Your browser does not support the video tag.
                                    </video>
                                </div>
                                <div v-if="index === 1" class="cursor-pointer" @click="showUpdateFileDialog">
                                    <div class="image-container">
                                        <img class="upload-file" v-if="isImage(file)" :src="file" alt="Selected Image" />
                                        <video v-else controls width="300" class="upload-file">
                                            <source :src="file" type="video/mp4" />
                                            Your browser does not support the video tag.
                                        </video>
                                        <div class="overlay"
                                            v-if="updateFeed.files.length > 1 && updateFeed.files.length !== 2">
                                            +{{ updateFeed.files.length - 2 }}
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div v-else>
                                <div v-if="index === 0" class="cursor-pointer" @click="showUpdateFileDialog">
                                    <img class="upload-file" v-if="isImage(file)" :src="getUrl(file)"
                                        alt="Selected Image" />
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
                                        <div class="overlay"
                                            v-if="updateFeed.files.length > 1 && updateFeed.files.length !== 2">
                                            +{{ updateFeed.files.length - 2 }}
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="profile-action pt-1" v-if="index === updateFeed.files.length - 1">
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
                        Lưu bài viết
                    </div>
                </div>
            </v-card-text>
        </v-card>
    </v-dialog>
</template>

<script>
import axios from 'axios';
import { format } from 'date-fns';
import { useToast } from "vue-toastification";
import { Swiper, SwiperSlide } from 'vue-awesome-swiper';
import 'swiper/css';
import 'swiper/css/navigation'

export default {
    name: 'FeedView',

    components: {
        Swiper,
        SwiperSlide,
    },
    setup() {
        // Get toast interface
        const toast = useToast();
        const onSwiper = (s) => {
            console.log('kjjsnjkanjkdsn', s)
        }
        return { toast, onSwiper }
    },
    created() {
        const userString = localStorage.getItem('user');
        if (userString) {
            this.user = JSON.parse(userString);
        }
        this.fetchFeed();
        this.getListOfFriends();
    },
    computed: {
        filteredFriends() {
            const normalizedSearchText = this.searchText.toLowerCase();
            return this.friends.filter(friend =>
                friend.userName.toLowerCase().includes(normalizedSearchText)
            );
        },
        sortedFeeds() {
            return this.feeds.slice().sort((a, b) => {
                const dateA = new Date(a.createdAt);
                const dateB = new Date(b.createdAt);

                return dateB - dateA;
            });
        },
    },
    data() {
        return {
            // swiperOptions: {
            //     navigation: {
            //         nextEl: '.swiper-button-next',
            //         prevEl: '.swiper-button-prev',
            //     },
            // },
            hoveredItem: '',
            selectedItem: '',
            popoverRight: 0,
            popoverTop: 0,
            posts: [],
            newComment: {
                postId: null,
                topComment: null,
                content: '',
                file: null
            },
            user: null,
            fileListHeight: "100px",
            body: '',
            newFeed: { content: '', files: [], audience: 'AllFriend', friendTag: [] },
            hasTagFriend: false,
            showPostVisible: false,
            privateSetting: 'Bạn bè',
            showUpdateFile: false,
            showVisibleInfoFeed: false,
            showPopupVisible: false,
            showingFeed: null,
            showUpdatePostVisible: false,
            listFriends: [],
            clickedFeed: null,
            updateFeed: null,
            allChosen: [
                'Công khai',
                'Bạn bè',
                'Bạn bè ngoại trừ',
                'Một số bạn bè',
                'Chỉ mình tôi'
            ],
            feeds: [
                {
                    id: null,
                    content: "",
                    audience: "",
                    postFather: null,
                    isLike: null,
                    userTagList: [
                        {
                            userName: "",
                            imageAvatar: "",
                            phoneNumber: ""
                        }
                    ],
                    userLikeList: [
                        {
                            userName: "",
                            imageAvatar: "",
                            phoneNumber: ""
                        }
                    ],
                    userShareList: [
                        {
                            userName: "",
                            imageAvatar: "",
                            phoneNumber: ""
                        }
                    ],
                    files: [],
                    createdAt: "",
                    updatedAt: "",
                    userPost: {
                        userName: "",
                        imageAvatar: "",
                        phoneNumber: ""
                    }
                }
            ],
            comments: [
                {
                    idComment: null,
                    isUserLike: null,
                    userComment: {
                        userName: '',
                        imageAvatar: '',
                        phoneNumber: ''
                    },
                    userLike: [
                        {
                            userName: '',
                            imageAvatar: '',
                            phoneNumber: ''
                        }
                    ],
                    infoCommentList: [

                    ],
                    content: '',
                    createdAt: '',
                    updatedAt: ''
                }
            ],
            friends: [],
            searchText: "",
        }
    },
    mounted() {
        this.fetchFeed();
        this.fetchAvatar();
    },

    methods: {
        selectItem(item) {
            this.selectedItem = item;
        },
        isSelected(item) {
            return this.selectedItem === item;
        },
        togglePopover(item) {
            this.selectItem(item);
        },
        hidePopover() {
            console.log("in")
            this.showPopupVisible = false;
        },
        handleClickAction(event, feed) {
            const rect = event.target.getBoundingClientRect();
            const x = rect.left;
            const y = rect.top;
            this.popoverRight = x - 654 + 'px';
            this.popoverTop = y - 2 + 'px';
            this.clickedFeed = feed;
            this.showPopupVisible = true;
            event.stopPropagation();
        },
        formatTimeDifference(date) {
            const now = new Date();
            const timeDiff = Math.abs(now - date);

            const seconds = Math.floor(timeDiff / 1000);
            const minutes = Math.floor(timeDiff / (1000 * 60));
            const hours = Math.floor(timeDiff / (1000 * 60 * 60));
            const days = Math.floor(timeDiff / (1000 * 60 * 60 * 24));

            if (seconds < 60) {
                return `${seconds} giây trước`;
            } else if (minutes < 60) {
                return `${minutes} phút trước`;
            } else if (hours < 24) {
                return `${hours} giờ trước`;
            } else {
                // Adjust the order based on magnitude
                if (seconds >= 60 && minutes < 60) {
                    return `${minutes} phút trước`;
                } else if (minutes >= 60 && hours < 24) {
                    return `${hours} giờ trước`;
                } else {
                    return `${days} ngày trước`;
                }
            }
        },
        handleMouseOver(event) {
            //console.log("Gọi hàm: handleMouseOver");
            event.target.classList.add("hovered");
        },
        handleMouseOut(event) {
            //console.log("Gọi hàm: handleMouseOut");
            event.target.classList.remove("hovered");
        },
        openFullImage() {
            // Hiển thị ảnh full màn hình và điều hướng qua các ảnh

        },
        // getIconClassPostOption(option){
        //     console.log("Gọi hàm getIconClassPostOption(option)");
        //     console.log(option);
        //     switch (option) {
        //     case ' Công khai':
        //         return 'fa-solid fa-earth-americas';
        //     case ' Bạn bè':
        //         return 'fa-solid fa-user-group';
        //     case ' Bạn bè ngoại trừ':
        //         return 'fa-solid fa-lock';
        //     case ' Một số bạn bè':
        //         return 'fa-solid fa-user';
        //     case ' Chỉ mình tôi':
        //         return 'fa-solid fa-user-minus';
        //     default:
        //         return 'fa-solid fa-earth-americas'; 
        // }
        // },
        fetchFeed() {
            console.log("Gọi hàm: fetchFeed()");
            // console.log("User đang đăng nhập: " + this.user);
            axios
                .get('/social-media/get-post',/**{
                    headers: {
                        'Authorization': localStorage.getItem("token"),
                        'Content-Type': 'multipart/form-data',
                    }
                }**/)
                .then(response => {
                    //console.log("Response status: " + response.status)
                    //response.data.getInfoPostResponse.forEach(p => console.log("Updated at: " + p.updatedAt))
                    this.feeds = response.data.getInfoPostResponse;
                    this.feeds.forEach(feed => this.updateIsUserLikePost(feed));
                    console.log("Danh sách bài viết: ", this.feeds);
                })
                .catch(error => {
                    console.log('error', error)
                })
        },
        showTableOption() {
            console.log("Gọi hàm showTableOption");
            const button = document.getElementsByClassName('table-option-comment')[0];
            if (button.style.display === 'none')
                button.style.display = 'block';
            else
                button.style.display = 'none';
        },
        showOptionComment() {
            console.log("Gọi hàm showOptionComment");
            const button = document.getElementsByClassName('show-more-option')[0];
            //console.log("Button: ", button);
            button.style.display = "inline-block";
        },
        hideOptionComment() {
            console.log("Gọi hàm hideOptionComment");
            const button = document.getElementsByClassName('show-more-option')[0];
            const buttonShowOption = document.getElementsByClassName('table-option-comment')[0];
            if (!buttonShowOption.style.display === 'none')
                button.style.display = "none";
            //console.log("Button: ", button);
        },
        likeComment(commentId) {
            console.log("Gọi hàm likeComment()");
            axios
                .post(`/social-media/like-comment/${commentId}`)
                .then(response => {
                    if (response.status === 200) {
                        console.log(response.data);
                        const comment = this.comments.filter(comment => comment.idComment === commentId)[0];
                        if (response.data === 'Thích bình luận thành công!')
                            comment.isUserLike = true;
                        else
                            comment.isUserLike = false;
                    }

                })
                .catch(error => {
                    console.log('error', error)
                })

        },
        fetchComment(postId) {
            console.log("Gọi hàm: fetchComment()");
            axios
                .get(`/social-media/get-all-info-comment/${postId}`)
                .then(response => {
                    //console.log("Response status: " + response.status)
                    //response.data.getInfoPostResponse.forEach(p => console.log("Updated at: " + p.updatedAt))
                    this.comments = response.data;
                    this.comments.forEach(comment => this.updateIsUserLikeComment(comment));
                })
                .catch(error => {
                    console.log('error', error)
                })
        },
        deletePost(id) {
            console.log("Gọi hàm: deletePost");
            this.posts = this.posts.filter(post => post.id !== id)
        },
        showPostOption() {
            console.log("Gọi hàm: showPostOption");
            this.showPostVisible = true;
        },
        closePostOption() {
            console.log("Gọi hàm: closePostOption");
            this.showPostVisible = false;
        },
        changeColorButtonComment(nameButton, isHovered) {
            const textDecoration = isHovered ? 'underline' : 'none';
            console.log("Name button: ", nameButton);
            console.log("Is hoevered: ", isHovered);
            console.log(document.getElementsByClassName(nameButton).style);
            const button = document.getElementsByClassName(nameButton)[0];
            // console.log("Button: ", button);
            button.style.textDecoration = textDecoration;
        },
        formatDate(date) {
            try {
                // console.log("Gọi hàm: formatDate(date)");
                // console.log("Giá trị của ngày: " + date)
                // console.log("Kiểu ngày: " + typeof (date))

                // Kiểm tra nếu date không tồn tại hoặc rỗng
                if (date == null || date.length === 0) {
                    return null;
                }

                // Gọi hàm format trong một khối try
                return format(date, 'HH:mm dd/MM/yyyy');
            } catch (error) {
                //console.error("Lỗi trong quá trình định dạng ngày:", error);
                // Trả về giá trị mặc định hoặc xử lý theo ý muốn của bạn khi có lỗi
                return null;
            }
        },
        onFileSelected(event) {
            console.log("Gọi hàm: onFileSelected");
            const files = event.target.files;
            if (files.length != 0) {
                if (!this.showUpdatePostVisible) {
                    this.newFeed.files = [];
                    for (let i = 0; i < files.length; i++) {
                        const file = files[i];
                        this.newFeed.files.push(file);
                    }
                } else {
                    this.updateFeed.files = [];
                    for (let i = 0; i < files.length; i++) {
                        const file = files[i];
                        this.updateFeed.files.push(file);
                    }
                }
                this.fileListHeight = "400px";
            }

        },
        // isImage(file) {
        //     try {
        //         console.log("Gọi hàm: isImage");
        //         return file.type.startsWith('image/');
        //     } catch (error) {
        //         console.log("Hàm isImage lỗi rồi");
        //     }
        // },
        checkIsImageUrl(mediaUrl) {
            try {
                console.log("Gọi hàm: checkIsImageUrl");
                console.log("Media url: ", mediaUrl);
                return mediaUrl.includes("/media/getImage/");
            } catch (error) {
                console.log("Hàm isImage lỗi rồi");
                return false;
            }
        },
        getUrl(file) {
            try {
                console.log("Gọi hàm: getUrl");
                return URL.createObjectURL(file);
            } catch (error) {
                console.log("Hàm getUrl lỗi rồi");
            }
        },
        isImage(file) {
            if (typeof (file) === 'string') {
                const imageExtensions = ["jpg", "jpeg", "png", "gif", "webp", "jfif"];
                const extension = this.getFileExtension(file);
                return imageExtensions.includes(extension);
            } else {
                return file.type.startsWith('image/');
            }
        },
        getFileExtension(url) {
            const parts = url.split(".");
            return parts[parts.length - 1].toLowerCase();
        },
        showUpdateFileDialog() {
            this.showUpdateFile = true;
            this.showPostVisible = false;
        },
        closeUpdateFileDialog() {
            if (!this.showUpdatePostVisible) {
                this.showUpdateFile = false;
                this.showPostVisible = true;
            } else {
                this.showUpdateFile = false;
            }
        },
        addFile(event) {
            console.log("Gọi hàm: addFile");
            const files = event.target.files;
            if (!this.showUpdatePostVisible) {
                if (files.length != 0) {
                    for (let i = 0; i < files.length; i++) {
                        const file = files[i];
                        this.newFeed.files.push(file);
                    }
                }
            } else {
                if (files.length != 0) {
                    for (let i = 0; i < files.length; i++) {
                        const file = files[i];
                        this.updateFeed.files.push(file);
                    }
                }
            }
        },
        removeFiles() {
            if (this.showUpdatePostVisible) {
                this.updateFeed = [];
            } else {
                this.newFeed.files = [];
            }
            this.fileListHeight = "100px";
        },
        removeFile(index) {
            if (!this.showUpdatePostVisible) {
                this.newFeed.files.splice(index, 1);
                if (this.newFeed.files.length === 0) {
                    this.fileListHeight = "100px";
                }
            } else {
                this.updateFeed.files.splice(index, 1);
                if (this.newFeed.files.length === 0) {
                    this.fileListHeight = "100px";
                }
            }
        },
        showChooseTag() {
            if (!this.showUpdatePostVisible) {
                this.hasTagFriend = true;
                this.friends = this.listFriends;
                this.showPostVisible = false;
            } else {
                this.hasTagFriend = true;
                const userTagPhoneNumbers = this.updateFeed.userTagList.map(user => user.phoneNumber);
                this.friends = this.listFriends.filter(friend => !userTagPhoneNumbers.includes(friend.phoneNumber));
            }
        },
        closeChooseTagDialog() {
            if (!this.showUpdatePostVisible) {
                this.hasTagFriend = false;
                this.showPostVisible = true;
            } else {
                this.hasTagFriend = false;
            }
        },
        deleteFriendByPhoneNumber(phoneNumber) {
            console.log("Gọi hàm: deleteFriendByPhoneNumber");
            this.friends = this.friends.filter(friend => friend.phoneNumber !== phoneNumber);
        },
        addFriendTag(friend) {
            if (!this.showUpdatePostVisible) {
                this.newFeed.friendTag.push(friend);
                this.deleteFriendByPhoneNumber(friend.phoneNumber);
            } else {
                this.updateFeed.userTagList.push(friend);
                this.deleteFriendByPhoneNumber(friend.phoneNumber);
            }
        },
        deleteFriendTag(friend) {
            if (!this.showUpdatePostVisible) {
                const indexInTag = this.newFeed.friendTag.findIndex(taggedFriend => taggedFriend.phoneNumber === friend.phoneNumber);
                if (indexInTag !== -1) {
                    this.newFeed.friendTag.splice(indexInTag, 1);
                }

                const indexInFriends = this.friends.findIndex(existingFriend => existingFriend.phoneNumber === friend.phoneNumber);
                if (indexInFriends === -1) {
                    this.friends.push(friend);
                }
            } else {
                const indexInTag = this.updateFeed.userTagList.findIndex(taggedFriend => taggedFriend.phoneNumber === friend.phoneNumber);
                if (indexInTag !== -1) {
                    this.updateFeed.userTagList.splice(indexInTag, 1);
                }

                const indexInFriends = this.friends.findIndex(existingFriend => existingFriend.phoneNumber === friend.phoneNumber);
                if (indexInFriends === -1) {
                    this.friends.push(friend);
                }
            }
        },
        async createPost() {
            console.log("Gọi hàm: createPost");
            try {
                console.log("PrivateSetting: ", this.privateSetting);
                if (this.privateSetting === 'Công khai') {
                    this.newFeed.audience = 'Public'
                }
                else if (this.privateSetting === 'Bạn bè') {
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

                // console.log(listFriendTag)

                // console.log(typeof (listFriendTag))

                const formData = new FormData();

                formData.append('content', this.newFeed.content);
                formData.append('audience', this.newFeed.audience);
                formData.append('userTagIDList', listFriendTag);

                console.log(this.newFeed.files)

                for (let i = 0; i < this.newFeed.files.length; i++) {
                    formData.append('files', this.newFeed.files[i]);
                }

                // console.log(formData);

                // console.log(localStorage.getItem("token"))

                const response = await axios.post(`social-media/create-new-post`, formData, /**{
                    headers: {
                        'Authorization': localStorage.getItem("token"),
                        'Content-Type': 'multipart/form-data',
                    }
                }**/);

                if (response.status === 200) {
                    this.showPostVisible = false;
                    this.newFeed.content = '';
                    this.fetchFeed();
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
            console.log("Gọi hàm: fetchAvatar()");
            axios.get(`users/imageAvatar`, {
                headers: {
                    'Authorization': localStorage.getItem("token"),
                },
                responseType: 'blob',
            }).then(response => {
                if (this.user.imageAvatarUrl) {
                    URL.revokeObjectURL(this.user.imageAvatarUrl);
                }
                //console.log("Type of response: " + typeof(response));
                this.user.imageAvatarUrl = URL.createObjectURL(response.data);
            }).catch(error => {
                console.error('Error fetching avatar:', error);
            });
            console.log("Image Avatar Url: " + this.user.imageAvatarUrl);
            //console.log('user.imageAvatarUrl changed:', this.user.imageAvatarUrl);
        },
        fetchUpdateLike(postId) {
            console.log("Gọi hàm: fetchUpdateLike(postId)");
            axios.put(`/social-media/like-post/${postId}`).then(response => {
                // Xử lý kết quả nếu cần
                if (response.status === 200) {
                    const userLike = {
                        "userName": this.user.fullName,
                        "imageAvatar": this.user.imageAvatarUrl.substring(5),
                        "phoneNumber": this.user.phoneNumber
                    }
                    // console.log("User like: " , userLike);
                    //console.log("User like key: " , Object.keys(this.user));
                    const post = this.feeds.find(f => f.id === postId);
                    // console.log("Danh sách người thích bài viết trước chỉnh sửa: ", this.feeds.find(f => f.id === postId).userLikeList.length);
                    // console.log(post);
                    // console.log("Response body: " + response.data);
                    if (response.data === "Thích bài viết thành công!") {
                        // console.log("Vào thích");
                        this.feeds.find(f => f.id === postId).userLikeList.push(userLike)
                        this.feeds.userLike = true;
                    }
                    else {
                        // console.log("Vào hủy thích");
                        this.feeds.find(f => f.id === postId).userLikeList.splice(post.userLikeList.indexOf(userLike), 1);
                        this.feeds.userLike = false;
                    }
                    // console.log(post);
                    // console.log("Danh sách người thích bài viết sau chỉnh sửa: ", this.feeds.find(f => f.id === postId).userLikeList.length);
                }
            }).catch(error => {
                console.error('Error updating post like:', error);
            });
        },
        likePost(feed) {
            console.log("Gọi hàm likePost(feed)");
            //console.log("Id bài viết: " + feed.id);
            const foundFeed = this.feeds.find(f => f.id === feed.id);

            if (foundFeed) {
                //console.log("Tìm thấy bài viết")
                const updatedFeed = Object.assign({}, foundFeed, { isLike: !foundFeed.isLike });
                //console.log(updatedFeed);
                const index = this.feeds.findIndex(f => f.id === feed.id);
                if (this.showVisibleInfoFeed) {
                    // if (updatedFeed.isLike)
                    //     //console.log("Đã like bài viết")
                    // else
                    //     //console.log("Đã hủy like bài viết")
                    this.showingFeed.isLike = updatedFeed.isLike;
                }
                this.fetchUpdateLike(feed.id);

                if (index !== -1) {
                    //console.log("Vô index !== -1")
                    this.feeds.splice(index, 1, updatedFeed);
                }
            }
        },
        updateIsUserLikePost(post) {
            console.log("Gọi hàm: updateIsUserLikePost(post)");
            const currentUserPhoneNumber = this.user.phoneNumber;

            //console.log(Object.keys(this.user))
            //console.log(this.user.fullName);

            // Kiểm tra xem có tồn tại user có phoneNumber trùng với currentUserPhoneNumber không
            const isUserLiked = post.userLikeList.some(user => user.phoneNumber === currentUserPhoneNumber);

            // Cập nhật trạng thái isLike của post
            post.isLike = isUserLiked;

            // Xuất thông báo kiểm tra
            if (isUserLiked) {
                console.log(`User with phoneNumber ${currentUserPhoneNumber} liked the post.`);
            } else {
                console.log(`User with phoneNumber ${currentUserPhoneNumber} unliked the post.`);
            }
        },
        updateIsUserLikeComment(comment) {
            console.log("Gọi hàm: updateIsUserLikeComment(post)");
            const currentUserPhoneNumber = this.user.phoneNumber;

            //console.log(Object.keys(this.user))
            //console.log(this.user.fullName);

            // Kiểm tra xem có tồn tại user có phoneNumber trùng với currentUserPhoneNumber không
            const isUserLiked = comment.userLike.some(user => user.phoneNumber === currentUserPhoneNumber);

            // Cập nhật trạng thái isLike của post
            comment.isUserLike = isUserLiked;

            // Xuất thông báo kiểm tra
            if (isUserLiked) {
                console.log(`User with phoneNumber ${currentUserPhoneNumber} liked the comment.`);
            } else {
                console.log(`User with phoneNumber ${currentUserPhoneNumber} unliked the comment.`);
            }
        },
        openFeedInfo(feed) {
            console.log("Gọi hàm: openFeedInfo");
            this.showVisibleInfoFeed = true;
            this.showingFeed = feed;
            this.fetchComment(feed.id);
            //this.newComment.senderPhoneNumber = this.user.phoneNumber;
            //console.log("Ngày showing feed: " + this.showingFeed.updatedAt)
        },
        closeFeedInfo() {
            console.log("Gọi hàm: closeFeedInfo");
            this.showVisibleInfoFeed = false;
            this.showPopupVisible = false;
        },
        handleFileChange(event) {
            console.log("Gọi hàm: handleFileChange");
            const selectedFile = event.target.files[0];
            //console.log(selectedFile);
            this.newComment.file = selectedFile;
        },
        openFilePicker() {
            console.log("Gọi hàm: openFilePicker");
            const fileInput = this.$refs.fileInputComment;
            if (fileInput) {
                fileInput.click();
            }
        },
        focusComment() {
            console.log("Gọi hàm: focusComment");
            this.$refs.comment.focus();
        },
        postComment(postId) {
            console.log("Gọi hàm: postComment");
            // console.log("New comment: ", this.newComment);
            // console.log("Post id: " , postId);
            this.newComment.postId = postId;
            // console.log("New comment: ", this.newComment);
            const formData = new FormData();
            formData.append('postId', this.newComment.postId);
            if (this.newComment.topComment) formData.append('topComment', this.newComment.topComment);
            if (this.newComment.content) formData.append('content', this.newComment.content);
            if (this.newComment.file) formData.append('file', this.newComment.file);
            axios.post(`social-media/create-new-comment`, formData)
                .then(response => {
                    if (response.status === 200) {
                        console.log(response.data);
                        // if (response.data === 'Thích bình luận thành công!')
                        //     comment.isUserLike = true;
                        // else
                        //     comment.isUserLike = false;
                    }

                })
                .catch(error => {
                    console.log('error', error)
                })


        },
        async getListOfFriends() {
            console.log("Gọi hàm: getListOfFriends");
            try {
                const response = await axios.get(`users/getAllFriendUser`, {
                    headers: {
                        'Authorization': localStorage.getItem("token")
                    }
                });

                if (response.status === 200) {

                    this.listFriends = response.data;
                } else {
                    console.error(response.data);
                    this.toast.error(response.data, { timeout: 1500 });
                }
            } catch (error) {
                if (error.response) {
                    if (error.response.status === 400) {
                        this.toast.error(error.response.data, { timeout: 1500 });
                    } else {
                        this.toast.error(error.response.data, { timeout: 1500 });
                    }
                } else if (error.request) {
                    this.toast.error('Không nhận được phản hồi từ máy chủ. Vui lòng thử lại!', { timeout: 1500 });
                } else {
                    this.toast.error('Error setting up the request:' + error.message, { timeout: 1500 });
                }
            }
        },
        openUpdatePost(clickedFeed) {
            // Your implementation here
            console.log(clickedFeed)
            this.showPopupVisible = false;
            this.showUpdatePostVisible = true;
            this.updateFeed = clickedFeed;
            if (clickedFeed.audience === 'Public') {
                this.privateSetting = "Công khai"
            } else if (clickedFeed.audience === 'AllFriend') {
                this.privateSetting = "Bạn bè"
            } else if (clickedFeed.audience === 'SomeOneCanSee') {
                this.privateSetting = "Một số bạn bè"
            } else if (clickedFeed.audience === 'AllExceptSomeOne') {
                this.privateSetting = "Bạn bè ngoại trừ"
            }
        },
        closeUpdatePostOption() {
            this.showUpdatePostVisible = false;
        },
        sharePost(feed) {
            if (confirm("Bạn có muốn chia sẻ bài viết?")) {
                console.log(feed)
            }
        }
    }
}
</script>

<style lang = "scss" scoped>
.post-container {
    display: flex;
    flex-direction: column;
    height: 100vh;
}

.container {
    height: 100%;
}

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

    .info-comment {
        font-size: 12px;

        p {
            display: inline;
            cursor: pointer;
            margin-right: 10px;
        }
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

.like-button-comment-active {
    color: blue;
}

.action {
    margin-right: 20px;

    .popover-action-container {


        .popoverAction {
            display: inline-block;
            position: absolute;
            border-radius: 4px;
            z-index: 9999;
            box-shadow: 0 0 10px 0 rgba(0, 0, 0, 0.2);
            border: 1px solid #d6dbe1;


            .popover-body {
                position: absolute;
                background-color: #fff;
                box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
                border-radius: 8px;
                height: 120px;
                width: 180px;
                justify-content: space-between;

                .separator {
                    height: 1px;
                    background: #d6dbe1;
                    margin: 4px;
                }

                .popover-item {
                    height: 36px;
                    padding-left: 14px;
                    padding-top: 6px;
                    padding-bottom: 2px;
                    cursor: pointer;
                }
            }
        }
    }
}

.name {
    width: 140px;
}

.date {
    width: 100px;
}
</style>
