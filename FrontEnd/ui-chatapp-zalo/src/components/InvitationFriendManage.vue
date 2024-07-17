<template>
    <div class="contact">
        <div class="header">
            <a id="user-group-icon">
                <font-awesome-icon icon="fa-solid fa-envelope-open" />
            </a>
            <p class="menu-name">
                <span>Invite request</span>
            </p>
        </div>
        <div class="wrapper">
            <div class="cart-list">
                <div class="title" style="height: 64px; width: 100%;">Invite request {{ receivedFriendList ?
                    `(${receivedFriendList.length})` : '0' }}</div>
                <div class="list">
                    <div v-for="friend in receivedFriendList" :key="friend.phoneNumber" class="item-list"
                        :class="{ 'hovered': hoveredItem === friend.phoneNumber }" @mouseenter="(hoveredItem = friend.phoneNumber)"
                        @mouseleave="hoveredItem = ''">
                        <div class="friend-info">
                            <div class="avatar-container">
                                <div class="avatar-wrapper">
                                    <img :src="friend.imageAvatar" class="avatar">
                                </div>
                            </div>
                            <div class="detail">
                                <div class="name-wrapper">
                                    <span>{{ friend.userName }}</span>
                                </div>
                            </div>
                        </div>
                        <div class="action">
                            <button class="refuse" @click="acceptInvitation(friend, false)">
                                <i class="fas fa-times"></i>
                                <span>Decline</span>
                            </button>
                            <button class="accept" @click="acceptInvitation(friend, true)">
                                <i class="fas fa-check"></i>
                                <span>Accept</span>
                            </button>
                        </div>
                    </div>
                    <hr style="border: none; border-bottom: 1px solid #ccc; margin-left: 78px;">
                </div>
            </div>
            <div class="cart-list">
                <div class="title" style="height: 64px; width: 100%;">Invite sent request {{ sendedFriendList ?
                    `(${sendedFriendList.length})` : '0' }}</div>
                <div class="list">
                    <div class="item-list" v-for="friend in sendedFriendList" :key="friend.phoneNumber"
                        :class="{ 'hovered': hoveredItem === friend.phoneNumber }" @mouseenter="(hoveredItem = friend.phoneNumber)"
                        @mouseleave="hoveredItem = ''">
                        <div class="friend-info">
                            <div class="avatar-container">
                                <div class="avatar-wrapper">
                                    <img :src="friend.imageAvatar" class="avatar">
                                </div>
                            </div>
                            <div class="detail">
                                <div class="name-wrapper">
                                    <span>{{ friend.userName }}</span>
                                </div>
                            </div>
                        </div>
                        <div class="action">
                            <button class="accept" @click="cancelInvitation(friend)">
                                <i class="fas fa-check"></i>
                                <span>Cancel invite sent request</span>
                            </button>
                        </div>
                    </div>
                    <hr style="border: none; border-bottom: 1px solid #ccc; margin-left: 78px;">
                </div>
            </div>
            <!-- <div class="cart-list">
                <div class="title-recommend" style="height: 64px; width: 100%;">
                    <span>Gợi ý kết bạn (2)</span>
                    <a class="down-icon">
                        <font-awesome-icon icon="fa-solid fa-chevron-down" />
                    </a>
                </div>
                <div class="list">
                    <div class="item-list" :class="{ 'hovered': hoveredItem === '5' }" @mouseenter="(hoveredItem = '5')"
                        @mouseleave="hoveredItem = ''">
                        <div class="friend-info">
                            <div class="avatar-container">
                                <div class="avatar-wrapper">
                                    <img src="https://i.imgur.com/gEKsypv.jpg" class="avatar">
                                </div>
                            </div>
                            <div class="detail">
                                <div class="name-wrapper">
                                    <span>Thanh Thoại</span>
                                </div>
                                <div class="label">
                                    <span>Lời nhắn</span>
                                </div>
                            </div>
                        </div>
                        <div class="action">
                            <button class="defuse">
                                <i class="fas fa-times"></i>
                                <span>Bỏ qua</span>
                            </button>
                            <button class="accept">
                                <i class="fas fa-check"></i>
                                <span>Kết bạn</span>
                            </button>
                        </div>
                    </div>
                    <hr style="border: none; border-bottom: 1px solid #ccc; margin-left: 78px;">
                    <div class="item-list" :class="{ 'hovered': hoveredItem === '6' }" @mouseenter="(hoveredItem = '6')"
                        @mouseleave="hoveredItem = ''">
                        <div class="friend-info">
                            <div class="avatar-container">
                                <div class="avatar-wrapper">
                                    <img src="https://i.imgur.com/gEKsypv.jpg" class="avatar">
                                </div>
                            </div>
                            <div class="detail">
                                <div class="name-wrapper">
                                    <span>Thanh Thoại</span>
                                </div>
                                <div class="label">
                                    <span>Lời nhắn</span>
                                </div>
                            </div>
                        </div>
                        <div class="action">
                            <button class="defuse">
                                <i class="fas fa-times"></i>
                                <span>Bỏ qua</span>
                            </button>
                            <button class="accept">
                                <i class="fas fa-check"></i>
                                <span>Kết bạn</span>
                            </button>
                        </div>
                    </div>
                </div>
            </div> -->
        </div>
    </div>
