<template>
  <div>
    <Card dis-hover>
      <Spin size="large" fix v-if="loading"></Spin>
      <Card dis-hover style="margin: 0 15%">
        <Form ref="formCreate" :model="data" :label-width="100" :rules="rules">
          <Row v-for="item in data">
            {{item.prefixDes}}
            <Input v-model="item.time" style="width: 30%">
              <span slot="append">{{item.unitName}}</span>
            </Input>
            {{item.suffixDes}}
          </Row>
        </Form>
      </Card>
    </Card>
  </div>
</template>
<script>
  import { getOrderSetting, updateOrderSetting, createOrderSetting} from '@/api/order';
  export default {
    data() {
      return {
        loading: false,
        data: [],
        rules: {
          des: [
            {required: true, message: "必输项不能为空", trigger: 'blur'}
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
          this.data = res.data.data.map(item => {
            return {...item};
          })
        } else {
          this.data = [];
          this.$Message.warning(res.data.msg);
        }
        setTimeout(() => {
          this.loading = false
        }, 300)
      },
    }
  }
</script>
<style lang="scss" scoped>

</style>
