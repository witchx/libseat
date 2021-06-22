<template>
    <div>
      <Card style="margin-bottom: 10px">
        <Form ref="form" :model="form" :label-width="100">
          <Row style="margin-bottom: 15px">
            <Col span="20">
              <Icon size="18" type="md-search"></Icon>
              <label style="font-size: 15px;">&nbsp;筛选搜索</label>
            </Col>
            <Col span="4">
              <Button style="float: right;margin-right: 10px;" type="default" @click="handleSearch">确定</Button>
              <Button style="float: right;margin-right: 10px;" type="default" @click="paramToEmpty">重置</Button>
            </Col>
          </Row>
          <Row :gutter="16">
            <Col span="6" v-if="!sellerId">
              <Select v-model="form.userId" filterable remote :remote-method="debounce(searchUser, 300)" :loading="user_loading" @on-change="chooseUser" placeholder="请输入搜索用户名称">
                <Option v-for="(option, index) in user_option" :value="option.value" :key="index">{{option.label}}</Option>
              </Select>
            </Col>
            <Col span="6">
              <Select v-model="form.stadiumId" filterable :disabled="!form.userId" @on-change="chooseStadium" :placeholder="form.userId ? '请选择场馆' : '请先选择用户'">
                <Option v-for="(option, index) in stadium_option" :value="option.value" :key="index">{{option.label}}</Option>
              </Select>
            </Col>
            <Col span="6">
              <Select v-model="form.roomId" filterable :disabled="!form.stadiumId" :placeholder="form.stadiumId ? '请选择自习室' : '请先选择场馆'">
                <Option v-for="(option, index) in rom_option" :value="option.value" :key="index">{{option.label}}</Option>
              </Select>
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
        <edit-seat :seat="seat" @refresh="handleSearch" />
        <Spin size="large" fix v-if="seat_loading"></Spin>
        <Button style="margin-top: 20px" v-if="seat.length" type="primary" @click="submit">提交</Button>
      </Card>
    </div>
</template>
<script>
  import { getUser} from '@/api/user';
  import { getStadium } from '@/api/stadium';
  import { getSeat, updateSeat } from '@/api/seat';
  import { getRoomByStadiumId } from '@/api/room'
  import editSeat from '@/components/editSeat';
  import lodash from 'lodash';
  export default {
    components: {
      editSeat
    },
    data() {
      return {
        user_loading: false,
        sellerId: this.$store.state.user.sellerId,
        form: {
          userId: '',
          stadiumId: '',
          roomId: ''
        },
        user_option: [],
        stadium_option: [],
        rom_option: [],
        seat: [],
        seat_loading: false,
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      async fetchData() {
        if (typeof (this.$route.query.roomId) !== "undefined") {
          this.form.roomId = this.$route.query.roomId;
          this.handleSearch();
        }
        if (this.sellerId) {
          this.form.userId = this.sellerId
          this.chooseUser()
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
            this.form.userId = '';
          }
          setTimeout(() => {
            this.user_loading = false
          }, 200)
        } else {
          this.user_option = [];
          this.form.userId = '';
        }
      },
      async chooseUser() {
        if ( this.form.userId ) {
          const res = await getStadium({
            userId: this.form.userId
          })
          if ( res.data.code === 200 ) {
            this.stadium_option = res.data.data.rows.map(item => {
              return {
                value: item.id,
                label: item.name
              }
            })
          } else {
            this.stadium_option = [];
            this.form.stadiumId = '';
          }
        } else {
          this.stadium_option = [];
          this.form.stadiumId = '';
        }
      },
      async chooseStadium() {
        if (this.form.stadiumId) {
          const res = await getRoomByStadiumId({stadiumId: this.form.stadiumId})
          if ( res.data.code === 200 ) {
            this.rom_option = res.data.data.rows.map(item => {
              return {
                value: item.id,
                label: `${item.name} (可用座位${item.availableSeatCount} 总座位${item.totalSeatCount})`
              }
            })
          } else {
            this.rom_option = [];
            this.form.roomId = '';
          }
        } else {
          this.rom_option = [];
          this.form.roomId = '';
        }
      },
      async handleSearch() {
        this.seat_loading = true
        const res = await getSeat({roomId: this.form.roomId , startTime: Date.now(), endTime: Date.now()})
        this.seat = res.data.data
        setTimeout(() => {
          this.seat_loading = false
        }, 200)
      },
      submit() {
        this.$Modal.confirm({
          title: '警告',
          content: '<p>此操作会清除当前未开始的预约信息，强烈建议在自习室闭馆后操作，确认提交？</p>',
          loading: true,
          onOk: async () => {
            const res = await updateSeat({id: this.form.roomId, seat: this.seat})
            if (res.data.code === 200) {
              this.$Modal.remove();
              this.$Message.success(res.data.msg);
              this.handleSearch()
            } else {
              this.$Modal.remove();
              this.$Message.error(res.data.msg);
              this.handleSearch();
            }
          },
          onCancel: () => {
          }
        });
      },
      debounce(fun, time) {
        return lodash.debounce(fun, time)
      },
      paramToEmpty() {
        this.form.stadiumId = '';
        this.form.userId = '';
        this.form.roomId = '';
      }
    }
  }
</script>
<style lang="scss" scoped>

</style>
