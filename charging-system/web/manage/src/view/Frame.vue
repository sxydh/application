<template>
  <div
    id="bhe_frame"
    style="position: relative; display: flex; width: 100%; height: 100vh;"
    v-loading="loading"
  >
    <div
      id="bhe_aside"
      style="position: relative; width: 300px; height: 100vh; overflow-y: scroll; background-color: #32363c;"
    >
      <div
        @click="goto('none','/home')"
        id="bhe_logo"
        style="width: auto; text-align: center; color: #ffffff; font-size: 70px; font-weight: 900; margin-top: 50px; margin-bottom: 50px;"
      >BHE</div>
      <div @click="goto('bhe_aboutMe','/me')" id="bhe_aboutMe" class="bhe_item">About Me</div>
      <div @click="goto('bhe_user','/user')" id="bhe_user" class="bhe_item">User</div>
      <div @click="goto('bhe_wallet','/wallet')" id="bhe_wallet" class="bhe_item">Wallet</div>
      <div @click="goto('bhe_water','/water')" id="bhe_water" class="bhe_item">Water</div>
      <div @click="goto('bhe_news','/news')" id="bhe_news" class="bhe_item">News</div>
      <div @click="goto('bhe_getHelp','/help')" id="bhe_getHelp" class="bhe_item">Get Help</div>
      <div id="bhe_placeholder" class="bhe_item"></div>
    </div>
    <div
      id="bhe_main"
      style="width: 100%; height: 100vh; overflow-y: scroll; background-color: #f4f4f4;"
    >
      <div
        id="bhe_navigation"
        style="padding: 15px; padding-left: 30px; padding-right: 30px; width: auto; height: 40px; background-color: #ffffff;"
      >
        <div id="bhe_search" style="float: left;">
          <el-input v-model="search" placeholder=" " style="width:400px;"></el-input>
          <el-button slot="append" icon="el-icon-search"></el-button>
        </div>
        <div id="bhe_notification" style="float: right; margin-right: 30px; margin-left: 20px;">
          <el-badge :value="messages.length">
            <el-button type="info" icon="el-icon-message-solid" circle></el-button>
          </el-badge>
        </div>
        <div id="bhe_username" style="float: right; padding: 9px; color: #606469;">sxydh</div>
        <div id="bhe_userMenu" style="float: right;">
          <el-dropdown trigger="click" @command="userMenu">
            <el-button
              type="primary"
              icon="el-icon-user-solid"
              circle
              style="padding:4px;font-size:30px;"
            ></el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="logout">Logout</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>
      <div
        id="bhe_content"
        style="position: relative; margin-top: 20px; padding: 20px 0px 0px 0px; width: auto;"
      >
        <router-view></router-view>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState, mapMutations, mapGetters, mapActions } from "vuex";
export default {
  data() {
    return {
      loading: false,
      search: "",
      messages: []
    };
  },
  methods: {
    ...mapMutations(["updateUser", "updateAuthorization"]),
    goto(id, addr) {
      $("#bhe_frame #bhe_aside .bhe_item").css("background-color", "");
      $("#bhe_frame #bhe_aside #" + id).css("background-color", "#2d3136");
      this.$router.push({ path: addr });
    },
    async userMenu(command) {
      this.loading = true;

      let [suc, error] = await this.myHttp.asyncPost({
        url: "/user/logout"
      });
      this.updateUser(null);
      this.updateAuthorization(null);
      this.$router.push({ path: "/entrance/login" });
    }
  }
};
</script>


<style>
#bhe_frame #bhe_logo:hover {
  cursor: pointer;
}

#bhe_frame .bhe_item {
  padding: 15px 15px 15px 50px;
  width: auto;
  text-align: left;
  color: #c9cacb;
  font-size: 30px;
  font-weight: 500;
  border-top: 1px solid #2c2f35;
}

#bhe_frame .bhe_item:hover {
  cursor: pointer;
  background-color: #2d3136;
}

#bhe_frame #bhe_placeholder:hover {
  cursor: inherit;
  background-color: inherit;
}
</style>


