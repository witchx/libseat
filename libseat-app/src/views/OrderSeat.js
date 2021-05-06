import React, {Component, Fragment} from 'react'
import { withRouter } from 'react-router-dom'
import { connect } from 'react-redux'
import {Modal, WingBlank, NavBar, Tabs, ListView, WhiteSpace, Toast, ActionSheet} from 'antd-mobile'
import { getSeatOrderList,updateOrder } from '../api/index'
import { strToTime } from '../utils/time'
import '../style/orderseat.scss'
import Rate from './Rate'
const tabs = [
    { title: '全部', index: '0' },
    { title: '已消费', index: '1' },
    { title: '未消费', index: '2' },
    { title: '已取消', index: '3' }
];
let star = 0;
export class OrderSeat extends Component {
    constructor(props) {
        super(props)
        const dataSource = new ListView.DataSource({
            rowHasChanged: (row1, row2) => row1 !== row2,
        });
        const dataSource1 = new ListView.DataSource({
            rowHasChanged: (row1, row2) => row1 !== row2,
        });
        const dataSource2 = new ListView.DataSource({
            rowHasChanged: (row1, row2) => row1 !== row2,
        });
        const dataSource3 = new ListView.DataSource({
            rowHasChanged: (row1, row2) => row1 !== row2,
        });
        this.state = {
            userId: this.props.id,
            companyName: this.props.companyName,
            data: null,
            data1: null,
            data2: null,
            data3: null,
            isLoading: false,
            dataSource,
            dataSource1,
            dataSource2,
            dataSource3,
            height: document.documentElement.clientHeight * 0.85
        }
    }
    componentWillMount() {
        this.init()
    }

    // 初始化
    init = () => {
        getSeatOrderList(this.state.userId).then(res => {
            // 将数据解构处理
            const { code,msg,data } = res.data
            // 状态码200表示获取购物车数据成功
            if (code === 200) {
                let data1 = [];
                let data2 = [];
                let data3 = [];
                for(let i=0; i<data.length; i++) {
                    switch (data[i].type) {
                        case 1:
                            data2.push(data[i]);
                            continue;
                        case 2:
                            data2.push(data[i]);
                            continue;
                        case 3:
                            data1.push(data[i]);
                            continue;
                        case 4:
                            data1.push(data[i]);
                            continue;
                        case 5:
                            data3.push(data[i]);
                            continue;
                        case 6:
                            data3.push(data[i]);
                            continue;
                        default:
                            continue;
                    }
                }
                setTimeout(() => {
                    this.setState({
                        data: data,
                        data1: data1,
                        data2: data2,
                        data3: data3,
                    })
                }, 600);
                setTimeout(() => {
                    //全部
                    const dataBlob = {};
                    for (let i = 0; i < this.state.data.length; i++) {
                        const ii = i;
                        dataBlob[`${ii}`] = `row - ${ii}`;
                    }
                    //已消费
                    const dataBlob1 = {};
                    for (let i = 0; i < this.state.data1.length; i++) {
                        const ii = i;
                        dataBlob1[`${ii}`] = `row - ${ii}`;
                    }
                    //未消费
                    const dataBlob2 = {};
                    for (let i = 0; i < this.state.data2.length; i++) {
                        const ii = i;
                        dataBlob2[`${ii}`] = `row - ${ii}`;
                    }
                    //已取消
                    const dataBlob3 = {};
                    for (let i = 0; i < this.state.data3.length; i++) {
                        const ii = i;
                        dataBlob3[`${ii}`] = `row - ${ii}`;
                    }
                    this.rData = dataBlob;
                    this.rData1 = dataBlob1;
                    this.rData2 = dataBlob2;
                    this.rData3 = dataBlob3;
                    this.setState({
                        dataSource: this.state.dataSource.cloneWithRows(this.rData),
                        dataSource1: this.state.dataSource1.cloneWithRows(this.rData1),
                        dataSource2: this.state.dataSource2.cloneWithRows(this.rData2),
                        dataSource3: this.state.dataSource3.cloneWithRows(this.rData3),
                        isLoading: true,
                    });

                }, 1000);
            }
        })
    }

