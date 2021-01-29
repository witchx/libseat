<style lang="less">
  @import "./common.less";
</style>
<template>
  <Modal class="modal" v-model="show" width="900" title="上传EXCEL" @on-visible-change="showChange">
    <div>
      <Card title="导入EXCEL">
        <Row>
          <Upload action="" :before-upload="handleBeforeUpload" accept=".xls, .xlsx" style="float: left;margin-right: 10px">
            <Button icon="ios-cloud-upload-outline" :loading="uploadLoading" @click="handleUploadFile">上传文件</Button>
          </Upload>
          <div class="ivu-upload-list-file" v-if="file !== null" style="float: left">
            <Icon type="ios-stats"></Icon>
            {{ file.name }}
            <Icon v-show="showRemoveFile" type="ios-close" class="ivu-upload-list-remove" @click.native="handleRemove()"></Icon>
          </div>
        </Row>
        <Row>
          <transition name="fade">
            <Progress v-if="showProgress" :percent="progressPercent" :stroke-width="2">
              <div v-if="progressPercent == 100">
                <Icon type="ios-checkmark-circle"></Icon>
                <span>成功</span>
              </div>
            </Progress>
          </transition>
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
  import excel from '@/libs/excel'
  import { createProductBatch } from '@/api/product';
  export default {
    props: {
      create_modal_show_excel: {
        type: Boolean,
        default: false
      }
    },
    data() {
      return {
        show: false,
        uploadLoading: false,
        progressPercent: 0,
        showProgress: false,
        showRemoveFile: false,
        file: null,
        tableData: [],
        tableTitle: [],
        tableLoading: false
      }
    },
    methods: {
      initUpload () {
        this.file = null
        this.showProgress = false
        this.loadingProgress = 0
        this.tableData = []
        this.tableTitle = []
      },
      handleUploadFile () {
        this.initUpload()
      },
      handleRemove () {
        this.initUpload()
        this.$Message.info('上传的文件已删除！')
      },
      handleBeforeUpload (file) {
        const fileExt = file.name.split('.').pop().toLocaleLowerCase()
        if (fileExt === 'xlsx' || fileExt === 'xls') {
          this.readFile(file)
          this.file = file
        } else {
          this.$Notice.warning({
            title: '文件类型错误',
            desc: '文件：' + file.name + '不是EXCEL文件，请选择后缀为.xlsx或者.xls的EXCEL文件。'
          })
        }
        return false
      },
      // 读取文件
      readFile (file) {
        const reader = new FileReader()
        reader.readAsArrayBuffer(file)
        reader.onloadstart = e => {
          this.uploadLoading = true
          this.tableLoading = true
          this.showProgress = true
        }
        reader.onprogress = e => {
          this.progressPercent = Math.round(e.loaded / e.total * 100)
        }
        reader.onerror = e => {
          this.$Message.error('文件读取出错')
        }
        reader.onload = e => {
          this.$Message.info('文件读取成功')
          const data = e.target.result
          const { header, results } = excel.read(data, 'array')
          const tableTitle = header.map(item => { return { title: item, key: item } })
          this.tableData = results
          this.tableTitle = tableTitle
          this.uploadLoading = false
          this.tableLoading = false
          this.showRemoveFile = true
        }
      },
      showChange(val) {
        if (!val) {
          this.$emit('noshow')
          this.$refs.form.resetFields();
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
      create_modal_show_excel: {
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
