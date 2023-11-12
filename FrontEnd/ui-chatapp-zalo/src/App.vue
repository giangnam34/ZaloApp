<template>
  <SidebarNav></SidebarNav>
  <ChatSidebarNav></ChatSidebarNav>
  <HomeChat></HomeChat>
</template>

<script>
import SidebarNav from './components/MainSidebarNav.vue';
import HomeChat from './components/HomeChat.vue'
import ChatSidebarNav from './components/ChatSidebarNav.vue';
export default {
  name: 'App',
  components: {
    SidebarNav,
    HomeChat,
    ChatSidebarNav
  }
}
</script>

<style>
#app {
  display: block;
  width: 100%;
  font-family: 'Segoe UI',Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}

* {
  margin: 0;
  padding: 0;
  border: 0;
  font-size: 100%;
}
</style>
