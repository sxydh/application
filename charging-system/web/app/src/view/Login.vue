<template>
  <div
    id="bhe_login"
    style="position: absolute; width: 340px; right: calc(50% - 270px); top: calc(50% - 230px); padding: 40px 100px 20px 100px; background-color: #ffffff;"
    v-loading="loading"
  >
    <div style="font-weight:800;font-size:23px;width:auto;text-align:center;">Welcome Back</div>
    <div style="height:10px;"/>
    <div style="width:auto;text-align:center;">
      Need an Account?
      <a
        @click="toRegister"
        style="color:#20A2D3;cursor: pointer;text-decoration:underline;"
        title="Need an account?"
      >Sign up here</a>.
    </div>
    <div style="height:40px;"/>
    <div>Username</div>
    <el-input placeholder=" " class="bhe_input" v-model="loginRq.phone" clearable></el-input>
    <div style="height:20px;"/>
    <div>Password</div>
    <el-input placeholder=" " class="bhe_input" v-model="loginRq.password" show-password></el-input>
    <el-checkbox v-model="loginRq.trustThisPc">Trust this computer for 30 days</el-checkbox>
    <div style="height:20px;"/>
    <div style="width:auto;text-align:center;">
      <el-button type="warning" :disabled="loginDisabled" @click="login">Login</el-button>
    </div>
    <div style="height:30px;"/>
    <div style="width:auto;text-align:center;">
      Forgot your
      <a
        @click="toResetP"
        style="color:#20A2D3;cursor: pointer;text-decoration:underline;"
        title="Forgot password?"
      >password</a>?
    </div>
  </div>
</template>

<script>
import { mapState, mapMutations, mapGetters, mapActions } from "vuex";
export default {
  data() {
    return {
      loginRq: { type: 2, phone: "", password: "", trustThisPc: false },
      loading: false
    };
  },
  methods: {
    ...mapMutations(["updateUser", "updateWallet", "updateWater"]),
    toResetP() {
      this.$router.push({ path: "/entrance/reset-p" });
    },
    async login() {
      this.loading = true;

      let param = Object.assign({}, this.loginRq);
      var md5 = require("md5");
      param.password = md5(param.password);
      let [suc, error] = await this.myHttp.asyncPost({
        url: "/user/login",
        data: JSON.stringify(param)
      });
      if (suc) {
        this.updateUser(suc.data.user);
        this.updateWater(suc.data.water);
        this.updateWallet(suc.data.wallet);
        this.$router.push({ path: "/home" });
      }

      this.loading = false;
    },
    toRegister() {
      this.$router.push({ path: "/entrance/register" });
    }
  },
  computed: {
    loginDisabled: function() {
      if (this.loginRq.phone && this.loginRq.password) {
        return false;
      } else {
        return true;
      }
    }
  }
};
</script>

<style>
#bhe_login .bhe_input {
  margin-top: 5px;
}
</style>

