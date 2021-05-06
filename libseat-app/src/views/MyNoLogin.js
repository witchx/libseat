import React, { Component } from 'react'
import {withRouter} from 'react-router-dom'
import {WingBlank, Flex, TabBar, NavBar} from 'antd-mobile'
import '../style/mynologin.scss'
import {connect} from "react-redux";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
export class MyNoLogin extends Component {
    constructor(props) {
        super(props)

        this.state = {
            companyName: this.props.companyName
        }
    }
    render() {
        return (
            <div>
                <header>
                    <NavBar
                        leftContent={this.props.companyName}
                        mode="dark"
                    >
                    </NavBar>
                    <WingBlank style={{ marginTop: '0.53333333333rem'}}>
                        <Flex justify="between">
                            <div className="avatar">
                                <div className="wrapper">
                                    <i className="iconfont icon-icontouxiang"></i>
                                </div>
                                <span>未登录</span>
                            </div>
                            <button className="goto-login" onClick={() => this.props.history.push('/login')}>立即登录</button>
                        </Flex>
                    </WingBlank>
                </header>
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

            </div>
        )
    }
}
const mapStateToProps = state => {
    return {
        companyName: state.userModule.companyName
    }
}
export default connect(mapStateToProps) (withRouter(MyNoLogin))
