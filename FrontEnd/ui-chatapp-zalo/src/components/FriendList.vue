<template>
    <div class="contact">
        <div class="header">
            <a id="user-group-icon">
                <font-awesome-icon icon="fa-solid fa-user-group" />
            </a>
            <p class="menu-name">
                <span>Danh sách bạn bè</span>
            </p>
        </div>
        <div class="wrapper">
            <div class="cart-list">
                <div class="title" style="height: 64px; width: 100%;">Bạn bè ({{ friends.length }})</div>
                <div class="filter-wrapper">
                    <div class="search-container">
                        <a id="search-icon">
                            <font-awesome-icon icon="fa-solid fa-magnifying-glass" />
                        </a>
                        <input type="text" v-model="searchText" id="contact-input-search" placeholder="Tìm kiếm">
                    </div>
                    <div class="filter-contact" @blur="hidePopover" tabindex="0">
                        <div class="filter-contact-child"
                            :class="{ 'hoveredFilter': hoveredItem === 'sort', 'selected': selectedItem === 'sort' }"
                            @mouseenter="(hoveredItem = 'sort')" @mouseleave="hoveredItem = ''"
                            @click="togglePopover('sort')">
                            <a id="sort-icon">
                                <font-awesome-icon icon="fa-solid fa-sort" />
                            </a>
                            <span>
                                {{ typeOfSort }}
                            </span>
                            <a id="down-icon">
                                <font-awesome-icon icon="fa-solid fa-chevron-down" />
                            </a>
                        </div>
                        <div class="popover" style="z-index: 20; opacity: 1; top: 47.6px; left: 291px;"
                            v-if="selectedItem === 'sort'">
                            <!-- Nội dung của popover -->
                            <div class="popover-content"
                                style="min-width: 240px; width: initial; box-sizing: border-box;">
                                <div class="popover-item" :class="{ 'hoveredFilter': hoveredItem === 'ascending' }"
                                    @mouseenter="(hoveredItem = 'ascending')" @mouseleave="hoveredItem = ''"
                                    @click="chooseTypeOfSort(hoveredItem)">
                                    <div style="width: 35px;" v-show="chosenType === 'descending'"></div>
                                    <a id="check-icon" v-show="chosenType === 'ascending'">
                                        <font-awesome-icon icon="fa-solid fa-check" />
                                    </a>
                                    <div class="truncate">
                                        <span>Tên (A-Z)</span>
                                    </div>
                                </div>
                                <div class="popover-item" :class="{ 'hoveredFilter': hoveredItem === 'descending' }"
                                    @mouseenter="(hoveredItem = 'descending')" @mouseleave="hoveredItem = ''"
                                    @click="chooseTypeOfSort(hoveredItem)">
                                    <div style="width: 35px;" v-show="chosenType === 'ascending'"></div>
                                    <a id="check-icon" v-show="chosenType === 'descending'">
                                        <font-awesome-icon icon="fa-solid fa-check" />
                                    </a>
                                    <div class="truncate">
                                        <span>Tên (Z-A)</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="filter-contact-child"
                            :class="{ 'hoveredFilter': hoveredItem === 'filter', 'selected': selectedItem === 'filter' }"
                            @mouseenter="(hoveredItem = 'filter')" @mouseleave="hoveredItem = ''"
                            @click="togglePopover('filter')">
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
                        <div class="popover" style="z-index: 20; opacity: 1; top: 47.6px; left: 539px;"
                            v-if="selectedItem === 'filter'">
                            <!-- Nội dung của popover -->
                            <div class="popover-content"
                                style="min-width: 240px; width: initial; box-sizing: border-box;">
                                <div class="popover-item" :class="{ 'hoveredFilter': hoveredItem === 'all' }"
                                    @mouseenter="(hoveredItem = 'all')" @mouseleave="hoveredItem = ''"
                                    @click="chooseTypeOfFilter(hoveredItem)">
                                    <div style="width: 35px;" v-show="chosenFilter === 'classify'"></div>
                                    <a id="check-icon" v-show="chosenFilter === 'all'">
                                        <font-awesome-icon icon="fa-solid fa-check" />
                                    </a>
                                    <div class="truncate">
                                        <span>Tất cả</span>
                                    </div>
                                </div>
                                <div class="popover-item" :class="{ 'hoveredFilter': hoveredItem === 'classify' }"
                                    @mouseenter="(hoveredItem = 'classify')" @mouseleave="hoveredItem = ''"
                                    @click="chooseTypeOfFilter(hoveredItem)">
                                    <div style="width: 35px;" v-show="chosenFilter === 'all'"></div>
                                    <a id="check-icon" v-show="chosenFilter === 'classify'">
                                        <font-awesome-icon icon="fa-solid fa-check" />
                                    </a>
                                    <div class="truncate">
                                        <span>Phân loại</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="list">
                    <div class="loop" v-for="friend in filteredFriends" :key="friend.phoneNumber">
                        <div style="display: flex; align-items: center; justify-content: space-between;"
                            :class="{ 'hovered': hoveredItem === friend.phoneNumber }"
                            @mouseenter="(hoveredItem = friend.phoneNumber)" @mouseleave="hoveredItem = ''">
                            <div class="friend-info">
                                <div class="avatar-container">
                                    <div class="avatar-wrapper">
                                        <img :src="friend.imageAvatar" class="avatar">
                                    </div>
                                </div>
                                <div class="detail">
                                    <span>{{ friend.userName }}</span>
                                </div>
                            </div>
                            <div class="action">
                                <div class="popover-action-container" @blur="hidePopover" tabindex="0">
                                    <a id="ellipsis-icon"
                                        @click="(event) => handleClickAction(friend.phoneNumber, event)">
                                        <font-awesome-icon icon="fa-solid fa-ellipsis" />
                                    </a>
                                    <div class="popoverAction" v-show="friend.phoneNumber === selectedItem"
                                        :style="{ left: popoverLeft, top: popoverTop }">
                                        <div class="popover-body">
                                            <div class="popover-item"
                                                :class="{ 'hoveredFilter': hoveredItem === 'info' }"
                                                @mouseenter="(hoveredItem = 'info')" @mouseleave="hoveredItem = ''"
                                                @click="showFoundUserDialog(friend.phoneNumber)">
                                                <div>
                                                    Xem thông tin
                                                </div>
                                            </div>
                                            <!-- <div class="separator"></div>
                                            <div class="popover-item" :class="{ 'hoveredFilter': hoveredItem === 'type' }"
                                                @mouseenter="(hoveredItem = 'type')" @mouseleave="hoveredItem = ''">
                                                <div>
                                                    Phân loại
                                                </div>
                                            </div>
                                            <div class="popover-item" :class="{ 'hoveredFilter': hoveredItem === 'rename' }"
                                                @mouseenter="(hoveredItem = 'rename')" @mouseleave="hoveredItem = ''">
                                                <div>
                                                    Đặt tên ghi nhớ
                                                </div>
                                            </div>
                                            <div class="separator"></div> -->
                                            <div class="popover-item"
                                                :class="{ 'hoveredFilter': hoveredItem === 'block' }"
                                                @mouseenter="(hoveredItem = 'block')" @mouseleave="hoveredItem = ''"
                                                @click="blockUser(friend.phoneNumber)">
                                                <div>
                                                    Chặn
                                                </div>
                                            </div>
                                            <div class="separator"></div>
                                            <div class="popover-item" style="color: red"
                                                :class="{ 'hoveredFilter': hoveredItem === 'delete' }"
                                                @mouseenter="(hoveredItem = 'delete')" @mouseleave="hoveredItem = ''"
                                                @click="deleteFriend(friend.phoneNumber)">
                                                Xóa bạn
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <hr style="border: none; border-bottom: 1px solid #ccc; margin-left: 78px;">
                    </div>
                </div>
            </div>
        </div>
    </div>
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
                <div class="mb-2"></div>
            </v-card-text>
        </v-card>
    </v-dialog>
