<template>
  <div v-if="loading" class="width-100">
  </div>
  <div v-else-if="!userIsValid" class="width-100">
    <SignIn @userLoggedIn="updateJWT"></SignIn>
  </div>
  <div v-else class="width-100" style="display: flex;">
    <MainSidebarNav @pageSelected="updateChosenPage" @userLoggedIn="updateJWT"></MainSidebarNav>
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
    <div v-if="chosenPage === 10" class="width-100 height-100">
      <PostPage></PostPage>
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
import PostPage from './components/PostPage.vue';
import VueJwtDecode from 'vue-jwt-decode';
import axios from 'axios';
export default {
  name: 'App',
  data() {
    return {
      loading: true,
      chosenPage: 10,
      userIsValid: false,
      jwt: '',
    }
  },
  components: {
    MainSidebarNav,
    HomeChat,
    ChatSidebarNav,
    SignIn,
    ContactNav,
    ToDo, 
    PostPage
  },
  async mounted() {
    await this.checkToken();
    this.loading = false;
    console.log(this.userIsValid);
  },
  methods: {
    updateJWT(userData) {
      this.jwt = userData; // Set user data here, or just set it to null if not available
      this.checkToken();
    },
    updateChosenPage(page) {
      this.chosenPage = page;
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

            const responseUser = await axios.get(`users/${currentUserID}`, {
              headers: {
                'Authorization': localStorage.getItem("token")
              }
            });
            const user = responseUser.data;

            localStorage.setItem('user', JSON.stringify(user));

            if (Date.now() >= decoded.exp * 1000) {
              this.userIsValid = false;
            } else {
              this.userIsValid = true;
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
