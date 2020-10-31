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
      <MenuList :menuItems="menuItems" @add-menu-item="newProject" @choose-menu-item="chooseProject" />
    </v-navigation-drawer>
    <v-main>
      <AddForm
        v-if="showAddForm"
        :formTitle="formTitle"
        :item="formItem"
        @save-item="save"
        @cancel-add-item="showAddForm = $event"
      />
      <div v-else>
        <Info :infoItem="selectedProject" @add-item="newTask" @edit-item="editProject" @del-item="delProject" />
        <Tasks
          :project="selectedProject"
          @edit-task="editTask"
          @del-task="delTask"
        />
      </div>
    </v-main>
  </v-app>
</template>

<script>
import serverApi from "../plugins/server-api.js";
import Tasks from "../components/Tasks.vue";
import MenuList from "../components/MenuList.vue";
import Menu from "../components/Menu.vue";
import AddForm from "../components/AddForm.vue";
import Info from "../components/Info.vue";

export default {
  name: "Project",
  components: {
    Tasks,
    MenuList,
    Menu,
    AddForm,
    Info
  },
  mounted() {
    serverApi
      .getProject()
      .then((data) => (this.menuItems.list = data._embedded.project));
    
    this.selectedProject = this.menuItems.list[0];
  },
  data() {
    return {
      drawer: null,
      menuItems: {
        title: "Project",
        list: [
          {
            id: 1,
            title: "Project 1",
            description: "Project 1 description",
          },
          {
            id: 2,
            title: "Project 2",
            description: "Project 2 description",
          },
          {
            id: 3,
            title: "Project 3",
            description: "Project 3 description",
          },
        ],
      },
      showAddForm: false,
      selectedProject: {},
      formItem: {},
      formTitle: "New Project",
    };
  },
  methods: {
    newProject() {
      this.showAddForm = false;
      this.formTitle = "New Project";
      this.formItem = {};
      this.showAddForm = true;
    },
    chooseProject(item) {
      this.showAddForm = false;
      this.selectedProject = item;
    },
    editProject(item) {
      this.formTitle = "Edit Project";
      this.formItem = item;
      this.showAddForm = true;
    },
    saveProject(item) {
      serverApi.saveProject(item).then(console.log("saved"));
      this.showAddForm = false;
    },
    delProject(item) {
      serverApi.delProject(item).then(console.log("deleted"));
      this.showAddForm = false;
    },
    newTask() {
      this.showAddForm = false;
      this.formTitle = "New Task";
      this.formItem = {};
      this.showAddForm = true;
    },
    editTask(item) {
      this.formTitle = "Edit Task";
      this.formItem = item;
      this.showAddForm = true;
    },
    saveTask(item) {
      serverApi.saveTask(item).then(console.log("saved"));
      this.showAddForm = false;
    },
    delTask(item) {
      serverApi.delTask(item).then(console.log("deleted"));
      this.showAddForm = false;
    },
    save(item) {
      if (this.formTitle.includes('Project')) {
        this.saveProject(item);
      } else {
        this.saveTask(item);
      }
    }
  },
};
</script>

<style scoped>
.toolbar-title {
  margin-left: 20px;
}
</style>
