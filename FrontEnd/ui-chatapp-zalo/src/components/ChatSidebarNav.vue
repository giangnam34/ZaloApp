<template>
    <div id="chat-message">
        <div class="relative">
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
            <div id="category-message">
                <div id="all-message">Tất cả</div>
                <div id="unread-message">Chưa đọc</div>
                <div id="category">
                    Phân loại
                    <font-awesome-icon icon="fa-solid fa-caret-down" style="color: #445574;" />
                </div>
                <div id="more">
                    <font-awesome-icon icon="fa-solid fa-ellipsis" />
                </div>
            </div>
        </div>
        <div id="list-message">
            <div class="message-item">
                <div class="nav__tabs__zalo web" title="Giang Nam">
                    <img src="https://s120-ava-talk.zadn.vn/3/e/6/b/1/120/8b72a04be66a9ef7009df15d86b69810.jpg"
                        class="a-child">
                </div>
                <div id="content">
                    <div id="chat-name">
                        Name
                    </div>
                    <div id="chat-info">
                        Info
                    </div>
                    <div id="chat-content">
                        Content
                    </div>
                </div>
            </div>
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
                                        <img :src="friend.avatar" class="avatar">
                                    </div>
                                </div>
                                <div class="detail" @click="showUserInfoDialog(friend)">
                                    <span>{{ friend.name }}</span>
                                </div>
                                <div class="action" @click="test2">
                                    <button class="add-friend-button">Thêm bạn</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="profile-action">
                    <div
                        class="cancel-button text-center cursor-pointer bg-gray-400 text-black rounded-lg h-10 mx-4 w-1/2">
                        Hủy
                    </div>
                    <div
                        class="search-button text-center cursor-pointer bg-blue-500 text-white rounded-lg h-10 mx-4 w-1/2">
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
                        <img class="cover-image-user" :src="user.avatar" alt="None" crossorigin="anonymous">
                    </div>
                    <div class="ava-name-container-user">
                        <div class="avatar-profile-user">
                            <div class="avatar-user">
                                <img class="avatar-image-user" :src="user.avatar">
                            </div>
                        </div>
                        <div class="fullname-profile-user">
                            <div class="fullname-user">{{ user.name }}</div>
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
                                <span class="content-user">{{ user.phoneNumber }}</span>
                            </div>
                            <div class="user-profile-item-user">
                                <span class="title-user">Giới tính</span>
                                <span class="content-user">{{ user.sex === 'Male' ? 'Nam' : 'Nữ' }}</span>
                            </div>
                            <div class="user-profile-item-user">
                                <span class="title-user">Ngày sinh</span>
                                <span class="content-user">{{ displayedDate }}</span>
                            </div>
                        </div>
                    </div>
                </div>
                <hr style="border: none; border-bottom: 1px solid #ccc;">
                <div class="mt-2"></div>
                <div class="profile-action-user">
                    <div
                        class="block-button text-center cursor-pointer bg-gray-400 text-black rounded-lg h-10 mr-2 w-1/2">
                        Chặn
                    </div>
                    <div
                        class="add-friend-button text-center cursor-pointer bg-blue-500 text-white rounded-lg h-10 ml-2 w-1/2">
                        Kết bạn
                    </div>
                </div>
                <div class="mb-2"></div>
            </v-card-text>
        </v-card>
    </v-dialog>
