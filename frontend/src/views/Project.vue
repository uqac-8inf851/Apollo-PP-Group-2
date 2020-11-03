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
        menuItemLabel="projectTitle"
        menuTitle="Project"
        @choose-menu-item="chooseProject"
      />
    </v-navigation-drawer>
    <v-main>
      <AddForm
        v-if="showAddForm"
        :formTitle="formTitle"
        :item="formItem"
        :itemLabel="formProps"
        @save-item="saveTask"
        @cancel-add-item="showAddForm = $event"
      />
      <div v-if="!showAddForm && selectedProject">
        <Info
          :infoItem="selectedProject"
          :infoItemLabel="formProps"
          :showDeleteBtn="showDeleteBtn"
          :showEditBtn="false"
          @add-item="newTask"
        />
        <Tasks
          :project="selectedProject"
          @edit-task="editTask"
          @show-delete-btn="showDeleteBtn = $event"
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
    Info,
  },
  created() {
    this.getProject();
  },
  data() {
    return {
      drawer: null,
      menuItems: {
        list: [],
      },
      showAddForm: false,
      selectedProject: null,
      formItem: {},
      formTitle: "New Project",
      formProps: [],
      showDeleteBtn: true
    };
  },
  methods: {
    getProject() {
      serverApi.getProject().then((data) => {
        this.menuItems.list = data._embedded.project
      });
    },
    chooseProject(item) {
      this.showAddForm = false;
      this.formProps = ["projectTitle", "projectDescription"],
      this.selectedProject = item;
    },
    async delProject(item) {
      await serverApi.delProject(item);
      this.getProject();
      this.showAddForm = false;
      this.selectedProject = null;
    },
    newTask() {
      this.showAddForm = false;
      this.formTitle = "New Task";
      this.formItem = {};
      this.formProps = ["taskTitle", "taskDescription"],
      this.showAddForm = true;
    },
    editTask(item) {
      this.formTitle = "Edit Task";
      this.formItem = item;
      this.formProps = ["taskTitle", "taskDescription"],
      this.showAddForm = true;
    },
    async saveTask(item) {
      item['project'] = this.selectedProject;
      await serverApi.saveTask(item).then(console.log("saved"));
      this.formProps = ["projectTitle", "projectDescription"],
      this.showAddForm = false;
    },
  },
};
</script>

<style scoped>
.toolbar-title {
  margin-left: 20px;
}
</style>
