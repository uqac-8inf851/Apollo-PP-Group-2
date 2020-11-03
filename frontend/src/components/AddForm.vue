<template>
  <v-form ref="form" v-model="valid" lazy-validation>
    <p>{{ formTitle }}</p>
    <v-text-field
      ref="title"
      v-model="form[itemLabel[0]]"
      :counter="50"
      :rules="[rules.required, rules.titleCount]"
      :error-messages="errorMessages"
      label="Title"
    ></v-text-field>
    <v-textarea
      ref="description"
      v-model="form[itemLabel[1]]"
      :counter="300"
      :rules="[rules.required, rules.descriptionCount]"
      :error-messages="errorMessages"
      label="Description"
    ></v-textarea>

    <v-btn :disabled="!valid" class="mr-4" @click="submit"> save </v-btn>
    <v-btn @click="cancel"> cancel </v-btn>
  </v-form>
</template>

<script>
export default {
  props: {
    formTitle: {
      type: String,
      required: true,
    },
    item: {
      type: Object,
    },
    itemLabel: {
      type: Array,
      required: true,
    },
  },
  data() {
    return {
      valid: false,
      rules: {
        required: (value) => !!value || "Required.",
        titleCount: (value) =>
          (value && value.length <= 50) ||
          "Title must be less than 50 characters",
        descriptionCount: (value) =>
          (value && value.length <= 300) || "Description must be less than 300 characters",
      },
      errorMessages: "",
    };
  },
  beforeMount() {
    this.form[this.itemLabel[1]] = this.form[this.itemLabel[1]] || '';
  },
  computed: {
    form() {
      let form = [];
      form[this.itemLabel[0]] = this.item[this.itemLabel[0]];
      form[this.itemLabel[1]] = this.item[this.itemLabel[1]];
      return form;
    },
  },
  methods: {
    submit() {
      if (this.$refs.form.validate()) {
        this.form["item"] = this.item;
        this.$emit("save-item", this.form);
      }
    },
    cancel() {
      this.$emit("cancel-add-item", false);
    },
  },
};
</script>

<style scoped>
form {
  margin: 40px;
}

form p {
  font-size: 1.5rem;
  font-weight: bold;
}
</style>