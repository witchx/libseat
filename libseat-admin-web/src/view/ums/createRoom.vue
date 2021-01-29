<template>
    <div>
      <Card style="margin-bottom: 10px">
        <Form ref="form" :model="form" :rules="ruleValidate" :label-width="100">
          <Row style="margin-bottom: 15px">
            <Col span="20">
              <Icon size="18" type="md-search"></Icon>
              <label style="font-size: 15px;">&nbsp;筛选搜索</label>
            </Col>
            <Col span="4">
              <Button style="float: right;margin-right: 10px;" type="default" @click="paramToEmpty">重置</Button>
            </Col>
          </Row>
          <Row :gutter="16">
            <Col span="6">
              <FormItem label="自习室名称" prop="name">
                <Input v-model="form.name" placeholder="请输入自习室名称"></Input>
              </FormItem>
            </Col>
            <Col span="6">
              <FormItem label="场馆" prop="stadiumId">
                <Select v-model="form.stadiumId" filterable remote :remote-method="debounce(searchStadium, 300)" :loading="lib_loading" placeholder="请输入搜索场馆名称">
                  <Option v-for="(option, index) in lib_option" :value="option.value" :key="index">{{option.label}}</Option>
                </Select>
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
        </Row>
      </Card>
      <Card style="position: relative;">
        <edit-seat :seat="seat" />
        <Button style="margin-top: 20px" v-if="seat.length" type="primary" @click="submit('form')">提交</Button>
      </Card>
    </div>
</template>
<script>
  import { createRoom } from '@/api/room'
  import { getStadium } from '@/api/stadium'
  import editSeat from '@/components/editSeat'
  import lodash from 'lodash'
  export default {
    components: {
      editSeat
    },
    data() {
      return {
        ruleValidate: {
          name: [
            { required: true, message: '请输入自习室名称', trigger: 'blur' },
          ],
          stadiumId: [
            { required: true, message: '请选择图书馆', trigger: 'change' },
          ]
        },
        lib_loading: false,
        form: {
          name: '',
          userId: '',
          stadiumId: ''
        },
        uni_option: [],
        lib_option: [],
        seat: [{row: [{value: 1}]}],
        seat_loading: false,
      }
    },
    methods: {
      async searchStadium(query) {
        if (query) {
          this.lib_loading = true
          const res = await getStadium({name: query})
          if (res.data.code === 200) {
            this.lib_option = res.data.data.rows.map(item => {
              return {
                value: item.id + '',
                label: item.companyName + '-'+item.name,
              }
            })
          }
          setTimeout(() => {
            this.lib_loading = false
          }, 200)
        } else {
          this.lib_option = []
        }
      },
      submit(name) {
        const vm = this;
        this.$refs[name].validate(async (valid) => {
          if (valid) {
            const res = await createRoom({name:vm.form.name, stadiumId: vm.form.stadiumId, seat:this.seat})
            if (res.data.code === 200) {
              this.$Message.success(res.data.msg);
            } else {
              this.$Message.warning(res.data.msg);
            }
            this.seat = [{row: [{value: 1}]}];
            this.$refs[name].resetFields();
          } else {
            return false
          }
        })
      },
      debounce(fun, time) {
        return lodash.debounce(fun, time)
      },
      paramToEmpty() {
        this.form.stadiumId = '';
        this.form.userId = '';
        this.form.name = '';
      }
    }
  }
</script>
<style lang="scss" scoped>

</style>
