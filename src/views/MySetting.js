import React, { Component } from 'react'
import { getUserDetail,updateUser,checkPassword ,uploadImage} from '../api/index'
import { withRouter } from 'react-router-dom'
import { CSSTransition } from 'react-transition-group'
import { Button, Modal,NavBar,Icon,List,Toast,Picker,ImagePicker } from 'antd-mobile'
import { connect } from 'react-redux'
import { createForm } from 'rc-form';
import '../style/mysetting.scss'
const alert = Modal.alert;
const sex = [
    {
        label: '男',
        value: 1,
    },
    {
        label: '女',
        value: 2,
    }
]
export class MySetting extends Component {
    constructor(props) {
        super(props)

        this.state = {
            isShow:true,
            isLoading: false
        }
    }
    componentDidMount() {
        this.fetchData()
    }
    fetchData = () => {
        // 获取用户信息
        getUserDetail(this.props.id).then(res => {
            const { code,msg,data } = res.data
            console.log(res.data)
            if (code === 200) {
                setTimeout(() => {
                    this.setState({
                        id: data.id,
                        tel: data.tel,
                        nickname: data.nickname,
                        companyName: data.companyName,
                        avatar: data.icon,
                        sex: data.sex,
                        username: data.username,
                        email: data.email,
                        files: [{
                            url: data.icon,
                            id: '1',
                        }],
                        isLoading:true
                    })
                    }, 300);

            }
        })
    }
    // 退出
    logout = () => {
        alert('即将退出账号', '您确定吗?', [
            {
                text: '取消',
                style: {
                    backgroundColor: '#777',
                    color: '#fff',
                    fontWeight: 700
                }
            },
            {
                text: '确定',
                style: {
                    backgroundColor: 'rgb(244, 51, 60)',
                    color: '#fff',
                    fontWeight: 700
                },
                onPress: () => {
                    // 退出
                    this.props.loginOut()
                    this.props.clearCard()
                    this.props.clearOrder()
                    this.props.clearSeat()
                    this.props.history.push('/mynologin')
                }
            }
        ])
    }
    onChange = async (files, type, index) => {
        this.setState({
            files,
        });
        console.log(files)
        if (type === 'add') {
            let formData = new FormData();
            let file = files[0].file;
            formData.append('file', file);
            const res = await uploadImage(this.state.id, formData)
            if (res.data.code === 200) {
                Toast.success('修改成功!', 2)
                setTimeout(() => {
                    this.fetchData()
                }, 2000)
            } else {
                Toast.fail(res.data.msg, 2)
            }
        }
    }
    render() {
        const { getFieldProps } = this.props.form;
        const {isShow}=this.state;
        const { files } = this.state;
        console.log(this.state)
        return (
            <div>
                <NavBar
                    icon={<Icon type="left" />}
                    mode="dark"
                    onLeftClick={() => {
                        this.props.history.go(-1)
                    }}
                    leftContent={[
                        <span>设置</span>
                    ]}
                >{this.props.history.location.query ? this.props.history.location.query.name : ''}</NavBar>
                <div className="bg_div"></div>
                {this.state.isLoading?<CSSTransition in={isShow}
                               classNames="card"
                               timeout={10000}
                               appear>
                   <div className="user_card">
                       <div style={{width:'100%'}}>
                           <ImagePicker
                               length="1"
                               files={files}
                               onChange={this.onChange}
                               onImageClick={(index, fs) => console.log(index, fs)}
                               selectable={files.length < 1}
                               multiple="false"
                               style={{width: '40%',marginLeft: '30%'}}
                           />
                       </div>
                        <List>
                            <List.Item extra={this.state.nickname+'用户'}
                                       onClick={() => {
                                           Modal.prompt('用户名', '请输入用户名!', [
                                               { text: '取消' },
                                               { text: '确认',
                                                   onPress: (value) => {
                                                       return new Promise(async (resolve) => {
                                                           if (/^[a-zA-Z0-9_-]{4,16}$/.test(value)) {
                                                               const res = await updateUser(this.state.id,{nickname: value})
                                                               if (res.data.code === 200) {
                                                                   Toast.success('修改成功!',2)
                                                                   setTimeout(() => {
                                                                       resolve()
                                                                       this.fetchData()
                                                                   }, 2000)
                                                               } else {
                                                                   Toast.fail(res.data.msg,2)
                                                               }
                                                           } else {
                                                               Toast.fail('请输入正确用户名!', 2)
                                                           }
                                                       })
                                                   }
                                               },
                                           ], 'default', this.state.nickname)}}
                            >
                                用户名
                            </List.Item>
                            <List.Item extra={this.state.username}>
                                账号
                            </List.Item>
                            <List.Item
                                earrow="horizontal"
                                extra={this.state.tel}
                                onClick={() => {
                                    Modal.prompt('手机号', '请输入手机号!', [
                                        { text: '取消' },
                                        { text: '确认',
                                            onPress: (value) => {
                                                return new Promise(async (resolve) => {
                                                    if (/^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\d{8}$/.test(value)) {
                                                        const res = await updateUser(this.state.id,{tel: value})
                                                        if (res.data.code === 200) {
                                                            Toast.success('修改成功!',2)
                                                            setTimeout(() => {
                                                                resolve()
                                                                this.fetchData()
                                                            }, 2000)
                                                        } else {
                                                            Toast.fail(res.data.msg,2)
                                                        }
                                                    } else {
                                                        Toast.fail('请输入正确手机号!', 2)
                                                    }
                                                })
                                            }
                                        },
                                    ], 'default', this.state.tel)}
                                }
                            >手机号
                            </List.Item>
                            <Picker data={sex}
                                    cols={1}
                                    {...getFieldProps('sex', {
                                        initialValue: [this.state.sex]
                                    })}
                                    extra="请选择"
                                    onOk={e => {
                                        console.log(e[0])
                                        this.fetchData()
                                        updateUser(this.state.id, {sex: e[0]}).then(res => {
                                            const { code,msg,data } = res.data
                                            if (code === 200) {
                                                Toast.success('修改成功!', 2)
                                                setTimeout(() => {
                                                }, 2000)
                                            } else {
                                                this.fetchData()
                                                Toast.fail(res.data.msg, 2)
                                            }
                                        })
                                    }}
                                    onDismiss={() => {}}
                            >
                                <List.Item arrow="horizontal">性别</List.Item>
                            </Picker>
                            <List.Item
                                extra={this.state.email}
                                onClick={() => {
                                    Modal.prompt('电子邮箱', '请输入邮箱!', [
                                        { text: '取消' },
                                        { text: '确认',
                                            onPress: (value) => {
                                                return new Promise(async (resolve) => {
                                                    if (/^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/.test(value)) {
                                                        const res = await updateUser(this.state.id,{email: value})
                                                        if (res.data.code === 200) {
                                                            Toast.success('修改成功!',2)
                                                            setTimeout(() => {
                                                                resolve()
                                                                this.fetchData()
                                                            }, 2000)
                                                        } else {
                                                            Toast.fail(res.data.msg,2)
                                                        }
                                                    } else {
                                                        Toast.fail('请输入正确邮箱!', 2)
                                                    }
                                                })
                                            }
                                        },
                                    ], 'default', this.state.email)
                                }}
                            >电子邮箱
                            </List.Item>
                            <List.Item arrow="horizontal"
                                  extra="点击修改"
                                  onClick={() => {
                                      Modal.prompt(
                                          '修改密码',
                                          '请输入原密码',
                                          async password => {
                                              const res = await checkPassword({ username: this.state.username, password: password})
                                              console.log(res)
                                              if (res.data.code === 200) {
                                                  Modal.prompt(
                                                      '修改密码',
                                                      '请输入新密码',
                                                      async password => {
                                                          const res = await updateUser(this.state.id,{password:password})
                                                          if (res.data.code === 200) {
                                                              Toast.success('修改成功!',2)
                                                          } else {
                                                              Toast.fail(res.data.msg,2)
                                                          }
                                                      },
                                                      'secure-text',
                                                      null,
                                                      ['请输入新密码'],
                                                  )
                                              } else {
                                                  Toast.fail('密码错误!校验失败!', 2)
                                              }
                                          },
                                          'secure-text',
                                          null,
                                          ['请输入原密码'],
                                      )
                                  }}
                            >密码
                            </List.Item>
                        </List>
                    </div>
                </CSSTransition>:''}
                <Button onClick={this.logout} className="saveButton">退出登录</Button>
            </div>
        )
    }
}
// 创建映射函数
const mapDispatchToProps = (dispatch) => {
    return {
        // 退出
        loginOut: () => {
            dispatch({ type: 'LOGINOUT' })
        },
        clearCard: () => {
            dispatch({ type: 'CLEAR_CARD' })
        },
        clearOrder: () => {
            dispatch({ type: 'CLEAR_ORDER' })
        },
        clearSeat: () => {
            dispatch({ type: 'CLEAR_SEAT' })
        }
    }
}
const mapStateToProps = state => {
    return {
        id: state.userModule.id
    }
}
export default connect(mapStateToProps, mapDispatchToProps)(createForm() (withRouter(MySetting)))
