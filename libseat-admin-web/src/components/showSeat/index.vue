<template>
  <Modal v-model="show" width="900" title="图书馆详情" @on-visible-change="showChange">

      <div v-if="seat.length" class="seat">
        <div class="line" v-for="(line, index) in seat" :key="index">
          <span class="line-index">{{index + 1}}</span>
          <div class="row" v-for="(row, _index) in line.row" :key="_index" @click="onclickSeat(row)">
            <span v-if="index === 0" class="row-index">{{_index + 1}}</span>
            <img v-if="row.value " :src="row.value === 1 ? empty : sold" @ alt="">
          </div>
          <div style="clear: both"></div>
        </div>
      </div>

  </Modal>
</template>
<script>
  import { timestampFormat } from '@/libs/tools'
  import { updateOrder } from '@/api/order'
  export default {
    props: {
      create_modal_show: {
        type: Boolean,
        default: false
      },
      seat: {
        type: Array,
        default: []
      }
    },
    data() {
      return {
        show: false,
        empty: require('@/assets/images/empty_seat.png'),
        sold: require('@/assets/images/sold_seat.png')
      }
    },
    methods: {
      showChange(val) {
        if (!val) {
          this.$emit('noshow')
        }
      },
      onclickSeat(row) {
        if (row.value === 1) {
          this.$Message.info({
            content: `SEAT_ID:${row.seatId} 空座位`,
            duration: 2,
            closable: true
          });
        } else if (row.value === 2) {
          debugger
          this.$Modal.confirm({
            okText: '强制签退',
            title: `SEAT_ID:${row.seatId}`,
            content: `<p>占用时间: ${timestampFormat(row.startTime)} 至 ${timestampFormat(row.endTime)}</p>`,
            onOk: async () => {
              await updateOrder({id: row.orderId, seatId: row.seatId, status: 3, endTime: Date.now()})
              this.$emit('refresh')
            },
            onCancel: () => {
            }
          });
        }
      }
    },
    watch: {
      create_modal_show: {
        handler: function (val1, val2) {
          if (this.show !== val1) {
            this.show = val1
          }
        },
        immediate: true
      }
    }
  }
</script>
<style lang="scss" scoped>
  .seat {
    border: 1px solid #dddddd;
    border-radius: 5px;
    padding: 50px;
    width: 100%;
    position: relative;
    overflow-x: auto;
    .line-index {
      float: left;
      line-height: 26px;
      color: #515a6e;
      margin-right: 50px;
      width: 15px;
    }
    .line {
      width: 100%;
      margin-top: 5px;
      white-space:nowrap;
      .row {
        display: inline-block;
        height: 26px;
        width: 30px;
        margin: 0 5px;
        .row-index {
          position: absolute;
          top: 20px;
          text-align: center;
          width: 30px;
        }
        img {
          cursor: pointer;
        }
      }
    }
  }
</style>
