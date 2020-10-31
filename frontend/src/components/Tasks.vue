<template>
  <v-expansion-panels>
    <v-expansion-panel v-for="item in tasks" :key="item.id">
      <v-expansion-panel-header disable-icon-rotate>
        {{ item.title }} of {{ project.title }}
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
import serverApi from '../plugins/server-api.js'

export default {
  name: "Tasks",
  props: {
    project: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      tasks: [
        {
          id: 1,
          title: "Task 1",
          description: "Description Task 1",
        },
        {
          id: 2,
          title: "Task 2",
          description: "Description Task 2",
        },
        {
          id: 3,
          title: "Task 3",
          description: "Description Task 3",
        },
      ],
    };
  },
  mounted() {
    this.getTasks();
  },
  methods: {
    edit(item) {
      this.$emit("edit-task", item);
    },
    del(item) {
      this.$emit("del-task", item);
    },
    getTasks() {
      serverApi
        .getTask(this.project.id)
        .then((data) => (this.projects = data._embedded.task));
    },
  },
  watch: {
    project: function () {
      this.getTasks();
    },
  },
};
</script>

<style scoped>
</style>