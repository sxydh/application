<template>
  <div
    id="bhe_chat"
    style="background-color: #000000; position: relative; width: 100%; height: 100vh; text-align: center;"
  >
    <div
      id="bhe_list"
      style="width: 700px; height: 700px; display: inline-block; text-align: left;"
    ></div>
    <div />
    <div id="bhe_input" style="border: none; width: 700px; height: 200px; display: inline-block;">
      <textarea
        id="bhe_textarea"
        autofocus
        style="outline: none; border: none; resize: none; color: #FFFFFF; width: 100%; height: 100%; background-color: #000000;"
        @keyup.enter="send"
      ></textarea>
      <button id="bhe_exit" @click="get">Get</button>
      <button id="bhe_exit" @click="exit">Exit</button>
    </div>
  </div>
</template>

<style>
::-webkit-scrollbar {
  background-color: #000000;
}
#bhe_list {
  overflow-y: scroll;
}
#bhe_list > p {
  word-wrap: break-word;
}
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
        TPort_: null,
        Content_: null
      }
    };
  },
  created() {
    this.initWebSocket();
  },
  mounted: function() {
    let element = $("#bhe_textarea");
    setInterval(function() {
      element.focus();
    }, 0);
  },
  destroyed() {
    this.webSocket.close();
  },
  methods: {
    get() {
      let messageObj = Object.assign({}, this.messageObj);
      messageObj.Method_ = "GET";

      let message = this.wrap(messageObj);
      this.webSocket.send(message);
      console.log("webSocket.send( " + message + " )");
    },
    exit() {
      let messageObj = Object.assign({}, this.messageObj);
      messageObj.Method_ = "LEAVE";

      let message = this.wrap(messageObj);
      this.webSocket.send(message);
      console.log("webSocket.send( " + message + " )");
    },
    send() {
      let messageObj = Object.assign({}, this.messageObj);
      messageObj.Content_ = $("#bhe_textarea").val();

      let message = this.wrap(messageObj);
      this.webSocket.send(message);
      console.log("webSocket.send( " + message + " )");

      $("#bhe_textarea").val("");

      this.handleMessage(
        messageObj.SAddress_ + messageObj.SPort_,
        messageObj.Content_,
        "style='color: #FFFFFF;'"
      );
    },
    initWebSocket() {
      const wsuri = process.env.SERVER;
      this.webSocket = new WebSocket(wsuri);
      this.webSocket.onmessage = this.onmessage;
      this.webSocket.onopen = this.onopen;
      this.webSocket.onerror = this.onerror;
      this.webSocket.onclose = this.onclose;
    },
    onopen(e) {
      console.log("webSocket.open( " + new Date() + " )");
    },
    onerror(e) {
      console.log("Capture exception", e);
      this.initWebSocket();
    },
    onmessage(e) {
      console.log("webSocket.onmessage( " + e.data + " )");

      Object.assign(this.messageObj, this.parse(e.data));
      this.handleMessage(
        this.messageObj.TAddress_ + this.messageObj.TPort_,
        this.messageObj.Content_,
        "style='color: #C3602C;'"
      );
    },
    handleMessage(speaker, content, style) {
      let md5 = require("md5");
      speaker = md5(speaker);

      let p = "<p " + style + ">" + speaker + ": " + content + "</p>";
      $("#bhe_list").append(p);

      let element = $("#bhe_list");
      element.scrollTop(element[0].scrollHeight);
    },
    onclose(e) {
      console.log("webSocket.close( " + new Date() + " )");
    },
    wrap(messageObj) {
      let result = "";

      let fields = Object.keys(messageObj);
      fields.forEach(field => {
        result += field + messageObj[field];
      });

      return result;
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
            let begin = message.indexOf(preField) + (preField + "").length;
            let end = message.indexOf(curField);
            let value = message.substring(begin, end);
            messageObj[preField] = value;
          }

          preField = curField;
        } else {
          let begin = message.indexOf(preField) + (preField + "").length;
          let value = message.substring(begin);
          messageObj[preField] = value;
        }
      }

      let SAddress_ = messageObj.TAddress_;
      let SPort_ = messageObj.TPort_;
      let TAddress_ = messageObj.SAddress_;
      let TPort_ = messageObj.SPort_;
      messageObj.SAddress_ = SAddress_;
      messageObj.SPort_ = SPort_;
      messageObj.TAddress_ = TAddress_;
      messageObj.TPort_ = TPort_;

      return messageObj;
    }
  }
};
</script>

