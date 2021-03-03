<template>
    <div>
      <Card style="margin-bottom: 10px">
        <Form ref="searchFormRef" :model="searchParams" :label-width="100">
          <Row style="margin-bottom: 15px">
            <Col span="20">
              <Icon size="18" type="md-search"></Icon>
              <label style="font-size: 15px;">&nbsp;筛选搜索</label>
            </Col>
            <Col span="4">
              <Button style="float: right;margin-right: 10px;" type="default" @click="fetchData">确定</Button>
              <Button style="float: right;margin-right: 10px;" type="default" @click="paramToEmpty">重置</Button>
            </Col>
          </Row>
          <Row :gutter="16">
            <Col span="6">
              <FormItem label="输入搜索:">
                <Input v-model="searchParams.no" @keyup.enter="fetchData" placeholder="订单号"/>
              </FormItem>
            </Col>
            <Col span="6">
              <FormItem label="公司:">
                <Input v-model="searchParams.company" @keyup.enter="fetchData" placeholder="公司名称"/>
              </FormItem>
            </Col>
            <Col span="6">
              <FormItem label="顾客:">
                <Input v-model="searchParams.customer" @keyup.enter="fetchData" placeholder="顾客名称"/>
              </FormItem>
            </Col>
            <Col span="6">
              <FormItem label="订单状态">
                <Select v-model="searchParams.status" filterable placeholder="全部">
                  <Option v-for="(option, index) in status_option" :value="option.value" :key="index">{{option.label}}</Option>
                </Select>
              </FormItem>
            </Col>
            <Col span="6">
              <FormItem label="订单进度">
                <Select v-model="searchParams.progress" filterable placeholder="全部">
                  <Option v-for="(option, index) in progress_option" :value="option.value" :key="index">{{option.label}}</Option>
                </Select>
              </FormItem>
            </Col>
            <Col span="6">
              <FormItem label="订单类型">
                <Select v-model="searchParams.type" filterable placeholder="全部">
                  <Option v-for="(option, index) in type_option" :value="option.value" :key="index">{{option.label}}</Option>
                </Select>
              </FormItem>
            </Col>
            <Col span="8">
              <FormItem label="提交时间:" aria-required="true">
                <DatePicker style="width: 80%;" ref="formDate"  @on-change="handleChange" format="yyyy-MM-dd HH:mm:ss" separator="  至  " type="datetimerange" placeholder="请选择时间"></DatePicker>
              </FormItem>
            </Col>
          </Row>
        </Form>
        <div style="clear: both"></div>
      </Card>
      <Card style="margin-bottom: 10px">
        <Row>
          <Col span="20">
            <Icon size="18" type="md-list"></Icon>
            <label style="font-size: 15px;">&nbsp;数据列表</label>
          </Col>
        </Row>
      </Card>
      <Card>
        <tables ref="tables" v-model="data" :columns="columns"/>
        <Button style="margin: 10px 0;" type="primary" @click="exportExcel">导出为Csv文件</Button>
        <Page style="margin-top: 20px;float: right;" :total="searchParams.total" :page-size="searchParams.pageSize" show-total show-elevator show-sizer @on-change="handlePage" @on-page-size-change='handlePageSize'/>
      </Card>
    </div>
