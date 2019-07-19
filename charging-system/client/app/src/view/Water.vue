<template>
  <div id="bhe_water">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="Profile" name="first">
        <div>EQM No.</div>
        <el-input class="bhe_input" placeholder v-model="water.eqm_num" :disabled="true"></el-input>
        <div class="bhe_sepseparator"></div>
        <div>LastValue</div>
        <el-input class="bhe_input" placeholder v-model="water.old_value" :disabled="true"></el-input>
        <div class="bhe_sepseparator"></div>
        <div>LatestValue</div>
        <el-input class="bhe_input" placeholder v-model="water.new_value" :disabled="true"></el-input>
        <div class="bhe_sepseparator"></div>
        <div>CreateTime</div>
        <el-input class="bhe_input" placeholder v-model="water.createtime" :disabled="true"></el-input>
        <div class="bhe_sepseparator"></div>
        <div>UpdateTime</div>
        <el-input class="bhe_input" placeholder v-model="water.updatetime" :disabled="true"></el-input>
      </el-tab-pane>
      <el-tab-pane label="Log" name="second">
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
      availablePageSize: [5, 10, 20, 30, 40],
      listStyle: {
        height: "600",
        width: "100%",
        pagination: { margin: "20px 0px 0px 0px" }
      },
      logPage: { offset: 0, limit: 20, position: 1, count: 0, loading: true },
      activeName: "first",
      water: {},
      logList: []
    };
  },
  methods: {
    ...mapGetters(["takeUser", "takeWallet", "takeWater"]),
    ...mapMutations(["updateUser", "updateWallet", "updateWater"]),
    toList() {
      $("#bhe_water #history_detail").css("display", "none");
      $("#bhe_water #history_list").css("display", "");
    },
    logHandleSizeChange(val) {
      this.logPage.loading = true;

      this.logPage.limit = val;
      this.synchronizeLogList();
    },
    logHandleCurrentChange(val) {
      this.logPage.loading = true;

      this.synchronizeLogList(val);
    },
    async synchronizeWater() {
      if (!this.takeWater()) {
        let [suc, error] = await this.myHttp.asyncGet({
          url: "/water/get"
        });
        if (suc) {
          this.water = suc.data;
          this.updateWater(suc.data);
        }
      }
      this.water = this.takeWater();
    },
    async synchronizeLogList(posit) {
      posit = posit != null ? posit : this.logPage.position;
      let cdt = {};
      this.logPage.offset = this.logPage.limit * --posit;
      let data = this.logPage;
      Object.assign(data, cdt);
      let [suc, error] = await this.myHttp.asyncPost({
        url: "/water/log/list",
        data: JSON.stringify(data)
      });
      if (suc) {
        this.logPage.count = suc.data.count;
        this.logList = suc.data.list;
      }
      this.logPage.loading = false;
    },
    handleClick(tab, event) {}
  },
  mounted() {
    this.synchronizeLogList();
    this.synchronizeWater();
  }
};
</script>

<style>
#bhe_water .el-tabs__item {
  font-weight: 769;
  font-size: 17px;
}

#bhe_water .bhe_tableExpand {
  font-size: 0;
}

#bhe_water .bhe_tableExpand label {
  width: 120px;
  color: #99a9bf;
}

#bhe_water .bhe_tableExpand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 100%;
}

#bhe_water .bhe_sepseparator {
  height: 19px;
}

#bhe_water .bhe_input {
  margin-top: 5px;
  width: 503px;
}
</style>