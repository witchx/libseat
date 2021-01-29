<template>
  <Modal class="modal" v-model="show" width="900" title="粘贴表格数据" @on-visible-change="showChange">
    <Row>
      <Card>
        <div class="update-paste-con">
          <paste-editor v-model="pasteDataArr" @on-success="handleSuccess" @on-error="handleError"/>
        </div>
        <div class="update-paste-btn-con">
          <span class="paste-tip">使用Tab键换列，使用回车键换行</span>
          <Button type="primary" style="float: right;" @click="handleShow">显示表格数据</Button>
        </div>
      </Card>
    </Row>
    <Row>
      <Card title="预览" class="margin-top-10">
        <Table :columns="columns" :data="tableData"/>
      </Card>
    </Row>
  </Modal>
</template>
<script>
  import PasteEditor from '_c/paste-editor'
  import { getTableDataFromArray } from '@/libs/util'
  import { createProductBatch } from '@/api/product';
  export default {
    props: {
      create_modal_show_paste: {
        type: Boolean,
        default: false
      }
    },
    components: {
      PasteEditor
    },
    data() {
      return {
        show: false,
        pasteDataArr: [],
        columns: [],
        tableData: [],
        validated: true,
        errorIndex: 0
      }
    },
    methods: {
      showChange(val) {
        if (!val) {
          this.$emit('noshow')
          this.$refs.form.resetFields();
        }
      },
      handleSuccess () {
        this.validated = true
      },
      handleError (index) {
        this.validated = false
        this.errorIndex = index
      },
      handleShow () {
        if (!this.validated) {
          this.$Notice.error({
            title: '您的内容不规范',
            desc: `您的第${this.errorIndex + 1}行数据不规范，请修改`
          })
        } else {
          let { columns, tableData } = getTableDataFromArray(this.pasteDataArr)
          this.columns = columns
          this.tableData = tableData
        }
      },
      async submit() {
        const res = await createProductBatch(JSON.stringify(this.tableData));
        if (res.data.code === 200) {
          this.$Message.success(res.data.msg);
        } else {
          this.$Message.warning(res.data.msg);
        }
      }
    },
    watch: {
      create_modal_show_paste: {
        handler: function (val1, val2) {
          if (this.show !== val1) {
            this.show = val1
          }
        },
        immediate: true
      }
    }
  }
</script>
<style lang="scss" scoped>
  .modal{
    .ivu-modal-body{
      max-height: 700px;
      overflow-y: scroll;
    }
  }
</style>