</template>
  
<script>
import axios from 'axios';
import { useToast } from "vue-toastification";
export default {
    setup() {
        // Get toast interface
        const toast = useToast();
        return { toast }
    },
    mounted() {
        this.getInviteFriend();
    },
    created() {
        const userString = localStorage.getItem('user');
        if (userString) {
            this.user = JSON.parse(userString);
        }
    },
    data() {
        return {
            hoveredItem: '',
            sendedFriendList: null,
            receivedFriendList: null,
            user: null,
        };
    },
    methods: {
        async getInviteFriend() {
            try {

                const response = await axios.get(`users/getAllInviteFriend`, {
                    headers: {
                        'Authorization': localStorage.getItem("token")
                    }
                });

                if (response.status === 200) {
                    const inviteFriendList = response.data
                    this.sendedFriendList = inviteFriendList.filter(user => user.isInviteFriendFromUser === true);

                    this.receivedFriendList = inviteFriendList.filter(user => user.isInviteFriendFromUser === false);
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
        async cancelInvitation(friend) {
            try {

                const cancelFriendRequest = {
                    fromPhoneNumberUser: this.user.phoneNumber,
                    toPhoneNumberUser: friend.phoneNumber,
                    isAcceptingInvite: false
                }

                const response = await axios.post(`users/cancelInviteFriend`, cancelFriendRequest, {
                    headers: {
                        'Authorization': localStorage.getItem("token")
                    }
                });

                if (response.status === 200) {
                    this.sendedFriendList = this.sendedFriendList.filter(user => user.phoneNumber !== friend.phoneNumber);
                    this.toast.success(response.data, {timeout: 1500})
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
        async acceptInvitation(friend, isAccepting) {
            try {

                const acceptFriendRequest = {
                    fromPhoneNumberUser: this.user.phoneNumber,
                    toPhoneNumberUser: friend.phoneNumber,
                    isAcceptingInvite: isAccepting
                }

                const response = await axios.post(`users/acceptingInviteFriend`, acceptFriendRequest, {
                    headers: {
                        'Authorization': localStorage.getItem("token")
                    }
                });

                if (response.status === 200) {
                    this.receivedFriendList = this.receivedFriendList.filter(user => user.phoneNumber !== friend.phoneNumber);
                    this.toast.success(response.data, {timeout: 1500})
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
    },
    name: 'InvitationFriendManage'
}
</script>
  
<style scoped lang = "scss">
.contact {
    display: flex;
    flex-direction: column;
    height: 100vh;
    width: 100%;

    .header {
        height: 8%;
        width: 100%;
        border-top: 1px solid rgb(160, 160, 160);
        border-left: 1px solid rgb(160, 160, 160);
        border-bottom: 1px solid rgb(160, 160, 160);
        display: flex;
        align-items: center;

        .menu-name {
            font-weight: bold;
        }
    }

    .header:last-child {
        border-bottom: none;
    }

    .header a {
        color: #333333;
        margin-right: 20px;
        margin-left: 30px;
    }

    .wrapper {
        background-color: #f4f4f4;
        height: 92%;
    }

    .cart-list {
        flex: 1;
        display: flex;
        flex-direction: column;
        margin: 0 16px;
    }

    .title {
        height: 64px;
        width: 100%;
        align-items: center;
        padding-top: 20px;
        padding-bottom: 20px;
        position: relative;
        font-weight: bold;
    }

    .filter-wrapper {
        position: relative;
        display: flex;
        background-color: #ffffff;
    }

    .search-container {
        border: 1px solid #ccc;
        border-radius: 4px;
        display: flex;
        align-items: center;
        padding: 4px;
        height: 30px;
        width: 260px;
        margin-top: 16px;
        margin-left: 16px;

        input {
            margin-left: 16px;
        }
    }

    .filter-contact {
        display: flex;
        justify-content: space-between;
        margin-top: 16px;
        margin-left: 16px;
        flex-wrap: wrap;
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
        margin-bottom: 8px;
        background-color: #f4f4f4;
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

    .list {
        background-color: #ffffff;

    }

    .contact-item {
        margin-bottom: 8px;
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
    }

    .friend-info {
        display: flex;
        align-items: center;
    }

    .action {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-top: 16px;
    }

    .name-wrapper,
    .label {
        display: flex;
    }

    .action button {
        display: flex;
        align-items: center;
        padding: 8px 16px;
        border-radius: 4px;
        cursor: pointer;
    }

    .label span {
        margin-top: 5px;
        margin-bottom: 2px;
        font-size: 13px;
    }

    .title-recommend {
        height: 64px;
        width: 100%;
        align-items: center;
        padding-top: 20px;
        padding-bottom: 20px;
        position: relative;
        font-weight: bold;
        display: flex;
    }

    .title-recommend span {
        margin-right: 10px;
    }

    .action .accept {
        background-color: #42a9d5;
        color: #ffff;
        margin-right: 80px;
        margin-left: 10px;
    }

    .action .refuse {
        background-color: #ffff;
        color: #42a9d5;
    }

    .action button span {
        align-items: center;
    }

    .item-list {
        display: flex;
        align-items: center;
        justify-content: space-between;
        border-radius: 10px;
        overflow: hidden;
        /* Ensures border radius is applied */
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
}
</style>