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
            <FormItem label="输入搜索:" aria-required="true">
                <Input v-model="searchParams.username" @keyup.enter="fetchData" placeholder="用户名称"/>
            </FormItem>
          </Col>
          <Col span="6">
            <FormItem label="公司:" aria-required="true">
              <Input v-model="searchParams.companyName" @keyup.enter="fetchData" placeholder="公司名称"/>
            </FormItem>
          </Col>
          <Col span="6">
            <FormItem label="用户状态" prop="libraryId">
              <Select v-model="searchParams.status" filterable placeholder="全部">
                <Option v-for="(option, index) in status_option" :value="option.value" :key="index">{{option.label}}</Option>
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
      <tables ref="tables" v-model="data" :columns="columns"/>
      <Button style="margin: 10px 0;" type="primary" @click="exportExcel">导出为Csv文件</Button>
      <Page style="margin-top: 20px;float: right;" :total="searchParams.total" :page-size="searchParams.pageSize" show-total show-elevator show-sizer @on-change="handlePage" @on-page-size-change='handlePageSize'/>
    </Card>
    <Modal v-model="show_create.value" title="创建">
      <Form ref="formCreate" :model="show_create" :rules="formRules" :label-width="100">
        <Row>
          <FormItem label="头像:" prop="icon" aria-required="true">
            <Avatar :src="show_create.icon" style="width: 50px; height: 50px"/>
            <Icon type="md-camera" size="20" style="margin-left: 15px;margin-bottom: 15px;" @click="imageShow"></Icon>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="用户名称:" prop="nickname" aria-required="true">
            <Input style="width: 80%" v-model="show_create.nickname"/>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="公司名称:" prop="companyName" aria-required="true">
            <Input style="width: 80%" v-model="show_create.companyName"/>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="地址:" prop="address" aria-required="true">
            <Input style="width: 80%" v-model="show_create.address"/>
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
            <RadioGroup v-model="show_create.status">
              <Radio :label="0">体验</Radio>
              <Radio :label="1">正式</Radio>
            </RadioGroup>
          </FormItem>
        </Row>
      </Form>
      <div slot="footer">
        <Button @click="show_create.value = false">取消</Button>
        <Button type="primary" @click="submitCreate">确定</Button>
      </div>
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
            <Input style="width: 80%" v-model="show_edit.companyName"/>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="地址:" prop="address" aria-required="true">
            <Input style="width: 80%" v-model="show_edit.address"/>
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
          <FormItem label="用户状态:" aria-required="true">
            <RadioGroup v-model="show_edit.status">
              <Radio :label="0">体验</Radio>
              <Radio :label="1">正式</Radio>
            </RadioGroup>
          </FormItem>
        </Row>
      </Form>
      <div slot="footer">
        <Button @click="show_edit.value = false">取消</Button>
        <Button type="primary" @click="submitEdit">确定</Button>
      </div>
    </Modal>
    <Modal v-model="show_password.value" title="修改密码">
      <Form ref="formPassword" :model="show_password" :rules="formRules" :label-width="100">
        <Row>
          <FormItem label="原密码:" prop="password" aria-required="true">
            <Input style="width: 80%" type="password" v-model="show_password.password"/>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="新密码:" prop="password2" aria-required="true">
            <Input style="width: 80%" type="password" v-model="show_password.password2"/>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="重新输入密码:" prop="password3" aria-required="true">
            <Input style="width: 80%" type="password"  v-model="show_password.password3"/>
          </FormItem>
        </Row>
      </Form>
      <div slot="footer">
        <Button @click="show_password.value = false">取消</Button>
        <Button type="primary" @click="submitPassword">确定</Button>
      </div>
    </Modal>
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
    <Modal v-model="show_detail.value" title="详情" footer-hide>
      <Form ref="formDetail" :model="show_detail" :label-width="100">
        <Row :gutter="16">
          <Col span="8">
            <FormItem label="订单总数:" prop="orderNum" aria-required="true">
              <span v-model="show_detail.orderNum"></span>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="昨日订单数:" prop="yesOrderNum" aria-required="true">
              <span v-model="show_detail.yesOrderNum"></span>
            </FormItem>
          </Col>
        </Row>
        <Row :gutter="16">
          <Col span="8">
            <FormItem label="营业总额:" prop="turnover" aria-required="true">
              <span v-model="show_detail.turnover"></span>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="昨日营业额:" prop="yesTurnover" aria-required="true">
              <span v-model="show_detail.yesTurnover"></span>
            </FormItem>
          </Col>
        </Row>
        <Row :gutter="16">
          <Col span="8">
            <FormItem label="顾客总数:" prop="customerNum" aria-required="true">
              <span v-model="show_detail.customerNum"></span>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="昨日新增顾客数:" prop="yesCustomerNum" aria-required="true">
              <span v-model="show_detail.yesCustomerNum"></span>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="vip顾客数:" prop="vipCustomerNum" aria-required="true">
              <span v-model="show_detail.vipCustomerNum"></span>
            </FormItem>
          </Col>
        </Row>
        <Row :gutter="16">
          <Col span="8">
            <FormItem label="场馆数:" prop="stadiumNum" aria-required="true">
              <span style="width: 80%" v-model="show_detail.stadiumNum"></span>
            </FormItem>
          </Col>
          <Col span="8">
            <Icon type="md-list" size="20" style="margin-left: 15px;margin-bottom: 15px;" @click="openStadiumPage()"></Icon>
          </Col>
        </Row>
        <Row :gutter="16">
          <Col span="8">
            <FormItem label="商品详情:" aria-required="true">
              <Icon type="md-list" size="20" style="margin-left: 15px;margin-bottom: 15px;" @click="openProductPage()"></Icon>
            </FormItem>
          </Col>
        </Row>
      </Form>
    </Modal>
  </div>
