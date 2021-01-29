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
            <FormItem label="输入搜索:" prop="name" aria-required="true">
              <Input v-model="searchParams.name" @keyup.enter="fetchData" placeholder="场馆名称"/>
            </FormItem>
          </Col>
          <Col span="6">
            <FormItem label="公司:" prop="companyName" aria-required="true">
              <Input v-model="searchParams.companyName" @keyup.enter="fetchData" placeholder="公司名称"/>
            </FormItem>
          </Col>
          <Col span="6">
            <FormItem label="地址:" prop="address" aria-required="true">
              <Input v-model="searchParams.address" @keyup.enter="fetchData" placeholder="场馆地址"/>
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
        <Col span="4">
          <Button style="float: right" type="default" @click="createShow">新建</Button>
        </Col>
      </Row>
    </Card>
    <Card>
      <tables ref="tables" :loading="loading" :columns="columns" v-model="data"/>
      <Button style="margin: 10px 0;" type="primary" @click="exportExcel">导出为Csv文件</Button>
      <Page style="margin-top: 20px;float: right;" :total="searchParams.total" :page-size="searchParams.pageSize" show-total show-elevator show-sizer @on-change="handlePage" @on-page-size-change='handlePageSize'/>
    </Card>
    <Modal v-model="show_edit.value" title="编辑">
      <Form ref="formEdit" :model="show_edit" :label-width="100" :rules="formRules">
        <Row>
          <FormItem label="场馆名:" prop="name" aria-required="true">
            <Input style="width: 80%" v-model="show_edit.name"/>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="场馆地址" prop="addressArr" aria-required="true">
            <al-cascader v-if="show_edit.value" style="width: 80%" data-type="name" v-model="show_edit.addressArr" searchable/>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="详细地址:" prop="detailAddress" aria-required="true">
            <Input style="width: 80%" v-model="show_edit.detailAddress"/>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="是否隐藏:" aria-required="true">
            <RadioGroup v-model="show_edit.hidden">
              <Radio :label="0">否</Radio>
              <Radio :label="1">是</Radio>
            </RadioGroup>
          </FormItem>
        </Row>
      </Form>
      <div slot="footer">
        <Button @click="show_edit.value = false">取消</Button>
        <Button type="primary" @click="submitEdit">确定</Button>
      </div>
    </Modal>
    <Modal v-model="show_create.value" title="创建">
      <Form ref="formCreate" :model="show_create" :label-width="100" :rules="formRules">
        <FormItem label="公司名" prop="userId">
          <Select style="width: 300px" v-model="show_create.userId" filterable remote :remote-method="debounce(searchUser, 300)" :loading="user_loading" placeholder="请输入搜索用户名称">
            <Option v-for="(option, index) in user_option" :value="option.value" :key="index">{{option.label}}</Option>
          </Select>
        </FormItem>
        <Row>
          <FormItem label="场馆名:" prop="name" aria-required="true">
            <Input style="width: 80%" v-model="show_create.name"/>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="场馆地址" prop="addressArr" aria-required="true">
            <al-cascader v-if="show_create.value" style="width: 80%" data-type="name" v-model="show_create.addressArr" searchable/>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="详细地址:" prop="detailAddress" aria-required="true">
            <Input style="width: 80%" v-model="show_create.detailAddress"/>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="是否隐藏:" aria-required="true">
            <RadioGroup v-model="show_create.hidden">
              <Radio :label="0">否</Radio>
              <Radio :label="1">是</Radio>
            </RadioGroup>
          </FormItem>
        </Row>
      </Form>
      <div slot="footer">
        <Button @click="show_edit.value = false">取消</Button>
        <Button type="primary" @click="submitCreate">确定</Button>
      </div>
    </Modal>
  </div>
