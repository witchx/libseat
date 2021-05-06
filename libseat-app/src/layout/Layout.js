import React, { Component } from 'react'
import { TabBar,NavBar } from 'antd-mobile';
import {withRouter} from 'react-router-dom'
import {connect} from 'react-redux'
export class Layout extends Component {
  render() {
    return (
        <>
          <TabBar
              unselectedTintColor="#bfbfbf"
              tintColor="#000000"
              barTintColor="white"
              tabBarPosition="bottom"
          >
            {/*
          unselectedTintColor	未选中的字体颜色
          tintColor	选中的字体颜色
          barTintColor	tabbar背景色
       */}
            {/*
          title	标题文字
          key	唯一标识
          icon 未选中的图标
          selectedIcon 选中的图标
          selected 是否选中
          badge 徽标
        */}
            <TabBar.Item
                title="场馆首页"
                key="Home"
                icon={<i className="iconfont iconhome" style={{color: '#bfbfbf'}}></i>}
                selectedIcon={<i className="iconfont iconhome" style={{color: '#000000'}}></i>}
                selected={this.props.location.pathname === '/'}
                onPress={() => {this.props.history.push('/')}}
            >
              {/* 利用props.children接收Layout组件innerHTML位置的内容 */}
              {this.props.children}
            </TabBar.Item>
            <TabBar.Item
                title="加入会员"
                key="Category"
                icon={<i className="iconfont icon-fenlei" style={{color: '#bfbfbf'}}></i>}
                selectedIcon={<i className="iconfont icon-fenlei" style={{color: '#000000'}}></i>}
                selected={this.props.location.pathname === '/card'}
                onPress={() => {this.props.history.push('/card')}}
            >

              {this.props.children}
            </TabBar.Item>
            <TabBar.Item
                title="使用记录"
                key="OrderSeat"
                icon={<i className="iconfont icongouwuche1" style={{color: '#bfbfbf'}}></i>}
                selectedIcon={<i className="iconfont icongouwuche1" style={{color: '#000000'}}></i>}
                selected={this.props.location.pathname === '/seatOrder'}
                onPress={() => {this.props.history.push('/seatOrder')}}
            >
              {this.props.children}
            </TabBar.Item>
            <TabBar.Item
                title={this.props.loginState?'个人中心': '未登录'}
                key="Mine"
                icon={<i className="iconfont iconweibiaoti2fuzhi12" style={{color: '#bfbfbf'}}></i>}
                selectedIcon={<i className="iconfont iconweibiaoti2fuzhi12" style={{color: '#000000'}}></i>}
                selected={['/login', '/register', '/my', '/mynologin'].includes(this.props.location.pathname)}
                onPress={() => {this.props.loginState? this.props.history.push('/my'): this.props.history.push('/mynologin')}}
            >
              {this.props.children}
            </TabBar.Item>
          </TabBar>
        </>


    )
  }
}

const mapStateToProps = state => {
  return {
    loginState: state.userModule.loginState,
    companyName: state.userModule.companyName
  }
}

export default connect(mapStateToProps)(withRouter(Layout))