    getSingle = (obj) => {
        switch (obj.type) {
            case 1://待支付
            case 2://待确认
                return(
                    <div className="sOrder_box">
                        <div className="sOrder_top">
                            <span>{obj.roomName}自习室</span>
                            <span className="sOrder_top_right">{obj.seatId}</span>
                        </div>
                        <div className="sOrder_top_right_one">未消费</div>
                        <div className="sOrder_mid">
                            预约时间
                        </div>
                        <div  className="sOrder_bottom">
                            {strToTime(obj.startTime)}&nbsp;&nbsp;-&nbsp;&nbsp;{strToTime(obj.endTime)}
                        </div>
                        <div className="sOrder_bottom_right_one" onClick={() => {
                            this.cancelAlert(obj.orderId)
                        }}>取消预约</div>
                    </div>
                )
            case 3://待评价
                return (
                    <div className="sOrder_box">
                        <div className="sOrder_top">
                            <span>{obj.roomName}自习室</span>
                            <span className="sOrder_top_right">{obj.seatId}</span>
                        </div>
                        <div className="sOrder_top_right_two">已消费</div>
                        <div className="sOrder_mid">
                            预约时间
                        </div>
                        <div  className="sOrder_bottom">
                            {strToTime(obj.startTime)}&nbsp;&nbsp;-&nbsp;&nbsp;{strToTime(obj.endTime)}
                        </div>
                        <div className="sOrder_bottom_right_two" onClick={()=>{
                            this.showActionSheet(obj.orderId)
                        }}>待评价</div>
                    </div>
                )
            case 4://已完成
                return (
                    <div className="sOrder_box">
                        <div className="sOrder_top">
                            <span>{obj.roomName}自习室</span>
                            <span className="sOrder_top_right">{obj.seatId}</span>
                        </div>
                        <div className="sOrder_top_right_two">已消费</div>
                        <div className="sOrder_mid">
                            预约时间
                        </div>
                        <div  className="sOrder_bottom">
                            {strToTime(obj.startTime)}&nbsp;&nbsp;-&nbsp;&nbsp;{strToTime(obj.endTime)}
                        </div>
                    </div>
                )
            case 5://已关闭
            case 6://已取消
                return (
                    <div className="sOrder_box">
                        <div className="sOrder_top">
                            <span>{obj.roomName}自习室</span>
                            <span className="sOrder_top_right">{obj.seatId}</span>
                        </div>
                        <div className="sOrder_top_right_three">已取消</div>
                        <div className="sOrder_mid">
                            预约时间
                        </div>
                        <div  className="sOrder_bottom">
                            {strToTime(obj.startTime)}&nbsp;&nbsp;-&nbsp;&nbsp;{strToTime(obj.endTime)}
                        </div>
                    </div>
                )
            default:
                break;
        }
    }

    getStarNum = (index) => {
        star = index;
    }

    showActionSheet = (orderId) => {
        const message = (
            <div style={{height: '130px'}}>
                <div style={{height: '70px',lineHeight: '70px',fontSize: '18px'}}>感谢您的评价</div>
                <Rate
                    count = {5}
                    className = {'rate_star'}
                    parent = {this}
                >
                </Rate>
            </div>
        )
        ActionSheet.showActionSheetWithOptions({
                options: ['确定'],
                message: message,
                maskClosable: true
            },
            (buttonIndex) => {
                if (buttonIndex === 0) {
                    updateOrder(orderId,{
                        evaluate: star,
                        progress: 4,
                        status: 4,
                        type: 0
                    }).then(res => {
                        const { code,msg,data } = res.data
                        // 状态码200表示获取购物车数据成功
                        if (code === 200) {
                            Toast.success(msg, 2);
                            this.init()
                        } else {
                            Toast.fail(msg, 2);
                        }
                        console.log(res)
                    })
                }
            });
    }

    cancelAlert = (orderId) => {
        const alertInstance = Modal.alert('取消预约', '确定取消该预约？', [
            { text: '取消'},
            { text: '确定', onPress: () => {
                    updateOrder(orderId,{
                        status: 6,
                        type: 0
                    }).then(res => {
                        const { code,msg,data } = res.data
                        // 状态码200表示获取购物车数据成功
                        if (code === 200) {
                            Toast.success(msg, 2);
                            this.init();
                        } else {
                            Toast.fail(msg, 2);
                        }
                        console.log(res)
                    })
            }},
        ]);
        setTimeout(() => {
            // 可以调用close方法以在外部close
            console.log('auto close');
            alertInstance.close();
        }, 500000);
    }

