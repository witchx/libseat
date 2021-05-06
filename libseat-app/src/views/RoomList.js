import React, { Component } from 'react'
import { withRouter } from 'react-router-dom'
import {connect} from "react-redux";
import { NavBar, List, Icon } from 'antd-mobile';
import {getRoomList} from "../api";

export class RoomList extends Component {
    constructor(props) {
        super(props)

        this.state = {
            list: []
        }
    }
    // 页面加载后获取数据
    UNSAFE_componentWillMount() {
        // 获取商品详情
        getRoomList(this.props.stadiumId,this.props.startTime,this.props.endTime).then(res => {
            const { code,msg,data } = res.data
            // 获取数据成功
            if (code === 200) {
                this.setState({
                    list: data
                })
            }
            console.log(this.state)
        })
    }

    render() {
        return (
            <div>
                {/* 顶部导航条 */}
                <NavBar
                    mode="dark"
                    icon={<Icon type="left" />}
                    leftContent={[
                        <span>自习室列表</span>
                    ]}
                    onLeftClick={() => this.props.history.goBack()}
                />
                <List>
                    {this.state.list.map((item, index) => {
                        return (
                            <List.Item arrow="horizontal"
                                  key={index}
                                  multipleLine
                                  onClick={() => {
                                      this.props.history.push( {
                                          pathname:'/roomDetail',
                                          query: {
                                              name: item.name
                                          }
                                      });
                                      this.props.saveRoomId({roomId: item.id})
                                  }}
                            >
                                {`${item.name}自习室`}
                                <List.Item.Brief>
                                    可选数：{item.availableSeatCount + ' '}
                                    座位数：{item.totalSeatCount}
                                </List.Item.Brief>
                            </List.Item>
                        )
                    })}
                </List>
            </div>

        )
    }
}

const mapStateToProps = state => {
    return {
        stadiumId: state.userModule.stadiumId,
        startTime: state.seatModule.startTime,
        endTime: state.seatModule.endTime
    }
}
// 创建映射函数
const mapDispatchToProps = (dispatch) => {
    return {
        saveRoomId:  (roomId) => {
            dispatch({ type: 'SAVE_ROOM_ID',payload: roomId })
        },
    }
}


export default  connect(mapStateToProps,mapDispatchToProps)(withRouter(RoomList))
