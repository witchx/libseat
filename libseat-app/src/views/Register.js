import React, { Component } from 'react'
import { withRouter } from 'react-router-dom'
import { List, InputItem, WhiteSpace, NavBar, Icon, Button, Toast, Flex, Radio,Picker } from 'antd-mobile';
import { createForm } from 'rc-form';
import { submitRegister,getAllCompany} from '../api/index'
import '../style/register.scss'
export class Register extends Component {
    constructor(props) {
        super(props)

        this.state = {
            tel: '',
            code: '',
            inputCode: '',
            email: '',
            password: '',
            password2: '',
            sex: 1,
            nickname: '',
            userId: '',
            data: null
        }
    }

    componentWillMount() {
        this.changeCode()
        getAllCompany().then(res => {
            const {data,code,msg} = res.data;
            if (code === 200) {
                this.setState({
                    data: data
                })
            }
        })
    }
    // 点击单选框时触发
    onChange = sex => {
        this.setState({
            sex:sex
        });
    };
    // 点击立即注册按钮
    handleRegister = () => {
        // validateFields方法用于js校验, error是错误对象,如果没有就是null
        this.changeCode();
        this.props.form.validateFields((error, value) => {
            if (error) {
                // 有错误,校验不通过
                Toast.fail('请检查数据是否填写正确', 2)
            } else if(this.state.code !== this.state.inputCode) {
                Toast.fail('验证码错误', 2)
            } else if(this.state.password !== this.state.password2) {
                Toast.fail('两次输入的密码不相同', 2)
            }else {
                var tel = this.state.tel.replace(/\s/g, '')
                submitRegister({
                    nickname: this.state.nickname,
                    tel: tel,
                    sex: this.state.sex,
                    email: this.state.email,
                    username: tel,
                    password: this.state.password,
                    userId: this.state.userId[0]
                }).then(res => {
                    console.log(res);
                    const { code,msg,data } = res.data
                    if (code === 200) {
                        // 提示注册成功
                        Toast.success(msg)
                        // 注册成功后返回登录页面
                        this.props.history.push('/login')
                    } else {
                        Toast.fail(msg)
                    }
                })
            }

        })
    }
    // 获得验证码
    createCode = () => {
        let code = "";
        let codeLength = 4;//验证码的长度
        let random = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 'a',    'b', 'c', 'd', 'e', 'f', 'g',
            'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'];//随机数
        for (let i = 0; i < codeLength; i++) {//循环操作
            const index = Math.floor(Math.random() * 36);//取得随机数的				索引（0~35）
            code += random[index];//根据索引取得随机数加到code上
        }
        this.setState({
            code: code
        })
    }
    //点击切换验证码
    changeCode = () => {
        this.createCode()
    }

    render() {
        const { getFieldError, getFieldProps } = this.props.form
        const RadioItem = Radio.RadioItem;
        return (
            <div>
                {this.props.location.pathname === '/register' ?
                    <NavBar
                        icon={<Icon type="left" />}
                        mode="dark"
                        onLeftClick={() => {
                            this.props.history.goBack()
                        }}
                        leftContent={[
                            <span>注册</span>
                        ]}
                    >
                    </NavBar>: ''}
                <List style={{position: 'relative'}}>
                    <InputItem
                        // 输入类型为手机号码
                        type="phone"
                        placeholder="请输入手机号码"
                        // 输入框尾部清空按钮
                        clear
                        {...getFieldProps('tel', {
                            // 输入框失焦时验证
                            validateTrigger: 'onBlur',
                            // 验证规则
                            rules: [
                                { required: true, message: "手机号码不能为空" },
                                { min: 11, message: "手机号码必须为11位" },
                            ]
                        })
                        }
                        // 验证不通过时设置error为true
                        error={getFieldError('tel') ? true : false}
                        // 点击右侧的错误弹出提示
                        onErrorClick={() => {
                            Toast.info(getFieldError('tel')[0], 2)
                        }}
                        // 输入框输入改变时同步数据到state中的username
                        onChange={v => {
                            this.setState({
                                tel: v
                            })
                        }}
                        // 将state中的username赋值给输入框
                        value={this.state.tel}
                    >
                        <span className="star">*</span> 手机号码
                    </InputItem>
                    <InputItem
                        placeholder="请输入昵称"
                        clear
                        {...getFieldProps('nickname', {
                            // 输入框失焦时验证
                            validateTrigger: 'onBlur',
                            // 验证规则
                            rules: [
                                { required: true, message: "昵称不能为空" },
                                { min: 5, message: "昵称最小长度为5位" },
                            ]
                        })
                        }
                        error={getFieldError('nickname') ? true : false}
                        onErrorClick={() => {
                            Toast.info(getFieldError('nickname')[0], 2)
                        }}
                        onChange={v => {
                            this.setState({
                                nickname: v
                            })
                        }}
                        value={this.state.nickname}
                    >
                        <span className="star">*</span> 昵称
                    </InputItem>
                    <Picker data={this.state.data}
                            cols={1}
                            onOk={v => this.setState({ userId: v })}
                            {...getFieldProps('userId', {
                                // 输入框失焦时验证
                                validateTrigger: 'onBlur',
                                // 验证规则
                                rules: [
                                    { required: true, message: "公司不能为空" }
                                ]
                            })}
                    >
                        <List.Item arrow="horizontal">
                            <span className="star">*</span> 公司
                        </List.Item>
                    </Picker>
                    <InputItem
                        // 输入类型为邮箱
                        type="email"
                        placeholder="请输入邮箱"
                        // 输入框尾部清空按钮
                        clear
                        {...getFieldProps('email', {
                            // 输入框失焦时验证
                            validateTrigger: 'onBlur',
                            // 验证规则
                            rules: [
                                { required: true, message: "邮箱不能为空" },
                                { type: 'email', message: '错误的 email 格式' }
                            ]
                        })
                        }
                        // 验证不通过时设置error为true
                        error={getFieldError('email') ? true : false}
                        // 点击右侧的错误弹出提示
                        onErrorClick={() => {
                            Toast.info(getFieldError('email')[0], 2)
                        }}
                        // 输入框输入改变时同步数据到state中的email
                        onChange={v => {
                            this.setState({
                                email: v
                            })
                        }}
                        // 将state中的email赋值给输入框
                        value={this.state.email}
                    >
                        <span className="star">*</span> 邮箱
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
                        error={getFieldError('password') ? true : false}
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
                        <span className="star">*</span>  密码
                    </InputItem>
                    <InputItem
                        type="password"
                        placeholder="请输入确认密码"
                        clear
                        {...getFieldProps('password2', {
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
                        error={getFieldError('password2') ? true : false}
                        onErrorClick={() => {
                            Toast.fail(getFieldError('password2')[0], 2)
                        }}
                        onChange={v => {
                            this.setState({
                                password2: v
                            })
                        }}
                        value={this.state.password2}
                    >
                        <span className="star">*</span> 确认密码
                    </InputItem>
                    <RadioItem key={1} checked={this.state.sex === 1} onChange={() => this.onChange(1)}>
                        <span className="star">*</span> 男
                    </RadioItem>
                    <RadioItem key={2} checked={this.state.sex === 2} onChange={() => this.onChange(2)}>
                        <span className="star">*</span> 女
                    </RadioItem>
                    <InputItem
                        placeholder="请输入验证码"
                        // 输入框尾部清空按钮
                        clear
                        extra={
                            <div style={{width: '100px',fontSize: '20px'}}
                                 onClick={this.changeCode}>{this.state.code}</div>
                        }
                        {...getFieldProps('inputCode', {
                            // 输入框失焦时验证
                            validateTrigger: 'onBlur',
                            // 验证规则
                            rules: [
                                { required: true, message: "验证码不能为空" },

                            ]
                        })
                        }
                        // 验证不通过时设置error为true
                        error={getFieldError('inputCode') ? true : false}
                        // 点击右侧的错误弹出提示
                        onErrorClick={() => {
                            Toast.info(getFieldError('inputCode')[0], 2)
                        }}
                        // 输入框输入改变时同步数据到state中的username
                        onChange={v => {
                            this.setState({
                                inputCode: v
                            })
                        }}
                        // 将state中的username赋值给输入框
                        value={this.state.inputCode}
                    >
                        <span className="star">*</span> 验证码
                    </InputItem>
                    <WhiteSpace />
                    <Flex justify="center">
                        {/* 注册按钮 */}
                        <Button type="primary" size="small" style={{ marginRight: 10, maxWidth: 120 }}
                            className="bottom-button"
                            onClick={this.handleRegister}>
                            立即注册
                            </Button>
                        {/* 取消注册按钮 */}
                        <Button type="warning" size="small" className="bottom-button" style={{ padding: '0 25px', maxWidth: 120 }}
                            onClick={() => this.props.history.push('/login')}>
                            取消
                            </Button>

                    </Flex>
                    <WhiteSpace />

                </List>

            </div>
        )
    }
}

export default createForm()(withRouter(Register))
