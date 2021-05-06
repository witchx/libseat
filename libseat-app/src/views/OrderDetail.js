import React, {Component, Fragment} from 'react'
import { NavBar, Icon, WhiteSpace} from 'antd-mobile';
import {getOrderDetail} from '../api/index'
import '../style/orderdetail.scss'
import { strToTime } from '../utils/time';
import {Link} from "react-router-dom";
export class OrderDetail extends Component {
    constructor(props) {
        super(props)
        const {orderId,type}= JSON.parse(this.props.match.params.data);
        this.state = {
            orderId: orderId,
            orderType: type,
            data: null,
            isLoading: false
        }
    }
    componentWillMount() {
        getOrderDetail(this.state.orderId,this.state.orderType).then(res => {
            const {data,code,msg} = res.data;
            if (code === 200) {
                setTimeout(() => {
                    this.setState({
                        data: data,
                        isLoading: true
                    })
                }, 600);
            }
        })
    }

    getStatus = (status) => {
        switch (status) {
            case 1:
                return (
                    <p>待支付</p>
                )
            case 2:
                return (
                    <p>待确认</p>
                )
            case 3:
                return (
                    <p>待评价</p>
                )
            case 4:
                return (
                    <p>已完成</p>
                )
            case 5:
                return (
                    <p>已关闭</p>
                )
            case 6:
                return (
                    <p>已取消</p>
                )
            default:
                break
        }
    }

    getInfo = (type) => {
        if (type === 1) {
            return this.state.data.vipCard.money + "元"
        } else if (type === 2) {
            return this.state.data.vipCard.times + "次"
        } else if(type === 3){
            return this.state.data.vipCard.usefulLife + "天"
        }
    }

    getTop = (type) => {
        switch (type) {
            case 0://座位
                return(
                    <div className="order_box">
                        <div className="order_box_top">账单详情</div>
                        <hr />
                        <div className="order_one">
                            <span className="order_left">预约桌位&nbsp;[{this.state.data.orderSeat.seatId}]</span>
                            <span className="order_right">{this.state.data.price}元</span>
                        </div>
                        <hr />
                        <div className="order_one">
                            <span className="order_right">{this.state.data.price}元</span>
                        </div>
                        <WhiteSpace size="lg"/>
                        <div className="order_one">
                            <span className="order_left">开始时间</span>
                            <span className="order_right">{strToTime(this.state.data.orderSeat.startTime)}</span>
                        </div>
                        <div className="order_one">
                            <span className="order_left">结束时间</span>
                            <span className="order_right">{strToTime(this.state.data.orderSeat.endTime)}</span>
                        </div>
                    </div>
                )
            case 1://会员卡
                return(
                    <div className="order_box">
                        <div className="order_box_top">账单详情</div>
                        <hr />
                        <div className="order_one">
                            <span className="order_left">新购会员卡&nbsp;[{this.state.data.vipCard.name}]</span>
                            <span className="order_right">{this.state.data.price}元</span>
                        </div>
                        <hr />
                        <div className="order_one">
                            <span className="order_right">{this.state.data.price}元</span>
                        </div>
                        <WhiteSpace size="lg"/>
                        <div className="order_one">
                            <span className="order_left">充值</span>
                            <span className="order_right">{this.getInfo(this.state.data.vipCard.type)}</span>
                        </div>
                    </div>
                )
            case 4://商品
                return (
                    <div className="order_box">
                        <div className="order_box_top">账单详情</div>
                        <hr />
                        <div className="order_one">
                            <span className="order_left">购买商品&nbsp;[{this.state.data.product.name}]</span>
                            <span className="order_right">{this.state.data.price}元</span>
                        </div>
                        <hr />
                        <div className="order_one">
                            <span className="order_right">{this.state.data.price}元</span>
                        </div>
                        <WhiteSpace size="lg"/>
                        <div className="order_one">
                            <span className="order_left">单价</span>
                            <span className="order_right">{this.state.data.product.price}元</span>
                        </div>
                        <div className="order_one">
                            <span className="order_left">数量</span>
                            <span className="order_right">{this.state.data.orderProduct.num}</span>
                        </div>
                    </div>
                )
            default:
                break;
        }
    }
    getPaymentType = (type) => {
        switch (type) {
            case 0:
                return (
                   <span className="order_right">直接支付</span>
                )
            case 1:
                return (
                    <span className="order_right">储值卡</span>
                )
            case 2:
                return (
                    <span className="order_right">计次卡</span>
                )
            case 3:
                return (
                    <span className="order_right">期限卡</span>
                )
        }
    }
    getBottom = (status) => {
        switch (status) {
            case 3://签到
                return (
                    <div className="order_box">
                        <div className="order_one">
                            <span className="order_left">确认时间</span>
                            <span className="order_right">{strToTime(this.state.data.confirmTime)}</span>
                        </div>
                    </div>
                )
            case 4://评价
                return (
                    <div className="order_box">
                        <div className="order_one">
                            <span className="order_left">评价时间</span>
                            <span className="order_right">{strToTime(this.state.data.evaluateTime)}</span>
                        </div>
                        <div className="order_one">
                            <span className="order_left">评价等级</span>
                            <span className="order_right">{this.state.data.evaluate}⭐</span>
                        </div>
                    </div>
                )
            default:
                break;
        }
    }
    render() {
        return (
            <div>
                <Fragment>
                    <NavBar
                        icon={<Icon type="left" />}
                        mode="dark"
                        onLeftClick={() => {
                            this.props.history.goBack()
                        }}
                        leftContent={[
                            <span>账单详情</span>
                        ]}
                    >
                    </NavBar>
                    {this.state.isLoading?<div>
                        <WhiteSpace size="lg"/>
                        <WhiteSpace size="lg"/>
                        <div className="order_top_top"><span className="order_top_top_left">{this.state.data.price}</span>元</div>
                        <div className="order_top_bottom">{this.getStatus(this.state.data.status)}</div>
                        <WhiteSpace size="lg"/>
                        <WhiteSpace size="lg"/>
                        {this.getTop(this.state.data.type)}
                        <WhiteSpace size="lg"/>
                        {this.state.data.paymentType?<div className="order_box">
                            <div className="order_one">
                                <span className="order_left">支付方式</span>
                                <span className="order_right">{this.getPaymentType(this.state.data.paymentType)}</span>
                            </div>
                            <div className="order_one">
                                <span className="order_left">账单时间</span>
                                <span className="order_right">{strToTime(this.state.data.createTime)}</span>
                            </div>
                            <div className="order_one">
                                <span className="order_left">账单号</span>
                                <span className="order_right">{this.state.data.no}</span>
                            </div>
                        </div>:''}
                        <WhiteSpace size="lg"/>
                        {this.getBottom(this.state.data.status)}
                    </div>:''}
                    <div className="pay_center">
                        <p>
                            <Link to="/" style={{color: 'black'}}>场馆首页</Link>
                            <span>&nbsp;&nbsp;|&nbsp;&nbsp;</span>
                            <Link to="/my" style={{color: 'black'}}>个人中心</Link>
                        </p>
                        <span className="alert_bottom">励步*提供技术支持</span>
                    </div>
                    <WhiteSpace size="lg"/>
                    <WhiteSpace size="lg"/>
                </Fragment>
            </div>
        )
    }
}

export default OrderDetail
