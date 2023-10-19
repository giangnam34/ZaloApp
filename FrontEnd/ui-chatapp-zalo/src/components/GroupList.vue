<template>
    <div class="contact">
        <div class="header">
            <a id="user-group-icon">
                <font-awesome-icon icon="fa-solid fa-users-line" />
            </a>
            <p class="menu-name">
                <span>Danh sách nhóm</span>
            </p>
        </div>
        <div class="wrapper">
            <div class="cart-list">
                <div class="title" style="height: 64px; width: 100%;">Nhóm (6)</div>
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
                                    <div style="width: 35px;" v-show="!(chosenType === 'ascending')"></div>
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
                                    <div style="width: 35px;" v-show="!(chosenType === 'descending')"></div>
                                    <a id="check-icon" v-show="chosenType === 'descending'">
                                        <font-awesome-icon icon="fa-solid fa-check" />
                                    </a>
                                    <div class="truncate">
                                        <span>Tên (Z-A)</span>
                                    </div>
                                </div>
                                <div class="popover-item" :class="{ 'hoveredFilter': hoveredItem === 'newest' }"
                                    @mouseenter="(hoveredItem = 'newest')" @mouseleave="hoveredItem = ''"
                                    @click="chooseTypeOfSort(hoveredItem)">
                                    <div style="width: 35px;" v-show="!(chosenType === 'newest')"></div>
                                    <a id="check-icon" v-show="chosenType === 'newest'">
                                        <font-awesome-icon icon="fa-solid fa-check" />
                                    </a>
                                    <div class="truncate">
                                        <span>Hoạt động (mới → cũ)</span>
                                    </div>
                                </div>
                                <div class="popover-item" :class="{ 'hoveredFilter': hoveredItem === 'oldest' }"
                                    @mouseenter="(hoveredItem = 'oldest')" @mouseleave="hoveredItem = ''"
                                    @click="chooseTypeOfSort(hoveredItem)">
                                    <div style="width: 35px;" v-show="!(chosenType === 'oldest')"></div>
                                    <a id="check-icon" v-show="chosenType === 'oldest'">
                                        <font-awesome-icon icon="fa-solid fa-check" />
                                    </a>
                                    <div class="truncate">
                                        <span>Hoạt động (cũ → mới)</span>
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
                                    <div style="width: 35px;" v-show="!(chosenFilter === 'all')"></div>
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
                                    <div style="width: 35px;" v-show="!(chosenFilter === 'classify')"></div>
                                    <a id="check-icon" v-show="chosenFilter === 'classify'">
                                        <font-awesome-icon icon="fa-solid fa-check" />
                                    </a>
                                    <div class="truncate">
                                        <span>Phân loại</span>
                                    </div>
                                </div>
                                <div class="popover-item" :class="{ 'hoveredFilter': hoveredItem === 'isAdmin' }"
                                    @mouseenter="(hoveredItem = 'isAdmin')" @mouseleave="hoveredItem = ''"
                                    @click="chooseTypeOfFilter(hoveredItem)">
                                    <div style="width: 35px;" v-show="!(chosenFilter === 'isAdmin')"></div>
                                    <a id="check-icon" v-show="chosenFilter === 'isAdmin'">
                                        <font-awesome-icon icon="fa-solid fa-check" />
                                    </a>
                                    <div class="truncate">
                                        <span>Nhóm tôi quản lý</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="list">
                    <div style="display: flex; align-items: center; justify-content: space-between;"
                        :class="{ 'hovered': hoveredItem === '1' }" @mouseenter="(hoveredItem = '1')"
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
                                    <span>5 thành viên</span>
                                </div>
                            </div>
                        </div>
                        <div class="action">
                            <!-- Các nút hoạt động -->
                            <a id="ellipsis-icon">
                                <font-awesome-icon icon="fa-solid fa-ellipsis" />
                            </a>
                        </div>
                    </div>
                    <hr style="border: none; border-bottom: 1px solid #ccc; margin-left: 78px;">
                    <div style="display: flex; align-items: center; justify-content: space-between;"
                        :class="{ 'hovered': hoveredItem === '2' }" @mouseenter="(hoveredItem = '2')"
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
                                    <span>5 thành viên</span>
                                </div>
                            </div>
                        </div>
                        <div class="action">
                            <!-- Các nút hoạt động -->
                            <a id="ellipsis-icon">
                                <font-awesome-icon icon="fa-solid fa-ellipsis" />
                            </a>
                        </div>
                    </div>
                    <hr style="border: none; border-bottom: 1px solid #ccc; margin-left: 78px;">
                    <div style="display: flex; align-items: center; justify-content: space-between;"
                        :class="{ 'hovered': hoveredItem === '3' }" @mouseenter="(hoveredItem = '3')"
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
                                    <span>5 thành viên</span>
                                </div>
                            </div>
                        </div>
                        <div class="action">
                            <!-- Các nút hoạt động -->
                            <a id="ellipsis-icon">
                                <font-awesome-icon icon="fa-solid fa-ellipsis" />
                            </a>
                        </div>
                    </div>
                    <hr style="border: none; border-bottom: 1px solid #ccc; margin-left: 78px;">
                    <div style="display: flex; align-items: center; justify-content: space-between;"
                        :class="{ 'hovered': hoveredItem === '4' }" @mouseenter="(hoveredItem = '4')"
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
                                    <span>5 thành viên</span>
                                </div>
                            </div>
                        </div>
                        <div class="action">
                            <!-- Các nút hoạt động -->
                            <a id="ellipsis-icon">
                                <font-awesome-icon icon="fa-solid fa-ellipsis" />
                            </a>
                        </div>
                    </div>
                    <hr style="border: none; border-bottom: 1px solid #ccc; margin-left: 78px;">
                    <div style="display: flex; align-items: center; justify-content: space-between;"
                        :class="{ 'hovered': hoveredItem === '5' }" @mouseenter="(hoveredItem = '5')"
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
                                    <span>5 thành viên</span>
                                </div>
                            </div>
                        </div>
                        <div class="action">
                            <!-- Các nút hoạt động -->
                            <a id="ellipsis-icon">
                                <font-awesome-icon icon="fa-solid fa-ellipsis" />
                            </a>
                        </div>
                    </div>
                    <hr style="border: none; border-bottom: 1px solid #ccc; margin-left: 78px;">
                    <div style="display: flex; align-items: center; justify-content: space-between;"
                        :class="{ 'hovered': hoveredItem === '6' }" @mouseenter="(hoveredItem = '6')"
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
                                    <span>5 thành viên</span>
                                </div>
                            </div>
                        </div>
                        <div class="action">
                            <!-- Các nút hoạt động -->
                            <a id="ellipsis-icon">
                                <font-awesome-icon icon="fa-solid fa-ellipsis" />
                            </a>
                        </div>
                    </div>
                    <hr style="border: none; border-bottom: 1px solid #ccc; margin-left: 78px;">
                </div>
            </div>
        </div>
    </div>
