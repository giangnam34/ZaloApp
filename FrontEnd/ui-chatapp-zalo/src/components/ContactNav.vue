<template>
    <div id="container" style="display: flex;">
        <div id="contact-nav">
            <div id="chat-message">
                <div id="search-content">
                    <font-awesome-icon icon="fa-solid fa-magnifying-glass" />
                    <input type="text" id="contact-input-search" placeholder="Tìm kiếm">
                    <div title="Thêm bạn">
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
    </div>
</template>
  
<script>
import FriendList from './FriendList.vue';
import GroupList from './GroupList.vue';
import InvitationFriendManage from './InvitationFriendManage.vue';
export default {
    components: {
        FriendList,
        GroupList,
        InvitationFriendManage
    },
    data() {
        return {
            selectedMenuItem: 'friends',
            hoveredItem: '',
        };
    },
    methods: {
        selectMenuItem(item) {
            this.selectedMenuItem = item;
        },
        isSelected(item) {
            return this.selectedMenuItem === item;
        }
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

</style>