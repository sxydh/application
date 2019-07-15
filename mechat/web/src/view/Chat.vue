<template>
  <div id="bhe_chat">
    <div id="bhe_list">
      <p id="bhe_separator"></p>
      <p id="bhe_system" style="color: #777777; display: none;">{{system.note}}</p>
      <p id="bhe_keep" style="color: #ff0000;">{{system.mode}}</p>
    </div>
    <div />
    <el-input
      id="bhe_input"
      type="textarea"
      autosize
      autofocus
      :disabled="input.disabled"
      v-model="input.value"
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
      input: { value: "", disabled: false },
      /*clientA => clientB*/
      system: {
        connectionStatus: 0, // connection status with the other side, 0: rejected, 1: connected, -1: confirming
        note: "",
        connectionId: "", // the other side name
        mode: "input a {{valid mode}}"
      },
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
  },
  destroyed() {
    this.webSocket.close();
  },
  methods: {
    instructionParse() {
      let instruction = this.input.value.replace(/\s+/g, "");
      if (!new RegExp("^{{2}\\w+}{2}$").test(instruction)) {
        return false;
      }

      let begin = instruction.indexOf("{{") + 2;
      let end = instruction.indexOf("}}");

      instruction = instruction.substring(begin, end);

      let valid = true;
      switch (instruction) {
        case "GET":
          break;
        case "LEAVE":
          break;
        case "LIST":
          break;
        case "POST":
          break;
        default:
          valid = false;
      }

      if (valid) {
        this.messageObj.Method_ = instruction;
        this.system.mode = "you are in {{" + instruction + "}}";
      } else {
        this.messageObj.Method_ = "";
        this.system.mode = "input a {{valid mode}}";
      }

      return true;
    },
    appendSystemNote() {
      let systemNote = $("#bhe_system").clone();
      systemNote.html(this.system.note);
      systemNote.removeAttr("id");
      systemNote.css("color", "#777777");
      let list = $("#bhe_list");
      let separator = $("#bhe_separator");
      $("#bhe_system").hide();
      list[0].insertBefore(systemNote[0], separator[0]);
      systemNote.show();

      let element = $("#bhe_chat");
      element.scrollTop(element[0].scrollHeight);
    },
    send(e) {
      if (this.instructionParse()) {
        this.input.value = "";
        return;
      }

      let messageObj = Object.assign({}, this.messageObj);
      messageObj.Content_ = this.input.value;

      switch (messageObj.Method_) {
        case "CONN":
          if (messageObj.Content_ != "y" || messageObj.Content_ != "n") {
            this.system.note = "please input y or n";
            this.appendSystemNote();
            return;
          }
          if (
            !(
              messageObj.SAddress_ &&
              messageObj.SPort_ &&
              messageObj.TAddress_ &&
              messageObj.TPort_
            )
          ) {
            this.system.note = "missing parameters";
            this.appendSystemNote();
            return;
          }

          this.system.note = "( " + messageObj.Content_ + " ) => CONN";
          this.appendSystemNote();
          break;
        case "GET":
          this.system.connectionStatus = -1;
          this.system.note = "( " + messageObj.Content_ + " ) => GET";
          this.appendSystemNote();
          break;
        case "LEAVE":
          if (
            !(
              messageObj.SAddress_ &&
              messageObj.SPort_ &&
              messageObj.TAddress_ &&
              messageObj.TPort_
            )
          ) {
            this.system.note = "missing parameters";
            this.appendSystemNote();
            return;
          }
          this.system.note = "(  ) => LEAVE";
          this.appendSystemNote();
          break;
        case "LIST":
          if (!(messageObj.SAddress_ && messageObj.SPort_)) {
            this.system.note = "missing parameters";
            this.appendSystemNote();
            return;
          }
          this.system.note = "(  ) => LIST";
          this.appendSystemNote();
          break;
        case "POST":
          if (
            !(
              messageObj.SAddress_ &&
              messageObj.SPort_ &&
              messageObj.TAddress_ &&
              messageObj.TPort_
            )
          ) {
            this.system.note = "missing parameters";
            this.appendSystemNote();
            return;
          }
          this.appendLocalMessage(messageObj.Content_);
          break;
        default:
          return;
      }

      let message = this.wrap(messageObj);
      this.webSocket.send(message);
      console.log("webSocket.send( " + message + " )");

      this.input.value = "";
    },
    appendLocalMessage(content) {
      let speaker = "local";
      let p = $(document.createElement("P"));
      p.css("color", "#ffffff");
      let inner = speaker + ": " + content;
      p.html(inner);
      let list = $("#bhe_list");
      let separator = $("#bhe_separator");
      list[0].insertBefore(p[0], separator[0]);

      let element = $("#bhe_chat");
      element.scrollTop(element[0].scrollHeight);
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

      Object.assign(this.messageObj, this.messageParse(e.data));

      this.handleMessage(Object.assign({}, this.messageObj));
    },
    handleMessage(mObj) {
      switch (mObj.Method_) {
        case "GET":
          // system reply after GET request
          if (!mObj.TAddress_ && !mObj.TPort_) {
            this.input.disabled = true;

            let second = 100;
            let countdown = setInterval(() => {
              let y = this.system.connectionStatus == 1;
              let n = second == 0 || this.system.connectionStatus == 0;
              if (y || n) {
                if (y) {
                  this.system.note = "connected to " + this.system.connectionId;
                } else if (n) {
                  this.system.note =
                    "rejection from " + this.system.connectionId;
                }

                this.input.disabled = false;

                this.appendSystemNote();

                clearInterval(countdown);
              }

              this.system.note = "waiting, " + --second + "s";
            }, 1000);

            $("#bhe_system").show();
          }

          // connected request from clientB
          else if (
            mObj.SAddress_ &&
            mObj.SPort_ &&
            mObj.TAddress_ &&
            mObj.TPort_
          ) {
            this.messageObj.Method_ = "CONN";
            this.system.mode = "CONN";

            let id = this.keyGen(mObj.TAddress_, mObj.TPort_);
            let second = 10;
            let countdown = setInterval(() => {
              let y = this.system.connectionStatus == 1;
              let n = second == 0 || this.system.connectionStatus == 0;
              if (y || n) {
                if (y) {
                  this.system.note = "connected to " + id;
                  this.system.mode = "POST";
                } else if (n) {
                  this.system.note = "rejection to " + id;
                  this.messageObj.Method_ = "CONN";
                  this.messageObj.Content_ = "n";
                  this.send();
                }

                this.appendSystemNote();

                clearInterval(countdown);
              }

              this.system.note =
                "connected request from " + id + ", y/n? " + --second + "s";
            }, 1000);

            $("#bhe_system").show();
          }
          break;

        case "CONN":
          let messageObj = Object.assign({}, this.messageObj);
          if (messageObj.Content_ == "y") {
            this.system.connectionStatus = 1;
          } else {
            this.system.connectionStatus = 0;
            this.messageObj.TAddress_ = "";
            this.messageObj.TPort_ = "";
          }
          break;

        case "LEAVE":
          this.system.connectionStatus = 0;

          let id = keyGen(mObj.TAddress_, mObj.TPort_);
          this.system.note = "disconnected to " + id;

          this.appendSystemNote();

          this.messageObj.TAddress_ = "";
          this.messageObj.TPort_ = "";
          break;

        case "LIST":
          let keyBs = mObj.Content_.split(",");
          keyBs.forEach(e => {
            this.system.note = "<br/>" + e;
          });

          this.appendSystemNote();

          break;

        case "POST":
          let speaker = keyGen(mObj.TAddress_, mObj.TPort_);
          let p = $(document.createElement("P"));
          p.css("color", "#ff6600");
          let inner = speaker + ": " + mObj.Content_;
          p.html(inner);
          let list = $("#bhe_list");
          let separator = $("#bhe_separator");
          list[0].insertBefore(p[0], separator[0]);

          let element = $("#bhe_chat");
          element.scrollTop(element[0].scrollHeight);
          break;
      }

      let element = $("#bhe_chat");
      element.scrollTop(element[0].scrollHeight);
    },
    keyGen(address, port) {
      let md5 = require("md5");
      return md5(address + port);
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
    messageParse(message) {
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

