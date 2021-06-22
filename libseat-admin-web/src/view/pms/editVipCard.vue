<template>
  <div>
    <Card style="margin: 0% 15%;">
      <Spin size="large" fix v-if="spinShow"></Spin>
      <div v-if="success === 200">
        <div class="success">
          <p>编辑商品成功！</p>
        </div>
        <back-btn-group class="back-btn-group"></back-btn-group>
      </div>
      <div v-else>
        <Steps :current="current" style="margin: 3% 0% 8% 16%;">
          <Step class="blue" title="商品类型" content="选择商品类型"></Step>
          <Step class="blue" title="商品信息" content="填写商品信息"></Step>
        </Steps>
        <div v-show="current===0" style="margin: 0% 15%;">
          <Form ref="formData1" :model="formData1" :rules="formRules1" :label-width="100">
            <Row v-if="formData2.id === ''">
              <FormItem label="公司:" prop="userId">
                <Select v-model="formData1.userId" filterable remote :remote-method="debounce(searchUser, 300)" :loading="user_loading" placeholder="请输入搜索公司名称">
                  <Option v-for="(option, index) in user_option" :value="option.value" :key="index">{{option.label}}</Option>
                </Select>
              </FormItem>
            </Row>
            <Row v-if="formData2.id !== ''">
              <FormItem label="公司:">
                <span>{{formData1.name}}</span>
              </FormItem>
            </Row>
            <Row>
              <FormItem label="商品类型:" prop="type">
                <Select v-model="formData1.type" filterable placeholder="请选择">
                  <Option v-for="(item, index) in product_type" :value="item.value" :key="index">{{item.label}}</Option>
                </Select>
              </FormItem>
            </Row>
            <Row style="text-align: center">
              <Button type="primary" @click="next()">下一步</Button>
            </Row>
          </Form>
        </div>
        <div v-show="current===1"  style="margin: 0% 15%;">
          <Form ref="formData2" :model="formData2"  :rules="formRules2" :label-width="100">
            <Row>
              <FormItem label="商品名称:" prop="name">
                <Input v-model.trim="formData2.name" placeholder="请输入商品名称"/>
              </FormItem>
            </Row>
            <Row>
              <FormItem label="原价:" prop="originalPrice">
                <Input v-model.number="formData2.originalPrice" placeholder="请输入原价"/>
              </FormItem>
            </Row>
            <Row>
              <FormItem label="现价:" prop="price">
                <Input v-model.number="formData2.price" placeholder="请输入现价"/>
              </FormItem>
            </Row>
            <Row>
              <FormItem label="有效期:" prop="usefulLife">
                <Input v-model.number="formData2.usefulLife" placeholder="请输入有效期"/>
              </FormItem>
            </Row>
            <!--储值卡-->
            <Row v-if="formData2.type === 1 ">
              <FormItem label="储值:" prop="money">
                <Input v-model.number="formData2.money" placeholder="请输入储值"/>
              </FormItem>
            </Row>
            <!--计次卡-->
            <Row  v-if="formData2.type === 2">
              <FormItem label="次数" prop="times">
                <Input v-model.number="formData2.times" placeholder="请输入次数"/>
              </FormItem>
            </Row>
            <Row>
              <FormItem label="描述:" prop="des">
                <Input :rows="5" :autosize="{maxRows:5,minRows: 5}" v-model.trim="formData2.des" type="textarea"/>
              </FormItem>
            </Row>
            <Row style="text-align: center;">
              <Button @click="prev" style="margin-right: 30px;">上一步</Button>
              <Button type="primary" @click="next()">确认提交</Button>
            </Row>
          </Form>
        </div>
      </div>
    </Card>
  </div>
