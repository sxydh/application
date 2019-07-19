<template>
  <div id="bhe_water">
    <div id="bhe_search">
      <el-input placeholder="num" v-model="search.eqmNum" style="width: 200px;" clearable></el-input>
      <el-button type="primary" icon="el-icon-search" @click="synchronizeWaterList(1)"></el-button>
    </div>
    <div style="height: 10px;" />
    <div id="bhe_waterList">
      <el-table
        :data="waterList"
        :height="listStyle.height"
        :style="{width: listStyle.width}"
        v-loading="waterPage.loading"
        element-loading-background="rgba(0, 0, 0, 0)"
      >
        <el-table-column type="expand">
          <template slot-scope="scope">
            <el-form label-position="left" inline class="bhe_tableExpand">
              <el-button size="mini" @click="openEdit(scope.row)" type="primary">Edit</el-button>
              <el-button size="mini" @click="toWaterLog(scope.row)" type="primary">Water log</el-button>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column prop="eqmNum" label="Equipment num" sortable width="300"></el-table-column>
        <el-table-column prop="oldValue" label="Old value" sortable width="150"></el-table-column>
        <el-table-column prop="newValue" label="New value" sortable width="150"></el-table-column>
        <el-table-column prop="createtime" label="Createtime" sortable width="250"></el-table-column>
        <el-table-column prop="updatetime" label="Updatetime" sortable width="250"></el-table-column>
        <el-table-column prop="lastOpt" label="Last Operation" sortable></el-table-column>
      </el-table>
      <el-pagination
        id="bhe_waterListPagination"
        :style="{margin:listStyle.pagination.margin}"
        @size-change="waterHandleSizeChange"
        @current-change="waterHandleCurrentChange"
        :current-page.sync="waterPage.position"
        :page-sizes="availablePageSize"
        :page-size="waterPage.limit"
        layout="total, sizes, prev, pager, next, jumper"
        :total="waterPage.count"
      ></el-pagination>
    </div>
    <div id="bhe_edit">
      <el-dialog title="Edit" :visible.sync="editInfo.visible" width="400px">
        <div>oldValue</div>
        <el-input v-model="editInfo.oldValue" placeholder style="width: 150px;"></el-input>
        <div>newValue</div>
        <el-input v-model="editInfo.newValue" placeholder style="width: 150px;"></el-input>
        <div slot="footer" class="dialog-footer">
          <el-button type="info" @click="editInfo.visible = false">Cancel</el-button>
          <el-button type="primary" @click="waterEdit()">OK</el-button>
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
      editInfo: {
        visible: false
      },
      availablePageSize: [5, 10, 20, 30, 40],
      listStyle: {
        height: "600",
        width: "100%",
        pagination: { margin: "20px 0px 0px 0px" }
      },
      waterPage: {
        offset: 0,
        limit: 20,
        position: 1,
        count: 0,
        loading: true
      },
      waterList: []
    };
  },
  methods: {
    ...mapGetters(["takeWater", "takeWaterPageData"]),
    ...mapMutations(["updateWater", "updateWaterPageData"]),
    toWaterLog(water) {
      this.updateWater(water);

      this.$data.available = true;
      this.updateWaterPageData(this.$data);

      this.$router.push({ path: "/water/log" });
    },
    openEdit(water) {
      this.editInfo.visible = true;
      this.editInfo.eqmNum = water.eqmNum;
    },
    async waterEdit() {
      let param = Object.assign({}, this.editInfo);

      this.editInfo.visible = false;
      this.waterPage.loading = true;
      let [suc, error] = await this.myHttp.asyncPost({
        url: "/water/update",
        data: JSON.stringify(param)
      });

      await this.synchronizeWaterList(1);
      this.waterPage.loading = false;
    },
    waterHandleSizeChange(val) {
      this.waterPage.limit = val;
      this.synchronizeWaterList(1);
    },
    waterHandleCurrentChange(val) {
      this.waterPage.position = val;
      this.synchronizeWaterList(val);
    },
    handleClick(tab, event) {},
    async synchronizeWaterList(posit) {
      let waterPageData = this.takeWaterPageData();

      this.waterPage.loading = true;
      if (!waterPageData || !waterPageData.available) {
        this.waterPage.offset = this.waterPage.limit * --posit;
        let data = Object.assign({}, this.waterPage, this.search);

        let [suc, error] = await this.myHttp.asyncPost({
          url: "/water/list",
          data: JSON.stringify(data)
        });
        if (suc) {
          this.waterPage.count = suc.data.count;
          this.waterList = suc.data.list;
        }
      } else {
        Object.assign(this.$data, waterPageData);
        waterPageData.available = false;
        this.updateWaterPageData(waterPageData);
      }
      this.waterPage.loading = false;
    }
  },
  created: function() {
    this.synchronizeWaterList(1);
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
</style>

