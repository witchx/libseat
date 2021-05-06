import { timestampToTime } from '../../utils/time'
let initState = {
    totalNum: 0,
    roomId: '',
    stadiumId: '',
    startTime: '',
    endTime: '',
    startStr: '',
    hours: '',
    minutes: ''
}

export const SeatReducer = (state = initState, action) => {
    switch (action.type) {
        //保存时间
        case 'SAVE_TIME':
            if (action.payload.startTime&&action.payload.endTime) {
                sessionStorage.setItem('startTime', action.payload.startTime);
                sessionStorage.setItem('endTime', action.payload.endTime)
            }
            return {...state,startTime: action.payload.startTime,endTime: action.payload.endTime};
        //保存自习室号
        case 'SAVE_ROOM_ID':
            if (action.payload.roomId) {
                sessionStorage.setItem('roomId', action.payload.roomId)
            }
            return {...state,roomId: action.payload.roomId};
        //保存座位号
        case 'SAVE_SEAT_ID':
            if (action.payload.seatId) {
                sessionStorage.setItem('seatId', action.payload.seatId)
            }
            return {...state,seatId: action.payload.seatId};
        //清空
        case 'CLEAR_SEAT':
            //支付完成
            sessionStorage.removeItem('startTime');
            sessionStorage.removeItem('endTime');
            sessionStorage.removeItem('roomId');
            sessionStorage.removeItem('seatId');
            return {}
        default:
            let startTime = sessionStorage.getItem('startTime');
            let endTime = sessionStorage.getItem('endTime');
            let hours = 0;
            let minutes = 0;
            if (startTime&&endTime){
                let start = new Date(parseInt(startTime));
                let end = new Date(parseInt(endTime));
                hours = end.getHours()-start.getHours();
                minutes = end.getMinutes()-start.getMinutes();
            }
        // 返回默认数据
        return {...state,
            seatId: sessionStorage.getItem('seatId')?sessionStorage.getItem('seatId'):'',
            roomId: sessionStorage.getItem('roomId')?sessionStorage.getItem('roomId'):'',
            startTime: startTime?startTime:'',
            endTime: endTime?endTime:'',
            hours: hours,
            minutes: minutes
        }
    }

}
