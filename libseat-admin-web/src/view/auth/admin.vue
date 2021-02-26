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
            <FormItem label="管理员名称:" prop="username" aria-required="true">
              <Input v-model="searchParams.username"  placeholder="请输入关键字" @on-search="fetchData" />
            </FormItem>
          </Col>
        </Row>
      </Form>
    </Card>
    <Card style="margin-bottom: 10px">
      <Row>
        <Col span="20">
          <Icon size="18" type="md-list"></Icon>
          <label style="font-size: 15px;">&nbsp;数据列表</label>
        </Col>
        <Col span="4">
          <Button style="float: right" type="default" :disabled="user.access[0] !== '1'" @click="createShow">新建</Button>
        </Col>
      </Row>
    </Card>
    <Card>
      <tables ref="tables" :loading="loading" :columns="columns" v-model="data"/>
      <Button style="margin: 10px 0;" type="primary" @click="exportExcel">导出为Csv文件</Button>
      <Page style="margin-top: 20px;float: right;" :total="searchParams.total" :page-size="searchParams.pageSize" show-total show-elevator show-sizer @on-change="handlePage" @on-page-size-change='handlePageSize'/>
    </Card>
    <Modal v-model="show_create.value" title="创建管理员">
        <Form ref="formCreate" :model="show_create" :rules="rule"  :label-width="100">
          <Row>
            <FormItem label="管理员名:"  prop="nickname"  aria-required="true">
              <Input style="width: 80%" v-model="show_create.nickname"/>
            </FormItem>
          </Row>
          <Row>
            <FormItem label="密码:" prop="password"  aria-required="true">
              <Input style="width: 80%" type="password" v-model="show_create.password"/>
            </FormItem>
          </Row>
        </Form>
      <div slot="footer">
        <Button @click="show_create.value = false">取消</Button>
        <Button type="primary" @click="submitCreate">创建</Button>
      </div>
    </Modal>
    <Modal v-model="show" title="分配角色">
      <Select v-model="roleIds" filterable  placeholder="请选择角色" multiple>
        <Option v-for="(option, index) in role_option" :value="option.value" :key="index">{{option.label}}</Option>
      </Select>
      <div slot="footer">
        <Button @click="show = false">取消</Button>
        <Button type="primary" @click="submit">确定</Button>
      </div>
    </Modal>
  </div>

