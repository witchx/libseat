import React, { useState, useRef, useLayoutEffect, useEffect } from 'react';
import { Toast } from 'antd-mobile';
import { timestampTohm } from '../utils/time'
import Panzoom from '@panzoom/panzoom'
import '../style/seat.scss'

const Seat = (props) => {
    const seat = useRef(null)
    const [width, setWidth] = useState(0)
    const [height, setHeight] = useState(700)

    useEffect(() => {
        if (props.seat.length) {
            setWidth(props.seat[0].row.length * 35 + 5)
        }
    }, [props.seat.length])


    useLayoutEffect(() => {
        const elem = document.getElementById('seat')
        const panzoom = Panzoom(elem, {
            maxScale: 1.2
        })
        panzoom.pan(10, 10)
        panzoom.zoom(0.8, { animate: true })
    }, [width, height])

    const handleChooseSeat = (row) => {
        if (row.value === 2) {
            console.log(row)
            Toast.info(`预约时间: ${timestampTohm(row.startTime)} - ${timestampTohm(row.endTime)}`,1)
        } else {
            const value = row.value === 1 ? 3 : 1
            const seatId = row.seatId
            props.onChoose({value, seatId})
        }
    }

    return (
        <div className="seat" id="seat"
        >
            <div
                ref={seat}
                style={{height: `${height}px`, width: `${width}px`}}
            >
                <div>
                    {props.seat.map((line, index) => {
                        return (
                            <div className="line"
                                 key={index}
                            >
                                {line.row.map((row, _index) => {
                                    return (
                                        <span className="row"
                                              key={_index}
                                        >
                                            {row.value ? <img onClickCapture={() => {handleChooseSeat(row)}}
                                                              src={row.value === 1 ? require('../assets/imgs/empty_seat.png') : (row.value === 2 ? require('../assets/imgs/disabled_seat.png') : require('../assets/imgs/chose_seat.png'))}
                                            /> : ''}
                                        </span>
                                    )
                                })}
                            </div>
                        )
                    })}
                </div>
            </div>
        </div>
    )
}

export default Seat
