import axios from '@/libs/api.request'

const domain = '/api/customer';

export function getCustomer(params) {
  return axios.request({
    url: domain + '/list',
    method: 'get',
    params: params
  })
}
export function updateCustomer(id,data) {
  return axios.request({
    url: domain + '/update/' + id,
    method: 'put',
    data: data
  })
}
export function createCustomer(data) {
  return axios.request({
    url: domain + '/create',
    headers: { 'content-type': 'application/json;charset=utf-8' },
    method: 'post',
    data: data
  })
}
export function deleteCustomer (data) {
  return axios.request({
    url: domain + '/delete',
    headers: { 'content-type': 'application/json;charset=utf-8' },
    method: 'delete',
    data: data
  })
}
