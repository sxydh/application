<template>
  <div
    id="bhe_chat"
    style="background-color: #000000; position: relative; width: 100%; height: 100vh; text-align: center;"
  >
    <div id="bhe_list" style="width: 700px; height: 700px; display: inline-block;"></div>
    <div id="bhe_input" style="width: 700px;"></div>
  </div>
</template>

<style>
</style>

<script>
export default {
  data() {
    return {
      webSocket: null,
      messageObj: {
        Method_: null,
        Type_: null,
        SAddress_: null,
        SPort_: null,
        TAddress_: null,
        Tport_: null,
        Content_: null
      }
    };
  },
  created() {
    this.initWebSocket();
  },
  destroyed() {
    this.webSocket.close();
  },
  methods: {
    initWebSocket() {
      const wsuri = process.env.SERVER;
      this.webSocket = new WebSocket(wsuri);
      this.webSocket.onmessage = this.webSocketOnmessage;
      this.webSocket.onopen = this.webSocketOnopen;
      this.webSocket.onerror = this.webSocketOnerror;
      this.webSocket.onclose = this.webSocketOnclose;
    },
    webSocketOnopen(e) {
      console.log("Open the connection", e);
    },
    webSocketOnerror(e) {
      console.log("Capture exception", e);
      this.initWebSocket();
    },
    webSocketOnmessage(e) {},
    webSocketOnclose(e) {
      console.log("The line is disconnected", e);
    },
    wrap(){

    },
    parse(message) {
      let messageObj = Object.assign({}, this.messageObj);

      let fields = Object.keys(messageObj);
      let length = fields.length;

      let curField;
      let preField;
      for (let i = 0; i <= length; i++) {
        if (i < length) {
          curField = fields[i];
          if (preField && preField != curField) {
            let begin = message.indexOf(preField);
            let end = message.indexOf(curField);
            let value = message.substring(begin, end);
            messageObj[preField] = value;
          }

          preField = curField;
        } else {
          let begin = message.indexOf(preField);
          let value = message.substring(begin);
          messageObj[preField] = value;
        }
      }

      return messageObj;
    }
  }
};
</script>

