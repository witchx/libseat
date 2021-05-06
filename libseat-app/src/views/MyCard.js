import React, { Component, Fragment } from 'react'
import { withRouter } from 'react-router-dom'
import {connect} from "react-redux";
import {Tabs, NavBar, WingBlank, ListView, WhiteSpace, ActionSheet, Button, Modal, Icon} from 'antd-mobile'
import {getMyVipCard} from '../api/index'
import '../style/card.scss'
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import { library } from '@fortawesome/fontawesome-svg-core'
import { faChevronDown } from '@fortawesome/free-solid-svg-icons'
library.add(faChevronDown)
const tabs = [
    { title: '储值卡', index: '1' },
    { title: '计次卡', index: '2' },
    { title: '期限卡', index: '3' },
];
export class MyCard extends Component {
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
            cards: null,
            isLoading: false,
            dataSource1,
            dataSource2,
            dataSource3,
            height: document.documentElement.clientHeight * 0.85,
        }
    }

    componentWillMount() {
        getMyVipCard(this.state.userId).then(res => {
            const {code, msg, data} = res.data
            if (code === 200) {
                setTimeout(() => {
                    this.setState({
                        cards: data,
                        isLoading: true,
                    })}, 600);
            }
        })
        setTimeout(() => {
            //储值卡
            const dataBlob1 = {};
            for (let i = 0; i < this.state.cards[1].length; i++) {
                const ii = i;
                dataBlob1[`${ii}`] = `row - ${ii}`;
            }
            //计次卡
            const dataBlob2 = {};
            for (let i = 0; i < this.state.cards[2].length; i++) {
                const ii = i;
                dataBlob2[`${ii}`] = `row - ${ii}`;
            }
            //期限卡
            const dataBlob3 = {};
            for (let i = 0; i < this.state.cards[3].length; i++) {
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
            });

        }, 1000);
    }

    getCard = (obj) => {
        switch (obj.type) {
            case 1:
                return (
                    <div className="card_box">
                        <div className="value_card">
                            <div className="card_top_top"><span className="card_top_top_left">{obj.money}</span><span className="card_top_top_right">元</span></div>
                            <div className="card_top_mid">{obj.name}</div>
                            <div className="card_top_bottom">使用规则<FontAwesomeIcon icon="chevron-down" style={{fontSize:'16px',marginLeft: '5px'}}/></div>
                        </div>
                        <div className="card_bottom">
                            <div className="card_bottom_left">{obj.usefulLife?("有效期："+obj.usefulLife+"天"):("不限有效期")}</div>
                            <div className="card_bottom_right">
                                <div className="card_bottom_right_left">{obj.originalPrice}</div>
                                <div className="card_bottom_right_mid">{obj.price}</div>
                                <div className="card_bottom_right_right">&yen;</div>
                            </div>
                        </div>
                    </div>
                );
            case 2:
                return (
                    <div className="card_box">
                        <div className="would_card">
                            <div className="card_top_top"><span className="card_top_top_left">{obj.times}</span><span className="card_top_top_right">次</span></div>
                            <div className="card_top_mid">{obj.name}</div>
                            <div className="card_top_bottom">使用规则<FontAwesomeIcon icon="chevron-down" style={{fontSize:'16px',marginLeft: '5px'}}/></div>
                        </div>
                        <div className="card_bottom">
                            <div className="card_bottom_left">{obj.usefulLife?("有效期："+obj.usefulLife+"天"):("不限有效期")}</div>
                            <div className="card_bottom_right">
                                <div className="card_bottom_right_left">{obj.originalPrice}</div>
                                <div className="card_bottom_right_mid">{obj.price}</div>
                                <div className="card_bottom_right_right">&yen;</div>
                            </div>
                        </div>
                    </div>
                );
            case 3:
                return (
                    <div className="card_box">
                        <div className="time_card">
                            <div className="card_top_top"><span className="card_top_top_left">{obj.usefulLife}</span><span className="card_top_top_right">天</span></div>
                            <div className="card_top_mid">{obj.name}</div>
                            <div className="card_top_bottom">使用规则<FontAwesomeIcon icon="chevron-down" style={{fontSize:'16px',marginLeft: '5px'}}/></div>
                        </div>
                        <div className="card_bottom">
                            <div className="card_bottom_left">{obj.usefulLife?("有效期："+obj.usefulLife+"天"):("不限有效期")}</div>
                            <div className="card_bottom_right">
                                <div className="card_bottom_right_left">{obj.originalPrice}</div>
                                <div className="card_bottom_right_mid">{obj.price}</div>
                                <div className="card_bottom_right_right">&yen;</div>
                            </div>
                        </div>
                    </div>
                );
        }

    }
    showActionSheet = (obj) => {
        const message = (
            <div>
                {this.getCard(obj)}
                <div dangerouslySetInnerHTML={{ __html: obj.des}}></div>
            </div>
        )
        ActionSheet.showActionSheetWithOptions({
                options: ['确定'],
                message: message,
                maskClosable: true
            },
            (buttonIndex) => {

            });
    }

    render() {
        const separator = (sectionID, rowID) => (
            <div
                key={`${sectionID}-${rowID}`}
                style={{
                    backgroundColor: '#f2f2f2',
                    height: 20,
                    borderTop: '1px solid #ECECED',
                    borderBottom: '1px solid #ECECED',
                }}
            />
        );
        let index1 = 0;
        const row1 = (rowData, sectionID, rowID) => {
            if (index1 > this.state.cards[1].length - 1) {
                index1 = 0;
            }
            const obj = this.state.cards[1][index1++];
            return (
                <div onClick={() => {
                    this.showActionSheet(obj)
                }}>
                    {this.getCard(obj)}
                </div>
            )
        };
        let index2 = 0;
        const row2 = (rowData, sectionID, rowID) => {
            if (index2 > this.state.cards[2].length - 1) {
                index2 = 0
            }
            const obj = this.state.cards[2][index2++];
            return (
                <div onClick={() => {
                    this.showActionSheet(obj)
                }}>
                    {this.getCard(obj)}
                </div>
            )
        };
        let index3 = 0;
        const row3 = (rowData, sectionID, rowID) => {
            if (index3 > this.state.cards[3].length - 1) {
                index3 = 0;
            }
            const obj = this.state.cards[3][index3++];
            return (
                <div onClick={() => {
                    this.showActionSheet(obj)
                }}>
                    {this.getCard(obj)}
                </div>
            )
        };
        return (
            <div>
                {this.props.location.pathname === "/myCard" ?
                    <Fragment>
                        <NavBar
                            icon={<Icon type="left" />}
                            mode="dark"
                            onLeftClick={() => {
                                this.props.history.goBack()
                            }}
                            leftContent={[
                                <span>我的卡包</span>
                            ]}
                        >
                        </NavBar>

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
                                    pageSize={this.state.cards[1].length-1}
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
                                    pageSize={this.state.cards[2].length-1}
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
                                    pageSize={this.state.cards[3].length-1}
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

export default connect(mapStateToProps) (withRouter(MyCard))
