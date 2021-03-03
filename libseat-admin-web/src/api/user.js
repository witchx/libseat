import axios from '@/libs/api.request'

const domain = '/api/user';

export function getUser (params) {
  return axios.request({
    url: domain + '/list',
    method: 'get',
    params: params
  })
}
export function getUserDetail (id) {
  return axios.request({
    url: domain + '/detail/'+id,
    method: 'get'
  })
}
export function updateUser (id,data) {
  return axios.request({
    url: domain + '/update/' + id,
    method: 'put',
    data: data
  })
}
export function updateUserPassword (id,data) {
  return axios.request({
    url: domain + '/password/' + id,
    headers: { 'content-type': 'application/json;charset=utf-8' },
    method: 'put',
    data: data
  })
}
export function createUser (data) {
  return axios.request({
    url: domain + '/create',
    method: 'post',
    data: data
  })
}
export function deleteUser (id,data) {
  return axios.request({
    url: domain + '/delete/' + id,
    method: 'delete',
    data: data
  })
}
