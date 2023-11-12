<template>
  <!-- <SidebarNav></SidebarNav>
  <ChatSidebarNav></ChatSidebarNav>
  <HomeChat></HomeChat> -->
  <SignIn></SignIn>
  
</template>

<script>
// import SidebarNav from './components/MainSidebarNav.vue';
// import HomeChat from './components/HomeChat.vue'
// import ChatSidebarNav from './components/ChatSidebarNav.vue';
import SignIn from './components/SignIn.vue';
export default {
  name: 'App',
  components: {
    // SidebarNav,
    // HomeChat,
    // ChatSidebarNav,
    SignIn
  }
}
</script>

<style>
#app {
  display: block;
  width: 100%;
  font-family: 'Segoe UI',Avenir, Helvetica, Arial, sans-serif;
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
</style>
