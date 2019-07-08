<template>
  <div id="bhe_help">
    <div>
      <iframe
        v-loading.body="loading"
        @load="iframeOnload"
        src="http://www.bhe.net.cn"
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


