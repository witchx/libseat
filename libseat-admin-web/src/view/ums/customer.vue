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
            <FormItem label="输入搜索:" prop="username" aria-required="true">
              <Input v-model="searchParams.username"  placeholder="顾客账号" @on-search="fetchData" />
            </FormItem>
          </Col>
          <Col span="6">
            <FormItem label="公司">
              <Input v-model="searchParams.userId"  placeholder="公司ID" @on-search="fetchData" />
            </FormItem>
          </Col>
          <Col span="6">
            <FormItem label="性别">
              <Select v-model="searchParams.sex" filterable placeholder="全部">
                <Option value="1">男</Option>
                <Option value="2">女</Option>
              </Select>
            </FormItem>
          </Col>
        </Row>
        <Row :gutter="16">
          <Col span="8">
            <FormItem label="创建时间:" aria-required="true">
              <DatePicker style="width: 80%;" ref="formDate1"  @on-change="handleChange1" format="yyyy-MM-dd HH:mm:ss" separator="  至  " type="datetimerange" placeholder="请选择时间"></DatePicker>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="最近登录时间:" aria-required="true">
              <DatePicker style="width: 80%;" ref="formDate2"  @on-change="handleChange2" format="yyyy-MM-dd HH:mm:ss" separator="  至  " type="datetimerange" placeholder="请选择时间"></DatePicker>
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
          <Button style="float: right" type="default" @click="createShow">新建</Button>
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
    </Card>
    <Modal v-model="show_image" width="654" title="更换头像">
      <Card>
        <div class="cropper-example cropper-first">
          <cropper
            :src="exampleImageSrc"
            crop-button-text="确认提交"
            @on-crop="handleCroped"
          ></cropper>
        </div>
      </Card>
    </Modal>
    <Modal v-model="show_edit.value" title="编辑">
      <Form ref="formEdit" :model="show_edit" :rules="formRules" :label-width="100">
        <Row>
          <FormItem label="头像:" prop="icon" aria-required="true">
            <Avatar :src="show_edit.icon" style="width: 50px; height: 50px"/>
            <Icon type="md-camera" size="20" style="margin-left: 15px;margin-bottom: 15px;" @click="imageShow"></Icon>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="用户名称:" prop="nickname" aria-required="true">
            <Input style="width: 80%" v-model="show_edit.nickname"/>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="公司名称:" prop="companyName" aria-required="true">
            <Input style="width: 80%" v-model="show_edit.companyName" readonly/>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="手机号码:" prop="tel" aria-required="true">
            <Input style="width: 80%" v-model="show_edit.tel"/>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="邮箱:" prop="email" aria-required="true">
            <Input style="width: 80%" v-model="show_edit.email"/>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="性别:" aria-required="true">
            <RadioGroup v-model="show_edit.sex">
              <Radio :label="1">男</Radio>
              <Radio :label="2">女</Radio>
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
      <Form ref="formCreate" :model="show_create" :rules="formRules" :label-width="100">
        <Row>
          <FormItem label="头像:" prop="icon" aria-required="true">
            <Avatar :src="show_create.icon" style="width: 50px; height: 50px"/>
            <Icon type="md-camera" size="20" style="margin-left: 15px;margin-bottom: 15px;" @click="imageShow"></Icon>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="用户昵称:" prop="nickname" aria-required="true">
            <Input style="width: 80%" v-model="show_create.nickname"/>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="公司:" prop="userId">
            <Select v-model="show_create.userId" filterable remote :remote-method="debounce(searchUser, 300)" :loading="user_loading" placeholder="请输入搜索公司名称">
              <Option v-for="(option, index) in user_option" :value="option.value" :key="index">{{option.label}}</Option>
            </Select>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="手机号码:" prop="tel" aria-required="true">
            <Input style="width: 80%" v-model="show_create.tel"/>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="邮箱:" prop="email" aria-required="true">
            <Input style="width: 80%" v-model="show_create.email"/>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="密码:" prop="password" aria-required="true">
            <Input style="width: 80%" type="password" v-model="show_create.password"/>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="重新输入密码:" prop="password2" aria-required="true">
            <Input style="width: 80%" type="password"  v-model="show_create.password2"/>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="用户状态:" aria-required="true">
            <RadioGroup v-model="show_create.sex">
              <Radio :label="1">男</Radio>
              <Radio :label="2">女</Radio>
            </RadioGroup>
          </FormItem>
        </Row>
      </Form>
      <div slot="footer">
        <Button @click="show_create.value = false">取消</Button>
        <Button type="primary" @click="submitCreate">确定</Button>
      </div>
    </Modal>
  </div>
