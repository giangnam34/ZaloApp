<template>
    <div class="post-container">
        <div class="container px-8 py-6 bg-gray-100 overflow-y-auto" ref="scrollContainer" @scroll="handleScroll">
            <div class="max-w-none w-auto mx-auto grid grid-cols-4 gap-4">
                <div class="main-left col-span-1">
                    <div class="p-4 bg-white border border-gray-200 text-center rounded-lg">
                        <img id="general_image" :src="user.imageAvatarUrl"
                            class="w-[200px] h-[200px] rounded-full mx-auto mb-6 object-cover object-bottom">
                        <p><strong>{{ user.fullName }}</strong></p>

                        <div id="display-number" class="mt-6 flex space-x-8 justify-around">
                            <p class="text-xs text-gray-500">{{ listFriends.length }} friends</p>
                            <p id="display-number-element-2" class="text-xs text-gray-500">{{ numberOfUserPost }} posts
                            </p>
                        </div>

                    </div>
                </div>

                <div class="main-center col-span-2 space-y-4">
                    <div class="bg-white border border-gray-200 rounded-lg">
                        <div class="p-4">
                            <span class="p-4 bg-gray-100 rounded-lg cursor-pointer d-block w-100" @click="showPostOption">What's is on your mind, {{
                                user.fullName }}?</span>
                        </div>
                    </div>

                    <div v-for="feed in feeds" :key="feed.id" class="p-4 bg-white border border-gray-200 rounded-lg">
                        <div v-if="feed.postFather === null">
                            <div class="mb-6 flex items-center justify-between">
                                <div class="flex items-center space-x-6">
                                    <img :src="feed.userPost.imageAvatar"
                                        @click="handleOpenUserDialog(feed.userPost.phoneNumber)"
                                        class="w-[50px] h-[50px] rounded-full flex justify-center align-center mr-4 object-cover object-bottom cursor-pointer">
                                    <div class="wrap-title flex">
                                        <div>
                                            <div class="flex">
                                                <p class="name"><strong>{{ feed.userPost.userName }}</strong></p>

                                            </div>
                                            <div class="wrap-icon flex">
                                                <div v-if="feed.audience === 'Public'">
                                                    <font-awesome-icon icon="fa-solid fa-earth-americas" />
                                                </div>
                                                <div v-if="feed.audience === 'AllFriend'">
                                                    <font-awesome-icon icon="fa-solid fa-user-group" />
                                                </div>
                                                <!-- <div v-if="feed.audience === 'OnlyMe'">
                                                    <font-awesome-icon icon="fa-solid fa-lock" />
                                                </div>
                                                <div v-if="feed.audience === 'SomeOneCanSee'">
                                                    <font-awesome-icon icon="fa-solid fa-user" />
                                                </div>
                                                <div v-if="feed.audience === 'AllExceptSomeOne'">
                                                    <font-awesome-icon icon="fa-solid fa-user-minus" />
                                                </div> -->
                                                <p class="text-gray-600 ml-2 date"> • {{ formatTimeDifference(new
                                                    Date(feed.createdAt)) }}
                                                </p>
                                            </div>
                                        </div>

                                    </div>
                                </div>
                                <div v-if="user.phoneNumber === feed.userPost.phoneNumber" id="more-icon">
                                    <div class="action cursor-pointer">
                                        <div class="popover-action-container" @click:outside="hidePopover">
                                            <a id="ellipsis-icon" @click="(event) => handleClickAction(event, feed)">
                                                <font-awesome-icon icon="fa-solid fa-ellipsis-vertical" />
                                            </a>
                                            <div class="popoverAction"
                                                v-show="showPopupVisible && !showVisibleInfoFeed && clickedFeed.userPost.phoneNumber === user.phoneNumber"
                                                :style="{ left: popoverLeft, top: popoverTop }">
                                                <div class="popover-body">
                                                    <div class="popover-item" @click="deletePost(clickedFeed.id)">
                                                        <div>
                                                            Delete post
                                                        </div>
                                                    </div>
                                                    <div class="popover-item" @click="openUpdatePost(clickedFeed)">
                                                        <div>
                                                            Edit post
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

                            <p class="mb-3">{{ feed.content || '' }}</p>

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
                                <div v-if="!feed.isLike" class="flex-1 flex items-center mr-2"
                                    @click="openListLikedUserDialog(feed.id)">
                                    <font-awesome-icon icon="fa-regular fa-thumbs-up" class="text-lg text-blue mr-2" />
                                    <span class="text-gray-500 text-lg hover:underline cursor-pointer">{{
                                        feed.userLikeList.length
                                    }}</span>
                                </div>

                                <!-- Test -->
                                <div v-else class="flex-1 flex items-center mr-2" @click="openListLikedUserDialog(feed.id)">
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
                            <div class="mb-6 flex items-center justify-between">
                                <div class="flex items-center space-x-6 w-full">
                                    <img :src="feed.userPost.imageAvatar"
                                        class="w-[50px] h-[50px] rounded-full flex justify-center align-center mr-4 object-cover object-bottom">
                                    <div class="wrap-title flex justify-between w-full">
                                        <div>
                                            <div class="flex">
                                                <p class="name"><strong>{{ feed.userPost.userName }}</strong></p>
                                            </div>
                                            <div class="wrap-icon">
                                                <div>
                                                    <font-awesome-icon icon="fa-solid fa-earth-americas" />
                                                    Công khai
                                                </div>
                                            </div>
                                        </div>
                                        <div v-if="user.phoneNumber === feed.userPost.phoneNumber" id="more-icon">
                                            <div class="action cursor-pointer">
                                                <div class="popover-action-container" @click:outside="hidePopover">
                                                    <a id="ellipsis-icon"
                                                        @click="(event) => handleClickAction(event, feed)">
                                                        <font-awesome-icon icon="fa-solid fa-ellipsis-vertical" />
                                                    </a>
                                                    <div class="popoverAction"
                                                        v-show="showPopupVisible && !showVisibleInfoFeed && clickedFeed.userPost.phoneNumber === user.phoneNumber"
                                                        :style="{ left: popoverLeft, top: popoverTop }">
                                                        <div class="popover-body">
                                                            <div class="popover-item" @click="deletePost(clickedFeed.id)">
                                                                <div>
                                                                    Delete post
                                                                </div>
                                                            </div>
                                                            <div class="separator"></div>
                                                            <div class="popover-item" @click="hidePopover">
                                                                <div>
                                                                    Cancel
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
                                <div v-if="feed.postFather.files.length > 0"
                                    class="image-container-feed cursor-pointer mb-2" @click="openFeedInfo(feed.postFather)">
                                    <img :src="feed.postFather.files[0]" class="w-full h-[500px] rounded-lg cursor-pointer"
                                        v-if="isImage(feed.postFather.files[0])" @click="openFeedInfo(feed.postFather)" />
                                    <video v-else controls width="300" class="w-full h-[500px] rounded-lg cursor-pointer">
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
                                            class="w-[50px] h-[50px] rounded-full flex justify-center align-center mr-4 object-cover object-bottom">
                                        <div class="wrap-title flex">
                                            <div>
                                                <div class="flex">
                                                    <p class="name"><strong>{{ feed.postFather.userPost.userName
                                                    }}</strong>
                                                    </p>
                                                </div>
                                                <div class="wrap-icon flex">
                                                    <div v-if="feed.postFather.audience === 'Public'">
                                                        <font-awesome-icon icon="fa-solid fa-earth-americas" />
                                                    </div>
                                                    <div v-if="feed.postFather.audience === 'AllFriend'">
                                                        <font-awesome-icon icon="fa-solid fa-user-group" />
                                                    </div>
                                                    <!-- <div v-if="feed.postFather.audience === 'OnlyMe'">
                                                            <font-awesome-icon icon="fa-solid fa-lock" />
                                                        </div>
                                                        <div v-if="feed.postFather.audience === 'SomeOneCanSee'">
                                                            <font-awesome-icon icon="fa-solid fa-user" />
                                                        </div>
                                                        <div v-if="feed.postFather.audience === 'AllExceptSomeOne'">
                                                            <font-awesome-icon icon="fa-solid fa-user-minus" />
                                                        </div> -->
                                                    <p class="text-gray-600 ml-2 date"> • {{
                                                        formatTimeDifference(new
                                                            Date(feed.postFather.createdAt))
                                                    }}
                                                    </p>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <p class="mb-3">{{ feed.postFather.content || '' }}</p>
                            </div>


                            <div class="my-3 flex">
                                <div v-if="!feed.isLike" class="flex-1 flex items-center mr-2"
                                    @click="openListLikedUserDialog(feed.id)">
                                    <font-awesome-icon icon="fa-regular fa-thumbs-up" class="text-lg text-blue mr-2" />
                                    <span class="text-gray-500 text-lg hover:underline cursor-pointer">{{
                                        feed.userLikeList.length
                                    }}</span>
                                </div>

                                <!-- Test -->
                                <div v-else class="flex-1 flex items-center mr-2" @click="openListLikedUserDialog(feed.id)">
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
                            </div>
                            <hr style="border: none; border-bottom: 1px solid #ccc;">
                        </div>
                    </div>
                </div>
                <div class="main-right col-span-1 ">
                    <div class="filter-wrapper bg-white border border-gray-200 rounded-lg">
                        <div class="filter-contact" @blur="hidePopover" tabindex="0">
                            <div class="filter-contact-child"
                                :class="{ 'hoveredFilter': hoveredItem === 'filter', 'selected': selectedItem === 'filter' }"
                                @mouseenter="(hoveredItem = 'filter')" @mouseleave="hoveredItem = ''"
                                @click="(event) => handleClickFilter(event, 'filter')">
                                <a id="filter-icon">
                                    <font-awesome-icon icon="fa-solid fa-filter" />
                                </a>
                                <span>
                                    {{ typeOfFilter }}
                                </span>
                                <a id="down-icon">
                                    <font-awesome-icon icon="fa-solid fa-chevron-down" />
                                </a>
                            </div>
                            <div class="popover" style="z-index: 20; opacity: 1;"
                                :style="{ left: popoverLeftFilter, top: popoverTopFilter }"
                                v-if="selectedItem === 'filter'">
                                <!-- Nội dung của popover -->
                                <div class="popover-content"
                                    style="min-width: 240px; width: initial; box-sizing: border-box;">
                                    <div class="popover-item" :class="{ 'hoveredFilter': hoveredItem === 'allPosts' }"
                                        @mouseenter="(hoveredItem = 'allPosts')" @mouseleave="hoveredItem = ''"
                                        @click="chooseTypeOfFilter(hoveredItem)">
                                        <div style="width: 35px;" v-show="chosenFilter === 'myPosts'"></div>
                                        <a id="check-icon" v-show="chosenFilter === 'allPosts'">
                                            <font-awesome-icon icon="fa-solid fa-check" />
                                        </a>
                                        <div class="truncate">
                                            <span>Tất cả bài viết</span>
                                        </div>
                                    </div>
                                    <div class="popover-item" :class="{ 'hoveredFilter': hoveredItem === 'myPosts' }"
                                        @mouseenter="(hoveredItem = 'myPosts')" @mouseleave="hoveredItem = ''"
                                        @click="chooseTypeOfFilter(hoveredItem)">
                                        <div style="width: 35px;" v-show="chosenFilter === 'allPosts'"></div>
                                        <a id="check-icon" v-show="chosenFilter === 'myPosts'">
                                            <font-awesome-icon icon="fa-solid fa-check" />
                                        </a>
                                        <div class="truncate">
                                            <span>Bài viết của tôi</span>
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
                                <img :src="user.imageAvatarUrl" class="avatar object-cover object-bottom">
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
                    <div class="icon-close" @click="closeUpdateFileDialog"><font-awesome-icon icon="fa-solid fa-x" />
                    </div>
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
                    <div class="icon-close" @click="closeChooseTagDialog"><font-awesome-icon icon="fa-solid fa-x" />
                    </div>
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
                                        <span class="color-blue">{{ friend.userName }}
                                            <font-awesome-icon icon="fa-solid fa-x" /></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div><span>List of friend</span></div>
                        <div class="friend-list-container">
                            <div v-for="friend in filteredFriends" v-bind:key="friend.phoneNumber"
                                class="position-relative">
                                <div class="friend-info friend-container" @click="addFriendTag(friend)">
                                    <div class="avatar-container">
                                        <div class="avatar-wrapper">
                                            <img :src="friend.imageAvatar" class="avatar object-cover object-bottom">
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
                        <div><span>List of friend</span></div>
                        <div class="friend-list-container">
                            <div v-for="friend in filteredFriends" v-bind:key="friend.phoneNumber"
                                class="position-relative">
                                <div class="friend-info friend-container" @click="addFriendTag(friend)">
                                    <div class="avatar-container">
                                        <div class="avatar-wrapper">
                                            <img :src="friend.imageAvatar" class="avatar object-cover object-bottom">
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
            <v-card-text id="dialog-content" class="dialog-content py-0 px-0 overflow-auto">
                <div class="bg-white p-4">
                    <div class="mb-6 flex items-center justify-between">
                        <div class="flex items-center space-x-6 w-full">
                            <img :src="showingFeed.userPost.imageAvatar"
                                class="w-[50px] h-[50px] rounded-full flex justify-center align-center mr-4 object-cover object-bottom">
                            <div class="flex justify-between w-full">
                                <div>
                                    <div class="flex">
                                        <p class="name"><strong>{{ showingFeed.userPost.userName }}</strong></p>
                                    </div>
                                    <div class="wrap-icon flex justify-between">
                                        <div v-if="showingFeed.audience === 'Public'">
                                            <font-awesome-icon icon="fa-solid fa-earth-americas" />
                                            Công khai
                                        </div>
                                        <div v-if="showingFeed.audience === 'AllFriend'">
                                            <font-awesome-icon icon="fa-solid fa-user-group" />
                                            Bạn bè
                                        </div>
                                        <!-- <div v-if="showingFeed.audience === 'OnlyMe'">
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
                                        </div> -->
                                        <p class="text-gray-600 ml-2 date"> • {{ formatTimeDifference(new
                                            Date(showingFeed.createdAt))
                                        }}
                                        </p>
                                    </div>
                                </div>
                                <div v-if="user.phoneNumber === showingFeed.userPost.phoneNumber" id="more-icon">
                                    <div class="action cursor-pointer">
                                        <div class="popover-action-container" @click:outside="hidePopover">
                                            <a id="ellipsis-icon" @click="(event) => handleClickAction(event, showingFeed)">
                                                <font-awesome-icon icon="fa-solid fa-ellipsis-vertical" />
                                            </a>
                                            <div class="popoverAction"
                                                v-show="showPopupVisible && clickedFeed.userPost.phoneNumber === user.phoneNumber"
                                                style=" right: 252px; top: 40px; ">
                                                <div class="popover-body">
                                                    <div class="popover-item" @click="deletePost(clickedFeed.id)">
                                                        <div>
                                                            Delete post
                                                        </div>
                                                    </div>
                                                    <div class="popover-item" @click="openUpdatePost(clickedFeed)">
                                                        <div>
                                                            Edit post
                                                        </div>
                                                    </div>
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

                    <p class="mb-3">{{ showingFeed.content || '' }}</p>

                    <!-- Hiển thị ảnh -->

                    <div>
                        <swiper :slides-per-view="1" @swiper="onSwiper" @slideChange="onSlideChange" :modules="modules"
                            navigation>
                            <swiper-slide v-for="(image, index) in showingFeed.files" :key="index">
                                <div class="image-container-feed cursor-pointer"
                                    @click="openFullImage(showingFeed.files, index)">
                                    <img v-if="isImage(image)" :src="image" class="w-full h-[500px] rounded-lg" />
                                    <video v-else controls class="w-full h-[500px] rounded-lg">
                                        <source :src="image" type="video/mp4" />
                                        Your browser does not support the video tag.
                                    </video>
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
                        <div v-if="!showingFeed.isLike" class="flex-1 flex items-center mr-2"
                            @click="openListLikedUserDialog(showingFeed.id)">
                            <font-awesome-icon icon="fa-regular fa-thumbs-up" class="text-lg text-blue mr-2" />
                            <span class="text-gray-500 text-lg hover:underline cursor-pointer">{{
                                showingFeed.userLikeList.length
                            }}</span>
                        </div>

                        <!-- Test -->
                        <div v-else class="flex-1 flex items-center mr-2" @click="openListLikedUserDialog(showingFeed.id)">
                            <font-awesome-icon
                                v-if="showingFeed.userLikeList !== null && showingFeed.userLikeList.length > 0"
                                icon="fa-regular fa-thumbs-up" class="text-lg text-blue mr-2" />
                            <span class="text-gray-500 text-lg hover:underline cursor-pointer">
                                <template v-if="showingFeed.userLikeList.length === 1">
                                    {{ user.fullName }}
                                </template>
                                <template v-else>
                                    Bạn và {{ showingFeed.userLikeList.length - 1 }} người khác
                                </template>
                            </span>
                        </div>
                        <!-- Test -->

                        <div class="flex-1 flex items-center justify-end">
                            <div class="flex items-center space-x-2 ml-auto hover:underline cursor-pointer"
                                @click="openFeedInfo(showingFeed)">
                                <font-awesome-icon icon="fa-regular fa-comment" />
                                <span class="text-gray-500 text-lg">{{ comments.length }} bình luận</span>
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
                    <div v-for="(comment, index) in comments.slice().reverse()" v-bind:key="comment.id">
                        <div class="mb-2 items-center justify-between">
                            <div class="flex items-center">
                                <img :src="comment.userComment.imageAvatar"
                                    class="w-[50px] h-[50px] rounded-full flex justify-center align-center mr-2 object-cover object-bottom">
                                <div class="comment-content rounded-lg bg-gray-200 p-2">
                                    <div>
                                        <p><strong>{{ comment.userComment.userName }}</strong></p>
                                    </div>
                                    <div>
                                        <p>{{ comment.content || '' }}</p>
                                    </div>
                                </div>
                                <div style="position: relative; display: inline-block;">

                                    <!-- div show-more-option -->
                                    <div @click="showTableOption(index)" class="show-more-option"
                                        style="cursor: pointer; margin-left: 10px;"
                                        v-if="this.user.phoneNumber === comment.userComment.phoneNumber">
                                        <p>...</p>
                                    </div>

                                    <!-- div table-option-comment -->
                                    <div class="table-option-comment rounded-lg bg-gray-200 p-2"
                                        :style="{ position: 'absolute', top: '100%', left: '0px', marginLeft: '10px', minWidth: '100px', display: isTableOptionVisible && currentCommentIndex === index ? 'block' : 'none' }">
                                        <p @click="chooseComment(comment)">Chỉnh sửa</p>
                                        <p @click="deleteComment(comment.idComment, showingFeed.id)">Xóa</p>
                                    </div>

                                </div>

                            </div>
                            <div class="ml-15">
                                <div class="comment-image">
                                    <div v-if="(typeof comment.contentMedia) === 'string'">
                                        <img class="upload-file"
                                            v-if="comment.contentMedia !== null && isImage(comment.contentMedia)"
                                            :src="comment.contentMedia" alt="Selected Image" />
                                        <video v-else-if="comment.contentMedia !== null" controls width="300"
                                            class="upload-file">
                                            <source :src="comment.contentMedia" type="video/mp4" />
                                            Trình duyệt không hỗ trợ định dạng này
                                        </video>
                                    </div>
                                    <div v-else>
                                        <img class="upload-file"
                                            v-if="comment.contentMedia !== null && isImage(comment.contentMedia)"
                                            :src="getUrl(comment.contentMedia)" alt="Selected Image" />
                                        <video v-else-if="comment.contentMedia !== null" controls width="300"
                                            class="upload-file">
                                            <source :src="getUrl(comment.contentMedia)" type="video/mp4" />
                                            Trình duyệt không hỗ trợ định dạng này
                                        </video>
                                    </div>
                                    <!-- <img class="file-comment rounded-lg" :src="comment.contentMedia" /> -->
                                </div>
                                <p class="text-gray-600">{{ formatDate(comment.createdAt) }}</p>
                            </div>
                            <!-- <div class="ml-15 info-comment">
                                <p class="like-button-comment" @click="likeComment(comment.idComment)"
                                    @mouseover="changeColorButtonComment('like-button-comment', true)"
                                    @mouseout="changeColorButtonComment('like-button-comment', false)"
                                    :class="{ 'like-button-comment-active': comment.isUserLike }">Thích </p>
                                <p class="reply-button-comment"
                                    @mouseover="changeColorButtonComment('reply-button-comment', true)"
                                    @mouseout="changeColorButtonComment('reply-button-comment', false)">Phản hồi</p>
                            </div> -->
                        </div>
                    </div>
                </div>
            </v-card-text>
        </v-card>
        <v-card class="dialog-component-2">
            <v-card-title class="dialog-title flex items-center justify-between">
                <div v-if="chosenUpdateComment === null">
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
                </div>
                <div v-else>
                    <div class="pr-2 flex-1">
                        <textarea ref="comment" class="p-4 w-full bg-gray-100 rounded-lg border comment-input"
                            placeholder="Viết bình luận..." v-model="chosenUpdateComment.content"></textarea>
                    </div>
                    <div class="flex flex-none items-center space-x-2">
                        <input type="file" @change="handleFileChange" ref="fileInputComment" style="display: none;" />
                        <v-btn v-if="chosenUpdateComment.contentMedia === null" @click="openFilePicker">Chọn
                            file</v-btn>
                        <div v-else class="cursor-pointer" @click="openFilePicker">
                            <div v-if="(typeof chosenUpdateComment.contentMedia) === 'string'">

                                <img class="upload-file-comment" v-if="isImage(chosenUpdateComment.contentMedia)"
                                    :src="chosenUpdateComment.contentMedia" alt="Selected Image" />
                                <video v-else controls width="300" class="upload-file-comment">
                                    <source :src="chosenUpdateComment.contentMedia" type="video/mp4" />
                                    Trình duyệt không hỗ trợ định dạng này
                                </video>
                            </div>
                            <div v-else>
                                <img class="upload-file-comment" v-if="isImage(chosenUpdateComment.contentMedia)"
                                    :src="getUrl(chosenUpdateComment.contentMedia)" alt="Selected Image" />
                                <video v-else controls width="300" class="upload-file-comment">
                                    <source :src="getUrl(chosenUpdateComment.contentMedia)" type="video/mp4" />
                                    Trình duyệt không hỗ trợ định dạng này
                                </video>
                            </div>
                        </div>
                        <v-btn @click="updateComment(showingFeed.id)">Hoàn tất chỉnh
                            sửa</v-btn>
                    </div>
                </div>
            </v-card-title>
        </v-card>
    </v-dialog>
    <!-- Pop up chỉnh sửa bài viết người dùng -->
    <v-dialog class="dialog-container" v-model="showUpdatePostVisible" max-width="500px"
        @click:outside="closeUpdatePostOption">
        <v-card class="dialog-component">
            <v-card-title class="dialog-title">
                <h2 class="title">Chỉnh sửa bài viết
                    <div class="icon-close" @click="closeUpdatePostOption"><font-awesome-icon icon="fa-solid fa-x" />
                    </div>
                </h2>
            </v-card-title>
            <hr style="border: none; border-bottom: 1px solid #ccc;">
            <v-card-text class="dialog-content">
                <div style="display: flex; align-items: center; justify-content: space-between;">
                    <div class="friend-info">
                        <div class="avatar-container">
                            <div class="avatar-wrapper">
                                <img :src="user.imageAvatarUrl" class="avatar object-cover object-bottom">
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
                <div class="profile-action" @click="updatePost(updateFeed)">
                    <div class="mx-4 flex items-center justify-center cursor-pointer bg-blue-400 rounded-lg h-8">
                        Lưu bài viết
                    </div>
                </div>
            </v-card-text>
        </v-card>
    </v-dialog>
    <v-dialog class="dialog-container-liked-user" v-model="showVisibleLikedUsers" max-width="500px"
        @click:outside="closeListLikedUserDialog">
        <v-card class="dialog-component-liked-user">
            <v-card-title class="dialog-title-liked-user">
                <h2 class="title-liked-user">Danh sách người dùng thích bài viết
                    <div class="icon-close-liked-user" @click="closeListLikedUserDialog"><font-awesome-icon
                            icon="fa-solid fa-x" />
                    </div>
                </h2>
            </v-card-title>
            <hr style="border: none; border-bottom: 1px solid #ccc;">
            <v-card-text class="dialog-content-liked-user">
                <div class="pt-1 pl-4 pr-4 pb-2">
                    <div class="mt-4"><span>Người dùng đã thích bài viết: {{ likedUsers.length }}</span></div>
                    <div class="friend-list-container-liked-user">
                        <div v-for="user in likedUsers" v-bind:key="user.phoneNumber" class="position-relative">
                            <div class="friend-info-liked-user friend-container-liked-user">
                                <div class="avatar-container-liked-user">
                                    <div class="avatar-wrapper-liked-user">
                                        <img :src="user.imageAvatar" class="avatar-liked-user">
                                    </div>
                                </div>
                                <div class="detail-liked-user">
                                    <span>{{ user.userName }}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="mb-2"></div>
            </v-card-text>
        </v-card>
    </v-dialog>
    <v-dialog class="dialog-container-user" v-model="showVisibleUserInfo" max-width="352px"
        @click:outside="closeUserInfoDialog">
        <v-card class="dialog-component-user">
            <v-card-title class="dialog-title-user">
                <h2 class="title-user">Info account
                    <div class="icon-close-user" @click="closeUserInfoDialog"><font-awesome-icon icon="fa-solid fa-x" />
                    </div>
                </h2>
            </v-card-title>
            <hr style="border: none; border-bottom: 1px solid #ccc;">
            <v-card-text class="dialog-content-user">
                <div class="profile-photo-user">
                    <div class="cover-avatar-user">
                        <img class="cover-image-user" :src="userFound.imageCoverAvatar" alt="None" crossorigin="anonymous">
                    </div>
                    <div class="ava-name-container-user">
                        <div class="avatar-profile-user">
                            <div class="avatar-user">
                                <img class="avatar-image-user" :src="userFound.imageAvatar">
                            </div>
                        </div>
                        <div class="fullname-profile-user">
                            <div class="fullname-user">{{ userFound.userName }}</div>
                        </div>
                    </div>
                </div>
                <hr style="border: none; border-bottom: 1px solid #ccc;">
                <div class="profile-information-user">
                    <div class="profile-header-user">
                        <strong>Info account</strong>
                    </div>
                    <div>
                        <div class="user-profile-details-user">
                            <div class="user-profile-item-user">
                                <span class="title-user">Phone number</span>
                                <span class="content-user">{{ userFound.phoneNumber }}</span>
                            </div>
                            <div class="user-profile-item-user">
                                <span class="title-user">Gender</span>
                                <span class="content-user">{{ userFound.gender === 'Male' ? 'Nam' : 'Nữ' }}</span>
                            </div>
                            <div class="user-profile-item-user">
                                <span class="title-user">Birthday</span>
                                <span class="content-user">{{ displayedDate }}</span>
                            </div>
                        </div>
                    </div>
                </div>
                <hr v-if="user.phoneNumber !== userFound.phoneNumber" style="border: none; border-bottom: 1px solid #ccc;">
                <div class="mt-2"></div>
                <div class="profile-action-user" v-if="user.phoneNumber !== userFound.phoneNumber">
                    <div v-if="isBlock"
                        class="block-button text-center cursor-pointer bg-gray-400 text-black rounded-lg h-10 mr-2 w-1/3 text-sm"
                        @click="unBlockUser(userFound.phoneNumber)">
                        Unblock
                    </div>
                    <div v-else-if="!sended"
                        class="block-button text-center cursor-pointer bg-gray-400 text-black rounded-lg h-10 mr-2 w-1/3 text-sm"
                        @click="blockUser(userFound.phoneNumber)">
                        Block
                    </div>
                    <div v-if="!sended && !isFriend && !isBlock"
                        class="add-friend-button text-center cursor-pointer bg-blue-500 text-white rounded-lg h-10 ml-2 text-sm flex-grow"
                        @click="addFriend">
                        Add friend
                    </div>
                    <div v-else-if="sended"
                        class="add-friend-button text-center bg-blue-500 text-white rounded-lg h-10 ml-2 text-sm flex-grow">
                        Waiting accept...
                    </div>
                    <div v-else-if="isBlock"
                        class="add-friend-button text-center bg-blue-500 text-white rounded-lg h-10 ml-2 text-sm flex-grow">
                        Can't send invite request
                    </div>
                    <div v-else
                        class="add-friend-button text-center bg-blue-500 text-white rounded-lg h-10 ml-2 text-sm flex-grow">
                        Is friend
                    </div>
                </div>
                <div class="mb-2"></div>
            </v-card-text>
        </v-card>
    </v-dialog>
