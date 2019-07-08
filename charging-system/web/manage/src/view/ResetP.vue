<template>
  <div
    id="bhe_resetP"
    style="position: absolute; width: 340px; right: calc(50% - 270px); top: calc(50% - 230px); padding: 40px 100px 20px 100px; background-color: #ffffff;"
    v-loading="loading"
  >
    <div style="font-weight:800;font-size:23px;width:auto;text-align:center;">Reset Password</div>
    <div style="height:50px;"/>
    <div>Username</div>
    <el-input placeholder=" " class="bhe_input" v-model="resetPRq.phone" clearable></el-input>
    <div style="height:5px;"/>
    <div style="width:100%;text-align:right;">
      <el-button
        @click="dycodeGet"
        :type="dycodeButton.type"
        :disabled="dycodeButton.disabled"
      >{{dycodeButton.val}}</el-button>
    </div>
    <div style="height:5px;"/>
    <div>New Password</div>
    <el-input placeholder=" " class="bhe_input" v-model="resetPRq.password" show-password></el-input>
    <div style="height:5px;"/>
    <div>Dycode</div>
    <el-input placeholder=" " class="bhe_input" v-model="resetPRq.dycode"></el-input>
    <div style="height:30px;"/>
    <div style="width:auto;text-align:center;">
      <el-button type="warning" :disabled="nextButton.disabled" @click="next">{{nextButtonText}}</el-button>
    </div>
  </div>
</template>

<script>
import { mapState, mapMutations, mapGetters, mapActions } from "vuex";
export default {
  data() {
    return {
      loading: false,
      dycodeButton: { type: "primary", disabled: false, val: "Dycode" },
      resetPRq: { phone: "", password: "", dycode: "" },
      nextButton: { disabled: false }
    };
  },
  computed: {
    nextButtonText: function() {
      if (
        this.resetPRq.phone &&
        this.resetPRq.password &&
        this.resetPRq.dycode
      ) {
        return "Reset";
      } else {
        return "Back";
      }
    }
  },
  methods: {
    ...mapMutations(["updateUser", "updateAuthorization"]),
    async next() {
      if (this.nextButtonText == "Reset") {
        if (!this.resetPRq.password) {
          this.$alert("Please enter new password", "ERROR", {
            confirmButtonText: "OK"
          });
          return;
        }

        this.loading = true;
        var md5 = require("md5");
        let param = Object.assign({}, this.resetPRq);
        param.password = md5(param.password);
        let [suc, error] = await this.myHttp.asyncPost({
          url: "/user/password/reset",
          data: JSON.stringify(param)
        });

        if (suc) {
          this.updateUser(null);
          this.updateAuthorization(null);
          this.$message.success("Successful operation, please re-login");
          this.$router.push({ path: "/entrance/login" });
        }
        this.loading = false;
      } else {
        this.$router.push({ path: "/entrance/login" });
      }
    },
    async dycodeGet() {
      if (!this.resetPRq.phone) {
        this.$alert("Please enter phone number", "ERROR", {
          confirmButtonText: "OK"
        });
        return;
      }

      this.loading = true;
      this.dycodeButton.disabled = true;
      this.dycodeButton.type = "info";
      let [suc, error] = await this.myHttp.asyncGet({
        url: "/user/dycode/get?phone=" + this.resetPRq.phone
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
    }
  }
};
</script>

<style>
#bhe_user .bhe_input {
  margin-top: 5px;
}
</style>