</template>
<script>
  import Tables from '_c/tables'
  import { uploadImg } from '@/api/data'
  import { getCustomer, updateCustomer,createCustomer, deleteCustomer,deleteCustomerBatch,getCustomerDetail } from '@/api/customer';
  import { getUser} from '@/api/user';
  import {validateUsername, validatePass, validatePhone, validateEMail} from '@/libs/validate';
  import { timestampFormat, getIconDefault} from '@/libs/tools';
  import Cropper from '@/components/cropper';
  import lodash from "lodash";
  const searchParams = {
    username: '',
    sex: '',
    userId: '',
    createTimeStart: '',
    createTimeEnd: '',
    lastLoginTimeStart: '',
    lastLoginTimeEnd: '',
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
      Cropper
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
              width: 50,
              key: 'id'
            },
            { title: '头像', key: 'icon', align: 'center',
              render: (h, params) => {
                return h('img', {
                  style: {
                    width: '50px',
                    'height': '50px',
                    'border-radius': '25%',
                    'margin-top': '5px'
                  },
                  attrs: {
                    src: params.row.icon
                  },
                  on: {
                    mouseenter: () => {
                      return '<Poptip placement= "bottom"><img slot="content" src="'+params.row.icon+'"></Poptip>'
                    }
                  }
                });
              }
            },
            {title: '账号', key: 'username'},
            {title: '昵称', key: 'nickname'
            },
            {
              title: '性别',
              key: 'sex',
              render: function (h, params) {
                if (params.row.sex === 1) {
                  return h('div','男');
                }
                if (params.row.sex === 2) {
                  return h('div','女');
                }
              }
            },
            {
              title: '公司',
              key: 'companyName'
            },
            {
              title: '电话',
              key: 'tel'
            },
            {
              title: '邮件',
              key: 'email'
            },
            { title: '最近登录时间', key: 'lastLoginTime', render: (h, params) => {
                return h('div', timestampFormat(params.row.lastLoginTime,'year'));
              }},
            { title: '创建时间', key: 'createTime', render: (h, params) => {
                return h('div', timestampFormat(params.row.createTime,'year'));
              }},
            { title: '修改时间', key: 'modifyTime',
              render: (h, params) => {
                return h('div', timestampFormat(params.row.modifyTime,'year'));
              }
            },
            { title: '操作', width: 200, key: 'action', align: 'center',
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
                        const res = await getCustomerDetail(params.row.id)
                        if (res.data.code === 200) {
                          this.show_detail.value = true;
                          Object.keys( res.data.data).forEach(key => {
                            this.show_detail[key] = res.data.data[key];
                          });
                        } else {
                          this.$Message.error(res.data.msg);
                        }
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
                        Object.keys( this.show_edit).forEach(key => {
                          if (key === 'value') {
                            this.show_edit[key] = true;
                          } else {
                            this.show_edit[key] = params.row[key];
                          }
                        });
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
                          await deleteCustomer(params.row.id ,{deleteFlag: 1})
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
        user: this.$store.state.user,
        selected: [],
        operates: operates,
        operateType: null,
        show_image: false,
        user_loading: false,
        iconDefault: getIconDefault(),
        user_option: [],
        exampleImageSrc: '',
        show_edit: {
          id: '',
          value: false,
          nickname: '',
          companyName: '',
          icon: '',
          tel: '',
          email: '',
          sex: '',
        },
        show_create: {
          value: false,
          nickname: '',
          userId: '',
          icon: '',
          tel: '',
          email: '',
          sex: 1,
          password: '',
          password2: ''
        },
        show_detail:{
          value: false,
          totalValue: '',
          totalTimes: '',
          totalDays: '',
          totalCoupon: '',
          hoursByWeek: '',
          yesOrderNum: '',
          orderNum: '',
          yesTurnover: '',
          turnover: '',
          stadiumNum: '',
        },
        formRules: {
          nickname: [
            {required: true, message: "必输项不能为空", trigger: 'blur'},
            {required: true, trigger: 'blur', validator: validateUsername}
          ],
          icon:[
            {required: true, message: "必输项不能为空", trigger: 'blur'}
          ],
          tel: [
            {required: true, message: "必输项不能为空", trigger: 'blur'},
            {required: true, trigger: 'blur', validator: validatePhone}
          ],
          email:[
            {required: true, message: "必输项不能为空", trigger: 'blur'},
            {required: true, trigger: 'blur', validator: validateEMail}
          ],
          password:[
            {required: true, message: "必输项不能为空", trigger: 'blur'},
            {required: true, trigger: 'blur', validator: validatePass}
          ],
          password2:[
            {required: true, message: "必输项不能为空", trigger: 'blur'},
            {required: true, trigger: 'blur', validator: validatePass}
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
        const res = await getCustomer(this.searchParams)
        if (res.data.code === 200) {
          this.data = res.data.data.rows.map(item => {
            return {...item}
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
      submitEdit() {
        this.$refs.formEdit.validate(async (valid) => {
          if (valid) {
            const res = await updateCustomer(this.show_edit.id,this.show_edit)
            if (res.data.code === 200) {
              this.$Message.success(res.data.msg);
              this.show_edit.value = false;
              this.$emit('refresh');
              this.fetchData();
            } else {
              this.$Message.error(res.data.msg);
            }
          }
        })
      },
      submitCreate() {
        this.$refs.formCreate.validate(async (valid) => {
          if (valid) {
            if (this.show_create.password === this.show_create.password2) {
              const res = await createCustomer(this.show_create)
              if (res.data.code === 200) {
                this.$Message.success(res.data.msg);
                this.show_create.value = false;
                this.$emit('refresh');
                this.fetchData();
              } else {
                this.$Message.error(res.data.msg);
              }
            } else {
              this.$Message.warning("两次密码不一样！")
            }
          }
        })
      },
      exportExcel () {
        this.$refs.tables.exportCsv({
          filename: `student-table-${(new Date()).valueOf()}.csv`,
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
      handleChange1(date) {
        this.searchParams.createStartTime = date[0];
        this.searchParams.createEndTime = date[1];
      },
      handleChange2(date) {
        this.searchParams.lastLoginTimeStart = date[0];
        this.searchParams.lastLoginTimeEnd = date[1];
      },
      batchConfirm() {
        if (this.operateType == null) {
          this.$Message.warning("请选择操作类型");
          return;
        }
        if(this.selected==null||this.selected.length<1) {
          this.$Message.warning("请选择要操作的行");
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
                const res = await deleteCustomerBatch(JSON.stringify(this.selected));
                if (res.data.code === 200) {
                  this.$Message.success(res.data.msg);
                  await this.fetchData();
                } else {
                  this.$Message.error(res.data.msg);
                }
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
        this.$refs.formDate1.handleClear();
        this.$refs.formDate2.handleClear();
        Object.keys(this.searchParams).forEach(key => {
          this.searchParams[key] = '';
        });
        this.searchParams.page = 1;
        this.searchParams.pageSize = 10;
        this.searchParams.total = 0;
      },
      imageShow() {
        this.show_image = !this.show_image;
      },
      createShow() {
        this.show_create.value = !this.show_create.value;
        this.createToEmpty();
      },
      createToEmpty() {
        this.$refs.formCreate.resetFields();
        this.show_create.sex = 1;
        this.show_create.icon = this.iconDefault;
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
      handleCroped (blob) {
        const formData = new FormData()
        formData.append('croppedImg', blob)
        uploadImg(formData).then(() => {
          this.$Message.success('Upload success~')
        })
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
