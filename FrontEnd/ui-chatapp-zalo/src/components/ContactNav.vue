<template>
    <div id="container" style="display: flex;">
        <div id="contact-nav">
            <div id="chat-message">
                <div id="search-content">
                    <font-awesome-icon icon="fa-solid fa-magnifying-glass" />
                    <input type="text" id="contact-input-search" placeholder="Tìm kiếm">
                    <div title="Thêm bạn" class="cursor-pointer" @click="showFindFriendDialog">
                        <font-awesome-icon icon="fa-solid fa-user-plus" />
                    </div>
                    <div title="Tạo nhóm chat">
                        <font-awesome-icon icon="fa-solid fa-users" />
                    </div>
                </div>
                <div id="menu-friend">
                    <div class="menu-contact">
                        <div class="menu-item"
                            :class="{ 'selected': selectedMenuItem === 'friends', 'hovered': hoveredItem === 'friends', 'hovering': selectedMenuItem === 'friends' }"
                            @mouseenter="!isSelected('friends') && (hoveredItem = 'friends')" @mouseleave="hoveredItem = ''"
                            @click="selectMenuItem('friends')">
                            <a id="user-group-icon">
                                <font-awesome-icon icon="fa-solid fa-user-group" />
                            </a>
                            <p class="menu-name">
                                <span>Danh sách bạn bè</span>
                            </p>
                        </div>
                        <div class="menu-item"
                            :class="{ 'selected': selectedMenuItem === 'groups', 'hovered': hoveredItem === 'groups', 'hovering': selectedMenuItem === 'groups' }"
                            @mouseenter="!isSelected('groups') && (hoveredItem = 'groups')" @mouseleave="hoveredItem = ''"
                            @click="selectMenuItem('groups')">
                            <a id="group-list-icon">
                                <font-awesome-icon icon="fa-solid fa-users-line" />
                            </a>
                            <p class="menu-name">
                                <span>Danh sách nhóm</span>
                            </p>
                        </div>
                        <div class="menu-item"
                            :class="{ 'selected': selectedMenuItem === 'friendRequests', 'hovered': hoveredItem === 'friendRequests', 'hovering': selectedMenuItem === 'friendRequests' }"
                            @mouseenter="!isSelected('friendRequests') && (hoveredItem = 'friendRequests')"
                            @mouseleave="hoveredItem = ''" @click="selectMenuItem('friendRequests')">
                            <a id="request-list-icon">
                                <font-awesome-icon icon="fa-solid fa-envelope-open" />
                            </a>
                            <p class="menu-name">
                                <span>Lời mời kết bạn</span>
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div style="width: 77%;">
            <div class="set-width" v-if="selectedMenuItem === 'friends'">
                <FriendList></FriendList>
            </div>
            <div class="set-width" v-if="selectedMenuItem === 'groups'">
                <GroupList></GroupList>
            </div>
            <div class="set-width" v-if="selectedMenuItem === 'friendRequests'">
                <InvitationFriendManage></InvitationFriendManage>
            </div>
        </div>
        <v-dialog class="dialog-container" v-model="showVisibleFindFriendDialog" max-width="500px"
            @click:outside="closeFindFriendDialog">
            <v-card class="dialog-component">
                <v-card-title class="dialog-title">
                    <h2 class="title">Thêm bạn
                        <div class="icon-close" @click="closeFindFriendDialog"><font-awesome-icon icon="fa-solid fa-x" />
                        </div>
                    </h2>
                </v-card-title>
                <hr style="border: none; border-bottom: 1px solid #ccc;">
                <v-card-text class="dialog-content">
                    <div class="pt-1 pl-4 pr-4 pb-2">
                        <input type="text" v-model="searchPhoneNumber" placeholder="Số điện thoại"
                            title="Vui lòng nhập số điện thoại" class="search-input" />
                        <div class="mt-4"><span>Có thể bạn quen</span></div>
                        <div class="friend-list-container">
                            <div v-for="friend in friends" v-bind:key="friend.phoneNumber" class="position-relative">
                                <div class="friend-info friend-container">
                                    <div class="avatar-container" @click="showUserInfoDialog(friend)">
                                        <div class="avatar-wrapper">
                                            <img :src="friend.imageAvatar" class="avatar">
                                        </div>
                                    </div>
                                    <div class="detail" @click="showUserInfoDialog(friend)">
                                        <span>{{ friend.userName }}</span>
                                    </div>
                                    <div class="action" @click="test2">
                                        <button class="add-friend-button">Thêm bạn</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="profile-action">
                        <div class="cancel-button text-center cursor-pointer bg-gray-400 text-black rounded-lg h-10 mx-4 w-1/2"
                            @click="closeFindFriendDialog">
                            Hủy
                        </div>
                        <div class="search-button text-center cursor-pointer bg-blue-500 text-white rounded-lg h-10 mx-4 w-1/2"
                            @click="showFoundUserDialog">
                            Tìm kiếm
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
                    <h2 class="title-user">Thông tin tài khoản
                        <div class="icon-close-user" @click="closeUserInfoDialog"><font-awesome-icon icon="fa-solid fa-x" />
                        </div>
                    </h2>
                </v-card-title>
                <hr style="border: none; border-bottom: 1px solid #ccc;">
                <v-card-text class="dialog-content-user">
                    <div class="profile-photo-user">
                        <div class="cover-avatar-user">
                            <img class="cover-image-user" :src="userFound.imageCoverAvatar" alt="None"
                                crossorigin="anonymous">
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
                            <strong>Thông tin cá nhân</strong>
                        </div>
                        <div>
                            <div class="user-profile-details-user">
                                <div class="user-profile-item-user">
                                    <span class="title-user">Điện thoại</span>
                                    <span class="content-user">{{ userFound.phoneNumber }}</span>
                                </div>
                                <div class="user-profile-item-user">
                                    <span class="title-user">Giới tính</span>
                                    <span class="content-user">{{ userFound.gender === 'Male' ? 'Nam' : 'Nữ' }}</span>
                                </div>
                                <div class="user-profile-item-user">
                                    <span class="title-user">Ngày sinh</span>
                                    <span class="content-user">{{ displayedDate }}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <hr v-if="user.phoneNumber !== userFound.phoneNumber"
                        style="border: none; border-bottom: 1px solid #ccc;">
                    <div class="mt-2"></div>
                    <div class="profile-action-user" v-if="user.phoneNumber !== userFound.phoneNumber">
                        <div class="block-button text-center cursor-pointer bg-gray-400 text-black rounded-lg h-10 mr-2 w-1/2 text-sm"
                            @click="blockUser(userFound.phoneNumber)">
                            Chặn
                        </div>
                        <div v-if="!sended && !isFriend"
                            class="add-friend-button text-center cursor-pointer bg-blue-500 text-white rounded-lg h-10 ml-2 w-1/2 text-sm"
                            @click="addFriend">
                            Kết bạn
                        </div>
                        <div v-else-if="sended"
                            class="add-friend-button text-center bg-blue-500 text-white rounded-lg h-10 ml-2 text-sm">
                            Chờ xác nhận...
                        </div>
                        <div v-else
                            class="add-friend-button text-center bg-blue-500 text-white rounded-lg h-10 ml-2 text-sm w-1/2">
                            Đã là bạn bè
                        </div>
                    </div>
                    <div class="mb-2"></div>
                </v-card-text>
            </v-card>
        </v-dialog>
    </div>
