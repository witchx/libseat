import axios from '@/libs/api.request'

const domain = '/api/vip';

export function getVipCard(params) {
  return axios.request({
    url: domain + '/list',
    method: 'get',
    params: params
  })
}
export function updateVipCard(id,data) {
  return axios.request({
    url: domain + '/update/' + id,
    method: 'put',
    data: data
  })
}
export function createVipCard(data) {
  return axios.request({
    url: domain + '/create',
    method: 'post',
    data: data
  })
}
export function deleteVipCardBatch (data) {
  return axios.request({
    url: domain + '/deleteBatch',
    headers: { 'content-type': 'application/json;charset=utf-8' },
    method: 'delete',
    data: data
  })
}
export function deleteVipCard (id) {
  return axios.request({
    url: domain + '/delete/' + id,
    method: 'delete'
  })
}

