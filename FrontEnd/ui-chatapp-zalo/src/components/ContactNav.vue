<template>
    <div id="sidebarNav">
        <div id="main-tab">
            <div class="nav__tabs__zalo web" title="Giang Nam">
                <img src="https://s120-ava-talk.zadn.vn/3/e/6/b/1/120/8b72a04be66a9ef7009df15d86b69810.jpg" class="a-child">
            </div>
            <div class="nav__tabs_top">
                <div class="nav__tabs_message" title="Tin nhắn">
                    <a id="chat-message-icon">
                        <font-awesome-icon icon="fa-solid fa-comment-dots" />
                    </a>
                </div>
                <div class="nav__tabs_contacts" title="Danh bạ">
                    <a id="address-icon">
                        <font-awesome-icon icon="fa-regular fa-address-book" />
                    </a>
                </div>
                <div class="nav__tabs_to-do" title="To-Do">
                    <a id="to-do-icon">
                        <font-awesome-icon icon="fa-regular fa-square-check" style="color: #ffffff;" /> </a>
                </div>
            </div>

            <div class="nav__tabs_middle">

            </div>

            <div class="nav__tabs_bottom">
                <div class="nav__tabs_cloud" title="Cloud của tôi">
                    <a id="chat-message-icon">
                        <font-awesome-icon icon="fa-solid fa-cloud" />
                    </a>
                </div>
                <div class="nav__tabs_tool" title="Công cụ">
                    <a id="address-icon">
                        <font-awesome-icon icon="fa-solid fa-toolbox" />
                    </a>
                </div>
                <div class="nav__tabs_setting" title="Cài đặt">
                    <a id="to-do-icon">
                        <font-awesome-icon icon="fa-solid fa-gear" />
                    </a>
                </div>

            </div>
        </div>
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
    <div class="set-width" v-if="selectedMenuItem === 'friends'">
        <FriendList></FriendList>
    </div>
    <div class="set-width" v-if="selectedMenuItem === 'groups'">
        <GroupList></GroupList>
    </div>
    <div class="set-width" v-if="selectedMenuItem === 'friendRequests'">
        <InvitationFriendManage></InvitationFriendManage>
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

#sidebarNav {
    height: 100vh;
    width: 408px;   
    border-right: 1px solid rgb(160, 160, 160);
    display: inline-block;

    #main-tab {
        display: inline-block;
        background-color: rgb(0, 145, 255);
        width: 16%;
        height: 100%;

        .nav__tabs__zalo {
            height: 12%;
            justify-content: center;
            padding-top: 32px;
            box-sizing: border-box;

            .a-child {
                width: 75%;
                height: 75%;
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
                box-sizing: border-box;
                border-radius: 50%;
                margin-left: 7px;
            }
        }

        .nav__tabs_top,
        .nav__tabs_bottom {

            height: 30%;

            .nav__tabs_message,
            .nav__tabs_contacts,
            .nav__tabs_to-do,
            .nav__tabs_tool,
            .nav__tabs_setting,
            .nav__tabs_cloud {
                height: 64px;
                width: 100%;
                display: table;

                #chat-message-icon,
                #address-icon,
                #to-do-icon {
                    color: #fff;
                    display: table-cell;
                    font-size: 28px;
                    vertical-align: middle;
                    text-align: center;
                }
            }

            .nav__tabs_contacts {
                background-color: rgb(22, 119, 193);
            }
        }

        .nav__tabs_middle {
            height: 32%;
        }

    }

    #chat-message {
        display: inline-block;
        height: 100%;
        width: 84%;
        vertical-align: top;

        //background-color: black;

        #search-content {
            height: 8.45%;
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
div.set-width{
    width: 84%;
}
</style>