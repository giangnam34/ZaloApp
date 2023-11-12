<template>
  <div id="sidebarNav" @mousemove="getMousePosition">
    <div id="main-tab">
      <div class="nav__tabs__zalo web" title="Giang Nam" ref="avatar" @click="isClickAvatar('avatar')">
        <img src="https://s120-ava-talk.zadn.vn/3/e/6/b/1/120/8b72a04be66a9ef7009df15d86b69810.jpg" class="a-child" />
        <div id="infoUser" v-show="isShowWhenClickAvatar">
          <div id="userName">Giang Nam</div>
          <hr>
          <div id="yourProfile" @mouseover="onHover('yourProfile')" @mouseleave="outHover()"
            :class="{ hoverProfile: isHover && name === 'yourProfile' }">Hồ sơ của bạn</div>
          <div id="setting" @mouseover="onHover('settingProfile')" @mouseleave="outHover()"
            :class="{ hoverProfile: isHover && name === 'settingProfile' }">Cài đặt</div>
          <hr>
          <div id="signout" @mouseover="onHover('signout')" @mouseleave="outHover()"
            :class="{ hoverProfile: isHover && name === 'signout' }">Đăng xuất</div>
        </div>
      </div>
      <div class="nav__tabs_top">
        <div class="nav__tabs_message" title="Tin nhắn" @mouseover="onHover('message')" @mouseleave="outHover()"
          @click="onClick(1)" :class="{ hover: isHover && name === 'message', isChoose: index === 1 }">
          <a id="chat-message-icon">
            <font-awesome-icon icon="fa-solid fa-comment-dots" />
          </a>
        </div>
        <div class="nav__tabs_contacts" title="Danh bạ" @mouseover="onHover('contacts')" @mouseleave="outHover()"
          @click="onClick(2)" :class="{ hover: isHover && name === 'contacts', isChoose: index === 2 }">
          <a id="address-icon">
            <font-awesome-icon icon="fa-regular fa-address-book" />
          </a>
        </div>
        <div class="nav__tabs_to-do" title="To-Do" @mouseover="onHover('toDo')" @mouseleave="outHover()"
          @click="onClick(3)" :class="{ hover: isHover && name === 'toDo', isChoose: index === 3 }">
          <a id="to-do-icon">
            <font-awesome-icon icon="fa-regular fa-square-check" style="color: #ffffff" />
          </a>
        </div>
      </div>
      <div class="nav__tabs_middle"></div>
      <div class="nav__tabs_bottom">
        <div class="nav__tabs_message" title="Cloud của tôi" @mouseover="onHover('cloud')" @mouseleave="outHover()"
          @click="onClick(4)" :class="{ hover: isHover && name === 'cloud', isChoose: index === 4 }">
          <a id="chat-message-icon">
            <font-awesome-icon icon="fa-solid fa-cloud" />
          </a>
        </div>
        <div ref="moreMessage" class="nav__tabs_contacts" title="Công cụ" @mouseover="onHover('tools')"
          @mouseleave="outHover()" @click="onClick(5)"
          :class="{ hover: isHover && name === 'tools', isChoose: index === 5 }">
          <a id="address-icon">
            <font-awesome-icon icon="fa-solid fa-toolbox" />
          </a>
          <div id="moreMessage" v-show="isShowWhenClickTools">
            <div id="subRocketChat" @mouseover.stop="onHover('subRocketChat')" @mouseleave.stop="outHover()"
              @click.prevent.stop :class="{ subHover: isHover && name === 'subRocketChat', isChoose: index === 7 }">
              <font-awesome-icon icon="fa-brands fa-rocketchat" />
              <div>
                Tin nhắn nhanh
              </div>
            </div>
            <div @mouseover.stop="onHover('subStar')" @mouseleave.stop="outHover()" @click.prevent.stop
              :class="{ subHover: isHover && name === 'subStar', isChoose: index === 8 }">
              <font-awesome-icon icon="fa-regular fa-star" />
              <div>
                Tin đánh dấu
              </div>
            </div>
            <div @mouseover.stop="onHover('subBullHorn')" @mouseleave.stop="outHover()" @click.prevent.stop
              :class="{ subHover: isHover && name === 'subBullHorn', isChoose: index === 9 }">
              <font-awesome-icon icon="fa-solid fa-bullhorn" />
              <div>
                Tin đồng thời
              </div>
            </div>
          </div>
        </div>
        <div ref="moreSetting" class="nav__tabs_to-do" title="Cài đặt" @mouseover="onHover('setting')"
          @mouseleave="outHover()" @click="onClick(6)"
          :class="{ hover: isHover && name === 'setting', isChoose: index === 6 }">
          <a id="to-do-icon">
            <font-awesome-icon icon="fa-solid fa-gear" />
          </a>
          <div id="moreSetting" v-show="isShowWhenClickSetting">
            <div id="inforAccount" @mouseover.stop="onHover('subInforAccount')" @mouseleave.stop="outHover()"
              @click.prevent.stop :class="{ subHover: isHover && name === 'subInforAccount', isChoose: index === 7 }">
              <div class="icon">
                <font-awesome-icon icon="fa-regular fa-user" />
              </div>
              <div>Thông tin tài khoản</div>
            </div>
            <div id="setting" @mouseover.stop="onHover('subSetting')" @mouseleave.stop="outHover()" @click.prevent.stop
              :class="{ subHover: isHover && name === 'subSetting', isChoose: index === 7 }">
              <div class="icon">
                <font-awesome-icon icon="fa-solid fa-gear" />
              </div>
              <div>Cài đặt</div>
            </div>
            <hr>
            <div id="database" @mouseover.stop="onHover('subDatabase')" @mouseleave.stop="outHover()" @click.prevent.stop
              :class="{ subHover: isHover && name === 'subDatabase', isChoose: index === 7 }">
              <div class="icon">
                <font-awesome-icon icon="fa-solid fa-database" />
              </div>
              <div>Dữ liệu</div>
              <div class="arrowRight"><font-awesome-icon icon="fa-solid fa-chevron-right" /></div>
              <div v-show="isShowWhenHoverData" id="subDatabase">
                <div @mouseover.stop="onHover('subSubDatabase')" @mouseleave.stop="outHover()" @click.prevent.stop
                  :class="{ subHover: isHover && name === 'subSubDatabase', isChoose: index === 7 }">Quản lý file</div>
              </div>
            </div>
            <div id="tools" @mouseover.stop="onHover('subTools')" @mouseleave.stop="outHover()" @click.prevent.stop
              :class="{ subHover: isHover && name === 'subTools', isChoose: index === 7 }">
              <div class="icon">
                <font-awesome-icon icon="fa-solid fa-wrench" />
              </div>
              <div>Công cụ </div>
              <div class="arrowRight"><font-awesome-icon icon="fa-solid fa-chevron-right" /></div>
            </div>
            <div id="languages" @mouseover.stop="onHover('subLanguages')" @mouseleave.stop="outHover()"
              @click.prevent.stop :class="{ subHover: isHover && name === 'subLanguages', isChoose: index === 7 }">
              <div class="icon">
                <font-awesome-icon icon="fa-solid fa-globe" />
              </div>
              <div>Ngôn ngữ</div>
              <div class="arrowRight"><font-awesome-icon icon="fa-solid fa-chevron-right" /></div>
            </div>
            <div id="about" @mouseover.stop="onHover('subAbout')" @mouseleave.stop="outHover()" @click.prevent.stop
              :class="{ subHover: isHover && name === 'subAbout', isChoose: index === 7 }">
              <div class="icon">
                <font-awesome-icon icon="fa-solid fa-info" style="margin-left: 5px;" />
              </div>
              <div>Giới thiệu</div>
              <div class="arrowRight"><font-awesome-icon icon="fa-solid fa-chevron-right" /></div>
            </div>
            <hr>
            <div id="logOut" style="margin-top: 0px" @mouseover.stop="onHover('subLogOut')" @mouseleave.stop="outHover()"
              @click.prevent.stop :class="{ subHover: isHover && name === 'subLogOut', isChoose: index === 7 }">
              <div class="icon">
                <!-- <font-awesome-icon icon="fa-solid fa-info" style="margin-left: 5px; color: #f3f5f6" /> -->
              </div>
              <div style="color: red; margin-left: 9px">Đăng xuất</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
  
