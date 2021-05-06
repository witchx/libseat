import React, { Component, Fragment } from 'react'
import { withRouter } from 'react-router-dom'
import {connect} from "react-redux";
import {Tabs, NavBar, WingBlank, ListView, WhiteSpace, ActionSheet, Button, Modal, Icon} from 'antd-mobile'
import '../style/rank.scss'
const tabs = [
    { title: '周排行', index: '1' },
    { title: '月排行', index: '2' },
    { title: '年排行', index: '3' },
];
export class MyRank extends Component {
    constructor(props) {
        super(props)
        const dataSource1 = new ListView.DataSource({
            rowHasChanged: (row1, row2) => row1 !== row2,
        });
        const dataSource2 = new ListView.DataSource({
            rowHasChanged: (row1, row2) => row1 !== row2,
        });
        const dataSource3 = new ListView.DataSource({
            rowHasChanged: (row1, row2) => row1 !== row2,
        });

        this.state = {
            userId: this.props.id,
            ranks: null,
            user: null,
            isLoading: false,
            dataSource1,
            dataSource2,
            dataSource3,
            height: document.documentElement.clientHeight * 0.85,
        }
    }

    componentWillMount() {
        setTimeout(() => {
            this.setState({
                ranks: JSON.parse(sessionStorage.getItem('ranks'))
            })}, 600);

        setTimeout(() => {
            //周排行
            const dataBlob1 = {};
            for (let i = 0; i < this.state.ranks[1].length; i++) {
                const ii = i;
                dataBlob1[`${ii}`] = `row - ${ii}`;
            }
            //月排行
            const dataBlob2 = {};
            for (let i = 0; i < this.state.ranks[2].length; i++) {
                const ii = i;
                dataBlob2[`${ii}`] = `row - ${ii}`;
            }
            //年排行
            const dataBlob3 = {};
            for (let i = 0; i < this.state.ranks[3].length; i++) {
                const ii = i;
                dataBlob3[`${ii}`] = `row - ${ii}`;
            }
            this.rData1 = dataBlob1;
            this.rData2 = dataBlob2;
            this.rData3 = dataBlob3;
            this.setState({
                dataSource1: this.state.dataSource1.cloneWithRows(this.rData1),
                dataSource2: this.state.dataSource2.cloneWithRows(this.rData2),
                dataSource3: this.state.dataSource3.cloneWithRows(this.rData3),
                user: this.state.ranks[0],
                isLoading: true,
            });

        }, 700);
    }

    getOne = (obj,type) => {
        switch (type) {
            case 1:
                return (
                <div className="rank_one">
                    <div className="rank_one_left">{obj.rank}</div>
                    <div className="rank_one_center">{obj.icon?<img src={obj.icon}></img>:''}</div>
                    <div className="rank_one_right">
                        <p>{obj.nickname}</p>
                        <div className="rank_one_right_bottom">
                            <span>学时：{obj.hoursByWeek}</span>
                            <span className="rank_one_right_bottom_right">天数：{obj.daysByWeek}</span>
                        </div>
                    </div>
                </div>
            )
            case 2:
                return (
                    <div className="rank_one">
                        <div className="rank_one_left">{obj.rank}</div>
                        <div className="rank_one_center">{obj.icon?<img src={obj.icon}></img>:''}</div>
                        <div className="rank_one_right">
                            <p>{obj.nickname}</p>
                            <div className="rank_one_right_bottom">
                                <span>学时：{obj.hoursByMonth}</span>
                                <span  className="rank_one_right_bottom_right">天数：{obj.daysByMonth}</span>
                            </div>
                        </div>
                    </div>
                )
            case 3:
                return (
                    <div className="rank_one">
                        <div className="rank_one_left">{obj.rank}</div>
                        <div className="rank_one_center">{obj.icon?<img src={obj.icon}></img>:''}</div>
                        <div className="rank_one_right">
                            <p>{obj.nickname}</p>
                            <div className="rank_one_right_bottom">
                                <span className="rank_one_right_bottom_left">学时：{obj.hoursByYear}</span>
                                <span className="rank_one_right_bottom_right">天数：{obj.daysByYear}</span>
                            </div>
                        </div>
                    </div>
                )
        }

    }

