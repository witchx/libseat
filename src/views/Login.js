import React, { Component } from 'react'
import { withRouter } from 'react-router-dom'
import {connect} from 'react-redux'
import { List, InputItem, WhiteSpace, WingBlank, Card, Button, Toast } from 'antd-mobile';
import { createForm } from 'rc-form';
import {submitLogin, getInfo, getCompanyInfo, getUserRank} from '../api/index'
import axios from 'axios'
import loginBg from "../assets/imgs/login-bg.jpg";
import Cookies from 'js-cookie'

export class Login extends Component {
    constructor(props) {
        super(props)
        this.state = {
            // 预设用户名和密码为13999999999，123456
            // 用户名
            username: '',
            // 密码
            password: '',
            companyId: '',
        }
    }
    UNSAFE_componentWillMount() {
        if(!this.props.loginState && this.props.location.state && this.props.location.state.from.pathname !== '/login') {
            // 如果跳转之前的不是登录页面，跳转到登录页面时提示请登录
        // 先判断是否有this.props.location.state，有的话意味着是从其他需要登录才能访问的页面跳转过来，否则就是直接访问登录页面
            Toast.info('请先登录', 1)
        }
    }
    // 点击登录
    handleLogin = () => {
        this.props.form.validateFields((error, value) => {
            // value为getFieldProps中指定的值
            if (error) {
                // 有错误,校验不通过
                Toast.fail('请检查数据是否填写正确', 1)
            } else {
                let obj = {
                    // 这里的号码格式是139 9999 9999 ，提交之前把中间的空格去掉
                    username: value.username.replace(/\s/g, ''),
                    password: value.password
                  }
                submitLogin(obj).then(res => {
                    // 解构赋值
                    const {code,msg,data} = res.data
                    // 状态码为200的即登录成功
                    if (code === 200) {
                        // 登录成功将token设置在请求头
                        Toast.success(msg, 1)
                        let token = data
                        axios.defaults.headers.common['Authorization'] = token
                        // 修改userReducer中的登录状态
                        this.props.changeLoginState({Login: true, token})
                        Cookies.set("token", token, { expires: 1 })
                        // 获取用户信息
                        getInfo(token).then(res => {
                            const {code,msg,data} = res.data
                            if (code === 200) {
                                this.setState({
                                    companyId: data.companyId
                                })
                                this.props.saveCompanyInfo({companyId: data.companyId,companyName:data.companyName})
                                this.props.saveUserID({id:data.id})
                            }
                            getCompanyInfo(this.state.companyId).then(res => {
                                // 解构赋值
                                const {code, data, msg} = res.data
                                if (code === 200) {
                                    this.props.saveStadiumId({stadiumId: data[0].stadiumId})
                                }
                            })
                        })
                        // 获取location中的from
                        const {from} = this.props.location.state || {from: {pathname: '/'}}
                        // 获取pathname
                        let pathname = from.pathname
                        if (pathname === '/') {
                            pathname = '/my'
                        }
                        // 登录成功的话弹框提示，1秒后消失
                        Toast.success(msg, 1, () => {
                            this.props.history.replace(pathname)
                        })
                    } else {
                        // 否则提示错误信息
                        Toast.fail(msg, 1)
                    }
                })
            }
        })

    }

    render() {
        const { getFieldError, getFieldProps } = this.props.form;
        return (
            <div className="login" style={{ backgroundImage: 'url('+loginBg+')',height:'100%'}}>
                <h2 style={{textAlign: 'center', margin: 'auto',paddingTop: '20%',color: '#fff'}}> SIGN UP</h2>
                <WingBlank size="lg">
                    <Card style={{transform: 'translateY(70%)'}}>
                        <Card.Body>
                            <List>
                                <InputItem
                                    // 输入类型为手机号码
                                    type="phone"
                                    placeholder="请输入手机号码"
                                    // 输入框尾部清空按钮
                                    clear
                                    {...getFieldProps('username', {
                                        // 输入框失焦时验证
                                        validateTrigger: 'onBlur',
                                        // 验证规则
                                        rules: [
                                            { required: true, message: "用户名不能为空" },
                                            { min: 11, message: "手机号码必须为11位" },
                                        ]
                                    })
                                    }
                                    // 验证不通过时设置error为true
                                    error={getFieldError('username') ? true : false}
                                    // 点击右侧的错误弹出提示
                                    onErrorClick={() => {
                                        Toast.info(getFieldError('username')[0], 2)
                                    }}
                                    // 输入框输入改变时同步数据到state中的username
                                    onChange={v => {
                                        this.setState({
                                            username: v
                                        })
                                    }}
                                    // 将state中的username赋值给输入框
                                    value={this.state.username}
                                >
                                    用户名
                                </InputItem>
                                <InputItem
                                    type="password"
                                    placeholder="请输入密码"
                                    clear
                                    {...getFieldProps('password', {
                                        validateTrigger: 'onBlur',
                                        rules: [
                                            {
                                                required: true, message: '密码不能为空'
                                            },
                                            {
                                                min: 6, message: '密码不能低于6位'
                                            }
                                        ]
                                    })}
                                    error={getFieldError('password') ? true: false}
                                    onErrorClick={() => {
                                        Toast.fail(getFieldError('password')[0], 2)
                                    }}
                                    onChange={v => {
                                        this.setState({
                                            password: v
                                        })
                                    }}
                                    value={this.state.password}
                                >
                                    密码
                                </InputItem>
                                <List.Item/>
                                <List.Item>
                                    <Button type="primary"
                                            style={{background: 'black'}}
                                            onClick={this.handleLogin}>
                                        登陆
                                    </Button>
                                </List.Item>
                                <List.Item>
                                    <Button onClick={() => this.props.history.push('/register')}>
                                        注册
                                    </Button>
                                </List.Item>
                            </List>
                        </Card.Body>
                    </Card>
                </WingBlank>
            </div>
        )
    }
}
// // 创建state映射函数

const mapStateToProps = state => {
    return {
        loginState: state.userModule.loginState
    }
}

// 创建映射函数，登录成功改变userReducer中的登录状态为true
const mapActionToProps = dispatch => {
    return {
        changeLoginState: newState => {
            dispatch({type: 'CHANGE_LOGIN_STATE', payload: newState})
        },
        saveCompanyInfo: company =>{
            dispatch({type: 'SAVE_COMPANY_INFO', payload: company})
        },
        saveUserID: id =>{
            dispatch({type: 'SAVE_USER_ID', payload: id})
        },
        saveStadiumId:  (stadiumId) => {
            dispatch({ type: 'SAVE_STADIUM_ID',payload: stadiumId })
        },
    }
}

export default connect(mapStateToProps, mapActionToProps)(createForm()(withRouter(Login)))
