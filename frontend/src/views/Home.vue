<template>
  <div>
    <div class="container-fluid">
      <div class="row">
        <div class="col-md-12">
          <h1>{{ title }}</h1>
          <h3>Message from backend: {{ message }}</h3>
          <button type="button" class="btn btn-primary">bootstrap button</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

window.axios = require("axios");

export default {
  name: 'Home',

  components: {

  },
  data() {
    return {
      title: "Hello Home",
      message: null,
    };
  },
  mounted() {
    axios
      .get("/hello")
      .then(response => (this.message = response.data));
  }
};
</script>

<style>

</style>
