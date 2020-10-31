<template>
  <v-expansion-panels>
    <v-expansion-panel v-for="item in projects" :key="item.id">
      <v-expansion-panel-header disable-icon-rotate>
        {{ item.title }} of {{ program.title }}
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
      <v-expansion-panel-content>
        {{ item.description }}
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
      projects: [
        {
          id: 1,
          title: "Project 1",
          description: "Description Project 1",
        },
        {
          id: 2,
          title: "Project 2",
          description: "Description Project 2",
        },
        {
          id: 3,
          title: "Project 3",
          description: "Description Project 3",
        },
      ],
    };
  },
  mounted() {
    this.getProjects();
  },
  methods: {
    edit(item) {
      this.$emit("edit-project", item);
    },
    del(item) {
      this.$emit("del-project", item);
    },
    getProjects() {
      serverApi
        .getProject(this.program.id)
        .then((data) => (this.projects = data._embedded.task));
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
</style>