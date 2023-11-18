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
  methods: {
    updateUser(userData) {
      this.user = userData; // Set user data here, or just set it to null if not available
    },
    updateChosenPage(page) {
      this.chosenPage = page;
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
  height: 100vh;
}

* {
  margin: 0;
  padding: 0;
  border: 0;
  font-size: 100%;
}
.width-100{
  width: 100%;
}
</style>
