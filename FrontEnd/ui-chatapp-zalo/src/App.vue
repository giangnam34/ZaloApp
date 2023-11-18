<template>
  <div div v-if="!user" class="width-100">
    <SignIn @userLoggedIn="updateUser"></SignIn>
  </div>
  <div div v-if="user" class="width-100" style="display: flex;">
    <MainSidebarNav @pageSelected="updateChosenPage" :user="user"></MainSidebarNav>
    <div v-if="chosenPage === 1" class="width-100">
      <ChatSidebarNav></ChatSidebarNav>
      <HomeChat></HomeChat>
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
  </div>
</template>

<script>
import MainSidebarNav from './components/MainSidebarNav.vue';
import HomeChat from './components/HomeChat.vue'
import ChatSidebarNav from './components/ChatSidebarNav.vue';
import ContactNav from './components/ContactNav.vue';
import ToDo from './components/ToDo.vue';
import SignIn from './components/SignIn.vue';
import VueJwtDecode from 'vue-jwt-decode';
import axios from 'axios';
export default {
  name: 'App',
  data() {
    return {
      user: null,
      chosenPage: 1,
    }
  },
  components: {
    MainSidebarNav,
    HomeChat,
    ChatSidebarNav,
    SignIn,
    ContactNav,
    ToDo
  },
  mounted() {
    this.checkToken();
  },
  methods: {
    updateUser(userData) {
      this.user = userData; // Set user data here, or just set it to null if not available
    },
    updateChosenPage(page) {
      this.chosenPage = page;
    },
    checkToken() {

      if(!this.user){
        return false;
      }

      const fullToken = localStorage.getItem("token");

      const parts = fullToken.split(' ');
      if (parts.length > 1) {
        const token = parts[1];

        try {
          let decoded = VueJwtDecode.decode(token)

          const currentUserID = decoded.sub;

          // Lấy thông tin user từ response.data và emit sự kiện userLoggedIn
          const responseUser = axios.get(`users/${currentUserID}`);

          const user = responseUser.data;

          this.user = user;

          if (Date.now() >= decoded.exp * 1000) {
            return false;
          }
          return true;
        } catch (error) {
          console.error('Lỗi khi giải mã JWT:', error);
        }
      } else {
        console.error('Token không hợp lệ.');
      }
    }
  },
}
</script>

<style>
#app {
  display: block;
  width: 100%;
  font-family: 'Segoe UI', Avenir, Helvetica, Arial, sans-serif;
  width: 100%;
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  display: flex;
  height: 100vh;
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
</style>
