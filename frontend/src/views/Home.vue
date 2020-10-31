<template>
  <v-app id="inspire">
    <v-system-bar app height="50" color="light-green darken-2">
      <v-icon large color="white">mdi-flask</v-icon>
      <v-toolbar-title class="toolbar-title">Apollo Institute</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-icon color="light-green lighten-5">mdi-account-circle</v-icon>
    </v-system-bar>

    <v-navigation-drawer v-model="drawer" app width="300">
      <Menu />
      <Project />
    </v-navigation-drawer>

    <Task />
  </v-app>
</template>

<script>
import Task from '../components/Task.vue'
import Project from '../components/Project.vue'
import Menu from '../components/Menu.vue'

export default {
  name: 'Home',
  components: {
    Task,
    Project,
    Menu
  },
  data() {
    return {
      drawer: null
    };
  },
  mounted() {
  }
};
</script>

<style scoped>
.toolbar-title {
  margin-left: 20px;
}
</style>
