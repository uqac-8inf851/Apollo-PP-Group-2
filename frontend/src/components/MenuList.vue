<template>
  <v-list class="pl-14" shaped>
    <v-btn
      v-if="showNewBtn"
      block
      color="light-green darken-2"
      text
      elevation="2"
      @click="addMenu"
    >New {{ menuTitle }}</v-btn>
    <v-btn
      :disabled="true"
      v-else
      block
      text
      elevation="2"
    >{{ menuTitle }}</v-btn>
    <v-list-item v-for="item in menuItems.list" :key="item.id" link>
      <v-list-item-content>
        <v-list-item-title @click="chooseMenu(item)">{{ item[menuItemLabel] }}</v-list-item-title>
      </v-list-item-content>
    </v-list-item>
  </v-list>
</template>

<script>

export default {
  name: 'MenuList',
  props: {
    menuTitle: {
      type: String,
      required: true
    },
    menuItemLabel: {
      type: String,
      required: true
    },
    menuItems: {
      type: Object,
      required: true,
    },
    showNewBtn: {
      type: Boolean,
      default: true
    }
  },
  methods: {
    addMenu() {
      this.$emit("add-menu-item", true);
    },
    chooseMenu(item) {
      this.$emit("choose-menu-item", item);
    }
  }
};
</script>

<style scoped>
</style>
