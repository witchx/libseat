import React, { Component } from 'react'
import { withRouter } from 'react-router-dom'
import {connect} from "react-redux";
import {NavBar, Modal, Toast, Icon, Button} from 'antd-mobile';
import {getSeat, getRoomName, createOrder} from "../api";
import Seat from './Seat'
import '../style/roomdetail.scss'
import { timestampToTime } from '../utils/time'

export class RoomDetail extends Component {
    constructor(props) {
        super(props)
        let data = this.props.location.query;
        this.state = {
            seat: [],
            seatId: '',
            startTime: this.props.startTime,
            endTime: this.props.endTime,
            roomId: this.props.roomId,
            customerId: this.props.id,
            name: '',
            hours: this.props.hours,
            minutes: this.props.minutes,
            money: 0,
            userId: this.props.userId,
            stadiumId: this.props.stadiumId,
            companyId: this.props.companyId,
            discount: 0
        }

    }
    // 页面加载后获取数据
    UNSAFE_componentWillMount() {
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
        // 获取座位详情
        getSeat(this.state.roomId,this.state.startTime,this.state.endTime).then(res => {
            const { code,msg,data } = res.data
            // 获取数据成功
            if (code === 200) {
                this.setState({
                    seat: data,
                })
            }
        })

        let money = 0;
        if(this.state.minutes > 30) {
            money = (this.state.hours + 1) * 8
        } else if (this.state.minutes > 0){
            money = this.state.hours * 8 + 4
        } else if((this.state.minutes + 60) > 30) {
            money = this.state.hours * 8
        } else {
            money = (this.state.hours - 1) * 8 + 4
        }
        this.setState({
            money: money
        })
    }

    onChoose = (newValue) => {
        console.log(newValue)
        const newSeat = this.state.seat.map(line =>{
                return {
                    row: line.row.map(row => {
                        return row.seatId === newValue.seatId ? newValue : {...row, value: row.value === 3 ? 1 : row.value}
                    })
                }
            }
        )
        if (newValue.value === 3) {
            this.setState({seatId: newValue.seatId})
        }
        this.setState({seat: newSeat})
    }

    handleSubmit = () => {
        if (!this.state.seatId) {
            Toast.fail('请选择座位!', 2)
            return false
        }
        if ( this.state.startTime <= (Date.now()+15*60*1000)) {
            Toast.fail('请提前15分钟选择座位!', 2)
            return false
        }
        Modal.alert('提示',
            (<div>
                <p className="room_alert">预约座位：{this.state.seatId}</p>
                <p className="room_alert">预约时间：{timestampToTime(this.state.startTime)}</p>
                <p className="room_alert">预约时长：{(this.state.minutes > 0)?(this.state.hours)+'小时'+(this.state.minutes)+'分钟':(this.state.hours-1)+'小时'+(this.state.minutes+60)+'分钟'}</p>
                <p className="room_alert_bottom">预约到店时间前30分钟内不可取消，可提前30分钟到店。</p>
            </div>)
            , [
            {text: '取消', onPress: () => console.log('cancel')},
            {
                text: '确定',
                onPress: () => {
                    if (this.props.loginState) {
                        //创建订单
                        createOrder({
                            userId: this.state.companyId,
                            customerId: this.state.userId,
                            stadiumId: this.state.stadiumId,
                            price: this.state.money,
                            type: 0,
                            seatId: this.state.seatId,
                            startTime: this.state.startTime,
                            endTime: this.state.endTime,
                            discount: this.state.discount
                        }).then(res => {
                            const {code, msg, data} = res.data
                            console.log(data)
                            // 获取数据成功
                            if (code === 200) {
                                this.props.saveOrderId({orderId: data})
                                this.props.saveSeatId({seatId: this.state.seatId})
                                this.props.history.push('/pay')
                            }
                        })
                    } else {
                        Modal.alert('您还未登录', '即将为你跳转到登陆页面!', [
                            { text: '取消' },
                            { text: '确认',
                                onPress: () => {
                                    this.props.history.push('/mynologin')
                                }
                            }
                        ])
                    }
                }
            }
        ])
    }

    render() {
        return (
            <div>
                {/* 顶部导航条 */}
                <NavBar
                    className="room_top"
                    mode="dark"
                    icon={<Icon type="left" />}
                    leftContent={[
                        <span>{this.state.name}</span>
                    ]}
                    onLeftClick={() => this.props.history.goBack()}
                />
                <Seat onChoose={this.onChoose}
                      seat={this.state.seat}
                />
                <div className="box">
                    <div className="one">
                        <img src={require('../assets/imgs/empty_seat.png')}/>
                        <span>可用位置</span>
                    </div>
                    <div className="one">
                        <img src={require('../assets/imgs/chose_seat.png')}/>
                        <span>选中位置</span>
                    </div>
                    <div className="one">
                        <img src={require('../assets/imgs/disabled_seat.png')}/>
                        <span>已被占用</span>
                    </div>
                    <div className="two">
                        <Button type="primary" className="sub" onClick={this.handleSubmit}>确认选座</Button>
                    </div>
                </div>
            </div>

        )
    }
}
const mapStateToProps = state => {
    return {
        companyId: state.userModule.companyId,
        userId: state.userModule.id,
        stadiumId: state.userModule.stadiumId,
        roomId: state.seatModule.roomId,
        startTime: state.seatModule.startTime,
        endTime: state.seatModule.endTime,
        id: state.userModule.id,
        hours: state.seatModule.hours,
        minutes: state.seatModule.minutes,
        loginState: state.userModule.loginState
    }
}

// 创建映射函数
const mapDispatchToProps = (dispatch) => {
    return {
        saveSeatId:  (seatId) => {
            dispatch({ type: 'SAVE_SEAT_ID',payload: seatId })
        },
        saveOrderId:  (orderId) => {
            dispatch({ type: 'SAVE_ORDER_ID',payload: orderId })
        },
    }
}

export default connect(mapStateToProps,mapDispatchToProps)(withRouter(RoomDetail))
