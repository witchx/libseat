<template>
  <div style="height: 100%">
    <Card dis-hover style="height: 100%">
      <Spin size="large" fix v-if="loading"></Spin>
      <Card dis-hover style="margin: 0 15%">
        <Form ref="formList" :model="data" :label-width="250">
          <div style="padding-top: 5%"></div>
          <FormItem v-for="(item,index) in data.list" :key="index"
                    id="form_item_size"
                    :label="item.prefixDes"
                    :prop="'list.' + index + '.time'"
                    :rules="[{required: true, message: '必输项不能为空'},{type: 'number', message: '请输入数字值'}]">
            <Row>
              <Col span="8">
                <Input v-model.number="item.time" size="large">
                  <span slot="append">{{item.unitName}}</span>
                </Input>
              </Col>
              <Col span="10">
                <span id="form_span_size">&nbsp;&nbsp;&nbsp;&nbsp;{{item.suffixDes}}</span>
              </Col>
              <Col span="4">
                <i-switch v-model="item.onOff" @on-change="onOffTask(item)" style="float: right;margin-right: 20%"></i-switch>
              </Col>
            </Row>
          </FormItem>
        </Form>
        <Row style="margin: 2% 0% 5% 250px;">
          <Button @click="create_show.value = true" style="margin-right: 30px;"  size="large">添加</Button>
          <Button @click="updateOrderSettingBatch" type="primary" size="large">提交</Button>
        </Row>
      </Card>
    </Card>
    <Modal v-model="create_show.value" title="创建">
      <Form ref="formCreate" :model="create_show" :label-width="100" :rules="rules">
        <Row>
          <FormItem label="前缀:" prop="prefixDes">
            <Input v-model.trim="create_show.prefixDes" style="width: 80%" placeholder="请输入前缀"/>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="后缀:" prop="suffixDes">
            <Input v-model.trim="create_show.suffixDes" style="width: 80%" placeholder="请输入后缀"/>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="时间:" prop="time">
            <InputNumber v-model="create_show.time" :min="1" style="width: 80%" placeholder="请输入时间"/>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="单位:" prop="unit">
            <Select v-model="create_show.unit" style="width: 80%" filterable placeholder="请选择">
              <Option v-for="(item, index) in unit_type" :value="item.value" :key="index">{{item.label}}</Option>
            </Select>
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
  import { getOrderSetting, updateOrderSetting, createOrderSetting, onOffOrderSetting} from '@/api/order';
  const unit_type = [
    {
      value: 1,
      label: '秒'
    },
    {
      value: 2,
      label: '分'
    },
    {
      value: 3,
      label: '时'
    },
    {
      value: 4,
      label: '天'
    },
    {
      value: 5,
      label: '周'
    },
    {
      value: 6,
      label: '月'
    },
    {
      value: 7,
      label: '年'
    }
  ]
  export default {
    data() {
      return {
        loading: false,
        data: {
          list:[]
        },
        create_show: {
          value: false,
          prefixDes: '',
          suffixDes: '',
          time: 0,
          unit: ''
        },
        unit_type: unit_type,
        rules: {
          prefixDes: [
            {required: true, message: "必输项不能为空", trigger: 'blur'}
          ],
          suffixDes: [
            {required: true, message: "必输项不能为空", trigger: 'blur'}
          ],
          time: [
            {required: true, type:'number', message: '必输项不能为空', trigger: 'blur'}
          ],
          unit: [
            {required: true, trigger: 'change',type: 'number',message: '必选项不能为空'}
          ]
        },
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      async fetchData() {
        this.loading = true
        const res = await getOrderSetting()
        if (res.data.code === 200) {
          this.data.list = res.data.data.map(item => {
            return {...item};
          })
        } else {
          this.data.list = [];
          this.$Message.warning(res.data.msg);
        }
        setTimeout(() => {
          this.loading = false
        }, 300)
      },
      async onOffTask(task) {
        const res = await onOffOrderSetting(task.id,{onOff:task.onOff})
        if (res.data.code === 200) {
          this.$Message.success(res.data.msg);
        } else {
          this.$Message.warning(res.data.msg);
        }
        this.fetchData();
      },
      async updateOrderSettingBatch() {
        this.$refs.formList.validate(async (valid) => {
          if (valid) {
            const res = await updateOrderSetting(JSON.stringify(this.data.list))
            if (res.data.code === 200) {
              this.$Message.success(res.data.msg);
            } else {
              this.$Message.warning(res.data.msg);
            }
            this.fetchData();
          }
        })
      },
      submitCreate() {
        this.$refs.formCreate.validate(async (valid) => {
          if (valid) {
            const res = await createOrderSetting(this.create_show)
            if (res.data.code === 200) {
              this.$Message.success(res.data.msg);
              Object.keys( this.create_show).forEach(key => {
                this.create_show[key] = '';
              });
              this.create_show.value = false
              this.create_show.time = 0
              this.$emit('refresh');
              this.fetchData();
            } else {
              this.$Message.warning(res.data.msg);
            }
          }
        })
      }
    }
  }
</script>
<style>
  #form_item_size .ivu-form-item-label{
    font-size: 15px;
  }
  #form_span_size{
    font-size: 15px;
  }
</style>
