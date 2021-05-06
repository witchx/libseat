import React, { Component } from 'react'
import { getUserInfo } from '../api/index'
import {TabBar, Card, WingBlank, WhiteSpace, List, NavBar} from 'antd-mobile'
import { withRouter } from 'react-router-dom'
import { connect } from 'react-redux'
import '../style/my.scss'
import user from "../assets/imgs/user.png";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import { library } from '@fortawesome/fontawesome-svg-core'
import { faList,faCogs,faCalendarCheck,faDiceTwo,faDiceOne,faDiceThree,faDiceFour,faArrowAltCircleRight } from '@fortawesome/free-solid-svg-icons'
library.add(faList,faCogs,faCalendarCheck,faDiceTwo,faDiceOne,faDiceThree,faDiceFour,faArrowAltCircleRight)
export class My extends Component {
    constructor(props) {
        super(props)

        this.state = {
            companyName: this.props.companyName,
            userId: this.props.id,
            totalValue: 0,
            totalTimes: 0,
            totalDays: 0,
            totalCoupon: 0,
            hoursByWeek: 0,
            isLoading: false,
        }
    }
    componentDidMount() {
        // 获取用户信息
        getUserInfo(this.state.userId).then(res => {
            const { code,msg,data } = res.data
            if (code === 200) {
                setTimeout(() => {
                    this.setState({
                        tel: data.tel,
                        nickname: data.nickname,
                        companyName: data.companyName,
                        avatar: data.icon,
                        totalValue: data.totalValue,
                        totalTimes: data.totalTimes,
                        totalDays: data.totalDays,
                        totalCoupon: data.totalCoupon,
                        hoursByWeek: data.hoursByWeek,
                        isLoading: true
                    })
                },600);

            }
        })
    }
    render() {
        return (
            <div>
                <NavBar
                    leftContent={this.props.companyName}
                    mode="dark"
                >
                </NavBar>
                <div  style={{background: '#118eea'}}>
                    {this.state.isLoading?<Card full style={{background: '#118eea',border:'0px'}}>
                        <Card.Header
                            title={<span style={{ color: '#ffffff',fontSize: '0.9em'}}>{this.state.nickname}用户</span>}
                            thumb={this.state.avatar?this.state.avatar:user}
                            thumbStyle={{ borderRadius: 50,border: '2px solid white',width: '65px', height: '65px'}}
                            style={{ fontSize: 24}}
                            extra={<span style={{ fontSize: 13, color: '#ffffff'}}>{this.state.tel}</span>}
                        />
                    </Card>:''}

                    <WingBlank size="lg" className="rank">
                        <span className="rank_left">学习排行</span>
                        <span className="rank_right" onClick={() => {
                            this.props.history.push('/rank');
                        }}>本周学习{this.state.hoursByWeek}小时<FontAwesomeIcon style={{marginLeft: '5px'}} icon="arrow-alt-circle-right"/></span>
                    </WingBlank>
                </div>
                <TabBar
                    unselectedTintColor="#000000"
                    tintColor="#000000"
                    barTintColor="white"
                >
                    <TabBar.Item
                        title="储值卡"
                        key="value"
                        icon={<FontAwesomeIcon icon="dice-one" style={{fontSize:'18px'}}/>}
                        onPress={() => { this.props.history.push('/myCard') }}
                    >
                    </TabBar.Item>
                    <TabBar.Item
                        title="计次卡"
                        key="would"
                        icon={<FontAwesomeIcon icon="dice-two" style={{fontSize:'18px'}}/>}
                        onPress={() => { this.props.history.push('/myCard') }}
                    >
                    </TabBar.Item>
                    <TabBar.Item
                        title="期限卡"
                        key="time"
                        icon={<FontAwesomeIcon icon="dice-three" style={{fontSize:'18px'}}/>}
                        onPress={() => { this.props.history.push('/myCard') }}
                    >
                    </TabBar.Item>
                    <TabBar.Item
                        title="优惠卷"
                        key="coupon"
                        icon={<FontAwesomeIcon icon="dice-four" style={{fontSize:'18px'}}/>}
                        onPress={() => { this.props.history.push('/myCard') }}
                    >
                    </TabBar.Item>
                </TabBar>
                <div className="my_box">
                    <div className="my_box_one">{this.state.totalValue}元</div>
                    <div className="my_box_one">{this.state.totalTimes}次</div>
                    <div className="my_box_one">{this.state.totalDays}天</div>
                    <div className="my_box_one">{this.state.totalCoupon}张</div>
                </div>
                <WhiteSpace size="lg" style={{background: '#ffffff'}}/>
                <WhiteSpace size="md"/>
                <List>
                    <List.Item
                        thumb={<FontAwesomeIcon icon="calendar-check" style={{fontSize:'18px'}}/>}
                        arrow="horizontal"
                        onClick={() => {
                            this.props.history.push('/seatOrder')
                        }}
                    >预约记录
                    </List.Item>
                </List>
                <WhiteSpace size="md"/>
                <List>
                    <List.Item
                        thumb={<FontAwesomeIcon icon="list" style={{fontSize:'18px'}}/>}
                        arrow="horizontal"
                        onClick={() => {
                            this.props.history.push('/order')
                        }}
                    >订单记录
                    </List.Item>
                    <List.Item
                        thumb={<FontAwesomeIcon icon="cogs" style={{fontSize:'18px'}}/>}
                        arrow="horizontal"
                        onClick={() => {
                            this.props.history.push('/setting')
                        }}
                    >用户设置
                    </List.Item>
                </List>
            </div>
        )
    }
}
const mapStateToProps = state => {
    return {
        companyName: state.userModule.companyName,
        id: state.userModule.id
    }
}
export default connect(mapStateToProps)(withRouter(My))
