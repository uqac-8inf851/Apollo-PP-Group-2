<template>
  <v-list-item>
    <v-list-item-content>
      <v-list-item-title v-for="track in tracks" :key="track.id">
        <v-icon>mdi-clock-check-outline</v-icon> {{ getDate(track.startTime) }} - {{ getDate(track.endTime) }}
      </v-list-item-title>
    </v-list-item-content>
  </v-list-item>
</template>
<script>
import serverApi from '../plugins/server-api.js'
import moment from 'moment';

export default {
  name: "Tracks",
  props: {
    task: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      tracks: [],
    };
  },
  mounted() {
    this.getTraks();
  },
  methods: {
    moment,
    getDate(date) {
      return moment(date).format('MM/DD/YYYY hh:mm')
    },
    async getTraks() {
      await serverApi
        .getTrackByTask(this.task)
        .then(data => {
          this.tracks = data._embedded.track;
        });
    },
  }
};
</script>