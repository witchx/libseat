import axios from '@/libs/api.request'

const domain = '/api/product';

export function getProduct(params) {
  return axios.request({
    url: domain + '/list',
    method: 'get',
    params: params
  })
}
export function updateProduct(id,data) {
  return axios.request({
    url: domain + '/update/' + id,
    method: 'put',
    data: data
  })
}
export function createProduct(data) {
  return axios.request({
    url: domain + '/create',
    method: 'post',
    data: data
  })
}
export function createProductBatch(data) {
  return axios.request({
    url: domain + '/createBatch',
    headers: { 'content-type': 'application/json;charset=utf-8' },
    method: 'post',
    data: data
  })
}
export function deleteProduct (data) {
  return axios.request({
    url: domain + '/delete',
    headers: { 'content-type': 'application/json;charset=utf-8' },
    method: 'delete',
    data: data
  })
}

