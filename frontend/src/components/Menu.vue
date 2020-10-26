<template>
  <v-navigation-drawer
    v-model="drawer"
    absolute
    color="grey lighten-3"
    mini-variant
  >
    <v-spacer></v-spacer>
    <v-divider class="mx-3 my-5"></v-divider>

    <v-avatar
      class="d-block text-center mx-auto mb-9"
      color="grey lighten-1"
      size="28"
    >
      <v-icon dark>mdi-billboard</v-icon>
    </v-avatar>
    <v-avatar
      class="d-block text-center mx-auto mb-9"
      color="grey lighten-1"
      size="28"
    >
      <v-icon dark>mdi-clipboard-text-outline</v-icon>
    </v-avatar>
    <v-avatar
      class="d-block text-center mx-auto mb-9"
      color="grey lighten-1"
      size="28"
    >
      <v-icon dark>mdi-file-chart-outline</v-icon>
    </v-avatar>
    <v-avatar
      class="d-block text-center mx-auto mb-9"
      color="grey lighten-1"
      size="28"
    >
      <v-icon dark>mdi-account</v-icon>
    </v-avatar>
    <v-avatar
      class="d-block text-center mx-auto mb-9"
      color="grey lighten-1"
      size="28"
    >
      <v-icon dark>mdi-settings-outline</v-icon>
    </v-avatar>
  </v-navigation-drawer>
</template>

<script>
export default {
  name: 'Menu',
  components: {

  },
  data() {
    return {
      drawer: null
    };
  }
};
</script>

<style scoped>

</style>