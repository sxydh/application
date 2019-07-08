<template>
  <div id="bhe_walletLog">
    <div id="bhe_search">
      <el-button @click="toWallet()">Back</el-button>
      <el-date-picker
        v-model="search.date"
        type="daterange"
        align="right"
        unlink-panels
        range-separator="To"
        start-placeholder="Start"
        end-placeholder="End"
        :picker-options="pickerOptions"
        format="yyyy-MM-dd"
        value-format="yyyy-MM-dd"
        style="margin-left: 30px;"
      ></el-date-picker>
      <el-input
        placeholder="description"
        v-model="search.remark"
        style="margin-left: 30px; width: 200px;"
        clearable
      ></el-input>
      <el-button type="primary" icon="el-icon-search" @click="synchronizeWalletLogList()"></el-button>
    </div>
    <div style="height: 10px;" />
    <div id="bhe_walletLogInfo">
      <el-table
        :data="walletLogList"
        :height="listStyle.height"
        :style="{width: listStyle.width}"
        v-loading="walletLogPage.loading"
      >
        <el-table-column prop="operator" label="Operator" sortable width="200"></el-table-column>
        <el-table-column prop="createtime" label="Createtime" sortable width="300"></el-table-column>
        <el-table-column prop="remark" label="Description" sortable></el-table-column>
      </el-table>
      <el-pagination
        id="bhe_walletLogListPagination"
        :style="{margin:listStyle.pagination.margin}"
        @size-change="walletLogHandleSizeChange"
        @current-change="walletLogHandleCurrentChange"
        :current-page="walletLogPage.position"
        :page-sizes="availablePageSize"
        :page-size="walletLogPage.limit"
        layout="total, sizes, prev, pager, next, jumper"
        :total="walletLogPage.count"
      ></el-pagination>
    </div>
  </div>
</template>

<script>
import { Loading } from "element-ui";
import { mapState, mapMutations, mapGetters, mapActions } from "vuex";
export default {
  data() {
    return {
      pickerOptions: {
        shortcuts: [
          {
            text: "This month",
            onClick(picker) {
              picker.$emit("pick", [new Date(), new Date()]);
            }
          },
          {
            text: "This year",
            onClick(picker) {
              const end = new Date();
              const start = new Date(new Date().getFullYear(), 0);
              picker.$emit("pick", [start, end]);
            }
          },
          {
            text: "Last six months",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setMonth(start.getMonth() - 6);
              picker.$emit("pick", [start, end]);
            }
          }
        ]
      },
      wallet: {},
      search: {},
      availablePageSize: [5, 10, 20, 30, 40],
      listStyle: {
        height: "600",
        width: "100%",
        pagination: { margin: "20px 0px 0px 0px" }
      },
      walletLogPage: {
        offset: 0,
        limit: 20,
        position: 1,
        count: 0,
        loading: true
      },
      walletLogList: []
    };
  },
  methods: {
    ...mapGetters(["takeWallet"]),
    ...mapMutations(["updateWallet"]),
    toWallet() {
      this.$router.push({ path: "/wallet" });
    },
    walletLogHandleSizeChange(val) {
      this.walletLogPage.limit = val;
      this.synchronizeWalletLogList();
    },
    walletLogHandleCurrentChange(val) {
      this.synchronizeWalletLogList(val);
    },
    handleClick(tab, event) {},

    async synchronizeWalletLogList(posit) {
      this.wallet = this.takeWallet();

      posit = posit != null ? posit : this.walletLogPage.position;
      this.walletLogPage.offset = this.walletLogPage.limit * --posit;

      let data = Object.assign({}, this.walletLogPage, this.search);
      data.num = this.wallet.num;

      if (data.date) {
        data.startDate = data.date[0];
        data.endDate = data.date[1];
      }

      this.walletLogPage.loading = true;
      let [suc, error] = await this.myHttp.asyncPost({
        url: "/wallet/log/list",
        data: JSON.stringify(data)
      });
      if (suc) {
        this.walletLogPage.count = suc.data.count;
        this.walletLogList = suc.data.list;
      }

      this.walletLogPage.loading = false;
    }
  },
  mounted() {
    this.synchronizeWalletLogList();
  }
};
</script>

<style>
#bhe_walletLog .el-tabs__item {
  font-weight: 769;
  font-size: 17px;
}

#bhe_walletLog .bhe_tableExpand {
  font-size: 0;
}

#bhe_walletLog .bhe_tableExpand label {
  width: 120px;
  color: #99a9bf;
}

#bhe_walletLog .bhe_tableExpand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 100%;
}
</style>

