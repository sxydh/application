<template>
  <div id="bhe_wallet">
    <div id="bhe_search">
      <el-input placeholder="phone" v-model="search.phone" style="width: 200px;" clearable></el-input>
      <el-input
        placeholder="num"
        v-model="search.name"
        style="margin-left: 30px; width: 200px;"
        clearable
      ></el-input>
      <el-button type="primary" icon="el-icon-search" @click="synchronizeWalletList(1)"></el-button>
    </div>
    <div style="height: 10px;" />
    <div id="bhe_walletInfo">
      <el-table
        :data="walletList"
        :height="listStyle.height"
        :style="{width: listStyle.width}"
        v-loading="walletPage.loading"
        element-loading-background="rgba(0, 0, 0, 0)"
      >
        <el-table-column type="expand">
          <template slot-scope="scope">
            <el-form label-position="left" inline class="bhe_tableExpand">
              <el-form-item label="User phone">
                <span>{{ scope.row.phone}}</span>
              </el-form-item>
              <el-button size="mini" @click="openRecharge(scope.row)" type="primary">Recharge</el-button>
              <el-button size="mini" @click="toOrderList(scope.row)" type="primary">Order list</el-button>
              <el-button size="mini" @click="toWalletLog(scope.row)" type="primary">Log list</el-button>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column prop="num" label="Num" sortable width="300"></el-table-column>
        <el-table-column prop="credit" label="Credit" sortable width="150"></el-table-column>
        <el-table-column prop="balance" label="Balance" sortable width="150"></el-table-column>
        <el-table-column prop="createtime" label="Createtime" sortable width="250"></el-table-column>
        <el-table-column prop="updatetime" label="Updatetime" sortable width="250"></el-table-column>
        <el-table-column prop="lastOpt" label="Last Operation" sortable></el-table-column>
      </el-table>
      <el-pagination
        id="bhe_walletListPagination"
        :style="{margin:listStyle.pagination.margin}"
        @size-change="walletHandleSizeChange"
        @current-change="walletHandleCurrentChange"
        :current-page.sync="walletPage.position"
        :page-sizes="availablePageSize"
        :page-size="walletPage.limit"
        layout="total, sizes, prev, pager, next, jumper"
        :total="walletPage.count"
      ></el-pagination>
    </div>
    <div id="bhe_recharge">
      <el-dialog title="Recharge" :visible.sync="rechargeInfo.visible" width="400px">
        <el-input v-model="rechargeInfo.value" placeholder="$" style="width: 150px;"></el-input>
        <div slot="footer" class="dialog-footer">
          <el-button type="info" @click="rechargeInfo.visible = false">Cancel</el-button>
          <el-button type="primary" @click="recharge()">OK</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import { mapState, mapMutations, mapGetters, mapActions } from "vuex";
export default {
  data() {
    return {
      search: {},
      rechargeInfo: {
        visible: false
      },
      availablePageSize: [5, 10, 20, 30, 40],
      listStyle: {
        height: "600",
        width: "100%",
        pagination: { margin: "20px 0px 0px 0px" }
      },
      walletPage: {
        offset: 0,
        limit: 20,
        position: 1,
        count: 0,
        loading: true
      },
      walletList: []
    };
  },
  methods: {
    ...mapGetters(["takeWallet", "takeWalletPageData"]),
    ...mapMutations(["updateWallet", "updateWalletPageData"]),
    toOrderList(wallet) {
      this.updateWallet(wallet);

      this.$data.available = true;
      this.updateWalletPageData(this.$data);

      this.$router.push({ path: "/wallet/order" });
    },
    toWalletLog(wallet) {
      this.updateWallet(wallet);

      this.$data.available = true;
      this.updateWalletPageData(this.$data);

      this.$router.push({ path: "/wallet/log" });
    },
    openRecharge(wallet) {
      this.rechargeInfo.visible = true;
      this.rechargeInfo.walletNum = wallet.num;
    },
    async recharge() {
      let param = Object.assign({}, this.rechargeInfo);
      param.value = parseInt(param.value) * 100;

      this.rechargeInfo.visible = false;
      this.walletPage.loading = true;
      let [suc, error] = await this.myHttp.asyncPost({
        url: "/wallet/recharge",
        data: JSON.stringify(param)
      });

      await this.synchronizeWalletList(1);
      this.walletPage.loading = false;
    },
    walletHandleSizeChange(val) {
      this.walletPage.limit = val;
      this.synchronizeWalletList(1);
    },
    walletHandleCurrentChange(val) {
      this.walletPage.position = val;
      this.synchronizeWalletList(val);
    },
    handleClick(tab, event) {},
    async synchronizeWalletList(posit) {
      let walletPageData = this.takeWalletPageData();

      this.walletPage.loading = true;
      if (!walletPageData || !walletPageData.available) {
        this.walletPage.offset = this.walletPage.limit * --posit;
        let data = Object.assign({}, this.walletPage, this.search);

        let [suc, error] = await this.myHttp.asyncPost({
          url: "/wallet/list",
          data: JSON.stringify(data)
        });
        if (suc) {
          this.walletPage.count = suc.data.count;
          this.walletList = suc.data.list;
        }
      } else {
        Object.assign(this.$data, walletPageData);
        walletPageData.available = false;
        this.updateWalletPageData(walletPageData);
      }
      this.walletPage.loading = false;
    }
  },
  created: function() {
    this.synchronizeWalletList(1);
  }
};
</script>

<style>
#bhe_wallet .el-tabs__item {
  font-weight: 769;
  font-size: 17px;
}

#bhe_wallet .bhe_tableExpand {
  font-size: 0;
}

#bhe_wallet .bhe_tableExpand label {
  width: 120px;
  color: #99a9bf;
}

#bhe_wallet .bhe_tableExpand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 100%;
}
</style>

