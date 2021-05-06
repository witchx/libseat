let initState = {
    cardId: '',
}

export const CardReducer = (state = initState, action) => {
    switch (action.type) {
        case 'SAVE_CARD_ID':
            if (action.payload.cardId) {
                sessionStorage.setItem('cardId', action.payload.cardId)
            }
            return {...state,cardId: action.payload.cardId};
        //清空
        case 'CLEAR_CARD':
            //支付完成
            sessionStorage.removeItem('cardId');
            sessionStorage.removeItem('cards');
            return {}
        default:
        // 返回默认数据
        return {...state,cardId: sessionStorage.getItem('cardId')?sessionStorage.getItem('cardId'):''}
    }

}
