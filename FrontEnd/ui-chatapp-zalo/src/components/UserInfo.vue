<template>
    <v-dialog class="dialog-container" v-model="dialogVisible" max-width="352px" @click:outside="closeDialog">
        <v-card class="dialog-component">
            <v-card-title class="dialog-title">
                <h2 class="title">Thông tin tài khoản
                    <div class="icon-close" @click="closeDialog"><font-awesome-icon icon="fa-solid fa-x" /></div>
                </h2>
            </v-card-title>
            <v-card-text class="dialog-content">
                <div class="profile-photo">
                    <div class="cover-avatar">
                        <img class="cover-image" src="https://i.imgur.com/gEKsypv.jpg" alt="None" crossorigin="anonymous">
                    </div>
                    <div class="ava-name-container">
                        <div class="avatar-profile">
                            <div class="avatar">
                                <img class="avatar-image" src="https://i.imgur.com/gEKsypv.jpg">
                            </div>
                        </div>
                        <div class="fullname-profile">
                            <div class="fullname">{{ user.fullName }}</div>
                        </div>
                    </div>
                </div>
                <div class="profile-information">
                    <div class="profile-header">
                        Thông tin cá nhân
                    </div>
                    <div>
                        <div class="user-profile-details">
                            <div class="user-profile-item">
                                <span>Điện thoại</span>
                                <span>{{ user.phoneNumber }}</span>
                            </div>
                            <div class="user-profile-item">
                                <span>Giới tính</span>
                                <span>{{ user.sex === 'Male' ? 'Nam' : 'Nữ' }}</span>
                            </div>
                            <div class="user-profile-item">
                                <span>Ngày sinh</span>
                                <span>{{ formattedBirthday }}</span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="profile-action">

                </div>
            </v-card-text>
        </v-card>
    </v-dialog>
</template>
  
<script>
import { format, parseISO } from 'date-fns';
import viLocale from 'date-fns/locale/vi';
export default {
    name: "UserInfo",
    props: {
        showPopup: Boolean
    },
    created() {
        // Lấy user từ localStorage
        const userString = localStorage.getItem('user');

        // Chuyển đổi chuỗi JSON thành đối tượng JavaScript
        if (userString) {
            this.user = JSON.parse(userString);
        }
    },
    data() {
        return {
            dialogVisible: false,
            user: null,
        };
    },
    computed: {
        formattedBirthday() {
            if (this.user && this.user.birthDay) {
                const parsedDate = parseISO(this.user.birthDay);
                return format(parsedDate, "dd 'tháng' MM, yyyy", { locale: viLocale });
            }
            return '';
        },
    },
    watch: {
        // Sử dụng watch để theo dõi sự thay đổi của prop từ component cha
        showPopup(newVal) {
            if (newVal) {
                this.openDialog();
            } else {
                this.closeDialog();
            }
        }
    },
    methods: {
        openDialog() {
            this.dialogVisible = true;
            this.$emit('update:showPopup', true); // emit sự kiện để cập nhật showPopup trong component cha
        },
        closeDialog() {
            this.dialogVisible = false;
            this.$emit('update:showPopup', false); // emit sự kiện để cập nhật showPopup trong component cha
        },
    },
};
</script>
  
<style scoped lang="scss">
.dialog-container {
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

            .profile-photo {
                position: relative;
                display: block;

                .cover-avatar {
                    position: relative;
                    display: table;
                    margin: auto;

                    .cover-image {
                        cursor: pointer;
                        width: 352px;
                        height: 152px;
                        object-fit: cover;
                    }
                }

                .ava-name-container {
                    text-align: center;
                    left: 0;
                    width: 100%;
                    color: #fff;
                    position: relative;
                    display: flex;
                    background: transparent;
                    margin-top: -48px;
                    flex-direction: column;

                    .avatar-profile {
                        cursor: pointer;
                        align-self: center;
                        display: block;

                        .avatar {
                            width: 80px;
                            height: 80px;
                            min-width: 80px;
                            min-height: 80px;
                            position: relative;
                            color: #fff;
                            overflow: hidden;
                            box-sizing: border-box;

                            .avatar-image {
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

                    .fullname-profile {
                        width: 100%;
                        color: #081c36;
                        font-size: 1.125rem; // Thay đổi font-size thành 1.125rem
                        font-weight: 500; // Thay đổi font-weight thành 500
                        line-height: 1.5;
                        display: flex;
                        flex-direction: column;
                        z-index: 1;
                        margin-top: 10px;

                        .fullname {
                            position: relative;
                            width: fit-content;
                            margin: auto;
                            max-width: 271px;
                            text-overflow: ellipsis;
                        }
                    }
                }
            }

            .profile-information {

                .profile-header {}

                div {
                    .user-profile-details {

                        .user-profile-item {}
                    }
                }
            }

            .profile-action {}
        }
    }
}
</style>
  