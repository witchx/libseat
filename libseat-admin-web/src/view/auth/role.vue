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
            <FormItem label="角色名称:" prop="account" aria-required="true">
                <Input v-model="searchParams.name" @keyup.enter="fetchData" placeholder="请输入关键字"/>
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
          <Button style="float: right" type="default" @click="modalNoshow">新建</Button>
        </Col>
      </Row>
    </Card>
    <Card>
      <tables ref="tables" v-model="data" :columns="columns"/>
      <Page style="margin-top: 20px;float: right;" :total="searchParams.total" :page-size="searchParams.pageSize" show-total show-elevator show-sizer @on-change="handlePage" @on-page-size-change='handlePageSize'/>
    </Card>
    <Modal  v-model="show.value" :title="model_title==='1'?'编辑':'创建'">
      <Form ref="form" :model="show" :rules="formRules" :label-width="100">
        <Row>
          <FormItem label="角色名称:" prop="name" aria-required="true">
            <Input v-model="show.name"/>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="角色描述:" prop="des" aria-required="true">
            <Input class='textarea' :rows="5" :autosize="{maxRows:5,minRows: 5}" v-model="show.des" type="textarea"/>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="是否启用:" aria-required="true">
            <RadioGroup v-model="show.deleteFlag">
              <Radio :label="'0'">是</Radio>
              <Radio :label="'1'">否</Radio>
            </RadioGroup>
          </FormItem>
        </Row>
      </Form>
      <div slot="footer">
        <Button @click="show.value = false">取消</Button>
        <Button v-if="model_title==='1'" type="primary" @click="submitEdit">确定</Button>
        <Button v-if="model_title==='2'" type="primary" @click="submitCreate">确定</Button>
      </div>
    </Modal>
    <Modal v-model="show_menu.value" title="分配菜单">
      <Form ref="formMenu" v-model="show_menu" :label-width="100">
        <Row>
          <FormItem label="菜单：" prop="mtcPersonnelId">
            <tree-select
              v-model="show_menu.selectMenu"
              style="width: 300px;"
              check-strictly
              :expand-all="true"
              :data="show_menu.menu"
            ></tree-select>
          </FormItem>
        </Row>
      </Form>
      <div slot="footer">
        <Button @click="show_menu.value = false">取消</Button>
        <Button type="primary" @click="submitMenu">确定</Button>
      </div>
    </Modal>
  </div>
</template>

<script>
  import Tables from '_c/tables'
  import TreeSelect from '_c/tree-select'
  import { getRole, updateRole, createRole, deleteRole } from '@/api/role';
  import { getMenu, updateMenuBatch } from '@/api/menu';
  import {timestampFormat} from '@/libs/tools';
  const searchParams = {
    name: '',
    page: 1,
    pageSize: 10,
    total: 0
  }
  export default {
    name: 'tables_page',
    components: {
      Tables,
      TreeSelect
    },
    data () {
      return {
        columns: [
          { title: 'ID', key: 'id',width: 80, sortable: true },
          { title: '名称', key: 'name'},
          { title: '描述', key: 'des'},
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
          {
            title: '是否启用',
            key: 'isEnable',
            render: (h, params) => {
              return h('i-switch',{
                props:{
                  size: 'large',
                  value: params.row.deleteFlag === '0'
                },
                on: {
                  'on-change': async (value) =>{
                    await updateRole(params.row.id, {deleteFlag: value?'0':'1'});
                  }
                }
              })
            }
          },
          { title: '操作', key: 'action', align: 'center',
            render: (h, params) => {
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
                    click: async () => {
                      this.show_menu.value = !this.show_menu.value;
                      if (this.show_menu.value) {
                        this.show_menu.menu = [];
                        this.show_menu.selectMenu = [];
                        const res = await getMenu({page:1,pageSize:9999})
                        if ( res.data.code === 200 ) {
                          res.data.data.rows.forEach(item => {
                            if (item.parentId === 0) {
                              this.show_menu.menu.push({id:item.id, title: item.title, children:[]})
                            }
                            if (item.access.indexOf(params.row.id.toString()) !== -1) {
                              this.show_menu.selectMenu.push(item.id)
                            }
                          })
                          res.data.data.rows.forEach(item => {
                            if (item.parentId !== 0) {
                              this.show_menu.menu.forEach(parent => {
                                  if (parent.id === item.parentId) {
                                    parent.children.push({id:item.id, title:item.title, loading: item.hidden === '0', checked: false, children: []})
                                  }
                              })
                            }
                          })
                        } else {
                          this.show_menu.menu = [];
                          this.show_menu.selectMenu = [];
                        }
                        this.show_menu.id = params.row.id;
                      }
                    }
                  }
                }, '分配菜单'),
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
                      this.show.value = !this.show.value;
                      this.show.name = params.row.name;
                      this.show.des = params.row.des;
                      this.show.deleteFlag = params.row.deleteFlag;
                      this.model_title = '1';
                      this.show.id = params.row.id;
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
                      await deleteRole(params.row.id)
                      this.$Message.success('删除成功！');
                      this.fetchData();
                    }
                  }
                }, [h('Button', {props: {type: 'error', size: 'small'}}, '删除')])
              ]);
            }
          }
        ],
        data: [],
        loading: true,
        show: {
          value:false,
          id: '',
          name: '',
          des: '',
          deleteFlag: '1'
        },
        show_menu: {
          value:false,
          id: '',
          menu: [],
          selectMenu: []
        },
        model_title: '',
        searchParams: searchParams,
        formRules: {
          name: [
            {required: true, message: "必输项不能为空", trigger: 'blur'}
          ],
          des:[
            {required: true, message: "必输项不能为空", trigger: 'blur'}
          ]
        }
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      exportExcel () {
        this.$refs.tables.exportCsv({
          filename: `university-table-${(new Date()).valueOf()}.csv`
        })
      },
      async fetchData() {
        this.loading = true
        const res = await getRole(this.searchParams)
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
      modalNoshow() {
        this.show.value = !this.show.value;

        this.model_title = '2';
        this.show.name = '';
        this.show.des = '';
        this.show.deleteFlag = '1';
      },
      async submitEdit() {
        this.$refs.form.validate(async (valid) => {
          if (valid) {
            const res = await updateRole(this.show.id, this.show)
            if (res.data.code === 200) {
              this.$Message.success(res.data.msg);
              this.show.value = false;
              this.$emit('refresh');
              this.fetchData();
            } else {
              this.$Message.warning(res.data.msg);
            }
          }
        })
      },
      async submitCreate() {
        this.$refs.form.validate(async (valid) => {
          if (valid) {
            const res = await createRole(this.show)
            if (res.data.code === 200) {
              this.$Message.success(res.data.msg);
              this.show.value = false
              this.$emit('refresh');
              this.fetchData();
            } else {
              this.$Message.warning(res.data.msg);
            }
          } else {
            return false
          }
        })
      },
      async submitMenu() {
        const res = await updateMenuBatch(this.show_menu.id,JSON.stringify(this.show_menu.selectMenu))
        if (res.data.code === 200) {
          this.$Message.success(res.data.msg);
          this.show_menu.value = false;
          this.$emit('refresh');
          this.fetchData();
        } else {
          this.$Message.warning(res.data.msg);
        }
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
        this.searchParams.name = ''
      }
    }
  }
</script>
<style>
</style>
