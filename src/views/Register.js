import React, { Component } from 'react'
import { withRouter } from 'react-router-dom'
import { List, InputItem, WhiteSpace, NavBar, Icon, Button, Toast, Flex, Radio } from 'antd-mobile';
import { createForm } from 'rc-form';
import { submitRegister } from '../api/index'
import { getVerigyCode } from '../api/index'
export class Register extends Component {
    constructor(props) {
        super(props)

        this.state = {
            mobile: '',
            code: '',
            email: '',
            pwd: '',
            verifyPwd: '',
            gender: '男',

        }
    }
    // 点击单选框时触发
    onChange = (gender) => {
        this.setState({
            gender
        });
    };
    // 点击立即注册按钮
    handleRegister = () => {
        // validateFields方法用于js校验, error是错误对象,如果没有就是null
        this.props.form.validateFields((error, value) => {
            if (error) {
                // 有错误,校验不通过
                Toast.fail('请检查数据是否填写正确', 2)
            } else {

                var mobile = this.state.mobile.replace(/\s/g, '')
                console.log(mobile)
                console.log(this.state)
                submitRegister({ ...this.state, mobile }).then(res => {
                    console.log(res);
                    const { meta: { status, msg } } = res.data
                    if (status === 200) {
                        // 提示注册成功
                        Toast.success(msg)
                        // 注册成功后返回首页
                        this.props.history.push('/')
                    }
                })
            }

        })
    }

    getCode = () => {
        // 这里的号码格式是139 9999 9999 ，提交之前把中间的空格去掉
        var mobile = this.state.mobile.replace(/\s/g, '')
        // 判断手机号码是否符合要求
        if (!/^1[3-9]\d{9}$/.test(mobile)) {
            Toast.fail('手机号码格式有误', 2)
        } else {

            getVerigyCode(mobile).then(res => {
                // 将验证码赋值给输入框
                console.log(res)
                const { meta: { status }, message } = res.data
                if (status === 200) {
                    this.setState({
                        code: message
                    })
                }
            })
        }

    }

    render() {
        const { getFieldError, getFieldProps } = this.props.form
        const RadioItem = Radio.RadioItem;

        return (
            <div>
                <NavBar
                    mode="dark"
                    leftContent={<Icon type='left' />}
                    onLeftClick={() => this.props.history.go(-1)}
                    style={{
                        position: 'fixed',
                        width: '100%',
                        left: 0,
                        top: 0,
                        zIndex: 1,
                    }}
                >
                    注册
                </NavBar>

                <List
                    style={{
                        marginTop: 45, position: 'relative'
                    }}

                >

                    <InputItem
                        // 输入类型为手机号码
                        type="phone"
                        placeholder="请输入手机号码"
                        // 输入框尾部清空按钮
                        clear
                        {...getFieldProps('phone', {
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
                        error={getFieldError('phone') ? true : false}
                        // 点击右侧的错误弹出提示
                        onErrorClick={() => {
                            Toast.info(getFieldError('phone')[0], 2)
                        }}
                        // 输入框输入改变时同步数据到state中的username
                        onChange={v => {
                            this.setState({
                                mobile: v
                            })
                        }}
                        // 将state中的username赋值给输入框
                        value={this.state.mobile}
                    >
                        <span className="star">*</span>  手机号码
                    </InputItem>
                    <button className="get-code" onClick={this.getCode}>获取验证码</button>


                    <InputItem
                        // 输入类型为数字
                        type="number"
                        placeholder="请输入验证码"
                        // 输入框尾部清空按钮
                        clear
                        {...getFieldProps('code', {
                            // 输入框失焦时验证
                            validateTrigger: 'onBlur',
                            // 验证规则
                            rules: [
                                { required: true, message: "验证码不能为空" },

                            ]
                        })
                        }
                        // 验证不通过时设置error为true
                        error={getFieldError('code') ? true : false}
                        // 点击右侧的错误弹出提示
                        onErrorClick={() => {
                            Toast.info(getFieldError('code')[0], 2)
                        }}
                        // 输入框输入改变时同步数据到state中的username
                        onChange={v => {
                            this.setState({
                                code: v
                            })
                        }}
                        // 将state中的username赋值给输入框
                        value={this.state.code}

                    >
                        <span className="star">*</span>
                        验证码

                    </InputItem>


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
                        <span className="star">*</span>  邮箱
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
                                pwd: v
                            })
                        }}
                        value={this.state.pwd}
                    >
                        <span className="star">*</span>  密码
                    </InputItem>
                    <InputItem
                        type="password"
                        placeholder="请输入确认密码"
                        clear
                        {...getFieldProps('verifyPwd', {
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

                        error={getFieldError('verifyPwd') ? true : false}
                        onErrorClick={() => {
                            Toast.fail(getFieldError('verifyPwd')[0], 2)
                        }}
                        onChange={v => {
                            this.setState({
                                verifyPwd: v
                            })
                        }}
                        value={this.state.verifyPwd}
                    >
                        <span className="star">*</span>   确认密码
                    </InputItem>

                    <RadioItem key={1} checked={this.state.gender === '男'} onChange={() => this.onChange('男')}>
                        <span className="star">*</span>  男
                    </RadioItem>
                    <RadioItem key={2} checked={this.state.gender === '女'} onChange={() => this.onChange('女')}>
                        <span className="star">*</span>   女
                    </RadioItem>
                    <WhiteSpace />
                    <Flex justify="center">
                        {/* 注册按钮 */}
                        <Button type="primary" size="small" style={{ marginRight: 10 }}
                            className="bottom-button"
                            onClick={this.handleRegister}>
                            立即注册
                    </Button>
                        {/* 取消注册按钮 */}
                        <Button type="warning" size="small" className="bottom-button" style={{ padding: '0 25px' }}
                            onClick={() => this.props.history.push('/login')}>
                            取消
                    </Button>

                    </Flex>
                    <WhiteSpace />

                </List>

                <style jsx>{`
                .bottom-button {
                    width: 30%;
                    padding: 10px 0;
                    display: flex;
                    justify-content: center;
                    align-items: center;
                }
                 .am-radio {
                     position: absolute;
                    padding: 10px;
                    left: -5px;
                    top: -2px;
                    border: 1px solid #ccc;
                    border-radius: 50%;
                    color: red;
                    margin-right: 5px;
                    }
                    .star {
                        color: red;
                    }
                    .get-code {
                        display: block;
                        width: auto;
                        height: 40px;
                        position: absolute;
                        right: 5px;
                       top: -17px;
                        transform: translateY(47%);
                        padding: 0 5px;
                        background-Color: #ccc;
                        color: white;
                        font-size: 14px;
                    }
                `}</style>

            </div>
        )
    }
}

export default createForm()(withRouter(Register))
