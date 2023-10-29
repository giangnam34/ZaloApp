<template>
  <div id="app">
    <!-- <ContactNav></ContactNav> -->
    <SignIn></SignIn>
  </div>
</template>

<script>
// import ContactNav from './components/ContactNav.vue';
import SignIn from './components/SignIn.vue';
export default {
  name: 'App',
  components: {
    // ContactNav,
    SignIn,
  },
}
</script>

<style>
#app {
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
