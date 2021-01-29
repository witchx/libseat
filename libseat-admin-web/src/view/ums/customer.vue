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
            <FormItem label="学生名称:" prop="name" aria-required="true">
              <Input v-model="searchParams.name"  placeholder="请输入关键字" @on-search="fetchData" />
            </FormItem>
          </Col>
          <Col span="6">
            <FormItem label="学生年级">
              <Select v-model="searchParams.sid" filterable placeholder="请选择学生年级">
                <Option v-for="sg in studentGrade" :value="sg">{{sg}}级</Option>
              </Select>
            </FormItem>
          </Col>
          <Col span="6">
            <FormItem label="学校id">
              <Input v-model="searchParams.universityId"  placeholder="请输入关键字" @on-search="fetchData" />
            </FormItem>
          </Col>
          <Col span="6">
            <FormItem label="学生性别">
              <Select v-model="searchParams.sex" filterable placeholder="请选择学生性别">
                <Option value="1">男</Option>
                <Option value="2">女</Option>
              </Select>
            </FormItem>
          </Col>
        </Row>
      </Form>
    </Card>
    <Card style="margin-bottom: 10px">
      <Row>
        <Col span="18">
          <Icon size="18" type="md-list"></Icon>
          <label style="font-size: 15px;">&nbsp;数据列表</label>
        </Col>
        <Col span="6">
          <Button type="default" style="float: right;margin-right: 10px;" @click="modalNoShowCSV">上传CSV</Button>
          <Button type="default" style="float: right;margin-right: 10px;" @click="modalNoShowEXCEL">上传EXCEL</Button>
          <Button type="default" style="float: right;margin-right: 10px;" @click="modalNoShowPaste">粘贴表格数据</Button>
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
      <upload-excel :create_modal_show_excel="create_modal_show_excel" @noshow="modalNoShowEXCEL" />
      <upload-paste :create_modal_show_csv="create_modal_show_csv" @noshow="modalNoShowCSV" />
      <upload-paste :create_modal_show_paste="create_modal_show_paste" @noshow="modalNoShowPaste" />
    </Card>
  </div>
</template>
<script>
  import Tables from '_c/tables'
  import { getCustomer, updateCustomer, deleteCustomer } from '@/api/customer';
  import uploadExcel from '@/components/uploadExcel';
  import uploadCsv from '@/components/uploadCsv';
  import uploadPaste from '@/components/uploadPaste';
  const searchParams = {
    name: '',
    sex: '',
    sid: '',
    universityId: '',
    page: 1,
    pageSize: 10,
    total: 0
  }
  const studentGrade = {
    0:2017,
    1:2018,
    2:2019,
    3:2020
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
      uploadExcel,
      uploadCsv,
      uploadPaste
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
              key: 'id'
            },
            {
              title: '学号',
              key: 'sid'
            },
            {
              title: '学生名',
              key: 'name'
            },
            {
              title: '性别',
              key: 'sex',
              render: function (h, params) {
                if (params.row.sex === '1') {
                  return h('div','男');
                }
                if (params.row.sex === '2') {
                  return h('div','女');
                }
              }
            },
            {
              title: '学校',
              key: 'universityName'
            },
            {
              title: '电话号码',
              key: 'tel'
            }
          ],
        data: [],
        loading: true,
        searchParams: searchParams,
        studentGrade: studentGrade,
        edit: {
          name: ''
        },
        user: this.$store.state.user,
        selected: [],
        operates: operates,
        operateType: null,
        create_modal_show_excel: false,
        create_modal_show_paste: false,
        create_modal_show_csv: false,
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
      modalNoShowCSV() {
        this.create_modal_show_csv = !this.create_modal_show_csv
      },
      modalNoShowEXCEL() {
        this.create_modal_show_excel = !this.create_modal_show_excel
      },
      modalNoShowPaste() {
        this.create_modal_show_paste = !this.create_modal_show_paste
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
      batchConfirm() {
        if (this.operateType == null) {
          this.$Message.warning("请选择操作类型");
          return;
        }
        if(this.selected==null||this.selected.length<1) {
          this.$Message.warning("请选择要操作的商品");
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
                const res = await deleteCustomer(JSON.stringify(this.selected));
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
        this.searchParams.name = '';
        this.searchParams.universityId = '';
        this.searchParams.sex = '';
        this.searchParams.sid = '';
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
      }
    }
  }
</script>
<style lang="scss" scoped>

</style>
