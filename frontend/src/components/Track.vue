<template>
  <div>
    <v-icon v-show="!timer" class="start" @click.native.stop="start(task)"
      >mdi-play-circle-outline</v-icon
    >
    <v-icon v-show="timer" class="stop" @click.native.stop="stop(task)"
      >mdi-stop-circle-outline</v-icon
    >
    {{ timer }}
  </div>
</template>
<script>
import serverApi from "../plugins/server-api.js";

export default {
  name: "Track",
  props: {
    task: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      timer: "",
    };
  },
  methods: {
    getId(item) {
      let href = item._links.task.href;
      return href.split("/task/")[1];
    },
    async start(item) {
      let start = Date.now();
      let now = new Date(start);
      let track = [];
      track["startTime"] = now.toISOString();
      track["task"] = item;
      await serverApi.saveTrack(track).then((data) => {
        this.track = data;
      });
      this._recordInterval = setInterval(() => {
        this.timer = this.toTime(Date.now() - start);
      }, 100);
    },
    async stop(item) {
      let stop = Date.now();
      let now = new Date(stop);
      this.track["endTime"] = now.toISOString();
      this.track["task"] = item;
      await serverApi.saveTrack(this.track).then((this.track = null));
      clearInterval(this._recordInterval);
      this.timer = "";
    },
    toTime(duration) {
      let seconds = parseInt((duration / 1000) % 60);
      let minutes = parseInt((duration / (1000 * 60)) % 60);
      let hours = parseInt((duration / (1000 * 60 * 60)) % 60);

      if (hours > 0) {
        return `${hours}:${minutes
          .toString()
          .padStart(2, "0")}:${seconds.toString().padStart(2, "0")}`;
      } else {
        return `${minutes}:${seconds.toString().padStart(2, "0")}`;
      }
    },
  },
};
</script>
<style scoped>
.start:hover {
  color: #689f38;
}
</style>