</template>
<script>
// import axios from 'axios';
import { format, parseISO } from 'date-fns';
import viLocale from 'date-fns/locale/vi';
import { useToast } from "vue-toastification";
export default {
    setup() {
        // Get toast interface
        const toast = useToast();
        return { toast }
    },
    data() {
        return {
            showVisibleFindFriendDialog: false,
            showVisibleUserInfo: false,
            friends: [
                { phoneNumber: '0965556651', name: "Võ Giang Nam", avatar: 'https://i.imgur.com/gEKsypv.jpg', birthDay: '2002-03-27T00:00:00.000+00:00' },
                { phoneNumber: '0965556652', name: "Từ Thanh Thoại", avatar: 'https://i.imgur.com/gEKsypv.jpg', birthDay: '2002-03-27T00:00:00.000+00:00' },
                { phoneNumber: '0965556653', name: "Kẻ Áo Đen", avatar: 'https://i.imgur.com/gEKsypv.jpg', birthDay: '2002-03-27T00:00:00.000+00:00' },
                { phoneNumber: '0965556654', name: "Kẻ Áo Vàng", avatar: 'https://i.imgur.com/gEKsypv.jpg', birthDay: '2002-03-27T00:00:00.000+00:00' },
                { phoneNumber: '0965556655', name: "Kẻ Áo Xanh", avatar: 'https://i.imgur.com/gEKsypv.jpg', birthDay: '2002-03-27T00:00:00.000+00:00' },
                { phoneNumber: '0965556656', name: "Kẻ Áo Đỏ", avatar: 'https://i.imgur.com/gEKsypv.jpg', birthDay: '2002-03-27T00:00:00.000+00:00' },
                { phoneNumber: '0965556657', name: "Kẻ Áo Tím", avatar: 'https://i.imgur.com/gEKsypv.jpg', birthDay: '2002-03-27T00:00:00.000+00:00' },
                { phoneNumber: '0965556658', name: "Kẻ Áo Cam", avatar: 'https://i.imgur.com/gEKsypv.jpg', birthDay: '2002-03-27T00:00:00.000+00:00' }
            ],
            searchPhoneNumber: '',
            user: null,
            displayedDate: '',
        };
    },
    methods: {
        formattedBirthday() {
            try {
                if (this.user && this.user.birthDay) {
                    const parsedDate = parseISO(this.user.birthDay);
                    this.displayedDate = format(parsedDate, "dd 'tháng' MM, yyyy", { locale: viLocale });
                }
            } catch (exception) {
                console.log("Error in formattedBirthday", exception);
            }
        },
        showFindFriendDialog() {
            try {
                this.showVisibleFindFriendDialog = true;
            } catch (exception) {
                console.log("Error in showFindFriendDialog", exception);
            }
        },
        closeFindFriendDialog() {
            try {
                this.showVisibleFindFriendDialog = false;
            } catch (exception) {
                console.log("Error in closeFindFriendDialog", exception);
            }
        },
        showUserInfoDialog(friend) {
            try {
                // const response = await axios.get(`users/findUserByPhoneNumber/${friend.phoneNumber}`, {
                //     headers: {
                //         'Authorization': localStorage.getItem("token")
                //     }
                // });

                // if (response.status === 200) {

                //     const userInfo = response.data;

                //     this.user = userInfo;

                console.log(friend);

                this.user = friend;
                this.formattedBirthday();
                this.showVisibleUserInfo = true;
                this.showVisibleFindFriendDialog = false;

                // } else {
                //     console.error(response.body);
                //     this.toast.error(response.body, { timeout: 3000 });
                // }
            } catch (exception) {
                console.log("Error in showUserInfoDialog", exception);

                // if (error.response) {
                //     if (error.response.status === 400) {
                //         this.toast.error(error.response.data, { timeout: 3000 });
                //     } else {
                //         this.toast.error(error.response.data, { timeout: 3000 });
                //     }
                // } else if (error.request) {
                //     this.toast.error('Không nhận được phản hồi từ máy chủ. Vui lòng thử lại!', { timeout: 3000 });
                // } else {
                //     this.toast.error('Error setting up the request:' + error.message, { timeout: 3000 });
                // }
            }
        },
        closeUserInfoDialog() {
            try {
                this.showVisibleUserInfo = false;
                this.showVisibleFindFriendDialog = true;
            } catch (exception) {
                console.log("Error in closeUserInfoDialog", exception);
            }
        },
        test2() {
            console.log(2)
        }
    },
    name: 'ChatSidebarNav',
}
</script>
<style scoped lang="scss">
div {

    #chat-message {
        display: inline-block;
        height: 100vh;
        width: 23%;
        vertical-align: top;
        border-right: 1px solid rgb(160, 160, 160);

        //background-color: black;

        #search-content {
            height: 64px;
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

        #category-message {
            padding-top: 5px;
            border-bottom: 1px solid #d6dbe2;

            #all-message,
            #unread-message,
            #category,
            #more {
                display: inline-block;
                color: #7589A3;
                padding-left: 16px;
                font-family: SegoeuiPc, Segoe UI, San Francisco, Helvetica Neue, Helvetica, Lucida Grande, Roboto, Ubuntu, Tahoma, Microsoft Sans Serif, Arial, sans-serif;
                font-size: .875rem;
                font-weight: 500;
                line-height: 1.5;
            }

            #category {
                margin-left: 80px;
            }

            #more {
                display: inline-block;
            }

            .fa-ellipsis {
                font-size: 18px;
                padding-top: 2px;
            }
        }

        #list-message {
            overflow: auto;
            max-height: 87.5%;

            .message-item {
                height: 74px;
                padding: 0 16px;

                .nav__tabs__zalo {
                    display: inline-block;
                    height: 100%;
                    width: 50px;
                    padding-top: 12px;

                    .a-child {
                        width: 48px;
                        height: 48px;
                        display: flex;
                        justify-content: center;
                        align-items: center;
                        background: var(--surface-background);
                        color: var(--text-secondary);
                        overflow: hidden;
                        object-fit: contain;
                        font-size: .75rem;
                        font-weight: 500;
                        line-height: 1.5;
                        border: 1px solid var(--WA100);
                        //box-sizing: border-box;
                        border-radius: 50%;
                        margin-left: 7px;
                    }
                }

                #content {
                    vertical-align: top;
                    display: inline-block;
                    padding-top: 14px;
                    padding-left: 12px;
                    height: 100%;
                    width: 78%;

                    #chat-name {
                        display: inline-block;
                        color: #081C36;
                        font-size: 16px;

                    }

                    #chat-content {
                        display: block;
                        color: #7589A3;
                        font-size: 14px;
                    }

                    #chat-info {
                        display: inline-block;
                        color: #7589A3;
                        font-size: 12px;
                        float: right;
                    }
                }
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