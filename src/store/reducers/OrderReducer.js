let initState = {
    orderId: '',
}

export const OrderReducer = (state = initState, action) => {
    switch (action.type) {
        //保存订单号
        case 'SAVE_ORDER_ID':
            if (action.payload.orderId) {
                sessionStorage.setItem('orderId', action.payload.orderId)
            }
            return {...state,orderId: action.payload.orderId};
        //清空
        case 'CLEAR_ORDER':
            sessionStorage.removeItem('orderId');
            return {}
        default:
        // 返回默认数据
        return {...state, orderId: sessionStorage.getItem('orderId')?sessionStorage.getItem('orderId'):''}
    }

}
