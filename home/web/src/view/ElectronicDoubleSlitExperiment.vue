<template>
  <div
    id="bhe_electronicDoubleSlitExperiment"
    style="background-color: #000000; position: relative; width: 100%; height: 100vh;"
  >
    <div id="bhe_dot" :style="dotStyle"></div>
  </div>
</template>

<style>
</style>

<script>
export default {
  data() {
    return {
      dotStyle: {
        backgroundColor: "#ffffff",
        width: "3px",
        height: "3px",
        position: "absolute",
        left: "50px",
        top: "50px"
      }
    };
  },
  methods: {
    randomlyUpdateDot() {
      let task = window.setInterval(() => {
        let width =
          parseInt(
            $("#bhe_electronicDoubleSlitExperiment")
              .css("width")
              .replace("px", "")
          ) - 20;
        let height =
          parseInt(
            $("#bhe_electronicDoubleSlitExperiment")
              .css("height")
              .replace("px", "")
          ) - 20;
        this.dotStyle.left = width * Math.random() + "px";
        this.dotStyle.top = height * Math.random() + "px";
      }, 0);
    }
  },
  mounted() {
    this.randomlyUpdateDot();
  }
};
</script>

