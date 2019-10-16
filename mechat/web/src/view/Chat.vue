<template>
  <div id="bhe_chat">
    <div id="bhe_list"></div>
    <div />
    <div id="bhe_system">
      <p id="bhe_note" style="color: #777777; display: none;">{{system.note}}</p>
      <p id="bhe_mode" style="color: #ff0000;">{{inputModeGet}}</p>
    </div>
    <div />
    <el-input
      id="bhe_input"
      type="textarea"
      autosize
      autofocus
      :readonly="input.readonly"
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
  background-color: black;
  color: #ffffff;
  display: inline-block;
  width: 700px;
  outline: none;
  border: none;
  resize: none;
  padding: 0px;
  margin: 0px;
}
#bhe_list,
#bhe_system {
  width: 700px;
  display: inline-block;
  text-align: left;
}
#bhe_list > p,
#bhe_system > p {
  margin-top: 0px;
  word-wrap: break-word;
}
</style>

<script>
export default {
  data() {
    return {
      input: { value: "", readonly: false },
      /*clientA => clientB*/
      system: {
        second: 10,
        color: "#777777",
        history: "",
        connectionStatus: 0, // connection status with the other side, 0: rejected, 1: connected, -1: confirming
        note: "",
        connectionId: "" // the other side name
      },
      local: { name: "&lt;local&gt;", color: "#ffffff" },
      theOther: { color: "#ff6600" },
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
  computed: {
    inputModeGet() {
      if (this.messageObj.Method_) {
        return "you are in {{" + this.messageObj.Method_ + "}}";
      } else {
        return "input {{HELP}} to get help";
      }
    }
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
      let list = $("#bhe_list");
      switch (instruction) {
        case "GET":
          break;
        case "LEAVE":
          break;
        case "LIST":
          break;
        case "POST":
          break;
        case "CLEAR":
          list.html("");
          return true;
        case "HELP":
          let help = ""
           + "▪ ctrl + enter key to send commands or messages <br/>"
           + "▪ you must select a mode before sending a message <br/>"
           + "▪ use the following command to switch to the corresponding mode <br/>"
           + "&nbsp;&nbsp;- {{LIST}}: you can send anything to get available users <br/>"
           + "&nbsp;&nbsp;- {{GET}}: send target id or nothing to establish a connection with the specific or random user correspondingly <br/>"
           + "&nbsp;&nbsp;- {{POST}}: send a message to the user you have connected to <br/>"
           + "&nbsp;&nbsp;- {{LEAVE}}: you can send anything to disconnect <br/>"
           + "&nbsp;&nbsp;- {{CLEAR}}: delete history message <br/>";
          this.system.history = help;
          this.appendSystemHistory();
          return true;
        default:
          valid = false;
      }

      if (valid) {
        this.messageObj.Method_ = instruction;
      } else {
        this.system.history = "invalid mode";
        this.appendSystemHistory();
      }

      return true;
    },
    appendSystemHistory() {
      let p = "<p style='color: " + this.system.color + "'>";
      p += this.system.history;
      p += "</p>";
      let list = $("#bhe_list");
      list.append(p);

      let element = $("#bhe_chat");
      element.scrollTop(element[0].scrollHeight);
    },
    send(e) {
      let result = this.instructionParse();
      console.log("instructionParse() => " + result);

      if (result) {
        this.input.value = "";
        return;
      }

      let messageObj = Object.assign({}, this.messageObj);
      messageObj.Content_ = this.input.value;

      switch (messageObj.Method_) {
        case "CONN":
          if (messageObj.Content_ != "y" && messageObj.Content_ != "n") {
            this.system.history = "please input y or n";
            this.appendSystemHistory();
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
            this.system.history = "crashed and need to be restarted";
            this.appendSystemHistory();
            return;
          }

          this.system.history = "( " + messageObj.Content_ + " ) => CONN";
          this.appendSystemHistory();
          break;
        case "GET":
          if (this.messageObj.TAddress_ && this.messageObj.TPort_) {
            this.system.history = "{{LEAVE}} before {{GET}}";
            this.appendSystemHistory();
            return;
          }
          this.system.connectionStatus = -1;
          this.system.history = "( " + messageObj.Content_ + " ) => GET";
          this.appendSystemHistory();
          break;
        case "LEAVE":
          if (!(messageObj.TAddress_ && messageObj.TPort_)) {
            this.system.history = "no connection";
            this.appendSystemHistory();
            return;
          }
          this.system.history = "(  ) => LEAVE";
          this.appendSystemHistory();
          break;
        case "LIST":
          if (this.messageObj.TAddress_ && this.messageObj.TPort_) {
            this.system.history = "{{LEAVE}} before {{LIST}}";
            this.appendSystemHistory();
            return;
          }
          this.system.history = "(  ) => LIST";
          this.appendSystemHistory();
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
            this.system.history = "no connection";
            this.appendSystemHistory();
            return;
          }
          this.appendLocalMessage(messageObj.Content_);
          break;
        default:
          return;
      }

      let message = this.wrap(messageObj);
      if (this.webSocket.readyState != 1) {
        this.system.history = "crashed and need to be restarted";
        this.appendSystemHistory();
        return;
      }
      this.webSocket.send(message);
      console.log("webSocket.send( " + message + " )");

      this.input.value = "";
    },
    appendLocalMessage(content) {
      let p = "<p style='color: " + this.local.color + "'>";
      p += this.local.name + ": " + content;
      p += "</p>";
      let list = $("#bhe_list");
      list.append(p);

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
            this.input.readonly = true;

            let second = this.system.second;
            this.system.connectionStatus = -1;
            let countdown = setInterval(() => {
              if (second == 0 || this.system.connectionStatus != -1) {
                clearInterval(countdown);
              }
              this.system.note = "waiting, " + second-- + "s";
            }, 1000);

            this.system.note = "waiting, " + second + "s";
            $("#bhe_note").show();
          }

          // connected request from clientB
          else if (
            mObj.SAddress_ &&
            mObj.SPort_ &&
            mObj.TAddress_ &&
            mObj.TPort_
          ) {
            this.messageObj.Method_ = "CONN";

            let id = this.keyGen(mObj.TAddress_, mObj.TPort_);
            let second = this.system.second;
            this.system.connectionStatus = -1;
            let countdown = setInterval(() => {
              if (second == 0 || this.system.connectionStatus != -1) {
                clearInterval(countdown);

                if (this.system.connectionStatus == -1) {
                  this.messageObj.Method_ = "CONN";
                  this.input.value = "n";
                  this.send();
                }
              }
              this.system.note =
                "connected request with " + id + ", y/n? " + second-- + "s";
            }, 1000);

            this.system.note =
              "connected request with " + id + ", y/n? " + second + "s";
            $("#bhe_note").show();
          }
          break;

        case "CONN":
          let messageObj = Object.assign({}, this.messageObj);
          this.system.connectionId = this.keyGen(
            messageObj.TAddress_,
            messageObj.TPort_
          );

          if (messageObj.Content_ == "y") {
            this.system.connectionStatus = 1;

            this.system.history = "connected with " + this.system.connectionId;

            this.messageObj.Method_ = "POST";
          } else if (messageObj.Content_ == "n") {
            this.system.connectionStatus = 0;

            this.system.history = "rejected with " + this.system.connectionId;

            this.messageObj.TAddress_ = "";
            this.messageObj.TPort_ = "";
            this.system.connectionId = "";

            this.messageObj.Method_ = "GET";
          }

          this.input.readonly = false;

          $("#bhe_note").hide();
          this.appendSystemHistory();
          break;

        case "LEAVE":
          this.system.connectionStatus = 0;

          let id = this.keyGen(mObj.TAddress_, mObj.TPort_);
          this.system.history = "disconnected with " + id;

          this.appendSystemHistory();

          this.messageObj.TAddress_ = "";
          this.messageObj.TPort_ = "";
          break;

        case "LIST":
          let keyBs = mObj.Content_.split(",");
          let length = keyBs.length;

          this.system.history = "";
          for (let i = 0; i < length; i++) {
            if (i < length - 1) {
              this.system.history += keyBs[i] + "<br/>";
            } else {
              this.system.history += keyBs[i];
            }
          }
          this.appendSystemHistory();

          break;

        case "POST":
          let speaker =
            "&lt;" + this.keyGen(mObj.TAddress_, mObj.TPort_) + "&gt;";
          let p = "<p style='color: " + this.theOther.color + "'>";
          p += speaker + ": " + mObj.Content_;
          p += "</p>";
          let list = $("#bhe_list");
          list.append(p);

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