<script>
export default {
  name: "SidebarNav",
  data() {
    return {
      coordinatesX: 0,
      coordinatesY: 0,
      isHover: false,
      name: "",
      isShowWhenClickAvatar: false,
      popUpName: "",
      index: 0,
      isShowWhenClickTools: false,
      isShowWhenClickSetting: false,
      isShowWhenHoverData: false
    };
  },
  mounted() {
    document.addEventListener("click", this.handleClickOutSide);
  },
  methods: {
    onHover(name) {
      this.isHover = true;
      this.name = name;
      if (name === "subDatabase") this.isShowWhenHoverData = true;
    },
    outHover() {
      this.isHover = false;
      this.isShowWhenHoverData = !this.isOutDiv(document.getElementById('database').getBoundingClientRect(), document.getElementById('subDatabase').getBoundingClientRect());
      this.name = "";
    },
    isOutDiv(coordinates, subCoordinates) {
      console.log(coordinates.top,coordinates.bottom, this.coordinatesY);
      console.log(coordinates.left,coordinates.right,this.coordinatesX);
      if (coordinates.top <= this.coordinatesY && coordinates.bottom >= this.coordinatesY
        && coordinates.left <= this.coordinatesX && subCoordinates.right >= this.coordinatesX) return false;
      return true;
    },
    getMousePosition(event) {
      this.coordinatesX = event.clientX;
      this.coordinatesY = event.clientY;
    },
    isClickAvatar(popUpName) {
      this.isShowWhenClickAvatar = !this.isShowWhenClickAvatar;
      this.popUpName = popUpName;
    },
    onClick(index) {
      this.index = index;
      if (index === 5) this.isShowWhenClickTools = !this.isShowWhenClickTools;
      else if (index === 6) this.isShowWhenClickSetting = !this.isShowWhenClickSetting;
    },
    handleClickOutSide(event) {
      // Lấy tham chiếu (ref) của phần tử bạn muốn kiểm tra
      const elementToCheckAvatar = this.$refs.avatar;
      const elementToCheckMoreMessage = this.$refs.moreMessage;
      const elementToCheckMoreSetting = this.$refs.moreSetting;

      //Kiểm tra xem phần tử mục tiêu của sự kiện click có là con của phần tử bạn muốn kiểm tra hay không
      if (elementToCheckAvatar && !elementToCheckAvatar.contains(event.target) && elementToCheckMoreMessage && !elementToCheckMoreMessage.contains(event.target)
        && elementToCheckMoreSetting && !elementToCheckMoreSetting.contains(event.target)) {
        // Sự kiện click bên ngoài đã xảy ra
        this.isShowWhenClickAvatar = false;
        this.isShowWhenClickTools = false;
        this.isShowWhenClickSetting = false;
        if (this.index === 5 || this.index === 6) this.index = 0;
      }
      else if (elementToCheckAvatar && elementToCheckAvatar.contains(event.target)) {
        this.isShowWhenClickTools = false;
        this.isShowWhenClickSetting = false;
      }
      else if (elementToCheckMoreMessage && elementToCheckMoreMessage.contains(event.target)) {
        this.isShowWhenClickAvatar = false;
        this.isShowWhenClickSetting = false;
      }
      else if (elementToCheckMoreSetting && elementToCheckMoreSetting.contains(event.target)) {
        this.isShowWhenClickAvatar = false;
        this.isShowWhenClickTools = false;
      }
    }
  },
  beforeUnmount() {
    document.removeEventListener("click", this.handleClickOutside);
  },
};
</script>
  
  <!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang = "scss">
