<template>
  <v-navigation-drawer
    v-model="drawer"
    absolute
    color="grey lighten-3"
    mini-variant
  >
    <v-spacer></v-spacer>
    <v-divider class="mx-3 my-5"></v-divider>

    <v-btn icon x-large to="/">
      <v-icon dark>mdi-billboard</v-icon>
    </v-btn>
    <v-btn icon x-large to="/project">
      <v-icon dark>mdi-clipboard-text-outline</v-icon>
    </v-btn>
    <v-btn icon x-large>
      <v-icon dark>mdi-file-chart-outline</v-icon>
    </v-btn>
    <v-btn icon x-large>
      <v-icon dark>mdi-account</v-icon>
    </v-btn>
    <v-btn icon x-large>
      <v-icon dark>mdi-settings-outline</v-icon>
    </v-btn>
  </v-navigation-drawer>
</template>

<script>

export default {
  name: "Menu",
  components: {},
  data() {
    return {
      drawer: null,
      menuItems: {
        title: "",
        list: [],
      },
    };
  },
};
</script>

<style scoped>
</style>