</template>
<script>
  import Tables from '_c/tables'
  import { getAdmin, createAdmin, updateAdmin, deleteAdmin } from '@/api/admin';
  import { getRole } from '@/api/role';
  import {timestampFormat} from '@/libs/tools';
  import {validateUsername, validatePass} from '@/libs/validate';
  import md5 from "js-md5";
  const searchParams = {
    username: '',
    page: 1,
    pageSize: 10,
    total: 0
  }
  export default {
    components: {
      Tables
    },
    data: function () {
      return {
        columns:
          [
            {
              title: 'ID',
              key: 'id',
              width: 80,
              sortable: true
            },
            {
              title: '头像',
              key: 'icon',
              align: 'center',
              render: (h, params) => {
                return h('img', {
                  style: {
                    width: '100px',
                    'height': '80px',
                    'border-radius': '5%'
                  },
                  attrs: {
                    src: params.row.icon
                  }
                });
              }
            },
            {
              title: '账号',
              key: 'username'
            },
            {
              title: '昵称',
              key: 'nickname',
              render: (h, params) => {
                if (params.row.edit) {
                  this.edit.nickname = params.row.nickname
                  return h('div', [
                    h('Input', {
                      props: {
                        placeholder: '请输入昵称',
                        style: {
                          width: '150px'
                        },
                        value: params.row.nickname
                      },
                      on: {
                        'on-change': (e) => {
                          this.edit.nickname = e.target.value

                        }
                      }
                    })
                  ])
                } else {
                  if (this.user.access[0] === '1') {
                    return h('div', [
                      params.row.nickname,
                      h('Icon', {
                        props: {
                          type: 'ios-create-outline',
                          size: 'small'
                        },
                        style: {
                          fontSize: '18px',
                          marginLeft: '10px',
                          color: 'deepskyblue'
                        },
                        on: {
                          click: () => {
                            this.data.forEach(item => {
                              item.edit = params.row.id === item.id ? true : false
                            });
                          }
                        }
                      })
                    ])
                  } else {
                    return h('div', params.row.nickname);
                  }
                }
              }
            },
            {
              title: ' ', key: '', width: 150,
              render: (h, params) => {
                if (params.row.edit) {
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
                          this.data[params.index].edit = false
                          this.edit.name = '';
                        }
                      }
                    }, '取消'),
                    h('Button', {
                      props: {
                        type: 'primary',
                        size: 'small'
                      },
                      on: {
                        click: async () => {
                          if (this.edit.nickname) {
                            const res = await updateAdmin(params.row.id, {nickname: this.edit.nickname})
                            if (res.data.code === 200) {
                              this.$Message.success('编辑成功！');
                              this.fetchData()
                            } else {
                              this.$Message.warning(res.data.msg);
                            }
                          } else {
                            this.$Message.warning('登录名不能为空！');
                          }
                        }
                      }
                    }, '确定'),
                  ])
                }
              }
            },
            {
              title: '角色名称',
              key: 'roleName'
            },
            {
              title: '创建时间', key: 'createTime',
              render: (h, params) => {
                return h('div', timestampFormat(params.row.createTime, 'year'));
              }
            },
            {
              title: '修改时间', key: 'modifyTime',
              render: (h, params) => {
                return h('div', timestampFormat(params.row.modifyTime, 'year'));
              }
            },
            {
              title: '最近登录时间', key: 'lastLoginTime',
              render: (h, params) => {
                return h('div', timestampFormat(params.row.lastLoginTime, 'year'));
              }
            },
            {
              title: '是否启用',
              key: 'isEnable',
              render: (h, params) => {
                return h('i-switch', {
                  props: {
                    size: 'large',
                    value: params.row.deleteFlag === '0'
                  },
                  on: {
                    'on-change': async (value) => {
                      await updateAdmin(params.row.id, {deleteFlag: value ? '0' : '1'});
                    }
                  }
                })
              }
            },
            {
              title: '操作',
              key: 'action',
              width: 200,
              align: 'center',
              render: (h, params) => {
                if (this.user.access[0] === '1') {
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
                          let vm = this;
                          this.show = !this.show;
                          if (this.show) {
                            const res = await getRole({name: ''})
                            if (res.data.code === 200) {
                              this.role_option = res.data.data.rows.map(item => {
                                return {
                                  value: item.id,
                                  label: item.name
                                }
                              })
                            } else {
                              this.role_option = [];
                            }
                            const roles = params.row.role;
                            if (roles != null && roles !== '' && roles !== '[]') {
                              const r = roles.replace("[", "").replace("]", "").split(",");
                              for (const i in r) {
                                vm.roleIds.push(parseInt(r[i]))
                              }
                            }
                            this.id = params.row.id;
                          }
                        }
                      }
                    }, '分配角色'),
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
                          await deleteAdmin(params.row.id)
                          this.$Message.success('删除成功！');
                          this.fetchData()
                        }
                      }
                    }, [h('Button', {props: {type: 'error', size: 'small'}}, '删除')])
                  ]);
                }
              }
            }
          ],
        data: [],
        loading: true,
        searchParams: searchParams,
        edit: {
          nickname: ''
        },
        show_create: {
          value: false,
          nickname: '',
          password: ''
        },
        user: this.$store.state.user,
        show: false,
        roleIds: [],
        role_option: [],
        id: '',
        rule: {
          nickname: [
            { validator: validateUsername, trigger: 'blur' },
          ],
          password: [
            { validator: validatePass, trigger: 'blur' },
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
        const res = await getAdmin(this.searchParams)
        if (res.data.code === 200) {
          this.data = res.data.data.rows.map(item => {
            return {
              ...item,
              edit: false
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
        this.show_create.value = !this.show_create.value;
        this.createToEmpty();
      },
      async submit() {
        const res = await updateAdmin(this.id, {role: JSON.stringify(this.roleIds)})
        if (res.data.code === 200) {
          this.$Message.success(res.data.msg);
          this.show = false;
          this.$emit('refresh');
          this.fetchData();
        } else {
          this.$Message.warning(res.data.msg);
        }
        this.roleIds = [];
        this.id = '';
      },
      submitCreate() {
        this.$refs.formCreate.validate(async (valid) => {
          if (valid) {
            this.show_create.password = md5(this.show_create.password);
            const res = await createAdmin(this.show_create)
            if (res.data.code === 200) {
              this.$Message.success(res.data.msg);
              this.show_create.value = false
              this.$emit('refresh')
            } else {
              this.$Message.warning(res.data.msg);
            }
          } else {
            return false
          }
        })
      },
      exportExcel () {
        this.$refs.tables.exportCsv({
          filename: `admin-table-${(new Date()).valueOf()}.csv`
        })
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
        this.searchParams.username = '';
      },
      createToEmpty() {
        this.show_create.username = '';
        this.show_create.password = '';
      }
    }
  }
</script>
<style lang="scss" scoped>

</style>
