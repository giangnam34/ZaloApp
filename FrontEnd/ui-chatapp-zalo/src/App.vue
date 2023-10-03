<template>
  <SidebarNav></SidebarNav>
  <HomeChat></HomeChat>
</template>

<script>
import SidebarNav from './components/SidebarNav.vue';
import HomeChat from './components/HomeChat.vue'

export default {
  name: 'App',
  components: {
    SidebarNav,
    HomeChat
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
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
