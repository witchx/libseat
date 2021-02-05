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
              <Input v-model="searchParams.name" @keyup.enter="fetchData" placeholder="自习室名称"/>
            </FormItem>
          </Col>
          <Col span="6">
            <FormItem label="场馆:" prop="stadiumName" aria-required="true">
              <Input v-model="searchParams.stadiumName" @keyup.enter="fetchData" placeholder="场馆名称"/>
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
          <Button style="float: right" type="default" @click="openCreateRoom">新建</Button>
        </Col>
      </Row>
    </Card>
    <Card>
      <tables ref="tables" :loading="loading" :columns="columns" v-model="data"/>
      <Button style="margin: 10px 0;" type="primary" @click="exportExcel">导出为Csv文件</Button>
      <Page style="margin-top: 20px;float: right;" :total="searchParams.total" :page-size="searchParams.pageSize" show-total show-elevator show-sizer @on-change="handlePage" @on-page-size-change='handlePageSize'/>
      <show-seat :create_modal_show="create_modal_show" @noshow="openRoomDetail" :seat="seat"/>
    </Card>
  </div>
</template>

<script>
  import Tables from '_c/tables';
  import { getSeat } from '@/api/seat';
  import showSeat from '@/components/showSeat';
  import { getRoom, updateRoom, deleteRoom } from '@/api/room';
  import {timestampFormat} from '@/libs/tools';
  const searchParams = {
    name: '',
    stadiumId: '',
    stadiumName: '',
    page: 1,
    pageSize: 10,
    total: 0
  }
  export default {
    name: 'tables_page',
    components: {
      Tables,
      showSeat
    },
    data () {
      return {
        columns: [
          { title: 'ID', key: 'id',width: 80, sortable: true },
          { title: '场馆名称', key: 'stadiumName'},
          { title: '自习室名称', key: 'name' , render: (h, params) => {
            if (params.row.edit) {
              this.edit.name = params.row.name;
              return h('div', [
                h('Input', {
                  props: {
                    placeholder: '请输入自习室名称',
                    value: params.row.name
                  },
                  on: {
                    'on-change': (e) => {
                      this.edit.name = e.target.value
                    }
                  }
                })
              ])
            } else {
              return h('div', [
                params.row.name,
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
            }
            }
          },
          { title: ' ', key: '' ,width: 150, render: (h, params) => {
              if (params.row.edit) {
                this.edit.name = params.row.name;
                return h('div', [
                  h('Button', {
                    props: {
                      type: 'default',
                      size: 'small'
                    },
                    style: {
                      marginRight: '5px'
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
                        if (this.edit.name) {
                          if (this.edit.name !== params.row.name) {
                            const res = await updateRoom( params.row.id, {name: this.edit.name})
                            if (res.data.code === 200) {
                              this.$Message.success('编辑成功！');
                              this.fetchData()
                            } else {
                              this.$Message.warning(res.data.msg);
                            }
                          }
                        } else {
                          this.$Message.warning('自习室名称不能为空！');
                        }
                      }
                    }
                  }, '确定')
                ])
              }
            }
          },
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
          { title: '操作', key: 'action', align: 'center', render: (h, params) => {
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
                      this.openRoomDetail(params.row.id)
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
                      this.openEditRoom(params.row.id)
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
                      await deleteRoom(params.row.id)
                      this.$Message.success('删除成功！');
                      this.fetchData()
                    }
                  }
                }, [h('Button', {props: {type: 'error', size: 'small'}}, '删除')])
              ]);
            }
          }
        ],
        data: [],
        loading: true,
        edit: {
          name: ''
        },
        searchParams: searchParams,
        create_modal_show: false,
        seat: []
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      exportExcel () {
        this.$refs.tables.exportCsv({
          filename: `room-table-${(new Date()).valueOf()}.csv`
        })
      },
      async fetchData() {
        this.loading = true
        if (typeof(this.$route.query.stadiumId) !== "undefined" && this.$route.query.stadiumId !== '') {
          this.searchParams.stadiumId = this.$route.query.stadiumId;
          this.$route.query.stadiumId = '';
        }
        const res = await getRoom(this.searchParams)
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
          this.loading = false;
        }, 300)
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
        this.searchParams.stadiumId = '';
        this.searchParams.stadiumName = '';
      },
      openCreateRoom() {
        const route = {
          name: 'createRoom',
          meta: {
            title: `创建图书馆`
          }
        }
        this.$router.push(route)
      },
      openEditRoom(roomId) {
        const route = {
          name: 'editRoom',
          query: {
            roomId
          },
          meta: {
            title: `编辑自习室-${roomId}`
          }
        }
        this.$router.push(route)
      },
      async openRoomDetail(id) {
        if (!this.create_modal_show) {
          const res = await getSeat({roomId: id , startTime: Date.now(), endTime: Date.now()})
          this.seat = res.data.data
        }
        this.create_modal_show = !this.create_modal_show
      }
    }
  }
</script>

<style>

</style>
