<template>
    <v-dialog class="dialog-container" v-model="dialogVisible" max-width="352px" @click:outside="closeDialog">
        <v-card class="dialog-component">
            <v-card-title class="dialog-title">
                <h2 class="title">Info account
                    <div class="icon-close" @click="closeDialog"><font-awesome-icon icon="fa-solid fa-x" /></div>
                </h2>
            </v-card-title>
            <hr style="border: none; border-bottom: 1px solid #ccc;">
            <v-card-text class="dialog-content">
                <div class="profile-photo">
                    <div class="cover-avatar">
                        <label for="addFileInput1" class="cursor-pointer">
                            <img class="cover-image" :src="user.imageCoverPhotoUrl" alt="None" crossorigin="anonymous">
                        </label>
                        <input type="file" id="addFileInput1" ref="fileInput" @change="addFileCoverAvatar"
                            style="display: none;" accept="image/*">
                    </div>
                    <div class="ava-name-container">
                        <div class="avatar-profile">
                            <div class="avatar">
                                <label for="addFileInput2" class="cursor-pointer">
                                    <img class="avatar-image" :src="user.imageAvatarUrl">
                                </label>
                                <input type="file" id="addFileInput2" ref="fileInput" @change="addFileAvatar"
                                    style="display: none;" accept="image/*">

                            </div>
                        </div>
                        <div class="fullname-profile">
                            <div class="fullname">{{ user.fullName }}</div>
                        </div>
                    </div>
                </div>
                <hr style="border: none; border-bottom: 1px solid #ccc;">
                <div class="profile-information">
                    <div class="profile-header">
                        <strong>Info account</strong>
                    </div>
                    <div>
                        <div class="user-profile-details">
                            <div class="user-profile-item">
                                <span class="title">Phone number</span>
                                <span class="content">{{ user.phoneNumber }}</span>
                            </div>
                            <div class="user-profile-item">
                                <span class="title">Gender</span>
                                <span class="content">{{ user.sex === 'Male' ? 'Nam' : 'Nữ' }}</span>
                            </div>
                            <div class="user-profile-item">
                                <span class="title">Birthday</span>
                                <span class="content">{{ displayedDate }}</span>
                            </div>
                        </div>
                    </div>
                </div>
                <hr style="border: none; border-bottom: 1px solid #ccc;">
                <div class="profile-action">
                    <div class="update-button" @click="openUpdateDialog">
                        <font-awesome-icon icon="fa-solid fa-pen-to-square" />
                        <span>Update</span>
                    </div>
                </div>
            </v-card-text>
        </v-card>
    </v-dialog>
    <v-dialog class="dialog-container" v-model="updateDialogVisible" max-width="352px">
        <v-card class="dialog-component">
            <v-card-title class="dialog-title">
                <div style="display: flex;">
                    <div @click="closeUpdateDialog" style="cursor: pointer; margin-right: 10px"><font-awesome-icon
                            icon="fa-solid fa-x" />
                    </div>
                    <h2>Update info account</h2>
                </div>
            </v-card-title>
            <v-card-text>
                <v-form @submit.prevent="submitUpdateForm">
                    <v-text-field label="Full name" v-model="updatedFullName"></v-text-field>
                    <v-row>
                        <v-col>
                            <v-text-field v-model="updatedGender" label="Gender" readonly
                                @click="toggleGenderMenu"></v-text-field>
                            <v-menu v-model="genderMenu" :close-on-content-click="false" transition="scale-transition"
                                :style="{ left: popoverLeft, top: popoverTop }">
                                <v-list>
                                    <v-list-item v-for="genderOption in genderOptions" :key="genderOption"
                                        @click="selectGender(genderOption)">
                                        <v-list-item-title>{{ genderOption }}</v-list-item-title>
                                    </v-list-item>
                                </v-list>
                            </v-menu>
                        </v-col>
                    </v-row>
                    <v-text-field label="Birthday" v-model="updatedBirthday" type="date"
                        format="yyyy-MM-dd"></v-text-field>

                    <v-btn type="submit" @click="updateUserInfo">Save</v-btn>
                </v-form>
            </v-card-text>
        </v-card>
    </v-dialog>
