<template>
  <v-card elevation="4">
    <v-card-title>{{ infoItem[infoItemLabel[0]] }}</v-card-title>
    <v-card-text>{{ infoItem[infoItemLabel[1]] }}</v-card-text>
    <v-card-actions>
      <v-tooltip bottom>
        <template v-slot:activator="{ on, attrs }">
          <v-btn color="grey" text v-bind="attrs" v-on="on" @click="add">
            <v-icon>mdi-clipboard-plus-outline</v-icon>
          </v-btn>
        </template>
        <span>New</span>
      </v-tooltip>
      <v-tooltip bottom>
        <template v-slot:activator="{ on, attrs }">
          <v-btn
            color="grey"
            text
            v-show="showEditBtn"
            v-bind="attrs"
            v-on="on"
            @click="edit(infoItem)"
          >
            <v-icon>mdi-file-document-edit-outline</v-icon>
          </v-btn>
        </template>
        <span>Edit</span>
      </v-tooltip>
      <v-tooltip bottom>
        <template v-slot:activator="{ on, attrs }">
          <v-btn
            color="grey"
            text
            v-show="showDeleteBtn"
            v-bind="attrs"
            v-on="on"
            @click="del(infoItem)"
          >
            <v-icon>mdi-trash-can-outline</v-icon>
          </v-btn>
        </template>
        <span>Delete</span>
      </v-tooltip>
    </v-card-actions>

    <v-divider class="mx-4"></v-divider>
  </v-card>
</template>
<script>
export default {
  name: "Info",
  props: {
    infoItemLabel: {
      type: Array,
      required: true,
    },
    infoItem: {
      type: Object,
      required: true,
    },
    showDeleteBtn: {
      type: Boolean,
      default: true
    },
    showEditBtn: {
      type: Boolean,
      default: true
    }
  },
  methods: {
    add() {
      this.$emit("add-item", true);
    },
    edit(item) {
      this.$emit("edit-item", item);
    },
    del(item) {
      this.$emit("del-item", item);
    },
  },
};
</script>