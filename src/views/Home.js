import React, { Component } from 'react'
import { withRouter } from 'react-router-dom'
import {Card, DatePicker, List, WingBlank, WhiteSpace, NavBar, Button, Modal,ListView} from 'antd-mobile';
import {getCompanyInfo} from '../api/index'
import '../style/home.scss'
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import { library } from '@fortawesome/fontawesome-svg-core'
import { faMapMarkerAlt,faShareSquare } from '@fortawesome/free-solid-svg-icons'
import {connect} from "react-redux";
library.add(faMapMarkerAlt,faShareSquare)

let day = 24 * 60 * 60 * 1000;
let days = parseInt(Date.now()/day)
let isChangeDay = Date.now() >= day*days + (14 * 60 * 60 * 1000+30 * 60 * 1000)

export class Home extends Component {
    constructor(props) {
        super(props);
        const dataSource = new ListView.DataSource({
            rowHasChanged: (row1, row2) => row1 !== row2,
        });
        this.state = {
            dataSource,
            isLoadingTop: false,
            isLoadingBottom: false,
            index: '',
            companyName: '',
            stadiumName: '',
            detailAddress: '',
            companyId: this.props.companyId?this.props.companyId:1,
            stadiumId: '',
            icon: '',
            size: 0,
            startTime: isChangeDay?((days+1)*day+30*60*1000):(Date.now()+60*60*1000),
            endTime:0
        }
    }

    UNSAFE_componentWillMount() {
        getCompanyInfo(this.state.companyId).then(res => {
            // 解构赋值
            const {code, data, msg} = res.data
            console.log(data)
            if (code === 200) {
                if (this.props.stadiumId) {
                    for(let i=0; i<  data.length; i++) {
                        if (data[i].stadiumId === parseInt(this.props.stadiumId)){
                            this.state.index = i
                            break;
                        }
                    }
                }else {
                    this.state.index = 0
                    this.props.saveCompanyInfo({companyId: data[this.state.index].userId,companyName:data[this.state.index].companyName})
                }
                this.fetchData(data)
            }
        })
    }

    fetchData = (data) => {
        setTimeout(() => {
            this.setState({
                companyId: this.props.companyId,
                companyName: this.props.companyName,
                stadiumName: data[this.state.index].stadiumName,
                detailAddress: data[this.state.index].detailAddress,
                stadiumId: data[this.state.index].stadiumId,
                icon: data[this.state.index].icon,
                size: data.length,
                data: data,
                isLoadingTop:true
            });
        }, 600);
        setTimeout(() => {
            const dataBlob = {};
            for (let i = 0; i < this.state.size; i++) {
                if (i !== this.state.index) {
                    const ii = i;
                    dataBlob[`${ii}`] = `row - ${ii}`;
                }
            }
            this.rData = dataBlob;
            this.setState({
                dataSource: this.state.dataSource.cloneWithRows(this.rData),
                isLoadingBottom: true,
            });
        },600);

    }

    next = () => {
        if (this.state.endTime===0||this.state.endTime==='') {
            Modal.alert('提示', '请选择结束时间!', [
                { text: '取消' },
                { text: '确认'}
            ])
        }else {
            this.props.saveStadiumId({stadiumId:this.state.stadiumId})
            this.props.saveTime({startTime: this.state.startTime,endTime: this.state.endTime})
            this.props.history.push('/roomList')
        }
    }

