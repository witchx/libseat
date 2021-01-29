import axios from '@/libs/api.request'

const domain = '/api/menu';

export function getMenu(params) {
  return axios.request({
    url: domain + '/list',
    method: 'get',
    params: params
  })
}
export function updateMenu(id,data) {
  return axios.request({
    url: domain + '/update/' + id,
    method: 'post',
    data: data
  })
}
export function updateMenuBatch(id,data) {
  return axios.request({
    url: domain + '/updateBatch/'+id,
    headers: { 'content-type': 'application/json;charset=utf-8' },
    method: 'post',
    data: data
  })
}
export function createMenu(data) {
  return axios.request({
    url: domain + '/create',
    method: 'post',
    data: data
  })
}
export function deleteMenu (id) {
  return axios.request({
    url: domain + '/delete/' + id,
    method: 'get'
  })
}
