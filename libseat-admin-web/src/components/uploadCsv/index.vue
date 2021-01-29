<template>
  <Modal class="modal" v-model="show" width="900" title="上传Csv" @on-visible-change="showChange">
    <div>
      <Card title="导入CSV">
        <Row>
          <Upload action="" :before-upload="beforeUpload">
            <Button icon="ios-cloud-upload-outline">上传Csv文件</Button>
          </Upload>
          <div class="ivu-upload-list-file" v-if="file !== null" style="float: left">
            <Icon type="ios-stats"></Icon>
            {{ file.name }}
            <Icon v-show="showRemoveFile" type="ios-close" class="ivu-upload-list-remove" @click.native="handleRemove()"></Icon>
          </div>
        </Row>
      </Card>
      <Card title="预览" class="margin-top-10">
        <Table :columns="tableTitle" :data="tableData" :loading="tableLoading"></Table>
      </Card>
    </div>
    <div slot="footer">
      <Button @click="show = false">取消</Button>
      <Button type="primary" @click="submit">确定</Button>
    </div>
  </Modal>
</template>
<script>
  import { getArrayFromFile, getTableDataFromArray } from '@/libs/util'
  import { createProductBatch } from '@/api/product';
  export default {
    props: {
      create_modal_show_csv: {
        type: Boolean,
        default: false
      },
      seat: {
        type: Array,
        default: []
      }
    },
    data() {
      return {
        show: false,
        showRemoveFile: false,
        file: null,
        tableData: [],
        tableTitle: [],
        tableLoading: false
      }
    },
    methods: {
      showChange(val) {
        if (!val) {
          this.$emit('noshow')
          this.$refs.form.resetFields();
        }
      },
      initUpload () {
        this.file = null
        this.tableData = []
        this.tableTitle = []
      },
      handleRemove () {
        this.initUpload()
        this.$Message.info('上传的文件已删除！')
      },
      beforeUpload (file) {
        getArrayFromFile(file).then(data => {
          let { columns, tableData } = getTableDataFromArray(data)
          this.tableTitle = columns
          this.tableData = tableData
        }).catch(() => {
          this.$Notice.warning({
            title: '只能上传Csv文件',
            desc: '只能上传Csv文件，请重新上传'
          })
        })
        return false
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
      create_modal_show_csv: {
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
