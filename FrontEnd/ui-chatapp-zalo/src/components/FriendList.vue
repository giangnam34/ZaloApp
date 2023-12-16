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
                        <input type="text" id="contact-input-search" placeholder="Tìm kiếm">
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
                            <div class="popover-content" style="min-width: 240px; width: initial; box-sizing: border-box;">
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
                            <div class="popover-content" style="min-width: 240px; width: initial; box-sizing: border-box;">
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
                    <span style="font-weight: bold; margin-left: 20px;" class="contact-item">T</span>
                    <div class="loop" v-for="friend in friends" :key="friend.id">
                        <div style="display: flex; align-items: center; justify-content: space-between;"
                            :class="{ 'hovered': hoveredItem === friend.id }" @mouseenter="(hoveredItem = friend.id)"
                            @mouseleave="hoveredItem = ''">
                            <div class="friend-info">
                                <div class="avatar-container">
                                    <div class="avatar-wrapper">
                                        <img :src="friend.img" class="avatar">
                                    </div>
                                </div>
                                <div class="detail">
                                    <span>{{ friend.name }}</span>
                                </div>
                            </div>
                            <div class="action">
                                <div class="popover-action-container" @blur="hidePopover" tabindex="0">
                                    <a id="ellipsis-icon" @click="(event) => handleClickAction(friend.id, event)">
                                        <font-awesome-icon icon="fa-solid fa-ellipsis" />
                                    </a>
                                    <div class="popoverAction" v-show="friend.id === selectedItem"
                                        :style="{ right: popoverRight, top: popoverTop }">
                                        <div class="popover-body">
                                            <div class="popover-item" :class="{ 'hoveredFilter': hoveredItem === 'info' }"
                                                @mouseenter="(hoveredItem = 'info')" @mouseleave="hoveredItem = ''">
                                                <div>
                                                    Xem thông tin
                                                </div>
                                            </div>
                                            <div class="separator"></div>
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
                                            <div class="separator"></div>
                                            <div class="popover-item" :class="{ 'hoveredFilter': hoveredItem === 'block' }"
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
</template>
  
<script>
import axios from 'axios';
import { useToast } from "vue-toastification";
export default {
    data() {
        return {
            selectedItem: '',
            typeOfSort: 'Tên (A-Z)',
            typeOfFilter: 'Tất cả',
            hoveredItem: '',
            chosenFilter: 'all',
            chosenType: 'ascending',
            popoverRight: 0,
            popoverTop: 0,
            friends: [
                { id: 1, name: "Friend 1", img: "https://i.imgur.com/gEKsypv.jpg", phoneNumber: "0965556651" },
                { id: 2, name: "Friend 2", img: "https://i.imgur.com/gEKsypv.jpg", phoneNumber: "0965556653" },
                { id: 3, name: "Friend 3", img: "https://i.imgur.com/gEKsypv.jpg", phoneNumber: "0965556654" },
                { id: 4, name: "Friend 4", img: "https://i.imgur.com/gEKsypv.jpg", phoneNumber: "0965556655" },
                { id: 5, name: "Friend 5", img: "https://i.imgur.com/gEKsypv.jpg", phoneNumber: "0965556656" },
                { id: 6, name: "Friend 6", img: "https://i.imgur.com/gEKsypv.jpg", phoneNumber: "0965556657" },
                { id: 7, name: "Friend 7", img: "https://i.imgur.com/gEKsypv.jpg", phoneNumber: "0965556658" },
                { id: 8, name: "Friend 8", img: "https://i.imgur.com/gEKsypv.jpg", phoneNumber: "0965556659" },
                { id: 9, name: "Friend 9", img: "https://i.imgur.com/gEKsypv.jpg", phoneNumber: "0965556660" }
            ],
            user: null,
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
    methods: {
        selectItem(item) {
            this.selectedItem = item;
        },
        clearSelectedItem() {
            this.selectedItem = '';
        },
        isSelected(item) {
            return this.selectedItem === item;
        },
        togglePopover(item) {
            this.selectItem(item);
        },
        hidePopover() {
            this.clearSelectedItem();
        },
        chooseTypeOfSort(item) {
            this.chosenType = item;
            if (item == 'ascending') {
                this.typeOfSort = 'Tên (A-Z)';
            } else {
                this.typeOfSort = 'Tên (Z-A)';
            }
        },
        chooseTypeOfFilter(item) {
            this.chosenFilter = item;
            if (item == 'all') {
                this.typeOfFilter = 'Tất cả';
            } else {
                this.typeOfFilter = 'Phân loại';
            }
        },
        handleClickAction(id, event) {
            this.selectedItem = id;
            const rect = event.target.getBoundingClientRect();
            const x = rect.left;
            const y = rect.top;
            this.popoverRight = x - 1216 + 'px';
            this.popoverTop = y - 210 + 'px';
            event.stopPropagation();
        },
        async getListOfFriends() {
            try {
                const response = await axios.get(`users/getAllFriendUser`, {
                    headers: {
                        'Authorization': localStorage.getItem("token")
                    }
                });

                if (response.status === 200) {

                    const friends = response.data;
                    console.log(friends);

                } else {
                    console.error(response.body);
                    this.toast.error(response.body, { timeout: 3000 });
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
                    this.toast.success(response.body, { timeout: 3000 });
                } else {
                    console.error(response.body);
                    this.toast.error(response.body, { timeout: 3000 });
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
                    this.toast.success(response.body, { timeout: 3000 });
                } else {
                    console.error(response.body);
                    this.toast.error(response.body || 'Đã xảy ra lỗi!', { timeout: 3000 });
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
    },
    name: 'FriendList'
}
</script>
  
<style scoped lang = "scss">
.contact {
    display: flex;
    flex-direction: column;
    height: 100vh;
    width: 100%;

    .header {
        flex: 0 0 auto;
        height: 64px;
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
        height: 1200px;
        overflow: scroll;

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
                    height: 210px;
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
</style>