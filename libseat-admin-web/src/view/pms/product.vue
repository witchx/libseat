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
              <Input v-model="searchParams.no"  placeholder="商品货号"/>
            </FormItem>
          </Col>
          <Col span="6">
            <FormItem label="商品:" prop="id" aria-required="true">
              <Input v-model="searchParams.name"  placeholder="商品名称"/>
            </FormItem>
          </Col>
          <Col span="6">
            <FormItem label="备注:" prop="id" aria-required="true">
              <Input v-model="searchParams.des"  placeholder="商品备注"/>
            </FormItem>
          </Col>
          <Col span="6">
            <FormItem label="公司:" prop="companyName" aria-required="true">
              <Input v-model="searchParams.companyName"  placeholder="公司名称"/>
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
          <Button type="default" style="float: right;margin-right: 10px;" @click="createShow">新建</Button>
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
    <Modal v-model="create_show.value" title="创建">
      <Form ref="formCreate" :model="create_show" :label-width="100" :rules="rules">
        <Row>
          <FormItem label="公司:" prop="userId">
            <Select  style="width:80%;" v-model="create_show.userId" filterable remote :remote-method="debounce(searchUser, 300)" :loading="user_loading" placeholder="请输入搜索用户名称">
              <Option v-for="(option, index) in user_option" :value="option.value" :key="index">{{option.label}}</Option>
            </Select>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="商品名称:" prop="name">
            <Input v-model="create_show.name" style="width: 80%;"/>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="金额:" prop="price">
            <Input v-model="create_show.price" type="number" style="width: 80%;"/>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="数量:" prop="num">
            <Input v-model="create_show.num" type="number" style="width: 80%;"/>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="备注:" prop="des">
            <Input v-model="create_show.des" style="width: 80%;"/>
          </FormItem>
        </Row>
      </Form>
      <div slot="footer">
        <Button @click="create_show.value = false">取消</Button>
        <Button type="primary" @click="submitCreate">确定</Button>
      </div>
    </Modal>
    <Modal v-model="edit_show.value" title="编辑">
      <Form ref="formEdit" :model="edit_show" :label-width="100" :rules="rules">
        <Row>
          <FormItem label="公司:">
            <Input v-model="edit_show.companyName" style="width: 80%;" readonly/>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="货号:">
            <Input v-model="edit_show.no" style="width: 80%;" readonly/>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="商品名称:" prop="name">
            <Input v-model="edit_show.name" style="width: 80%;"/>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="金额:" prop="price">
            <Input v-model="edit_show.price" type="number" style="width: 80%;"/>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="数量:" prop="num">
            <Input v-model="edit_show.num" type="number" style="width: 80%;"/>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="描述:" prop="des">
            <Input v-model="edit_show.des" style="width: 80%;"/>
          </FormItem>
        </Row>
      </Form>
      <div slot="footer">
        <Button @click="edit_show.value = false">取消</Button>
        <Button type="primary" @click="submitEdit">确定</Button>
      </div>
    </Modal>
  </div>
</template>
<script>
  import Tables from '_c/tables'
  import { getProduct, createProduct, updateProduct, deleteProduct,deleteProductBatch } from '@/api/product';
  import uploadExcel from '@/components/uploadExcel';
  import uploadCsv from '@/components/uploadCsv';
  import uploadPaste from '@/components/uploadPaste';
  import { getUser} from '@/api/user';
  import {validateNumber} from '@/libs/validate';
  import lodash from "lodash";
  const searchParams = {
    id: '',
    no: '',
    name: '',
    companyName: '',
    des: '',
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
              title: '货号',
              key: 'no'
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
              title: '价格',
              key: 'price',
              width: 200,
              render:(h, params) => {
                return h('div','￥'+params.row.price)
              }
            },
            {
              title: '数量',
              key: 'num'
            },
            {
              title: '备注',
              key: 'des'
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
                        Object.keys(this.edit_show).forEach(key => {
                          this.edit_show[key] = params.row[key];
                        })
                        this.edit_show.value = true;
                      }
                    }
                  }, '编辑'),
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
                          await deleteProduct(params.row.id)
                          this.$Message.success('删除成功！');
                          this.fetchData()
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
              }}
          ],
        data: [],
        loading: true,
        searchParams: searchParams,
        operates: operates,
        operateType: null,
        searchSelected: [],
        selected: [],
        user_option: [],
        user_loading: false,
        create_modal_show_excel: false,
        create_modal_show_paste: false,
        create_modal_show_csv: false,
        create_show: {
          value: false,
          no: '',
          name: '',
          userId: '',
          companyName: '',
          price: '',
          des: '',
          num: ''
        },
        edit_show: {
          value: false,
          id: '',
          no: '',
          name: '',
          userId: '',
          companyName: '',
          price: '',
          des: '',
          num: ''
        },
        rules: {
          name: [
            {required: true, message: "必输项不能为空", trigger: 'blur'}
          ],
          userId: [
            {required: true, type: 'number', message: "必输项不能为空", trigger: 'change'}
          ],
          price: [
            { validator: validateNumber, trigger: 'blur'}
          ],
          num: [
            { validator: validateNumber, trigger: 'blur'}
          ],
          des: [
            {required: true, message: "必输项不能为空", trigger: 'blur'}
          ]
        }
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      async fetchData() {
        this.loading = true
        if (typeof(this.$route.query.userId) !== "undefined" && this.$route.query.userId !== '') {
          this.searchParams.id = this.$route.query.userId;
          this.$route.query.userId = '';
        }
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
      createShow() {
        this.createToEmpty();
        this.create_show.value = true;
      },
      createToEmpty() {
        Object.keys( this.create_show).forEach(key => {
          this.create_show[key] = '';
        });
      },
      submitCreate() {
        this.$refs.formCreate.validate(async (valid) => {
          if (valid) {
            const res = await createProduct(this.create_show)
            if (res.data.code === 200) {
              this.$Message.success(res.data.msg);
              this.create_show.value = false;
              this.$emit('refresh');
              this.fetchData();
            }
          } else {
            this.$Message.warning("");
          }
        })
      },
      submitEdit() {
        this.$refs.formEdit.validate(async (valid) => {
          if (valid) {
            const res = await updateProduct(this.edit_show.id, this.edit_show)
            if (res.data.code === 200) {
              this.$Message.success(res.data.msg);
              this.edit_show.value = false;
              this.$emit('refresh');
              this.fetchData();
            }
          }
        })
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
                const res = await deleteProductBatch(JSON.stringify(this.selected));
              }
            });
            break;
        }
      },
      async searchUser(query) {
        if (query) {
          this.user_loading = true
          const res = await getUser({companyName: query})
          if ( res.data.code === 200 ) {
            this.user_option = res.data.data.rows.map(item => {
              return {
                value: item.id,
                label: item.companyName
              }
            })
          } else {
            this.user_option = [];
          }
          setTimeout(() => {
            this.user_loading = false
          }, 200)
        } else {
          this.user_option = [];
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
      },
      debounce(fun, time) {
        return lodash.debounce(fun, time)
      }
    }
  }
</script>
<style lang="scss" scoped>

</style>
