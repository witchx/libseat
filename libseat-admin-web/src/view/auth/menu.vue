<template>
  <div>
    <Card style="margin-bottom: 10px">
      <Row>
        <Col span="20">
          <Icon size="18" type="md-list"></Icon>
          <label style="font-size: 15px;">&nbsp;数据列表</label>
        </Col>
        <Col span="4">
          <Button style="float: right" type="default" @click="createMenu">新建</Button>
        </Col>
      </Row>
    </Card>
    <Card>
      <tree-table expand-key="title" :expand-type="false" :loading="loading" :selectable="false" :columns="columns" :data="data" >
        <template slot="level" slot-scope="scope">
          <span>{{scope.row.level+1}}级</span>
        </template>
        <template slot="createTime" slot-scope="scope">
          <span>{{scope.row.createTime | timeFormat }}</span>
        </template>
        <template slot="modifyTime" slot-scope="scope">
          <span>{{ scope.row.modifyTime | timeFormat }}</span>
        </template>
        <template slot="icon" slot-scope="scope">
          <Icon :type="scope.row.icon" size="20" slot="prepend"></Icon>
        </template>
        <template slot="isEnable" slot-scope="scope">
          <i-switch v-model="scope.row.hidden" :true-value="0" :false-value="1" @on-change="switchHidden(scope)"/>
        </template>
        <template slot="action" slot-scope="scope">
          <Button type="default" size="small" @click="editMenu(scope)" style="margin-right: 5px">编辑</Button>
          <Button type="error" size="small" @click="deleteMenu(scope)">删除</Button>
        </template>
      </tree-table>
      <Page style="margin-top: 20px;float: right;" :total="searchParams.total" :page-size="searchParams.pageSize" show-total show-elevator show-sizer @on-change="handlePage" @on-page-size-change='handlePageSize'/>
    </Card>
    <Modal v-model="show.value" :title="model_title==='1'?'编辑':'创建'">
      <Form ref="form" :model="show" :rules="formRules" :label-width="100">
        <Row>
          <FormItem label="菜单名称:" prop="title" aria-required="true">
            <Input v-model="show.title"/>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="上级菜单:" prop="des" aria-required="true">
            <Select v-model="show.parentId" filterable  placeholder="请选择上级菜单">
              <Option v-for="(option, index) in parent_menu_option" :value="option.value" :key="index">{{option.label}}</Option>
            </Select>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="前端名称:" prop="name" aria-required="true">
            <Input v-model="show.name"/>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="前端图标:" prop="icon" aria-required="true">
            <Input style="width: 80%" v-model="show.icon"/><Icon style="margin-left: 15px" :type="show.icon" size="25"></Icon>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="是否显示:" aria-required="true">
            <RadioGroup v-model="show.hidden">
              <Radio :label="0">是</Radio>
              <Radio :label="1">否</Radio>
            </RadioGroup>
          </FormItem>
        </Row>
        <Row>
          <FormItem label="排序:" prop="sort" aria-required="true">
            <Input v-model="show.sort"/>
          </FormItem>
        </Row>
      </Form>
      <div slot="footer">
        <Button @click="show.value = false">取消</Button>
        <Button v-if="model_title==='1'" type="primary" @click="submitEdit">确定</Button>
        <Button v-if="model_title==='2'" type="primary" @click="submitCreate">确定</Button>
      </div>
    </Modal>
    <Modal v-model="show_delete.value" title="删除提示">
      <p> 确定删除？ </p>
      <div slot="footer">
        <Button @click="show_delete.value = false">取消</Button>
        <Button type="primary" @click="submitDelete">确定</Button>
      </div>
    </Modal>
  </div>
</template>

