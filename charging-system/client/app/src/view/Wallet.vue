<template>
  <div id="bhe_wallet">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="Profile" name="first" v-loading="profile.loading">
        <div>No.</div>
        <el-input class="bhe_input" placeholder v-model="wallet.num" disabled></el-input>
        <div class="bhe_sepseparator"></div>
        <div>Balance</div>
        <el-input class="bhe_input" placeholder v-model="wallet.balance" disabled></el-input>
        <div id="bhe_rechargeEntrance">
          <router-view></router-view>
          <el-button
            id="bhe_rechargeEntranceButton"
            @click="hideEntranceButton"
            type="primary"
            style="margin-top:5px;"
          >Recharge</el-button>
        </div>
        <div class="bhe_sepseparator"></div>
        <div>Credit</div>
        <el-input class="bhe_input" placeholder v-model="wallet.credit" disabled></el-input>
      </el-tab-pane>
      <el-tab-pane label="Order" name="second">
        <div id="bhe_orderList">
          <el-table
            :data="orderList"
            :height="listStyle.height"
            :style="{width: listStyle.width}"
            v-loading="orderPage.loading"
          >
            <el-table-column type="expand">
              <template slot-scope="scope">
                <el-form label-position="left" inline class="bhe_tableExpand">
                  <el-form-item label="You need:" :style="{display:scope.row.status==0?'':'none'}">
                    <el-button size="mini" @click="payAgain(scope.row)" type="primary">Pay</el-button>
                    <el-button size="mini" @click="discardOrder(scope.row)" type="info">Cancel</el-button>
                  </el-form-item>
                  <el-form-item label="Operator">
                    <span>{{ scope.row.operator }}</span>
                  </el-form-item>
                  <el-form-item label="UpdateTime">
                    <span>{{ scope.row.updatetime }}</span>
                  </el-form-item>
                </el-form>
              </template>
            </el-table-column>
            <el-table-column prop="id" label="OrderID" sortable width="450"></el-table-column>
            <el-table-column prop="createtime" label="CreateTime" sortable></el-table-column>
            <el-table-column prop="type" label="PayType" sortable width="130"></el-table-column>
            <el-table-column prop="amount" label="Amount" sortable width="130"></el-table-column>
            <el-table-column prop="credit" label="Credit" sortable width="130"></el-table-column>
            <el-table-column prop="balance" label="Balance" sortable width="200"></el-table-column>
            <el-table-column prop="status" label="Status" sortable width="130">
              <template slot-scope="scope">
                <div
                  :style="{color:scope.row.status==0?'#F26666':'inherit'}"
                >{{scope.row.status==-1?"discard":scope.row.status==0?"unpaid":scope.row.status==1?"finished":""}}</div>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            id="bhe_orderListPagination"
            :style="{margin:listStyle.pagination.margin}"
            @size-change="orderHandleSizeChange"
            @current-change="orderHandleCurrentChange"
            :current-page="orderPage.position"
            :page-sizes="availablePageSize"
            :page-size="orderPage.limit"
            layout="total, sizes, prev, pager, next, jumper"
            :total="orderPage.count"
          ></el-pagination>
        </div>
      </el-tab-pane>
      <el-tab-pane label="Log" name="third">
        <div id="bhe_logList">
          <el-table
            :data="logList"
            :height="listStyle.height"
            :style="{width: listStyle.width}"
            v-loading="logPage.loading"
          >
            <el-table-column type="expand">
              <template>
                <el-form label-position="left" inline class="bhe_tableExpand">
                  <el-form-item label="Placeholder:"></el-form-item>
                </el-form>
              </template>
            </el-table-column>
            <el-table-column prop="createtime" label="CreateTime" sortable width="200"></el-table-column>
            <el-table-column prop="operator" label="Operator" sortable width="200"></el-table-column>
            <el-table-column prop="remark" label="Remark" sortable></el-table-column>
          </el-table>
          <el-pagination
            id="bhe_logListPagination"
            :style="{margin:listStyle.pagination.margin}"
            @size-change="logHandleSizeChange"
            @current-change="logHandleCurrentChange"
            :current-page="logPage.position"
            :page-sizes="availablePageSize"
            :page-size="logPage.limit"
            layout="total, sizes, prev, pager, next, jumper"
            :total="logPage.count"
          ></el-pagination>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import { mapState, mapMutations, mapGetters, mapActions } from "vuex";