</template>
  
<script>
export default {
    data() {
        return {
            selectedItem: '',
            typeOfSort: 'Hoạt động (mới → cũ)',
            typeOfFilter: 'Tất cả',
            hoveredItem: '',
            chosenFilter: 'all',
            chosenType: 'newest'
        };
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
            } else if (item == 'descending') {
                this.typeOfSort = 'Tên (Z-A)';
            } else if (item == 'newest') {
                this.typeOfSort = 'Hoạt động (mới → cũ)';
            } else {
                this.typeOfSort = 'Hoạt động (cũ → mới)';
            }
        },
        chooseTypeOfFilter(item) {
            this.chosenFilter = item;
            if (item == 'all') {
                this.typeOfFilter = 'Tất cả';
            } else if (item == 'classify') {
                this.typeOfFilter = 'Phân loại';
            } else {
                this.typeOfFilter = 'Nhóm tôi quản lý';
            }
        },
    },
    name: 'GroupList'
}
</script>
  
<style scoped lang = "scss">
.contact {
    display: flex;
    flex-direction: column;
    height: 100vh;
    width: 73%;

    .header {
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
        align-items: center;
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
        margin-right: 10px;
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
    }

    #down-icon {
        margin-left: auto;
        margin-right: 6px;
    }

    .name-wrapper,
    .label {
        display: flex;
    }

    .label span {
        margin-top: 5px;
        margin-bottom: 2px;
        font-size: 13px;
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