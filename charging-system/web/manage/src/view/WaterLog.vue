<template>
  <div id="bhe_waterLog">
    <div id="bhe_search">
      <el-button @click="toWater()">Back</el-button>
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
      <el-button type="primary" icon="el-icon-search" @click="synchronizeWaterLogList()"></el-button>
    </div>
    <div style="height: 10px;" />
    <div id="bhe_waterLogInfo">
      <el-table
        :data="waterLogList"
        :height="listStyle.height"
        :style="{width: listStyle.width}"
        v-loading="waterLogPage.loading"
        element-loading-background="rgba(0, 0, 0, 0)"
      >
        <el-table-column prop="operator" label="Operator" sortable width="200"></el-table-column>
        <el-table-column prop="createtime" label="Createtime" sortable width="300"></el-table-column>
        <el-table-column prop="remark" label="Description" sortable></el-table-column>
      </el-table>
      <el-pagination
        id="bhe_waterLogListPagination"
        :style="{margin:listStyle.pagination.margin}"
        @size-change="waterLogHandleSizeChange"
        @current-change="waterLogHandleCurrentChange"
        :current-page="waterLogPage.position"
        :page-sizes="availablePageSize"
        :page-size="waterLogPage.limit"
        layout="total, sizes, prev, pager, next, jumper"
        :total="waterLogPage.count"
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
      water: {},
      search: {},
      availablePageSize: [5, 10, 20, 30, 40],
      listStyle: {
        height: "600",
        width: "100%",
        pagination: { margin: "20px 0px 0px 0px" }
      },
      waterLogPage: {
        offset: 0,
        limit: 20,
        position: 1,
        count: 0,
        loading: true
      },
      waterLogList: []
    };
  },
  methods: {
    ...mapGetters(["takeWater"]),
    ...mapMutations(["updateWater"]),
    toWater() {
      this.$router.push({ path: "/water" });
    },
    waterLogHandleSizeChange(val) {
      this.waterLogPage.limit = val;
      this.synchronizeWaterLogList();
    },
    waterLogHandleCurrentChange(val) {
      this.synchronizeWaterLogList(val);
    },
    handleClick(tab, event) {},

    async synchronizeWaterLogList(posit) {
      this.water = this.takeWater();

      posit = posit != null ? posit : this.waterLogPage.position;
      this.waterLogPage.offset = this.waterLogPage.limit * --posit;

      let data = Object.assign({}, this.waterLogPage, this.search);
      data.num = this.water.num;

      if (data.date) {
        data.startDate = data.date[0];
        data.endDate = data.date[1];
      }

      this.waterLogPage.loading = true;
      let [suc, error] = await this.myHttp.asyncPost({
        url: "/water/log/list",
        data: JSON.stringify(data)
      });
      if (suc) {
        this.waterLogPage.count = suc.data.count;
        this.waterLogList = suc.data.list;
      }

      this.waterLogPage.loading = false;
    }
  },
  mounted() {
    this.synchronizeWaterLogList();
  }
};
</script>

<style>
#bhe_waterLog .el-tabs__item {
  font-weight: 769;
  font-size: 17px;
}

#bhe_waterLog .bhe_tableExpand {
  font-size: 0;
}

#bhe_waterLog .bhe_tableExpand label {
  width: 120px;
  color: #99a9bf;
}

#bhe_waterLog .bhe_tableExpand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 100%;
}
</style>

