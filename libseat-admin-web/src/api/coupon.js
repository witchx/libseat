import axios from '@/libs/api.request'

const domain = '/api/coupon';

export function getCoupon(params) {
  return axios.request({
    url: domain + '/list',
    method: 'get',
    params: params
  })
}
export function updateCoupon(id,data) {
  return axios.request({
    url: domain + '/update/' + id,
    method: 'put',
    data: data
  })
}
export function createCoupon(data) {
  return axios.request({
    url: domain + '/create',
    method: 'post',
    data: data
  })
}
export function deleteCouponBatch (data) {
  return axios.request({
    url: domain + '/deleteBatch',
    headers: { 'content-type': 'application/json;charset=utf-8' },
    method: 'delete',
    data: data
  })
}
export function deleteCoupon (id) {
  return axios.request({
    url: domain + '/delete/' + id,
    method: 'delete'
  })
}