    render() {
        const separator = (sectionID, rowID) => (
            <div
                key={`${sectionID}-${rowID}`}
                style={{
                    backgroundColor: '#f2f2f2',
                    height: 20,
                    borderTop: '1px solid #ECECED',
                    borderBottom: '1px solid #ECECED',
                }}
            />
        );
        let index = 0;
        const row = (rowData, sectionID, rowID) => {
            if (index > this.state.data.length - 1) {
                index = 0;
            }
            const obj = this.state.data[index++];
            return (
                <div>
                    {this.getSingle(obj)}
                </div>
            )
        };
        let index1 = 0;
        const row1 = (rowData, sectionID, rowID) => {
            if (index1 > this.state.data1.length - 1) {
                index1 = 0;
            }
            const obj = this.state.data1[index1++];
            return (
                <div>
                    {this.getSingle(obj)}
                </div>
            )
        };
        let index2 = 0;
        const row2 = (rowData, sectionID, rowID) => {
            if (index2 > this.state.data2.length - 1) {
                index2 = 0
            }
            const obj = this.state.data2[index2++];
            return (
                <div>
                    {this.getSingle(obj)}
                </div>
            )
        };
        let index3 = 0;
        const row3 = (rowData, sectionID, rowID) => {
            if (index3 > this.state.data3.length - 1) {
                index3 = 0;
            }
            const obj = this.state.data3[index3++];
            return (
                <div>
                    {this.getSingle(obj)}
                </div>
            )
        };
        return (
            <div>
                {/* 顶部导航条 */}
                {this.props.location.pathname === "/seatOrder" ?
                    <Fragment>
                        <NavBar mode="dark" leftContent={this.state.companyName}>

                        </NavBar>

                        <Tabs tabs={tabs}
                              initialPage={0}
                              renderTab={tab => <span>{tab.title}</span>}
                              animated={false} useOnPan={false}
                        >
                            <WingBlank size="lg">
                                {this.state.isLoading?<ListView
                                    ref={el => this.lv = el}
                                    dataSource={this.state.dataSource}
                                    renderRow={row}
                                    renderSeparator={separator}
                                    pageSize={this.state.data.length-1}
                                    scrollRenderAheadDistance={500}
                                    style={{
                                        height: this.state.height
                                    }}
                                >
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <span className="card_bottom_alert">励步*提供技术支持</span>
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                </ListView>:''}
                            </WingBlank>

                            <WingBlank size="lg">
                                {this.state.isLoading?<ListView
                                    ref={el => this.lv = el}
                                    dataSource={this.state.dataSource1}
                                    renderRow={row1}
                                    renderSeparator={separator}
                                    pageSize={this.state.data1.length-1}
                                    scrollRenderAheadDistance={500}
                                    style={{
                                        height: this.state.height
                                    }}
                                >
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <span className="card_bottom_alert">励步*提供技术支持</span>
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                </ListView>:''}
                            </WingBlank>
                            <WingBlank size="lg">
                                {this.state.isLoading?<ListView
                                    ref={el => this.lv = el}
                                    dataSource={this.state.dataSource2}
                                    renderRow={row2}
                                    renderSeparator={separator}
                                    pageSize={this.state.data2.length-1}
                                    scrollRenderAheadDistance={500}
                                    style={{
                                        height: this.state.height
                                    }}
                                >
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <span className="card_bottom_alert">励步*提供技术支持</span>
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                </ListView>:''}
                            </WingBlank>
                            <WingBlank size="lg">
                                {this.state.isLoading?<ListView
                                    ref={el => this.lv = el}
                                    dataSource={this.state.dataSource3}
                                    renderRow={row3}
                                    renderSeparator={separator}
                                    pageSize={this.state.data3.length-1}
                                    scrollRenderAheadDistance={500}
                                    style={{
                                        height: this.state.height
                                    }}
                                >
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <span className="card_bottom_alert">励步*提供技术支持</span>
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                </ListView>:''}
                            </WingBlank>
                        </Tabs>
                    </Fragment>
                    : ''}
            </div >
        )
    }
}

const mapStateToProps = (state) => {
    return {
        id: state.userModule.id,
        companyName: state.userModule.companyName,
    }
}

export default connect(mapStateToProps)(withRouter(OrderSeat))
