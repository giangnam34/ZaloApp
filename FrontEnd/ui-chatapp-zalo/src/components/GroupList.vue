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
        <div class="wrapper" id="wrapper">
            <div class="cart-list">
                <div class="title" style="height: 64px; width: 100%;">Nhóm ({{ groups.length }})</div>
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
                            <div class="popover-content"
                                style="min-width: 240px; width: initial; box-sizing: border-box;">
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
                            <div class="popover-content"
                                style="min-width: 240px; width: initial; box-sizing: border-box;">
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
                    <div class="loop" v-for="group in groups" :key="group.id">
                        <div style="display: flex; align-items: center; justify-content: space-between;"
                            :class="{ 'hovered': hoveredItem === group.id }" @mouseenter="(hoveredItem = group.id)"
                            @mouseleave="hoveredItem = ''">
                            <div class="friend-info">
                                <div class="avatar-container">
                                    <div class="avatar-wrapper">
                                        <img :src=group.img class="avatar">
                                    </div>
                                </div>
                                <div class="detail">
                                    <div class="name-wrapper">
                                        <span>{{ group.name }}</span>
                                    </div>
                                    <div class="label">
                                        <span>{{ group.quantity }} thành viên</span>
                                    </div>
                                </div>
                            </div>
                            <div class="action">
                                <div class="popover-action-container" @blur="hidePopover" tabindex="0">
                                    <a id="ellipsis-icon" @click="(event) => handleClickAction(group.id, event)">
                                        <font-awesome-icon icon="fa-solid fa-ellipsis" />
                                    </a>
                                    <div class="popoverAction" v-show="group.id === selectedItem"
                                        :style="{ left: popoverLeft, top: popoverTop }">
                                        <div class="popover-body">
                                            <div class="popover-item"
                                                :class="{ 'hoveredFilter': hoveredItem === 'type' }"
                                                @mouseenter="(hoveredItem = 'type')" @mouseleave="hoveredItem = ''">
                                                <div>
                                                    Phân loại
                                                </div>
                                            </div>
                                            <div class="separator"></div>
                                            <div class="popover-item" style="color: red"
                                                :class="{ 'hoveredFilter': hoveredItem === 'delete' }"
                                                @mouseenter="(hoveredItem = 'delete')" @mouseleave="hoveredItem = ''"
                                                @click="quitGroup(group.id)">
                                                Rời nhóm
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
export default {
    data() {
        return {
            selectedItem: '',
            typeOfSort: 'Hoạt động (mới → cũ)',
            typeOfFilter: 'Tất cả',
            hoveredItem: '',
            chosenFilter: 'all',
            chosenType: 'newest',
            popoverRight: 0,
            popoverTop: 0,
            popoverLeft: 0,
            groups: [
                { id: 1, name: "Group 1", quantity: 5, img: "https://i.imgur.com/gEKsypv.jpg" },
                { id: 2, name: "Group 2", quantity: 5, img: "https://i.imgur.com/gEKsypv.jpg" },
                { id: 3, name: "Group 3", quantity: 5, img: "https://i.imgur.com/gEKsypv.jpg" },
                { id: 4, name: "Group 4", quantity: 5, img: "https://i.imgur.com/gEKsypv.jpg" },
                { id: 5, name: "Group 5", quantity: 5, img: "https://i.imgur.com/gEKsypv.jpg" },
                { id: 6, name: "Group 6", quantity: 5, img: "https://i.imgur.com/gEKsypv.jpg" },
                { id: 7, name: "Group 7", quantity: 5, img: "https://i.imgur.com/gEKsypv.jpg" },
                { id: 8, name: "Group 8", quantity: 5, img: "https://i.imgur.com/gEKsypv.jpg" },
                { id: 9, name: "Group 9", quantity: 5, img: "https://i.imgur.com/gEKsypv.jpg" }
            ]
        };
    },
    methods: {
        selectItem(item) {
            try {
                this.selectedItem = item;
            } catch (exception) {
                console.log("Error in selectItem", exception);
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
                } else if (item === 'descending') {
                    this.typeOfSort = 'Tên (Z-A)';
                } else if (item === 'newest') {
                    this.typeOfSort = 'Hoạt động (mới → cũ)';
                } else {
                    this.typeOfSort = 'Hoạt động (cũ → mới)';
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
                } else if (item === 'classify') {
                    this.typeOfFilter = 'Phân loại';
                } else {
                    this.typeOfFilter = 'Nhóm tôi quản lý';
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
                console.log("Error in handleClickAction", exception);
            }
        },

        quitGroup(id) {
            try {
                const index = this.groups.findIndex(group => group.id === id);
                if (index !== -1) {
                    this.groups.splice(index, 1);
                }
            } catch (exception) {
                console.log("Error in quitGroup", exception);
            }
        },
    },
    name: 'GroupList'
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
                    height: 90px;
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