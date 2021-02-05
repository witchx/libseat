<template>
  <div>
    <Card style="width:800px;float: right;margin-right: 25%;">
      <Spin size="large" fix v-if="loading"></Spin>
      <div style="margin-left: 165px;margin-top: 30px">
        <Steps :current="data.progress" style="margin-bottom: 50px;">
          <Step class="blue" title="提交订单" :content="data.createTime"></Step>
          <Step class="blue" title="支付订单" :content="data.payTime"></Step>
          <Step class="blue" title="确认签到" :content="data.order.signTime"></Step>
          <Step class="blue" title="完成评价" :content="data.order.evaluateTime"></Step>
        </Steps>
        <div>
          <Card style="width:350px">
            <p slot="title" style="color: #f56c6c">
              <Icon type="md-warning" color="#f56c6c"></Icon>当前订单状态：{{data.order.orderStatus}}
            </p>
            <div slot="extra">
              <Button type="default"  @click="" v-if="data.order.status === 0">取消订单</Button>
              <Button type="default"  @click="" v-if="data.order.status === 3 || data.order.status === 1">删除订单</Button>
              <Button type="default"  @click="" v-if="data.order.status === 2">关闭订单</Button>
              <Button type="default"  @click="">备注订单</Button>
            </div>
            <div>
              <row><Icon type="md-bookmark"></Icon>基本信息</row>
              <Table border :columns="columns1" :data="data"></Table>
            </div>
            <div>
              <row><Icon type="md-bookmark"></Icon>顾客信息</row>
              <Table border :columns="columns2" :data="data"></Table>
            </div>
            <div>
              <row><Icon type="md-bookmark"></Icon>座位信息</row>
              <Table border :columns="columns3" :data="data"></Table>
            </div>
            <div>
              <row><Icon type="md-bookmark"></Icon>费用信息</row>
              <Table border :columns="columns4" :data="data"></Table>
            </div>
            <div>
              <row><Icon type="md-bookmark"></Icon>操作信息</row>
              <Table border :columns="columns5" :data="data"></Table>
            </div>
          </Card>
        </div>
      </div>
    </Card>
  </div>
</template>
<script>
  import { getOrder, getOrderDetail, updateOrder, deleteOrder } from '@/api/order';
  import { timestampFormat} from '@/libs/tools';
  export default {
    components: {

    },
    data() {
      return {
        loading: false,
        data: '',
        columns1: [
          {
            title: '订单编号',
            key: 'order.no'
          },
          {
            title: '公司名称',
            key: 'order.companyName'
          },
          {
            title: '订单类型',
            key: 'orderType'
          },
          {
            title: '自动好评时间',
            key: 'defaultEvaluateTime'
          },
          {
            title: '地址',
            key: 'stadiumAddr'
          }
        ],
        columns2: [
          {
            title: '顾客账号',
            key: 'order.customerName'
          },
          {
            title: '顾客电话',
            key: 'customerTel'
          },
          {
            title: '顾客邮件',
            key: 'customerMail'
          },
        ],
        columns3: [
          {
            title: '座位id',
            key: 'order.seatId'
          },
          {
            title: '开始时间',
            key: 'customerTel'
          },
          {
            title: '顾客邮件',
            key: 'customerMail'
          },
        ],
        columns4: [
          {
            title: '顾客账号',
            key: 'order.customerName'
          },
          {
            title: '顾客电话',
            key: 'customerTel'
          },
          {
            title: '顾客邮件',
            key: 'customerMail'
          }
        ],
        columns5: [],
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      async fetchData() {
        if (typeof(this.$route.query.orderId) !== "undefined" && this.$route.query.orderId !== '') {
          this.loading = true
          const res = await getOrderDetail({id: this.$route.query.orderId, type: this.$route.query.type})
          if (res.data.code === 200) {
            this.data = res.data.data.rows[0]
            console.log(this.data)
          } else {
            this.data = '';
            this.$Message.warning(res.data.msg);
          }
        }
        this.$route.query.orderId = '';
        setTimeout(() => {
          this.loading = false
        }, 300)
      },
    }
  }
</script>
<style>

</style>