.isChoose {
  background-color: #006edc;
}

.hoverProfile {
  background-color: #f3f5f6;
  cursor: pointer;
}

.hover {
  background-color: #006edc;
  cursor: pointer;
}

.subHover {
  background-color: #dfe2e7;
  cursor: pointer;
}

::placeholder {
  //padding-left: 18px;
  font-size: 14px;
}

#sidebarNav {
  height: 99.2vh;
  width: 64px;
  //border-right: 1px solid rgb(160, 160, 160);
  display: inline-block;

  #main-tab {
    display: inline-block;
    background-color: rgb(0, 145, 255);
    width: 100%;
    height: 100%;

    .nav__tabs__zalo {
      height: 100px;
      justify-content: center;
      padding-top: 32px;
      box-sizing: border-box;

      .a-child {
        position: relative;
        cursor: pointer;
        width: 75%;
        height: 75%;
        display: flex;
        justify-content: center;
        align-items: center;
        background: var(--surface-background);
        color: var(--text-secondary);
        overflow: hidden;
        object-fit: contain;
        font-size: 0.75rem;
        font-weight: 500;
        line-height: 1.5;
        border: 1px solid var(--WA100);
        box-sizing: border-box;
        border-radius: 50%;
        margin-left: 7px;
      }

      #infoUser {
        position: relative;
        left: 64px;
        bottom: 50px;
        height: 157px;
        width: 280px;
        background-color: #fff;
        border: 1px solid rgba(0, 0, 0, 0.3);
        border-radius: 6px;
        z-index: 1;
        font-family: SegoeuiPc, Segoe UI, San Francisco, Helvetica Neue, Helvetica, Lucida Grande, Roboto, Ubuntu,
          Tahoma, Microsoft Sans Serif, Arial, sans-serif;

        hr {
          border: none;
          border-bottom: 1px solid #ccc;
          margin-left: 10px;
          margin-right: 10px;
        }

        #userName,
        #yourProfile,
        #setting,
        #signout {
          height: 27px;
          padding-left: 10px;
          padding-top: 5px;
          font-size: 1.05rem;
          margin-bottom: 8px;
        }

        #userName {
          font-weight: bold;
        }

        #yourProfile,
        #setting,
        #signout {
          margin-top: 5px;
          font-size: .9rem;
        }
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

      .nav__tabs_contacts {
        position: relative;

        #moreMessage {
          position: absolute;
          height: 122px;
          width: 312px;
          background-color: white;
          border: 1px solid rgba(0, 0, 0, 0.3);
          border-radius: 6px;

          div {
            display: inline-block;
            //background-color: black;
            padding: 10px 8px;
            width: 95.4px;
            height: 98px;
            margin: 4px;
            text-align: center;
            font-size: 1.5rem;

            div {
              //display: block;
              text-align: center;
              font-size: 0.875rem;
              width: 72px;
              height: 42px;
            }
          }
        }
      }
    }

    .nav__tabs_middle {
      height: 270px;
    }

    .nav__tabs_bottom {
      .nav__tabs_to-do {
        position: relative;

        #moreSetting {
          position: absolute;
          width: 223px;
          height: 287px;
          background-color: #fff;
          bottom: 63px;
          left: 5px;
          border: 1px solid rgba(0, 0, 0, 0.3);
          border-radius: 4px;

          div {
            height: 36px;
            padding-left: 8px;
            padding-right: 8px;
            margin-top: 5px;
            font-size: 0.9rem;

            .icon {
              margin-right: 12px;
            }

            #subDatabase {
              width: 157px;
              height: 53px;
              position: absolute;
              background-color: #fff;
              right: -160px;
              top: 78px;
              border: 1px solid rgba(0, 0, 0, 0.3);
              border-radius: 5px;
              padding: 0px;

              div {
                margin-top: 8px;
                height: 36px;
                width: 101%;
                padding: 7px;

              }
            }

            .arrowRight {
              float: right;
              font-size: 0.9rem;
            }

            div {
              display: inline-block;
              vertical-align: middle;

            }
          }
        }
      }
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
        background-color: #eaedf0;
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
        color: #7589a3;
        padding-left: 16px;
        font-family: SegoeuiPc, Segoe UI, San Francisco, Helvetica Neue,
          Helvetica, Lucida Grande, Roboto, Ubuntu, Tahoma, Microsoft Sans Serif,
          Arial, sans-serif;
        font-size: 0.875rem;
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
            font-size: 0.75rem;
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
            color: #081c36;
            font-size: 16px;
          }

          #chat-content {
            display: block;
            color: #7589a3;
            font-size: 14px;
          }

          #chat-info {
            display: inline-block;
            color: #7589a3;
            font-size: 12px;
            float: right;
          }
        }
      }
    }
  }
}
</style>
  