</template>
<script>
import axios from 'axios';
import { format, parseISO } from 'date-fns';
import viLocale from 'date-fns/locale/vi';
import { useToast } from "vue-toastification";
import { Swiper, SwiperSlide } from 'vue-awesome-swiper';
import { Navigation } from 'swiper/modules';
import 'swiper/swiper-bundle.css';
import 'swiper/css';
import 'swiper/css/navigation';




export default {
    name: 'FeedView',

    components: {
        Swiper,
        SwiperSlide,
        // ShowImage
    },
    setup() {
        // Get toast interface
        const toast = useToast(

        );
        return {
            toast,
            modules: [Navigation]
        }
    },
    created() {
        const userString = localStorage.getItem('user');
        if (userString) {
            this.user = JSON.parse(userString);
        }
        this.getListOfFriends();
    },
    watch: {
        myFeeds: {
            handler(newFeeds) {
                this.feeds = [...newFeeds];
            },
            deep: true, // If myFeeds is an array or object, use deep to watch for changes in its content
            immediate: true // Run the handler immediately after the component is created
        }
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
    errorCaptured(err) {
        this.error = err;
        this.hasError = true;
        // Xử lý lỗi ở đây nếu cần thiết
        console.error('Lỗi đã được bắt:', err);
        // Trả về false để ngăn lỗi lan ra bên ngoài
        return false;
    },
    data() {
        return {
            hoveredItem: '',
            selectedItem: '',
            typeOfFilter: 'Tất cả bài viết',
            chosenFilter: 'allPosts',
            popoverRight: 0,
            popoverTop: 0,
            popoverLeft: 0,
            popoverLeftFilter: 0,
            popoverTopFilter: 0,
            posts: [],
            newComment: {
                postId: null,
                topComment: null,
                content: '',
                file: null
            },
            user: null,
            fileListHeight: "400px",
            body: '',
            newFeed: { content: '', files: [], audience: 'Public', friendTag: [] },
            hasTagFriend: false,
            showPostVisible: false,
            privateSetting: 'Công khai',
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
                // 'Bạn bè ngoại trừ',
                // 'Một số bạn bè',
                // 'Chỉ mình tôi'
            ],
            feeds: [],
            myFeeds: [],
            comments: [],
            friends: [],
            searchText: "",
            chosenUpdateComment: null,
            showVisibleLikedUsers: false,
            currentPage: 0,
            likedUsers: [],
            isUserScrollToBottom: false,
            error: null,
            hasError: false,
            numberOfUserPost: 0,
            isTableOptionVisible: false,
            currentCommentIndex: null,
            showVisibleUserInfo: false,
            userFound: null,
            displayedDate: '',
            sended: false,
            isFriend: false,
            inviteList: null,
            searchPhoneNumber: '',
            blockList: [],
            isBlock: false,
        }
    },
    mounted() {
        this.feeds = [];
        this.myFeeds = [];
        this.chooseTypeOfFilter('allPosts');
        this.fetchAvatar();
        this.countPostsUser();
        window.addEventListener('scroll', this.handleScroll);

    },
    beforeUnmount() {
        window.removeEventListener('scroll', this.handleScroll);
    },
    methods: {
        handleClickFilter(event, item) {
            // const rect = event.target.getBoundingClientRect();
            // const mouseX = rect.left;
            // const mouseY = rect.top;

            // const windowWidth = window.innerWidth + window.scrollX;
            // const windowHeight = window.innerHeight + window.scrollY;

            // const popupWidth = 180;
            // const popupHeight = 120;

            // let popupLeft, popupTop;

            // if (mouseX + popupWidth > windowWidth) {
            //     popupLeft = mouseX - popupWidth;
            // } else {
            //     popupLeft = mouseX;
            // }

            // if (mouseY + popupHeight > windowHeight) {
            //     popupTop = mouseY - popupHeight;
            // } else {
            //     popupTop = mouseY;
            // }

            // if (popupLeft < window.scrollX) {
            //     popupLeft = window.scrollX;
            // }

            // if (popupTop < window.scrollY) {
            //     popupTop = window.scrollY;
            // }

            // this.popoverLeftFilter = popupLeft + 'px';
            // this.popoverTopFilter = popupTop + 'px';
            this.popoverLeftFilter = "1516px";
            this.popoverTopFilter = "69px";
            this.togglePopover(item);
            event.stopPropagation();
        },
        chooseTypeOfFilter(item) {
            console.log("Item");
            console.log(item);
            console.log(item == 'allPosts');
            this.chosenFilter = item;
            if (item == 'allPosts') {
                console.log("All post")
                this.currentPage = 0;
                this.myFeeds = [];
                this.typeOfFilter = 'Tất cả bài viết';
                this.fetchFeed();
                this.clearSelectedItem();
            } else {
                this.typeOfFilter = 'Bài viết của tôi';
                this.currentPage = 0;
                this.myFeeds = [];
                this.fetchMyFeed();
                this.clearSelectedItem();
            }
        },
        handleChange(value) {
            console.log(value);
        },
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
            this.clearSelectedItem();
        },
        clearSelectedItem() {
            this.selectedItem = '';
        },
        handleClickAction(event, feed) {
            // const rect = event.target.getBoundingClientRect();
            // const x = rect.left;
            // const y = rect.top;
            // this.popoverRight = x - 680 + 'px';
            // this.popoverTop = y - 2 + 'px';
            // this.clickedFeed = feed;
            // this.showPopupVisible = !this.showPopupVisible;
            // event.stopPropagation();
            const rect = event.target.getBoundingClientRect();
            const mouseX = rect.left;
            const mouseY = rect.top;

            const windowWidth = window.innerWidth + window.scrollX;
            const windowHeight = window.innerHeight + window.scrollY;

            const popupWidth = 180;
            const popupHeight = 120;

            let popupLeft, popupTop;

            if (mouseX + popupWidth > windowWidth) {
                popupLeft = mouseX - popupWidth;
            } else {
                popupLeft = mouseX;
            }

            if (mouseY + popupHeight > windowHeight) {
                popupTop = mouseY - popupHeight;
            } else {
                popupTop = mouseY;
            }

            if (popupLeft < window.scrollX) {
                popupLeft = window.scrollX;
            }

            if (popupTop < window.scrollY) {
                popupTop = window.scrollY;
            }

            this.popoverLeft = popupLeft + 'px';
            this.popoverTop = popupTop + 'px';
            this.clickedFeed = feed;
            this.showPopupVisible = !this.showPopupVisible;
            event.stopPropagation();
        },
        formatTimeDifference(date) {
            const now = new Date();

            console.log("post time: " + date)
            console.log("time now: " + now)
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
        chooseComment(comment) {
            this.chosenUpdateComment = Object.assign({}, comment);
            console.log(this.chosenUpdateComment);
            this.isTableOptionVisible = false;
            // other actions, e.g., focusing on the comment input
        },
        updateComment(feedId) {

            // console.log("New comment: ", this.newComment);
            const formData = new FormData();
            formData.append('commentId', this.chosenUpdateComment.idComment);
            if (this.chosenUpdateComment.content) formData.append('content', this.chosenUpdateComment.content);
            if (this.chosenUpdateComment.contentMedia) formData.append('file', this.chosenUpdateComment.contentMedia);
            axios
                .put(`/social-media/update-comment/`, formData)
                .then(response => {
                    //console.log("Response status: " + response.status)
                    //response.data.getInfoPostResponse.forEach(p => console.log("Updated at: " + p.updatedAt))
                    if (response.status === 200) {
                        console.log(response.data);
                        if (response.data === 'Cập nhật bình luận thành công!') {
                            this.toast.success(response.data, 1500);
                            this.chosenUpdateComment = null;
                            this.clearContentComment();
                            this.fetchComment(feedId);
                            this.getFeedUpdate(feedId);
                        }
                    } else
                        this.toast.error(response.data, { timeout: 1500 });
                })
                .catch(error => {
                    console.log('error', error)
                })
        },
        deleteComment(commentId, feedId) {
            axios
                .delete(`/social-media/delete-comment/${commentId}`, {
                    headers: {
                        'Authorization': localStorage.getItem("token"),
                        'Content-Type': 'multipart/form-data',
                    }
                })
                .then(response => {
                    if (response.status === 200) {
                        console.log(response.data);
                        if (response.data === 'Xóa bình luận thành công!') {
                            this.toast.success("Xóa bình luận thành công!", 500);
                        }
                        this.fetchComment(feedId);
                        this.isTableOptionVisible = false;
                        this.getFeedUpdate(feedId);
                    }
                })
                .catch(error => {
                    console.log('error', error);
                });
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
        fetchFeed() {
            console.log("Gọi hàm: fetchFeed()");
            // console.log("User đang đăng nhập: " + this.user);
            axios
                .get(`/social-media/get-post/${this.currentPage}`,/**{
                    headers: {
                        'Authorization': localStorage.getItem("token"),
                        'Content-Type': 'multipart/form-data',
                    }
                }**/)
                .then(response => {
                    //console.log("Response status: " + response.status)
                    //response.data.getInfoPostResponse.forEach(p => console.log("Updated at: " + p.updatedAt))
                    this.myFeeds.push(...response.data.getInfoPostResponse);
                    this.myFeeds.forEach(feed => {
                        this.updateIsUserLikePost(feed);
                        if (feed.postFather) {
                            this.updateIsUserLikePost(feed.postFather);
                        }
                    });
                    console.log("Danh sách bài viết: ", this.feeds);
                })
                .catch(error => {
                    console.log('error', error)
                })
        },
        fetchMyFeed() {
            console.log("Gọi hàm: fetchMyFeed()");
            // console.log("User đang đăng nhập: " + this.user);
            axios
                .get(`/social-media/get-post-user/${this.currentPage}`,/**{
                    headers: {
                        'Authorization': localStorage.getItem("token"),
                        'Content-Type': 'multipart/form-data',
                    }
                }**/)
                .then(response => {
                    //console.log("Response status: " + response.status)
                    //response.data.getInfoPostResponse.forEach(p => console.log("Updated at: " + p.updatedAt))
                    if (this.currentPage === 0) {
                        this.myFeeds = response.data.getInfoPostResponse;
                        this.myFeeds.forEach(feed => {
                            this.updateIsUserLikePost(feed);
                            if (feed.postFather) {
                                this.updateIsUserLikePost(feed.postFather);
                            }
                        });
                        console.log("Danh sách bài viết: ", this.myFeeds);
                    }
                    else {
                        this.myFeeds.push(...response.data.getInfoPostResponse);
                        this.myFeeds.forEach(feed => {
                            this.updateIsUserLikePost(feed);
                            if (feed.postFather) {
                                this.updateIsUserLikePost(feed.postFather);
                            }
                        });
                        console.log("Danh sách bài viết: ", this.myFeeds);
                    }
                })
                .catch(error => {
                    console.log('error', error)
                })
        },
        showTableOption(index) {
            if (this.currentCommentIndex === index) {
                this.isTableOptionVisible = !this.isTableOptionVisible;
            } else {
                this.isTableOptionVisible = true;
            }
            this.currentCommentIndex = index;
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
        deletePost(postId) {
            console.log("Gọi hàm: deletePost");
            console.log("Post id:", postId);
            //this.posts = this.posts.filter(post => post.id !== id)
            axios
                .delete(`/social-media/delete-post/${postId}`)
                .then(response => {
                    //console.log("Response status: " + response.status)
                    //response.data.getInfoPostResponse.forEach(p => console.log("Updated at: " + p.updatedAt))
                    if (response.status === 200) {
                        this.toast.success("Delete post successfully!", 1500);
                        if (this.showPopupVisible) {
                            this.showPopupVisible = false;
                        }
                        if (this.showVisibleInfoFeed) {
                            this.showVisibleInfoFeed = false;
                        }
                        this.countPostsUser();
                        this.feeds = this.feeds.filter(element => {
                            return element.id !== postId && (!element.postFather || element.postFather.id !== postId);
                        });
                    }
                    else
                        this.toast.error("Có lỗi xảy ra, vui lòng thử lại!", 1500);
                })
                .catch(error => {
                    console.log('error', error)
                    this.toast.error("Có lỗi xảy ra, vui lòng thử lại!", 1500);
                })
        },
        showPostOption() {
            console.log("Gọi hàm: showPostOption");
            this.showPostVisible = true;
        },
        closePostOption() {
            console.log("Gọi hàm: closePostOption");
            this.clearContentPost();
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

                let originalDate = new Date(date);

                // Trừ 7 giờ
                originalDate.setHours(originalDate.getHours() - 7);

                // Gọi hàm format để định dạng ngày giờ
                return format(originalDate, 'HH:mm dd/MM/yyyy');
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
                // this.fileListHeight = "400px";
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
                console.log("Là ảnh hay không: ", mediaUrl.includes("/media/getImage/"));
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
                this.updateFeed.files = [];
                // this.updateFeed.files=[];
            } else {
                this.newFeed.files = [];
            }
            // this.fileListHeight = "100px";
        },
        removeFile(index) {
            if (!this.showUpdatePostVisible) {
                this.newFeed.files.splice(index, 1);
                // if (this.newFeed.files.length === 0) {
                //     this.fileListHeight = "100px";
                // }
            } else {
                this.updateFeed.files.splice(index, 1);
                // if (this.newFeed.files.length === 0) {
                //     this.fileListHeight = "100px";
                // }
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
                }
                // } else if (this.privateSetting === 'Chỉ mình tôi') {
                //     this.newFeed.audience = 'OnlyMe'
                // } else if (this.privateSetting === 'Một số bạn bè') {
                //     this.newFeed.audience = 'SomeOneCanSee'
                // } else {
                //     this.newFeed.audience = 'AllExceptSomeOne'
                // }

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

                if (this.newFeed.files && this.newFeed.files.length > 0) {
                    for (let i = 0; i < this.newFeed.files.length; i++) {
                        const file = this.newFeed.files[i];
                        if (typeof file === 'string') {
                            const response = await fetch(file);
                            const blob = await response.blob();
                            const filename = file.substring(file.lastIndexOf('/') + 1);
                            formData.append('files', blob, filename);
                        } else {
                            formData.append('files', file);
                        }
                    }
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
                    this.clearContentPost();
                    this.countPostsUser();
                    const createdFeed = await this.getPostInfo(response.data.getInfoPostResponse.id)
                    this.feeds.unshift(createdFeed);
                    console.log(createdFeed)
                    this.updateIsUserLikePost(createdFeed);
                    this.toast.success(response.data.mesage, { timeout: 3000 });
                } else {
                    this.toast.error(response.data.mesage, { timeout: 3000 });
                }
            } catch (error) {
                if (error.response) {
                    if (error.response.status === 400) {
                        console.log(error)
                        this.toast.error(error.response.data.mesage, { timeout: 3000 });
                    } else {
                        console.log(error)
                        this.toast.error(error.response.data.mesage, { timeout: 3000 });
                    }
                } else if (error.request) {
                    console.log('Không nhận được phản hồi từ máy chủ. Vui lòng thử lại!')
                    this.toast.error('Không nhận được phản hồi từ máy chủ. Vui lòng thử lại!', { timeout: 3000 });
                } else {
                    console.log('Error setting up the request:' + error.message)
                    this.toast.error('Error setting up the request:' + error.message, { timeout: 3000 });
                }
            }
        },
        async updatePost(post) {
            console.log("Gọi hàm: updatePost");
            try {
                console.log("UpdatePost: ", post);

                // Thiết lập audience dựa trên privateSetting
                switch (this.privateSetting) {
                    case 'Công khai':
                        this.newFeed.audience = 'Public';
                        break;
                    default:
                        this.newFeed.audience = 'AllFriend';
                    // case 'OnlyMe':
                    //     this.newFeed.audience = 'OnlyMe';
                    //     break;
                    // case 'SomeOneCanSee':
                    //     this.newFeed.audience = 'SomeOneCanSee';
                    //     break;
                    // default:
                    //     this.newFeed.audience = 'AllExceptSomeOne';
                }

                const listFriendTag = [];
                // Xử lý userTagIDList từ friendTag
                if (this.newFeed.friendTag) {
                    for (let friendTagKey in this.newFeed.friendTag) {
                        if (Object.prototype.hasOwnProperty.call(this.newFeed.friendTag, friendTagKey)) {
                            const friendTagValue = this.newFeed.friendTag[friendTagKey];
                            listFriendTag.push(friendTagValue.phoneNumber);
                        }
                    }
                }

                const formData = new FormData();
                formData.append('content', post.content);
                formData.append('audience', this.newFeed.audience);
                formData.append('userTagIDList', listFriendTag);

                // Xử lý tệp tin
                if (post.files && post.files.length > 0) {
                    for (let i = 0; i < post.files.length; i++) {
                        const file = post.files[i];
                        if (typeof file === 'string') {
                            const response = await fetch(file);
                            const blob = await response.blob();
                            const filename = file.substring(file.lastIndexOf('/') + 1);
                            formData.append('files', blob, filename);
                        } else {
                            formData.append('files', file);
                        }
                    }
                }

                // Gửi yêu cầu POST đến API
                const response = await axios.post(`social-media/update-post/${post.id}`, formData);

                console.log(response);

                // Xử lý phản hồi từ máy chủ
                if (response.status === 200) {
                    this.showPostVisible = false;
                    this.updateFeed = null;

                    this.getFeedUpdate(post.id);

                    this.closeUpdatePostOption();
                    if (this.showVisibleInfoFeed) {
                        this.closeFeedInfo();
                    }
                    this.toast.success(response.data, { timeout: 1000 });
                } else {
                    this.toast.error(response.data, { timeout: 1000 });
                }
            } catch (error) {
                // Xử lý các loại lỗi
                if (error.response) {
                    this.toast.error(error.response.data, { timeout: 1000 });
                } else if (error.request) {
                    this.toast.error('Không nhận được phản hồi từ máy chủ. Vui lòng thử lại!', { timeout: 1500 });
                } else {
                    this.toast.error('Error setting up the request:' + error.message, { timeout: 1500 });
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
                foundFeed.isLike = !foundFeed.isLike;

                // Gọi hàm fetchUpdateLike để cập nhật trạng thái này lên server
                this.fetchUpdateLike(feed.id);

                // Nếu có đang hiển thị chi tiết feed (this.showVisibleInfoFeed), hãy cập nhật cho feed này
                if (this.showVisibleInfoFeed && this.showingFeed && this.showingFeed.id === feed.id) {
                    this.showingFeed.isLike = foundFeed.isLike;
                }

                // Cập nhật lại trong danh sách feeds
                const index = this.feeds.findIndex(f => f.id === feed.id);
                if (index !== -1) {
                    this.feeds[index] = foundFeed; // Directly update the array element
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
            this.chosenUpdateComment = null;
        },
        handleFileChange(event) {
            console.log("Gọi hàm: handleFileChange");
            const selectedFile = event.target.files[0];
            //console.log(selectedFile);
            if (this.chosenUpdateComment == null) {
                this.newComment.file = selectedFile;
            } else {
                this.chosenUpdateComment.contentMedia = selectedFile;
            }

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
                        this.toast.success(response.data, { timeout: 1500 });
                        this.clearContentComment();
                        this.fetchComment(postId);
                        this.getFeedUpdate(postId);
                        const commentContainer = document.querySelector('#dialog-content');
                        console.log('scroll element', commentContainer)
                        console.log('scroll element', commentContainer.scrollHeight)
                        commentContainer.scrollTo(0, 500)
                    }
                    else
                        this.toast.error(response.data, { timeout: 1500 });


                })
                .catch(error => {
                    console.log('error', error)
                })


        },
        clearContentComment() {
            this.newComment.content = '';
            this.newComment.file = null;

        },
        clearContentPost() {
            this.newFeed.content = '';
            this.newFeed.files = [];
            this.newFeed.audience = 'Public';
            this.newFeed.friendTag = [];
            // this.fileListHeight = "100px";
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
            //console.log(clickedFeed)
            this.showPopupVisible = false;
            this.updateFeed = Object.assign({}, clickedFeed);
            this.showUpdatePostVisible = true;
            if (clickedFeed.audience === 'Public') {
                this.privateSetting = "Công khai"
            } else if (clickedFeed.audience === 'AllFriend') {
                this.privateSetting = "Bạn bè"
            }
            // } else if (clickedFeed.audience === 'SomeOneCanSee') {
            //     this.privateSetting = "Một số bạn bè"
            // } else if (clickedFeed.audience === 'AllExceptSomeOne') {
            //     this.privateSetting = "Bạn bè ngoại trừ"
            // }
        },
        closeUpdatePostOption() {
            this.showUpdatePostVisible = false;
        },
        async sharePost(feed) {
            if (confirm("Bạn có muốn chia sẻ bài viết?")) {
                console.log("Feed: ", feed)
                try {

                    // console.log(listFriendTag)

                    // console.log(typeof (listFriendTag))

                    const formData = new FormData();

                    formData.append('postTopId', feed.id);

                    const response = await axios.post(`social-media/create-new-post`, formData);

                    if (response.status === 200) {
                        // this.showPostVisible = false;
                        // this.newFeed.content = '';
                        const createdFeed = await this.getPostInfo(response.data.getInfoPostResponse.id)
                        this.feeds.unshift(createdFeed);
                        console.log(createdFeed)
                        this.updateIsUserLikePost(createdFeed);
                        this.closeFeedInfo();
                        this.toast.success(response.data.mesage, { timeout: 3000 });
                    } else {
                        this.toast.error(response.data.mesage, { timeout: 3000 });
                    }
                } catch (error) {
                    if (error.response) {
                        if (error.response.status === 400) {
                            console.log(error.response.data)
                            this.toast.error(error.response.data.mesage, { timeout: 3000 });
                        } else {
                            console.log(error.response.data)
                            this.toast.error(error.response.data.mesage, { timeout: 3000 });
                        }
                    } else if (error.request) {
                        console.log('Không nhận được phản hồi từ máy chủ. Vui lòng thử lại!')
                        // this.toast.error('Không nhận được phản hồi từ máy chủ. Vui lòng thử lại!', { timeout: 3000 });
                    } else {
                        console.log('Error setting up the request:' + error.message)
                        // this.toast.error('Error setting up the request:' + error.message, { timeout: 3000 });
                    }
                }

            }
        },
        openListLikedUserDialog(id) {
            if (id) {
                this.getAllUserLiked(id);
            } else {
                console.error('ID is null or undefined:', id);
                // Optionally handle this case, such as showing an error message or logging it
            }
        },
        closeListLikedUserDialog() {
            this.showVisibleLikedUsers = false;
        },
        async handleScroll() {
            const container = this.$refs.scrollContainer;
            if (container.clientHeight + container.scrollTop > container.scrollHeight - 1000) {
                if (!this.isUserScrollToBottom) {
                    this.isUserScrollToBottom = true;
                    if (this.chosenFilter == 'allPosts') {
                        this.currentPage++;
                        await this.fetchFeed();
                    }
                    else {
                        this.currentPage++;
                        await this.fetchMyFeed();
                    }
                }
            } else {
                this.isUserScrollToBottom = false;
            }
        },
        async getAllUserLiked(id) {
            try {
                const response = await axios.get(`social-media/get-all-users-liked-post/${id}`);

                if (response.status === 200) {
                    console.log(response)
                    this.likedUsers = response.data.getAllUsersLikedPostResponses;
                    this.showVisibleLikedUsers = true;
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
        onSwiper(swiper) {
            // Store swiper instance in data
            this.swiperInstance = swiper;
            console.log('Swiper instance:', swiper);
        },
        onSlideChange() {
            // Check if swiperInstance is defined
            if (this.swiperInstance) {
                // Access activeIndex from swiperInstance
                console.log('Slide changed:', this.swiperInstance.activeIndex);
            }
        },
        countPostsUser() {
            console.log("Gọi hàm: countPostsUser()");
            // console.log("User đang đăng nhập: " + this.user);
            axios
                .get(`/social-media/countPostsUsers`,/**{
                    headers: {
                        'Authorization': localStorage.getItem("token"),
                        'Content-Type': 'multipart/form-data',
                    }
                }**/)
                .then(response => {
                    //console.log("Response status: " + response.status)
                    //response.data.getInfoPostResponse.forEach(p => console.log("Updated at: " + p.updatedAt))
                    this.numberOfUserPost = response.data;
                })
                .catch(error => {
                    console.log('error', error)
                })
        },
        formattedBirthday() {
            try {
                if (this.userFound && this.userFound.birthDay) {
                    const parsedDate = parseISO(this.userFound.birthDay);
                    this.displayedDate = format(parsedDate, "dd 'tháng' MM, yyyy", { locale: viLocale });
                }
            } catch (exception) {
                console.log("Error in formattedBirthday", exception);
            }
        },
        showFindFriendDialog() {
            try {
                this.searchPhoneNumber = '';
            } catch (exception) {
                console.log("Error in showFindFriendDialog", exception);
            }
        },
        showUserInfoDialog(friend) {
            try {
                this.userFound = friend;
                this.formattedBirthday();
                this.showVisibleUserInfo = true;
            } catch (exception) {
                console.log("Error in showUserInfoDialog", exception);
            }
        },
        closeUserInfoDialog() {
            try {
                this.showVisibleUserInfo = false;
            } catch (exception) {
                console.log("Error in closeUserInfoDialog", exception);
            }
        },
        async showFoundUserDialog() {
            try {
                const responseUser = await axios.get(`users/findUserByPhoneNumber/${this.searchPhoneNumber}`, {
                    headers: {
                        'Authorization': localStorage.getItem("token")
                    }
                });

                if (responseUser.status === 200) {
                    const userTemp = responseUser.data;
                    await this.getInviteFriend();
                    this.checkUser();
                    this.showUserInfoDialog(userTemp);
                } else {
                    console.error(responseUser.data.msg);
                    this.toast.error(responseUser.data || 'Đã xảy ra lỗi!', { timeout: 1500 });
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
        async addFriend() {
            try {

                const userString = localStorage.getItem('user');
                if (userString) {
                    const sender = JSON.parse(userString);

                    const addFriendRequest = {
                        fromPhoneNumberUser: sender.phoneNumber,
                        toPhoneNumberUser: this.userFound.phoneNumber,
                        isAcceptingInvite: false
                    }

                    console.log(addFriendRequest)

                    const responseUser = await axios.post(`users/sendInviteFriend`, addFriendRequest, {
                        headers: {
                            'Authorization': localStorage.getItem("token")
                        }
                    });

                    if (responseUser.status === 200) {
                        this.toast.success(responseUser.data)
                        this.showVisibleUserInfo = false;
                        this.searchPhoneNumber = '';
                    } else {
                        console.error(responseUser.data);
                        this.toast.error(responseUser.data || 'Đã xảy ra lỗi!', { timeout: 1500 });
                    }
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
        async blockUser(phoneNumber) {
            try {
                const friendRequest = {
                    fromPhoneNumberUser: this.user.phoneNumber,
                    toPhoneNumberUser: phoneNumber,
                }

                const response = await axios.post(`users/blockFriendUser`, friendRequest, {
                    headers: {
                        'Authorization': localStorage.getItem("token")
                    }
                });

                if (response.status === 200) {
                    await this.getListOfFriends();
                    this.showVisibleUserInfo = false;
                    this.searchPhoneNumber = '';
                    this.toast.success(response.data, { timeout: 1500 });
                } else {
                    console.error(response.body);
                    this.toast.error(response.data || 'Đã xảy ra lỗi!', { timeout: 1500 });
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
        async getInviteFriend() {
            try {

                const response = await axios.get(`users/getAllInviteFriend`, {
                    headers: {
                        'Authorization': localStorage.getItem("token")
                    }
                });

                if (response.status === 200) {
                    this.inviteList = response.data;
                } else {
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
        async checkUser() {
            try {
                await this.getInviteFriend();
                await this.getListOfFriends();
                await this.getListOfBlockedFriends();

                const isFriend = this.listFriends.some(friend => friend.phoneNumber === this.searchPhoneNumber);
                const isInvited = this.inviteList.some(friend => friend.phoneNumber === this.searchPhoneNumber);
                const isBlock = this.blockList.some(friend => friend.phoneNumber === this.searchPhoneNumber);

                if (isFriend) {
                    this.isFriend = true;
                    this.sended = false;
                } else {
                    this.isFriend = false;

                    if (isInvited) {
                        this.sended = true;
                    } else {
                        this.sended = false;
                    }
                }

                this.isBlock = isBlock;

                // Additional checks
                if (this.listFriends.length === 0 && this.inviteList.length === 0) {
                    this.isFriend = false;
                    this.sended = false;
                }
            } catch (exception) {
                console.log("Error in checkUser", exception);
            }
        },
        handleOpenUserDialog(phoneNumber) {
            this.searchPhoneNumber = phoneNumber;
            this.showFoundUserDialog();
        },
        async getListOfBlockedFriends() {
            try {
                const response = await axios.get(`users/getAllBlockedUser`, {
                    headers: {
                        'Authorization': localStorage.getItem("token")
                    }
                });

                if (response.status === 200) {

                    this.blockList = response.data;
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
        async unBlockUser(phoneNumber) {
            try {
                const friendRequest = {
                    fromPhoneNumberUser: this.user.phoneNumber,
                    toPhoneNumberUser: phoneNumber,
                }

                const response = await axios.post(`users/unBlockFriendUser`, friendRequest, {
                    headers: {
                        'Authorization': localStorage.getItem("token")
                    }
                });

                if (response.status === 200) {
                    this.blockList = this.blockList.filter(friend => friend.phoneNumber !== phoneNumber);
                    this.showVisibleUserInfo = false;
                    this.searchPhoneNumber = '';
                    this.toast.success(response.data, { timeout: 1500 });
                } else {
                    console.error(response.body);
                    this.toast.error(response.data || 'Đã xảy ra lỗi!', { timeout: 1500 });
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
        async getPostInfo(postId) {
            try {
                const response = await axios.get(`social-media/getAPostInfo/${postId}`);
                if (response.status === 200 && response.data && response.data.id) {
                    return response.data;
                } else {
                    this.toast.error(response.data || 'Post not found!', { timeout: 1500 });
                    return null;
                }
            } catch (error) {
                if (error.response) {
                    // this.toast.error(error.response.data, { timeout: 1500 });
                    console.log(error.response.data)
                } else if (error.request) {
                    // this.toast.error('No response from the server. Please try again!', { timeout: 1500 });
                    console.log('No response from the server. Please try again!')
                } else {
                    // this.toast.error('Error setting up the request: ' + error.message, { timeout: 1500 });
                    console.log('Error setting up the request: ' + error.message)
                }
                return null;
            }
        },
        async getFeedUpdate(postId) {
            try {
                const updatedPostInfo = await this.getPostInfo(postId);
                if (updatedPostInfo) {
                    const feedIndex = this.feeds.findIndex(feed => feed.id === postId);
                    if (feedIndex !== -1) {
                        this.feeds[feedIndex] = updatedPostInfo;
                        this.updateIsUserLikePost(this.feeds[feedIndex]);
                        if (this.feeds[feedIndex].postFather) {
                            this.updateIsUserLikePost(this.feeds[feedIndex].postFather);
                        }
                        console.log('Feed updated successfully!')
                    } else {
                        console.log('Feed not found!')
                    }
                }
            } catch (error) {
                console.error(error);
                // this.toast.error('An error occurred while updating the feed.', { timeout: 1500 });
            }
        }
    },
}
</script>

<style lang="scss" scoped>
.table-option-comment {
    p {
        cursor: pointer;
    }

    p:hover {
        background-color: #ccc;
    }
}

.post-container {
    display: flex;
    flex-direction: column;
    height: 100vh;
}

.container {
    height: 100%;
    width: 100%;
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

.dialog-container-liked-user {

    width: 500px;

    .dialog-component-liked-user {
        .dialog-title-liked-user {
            .title-liked-user {
                display: flex;
                justify-content: space-between;
                align-items: center;

                .icon-close-liked-user {
                    cursor: pointer;
                }
            }
        }

        .dialog-content-liked-user {
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

            .avatar-container-liked-user {
                height: 80px;
                display: flex;
                align-items: center;
                margin-left: 14px;
                margin-right: 4px;
            }

            .avatar-wrapper-liked-user {
                width: 50px;
                height: 50px;
                display: flex;
                justify-content: center;
                align-items: center;
                overflow: hidden;
                border-radius: 50%;
                margin-right: 16px;
            }

            .avatar-liked-user {
                width: 100%;
                height: 100%;
                object-fit: cover;
            }

            .detail-liked-user {
                flex: 1;

                .form-control {
                    background-color: #cccc;
                    border-radius: 5%;
                    text-align: center;
                }
            }

            .friend-info-liked-user {
                display: flex;
                align-items: center;
                justify-content: space-between;
            }

            .profile-action-liked-user {
                height: 40px;
                display: flex;
            }
        }
    }

    .position-relative {
        position: relative;
    }

    .friend-container-liked-user {
        overflow-x: hidden;
        display: flex;
        justify-content: space-between;
        margin-right: 10px;
        transition: box-shadow 0.3s, transform 0.3s;
        cursor: pointer;
    }

    .friend-container-liked-user:hover {
        box-shadow: 0 0 5px rgba(0, 0, 0, 0.3);
        transform: scale(1.05);
    }

    .search-input-liked-user {
        margin-top: 8px;
        padding: 4px;
        width: 100%;
        box-sizing: border-box;
        border: 1px solid #ccc;
        border-radius: 8px;
    }

    .friend-list-container-liked-user {
        height: 250px;
        overflow-y: auto;
        overflow-x: hidden;
        border: 1px solid #ccc;
        border-radius: 8px;
    }

    .action-liked-user {
        margin-left: 8px;
        cursor: pointer;
    }

    .add-friend-button-liked-user {
        background-color: #007BFF;
        color: #ffffff;
        border-radius: 5px;
        padding: 8px 16px;
        border: none;
    }

    .add-friend-button-liked-user:hover {
        transform: scale(1.05);
    }

    .cancel-button-liked-user,
    .search-button-liked-user {
        padding: 8px 16px;
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
    width: fit-content;
}

@media only screen and (max-width: 776px) {
    #display-number {
        flex-direction: column;
        justify-content: flex-start;
    }

    #display-number-element-2 {
        margin-left: 0;
    }

    .dialog-container-feed {
        max-width: 710px !important;
    }
}

.filter-wrapper {
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: #ffffff;
}

.filter-contact {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: space-between;
    flex-wrap: wrap;
    padding-top: 16px;
    padding-bottom: 16px;

    .popover {
        display: inline-block;
        position: absolute;
        //background: #fff;
        //padding: 8px;
        border-radius: 4px;
        z-index: 9999;
        box-shadow: 0 0 10px 0 rgba(0, 0, 0, 0.2);
        border: 1px solid #d6dbe1;
    }

    .popover-content {
        position: absolute;
        background-color: #fff;
        box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);

        .popover-item {
            display: flex;
            align-items: center;
            height: 40px;
            cursor: pointer;

            #check-icon {
                color: #005ae0;
                margin-left: 8px;
                margin-right: 14px;
            }

            .truncate {
                white-space: nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
            }
        }
    }
}

.filter-contact-child {
    flex-grow: 1;
    flex-basis: 0;
    display: flex;
    height: 30px;
    width: 240px;
    align-items: center;
    padding: 4px 8px;
    border: 1px solid #ccc;
    border-radius: 4px;
    background-color: #f4f4f4;
    cursor: pointer;
    /* Thêm margin-bottom để tạo khoảng cách giữa các phần tử */

    span {
        margin-left: 16px;
    }
}

.filter-contact-child:not(:last-child) {
    margin-right: 8px;
    margin-bottom: 8px;
    /* Thêm margin-bottom để tạo khoảng cách giữa các phần tử */
}

#down-icon {
    margin-left: auto;
    margin-right: 6px;
}

.hoveredFilter {
    background-color: #d6dbe1;
}

.hovered {
    cursor: pointer;
    background-color: #f3f5f6;
}

.selected {
    color: #005ae0;
    background: #e5efff;
    border: #e5efff;
}

.swiper-button-next,
.swiper-button-prev {
    color: #fff;
    /* Text color */
    background-color: rgba(0, 0, 0, 0.5);
    /* Semi-transparent background for visibility */
    border-radius: 50%;
    /* Circular shape */
    width: 60px;
    /* Size of the button */
    height: 60px;
    /* Size of the button */
    display: flex;
    align-items: center;
    justify-content: center;
    transition: background-color 0.3s, transform 0.3s;
    /* Smooth transitions */
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.5);
    /* Subtle shadow for better contrast */
}

.swiper-button-next::after,
.swiper-button-prev::after {
    font-size: 30px;
    /* Size of the arrow icons */
}

.swiper-button-next:hover,
.swiper-button-prev:hover {
    background-color: rgba(0, 0, 0, 0.7);
    /* Darker on hover */
    transform: scale(1.1);
    /* Slightly larger on hover */
}

.swiper-button-next:active,
.swiper-button-prev:active {
    background-color: rgba(0, 0, 0, 0.9);
    /* Even darker when active */
}

.swiper-button-next:focus,
.swiper-button-prev:focus {
    outline: none;
    /* Remove default focus outline */
    box-shadow: 0 0 0 3px rgba(255, 255, 255, 0.7);
    /* Custom focus outline */
}

.dialog-container-user {
    .dialog-component-user {
        .dialog-title-user {
            .title-user {
                display: flex;
                justify-content: space-between;
                align-items: center;

                .icon-close-user {
                    cursor: pointer;
                }
            }
        }

        .dialog-content-user {
            padding: 0;
            display: flex;
            flex-direction: column;
            position: relative;
            height: auto;

            .profile-photo-user {
                position: relative;
                display: block;

                .cover-avatar-user {
                    position: relative;
                    display: table;
                    margin: auto;

                    .cover-image-user {
                        cursor: pointer;
                        width: 352px;
                        height: 152px;
                        object-fit: cover;
                    }
                }

                .ava-name-container-user {
                    text-align: center;
                    left: 0;
                    width: 100%;
                    color: #fff;
                    position: relative;
                    display: flex;
                    background: transparent;
                    margin-top: -48px;
                    flex-direction: column;

                    .avatar-profile-user {
                        cursor: pointer;
                        align-self: center;
                        display: block;

                        .avatar-user {
                            width: 80px;
                            height: 80px;
                            min-width: 80px;
                            min-height: 80px;
                            position: relative;
                            color: #fff;
                            overflow: hidden;
                            box-sizing: border-box;

                            .avatar-image-user {
                                display: flex;
                                justify-content: center;
                                align-items: center;
                                background: #e6e8ea;
                                color: #7589a3;
                                overflow: hidden;
                                object-fit: cover;
                                font-size: 1.25rem;
                                font-weight: 500;
                                line-height: 1.5;
                                border-width: 2px;
                                box-sizing: border-box;
                                border-radius: 50%;
                                border: 2px solid #fff;
                                width: 80px;
                                height: 80px;
                                min-width: 80px;
                                min-height: 80px;
                                overflow-clip-margin: content-box;
                            }
                        }
                    }

                    .fullname-profile-user {
                        width: 100%;
                        color: #081c36;
                        font-size: 1.125rem; // Thay đổi font-size thành 1.125rem
                        font-weight: 500; // Thay đổi font-weight thành 500
                        line-height: 1.5;
                        display: flex;
                        flex-direction: column;
                        z-index: 1;
                        margin-top: 10px;

                        .fullname-user {
                            position: relative;
                            width: fit-content;
                            margin: auto;
                            max-width: 271px;
                            text-overflow: ellipsis;
                        }
                    }
                }
            }

            .profile-information-user {

                .profile-header-user {
                    margin-top: 5px;
                    margin-bottom: 10px;
                    margin-left: 3px;
                }

                .user-profile-details-user {
                    display: flex;
                    flex-direction: column;

                    .user-profile-item-user {
                        display: flex;
                        justify-content: space-between;
                        margin-bottom: 10px;

                        .title-user {
                            flex-basis: 40%;
                            margin-left: 3px;
                        }

                        .content-user {
                            flex-basis: 60%;
                        }
                    }
                }
            }

            .profile-action-user {
                height: 40px;
                display: flex;
            }
        }

    }

    .block-button,
    .add-friend-button {
        padding: 8px 16px;
    }
}

@media only screen and (min-width:768px) and (max-width:787px) {
    #general_image {
        height: 115px;
    }
}

@media only screen and (min-width:788px) and (max-width:1023px) {
    #general_image {
        height: 130px;
    }
}

@media only screen and (min-width:1024px) and (max-width:1070px) {
    #general_image {
        height: 180px;
    }
}

@media only screen and (min-width: 1280px) {
    .container {
        max-width: 1920px;
    }

}
</style>
