import Cookies from 'js-cookie'
// 初始token为空
let initState = {
    loginState: false,
    nickname: '',
    tel: '',
    avatar: '',
    id: '',
    companyName:''
}
export const UserReducer = (state = initState, action) => {
    switch (action.type) {
        case 'CHANGE_LOGIN_STATE':
            if (action.payload.Login) {
                sessionStorage.setItem('token', action.payload.token)
            }
            return {...state, loginState: action.payload.Login}
        case 'SAVE_COMPANY_INFO':
            if (action.payload.companyName) {
                sessionStorage.setItem('companyName', action.payload.companyName)
                sessionStorage.setItem('companyId', action.payload.companyId)
            }
            return {...state,companyName: action.payload.companyName,companyId: action.payload.companyId}
        // 保存场馆号
        case 'SAVE_STADIUM_ID':
            if (action.payload.stadiumId) {
                sessionStorage.setItem('stadiumId', action.payload.stadiumId)
            }
            return {...state,stadiumId: action.payload.stadiumId};
        case 'SAVE_USER_ID':
            if (action.payload.id) {
                sessionStorage.setItem('id', action.payload.id)
            }
            return {...state,id: action.payload.id};
        case 'LOGINOUT':
            Cookies.remove("token")
            sessionStorage.removeItem('token');
            sessionStorage.removeItem('companyName');
            sessionStorage.removeItem('companyId');
            sessionStorage.removeItem('stadiumId');
            sessionStorage.removeItem('id');
            return {...state, loginState: false};
        default:
            return {...state,
                id: sessionStorage.getItem('id')? sessionStorage.getItem('id'): '',
                loginState: !!sessionStorage.getItem('token'),
                companyId:sessionStorage.getItem('companyId')?sessionStorage.getItem('companyId'):'',
                companyName:sessionStorage.getItem('companyName')?sessionStorage.getItem('companyName'):'',
                stadiumId: sessionStorage.getItem('stadiumId')?sessionStorage.getItem('stadiumId'):'',
            }
    }
};
