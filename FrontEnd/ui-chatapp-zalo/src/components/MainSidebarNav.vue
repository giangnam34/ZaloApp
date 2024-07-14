<template>
  <div id="sidebarNav" @mousemove="getMousePosition">
    <div id="main-tab">
      <div class="nav__tabs__zalo web" :title="user.fullName" ref="avatar" @click="isClickAvatar('avatar')">
        <div class="avatar-container">
          <img :src="user.imageAvatarUrl" class="a-child avatar-wrapper" />
        </div>
        <div id="infoUser" v-show="isShowWhenClickAvatar">
          <div id="userName">{{ user.fullName }}</div>
          <hr>
          <div id="yourProfile" @mouseover="onHover('yourProfile')" @mouseleave="outHover()"
            :class="{ hoverProfile: isHover && name === 'yourProfile' }" @click="emitOpenDialogEvent">Hồ sơ của bạn
          </div>
          <div id="signout" @mouseover="onHover('signout')" @mouseleave="outHover()"
            :class="{ hoverProfile: isHover && name === 'signout' }" @click="logout()">Đăng xuất</div>
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
        <div class="nav__tabs_posts" title="Bài đăng" @mouseover="onHover('posts')" @mouseleave="outHover()"
          @click="onClick(3)" :class="{ hover: isHover && name === 'posts', isChoose: index === 3 }">
          <a id="post-icon">
            <font-awesome-icon icon="fa-solid fa-newspaper" style="color: #ffffff" />
          </a>
        </div>
      </div>
      <div class="nav__tabs_middle"></div>
      <div class="nav__tabs_bottom">
        <UserInfo v-model:showPopup="showPopup" v-model="user" @update="handleUserUpdate"></UserInfo>
      </div>
    </div>
  </div>
</template>

<script>
import UserInfo from './UserInfo.vue';
import VueJwtDecode from 'vue-jwt-decode';
import axios from 'axios';
export default {
  components: {
    UserInfo
  },
  name: "SidebarNav",
  data() {
    return {
      coordinatesX: 0,
      coordinatesY: 0,
      isHover: false,
      name: "",
      isShowWhenClickAvatar: false,
      popUpName: "",
      index: 1,
      user: null,
      showPopup: false,
    };
  },
  emits: ['userLoggedIn'],
  created() {
    // Lấy user từ localStorage
    const userString = localStorage.getItem('user');

    // Chuyển đổi chuỗi JSON thành đối tượng JavaScript
    if (userString) {
      this.user = JSON.parse(userString);
    }
  },
  // watch: {
  //   'user.imageAvatarUrl': {
  //     handler: 'fetchAvatar',
  //   },
  // },
  mounted() {
    this.fetchAvatar();
  },
  methods: {
    handleUserUpdate() {
      // Xử lý khi thông tin user thay đổi từ UserInfo
      this.fetchAvatar();
    },
    emitOpenDialogEvent() {
      this.showPopup = true;
      const userString = localStorage.getItem('user');

      // Chuyển đổi chuỗi JSON thành đối tượng JavaScript
      if (userString) {
        this.user = JSON.parse(userString);
      }
    },
    onHover(name) {
      this.isHover = true;
      this.name = name;
    },
    outHover() {
      this.isHover = false;
      this.name = "";
    },
    isOutDiv(coordinates, subCoordinates) {
      // console.log(coordinates.top, coordinates.bottom, this.coordinatesY);
      // console.log(coordinates.left, coordinates.right, this.coordinatesX);
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
      this.$emit('pageSelected', index);
    },
    async logout() {
      await this.updateUserOfflineActivity();
      localStorage.removeItem("token");
      localStorage.setItem("isValid", false);
      localStorage.removeItem("user");
      this.$emit("userLoggedIn", '');
      event.preventDefault();
      event.stopPropagation();
    },
    async updateUserOfflineActivity() {
      const fullToken = localStorage.getItem('token');
      if (fullToken) {
        const parts = fullToken.split(' ');
        if (parts.length > 1) {
          const token = parts[1];
          let decoded = VueJwtDecode.decode(token);
          const currentUserID = decoded.sub;
          const responseUser = await axios.get(`users/update-user-offline-activity/${currentUserID}`, {
            headers: {
              'Authorization': localStorage.getItem("token")
            }
          });
          console.log(responseUser.status);
        }
      }
    },
    fetchAvatar() {
      axios.get(`users/imageAvatar`, {
        headers: {
          'Authorization': localStorage.getItem("token"),
        },
        responseType: 'blob',
      }).then(response => {
        if (this.user.imageAvatarUrl) {
          URL.revokeObjectURL(this.user.imageAvatarUrl);
        }
        this.user.imageAvatarUrl = URL.createObjectURL(response.data);
      }).catch(error => {
        console.error('Error fetching avatar:', error);
      });
      //console.log('user.imageAvatarUrl changed:', this.user.imageAvatarUrl);
    },
  },
  beforeUnmount() {
    document.removeEventListener("click", this.handleClickOutside);
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
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
  height: 100vh;
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
      padding-top: 4px;
      box-sizing: border-box;
      padding-left: 5px;

      .avatar-container {
        height: 100%;
        display: flex;
        align-items: center;

        img {
          width: 100%;
          height: 100%;
          object-fit: cover;
        }

        .a-child {
          position: relative;
          cursor: pointer;
          background: var(--surface-background);
          color: var(--text-secondary);
          overflow: hidden;
        }

        .avatar-wrapper {
          width: 50px;
          height: 50px;
          display: flex;
          justify-content: center;
          align-items: center;
          overflow: hidden;
          border-radius: 50%;
        }
      }

      #infoUser {
        position: absolute;
        top: 52px; // Thay đổi giá trị nếu cần
        left: 64px; // Thay đổi giá trị nếu cần
        bottom: 50px;
        height: 120px;
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
      .nav__tabs_to-do,
      .nav__tabs_posts {
        height: 64px;
        width: 100%;
        display: table;

        #chat-message-icon,
        #address-icon,
        #to-do-icon,
        #post-icon {
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
      height: 188px;
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
}
</style>