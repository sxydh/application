<template>
  <div
    id="bhe_register"
    style="position: absolute; width: 340px; right: calc(50% - 270px); top: calc(50% - 310px); padding: 40px 100px 20px 100px; background-color: #ffffff;"
    v-loading="loading"
  >
    <div style="font-weight:800;font-size:23px;width:auto;text-align:center;">Get started in the BHE</div>
    <div style="height:40px;"/>
    <div>Phone</div>
    <el-input placeholder=" " class="bhe_input" v-model="registerRq.phone" clearable></el-input>
    <div style="height:5px;"/>
    <div style="width:100%;text-align:right;">
      <el-button
        @click="dycodeGet"
        :type="dycodeButton.type"
        :disabled="dycodeButton.disabled"
      >{{dycodeButton.val}}</el-button>
    </div>
    <div style="height:5px;"/>
    <div>Password</div>
    <el-input placeholder=" " class="bhe_input" v-model="registerRq.password" show-password></el-input>
    <div style="height:5px;"/>
    <div>Dycode</div>
    <el-input placeholder=" " class="bhe_input" v-model="registerRq.dycode"></el-input>
    <div style="height:5px;"/>
    <div>Area</div>
    <el-cascader
      placeholder=" "
      :options="nodes"
      :show-all-levels="false"
      v-model="path"
      @change="pathChanged"
    ></el-cascader>
    <div style="height:5px;"/>
    <div>Address</div>
    <el-input placeholder=" " v-model="registerRq.address"></el-input>
    <div style="height:30px;"/>
    <div style="width:auto;text-align:center;">
      <el-button type="warning" :disabled="registerDisabled" @click="register">Continue</el-button>
    </div>
    <div style="height:30px;"/>
    <div style="width:auto;text-align:center;">
      Already have an account?
      <a
        @click="login"
        style="color:#20A2D3;cursor: pointer;text-decoration:underline;"
        title="Forgot username?"
      >Log in</a>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      loading: false,
      dycodeButton: { type: "primary", disabled: false, val: "Dycode" },
      registerRq: {
        type: 2,
        address: "",
        phone: "",
        password: "",
        dycode: "",
        nodeId: null
      },
      path: [],
      nodes: []
    };
  },
  methods: {
    login() {
      this.$router.push({ path: "/entrance/login" });
    },
    pathChanged() {
      this.registerRq.nodeId = this.path[this.path.length - 1];
    },
    async register() {
      let param = Object.assign({}, this.registerRq);
      var md5 = require("md5");
      param.password = md5(param.password == "" ? "000000" : param.password);

      this.loading = true;
      let [suc, error] = await this.myHttp.asyncPost({
        url: "/user/register",
        data: JSON.stringify(param)
      });
      if (suc) {
        this.$message.success("Successful registration, please login");
      }

      this.loading = false;
    },
    async dycodeGet() {
      if (!this.registerRq.phone) {
        this.$alert("Please enter phone number", "ERROR", {
          confirmButtonText: "OK"
        });
        return;
      }

      this.loading = true;
      this.dycodeButton.disabled = true;
      this.dycodeButton.type = "info";
      let [suc, error] = await this.myHttp.asyncGet({
        url: "/user/dycode/get?phone=" + this.registerRq.phone
      });

      this.loading = false;
      let time = 60;
      this.dycodeButton.val = time;
      let clock = window.setInterval(() => {
        if (time > 0) {
          this.dycodeButton.val = --time;
        } else {
          this.dycodeButton.type = "primary";
          this.dycodeButton.val = "Dycode";
          this.dycodeButton.disabled = false;
          window.clearInterval(clock);
        }
      }, 1000);
    },
    async initArea() {
      let [suc, error] = await this.myHttp.asyncGet({
        url: "/user/node/list"
      });
      if (suc.data) {
        this.nodes = this.nodeToCascader(suc.data);
      }
    },
    nodeToCascader(nodes) {
      let recursive = (nodes, preOption) => {
        let i = 0;
        nodes.forEach(function(node) {
          if (node.p_id == preOption.value) {
            if (!preOption.children) {
              preOption.children = [];
            }
            let option = new Object();
            option.value = node.id;
            option.label = node.name;
            preOption.children[i] = option;
            i++;
            recursive(nodes, option);
          }
        });
      };
      //
      let result = [];
      let i = 0;
      nodes.forEach(function(node) {
        if (node.p_id == -1) {
          let option = new Object();
          option.value = node.id;
          option.label = node.name;
          result[i] = option;
          i++;
          recursive(nodes, option);
        }
      });
      return result;
    }
  },
  computed: {
    registerDisabled: function() {
      if (
        this.registerRq.phone &&
        this.registerRq.password &&
        this.registerRq.dycode &&
        this.registerRq.nodeId
      ) {
        return false;
      } else {
        return true;
      }
    }
  },
  mounted() {
    this.initArea();
  }
};
</script>

<style>
#bhe_user .bhe_input {
  margin-top: 5px;
}
</style>