export default {
  data() {
    return {
      profile: { loading: false },
      availablePageSize: [5, 10, 20, 30, 40],
      listStyle: {
        height: "600",
        width: "100%",
        pagination: { margin: "20px 0px 0px 0px" }
      },
      logPage: { offset: 0, limit: 20, position: 1, count: 0, loading: true },
      orderPage: { offset: 0, limit: 20, position: 1, count: 0, loading: true },
      activeName: "first",
      wallet: {},
      orderList: [],
      logList: []
    };
  },
  methods: {
    ...mapGetters(["takeUser", "takeWallet", "takeWater"]),
    ...mapMutations(["updateUser", "updateWallet", "updateWater"]),
    discardOrder(order) {
      this.$confirm("Are you sure ?", "Note", {
        confirmButtonText: "OK",
        cancelButtonText: "CANCEL",
        type: "warning"
      })
        .then(async () => {
          this.orderPage.loading = true;

          let [suc, error] = await this.myHttp.asyncPost({
            url: "/wallet/order/cancel",
            data: JSON.stringify({ id: order.id })
          });
          if (suc) {
            this.$message({
              type: "info",
              message: "You have cancelled this order !"
            });
          }

          this.orderPage.loading = false;

          this.synchronizeOrderList();
        })
        .catch(() => {});
    },
    async payAgain(order) {
      this.orderPage.loading = true;

      let qrCode = null;
      let [suc, error] = await this.myHttp.asyncGet({
        url: "/wallet/qr_code/get"
      });
      if (suc) {
        this.orderPage.loading = false;

        qrCode = suc.data;
      } else {
        this.orderPage.loading = false;
        return;
      }

      let count = 10;
      let html =
        "<div style='width:100%;text-align:center;'> <img style='width:200px;height:200px;' src='" +
        qrCode +
        "'></div>";
      this.$alert(html, "Please scan the QR code", {
        dangerouslyUseHTMLString: true,
        confirmButtonText: "OK",
        center: true,
        customClass: "bhe_qrCodeBox"
      })
        .then(() => {
          count = 0;

          this.synchronizeOrderList();
        })
        .catch(() => {
          count = 0;
        });
      let payment = window.setInterval(async () => {
        if (count > 0) {
          let [suc, error] = await this.myHttp.asyncPost({
            url: "/wallet/order/get",
            data: JSON.stringify({ id: order.id })
          });
          if (suc) {
            if (suc.data && suc.data.status == 1) {
              window.location.reload();
            }
          }
          count--;
        } else {
          window.clearInterval(payment);
        }
      }, 5000);
    },
    orderHandleSizeChange(val) {
      this.orderPage.limit = val;
      this.synchronizeOrderList();
    },
    logHandleSizeChange(val) {
      this.logPage.limit = val;
      this.synchronizeLogList();
    },
    orderHandleCurrentChange(val) {
      this.synchronizeOrderList(val);
    },
    logHandleCurrentChange(val) {
      this.synchronizeLogList(val);
    },
    hideEntranceButton(event) {
      $("#bhe_wallet #bhe_rechargeEntranceButton").hide();
      this.$router.push({
        path: "/wallet/recharge"
      });
    },
    handleClick(tab, event) {},
    async synchronizeWallet() {
      this.profile.loading = true;
      let [suc, error] = await this.myHttp.asyncGet({
        url: "/wallet/get"
      });

      if (suc) {
        this.wallet = suc.data;
        this.updateWallet(suc.data);
      } else {
        this.wallet = this.takeWallet();
      }
      this.profile.loading = false;
    },
    async synchronizeLogList(posit) {
      posit = posit != null ? posit : this.logPage.position;
      let cdt = {};
      this.logPage.offset = this.logPage.limit * --posit;
      let data = this.logPage;
      Object.assign(data, cdt);

      this.logPage.loading = true;
      let [suc, error] = await this.myHttp.asyncPost({
        url: "/wallet/log/list",
        data: JSON.stringify(data)
      });
      if (suc) {
        this.logPage.count = suc.data.count;
        this.logList = suc.data.list;
      }

      this.logPage.loading = false;
    },
    async synchronizeOrderList(posit) {
      posit = posit != null ? posit : this.orderPage.position;
      let cdt = {};
      this.orderPage.offset = this.orderPage.limit * --posit;
      let data = this.orderPage;
      Object.assign(data, cdt);

      this.orderPage.loading = true;
      let [suc, error] = await this.myHttp.asyncPost({
        url: "/wallet/order/list",
        data: JSON.stringify(data)
      });
      if (suc) {
        this.orderPage.count = suc.data.count;
        this.orderList = suc.data.list;
        let i;
        for (i in this.orderList) {
          this.orderList[i].type =
            this.orderList[i].type == 1
              ? "AliPay"
              : this.orderList[i].type == 2
              ? "Wechat"
              : "";
        }
      }

      this.orderPage.loading = false;
    }
  },
  mounted() {
    this.synchronizeWallet();
    this.synchronizeLogList();
    this.synchronizeOrderList();
  },
  watch: {
    $route(to, from) {
      if (from.fullPath == "/wallet/recharge") {
        this.synchronizeWallet();
        this.synchronizeLogList();
        this.synchronizeOrderList();
      }
    }
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

#bhe_wallet .bhe_sepseparator {
  height: 19px;
}

#bhe_wallet .bhe_input {
  margin-top: 5px;
  width: 503px;
}
</style>

