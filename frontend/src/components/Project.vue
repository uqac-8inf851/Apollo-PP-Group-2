<template>
  <v-list class="pl-14" shaped>
    <v-btn
      block
      color="light-green darken-2"
      text
      elevation="2"
    >New Project</v-btn>
    <v-list-item v-for="item in projects" :key="item.id" link>
      <v-list-item-content>
        <v-list-item-title>{{ item.title }}</v-list-item-title>
      </v-list-item-content>
    </v-list-item>
  </v-list>
</template>

<script>
import serverApi from '../plugins/server-api.js'

export default {
  name: 'Project',
  components: {

  },
  data() {
    return {
      projects: []
    };
  },
  mounted() {
    serverApi.getProject().then(data => (
      this.projects = data._embedded.project
    ));
  }
};
</script>

<style scoped>
</style>
