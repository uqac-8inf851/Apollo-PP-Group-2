<template>
  <v-expansion-panels focusable inset>
    <v-expansion-panel v-for="item in projects" :key="item.id">
      <v-expansion-panel-header disable-icon-rotate>
        {{ item.projectTitle }} of {{ program.programTitle }}
        <v-spacer></v-spacer>
        <div class="text-end">
          <v-icon @click.native.stop="edit(item)"
            >mdi-file-document-edit-outline</v-icon
          >
          <v-icon class="trash" @click.native.stop="del(item)"
            >mdi-trash-can-outline</v-icon
          >
        </div>
        <template v-slot:actions>
          <v-icon expand-icon></v-icon>
        </template>
      </v-expansion-panel-header>
      <v-expansion-panel-content class="panel-content">
        {{ item.projectDescription }}
      </v-expansion-panel-content>
    </v-expansion-panel>
  </v-expansion-panels>
</template>

<script>
import serverApi from "../plugins/server-api.js";

export default {
  name: "Projects",
  props: {
    program: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      projects: [],
    };
  },
  mounted() {
    this.getProjects();
  },
  methods: {
    edit(item) {
      this.$emit("edit-project", item);
    },
    async del(item) {
      await serverApi.delProject(item);
      this.getProjects();
    },
    showDeleteBtn() {
      let show = (this.projects.length == 0);
      this.$emit("show-delete-btn", show);
    },
    async getProjects() {
      await serverApi
        .getProjectByProgram(this.program)
        .then(data => {
          this.projects = data._embedded.project;
          this.showDeleteBtn();
        });
    },
  },
  watch: {
    program: function () {
      this.getProjects();
    },
  },
};
</script>

<style scoped>
.trash:hover {
  color: red;
}

.panel-content {
  padding-top: 15px;
}
</style>