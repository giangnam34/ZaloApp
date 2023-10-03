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
                <div class="nav__tabs_message" title="Cloud của tôi">
                    <a id="chat-message-icon">
                        <font-awesome-icon icon="fa-solid fa-cloud" />
                    </a>
                </div>
                <div class="nav__tabs_contacts" title="Công cụ">
                    <a id="address-icon">
                        <font-awesome-icon icon="fa-solid fa-toolbox" />
                    </a>
                </div>
                <div class="nav__tabs_to-do" title="Cài đặt">
                    <a id="to-do-icon">
                        <font-awesome-icon icon="fa-solid fa-gear" />
                    </a>
                </div>

            </div>
        </div>
        <div id="chat-message">
            <div class="relative">
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
    </div>
</template>
  
<script>
export default {
    name: 'SidebarNav'
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
            height: 100px;
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

            .nav__tabs_message,
            .nav__tabs_contacts,
            .nav__tabs_to-do {
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
        }

        .nav__tabs_middle {
            height: 270px;
        }

    }

    #chat-message {
        display: inline-block;
        height: 100%;
        width: 84%;
        vertical-align: top;

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
</style>
  