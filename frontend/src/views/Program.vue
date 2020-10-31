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
        @add-menu-item="newProgram"
        @choose-menu-item="chooseProgram"
      />
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
        <Info :infoItem="selectedProgram" @add-item="newProject" @edit-item="editProgram" @del-item="delProgram" />
        <Projects
          :program="selectedProgram"
          @edit-project="editProject"
          @del-project="delProject"
        />
      </div>
    </v-main>
  </v-app>
</template>

<script>
import serverApi from "../plugins/server-api.js";
import Projects from "../components/Projects.vue";
import MenuList from "../components/MenuList.vue";
import Menu from "../components/Menu.vue";
import AddForm from "../components/AddForm.vue";
import Info from "../components/Info.vue";

export default {
  name: "Program",
  components: {
    Projects,
    MenuList,
    Menu,
    AddForm,
    Info
  },
  mounted() {
    serverApi
      .getProgram()
      .then((data) => (this.menuItems.list = data._embedded.project));

    this.selectedProgram = this.menuItems.list[0];
  },
  data() {
    return {
      drawer: null,
      menuItems: {
        title: "Program",
        list: [
          {
            id: 1,
            title: "Program 1",
            description: "Program 1 description",
          },
          {
            id: 2,
            title: "Program 2",
            description: "Program 2 description",
          },
          {
            id: 3,
            title: "Program 3",
            description: "Program 3 description",
          },
        ],
      },
      showAddForm: false,
      selectedProgram: {},
      formItem: {},
      formTitle: "New Program",
    };
  },
  methods: {
    newProgram() {
      this.showAddForm = false;
      this.formTitle = "New Program";
      this.formItem = {};
      this.showAddForm = true;
    },
    chooseProgram(item) {
      this.showAddForm = false;
      this.selectedProgram = item;
    },
    editProgram(item) {
      this.formTitle = "Edit Program";
      this.formItem = item;
      this.showAddForm = true;
    },
    saveProgram(item) {
      serverApi.saveProgram(item).then(console.log("saved"));
      this.showAddForm = false;
    },
    delProgram(item) {
      serverApi.delProgram(item).then(console.log("deleted"));
      this.showAddForm = false;
    },
    newProject() {
      this.showAddForm = false;
      this.formTitle = "New Project";
      this.formItem = {};
      this.showAddForm = true;
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
    save(item) {
      if (this.formTitle.includes('Program')) {
        this.saveProgram(item);
      } else {
        this.saveProject(item);
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
