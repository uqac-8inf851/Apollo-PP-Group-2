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
        menuItemLabel="programTitle"
        menuTitle="Program"
        @add-menu-item="newProgram"
        @choose-menu-item="chooseProgram"
      />
    </v-navigation-drawer>
    <v-main>
      <AddForm
        v-if="showAddForm"
        :formTitle="formTitle"
        :item="formItem"
        :itemLabel="formProps"
        @save-item="save"
        @cancel-add-item="showAddForm = $event"
      />
      <div v-if="!showAddForm && selectedProgram">
        <Info
          :infoItem="selectedProgram"
          :infoItemLabel="formProps"
          :showDeleteBtn="showDeleteBtn"
          @add-item="newProject"
          @edit-item="editProgram"
          @del-item="delProgram"
        />
        <Projects
          :program="selectedProgram"
          @edit-project="editProject"
          @show-delete-btn="showDeleteBtn = $event"
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
    Info,
  },
  created() {
    this.getProgram();
  },
  data() {
    return {
      drawer: null,
      menuItems: {
        list: [],
      },
      showAddForm: false,
      selectedProgram: null,
      formItem: {},
      formTitle: "New Program",
      formProps: [],
      showDeleteBtn: true
    };
  },
  methods: {
    getProgram() {
      serverApi.getProgram().then((data) => {
        this.menuItems.list = data._embedded.program;
      });
    },
    newProgram() {
      this.showAddForm = false;
      this.formTitle = "New Program";
      this.formItem = {};
      this.formProps = ["programTitle", "programDescription"],
      this.showAddForm = true;
    },
    chooseProgram(item) {
      this.showAddForm = false;
      this.formProps = ["programTitle", "programDescription"],
      this.selectedProgram = item;
    },
    editProgram(item) {
      this.formTitle = "Edit Program";
      this.formItem = item;
      this.showAddForm = true;
    },
    async saveProgram(item) {
      await serverApi.saveProgram(item).then(data => this.selectedProgram = data);
      this.getProgram();
      this.showAddForm = false;
    },
    async delProgram(item) {
      await serverApi.delProgram(item);
      this.getProgram();
      this.showAddForm = false;
      this.selectedProgram = null
    },
    newProject() {
      this.showAddForm = false;
      this.formTitle = "New Project";
      this.formItem = {};
      this.formProps = ["projectTitle", "projectDescription"],
      this.showAddForm = true;
    },
    editProject(item) {
      this.formTitle = "Edit Project";
      this.formItem = item;
      this.formProps = ["projectTitle", "projectDescription"],
      this.showAddForm = true;
    },
    async saveProject(item) {
      item['program'] = this.selectedProgram;
      await serverApi.saveProject(item).then(console.log("saved"));
      this.formProps = ["programTitle", "programDescription"],
      this.showAddForm = false;
    },
    save(item) {
      if (this.formTitle.includes("Program")) {
        this.saveProgram(item);
      } else {
        this.saveProject(item);
      }
    },
  },
};
</script>

<style scoped>
.toolbar-title {
  margin-left: 20px;
}
</style>
