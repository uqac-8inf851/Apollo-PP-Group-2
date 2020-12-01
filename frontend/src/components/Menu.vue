<template>
  <v-navigation-drawer
    v-model="drawer"
    absolute
    color="grey lighten-3"
    mini-variant
  >
    <v-spacer></v-spacer>
    <v-divider class="mx-3 my-5"></v-divider>

    <v-tooltip right>
      <template v-slot:activator="{ on, attrs }">
        <v-btn
          icon x-large
          v-bind="attrs"
          v-on="on"
          to="/"
        >
          <v-icon dark>mdi-billboard</v-icon>
        </v-btn>
      </template>
      <span>Program</span>
    </v-tooltip>
    <v-tooltip right>
      <template v-slot:activator="{ on, attrs }">
        <v-btn
          icon x-large
          v-bind="attrs"
          v-on="on"
          to="/project"
        >
          <v-icon dark>mdi-clipboard-text-outline</v-icon>
        </v-btn>
      </template>
      <span>Project</span>
    </v-tooltip>
    <v-tooltip right>
      <template v-slot:activator="{ on, attrs }">
        <v-btn
          icon x-large
          v-bind="attrs"
          v-on="on"
          to="/report"
        >
          <v-icon dark>mdi-file-chart-outline</v-icon>
        </v-btn>
      </template>
      <span>Report</span>
    </v-tooltip>
    <v-tooltip right>
      <template v-slot:activator="{ on, attrs }">
        <v-btn
          icon x-large
          v-bind="attrs"
          v-on="on"
        >
          <v-icon dark>mdi-account</v-icon>
        </v-btn>
      </template>
      <span>User</span>
    </v-tooltip>
    <v-tooltip right>
      <template v-slot:activator="{ on, attrs }">
        <v-btn
          icon x-large
          v-bind="attrs"
          v-on="on"
        >
          <v-icon dark>mdi-settings-outline</v-icon>
        </v-btn>
      </template>
      <span>Settings</span>
    </v-tooltip>
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