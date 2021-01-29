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
            <FormItem label="输入搜索:" prop="no" aria-required="true">
              <Input v-model="searchParams.no"  placeholder="商品货号" @on-search="fetchData" />
            </FormItem>
          </Col>
          <Col span="6">
            <FormItem label="商品:" prop="id" aria-required="true">
              <Input v-model="searchParams.name"  placeholder="商品名称" @on-search="fetchData" />
            </FormItem>
          </Col>
          <Col span="6">
            <FormItem label="公司:" prop="companyName" aria-required="true">
              <Input v-model="searchParams.companyName"  placeholder="公司名称" @on-search="fetchData" />
            </FormItem>
          </Col>
          <Col span="6">
            <FormItem label="商品类型">
              <Cascader :data="product_type" v-model="searchSelected" clearable filterable placeholder="全部"></Cascader>
            </FormItem>
          </Col>
        </Row>
      </Form>
    </Card>
    <Card style="margin-bottom: 10px">
      <Row>
        <Col span="18">
          <Icon size="18" type="md-list"></Icon>
          <label style="font-size: 15px;">&nbsp;数据列表</label>
        </Col>
        <Col span="6">
          <Button type="default" style="float: right;margin-right: 10px;" @click="modalNoShowCSV">上传CSV</Button>
          <Button type="default" style="float: right;margin-right: 10px;" @click="modalNoShowEXCEL">上传EXCEL</Button>
          <Button type="default" style="float: right;margin-right: 10px;" @click="modalNoShowPaste">粘贴表格数据</Button>
          <Button type="default" style="float: right;margin-right: 10px;" @click="openCreatePage('')">新建</Button>
        </Col>
      </Row>
    </Card>
    <Card>
      <tables ref="tables" @on-select-all-cancel="handleCancelSelectAll"
              @on-select-all="handleSelectAll"
              @on-select="handleSelect"
              @on-select-cancel="handleCancel"
              :loading="loading" :columns="columns" v-model="data"/>

      <Select v-model="operateType" filterable placeholder="批量操作" style="width: 130px;margin-right: 15px">
        <Option v-for="(operate, index) in operates" :value="operate.value" :key="index">{{operate.label}}</Option>
      </Select>
      <Button style="margin: 10px 0;" type="primary" @click="batchConfirm">确定</Button>
      <Page style="margin-top: 20px;float: right;" :total="searchParams.total" :page-size="searchParams.pageSize" show-total show-elevator show-sizer @on-change="handlePage" @on-page-size-change='handlePageSize'/>
      <upload-excel :create_modal_show_excel="create_modal_show_excel" @noshow="modalNoShowEXCEL" />
      <upload-paste :create_modal_show_csv="create_modal_show_csv" @noshow="modalNoShowCSV" />
      <upload-paste :create_modal_show_paste="create_modal_show_paste" @noshow="modalNoShowPaste" />
    </Card>
    <Modal v-model="detail_show.value" title="详情" footer-hide>
      <Form ref="formData2" :model="detail_show" :label-width="100" style="margin-left: 10%">
        <Row>
          <FormItem label="商品名称:" prop="name">
            <span>{{detail_show.name}}</span>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="原价:" prop="originalPrice">
            <span>{{detail_show.originalPrice}}</span>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="现价:" prop="price">
            <span>{{detail_show.price}}</span>
          </FormItem>
        </Row>
        <!--限时--> <!--期限卡-->
        <Row v-if="detail_show.conType === 1 || detail_show.conType === 5">
          <FormItem label="开始有效时间:" prop="startTime">
            <span>{{detail_show.startTime}}</span>
          </FormItem>
        </Row>
        <Row v-if="detail_show.conType === 1 || detail_show.conType === 5">
          <FormItem label="结束有效时间:" prop="endTime">
            <span>{{detail_show.endTime}}</span>
          </FormItem>
        </Row>
        <!--限量-->
        <Row  v-if="detail_show.conType === 2">
          <FormItem label="数量:" prop="salesLimit">
            <span>{{detail_show.salesLimit}}</span>
          </FormItem>
        </Row>
        <!--储值卡-->
        <Row v-if="detail_show.conType === 1 || detail_show.conType === 2 || detail_show.conType === 3">
          <FormItem label="储值:" prop="money">
            <span>{{detail_show.money}}</span>
          </FormItem>
        </Row>
        <!--计次卡-->
        <Row  v-if="detail_show.conType === 4">
          <FormItem label="次数" prop="times">
            <span>{{detail_show.times}}</span>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="描述:" prop="des">
            <span>{{detail_show.des}}</span>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="标签:" prop="status">
            <CheckboxGroup v-model="detail_show.statusArr">
              <Checkbox v-for="option in product_status" :label="option.value">{{option.label}}</Checkbox>
            </CheckboxGroup>
          </FormItem>
        </Row>
      </Form>
    </Modal>
  </div>