</template>
  
<script>
import axios from 'axios';
import FriendList from './FriendList.vue';
import GroupList from './GroupList.vue';
import InvitationFriendManage from './InvitationFriendManage.vue';
import { format, parseISO } from 'date-fns';
import viLocale from 'date-fns/locale/vi';
import { useToast } from "vue-toastification";
export default {
    components: {
        FriendList,
        GroupList,
        InvitationFriendManage
    },
    created() {
        const userString = localStorage.getItem('user');
        if (userString) {
            this.user = JSON.parse(userString);
        }
        this.getListOfFriends();
    },
    setup() {
        // Get toast interface
        const toast = useToast();
        return { toast }
    },
    data() {
        return {
            selectedMenuItem: 'friends',
            hoveredItem: '',
            showVisibleFindFriendDialog: false,
            showVisibleUserInfo: false,
            friends: [
                { phoneNumber: '0965556651', userName: "Võ Giang Nam", imageAvatar: 'https://i.imgur.com/z9fdzMv.jpg', imageCoverAvatar: 'https://i.imgur.com/gEKsypv.jpg', birthDay: '2002-03-27T00:00:00.000+00:00', gender: 'Male' },
                { phoneNumber: '0965556652', userName: "Từ Thanh Thoại", imageAvatar: 'https://i.imgur.com/z9fdzMv.jpg', imageCoverAvatar: 'https://i.imgur.com/gEKsypv.jpg', birthDay: '2002-03-27T00:00:00.000+00:00', gender: 'Male' },
                { phoneNumber: '0965556653', userName: "Kẻ Áo Đen", imageAvatar: 'https://i.imgur.com/z9fdzMv.jpg', imageCoverAvatar: 'https://i.imgur.com/gEKsypv.jpg', birthDay: '2002-03-27T00:00:00.000+00:00', gender: 'Male' },
                { phoneNumber: '0965556654', userName: "Kẻ Áo Vàng", imageAvatar: 'https://i.imgur.com/z9fdzMv.jpg', imageCoverAvatar: 'https://i.imgur.com/gEKsypv.jpg', birthDay: '2002-03-27T00:00:00.000+00:00', gender: 'Male' },
                { phoneNumber: '0965556655', userName: "Kẻ Áo Xanh", imageAvatar: 'https://i.imgur.com/z9fdzMv.jpg', imageCoverAvatar: 'https://i.imgur.com/gEKsypv.jpg', birthDay: '2002-03-27T00:00:00.000+00:00', gender: 'Male' },
                { phoneNumber: '0965556656', userName: "Kẻ Áo Đỏ", imageAvatar: 'https://i.imgur.com/z9fdzMv.jpg', imageCoverAvatar: 'https://i.imgur.com/gEKsypv.jpg', birthDay: '2002-03-27T00:00:00.000+00:00', gender: 'Male' },
                { phoneNumber: '0965556657', userName: "Kẻ Áo Tím", imageAvatar: 'https://i.imgur.com/z9fdzMv.jpg', imageCoverAvatar: 'https://i.imgur.com/gEKsypv.jpg', birthDay: '2002-03-27T00:00:00.000+00:00', gender: 'Male' },
                { phoneNumber: '0965556658', userName: "Kẻ Áo Cam", imageAvatar: 'https://i.imgur.com/z9fdzMv.jpg', imageCoverAvatar: 'https://i.imgur.com/gEKsypv.jpg', birthDay: '2002-03-27T00:00:00.000+00:00', gender: 'Male' }
            ],
            listOfFriends: [],
            searchPhoneNumber: '',
            userFound: null,
            user: null,
            displayedDate: '',
            sended: false,
            isFriend: false,
            inviteList: null,
        };
    },
    methods: {
        selectMenuItem(item) {
            this.selectedMenuItem = item;
        },
        isSelected(item) {
            return this.selectedMenuItem === item;
        },
        formattedBirthday() {
            if (this.userFound && this.userFound.birthDay) {
                const parsedDate = parseISO(this.userFound.birthDay);
                this.displayedDate = format(parsedDate, "dd 'tháng' MM, yyyy", { locale: viLocale });
            }
        },
        showFindFriendDialog() {
            this.searchPhoneNumber = '';
            this.showVisibleFindFriendDialog = true;
        },
        closeFindFriendDialog() {
            this.showVisibleFindFriendDialog = false;
        },
        showUserInfoDialog(friend) {
            this.userFound = friend;

            this.formattedBirthday();

            this.showVisibleUserInfo = true;
            this.showVisibleFindFriendDialog = false;
        },
        closeUserInfoDialog() {
            this.showVisibleUserInfo = false;
            this.sended = false;
            this.showVisibleFindFriendDialog = true;
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
                        this.showVisibleFindFriendDialog = true;
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
            await this.getInviteFriend();
            await this.getListOfFriends();
            const isFriend = this.listOfFriends.some(friend => friend.phoneNumber === this.searchPhoneNumber);
            const isInvited = this.inviteList.some(friend => friend.phoneNumber === this.searchPhoneNumber);

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

            // Additional checks
            if (this.listOfFriends.length === 0 && this.inviteList.length === 0) {
                this.isFriend = false;
                this.sended = false;
            }
        },
        async getListOfFriends() {
            try {
                const response = await axios.get(`users/getAllFriendUser`, {
                    headers: {
                        'Authorization': localStorage.getItem("token")
                    }
                });

                if (response.status === 200) {

                    this.listOfFriends = response.data;
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
    },
    name: 'ContactNav'
}
</script>
  
  <!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang = "scss">
::placeholder {
    //padding-left: 18px;
    font-size: 14px;
}

#contact-nav {
    height: 100vh;
    width: 23%;
    border-right: 1px solid rgb(160, 160, 160);
    display: flex;

    #chat-message {
        display: inline-block;
        height: 100%;
        width: 100%;
        vertical-align: top;

        //background-color: black;

        #search-content {
            height: 60px;
            width: 100%;
            padding: 0 16px;
            display: flex;
            //background-color: black;

            #contact-input-search {
                height: 45%;
                width: 100%;
                margin-top: 18px;
                border-radius: 6px;
                background-color: #EAEDF0;
                padding-left: 18px;
            }

            #contact-input-search:focus {
                outline-color: #80b3ff;
                outline-width: 0.05px;
            }

            .fa-magnifying-glass {
                position: absolute;
                margin-left: 2px;
                margin-top: 26px;
                font-size: 12px;
            }

            .fa-user-plus,
            .fa-users {
                font-size: 14px;
                padding-left: 10px;
                margin-top: 26px;
            }
        }

        #menu-friend {
            height: 91.55%;
            width: 100%;
            max-width: 100%;
            min-width: 300px;
            transition: visibility 0s, opacity .5s linear;

            .menu-contact {
                background-color: #ffffff;
                height: 100%;
                box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
            }

            .menu-contact .menu-item {
                display: flex;
                align-items: center;
                padding: 10px;
            }

            .menu-contact .menu-item:last-child {
                border-bottom: none;
            }

            .menu-contact .menu-item a {
                color: #333333;
                margin-right: 30px;
            }

            .menu-contact .menu-item .menu-name {
                font-weight: bold;
            }

            .menu-item {
                cursor: pointer;
            }

            .selected {
                background-color: rgb(197, 222, 241);
            }

            .hovered {
                background-color: #f3f5f6;
            }

            .hovering {
                background-color: rgb(197, 222, 241);
            }
        }
    }
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
                margin-right: 4px;
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
                flex: 1;

                .form-control {
                    background-color: #cccc;
                    border-radius: 5%;
                    text-align: center;
                }
            }

            .friend-info {
                display: flex;
                align-items: center;
                justify-content: space-between;
            }

            .profile-action {
                height: 40px;
                display: flex;
            }
        }
    }

    .position-relative {
        position: relative;
    }

    .friend-container {
        overflow-x: hidden;
        display: flex;
        justify-content: space-between;
        margin-right: 10px;
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
        border: 1px solid #ccc;
        border-radius: 8px;
    }

    .friend-list-container {
        height: 250px;
        overflow-y: auto;
        overflow-x: hidden;
        border: 1px solid #ccc;
        border-radius: 8px;
    }

    .action {
        margin-left: 8px;
        cursor: pointer;
    }

    .add-friend-button {
        background-color: #007BFF;
        color: #ffffff;
        border-radius: 5px;
        padding: 8px 16px;
        border: none;
    }

    .add-friend-button:hover {
        transform: scale(1.05);
    }

    .cancel-button,
    .search-button {
        padding: 8px 16px;
    }
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
</style>