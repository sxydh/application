<template>
  <div id="bhe_chat">
    <div id="bhe_list"></div>
    <div />
    <el-input
      id="bhe_input"
      type="textarea"
      autosize
      autofocus
      v-model="input"
      @keyup.ctrl.enter.native="send"
    ></el-input>
  </div>
</template>

<style>
#bhe_chat {
  overflow-y: scroll;
  position: relative;
  width: 100%;
  height: 100vh;
  text-align: center;
}
#bhe_input {
  background-color: #000000;
  color: #ffffff;
  display: inline-block;
  width: 700px;
  outline: none;
  border: none;
  resize: none;
}
#bhe_list {
  width: 700px;
  display: inline-block;
  text-align: left;
}
#bhe_list > p {
  word-wrap: break-word;
}
</style>

<script>
export default {
  data() {
    return {
      input: "",
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
    let element = $("#bhe_input");
    setInterval(function() {
      element.focus();
    }, 0);

    setTimeout(() => {
      this.get();
    }, 2000);
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
    send(e) {
      let messageObj = Object.assign({}, this.messageObj);
      messageObj.Content_ = this.input;

      let message = this.wrap(messageObj);
      this.webSocket.send(message);
      console.log("webSocket.send( " + message + " )");

      this.input = "";

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

      this.handleMessage(Object.assign({}, this.messageObj));
    },
    handleMessage(mObj) {
      switch (mObj.Method_) {
        case "GET":
          if (!mObj.SAddress_ && !mobj.SPort_) {
            let speaker = "system";
            let uuidv1 = require("uuid/v1");
            let id = uuidv1();

            let p = "";
            p += "<p id='" + id + "' style='color: #f54242'>";
            p += speaker + ": waiting, " + mObj.Content_;
            p += "</p>";
            $("#bhe_list").append(p);
            break;
          }
        case "POST":
          let md5 = require("md5");
          speaker = md5(mObj.SAddress_ + mObj.SPort_);

          let p = "";
          p += "<p style='color: #C3602C'>";
          p += speaker + ": " + mObj.Content_;
          p += "</p>";
          $("#bhe_list").append(p);
          break;
      }

      let element = $("#bhe_chat");
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

