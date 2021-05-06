import React, { Component } from 'react'
import {Link, withRouter} from 'react-router-dom';
import {NavBar, Icon, ActionSheet, Button,Toast} from 'antd-mobile'
import { connect } from 'react-redux'
import {getOrder, createPay, getRoomName} from '../api/index'
import '../style/pay.scss'
import { timestampToTime } from '../utils/time'

export class Pay extends Component {
    constructor(props) {
        super(props)

        this.state = {
            userId: this.props.userId,
            roomId: this.props.roomId,
            seatId: this.props.seatId,
            startTime: this.props.startTime,
            endTime: this.props.endTime,
            hours: this.props.hours,
            minutes: this.props.minutes,
            companyId: this.props.companyId,
            name: '',
            pay: 0,
            disable: false,
            orderId: this.props.orderId,
            couponId: '',
            orderInfo: ''
        }
    }
    UNSAFE_componentWillMount() {
        getOrder(this.state.orderId).then(res => {
            const { code,msg,data } = res.data
            // 获取数据成功
            if (code === 200) {
                this.setState({
                    orderInfo: data
                })
            }
            console.log(this.state)
        })
        if (this.state.orderInfo.type === 0) {
            //座位
            // 获取自习室名称
            getRoomName(this.state.roomId).then(res => {
                const { code,msg,data } = res.data
                // 获取数据成功
                if (code === 200) {
                    this.setState({
                        name: data,
                    })
                }
            })
            if(this.state.startTime >=this.state.endTime) {
                this.setState({
                    disable: true
                })
            }
        }
    }

    getInfo = (type) => {
        if (type === 1) {
            return this.state.orderInfo.money + "元"
        } else if (type === 2) {
            return this.state.orderInfo.times + "次"
        } else if(type === 3){
            return this.state.orderInfo.usefulLife + "天"
        }
    }

    getPay = () => {
        switch(this.state.orderInfo.type) {
            case 0:
                //座位
                return (
                    <div className="pay_box">
                        <div className="pay_top">{this.state.name}自习室&nbsp;{this.state.seatId}</div>
                        <p className="pay_alert">预约时间：</p>
                        <p className="pay_info">{timestampToTime(this.state.startTime)}</p>
                        <p className="pay_alert">预约时长：</p>
                        <p className="pay_info">{(this.state.minutes > 0)?(this.state.hours)+'小时'+(this.state.minutes)+'分钟':(this.state.hours-1)+'小时'+(this.state.minutes+60)+'分钟'}</p>
                        <div className="pay_mid"><span className="pay_big_alert">单价</span> <span className="pay_single">8元/小时</span></div>
                        <div className="pay_bottom">
                            <span className="pay_count_alert">费用总计</span>
                            <span className="pay_count_money">{this.state.orderInfo.price}</span>
                            <span className="pay_count_dan">元</span>
                        </div>
                        <img className="pay_img" src={require('../assets/imgs/chose_seat.png')}/>
                    </div>
                )
            case 1:
                //vip
                return (
                    <div className="pay_box">
                        <div className="pay_top_top">新购会员卡</div>
                        <div className="pay_top_mid">
                            <span className="pay_card_alert_left">充值：</span>
                            <span className="pay_card_info_left">{this.state.orderInfo.price}元</span>
                            <span className="pay_card_alert_right">到账：</span>
                            <span className="pay_card_info_right">{this.getInfo(this.state.orderInfo.cardType)}</span>
                        </div>
                        <p className="pay_top_bottom">{this.state.orderInfo.usefulLife?("有效期："+this.state.orderInfo.usefulLife):("不限有效期")}</p>
                        <div className="pay_mid"><span className="pay_big_alert">直接支付</span> <span className="pay_single">{this.state.orderInfo.price}元</span></div>
                        <div className="pay_bottom">
                            <span className="pay_count_alert">费用总计</span>
                            <span className="pay_count_money">{this.state.orderInfo.price}</span>
                            <span className="pay_count_dan">元</span>
                        </div>
                    </div>
                )
        }
    }

    showActionSheet = () => {
        if(this.state.orderId) {
            let BUTTONS = [];
            if (this.state.orderInfo.type === 0) {
                //座位
                BUTTONS = ['直接支付', '储值卡', '计次卡','期限卡', '取消'];
            } else if (this.state.orderInfo.type === 1) {
                //vip
                BUTTONS = ['直接支付', '取消'];
            }
            ActionSheet.showActionSheetWithOptions({
                    options: BUTTONS,
                    cancelButtonIndex: BUTTONS.length - 1,
                    title: '选择支付方式',
                    maskClosable: true
                },
                (buttonIndex) => {
                    console.log(buttonIndex)
                    if (buttonIndex !== BUTTONS.length - 1){
                        //支付
                        createPay({
                            orderType: this.state.orderInfo.type,
                            userId: this.state.companyId,
                            customerId: this.state.userId,
                            paymentType: buttonIndex,
                            orderId: this.state.orderId,
                            couponId: this.state.couponId,
                            discount: this.state.orderInfo.discount
                        }).then(res => {
                            const {code, msg, data} = res.data
                            console.log(data)
                            // 获取数据成功
                            if (code === 200) {
                                Toast.success('预约成功!', 2, () => {
                                    this.props.clearOrder();
                                    if (this.state.orderInfo.type === 0) {
                                        this.props.clearSeat();
                                        this.props.history.push('/result/'+this.state.orderId)
                                    } else if (this.state.orderInfo.type === 1) {
                                        this.props.clearCard();
                                        this.props.history.push('/result/'+this.state.orderId)
                                    }

                                })
                            } else {
                                Toast.fail(msg, 2)
                            }
                        })
                    }
                });
        }

    }

    render() {
        return (
            <div>
                {/* 顶部导航条 */}
                <NavBar
                    mode="dark"
                    icon={<Icon type="left" />}
                    leftContent={[
                        <span>支付</span>
                    ]}
                    onLeftClick={() => this.props.history.goBack()}
                />
                {this.getPay()}
                <div className="pay_center">
                    <p>
                        <Link to="/" style={{color: 'black'}}>场馆首页</Link>
                        <span>&nbsp;&nbsp;|&nbsp;&nbsp;</span>
                        <Link to="/my" style={{color: 'black'}}>个人中心</Link>
                    </p>
                    <span className="alert_bottom">励步*提供技术支持</span>
                </div>
                <div className="submit_order_footer">
                    <div className="submit_order_footer_left">
                        <span className="total-price">{this.state.orderInfo.price}</span>
                        <span className="total_dan">元</span>
                    </div>
                    <Button disable={this.state.disable} onClick={this.showActionSheet} className="submit_order" type="primary">确认支付</Button>
                </div>
            </div>
        )
    }
}

const mapStateToProps = (state) => {
    return {
        userId: state.userModule.id,
        roomId: state.seatModule.roomId,
        seatId: state.seatModule.seatId,
        startTime: state.seatModule.startTime,
        endTime: state.seatModule.endTime,
        hours: state.seatModule.hours,
        minutes: state.seatModule.minutes,
        orderId: state.orderModule.orderId,
        companyId: state.userModule.companyId
    }
}

const mapActionToProps = (dispatch) => {
    return {
        clearOrder: () => {
            dispatch({ type: 'CLEAR_ORDER'})
        },
        clearSeat: () => {
            dispatch({ type: 'CLEAR_SEAT'})
        },
        clearCard: () => {
            dispatch({ type: 'CLEAR_CARD'})
        }
    }
}
export default connect(mapStateToProps, mapActionToProps)(withRouter(Pay))