</template>

<script>
import { format, parseISO } from 'date-fns';
import viLocale from 'date-fns/locale/vi';
import axios from 'axios';
import { useToast } from "vue-toastification";
export default {
    name: "UserInfo",
    props: {
        showPopup: Boolean
    },
    setup() {
        // Get toast interface
        const toast = useToast();
        return { toast }
    },
    mounted() {
        this.fetchAvatar();
        this.fetchCoverAvatar();
    },
    created() {
        const userString = localStorage.getItem('user');
        if (userString) {
            this.user = JSON.parse(userString);
        }
        this.formattedBirthday();
    },
    data() {
        return {
            dialogVisible: false,
            user: null,
            updateDialogVisible: false,
            updatedFullName: '',
            updatedBirthday: null,
            updatedGender: '',
            genderOptions: ['Nam', 'Nữ'],
            genderMenu: false,
            displayedDate: '',
            popoverLeft: "608px",
            popoverTop: "396px",
        };
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
        formattedBirthday() {
            try {
                if (this.user && this.user.birthDay) {
                    const parsedDate = parseISO(this.user.birthDay);
                    this.displayedDate = format(parsedDate, "dd 'tháng' MM, yyyy", { locale: viLocale });
                }
            } catch (error) {
                console.error("Error in formattedBirthday:", error);
                // Optionally handle or log the error further if needed
            }
        },
        openDialog() {
            try {
                this.dialogVisible = true;
                this.$emit('update:showPopup', true);
            } catch (error) {
                console.error("Error in openDialog:", error);
                // Optionally handle or log the error further if needed
            }
        },

        closeDialog() {
            try {
                this.dialogVisible = false;
                this.$emit('update:showPopup', false);
            } catch (error) {
                console.error("Error in closeDialog:", error);
                // Optionally handle or log the error further if needed
            }
        },
        formatDate(date) {
            try {
                const year = date.getFullYear();
                const month = (date.getMonth() + 1).toString().padStart(2, '0');
                const day = date.getDate().toString().padStart(2, '0');
                return `${year}-${month}-${day}`;
            } catch (error) {
                console.error("Error in formatDate:", error);
                // Optionally handle or log the error further if needed
            }
        },
        openUpdateDialog() {
            try {
                this.updateDialogVisible = true;
                this.dialogVisible = false;
                this.updatedFullName = this.user.fullName;
                this.updatedGender = this.user.sex === 'Male' ? 'Nam' : 'Nữ';

                const parts = this.displayedDate.split(" ");
                const day = parseInt(parts[0], 10);
                const month = parseInt(parts[2].replace("tháng", ""), 10);
                const year = parseInt(parts[3], 10);
                const date = new Date(year, month - 1, day);
                this.updatedBirthday = this.formatDate(date);
            } catch (error) {
                console.error("Error in openUpdateDialog:", error);
                // Optionally handle or log the error further if needed
            }
        },

        closeUpdateDialog() {
            try {
                this.updateDialogVisible = false;
                this.dialogVisible = true;
            } catch (error) {
                console.error("Error in closeUpdateDialog:", error);
                // Optionally handle or log the error further if needed
            }
        },
        submitUpdateForm() {
            try {
                this.closeUpdateDialog();
            } catch (error) {
                console.error("Error in submitUpdateForm:", error);
                // Optionally handle or log the error further if needed
            }
        },
        toggleGenderMenu() {
            try {
                this.genderMenu = !this.genderMenu;
            } catch (error) {
                console.error("Error in toggleGenderMenu:", error);
                // Optionally handle or log the error further if needed
            }
        },

        selectGender(genderOption) {
            try {
                this.updatedGender = genderOption;
                this.genderMenu = false;
            } catch (error) {
                console.error("Error in selectGender:", error);
                // Optionally handle or log the error further if needed
            }
        },
        async updateUserInfo() {
            try {

                const dateString = this.updatedBirthday;
                const dateObject = new Date(dateString);

                const changeInfoUserRequest = {
                    fullName: this.updatedFullName,
                    sex: this.updatedGender === 'Nam' ? 'Male' : 'Female',
                    birthday: dateObject,
                }

                console.log(changeInfoUserRequest);

                const response = await axios.post(`users/changeInfoUser`, changeInfoUserRequest, {
                    headers: {
                        'Authorization': localStorage.getItem("token")
                    }
                });

                if (response.status === 200) {
                    this.user.fullName = changeInfoUserRequest.fullName;
                    this.user.sex = changeInfoUserRequest.sex;
                    const year = changeInfoUserRequest.birthday.getUTCFullYear();
                    const month = String(changeInfoUserRequest.birthday.getUTCMonth() + 1).padStart(2, "0");
                    const day = String(changeInfoUserRequest.birthday.getUTCDate()).padStart(2, "0");
                    const hours = String(changeInfoUserRequest.birthday.getUTCHours()).padStart(2, "0");
                    const minutes = String(changeInfoUserRequest.birthday.getUTCMinutes()).padStart(2, "0");
                    const seconds = String(changeInfoUserRequest.birthday.getUTCSeconds()).padStart(2, "0");
                    const formattedDate = `${year}-${month}-${day}T${hours}:${minutes}:${seconds}.000+00:00`;
                    this.user.birthDay = formattedDate;
                    this.formattedBirthday();
                    this.updateDialogVisible = false;
                    this.dialogVisible = true;
                    this.toast.success(response.data, { timeout: 1500 });
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
        async addFileCoverAvatar(event) {
            try {
                const formData = new FormData();
                formData.append('file', event.target.files[0]);

                const response = await axios.post(`users/changeImageCoverAvatar`, formData, {
                    headers: {
                        'Authorization': localStorage.getItem("token"),
                        'Content-Type': 'multipart/form-data',
                    }
                });

                if (response.status === 200) {
                    this.fetchCoverAvatar();
                    this.toast.success(response.data, { timeout: 1500 });
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
        async addFileAvatar(event) {
            try {
                const formData = new FormData();
                formData.append('file', event.target.files[0]);

                const response = await axios.post(`users/changeImageAvatar`, formData, {
                    headers: {
                        'Authorization': localStorage.getItem("token"),
                        'Content-Type': 'multipart/form-data',
                    }
                });

                if (response.status === 200) {
                    this.fetchAvatar();
                    this.$emit('update', this.user);
                    this.toast.success(response.data, { timeout: 1500 });
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
        fetchCoverAvatar() {
            axios.get(`users/imageCoverAvatar`, {
                headers: {
                    'Authorization': localStorage.getItem("token"),
                },
                responseType: 'blob',
            }).then(response => {
                this.user.imageCoverPhotoUrl = URL.createObjectURL(response.data);
            }).catch(error => {
                console.error('Error fetching avatar:', error);
            });
        },
        fetchAvatar() {
            axios.get(`users/imageAvatar`, {
                headers: {
                    'Authorization': localStorage.getItem("token"),
                },
                responseType: 'blob',
            }).then(response => {
                this.user.imageAvatarUrl = URL.createObjectURL(response.data);
            }).catch(error => {
                console.error('Error fetching avatar:', error);
            });
        },
    }
}
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

                .profile-header {
                    margin-top: 5px;
                    margin-bottom: 10px;
                    margin-left: 3px;
                }

                .user-profile-details {
                    display: flex;
                    flex-direction: column;

                    .user-profile-item {
                        display: flex;
                        justify-content: space-between;
                        margin-bottom: 10px;

                        .title {
                            flex-basis: 40%;
                            margin-left: 3px;
                        }

                        .content {
                            flex-basis: 60%;
                        }
                    }
                }
            }

            .profile-action {
                height: 40px;

                .update-button {
                    margin-top: 10px;
                    display: flex;
                    justify-content: center;
                    cursor: pointer;
                }
            }
        }
    }
}
</style>