    render() {
        const separator = (sectionID, rowID) => (
            <div
                key={`${sectionID}-${rowID}`}
                style={{
                    backgroundColor: '#F5F5F9',
                    height: 1,
                    borderTop: '1px solid #ECECED',
                    borderBottom: '1px solid #ECECED',
                }}
            />
        );
        let index = 0;
        const row = (rowData, sectionID, rowID) => {
            if (index >= this.state.size) {
                index = 0;
            }
            if (this.state.index === index) {
                index++;
            }
            console.log(index)
            const obj = this.state.data[index++];
            return (
                <div key={rowID}
                     style={{ padding: '21px 15px',height: '105px' }}
                     onClick={() => {
                         for(let i=0; i< this.state.size; i++) {
                             if (this.state.data[i].stadiumId === obj.stadiumId){
                                 setTimeout(() =>  {
                                     this.setState({index : i})
                                 },600)
                                 this.props.saveStadiumId({stadiumId: this.state.data[i].stadiumId})
                                 break;
                             }
                         }
                         setTimeout(() =>  {
                             this.fetchData(this.state.data);
                         },600)

                     }}
                >
                    <div>
                        <img style={{ height: '64px', marginRight: '15px',float: 'left' }} src={obj.icon} alt="" />
                    </div>
                    <div>
                        <div style={{
                            lineHeight: '25px',
                            fontWeight: 'bolder'
                        }}>
                            {obj.companyName}({obj.stadiumName})
                        </div>
                        <div style={{
                            color: '#888'
                        }}>
                            {obj.detailAddress}
                        </div>
                    </div>
                </div>
            );
        };
        return (
            <div>
                <NavBar
                    leftContent={this.props.companyName}
                    mode="dark"
                >
                </NavBar>
                <WingBlank>
                    {this.state.isLoadingTop?<div>
                        <div className="top">
                            <strong>{this.state.companyName}({this.state.stadiumName})</strong>
                            <div className="share">
                                <FontAwesomeIcon icon="share-square" style={{fontSize:'18px',float: 'left',marginLeft: '10px',marginTop: '4px'}}/>
                                <div className="font">分享</div>
                            </div>
                        </div>
                        <WhiteSpace size="sm" />
                        <img src={this.state.icon} alt="" className="icon"/>
                        <WhiteSpace size="md" />
                        <Card style={{minHeight: '25px'}}>
                            <Card.Body>
                                <FontAwesomeIcon icon="map-marker-alt" style={{fontSize:'18px',color:'black',float: 'left'}}/>
                                <p style={{marginLeft: '15px'}}>{this.state.detailAddress}</p>
                            </Card.Body>
                        </Card>
                    </div>:''}
                    <WhiteSpace size="lg" />
                    <Card>
                        <Card.Header style={{background: 'black'}}
                            title={<span style={{background: 'black',color: 'white'}}>在线预约</span>}
                            extra={<span style={{background: 'black',color: 'white'}}>08:30 - 23:00</span>}
                        />
                        <Card.Body>
                            <List className="time_picker">
                                <DatePicker
                                    format="yyyy-MM-dd HH:mm"
                                    minDate={new Date(Date.now()+60*60*1000)}
                                    maxDate={new Date((days+2)*day+(14 * 60 * 60 * 1000))}
                                    onOk={time => {
                                        time = time.getTime();
                                        let days = parseInt(time/day);
                                        let min = days * day + 30 * 60 * 1000;
                                        let max = days * day + 14 * 60 * 60 * 1000;
                                        if (time < min) {
                                            time = min;
                                            if (time < Date.now()){
                                                time = Date.now()
                                            }
                                        }
                                        if (time > max) {
                                            time = max
                                        }
                                        this.setState({
                                            endTime: 0,
                                            startTime: time
                                        });
                                    }}
                                    value={new Date(this.state.startTime)}
                                >
                                    <List.Item arrow="horizontal">开始时间</List.Item>
                                </DatePicker>
                                <DatePicker
                                    format="yyyy-MM-dd HH:mm"
                                    minDate={new Date(this.state.startTime+30*60*1000)}
                                    maxDate={new Date(parseInt(this.state.startTime/day)*day+(14 * 60 * 60 * 1000 + 30 * 60 * 1000))}
                                    onOk={time => {
                                        time = time.getTime();
                                        let days = parseInt(time/day);
                                        let min = days * day + 60 * 60 * 1000;
                                        let max = days * day + 14 * 60 * 60 * 1000 + 30 * 60 * 1000;
                                        if (time < min) {
                                            time = min;
                                            if (time < Date.now()){
                                                time = Date.now()
                                            }
                                        }
                                        if (time > max) {
                                            time = max
                                        }
                                        this.setState({
                                            endTime: time
                                        });
                                    }}
                                    value={this.state.endTime === 0 ? '':new Date(this.state.endTime)}
                                >
                                    <List.Item arrow="horizontal">结束时间</List.Item>
                                </DatePicker>
                            </List>
                            <Button type="primary" style={{position: 'unset'}} onClick={this.next}>下一步</Button>
                        </Card.Body>
                    </Card>
                    <WhiteSpace size="md" />
                    {this.state.isLoadingBottom?<Card>
                        <Card.Header
                            title="智慧门店"
                            extra="查看更多"
                        />
                        <Card.Body>
                            <ListView
                                horizontal="true"
                                ref={el => this.lv = el}
                                dataSource={this.state.dataSource}
                                renderRow={row}
                                renderSeparator={separator}
                                className="am-list"
                                pageSize={this.state.size-1}
                                scrollRenderAheadDistance={500}
                                style={{
                                    height: '105px'
                                }}
                            />
                        </Card.Body>
                    </Card>:''}

                    <span className="bottom">励步*提供技术支持</span>
                </WingBlank>
            </div>
        )
    }
}
const mapStateToProps = state => {
    return {
        companyName: state.userModule.companyName,
        companyId: state.userModule.companyId,
        stadiumId: state.userModule.stadiumId
    }
}
// 创建映射函数
const mapDispatchToProps = (dispatch) => {
    return {
        saveTime: (time) => {
            dispatch({ type: 'SAVE_TIME',payload: time })
        },
        saveStadiumId:  (stadiumId) => {
            dispatch({ type: 'SAVE_STADIUM_ID',payload: stadiumId })
        },
        saveCompanyInfo: company =>{
            dispatch({type: 'SAVE_COMPANY_INFO', payload: company})
        },
    }
}
export default connect(mapStateToProps,mapDispatchToProps)(withRouter(Home))