</template>
<script>
  import Tables from '_c/tables'
  import { getProduct,deleteProduct } from '@/api/product';
  import uploadExcel from '@/components/uploadExcel';
  import uploadCsv from '@/components/uploadCsv';
  import uploadPaste from '@/components/uploadPaste';
  import {timestampFormat} from '@/libs/tools';
  const searchParams = {
    id: '',
    no: '',
    name: '',
    type: '',
    companyName: '',
    conType: '',
    page: 1,
    pageSize: 10,
    total: 0
  }
  const operates = [
    {
      label: "生成CSV",
      value: "spannedCSV"
    },
    {
      label: "批量删除",
      value: "deleteBatch"
    }
  ]
  const product_type = [
    {
      label: '优惠劵',
      value: 1,
      children: [
        {
          value: 1,
          label: '限时'
        },
        {
          value: 2,
          label: '限量'
        }
      ]
    },
    {
      label: '会员卡',
      value: 2,
      children: [
        {
          value: 3,
          label: '储值卡'
        },
        {
          value: 4,
          label: '计次卡'
        },
        {
          value: 5,
          label: '期限卡'
        }
      ]
    }
  ]
  const product = {
    '2':'限时优惠劵',
    '3':'限量优惠劵',
    '5':'储值会员卡',
    '6':'计次会员卡',
    '7':'期限会员卡'
  }
  const product_status = [
    {
      label: '上架',
      value: 1
    },
    {
      label: '新品',
      value: 2
    },
    {
      label: '推荐',
      value: 3
    }
  ]
  export default {
    components: {
      Tables,
      uploadExcel,
      uploadCsv,
      uploadPaste
    },
    data() {
      return {
        columns:
          [
            {
              type: 'selection',
              width: 60,
              align: 'center',
              sortable: true
            },
            {
              title: 'ID',
              width: 60,
              key: 'id'
            },
            {
              title: '公司名称',
              key: 'companyName'
            },
            {
              title: '商品名称',
              key: 'name'
            },
            {
              title: '价格/货号',
              key: 'priceAndNo',
              width: 200,
              render:(h, params) => {
                return h('div',[
                  h('div','价格: ￥'+params.row.price),
                  h('div','货号:'+params.row.no)
                ])
              }
            },
            {
              title: '类型',
              key: 'kind',
              render: (h, params) => {
                return h('div', this.product[params.row.conType+params.row.type])
              }
            },
            {
              title: '销量',
              key: 'sales'
            },
            {
              title: '创建时间',
              key: 'createTime',
              render: (h, params) => {
                return h('div', timestampFormat(params.row.createTime,'year'));
              }
            },
            {
              title: '修改时间',
              key: 'modifyTime',
              render: (h, params) => {
                return h('div', timestampFormat(params.row.modifyTime,'year'));
              }
            },
            { title: '操作', key: 'action', width: 200, align: 'center', render: (h, params) => {
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
                        Object.keys(params.row).forEach(key => {
                          if (key === 'status') {
                            this.detail_show.statusArr = params.row[key].split(',').map(item => parseInt(item))
                          } else {
                            this.detail_show[key] = params.row[key]
                          }
                        })
                        console.log(this.detail_show)
                        this.detail_show.value = true;
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
                      click: () => {
                        this.openCreatePage(params.row.id);
                      }
                    }
                  }, '编辑')
                ]);
              }}
          ],
        data: [],
        loading: true,
        searchParams: searchParams,
        product_type: product_type,
        product_status: product_status,
        product: product,
        operates: operates,
        operateType: null,
        user: this.$store.state.user,
        searchSelected: [],
        selected: [],
        create_modal_show_excel: false,
        create_modal_show_paste: false,
        create_modal_show_csv: false,
        detail_show: {
          value: false,
          no: '',
          name: '',
          type: '',
          userId: '',
          conType: '',
          times: '',
          startTime: '',
          endTime: '',
          money: '',
          originalPrice: '',
          price: '',
          status: '',
          statusArr: [],
          des: ''
        }
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      async fetchData() {
        this.loading = true
        if (this.searchSelected.length>0) {
          this.searchParams.type = this.searchSelected[0];
          this.searchParams.conType = this.searchSelected[1];
        }
        const res = await getProduct(this.searchParams)
        if (res.data.code === 200) {
          this.data = res.data.data.rows.map(item => {
            return {
              ...item
            }
          })
          this.searchParams.total = res.data.data.total
        } else {
          this.data=[];
          this.$Message.warning(res.data.msg);
        }
        setTimeout(() => {
          this.loading = false
        }, 300)
      },
      modalNoShowCSV() {
        this.create_modal_show_csv = !this.create_modal_show_csv
      },
      modalNoShowEXCEL() {
        this.create_modal_show_excel = !this.create_modal_show_excel
      },
      modalNoShowPaste() {
        this.create_modal_show_paste = !this.create_modal_show_paste
      },
      openCreatePage(id) {
        const route = {
          name: 'editProduct',
          query: {
            productId:id
          },
          meta: {
            title: '编辑商品'
          }
        }
        this.$router.push(route)
      },
      exportExcel () {
        this.$refs.tables.exportCsv({
          filename: `product-table-${(new Date()).valueOf()}.csv`,
          columns: this.columns.filter((col, index) => index > 0),
          data: this.data.filter((item, index) => {
            for (let i = 0; i < this.selected.length; i++) {
              if (item.id === this.selected[i]) {
                return true
              }
            }
            return false
          })
        })
      },
      batchConfirm() {
        if (this.operateType == null) {
          this.$Message.warning("请选择操作类型");
          return;
        }
        if(this.selected==null||this.selected.length<1) {
          this.$Message.warning("请选择要操作的商品");
          return;
        }
        switch (this.operateType) {
          case this.operates[0].value:
            this.exportExcel();
            break;
          case this.operates[1].value:
            this.$Modal.confirm({
              title: "删除提示",
              content: "是否要进行删除操作?",
              okText: "确定",
              cancelText: "取消",
              onOk: async () => {
                const res = await deleteProduct(JSON.stringify(this.selected));
              }
            });
            break;
        }
      },
      handlePage(page) {
        this.searchParams.page = page;
        let start = (page-1)*this.searchParams.pageSize;
        let end = page * this.searchParams.pageSize;
        this.handleTableChecked(this.data.splice(start,end))
        this.fetchData()
      },
      handlePageSize(value) {
        this.searchParams.pageSize = value
        this.fetchData()
      },
      paramToEmpty() {
        Object.keys(this.searchParams).forEach(key => {
          this.searchParams[key] = '';
        })
        this.searchParams.page = 1;
        this.searchParams.pageSize = 10;
        this.searchParams.total = 0;
        this.searchSelected = [];
      },
      handleSelect(selection, row) {
        this.selected.push(row.id);
        this.data.forEach(item => {
          if (item.id === row.id) {
            item['_checked'] = true
          }
        })
      },
      handleSelectAll(selection) {
        selection.forEach(item => {
          this.selected.push(item.id)
        })
        this.selected = Array.from(new Set(this.selected))
      },
      handleCancel(selection, row) {
        this.selected.forEach((item, index) => {
          if (row.id === item) {
            this.selected.splice(index, 1)
          }
        })
      },
      handleCancelSelectAll(selection) {
        this.data.forEach(item => {
          this.selected.forEach((e, index) => {
            if (item.id === e) {
              this.selected.splice(index, 1)
            }
          })
        })
      },
      handleTableChecked (data) {//还原有勾选的数据
        this.selected.forEach(item => {
          data.forEach(e => {
            if (item === e.id) {
              e['_checked'] = true
            }
          })
        })
      }
    }
  }
</script>
<style lang="scss" scoped>

</style>