</template>
<script>
  import {  getVipCard, updateVipCard, createVipCard} from '@/api/vipCard';
  import { getUser} from '@/api/user';
  import backBtnGroup from '../error-page/back-btn-group.vue'
  import lodash from "lodash";
  const product_type = [
    {
      value: 1,
      label: '储值卡'
    },
    {
      value: 2,
      label: '计次卡'
    },
    {
      value: 3,
      label: '期限卡'
    }
  ]
  export default {
    components: {
      backBtnGroup
    },
    data() {
      return {
        spinShow: false,
        current: 0,
        success: 0,
        status: 'wait',
        product_type: product_type,
        user_loading: false,
        user_option: [],
        formData1: {
          userId: '',
          type: '',
          name: '',
        },
        formData2: {
          id: '',
          no: '',
          name: '',
          type: '',
          userId: '',
          times: '',
          usefulLife: '',
          money: '',
          originalPrice: '',
          price: '',
          des: ''
        },
        formRules1: {
          type: [
            {required: true, trigger: 'change',type: 'number',message: '必选项不能为空'}
          ],
          userId: [
            {required: true, type: 'number', message: "必选项不能为空", trigger: 'change'}
          ],
        },
        formRules2: {
          no: [
            {required: true, message: "必输项不能为空", trigger: 'blur'}
          ],
          name:[
            {required: true, message: "必输项不能为空", trigger: 'blur'}
          ],
          times: [
            {required: true, message: "必输项不能为空"},
            {type: 'number', message: "请输入数字值"},
          ],
          userId:[
            {required: true, message: "必输项不能为空"},
            {type: 'number', message: "请输入数字值"},
          ],
          usefulLife:[
            {type: 'number', message: "请输入数字值"},
          ],
          money:[
            {required: true, message: "必输项不能为空"},
            {type: 'number', message: "请输入数字值"},
          ],
          originalPrice:[
            {required: true, message: "必输项不能为空"},
            {type: 'number', message: "请输入数字值"},
          ],
          price:[
            {required: true, message: "必输项不能为空"},
            {type: 'number', message: "请输入数字值"},
          ],
          des:[
            {required: true, message: "必输项不能为空", trigger: 'blur'}
          ]
        }
      }
    },
    created() {
      this.toEmpty();
      this.fetchData();
    },
    methods: {
      async fetchData() {
        if (typeof(this.$route.query.vipCardId) !== "undefined" && this.$route.query.vipCardId !== '') {
          this.spinShow = true;
          const res = await getVipCard({id: this.$route.query.vipCardId})
          if (res.data.code === 200) {
            Object.keys(res.data.data.rows[0]).forEach(key => {
              if (key === 'userId') {
                this.formData1[key] = res.data.data.rows[0][key]
              }
              if (key === 'type') {
                this.formData1[key] = res.data.data.rows[0][key]
              }
              this.formData2[key] = res.data.data.rows[0][key]
            })
            const res2 = await getUser({id: this.formData1['userId']})
            if ( res2.data.code === 200 ) {
              this.formData1['name'] = res2.data.data.rows[0]['companyName']
            }
          }
          this.$route.query.vipCardId = ''
          setTimeout(() => {
            this.spinShow = false;
          }, 300)
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
      // 上一步
      prev () {
        if (this.current > 0) {
          this.current--
        }
      },
      // 下一步
      next () {
        switch (this.current) {
          case 0:
            this.$refs['formData1'].validate(valid => {
              if (valid) {
                this.current++;
                this.formData2.type = this.formData1.type;
                this.formData2.userId = this.formData1.userId;
              }
            });
            break;
          case 1:
            this.$refs['formData2'].validate(valid => {
              if (valid) {
                this.$Modal.confirm({
                  title: "提示",
                  content: "确认要提交该商品?",
                  okText: "确定",
                  cancelText: "取消",
                  onOk: async () => {
                    this.handleSubmit();
                  }
                });
              }
            });
            break;
        }
      },
      // 确认提交
      async handleSubmit() {
        let res = '';
        if (this.formData2.id === '') {
          res = await createVipCard(this.formData2)
        } else {
          res = await updateVipCard(this.formData2.id,this.formData2)
        }
        this.success = res.data.code;
        if (this.success !== 200) {
          this.$Message.warning(res.data.msg);
        }
      },
      toEmpty() {
        Object.keys( this.formData1).forEach(key => {
          this.formData1[key] = '';
        });
        Object.keys(this.formData2).forEach(key => {
          this.formData2[key] = '';
        });
      },
      debounce(fun, time) {
        return lodash.debounce(fun, time)
      }
    }
  }
</script>
<style lang="scss" scoped>
  .success {
    background-image: url(../../assets/images/success.png);
    background-size: contain;
    background-repeat: no-repeat;
    margin-top: 50px;
    margin-left: 200px;
    margin-bottom: 50px;
    height: 300px;
    width: 300px;
    position: relative;
    p {
      position: absolute;
      width: 100%;
      bottom: 20px;
      text-align: center;
      font-size: 16px;
      color: #7c7c7c;
    }
  }
  .back-btn-group{
    position: absolute;
    right: 0px;
    bottom: 20px;
  }
</style>
