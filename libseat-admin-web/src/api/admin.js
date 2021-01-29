import axios from '@/libs/api.request'

const domain = '/api/admin';

export function getAdmin(params) {
  return axios.request({
    url: domain + '/list',
    method: 'get',
    params: params
  })
}
export function updateAdmin(id,data) {
  return axios.request({
    url: domain + '/update/' + id,
    method: 'post',
    data: data
  })
}
export function createAdmin(data) {
  return axios.request({
    url: domain + '/create',
    method: 'post',
    data: data
  })
}
export function deleteAdmin (id) {
  return axios.request({
    url: domain + '/delete/' + id,
    method: 'get'
  })
}
