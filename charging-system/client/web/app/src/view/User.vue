<template>
  <div id="bhe_user">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="Profile" name="first" v-loading="profile.loading">
        <div>Phone</div>
        <el-input class="bhe_input" placeholder=" " v-model="user.phone" disabled></el-input>
        <div class="bhe_sepseparator"></div>
        <div>Username</div>
        <el-input class="bhe_input" v-model="user.name" :disabled="profile.disabled"></el-input>
        <div class="bhe_sepseparator"></div>
        <div>Age</div>
        <el-input class="bhe_input" v-model="user.age" :disabled="profile.disabled"></el-input>
        <div class="bhe_sepseparator"></div>
        <div>Sex</div>
        <el-select v-model="user.sex" style="width:100px;" :disabled="profile.disabled">
          <el-option
            style="width:100px;"
            v-for="item in sexOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
        <div class="bhe_sepseparator"></div>
        <div>Address</div>
        <el-input class="bhe_input" v-model="user.address" :disabled="profile.disabled"></el-input>
        <div class="bhe_sepseparator"></div>
        <el-button @click="profileUpdate" type="primary">{{profile.buttonText}}</el-button>
        <div class="bhe_sepseparator"></div>
      </el-tab-pane>
      <el-tab-pane
        label="Password & Authentication"
        name="second"
        v-loading="authentication.loading"
      >
        <el-input
          placeholder="old password"
          class="bhe_input"
          v-model="user.oldPassword"
          show-password
        ></el-input>
        <div/>
        <el-input
          placeholder="new password"
          class="bhe_input"
          v-model="user.password"
          show-password
        ></el-input>
        <div class="bhe_sepseparator"></div>
        <el-button @click="resetP" type="primary">Save</el-button>
        <div class="bhe_sepseparator"></div>
      </el-tab-pane>
      <el-tab-pane label="Settings" name="third">
        <div>Notifications</div>
        <el-switch
          v-model="messageSwitch"
          active-color="#13ce66"
          inactive-color="#ff4949"
          active-value="Email alerts for account activity are enabled."
          inactive-value="Email alerts for account activity are disabled."
        ></el-switch>
        <div>{{messageSwitch}}</div>
        <div class="bhe_sepseparator"></div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
import { mapState, mapMutations, mapGetters, mapActions } from "vuex";
export default {
  data() {
    return {
      profile: { disabled: true, buttonText: "Edit", loading: false },
      authentication: { loading: false },
      user: {},
      activeName: "first",
      messageSwitch: "",
      sexOptions: [
        { value: "1", label: "female" },
        { value: "2", label: "male" }
      ]
    };
  },
  methods: {
    ...mapGetters(["takeUser", "takeWallet", "takeWater"]),
    ...mapMutations(["updateUser", "updateWallet", "updateWater"]),
    async resetP() {
      if (!this.user.password) {
        this.$alert("Please enter new password", "ERROR", {
          confirmButtonText: "OK"
        });
        return;
      }

      let param = Object.assign({}, this.user);
      var md5 = require("md5");
      param.password = md5(param.password);
      param.oldPassword = md5(param.oldPassword);

      this.authentication.loading = true;
      let [suc, error] = await this.myHttp.asyncPost({
        url: "/user/password/reset",
        data: JSON.stringify(param)
      });
      if (suc) {
        this.updateUser(null);
        this.updateWater(null);
        this.updateWallet(null);
        this.$message.success("Successful operation, please re-login");

        this.authentication.loading = false;
        this.$router.push({ path: "/entrance/login" });
      }

      this.authentication.loading = false;
    },
    async profileUpdate() {
      if (this.profile.buttonText == "Save") {
        this.profile.buttonText = "Edit";
        this.profile.disabled = true;

        this.profile.loading = true;
        let param = Object.assign({}, this.user);
        param.sex = parseInt(param.sex);
        let [suc, error] = await this.myHttp.asyncPost({
          url: "/user/update",
          data: JSON.stringify(param)
        });
        if (suc) {
          this.synchronizeUser(this.user);
          this.$message.success("Successful operation!");
        }

        this.profile.loading = false;
      } else if (this.profile.buttonText == "Edit") {
        this.profile.buttonText = "Save";
        this.profile.disabled = false;
      }
    },
    handleClick(tab, event) {},
    synchronizeUser(user) {
      if (user) {
        this.updateUser(user);
      } else {
        let cachedUser = this.takeUser();
        let copyUser = Object.assign({}, cachedUser);
        copyUser.sex = copyUser.sex + "";
        this.user = copyUser;
      }
    }
  },
  mounted() {
    this.synchronizeUser();
  }
};
</script>

<style>
#bhe_user .el-tabs__item {
  font-weight: 769;
  font-size: 17px;
}

#bhe_user .bhe_input {
  margin-top: 5px;
  width: 199px;
}

#bhe_user .bhe_sepseparator {
  height: 19px;
}
</style>


