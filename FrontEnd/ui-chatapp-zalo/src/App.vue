<template >
  <!-- <SidebarNav></SidebarNav> -->
  <!-- <HomeChat></HomeChat> -->
  <ContactNav></ContactNav>
  <!-- <FriendList></FriendList> -->
  <!-- <GroupList></GroupList> -->
  <InvitationFriendManage></InvitationFriendManage>
</template>

<script>
// import SidebarNav from './components/SidebarNav.vue';
import ContactNav from './components/ContactNav.vue';
// import FriendList from './components/FriendList.vue';
// import GroupList from './components/GroupList.vue';
import InvitationFriendManage from './components/InvitationFriendManage.vue';
// import HomeChat from './components/HomeChat.vue';

export default {
  name: 'App',
  components: {
    // SidebarNav,
    // HomeChat
    ContactNav,
    InvitationFriendManage
    // GroupList
    // FriendList
  }
}
</script>

<style>
#app {
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