</template>

<script>
  import { getUser, updateUser, updateUserPassword, deleteUser, createUser } from '@/api/user';
  import { uploadImg } from '@/api/data'
  import {validateUsername, validatePass, validatePhone, validateEMail} from '@/libs/validate';
  import { timestampFormat, getIconDefault} from '@/libs/tools';
  import Tables from '_c/tables'
  import Cropper from '@/components/cropper'
  const searchParams = {
    status: '',
    username: '',
    companyName: '',
    createTimeStart: '',
    createTimeEnd: '',
    lastLoginTimeStart: '',
    lastLoginTimeEnd: '',
    page: 1,
    pageSize: 10,
    total: 0
  }
  const status_option = [
    {
      label: "体验",
      value: 0
    },
    {
      label: "正式",
      value: 1
    }
  ]
  export default {
    name: 'tables_page',
    components: {
      Tables,
      Cropper
    },
    data () {
      return {
        columns: [
          { title: 'ID', key: 'id',width: 80, sortable: true },
          { title: '头像', key: 'icon', align: 'center',
            render: (h, params) => {
              return h('img', {
                style: {
                  width: '100px',
                  'height': '80px',
                  'border-radius': '5%'
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
          { title: '用户账号', key: 'username'},
          { title: '用户名称', key: 'nickname'},
          { title: '公司名称', key: 'companyName'},
          { title: '电话号码', key: 'tel'},
          { title: '邮箱', key: 'email'},
          { title: '地址', key: 'address'},
          { title: '密码', key: '',
            render: (h, params) => {
              return h('div', [
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
                      this.show_password.value = true;
                      this.show_password.id = params.row.id;
                      this.$refs.formPassword.resetFields();
                    }
                  }
                })
              ])
            }
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
          { title: '是否启用',width: 100, key: 'status',
            render: (h, params) => {
              return h('i-switch', {
                props: {
                  size: 'large',
                  value: params.row.status === 1
                },
                on: {
                  'on-change': async (value) => {
                    await updateUser(params.row.id, {status: value ? 1 : 0});
                  }
                }
              })
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
                    click: () => {
                      this.show_detail.value = true;
                      this.show_detail.id = params.row.id;
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
                      this.show_edit.id= params.row.id;
                      this.show_edit.nickname= params.row.nickname;
                      this.show_edit.companyName= params.row.companyName;
                      this.show_edit.icon = params.row.icon;
                      this.show_edit.tel = params.row.tel;
                      this.show_edit.address = params.row.address;
                      this.show_edit.email = params.row.email;
                      this.show_edit.status = params.row.status;
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
                      await deleteUser(params.row.id ,{deleteFlag: 1})
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
        searchParams: searchParams,
        data: [],
        loading: true,
        status_option: status_option,
        iconDefault: getIconDefault(),
        show_edit: {
          id: '',
          value: false,
          nickname: '',
          companyName: '',
          icon: '',
          tel: '',
          address: '',
          email: '',
          status: 0
        },
        show_create: {
          value: false,
          nickname: '',
          companyName: '',
          icon: '',
          tel: '',
          address: '',
          email: '',
          status: 0,
          password: '',
          password2: ''
        },
        show_password: {
          value: false,
          id: '',
          password: '',
          password2: '',
          password3: ''
        },
        show_image: false,
        show_detail: {
          value: false,
          id: '',
          yesVipCustomerNum: '',
          vipCustomerNum: '',
          yesCustomerNum: '',
          customerNum: '',
          yesOrderNum: '',
          orderNum: '',
          yesTurnover: '',
          turnover: '',
          stadiumNum: '',
        },
        exampleImageSrc: '',
        formRules: {
          companyName: [
            {required: true, message: "必输项不能为空", trigger: 'blur'}
          ],
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
          address:[
            {required: true, message: "必输项不能为空", trigger: 'blur'}
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
          ],
          password3:[
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
      exportExcel () {
        this.$refs.tables.exportCsv({
          filename: `user-table-${(new Date()).valueOf()}.csv`
        })
      },
      async fetchData() {
        this.loading = true
        const res = await getUser(this.searchParams)
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
      imageShow() {
        this.show_image = !this.show_image;
      },
      submitPassword() {
        this.$refs.formPassword.validate(async (valid) => {
          if (valid) {
            if (this.show_password.password2 === this.show_password.password3) {
              const res = await updateUserPassword(this.show_password.id,JSON.stringify({password: this.show_password.password, password2: this.show_password.password2}))
              if (res.data.code === 200) {
                this.$Message.success(res.data.msg);
                this.show_password.value = false;

                this.$emit('refresh');
                this.fetchData();
              } else {
                this.$Message.error(res.data.msg);
              }
            } else {
              this.$Message.warning("两次密码不一致！");
            }
          }
        })
      },
      submitEdit() {
        this.$refs.formEdit.validate(async (valid) => {
          if (valid) {
            const res = await updateUser(this.show_edit.id,this.show_edit)
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
              if (this.show_create.password === this.show_create.password2) {
                const res = await createUser(this.show_create)
                if (res.data.code === 200) {
                  this.$Message.success(res.data.msg);
                  this.show_create.value = false;
                  this.$emit('refresh');
                  this.fetchData();
                }
              } else {
                this.$Message.warning("两次密码不一样！")
              }
          }
        })
      },
      handleCroped (blob) {
        const formData = new FormData()
        formData.append('croppedImg', blob)
        uploadImg(formData).then(() => {
          this.$Message.success('Upload success~')
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
        this.$refs.formDate1.handleClear();
        this.$refs.formDate2.handleClear();
        this.searchParams.status = '';
        this.searchParams.username = '';
        this.searchParams.createTime_start = '';
        this.searchParams.createTime_end = '';
        this.searchParams.lastLoginTime_start = '';
        this.searchParams.lastLoginTime_end = '';
        this.searchParams.page = 1;
        this.searchParams.pageSize = 10;
      },
      createToEmpty() {
        this.$refs.formCreate.resetFields();
        this.show_create.status = 0;
        this.show_create.icon = this.iconDefault;
      },
      openStadiumPage() {
        const userId = this.show_detail.id;
        const route = {
          name: 'stadium',
          query: {
            userId
          },
          meta: {
            title: `${userId} 的场馆情况`
          }
        }
        this.$router.push(route)
      },
      openProductPage() {
        const userId = this.show_detail.id;
        const route = {
          name: 'product',
          query: {
            userId
          },
          meta: {
            title: `${userId} 的商品情况`
          }
        }
        this.$router.push(route)
      },
      handleChange1(date) {
        this.searchParams.createStartTime = date[0];
        this.searchParams.createEndTime = date[1];
      },
      handleChange2(date) {
        this.searchParams.lastLoginTimeStart = date[0];
        this.searchParams.lastLoginTimeEnd = date[1];
      }
    }
  }
</script>
<style>
</style>
