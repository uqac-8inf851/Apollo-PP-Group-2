<template>
  <form ref="form">
    <p>{{ formTitle }}</p>
    <v-text-field
      ref="title"
      v-model="form.title"
      :counter="50"
      :rules="[rules.required]"
      :error-messages="errorMessages"
      label="Title"
    ></v-text-field>
    <v-textarea
      ref="description"
      v-model="form.description"
      :counter="100"
      :error-messages="errorMessages"
      label="Description"
    ></v-textarea>

    <v-btn class="mr-4" @click="submit"> save </v-btn>
    <v-btn @click="cancel"> cancel </v-btn>
  </form>
</template>

<script>
export default {
  props: {
    formTitle: {
      type: String,
      required: true
    },
    item: {
      type: Object,
    },
  },
  data() {
    return {
      rules: {
        required: (value) => !!value || "Required.",
      },
      errorMessages: '',
      formHasErrors: false,
    };
  },
  computed: {
    form () {
      return {
        title: this.item.title,
        description: this.item.description,
      }
    },
  },
  methods: {
    submit() {
      this.formHasErrors = false;

      Object.keys(this.form).forEach((f) => {
        if (!this.form[f]) this.formHasErrors = true;

        this.$refs[f].validate(true);
      });

      if (!this.formHasErrors) {
        if (this.item.id) this.form['id'] = this.item.id;
        
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