<template>
  <div id="bhe_user">
    <div id="bhe_search">
      <el-button type="primary" @click="openAddUser()">Create</el-button>
      <el-cascader
        placeholder="location"
        :options="nodes"
        :show-all-levels="false"
        v-model="path.search"
        @change="pathChanged"
        :props="{ checkStrictly: true }"
        style="margin-left: 30px; width: 150px;"
        clearable
      ></el-cascader>
      <el-input
        placeholder="phone"
        v-model="search.phone"
        style="margin-left: 30px; width: 200px;"
        clearable
      ></el-input>
      <el-input
        placeholder="name"
        v-model="search.name"
        style="margin-left: 30px; width: 200px;"
        clearable
      ></el-input>
      <el-button type="primary" icon="el-icon-search" @click="synchronizeUserList()"></el-button>
    </div>
    <div style="height: 10px;" />
    <div id="bhe_userInfo">
      <el-table
        :data="userList"
        :height="listStyle.height"
        :style="{width: listStyle.width}"
        v-loading="userPage.loading"
        element-loading-background="rgba(0, 0, 0, 0)"
      >
        <el-table-column type="expand">
          <template slot-scope="scope">
            <el-form label-position="left" inline class="bhe_tableExpand">
              <el-form-item label="Sex">
                <span>{{ scope.row.sex==1?'Female':scope.row.sex==2?'Male':''}}</span>
              </el-form-item>
              <el-form-item label="Age">
                <span>{{ scope.row.age}}</span>
              </el-form-item>
              <el-form-item label="Address">
                <span>{{ scope.row.address}}</span>
              </el-form-item>
              <el-form-item label="Updatetime">
                <span>{{ scope.row.updatetime }}</span>
              </el-form-item>
              <el-form-item label="Remark">
                <span>{{ "placeholder" }}</span>
              </el-form-item>
              <el-button size="mini" @click="openEditUser(scope.row)" type="primary">Edit</el-button>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="Phone" sortable width="150"></el-table-column>
        <el-table-column prop="name" label="Name" sortable width="150"></el-table-column>
        <el-table-column prop="typeStr" label="Type" sortable width="150"></el-table-column>
        <el-table-column prop="roleStr" label="Role" sortable width="150"></el-table-column>
        <el-table-column prop="statusStr" label="Status" sortable width="100"></el-table-column>
        <el-table-column prop="area" label="Area" sortable width="250"></el-table-column>
        <el-table-column prop="createtime" label="Createtime" sortable width="300"></el-table-column>
        <el-table-column prop="message" label="Message" sortable></el-table-column>
      </el-table>
      <el-pagination
        id="bhe_userListPagination"
        :style="{margin:listStyle.pagination.margin}"
        @size-change="userHandleSizeChange"
        @current-change="userHandleCurrentChange"
        :current-page="userPage.position"
        :page-sizes="availablePageSize"
        :page-size="userPage.limit"
        layout="total, sizes, prev, pager, next, jumper"
        :total="userPage.count"
      ></el-pagination>
    </div>
    <div id="bhe_userEdit">
      <el-dialog
        title="Edit user information"
        :visible.sync="userEdit.visible"
        center
        width="600px"
      >
        <div>Phone</div>
        <el-input class="bhe_input" placeholder=" " v-model="editUser.phone"></el-input>
        <div>Username</div>
        <el-input class="bhe_input" v-model="editUser.name"></el-input>
        <div>Password</div>
        <el-input class="bhe_input" v-model="editUser.password"></el-input>
        <div>Sex</div>
        <el-select class="bhe_input" v-model="editUser.sex" style="width:100px;">
          <el-option
            style="width:100px;"
            v-for="item in sexOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
        <div>Age</div>
        <el-input class="bhe_input" v-model="editUser.age"></el-input>
        <div>Address</div>
        <el-input class="bhe_input" v-model="editUser.address"></el-input>
        <div>Role</div>
        <el-select class="bhe_input" v-model="editUser.role" style="width:100px;">
          <el-option
            style="width:100px;"
            v-for="item in roleOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
        <div>Status</div>
        <el-select class="bhe_input" v-model="editUser.status" style="width:100px;">
          <el-option
            style="width:100px;"
            v-for="item in statusOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
        <div>Area</div>
        <el-cascader
          placeholder=" "
          :options="nodes"
          :show-all-levels="false"
          v-model="path.edit"
          style="width: 150px;"
        ></el-cascader>
        <div slot="footer" class="dialog-footer">
          <el-button type="info" @click="userEdit.visible = false">Cancel</el-button>
          <el-button type="primary" @click="updateSubUser">OK</el-button>
        </div>
      </el-dialog>
    </div>
    <div id="bhe_userAdd">
      <el-dialog title="New user" :visible.sync="userAdd.visible" center width="600px">
        <div>Phone</div>
        <el-input class="bhe_input" placeholder=" " v-model="addUser.phone"></el-input>
        <div>Username</div>
        <el-input class="bhe_input" v-model="addUser.name"></el-input>
        <div>Password</div>
        <el-input class="bhe_input" v-model="addUser.password" show-password></el-input>
        <div>Sex</div>
        <el-select class="bhe_input" v-model="addUser.sex" style="width:100px;">
          <el-option
            style="width:100px;"
            v-for="item in sexOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
        <div>Age</div>
        <el-input class="bhe_input" v-model="addUser.age"></el-input>
        <div>Address</div>
        <el-input class="bhe_input" v-model="addUser.address"></el-input>
        <div>Type</div>
        <el-select
          class="bhe_input"
          v-model="addUser.type"
          style="width:100px;"
          @change="addUserRoleInit()"
        >
          <el-option
            style="width:100px;"
            v-for="item in typeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
        <div>Role</div>
        <el-select class="bhe_input" v-model="addUser.role" style="width:100px;">
          <el-option
            style="width:100px;"
            v-for="item in roleOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
        <div>Area</div>
        <el-cascader
          placeholder=" "
          :options="nodes"
          :show-all-levels="false"
          v-model="path.edit"
          style="width: 150px;"
        ></el-cascader>
        <div slot="footer" class="dialog-footer">
          <el-button type="info" @click="userAdd.visible = false">Cancel</el-button>
          <el-button type="primary" @click="subUserAdd()">OK</el-button>
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
      path: { search: [], edit: [], add: [] },
      nodes: [],
      sexOptions: [
        { value: "1", label: "female" },
        { value: "2", label: "male" }
      ],
      statusOptions: [
        { value: "1", label: "enabled" },
        { value: "0", label: "disabled" }
      ],
      typeOptions: [],
      roleOptions: [],
      addUser: {},
      editUser: {},
      userAdd: { visible: false, loading: false },
      userEdit: { visible: false, loading: false },
      availablePageSize: [5, 10, 20, 30, 40],
      listStyle: {
        height: "600",
        width: "100%",
        pagination: { margin: "20px 0px 0px 0px" }
      },
      userPage: { offset: 0, limit: 20, position: 1, count: 0, loading: true },
      userList: []
    };
  },
  methods: {
    ...mapGetters(["takeUser"]),
    ...mapMutations(["updateUser"]),
    addUserRoleInit() {
      let cachedUser = this.takeUser();

      if (cachedUser.role == 0) {
        if (this.addUser.type) {
          if (this.addUser.type + "" == "1") {
            this.roleOptions = [
              { value: "1", label: "Accountant" },
              { value: "2", label: "Recorder" }
            ];
          } else if (this.addUser.type + "" == "2") {
            this.roleOptions = [{ value: "1", label: "Member" }];
          }
        }
      } else if (cachedUser.role == 1) {
        this.roleOptions = [{ value: "1", label: "Member" }];
      }
    },
    async subUserAdd() {
      let param = Object.assign({}, this.addUser);
      param.nodeId = this.path.edit[this.path.edit.length - 1];

      if (
        !param.phone ||
        !/^((13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\d{8}$/.test(
          param.phone
        ) ||
        !param.password ||
        !param.type ||
        !param.role ||
        !param.nodeId
      ) {
        this.$message.error("invalid arguments!");
        return;
      }

      let addUserModel = JSON.stringify(param);
      window.localStorage.setItem("addUserModel", addUserModel);

      this.userAdd.visible = false;
      this.userPage.loading = true;

      param.age = parseInt(param.age);
      param.sex = parseInt(param.sex);
      param.status = parseInt(param.status);
      param.role = parseInt(param.role);
      param.type = parseInt(param.type);
      var md5 = require("md5");
      param.password = md5(param.password);

      let [suc, error] = await this.myHttp.asyncPost({
        url: "/user/sub/add",
        data: JSON.stringify(param)
      });
      if (suc) {
        await this.synchronizeUserList();
      }

      this.userPage.loading = false;
    },
    pathChanged() {
      this.search.nodeId = this.path.search[this.path.search.length - 1];
    },
    async initArea() {
      let [suc, error] = await this.myHttp.asyncGet({
        url: "/user/node/list"
      });
      if (suc.data) {
        this.nodes = this.nodeToCascader(suc.data);
      }
    },
    nodeToCascader(nodes) {
      let recursive = (nodes, preOption) => {
        let i = 0;
        nodes.forEach(function(node) {
          if (node.p_id == preOption.value) {
            if (!preOption.children) {
              preOption.children = [];
            }
            let option = new Object();
            option.value = node.id;
            option.label = node.name;
            preOption.children[i] = option;
            i++;
            recursive(nodes, option);
          }
        });
      };
      //
      let result = [];
      let i = 0;
      nodes.forEach(function(node) {
        if (node.p_id == -1) {
          let option = new Object();
          option.value = node.id;
          option.label = node.name;
          result[i] = option;
          i++;
          recursive(nodes, option);
        }
      });
      return result;
    },
    openAddUser() {
      let addUserModel = localStorage.getItem("addUserModel");
      if (addUserModel) {
        this.addUser = JSON.parse(addUserModel);
      }

      this.userAdd.visible = true;

      let cachedUser = this.takeUser();

      this.typeOptions = {};
      this.roleOptions = {};

      if (cachedUser.role == 0) {
        this.typeOptions = [
          { value: "1", label: "Manage" },
          { value: "2", label: "App" }
        ];
        if (this.addUser.type) {
          if (this.addUser.type + "" == "1") {
            this.roleOptions = [
              { value: "1", label: "Accountant" },
              { value: "2", label: "Recorder" }
            ];
          } else if (this.addUser.type + "" == "2") {
            this.roleOptions = [{ value: "1", label: "Member" }];
          }
        }
      } else if (cachedUser.role == 1) {
        this.typeOptions = [{ value: "2", label: "App" }];
        this.roleOptions = [{ value: "1", label: "Member" }];
      }
    },
    openEditUser(user) {
      this.userEdit.visible = true;
      this.editUser = Object.assign({}, user);

      let cachedUser = this.takeUser();

      this.roleOptions = {};
      if (cachedUser.role == 0) {
        if (user.type + "" == "1") {
          this.roleOptions = [
            { value: "1", label: "Accountant" },
            { value: "2", label: "Recorder" }
          ];
        } else if (user.type + "" == "2") {
          this.roleOptions = [{ value: "1", label: "Member" }];
        }
      } else if (cachedUser.role == 1) {
        this.roleOptions = [{ value: "1", label: "Member" }];
      }

      this.editUser.status = user.status + "";
      this.editUser.role = user.role + "";
    },
    async updateSubUser() {
      this.userEdit.visible = false;
      this.userPage.loading = true;

      let param = Object.assign({}, this.editUser);
      param.sex = parseInt(param.sex);
      param.status = parseInt(param.status);
      param.role = parseInt(param.role);
      if (param.password) {
        var md5 = require("md5");
        param.password = md5(param.password);
      }
      param.nodeId = this.path.edit[this.path.edit.length - 1];

      let [suc, error] = await this.myHttp.asyncPost({
        url: "/user/sub/update",
        data: JSON.stringify(param)
      });
      if (suc) {
        this.synchronizeUserList();
      }

      this.userPage.loading = false;
    },
    userHandleSizeChange(val) {
      this.userPage.limit = val;
      this.synchronizeUserList();
    },
    userHandleCurrentChange(val) {
      this.synchronizeUserList(val);
    },
    handleClick(tab, event) {},
    async synchronizeUserList(posit) {
      posit = posit != null ? posit : this.userPage.position;
      this.userPage.offset = this.userPage.limit * --posit;
      let data = Object.assign({}, this.userPage, this.search);

      this.userPage.loading = true;
      let [suc, error] = await this.myHttp.asyncPost({
        url: "/user/list",
        data: JSON.stringify(data)
      });
      if (suc) {
        this.userPage.count = suc.data.count;
        this.userList = suc.data.list;
        let i;
        for (i in this.userList) {
          let curUser = this.userList[i];

          this.userList[i].statusStr =
            curUser.status == 1
              ? "enabled"
              : curUser.status == 0
              ? "disabled"
              : "";
          if (curUser.type == 1) {
            this.userList[i].typeStr = "Manage";
            this.userList[i].roleStr =
              curUser.role == 0
                ? "Administrator"
                : curUser.role == 1
                ? "Accountant"
                : curUser.role == 2
                ? "Recorder"
                : "";
          } else if (curUser.type == 2) {
            this.userList[i].typeStr = "App";
            this.userList[i].roleStr = curUser.role == 1 ? "Member" : "";
          }
        }
      }

      this.userPage.loading = false;
    }
  },
  mounted() {
    this.initArea();
    this.synchronizeUserList();
  }
};
</script>

<style>
#bhe_userAdd .bhe_input,
#bhe_userEdit .bhe_input {
  width: 300px;
  margin: 5px 0px 20px 0px;
}

#bhe_user .el-tabs__item {
  font-weight: 769;
  font-size: 17px;
}

#bhe_user .bhe_tableExpand {
  font-size: 0;
}

#bhe_user .bhe_tableExpand label {
  width: 120px;
  color: #99a9bf;
}

#bhe_user .bhe_tableExpand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 100%;
}
</style>