</template>
<script>
  import { getOrder, updateOrder, deleteOrder } from '@/api/order';
  import { timestampFormat} from '@/libs/tools';
  import Tables from '_c/tables'

  const searchParams = {
    no: '',
    company: '',
    customer: '',
    createStartTime: '',
    createEndTime: '',
    progress: '',
    status: '',
    page: 1,
    pageSize: 10,
    total: 0
  }
  const progress_option = [
    {
      label: "已提交",
      value: 1
    },
    {
      label: "已支付",
      value: 2
    },
    {
      label: "已确认",
      value: 3
    },
    {
      label: "已完成",
      value: 4
    }
  ]
  const status_option = [
    {
      label: "待支付",
        value: 1
    },
    {
      label: "待确认",
        value: 2
    },
    {
      label: "待评价",
        value: 3
    },
    {
      label: "已完成",
        value: 4
    },
    {
      label: "已关闭",
      value: 5
    },
    {
      label: "已取消",
      value: 6
    }
  ]
  const type_option = [
    {
      label: "座位",
      value: 0
    },
    {
      label: "会员卡",
      value: 1
    },
    {
      label: "优惠劵",
      value: 2
    },
    {
      label: "商品",
      value: 3
    },
  ]
  export default {
    name: 'tables_page',
    components: {
      Tables
    },
    data () {
      return {
        columns: [
          {title: 'ID', key: 'id', width: 80, sortable: true},
          {title: '订单编号', key: 'no'},
          {title: '订单类型', key: 'orderType'},
          {title: '提交时间', key: 'createTime',
            render: (h, params) => {
              return h('div', timestampFormat(params.row.createTime, 'year'));
            }
          },
          {title: '公司名称', key: 'companyName'},
          {title: '顾客名称', key: 'customerName'},
          {title: '订单金额', key: 'price',
            render: (h, params) => {
              return h('div', '￥'+ params.row.price);
            }
          },
          {title: '订单进度', key: 'orderProgress'},
          {title: '订单状态', key: 'orderStatus'},
          {
            title: '操作', width: 200, key: 'action', align: 'center',
            render: (h, params) => {
              if ((params.row.status > 3) || (params.row.type === 0 && params.row.status === 2)) {
                return h('div', [
                  h('Button', {
                    props: {
                      type: 'default',
                      size: 'small'
                    },
                    style: {
                      marginRight: '5px',
                    },
                    on: {
                      click: () => {
                        this.showDetail(params.row.id,params.row.type)
                      }
                    }
                  }, '查看'),
                  h('Poptip', {
                      props: {
                        confirm: true,
                        title: "确定删除？"
                      },
                      style: {
                        textAlign: 'left'
                      },
                      on: {
                        'on-ok': async () => {
                          await this.delete(params.row.id)
                        }
                      }
                    },
                    [h('Button', {
                      props: {
                        type: 'error',
                        size: 'small'
                      },
                      style: {
                        marginRight: '5px',
                      }
                    }, '删除')])
                ]);
              } else if (params.row.status === 1) {
                return h('div', [
                  h('Button', {
                    props: {
                      type: 'default',
                      size: 'small'
                    },
                    style: {
                      marginRight: '5px',
                    },
                    on: {
                      click: () => {
                        this.showDetail(params.row.id,params.row.type)
                      }
                    }
                  }, '查看'),
                  h('Button', {
                    props: {
                      type: 'default',
                      size: 'small'
                    },
                    style: {
                      marginRight: '5px',
                    },
                    on: {
                      click: async () => {
                        const res = await updateOrder(params.row.id, {status: 5})
                        if (res.data.code === 200) {
                          this.$Message.success(res.data.msg);
                          await this.fetchData();
                        } else {
                          this.$Message.error(res.data.msg);
                        }
                      }
                    }
                  }, '关闭')
                ]);
              } else if (params.row.status === 2 && params.row.type === 0) {
                return h('div', [
                  h('Button', {
                    props: {
                      type: 'default',
                      size: 'small'
                    },
                    style: {
                      marginRight: '5px',
                    },
                    on: {
                      click: () => {
                        this.showDetail(params.row.id,params.row.type)
                      }
                    }
                  }, '查看'),
                  h('Button', {
                    props: {
                      type: 'default',
                      size: 'small'
                    },
                    style: {
                      marginRight: '5px',
                    },
                    on: {
                      click: async () => {
                        const res = await updateOrder(params.row.id, {status: 6})
                        if (res.data.code === 200) {
                          this.$Message.success(res.data.msg);
                          await this.fetchData();
                        } else {
                          this.$Message.error(res.data.msg);
                        }
                      }
                    }
                  }, '取消')
                ]);
              } else {
                return h('div', [
                  h('Button', {
                    props: {
                      type: 'default',
                      size: 'small'
                    },
                    style: {
                      marginRight: '5px',
                    },
                    on: {
                      click: () => {
                        this.showDetail(params.row.id,params.row.type)
                      }
                    }
                  }, '查看')
                ]);
              }
            }
          }
        ],
        searchParams: searchParams,
        data: [],
        loading: true,
        progress_option: progress_option,
        type_option: type_option,
        status_option: status_option
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      exportExcel() {
        this.$refs.tables.exportCsv({
          filename: `order-table-${(new Date()).valueOf()}.csv`
        })
      },
      async fetchData() {
        this.loading = true
        const res = await getOrder(this.searchParams)
        if (res.data.code === 200) {
          this.data = res.data.data.rows.map(item => {
            return {
              ...item,
              edit: false
            }
          })
          this.searchParams.total = res.data.data.total
        } else {
          this.data = [];
          this.$Message.warning(res.data.msg);
        }
        setTimeout(() => {
          this.loading = false
        }, 300)
      },
      showDetail(orderId,type) {
        const route = {
          name: 'orderDetail',
          query: {
            orderId,
            type
          },
          meta: {
            title: '商品详情'
          }
        }
        this.$router.push(route)
      },
      async delete(id) {
        await deleteOrder(id, {deleteFlag: 1})
        this.$Message.success('删除成功！');
        await this.fetchData()
      },
      paramToEmpty() {
        this.$refs.formDate.handleClear();
        Object.keys(this.searchParams).forEach(key => {
          this.searchParams[key] = '';
        })
        this.searchParams.page = 1;
        this.searchParams.pageSize = 10;
        this.searchParams.total = 0;
      },
      handlePage(page) {
        this.searchParams.page = page
        this.fetchData()
      },
      handlePageSize(value) {
        this.searchParams.pageSize = value
        this.fetchData()
      },
      handleChange(date) {
        this.searchParams.createTimeStart = date[0];
        this.searchParams.createTimeEnd = date[1];
      },
    }
  }
</script>
<style lang="scss" scoped>

</style>
