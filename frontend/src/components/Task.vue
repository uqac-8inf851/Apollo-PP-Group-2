<template>
    <v-main>
      <v-expansion-panels>
        <v-expansion-panel v-for="item in tasks" :key="item.id">
          <v-expansion-panel-header>
            {{ item.title }}
            <template v-slot:actions>
              <v-icon></v-icon>
            </template>
          </v-expansion-panel-header>
          <v-expansion-panel-content>
            {{ item.description }}
          </v-expansion-panel-content>
        </v-expansion-panel>
      </v-expansion-panels>
    </v-main>
</template>

<script>
import serverApi from '../plugins/server-api.js'

export default {
  name: 'Task',
  components: {

  },
  data() {
    return {
      tasks: []
    };
  },
  mounted() {
    serverApi.getTask().then(data => (
      this.tasks = data._embedded.task
    ));
  }
};
</script>

<style scoped>

</style>