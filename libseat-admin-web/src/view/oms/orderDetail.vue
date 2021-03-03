<template>
  <div>
    <Card dis-hover>
      <Spin size="large" fix v-if="loading"></Spin>
      <div v-if="data[0] && data[0].order" style="margin: 2% 7%;">
        <Steps :current="data[0].order.progress" v-model="data" style="margin: 0% 0% 6% 8%;">
          <Step class="blue" title="提交订单" :content="data[0].order.createTime"></Step>
          <Step class="blue" title="支付订单" :content="data[0].order.payTime"></Step>
          <Step v-if="data[0].order.type === 0" class="blue" title="确认签到" :content="data[0].order.confirmTime"></Step>
          <Step v-if="data[0].order.type === 0" class="blue" title="完成评价" :content="data[0].order.evaluateTime"></Step>
          <Step class="blue" title="订单结束"></Step>
        </Steps>
        <div>
          <Card dis-hover style="margin: 0 15%">
            <p slot="title" style="color: #f56c6c">
              <Icon type="md-warning" color="#f56c6c" size="25" style="margin-right: 5px"></Icon>当前订单状态：{{data[0].order.orderStatus}}
            </p>
            <div slot="extra" style="margin-top: -4px;">
              <Button type="default"  @click="closeOrder" v-if="data[0].order.status === 1" style="margin-right: 10px">关闭订单</Button>
              <Button type="default"  @click="cancelOrder" v-if="data[0].order.type === 0 && data[0].order.status === 2" style="margin-right: 10px">取消订单</Button>
              <Button type="default"  @click="deleteOrder" v-if="(data[0].order.status > 3) || (data[0].order.type === 0 && data[0].order.status === 2)" style="margin-right: 10px">删除订单</Button>
              <Button type="default"  @click="create_show.value = true">备注订单</Button>
            </div>
            <div>
              <row style="margin: 15px 0px 10px 0px;"><Icon type="md-bookmark" size="23" style="margin-right: 5px"></Icon>基本信息</row>
              <Table border :loading="loading" :columns="columns1" :data="data"></Table>
            </div>
            <div>
              <row style="margin: 15px 0px 10px 0px;"><Icon type="md-bookmark" size="23" style="margin-right: 5px"></Icon>顾客信息</row>
              <Table border :loading="loading" :columns="columns2" :data="data"></Table>
            </div>
            <div v-if="data[0].order.type === 0">
              <row style="margin: 15px 0px 10px 0px;"><Icon type="md-bookmark" size="23" style="margin-right: 5px"></Icon>座位信息</row>
              <Table border :loading="loading" :columns="columns3_0" :data="data"></Table>
            </div>
            <div v-if="data[0].order.type === 1">
              <row style="margin: 15px 0px 10px 0px;"><Icon type="md-bookmark" size="23" style="margin-right: 5px"></Icon>会员卡信息</row>
              <Table border :loading="loading" :columns="columns3_1" :data="data"></Table>
            </div>
            <div v-if="data[0].order.type === 2">
              <row style="margin: 15px 0px 10px 0px;"><Icon type="md-bookmark" size="23" style="margin-right: 5px"></Icon>优惠劵信息</row>
              <Table border :loading="loading" :columns="columns3_2" :data="data"></Table>
            </div>
            <div v-if="data[0].order.type === 3">
              <row style="margin: 15px 0px 10px 0px;"><Icon type="md-bookmark" size="23" style="margin-right: 5px"></Icon>商品信息</row>
              <Table border :loading="loading" :columns="columns3_3" :data="product"></Table>
            </div>
            <div>
              <row style="margin: 15px 0px 10px 0px;"><Icon type="md-bookmark" size="23" style="margin-right: 5px"></Icon>费用信息</row>
              <Table border :loading="loading" :columns="columns4" :data="data"></Table>
            </div>
            <div>
              <row style="margin: 15px 0px 10px 0px;"><Icon type="md-bookmark" size="23" style="margin-right: 5px"></Icon>操作信息</row>
              <Table border :loading="loading" :columns="columns5" :data="record"></Table>
            </div>
          </Card>
        </div>
      </div>
    </Card>
    <Modal v-model="create_show.value" title="创建">
      <Form ref="formCreate" :model="create_show" :label-width="100" :rules="rules">
        <Row>
          <FormItem label="备注:" prop="des">
            <Input class='textarea' :rows="5" :autosize="{maxRows:5,minRows: 5}" v-model="create_show.des" type="textarea"/>
          </FormItem>
        </Row>
      </Form>
      <div slot="footer">
        <Button @click="create_show.value = false">取消</Button>
        <Button type="primary" @click="submitCreate">确定</Button>
      </div>
    </Modal>
  </div>
