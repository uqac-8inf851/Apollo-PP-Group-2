<template>
  <v-expansion-panels focusable inset>
    <v-expansion-panel v-for="item in tasks" :key="item.id">
      <v-expansion-panel-header disable-icon-rotate>
        {{ item.taskTitle }} of {{ project.projectTitle }}
        <v-spacer></v-spacer>
        <div class="text-end">
          <v-icon @click.native.stop="edit(item)">mdi-file-document-edit-outline</v-icon>
          <v-icon class="trash" @click.native.stop="del(item)">mdi-trash-can-outline</v-icon>
        </div>
        <template v-slot:actions>
          <v-icon expand-icon></v-icon>
        </template>
      </v-expansion-panel-header>
      <v-expansion-panel-content class="panel-content">
        {{ item.taskDescription }}
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
      tasks: [],
    };
  },
  mounted() {
    this.getTasks();
  },
  methods: {
    edit(item) {
      this.$emit("edit-task", item);
    },
    async del(item) {
      await serverApi.delTask(item);
      this.getTasks();
    },
    showDeleteBtn() {
      let show = (this.tasks.length == 0);
      this.$emit("show-delete-btn", show);
    },
    async getTasks() {
      await serverApi
        .getTaskByProject(this.project)
        .then(data => {
          this.tasks = data._embedded.task;
          this.showDeleteBtn();
        });
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
.trash:hover {
  color: red;
}

.panel-content {
  padding-top: 15px;
}
</style>