    render() {
        const separator = (sectionID, rowID) => (
            <div
                key={`${sectionID}-${rowID}`}
                style={{
                    backgroundColor: '#f2f2f2',
                    height: 10,
                    borderTop: '1px solid #ECECED',
                    borderBottom: '1px solid #ECECED',
                }}
            />
        );
        let index1 = 0;
        const row1 = (rowData, sectionID, rowID) => {
            if (index1 > this.state.ranks[1].length - 1) {
                index1 = 0;
            }
            const obj = this.state.ranks[1][index1++];
            return (
                <div>
                    {this.getOne(obj,1)}
                </div>
            )
        };
        let index2 = 0;
        const row2 = (rowData, sectionID, rowID) => {
            if (index2 > this.state.ranks[2].length - 1) {
                index2 = 0
            }
            const obj = this.state.ranks[2][index2++];
            return (
                <div>
                    {this.getOne(obj,2)}
                </div>
            )
        };
        let index3 = 0;
        const row3 = (rowData, sectionID, rowID) => {
            if (index3 > this.state.ranks[3].length - 1) {
                index3 = 0;
            }
            const obj = this.state.ranks[3][index3++];
            return (
                <div>
                    {this.getOne(obj,3)}
                </div>
            )
        };
        return (
            <div>
                {this.props.location.pathname === "/rank" ?
                    <Fragment>
                        <NavBar
                            icon={<Icon type="left" />}
                            mode="dark"
                            onLeftClick={() => {
                                this.props.history.goBack()
                            }}
                            leftContent={[
                                <span>学习报告</span>
                            ]}
                        >
                        </NavBar>
                        {this.state.isLoading?<div className="rank_box">
                            <div className="rank_box_left">{this.state.user.icon?<img src={this.state.user.icon}></img>:''}</div>
                            <div className="rank_box_right">
                                <p>{this.state.user.nickname}用户</p>
                                <div className="rank_box_right_bottom">
                                    <span>累积学时：{this.state.user.hoursByYear}</span>
                                    <span className="rank_box_right_bottom_right">累积天数：{this.state.user.daysByYear}</span>
                                </div>
                            </div>
                        </div>:''}
                        <Tabs tabs={tabs}
                              initialPage={0}
                              renderTab={tab => <span>{tab.title}</span>}
                              animated={false} useOnPan={false}
                        >
                            <WingBlank size="lg">
                                {this.state.isLoading?<ListView
                                    ref={el => this.lv = el}
                                    dataSource={this.state.dataSource1}
                                    renderRow={row1}
                                    renderSeparator={separator}
                                    pageSize={this.state.ranks[1].length-1}
                                    scrollRenderAheadDistance={500}
                                    style={{
                                        height: this.state.height
                                    }}
                                >
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <span className="card_bottom_alert">励步*提供技术支持</span>
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                </ListView>:''}

                            </WingBlank>

                            <WingBlank size="lg">
                                {this.state.isLoading?<ListView
                                    ref={el => this.lv = el}
                                    dataSource={this.state.dataSource2}
                                    renderRow={row2}
                                    renderSeparator={separator}
                                    pageSize={this.state.ranks[2].length-1}
                                    scrollRenderAheadDistance={500}
                                    style={{
                                        height: this.state.height
                                    }}
                                >
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <span className="card_bottom_alert">励步*提供技术支持</span>
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                </ListView>:''}

                            </WingBlank>
                            <WingBlank size="lg">
                                {this.state.isLoading?<ListView
                                    ref={el => this.lv = el}
                                    dataSource={this.state.dataSource3}
                                    renderRow={row3}
                                    renderSeparator={separator}
                                    pageSize={this.state.ranks[3].length-1}
                                    scrollRenderAheadDistance={500}
                                    style={{
                                        height: this.state.height
                                    }}
                                >
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <span className="card_bottom_alert">励步*提供技术支持</span>
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                    <WhiteSpace size="lg" />
                                </ListView>:''}

                            </WingBlank>
                        </Tabs>
                    </Fragment>
                    : ''}
            </div>
        )
    }
}

const mapStateToProps = state => {
    return {
        id: state.userModule.id
    }
}

export default connect(mapStateToProps) (withRouter(MyRank))
