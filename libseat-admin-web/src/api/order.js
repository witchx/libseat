import axios from '@/libs/api.request'

const domain  = '/api/order';

export function getOrder(params) {
  return axios.request({
    url:domain,
    method:'get',
    params:params
  })
}
export function getOrderDetail(params) {
  return axios.request({
    url:domain,
    method:'get',
    params:params
  })
}
export function updateOrder(data) {
  return axios.request({
    url:domain,
    method:'put',
    data:data
  })
}
export function deleteOrder (data) {
  return axios.request({
    url: domain + '/delete',
    method: 'delete',
    data: data
  })
}
