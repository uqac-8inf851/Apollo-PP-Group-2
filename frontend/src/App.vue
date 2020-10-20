<template>
  <v-app id="inspire">
    <v-system-bar app height="50" color="light-green darken-2">
      <v-avatar
          class="d-block text-center mx-auto "
          color="light-green lighten-5"
          size="48"
      >
        <v-icon x-large>mdi-flask</v-icon>FP Institute
      </v-avatar>
      <v-toolbar-title>Apollo Institute</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-icon color="light-green lighten-5">mdi-account-circle</v-icon>
    </v-system-bar>

    <v-navigation-drawer v-model="drawer" app width="300">
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

      <v-list class="pl-14" shaped>
        <v-btn
          block
          color="light-green darken-2"
          text
          elevation="2"
        >New Project</v-btn>
        <v-list-item v-for="n in 5" :key="n" link>
          <v-list-item-content>
            <v-list-item-title>Project {{ n }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>

    <v-main>
      <v-expansion-panels>
        <v-expansion-panel v-for="(item, i) in 5" :key="i">
          <v-expansion-panel-header>
            Task {{ i+1 }} of Project 1
            <template v-slot:actions>
              <v-icon></v-icon>
            </template>
          </v-expansion-panel-header>
          <v-expansion-panel-content>
            Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do
            eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim
            ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut
            aliquip ex ea commodo consequat.
          </v-expansion-panel-content>
        </v-expansion-panel>
      </v-expansion-panels>
    </v-main>
  </v-app>
</template>

<script>
export default {
  data: () => ({ drawer: null }),
};
</script>

<style>
</style>
