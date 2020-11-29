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
      <MenuList
        :menuItems="menuItems"
        :showNewBtn="false"
        menuItemLabel="taskTitle"
        menuTitle="Report"
        @choose-menu-item="chooseTask"
      />
    </v-navigation-drawer>
    <v-main>
      <v-card v-if="selectedTask" elevation="4">
        <v-card-title>{{ selectedTask.taskTitle }}</v-card-title>
        <v-card-text>{{ selectedTask.taskDescription }}</v-card-text>
        <v-divider class="mx-4"></v-divider>
        <Tracks :task="selectedTask" />
      </v-card> 
    </v-main>
  </v-app>
</template>

<script>
import serverApi from "../plugins/server-api.js";
import MenuList from "../components/MenuList.vue";
import Menu from "../components/Menu.vue";
import Tracks from "../components/Tracks.vue";

export default {
  name: "Report",
  components: {
    MenuList,
    Menu,
    Tracks
  },
  created() {
    this.getTask();
  },
  data() {
    return {
      drawer: null,
      menuItems: {
        list: [],
      },
      selectedTask: null,
    };
  },
  methods: {
    getTask() {
      serverApi.getTask().then((data) => {
        this.menuItems.list = data._embedded.task
      });
    },
    chooseTask(item) {
      console.log(item)
      this.selectedTask = item;
    },
  },
};
</script>

<style scoped>
.toolbar-title {
  margin-left: 20px;
}

.v-card {
  height: 100%;
}
</style>
