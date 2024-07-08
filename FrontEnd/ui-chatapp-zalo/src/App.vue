<template>
  <div v-if="loading" class="width-100">
  </div>
  <div v-else-if="!userIsValid" class="width-100">
    <SignIn @userLoggedIn="updateJWT"></SignIn>
  </div>
  <div v-else class="width-100" style="display: flex;">
    <div v-if="isAdmin" class="width-100" style="display: flex;">
      <AdminPage @userLoggedIn="updateJWT"></AdminPage>
    </div>
    <div v-else class="width-100" style="display: flex;">
      <MainSidebarNav @pageSelected="updateChosenPage" @userLoggedIn="updateJWT"></MainSidebarNav>
      <div v-if="chosenPage === 1" class="width-100">
        <!-- <ChatSidebarNav></ChatSidebarNav>
      <HomeChat></HomeChat> -->
        <TestChat></TestChat>
      </div>
      <div v-if="chosenPage === 2" class="width-100">
        <ContactNav></ContactNav>
      </div>
      <div v-if="chosenPage === 3" class="width-100">
        <ToDo></ToDo>
      </div>
      <div v-if="chosenPage === 4" class="width-100">
        <ChatSidebarNav></ChatSidebarNav>
        <HomeChat></HomeChat>
      </div>
      <div v-if="chosenPage === 5" class="width-100">
        <ChatSidebarNav></ChatSidebarNav>
        <HomeChat></HomeChat>
      </div>
      <div v-if="chosenPage === 6" class="width-100">
        <ChatSidebarNav></ChatSidebarNav>
        <HomeChat></HomeChat>
      </div>
      <div v-if="chosenPage === 10" class="width-100 height-100">
        <PostPage></PostPage>
      </div>
    </div>
  </div>
</template>

<script>
import MainSidebarNav from './components/MainSidebarNav.vue';
import HomeChat from './components/HomeChat.vue'
import ChatSidebarNav from './components/ChatSidebarNav.vue';
import ContactNav from './components/ContactNav.vue';
import ToDo from './components/ToDo.vue';
import TestChat from './components/ChatComponent.vue';
import SignIn from './components/SignIn.vue';
import PostPage from './components/PostPage.vue';
import AdminPage from './components/AdminPage.vue';
import VueJwtDecode from 'vue-jwt-decode';
import axios from 'axios';
import SockJS from 'sockjs-client';
import Stomp from "webstomp-client";
import { onUnmounted } from 'vue';
export default {
  name: 'App',
  data() {
    return {
      loading: true,
      chosenPage: 1,
      userIsValid: false,
      currentUserID: null,
      jwt: '',
      isAdmin: false,
    }
  },
  components: {
    MainSidebarNav,
    HomeChat,
    ChatSidebarNav,
    SignIn,
    TestChat,
    ContactNav,
    ToDo,
    PostPage,
    AdminPage
  },
  async mounted() {
    await this.checkToken();
    this.loading = false;
    console.log(this.userIsValid);
    window.addEventListener("beforeunload", this.updateUserOfflineActivity);
  },
  beforeUnmount() {
    window.removeEventListener('beforeunload', this.updateUserOfflineActivity);
  },
  methods: {
    updateJWT(userData) {
      this.jwt = userData;
      this.checkToken();
    },
    updateChosenPage(page) {
      this.chosenPage = page;
    },

    async subcribeTopicWebSocket() {
      console.log("Call function subcribeWebsocket");
      var socket = new SockJS('http://localhost:8181/chat/');
      var stompClient = Stomp.over(socket);
      stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/messages', function (messageOutput) {
          console.log("Message");
          console.log(messageOutput);
        });
        stompClient.send("/app/chat", "Hehe");
      });
    },

    async subcribeSpecificUserWebSocket() {
      var socket = new SockJS('http://localhost:8181/room');
      var stompClient = Stomp.over(socket);
      var sessionId = "";
      var userId = JSON.parse(localStorage.getItem('user'))['id'];
      console.log(userId);

      stompClient.connect({ userId: 'user' + userId }, function () {
        var url = stompClient.ws._transport.url;
        console.log(url);
        url = url.replace(
          "ws://localhost:8181/room/", "");
        url = url.replace("/websocket", "");
        url = url.replace(/^[0-9]+\//, "");
        console.log("Your current session is: " + url);
        sessionId = url;
        console.log(sessionId);
        stompClient.subscribe('/user/topic/specific-user'
          , function (message) {
            //handle messages
            console.log("Message");
            console.log(message);
          }
        )
        stompClient.send("/app/room", "Hehehe", { userId: 'user' + 2 });

      }
      )
    },

    async checkToken() {
      const fullToken = localStorage.getItem('token');

      if (fullToken) {
        const parts = fullToken.split(' ');
        if (parts.length > 1) {
          const token = parts[1];

          try {
            let decoded = VueJwtDecode.decode(token);

            const currentUserID = decoded.sub;

            this.currentUserID = currentUserID;

            console.log(decoded);

            const responseUser = await axios.get(`users/${currentUserID}`, {
              headers: {
                'Authorization': localStorage.getItem("token")
              }
            });
            const user = responseUser.data;

            console.log("Response");
            console.log(responseUser.status);

            localStorage.setItem('user', JSON.stringify(user));

            if (Date.now() >= decoded.exp * 1000) {
              this.userIsValid = false;
            } else {
              this.userIsValid = true;
              this.isAdmin = user.roles.includes('ROLE_ADMIN');
              // this.subcribeTopicWebSocket();
              // this.subcribeSpecificUserWebSocket();
            }
            localStorage.setItem("isValid", this.userIsValid);
          } catch (error) {
            console.error('Lỗi khi giải mã JWT:', error);
            localStorage.setItem("isValid", false);
            this.userIsValid = false;
          }
        } else {
          console.error('Token không hợp lệ.');
          localStorage.setItem("isValid", false);
          this.userIsValid = false;
        }
      } else {
        localStorage.setItem("isValid", false);
        this.userIsValid = false;
      }

    },

    async updateUserOfflineActivity(){
      const responseUser = await axios.get(`users/update-user-offline-activity/${this.currentUserID}`, {
              headers: {
                'Authorization': localStorage.getItem("token")
              }
            });
      console.log(responseUser.status);
    }
  },
}
</script>

<style>
#app {
  display: block;
  width: 100%;
  font-family: 'Segoe UI', Avenir, Helvetica, Arial, sans-serif;
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  display: flex;
  min-height: 100vh;
}

* {
  margin: 0;
  padding: 0;
  border: 0;
  font-size: 100%;
}

.width-100 {
  width: 100%;
}

.height-100 {
  height: 100%;
}
</style>