</template>
<script>
  import Tables from '_c/tables'
  import { getStadium, createStadium, updateStadium, deleteStadium } from '@/api/stadium';
  import { getUser} from '@/api/user';
  import {timestampFormat} from '@/libs/tools';
  import lodash from "lodash";
  const searchParams = {
    name: '',
    userId: '',
    companyName: '',
    address: '',
    page: 1,
    pageSize: 10,
    total: 0
  }
  export default {
    name: 'tables_page',
    components: {
      Tables
    },
    data () {
      return {
        columns: [
          { title: 'ID', key: 'id',width: 80, sortable: true },
          { title: '公司名称', key: 'companyName'},
          { title: '场馆名称', key: 'name'},
          { title: '地址', key: 'address'},
          { title: '详细地址', key: 'detailAddress'},
          { title: '创建时间', key: 'createTime',
            render: (h, params) => {
              return h('div', timestampFormat(params.row.createTime,'year'));
            }
          },
          { title: '修改时间', key: 'modifyTime',
            render: (h, params) => {
              return h('div', timestampFormat(params.row.modifyTime,'year'));
            }
          },
          { title: '是否启用', key: 'status',
            render: (h, params) => {
              return h('i-switch', {
                props: {
                  size: 'large',
                  value: params.row.hidden === 0
                },
                on: {
                  'on-change': async (value) => {
                    await updateStadium(params.row.id, {hidden: value ? 0 : 1});
                  }
                }
              })
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
                      this.openRoomPage(params.row.id)
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
                      this.show_edit.value = true;
                      this.show_edit.id = params.row.id;
                      this.show_edit.name = params.row.name;
                      this.show_edit.addressArr.length = 0;
                      this.show_edit.address = params.row.address;
                      if (!!this.show_edit.address) {
                        this.show_edit.addressArr = params.row.address.split("/");
                      }
                      this.show_edit.detailAddress = params.row.detailAddress;
                      this.show_edit.hidden = params.row.hidden;
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
                      await deleteStadium(params.row.id)
                      this.$Message.success('删除成功！');
                      this.fetchData()
                    }
                  }
                }, [h('Button', {props: {type: 'error', size: 'small'}}, '删除')])
              ]);
            }}
        ],
        data: [],
        searchParams: searchParams,
        loading: true,
        show_edit: {
          value: false,
          id: '',
          name: '',
          addressArr: [],
          address: '',
          detailAddress: '',
          hidden: 0
        },
        show_create: {
          value: false,
          userId: '',
          name: '',
          addressArr: [],
          address: '',
          detailAddress: '',
          hidden: 0
        },
        user_option: [],
        user_loading: false,
        formRules: {
          userId: [
            { required: true, message: '必输项不能为空', trigger: 'change' },
          ],
          name: [
            {required: true, message: "必输项不能为空", trigger: 'blur'}
          ],
          detailAddress:[
            {required: true, message: "必输项不能为空", trigger: 'change'}
          ],
          addressArr: [
            {required: true, trigger: 'change',type: 'array',message: '必选项不能为空'}
          ]
        }
      }
    },
    created() {
      this.fetchData();
    },
    methods: {
      exportExcel () {
        this.$refs.tables.exportCsv({
          filename: `stadium-table-${(new Date()).valueOf()}.csv`
        })
      },
      async fetchData() {
        this.loading = true
        if (typeof(this.$route.query.userId) !== "undefined" && this.$route.query.userId !== '') {
          this.searchParams.userId = this.$route.query.userId
          this.$route.query.userId = ''
        }
        const res = await getStadium(this.searchParams)
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
          this.loading = false;
        }, 300)
      },
      createShow() {
        this.show_create.value = !this.show_create.value
        this.createToEmpty();
      },
      submitEdit() {
        this.$refs.formEdit.validate(async (valid) => {
          if (valid) {
            this.show_edit.address = this.show_edit.addressArr.join('/');
            const res = await updateStadium(this.show_edit.id,this.show_edit)
            if (res.data.code === 200) {
              this.$Message.success(res.data.msg);
              this.show_edit.value = false;
              this.$emit('refresh');
              this.fetchData();
            }
          }
        })
      },
      submitCreate() {
        this.$refs.formCreate.validate(async (valid) => {
          if (valid) {
            this.show_create.address = this.show_create.addressArr.join('/');
            const res = await createStadium(this.show_create)
            if (res.data.code === 200) {
              this.$Message.success(res.data.msg);
              this.show_create.value = false;

              this.$emit('refresh');
              this.fetchData();
            }
          }
        })
      },
      async searchUser(query) {
        if (query) {
          this.user_loading = true
          const res = await getUser({companyName: query})
          if (res.data.code === 200) {
            this.user_option = res.data.data.rows.map(item => {
              return {
                value: item.id + '',
                label: item.companyName
              }
            })
            setTimeout(() => {
              this.user_loading = false
            }, 200)
          } else {
            this.user_option = []
          }
        } else {
          this.user_option = []
        }
      },
      debounce(fun, time) {
        return lodash.debounce(fun, time)
      },
      handlePage(page) {
        this.searchParams.page = page
        this.fetchData()
      },
      handlePageSize(value) {
        this.searchParams.pageSize = value
        this.fetchData()
      },
      paramToEmpty() {
        this.searchParams.name = '';
        this.searchParams.userId = '';
        this.searchParams.companyName = '';
        this.searchParams.address = '';
      },
      createToEmpty() {
        this.show_create.userId = '';
        this.show_create.name = '';
        this.show_create.addressArr = [];
        this.show_create.address = '';
        this.show_create.detailAddress = '';
        this.show_create.hidden = 0;
      },
      openRoomPage(stadiumId) {
        const route = {
          name: 'room',
          query: {
            stadiumId
          },
          meta: {
            title: `${stadiumId} 场馆的自习室情况`
          }
        }
        this.$router.push(route)
      }
    }
  }
</script>

<style>

</style>
