import React, {Component, Fragment} from 'react'
import {Link, withRouter} from 'react-router-dom'
import {WhiteSpace} from 'antd-mobile'
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import { library } from '@fortawesome/fontawesome-svg-core'
import { far } from '@fortawesome/free-regular-svg-icons'
import {connect} from "react-redux";
import {getPay} from "../api";
import { strToTime } from '../utils/time';
import '../style/result.scss'
library.add(far)
export class Result extends Component {
    constructor(props) {
        super(props)
        this.state = {
            orderId: this.props.match.params.id,
            companyName: this.props.companyName,
            seconds: 5,
            isLoading: false,
            data: null,
            success: false
        }
    }
    componentWillMount() {
        getPay(this.state.orderId).then(res => {
            const {data,code,msg} = res.data;
            if (code === 200) {
                setTimeout(() => {
                    this.setState({
                        data: data,
                        isLoading: true,
                        success: true
                    })
                }, 600);
            }
        })
        let timer = setInterval(() => {
            this.setState((preState) =>({
                seconds: preState.seconds - 1,
            }),() => {
                if(this.state.seconds === 0){
                    clearInterval(timer);
                }
            });
        }, 1000)
    }
    getPaymentType = (type) => {
        switch (type) {
            case 0:
                return (
                    <span className="result_right">支付宝</span>
                )
            case 1:
                return (
                    <span className="result_right">储值卡</span>
                )
            case 2:
                return (
                    <span className="result_right">计次卡</span>
                )
            case 3:
                return (
                    <span className="result_right">期限卡</span>
                )
        }
    }
    render() {
        if (this.state.seconds === 0) {
            this.props.history.push('/')
        }
        return (
            <Fragment>
                {this.state.isLoading?<div>
                    {this.state.success?<div>
                        <div className="result_top">
                            <FontAwesomeIcon className="result_top_top" style={{color: '#5c8de4'}} icon={["far","check-circle"]}/>
                            <div className="result_top_mid">支付成功</div>
                            <div className="result_top_bottom">{this.state.seconds}秒后返回首页</div>
                        </div>
                        <div className="result_box">
                            <div className="result_box_top">{this.state.companyName}</div>
                            <div className="result_one">
                                <div className="result_left">账单时间</div>
                                <div className="result_right">{strToTime(this.state.data.time)}</div>
                            </div>
                            <div className="result_one">
                                <div className="result_left">账单金额</div>
                                <div className="result_right">{this.state.data.price}</div>
                            </div>
                            <div className="result_one">
                                <div className="result_left">交易方式</div>
                                {this.getPaymentType(this.state.data.paymentType)}
                            </div>
                            <div className="result_one">
                                <div className="result_left">交易单号</div>
                                <div className="result_right">{this.state.data.no}</div>
                            </div>
                            <div className="result_one">
                                <div className="result_right" style={{color: 'cyan'}} onClick={() => {
                                    const data = {
                                        orderId: this.state.orderId,
                                        type: this.state.data.type
                                    }
                                    this.props.history.push('/orderDetail/'+JSON.stringify(data));
                                }}>查看账单明细</div>
                            </div>
                        </div>
                    </div>:<div>
                        <div className="result_top">
                            <FontAwesomeIcon className="result_top_top" style={{color: 'red'}} icon={["far","times-circle"]}/>
                            <div className="result_top_mid">支付失败</div>
                            <div className="result_top_bottom">{this.state.seconds}秒后返回首页</div>
                        </div>
                    </div>}
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
        )
    }
}
const mapStateToProps = state => {
    return {
        companyName: state.userModule.companyName
    }
}

export default  connect(mapStateToProps)(withRouter(Result))