<script>
  import { getMenu, updateMenu, createMenu, deleteMenu } from '@/api/menu';
  import {timestampFormat} from '@/libs/tools';
  const searchParams = {
    page: 1,
    pageSize: 99999,
    total: 0
  }
  export default {
    name: 'tables_page',
    data () {
      return {
        columns: [
          { title: 'ID', key: 'id',width: 80, sortable: true },
          { title: '菜单名称', key: 'title'},
          { title: '菜单级数', key: 'level',type: 'template', template: 'level'},
          { title: '前端名称', key: 'name'},
          { title: '前端图标', key: 'icon',type: 'template', template: 'icon'},
          { title: '排序', key: 'sort'},
          { title: '创建时间', key: 'createTime',type: 'template', template: 'createTime' },
          { title: '修改时间', key: 'modifyTime',type: 'template', template: 'modifyTime'},
          {title: '是否启用', key: 'isEnable',type: 'template', template: 'isEnable'},
          {title: '操作', key: 'action', minWidth: '200px', type: 'template', template: 'action'}
        ],
        data: [],
        loading: true,
        show: {
          id: '',
          value: false,
          title: '',
          name: '',
          icon: '',
          sort: 0,
          hidden: 0,
          parentId: ''
        },
        show_delete: {
          value: false,
          id: ''
        },
        model_title: '',
        hidden: '',
        searchParams: searchParams,
        parent_menu_option: [],
        formRules: {
          title: [
            {required: true, message: "必输项不能为空", trigger: 'blur'}
          ],
          name:[
            {required: true, message: "必输项不能为空", trigger: 'blur'}
          ],
          icon:[
            {required: true, message: "必输项不能为空", trigger: 'blur'}
          ]
        }
      }
    },
    created() {
      this.fetchData()
    },
    filters: {
      timeFormat: function (value) {
        return timestampFormat(value,'year')
      }
    },
    methods: {
      async fetchData() {
        this.loading = true
        const res = await getMenu(this.searchParams)
        if (res.data.code === 200) {
          const menu = [];
          const rows = res.data.data.rows;
          for ( const i in rows) {
            if (rows[i].parentId === 0) {
              rows[i].children = []
              menu.push(rows[i])
            }
          }
          for ( const i in rows) {
            if (rows[i].parentId !== 0) {
              for (const j in menu) {
                if (menu[j].id === rows[i].parentId) {
                  menu[j].children.push(rows[i]);
                }
              }
            }
          }
          this.data = menu;
          this.searchParams.total = res.data.data.total
        } else {
          this.data=[];
          this.$Message.warning(res.data.msg);
        }
        setTimeout(() => {
          this.loading = false
        }, 300)
      },
      async editMenu(scope) {
        this.show.value = !this.show.value;
        if (this.show.value) {
          this.model_title = '1';
          this.show.id = scope.row.id;
          this.show.sort = scope.row.sort;
          this.show.hidden = scope.row.hidden;
          this.show.parentId = scope.row.parentId;
          this.show.name = scope.row.name;
          this.show.icon = scope.row.icon;
          this.show.title = scope.row.title;
          const res = await getMenu(this.searchParams)
          this.parent_menu_option.push( {value: 0, label: '无上级菜单'})
          if (res.data.code === 200) {
            res.data.data.rows.forEach(item => {
              this.parent_menu_option.push({value: item.id, label: item.title})
            })
          }
        }
      },
      async createMenu() {
        this.show.value = !this.show.value;
        if (this.show.value) {
          this.model_title = '2';
          this.show.sort = 0;
          this.show.hidden = 1;
          this.show.parentId = '';
          this.show.name = '';
          this.show.icon = '';
          this.show.title = '';
          this.show.id = '';
          const res = await getMenu(this.searchParams)
          this.parent_menu_option.push( {value: 0, label: '无上级菜单'})
          if (res.data.code === 200) {
            res.data.data.rows.forEach(item => {
              this.parent_menu_option.push({value: item.id, label: item.title})
            })
          }
        }
      },
      deleteMenu(scope) {
        this.show_delete.value = !this.show_delete.value;
        this.show_delete.id = scope.row.id;
      },
      async submitDelete() {
        const res = await deleteMenu(this.show_delete.id)
        if (res.data.code === 200) {
          this.$Message.success('删除成功！');
          this.show_delete.value = false;
          this.$emit('refresh');
          this.fetchData();
        }
      },
      async submitEdit() {
        this.$refs.form.validate(async (valid) => {
          if (valid) {
            const res = await updateMenu(this.show.id,this.show);
            if (res.data.code === 200) {
              this.$Message.success(res.data.msg);
              this.show.value = false;
              this.$emit('refresh');
              this.fetchData();
            } else {
              this.$Message.warning(res.data.msg);
            }
          }
        })

      },
      submitCreate() {
        this.$refs.form.validate(async (valid) => {
          if (valid) {
            const res = await createMenu(this.show)
            if (res.data.code === 200) {
              this.$Message.success(res.data.msg);
              this.show.value = false
              this.$emit('refresh');
              this.fetchData();
            } else {
              this.$Message.warning(res.data.msg);
            }
          } else {
            return false
          }
        })
      },
      async switchHidden(scope) {
        await updateMenu(scope.row.id,{hidden: scope.row.hidden})
      },
      handlePage(page) {
        this.searchParams.page = page
        this.fetchData()
      },
      handlePageSize(value) {
        this.searchParams.pageSize = value
        this.fetchData()
      }
    }
  }
</script>
<style>
</style>
