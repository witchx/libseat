import axios from '@/libs/api.request'

const domain = '/api/customer';

export function getCustomer(params) {
  return axios.request({
    url: domain + '/list',
    method: 'get',
    params: params
  })
}
export function getCustomerDetail(id) {
  return axios.request({
    url: domain + '/detail/' + id,
    method: 'get'
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
    method: 'post',
    data: data
  })
}
export function deleteCustomerBatch (data) {
  return axios.request({
    url: domain + '/delete/batch',
    headers: { 'content-type': 'application/json;charset=utf-8' },
    method: 'delete',
    data: data
  })
}
export function deleteCustomer (id,data) {
  return axios.request({
    url: domain + '/delete/' + id,
    method: 'delete',
    data: data
  })
}
