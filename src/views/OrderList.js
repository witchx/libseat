import React, {Component, Fragment} from 'react'
import { NavBar, Icon, ListView, WhiteSpace} from 'antd-mobile';
import {getOrderList} from '../api/index'
import '../style/orderlist.scss'
import {connect} from "react-redux";
import {withRouter} from "react-router-dom";
import { strToTime } from '../utils/time';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import { library } from '@fortawesome/fontawesome-svg-core';
import { faClipboardList } from '@fortawesome/free-solid-svg-icons';
library.add(faClipboardList)
export class OrderList extends Component {
    constructor(props) {
        super(props)
        const dataSource = new ListView.DataSource({
            rowHasChanged: (row1, row2) => row1 !== row2,
        });
        this.state = {
            userId: this.props.id,
            data: null,
            isLoading: false,
            dataSource,
            height: document.documentElement.clientHeight * 0.95
        }
    }
    componentWillMount() {
        this.init()
    }

    // 初始化
    init = () => {
        getOrderList(this.state.userId).then(res => {
            const { code,msg,data } = res.data
            if (code === 200) {
                console.log(data)
                setTimeout(() => {
                    this.setState({
                        data: data,
                    })
                }, 600);
                setTimeout(() => {
                    const dataBlob = {};
                    for (let i = 0; i < this.state.data.length; i++) {
                        const ii = i;
                        dataBlob[`${ii}`] = `row - ${ii}`;
                    }
                    this.rData = dataBlob;
                    this.setState({
                        dataSource: this.state.dataSource.cloneWithRows(this.rData),
                        isLoading: true,
                    });

                }, 600);
            }
        })
    }

    getSingle = (obj) => {
        switch (obj.orderType) {
            case 0://座位
                return(
                    <div className="aOrder_box">
                        <div className="aOrder_left">
                            <FontAwesomeIcon className="aOrder_left_icon" icon="clipboard-list"/>
                        </div>
                        <div className="aOrder_center">
                            <div className="aOrder_center_top">预约座位</div>
                            <div className="aOrder_center_bottom">{strToTime(obj.orderTime)}</div>
                        </div>
                        <div className="aOrder_right">
                            <div className="aOrder_right_top">{obj.price}元</div>
                            <div className="aOrder_right_bottom">
                                {this.getStatus(obj.orderStatus)}
                            </div>
                        </div>
                    </div>
                )
            case 1://会员卡
                return(
                    <div className="aOrder_box">
                        <div className="aOrder_left">
                            <FontAwesomeIcon className="aOrder_left_icon" icon="clipboard-list"/>
                        </div>
                        <div className="aOrder_center">
                            <div className="aOrder_center_top">新购会员卡</div>
                            <div className="aOrder_center_bottom">{strToTime(obj.orderTime)}</div>
                        </div>
                        <div className="aOrder_right">
                            <div className="aOrder_right_top">{obj.price}元</div>
                            <div className="aOrder_right_bottom">
                                {this.getStatus(obj.orderStatus)}
                            </div>
                        </div>
                    </div>
                )
            case 3://优惠劵
                return (
                    <div className="aOrder_box">
                        <div className="aOrder_left">
                            <FontAwesomeIcon className="aOrder_left_icon" icon="clipboard-list"/>
                        </div>
                        <div className="aOrder_center">
                            <div className="aOrder_center_top">优惠劵</div>
                            <div className="aOrder_center_bottom">{strToTime(obj.orderTime)}</div>
                        </div>
                        <div className="aOrder_right">
                            <div className="aOrder_right_top">{obj.price}元</div>
                            <div className="aOrder_right_bottom">
                                {this.getStatus(obj.orderStatus)}
                            </div>
                        </div>
                    </div>
                )
            case 4://商品
                return (
                    <div className="aOrder_box">
                        <div className="aOrder_left">
                            <FontAwesomeIcon className="aOrder_left_icon" icon="clipboard-list"/>
                        </div>
                        <div className="aOrder_center">
                            <div className="aOrder_center_top">购买商品</div>
                            <div className="aOrder_center_bottom">{strToTime(obj.orderTime)}</div>
                        </div>
                        <div className="aOrder_right">
                            <div className="aOrder_right_top">{obj.price}元</div>
                            <div className="aOrder_right_bottom">
                                {this.getStatus(obj.orderStatus)}
                            </div>
                        </div>
                    </div>
                )
            default:
                break;
        }
    }
    getStatus = (status) => {
        switch (status) {
            case 1:
                return (
                    <p style={{color: 'red'}}>待支付</p>
                )
            case 2:
                return (
                    <p style={{color: 'rgb(245 165 109)'}}>待确认</p>
                )
            case 3:
                return (
                    <p style={{color: '#2641c7'}}>待评价</p>
                )
            case 4:
                return (
                    <p style={{color: '#22e856'}}>已完成</p>
                )
            case 5:
                return (
                    <p style={{color: '#676767'}}>已关闭</p>
                )
            case 6:
                return (
                    <p style={{color: '#676767'}}>已取消</p>
                )
            default:
                break
        }
    }

    render() {
        const separator = (sectionID, rowID) => (
            <div
                key={`${sectionID}-${rowID}`}
                style={{
                    backgroundColor: '#f2f2f2',
                    height: 3,
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
                <div onClick={() => {
                    const data = {
                        orderId: obj.orderId,
                        type: obj.orderType
                    }
                    this.props.history.push('/orderDetail/'+JSON.stringify(data));
                }}>
                    {this.getSingle(obj)}
                </div>
            )
        };
        return (
            <div>
                {this.props.location.pathname === "/order" ?<Fragment>
                    <NavBar
                        icon={<Icon type="left" />}
                        mode="dark"
                        onLeftClick={() => {
                            this.props.history.goBack()
                        }}
                        leftContent={[
                            <span>我的账单</span>
                        ]}
                    >
                    </NavBar>
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
                </Fragment>:''}
            </div>
        )
    }
}
const mapStateToProps = (state) => {
    return {
        id: state.userModule.id,
    }
}

export default connect(mapStateToProps)(withRouter(OrderList))