</template>

<script>
import axios from 'axios';
import { useToast } from "vue-toastification";
import { format, parseISO } from 'date-fns';
import viLocale from 'date-fns/locale/vi';
export default {
    data() {
        return {
            selectedItem: '',
            typeOfSort: 'Tên (A-Z)',
            typeOfFilter: 'Tất cả',
            hoveredItem: '',
            showVisibleUserInfo: false,
            chosenFilter: 'all',
            chosenType: 'ascending',
            popoverRight: 0,
            popoverTop: 0,
            popoverLeft: 0,
            displayedDate: '',
            friends: [],
            user: null,
            userFound: null,
            searchText: "",
        };
    },
    setup() {
        // Get toast interface
        const toast = useToast();
        return { toast }
    },
    created() {
        this.getListOfFriends();
        const userString = localStorage.getItem('user');
        if (userString) {
            this.user = JSON.parse(userString);
        }
    },
    computed: {
        filteredFriends() {
            const normalizedSearchText = this.searchText.toLowerCase();
            return this.friends.filter(friend =>
                friend.userName.toLowerCase().includes(normalizedSearchText)
            );
        },
    },
    methods: {
        selectItem(item) {
            try {
                this.selectedItem = item;
            } catch (exception) {
                console.log("Error in selectItem", exception);
            }
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

        clearSelectedItem() {
            try {
                this.selectedItem = '';
            } catch (exception) {
                console.log("Error in clearSelectedItem", exception);
            }
        },

        isSelected(item) {
            try {
                return this.selectedItem === item;
            } catch (exception) {
                console.log("Error in isSelected", exception);
                return false;
            }
        },

        togglePopover(item) {
            try {
                this.selectItem(item);
            } catch (exception) {
                console.log("Error in togglePopover", exception);
            }
        },

        hidePopover() {
            try {
                this.clearSelectedItem();
            } catch (exception) {
                console.log("Error in hidePopover", exception);
            }
        },

        chooseTypeOfSort(item) {
            try {
                this.chosenType = item;
                if (item === 'ascending') {
                    this.typeOfSort = 'Tên (A-Z)';
                } else {
                    this.typeOfSort = 'Tên (Z-A)';
                }
            } catch (exception) {
                console.log("Error in chooseTypeOfSort", exception);
            }
        },

        chooseTypeOfFilter(item) {
            try {
                this.chosenFilter = item;
                if (item === 'all') {
                    this.typeOfFilter = 'Tất cả';
                } else {
                    this.typeOfFilter = 'Phân loại';
                }
            } catch (exception) {
                console.log("Error in chooseTypeOfFilter", exception);
            }
        },
        handleClickAction(id, event) {
            try {
                this.selectedItem = id;
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
                event.stopPropagation();
            } catch (exception) {
                console.log("Error in handleClickAction ", exception);
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

                    this.friends = response.data;
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
        async deleteFriend(phoneNumber) {
            try {
                const friendRequest = {
                    fromPhoneNumberUser: this.user.phoneNumber,
                    toPhoneNumberUser: phoneNumber,
                }

                const response = await axios.post(`users/unFriendUser`, friendRequest, {
                    headers: {
                        'Authorization': localStorage.getItem("token")
                    }
                });

                if (response.status === 200) {
                    this.friends = this.friends.filter(friend => friend.phoneNumber !== phoneNumber);
                    this.toast.success(response.data, { timeout: 1500 });
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
                    this.friends = this.friends.filter(friend => friend.phoneNumber !== phoneNumber);
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
        showUserInfoDialog(friend) {
            try {
                this.userFound = friend;

                this.formattedBirthday();

                this.selectedItem = '';

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
        async showFoundUserDialog(phoneNumber) {
            try {
                const responseUser = await axios.get(`users/findUserByPhoneNumber/${phoneNumber}`, {
                    headers: {
                        'Authorization': localStorage.getItem("token")
                    }
                });

                if (responseUser.status === 200) {
                    const userTemp = responseUser.data;
                    this.showUserInfoDialog(userTemp);
                } else {
                    console.error(responseUser.body);
                    this.toast.error(responseUser.body || 'Đã xảy ra lỗi!', { timeout: 1500 });
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
        }
    },
    name: 'FriendList'
}
</script>

<style scoped lang="scss">
.contact {
    display: flex;
    flex-direction: column;
    height: 100vh;
    width: 100%;

    .header {
        flex: 0 0 auto;
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
        flex: 1 1 auto;
        height: 92%;
        overflow-y: scroll;
        overflow-x: hidden;

        ::-webkit-scrollbar {
            width: 8px;
            /* Độ rộng của thanh cuộn */
        }

        ::-webkit-scrollbar-thumb {
            background-color: #ccc;
            /* Màu nền của thanh cuộn */
            border-radius: 4px;
            /* Bo góc của thanh cuộn */
        }

        ::-webkit-scrollbar-thumb:hover {
            background-color: #999;
            /* Màu nền khi di chuột qua thanh cuộn */
        }

        ::-webkit-scrollbar-track {
            background-color: #f1f1f1;
            /* Màu nền của phần bên trong thanh cuộn */
            border-radius: 4px;
            /* Bo góc của phần bên trong thanh cuộn */
        }
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
        margin-bottom: 8px;
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