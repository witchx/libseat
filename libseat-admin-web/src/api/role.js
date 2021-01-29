import axios from '@/libs/api.request'

const domain = '/api/role';

export function getRole(params) {
  return axios.request({
    url: domain + '/list',
    method: 'get',
    params: params
  })
}
export function updateRole(id,data) {
  return axios.request({
    url: domain + '/update/' + id,
    method: 'post',
    data: data
  })
}
export function createRole(data) {
  return axios.request({
    url: domain + '/create',
    method: 'post',
    data: data
  })
}
export function deleteRole (id,data) {
  return axios.request({
    url: domain + '/delete/' + id,
    method: 'get',
    data: data
  })
}
