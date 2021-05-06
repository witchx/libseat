import axios from 'axios'
import qs from 'querystring'
// 设置公共请求前缀
export const baseUrl = 'http://localhost:8888';
axios.defaults.baseURL = `${baseUrl}/api`;
// 设置请求头token
axios.defaults.headers.common['Authorization'] = sessionStorage.getItem('token')

// 登录账号
export const submitLogin = (userInfoObj) => axios.post('/login', userInfoObj)
// 注册账号
export const submitRegister = (data) => axios.post('/register',data)
//得到所有公司
export const getAllCompany = () => axios.get('/allCompany')
// 获取基本信息
export const getInfo = value => axios.get('/info?token='+value)
// 获取用户信息
export const getUserInfo = value => axios.get('/my/info?id='+value)
// 获取用户信息
export const getUserRank = value => axios.get('/my/rank?id='+value)
// 获取用户详细信息
export const getUserDetail = value => axios.get('/my/detail?id='+value)
// 修改用户信息
export const updateUser = (id,data) => axios.post('/my/update_'+id,data)
// 核对用户密码
export const checkPassword = data => axios.post('/my/password',data)
// 获取首页商家信息数据
export const getCompanyInfo = (id) => axios.get('/home/company?id='+id)
// 获取自习室列表数据
export const getRoomList = (id,startTime,endTime) => axios.get('/room/all?stadiumId='+id+'&startTime='+startTime+'&endTime='+endTime)
// 获取自习室名称数据
export const getRoomName = (id) => axios.get('/room/name?id='+id)
// 获取自习室信息数据
export const getSeat = (id,startTime,endTime) => axios.get('/seat/all?roomId='+id+'&startTime='+startTime+'&endTime='+endTime)
// 创建订单
export const createOrder = (data) => axios.post('/order/create',data)
// 获取订单
export const getOrder = (id) => axios.get('/order/info?id='+id)
// 获取订单
export const getOrderDetail = (id,type) => axios.get('/order/detail?id='+id+'&type='+type)
// 创建支付订单
export const createPay = (data) => axios.post('/pay/create',data)
// 获取订单
export const getPay = (id) => axios.get('/pay/info?id='+id)
// 获取会员卡
export const getVipCard = (id) => axios.get('/card/all?id='+id)
// 获取会员卡
export const getMyVipCard = (id) => axios.get('/card/my?id='+id)
// 获取全部的座位订单
export const getSeatOrderList = (id) => axios.get('/order/seat/all?id='+id)
// 获取全部的订单
export const getOrderList = (id) => axios.get('/order/all?id='+id)
// 更新订单状态
export const updateOrder = (id,data) => axios.post('/order/update_'+id,data)
