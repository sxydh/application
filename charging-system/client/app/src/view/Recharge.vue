<template>
  <div id="bhe_recharge">
    <div style="margin:5px;"/>
    <el-radio
      v-model="rechargeRq.type"
      label="1"
      border
      style="width:140px;"
      :disabled="payDisabled"
    >AliPay</el-radio>
    <div style="margin:5px;"/>
    <el-radio
      v-model="rechargeRq.type"
      label="2"
      border
      style="width:140px;"
      :disabled="payDisabled"
    >WechatPay</el-radio>
    <div style="margin:5px;"/>
    <el-input
      placeholder
      v-model="rechargeRq.value"
      style="width:140px;margin-right:10px;"
      :disabled="payDisabled"
    ></el-input>
    <el-button @click="pay" type="primary" :icon="payIcon" circle :disabled="payDisabled"></el-button>
    <el-button
      @click="toWallet"
      type="danger"
      icon="el-icon-close"
      circle
      :disabled="cancelDisabled"
    ></el-button>
  </div>
</template>

<script>
import { mapState, mapMutations, mapGetters, mapActions } from "vuex";
export default {
  data() {
    return {
      rechargeRq: { id: null, value: 0, type: "1" },
      payIcon: "el-icon-check",
      payDisabled: false,
      cancelDisabled: false
    };
  },
  methods: {
    ...mapGetters(["takeUser", "takeWallet", "takeWater"]),
    ...mapMutations(["updateUser", "updateWallet", "updateWater"]),
    async pay(event) {
      if (
        !new RegExp("(^[1-9]\\d*(\\.\\d+)?$)|(^0\\.\\d+$)").test(
          this.rechargeRq.value
        )
      ) {
        this.$alert("Must be a value greater than 0 !", "ERROR", {
          confirmButtonText: "OK"
        });
        return;
      }

      this.payIcon = "el-icon-loading";
      this.payDisabled = true;
      this.cancelDisabled = true;
      let param = Object.assign({}, this.rechargeRq);
      param.type = parseInt(param.type);
      param.value = parseInt(param.value);
      let [suc, error] = await this.myHttp.asyncPost({
        url: "/wallet/recharge",
        data: JSON.stringify(param)
      });
      if (suc) {
        this.QRCode(suc.data.order);
      } else {
        this.toWallet();
      }
    },
    toWallet(event) {
      $("#bhe_wallet #bhe_rechargeEntranceButton").show();
      this.$router.push({ path: "/wallet" });
    },
    QRCode(order) {
      let count = 10;
      let html =
        "<div style='width:100%;text-align:center;'> <img style='width:200px;height:200px;' src=" +
        order.qr_code +
        " > </div>";
      this.$confirm(html, "Please scan the QR code", {
        dangerouslyUseHTMLString: true,
        confirmButtonText: "OK",
        cancelButtonText: "CANCEL",
        center: true,
        customClass: "bhe_QRCodeBox"
      })
        .then(() => {
          count = 0;

          this.$message({
            type: "success",
            message: "Successful operation, see account history for details"
          });

          this.toWallet();
        })
        .catch(async () => {
          count = 0;

          let [suc, error] = await this.myHttp.asyncPost({
            url: "/wallet/order/cancel",
            data: JSON.stringify({ id: order.id })
          });

          this.toWallet();
          this.$message({
            type: "info",
            message: "You have cancelled"
          });
        });
      let payment = window.setInterval(async () => {
        if (count > 0) {
          let [suc, error] = await this.myHttp.asyncPost({
            url: "/wallet/order/get",
            data: JSON.stringify({ id: order.id })
          });
          if (suc) {
            if (suc.data && suc.data.status == 1) {
              this.toWallet();
              $(".bhe_QRCodeBox")
                .find(".el-button")
                .trigger("click");
              window.clearInterval(payment);
            }
          }
          count--;
        } else {
          window.clearInterval(payment);
        }
      }, 2000);
    }
  }
};
</script>

