<template>
    <!--<div>
        <h1>
            请选择要查询的自习室
        </h1>
        <div style="margin-top: 20px">
            <Row>
                <Col :gutter="16" span="6" style="padding-right:10px">
                    <Select v-model="form.universityId" filterable remote :remote-method="debounce(searchUni, 300)" :loading="uni_loading" @on-change="chooseUni" placeholder="请输入搜索大学名称">
                        <Option v-for="(option, index) in uni_option" :value="option.value" :key="index">{{option.label}}</Option>
                    </Select>
                </Col>
                <Col :gutter="16" span="6" style="padding-right:10px">
                    <Select v-model="form.libraryId" filterable :disabled="!form.universityId" @on-change="chooseLib" :placeholder="form.universityId ? '请选择图书馆' : '请先选择大学'">
                        <Option v-for="(option, index) in lib_option" :value="option.value" :key="index">{{option.label}}</Option>
                    </Select>
                </Col>
                <Col :gutter="16" span="6" style="padding-right:10px">
                    <Select v-model="form.roomId" filterable :disabled="!form.libraryId" :placeholder="form.libraryId ? '请选择自习室' : '请先选择图书馆'">
                        <Option v-for="(option, index) in rom_option" :value="option.value" :key="index">{{option.label}}</Option>
                    </Select>
                </Col>
                <Col :gutter="16" span="4" style="padding-right:10px">
                    <Button type="primary" @click="handleSearch">查询</Button>
                </Col>
            </Row>
        </div>
        <div style="margin-top: 20px; position: relative">
            <seat :seat="seat" @refresh="handleSearch" />
            <Spin size="large" fix v-if="seat_loading"></Spin>
        </div>
    </div>-->
</template>
<script>
   /* import { getUniversity } from '@/api/university';
    import { getLibrary } from '@/api/library';
    import { getSeat } from '@/api/seat';
    import { getRoom } from '@/api/room';
    import seat from '@/components/seat';
    import lodash from 'lodash';
    export default {
        components: {
            seat
        },
        data() {
            return {
                uni_loading: false,
                form: {
                    universityId: '',
                    libraryId: '',
                    roomId: ''
                },
                uni_option: [],
                lib_option: [],
                rom_option: [],
                seat: [],
                seat_loading: false
            }
        },
        methods: {
            async searchUni(query) {
                if (query) {
                    this.uni_loading = true
                    const res = await getUniversity({
                        name: query,
                        page: 0,
                        pageSize: 99999
                    })
                    if ( res.code === 200 ) {
                        this.uni_option = res.data.rows.map(item => {
                            return {
                                value: item.id,
                                label: item.name
                            }
                        })
                    } else {
                        this.uni_option = [];
                        this.form.universityId = '';
                    }
                    setTimeout(() => {
                        this.uni_loading = false
                    }, 200)
                } else {
                    this.uni_option = [];
                    this.form.universityId = '';
                }
            },
            async chooseUni() {
                if ( this.form.universityId ) {
                    const res = await getLibrary({
                        id: this.form.universityId,
                        page: 0,
                        pageSize: 99999
                    })
                    if ( res.code === 200 ) {
                        this.lib_option = res.data.rows.map(item => {
                            return {
                                value: item.id,
                                label: item.name
                            }
                        })
                    } else {
                        this.lib_option = [];
                        this.form.libraryId = '';
                    }
                } else {
                    this.lib_option = [];
                    this.form.libraryId = '';
                }
            },
            async chooseLib() {
                if (this.form.libraryId) {
                    const res = await getRoom({
                        id: this.form.libraryId,
                        page: 0,
                        pageSize: 99999
                    })
                    if ( res.code === 200 ) {
                        this.rom_option = res.data.rows.map(item => {
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
                const res = await getSeat({roomId: this.form.roomId, startTime: Date.now(), endTime: Date.now() + 1})
                this.seat = res.data
                setTimeout(() => {
                    this.seat_loading = false
                }, 200)
            },
            debounce(fun, time) {
                return lodash.debounce(fun, time)
            }
        }
    }*/
</script>
<style lang="scss" scoped>

</style>