</template>
<script>
  import { getOrder, getOrderDetail, getOrderRecord, updateOrder, deleteOrder, createOrderRecord, getOrderProduct } from '@/api/order';
  import { timestampFormat} from '@/libs/tools';
  export default {
    components: {

    },
    data() {
      return {
        loading: false,
        data: [],
        record: [],
        product: [],
        create_show: {
          value: false,
          des: '',
          operationType: '',
          operationId: '',
          operationName: '',
          orderId: '',
          orderStatusType: '',
          payId: ''
        },
        rules: {
          des: [
            {required: true, message: "必输项不能为空", trigger: 'blur'}
          ]
        },
        columns1: [
          {
            title: '订单编号',
            key: 'no',
            render: (h, params) => {
              return h('div', params.row.order.no);
            }
          },
          {
            title: '公司名称',
            key: 'companyName',
            render: (h, params) => {
              return h('div', params.row.order.companyName);
            }
          },
          {
            title: '订单类型',
            key: 'orderType',
            render: (h, params) => {
              return h('div', params.row.order.orderType);
            }
          },
          {
            title: '自动好评时间',
            key: 'defaultEvaluateTime',
            render: (h, params) => {
              return h('div', '15天');
            }
          },
          {
            title: '场馆地址',
            width: 300,
            key: 'stadiumAddr'
          }
        ],
        columns2: [
          {
            title: '顾客账号',
            key: 'customerUsername',
            render: (h, params) => {
              return h('div', params.row.customer.username);
            }
          },
          {
            title: '顾客电话',
            key: 'customerTel',
            render: (h, params) => {
              return h('div', params.row.customer.tel);
            }
          },
          {
            title: '顾客邮件',
            key: 'customerMail',
            render: (h, params) => {
              return h('div', params.row.customer.mail);
            }
          },
        ],
        columns3_0: [
          {
            title: '座位id',
            key: 'seatId'
          },
          {
            title: '开始时间',
            key: 'startTime'
          },
          {
            title: '结束时间',
            key: 'endTime'
          },
        ],
        columns3_1: [
          {
            title: '货号',
            key: 'vipCardNo',
            render: (h, params) => {
              return h('div', params.row.vipCard.no);
            }
          },
          {
            title: '名称',
            key: 'vipCardName',
            render: (h, params) => {
              return h('div', params.row.vipCard.name);
            }
          },
          {
            title: '价格',
            key: 'vipCardPrice',
            render: (h, params) => {
              return h('div','￥' + params.row.vipCard.originalPrice);
            }
          },
          {
            title: '类型',
            key: 'vipCardType',
            render: (h, params) => {
              return h('div', params.row.vipCard.vipCardType);
            }
          },
          {
            title: '具体信息',
            key: 'detail',
            width: 250,
            render: (h, params) => {
              switch (params.row.vipCard.type) {
                case 1:
                  return h('div', '总储值：￥' + params.row.vipCard.money);
                case 2:
                  return h('div', '总次数：' + params.row.vipCard.times);
                case 3:
                  return h('div', [
                    h('div', '开始时间：' + params.row.vipCard.startTime),
                    h('div', '结束时间：' + params.row.vipCard.endTime)
                  ]);
              }
            }
          },
        ],
        columns3_2: [],
        columns3_3: [
          {
            title: '货号',
            key: 'productNo',
            render: (h, params) => {
              return h('div', params.row.product.no);
            }
          },
          {
            title: '名称',
            key: 'productName',
            render: (h, params) => {
              return h('div', params.row.product.name);
            }
          },
          {
            title: '单价',
            key: 'productPrice',
            render: (h, params) => {
              return h('div', params.row.product.price);
            }
          },
          {
            title: '数量',
            key: 'num'
          }
        ],
        columns4: [
          {
            title: '合计',
            key: 'orderPrice',
            render: (h, params) => {
              const price = params.row.order.price + params.row.order.coupon + params.row.order.discount;
              return h('div', '￥'+ price);
            }
          },
          {
            title: '优惠劵',
            key: 'orderCoupon',
            render: (h, params) => {
              return h('div', '-￥' + params.row.order.coupon);
            }
          },
          {
            title: '折扣',
            key: 'orderDiscount',
            render: (h, params) => {
              return h('div', '-￥' + params.row.order.discount);
            }
          },
          {
            title: '是否使用会员卡',
            key: 'vipCardId',
            render: (h, params) => {
              if (params.row.order.vipCardId != null) {
                return h('div', '是');
              } else {
                return h('div', '否');
              }
            }
          },
          {
            title: '应付金额',
            key: 'amount',
            render: (h, params) => {
              return h('div', '￥'+ params.row.order.price);
            }
          }
        ],
        columns5: [
          {
            title: '操作者',
            key: 'operationName'
          },
          {
            title: '操作时间',
            key: 'createTime',
            render: (h, params) => {
              return h('div', timestampFormat(params.row.createTime, 'year'));
            }
          },
          {
            title: '操作者类型',
            key: 'operation'
          },
          {
            title: '订单状态',
            key: 'orderStatus'
          },
          {
            title: '备注',
            key: 'des'
          },
        ],
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      async fetchData() {
        this.data.length = 0
        this.record.length = 0
        this.product.length = 0
        let flag = false;
        if (typeof(this.$route.query.orderId) !== "undefined" && this.$route.query.orderId !== '') {
          this.loading = true
          const res1 = await getOrderDetail({id: this.$route.query.orderId, type: this.$route.query.type})
          if (res1.data.code === 200) {
            this.data.push(res1.data.data)
            flag = true;
          } else {
            this.data = [];
            this.$Message.warning(res1.data.msg);
          }
          if (flag) {
            const res2 = await getOrderRecord({id: this.$route.query.orderId})
            if (res2.data.code === 200) {
              this.record.push(...res2.data.data)
            } else {
              this.record = [];
              this.$Message.warning(res2.data.msg);
            }
            //为商品
            if (this.data[0].order.type === 3) {
              const res3 = await getOrderProduct({id: this.$route.query.orderId})
              if (res3.data.code === 200) {
                this.product.push(...res3.data.data)
                console.log(this.product)
              } else {
                this.record = [];
                this.$Message.warning(res3.data.msg);
              }
            }
          }
        }
        /*this.$route.query.orderId = '';*/
        setTimeout(() => {
          this.loading = false
        }, 300)
      },
      cancelOrder() {
        const vm = this
        this.$Modal.confirm({
          title: "提示",
          content: "是否要进行取消订单操作?",
          okText: "确定",
          cancelText: "取消",
          onOk: async () => {
            const res = await updateOrder(vm.data[0].order.id, {status: 6})
            if (res.data.code === 200) {
              this.$Message.success(res.data.msg);
              await this.fetchData();
            } else {
              this.$Message.error(res.data.msg);
            }
          }
        });
      },
      closeOrder() {
        const vm = this
        this.$Modal.confirm({
          title: "提示",
          content: "是否要进行关闭订单操作?",
          okText: "确定",
          cancelText: "取消",
          onOk: async () => {
            const res = await updateOrder(vm.data[0].order.id, {status: 5})
            if (res.data.code === 200) {
              this.$Message.success(res.data.msg);
              await this.fetchData();
            } else {
              this.$Message.error(res.data.msg);
            }
          }
        });
      },
      deleteOrder() {
        const vm = this
        this.$Modal.confirm({
          title: "提示",
          content: "是否要进行删除订单操作?",
          okText: "确定",
          cancelText: "取消",
          onOk: async () => {
            const res = await deleteOrder(vm.data[0].order.id, {deleteFlag: 1})
            if (res.data.code === 200) {
              this.$Message.success(res.data.msg);
              await this.fetchData();
            } else {
              this.$Message.error(res.data.msg);
            }
          }
        });
      },
      submitCreate() {
        this.$refs.formCreate.validate(async (valid) => {
          if (valid) {
            const operation = this.$store.state.user;
            const order = this.data[0].order;
            this.create_show.operationId = operation.userId;
            this.create_show.operationName = operation.nickname;
            this.create_show.orderId = order.id;
            this.create_show.orderStatusType = order.status;
            this.create_show.payId = order.payId;
            const res = await createOrderRecord(this.create_show)
            if (res.data.code === 200) {
              this.$Message.success(res.data.msg);
              Object.keys( this.create_show).forEach(key => {
                this.create_show[key] = '';
              });
              this.create_show.value = false;
              this.$emit('refresh');
              this.fetchData();
            }
          } else {
            this.$Message.warning(res.data.msg);
          }
        })
      }
    }
  }
</script>
<style  lang="scss" scoped>
</style>
