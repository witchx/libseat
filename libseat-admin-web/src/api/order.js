import axios from '@/libs/api.request'

const domain  = '/api/order';

export function getOrder(params) {
  return axios.request({
    url:domain + '/list',
    method:'get',
    params:params
  })
}
export function getOrderDetail(params) {
  return axios.request({
    url:domain + '/detail',
    method:'get',
    params:params
  })
}
export function getOrderRecord(params) {
  return axios.request({
    url:domain + '/record',
    method:'get',
    params:params
  })
}
export function getOrderProduct(params) {
  return axios.request({
    url:domain + '/product',
    method:'get',
    params:params
  })
}
export function updateOrder(id,data) {
  return axios.request({
    url:domain + '/update/' + id,
    method:'post',
    data:data
  })
}

export function createOrderRecord(data) {
  return axios.request({
    url: domain + '/createRecord',
    method: 'post',
    data: data
  })
}
export function deleteOrder (data) {
  return axios.request({
    url: domain + '/delete',
    method: 'delete',
    data: data
  })
}
export function getOrderSetting(params) {
  return axios.request({
    url:domain + '/setting/list',
    method:'get',
    params:params
  })
}
export function updateOrderSetting(data) {
  return axios.request({
    url:domain + '/setting/update',
    headers: { 'content-type': 'application/json;charset=utf-8' },
    method:'put',
    data:data
  })
}

export function onOffOrderSetting(id,data) {
  return axios.request({
    url:domain + '/setting/onOff/' + id,
    method:'put',
    data:data
  })
}

export function createOrderSetting(data) {
  return axios.request({
    url: domain + '/setting/create',
    method: 'post',
    data: data
  })
}
