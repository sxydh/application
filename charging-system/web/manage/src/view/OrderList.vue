<template>
  <div id="bhe_orderList">
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
      <el-select class="bhe_input" v-model="search.type" style="width:100px;">
        <el-option
          style="width:100px;"
          v-for="item in search.types"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        ></el-option>
      </el-select>
      <el-button type="primary" icon="el-icon-search" @click="synchronizeOrderList()"></el-button>
    </div>
    <div style="height: 10px;" />
    <div id="bhe_orderListInfo">
      <el-table
        :data="orderList"
        :height="listStyle.height"
        :style="{width: listStyle.width}"
        v-loading="orderListPage.loading"
        element-loading-background="rgba(0, 0, 0, 0)"
      >
        <el-table-column prop="id" label="OrderID" sortable width="300"></el-table-column>
        <el-table-column prop="createtime" label="CreateTime" sortable width="250"></el-table-column>
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
        <el-table-column prop="remark" label="Remark" sortable></el-table-column>
      </el-table>
      <el-pagination
        id="bhe_orderListPagination"
        :style="{margin:listStyle.pagination.margin}"
        @size-change="orderListHandleSizeChange"
        @current-change="orderListHandleCurrentChange"
        :current-page="orderListPage.position"
        :page-sizes="availablePageSize"
        :page-size="orderListPage.limit"
        layout="total, sizes, prev, pager, next, jumper"
        :total="orderListPage.count"
      ></el-pagination>
    </div>
  </div>
</template>

<script>
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
      search: {
        types: [
          { value: "1", label: "Alipay" },
          { value: "2", label: "Wechat" },
          { value: "3", label: "Cash" }
        ]
      },
      availablePageSize: [5, 10, 20, 30, 40],
      listStyle: {
        height: "600",
        width: "100%",
        pagination: { margin: "20px 0px 0px 0px" }
      },
      orderListPage: {
        offset: 0,
        limit: 20,
        position: 1,
        count: 0,
        loading: true
      },
      orderList: []
    };
  },
  methods: {
    ...mapGetters(["takeWallet"]),
    ...mapMutations(["updateWallet"]),
    toWallet() {
      this.$router.push({ path: "/wallet" });
    },
    orderListHandleSizeChange(val) {
      this.orderListPage.limit = val;
      this.synchronizeOrderList();
    },
    orderListHandleCurrentChange(val) {
      this.synchronizeOrderList(val);
    },
    handleClick(tab, event) {},

    async synchronizeOrderList(posit) {
      let wallet = this.takeWallet();

      posit = posit != null ? posit : this.orderListPage.position;
      this.orderListPage.offset = this.orderListPage.limit * --posit;

      let data = Object.assign({}, this.orderListPage, this.search);
      data.num = wallet.num;
      data.type = parseInt(data.type);

      if (data.date) {
        data.startDate = data.date[0];
        data.endDate = data.date[1];
      }

      this.orderListPage.loading = true;
      let [suc, error] = await this.myHttp.asyncPost({
        url: "/wallet/order/list",
        data: JSON.stringify(data)
      });
      if (suc) {
        this.orderListPage.count = suc.data.count;
        this.orderList = suc.data.list;

        let i;
        for (i in this.orderList) {
          this.orderList[i].type =
            this.orderList[i].type == 1
              ? "AliPay"
              : this.orderList[i].type == 2
              ? "Wechat"
              : this.orderList[i].type == 3
              ? "Cash"
              : "";
        }
      }

      this.orderListPage.loading = false;
    }
  },
  mounted() {
    this.synchronizeOrderList();
  }
};
</script>

<style>
#bhe_orderList .el-tabs__item {
  font-weight: 769;
  font-size: 17px;
}

#bhe_orderList .bhe_tableExpand {
  font-size: 0;
}

#bhe_orderList .bhe_tableExpand label {
  width: 120px;
  color: #99a9bf;
}

#bhe_orderList .bhe_tableExpand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 100%;
}
</style>

