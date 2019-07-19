<template>
  <div id="bhe_news">
    <div>
      <iframe
        v-loading.body="loading"
        @load="iframeOnload"
        src="https://en.wikipedia.org/wiki/Water"
        style="overflow:auto;width:100%;height:700px;border:none;"
      ></iframe>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return { loading: true };
  },
  methods: {
    iframeOnload() {
      this.loading = false;
    }
  }
};
</script>

